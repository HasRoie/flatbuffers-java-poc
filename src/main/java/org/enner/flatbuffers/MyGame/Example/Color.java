package org.enner.flatbuffers.MyGame.Example;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public enum Color {
    RED(1, "Red"),
    GREEN(2, "Green"),
    BLUE(8, "Blue");

    Color(int id, String name) {
        this.id = (byte) id;
        this.name = name;
    }

    final byte id;
    final String name;

}
