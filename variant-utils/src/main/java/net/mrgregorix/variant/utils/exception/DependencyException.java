package net.mrgregorix.variant.utils.exception;

/**
 * An exception that is thrown when a dependency cannot be found.
 */
public class DependencyException extends RuntimeException
{
    /**
     * {@inheritDoc}
     */
    public DependencyException()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public DependencyException(final String message)
    {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public DependencyException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public DependencyException(final Throwable cause)
    {
        super(cause);
    }
}
