package net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall;

import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;

public abstract class CallRequestPacket extends Packet
{
    private final int    callId;
    private final byte[] data;

    public CallRequestPacket(final PacketType packetType, final int callId, final byte[] data)
    {
        super(packetType);
        this.callId = callId;
        this.data = data;
    }

    public int getCallId()
    {
        return this.callId;
    }

    public byte[] getData()
    {
        return this.data;
    }
}
