package org.enner.flatbuffers;

import java.nio.ByteBuffer;

/**
 * Helper functions that are useful when creating a message
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 11 Jan 2015
 */
public class Builders {

    public static void skip(ByteBuffer buffer, int numBytes) {
        buffer.position(buffer.position() + numBytes);
    }

    public static void skipAndClear(ByteBuffer buffer, int numBytes, boolean memsetZero) {
        int address = getNextAddress(buffer);
        skip(buffer, numBytes);
        if (memsetZero)
            Builders.setZeros(buffer, address, numBytes);
    }

    public static void setZeros(ByteBuffer buffer, int address, int length) {
        int remaining = length;

        // Try to write in 8 byte chunks to lower
        //number of calls
        while (remaining >= Utilities.SIZEOF_LONG) {
            buffer.putLong(address + remaining, 0);
            remaining -= Utilities.SIZEOF_LONG;
        }

        // Do 4 byte as well. It may not be worth it,
        // but pointers etc. get set quite often.
        while (remaining >= Utilities.SIZEOF_INT) {
            buffer.putInt(address + remaining, 0);
            remaining -= Utilities.SIZEOF_INT;
        }

        // Do the rest individually
        while (remaining > 0) {
            buffer.put(address + remaining, (byte) 0);
            remaining -= Utilities.SIZEOF_BYTE;
        }

    }

    public static int getNextAddress(ByteBuffer buffer) {
        return buffer.position();
    }
}
