package net.mrgregorix.variant.rpc.api.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Serializer for objects that is used to serialize and deserialize method call arguments and results.
 */
public interface DataSerializer
{
    /**
     * Whether this serializer is persistent
     * <p>
     * A persistent serializer is a serializer that holds states. It can produce a mega packet containing shared definitions. Two persistent serializer must always give the same results for the same arguments only if they mega packets are the same.
     * <p>
     * A non-persistent serializer is fully stateless. For two given streams of input it always produces the same result.
     *
     * @return whether the serializer is persistent
     */
    boolean isPersistent();

    /**
     * Produces a mega packet, containing shared definitions that will be used to setup a mirroring persistent serializer.
     * <p>
     * In non-persistent serializer this function is never used.
     *
     * @param data  stream where to write the data
     * @param types types that will be used in communication
     *
     * @throws IOException when an {@link IOException} occurred while creating the packet
     */
    void produceMegaPacket(DataOutputStream data, Collection<Class<?>> types) throws IOException;

    /**
     * Initialize this persistent serializer with a mega packet created previously by {@link #produceMegaPacket(DataOutputStream, Collection)}.
     * <p>
     * This has no effect on non-persistent serializers.
     *
     * @param data stream containing the mega packet.
     *
     * @throws IOException when an {@link IOException} occurred while working on the stream.
     */
    void initializeWithMegaPacket(DataInputStream data) throws IOException;

    /**
     * Serializers the given object and puts the output to the given data stream.
     *
     * @param data   data stream where the serialized data will be written.
     * @param object object to serialize.
     *
     * @throws IOException when an {@link IOException} occurred while working on the stream.
     */
    default void serialize(DataOutputStream data, Object object) throws IOException
    {
        this.serialize(data, object, object == null ? null : object.getClass());
    }

    /**
     * Serializers the given object and puts the output to the given data stream.
     *
     * @param data   data stream where the serialized data will be written.
     * @param object object to serialize
     * @param type   type that this should be serialized as. If null the type will be deduces using {@link Object#getClass()} from object parameter.
     *
     * @throws IOException when an {@link IOException} occurred while working on the stream.
     */
    void serialize(DataOutputStream data, Object object, @Nullable Class<?> type) throws IOException;

    /**
     * Deserializes an object from the given data stream
     *
     * @param data data stream
     *
     * @return the deserialized object
     *
     * @throws IOException when an {@link IOException} occurred while working on the stream.
     */
    @Nullable
    default Object deserialize(DataInputStream data) throws IOException
    {
        return this.deserialize(data, null);
    }

    /**
     * Deserializes an object of a known type from the given data stream.
     *
     * @param data          data stream
     * @param deserializeAs type of the object that will be deserialized
     *
     * @return the deserialized object
     *
     * @throws IOException when an {@link IOException} occurred while working on the stream.
     */
    @Nullable
    Object deserialize(DataInputStream data, Class<?> deserializeAs) throws IOException;

    /**
     * Deserializes an object and writes the deserialized property to an already existing object.
     *
     * @param data          data stream
     * @param object        object that the properties will be set on
     * @param deserializeAs type of the object that will be deserialized
     *
     * @throws IOException when an {@link IOException} occurred while working on the stream.
     */
    void deserialize(DataInputStream data, Object object, Class<?> deserializeAs) throws IOException;

    /**
     * Returns a {@link SerializerSpec} used by this serializer. May be null if the serializer uses its own type system.
     *
     * @return {@link SerializerSpec} used by this serializer
     */
    @Nullable
    SerializerSpec getSerializerSpec();

    /**
     * Returns an exact clone of this serializer, but not initialized with a mega packet
     *
     * @return an exact clone of this serializer, but not initialized with a mega packet
     */
    DataSerializer makeClone();
}
