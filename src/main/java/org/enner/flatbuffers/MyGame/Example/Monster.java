package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.Table;

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
        int offset = bb.getInt(position);
        return position + offset;
    }

    public static short getHp(ByteBuffer bb, int monster) {
        int offset = 8;
        short defaultValue = 100;
        return Table.getShortValue(bb, monster, offset, defaultValue);
    }

    public static int getPos(ByteBuffer bb, int monster) {
        return 0;
    }

    public static short getMana(ByteBuffer bb, int monster) {
        int offset = 6;
        short defaultValue = 150;
        return Table.getShortValue(bb, monster, offset, defaultValue);
    }

    public static String getName(ByteBuffer bb, int monster) {
        return null;
    }

    public static byte getInventory(ByteBuffer bb, int monster) {
        return 0;
    }

    public static int getInventoryLength(ByteBuffer bb, int monster) {
        int offset = 14;
        return Table.getVectorLength(bb, monster, offset);
    }

    public static byte getInventory(ByteBuffer bb, int monster, int i) {
        return 0;
    }

    public static Color getColor(ByteBuffer bb, int monster) {
        return null;
    }

}
