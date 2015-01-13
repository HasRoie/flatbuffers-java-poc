package org.enner.flatbuffers;

import org.enner.flatbuffers.Addressable.ReferenceType;

import static org.enner.flatbuffers.Utilities.*;

/**
 * Base class for all Tables. For now it contains convenience
 * wrappers for both setters and getters due to single inheritance.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 11 Jan 2015
 */
public abstract class Table extends ReferenceType {

    /**
     * @return number of fields in the vector table
     */
    public abstract int fieldCount();

    protected boolean hasField(int fieldId) {
        return Tables.hasField(getBuffer(), getAddress(), fieldId);
    }

    protected boolean hasAddressable(int fieldId, Type type) {
        switch (type) {
            case VALUE:
                return hasField(fieldId);
            case REFERENCE:
                return getReferenceTypeAddress(fieldId) != NULL;
        }
        return false;
    }

    /**
     * @return sets the object to the found address. returns null if field is not found.
     * You should call hasAddressable(fieldId, type) if you are not sure whether the
     * field is populated.
     */
    protected <T extends Addressable> T getAddressable(T addressable, int fieldId) {
        int address = NULL;
        switch (addressable.getType()) {
            case VALUE:
                address = getValueTypeAddress(fieldId);
                break;
            case REFERENCE:
                address = getReferenceTypeAddress(fieldId);
                break;
        }
        addressable.setAddress(address);
        return addressable;
    }

    protected int getValueTypeAddress(int fieldId) {
        return Tables.getValueTypeAddress(getBuffer(), getAddress(), fieldId);
    }

    protected int getReferenceTypeAddress(int fieldId) {
        return Tables.getReferenceTypeAddress(getBuffer(), getAddress(), fieldId);
    }

    protected int getPayloadSize() {
        return Tables.getPayloadSize(getBuffer(), getAddress());
    }

    protected int initReferencePointer(int fieldId) {
        return Tables.initReferencePointer(getBuffer(), getAddress(), fieldId);
    }

    protected int initPrimitive(int fieldId, int size) {
        return Tables.initValueType(getBuffer(), getAddress(), fieldId, size, false);
    }

    protected int getVectorLength(int fieldId) {
        int vector = getReferenceTypeAddress(fieldId);
        return Vectors.getLength(getBuffer(), vector);
    }

    protected int getVectorElementAddress(int fieldId, int index, int elementSize) {
        int vector = getReferenceTypeAddress(fieldId);
        return Vectors.getValueTypeAddress(getBuffer(), vector, index, elementSize);
    }

    /**
     * @return A builder to an existing location, or to a newly allocated one
     * if none exists
     */
    protected <T extends Addressable> T getOrCreateAddressable(T builder, int fieldId) {

        if (builder instanceof Struct) {
            int address = getValueTypeAddress(fieldId);
            if (address == NULL) {
                int size = ((Struct) builder).size();
                address = Tables.initValueType(getBuffer(), getAddress(), fieldId, size, false);
            }
            builder.setAddress(address);
            return builder;

        } else if (builder instanceof Table) {

            // Find table
            int pointer = initReferencePointer(fieldId);
            int address = Pointers.dereference(getBuffer(), pointer);

            // Build new table if none exists
            if (address == NULL) {
                int numFields = ((Table) builder).fieldCount();
                address = Builders.addTable(getBuffer(), numFields);
                Pointers.setReference(getBuffer(), pointer, address);
            }

            builder.setAddress(address);
            return builder;
        }

        throw new IllegalArgumentException("Unknown addressable type.");
    }


}
