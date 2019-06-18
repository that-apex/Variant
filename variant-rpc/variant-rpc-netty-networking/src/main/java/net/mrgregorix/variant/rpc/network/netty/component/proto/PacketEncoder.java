package net.mrgregorix.variant.rpc.network.netty.component.proto;

import io.netty.buffer.ByteBuf;

/**
 * An encoder for a {@link Packet}
 *
 * @param <T> type of the packet to encode
 */
@FunctionalInterface
public interface PacketEncoder <T extends Packet>
{
    int UNSPECIFIED_SIZE = 0;

    /**
     * Hint the byte size of a packet
     *
     * @param packet packet to get the expected size of
     *
     * @return the expected size of the packet
     */
    default int hintSize(T packet)
    {
        return UNSPECIFIED_SIZE;
    }

    /**
     * Encode the given packet into a byte buffer
     *
     * @param buf    byte buffer
     * @param packet packet to encode
     */
    void encodePacket(ByteBuf buf, T packet);
}
