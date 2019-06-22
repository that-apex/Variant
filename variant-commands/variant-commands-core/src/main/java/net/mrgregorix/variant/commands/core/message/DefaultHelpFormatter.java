package net.mrgregorix.variant.commands.core.message;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.message.HelpFormatter;
import net.mrgregorix.variant.commands.api.message.HelpPageInfo;

public class DefaultHelpFormatter implements HelpFormatter
{
    @Override
    public String formatHelpForCommand(final CommandInfo command)
    {
        return "Command: \n" +
               "\tName: " + command.getName() + "\n" +
               "\tDescription: " + command.getDescription() + "\n" +
               "\tUsage: " + command.getUsage();
    }

    @Override
    public String formatHeaderForList(final HelpPageInfo info)
    {
        return " == Page [" + info.getCurrent() + "/" + info.getMax() + "] ==";
    }

    @Override
    public String formatHelpEntry(final CommandInfo command)
    {
        return "\t" + command.getFullPrefix() + " " + command.getUsage() + " - " + command.getDescription();
    }

    @Override
    public String formatFooterForList(final HelpPageInfo info)
    {
        String message = " == [ End of help page ] == ";

        if (info.hasNext())
        {
            message += " \nType " + info.getHelpCommand() + " " + info.getNext() + " for more help";
        }

        return message;
    }
}
