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
        return Pointer.dereference(bb, bb.position());
    }

    final static int FIELD_POS = 0;
    final static int FIELD_HP = 2;
    final static int FIELD_NAME = 3;
    final static int FIELD_INVENTORY = 5;

    final static short DEFAULT_HP = 100;
    final static String DEFAULT_NAME = null;
    final static byte DEFAULT_INVENTORY = 0;

    public static short getHp(ByteBuffer bb, int monster) {
        int address = Table.getValueTypeAddress(bb, monster, FIELD_HP);
        return Utilities.getShort(bb, address, DEFAULT_HP);
    }

    public static boolean hasHp(ByteBuffer buffer, int monster) {
        return Table.hasField(buffer, monster, FIELD_HP);
    }

    public static int setHp(FlatBufferBuilder fbb, int monster, short hp) {
        int address = Table.getValueTypeAddress(fbb.getBuffer(), monster, FIELD_HP);
        if (address != NULL) {
            fbb.getBuffer().putShort(address, hp);
        } else {
            address = fbb.getNextAddress();
            fbb.getBuffer().putShort(hp);
            Table.setValueTypeAddress(fbb, monster, FIELD_HP, address);
        }
        return address;
    }

    public static int getPos(ByteBuffer bb, int monster) {
        return Table.getValueTypeAddress(bb, monster, FIELD_POS);
    }

    public static boolean hasPos(ByteBuffer bb, int monster) {
        return Table.hasField(bb, monster, FIELD_POS);
    }

    public static int getPosBuilder(FlatBufferBuilder fbb, int monster) {
        // Existing pos can be overwritten to save
        int address = Table.getValueTypeAddress(fbb.getBuffer(), monster, FIELD_POS);
        if (address == NULL) {
            // We set the pointer first in order to make sure that we don't
            // allocate a struct that is outside the 65KB range.
            address = fbb.getNextAddress();
            Table.setValueTypeAddress(fbb, monster, FIELD_POS, address);
            fbb.addStruct(Vec3.size());
        }
        return address;
    }

    public static boolean hasName(ByteBuffer bb, int monster) {
        return Table.hasField(bb, monster, FIELD_NAME);
    }

    public static String getName(ByteBuffer bb, int monster) {
        int string = Table.getReferenceTypeAddress(bb, monster, FIELD_NAME);
        return FlatString.toJavaString(bb, string, DEFAULT_NAME);
    }

    public static void initName(FlatBufferBuilder fbb, int monster) {
        Table.initReferencePointer(fbb, monster, FIELD_NAME);
    }

    public static int setName(FlatBufferBuilder fbb, int monster, int flatString) {
        return 0;
    }

    public static short getMana(ByteBuffer bb, int monster) {
        int id = 1;
        short defaultValue = 150;
        int address = Table.getValueTypeAddress(bb, monster, id);
        return Utilities.getShort(bb, address, defaultValue);
    }

    public static int getInventoryLength(ByteBuffer bb, int monster) {
        int vector = Table.getReferenceTypeAddress(bb, monster, FIELD_INVENTORY);
        return Vector.size(bb, vector);
    }

    public static byte getInventory(ByteBuffer bb, int monster, int index) {
        int vector = Table.getReferenceTypeAddress(bb, monster, FIELD_INVENTORY);
        int address = Vector.getValueTypeAddress(bb, vector, index, SIZEOF_BYTE);
        return Utilities.getByte(bb, address, DEFAULT_INVENTORY);
    }

    public static void initInventory(FlatBufferBuilder fbb, int monster) {
        Table.initReferencePointer(fbb, monster, FIELD_INVENTORY);
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
        return FlatString.toJavaString(bb, string, defaultValue);
    }

    public static int getTestArrayOfStringLength(ByteBuffer bb, int monster) {
        int id = 10;
        int vector = Table.getReferenceTypeAddress(bb, monster, id);
        return Vector.size(bb, vector);
    }

}
