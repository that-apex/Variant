package net.mrgregorix.variant.rpc.api.serialize;

import java.util.Collection;

import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

/**
 * Represents a serialized specification, holding the {@link TypeSerializer} that may be used by and shared by multiple {@link DataSerializer}.
 */
public interface SerializerSpec
{
    /**
     * Returns a collection of all registered {@link TypeSerializer}s.
     *
     * @return a collection of all registered {@link TypeSerializer}s.
     */
    @CollectionMayBeImmutable
    Collection<TypeSerializer<?>> getSerializers();

    /**
     * Registers a new {@link TypeSerializer}
     *
     * @param serializer serializer to be registered.
     *
     * @return true if the serialized was registered, false if it was registered already.
     */
    boolean registerSerializer(TypeSerializer<?> serializer);

    /**
     * Unregisters the given {@link TypeSerializer}
     *
     * @param serializer serializer to unregister
     *
     * @return true if the serializer was unregistered, false if it was never registered.
     */
    boolean unregisterSerializer(TypeSerializer<?> serializer);

    /**
     * Finds a {@link TypeSerializer} that the {@link TypeSerializer#getIdentifier()} is equal to the given identifier.
     *
     * @param identifier the identifier to be looked for
     *
     * @return the type serializer that was found
     *
     * @throws IllegalArgumentException when no serializer with such identifier is found
     */
    TypeSerializer<?> getSerializerByIdentifier(String identifier);

    /**
     * Finds a {@link TypeSerializer} that the {@link TypeSerializer#shouldHandle(Class)} returns true for the given class.
     *
     * @param clazz class to look for
     * @param <T>   type of the class
     *
     * @return the found serializer
     *
     * @throws IllegalArgumentException when no matching serializer is found
     */
    <T> TypeSerializer<? extends T> getSerializerByClass(Class<T> clazz);
}
