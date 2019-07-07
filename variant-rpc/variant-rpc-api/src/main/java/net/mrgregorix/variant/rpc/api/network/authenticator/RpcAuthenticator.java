package net.mrgregorix.variant.rpc.api.network.authenticator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.mrgregorix.variant.rpc.api.network.authenticator.result.AuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.provider.RpcConnectionData;

/**
 * An authenticator for the Variant RPC's connections.
 */
public interface RpcAuthenticator
{
    /**
     * Called when a new client connected to the server
     *
     * @param connection the newly connected client
     *
     * @return action to be done, see {@link AuthenticationResult}
     */
    AuthenticationResult clientConnected(RpcConnectionData connection);

    /**
     * Called when authentication data is received from the client
     *
     * @param connection client sending the data
     * @param data       data sent by the client
     *
     * @return action to be done, see {@link AuthenticationResult}
     */
    AuthenticationResult dataReceivedFromClient(RpcConnectionData connection, DataInputStream data) throws IOException;

    /**
     * Checks whether the given data that was send from the server is matching this authenticator.
     *
     * @param input data sent by the
     *
     * @return does the data match
     *
     * @throws IOException implementation dependent
     */
    boolean matchDataReceivedFromServer(DataInputStream input) throws IOException;

    /**
     * Called when any authentication data is received from the server
     *
     * @param input  data received from the server
     * @param output data to send back to the server
     *
     * @throws IOException implementation dependent
     */
    void dataReceivedFromServer(DataInputStream input, DataOutputStream output) throws IOException;
}
