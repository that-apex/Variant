package net.mrgregorix.variant.rpc.network.netty.utils;

import io.netty.util.AttributeKey;
import net.mrgregorix.variant.rpc.network.netty.component.server.NettyRpcConnectionData;

/**
 * Holder for all {@link AttributeKey}s used by this module.
 */
public class ChannelAttributes
{
    /**
     * Holds the {@link NettyRpcConnectionData} of a connection. Set when the first InitPacket arrives.
     */
    public static final AttributeKey<NettyRpcConnectionData> CONNECTION_DATA = AttributeKey.valueOf("VariantRPC|Connection_Data");

    private ChannelAttributes()
    {
    }
}
