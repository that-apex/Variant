package net.mrgregorix.variant.commands.core.manager;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.commands.api.CommandListener;
import net.mrgregorix.variant.commands.api.CommandSender;
import net.mrgregorix.variant.commands.api.VariantCommands;
import net.mrgregorix.variant.commands.api.annotation.Argument;
import net.mrgregorix.variant.commands.api.annotation.Command;
import net.mrgregorix.variant.commands.api.annotation.Flag;
import net.mrgregorix.variant.commands.api.annotation.Subcommand;
import net.mrgregorix.variant.commands.api.annotation.meta.ParameterDescription;
import net.mrgregorix.variant.commands.api.manager.CommandManager;
import net.mrgregorix.variant.commands.api.manager.UnknownCommandException;
import net.mrgregorix.variant.commands.api.manager.ValueProvider;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.core.CommandInfoImpl;
import net.mrgregorix.variant.commands.core.manager.argumentresolvers.ArgumentParameterResolver;
import net.mrgregorix.variant.commands.core.manager.argumentresolvers.FlagParameterResolver;
import net.mrgregorix.variant.commands.core.manager.argumentresolvers.ValueProviderParameterResolver;
import net.mrgregorix.variant.commands.core.manager.valueproviders.CommandInfoValueProvider;
import net.mrgregorix.variant.commands.core.manager.valueproviders.FullCommandValueProvider;
import net.mrgregorix.variant.commands.core.manager.valueproviders.SenderValueProvider;
import net.mrgregorix.variant.commands.core.parser.definition.ArgumentTypeDefinition;
import net.mrgregorix.variant.commands.core.parser.definition.FlagTypeDefinition;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;

public class CommandManagerImpl implements CommandManager
{
    private final CollectionWithImmutable<ValueProvider<?>, ImmutableList<ValueProvider<?>>> providers         = WrappedCollectionWithImmutable.withImmutableList(new ArrayList<>());
    private final List<RegisteredMethod>                                                     registeredMethods = new ArrayList<>();
    private final VariantCommands variantCommands;

    public CommandManagerImpl(final VariantCommands variantCommands)
    {
        this.variantCommands = variantCommands;
        this.registerValueProvider(new SenderValueProvider());
        this.registerValueProvider(new CommandInfoValueProvider());
        this.registerValueProvider(new FullCommandValueProvider());
    }

    @Override
    public Collection<ValueProvider<?>> getValueProviders()
    {
        return this.providers.getImmutable();
    }

    @Override
    public boolean registerValueProvider(final ValueProvider<?> provider)
    {
        return this.providers.add(provider);
    }

    @Override
    public boolean unregisterValueProvider(final ValueProvider<?> provider)
    {
        return this.providers.remove(provider);
    }

    @Override
    public void registerListener(final CommandListener listener)
    {
        final Class<?> listenerType = listener instanceof Proxy ? ((Proxy) listener).getProxyBaseClass() : listener.getClass();

        for (final Method method : listenerType.getDeclaredMethods())
        {
            final Command commandAnnotation = method.getDeclaredAnnotation(Command.class);

            if (commandAnnotation == null)
            {
                continue;
            }

            this.handleMethod(method, commandAnnotation, listener);
        }
    }

