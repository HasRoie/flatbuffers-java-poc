package org.enner.flatbuffers;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Table {

    /**
     * Bool / Byte / Enum
     */
    public static byte getByteValue(ByteBuffer bb, int tableAddress, int entryOffset, byte defaultValue) {
        int address = Table.getEntryAddress(bb, tableAddress, entryOffset);
        return address == NULL ? defaultValue : bb.get(address);
    }

    /**
     * Unsigned Byte
     */
    public static short getUnsignedByteValue(ByteBuffer bb, int tableAddress, int entryOffset, short defaultValue) {
        int address = Table.getEntryAddress(bb, tableAddress, entryOffset);
        return address == NULL ? defaultValue : unsigned(bb.get(address));
    }

    /**
     * Short
     */
    public static short getShortValue(ByteBuffer bb, int tableAddress, int entryOffset, short defaultValue) {
        int address = Table.getEntryAddress(bb, tableAddress, entryOffset);
        return address == NULL ? defaultValue : bb.getShort(address);
    }

    /**
     * Unsigned Short
     */
    public static int getUnsignedShortValue(ByteBuffer bb, int tableAddress, int entryOffset, int defaultValue) {
        int address = Table.getEntryAddress(bb, tableAddress, entryOffset);
        return address == NULL ? defaultValue : unsigned(bb.getShort(address));
    }

    /**
     * Integer
     */
    public static int getIntegerValue(ByteBuffer bb, int tableAddress, int entryOffset, int defaultValue) {
        int address = Table.getEntryAddress(bb, tableAddress, entryOffset);
        return address == NULL ? defaultValue : bb.getInt(address);
    }

    /**
     * Unsigned Integer
     */
    public static long getUnsignedIntegerValue(ByteBuffer bb, int tableAddress, int entryOffset, long defaultValue) {
        int address = Table.getEntryAddress(bb, tableAddress, entryOffset);
        return address == NULL ? defaultValue : unsigned(bb.getInt(address));
    }

    /**
     * Tables / Strings / Unions / Vectors
     */
    public static int getAddressValue(ByteBuffer bb, int tableAddress, int entryOffset) {
        // Pointers are stored as ints relative to itself
        int address = Table.getEntryAddress(bb, tableAddress, entryOffset);
        return address == NULL ? NULL : bb.getInt(address) + address;
    }

    public static int getVectorLength(ByteBuffer bb, int tableAddress, int entryOffset) {
        // The length is the first int of a vector
        int address = getAddressValue(bb, tableAddress, entryOffset);
        return address == NULL ? 0 : bb.getInt(address);
    }

    public static int getVectorElementAddress(ByteBuffer bb, int tableAddress, int entryOffset, int index, int elementSize) {
        // Check if there is an entry
        int address = getAddressValue(bb, tableAddress, entryOffset);
        if (address == NULL) return NULL;

        // Make sure index is not out of bounds
        int numElements = bb.getInt(address);
        if (index >= numElements)
            throw new IllegalArgumentException("Vector index is out of bounds.");

        // Data starts directly after the size header, in contiguous memory
        return address + SIZEOF_INT + index * elementSize;
    }

    /**
     * Returns the absolute address of the pointer in the vector table. Returns 0 on failure
     */
    public static int getEntryAddress(ByteBuffer bb, int tableAddress, int entryOffset) {
        // Construct the absolute address based on the relative pointer. NULL means the value
        // doesn't exist. Note: The corner case where address+offset=NULL can't happen because
        // addresses and offsets are both unsigned.
        int offset = Table.getRelativePointerToEntry(bb, tableAddress, entryOffset);
        return offset == NULL ? NULL : tableAddress + offset;
    }

    public static int NULL = 0;

    static int getVectorTableAddress(ByteBuffer bb, int tableAddress) {
        // Start of object holds the relative offset to its vtable
        // Note that the offset is subtracted, not added
        int vtableOffset = bb.getInt(tableAddress);
        int vtableAddress = tableAddress - vtableOffset;
        return vtableAddress;
    }

    static int getVectorTableLength(ByteBuffer bb, int vtAddress) {
        // Read the length of vtable as the first short (needed
        // for backwards compatibility in case fields got added)
        int vtableLength = unsigned(bb.getShort(vtAddress));
        return vtableLength;
    }

    static int getVectorTableOffset(ByteBuffer bb, int vtAddress, int entryOffset) {
        // If the vtable is not large enough, report that the entry isn't set. This
        // is done for backwards compatibility reasons. Note that these entries are
        // 16 bit aligned and that (vtableLength-1) is only checked to prevent
        // malicious input (odd offset)
        int vtableLength = Table.getVectorTableLength(bb, vtAddress);
        if (entryOffset >= (vtableLength - 1))
            return 0;

        // Find the relative offset (to the table start) of the chosen entry
        int addressOffset = unsigned(bb.getShort(vtAddress + entryOffset));
        return addressOffset;
    }

    static int getRelativePointerToEntry(ByteBuffer bb, int tableAddress, int entryOffset) {
        // Jump to the vector table and extract the relative offset for this entry
        int vtableAddress = Table.getVectorTableAddress(bb, tableAddress);
        int offset = Table.getVectorTableOffset(bb, vtableAddress, entryOffset);
        return offset;
    }

}
