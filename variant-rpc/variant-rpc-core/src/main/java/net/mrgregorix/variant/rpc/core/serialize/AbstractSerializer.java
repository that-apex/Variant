package net.mrgregorix.variant.rpc.core.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.SerializerSpec;
import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;
import net.mrgregorix.variant.rpc.core.serialize.nonpersistent.NonPersistentDataSerializer;
import net.mrgregorix.variant.rpc.core.serialize.persistent.PersistentDataSerializer;
import net.mrgregorix.variant.utils.annotation.NotNull;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * A helper {@link DataSerializer} implementation that implements functions common for both persistent and non-persistent serializer for VariantRPC-specific protocol.
 *
 * @see NonPersistentDataSerializer
 * @see PersistentDataSerializer
 */
public abstract class AbstractSerializer implements DataSerializer
{
    private final SerializerSpec serializerSpec;

    protected AbstractSerializer(final SerializerSpec serializerSpec)
    {
        this.serializerSpec = Objects.requireNonNull(serializerSpec);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serialize(final DataOutputStream data, final Object object, @Nullable Class<?> type) throws IOException
    {
        if (object == null)
        {
            data.writeUTF("");
            return;
        }

        if (type == null)
        {
            type = object.getClass();
        }

        final TypeSerializer<Object> serializer = (TypeSerializer<Object>) this.serializerSpec.getSerializerByClass(type);
        data.writeUTF(serializer.getIdentifier());
        serializer.serialize(data, object, type);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public Object deserialize(final DataInputStream data, final Class<?> deserializeAs) throws IOException
    {
        final String identifier = data.readUTF();

        if (identifier.isEmpty())
        {
            return null;
        }

        return ((TypeSerializer<Object>) this.serializerSpec.getSerializerByIdentifier(identifier)).deserialize(data, deserializeAs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void deserialize(final DataInputStream data, final Object object, final Class<?> deserializeAs) throws IOException
    {
        final String identifier = data.readUTF();

        if (identifier.isEmpty())
        {
            return;
        }

        ((TypeSerializer<Object>) this.serializerSpec.getSerializerByIdentifier(identifier)).deserialize(object, data, deserializeAs);
    }

    @Override
    @NotNull
    public SerializerSpec getSerializerSpec()
    {
        return this.serializerSpec;
    }
}
