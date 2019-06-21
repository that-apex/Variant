package net.mrgregorix.variant.commands.api;

import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.commands.api.manager.CommandManager;
import net.mrgregorix.variant.commands.api.message.HelpFormatter;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;

@ModuleImplementation("net.mrgregorix.variant.commands.core.VariantCommandsImpl")
public interface VariantCommands extends VariantModule
{
    void sendHelp(CommandSender sender);

    void sendHelp(CommandSender sender, CommandInfo command);

    HelpFormatter getHelpFormatter();

    void setHelpFormatter(HelpFormatter formatter);

    ArgumentParser getArgumentParser();

    CommandManager getCommandManager();
}
