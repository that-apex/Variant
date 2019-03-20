package net.mrgregorix.variant.api.module;

/**
 * An exception that is thrown when a new {@link VariantModule} is being registered, but no suitable implementation is found.
 */
public class ModuleHasNoImplementationException extends RuntimeException
{
    /**
     * {@inheritDoc}
     */
    public ModuleHasNoImplementationException()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    public ModuleHasNoImplementationException(final String message)
    {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    public ModuleHasNoImplementationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public ModuleHasNoImplementationException(final Throwable cause)
    {
        super(cause);
    }
}
