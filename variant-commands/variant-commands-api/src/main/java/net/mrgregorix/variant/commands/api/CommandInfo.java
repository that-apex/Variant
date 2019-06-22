package net.mrgregorix.variant.commands.api;

import java.util.Collection;

public interface CommandInfo
{
    String getName();

    String getDescription();

    String getUsage();

    String getFullPrefix();

    Collection<? extends CommandInfo> getSubcommands();
}
