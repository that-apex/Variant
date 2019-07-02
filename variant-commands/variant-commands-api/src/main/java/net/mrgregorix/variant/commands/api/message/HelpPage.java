package net.mrgregorix.variant.commands.api.message;

import net.mrgregorix.variant.commands.api.annotation.Argument;

/**
 * Represents a help page number.
 *
 * <p>Can be used as an {@link Argument}</p>
 */
public interface HelpPage
{
    /**
     * Returns the help page number.
     *
     * @return the help page number
     */
    int getPage();
}
