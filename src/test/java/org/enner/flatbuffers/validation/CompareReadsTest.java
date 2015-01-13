package org.enner.flatbuffers.validation;

import org.enner.flatbuffers.Vector;
import org.enner.flatbuffers.Vector.*;
import org.enner.flatbuffers.test.Monster;
import org.enner.flatbuffers.test.Position;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Vector.*;
import static org.junit.Assert.*;

public class CompareReadsTest {

    byte[] testData;
    ByteBuffer bb;
    com.google.flatbuffers.MyGame.Example.Monster gMonster;
    com.google.flatbuffers.MyGame.Example.Vec3 gPos;
    Monster monster;
    Position pos;
    Vector vector;

    @Before
    public void setUp() throws Exception {
        testData = GoogleTestData.createData();
        bb = ByteBuffer.wrap(testData).asReadOnlyBuffer();
        gMonster = com.google.flatbuffers.MyGame.Example.Monster.getRootAsMonster(bb);
        gPos = gMonster.pos();
        monster = Monster.withBuffer(bb).getFromRoot();
        pos = Position.withBuffer(bb);
        vector = CombinedVector.withBuffer(bb);
    }

    @Test
    public void testGetMonsterFromRoot() throws Exception {
        assertEquals(48, monster.getFromRoot().getAddress());
    }

    @Test
    public void testGetHp() throws Exception {
        assertEquals(gMonster.hp(), monster.getFromRoot().getHp());
    }

    @Test
    public void testGetPos() throws Exception {
        monster.getPosition(pos);
        assertEquals(gPos.x(), pos.getX(), 0);
        assertEquals(gPos.y(), pos.getY(), 0);
        assertEquals(gPos.z(), pos.getZ(), 0);
    }

    @Test
    public void testGetMana() throws Exception {
        assertEquals(gMonster.mana(), monster.getMana());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals(gMonster.name(), monster.getName());
    }

    @Test
    public void testGetInventory() throws Exception {
        ByteVector inventory = monster.getInventory(vector);
        int length = inventory.length();
        assertEquals(gMonster.inventoryLength(), length);
        for (int i = 0; i < length; i++) {
            assertEquals(gMonster.inventory(i), inventory.getByte(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetInventoryOutOfBounds() throws Exception {
        monster.getInventory(vector).getByte(5);
    }

    @Test
    public void testGetInventoryLength() throws Exception {
        assertEquals(gMonster.inventoryLength(), monster.getInventory(vector).length());
    }

    @Test
    public void testGetColor() throws Exception {
        assertEquals(gMonster.color(), monster.getColor().getValue());
    }

    @Test
    public void testGetEnemy() throws Exception {
        assertEquals(gMonster.enemy() == null, monster.getEnemy(new Monster()).isNull());
    }

    @Test
    public void testArrayOfStrings() throws Exception {
       /* int length = monster.getTestArrayOfStringLength();
        assertEquals(gMonster.testarrayofstringLength(), length);
        for (int i = 0; i < length; i++) {
            assertEquals(gMonster.testarrayofstring(i), monster.getTestArrayOfString(i));
        }*/
    }

}