package net.mrgregorix.variant.rpc.core.serialize;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;
import net.mrgregorix.variant.utils.annotation.Nullable;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;
import net.mrgregorix.variant.utils.priority.PriorityConstants;

/**
 * Represents a serializer for a primitive type
 *
 * @param <T> the primitive type to be managed by this serializer
 */
public class PrimitiveSerializer <T> extends AbstractModifiablePrioritizable<TypeSerializer<T>> implements TypeSerializer<T>
{
    /**
     * All the registered primitive serializers.
     */
    private static final CollectionWithImmutable<PrimitiveSerializer<?>, ImmutableList<PrimitiveSerializer<?>>> PRIMITIVE_SERIALIZERS = WrappedCollectionWithImmutable.withImmutableList(new ArrayList<>());

    private final int             id;
    private final Class<?>        primitiveType;
    private final Class<T>        type;
    private final Serializer<T>   serializer;
    private final Deserializer<T> deserializer;

    /**
     * Crates a new PrimitiveSerializer
     *
     * @param id            a unique id for this primitive type
     * @param primitiveType a primitive type to use
     * @param type          a wrapper type of the given primitive type
     * @param serializer    a {@link Serializer} used for serializing this primitive type
     * @param deserializer  a {@link Deserializer} used for deserializing this primitive type
     */
    private PrimitiveSerializer(final int id, final Class<?> primitiveType, final Class<T> type, final Serializer<T> serializer, final Deserializer<T> deserializer)
    {
        this.setPriority(PriorityConstants.HIGHEST);
        this.id = id;
        this.primitiveType = primitiveType;
        this.type = type;
        this.serializer = serializer;
        this.deserializer = deserializer;
    }

    @Override
    public Class<? extends T> getMainType()
    {
        return this.type;
    }

    @Override
    public boolean shouldHandle(final Class<? extends T> test)
    {
        return test == this.type || test == this.primitiveType;
    }

    @Override
    public Collection<Class<?>> usedTypes(Class<? extends T> type)
    {
        return Collections.emptyList();
    }

    @Override
    public String getIdentifier()
    {
        return "\0" + (char) this.id;
    }

    @Override
    public void serialize(final DataOutputStream data, final T object, final Class<?> serializeAs) throws IOException
    {
        this.serializer.serialize(data, object);
    }

    @Override
    public void deserialize(final T object, final DataInputStream data, final Class<?> deserializeAs) throws IOException
    {
    }

    @Nullable
    @Override
    public T deserialize(final DataInputStream data, @Nullable final Class<?> deserializeAs) throws IOException
    {
        return this.deserializer.deserialize(data);
    }

    /**
     * A serializer for primitive type
     *
     * @param <T> serialized primitive type
     */
    @FunctionalInterface
    public interface Serializer <T>
    {
        void serialize(final DataOutputStream data, final T object) throws IOException;
    }

    /**
     * A deserializer for primitive type
     *
     * @param <T> deserialized primitive type
     */
    @FunctionalInterface
    public interface Deserializer <T>
    {
        T deserialize(final DataInputStream data) throws IOException;
    }

    static
    {
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<Byte>(1, byte.class, Byte.class, DataOutputStream::writeByte, DataInputStream::readByte));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<Short>(2, short.class, Short.class, DataOutputStream::writeShort, DataInputStream::readShort));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<Character>(3, char.class, Character.class, DataOutputStream::writeChar, DataInputStream::readChar));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(4, int.class, Integer.class, DataOutputStream::writeInt, DataInputStream::readInt));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(5, long.class, Long.class, DataOutputStream::writeLong, DataInputStream::readLong));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(6, float.class, Float.class, DataOutputStream::writeFloat, DataInputStream::readFloat));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(7, double.class, Double.class, DataOutputStream::writeDouble, DataInputStream::readDouble));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(8, boolean.class, Boolean.class, DataOutputStream::writeBoolean, DataInputStream::readBoolean));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(9, String.class, String.class, DataOutputStream::writeUTF, DataInput::readUTF));
    }

    /**
     * Returns all the {@link PrimitiveSerializer} for all Java primitive types (including String)
     *
     * @return all the {@link PrimitiveSerializer} for all Java primitive types (including String)
     */
    @CollectionMayBeImmutable
    public static List<PrimitiveSerializer<?>> getPrimitiveSerializers()
    {
        return PrimitiveSerializer.PRIMITIVE_SERIALIZERS.getImmutable();
    }
}
