package net.mrgregorix.variant.rpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.SerializerSpec;
import net.mrgregorix.variant.rpc.core.serialize.SimpleSerializerSpec;
import net.mrgregorix.variant.rpc.core.serialize.nonpersistent.NonPersistentDataSerializer;
import net.mrgregorix.variant.rpc.core.serialize.persistent.PersistentDataSerializer;
import net.mrgregorix.variant.rpc.custom.DateSerializer;
import net.mrgregorix.variant.rpc.serialize.CustomType;
import net.mrgregorix.variant.rpc.serialize.Inner;
import net.mrgregorix.variant.rpc.serialize.SerializedClass;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestSerialize
{
    @Test
    public void test() throws IOException
    {
        final SerializerSpec serializerSpec = new SimpleSerializerSpec();
        serializerSpec.registerSerializer(new DateSerializer());

        {
            final PersistentDataSerializer serializerWrite = new PersistentDataSerializer(serializerSpec);
            final PersistentDataSerializer serializerRead = new PersistentDataSerializer(serializerSpec);

            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            final DataOutputStream outputData = new DataOutputStream(output);
            serializerWrite.produceMegaPacket(outputData, Collections.singleton(SerializedClass.class));

            final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            final DataInputStream inputData = new DataInputStream(input);
            serializerRead.initializeWithMegaPacket(inputData);

            this.testSerializer(serializerWrite, serializerRead);
        }

        this.testSerializer(new NonPersistentDataSerializer(serializerSpec), new NonPersistentDataSerializer(serializerSpec));
    }

    private void testSerializer(final DataSerializer serializerWrite, final DataSerializer serializerRead) throws IOException
    {
        // test
        final SerializedClass serializedClass = new SerializedClass();
        serializedClass.parentInt = 2177;
        serializedClass.i = 5;
        serializedClass.j = 3231123124128L;
        serializedClass.k = 326.464;
        serializedClass.x = new CustomType();
        serializedClass.x.x = 2;
        serializedClass.x.y = 7;
        serializedClass.date = new Date(1);
        serializedClass.file = new File("/tmp/heh");
        serializedClass.nullValue = null;
        serializedClass.inner = new Inner();
        serializedClass.inner.component = 999;
        serializedClass.inner.component2 = 998;
        serializedClass.inner.component3 = 997;
        serializedClass.doubleArray = new double[] {3.14, 7.5, 7.5, 7.6};
        serializedClass.customArray = new Object[] {null, 4.04, "Hey, yo", new Inner()};
        serializedClass.hello = Arrays.asList("hello", "from", "the", null, "side");
        serializedClass.multiDimensions = new int[][] {
            {4, 4},
            {1, 2},
            {7, 5, 2, 1}
        };

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final DataOutputStream outputData = new DataOutputStream(output);

        serializerWrite.serialize(outputData, serializedClass);

        final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        final DataInputStream inputData = new DataInputStream(input);

        SerializedClass.SETTER_USED = false;
        final Object deserializedObject = serializerRead.deserialize(inputData);
        assertThat("deserialized object does not extend SerializedClass", deserializedObject, is(instanceOf(SerializedClass.class)));
        assertThat("setter not used", SerializedClass.SETTER_USED);

        final SerializedClass deserializedClass = (SerializedClass) deserializedObject;
        assertThat("deserializer cannot return an existing instance", deserializedClass, is(not(sameInstance(serializedClass))));
        assertThat("deserialized object differs", deserializedClass, equalTo(serializedClass));
    }
}
