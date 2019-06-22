package net.mrgregorix.variant.commands.api;

import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.commands.api.manager.CommandManager;
import net.mrgregorix.variant.commands.api.message.HelpFormatter;
import net.mrgregorix.variant.commands.api.message.HelpPage;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;

@ModuleImplementation("net.mrgregorix.variant.commands.core.VariantCommandsImpl")
public interface VariantCommands extends VariantModule
{
    void sendHelp(CommandSender sender, CommandInfo info, HelpPage helpPage);

    void sendHelp(CommandSender sender, HelpPage helpPage);

    HelpFormatter getHelpFormatter();

    void setHelpFormatter(HelpFormatter formatter);

    ArgumentParser getArgumentParser();

    CommandManager getCommandManager();
}
