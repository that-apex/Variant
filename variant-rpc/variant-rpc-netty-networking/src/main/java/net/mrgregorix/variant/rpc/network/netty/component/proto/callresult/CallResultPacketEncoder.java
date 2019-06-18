package net.mrgregorix.variant.rpc.network.netty.component.proto.callresult;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketEncoder;

public class CallResultPacketEncoder implements PacketEncoder<CallResultPacket>
{
    @Override
    public int hintSize(final CallResultPacket packet)
    {
        return 5 + (packet.isSuccess() ? packet.getReturnValue().length : packet.getExceptionValue().length);
    }

    @Override
    public void encodePacket(final ByteBuf buf, final CallResultPacket packet)
    {
        buf.writeInt(packet.getCallId());
        buf.writeBoolean(packet.isSuccess());
        buf.writeBytes(packet.isSuccess() ? packet.getReturnValue() : packet.getExceptionValue());
    }
}
