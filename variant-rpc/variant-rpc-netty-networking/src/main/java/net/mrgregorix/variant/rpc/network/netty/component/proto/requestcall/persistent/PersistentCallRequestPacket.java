package net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.persistent;

import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.CallRequestPacket;

public class PersistentCallRequestPacket extends CallRequestPacket
{
    private final int serviceId;
    private final int methodId;

    public PersistentCallRequestPacket(final int callId, final int serviceId, final int methodId, final byte[] data)
    {
        super(PacketType.PACKET_REQUEST_CALL_PERSISTENT, callId, data);
        this.serviceId = serviceId;
        this.methodId = methodId;
    }

    public int getServiceId()
    {
        return this.serviceId;
    }

    public int getMethodId()
    {
        return this.methodId;
    }
}
