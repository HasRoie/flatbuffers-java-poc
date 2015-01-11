package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.MyGame.Test.GoogleTestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;
import static org.junit.Assert.*;

public class MonsterReadTest {

    byte[] testData;
    ByteBuffer bb;
    com.google.flatbuffers.MyGame.Example.Monster monster;
    com.google.flatbuffers.MyGame.Example.Vec3 pos;

    @Before
    public void setUp() throws Exception {
        testData = GoogleTestData.createTestData();
        bb = ByteBuffer.wrap(testData);
        monster = com.google.flatbuffers.MyGame.Example.Monster.getRootAsMonster(bb);
        pos = monster.pos();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMonsterFromRoot() throws Exception {
        assertEquals(48, Monster.getMonsterFromRoot(bb));
    }

    @Test
    public void testGetHp() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        assertEquals(this.monster.hp(), Monster.getHp(bb, monster));
    }

    @Test
    public void testGetPos() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        int pos = Monster.getPos(bb, monster);
        assertEquals(this.pos.x(), Vec3.getX(bb, pos), 0);
        assertEquals(this.pos.y(), Vec3.getY(bb, pos), 0);
        assertEquals(this.pos.z(), Vec3.getZ(bb, pos), 0);
    }

    @Test
    public void testGetMana() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        assertEquals(this.monster.mana(), Monster.getMana(bb, monster));
    }

    @Test
    public void testGetName() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        assertEquals(this.monster.name(), Monster.getName(bb, monster));
    }

    @Test
    public void testGetInventory() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        int length = Monster.getInventoryLength(bb, monster);
        assertEquals(this.monster.inventoryLength(), length);
        for (int i = 0; i < length; i++) {
            assertEquals(this.monster.inventory(i), Monster.getInventory(bb, monster, i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInventoryOutOfBounds() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        Monster.getInventory(bb, monster, 5);
    }

    @Test
    public void testGetInventoryLength() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        assertEquals(this.monster.inventoryLength(), Monster.getInventoryLength(bb, monster));
    }

    @Test
    public void testGetColor() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        assertEquals(this.monster.color(), Monster.getColor(bb, monster).getValue());
    }

    @Test
    public void testGetEnemy() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        assertEquals(this.monster.enemy() == null, Monster.getEnemy(bb, monster) == NULL);
    }

    @Test
    public void testArrayOfStrings() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        int length = Monster.getTestArrayOfStringLength(bb, monster);
        assertEquals(this.monster.testarrayofstringLength(), length);
        for (int i = 0; i < length; i++) {
            assertEquals(this.monster.testarrayofstring(i), Monster.getTestArrayOfString(bb, monster, i));
        }
    }

}