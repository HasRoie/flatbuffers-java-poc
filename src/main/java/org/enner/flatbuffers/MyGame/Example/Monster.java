package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.enner.flatbuffers.Utilities.*;

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
        int address = Table.getElementAddress(bb, monster, id);
        return Utilities.getShort(bb, address, defaultValue);
    }

    public static int getPos(ByteBuffer bb, int monster) {
        int id = 0;
        return Table.getElementAddress(bb, monster, id);
    }

    public static short getMana(ByteBuffer bb, int monster) {
        int id = 1;
        short defaultValue = 150;
        int address = Table.getElementAddress(bb, monster, id);
        return Utilities.getShort(bb, address, defaultValue);
    }

    public static String getName(ByteBuffer bb, int monster) {
        int id = 3;
        String defaultValue = null;
        int pointer = Table.getElementAddress(bb, monster, id);
        int string = Pointer.dereference(bb, pointer);
        return FlatString.createString(bb, string, defaultValue);
    }

    public static int getInventoryLength(ByteBuffer bb, int monster) {
        int id = 5;
        int pointer = Table.getElementAddress(bb, monster, id);
        int vector = Pointer.dereference(bb, pointer);
        return Vector.getLength(bb, vector);
    }

    public static byte getInventory(ByteBuffer bb, int monster, int index) {
        int id = 5;
        int elementSize = 1;
        byte defaultValue = 0;
        int pointer = Table.getElementAddress(bb, monster, id);
        int vector = Pointer.dereference(bb, pointer);
        int address = Vector.getElementAddress(bb, vector, index, elementSize);
        return Utilities.getByte(bb, address, defaultValue);
    }

    public static Color getColor(ByteBuffer bb, int monster) {
        int id = 6;
        byte defaultValue = 8;
        int address = Table.getElementAddress(bb, monster, id);
        byte enumValue = Utilities.getByte(bb, address, defaultValue);
        return Color.getByValue(enumValue);
    }

    public static int getEnemy(ByteBuffer bb, int monster) {
        int id = 12;
        int pointer = Table.getElementAddress(bb, monster, id);
        return pointer == NULL ? NULL : Pointer.dereference(bb, pointer);
    }

    public static String getTestArrayOfString(ByteBuffer bb, int monster, int index) {
        int id = 10;
        String defaultValue = null;
        int vectorPointer = Table.getElementAddress(bb, monster, id);
        int vector = Pointer.dereference(bb, vectorPointer);
        int stringPointer = Vector.getElementAddress(bb, vector, index, 4);
        int address = Pointer.dereference(bb, stringPointer);
        return FlatString.createString(bb, address, defaultValue);
    }

    public static int getTestArrayOfStringLength(ByteBuffer bb, int monster) {
        int id = 10;
        int pointer = Table.getElementAddress(bb, monster, id);
        int vector = Pointer.dereference(bb, pointer);
        return Vector.getLength(bb, vector);
    }

}
