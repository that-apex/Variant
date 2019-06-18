package net.mrgregorix.variant.rpc.network.netty.component.proto.megapacket;

import java.lang.reflect.Method;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;
import net.mrgregorix.variant.rpc.network.netty.utils.SignatureHelper;

public class MegaPacketDecoder implements PacketDecoder<MegaPacket>
{
    @Override
    public MegaPacket decodePacket(final ByteBuf buf)
    {
        final Method[] methodIds = new Method[buf.readShort()];

        for (int i = 0; i < methodIds.length; i++)
        {
            final Class<?> type;
            try
            {
                type = Class.forName(BufferUtils.readString(buf));
            }
            catch (final ClassNotFoundException e)
            {
                throw new IllegalArgumentException("No class with the given name found", e);
            }

            final String signature = BufferUtils.readString(buf);
            final Method method = SignatureHelper.findBySignature(type, signature);

            if (method != null)
            {
                methodIds[i] = method;
                continue;
            }

            throw new IllegalArgumentException("Method with signature: " + signature + " not found in class " + type);
        }

        final byte[] data = new byte[buf.readShort()];
        buf.readBytes(data);
        return new MegaPacket(methodIds, data);
    }
}
