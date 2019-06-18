package net.mrgregorix.variant.rpc.api.network.exception;

import net.mrgregorix.variant.rpc.api.service.RpcService;

/**
 * Exception thrown when a {@link RpcService} method call reaches a timeout.
 */
public class CallTimeoutException extends RuntimeException
{
    public CallTimeoutException(final String message)
    {
        super(message);
    }
}
