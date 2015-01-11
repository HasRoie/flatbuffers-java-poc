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
    public static boolean hasElement(ByteBuffer bb, int tableAddress, int fieldId) {
        return getElementOffset(bb, tableAddress, fieldId) != NULL;
    }

    /**
     * Returns the absolute address of the pointer in the vector table. Returns 0 on failure
     */
    public static int getElementAddress(ByteBuffer bb, int tableAddress, int fieldId) {
        // Construct the absolute address based on the relative pointer. NULL means the value
        // doesn't exist. Note: The corner case where address+offset=NULL can't happen because
        // addresses and offsets are both unsigned and greater than 0.
        int offset = Table.getElementOffset(bb, tableAddress, fieldId);
        return offset == NULL ? NULL : tableAddress + offset;
    }

    /**
     * Returns the offset of an element relative to the table address
     */
    private static int getElementOffset(ByteBuffer bb, int tableAddress, int fieldId) {
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
        int elementOffset = unsigned(bb.getShort(vtableAddress + offsetFromVectorTable));
        return elementOffset;
    }

    private static int getVectorTableAddress(ByteBuffer bb, int tableAddress) {
        // Start of object holds the relative offset to its vtable. The pointer
        // is signed so that the vtable can be located anywhere. Note that for
        // some reason the offset is subtracted instead of added.
        int vectorTableAddress = tableAddress - bb.getInt(tableAddress);

        // Fail if vector table doesn't exist. This should never happen if
        // the element is initialized correctly.
        checkAddressNotNull(vectorTableAddress);
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
