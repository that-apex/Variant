package net.mrgregorix.variant.commands.api;

import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.commands.api.manager.CommandManager;
import net.mrgregorix.variant.commands.api.message.HelpFormatter;
import net.mrgregorix.variant.commands.api.message.HelpPage;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;

/**
 * The core class for the Variant Commands module.
 */
@ModuleImplementation("net.mrgregorix.variant.commands.core.VariantCommandsImpl")
public interface VariantCommands extends VariantModule
{
    /**
     * Sends help page to the given sender about the given command using the help formatter from {@link #getHelpFormatter()}
     *
     * @param sender   sender to send the help to
     * @param info     command to get the help about
     * @param helpPage information about what help page to get
     */
    void sendHelp(CommandSender sender, CommandInfo info, HelpPage helpPage);

    /**
     * Sends help page to the given sender about all the commands available for that sender.
     *
     * @param sender   sender to send the help to
     * @param helpPage information about what help page to get
     */
    void sendHelp(CommandSender sender, HelpPage helpPage);

    /**
     * Returns the {@link HelpFormatter} used for formatting help pages.
     *
     * @return the {@link HelpFormatter} used for formatting help pages
     */
    HelpFormatter getHelpFormatter();

    /**
     * Sets the {@link HelpFormatter} used for formatting help pages
     *
     * @param formatter formatter to set
     */
    void setHelpFormatter(HelpFormatter formatter);

    /**
     * Returns the {@link ArgumentParser} used by this {@link VariantCommands} instance.
     *
     * @return the {@link ArgumentParser} used by this {@link VariantCommands} instance.
     *
     * @see ArgumentParser
     */
    ArgumentParser getArgumentParser();

    /**
     * Returns the {@link CommandManager} used by this {@link VariantCommands} instance.
     *
     * @return the {@link CommandManager} used by this {@link VariantCommands} instance.
     *
     * @see CommandManager
     */
    CommandManager getCommandManager();
}
