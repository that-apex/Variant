package net.mrgregorix.variant.rpc.network.netty.configuration;

import java.util.concurrent.Executor;

import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import net.mrgregorix.variant.rpc.api.network.exception.CallTimeoutException;

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

    /**
     * Creates an executor that will be used for waiting to call responses.
     *
     * @return an executor that will be used for waiting to call response.
     */
    Executor createWaitingExecutor();

    /**
     * Returns the timeout after which a {@link CallTimeoutException} will be thrown if no response for an RPC call was received.
     *
     * @return the call timeout
     */
    long getCallTimeout();
}
