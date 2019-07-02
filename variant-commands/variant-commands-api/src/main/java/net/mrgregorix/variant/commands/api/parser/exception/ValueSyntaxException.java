package net.mrgregorix.variant.commands.api.parser.exception;

/**
 * Thrown when the supplied value is of an incorrect syntax.
 */
public class ValueSyntaxException extends ParsingException
{
    private final Class<?> type;

    public ValueSyntaxException(final Class<?> type)
    {
        this.type = type;
    }

    public ValueSyntaxException(final String message, final Class<?> type)
    {
        super(message);
        this.type = type;
    }

    public ValueSyntaxException(final String message, final Throwable cause, final Class<?> type)
    {
        super(message, cause);
        this.type = type;
    }

    public ValueSyntaxException(final Throwable cause, final Class<?> type)
    {
        super(cause);
        this.type = type;
    }

    public Class<?> getType()
    {
        return this.type;
    }
}
