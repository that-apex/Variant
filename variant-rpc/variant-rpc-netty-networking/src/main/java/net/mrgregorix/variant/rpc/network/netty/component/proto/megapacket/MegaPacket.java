package net.mrgregorix.variant.rpc.network.netty.component.proto.megapacket;

import java.lang.reflect.Method;

import com.google.common.base.MoreObjects;
import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;

public class MegaPacket extends Packet
{
    private final byte[]   data;
    private final Method[] methodIds;

    public MegaPacket(final Method[] methodIds, final byte[] data)
    {
        super(PacketType.PACKET_MEGAPACKET);
        this.methodIds = methodIds;
        this.data = data;
    }

    public byte[] getData()
    {
        return this.data;
    }

    public Method[] getMethodIds()
    {
        return this.methodIds;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("data", this.data)
                          .add("methodIds", this.methodIds)
                          .toString();
    }
}
