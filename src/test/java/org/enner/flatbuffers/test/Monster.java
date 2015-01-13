package org.enner.flatbuffers.test;

import org.enner.flatbuffers.Addressable.Builder;
import org.enner.flatbuffers.*;
import org.enner.flatbuffers.Vector.ByteVector;
import org.enner.flatbuffers.Vector.StructVector;
import org.enner.flatbuffers.Vector.TableVector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.enner.flatbuffers.Primitives.*;
import static org.enner.flatbuffers.Utilities.*;

/**
 * Table for unit and syntax tests for the various possible combinations.
 * It's similar enough to be mostly compatible to the original example.
 * <p/>
 * This is a manually written representation of what the code generated
 * for a table could look like. It has been written with the syntax of
 * protobuf in mind.
 * <p/>
 * Primitive types
 * - Hp: short
 * - Friendly: boolean
 * - Color: enum
 * <p/>
 * Complex types
 * - Position: (Position) struct
 * - Enemy: (Monster) table
 * - Name: string
 * <p/>
 * Vectors
 * - Inventory: vector of bytes
 * - Waypoints: vector of (Position) structs
 * - Friends: vector of (Monster) tables
 * - Aliases: vector of strings
 * <p/>
 * Missing
 * - Unions
 * - (required)
 * - (deprecated)
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public final class Monster extends Table implements Builder {

    public boolean hasHp() {
        return hasField(HP_ID);
    }

    public short getHp() {
        return getShort(getBuffer(), getValueTypeAddress(HP_ID), HP_DEFAULT);
    }

    public Monster setHp(short value) {
        setShort(getBuffer(), initPrimitive(HP_ID, SIZEOF_SHORT), value);
        return this;
    }

    public boolean hasMana() {
        return hasField(MANA_ID);
    }

    public short getMana() {
        return getShort(getBuffer(), getValueTypeAddress(MANA_ID), MANA_DEFAULT);
    }

    public Monster setMana(short value) {
        setShort(getBuffer(), initPrimitive(MANA_ID, SIZEOF_SHORT), value);
        return this;
    }

    public boolean hasFriendly() {
        return hasField(FRIENDLY_ID);
    }

    public boolean isFriendly() {
        return getBoolean(getBuffer(), getValueTypeAddress(FRIENDLY_ID), FRIENDLY_DEFAULT);
    }

    public Monster setFriendly(boolean value) {
        setBoolean(getBuffer(), initPrimitive(FRIENDLY_ID, SIZEOF_BOOL), value);
        return this;
    }

    public boolean hasColor() {
        return hasField(COLOR_ID);
    }

    public Color getColor() {
        byte value = getByte(getBuffer(), getValueTypeAddress(COLOR_ID), COLOR_DEFAULT);
        return Color.getByValue(value);
    }

    public Monster setColor(Color value) {
        setByte(getBuffer(), initPrimitive(COLOR_ID, SIZEOF_SHORT), value.getValue());
        return this;
    }

    public boolean hasPosition() {
        return hasField(POSITION_ID);
    }

    public Position getPosition(Position position) {
        position.setAddress(getValueTypeAddress(POSITION_ID));
        return position;
    }

    public Position getOrCreatePosition(Position position) {
        return getAddressableBuilder(position, POSITION_ID);
    }

    public boolean hasEnemy() {
        return hasAddressable(ENEMY_ID, Type.REFERENCE);
    }

    public Monster getEnemy(Monster monster) {
        return getAddressable(monster, ENEMY_ID);
    }

    public Monster initEnemy() {
        initReferencePointer(ENEMY_ID);
        return this;
    }

    public boolean hasInventory() {
        return hasAddressable(INVENTORY_ID, Type.REFERENCE);
    }

    public ByteVector getInventory(Vector vector) {
        vector.setAddress(getReferenceTypeAddress(INVENTORY_ID));
        vector.setContainsReferences(false);
        vector.setElementSize(SIZEOF_BYTE);
        return (ByteVector) vector;
    }

    public Monster initInventory() {
        initReferencePointer(INVENTORY_ID);
        return this;
    }

    public Monster createInventory(int length) {
        int pointer = initReferencePointer(INVENTORY_ID);
        int address = Builders.addVector(getBuffer(), length, SIZEOF_BYTE, false);
        Pointers.setReference(buffer, pointer, address);
        return this;
    }

    public boolean hasWaypoints() {
        return hasAddressable(WAYPOINTS_ID, Type.REFERENCE);
    }

    @SuppressWarnings("unchecked")
    public StructVector<Position> getWaypoints(Vector vector) {
        vector.setAddress(getReferenceTypeAddress(WAYPOINTS_ID));
        vector.setContainsReferences(false);
        vector.setElementSize(Position.SIZEOF);
        return (StructVector<Position>) vector;
    }

    public Monster initWaypoints() {
        initReferencePointer(WAYPOINTS_ID);
        return this;
    }

    public Monster createWaypoints(int length) {
        int pointer = initReferencePointer(WAYPOINTS_ID);
        int address = Builders.addVector(getBuffer(), length, Position.SIZEOF, false);
        Pointers.setReference(buffer, pointer, address);
        return this;
    }

    public Monster getOrCreateEnemy(Monster enemy) {
        return getAddressableBuilder(enemy, ENEMY_ID);
    }

    public boolean hasName() {
        return hasAddressable(NAME_ID, Type.REFERENCE);
    }

    public Monster initName() {
        initReferencePointer(NAME_ID);
        return this;
    }

    public String getName() {
        return Strings.toJavaString(getBuffer(), getReferenceTypeAddress(NAME_ID), null);
    }

    public Monster setName(String name) {
        // TODO: implement string to utf-8
        return this;
    }

    public Monster getFromRoot() {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        setAddress(Pointers.dereference(buffer, buffer.position()));
        return this;
    }

    public Monster createAtRoot() {
        int pointer = Builders.addRootTable(getBuffer(), fieldCount());
        setAddress(Pointers.dereference(getBuffer(), pointer));
        return this;
    }

    public Monster create() {
        setAddress(Builders.addTable(getBuffer(), fieldCount()));
        return this;
    }

    public static Monster withBuffer(ByteBuffer buffer) {
        checkNotNull(buffer);
        Monster monster = new Monster();
        monster.setBuffer(buffer);
        monster.setAddress(NULL);
        return monster;
    }

    private void Monster() {
    }

    @Override
    public int fieldCount() {
        return 16;
    }

    private static int POSITION_ID = 0;
    private static int MANA_ID = 1;
    private static int HP_ID = 2;
    private static int NAME_ID = 3; // (required)
    private static int COLOR_ID = 6;
    private static int INVENTORY_ID = 5; // ubyte
    private static int FRIENDLY_ID = 4;
    private static int FRIENDS_ID = 11;  // was "testarrayoftables"
    private static int ALIASES_ID = 12; // was "testarrayofstring"
    private static int ENEMY_ID = 12;
    private static int WAYPOINTS_ID = 15; // not in original message

    // Only primitives need defaults
    static short HP_DEFAULT = 100;
    static short MANA_DEFAULT = 150;
    static boolean FRIENDLY_DEFAULT = true;
    static byte COLOR_DEFAULT = Color.BLUE.getValue();
    static short INVENTORY_DEFAULT = 0; // ubytes

    public boolean hasFriends() {
        return hasAddressable(FRIENDS_ID, Type.REFERENCE);
    }

    @SuppressWarnings("unchecked")
    public TableVector<Monster> getFriends(Vector vector) {
        vector.setAddress(getReferenceTypeAddress(FRIENDS_ID));
        vector.setContainsReferences(true);
        vector.setElementSize(SIZEOF_POINTER);
        return (TableVector<Monster>) vector;
    }

    public Monster initFriends() {
        initReferencePointer(FRIENDS_ID);
        return this;
    }

    public Monster createFriends(int length) {
        int pointer = initReferencePointer(FRIENDS_ID);
        int address = Builders.addVector(getBuffer(), length, SIZEOF_POINTER, true);
        Pointers.setReference(buffer, pointer, address);
        return this;
    }

}
