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
    public static boolean hasField(ByteBuffer bb, int tableAddress, int fieldId) {
        checkNotNull(bb);
        checkNotNegative(tableAddress);
        checkNotNegative(fieldId);
        return getFieldOffset(bb, tableAddress, fieldId) != NULL;
    }

    /**
     * Returns the address of a stored value type (byte / short / int / struct / etc.)
     * NULL if field is not set
     */
    public static int getValueTypeAddress(ByteBuffer bb, int tableAddress, int fieldId) {
        // Construct the absolute address based on the relative pointer. NULL means the value
        // doesn't exist. Note: The corner case where address+offset=NULL can't happen because
        // addresses and offsets are both unsigned and greater than 0.
        checkNotNull(bb);
        checkNotNegative(tableAddress);
        checkNotNegative(fieldId);
        int offset = Table.getFieldOffset(bb, tableAddress, fieldId);
        return offset == NULL ? NULL : tableAddress + offset;
    }

    public static void setValueTypeAddress(FlatBufferBuilder fbb, int tableAddress, int fieldId, int address) {
        checkNotNull(fbb);
        checkNotNegative(tableAddress);
        checkNotNegative(fieldId);
        ByteBuffer bb = checkNotNull(fbb.getBuffer());

        // Make sure offset is within uint16 range
        int offset = address - tableAddress;
        checkNotNegative(offset);
        checkArgument(offset <= USHORT_MAX, "Address is outside the table's range of at most UINT16_MAX bytes");

        // Update stored offset
        int vtableAddress = Table.getVectorTableAddress(bb, tableAddress);
        checkArgument(fieldId < getFieldCount(bb, vtableAddress));
        int offsetFromVectorTable = SIZEOF_VECTOR_TABLE_HEADER + fieldId * SIZEOF_SHORT;
        bb.putShort(vtableAddress + offsetFromVectorTable, (short) offset);
    }

    public static int getPointerAddress(ByteBuffer bb, int tableAddress, int fieldId) {
        return getValueTypeAddress(bb, tableAddress, fieldId);
    }

    /**
     * Returns the address of a stored reference type (String / Vector / Table)
     * NULL if field or reference have not been set
     */
    public static int getReferenceTypeAddress(ByteBuffer bb, int tableAddress, int fieldId) {
        int pointer = getPointerAddress(bb, tableAddress, fieldId);
        return pointer == NULL ? NULL : Pointer.dereference(bb, pointer);
    }


    /**
     * Returns the offset of an element relative to the table address
     */
    private static int getFieldOffset(ByteBuffer bb, int tableAddress, int fieldId) {
        // Find the start of the vector table
        int vtableAddress = Table.getVectorTableAddress(bb, tableAddress);

        // Check whether the field can exist. Older versions of the schema
        // may have fewer fields. In this case always assume a NULL offset.
        int numFields = getFieldCount(bb, vtableAddress);
        if (fieldId >= numFields)
            return NULL;

        // Find the location in which the offset would be stored
        int offsetFromVectorTable = SIZEOF_VECTOR_TABLE_HEADER + fieldId * SIZEOF_SHORT;
        int fieldOffset = unsigned(bb.getShort(vtableAddress + offsetFromVectorTable));
        return fieldOffset;
    }

    private static int getVectorTableAddress(ByteBuffer bb, int tableAddress) {
        // Start of object holds the relative offset to its vtable. The pointer
        // is signed so that the vtable can be located anywhere. Note that for
        // some reason the offset is subtracted instead of added. Since the
        // vtable can technically be located at the beginning, we can't do
        // a null check
        int vectorTableAddress = tableAddress - bb.getInt(tableAddress);
        return vectorTableAddress;
    }

    private static int getFieldCount(ByteBuffer bb, int vtAddress) {
        // The vector table stores its size (in bytes, including header)
        // in the first 2 bytes as uint16
        int vectorTableSize = unsigned(bb.getShort(vtAddress));
        int numFields = (vectorTableSize - SIZEOF_VECTOR_TABLE_HEADER) / SIZEOF_SHORT;
        return numFields;
    }

    private static int getPayloadSize(ByteBuffer bb, int vtAddress) {
        // Payload size is in the 2nd short of the header. Size is in bytes
        // and includes the header itself.
        return unsigned(bb.getShort(vtAddress + SIZEOF_SHORT));
    }

}
