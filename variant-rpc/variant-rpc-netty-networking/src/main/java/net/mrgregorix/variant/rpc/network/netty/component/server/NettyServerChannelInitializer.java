package net.mrgregorix.variant.rpc.network.netty.component.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.mrgregorix.variant.rpc.network.netty.component.handler.PacketMessageHandler;

/**
 * A {@link ChannelInitializer} for a {@link NettyRpcNetworkServer}
 */
public class NettyServerChannelInitializer extends ChannelInitializer<Channel>
{
    private final NettyRpcNetworkServer server;

    /**
     * Create a new NettyServerChannelInitializer
     *
     * @param server server to use
     */
    public NettyServerChannelInitializer(final NettyRpcNetworkServer server)
    {
        this.server = server;
    }

    @Override
    protected void initChannel(final Channel ch) throws Exception
    {
        this.server.addChannel(ch);

        ch.pipeline().addLast("VariantRpc|PacketMessageHandler", new PacketMessageHandler());
        ch.pipeline().addLast("VariantRpc|IncomingServerPacketHandler", new IncomingServerPacketHandler(this.server));
    }
}
