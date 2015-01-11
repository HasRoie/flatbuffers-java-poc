package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.FlatBufferBuilder;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Vec3 {

    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 4;
    public static final int OFFSET_Z = 8;

    public static int size() {
        return 3 * SIZEOF_FLOAT;
    }

    public static float getX(ByteBuffer bb, int vec3Address) {
        return bb.getFloat(vec3Address + OFFSET_X);
    }

    public static float getY(ByteBuffer bb, int vec3Address) {
        return bb.getFloat(vec3Address + OFFSET_Y);
    }

    public static float getZ(ByteBuffer bb, int vec3Address) {
        return bb.getFloat(vec3Address + OFFSET_Z);
    }

    public static void setX(FlatBufferBuilder fbb, int address, float value) {
        fbb.getBuffer().putFloat(address + OFFSET_X, value);
    }

    public static void setY(FlatBufferBuilder fbb, int address, float value) {
        fbb.getBuffer().putFloat(address + OFFSET_Y, value);
    }

    public static void setZ(FlatBufferBuilder fbb, int address, float value) {
        fbb.getBuffer().putFloat(address + OFFSET_Z, value);
    }

    public static void setAll(FlatBufferBuilder builder, int address, float x, float y, float z) {
        setX(builder, address, x);
        setY(builder, address, y);
        setZ(builder, address, z);
    }

}
