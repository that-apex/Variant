package net.mrgregorix.variant.rpc.network.netty.component.proto;

/**
 * Represents a single network packet.
 */
public abstract class Packet
{
    private final PacketType packetType;

    /**
     * Creates a new packet
     *
     * @param packetType type of the packet
     */
    protected Packet(final PacketType packetType)
    {
        this.packetType = packetType;
    }

    /**
     * Returns the type of this packet
     *
     * @return the type of this packet
     */
    public PacketType getPacketType()
    {
        return this.packetType;
    }
}
