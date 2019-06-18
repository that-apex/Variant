package net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.nonpersistent;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;
import net.mrgregorix.variant.rpc.network.netty.utils.SignatureHelper;

public class NonPersistentCallRequestPacketEncoder implements PacketEncoder<NonPersistentCallRequestPacket>
{
    @Override
    public void encodePacket(final ByteBuf buf, final NonPersistentCallRequestPacket packet)
    {
        buf.writeInt(packet.getCallId());
        BufferUtils.writeString(buf, packet.getService().getName());
        BufferUtils.writeString(buf, packet.getMethod().getDeclaringClass().getName());
        BufferUtils.writeString(buf, SignatureHelper.generateSignature(packet.getMethod()));
        buf.writeShort(packet.getData().length);
        buf.writeBytes(packet.getData());
    }
}
