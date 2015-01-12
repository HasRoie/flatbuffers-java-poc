package org.enner.flatbuffers;

import org.enner.flatbuffers.Addressable.ValueType;

/**
 * Base class for all structs
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 11 Jan 2015
 */
public abstract class Struct extends ValueType {

    /**
     * @return size in bytes
     */
    public abstract int size();

}
