package net.mrgregorix.variant.commands.api.parser.exception;

public class RadixNotSupported extends ParsingException
{
    public RadixNotSupported()
    {
    }

    public RadixNotSupported(final String message)
    {
        super(message);
    }

    public RadixNotSupported(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public RadixNotSupported(final Throwable cause)
    {
        super(cause);
    }
}
