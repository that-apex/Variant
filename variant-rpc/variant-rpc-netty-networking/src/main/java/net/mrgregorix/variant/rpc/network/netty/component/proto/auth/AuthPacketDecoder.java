package net.mrgregorix.variant.rpc.network.netty.component.proto.auth;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.DataAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.FailedAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.SuccessAuthenticationResult;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;

public class AuthPacketDecoder implements PacketDecoder<AuthPacket>
{
    @Override
    public AuthPacket decodePacket(final ByteBuf buf)
    {
        final int issuerId = buf.readInt();

        if (buf.readBoolean())
        {
            return new AuthPacket(new SuccessAuthenticationResult(), issuerId);
        }

        if (buf.readBoolean())
        {
            return new AuthPacket(new FailedAuthenticationResult(BufferUtils.readString(buf)), issuerId);
        }

        if (buf.readBoolean())
        {
            final byte[] data = new byte[buf.readShort()];
            buf.readBytes(data);
            return new AuthPacket(new DataAuthenticationResult(data), issuerId);
        }

        throw new IllegalStateException("Invalid AuthPacket");
    }
}
