package net.mrgregorix.variant.rpc.network.netty.component.proto.init;

import java.util.Map;

import com.google.common.base.Preconditions;
import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;

public class InitPacketEncoder implements PacketEncoder<InitPacket>
{
    @Override
    public void encodePacket(final ByteBuf buf, final InitPacket packet)
    {
        Preconditions.checkArgument(packet.getUnknownClasses().isEmpty(), "unknown classes must be empty in a new packet");

        buf.writeShort(packet.getServices().size());
        for (final Class<? extends RpcService> service : packet.getServices())
        {
            BufferUtils.writeString(buf, service.getName());
        }

        final Map<String, byte[]> data = packet.getData();
        buf.writeShort(data.size());
        for (final Map.Entry<String, byte[]> entry : data.entrySet())
        {
            BufferUtils.writeString(buf, entry.getKey());
            buf.writeShort(entry.getValue().length);
            buf.writeBytes(entry.getValue());
        }

        buf.writeBoolean(packet.isPersistent());
    }
}
