package net.mrgregorix.variant.rpc.network.netty.component.server;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;

import io.netty.channel.Channel;
import net.mrgregorix.variant.rpc.api.network.provider.RpcConnectionData;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.component.proto.connectionclose.ConnectionClosedPacket;

/**
 * A {@link RpcConnectionData} implementation for the {@link NettyRpcNetworkServer}
 */
public class NettyRpcConnectionData implements RpcConnectionData
{
    private final Channel                           channel;
    private final List<Class<? extends RpcService>> services;
    private final DataSerializer                    dataSerializer;
    private       boolean                           isDisconnected;
    private       Method[]                          methodIds;

    /**
     * Crates a new NettyRpcConnectionData
     *
     * @param channel    {@link Channel} channel to be used by this connection
     * @param services   list of services that this connection wishes to use
     * @param serializer serializer that this connection uses
     */
    public NettyRpcConnectionData(final Channel channel, final List<Class<? extends RpcService>> services, final DataSerializer serializer)
    {
        this.channel = channel;
        this.services = services;
        this.dataSerializer = serializer;
    }


    @Override
    public InetSocketAddress getAddress()
    {
        return (InetSocketAddress) this.channel.remoteAddress();
    }

    @Override
    public void disconnect(final String reason)
    {
        this.channel.writeAndFlush(new ConnectionClosedPacket(reason)).addListener(f -> this.channel.close());
        this.isDisconnected = true;
    }

    @Override
    public boolean isDisconnected()
    {
        return this.isDisconnected;
    }

    /**
     * Gets the method IDs cache used by this connection.
     *
     * @return the method IDs cache used by this connection
     */
    public Method[] getMethodIds()
    {
        return this.methodIds;
    }

    /**
     * Sets the method IDs cache used by this connection.
     *
     * @param methodIds the method IDs cache used by this connection
     */
    public void setMethodIds(final Method[] methodIds)
    {
        this.methodIds = methodIds;
    }

    /**
     * Gets list of services that this connection wishes to use
     *
     * @return list of services that this connection wishes to use
     */
    public List<Class<? extends RpcService>> getServices()
    {
        return this.services;
    }

    @Override
    public DataSerializer getDataSerializer()
    {
        return this.dataSerializer;
    }
}
