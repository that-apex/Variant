package net.mrgregorix.variant.rpc.core.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.Date;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestSerialize
{
    @Test
    public void test()
    {

    }

    private void testSerializer(final DataSerializer serializerWrite, final DataSerializer serializerRead)
    {
        final SerializedClass serializedClass = new SerializedClass();
        serializedClass.i = 5;
        serializedClass.j = 3231123124128L;
        serializedClass.k = 326.464;
        serializedClass.x = new CustomType();
        serializedClass.x.x = 2;
        serializedClass.x.y = 7;
        serializedClass.date = new Date(1);
        serializedClass.file = new File("/tmp/heh");
        serializedClass.inner = new Inner();
        serializedClass.inner.component = 999;
        serializedClass.inner.component2 = 998;
        serializedClass.inner.component3 = 997;

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final DataOutputStream outputData = new DataOutputStream(output);

        serializerWrite.produceMegaPacket(outputData, Collections.singleton(SerializedClass.class));
        serializerWrite.serialize(outputData, serializedClass);

        final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        final DataInputStream inputData = new DataInputStream(input);
        serializerRead.initializeWithMegaPacket(inputData);
        final Object deserializedObject = serializerRead.deserialize(inputData);

        assertThat("deserialized object does not extend SerializedClass", deserializedObject, is(instanceOf(SerializedClass.class)));

        final SerializedClass deserializedClass = (SerializedClass) deserializedObject;
        assertThat("deserializer cannot return an existing instance", deserializedClass, is(not(sameInstance(serializedClass))));
        assertThat("deserialized object differs", deserializedClass, equalTo(serializedClass));
    }
}
