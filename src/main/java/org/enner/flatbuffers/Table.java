package org.enner.flatbuffers;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

/**
 * Tables consist of a pointer, a vector table, and data. The
 * (signed) pointer points towards the vector table, while the
 * vector table points towards the data relative to the table
 * address. The memory does not have to be contiguous, but
 * is limited to a 65 KB offset relative to the address.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Table {

    /**
     * Returns true if there exists an entry in the vector table. Note that this can
     * be ambiguous because implementations may omit default values during serialization.
     */
    public static boolean hasElement(ByteBuffer bb, int tableAddress, int vtableOffset) {
        return getElementOffset(bb, tableAddress, vtableOffset) != NULL;
    }

    /**
     * Returns the absolute address of the pointer in the vector table. Returns 0 on failure
     */
    public static int getElementAddress(ByteBuffer bb, int tableAddress, int vtableOffset) {
        // Construct the absolute address based on the relative pointer. NULL means the value
        // doesn't exist. Note: The corner case where address+offset=NULL can't happen because
        // addresses and offsets are both unsigned and greater than 0.
        int offset = Table.getElementOffset(bb, tableAddress, vtableOffset);
        return offset == NULL ? NULL : tableAddress + offset;
    }

    /**
     * Returns the relative offset of an element relative to the table address
     */
    private static int getElementOffset(ByteBuffer bb, int tableAddress, int entryOffset) {
        // Jump to the vector table and extract the relative offset for this entry
        int vtableAddress = Table.getVectorTableAddress(bb, tableAddress);
        int offset = Table.getVectorTableOffset(bb, vtableAddress, entryOffset);
        return offset;
    }

    private static int getVectorTableAddress(ByteBuffer bb, int tableAddress) {
        // Start of object holds the relative offset to its vtable
        // Note that the offset is subtracted, not added
        int vtableOffset = bb.getInt(tableAddress);
        int vtableAddress = tableAddress - vtableOffset;
        return vtableAddress;
    }

    private static int getVectorTableOffset(ByteBuffer bb, int vtAddress, int entryOffset) {
        // If the vtable is not large enough, report that the entry isn't set. This
        // is done for backwards compatibility reasons. "vtableLength - 1" is checked
        // to prevent malicious input of odd offsets
        int vtableLength = Table.getVectorTableSize(bb, vtAddress);
        if (entryOffset >= (vtableLength - 1))
            return NULL;

        // Find the relative offset (to the table start) of the chosen entry
        int addressOffset = unsigned(bb.getShort(vtAddress + entryOffset));
        return addressOffset;
    }

    private static int getVectorTableSize(ByteBuffer bb, int vtAddress) {
        // Read the length of vtable as the first short (needed
        // for backwards compatibility in case fields got added)
        int vtableLength = unsigned(bb.getShort(vtAddress));
        return vtableLength;
    }

}
