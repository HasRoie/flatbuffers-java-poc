package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.MyGame.Test.GoogleTestData;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;
import static org.junit.Assert.*;

public class MonsterReadTest {

    byte[] testData;
    ByteBuffer bb;
    com.google.flatbuffers.MyGame.Example.Monster gMonster;
    com.google.flatbuffers.MyGame.Example.Vec3 gPos;
    Monster monster;
    Vec3 pos;

    @Before
    public void setUp() throws Exception {
        testData = GoogleTestData.createTestData();
        bb = ByteBuffer.wrap(testData);
        gMonster = com.google.flatbuffers.MyGame.Example.Monster.getRootAsMonster(bb);
        gPos = gMonster.pos();
        monster = new Monster();
        monster.setBuffer(bb);
        monster.getFromRoot();
        pos = new Vec3();
        pos.setBuffer(bb);
    }

    @Test
    public void testGetMonsterFromRoot() throws Exception {
        assertEquals(48, Monster.getMonsterFromRoot(bb));
        assertEquals(48, monster.getFromRoot().getAddress());
    }

    @Test
    public void testGetHp() throws Exception {
        assertEquals(gMonster.hp(), monster.getFromRoot().getHp());
    }

    @Test
    public void testGetPos() throws Exception {
        monster.getPos(pos);
        assertEquals(this.gPos.x(), pos.getX(), 0);
        assertEquals(this.gPos.y(), pos.getY(), 0);
        assertEquals(this.gPos.z(), pos.getZ(), 0);
    }

    @Test
    public void testGetMana() throws Exception {
        assertEquals(this.gMonster.mana(), monster.getMana());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(this.gMonster.name(), monster.getName());
    }

    @Test
    public void testGetInventory() throws Exception {
        int length = monster.getInventoryLength();
        assertEquals(gMonster.inventoryLength(), length);
        for (int i = 0; i < length; i++) {
            assertEquals(gMonster.inventory(i), monster.getInventory(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInventoryOutOfBounds() throws Exception {
        monster.getInventory(5);
    }

    @Test
    public void testGetInventoryLength() throws Exception {
        assertEquals(gMonster.inventoryLength(), monster.getInventoryLength());
    }

    @Test
    public void testGetColor() throws Exception {
        assertEquals(this.gMonster.color(), monster.getColor().getValue());
    }

    @Test
    public void testGetEnemy() throws Exception {
        assertEquals(this.gMonster.enemy() == null, monster.getEnemy(new Monster()).getAddress() == NULL);
    }

    @Test
    public void testArrayOfStrings() throws Exception {
        int length = monster.getTestArrayOfStringLength();
        assertEquals(this.gMonster.testarrayofstringLength(), length);
        for (int i = 0; i < length; i++) {
            assertEquals(this.gMonster.testarrayofstring(i), monster.getTestArrayOfString(i));
        }
    }

}