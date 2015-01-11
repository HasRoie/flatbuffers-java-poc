package org.enner.flatbuffers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.enner.flatbuffers.Utilities.*;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class FlatBufferBuilder {

    public static FlatBufferBuilder wrapAndClear(ByteBuffer buffer) {
        return new FlatBufferBuilder(buffer);
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

    public int getRootAddress() {
        return this.rootAddress;
    }

    public int getNextAddress() {
        return buffer.position();
    }

    public FlatBufferBuilder skip(int numBytes) {
        buffer.position(buffer.position() + numBytes);
        return this;
    }

    public FlatBufferBuilder skipAndFillWithZeros(int numBytes, boolean setAllZeros) {
        int address = getNextAddress();
        skip(numBytes);
        if (setAllZeros)
            setZeros(address, numBytes);
        return this;
    }

    public int addTable(int numElements) {
        // Build header and a zeroed vector table
        int address = getNextAddress();
        buffer.putInt(-SIZEOF_INT); // table header contains negative offset to its vtable
        buffer.putShort((short) (SIZEOF_VECTOR_TABLE_HEADER + numElements * SIZEOF_SHORT));
        buffer.putShort((short) 0); // ignore payload-length field. It doesn't mean much if payload is not contiguous
        skipAndFillWithZeros(numElements * SIZEOF_SHORT, true); // zero offsets to avoid issues with garbage
        return address;
    }

    public int addStruct(int size) {
        int address = getNextAddress();
        buffer.position(buffer.position() + size);
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

        // Skip elements without zeroing memory. This may
        // result in garbage pointers, but it would be too
        // expensive for large vectors of structs.
        skip(numElements * elementSize);
        return address;
    }

    public void setZeros(int address, int length) {
        int remaining = length;

        // Try to write in 8 byte chunks to lower
        //number of calls
        while (remaining >= SIZEOF_LONG) {
            buffer.putLong(address + remaining, 0);
            remaining -= SIZEOF_LONG;
        }

        // Do 4 byte as well. It may not be worth it,
        // but pointers etc. get set quite often.
        while (remaining >= SIZEOF_INT) {
            buffer.putInt(address + remaining, 0);
            remaining -= SIZEOF_INT;
        }

        // Do the rest individually
        while (remaining > 0) {
            buffer.put(address + remaining, (byte) 0);
            remaining -= SIZEOF_BYTE;
        }

    }

    private FlatBufferBuilder(ByteBuffer buffer) {
        setBufferAndClear(buffer);
    }

    ByteBuffer buffer;
    int rootAddress = -1;

    public FlatBufferBuilder createRootTable(int numFields) {
        this.rootAddress = getNextAddress();
        int pointer = addNullPointer();
        int table = addTable(numFields);
        Pointer.setReference(buffer, pointer, table);
        return this;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
