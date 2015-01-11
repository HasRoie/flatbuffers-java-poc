package org.enner.flatbuffers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.enner.flatbuffers.Utilities.*;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class FlatBufferBuilder {

    public FlatBufferBuilder(ByteBuffer buffer) {
        setBufferAndClear(buffer);
    }

    public FlatBufferBuilder setBufferAndClear(ByteBuffer buffer) {
        buffer.clear();
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.buffer = buffer;
        return this;
    }

    public FlatBufferBuilder clear() {
        this.buffer.clear();
        return this;
    }

    public int getNextAddress() {
        return buffer.position() + 1;
    }

    public int addTable(int numElements) {
        // Build Header
        int address = getNextAddress();
        buffer.putInt(-SIZEOF_INT); // table start with negative offset to its vtable
        buffer.putShort((short) ((numElements + 2) * SIZEOF_SHORT)); // 2x uint16 headers
        buffer.putShort((short) 0); // ignore payload-length field

        // Zero vtable offsets in case there is garbage
        addZeros(numElements * SIZEOF_SHORT);
        return address;
    }

    public int addNullPointer() {
        int address = getNextAddress();
        buffer.putInt(NULL);
        return address;
    }

    public int addVector(int numElements, int elementSize) {
        // Get address and write header
        int address = getNextAddress();
        buffer.putInt(numElements);

        // Zero the buffer in case there is garbage
        addZeros(numElements * elementSize);
        return address;
    }

    public void addZeros(int count) {
        int remaining = count;

        // Try to write in 8 byte chunks to lower
        //number of calls
        while (remaining >= SIZEOF_LONG) {
            buffer.putLong(0);
            remaining -= SIZEOF_LONG;
        }

        // Do the rest individually
        while (remaining > 0) {
            buffer.put((byte) 0);
            remaining -= SIZEOF_BYTE;
        }

    }

    ByteBuffer buffer;

}
