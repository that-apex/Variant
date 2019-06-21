package net.mrgregorix.variant.commands.api.parser.exception;

public class StringSyntaxErrorException extends ParsingException
{
    public StringSyntaxErrorException()
    {
        super();
    }

    public StringSyntaxErrorException(final String message)
    {
        super(message);
    }

    public StringSyntaxErrorException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public StringSyntaxErrorException(final Throwable cause)
    {
        super(cause);
    }
}
