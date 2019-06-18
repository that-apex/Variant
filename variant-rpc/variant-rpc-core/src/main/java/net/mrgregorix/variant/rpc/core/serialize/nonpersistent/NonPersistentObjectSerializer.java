package net.mrgregorix.variant.rpc.core.serialize.nonpersistent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.core.serialize.AbstractObjectSerializer;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * The {@link AbstractObjectSerializer} implementation for the {@link NonPersistentDataSerializer}
 */
public class NonPersistentObjectSerializer extends AbstractObjectSerializer
{
    /**
     * Creates a new NonPersistentObjectSerializer
     *
     * @param defaultSerializer a {@link DataSerializer} to use
     */
    public NonPersistentObjectSerializer(final DataSerializer defaultSerializer)
    {
        super(defaultSerializer);
    }

    @Override
    public Collection<Class<?>> usedTypes(final Class<?> type)
    {
        return Collections.emptyList(); // non-persistent
    }

    @Override
    public void serialize(final DataOutputStream data, final Object object, final Class<?> serializeAs) throws IOException
    {
        if (object.getClass() == serializeAs)
        {
            data.writeUTF(object.getClass().getName());
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
            try
            {
                deserializeAs = Class.forName(data.readUTF());
            }
            catch (final ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }

        final Object object = this.instantiateType(deserializeAs);
        this.deserialize(object, data, deserializeAs);
        return object;
    }
}

