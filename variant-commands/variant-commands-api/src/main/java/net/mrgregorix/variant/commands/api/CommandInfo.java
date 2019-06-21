package net.mrgregorix.variant.commands.api;

import net.mrgregorix.variant.commands.api.annotation.Command;

public interface CommandInfo
{
    Command getCommandAnnotation();

    void sendHelp(CommandSender sender);
}
