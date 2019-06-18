package net.mrgregorix.variant.rpc.network.netty.component.proto.connectionclose;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;

public class ConnectionClosedPacketEncoder implements PacketEncoder<ConnectionClosedPacket>
{
    @Override
    public int hintSize(final ConnectionClosedPacket packet)
    {
        return packet.getReason().length() + 2;
    }

    @Override
    public void encodePacket(final ByteBuf buf, final ConnectionClosedPacket packet)
    {
        BufferUtils.writeString(buf, packet.getReason());
    }
}
