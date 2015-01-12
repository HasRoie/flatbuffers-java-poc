package org.enner.flatbuffers.MyGame.Test;

import org.enner.flatbuffers.Builders;
import org.enner.flatbuffers.MyGame.Example.Monster;
import org.enner.flatbuffers.MyGame.Example.Monster.MonsterBuilder;
import org.enner.flatbuffers.MyGame.Example.Vec3;
import org.enner.flatbuffers.Vector.ByteVectorBuilder;

import java.nio.ByteBuffer;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 11 Jan 2015
 */
public class PocTestData {

    public static byte[] createTestData2() {

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int rootAddress = Builders.getNextAddress(buffer);

        // Setup of the root object
        MonsterBuilder monster = Monster.newBuilder(buffer)
                .createRoot();

        // Initialize pointers for locality
        monster
                .initInventory()
                .initName();

        // Add value types
        monster
                .setHp((short) 40)
                .setMana((short) 120);

        // Building Structs (at once or one at a time)
        Vec3.Builder pos = monster.getPosBuilder()
                .setAll(0, 1, 2)
                .setX(0)
                .setY(1)
                .setZ(2);

        // Building Tables
        Monster.Builder enemy = monster.getEnemyBuilder()
                .setHp((short) 30);

        // Building Strings

        // Creating and iterating through a vector of primitives
        ByteVectorBuilder inventory = monster
                .createInventory(5)
                .getInventoryBuilder();

        if (monster.hasInventory()) {
            int length = inventory.length();
            for (int i = 0; i < length; i++) {
                inventory.setByte(i, (byte) i);
                byte value = inventory.getByte(i);
            }
        }

        // Creating and iterating through a vector of structs


        // Creating and iterating through a vector of tables


        // Creating and iterating through a vector of vectors


        // Creating and iterating through a vector of strings



        /*
        TableVector<Monster.Builder> enemies = monster.getEnemiesBuilder();
        length = enemies.length();
        for (int i = 0; i < length; i++) {
            Monster.Builder builder = enemies.getTableBuilder(i)
                    .setMana(30)
                    .recycle();
        }


        if (monsters.hasPositions()) {
            StructVector<Vec3.Builder> positions = monster.getPositions();
            checkState(!positions.isNull());
            Vec3.Builder position = positions
                    .getStruct(2)
                    .recycle();
        }

        StringVectorBuilder names = monster.getNames();
        for (int i = names.getLength(); i >= 0; i--) {
            names
        }

        monster.recycle();
        pos.recycle();
        inventory.recycle();
        enemy.recycle();
        enemies.recycle();

        byte[] array = FlatBuffers.sizedByteArray(rootAddress);*/
        return null;
    }

}

