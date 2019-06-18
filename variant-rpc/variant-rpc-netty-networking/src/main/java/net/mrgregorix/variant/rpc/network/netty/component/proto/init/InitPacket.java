package net.mrgregorix.variant.rpc.network.netty.component.proto.init;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;

public class InitPacket extends Packet
{
    private final List<Class<? extends RpcService>> services;
    private final Collection<String>                unknownClasses;
    private final Map<String, byte[]>               data;
    private final boolean                           persistent;

    public InitPacket(final List<Class<? extends RpcService>> services, final Collection<String> unknownClasses, final Map<String, byte[]> data, final boolean persistent)
    {
        super(PacketType.PACKET_INIT);
        this.services = services;
        this.unknownClasses = unknownClasses;
        this.data = data;
        this.persistent = persistent;
    }

    public List<Class<? extends RpcService>> getServices()
    {
        return this.services;
    }

    public Collection<String> getUnknownClasses()
    {
        return this.unknownClasses;
    }

    public Map<String, byte[]> getData()
    {
        return this.data;
    }

    public boolean isPersistent()
    {
        return this.persistent;
    }
}
