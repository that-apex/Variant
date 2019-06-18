package net.mrgregorix.variant.rpc.network.netty.configuration;

import io.netty.channel.Channel;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;

/**
 * A {@link ConfigurationFactory} that uses epoll for transport.
 *
 * @see EpollEventLoopGroup
 * @see EpollSocketChannel
 * @see <a href="https://en.wikipedia.org/wiki/Epoll">https://en.wikipedia.org/wiki/Epoll</a>
 */
public class EpollConfigurationFactory extends DefaultConfigurationFactory
{
    @Override
    public EpollEventLoopGroup createBossGroup()
    {
        return new EpollEventLoopGroup(1, this.createBossThreadFactory());
    }

    @Override
    public EpollEventLoopGroup createWorkerGroup()
    {
        return new EpollEventLoopGroup(0, this.createWorkerThreadFactory());
    }

    @Override
    public Class<? extends ServerChannel> getServerChannel()
    {
        return EpollServerSocketChannel.class;
    }

    @Override
    public Class<? extends Channel> getClientChannel()
    {
        return EpollSocketChannel.class;
    }
}
