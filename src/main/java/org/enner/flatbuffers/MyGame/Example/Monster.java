package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.FlatString;
import org.enner.flatbuffers.Table;
import org.enner.flatbuffers.Utilities;
import org.enner.flatbuffers.Vector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Monster {

    public static int getMonsterFromRoot(ByteBuffer bb) {
        // First int is a pointer to the start of the object
        bb.order(ByteOrder.LITTLE_ENDIAN);
        int position = bb.position();
        int id = bb.getInt(position);
        return position + id;
    }

    public static short getHp(ByteBuffer bb, int monster) {
        int id = 2;
        short defaultValue = 100;
        int address = Table.getValueTypeAddress(bb, monster, id);
        return Utilities.getShort(bb, address, defaultValue);
    }

    public static int getPos(ByteBuffer bb, int monster) {
        int id = 0;
        return Table.getValueTypeAddress(bb, monster, id);
    }

    public static short getMana(ByteBuffer bb, int monster) {
        int id = 1;
        short defaultValue = 150;
        int address = Table.getValueTypeAddress(bb, monster, id);
        return Utilities.getShort(bb, address, defaultValue);
    }

    public static String getName(ByteBuffer bb, int monster) {
        int id = 3;
        String defaultValue = null;
        int string = Table.getReferenceTypeAddress(bb, monster, id);
        return FlatString.createString(bb, string, defaultValue);
    }

    public static int getInventoryLength(ByteBuffer bb, int monster) {
        int id = 5;
        int vector = Table.getReferenceTypeAddress(bb, monster, id);
        return Vector.getLength(bb, vector);
    }

    public static byte getInventory(ByteBuffer bb, int monster, int index) {
        int id = 5;
        int elementSize = 1;
        byte defaultValue = 0;
        int vector = Table.getReferenceTypeAddress(bb, monster, id);
        int address = Vector.getValueTypeAddress(bb, vector, index, elementSize);
        return Utilities.getByte(bb, address, defaultValue);
    }

    public static Color getColor(ByteBuffer bb, int monster) {
        int id = 6;
        byte defaultValue = 8;
        int address = Table.getValueTypeAddress(bb, monster, id);
        byte enumValue = Utilities.getByte(bb, address, defaultValue);
        return Color.getByValue(enumValue);
    }

    public static int getEnemy(ByteBuffer bb, int monster) {
        int id = 12;
        return Table.getReferenceTypeAddress(bb, monster, id);
    }

    public static String getTestArrayOfString(ByteBuffer bb, int monster, int index) {
        int id = 10;
        String defaultValue = null;
        int vector = Table.getReferenceTypeAddress(bb, monster, id);
        int string = Vector.getReferenceTypeAddress(bb, vector, index);
        return FlatString.createString(bb, string, defaultValue);
    }

    public static int getTestArrayOfStringLength(ByteBuffer bb, int monster) {
        int id = 10;
        int vector = Table.getReferenceTypeAddress(bb, monster, id);
        return Vector.getLength(bb, vector);
    }

}
