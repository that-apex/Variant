package net.mrgregorix.variant.rpc.network.netty.component.handler;

import java.util.Arrays;
import java.util.Objects;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.mrgregorix.variant.rpc.network.netty.component.proto.Packet;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.PacketType;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * A handler for encoding and decoding packets
 */
public class PacketMessageHandler extends ChannelDuplexHandler
{
    private           ByteBuf    currentWholePacket;
    private @Nullable ByteBuf    currentData;
    private @Nullable PacketType packetType   = null;
    private           int        expectedSize = - 1;

    public PacketMessageHandler()
    {
    }

    @SuppressWarnings("unchecked")
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise)
    {
        final Packet packet = (Packet) msg;
        final PacketEncoder<Packet> encoder = (PacketEncoder<Packet>) packet.getPacketType().getEncoder();

        final ByteBuf buffer = ctx.alloc().buffer(encoder.hintSize(packet) + 3);
        buffer.writeByte(packet.getPacketType().getId());
        buffer.writeShort(0); // size, to be set later
        encoder.encodePacket(buffer, packet);
        buffer.setShort(1, buffer.writerIndex() - 3); // set real size

        ctx.write(buffer, promise);
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg)
    {
        if (this.currentWholePacket == null)
        {
            this.currentWholePacket = ctx.alloc().buffer();
        }

        final ByteBuf incomingBuffer = (ByteBuf) msg;
        this.currentWholePacket.writeBytes(incomingBuffer);

        while (this.currentWholePacket.isReadable())
        {
            final boolean hasChanges = this.processPacket(ctx);
            if (! hasChanges)
            {
                break;
            }
        }
    }

    private boolean processPacket(final ChannelHandlerContext ctx)
    {
        if (this.packetType == null)
        {
            if (this.currentWholePacket.readableBytes() < 1)
            {
                return false;
            }

            final byte id = this.currentWholePacket.readByte();
            this.packetType = PacketType.byId(id).orElseThrow(() -> new IllegalArgumentException("No packet with id of " + id + " found"));

            return true;
        }

        if (this.expectedSize == - 1)
        {
            if (this.currentWholePacket.readableBytes() < 2)
            {
                return false;
            }

            this.expectedSize = this.currentWholePacket.readUnsignedShort();
            this.currentData = ctx.alloc().buffer(this.expectedSize);
            return true;
        }

        final int currentSize = Objects.requireNonNull(this.currentData).writerIndex();
        final int leftToRead = this.expectedSize - currentSize;

        if (this.currentWholePacket.readableBytes() < leftToRead)
        {
            Objects.requireNonNull(this.currentData).writeBytes(this.currentWholePacket);
            return false;
        }

        Objects.requireNonNull(this.currentData).writeBytes(this.currentWholePacket, leftToRead);

        final Packet packet = this.decodePacketAndReset();
        ctx.fireChannelRead(packet);

        return true;
    }

    @SuppressWarnings("ConstantConditions")
    private Packet decodePacketAndReset()
    {
        if (Boolean.getBoolean("variant.netty.debugPacketsRaw"))
        {
            this.currentData.markReaderIndex();
            final byte[] data = new byte[this.currentData.readableBytes()];
            this.currentData.readBytes(data);
            this.currentData.resetReaderIndex();
            System.out.println(this.hashCode() + ": Raw data: type = " + this.packetType + ", size = " + data.length + ", data = " + Arrays.toString(data));
        }

        final Packet packet = Objects.requireNonNull(this.packetType).getDecoder().decodePacket(this.currentData);

        if (Boolean.getBoolean("variant.netty.debugPackets"))
        {
            System.out.println(this.hashCode() + ": Decoded: " + packet);
        }

        this.packetType = null;
        this.expectedSize = - 1;
        return packet;
    }
}
