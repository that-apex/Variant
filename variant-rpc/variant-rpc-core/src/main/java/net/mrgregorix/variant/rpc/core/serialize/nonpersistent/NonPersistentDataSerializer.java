package net.mrgregorix.variant.rpc.core.serialize.nonpersistent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collection;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.SerializerSpec;
import net.mrgregorix.variant.rpc.core.serialize.AbstractSerializer;
import net.mrgregorix.variant.rpc.core.serialize.DefaultSerializers;

/**
 * The default {@link DataSerializer} for non-persistent connections.
 * <p>
 * It uses a VariantRPC-specific protocol.
 */
public class NonPersistentDataSerializer extends AbstractSerializer
{
    /**
     * Creates a new NonPersistentDataSerializer
     *
     * @param serializerSpec {@link SerializerSpec} to use
     */
    public NonPersistentDataSerializer(final SerializerSpec serializerSpec)
    {
        super(serializerSpec);
        DefaultSerializers.nonpersistentSerializers(this);
    }

    @Override
    public void produceMegaPacket(final DataOutputStream data, final Collection<Class<?>> types)
    {
        // non-persistent
    }

    @Override
    public void initializeWithMegaPacket(final DataInputStream data)
    {
        // non-persistent
    }

    @Override
    public boolean isPersistent()
    {
        return false;
    }

    @Override
    public DataSerializer makeClone()
    {
        return new NonPersistentDataSerializer(this.getSerializerSpec());
    }
}
