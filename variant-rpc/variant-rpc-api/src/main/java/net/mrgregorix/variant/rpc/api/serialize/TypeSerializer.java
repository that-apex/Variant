package net.mrgregorix.variant.rpc.api.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

public interface TypeSerializer <T>
{
    Class<? extends T> getMainType();

    default boolean shouldHandle(Class<? extends T> test)
    {
        return this.getMainType().isAssignableFrom(test);
    }

    default boolean handleNullValues()
    {
        return false;
    }

    Collection<Class<?>> usedTypes(Class<? extends T> type);

    String getIdentifier();

    void serialize(DataOutputStream data, T object) throws IOException;

    void deserialize(T object, DataInputStream data) throws IOException;

    T deserialize(DataInputStream data) throws IOException;
}
