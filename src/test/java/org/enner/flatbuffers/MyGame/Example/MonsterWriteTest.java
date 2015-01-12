package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.FlatBufferBuilder;
import org.enner.flatbuffers.MyGame.Example.Monster.MonsterBuilder;
import org.enner.flatbuffers.MyGame.Example.Vec3.Vec3Builder;
import org.enner.flatbuffers.Vector.ByteVectorBuilder;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class MonsterWriteTest {

    byte[] testData;
    ByteBuffer buffer;
    FlatBufferBuilder fbb;
    MonsterBuilder monster;
    MonsterBuilder enemy;
    Vec3Builder pos;

    private int createRootMonster() {
        int numFields = 15;
        fbb.clear().createRootTable(numFields);
        int position = buffer.position();
        buffer.position(fbb.getRootAddress());
        int monster = Monster.getMonsterFromRoot(buffer);
        this.monster.getFromRoot();
        buffer.position(position);
        return monster;
    }

    @Before
    public void setUp() throws Exception {
        testData = new byte[1024];
        buffer = ByteBuffer.wrap(testData);
        fbb = FlatBufferBuilder.wrapAndClear(buffer);
        monster = Monster.newBuilder(buffer);
        pos = new Vec3Builder();
        pos.setBuffer(buffer);
    }

    @Test
    public void testCreateRoot() throws Exception {
        int monster = createRootMonster();
        assertEquals(4, createRootMonster());
    }

    @Test
    public void testSetHp() throws Exception {
        createRootMonster();
        assertFalse(monster.hasHp());
        short hp = 65;
        monster.setHp(hp);
        assertTrue(monster.hasHp());
        assertEquals(hp, monster.getHp());
        hp = 96;
        monster.setHp(hp);
        assertEquals(hp, monster.getHp());
    }

    @Test
    public void testSetPos() throws Exception {
        createRootMonster();
        assertFalse(monster.hasPos());
        monster.getPosBuilder(pos);
        assertTrue(monster.hasPos());
        monster.getPosBuilder(pos)
                .setX(0f)
                .setY(1f)
                .setZ(2f);
        assertEquals(0f, pos.getX(), 0);
        assertEquals(1f, pos.getY(), 0);
        assertEquals(2f, pos.getZ(), 0);
        pos.setAll(2f, 3f, 4f);
        assertEquals(2f, pos.getX(), 0);
        assertEquals(3f, pos.getY(), 0);
        assertEquals(4f, pos.getZ(), 0);
    }

    @Test
    public void testSetName() throws Exception {
        createRootMonster();
        assertFalse(monster.hasName());
        monster.initName();
        assertFalse(monster.hasName());
        assertEquals(null, monster.getName());
        // TODO: allocate string and write
    }

    @Test
    public void testSetInventory() throws Exception {
        createRootMonster();
        int numElements = 5;
        int elementSize = 1;

        assertFalse(monster.hasInventory());
        assertFalse(monster.initInventory().hasInventory());
        assertTrue(monster.createInventory(numElements).hasInventory());
        assertEquals(numElements, monster.getInventory().length());

        ByteVectorBuilder inventory = monster.getInventoryBuilder();
        for (int i = 0; i < numElements; i++) {
            inventory.setByte(i, (byte) i);
            assertEquals(i, inventory.getByte(i));
        }
    }

}