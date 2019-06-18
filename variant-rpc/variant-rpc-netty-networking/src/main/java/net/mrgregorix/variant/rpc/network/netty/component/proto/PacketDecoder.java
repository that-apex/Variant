package net.mrgregorix.variant.rpc.network.netty.component.proto;

import io.netty.buffer.ByteBuf;

/**
 * A decoder for a {@link Packet}
 *
 * @param <T> type of the packet to decode.
 */
@FunctionalInterface
public interface PacketDecoder <T extends Packet>
{
    /**
     * Decode a packet from the given byte buffer
     *
     * @param buf byte buffer
     *
     * @return decoded packet
     */
    T decodePacket(ByteBuf buf);
}
