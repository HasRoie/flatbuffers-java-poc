package org.enner.flatbuffers.MyGame.Example;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public enum Color {
    RED(1, "Red"),
    GREEN(2, "Green"),
    BLUE(8, "Blue");

    public String getName() {
        return name;
    }

    public byte getValue() {
        return value;
    }

    public static Color getByValue(byte value) {
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].value == value)
                return elements[i];
        }
        return null;
    }

    private Color(int id, String name) {
        this.value = (byte) id;
        this.name = name;
    }

    private final byte value;
    private final String name;
    private final static Color[] elements = Color.values();

}
