package org.enner.flatbuffers;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

/**
 * Vectors consist of the number of elements (uint32), followed by the data
 * in contiguous memory. The address pointer points towards the size info.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Vector {

    /**
     * @return element count
     */
    public static int size(ByteBuffer buffer, int address) {
        checkAddressNotNull(address);
        int elementCount = buffer.getInt(address);
        return elementCount;
    }

    public static int getFirstElementAddress(ByteBuffer buffer, int address) {
        checkAddressNotNull(address);
        return address + SIZEOF_VECTOR_HEADER;
    }

    /**
     * Returns the address of a stored value type (byte / short / int / struct / etc.)
     */
    public static int getValueTypeAddress(ByteBuffer buffer, int address, int index, int elementSize) {
        // Vector can't be stored in the beginning of the buffer
        checkAddressNotNull(address);
        checkNotNegative(index);
        checkNotNegative(elementSize);

        // Make sure index is not out of bounds
        int numElements = size(buffer, address);
        if (index >= numElements)
            throw new IndexOutOfBoundsException();

        // Data starts directly after the size header, in contiguous memory
        return address + SIZEOF_VECTOR_HEADER + index * elementSize;
    }

    /**
     * Returns the address of a stored reference type (String / Vector / Table)
     * NULL if reference has not been set
     */
    public static int getReferenceTypeAddress(ByteBuffer buffer, int address, int index) {
        int pointer = getValueTypeAddress(buffer, address, index, SIZEOF_POINTER);
        return pointer == NULL ? NULL : Pointer.dereference(buffer, pointer);
    }

}
