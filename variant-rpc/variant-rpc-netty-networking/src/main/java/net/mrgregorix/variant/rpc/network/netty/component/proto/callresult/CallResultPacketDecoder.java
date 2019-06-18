package net.mrgregorix.variant.rpc.network.netty.component.proto.callresult;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketDecoder;

public class CallResultPacketDecoder implements PacketDecoder<CallResultPacket>
{
    @Override
    public CallResultPacket decodePacket(final ByteBuf buf)
    {
        final int callId = buf.readInt();
        final boolean success = buf.readBoolean();
        final byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);

        if (success)
        {
            return CallResultPacket.createSuccess(callId, data);
        }
        else
        {
            return CallResultPacket.createFailure(callId, data);
        }
    }
}
