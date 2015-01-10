package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.MyGame.Test.GoogleTestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

public class MonsterTest {

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
        assertEquals(48, MonsterReadTest.getMonsterFromRoot(bb));
    }

    @Test
    public void testGetHp() throws Exception {
        int monster = MonsterReadTest.getMonsterFromRoot(bb);
        short hp = MonsterReadTest.getHp(bb, monster);
        assertEquals(this.monster.hp(), hp);
    }

    @Test
    public void testGetPos() throws Exception {

    }

    @Test
    public void testGetMana() throws Exception {

    }

    @Test
    public void testGetName() throws Exception {

    }

    @Test
    public void testGetInventory() throws Exception {

    }

    @Test
    public void testGetInventoryLength() throws Exception {

    }

    @Test
    public void testGetInventory1() throws Exception {

    }

    @Test
    public void testGetColor() throws Exception {

    }
}