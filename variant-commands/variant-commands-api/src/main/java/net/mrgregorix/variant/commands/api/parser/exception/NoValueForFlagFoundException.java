package net.mrgregorix.variant.commands.api.parser.exception;

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
