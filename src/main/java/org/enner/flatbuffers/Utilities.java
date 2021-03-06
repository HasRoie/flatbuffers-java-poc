package org.enner.flatbuffers;

import java.nio.ByteBuffer;

/**
 * This class contains constants and generally useful
 * helper methods.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Utilities {

    // Built-in types
    public static final int SIZEOF_BYTE = Byte.BYTES;
    public static final int SIZEOF_BOOL = Byte.BYTES;
    public static final int SIZEOF_ENUM = Byte.BYTES;
    public static final int SIZEOF_SHORT = Short.BYTES;
    public static final int SIZEOF_INT = Integer.BYTES;
    public static final int SIZEOF_LONG = Long.BYTES;
    public static final int SIZEOF_FLOAT = Float.BYTES;
    public static final int SIZEOF_DOUBLE = Double.BYTES;

    // Protocol types
    public static final int SIZEOF_POINTER = SIZEOF_INT;
    public static final int SIZEOF_VECTOR_HEADER = SIZEOF_INT;
    public static final int SIZEOF_TABLE_HEADER = SIZEOF_INT;
    public static final int SIZEOF_VECTOR_TABLE_HEADER = 2 * SIZEOF_SHORT;
    public static final int FILE_IDENTIFIER_LENGTH = 4;

    // Comparison constants
    public static final int USHORT_MAX = ((int) Short.MAX_VALUE) << 1;
    public static int NULL = 0;

    // Store unsigned values in larger types

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
        if (value < 0)
            throw new IllegalArgumentException("Value is not allowed to be negative.");
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
