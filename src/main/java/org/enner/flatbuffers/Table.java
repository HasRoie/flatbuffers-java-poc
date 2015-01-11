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
        return getFieldAddress(bb, tableAddress, fieldId) != NULL;
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
        return getFieldAddress(bb, tableAddress, fieldId);
    }

    public static void setValueTypeAddress(FlatBufferBuilder fbb, int tableAddress, int fieldId, int address) {
        checkNotNull(checkNotNull(fbb).getBuffer());
        checkNotNegative(tableAddress);
        checkNotNegative(fieldId);
        ByteBuffer bb = checkNotNull(fbb.getBuffer());

        int vtableAddress = Pointer.dereferenceSigned32(bb, tableAddress);
        checkArgument(fieldId < getFieldCount(bb, vtableAddress), "Field id is too large.");
        int offsetFromVectorTable = SIZEOF_VECTOR_TABLE_HEADER + fieldId * SIZEOF_SHORT;
        int pointer = vtableAddress + offsetFromVectorTable;
        Pointer.setUnsigned16Reference(fbb.getBuffer(), pointer, address, tableAddress);
    }

    /**
     * Returns the address of a stored reference type (String / Vector / Table)
     * NULL if field or reference have not been set
     */
    public static int getReferenceTypeAddress(ByteBuffer bb, int tableAddress, int fieldId) {
        int pointer = getPointerAddress(bb, tableAddress, fieldId);
        return pointer == NULL ? NULL : Pointer.dereference(bb, pointer);
    }

    public static void setReferenceTypeAddress(FlatBufferBuilder fbb, int tableAddress, int fieldId, int target) {
        ByteBuffer buffer = checkNotNull(fbb).getBuffer();
        int pointer = getPointerAddress(buffer, tableAddress, fieldId);
        checkState(pointer != NULL, "Pointer must be initialized before setting an address");
        Pointer.setReference(buffer, pointer, target);
    }

    private static int getPointerAddress(ByteBuffer bb, int tableAddress, int fieldId) {
        return getValueTypeAddress(bb, tableAddress, fieldId);
    }

    private static int initValueType(FlatBufferBuilder fbb, int table, int fieldId, int size, boolean fillWithZeros) {
        ByteBuffer buffer = checkNotNull(checkNotNull(fbb).getBuffer());
        checkNotNegative(table);
        checkNotNegative(fieldId);
        // Do nothing if data has already been initialized
        int address = Table.getValueTypeAddress(buffer, table, fieldId);
        if (address == NULL) {
            // Set the address first in order to make sure that we don't allocate memory
            // that we can't point to
            address = fbb.getNextAddress();
            Table.setValueTypeAddress(fbb, table, fieldId, address);
            fbb.skipAndFillWithZeros(size, fillWithZeros);
        }
        return address;
    }

    /**
     * Makes sure that a pointer object exists. If the pointer already exists, then nothing happens.
     * This is useful for
     *
     * @return address of pointer. The pointer gets initialized as NULL
     */
    public static int initReferencePointer(FlatBufferBuilder fbb, int tableAddress, int fieldId) {
        return initValueType(fbb, tableAddress, fieldId, SIZEOF_POINTER, true);
    }

    /**
     * @return start address of value. Note that the space may contain garbage.
     */
    public static int initValueType(FlatBufferBuilder fbb, int table, int fieldId, int size) {
        return initValueType(fbb, table, fieldId, size, false);
    }

    /**
     * Returns the offset of an element relative to the table address
     */
    private static int getFieldAddress(ByteBuffer bb, int tableAddress, int fieldId) {

        // Find the start of the vector table
        int vtableAddress = Pointer.dereferenceSigned32(bb, tableAddress);

        // Check whether the field can exist. Older versions of the schema
        // may have fewer fields. In this case always assume a NULL offset.
        int numFields = getFieldCount(bb, vtableAddress);
        if (fieldId >= numFields)
            return NULL;

        // Find the location in which the offset would be stored
        int offsetFromVectorTable = SIZEOF_VECTOR_TABLE_HEADER + fieldId * SIZEOF_SHORT;
        int pointer = vtableAddress + offsetFromVectorTable;
        return Pointer.dereferenceUnsigned16(bb, pointer, tableAddress);
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
