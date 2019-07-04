package net.mrgregorix.variant.commands.api.parser.exception;

import net.mrgregorix.variant.commands.api.annotation.types.Radix;

/**
 * Thrown when a {@link Radix} is set to a {@code float} or a {@code double} parameter.
 */
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
