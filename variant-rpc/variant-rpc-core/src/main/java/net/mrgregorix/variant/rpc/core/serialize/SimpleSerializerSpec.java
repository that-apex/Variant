package net.mrgregorix.variant.rpc.core.serialize;

import java.util.Collection;
import java.util.TreeSet;

import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.rpc.api.serialize.SerializerSpec;
import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;

/**
 * A simple implementation of the {@link SerializerSpec} based on a {@link TreeSet} to hold the {@link TypeSerializer}s
 */
public class SimpleSerializerSpec implements SerializerSpec
{
    private final CollectionWithImmutable<TypeSerializer<?>, ImmutableList<TypeSerializer<?>>> serializers = WrappedCollectionWithImmutable.withImmutableList(new TreeSet<>());

    @Override
    public Collection<TypeSerializer<?>> getSerializers()
    {
        return this.serializers.getImmutable();
    }

    @Override
    public boolean registerSerializer(final TypeSerializer<?> serializer)
    {
        return this.serializers.add(serializer);
    }

    @Override
    public boolean unregisterSerializer(final TypeSerializer<?> serializer)
    {
        return this.serializers.remove(serializer);
    }

    @Override
    public TypeSerializer<?> getSerializerByIdentifier(final String identifier)
    {
        return this.serializers
            .stream()
            .filter(serializer -> serializer.getIdentifier().equals(identifier))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Identifier " + identifier + " not found"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeSerializer<? extends T> getSerializerByClass(final Class<T> clazz)
    {
        return (TypeSerializer<? extends T>) this.serializers
            .stream()
            .filter(serializer -> ((TypeSerializer<Object>) serializer).shouldHandle(clazz))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("Serializer for type " + clazz + " not found"));
    }
}
