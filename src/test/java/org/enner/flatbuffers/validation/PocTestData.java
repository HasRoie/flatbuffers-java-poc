package org.enner.flatbuffers.validation;

import org.enner.flatbuffers.FlatBuffers;
import org.enner.flatbuffers.Vector;
import org.enner.flatbuffers.Vector.ByteVector;
import org.enner.flatbuffers.Vector.CombinedVector;
import org.enner.flatbuffers.Vector.StructVector;
import org.enner.flatbuffers.Vector.TableVector;
import org.enner.flatbuffers.test.Color;
import org.enner.flatbuffers.test.Monster;
import org.enner.flatbuffers.test.Position;

import java.nio.ByteBuffer;

/**
 * Populates a buffer with some data created by this Api.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 11 Jan 2015
 */
public class PocTestData {

    public static byte[] createData(ByteBuffer buffer) {

        // Setup reusable objects
        buffer.clear();
        Monster rootMonster = Monster.withBuffer(buffer);
        Monster monster = Monster.withBuffer(buffer);
        Vector vector = CombinedVector.withBuffer(buffer);
        Position position = Position.withBuffer(buffer);

        // Create main monster
        rootMonster.createAtRoot() // also creates root pointer
                .setHp((short) 80) // appends hp to buffer
                .setMana((short)120) // appends mana to buffer
                .setHp((short) 30) // overwrites existing hp
                .setColor(Color.GREEN) // appends enum to buffer
                .getOrCreatePosition(position) // reserves space and returns typed struct
                .setX(1) // writes to the reserved space in struct
                .setY(2)
                .setZ(3);

        // Create inventory (vector of bytes)
        ByteVector inventory = rootMonster
                .createInventory(5) // creates an n element byte vector
                .getInventory(vector); // returns type-safe vector

        // Populate inventory
        int length = inventory.length(); // get length of vector
        for (int i = 0; i < length; i++) {
            inventory.setByte(i, (byte) i); // sets byte at index
        }

        // Add a monster enemy (referenced table)
        rootMonster
                .getOrCreateEnemy(monster) // appends a new monster to buffer
                .setColor(Color.RED)
                .setHp((short) 200)
                .getOrCreatePosition(position)
                .setAll(3, 4, 5); // sets all struct fields at once (better to avoid potential garbage)

        // Create a vector of monster friends (vector of tables)
        int numFriends = 10;
        TableVector<Monster> friends = rootMonster
                .createFriends(numFriends)
                .getFriends(vector);

        // Populate friends
        for (int i = 0; i < numFriends; i++) {

            Monster friend = monster.create() // creates a new monster at end of buffer
                    .setColor(Color.BLUE)
                    .setHp((short) (100 * i));

            // Note that we can't refer to previously
            // created monsters due to the uint32 reference
            // restriction.
            friends.setTable(i, friend); // sets reference to newly created monster

        }

        // Populate a vector of waypoints (vector of structs)
        StructVector<Position> waypoints = rootMonster
                .createWaypoints(30)
                .getWaypoints(vector);

        length = waypoints.length();
        for (int i = 0; i < length; i++) {
            waypoints.getStruct(position, i)
                    .setX(i * 2)
                    .setY(i / 2)
                    .setZ(1);
        }

        // Create and iterate a vector of vectors


        // Create and iterate a vector of strings


        // Recycle objects (maybe?)
        // rootMonster.recycle()
        // monster.recycle()
        // vector.recycle()
        // position.recycle()

        // return as byte array. Valid data is between start and the current position
        return FlatBuffers.getSizedByteArray(buffer, 0, buffer.position());

    }

}
