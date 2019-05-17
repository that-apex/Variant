package net.mrgregorix.variant.rpc.core.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;

import net.mrgregorix.variant.rpc.api.serialize.DefaultSerializer;
import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;

public class DefaultObjectSerializer implements TypeSerializer<Object>
{
    private final DefaultSerializer defaultSerializer;
    private       Class<?>[]        idArray;

    public DefaultObjectSerializer(final DefaultSerializer defaultSerializer)
    {
        this.defaultSerializer = defaultSerializer;
    }

    @Override
    public Class<?> getMainType()
    {
        return Object.class;
    }

    public void setupIds(final Class<?>[] array)
    {
        this.idArray = array;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<Class<?>> usedTypes(final Class<?> type)
    {
        final Collection<Class<?>> output = new ArrayList<>();
        output.add(type);

        for (final Field declaredField : type.getDeclaredFields())
        {
            if (Modifier.isTransient(declaredField.getModifiers()))
            {
                continue;
            }

            output.addAll(((TypeSerializer<Object>) this.defaultSerializer.getSerializerByClass(declaredField.getType())).usedTypes(declaredField.getType()));
        }

        return output;
    }

    @Override
    public String getIdentifier()
    {
        return "\0\0";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(final DataOutputStream data, final Object object) throws IOException
    {
        for (int i = 0; i < this.idArray.length; i++)
        {
            if (this.idArray[i] == object.getClass())
            {
                data.writeInt(i);
            }
        }

        if (object.getClass().getSuperclass() != Object.class)
        {
            ((TypeSerializer<Object>) this.defaultSerializer.getSerializerByClass(object.getClass().getSuperclass())).serialize(data, object);
        }

        for (final Field declaredField : object.getClass().getDeclaredFields())
        {
            if (Modifier.isTransient(declaredField.getModifiers()))
            {
                continue;
            }

            declaredField.setAccessible(true);
            try
            {
                final Object part = declaredField.get(object);
                ((TypeSerializer<Object>) this.defaultSerializer.getSerializerByClass(declaredField.getType())).serialize(data, part);
            }
            catch (final IllegalAccessException e)
            {
                throw new AssertionError("This should never happen", e);
            }
        }
    }

    @Override
    public Object deserialize(DataInputStream data) throws IOException
    {
        final Class<?> type = this.idArray[data.readInt()];
        final Object object;
        try
        {
            object = type.getDeclaredConstructor().newInstance();
        }
        catch (final NoSuchMethodException | IllegalAccessException e)
        {
            throw new AssertionError("This should never happen", e);
        }
        catch (final InvocationTargetException | InstantiationException e)
        {
            throw new RuntimeException("Exception while calling constructor of " + type, e);
        }

        this.deserialize(object, data);
        return object;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void deserialize(final Object object, final DataInputStream data) throws IOException
    {
        if (object.getClass().getSuperclass() != Object.class)
        {
            ((TypeSerializer<Object>) this.defaultSerializer.getSerializerByClass(object.getClass().getSuperclass())).deserialize(object, data);
        }

        for (final Field declaredField : object.getClass().getDeclaredFields())
        {
            if (Modifier.isTransient(declaredField.getModifiers()))
            {
                continue;
            }

            declaredField.setAccessible(true);
            try
            {
                declaredField.set(object, (((TypeSerializer<Object>) this.defaultSerializer.getSerializerByClass(declaredField.getClass())).deserialize(data)));
            }
            catch (final IllegalAccessException e)
            {
                throw new AssertionError("This should never happen", e);
            }
        }
    }
}
