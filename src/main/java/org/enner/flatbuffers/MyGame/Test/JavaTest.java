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

        int monster = MonsterReadTest.getMonsterFromRoot(bb);
        short hp = MonsterReadTest.getHp(bb, monster);
        short mana = MonsterReadTest.getMana(bb, monster);
        String name = MonsterReadTest.getName(bb, monster);

        int vec3 = MonsterReadTest.getPos(bb, monster);
        float x = Vec3.getX(bb, vec3);
        float y = Vec3.getY(bb, vec3);
        float z = Vec3.getZ(bb, vec3);

        int inventory = MonsterReadTest.getInventory(bb, monster);
        int inventoryLength = MonsterReadTest.getInventoryLength(bb, monster);
        for (int i = 0; i < inventoryLength; i++) {
            byte inv = MonsterReadTest.getInventory(bb, monster, i);
        }

        Color color = MonsterReadTest.getColor(bb, monster);

    }

}
