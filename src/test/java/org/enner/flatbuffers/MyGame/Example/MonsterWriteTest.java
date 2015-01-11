package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.FlatBufferBuilder;
import org.enner.flatbuffers.Vector;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class MonsterWriteTest {

    byte[] testData;
    ByteBuffer buffer;
    FlatBufferBuilder fbb;

    private int createRootMonster() {
        int numFields = 15;
        fbb.clear().createRootTable(numFields);
        int position = buffer.position();
        buffer.position(fbb.getRootAddress());
        int monster = Monster.getMonsterFromRoot(buffer);
        buffer.position(position);
        return monster;
    }

    @Before
    public void setUp() throws Exception {
        testData = new byte[1024];
        buffer = ByteBuffer.wrap(testData);
        fbb = FlatBufferBuilder.wrapAndClear(buffer);
    }

    @Test
    public void testCreateRoot() throws Exception {
        int monster = createRootMonster();
        assertEquals(4, createRootMonster());
    }

    @Test
    public void testSetHp() throws Exception {
        int monster = createRootMonster();
        assertFalse(Monster.hasHp(buffer, monster));
        short hp = 65;
        Monster.setHp(fbb, monster, hp);
        assertTrue(Monster.hasHp(buffer, monster));
        assertEquals(hp, Monster.getHp(buffer, monster));
        hp = 96;
        Monster.setHp(fbb, monster, hp);
        assertEquals(hp, Monster.getHp(buffer, monster));
    }

    @Test
    public void testSetPos() throws Exception {
        int monster = createRootMonster();
        assertFalse(Monster.hasPos(buffer, monster));
        int pos = Monster.getPosBuilder(fbb, monster);
        assertTrue(Monster.hasPos(buffer, monster));
        assertEquals(pos, Monster.getPos(buffer, monster));
        Vec3.setX(fbb, pos, 0f);
        Vec3.setY(fbb, pos, 1f);
        Vec3.setZ(fbb, pos, 2f);
        assertEquals(0f, Vec3.getX(buffer, pos), 0);
        assertEquals(1f, Vec3.getY(buffer, pos), 0);
        assertEquals(2f, Vec3.getZ(buffer, pos), 0);
        Vec3.setAll(fbb, pos, 2f, 3f, 4f);
        assertEquals(2f, Vec3.getX(buffer, pos), 0);
        assertEquals(3f, Vec3.getY(buffer, pos), 0);
        assertEquals(4f, Vec3.getZ(buffer, pos), 0);
    }

    @Test
    public void testSetName() throws Exception {
        int monster = createRootMonster();
        assertFalse(Monster.hasName(buffer, monster));
        Monster.initName(fbb, monster);
        assertTrue(Monster.hasName(buffer, monster));
        assertEquals(null, Monster.getName(buffer, monster));
        // TODO: allocate string and write
    }

    @Test
    public void testSetInventory() throws Exception {
        int monster = createRootMonster();
        int numElements = 5;
        int elementSize = 1;
        Monster.initInventory(fbb, monster);
        int vector = fbb.addVector(numElements, elementSize);
        Monster.setInventoryAddress(fbb, monster, vector);


        assertEquals(vector, Monster.getInventoryAddress(buffer, monster));

        for (int i = 0; i < numElements; i++) {
            int address = Vector.getValueTypeAddress(buffer, vector, 0, elementSize);
            buffer.put(address, (byte) i);
            assertEquals(i, Monster.getInventory(buffer, monster, i));
        }
    }

}