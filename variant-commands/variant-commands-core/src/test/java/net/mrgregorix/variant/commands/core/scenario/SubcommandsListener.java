package net.mrgregorix.variant.commands.core.scenario;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandListener;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.annotation.Argument;
import net.mrgregorix.variant.commands.api.annotation.Command;
import net.mrgregorix.variant.commands.api.annotation.Info;
import net.mrgregorix.variant.commands.api.annotation.Sender;
import net.mrgregorix.variant.commands.api.annotation.Subcommand;

public class SubcommandsListener implements CommandListener
{
    @Command(
        name = "math",
        description = "Math command"
    )
    public void main(@Sender final CommandSender sender, @Info final CommandInfo info)
    {
        info.sendHelp(sender);
    }

    @Command(
        name = "add",
        description = "Adds two numbers"
    )
    @Subcommand(of = "math")
    public void add(@Sender final CommandSender sender, @Argument(defaultValue = "3") final int x, @Argument final int y)
    {
        sender.sendMessage(String.format("%s + %s = %s", x, y, x + y));
    }

    @Command(
        name = "subtract",
        description = "Subtracts two numbers"
    )
    @Subcommand(of = "math")
    public void subtracts(@Sender final CommandSender sender, @Argument final int x, @Argument final int y)
    {
        sender.sendMessage(String.format("%s + %s = %s", x, y, x - y));
    }
}
