package net.mrgregorix.variant.commands.core;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.VariantCommands;
import net.mrgregorix.variant.commands.api.manager.CommandManager;
import net.mrgregorix.variant.commands.api.message.HelpFormatter;
import net.mrgregorix.variant.commands.api.message.HelpPage;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.core.manager.CommandManagerImpl;
import net.mrgregorix.variant.commands.core.message.DefaultHelpFormatter;
import net.mrgregorix.variant.commands.core.message.HelpPageInfoImpl;
import net.mrgregorix.variant.commands.core.parser.ArgumentParserImpl;

public class VariantCommandsImpl implements VariantCommands
{
    public static final String MODULE_NAME = "Variant::Commands::Core";

    private final ArgumentParser argumentParser = new ArgumentParserImpl();
    private final CommandManager commandManager = new CommandManagerImpl();
    private       HelpFormatter  helpFormatter  = new DefaultHelpFormatter();

    @Override
    public String getName()
    {
        return VariantCommandsImpl.MODULE_NAME;
    }

    @Override
    public void initialize(final Variant variant)
    {
    }

    @Override
    public void setHelpFormatter(final HelpFormatter helpFormatter)
    {
        this.helpFormatter = helpFormatter;
    }

    @Override
    public HelpFormatter getHelpFormatter()
    {
        return this.helpFormatter;
    }

    @Override
    public void sendHelp(final CommandSender sender, final CommandInfo info, final HelpPage helpPage)
    {
        final HelpPageInfoImpl helpPageInfo = new HelpPageInfoImpl(info.getSubcommands(), helpPage.getPage(), 10, info.getFullPrefix() + " help");
        final StringBuilder help = new StringBuilder();
        help.append(this.helpFormatter.formatHelpForCommand(info)).append("\n");
        this.createHelpPage(help, helpPageInfo);
        sender.sendMessage(help.toString());
    }

    @Override
    public void sendHelp(final CommandSender sender, final HelpPage helpPage)
    {
        final HelpPageInfoImpl helpPageInfo = new HelpPageInfoImpl(this.commandManager.getAllCommandInfos(), helpPage.getPage(), 10, "help");
        final StringBuilder help = new StringBuilder();
        this.createHelpPage(help, helpPageInfo);
        sender.sendMessage(help.toString());
    }

    private void createHelpPage(final StringBuilder help, final HelpPageInfoImpl helpPageInfo)
    {
        help.append(this.helpFormatter.formatHeaderForList(helpPageInfo)).append("\n");

        for (final CommandInfo subcommand : helpPageInfo.getInfos())
        {
            help.append(this.helpFormatter.formatHelpEntry(subcommand)).append("\n");
        }

        help.append(this.helpFormatter.formatFooterForList(helpPageInfo)).append("\n");
    }

    @Override
    public ArgumentParser getArgumentParser()
    {
        return this.argumentParser;
    }

    @Override
    public CommandManager getCommandManager()
    {
        return this.commandManager;
    }
}
