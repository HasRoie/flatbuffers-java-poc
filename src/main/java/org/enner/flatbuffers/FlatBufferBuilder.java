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

    public FlatBufferBuilder skipAndFillWithZeros(int numBytes, boolean memsetZero) {
        int address = getNextAddress();
        skip(numBytes);
        if (memsetZero)
            Builders.setZeros(buffer, address, numBytes);
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

    private FlatBufferBuilder(ByteBuffer buffer) {
        setBufferAndClear(buffer);
    }

    ByteBuffer buffer;
    int rootAddress = -1;

    public FlatBufferBuilder createRootTable(int numFields) {
        this.rootAddress = getNextAddress();
        int pointer = addNullPointer();
        int table = addTable(numFields);
        Pointers.setReference(buffer, pointer, table);
        return this;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
