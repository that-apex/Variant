package net.mrgregorix.variant.rpc.core.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;

import net.mrgregorix.variant.rpc.api.serialize.DefaultSerializer;
import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;

public class ArraySerializer implements TypeSerializer<Object>
{
    private final DefaultSerializer dataSerializer;

    public ArraySerializer(final DefaultSerializer dataSerializer)
    {
        this.dataSerializer = dataSerializer;
    }

    @Override
    public Class<?> getMainType()
    {
        return Object[].class;
    }

    @Override
    public boolean shouldHandle(final Class<?> test)
    {
        return test.isArray();
    }

    @Override
    public Collection<Class<?>> usedTypes(final Class<?> type)
    {
        return Collections.singletonList(this.arrayType(type));
    }

    private Class<?> arrayType(final Class<?> type)
    {
        return Array.newInstance(type, 0).getClass(); // todo maybe do it better, in the future xd
    }

    @Override
    public String getIdentifier()
    {
        return "\0" + (char) 10;
    }

    @Override
    public void deserialize(final Object object, final DataInputStream data) throws IOException
    {
        final TypeSerializer<?> typeSerializer = this.dataSerializer.getSerializerByIdentifier(data.readUTF());
        final int length = data.readInt();
        final Object[] array = ((Object[]) object);

        for (int i = 0; i < length; i++)
        {
            array[i] = typeSerializer.deserialize(data);
        }
    }

    @Override
    public Object deserialize(final DataInputStream data) throws IOException
    {
        final TypeSerializer<?> typeSerializer = this.dataSerializer.getSerializerByIdentifier(data.readUTF());
        final int length = data.readInt();
        final Object[] array = new Object[length];

        for (int i = 0; i < length; i++)
        {
            array[i] = typeSerializer.deserialize(data);
        }

        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(final DataOutputStream data, final Object object) throws IOException
    {
        final TypeSerializer<Object> typeSerializer = (TypeSerializer<Object>) this.dataSerializer.getSerializerByClass(this.arrayType(object.getClass()));
        data.writeUTF(typeSerializer.getIdentifier());

        final Object[] array = (Object[]) object;
        data.writeInt(array.length);

        for (final Object o : array)
        {
            typeSerializer.serialize(data, o);
        }
    }
}
