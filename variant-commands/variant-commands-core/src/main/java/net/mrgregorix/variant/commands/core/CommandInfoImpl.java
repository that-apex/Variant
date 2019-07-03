package net.mrgregorix.variant.commands.core;

import java.util.Collection;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.annotation.Command;

/**
 * Basic implementation of a {@link CommandInfo}
 */
public class CommandInfoImpl implements CommandInfo
{
    private final Command                           commandAnnotation;
    private final String                            usage;
    private final String                            fullPrefix;
    private       Collection<? extends CommandInfo> subcommands;

    /**
     * Creates a new CommandInfoImpl
     *
     * @param commandAnnotation the {@link Command} annotation containing the data about this command
     * @param usage             usage of this command
     * @param fullPrefix        the full prefix of this command (see {@link CommandInfo#getFullPrefix()})
     */
    public CommandInfoImpl(final Command commandAnnotation, final String usage, final String fullPrefix)
    {
        this.commandAnnotation = commandAnnotation;
        this.usage = usage;
        this.fullPrefix = fullPrefix;
    }

    @Override
    public String getName()
    {
        return this.commandAnnotation.name();
    }

    @Override
    public String getDescription()
    {
        return this.commandAnnotation.description();
    }

    @Override
    public String getUsage()
    {
        return this.usage;
    }

    @Override
    public Collection<? extends CommandInfo> getSubcommands()
    {
        return this.subcommands;
    }

    public void setSubcommands(final Collection<? extends CommandInfo> subcommands)
    {
        this.subcommands = subcommands;
    }

    @Override
    public String getFullPrefix()
    {
        return this.fullPrefix;
    }
}
