package net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.nonpersistent;

import java.lang.reflect.Method;

import io.netty.buffer.ByteBuf;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.utils.BufferUtils;
import net.mrgregorix.variant.rpc.network.netty.utils.SignatureHelper;

public class NonPersistentCallRequestPacketDecoder implements PacketDecoder<NonPersistentCallRequestPacket>
{
    @SuppressWarnings("unchecked")
    @Override
    public NonPersistentCallRequestPacket decodePacket(final ByteBuf buf)
    {
        final int callId = buf.readInt();
        final String serviceClassName = BufferUtils.readString(buf);
        final String declaringClassName = BufferUtils.readString(buf);
        final String signature = BufferUtils.readString(buf);
        final byte[] data = new byte[buf.readShort()];
        buf.readBytes(data);

        final Class<? extends RpcService> service;
        try
        {
            service = (Class<? extends RpcService>) Class.forName(serviceClassName);
        }
        catch (final ClassNotFoundException e)
        {
            throw new IllegalArgumentException("Service with name: " + serviceClassName + " not found");
        }

        final Class<?> declaringClass;
        try
        {
            declaringClass = Class.forName(declaringClassName);
        }
        catch (final ClassNotFoundException e)
        {
            throw new IllegalArgumentException("Class with name: " + declaringClassName + " not found");
        }

        final Method method = SignatureHelper.findBySignature(declaringClass, signature);

        if (method == null)
        {
            throw new IllegalArgumentException("No method with signature: " + signature + " found in " + declaringClass);
        }

        return new NonPersistentCallRequestPacket(callId, service, method, data);
    }
}
