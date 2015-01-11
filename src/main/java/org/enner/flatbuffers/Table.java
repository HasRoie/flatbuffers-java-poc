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
        int offset = Table.getFieldOffset(bb, tableAddress, fieldId);
        return offset == NULL ? NULL : tableAddress + offset;
    }

    /**
     * Returns the address of a stored reference type (String / Vector / Table)
     * NULL if field or reference have not been set
     */
    public static int getReferenceTypeAddress(ByteBuffer bb, int tableAddress, int fieldId) {
        int pointer = getValueTypeAddress(bb, tableAddress, fieldId);
        return pointer == NULL ? NULL : Pointer.dereference(bb, pointer);
    }

    /**
     * Returns the offset of an element relative to the table address
     */
    private static int getFieldOffset(ByteBuffer bb, int tableAddress, int fieldId) {
        // Jump to the vector table and extract the relative offset for this entry

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

}
