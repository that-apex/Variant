package net.mrgregorix.variant.commands.api.parser.exception;

import net.mrgregorix.variant.commands.api.parser.TypeDefinition;

public class ParsingException extends Exception
{
    private TypeDefinition definition;

    public ParsingException()
    {
        super();
    }

    public ParsingException(final String message)
    {
        super(message);
    }

    public ParsingException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public ParsingException(final Throwable cause)
    {
        super(cause);
    }

    public TypeDefinition getDefinition()
    {
        return this.definition;
    }

    public void setDefinition(final TypeDefinition definition)
    {
        this.definition = definition;
    }

    @Override
    public String getMessage()
    {
        String message = super.getMessage();
        if (this.definition != null)
        {
            message += " (Definition: " + this.definition + ")";
        }
        return message;
    }
}
