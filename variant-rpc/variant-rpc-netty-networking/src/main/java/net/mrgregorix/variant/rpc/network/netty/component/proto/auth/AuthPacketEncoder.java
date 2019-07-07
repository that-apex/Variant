package net.mrgregorix.variant.rpc.network.netty.component.proto.auth;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.AuthenticationResult;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;

public class AuthPacketEncoder implements PacketEncoder<AuthPacket>
{
    @Override
    public int hintSize(final AuthPacket packet)
    {
        int size = 7;

        if (packet.getAuthenticationResult().hasData())
        {
            size += 2 + packet.getAuthenticationResult().getData().length;
        }

        if (packet.getAuthenticationResult().isFailure())
        {
            size += 2 + packet.getAuthenticationResult().getFailReason().length();
        }

        return size;
    }

    @Override
    public void encodePacket(final ByteBuf buf, final AuthPacket packet)
    {
        buf.writeInt(packet.getIssuerId());

        final AuthenticationResult result = packet.getAuthenticationResult();
        buf.writeBoolean(result.isSuccessful());
        buf.writeBoolean(result.isFailure());

        if (result.isFailure())
        {
            BufferUtils.writeString(buf, result.getFailReason());
        }

        buf.writeBoolean(result.hasData());
        if (result.hasData())
        {
            buf.writeShort(result.getData().length);
            buf.writeBytes(result.getData());
        }
    }
}
