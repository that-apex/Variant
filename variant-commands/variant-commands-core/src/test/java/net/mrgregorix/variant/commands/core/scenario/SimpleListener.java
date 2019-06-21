package net.mrgregorix.variant.commands.core.scenario;

import java.util.List;
import java.util.stream.Collectors;

import net.mrgregorix.variant.commands.api.CommandListener;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.annotation.Argument;
import net.mrgregorix.variant.commands.api.annotation.Command;
import net.mrgregorix.variant.commands.api.annotation.Flag;
import net.mrgregorix.variant.commands.api.annotation.Sender;
import net.mrgregorix.variant.commands.api.annotation.types.CollectionType;
import net.mrgregorix.variant.commands.api.annotation.FullCommand;
import net.mrgregorix.variant.commands.api.annotation.types.Raw;

public class SimpleListener implements CommandListener
{
    @Command(
        name = "hello_world",
        description = "Hello world! :0"
    )
    public void helloWorld(@Sender final CommandSender sender, @Argument final String name)
    {
        sender.sendMessage("Hello " + name + "!");
    }

    @Command(
        name = "irregulars",
        description = "hello"
    )
    public void irregulars(@Flag(name = "square") final boolean square, @Argument @CollectionType(int.class) final List<Integer> values, @Sender final CommandSender sender)
    {
        sender.sendMessage(values.stream().map(it -> it = square ? (it * it) : it).map(Object::toString).collect(Collectors.joining(", ")));
    }

    @Command(
        name = "raw",
        description = "hello"
    )
    public void testRaw(@Sender final CommandSender sender, @Argument final int x, @Argument @Raw final String raw, @FullCommand final String fullCommand)
    {
        sender.sendMessage("x = " + x);
        sender.sendMessage("raw = " + raw);
        sender.sendMessage("fullCommand = " + fullCommand);
    }
}
