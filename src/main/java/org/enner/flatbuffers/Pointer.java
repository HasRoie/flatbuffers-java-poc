package org.enner.flatbuffers;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

/**
 * Pointers are uint32 that contain the address to an element. The
 * value is an offset with respect to the address of the pointer.
 * <p/>
 * Pointers can point to Tables / Strings / Unions / Vectors
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Pointer {

    public static int dereference(ByteBuffer buffer, int address) {
        // Read the offset. We could check for NULL, but the pointer
        // may be located at position 0.
        int offset = buffer.getInt(address);
        checkNotNegative(offset);

        // We can use NULL is an invalid state. A NULL value would mean that the pointer
        // is simultaneously some other object, which is impossible.
        return offset == NULL ? NULL : address + offset;
    }

    public static void setReference(ByteBuffer buffer, int pointerAddress, int targetAddress) {
        int offset = targetAddress - pointerAddress;
        checkNotNegative(offset);
        buffer.putInt(pointerAddress, offset);
    }

}
