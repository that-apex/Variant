package net.mrgregorix.variant.rpc.api.serialize;

import java.util.Collection;

import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

public interface DefaultSerializer extends DataSerializer
{
    @CollectionMayBeImmutable
    Collection<TypeSerializer<?>> getSerializers();

    boolean registerSerializer(TypeSerializer<?> serializer);

    boolean unregisterSerializer(TypeSerializer<?> serializer);

    TypeSerializer<?> getSerializerByIdentifier(String identifier);

    <T> TypeSerializer<? extends T> getSerializerByClass(Class<T> clazz);
}
