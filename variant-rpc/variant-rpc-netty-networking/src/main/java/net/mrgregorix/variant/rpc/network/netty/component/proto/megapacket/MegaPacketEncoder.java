package net.mrgregorix.variant.rpc.network.netty.component.proto.megapacket;

import java.lang.reflect.Method;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;
import net.mrgregorix.variant.rpc.network.netty.utils.SignatureHelper;

public class MegaPacketEncoder implements PacketEncoder<MegaPacket>
{
    @Override
    public void encodePacket(final ByteBuf buf, final MegaPacket packet)
    {
        final Method[] methodIds = packet.getMethodIds();
        buf.writeShort(methodIds.length);

        for (final Method method : methodIds)
        {
            BufferUtils.writeString(buf, method.getDeclaringClass().getName());
            BufferUtils.writeString(buf, SignatureHelper.generateSignature(method));
        }

        final byte[] data = packet.getData();
        buf.writeShort(data.length);
        buf.writeBytes(data);
    }
}
