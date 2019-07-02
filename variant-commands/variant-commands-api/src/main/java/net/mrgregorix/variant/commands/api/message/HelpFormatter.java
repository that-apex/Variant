package net.mrgregorix.variant.commands.api.message;

import net.mrgregorix.variant.commands.api.CommandInfo;

/**
 * Helper for creating help pages
 */
public interface HelpFormatter
{
    /**
     * Creates a help information for the given command
     *
     * @param command command to create help information for
     *
     * @return the formatted help page
     */
    String formatHelpForCommand(CommandInfo command);

    /**
     * Creates a list header for a help page
     *
     * @param info info about the help page
     *
     * @return the formatted header
     */
    String formatHeaderForList(HelpPageInfo info);

    /**
     * Creates a help for a single command in a list.
     *
     * @param command command to create help for
     *
     * @return the formatted list element
     */
    String formatHelpEntry(CommandInfo command);

    /**
     * Creates a list footer for a help page
     *
     * @param info info about the help page
     *
     * @return the formatted footer
     */
    String formatFooterForList(HelpPageInfo info);
}
