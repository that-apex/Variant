package net.mrgregorix.variant.commands.api.message;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.utils.annotation.Nullable;

public interface HelpFormatter
{
    String formatHelpForCommand(CommandInfo command);

    String formatHeaderForList(HelpPageInfo info);

    String formatHelpEntry(@Nullable CommandInfo mainCommand, CommandInfo subCommand);

    String formatFooterForList(HelpPageInfo info);
}
