package net.mrgregorix.variant.rpc.core.serialize.persistent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.core.serialize.AbstractObjectSerializer;
import net.mrgregorix.variant.utils.annotation.Nullable;


/**
 * The {@link AbstractObjectSerializer} implementation for the {@link PersistentDataSerializer}
 */
public class PersistentObjectSerializer extends AbstractObjectSerializer
{
    private final PersistentDataSerializer defaultSerializer;

    /**
     * Creates a new PersistentObjectSerializer
     *
     * @param defaultSerializer a {@link DataSerializer} to use
     */
    public PersistentObjectSerializer(final PersistentDataSerializer defaultSerializer)
    {
        super(defaultSerializer);
        this.defaultSerializer = defaultSerializer;
    }

    @Override
    public Collection<Class<?>> usedTypes(final Class<?> type)
    {
        final Set<Class<?>> types = new HashSet<>();

        for (final Field declaredField : type.getDeclaredFields())
        {
            if ((declaredField.getModifiers() & IGNORED_MODIFIERS) != 0 || types.contains(declaredField.getType()))
            {
                continue;
            }

            types.add(declaredField.getType());
        }

        if (type.getSuperclass() != Object.class && ! type.isInterface())
        {
            types.add(type.getSuperclass());
        }

        return types;
    }

    @Override
    public void serialize(final DataOutputStream data, final Object object, final Class<?> serializeAs) throws IOException
    {
        if (object.getClass() == serializeAs)
        {
            final short id = this.defaultSerializer.getId(serializeAs);
            data.writeShort(id);

            if (id == - 1)
            {
                data.writeUTF(serializeAs.getName());
            }
        }

        if (serializeAs.getSuperclass() != Object.class)
        {
            this.getDefaultSerializer().serialize(data, object, serializeAs.getSuperclass());
        }

        this.serializeFields(data, object, serializeAs);
    }

    @Nullable
    @Override
    public Object deserialize(final DataInputStream data, @Nullable Class<?> deserializeAs) throws IOException
    {
        if (deserializeAs == null)
        {
            final short id = data.readShort();
            if (id == - 1)
            {
                try
                {
                    deserializeAs = Class.forName(data.readUTF());
                }
                catch (final ClassNotFoundException e)
                {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                deserializeAs = this.defaultSerializer.getType(id);
            }
        }

        final Object object = this.instantiateType(deserializeAs);
        this.deserialize(object, data, deserializeAs);
        return object;
    }
}

