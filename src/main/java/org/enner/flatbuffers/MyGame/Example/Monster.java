package org.enner.flatbuffers.MyGame.Example;

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
        int entryOffset = 8;
        short defaultValue = 100;

        // Start of object holds the relative offset to its vtable
        // Note that the offset is subtracted, not added
        int vtableOffset = bb.getInt(monster);
        int vtableAddress = monster - vtableOffset;

        // Read the length of vtable as the first short (needed
        // for backwards compatibility in case fields got added)
        int vtableLength = bb.getShort(vtableAddress) & 0xFFFF; // unsigned

        // Find the entry address that is stored relative to the vtable (auto-gen)
        if (entryOffset < vtableLength) {

            int hpOffset = bb.getShort(vtableAddress + entryOffset) & 0xFFFF; // unsigned
            if (hpOffset == 0)
                return defaultValue;

            int hpAddress = monster + hpOffset; // offset from start of object
            short hp = bb.getShort(hpAddress);
            return hp;

        } else {
            return 0;
        }
    }

    public static int getPos(ByteBuffer bb, int monster) {
        return 0;
    }

    public static short getMana(ByteBuffer bb, int monster) {
        int entryOffset = 6;
        short defaultValue = 150;

        // Start of object holds the relative offset to its vtable
        // Note that the offset is subtracted, not added
        int vtableOffset = bb.getInt(monster);
        int vtableAddress = monster - vtableOffset;

        // Read the length of vtable as the first short (needed
        // for backwards compatibility in case fields got added)
        int vtableLength = bb.getShort(vtableAddress) & 0xFFFF; // unsigned

        // Find the entry address that is stored relative to the vtable (auto-gen)
        if (entryOffset < vtableLength) {

            int hpOffset = bb.getShort(vtableAddress + entryOffset) & 0xFFFF; // unsigned
            if (hpOffset == 0)
                return defaultValue;

            int hpAddress = monster + hpOffset; // offset from start of object
            short hp = bb.getShort(hpAddress);
            return hp;

        } else {
            return 0;
        }
    }

    public static String getName(ByteBuffer bb, int monster) {
        return null;
    }

    public static byte getInventory(ByteBuffer bb, int monster) {
        return 0;
    }

    public static int getInventoryLength(ByteBuffer bb, int monster) {
        return 0;
    }

    public static byte getInventory(ByteBuffer bb, int monster, int i) {
        return 0;
    }

    public static Color getColor(ByteBuffer bb, int monster) {
        return null;
    }

}
