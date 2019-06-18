package net.mrgregorix.variant.rpc.network.netty;

import java.util.Collection;
import java.util.concurrent.Future;

import com.google.common.base.Preconditions;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkComponent;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.network.provider.RpcNetworkingProvider;
import net.mrgregorix.variant.rpc.api.network.provider.RpcServerHandler;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.component.AbstractNettyNetworkComponent;
import net.mrgregorix.variant.rpc.network.netty.component.client.NettyRpcNetworkClient;
import net.mrgregorix.variant.rpc.network.netty.component.server.NettyRpcNetworkServer;
import net.mrgregorix.variant.rpc.network.netty.configuration.ConfigurationFactory;
import net.mrgregorix.variant.rpc.network.netty.configuration.DefaultConfigurationFactoryHolder;

/**
 * A {@link RpcNetworkingProvider} that uses the Netty library for networking.
 */
public class NettyRpcNetworkingProvider implements RpcNetworkingProvider
{
    private final ConfigurationFactory configurationFactory;

    /**
     * Create a new NettyRpcNetworkingProvider using the default configuration factory.
     *
     * @see DefaultConfigurationFactoryHolder#getConfigurationFactory()
     */
    public NettyRpcNetworkingProvider()
    {
        this(DefaultConfigurationFactoryHolder.getConfigurationFactory());
    }

    /**
     * Creates a new NettyRpcNetworkingProvider
     *
     * @param configurationFactory factory for the netty-specific settings.
     *
     * @see ConfigurationFactory
     */
    public NettyRpcNetworkingProvider(final ConfigurationFactory configurationFactory)
    {
        this.configurationFactory = configurationFactory;
    }

    @Override
    public NettyRpcNetworkServer setupServer(final String name, final String address, final int port, final Collection<Class<? extends RpcService>> supportedServices, final RpcServerHandler handler, final DataSerializer persistentDataSerializer,
                                             final DataSerializer nonPersistentDataSerializer)
    {
        return new NettyRpcNetworkServer(this.configurationFactory, name, address, port, handler, supportedServices, persistentDataSerializer, nonPersistentDataSerializer);
    }

    @Override
    public RpcNetworkClient setupClient(final String name, final String address, final int port, final Collection<Class<? extends RpcService>> services, final DataSerializer serializer)
    {
        return new NettyRpcNetworkClient(this.configurationFactory, name, address, port, services, serializer);
    }

    @Override
    public Future<Void> disposeServer(final RpcNetworkServer server)
    {
        return this.dispose(server);
    }

    @Override
    public Future<Void> disposeClient(final RpcNetworkClient client)
    {
        return this.dispose(client);
    }

    private Future<Void> dispose(final RpcNetworkComponent server)
    {
        Preconditions.checkArgument(server instanceof AbstractNettyNetworkComponent, "Invalid component");
        return ((AbstractNettyNetworkComponent) server).dispose();
    }
}
