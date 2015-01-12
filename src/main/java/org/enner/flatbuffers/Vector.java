package org.enner.flatbuffers;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

/**
 * Generic implementation for FlatBuffer's vector. It has
 * similar behavior to the C++ vector, which is more like
 * an array in Java. Once constructed, it is not resizable.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 12 Jan 2015
 */
public interface Vector extends Addressable {

    public int length();

    /**
     * @return size in bytes
     */
    public int size();

    public int getElementSize();

    public void setElementSize(int elementSize);

    public void setContainsReferences(boolean containsReferences);

    public static interface ByteVector extends Vector {
        byte getByte(int index);

        ByteVector setByte(int index, byte value);
    }

    public static interface StructVector<T extends Addressable> extends Vector {
        public T getStruct(T struct, int index);
    }

    public static interface TableVector<T extends Addressable> extends Vector {
        public T getTable(T table, int index);

        public TableVector<T> setTable(int index, T table);
    }

    public static interface VectorVector<T extends Addressable> extends Vector {
        public T getVector(T vector, int index);

        public VectorVector<T> setVector(int index, T vector);
    }

    public static class CombinedVector<T extends Addressable> extends ReferenceType implements ByteVector, StructVector<T>, TableVector<T>, VectorVector<T> {

        @SuppressWarnings("unchecked")
        public static <V extends Addressable> CombinedVector<V> newVector(ByteBuffer buffer, int address, int elementSize, boolean containsReferences) {
            CombinedVector vector = new CombinedVector();
            vector.setBuffer(buffer);
            vector.setAddress(address);
            vector.setContainsReferences(containsReferences);
            vector.setElementSize(elementSize);
            return (CombinedVector<V>) vector;
        }

        @SuppressWarnings("unchecked")
        public static <V extends Addressable> CombinedVector<V> withBuffer(ByteBuffer buffer) {
            CombinedVector vector = new CombinedVector();
            vector.setBuffer(buffer);
            vector.setAddress(0);
            vector.setContainsReferences(false);
            vector.setElementSize(0);
            return (CombinedVector<V>) vector;
        }

        /**
         * @return number of elements
         */
        public int length() {
            return Vectors.getLength(getBuffer(), getAddress());
        }

        /**
         * @return size in bytes
         */
        public int size() {
            return length() * getElementSize();
        }

        public int getElementSize() {
            return elementSize;
        }

        public void setElementSize(int elementSize) {
            this.elementSize = elementSize;
        }

        public void setContainsReferences(boolean containsReferences) {
            this.containsReferences = containsReferences;
        }

        protected boolean hasReference(int index) {
            checkState(containsReferences, "Vector contains values, not references");
            return getReferenceTypeAddress(index) != NULL;
        }

        protected int getReferenceTypeAddress(int index) {
            return Vectors.getReferenceTypeAddress(getBuffer(), getAddress(), index);
        }

        protected int getValueTypeAddress(int index) {
            return Vectors.getValueTypeAddress(getBuffer(), getAddress(), index, getElementSize());
        }

        protected boolean containsReferences;
        protected int elementSize;

        @Override
        public byte getByte(int index) {
            return Primitives.getByte(getBuffer(), getValueTypeAddress(index), (byte) 0);
        }

        @Override
        public ByteVector setByte(int index, byte value) {
            Primitives.setByte(getBuffer(), getValueTypeAddress(index), value);
            return this;
        }

        @Override
        public T getStruct(T struct, int index) {
            struct.setAddress(getValueTypeAddress(index));
            return struct;
        }

        @Override
        public T getTable(T table, int index) {
            table.setAddress(getReferenceTypeAddress(index));
            return table;
        }

        @Override
        public TableVector<T> setTable(int index, T table) {
            int pointer = getValueTypeAddress(index);
            Pointers.setReference(getBuffer(), pointer, table.getAddress());
            return this;
        }

        @Override
        public T getVector(T vector, int index) {
            vector.setAddress(getReferenceTypeAddress(index));
            return vector;
        }

        @Override
        public VectorVector<T> setVector(int index, T vector) {
            int pointer = getValueTypeAddress(index);
            Pointers.setReference(getBuffer(), pointer, vector.getAddress());
            return this;
        }

    }

}
