package org.enner.flatbuffers;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import static org.enner.flatbuffers.Utilities.*;

/**
 * Strings are encoded as a vector of UTF-8 bytes. Null termination
 * is omitted.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public final class Strings {

    /**
     * Note: Edited from original Table.__string(int)
     * <p/>
     * Create a java String from UTF-8 data stored inside the flatbuffer.
     * This allocates a new string and converts to wide chars upon each access,
     * which is not very efficient.
     */
    public static String toJavaString(ByteBuffer buffer, int address, String defaultValue) {
        if (address == NULL)
            return defaultValue;

        int start = Vectors.getFirstElementAddress(address);
        int length = Vectors.getLength(buffer, address);

        if (buffer.hasArray()) {
            return new String(buffer.array(), start, length, UTF_8);
        } else {
            // We can't access .array(), since the ByteBuffer is read-only.
            // We're forced to make an extra copy:
            byte[] copy = new byte[length];
            int position = buffer.position();
            buffer.position(start);
            buffer.get(copy);
            buffer.position(position);
            return new String(copy, 0, copy.length, UTF_8);
        }
    }

    public static final Charset UTF_8 = Charset.forName("UTF-8");

}
