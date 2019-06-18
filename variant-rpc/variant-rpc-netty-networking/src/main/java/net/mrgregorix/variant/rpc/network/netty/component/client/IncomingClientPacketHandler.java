package net.mrgregorix.variant.rpc.network.netty.component.client;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import net.mrgregorix.variant.rpc.api.network.exception.ConnectionClosedByRemoteHostException;
import net.mrgregorix.variant.rpc.api.network.provider.result.FailedRpcCallResult;
import net.mrgregorix.variant.rpc.api.network.provider.result.RpcServiceCallResult;
import net.mrgregorix.variant.rpc.api.network.provider.result.SuccessfulRpcCallResult;
import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.callresult.CallResultPacket;
import net.mrgregorix.variant.rpc.network.netty.component.proto.connectionclose.ConnectionClosedPacket;
import net.mrgregorix.variant.rpc.network.netty.component.proto.megapacket.MegaPacket;

/**
 * Handler for incoming packets for a {@link NettyRpcNetworkClient}
 */
public class IncomingClientPacketHandler extends ChannelInboundHandlerAdapter
{
    private final NettyRpcNetworkClient client;

    /**
     * Creates a new IncomingClientPacketHandler
     *
     * @param client client to use
     */
    public IncomingClientPacketHandler(final NettyRpcNetworkClient client)
    {
        this.client = client;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception
    {
        final Packet packet = (Packet) msg;

        switch (packet.getPacketType())
        {
            case PACKET_CONNECTION_CLOSE:
            {
                this.handle(ctx, (ConnectionClosedPacket) packet);
                break;
            }
            case PACKET_MEGAPACKET:
            {
                this.handle(ctx, (MegaPacket) packet);
                break;
            }
            case PACKET_CALL_RESULT:
            {
                this.handle(ctx, ((CallResultPacket) packet));
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Invalid packet: " + packet.getPacketType());
            }
        }
    }

    private void handle(final ChannelHandlerContext ctx, final ConnectionClosedPacket packet)
    {
        ctx.channel().close();
        throw new ConnectionClosedByRemoteHostException(packet.getReason());
    }

    private void handle(final ChannelHandlerContext ctx, final MegaPacket packet) throws IOException
    {
        this.client.setMethodIds(packet.getMethodIds());
        this.client.getDataSerializer().initializeWithMegaPacket(new DataInputStream(new ByteArrayInputStream(packet.getData())));
    }

    private void handle(final ChannelHandlerContext ctx, final CallResultPacket packet) throws IOException
    {
        final RpcServiceCallResult callResult;

        if (packet.isSuccess())
        {
            final Object returnValue = this.client.getDataSerializer().deserialize(new DataInputStream(new ByteArrayInputStream(packet.getReturnValue())));
            callResult = new SuccessfulRpcCallResult(packet.getCallId(), returnValue);
        }
        else
        {
            final Object exceptionValue = this.client.getDataSerializer().deserialize(new DataInputStream(new ByteArrayInputStream(packet.getExceptionValue())));
            callResult = new FailedRpcCallResult(packet.getCallId(), (Exception) exceptionValue);
        }

        this.client.notifyResult(callResult);
    }
}