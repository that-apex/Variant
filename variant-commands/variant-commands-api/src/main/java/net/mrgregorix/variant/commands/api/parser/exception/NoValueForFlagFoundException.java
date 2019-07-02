package net.mrgregorix.variant.commands.api.parser.exception;

/**
 * Thrown when no value was supplied for a flag that requires a value.
 */
public class NoValueForFlagFoundException extends ParsingException
{
    public NoValueForFlagFoundException()
    {
    }

    public NoValueForFlagFoundException(final String message)
    {
        super(message);
    }

    public NoValueForFlagFoundException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public NoValueForFlagFoundException(final Throwable cause)
    {
        super(cause);
    }
}
