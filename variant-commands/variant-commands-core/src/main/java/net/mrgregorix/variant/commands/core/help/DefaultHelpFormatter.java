package net.mrgregorix.variant.commands.core.help;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.message.HelpFormatter;
import net.mrgregorix.variant.commands.api.message.HelpPageInfo;

public class DefaultHelpFormatter implements HelpFormatter
{
    @Override
    public String formatHelpForCommand(final CommandInfo command)
    {
        return null;
    }

    @Override
    public String formatHeaderForList(final HelpPageInfo info)
    {
        return null;
    }

    @Override
    public String formatHelpEntry(final CommandInfo mainCommand, final CommandInfo subCommand)
    {
        return null;
    }

    @Override
    public String formatFooterForList(final HelpPageInfo info)
    {
        return null;
    }
}
