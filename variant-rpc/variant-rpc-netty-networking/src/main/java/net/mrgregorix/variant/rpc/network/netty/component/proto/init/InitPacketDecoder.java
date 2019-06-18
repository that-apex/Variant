package net.mrgregorix.variant.rpc.network.netty.component.proto.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;

public class InitPacketDecoder implements PacketDecoder<InitPacket>
{
    @SuppressWarnings("unchecked")
    @Override
    public InitPacket decodePacket(final ByteBuf buf)
    {
        final short size = buf.readShort();
        final List<Class<? extends RpcService>> services = new ArrayList<>(size);
        final Collection<String> unknownClasses = new ArrayList<>();

        for (int i = 0; i < size; i++)
        {
            final String name = BufferUtils.readString(buf);

            try
            {
                services.add((Class<? extends RpcService>) Class.forName(name));
            }
            catch (final ClassNotFoundException e)
            {
                unknownClasses.add(name);
            }
        }

        final short dataSize = buf.readShort();
        final Map<String, byte[]> data = new HashMap<>(dataSize);
        for (int i = 0; i < dataSize; i++)
        {
            final String name = BufferUtils.readString(buf);
            final byte[] entry = new byte[buf.readShort()];
            buf.readBytes(entry);
            data.put(name, entry);
        }

        final boolean persistent = buf.readBoolean();

        return new InitPacket(services, unknownClasses, data, persistent);
    }
}
