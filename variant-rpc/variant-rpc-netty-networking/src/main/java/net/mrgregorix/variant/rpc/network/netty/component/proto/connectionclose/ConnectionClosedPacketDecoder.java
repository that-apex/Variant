package net.mrgregorix.variant.rpc.network.netty.component.proto.connectionclose;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;

public class ConnectionClosedPacketDecoder implements PacketDecoder<ConnectionClosedPacket>
{
    @Override
    public ConnectionClosedPacket decodePacket(final ByteBuf buf)
    {
        return new ConnectionClosedPacket(BufferUtils.readString(buf));
    }
}
