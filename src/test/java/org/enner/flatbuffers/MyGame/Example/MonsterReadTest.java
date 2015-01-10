package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.MyGame.Test.GoogleTestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class MonsterReadTest {

    byte[] testData;
    ByteBuffer bb;
    com.google.flatbuffers.MyGame.Example.Monster monster;

    @Before
    public void setUp() throws Exception {
        testData = GoogleTestData.createTestData();
        bb = ByteBuffer.wrap(testData);
        monster = com.google.flatbuffers.MyGame.Example.Monster.getRootAsMonster(bb);
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

    }

    @Test
    public void testGetMana() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        assertEquals(this.monster.mana(), Monster.getMana(bb, monster));
    }

    @Test
    public void testGetName() throws Exception {

    }

    @Test
    public void testGetInventory() throws Exception {

    }

    @Test
    public void testGetInventoryLength() throws Exception {
        int monster = Monster.getMonsterFromRoot(bb);
        assertEquals(this.monster.inventoryLength(), Monster.getInventoryLength(bb, monster));
    }

    @Test
    public void testGetInventory1() throws Exception {

    }

    @Test
    public void testGetColor() throws Exception {

    }
}