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
        rootMonster.createAtRoot()
                .setHp((short) 80)
                .setColor(Color.GREEN)
                .getOrCreatePosition(position)
                .setX(1)
                .setY(2)
                .setZ(3);

        // Create inventory
        ByteVector inventory = rootMonster
                .createInventory(5)
                .getInventory(vector);

        // Populate inventory
        int length = inventory.length();
        for (int i = 0; i < length; i++) {
            inventory.setByte(i, (byte) i);
        }

        // Add a monster enemy
        rootMonster
                .getOrCreateEnemy(monster)
                .setColor(Color.RED)
                .setHp((short) 200)
                .getOrCreatePosition(position)
                .setAll(3, 4, 5);

        // Create a vector of monster friends (vector of tables)
        int numFriends = 10;
        TableVector<Monster> friends = rootMonster
                .createFriends(numFriends)
                .getFriends(vector);

        // Populate friends
        for (int i = 0; i < numFriends; i++) {

            Monster friend = monster.create()
                    .setColor(Color.BLUE)
                    .setHp((short) (100 * i));

            // Note that we can't refer to previously
            // created monsters due to the uint32 reference
            // restriction.
            friends.setTable(i, friend);

        }

        // Populate a vector of waypoints (vector of structs)
        StructVector<Position> waypoints = rootMonster
                .createWaypoints(30)
                .getWaypoints(vector);

        for (int i = waypoints.length(); i >= 0; i++) {
            waypoints.getStruct(position, i)
                    .setX(i * 2)
                    .setY(i / 2)
                    .setZ(1);
        }

        // Creating and iterating through a vector of vectors


        // Creating and iterating through a vector of strings

        // Recycle objects (maybe?)
        // rootMonster.recycle()
        // monster.recycle()
        // vector.recycle()
        // position.recycle()

        // return as byte array
        return FlatBuffers.getSizedByteArray(buffer, 0, buffer.position());

    }

}
