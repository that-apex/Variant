package net.mrgregorix.variant.commands.core;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.VariantCommands;
import net.mrgregorix.variant.commands.api.annotation.Command;

public class CommandInfoImpl implements CommandInfo
{
    private final Command         commandAnnotation;
    private final VariantCommands variantCommands;

    public CommandInfoImpl(final Command commandAnnotation, final VariantCommands variantCommands)
    {
        this.commandAnnotation = commandAnnotation;
        this.variantCommands = variantCommands;
    }

    @Override
    public Command getCommandAnnotation()
    {
        return this.commandAnnotation;
    }

    @Override
    public void sendHelp(final CommandSender sender)
    {
        this.variantCommands.sendHelp(sender, this);
    }
}
