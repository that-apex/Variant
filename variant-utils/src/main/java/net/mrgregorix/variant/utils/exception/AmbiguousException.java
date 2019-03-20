package net.mrgregorix.variant.utils.exception;

/**
 * An exception that is thrown when an ambiguous method parameter was used or when there are multiple results for a requested query.
 */
public class AmbiguousException extends RuntimeException
{
    /**
     * {@inheritDoc}
     */
    public AmbiguousException()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public AmbiguousException(final String message)
    {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public AmbiguousException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public AmbiguousException(final Throwable cause)
    {
        super(cause);
    }
}
