package net.mrgregorix.variant.rpc.api.network.exception;

import net.mrgregorix.variant.rpc.api.service.RpcService;

/**
 * Exception thrown when a {@link RpcService} method is called but the client is not connected.
 */
public class ConnectionFailureException extends RuntimeException
{
    public ConnectionFailureException(final String message)
    {
        super(message);
    }

    public ConnectionFailureException(final String message, final Throwable cause)
    {
        super(message, cause);
    }
}
