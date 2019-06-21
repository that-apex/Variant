package net.mrgregorix.variant.commands.api.parser.exception;

public class NotEnoughParametersException extends ParsingException
{
    public NotEnoughParametersException()
    {
    }

    public NotEnoughParametersException(final String message)
    {
        super(message);
    }

    public NotEnoughParametersException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public NotEnoughParametersException(final Throwable cause)
    {
        super(cause);
    }
}
