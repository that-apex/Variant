package net.mrgregorix.variant.commands.api.parser.exception;

/**
 * Thrown when a default value for a parameter is requested, but the parameter has no default value defined.
 */
public class NoDefaultValueException extends ParsingException
{
    public NoDefaultValueException()
    {
    }

    public NoDefaultValueException(final String message)
    {
        super(message);
    }

    public NoDefaultValueException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public NoDefaultValueException(final Throwable cause)
    {
        super(cause);
    }
}
