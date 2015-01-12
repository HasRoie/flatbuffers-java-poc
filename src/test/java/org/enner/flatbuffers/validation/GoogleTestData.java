package org.enner.flatbuffers.validation;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.MyGame.Example.*;

import java.nio.ByteBuffer;

/**
 * Populates a buffer with some data created by the Google Java
 * Api.
 *
 * @author Florian Enner < florian @ hebirobotics.com >
 * @since 10 Jan 2015
 */
public class GoogleTestData {

    public static byte[] createDataSimple(ByteBuffer buffer) {

        FlatBufferBuilder fbb = new FlatBufferBuilder(buffer);
        int str = fbb.createString("MyMonster");

        int inv = Monster.createInventoryVector(fbb, new byte[]{0, 1, 2, 3, 4});

        int fred = fbb.createString("Fred");
        Monster.startMonster(fbb);
        Monster.addName(fbb, fred);
        int mon2 = Monster.endMonster(fbb);

        return fbb.sizedByteArray();
    }

    /**
     * Official example, but this Api doesn't support everything yet
     *
     * @return
     */
    public static byte[] createData() {

        FlatBufferBuilder fbb = new FlatBufferBuilder(1);

        // We set up the same values as monsterdata.json:

        int str = fbb.createString("MyMonster");

        int inv = Monster.createInventoryVector(fbb, new byte[]{0, 1, 2, 3, 4});

        int fred = fbb.createString("Fred");
        Monster.startMonster(fbb);
        Monster.addName(fbb, fred);
        int mon2 = Monster.endMonster(fbb);

        Monster.startTest4Vector(fbb, 2);
        Test.createTest(fbb, (short) 10, (byte) 20);
        Test.createTest(fbb, (short) 30, (byte) 40);
        int test4 = fbb.endVector();

        int testArrayOfString = Monster.createTestarrayofstringVector(fbb, new int[]{
                fbb.createString("test1"),
                fbb.createString("test2")
        });

        Monster.startMonster(fbb);
        Monster.addPos(fbb, Vec3.createVec3(fbb, 1.0f, 2.0f, 3.0f, 3.0,
                Color.Green, (short) 5, (byte) 6));
        Monster.addHp(fbb, (short) 80);
        Monster.addName(fbb, str);
        Monster.addInventory(fbb, inv);
        Monster.addTestType(fbb, (byte) Any.Monster);
        Monster.addTest(fbb, mon2);
        Monster.addTest4(fbb, test4);
        Monster.addTestarrayofstring(fbb, testArrayOfString);
        int mon = Monster.endMonster(fbb);

        Monster.finishMonsterBuffer(fbb, mon);

        return fbb.sizedByteArray();

    }

}
