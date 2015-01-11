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

    static final int SIZEOF_BYTE = Byte.BYTES;
    static final int SIZEOF_SHORT = Short.BYTES;
    static final int SIZEOF_INT = Integer.BYTES;
    static final int FILE_IDENTIFIER_LENGTH = 4;
    public static int NULL = 0;

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

}
