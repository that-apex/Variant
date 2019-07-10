package net.mrgregorix.variant.rpc.network.netty.component.proto.connectionclose;

import com.google.common.base.MoreObjects;
import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;

public class ConnectionClosedPacket extends Packet
{
    private final String reason;

    public ConnectionClosedPacket(final String reason)
    {
        super(PacketType.PACKET_CONNECTION_CLOSE);
        this.reason = reason;
    }

    public String getReason()
    {
        return this.reason;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("reason", this.reason)
                          .toString();
    }
}
