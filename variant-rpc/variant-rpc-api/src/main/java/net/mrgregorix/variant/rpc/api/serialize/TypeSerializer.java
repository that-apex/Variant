package net.mrgregorix.variant.rpc.api.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import net.mrgregorix.variant.utils.annotation.Nullable;
import net.mrgregorix.variant.utils.priority.ModifiablePrioritizable;

/**
 * Represents a serializer that is able to turn a specific object into a stream of bytes.
 *
 * @param <T> type of the serializer object.
 */
public interface TypeSerializer <T> extends ModifiablePrioritizable<TypeSerializer<T>>
{
    /**
     * Returns he most basic object type that this serializer should handle.
     * <p>
     * This should always be implemented as {@code return Class&lt;T&gt.class;} where T is the generic class parameter T.
     *
     * @return the most basic object type that this serializer should handle.
     */
    Class<? extends T> getMainType();

    /**
     * Returns whether this serializer can handle the given type.
     *
     * @param test type to be checked
     *
     * @return whether this serializer can handle the given type.
     */
    default boolean shouldHandle(Class<? extends T> test)
    {
        return this.getMainType().isAssignableFrom(test);
    }

    /**
     * Returns a collection of types that a {@link DataSerializer} must also know to serialize/deserialize the given type
     *
     * @param type type to be checked
     *
     * @return a collection of types that a {@link DataSerializer} must also know to serialize/deserialize the given type
     */
    Collection<Class<?>> usedTypes(Class<? extends T> type);

    /**
     * Returns a unique identifier used to identify this type serializer
     *
     * @return a unique identifier used to identify this type serializer
     */
    String getIdentifier();

    /**
     * Serializer the given object to the given data stream.
     *
     * @param data        data stream
     * @param object      object to be serialized
     * @param serializeAs as what type to serialize this object
     *
     * @throws IOException when an {@link IOException} occurred while working on the stream.
     */
    void serialize(DataOutputStream data, T object, Class<?> serializeAs) throws IOException;

    /**
     * Deserialized an object from the given data stream.
     *
     * @param data          data stream
     * @param deserializeAs as what type to deserialize the object, {@code null} if unknown
     *
     * @return the deserialized object
     *
     * @throws IOException when an {@link IOException} occurred while working on the stream.
     */
    @Nullable
    T deserialize(DataInputStream data, @Nullable Class<?> deserializeAs) throws IOException;

    /**
     * Deserializes an object and writes the deserialized property to an already existing object.
     *
     * @param object        object that the properties will be set on
     * @param data          data stream
     * @param deserializeAs as what type to deserialize the object, {@code null} if unknown
     *
     * @throws IOException when an {@link IOException} occurred while working on the stream.
     */
    void deserialize(T object, DataInputStream data, @Nullable Class<?> deserializeAs) throws IOException;
}
