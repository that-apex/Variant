package net.mrgregorix.variant.rpc.api.network.exception;

/**
 * Exception thrown when an authentication failure has occurred.
 */
public class AuthenticationFailureException extends RuntimeException
{
    public AuthenticationFailureException(final String message)
    {
        super(message);
    }
}
