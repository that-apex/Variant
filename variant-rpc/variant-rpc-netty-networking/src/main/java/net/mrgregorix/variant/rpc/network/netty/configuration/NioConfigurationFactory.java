package net.mrgregorix.variant.rpc.network.netty.configuration;

import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * A {@link ConfigurationFactory} that uses NIO for transport.
 *
 * @see NioEventLoopGroup
 * @see NioSocketChannel
 */
public class NioConfigurationFactory extends DefaultConfigurationFactory
{
    @Override
    public NioEventLoopGroup createBossGroup()
    {
        return new NioEventLoopGroup(1, this.createBossThreadFactory());
    }

    @Override
    public NioEventLoopGroup createWorkerGroup()
    {
        return new NioEventLoopGroup(0, this.createWorkerThreadFactory());
    }

    @Override
    public Class<NioServerSocketChannel> getServerChannel()
    {
        return NioServerSocketChannel.class;
    }

    @Override
    public Class<? extends Channel> getClientChannel()
    {
        return NioSocketChannel.class;
    }
}
