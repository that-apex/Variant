package net.mrgregorix.variant.inject.api.injector;

/**
 * Exception thrown when a problem occurs while injecting
 */
public class InjectionException extends RuntimeException
{
    /**
     * {@inheritDoc}
     */
    public InjectionException()
    {
    }

    /**
     * {@inheritDoc}
     */
    public InjectionException(final String message)
    {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public InjectionException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public InjectionException(final Throwable cause)
    {
        super(cause);
    }
}
