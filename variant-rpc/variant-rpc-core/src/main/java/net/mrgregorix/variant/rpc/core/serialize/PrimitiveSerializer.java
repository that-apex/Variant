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
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;

public class PrimitiveSerializer <T> implements TypeSerializer<T>
{
    private static final CollectionWithImmutable<PrimitiveSerializer<?>, ImmutableList<PrimitiveSerializer<?>>> PRIMITIVE_SERIALIZERS = WrappedCollectionWithImmutable.withImmutableList(new ArrayList<>());

    private final int             id;
    private final Class<T>        type;
    private final Serializer<T>   serializer;
    private final Deserializer<T> deserializer;

    public PrimitiveSerializer(final int id, final Class<T> type, final Serializer<T> serializer, final Deserializer<T> deserializer)
    {
        this.id = id;
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
    public void serialize(final DataOutputStream data, final T object) throws IOException
    {
        this.serializer.serialize(data, object);
    }

    @Override
    public void deserialize(final T object, final DataInputStream data)
    {
    }

    @Override
    public T deserialize(final DataInputStream data) throws IOException
    {
        return this.deserializer.deserialize(data);
    }

    @FunctionalInterface
    public interface Serializer <T>
    {
        void serialize(final DataOutputStream data, final T object) throws IOException;
    }

    @FunctionalInterface
    public interface Deserializer <T>
    {
        T deserialize(final DataInputStream data) throws IOException;
    }

    static
    {
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<Byte>(1, Byte.class, DataOutputStream::writeByte, DataInputStream::readByte));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<Short>(2, Short.class, DataOutputStream::writeShort, DataInputStream::readShort));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<Character>(3, Character.class, DataOutputStream::writeChar, DataInputStream::readChar));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(4, Integer.class, DataOutputStream::writeInt, DataInputStream::readInt));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(5, Long.class, DataOutputStream::writeLong, DataInputStream::readLong));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(6, Float.class, DataOutputStream::writeFloat, DataInputStream::readFloat));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(7, Double.class, DataOutputStream::writeDouble, DataInputStream::readDouble));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(8, Boolean.class, DataOutputStream::writeBoolean, DataInputStream::readBoolean));
        PRIMITIVE_SERIALIZERS.add(new PrimitiveSerializer<>(9, String.class, DataOutputStream::writeUTF, DataInput::readUTF));
    }

    @CollectionMayBeImmutable
    public static List<PrimitiveSerializer<?>> getPrimitiveSerializers()
    {
        return PrimitiveSerializer.PRIMITIVE_SERIALIZERS.getImmutable();
    }
}
