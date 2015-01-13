package org.enner.flatbuffers.test;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.*;

/**
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class VectorTest {

    ByteBuffer buffer;
    Monster monster;

    @Before
    public void setUp() throws Exception {
        buffer = ByteBuffer.allocate(1024);
        monster = Monster.withBuffer(buffer)
                .createAtRoot();
    }

    @Test
    public void testHpInt16() throws Exception {
        assertFalse(monster.hasHp());
        assertEquals(Monster.HP_DEFAULT, monster.getHp());
        monster.setHp((short) 12);
        assertEquals(12, monster.getHp());
    }

}
