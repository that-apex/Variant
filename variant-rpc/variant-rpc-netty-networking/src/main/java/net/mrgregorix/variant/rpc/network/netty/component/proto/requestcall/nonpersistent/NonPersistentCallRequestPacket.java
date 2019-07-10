package net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.nonpersistent;

import java.lang.reflect.Method;

import com.google.common.base.MoreObjects;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.CallRequestPacket;

public class NonPersistentCallRequestPacket extends CallRequestPacket
{
    private final Class<? extends RpcService> service;
    private final Method                      method;

    public NonPersistentCallRequestPacket(final int callId, final Class<? extends RpcService> service, final Method method, final byte[] data)
    {
        super(PacketType.PACKET_REQUEST_CALL_NON_PERSISTENT, callId, data);
        this.service = service;
        this.method = method;
    }

    public Class<? extends RpcService> getService()
    {
        return this.service;
    }

    public Method getMethod()
    {
        return this.method;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("super", super.toString())
                          .add("service", this.service)
                          .add("method", this.method)
                          .toString();
    }
}
