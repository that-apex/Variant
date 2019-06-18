package net.mrgregorix.variant.rpc.network.netty.component.proto;

import java.util.Arrays;
import java.util.Optional;

import com.google.common.base.Preconditions;
import net.mrgregorix.variant.rpc.network.netty.component.proto.callresult.CallResultPacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.callresult.CallResultPacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.connectionclose.ConnectionClosedPacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.connectionclose.ConnectionClosedPacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.init.InitPacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.init.InitPacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.megapacket.MegaPacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.megapacket.MegaPacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.nonpersistent.NonPersistentCallRequestPacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.nonpersistent.NonPersistentCallRequestPacketEncoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.persistent.PersistentCallRequestPacketDecoder;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.persistent.PersistentCallRequestPacketEncoder;

/**
 * Represents a type of a packet and holds its encoder and decoder.
 */
public enum PacketType
{
    PACKET_INIT(0, new InitPacketEncoder(), new InitPacketDecoder()),
    PACKET_MEGAPACKET(1, new MegaPacketEncoder(), new MegaPacketDecoder()),
    PACKET_CONNECTION_CLOSE(2, new ConnectionClosedPacketEncoder(), new ConnectionClosedPacketDecoder()),
    PACKET_REQUEST_CALL_PERSISTENT(3, new PersistentCallRequestPacketEncoder(), new PersistentCallRequestPacketDecoder()),
    PACKET_REQUEST_CALL_NON_PERSISTENT(4, new NonPersistentCallRequestPacketEncoder(), new NonPersistentCallRequestPacketDecoder()),
    PACKET_CALL_RESULT(5, new CallResultPacketEncoder(), new CallResultPacketDecoder());

    private final byte             id;
    private final PacketEncoder<?> encoder;
    private final PacketDecoder<?> decoder;

    <T extends Packet> PacketType(final int id, final PacketEncoder<T> encoder, final PacketDecoder<T> decoder)
    {
        Preconditions.checkArgument(id >= 0 && id <= Byte.MAX_VALUE, "Invalid ID");
        this.id = (byte) id;
        this.encoder = encoder;
        this.decoder = decoder;
    }

    /**
     * Get the numeric id of the packet
     *
     * @return the numeric id of the packet
     */
    public byte getId()
    {
        return this.id;
    }

    /**
     * Get the {@link PacketEncoder} for the given packet type.
     *
     * @return the {@link PacketEncoder} for the given packet type.
     */
    public PacketEncoder<?> getEncoder()
    {
        return this.encoder;
    }

    /**
     * Get the {@link PacketDecoder} for the given packet type.
     *
     * @return the {@link PacketDecoder} for the given packet type.
     */
    public PacketDecoder<?> getDecoder()
    {
        return this.decoder;
    }

    /**
     * Get a {@link PacketType} that id matches the given id.
     *
     * @param id id to look for
     *
     * @return found packet type or empty optional
     */
    public static Optional<PacketType> byId(int id)
    {
        return Arrays.stream(PacketType.values()).filter(it -> it.getId() == id).findFirst();
    }
}
