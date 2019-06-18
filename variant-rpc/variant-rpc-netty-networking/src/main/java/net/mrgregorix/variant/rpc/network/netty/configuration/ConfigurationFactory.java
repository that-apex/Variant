package net.mrgregorix.variant.rpc.network.netty.configuration;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;

/**
 * A factory for creating Netty-specific configurations
 */
public interface ConfigurationFactory
{
    /**
     * Creates a new {@link EventLoopGroup} that will be used as a boss-group.
     *
     * <p>
     * Number of threads in this group may be set to 1.
     * </p>
     *
     * @return a new {@link EventLoopGroup} that will be used as a boss-group.
     */
    EventLoopGroup createBossGroup();

    /**
     * Creates a new {@link EventLoopGroup} that will be used as a worker-group.
     *
     * @return a new {@link EventLoopGroup} that will be used as a worker-group.
     */
    EventLoopGroup createWorkerGroup();

    /**
     * Gets the {@link ServerChannel} type to be used by the server.
     *
     * @return the {@link ServerChannel} type to be used by the server.
     */
    Class<? extends ServerChannel> getServerChannel();

    /**
     * Gets the {@link Channel} type to be used by the client.
     *
     * @return the {@link Channel} type to be used by the client.
     */
    Class<? extends Channel> getClientChannel();
}
