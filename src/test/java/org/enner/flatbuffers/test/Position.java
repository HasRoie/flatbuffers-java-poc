package org.enner.flatbuffers.test;

import org.enner.flatbuffers.Addressable.Builder;
import org.enner.flatbuffers.Struct;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Primitives.*;
import static org.enner.flatbuffers.Utilities.*;

/**
 * Struct for unit and syntax tests for the various possible
 * combinations. This is the manually written representation
 * of what the code generated for a struct should look like.
 * <p/>
 * Some types have been omitted because they are already being
 * tested by the table test class.
 * <p/>
 * Primitive types
 * - x: float
 * - y: float
 * - z: float
 * <p/>
 * Complex types
 * - Errors: vector of bytes
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Position extends Struct implements Builder {

    public float getX() {
        return getFloat(getBuffer(), getAddress() + X_OFFSET, 0);
    }

    public float getY() {
        return getFloat(getBuffer(), getAddress() + Y_OFFSET, 0);
    }

    public float getZ() {
        return getFloat(getBuffer(), getAddress() + Z_OFFSET, 0);
    }

    public Position setX(float value) {
        setFloat(getBuffer(), getAddress() + X_OFFSET, value);
        return this;
    }

    public Position setY(float value) {
        setFloat(getBuffer(), getAddress() + Y_OFFSET, value);
        return this;
    }

    public Position setZ(float value) {
        setFloat(getBuffer(), getAddress() + Z_OFFSET, value);
        return this;
    }

    public Position setAll(float x, float y, float z) {
        return setX(x).setY(y).setZ(z);
    }

    public static Position withBuffer(ByteBuffer buffer) {
        checkNotNull(buffer);
        Position position = new Position();
        position.setBuffer(buffer);
        position.setAddress(NULL);
        return position;
    }

    private Position() {
    }

    @Override
    public int size() {
        return SIZEOF;
    }

    static int X_OFFSET = 0;
    static int Y_OFFSET = X_OFFSET + SIZEOF_FLOAT;
    static int Z_OFFSET = Y_OFFSET + SIZEOF_FLOAT;
    public static int SIZEOF = Z_OFFSET + SIZEOF_FLOAT;

}
