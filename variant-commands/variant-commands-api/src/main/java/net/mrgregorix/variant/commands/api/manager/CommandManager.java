package net.mrgregorix.variant.commands.api.manager;

import java.util.Collection;

import net.mrgregorix.variant.commands.api.CommandListener;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

public interface CommandManager
{
    @CollectionMayBeImmutable
    Collection<ValueProvider<?>> getValueProviders();

    boolean registerValueProvider(ValueProvider<?> parser);

    boolean unregisterValueProvider(ValueProvider<?> parser);
    
    void registerListener(CommandListener listener);

    void invoke(CommandSender sender, ArgumentParser parser, String commandString) throws ParsingException;
}
