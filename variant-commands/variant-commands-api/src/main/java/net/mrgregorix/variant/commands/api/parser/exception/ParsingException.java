package net.mrgregorix.variant.commands.api.parser.exception;

import java.util.Objects;

import net.mrgregorix.variant.commands.api.parser.TypeDefinition;

/**
 * An exception thrown when there is a problem with parsing a command argument
 */
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

    /**
     * Returns the {@link TypeDefinition} that parsing caused this exception.
     *
     * @return the {@link TypeDefinition} that parsing caused this exception
     */
    public TypeDefinition getDefinition()
    {
        return Objects.requireNonNull(this.definition, "ParsingException incomplete.");
    }

    /**
     * Sets the {@link TypeDefinition} that caused this exception
     * <p>Internal method. Should not be used.</p>
     *
     * @param definition .
     */
    public void setDefinition(final TypeDefinition definition)
    {
        this.definition = Objects.requireNonNull(definition);
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
