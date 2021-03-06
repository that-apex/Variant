package net.mrgregorix.variant.rpc.network.netty.component.proto.auth;

import com.google.common.base.MoreObjects;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.AuthenticationResult;
import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;

public class AuthPacket extends Packet
{
    private final AuthenticationResult authenticationResult;
    private final int                  issuerId;

    public AuthPacket(final AuthenticationResult authenticationResult, final int issuerId)
    {
        super(PacketType.PACKET_AUTH);
        this.authenticationResult = authenticationResult;
        this.issuerId = issuerId;
    }

    public AuthenticationResult getAuthenticationResult()
    {
        return this.authenticationResult;
    }

    public int getIssuerId()
    {
        return this.issuerId;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("issuerId", this.issuerId)
                          .add("authenticationResult", this.authenticationResult)
                          .toString();
    }
}
