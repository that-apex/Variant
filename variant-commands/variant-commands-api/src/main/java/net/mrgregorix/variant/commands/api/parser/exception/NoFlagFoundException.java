package net.mrgregorix.variant.commands.api.parser.exception;

public class NoFlagFoundException extends ParsingException
{
    public NoFlagFoundException()
    {
    }

    public NoFlagFoundException(final String message)
    {
        super(message);
    }

    public NoFlagFoundException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public NoFlagFoundException(final Throwable cause)
    {
        super(cause);
    }
}
