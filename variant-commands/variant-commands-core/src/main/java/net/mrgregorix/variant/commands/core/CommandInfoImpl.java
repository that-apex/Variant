package net.mrgregorix.variant.commands.core;

import java.util.Collection;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.annotation.Command;

public class CommandInfoImpl implements CommandInfo
{
    private final Command                           commandAnnotation;
    private final String                            usage;
    private final String                            fullPrefix;
    private       Collection<? extends CommandInfo> subcommands;

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
