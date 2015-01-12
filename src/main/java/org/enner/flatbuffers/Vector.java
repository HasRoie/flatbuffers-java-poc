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
    }

    public static interface ByteVectorBuilder extends ByteVector {
        ByteVectorBuilder setByte(int index, byte value);
    }

    public static class CombinedVector extends ReferenceType implements ByteVectorBuilder {

        public static CombinedVector newVector(ByteBuffer buffer, int address, int elementSize, boolean containsReferences) {
            CombinedVector vector = new CombinedVector();
            vector.setBuffer(buffer);
            vector.setAddress(address);
            vector.setContainsReferences(containsReferences);
            vector.setElementSize(elementSize);
            return vector;
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
        public ByteVectorBuilder setByte(int index, byte value) {
            Primitives.setByte(getBuffer(), getValueTypeAddress(index), value);
            return this;
        }
    }

}
