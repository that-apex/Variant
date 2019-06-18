package net.mrgregorix.variant.rpc.core.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;

import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;
import net.mrgregorix.variant.utils.annotation.Nullable;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;
import net.mrgregorix.variant.utils.priority.PriorityConstants;
import net.mrgregorix.variant.utils.reflect.PrimitiveUtils;

/**
 * A serializer for all array types. Supports multidimensional arrays.
 */
public class ArraySerializer extends AbstractModifiablePrioritizable<TypeSerializer<Object>> implements TypeSerializer<Object>
{
    private final DataSerializer dataSerializer;

    /**
     * Creates a new ArraySerializer
     *
     * @param dataSerializer a {@link DataSerializer} to use
     */
    public ArraySerializer(final DataSerializer dataSerializer)
    {
        this.dataSerializer = dataSerializer;
        this.setPriority(PriorityConstants.NEARLY_LOWEST);
    }

    @Override
    public Collection<Class<?>> usedTypes(final Class<?> type)
    {
        return Collections.singletonList(type.getComponentType());
    }

    @Override
    public Class<?> getMainType()
    {
        return Object[].class;
    }

    @Override
    public boolean shouldHandle(final Class<?> test)
    {
        return test.isArray();
    }

    @Override
    public String getIdentifier()
    {
        return "" + (char) 11;
    }

    @Override
    public void deserialize(final Object object, final DataInputStream data, @Nullable final Class<?> deserializeAs) throws IOException
    {
        final int length = data.readInt();
        data.readByte(); // primitive id, not needed if the array already exists
        data.readByte(); // depth, not needed if the array already exists

        for (int i = 0; i < length; i++)
        {
            Array.set(object, i, this.dataSerializer.deserialize(data));
        }
    }

    @Override
    @Nullable
    public Object deserialize(final DataInputStream data, @Nullable final Class<?> deserializeAs) throws IOException
    {
        final int length = data.readInt();

        // deduce the type of its a primitive
        final int primitiveId = data.readByte();
        Class<?> defaultType = primitiveId == Byte.MIN_VALUE ? Object.class : PrimitiveUtils.getById(primitiveId);
        final int depth = data.readByte();
        for (int i = 0; i < depth; i++)
        {
            defaultType = Array.newInstance(defaultType, 0).getClass();
        }

        // create new array
        final Object array = Array.newInstance(deserializeAs == null ? defaultType : deserializeAs.getComponentType(), length);

        // set all values
        for (int i = 0; i < length; i++)
        {
            Array.set(array, i, this.dataSerializer.deserialize(data));
        }

        return array;
    }

    @Override
    public void serialize(final DataOutputStream data, final Object object, final Class<?> serializeAs) throws IOException
    {
        final int length = Array.getLength(object);
        data.writeInt(length);

        // calculate depth
        int depth = 0;
        Class<?> component = object.getClass().getComponentType();
        while (component.isArray())
        {
            component = component.getComponentType();
            depth++;
        }

        // is primitive?
        data.writeByte(component.isPrimitive() ? PrimitiveUtils.getId(component) : Byte.MIN_VALUE);
        data.writeByte(depth);

        // write the array
        for (int i = 0; i < length; i++)
        {
            final Object element = Array.get(object, i);
            this.dataSerializer.serialize(data, element);
        }
    }
}
