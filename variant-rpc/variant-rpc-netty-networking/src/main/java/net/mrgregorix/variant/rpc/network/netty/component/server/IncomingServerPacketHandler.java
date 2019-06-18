package net.mrgregorix.variant.rpc.network.netty.component.server;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Attribute;
import net.mrgregorix.variant.rpc.api.network.provider.result.RpcServiceCallResult;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.callresult.CallResultPacket;
import net.mrgregorix.variant.rpc.network.netty.component.proto.init.InitPacket;
import net.mrgregorix.variant.rpc.network.netty.component.proto.megapacket.MegaPacket;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.CallRequestPacket;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.nonpersistent.NonPersistentCallRequestPacket;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.persistent.PersistentCallRequestPacket;
import net.mrgregorix.variant.rpc.network.netty.utils.ChannelAttributes;
import net.mrgregorix.variant.utils.reflect.MemberUtils;


/**
 * Handler for incoming packets for a {@link NettyRpcNetworkServer}
 */
public class IncomingServerPacketHandler extends ChannelInboundHandlerAdapter
{
    private final NettyRpcNetworkServer server;

    /**
     * Creates a new IncomingServerPacketHandler
     *
     * @param server server to use
     */
    public IncomingServerPacketHandler(final NettyRpcNetworkServer server)
    {
        this.server = server;
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception
    {
        final Packet packet = (Packet) msg;

        switch (packet.getPacketType())
        {
            case PACKET_INIT:
            {
                this.handlePacket(ctx, (InitPacket) packet);
                break;
            }
            case PACKET_REQUEST_CALL_NON_PERSISTENT:
            case PACKET_REQUEST_CALL_PERSISTENT:
            {
                this.handlePacket(ctx, (CallRequestPacket) packet);
                break;
            }
            default:
            {
                final NettyRpcConnectionData data = ctx.channel().attr(ChannelAttributes.CONNECTION_DATA).get();
                if (data != null)
                {
                    data.disconnect("Invalid packet received: " + packet.getPacketType().getId());
                }

                ctx.channel().close();

                break;
            }
        }
    }

    private void handlePacket(final ChannelHandlerContext ctx, final InitPacket packet) throws IOException
    {
        final Attribute<NettyRpcConnectionData> attribute = ctx.channel().attr(ChannelAttributes.CONNECTION_DATA);

        if (attribute.get() != null)
        {
            attribute.get().disconnect("Init packet received multiple times");
            throw new IllegalStateException("Init packet received multiple times");
        }

        final DataSerializer serializer = (packet.isPersistent() ? this.server.getPersistentDataSerializer() : this.server.getNonPersistentDataSerializer()).makeClone();
        attribute.set(new NettyRpcConnectionData(ctx.channel(), packet.getServices(), serializer));

        final NettyRpcConnectionData data = attribute.get();
        this.server.getHandler().newConnection(data, packet.getData());

        if (data.isDisconnected())
        {
            return;
        }

        final StringBuilder errorMessage = new StringBuilder();

        for (final Class<? extends RpcService> service : packet.getServices())
        {
            if (! this.server.getSupportedServices().contains(service))
            {
                errorMessage.append("Service: ").append(service.getName()).append(" is not supported by this server. \n");
            }
        }

        for (final String unknownClass : packet.getUnknownClasses())
        {
            errorMessage.append("Class: ").append(unknownClass).append(" is not known to the server\n");
        }

        if (errorMessage.length() > 0)
        {
            data.disconnect("Failed to initialize the connection: " + errorMessage.toString());
            return;
        }

        if (data.getDataSerializer().isPersistent())
        {
            final Collection<Class<?>> types = new HashSet<>();
            final Collection<Method> methodList = new ArrayList<>();

            for (final Class<? extends RpcService> service : packet.getServices())
            {
                for (final Method method : MemberUtils.getAllMethods(service))
                {
                    if (method.getDeclaringClass() == RpcService.class)
                    {
                        continue;
                    }

                    methodList.add(method);
                    if (method.getReturnType() != void.class)
                    {
                        types.add(method.getReturnType());
                    }

                    Collections.addAll(types, method.getParameterTypes());
                    Collections.addAll(types, method.getExceptionTypes());
                }
            }

            data.setMethodIds(methodList.toArray(new Method[0]));

            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            this.server.getPersistentDataSerializer().produceMegaPacket(new DataOutputStream(stream), types);
            data.getDataSerializer().initializeWithMegaPacket(new DataInputStream(new ByteArrayInputStream(stream.toByteArray())));
            ctx.channel().writeAndFlush(new MegaPacket(data.getMethodIds(), stream.toByteArray()));
        }
    }

    private void handlePacket(final ChannelHandlerContext ctx, final CallRequestPacket packet)
    {
        final NettyRpcConnectionData connectionData = ctx.channel().attr(ChannelAttributes.CONNECTION_DATA).get();

        if (connectionData == null)
        {
            ctx.channel().close();
            throw new IllegalStateException("Uninitialized");
        }

        final Method method;
        final Class<? extends RpcService> service;

        if (packet instanceof PersistentCallRequestPacket)
        {
            final int methodId = ((PersistentCallRequestPacket) packet).getMethodId();
            if (methodId < 0 || methodId >= connectionData.getMethodIds().length)
            {
                throw new IllegalArgumentException("Invalid method id: " + methodId);
            }

            method = connectionData.getMethodIds()[methodId];
            service = connectionData.getServices().get(((PersistentCallRequestPacket) packet).getServiceId());
        }
        else if (packet instanceof NonPersistentCallRequestPacket)
        {
            method = ((NonPersistentCallRequestPacket) packet).getMethod();
            service = ((NonPersistentCallRequestPacket) packet).getService();
        }
        else
        {
            throw new AssertionError("What :0");
        }

        final Object[] arguments;
        try
        {
            final DataInputStream dataInputStream = new DataInputStream(new ByteArrayInputStream(packet.getData()));
            final int length = dataInputStream.readShort();
            arguments = new Object[length];

            for (int i = 0; i < length; i++)
            {
                arguments[i] = connectionData.getDataSerializer().deserialize(dataInputStream);
            }
        }
        catch (final IOException e)
        {
            throw new RuntimeException(e);
        }

        final RpcServiceCallResult result = this.server.getHandler().requestedServiceCall(packet.getCallId(), connectionData, service, method, arguments);

        final Object toEncode = result.wasSuccessful() ? result.getResult() : result.getException();
        final ByteArrayOutputStream data = new ByteArrayOutputStream();
        try
        {
            connectionData.getDataSerializer().serialize(new DataOutputStream(data), toEncode);
        }
        catch (final IOException e)
        {
            throw new RuntimeException("Failed to encode call result", e);
        }

        ctx.channel().writeAndFlush(result.wasSuccessful() ?
                                    CallResultPacket.createSuccess(result.getCallId(), data.toByteArray()) :
                                    CallResultPacket.createFailure(result.getCallId(), data.toByteArray()));
    }
}
