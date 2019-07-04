package net.mrgregorix.variant.commands.api.parser.exception;

/**
 * Thrown when the command argument parsing was finished but there is still more data to parse.
 */
public class TooManyParametersException extends ParsingException
{
    public TooManyParametersException()
    {
    }

    public TooManyParametersException(final String message)
    {
        super(message);
    }

    public TooManyParametersException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public TooManyParametersException(final Throwable cause)
    {
        super(cause);
    }
}
