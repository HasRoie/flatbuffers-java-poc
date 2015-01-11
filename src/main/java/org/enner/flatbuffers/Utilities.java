package org.enner.flatbuffers;

import java.nio.ByteBuffer;

/**
 * This class contains convenience functions related to parsing
 * elements from addresses. They return defaults if addresses are
 * NULL.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Utilities {

    public static final int SIZEOF_BYTE = Byte.BYTES;
    public static final int SIZEOF_SHORT = Short.BYTES;
    public static final int SIZEOF_INT = Integer.BYTES;
    public static final int SIZEOF_LONG = Long.BYTES;
    public static final int SIZEOF_FLOAT = Float.BYTES;
    public static final int SIZEOF_DOUBLE = Double.BYTES;

    public static final int SIZEOF_POINTER = SIZEOF_INT;
    public static final int SIZEOF_VECTOR_HEADER = SIZEOF_INT;
    public static final int SIZEOF_TABLE_HEADER = SIZEOF_INT;
    public static final int SIZEOF_VECTOR_TABLE_HEADER = 2 * SIZEOF_SHORT;
    public static final int FILE_IDENTIFIER_LENGTH = 4;

    public static final int USHORT_MAX = ((int) Short.MAX_VALUE) << 1;
    public static int NULL = 0;

    public static float getFloat(ByteBuffer bb, int address, float defaultValue) {
        return address == NULL ? defaultValue : bb.getFloat(address);
    }

    public static double getDouble(ByteBuffer bb, int address, double defaultValue) {
        return address == NULL ? defaultValue : bb.getDouble(address);
    }

    /**
     * Bool / Byte / Enum
     */
    public static byte getByte(ByteBuffer bb, int address, byte defaultValue) {
        return address == NULL ? defaultValue : bb.get(address);
    }

    public static short getShort(ByteBuffer bb, int address, short defaultValue) {
        return address == NULL ? defaultValue : bb.getShort(address);
    }

    public static int getInt(ByteBuffer bb, int address, int defaultValue) {
        return address == NULL ? defaultValue : bb.getInt(address);
    }

    // Store unsigned values in larger types

    public static short getUnsignedByte(ByteBuffer bb, int address, short defaultValue) {
        return address == NULL ? defaultValue : unsigned(bb.get(address));
    }

    public static int getUnsignedShort(ByteBuffer bb, int address, int defaultValue) {
        return address == NULL ? defaultValue : unsigned(bb.getShort(address));
    }

    public static long getUnsignedInt(ByteBuffer bb, int address, long defaultValue) {
        return address == NULL ? defaultValue : unsigned(bb.getInt(address));
    }

    public static short unsigned(byte value) {
        return (short) (value & 0xFF);
    }

    public static int unsigned(short value) {
        return value & 0xFFFF;
    }

    public static long unsigned(int value) {
        return value & 0xFFFFFFFFL;
    }

    public static int checkNotNegative(int value) {
        // Make sure that it's unsigned. Note that it's not converted to long
        // in case the protocol is changed to support signed values in the future.
        if (value < 0)
            throw new IllegalStateException("Value is not allowed to be negative.");
        return value;
    }

    public static int checkAddressNotNull(int address) {
        if (address == NULL)
            throw new NullPointerException("FlatBuffer pointer has not been set");
        return address;
    }

    public static <T> T checkNotNull(T object) {
        if (object == null)
            throw new NullPointerException();
        return object;
    }

    public static void checkArgument(boolean expression) {
        if (!expression)
            throw new IllegalArgumentException();
    }

    public static void checkArgument(boolean expression, String error) {
        if (!expression)
            throw new IllegalArgumentException(error);
    }

    public static void checkState(boolean expression) {
        if (!expression)
            throw new IllegalStateException();
    }

    public static void checkState(boolean expression, String error) {
        if (!expression)
            throw new IllegalStateException(error);
    }


}
