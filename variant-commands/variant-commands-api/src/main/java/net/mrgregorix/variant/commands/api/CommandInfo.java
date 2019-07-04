package net.mrgregorix.variant.commands.api;

import java.util.Collection;

/**
 * Information about a registered command
 */
public interface CommandInfo
{
    /**
     * Returns name of the command.
     *
     * @return name of the command
     */
    String getName();

    /**
     * Returns description of the command.
     *
     * @return description of the command
     */
    String getDescription();


    /**
     * Returns usage string of the command.
     *
     * @return usage string of the command
     */
    String getUsage();

    /**
     * Returns the full prefix of the command, that is <pre>&lt;base command name>[space]&lt;this command name></pre>
     *
     * @return the full prefix of the command
     */
    String getFullPrefix();

    /**
     * Returns collection containing all subcommands of this command.
     *
     * @return collection containing all subcommands of this command
     */
    Collection<? extends CommandInfo> getSubcommands();
}
