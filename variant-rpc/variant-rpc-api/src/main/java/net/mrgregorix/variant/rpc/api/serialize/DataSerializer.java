package net.mrgregorix.variant.rpc.api.serialize;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collection;

public interface DataSerializer
{
    void produceMegaPacket(DataOutputStream data, Collection<Class<?>> types);

    void initializeWithMegaPacket(DataInputStream data);

    void serialize(DataOutputStream data, Object object);

    Object deserialize(DataInputStream data);
}
