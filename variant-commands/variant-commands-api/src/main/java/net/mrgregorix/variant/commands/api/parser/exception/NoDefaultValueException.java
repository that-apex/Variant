package net.mrgregorix.variant.commands.api.parser.exception;

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
