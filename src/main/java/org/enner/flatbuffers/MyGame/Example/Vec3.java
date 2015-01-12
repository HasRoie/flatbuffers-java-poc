package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.Addressable;
import org.enner.flatbuffers.Primitives;
import org.enner.flatbuffers.Struct;

import static org.enner.flatbuffers.Utilities.*;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Vec3 extends Struct implements Addressable {

    public float getX() {
        return Primitives.getFloat(getBuffer(), getAddress() + OFFSET_X, 0);
    }

    public float getY() {
        return Primitives.getFloat(getBuffer(), getAddress() + OFFSET_Y, 0);
    }

    public float getZ() {
        return Primitives.getFloat(getBuffer(), getAddress() + OFFSET_Z, 0);
    }

    public static class Vec3Builder extends Vec3 implements Addressable.Builder {

        public Vec3Builder setX(float value) {
            Primitives.setFloat(getBuffer(), getAddress() + OFFSET_X, value);
            return this;
        }

        public Vec3Builder setY(float value) {
            Primitives.setFloat(getBuffer(), getAddress() + OFFSET_Y, value);
            return this;
        }

        public Vec3Builder setZ(float value) {
            Primitives.setFloat(getBuffer(), getAddress() + OFFSET_Z, value);
            return this;
        }

        public Vec3Builder setAll(float x, float y, float z) {
            return setX(x).setY(y).setZ(z);
        }

    }

    @Override
    public int size() {
        return SIZE;
    }

    public static final int OFFSET_X = 0;
    public static final int OFFSET_Y = 4;
    public static final int OFFSET_Z = 8;
    public static final int SIZE = 3 * SIZEOF_FLOAT;

}
