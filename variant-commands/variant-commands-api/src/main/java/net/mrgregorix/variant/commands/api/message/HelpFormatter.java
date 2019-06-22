package net.mrgregorix.variant.commands.api.message;

import net.mrgregorix.variant.commands.api.CommandInfo;

public interface HelpFormatter
{
    String formatHelpForCommand(CommandInfo command);

    String formatHeaderForList(HelpPageInfo info);

    String formatHelpEntry(CommandInfo command);

    String formatFooterForList(HelpPageInfo info);
}
