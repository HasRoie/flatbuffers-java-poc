package org.enner.flatbuffers;

import java.nio.ByteBuffer;

/**
 * Vectors consist of the number of elements (uint32), followed by the data
 * in contiguous memory. The address pointer points towards the size info.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Vector {

    public static int getLength(ByteBuffer buffer, int address) {
        // Length field is located at the beginning
        return address == Utilities.NULL ? 0 : buffer.getInt(address);
    }

    public static int getElementAddress(ByteBuffer buffer, int address, int index, int elementSize) {
        if (address == Utilities.NULL)
            return Utilities.NULL;

        // Make sure index is not out of bounds
        int numElements = buffer.getInt(address);
        if (index >= numElements)
            throw new IndexOutOfBoundsException();

        // Data starts directly after the size header, in contiguous memory
        return address + Utilities.SIZEOF_INT + index * elementSize;
    }

}
