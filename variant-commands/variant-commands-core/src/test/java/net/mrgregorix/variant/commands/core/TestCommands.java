package net.mrgregorix.variant.commands.core;

import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.VariantCommands;
import net.mrgregorix.variant.commands.api.manager.CommandManager;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.core.manager.CommandManagerImpl;
import net.mrgregorix.variant.commands.core.parser.ArgumentParserImpl;
import net.mrgregorix.variant.commands.core.scenario.SimpleListener;
import net.mrgregorix.variant.commands.core.scenario.SubcommandsListener;
import org.junit.jupiter.api.Test;

public class TestCommands
{
    @Test
    public void testCommands() throws ParsingException
    {
        final VariantCommands variantCommands = new VariantCommandsImpl();
        final ArgumentParser argumentParser = new ArgumentParserImpl();
        final CommandManager commandManager = new CommandManagerImpl(variantCommands);
        commandManager.registerListener(new SimpleListener());
        commandManager.registerListener(new SubcommandsListener());

        final CommandSender sender = new CommandSender()
        {
            @Override
            public String getName()
            {
                return "Test";
            }

            @Override
            public void sendMessage(final String message)
            {
                System.out.println("[Sender] " + message);
            }
        };

        commandManager.invoke(sender, argumentParser, "hello_world John6");
        commandManager.invoke(sender, argumentParser, "math");

        commandManager.invoke(sender, argumentParser, "math add _ 3");
        commandManager.invoke(sender, argumentParser, "math add 9 3");
        commandManager.invoke(sender, argumentParser, "math subtract 9 3");
        commandManager.invoke(sender, argumentParser, "irregulars 1,73,6,4");
        commandManager.invoke(sender, argumentParser, "irregulars -square 1,73,6,4");
        commandManager.invoke(sender, argumentParser, "raw 94 this is \"a test");
    }
}
