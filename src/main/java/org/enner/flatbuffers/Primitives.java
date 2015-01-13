package org.enner.flatbuffers;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

/**
 * Helper functions to read and write primitive types, i.e.,
 * bool | byte | ubyte | short | ushort | int | uint | float | long | ulong | double
 * <p/>
 * Note that none of these primitives can be located at location
 * NULL. All unsigned types are stored in a larger representation.
 * Since there is no representation larger than long, ulong isn't
 * implemented.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 11 Jan 2015
 */
public final class Primitives {

    public static boolean getBoolean(ByteBuffer bb, int address, boolean defaultValue) {
        return address == NULL ? defaultValue : bb.get(address) != 0;
    }

    public static byte getByte(ByteBuffer bb, int address, byte defaultValue) {
        return address == NULL ? defaultValue : bb.get(address);
    }

    public static short getUnsignedByte(ByteBuffer bb, int address, short defaultValue) {
        return address == NULL ? defaultValue : unsigned(bb.get(address));
    }

    public static short getShort(ByteBuffer bb, int address, short defaultValue) {
        return address == NULL ? defaultValue : bb.getShort(address);
    }

    public static int getUnsignedShort(ByteBuffer bb, int address, int defaultValue) {
        return address == NULL ? defaultValue : unsigned(bb.getShort(address));
    }

    public static int getInt(ByteBuffer bb, int address, int defaultValue) {
        return address == NULL ? defaultValue : bb.getInt(address);
    }

    public static long getUnsignedInt(ByteBuffer bb, int address, long defaultValue) {
        return address == NULL ? defaultValue : unsigned(bb.getInt(address));
    }

    public static long getLong(ByteBuffer bb, int address, long defaultValue) {
        return address == NULL ? defaultValue : bb.getLong(address);
    }

    public static float getFloat(ByteBuffer bb, int address, float defaultValue) {
        return address == NULL ? defaultValue : bb.getFloat(address);
    }

    public static double getDouble(ByteBuffer bb, int address, double defaultValue) {
        return address == NULL ? defaultValue : bb.getDouble(address);
    }

    public static void setBoolean(ByteBuffer bb, int address, boolean value) {
        bb.put(checkAddressNotNull(address), (byte) (value ? 1 : 0));
    }

    public static void setByte(ByteBuffer bb, int address, byte value) {
        bb.put(checkAddressNotNull(address), value);
    }

    public static void setUnsignedByte(ByteBuffer bb, int address, short value) {
        bb.put(checkAddressNotNull(address), (byte) value);
    }

    public static void setShort(ByteBuffer bb, int address, short value) {
        bb.putShort(checkAddressNotNull(address), value);
    }

    public static void setUnsignedShort(ByteBuffer bb, int address, int value) {
        bb.putShort(checkAddressNotNull(address), (short) value);
    }

    public static void setInt(ByteBuffer bb, int address, int value) {
        bb.putInt(checkAddressNotNull(address), value);
    }

    public static void setUnsignedInt(ByteBuffer bb, int address, long value) {
        bb.putInt(checkAddressNotNull(address), (int) value);
    }

    public static void setLong(ByteBuffer bb, int address, long value) {
        bb.putLong(checkAddressNotNull(address), value);
    }

    public static void setFloat(ByteBuffer bb, int address, float value) {
        bb.putFloat(checkAddressNotNull(address), value);
    }

    public static void setDouble(ByteBuffer bb, int address, double value) {
        bb.putDouble(checkAddressNotNull(address), value);
    }

}
