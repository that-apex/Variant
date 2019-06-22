package net.mrgregorix.variant.commands.core;

import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.VariantCommands;
import net.mrgregorix.variant.commands.api.manager.CommandManager;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.core.scenario.SimpleListener;
import net.mrgregorix.variant.commands.core.scenario.SubcommandsListener;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
public class TestCommands
{
    @Test
    public void testCommands() throws ParsingException
    {
        final VariantCommands variantCommands = new VariantCommandsImpl();
        final CommandManager commandManager = variantCommands.getCommandManager();
        final ArgumentParser argumentParser = variantCommands.getArgumentParser();

        commandManager.registerListener(new SimpleListener(variantCommands));
        commandManager.registerListener(new SubcommandsListener(variantCommands));

        final TestSender sender = new TestSender();

        commandManager.invoke(sender, argumentParser, "help");
        assertThat("Invalid result", sender.getAndClearBuffer(), equalTo(" == Page [1/1] ==\n" +
                                                                         "\thello_world <arg1> - Hello world! :0\n" +
                                                                         "\thelp [arg1] - help page\n" +
                                                                         "\tirregulars [-square] <arg1> - hello\n" +
                                                                         "\tmath [arg2] - Math command\n" +
                                                                         "\tmath add <arg1> <arg2> - Adds two numbers\n" +
                                                                         "\tmath subtract <arg1> <arg2> - Subtracts two numbers\n" +
                                                                         "\traw <arg1> <arg2> - hello\n" +
                                                                         " == [ End of help page ] == \n\n"));

        commandManager.invoke(sender, argumentParser, "math help 1");
        assertThat("Invalid result", sender.getAndClearBuffer(), equalTo("Command: \n" +
                                                                         "\tName: math\n" +
                                                                         "\tDescription: Math command\n" +
                                                                         "\tUsage: [arg2]\n" +
                                                                         " == Page [1/1] ==\n" +
                                                                         "\tmath add <arg1> <arg2> - Adds two numbers\n" +
                                                                         "\tmath subtract <arg1> <arg2> - Subtracts two numbers\n" +
                                                                         " == [ End of help page ] == \n\n"));

        commandManager.invoke(sender, argumentParser, "hello_world John6");
        assertThat("Invalid result", sender.getAndClearBuffer(), equalTo("Hello John6!\n"));
        commandManager.invoke(sender, argumentParser, "math add _ 3");
        assertThat("Invalid result", sender.getAndClearBuffer(), equalTo("3 + 3 = 6\n"));
        commandManager.invoke(sender, argumentParser, "math add 9 3");
        assertThat("Invalid result", sender.getAndClearBuffer(), equalTo("9 + 3 = 12\n"));
        commandManager.invoke(sender, argumentParser, "math subtract 9 3");
        assertThat("Invalid result", sender.getAndClearBuffer(), equalTo("9 - 3 = 6\n"));
        commandManager.invoke(sender, argumentParser, "irregulars 1,73,6,4");
        assertThat("Invalid result", sender.getAndClearBuffer(), equalTo("1, 73, 6, 4\n"));
        commandManager.invoke(sender, argumentParser, "irregulars -square 1,73,6,4");
        assertThat("Invalid result", sender.getAndClearBuffer(), equalTo("1, 5329, 36, 16\n"));
        commandManager.invoke(sender, argumentParser, "raw 94 this is \"a test");
        assertThat("Invalid result", sender.getAndClearBuffer(), equalTo("x = 94\n" +
                                                                         "raw = this is \"a test\n" +
                                                                         "fullCommand =  94 this is \"a test\n"));
    }

    private static final class TestSender implements CommandSender
    {
        private final StringBuilder buffer = new StringBuilder();

        @Override
        public String getName()
        {
            return "Test";
        }

        @Override
        public void sendMessage(final String message)
        {
            System.out.println("[Sender] " + message);
            this.buffer.append(message).append("\n");
        }

        public String getAndClearBuffer()
        {
            final String string = this.buffer.toString();
            this.buffer.setLength(0);
            return string;
        }
    }
}
