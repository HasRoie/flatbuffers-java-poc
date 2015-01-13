package org.enner.flatbuffers;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

/**
 * There are 3 different kinds of pointers in FlatBuffers. All
 * of them contain an offset to their own location to describe
 * the address of another element. Values of NULL (zero) only
 * happen if pointers are not been set.
 * <p/>
 * 1) UInt32: These are the most common pointers. They are used
 * to refer to all reference types, i.e., Tables, Vectors, Unions,
 * and Strings.
 * <p/>
 * 2) Int32: These are used to point from a Table header into the
 * vector table. They are signed because vector tables may be
 * located anywhere in the buffer. The reason for this is that
 * many elements may share a common vector table, and thus can
 * save much overhead. Note that for some reason these are
 * subtracted from the pointer instead of added.
 * <p/>
 * 3) UInt16: These are stored in vector tables and they point to
 * the location of a table element. Note that in this case the
 * pointers contain the offset with respect to the start of the
 * table, not to the pointer itself.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public final class Pointers {

    public static int dereference(ByteBuffer buffer, int pointer) {
        // Read the offset. We could check for NULL, but the pointer
        // may be located at position 0.
        int offset = buffer.getInt(pointer);
        checkNotNegative(offset);

        // We can use NULL is an invalid state. A NULL value would mean that the pointer
        // is simultaneously some other object, which is impossible.
        return offset == NULL ? NULL : pointer + offset;
    }

    public static void setReference(ByteBuffer buffer, int pointer, int targetAddress) {
        int offset = targetAddress - pointer;
        checkArgument(offset >= 0, "Pointer can't be negative. Make sure to initialize it first");
        buffer.putInt(pointer, offset);
    }

    public static int dereferenceUnsigned16(ByteBuffer buffer, int pointer, int tableAddress) {
        checkNotNull(buffer);
        checkAddressNotNull(pointer);
        checkNotNegative(pointer);
        checkNotNegative(tableAddress);
        int offset = unsigned(buffer.getShort(pointer));
        return offset == NULL ? NULL : tableAddress + offset;
    }

    public static void setUnsigned16Reference(ByteBuffer buffer, int pointer, int targetAddress, int tableAddress) {
        // Make sure that the offset is within the 65KB limit of unsigned int16
        int offset = targetAddress - tableAddress;
        checkNotNegative(offset);
        checkArgument(offset <= USHORT_MAX, "Address is outside the table's range of at most UINT16_MAX bytes");
        buffer.putShort(pointer, (short) offset);
    }

    public static final int dereferenceSigned32(ByteBuffer buffer, int pointer) {
        // Since it can technically be located at the beginning, we can't check
        // for NULL
        checkNotNull(buffer);
        checkNotNegative(pointer);
        // A NULL offset would mean that there is no vector table, which
        // means that the initialization was wrong
        int offset = checkAddressNotNull(buffer.getInt(pointer));
        return pointer - offset;
    }

    public static final void setSigned32Reference(ByteBuffer buffer, int pointer, int vectorTableAddress) {
        checkNotNull(buffer);
        checkNotNegative(pointer);
        checkNotNegative(vectorTableAddress);
        int offset = checkAddressNotNull(pointer - vectorTableAddress);
        buffer.putInt(pointer, offset);
    }

}
