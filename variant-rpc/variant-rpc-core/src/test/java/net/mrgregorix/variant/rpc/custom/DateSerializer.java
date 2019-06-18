package net.mrgregorix.variant.rpc.custom;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;
import net.mrgregorix.variant.utils.annotation.Nullable;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

public class DateSerializer extends AbstractModifiablePrioritizable<TypeSerializer<Date>> implements TypeSerializer<Date>
{
    @Override
    public Class<? extends Date> getMainType()
    {
        return Date.class;
    }

    @Override
    public Collection<Class<?>> usedTypes(Class<? extends Date> type)
    {
        return Collections.emptyList();
    }

    @Override
    public String getIdentifier()
    {
        return "Test::Date";
    }

    @Override
    public void serialize(final DataOutputStream data, final Date object, final Class<?> serializeAs) throws IOException
    {
        data.writeLong(object.getTime());
    }

    @Nullable
    @Override
    public Date deserialize(final DataInputStream data, @Nullable final Class<?> deserializeAs) throws IOException
    {
        return new Date(data.readLong());
    }

    @Override
    public void deserialize(final Date object, final DataInputStream data, @Nullable final Class<?> deserializeAs) throws IOException
    {
        object.setTime(data.readLong());
    }
}
