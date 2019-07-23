package net.mrgregorix.variant.rpc.network.netty.component.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import net.mrgregorix.variant.rpc.network.netty.component.handler.PacketMessageHandler;

/**
 * A {@link ChannelInitializer} for a {@link NettyRpcNetworkClient}
 */
public class NettyClientChannelInitializer extends ChannelInitializer<Channel>
{
    private final NettyRpcNetworkClient client;

    /**
     * Create a new NettyClientChannelInitializer
     *
     * @param client client to use
     */
    public NettyClientChannelInitializer(final NettyRpcNetworkClient client)
    {
        this.client = client;
    }

    @Override
    protected void initChannel(final Channel ch)
    {
        ch.pipeline().addFirst("VariantRpc|PacketMessageHandler", new PacketMessageHandler());
        ch.pipeline().addLast("VariantRpc|IncomingClientPacketHandler", new IncomingClientPacketHandler(this.client));
    }
}
