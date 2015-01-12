package org.enner.flatbuffers.MyGame.Example;

import org.enner.flatbuffers.*;
import org.enner.flatbuffers.MyGame.Example.Vec3.Vec3Builder;
import org.enner.flatbuffers.Vector.ByteVector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.enner.flatbuffers.Utilities.*;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class Monster extends Table implements Addressable {

    private Color color;

    public static int getMonsterFromRoot(ByteBuffer bb) {
        // First int is a pointer to the start of the object
        bb.order(ByteOrder.LITTLE_ENDIAN);
        return Pointers.dereference(bb, bb.position());
    }

    public static MonsterBuilder newBuilder(ByteBuffer buffer) {
        return new MonsterBuilder(buffer);
    }

    public Monster getFromRoot() {
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        setAddress(Pointers.dereference(buffer, buffer.position()));
        return this;
    }

    public boolean hasHp() {
        return hasField(FIELD_HP);
    }

    public short getHp() {
        return Primitives.getShort(getBuffer(), getValueTypeAddress(FIELD_HP), DEFAULT_HP);
    }

    public boolean hasMana() {
        return hasField(FIELD_MANA);
    }

    public short getMana() {
        return Primitives.getShort(getBuffer(), getValueTypeAddress(FIELD_MANA), DEFAULT_MANA);
    }

    public boolean hasPos() {
        return hasAddressable(FIELD_POS, Type.VALUE);
    }

    public <T extends Vec3> T getPos(T vec3) {
        return getAddressable(vec3, FIELD_POS);
    }

    public boolean hasName() {
        return this.hasAddressable(FIELD_NAME, Type.REFERENCE);
    }

    public String getName() {
        return Strings.toJavaString(getBuffer(), getReferenceTypeAddress(FIELD_NAME), DEFAULT_NAME);
    }

    public boolean hasInventory() {
        return hasAddressable(FIELD_INVENTORY, Type.REFERENCE);
    }

    public ByteVector getInventory() {
        int address = getReferenceTypeAddress(FIELD_INVENTORY);
        return Vector.CombinedVector.newVector(getBuffer(), address, SIZEOF_BYTE, false);
    }

    public boolean hasEnemy() {
        return this.hasAddressable(FIELD_NAME, Type.REFERENCE);
    }

    public <T extends Monster> T getEnemy(T monster) {
        return getAddressable(monster, FIELD_ENEMY);
    }

    public boolean hasTestArrayOfString() {
        return hasAddressable(FIELD_TEST_ARRAY_OF_STRING, Type.REFERENCE);
    }

    public int getTestArrayOfStringLength() {
        return getVectorLength(FIELD_TEST_ARRAY_OF_STRING);
    }

    public String getTestArrayOfString(int index) {
        int pointer = getVectorElementAddress(FIELD_TEST_ARRAY_OF_STRING, index, SIZEOF_BYTE);
        int address = Pointers.dereference(getBuffer(), pointer);
        return Strings.toJavaString(getBuffer(), address, DEFAULT_TEST_ARRAY_OF_STRING);
    }

    public Color getColor() {
        byte value = Primitives.getByte(getBuffer(), getValueTypeAddress(FIELD_COLOR), DEFAULT_COLOR);
        return Color.getByValue(value);
    }

    public static class MonsterBuilder extends Monster implements Builder {

        private MonsterBuilder(ByteBuffer buffer) {
            this.buffer = buffer;
        }

        public MonsterBuilder createRoot() {
            setAddress(Builders.addTable(getBuffer(), fieldCount()));
            return this;
        }

        public MonsterBuilder setHp(short value) {
            Primitives.setShort(getBuffer(), initValueType(FIELD_HP, SIZEOF_SHORT), value);
            return this;
        }

        public MonsterBuilder setMana(short value) {
            Primitives.setShort(getBuffer(), initValueType(FIELD_MANA, SIZEOF_SHORT), value);
            return this;
        }

        public MonsterBuilder initPos() {
            initReferencePointer(FIELD_POS);
            return this;
        }

        public Vec3Builder getPosBuilder() {
            return getPosBuilder(new Vec3Builder());
        }

        public Vec3Builder getPosBuilder(Vec3Builder builder) {
            return this.getAddressableBuilder(builder, FIELD_POS);
        }

        public MonsterBuilder initName() {
            initReferencePointer(FIELD_NAME);
            return this;
        }

        public MonsterBuilder initInventory() {
            initReferencePointer(FIELD_INVENTORY);
            return this;
        }

        public MonsterBuilder createInventory(int length) {
            int pointer = initReferencePointer(FIELD_INVENTORY);
            int address = Builders.addVector(getBuffer(), length, SIZEOF_BYTE, false);
            Pointers.setReference(buffer, pointer, address);
            return this;
        }

        public MonsterBuilder setInventoryAddress(int address) {
            Tables.setReferenceTypeAddress(getBuffer(), getAddress(), FIELD_INVENTORY, address);
            return this;
        }

        public MonsterBuilder setName(String value) {
            // TODO: implement string to utf-8
            return this;
        }

        public MonsterBuilder initEnemy() {
            initReferencePointer(FIELD_ENEMY);
            return this;
        }

        public MonsterBuilder getEnemyBuilder() {
            return getEnemyBuilder(Monster.newBuilder(getBuffer()));
        }

        public MonsterBuilder getEnemyBuilder(MonsterBuilder builder) {
            return this.getAddressableBuilder(builder, FIELD_POS);
        }

        public MonsterBuilder initTestArrayOfString() {
            initReferencePointer(FIELD_TEST_ARRAY_OF_STRING);
            return this;
        }

        public Vector.ByteVectorBuilder getInventoryBuilder() {
            int address = getReferenceTypeAddress(FIELD_INVENTORY);
            return Vector.CombinedVector.newVector(getBuffer(), address, SIZEOF_BYTE, false);
        }
    }

    final static int FIELD_POS = 0;
    final static int FIELD_MANA = 1;
    final static int FIELD_HP = 2;
    final static int FIELD_NAME = 3;
    final static int FIELD_INVENTORY = 5;
    final static int FIELD_ENEMY = 12;
    final static int FIELD_TEST_ARRAY_OF_STRING = 12;
    final static int FIELD_COLOR = 6;

    final static short DEFAULT_HP = 100;
    final static short DEFAULT_MANA = 150;
    final static String DEFAULT_NAME = null;
    final static String DEFAULT_TEST_ARRAY_OF_STRING = null;
    final static byte DEFAULT_INVENTORY = 0;
    final static byte DEFAULT_ENEMY = 0;
    final static byte DEFAULT_COLOR = 8;

    @Override
    public int fieldCount() {
        return 15;
    }

}