    @SuppressWarnings("unchecked")
    private void handleMethod(final Method method, final Command commandAnnotation, final CommandListener listener)
    {
        method.setAccessible(true);
        String prefix = commandAnnotation.name();

        RegisteredMethod parentMethod = null;
        final Subcommand subcommand = method.getDeclaredAnnotation(Subcommand.class);
        if (subcommand != null)
        {
            parentMethod = this.registeredMethods
                .stream()
                .filter(it -> it.getListener() == listener && it.getCommand().name().equals(subcommand.of()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No command of name " + subcommand.of() + " exists to be put as main command of " + commandAnnotation.name()));

            prefix = parentMethod.getCommand().name() + " " + prefix;
        }

        final List<TypeDefinition> argumentDefinitions = new ArrayList<>();
        final List<TypeDefinition> flagDefinitions = new ArrayList<>();

        final Parameter[] parameters = method.getParameters();
        final ParameterResolver[] parameterResolvers = new ParameterResolver[parameters.length];

        int argumentNumber = 0;
        for (int i = 0; i < parameters.length; i++)
        {
            final Parameter parameter = parameters[i];

            Annotation parameterDescription = null;
            for (final Annotation declaredAnnotation : parameter.getDeclaredAnnotations())
            {
                if (declaredAnnotation.annotationType().isAnnotationPresent(ParameterDescription.class))
                {
                    if (parameterDescription != null)
                    {
                        throw new IllegalArgumentException("Parmeter " + i + ": " + parameter + " has multiple value providers");
                    }

                    parameterDescription = declaredAnnotation;
                }
            }

            if (parameterDescription == null)
            {
                throw new IllegalArgumentException("Parameter " + i + ": " + parameter + " has no valid value providers");
            }

            if (parameterDescription instanceof Argument)
            {
                final TypeDefinition argumentTypeDefinition = new ArgumentTypeDefinition(parameter, (Argument) parameterDescription);
                argumentDefinitions.add(argumentTypeDefinition);
                parameterResolvers[i] = new ArgumentParameterResolver(argumentNumber++);
            }
            else if (parameterDescription instanceof Flag)
            {
                final TypeDefinition flagTypeDefinition = new FlagTypeDefinition(parameter, (Flag) parameterDescription);
                flagDefinitions.add(flagTypeDefinition);
                parameterResolvers[i] = new FlagParameterResolver(flagTypeDefinition);
            }
            else
            {
                ValueProvider<Annotation> provider = null;

                for (final ValueProvider<?> test : this.providers)
                {
                    if (! test.getAnnotationType().isInstance(parameterDescription))
                    {
                        continue;
                    }

                    if (provider != null)
                    {
                        throw new IllegalArgumentException("Parmeter " + i + ": " + parameter + " has multiple value providers");
                    }

                    provider = (ValueProvider<Annotation>) test;
                }


                if (provider == null)
                {
                    throw new IllegalArgumentException("Parameter " + i + ": " + parameter + " has no valid value providers");
                }

                provider.validate(parameter, parameterDescription);
                parameterResolvers[i] = new ValueProviderParameterResolver(provider, parameter, parameterDescription);
            }
        }

        int level = 0;
        RegisteredMethod parent = parentMethod;

        while (parent != null)
        {
            parent = parent.getParentMethod();
            level++;
        }

        this.registeredMethods.add(new RegisteredMethod(
            prefix,
            commandAnnotation,
            new CommandInfoImpl(commandAnnotation, this.variantCommands),
            method,
            listener,
            parentMethod,
            argumentDefinitions.toArray(new TypeDefinition[0]),
            flagDefinitions.toArray(new TypeDefinition[0]),
            parameterResolvers,
            level
        ));
    }

    @Override
    public void invoke(final CommandSender sender, final ArgumentParser parser, final String commandString) throws ParsingException
    {
        RegisteredMethod match = null;

        for (final RegisteredMethod registeredMethod : this.registeredMethods)
        {
            if (! commandString.equalsIgnoreCase(registeredMethod.getPrefix()) && ! commandString.toLowerCase().startsWith(registeredMethod.getPrefix() + " "))
            {
                continue;
            }

            if (match == null)
            {
                match = registeredMethod;
                continue;
            }

            if (registeredMethod.getLevel() > match.getLevel())
            {
                match = registeredMethod;
            }
        }

        if (match == null)
        {
            throw new UnknownCommandException(commandString.contains(" ") ? commandString.substring(0, commandString.indexOf(' ')) : commandString);
        }

        final ParsingResult result = parser.parse(match.getArgumentDefinitions(), match.getFlagDefinitions(), commandString.substring(match.getPrefix().length()));

        final Object[] arguments = new Object[match.getMethod().getParameterCount()];

        for (int i = 0; i < arguments.length; i++)
        {
            arguments[i] = match.getParameterResolvers()[i].resolve(match, sender, result);
        }

        try
        {
            match.getMethod().invoke(match.getListener(), arguments);
        }
        catch (final IllegalAccessException e)
        {
            throw new RuntimeException(e);
        }
        catch (final InvocationTargetException e)
        {
            throw new RuntimeException("An exception occurred wihle executing command " + commandString, e.getTargetException());
        }
    }
}
