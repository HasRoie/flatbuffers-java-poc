package org.enner.flatbuffers.MyGame.Example;

import java.nio.ByteBuffer;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Vec3 {

    public static float getX(ByteBuffer bb, int vec3Address) {
        int offset = 0;
        return bb.getFloat(vec3Address + offset);
    }

    public static float getY(ByteBuffer bb, int vec3Address) {
        int offset = 4;
        return bb.getFloat(vec3Address + offset);
    }

    public static float getZ(ByteBuffer bb, int vec3Address) {
        int offset = 8;
        return bb.getFloat(vec3Address + offset);
    }

}
