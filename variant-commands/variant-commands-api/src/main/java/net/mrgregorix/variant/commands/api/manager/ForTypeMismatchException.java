package net.mrgregorix.variant.commands.api.manager;

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
