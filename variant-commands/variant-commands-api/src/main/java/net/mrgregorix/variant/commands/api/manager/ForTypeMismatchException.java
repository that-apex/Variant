package net.mrgregorix.variant.commands.api.manager;

import net.mrgregorix.variant.commands.api.annotation.meta.ForType;

/**
 * Thrown when a {@link ForType} annotated parameter is used for an invalid type.
 */
public class ForTypeMismatchException extends RuntimeException
{
    public ForTypeMismatchException()
    {
    }

    public ForTypeMismatchException(final String message)
    {
        super(message);
    }

    public ForTypeMismatchException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public ForTypeMismatchException(final Throwable cause)
    {
        super(cause);
    }
}
