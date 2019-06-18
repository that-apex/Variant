package net.mrgregorix.variant.rpc.core.serialize;

import java.util.Objects;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;
import net.mrgregorix.variant.rpc.core.serialize.nonpersistent.NonPersistentObjectSerializer;
import net.mrgregorix.variant.rpc.core.serialize.persistent.PersistentDataSerializer;
import net.mrgregorix.variant.rpc.core.serialize.persistent.PersistentObjectSerializer;

/**
 * Helper methods for registering default {@link TypeSerializer}s for the Variant's default {@link DataSerializer}s
 */
public class DefaultSerializers
{
    /**
     * Register all default {@link TypeSerializer}s for a persistent {@link DataSerializer}
     *
     * @param defaultSerializer a persistent data serializer
     */
    public static void persistentSerializers(final PersistentDataSerializer defaultSerializer)
    {
        defaults(Objects.requireNonNull(defaultSerializer));
        defaultSerializer.getSerializerSpec().registerSerializer(new PersistentObjectSerializer(defaultSerializer));
    }

    /**
     * Register all default {@link TypeSerializer}s for a non-persistent {@link DataSerializer}
     *
     * @param defaultSerializer a non-persistent data serializer
     */
    public static void nonpersistentSerializers(final DataSerializer defaultSerializer)
    {
        defaults(defaultSerializer);
        Objects.requireNonNull(defaultSerializer.getSerializerSpec()).registerSerializer(new NonPersistentObjectSerializer(defaultSerializer));
    }

    private static void defaults(final DataSerializer defaultSerializer)
    {
        PrimitiveSerializer.getPrimitiveSerializers().forEach(Objects.requireNonNull(defaultSerializer.getSerializerSpec())::registerSerializer);
        defaultSerializer.getSerializerSpec().registerSerializer(new ArraySerializer(defaultSerializer));
    }

    private DefaultSerializers()
    {
    }
}
