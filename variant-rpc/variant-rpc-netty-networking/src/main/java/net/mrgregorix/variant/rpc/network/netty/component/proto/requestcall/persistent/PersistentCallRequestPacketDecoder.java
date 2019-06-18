package net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.persistent;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketDecoder;

public class PersistentCallRequestPacketDecoder implements PacketDecoder<PersistentCallRequestPacket>
{
    @Override
    public PersistentCallRequestPacket decodePacket(final ByteBuf buf)
    {
        final int callId = buf.readInt();
        final int serviceId = buf.readShort();
        final int methodId = buf.readShort();
        final byte[] data = new byte[buf.readShort()];
        buf.readBytes(data);
        return new PersistentCallRequestPacket(callId, serviceId, methodId, data);
    }
}
