package net.mrgregorix.variant.rpc.core.scenario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import net.mrgregorix.variant.rpc.api.serialize.TypeSerializer;

public class DateSerializer implements TypeSerializer<Date>
{
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
    public void serialize(final DataOutputStream data, final Date object) throws IOException
    {
        data.writeLong(object.getTime());
    }

    @Override
    public Date deserialize(DataInputStream data) throws IOException
    {
        return new Date(data.readLong());
    }
}
