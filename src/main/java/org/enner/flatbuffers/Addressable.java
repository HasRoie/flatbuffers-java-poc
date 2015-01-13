package org.enner.flatbuffers;

import java.nio.ByteBuffer;

import static org.enner.flatbuffers.Utilities.*;

/**
 * Base and identifier classes for all types.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 11 Jan 2015
 */
public interface Addressable {

    public int getAddress();

    public void setAddress(int address);

    public ByteBuffer getBuffer();

    public void setBuffer(ByteBuffer buffer);

    public Type getType();

    public void initialize(ByteBuffer buffer, int address);

    public boolean isNull();

    public static enum Type {
        VALUE, // primitives / struct
        REFERENCE; // table / vector / string
    }

    public static abstract class BaseAddressable implements Addressable {

        public abstract Type getType();

        public int getAddress() {
            return this.address;
        }

        public void setAddress(int address) {
            this.address = checkNotNegative(address);
        }

        public ByteBuffer getBuffer() {
            return buffer;
        }

        public void setBuffer(ByteBuffer buffer) {
            this.buffer = checkNotNull(buffer);
        }

        public void initialize(ByteBuffer buffer, int address) {
            setBuffer(buffer);
            setAddress(address);
        }

        public boolean isNull() {
            return address == NULL;
        }

        protected ByteBuffer buffer = null;
        protected int address = 0;

    }

    public static abstract class ValueType extends BaseAddressable {
        public Type getType() {
            return Type.VALUE;
        }
    }

    public static abstract class ReferenceType extends BaseAddressable {
        public Type getType() {
            return Type.REFERENCE;
        }
    }

}
