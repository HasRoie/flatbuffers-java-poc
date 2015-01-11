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
        return Pointers.dereference(bb, bb.position());
    }

    final static int FIELD_POS = 0;
    final static int FIELD_HP = 2;
    final static int FIELD_NAME = 3;
    final static int FIELD_INVENTORY = 5;

    final static short DEFAULT_HP = 100;
    final static String DEFAULT_NAME = null;
    final static byte DEFAULT_INVENTORY = 0;

    public static short getHp(ByteBuffer bb, int monster) {
        int address = Tables.getValueTypeAddress(bb, monster, FIELD_HP);
        return Primitives.getShort(bb, address, DEFAULT_HP);
    }

    public static boolean hasHp(ByteBuffer buffer, int monster) {
        return Tables.hasField(buffer, monster, FIELD_HP);
    }

    public static int setHp(FlatBufferBuilder fbb, int monster, short hp) {
        int address = Tables.getValueTypeAddress(fbb.getBuffer(), monster, FIELD_HP);
        if (address != NULL) {
            fbb.getBuffer().putShort(address, hp);
        } else {
            address = fbb.getNextAddress();
            fbb.getBuffer().putShort(hp);
            Tables.setValueTypeAddress(fbb.getBuffer(), monster, FIELD_HP, address);
        }
        return address;
    }

    public static int getPos(ByteBuffer bb, int monster) {
        return Tables.getValueTypeAddress(bb, monster, FIELD_POS);
    }

    public static boolean hasPos(ByteBuffer bb, int monster) {
        return Tables.hasField(bb, monster, FIELD_POS);
    }

    public static int getPosBuilder(FlatBufferBuilder fbb, int monster) {
        // Existing pos can be overwritten to save
        int address = Tables.getValueTypeAddress(fbb.getBuffer(), monster, FIELD_POS);
        if (address == NULL) {
            // We set the pointer first in order to make sure that we don't
            // allocate a struct that is outside the 65KB range.
            address = fbb.getNextAddress();
            Tables.setValueTypeAddress(fbb.getBuffer(), monster, FIELD_POS, address);
            fbb.addStruct(Vec3.size());
        }
        return address;
    }

    public static boolean hasName(ByteBuffer bb, int monster) {
        return Tables.hasField(bb, monster, FIELD_NAME);
    }

    public static String getName(ByteBuffer bb, int monster) {
        int string = Tables.getReferenceTypeAddress(bb, monster, FIELD_NAME);
        return Strings.toJavaString(bb, string, DEFAULT_NAME);
    }

    public static void initName(FlatBufferBuilder fbb, int monster) {
        Tables.initReferencePointer(fbb.getBuffer(), monster, FIELD_NAME);
    }

    public static int setName(FlatBufferBuilder fbb, int monster, int flatString) {
        return 0;
    }

    public static short getMana(ByteBuffer bb, int monster) {
        int id = 1;
        short defaultValue = 150;
        int address = Tables.getValueTypeAddress(bb, monster, id);
        return Primitives.getShort(bb, address, defaultValue);
    }

    public static int getInventoryLength(ByteBuffer bb, int monster) {
        int vector = Tables.getReferenceTypeAddress(bb, monster, FIELD_INVENTORY);
        return Vectors.size(bb, vector);
    }

    public static byte getInventory(ByteBuffer bb, int monster, int index) {
        int vector = Tables.getReferenceTypeAddress(bb, monster, FIELD_INVENTORY);
        int address = Vectors.getValueTypeAddress(bb, vector, index, SIZEOF_BYTE);
        return Primitives.getByte(bb, address, DEFAULT_INVENTORY);
    }

    public static int getInventoryAddress(ByteBuffer buffer, int monster) {
        return Tables.getReferenceTypeAddress(buffer, monster, FIELD_INVENTORY);
    }

    public static void initInventory(FlatBufferBuilder fbb, int monster) {
        Tables.initReferencePointer(fbb.getBuffer(), monster, FIELD_INVENTORY);
    }

    public static void setInventoryAddress(FlatBufferBuilder fbb, int monster, int inventory) {
        Tables.setReferenceTypeAddress(fbb.getBuffer(), monster, FIELD_INVENTORY, inventory);
    }

    public static Color getColor(ByteBuffer bb, int monster) {
        int id = 6;
        byte defaultValue = 8;
        int address = Tables.getValueTypeAddress(bb, monster, id);
        byte enumValue = Primitives.getByte(bb, address, defaultValue);
        return Color.getByValue(enumValue);
    }

    public static int getEnemy(ByteBuffer bb, int monster) {
        int id = 12;
        return Tables.getReferenceTypeAddress(bb, monster, id);
    }

    public static String getTestArrayOfString(ByteBuffer bb, int monster, int index) {
        int id = 10;
        String defaultValue = null;
        int vector = Tables.getReferenceTypeAddress(bb, monster, id);
        int string = Vectors.getReferenceTypeAddress(bb, vector, index);
        return Strings.toJavaString(bb, string, defaultValue);
    }

    public static int getTestArrayOfStringLength(ByteBuffer bb, int monster) {
        int id = 10;
        int vector = Tables.getReferenceTypeAddress(bb, monster, id);
        return Vectors.size(bb, vector);
    }

}
