package net.mrgregorix.variant.commands.api.parser.exception;

/**
 * Thrown when a flag is inputted for a command, but the command does not define such flag.
 */
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
