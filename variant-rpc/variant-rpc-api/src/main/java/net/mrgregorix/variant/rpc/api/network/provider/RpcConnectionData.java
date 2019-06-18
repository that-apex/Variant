package net.mrgregorix.variant.rpc.api.network.provider;

import java.net.InetSocketAddress;

import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;

/**
 * Represents a connection connected to a {@link RpcNetworkServer}
 */
public interface RpcConnectionData
{
    /**
     * Returns the serializer used by this connection.
     *
     * @return the serializer used by this connection.
     */
    DataSerializer getDataSerializer();

    /**
     * Returns the address of this connection
     *
     * @return the address of this connection
     */
    InetSocketAddress getAddress();

    /**
     * Disconnects this connection. The supplied reason will be send to the client.
     *
     * @param reason reason for disconnect.
     */
    void disconnect(String reason);

    /**
     * Checks whether {@link #disconnect(String)} was previously called on this object.
     *
     * @return whether disconnect was called.
     */
    boolean isDisconnected();
}
