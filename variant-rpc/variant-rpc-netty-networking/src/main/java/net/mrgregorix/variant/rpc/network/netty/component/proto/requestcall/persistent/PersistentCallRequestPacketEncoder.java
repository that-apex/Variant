package net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.persistent;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketEncoder;

public class PersistentCallRequestPacketEncoder implements PacketEncoder<PersistentCallRequestPacket>
{
    @Override
    public int hintSize(final PersistentCallRequestPacket packet)
    {
        return 10 + packet.getData().length;
    }

    @Override
    public void encodePacket(final ByteBuf buf, final PersistentCallRequestPacket packet)
    {
        buf.writeInt(packet.getCallId());
        buf.writeShort(packet.getServiceId());
        buf.writeShort(packet.getMethodId());
        buf.writeShort(packet.getData().length);
        buf.writeBytes(packet.getData());
    }
}
