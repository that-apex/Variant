package net.mrgregorix.variant.rpc.network.netty.component.proto.init;

import java.util.Collection;
import java.util.List;

import com.google.common.base.MoreObjects;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;

public class InitPacket extends Packet
{
    private final List<Class<? extends RpcService>> services;
    private final Collection<String>                unknownClasses;
    private final boolean                           persistent;

    public InitPacket(final List<Class<? extends RpcService>> services, final Collection<String> unknownClasses, final boolean persistent)
    {
        super(PacketType.PACKET_INIT);
        this.services = services;
        this.unknownClasses = unknownClasses;
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

    public boolean isPersistent()
    {
        return this.persistent;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("services", this.services)
                          .add("unknownClasses", this.unknownClasses)
                          .add("persistent", this.persistent)
                          .toString();
    }
}
