package net.mrgregorix.variant.rpc.api.network.exception;

/**
 * Exception thrown when the remote host closes the RPC connection.
 */
public class ConnectionClosedByRemoteHostException extends RuntimeException
{
    public ConnectionClosedByRemoteHostException(final String message)
    {
        super(message);
    }
}
