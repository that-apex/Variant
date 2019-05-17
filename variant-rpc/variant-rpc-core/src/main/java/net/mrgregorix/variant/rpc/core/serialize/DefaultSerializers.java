package net.mrgregorix.variant.rpc.core.serialize;

import net.mrgregorix.variant.rpc.api.serialize.DefaultSerializer;

public class DefaultSerializers
{
    public static void initialize(final DefaultSerializer defaultSerializer)
    {
        defaultSerializer.registerSerializer(new ArraySerializer(defaultSerializer));
        PrimitiveSerializer.getPrimitiveSerializers().forEach(defaultSerializer::registerSerializer);
        defaultSerializer.registerSerializer(new DefaultObjectSerializer(defaultSerializer));
    }
}
