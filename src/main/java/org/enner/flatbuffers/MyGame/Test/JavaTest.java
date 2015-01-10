package org.enner.flatbuffers.MyGame.Test;

import org.enner.flatbuffers.*;
import org.enner.flatbuffers.MyGame.Example.*;

import java.nio.ByteBuffer;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class JavaTest {

    public static void main(String[] args) {

        // Read data


        // Create builder for some data
        byte[] data = new byte[100];
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        FlatBufferBuilder builder = new FlatBufferBuilder(byteBuffer);




    }

    static void testBuffer(ByteBuffer bb) {

        int monster = Monster.getMonsterFromRoot(bb);
        short hp = Monster.getHp(bb, monster);
        short mana = Monster.getMana(bb, monster);
        String name = Monster.getName(bb, monster);

        int vec3 = Monster.getPos(bb, monster);
        float x = Vec3.getX(bb, vec3);
        float y = Vec3.getY(bb, vec3);
        float z = Vec3.getZ(bb, vec3);

        int inventoryLength = Monster.getInventoryLength(bb, monster);
        for (int i = 0; i < inventoryLength; i++) {
            byte inv = Monster.getInventory(bb, monster, i);
        }

        Color color = Monster.getColor(bb, monster);

    }

}
