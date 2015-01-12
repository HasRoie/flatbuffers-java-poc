package org.enner.flatbuffers.test;

import org.enner.flatbuffers.Vector;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;
import static org.enner.flatbuffers.Vector.*;
import static org.junit.Assert.*;

public class ReadWriteTest {

    ByteBuffer buffer;
    Monster monster;

    @Before
    public void setUp() throws Exception {
        buffer = ByteBuffer.allocate(1024);
        monster = Monster.withBuffer(buffer)
                .createAtRoot();
    }

    @Test
    public void testCreateMonsterInRoot() throws Exception {
        Monster monster = Monster.withBuffer(buffer);
        assertTrue(monster.isNull());
        monster.createAtRoot();
        assertFalse(monster.isNull());
    }

    @Test
    public void testHpInt16() throws Exception {
        assertFalse(monster.hasHp());
        assertEquals(Monster.HP_DEFAULT, monster.getHp());
        monster.setHp((short) 12);
        assertEquals(12, monster.getHp());
    }

    @Test
    public void testFriendlyBoolean() throws Exception {
        assertFalse(monster.hasFriendly());
        assertEquals(Monster.FRIENDLY_DEFAULT, monster.isFriendly());
        monster.setFriendly(false);
        assertEquals(false, monster.isFriendly());
    }

    @Test
    public void testColorEnum() throws Exception {
        assertFalse(monster.hasColor());
        assertEquals(Monster.COLOR_DEFAULT, monster.getColor().getValue());
        monster.setColor(Color.GREEN);
        assertEquals(Color.GREEN, monster.getColor());
    }

    @Test
    public void testPositionStruct() throws Exception {
        assertFalse(monster.hasPosition());
        Position position = monster.getPosition(Position.withBuffer(buffer));
        assertTrue(position.isNull());
        assertFalse(monster.getOrCreatePosition(position).isNull());
        position.setAll(1, 2, 3);
        assertEquals(1, position.getX(), 0);
        assertEquals(2, position.getY(), 0);
        assertEquals(3, position.getZ(), 0);
        assertTrue(monster.hasPosition());
    }

    @Test
    public void testEnemyTable() throws Exception {
        assertFalse(monster.hasEnemy());
        Monster enemy = Monster.withBuffer(buffer);
        assertTrue(monster.getEnemy(enemy).isNull());
        assertTrue(monster.initEnemy().getEnemy(enemy).isNull());
        assertFalse(monster.hasEnemy());
        assertFalse(monster.getOrCreateEnemy(enemy).isNull());
        assertTrue(monster.hasEnemy());
    }

    @Test
    public void testInventoryVectorOfPrimitives() throws Exception {
        assertFalse(monster.hasInventory());
        Vector vector = CombinedVector.newVector(buffer, 0, 0, true);
        assertTrue(monster.getInventory(vector).isNull());
        assertTrue(monster.initInventory().getInventory(vector).isNull());
        assertFalse(monster.hasInventory());
        assertFalse(monster.createInventory(5).getInventory(vector).isNull());
        assertEquals(5, vector.length());
        assertEquals(6, monster.createInventory(6).getInventory(vector).length());
        assertTrue(monster.hasInventory());
    }

    @Test
    public void testWaypointsVectorOfStructs() throws Exception {
        assertFalse(monster.hasWaypoints());
        StructVector<Position> vector = CombinedVector.newVector(buffer, 0, 0, true);
        assertTrue(monster.getWaypoints(vector).isNull());
        assertTrue(monster.initWaypoints().getWaypoints(vector).isNull());
        assertFalse(monster.hasWaypoints());
        assertFalse(monster.createWaypoints(5).getWaypoints(vector).isNull());
        assertEquals(5, vector.length());
        assertEquals(6, monster.createWaypoints(6).getWaypoints(vector).length());
        assertTrue(monster.hasWaypoints());

        // Access structs
        Position position = Position.withBuffer(buffer);
        assertFalse(monster.getWaypoints(vector)
                .getStruct(position, 0)
                .isNull());

        // Check if the offset is correct
        assertEquals(4 * Position.SIZEOF + SIZEOF_VECTOR_TABLE_HEADER,
                vector.getStruct(position, 4).getAddress() - vector.getAddress());
    }

    @Test
    public void testFriendsVectorOfTables() throws Exception {
        assertFalse(monster.hasFriends());
        TableVector<Monster> vector = CombinedVector.newVector(buffer, 0, 0, true);
        assertTrue(monster.getFriends(vector).isNull());
        assertTrue(monster.initFriends().getFriends(vector).isNull());
        assertFalse(monster.hasFriends());
        assertFalse(monster.createFriends(5).getFriends(vector).isNull());
        assertEquals(5, vector.length());
        assertEquals(6, monster.createFriends(6).getFriends(vector).length());
        assertTrue(monster.hasFriends());

        Monster friend = Monster.withBuffer(buffer);
        int length = vector.length();
        for (int i = 0; i < length; i++) {
            // Make sure everything has initialized to zero
            assertTrue(vector.getTable(friend, i).isNull());

            // Create elements
            vector.setTable(i, friend.create());

            // Populate elements
            short hp = vector.getTable(friend, i)
                    .setHp((short) i)
                    .getHp();
            assertEquals(i, hp);
        }

    }

}