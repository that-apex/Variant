package net.mrgregorix.variant.commands.api.manager;

import java.util.Collection;

import net.mrgregorix.variant.commands.api.CommandInfo;
import net.mrgregorix.variant.commands.api.CommandListener;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.annotation.Command;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

/**
 * Manager for registering command listeners and invoking commands on them.
 */
public interface CommandManager
{
    /**
     * Returns all registered {@link ValueProvider}s.
     *
     * @return all registered {@link ValueProvider}s.
     */
    @CollectionMayBeImmutable
    Collection<ValueProvider<?>> getValueProviders();

    /**
     * Registers a new {@link ValueProvider} to this manager.
     *
     * @param provider provider to register
     *
     * @return whether the provider was registered, false if it was already registered
     */
    boolean registerValueProvider(ValueProvider<?> provider);

    /**
     * Unregister {@link ValueProvider} from this manager.
     *
     * @param provider provider to unregister
     *
     * @return whether the provider was unregistered, false if it was not registered
     */
    boolean unregisterValueProvider(ValueProvider<?> provider);

    /**
     * Register a new {@link CommandListener}. All methods from this listener annotated with {@link Command} will be registered as command handlers in this manager.
     *
     * @param listener listener to register
     */
    void registerListener(CommandListener listener);

    /**
     * Returns a collection of {@link CommandInfo}s of commands registerd to this manager.
     *
     * @return a collection of registed {@link CommandInfo}s
     */
    @CollectionMayBeImmutable
    Collection<? extends CommandInfo> getAllCommandInfos();

    /**
     * Parses the given command and its arguments
     *
     * @param sender        sender that is executing this command
     * @param parser        parser for the command's arguments
     * @param commandString the full string with the command and its arguments to be parsed
     *
     * @throws UnknownCommandException when no command matching the given string was found
     * @throws ParsingException        when there was a problem while parsing commands arguments. Rethrown from {@link ArgumentParser#parse(TypeDefinition[], TypeDefinition[], String)}
     */
    void invoke(CommandSender sender, ArgumentParser parser, String commandString) throws UnknownCommandException, ParsingException;
}
