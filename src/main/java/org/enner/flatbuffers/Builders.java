package org.enner.flatbuffers;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

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

    /**
     * @return root address
     */
    public static int addRootTable(ByteBuffer buffer, int numFields) {
        int root = getNextAddress(buffer);
        int pointer = addNullPointer(buffer);
        int table = addTable(buffer, numFields);
        Pointers.setReference(buffer, pointer, table);
        return root;
    }

    /**
     * @return table address
     */
    public static int addTable(ByteBuffer buffer, int numFields) {
        // Build header
        int address = getNextAddress(buffer);
        buffer.putInt(-SIZEOF_INT); // table header contains negative offset to its vtable
        buffer.putShort((short) (SIZEOF_VECTOR_TABLE_HEADER + numFields * SIZEOF_SHORT));
        buffer.putShort((short) 0); // ignore payload-length field. It doesn't mean much if payload is not contiguous

        // Zero the vector table to avoid issues due to potential garbage
        skipAndClear(buffer, numFields * SIZEOF_SHORT, true);
        return address;
    }

    public static int addNullPointer(ByteBuffer buffer) {
        int address = getNextAddress(buffer);
        buffer.putInt(0);
        return address;
    }

}
