package net.mrgregorix.variant.rpc.network.netty.component.server;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.network.provider.RpcServerHandler;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.NettyRpcNetworkingProvider;
import net.mrgregorix.variant.rpc.network.netty.component.AbstractNettyNetworkComponent;
import net.mrgregorix.variant.rpc.network.netty.configuration.ConfigurationFactory;


/**
 * A {@link NettyRpcNetworkServer} that uses Netty for networking.
 *
 * @see NettyRpcNetworkingProvider#setupServer(String, String, int, Collection, RpcServerHandler, DataSerializer, DataSerializer)
 */
public class NettyRpcNetworkServer extends AbstractNettyNetworkComponent implements RpcNetworkServer
{
    private final RpcServerHandler                        handler;
    private final Collection<Class<? extends RpcService>> supportedServices;
    private final DataSerializer                          persistentDataSerializer;
    private final DataSerializer                          nonPersistentDataSerializer;
    private       ChannelGroup                            channelGroup;
    private       Channel                                 serverChannel;
    private       EventLoopGroup                          bossGroup;
    private       EventLoopGroup                          workerGroup;

    /**
     * Create a new NettyRpcNetworkServer
     *
     * @param configurationFactory        {@link ConfigurationFactory} to use by this server
     * @param name                        a unique name of the server
     * @param address                     address that the server will bind to
     * @param port                        port that the server will bind to
     * @param supportedServices           services that this server supports
     * @param handler                     {@link RpcServerHandler} for handling call requests and authorizing connections.
     * @param persistentDataSerializer    {@link DataSerializer} used for persistent connection
     * @param nonPersistentDataSerializer {@link DataSerializer} used for non-persistent connection
     */
    public NettyRpcNetworkServer(final ConfigurationFactory configurationFactory, final String name, final String address, final int port, final RpcServerHandler handler, final Collection<Class<? extends RpcService>> supportedServices,
                                 final DataSerializer persistentDataSerializer, final DataSerializer nonPersistentDataSerializer)
    {
        super(configurationFactory, name, address, port);
        this.handler = handler;
        this.supportedServices = supportedServices;
        this.persistentDataSerializer = persistentDataSerializer;
        this.nonPersistentDataSerializer = nonPersistentDataSerializer;
    }

    @Override
    public boolean isRunning()
    {
        return this.serverChannel.isOpen();
    }

    @Override
    public void start()
    {
        this.doStart();
    }

    @Override
    public void startBlocking()
    {
        this.doStart().syncUninterruptibly();
    }

    private ChannelFuture doStart()
    {
        if (this.bossGroup == null || this.workerGroup == null)
        {
            this.bossGroup = this.getConfigurationFactory().createBossGroup();
            this.workerGroup = this.getConfigurationFactory().createWorkerGroup();
        }

        this.channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

        final ChannelFuture future = new ServerBootstrap()
            .group(this.bossGroup, this.workerGroup)
            .channel(this.getConfigurationFactory().getServerChannel())
            .childHandler(new NettyServerChannelInitializer(this))
            .localAddress(this.getAddress(), this.getPort())
            .bind();

        this.serverChannel = future.channel();
        return future;
    }

    @Override
    public void stop()
    {
        this.serverChannel.close();
        this.channelGroup.close();
        this.cleanGroup();
    }

    @Override
    public void stopBlocking()
    {
        this.serverChannel.close().awaitUninterruptibly();
        this.channelGroup.close().awaitUninterruptibly();
        this.cleanGroup();
    }

    private void cleanGroup()
    {
        this.channelGroup.clear();
        this.channelGroup = null;
    }

    @Override
    public Future<Void> dispose()
    {
        return CompletableFuture.runAsync(() -> {
            if (this.serverChannel != null && this.serverChannel.isOpen())
            {
                this.stopBlocking();
            }
            if (this.bossGroup != null)
            {
                final io.netty.util.concurrent.Future<?> bossShutdown = this.bossGroup.shutdownGracefully();
                final io.netty.util.concurrent.Future<?> workerShutdown = this.workerGroup.shutdownGracefully();

                bossShutdown.awaitUninterruptibly();
                workerShutdown.awaitUninterruptibly();
            }
        });
    }

    /**
     * Gets the {@link DataSerializer} that this server will use for persistent connections.
     *
     * @return the {@link DataSerializer} that this server will use for persistent connections
     */
    public DataSerializer getPersistentDataSerializer()
    {
        return this.persistentDataSerializer;
    }

    /**
     * Gets the {@link DataSerializer} that this server will use for non-persistent connections.
     *
     * @return the {@link DataSerializer} that this server will use for non-persistent connections
     */
    public DataSerializer getNonPersistentDataSerializer()
    {
        return this.nonPersistentDataSerializer;
    }

    /**
     * Gets the list of services that this server supports.
     *
     * @return the list of services that this server supports
     */
    public Collection<Class<? extends RpcService>> getSupportedServices()
    {
        return this.supportedServices;
    }

    /**
     * Gets the {@link RpcServerHandler} for handling call requests and authorizing connections.
     *
     * @return the {@link RpcServerHandler} for handling call requests and authorizing connections
     */
    public RpcServerHandler getHandler()
    {
        return this.handler;
    }

    /**
     * Adds client channel to this server's {@link ChannelGroup}
     *
     * @param channel channel to add
     */
    public void addChannel(final Channel channel)
    {
        this.channelGroup.add(channel);
    }
}
