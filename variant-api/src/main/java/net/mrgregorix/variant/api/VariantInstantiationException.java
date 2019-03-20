package net.mrgregorix.variant.api;

/**
 * Thrown when a {@link Variant} instance fails to instantiate a class.
 */
public class VariantInstantiationException extends RuntimeException
{
    /**
     * {@inheritDoc}
     */
    public VariantInstantiationException()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public VariantInstantiationException(final String message)
    {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public VariantInstantiationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public VariantInstantiationException(final Throwable cause)
    {
        super(cause);
    }
}