package net.mrgregorix.variant.rpc.core.serialize.persistent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.SerializerSpec;
import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;
import net.mrgregorix.variant.rpc.core.serialize.AbstractSerializer;
import net.mrgregorix.variant.rpc.core.serialize.DefaultSerializers;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * The default {@link DataSerializer} for persistent connections.
 * <p>
 * It uses a VariantRPC-specific protocol.
 */
public class PersistentDataSerializer extends AbstractSerializer
{
    @Nullable
    private Class<?>[] ids = null;

    /**
     * Creates a new PersistentDataSerializer
     *
     * @param serializerSpec a {@link SerializerSpec} to use
     */
    public PersistentDataSerializer(final SerializerSpec serializerSpec)
    {
        super(serializerSpec);
        DefaultSerializers.persistentSerializers(this);
    }

    /**
     * Gets the ID of the given class. Or -1 if its not in the ID cache.
     *
     * @param type type to look for
     *
     * @return the ID of the given class. Or -1 if its not the ID cache.
     *
     * @throws NullPointerException if the ID cache was not yet initialized
     */
    public short getId(final Class<?> type)
    {
        final Class<?>[] ids = Objects.requireNonNull(this.ids);
        for (short i = 0; i < ids.length; i++)
        {
            if (this.ids[i] == type)
            {
                return i;
            }
        }

        return - 1;
    }

    /**
     * Gets the class from the ID cache with the given ID.
     *
     * @param id id of class to get
     *
     * @return the found class
     *
     * @throws NullPointerException           if the ID cache was not yet initialized
     * @throws ArrayIndexOutOfBoundsException if the ID is invalid
     */
    public Class<?> getType(final int id)
    {
        return Objects.requireNonNull(this.ids)[id];
    }

    @Override
    public void produceMegaPacket(final DataOutputStream data, final Collection<Class<?>> types) throws IOException
    {
        final Set<Class<?>> allTypes = new HashSet<>();
        this.collectTypes(allTypes, types);
        allTypes.removeIf(type -> type.isArray() || type.isPrimitive());

        data.writeShort(allTypes.size());
        this.ids = new Class<?>[allTypes.size()];
        int id = 0;

        for (final Class<?> type : allTypes)
        {
            this.ids[id++] = type;
            data.writeUTF(type.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private void collectTypes(final Set<Class<?>> output, final Collection<Class<?>> types)
    {
        for (final Class<?> type : types)
        {
            if (output.contains(type))
            {
                continue;
            }

            output.add(type);

            final Collection<Class<?>> current = ((TypeSerializer<Object>) this.getSerializerSpec().getSerializerByClass(type)).usedTypes(type);
            output.addAll(current);
            this.collectTypes(output, current);
        }
    }

    @Override
    public void initializeWithMegaPacket(final DataInputStream data) throws IOException
    {
        final short length = data.readShort();
        this.ids = new Class<?>[length];

        for (int i = 0; i < length; i++)
        {
            try
            {
                this.ids[i] = Class.forName(data.readUTF());
            }
            catch (final ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public boolean isPersistent()
    {
        return true;
    }

    @Override
    public DataSerializer makeClone()
    {
        return new PersistentDataSerializer(this.getSerializerSpec());
    }
}
