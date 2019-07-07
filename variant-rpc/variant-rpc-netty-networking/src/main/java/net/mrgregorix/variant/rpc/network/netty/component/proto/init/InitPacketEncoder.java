package net.mrgregorix.variant.rpc.network.netty.component.proto.init;

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

        buf.writeBoolean(packet.isPersistent());
    }
}
