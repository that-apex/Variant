package net.mrgregorix.variant.commands.core.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.ParsingResult;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.TypeParser;
import net.mrgregorix.variant.commands.api.parser.UseDefaultTypeException;
import net.mrgregorix.variant.commands.api.parser.exception.NoDefaultValueException;
import net.mrgregorix.variant.commands.api.parser.exception.NoFlagFoundException;
import net.mrgregorix.variant.commands.api.parser.exception.NotEnoughParametersException;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.api.parser.exception.TooManyParametersException;
import net.mrgregorix.variant.commands.core.parser.defaults.DefaultTypeParsers;
import net.mrgregorix.variant.commands.core.parser.result.ParsingResultImpl;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;

public class ArgumentParserImpl implements ArgumentParser
{
    private final CollectionWithImmutable<TypeParser<?, ?>, ImmutableList<TypeParser<?, ?>>> parsers = WrappedCollectionWithImmutable.withImmutableList(new ArrayList<>());

    public ArgumentParserImpl()
    {
        DefaultTypeParsers.register(this);
    }

    @Override
    public Collection<TypeParser<?, ?>> getTypeParsers()
    {
        return this.parsers.getImmutable();
    }

    @Override
    public boolean registerTypeParser(final TypeParser<?, ?> parser)
    {
        return this.parsers.add(parser);
    }

    @Override
    public boolean unregisterTypeParser(final TypeParser<?, ?> parser)
    {
        return this.parsers.remove(parser);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeParser<T, ?> getParserFor(final Class<T> type)
    {
        return this.parsers
            .stream()
            .filter(it -> it.matches(type))
            .map(parser -> (TypeParser<T, ?>) parser)
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("No parser found for type " + type));
    }

    @Override
    public ParsingResult parse(final TypeDefinition[] argumentDefinitions, final TypeDefinition[] flagDefinitions, final String string) throws ParsingException
    {
        final StringParser stringParser = new StringParserImpl(string);

        final Map<TypeDefinition, Object> flags = this.parseFlags(flagDefinitions, stringParser);
        final Collection<Object> objects = this.parseArguments(argumentDefinitions, stringParser);

        if (! stringParser.isFinished())
        {
            throw new TooManyParametersException();
        }

        return new ParsingResultImpl(string, objects.toArray(new Object[0]), flags);
    }

    private Map<TypeDefinition, Object> parseFlags(final TypeDefinition[] flagDefinitions, final StringParser stringParser) throws ParsingException
    {
        final Map<TypeDefinition, Object> flags = new HashMap<>();
        stringParser.skipAll(' ');

        if (stringParser.isFinished())
        {
            return flags;
        }

        while (stringParser.peekCharacter() == '-')
        {
            stringParser.readCharacter();
            final String flagName = stringParser.readUntil(' ');
            stringParser.skipAll(' ');

            final TypeDefinition flagDefinition = Arrays.stream(flagDefinitions)
                                                        .filter(it -> it.getName().equals(flagName))
                                                        .findFirst()
                                                        .orElseThrow(() -> new NoFlagFoundException(flagName));

            if (flagDefinition.getType() == boolean.class)
            {
                flags.put(flagDefinition, true);
                continue;
            }

            final Object type = this.getParserFor(flagDefinition.getType()).parseType(this, stringParser, flagDefinition);
            flags.put(flagDefinition, type);

            stringParser.skipAll(' ');
        }

        for (final TypeDefinition flagDefinition : flagDefinitions)
        {
            if (flags.containsKey(flagDefinition))
            {
                continue;
            }

            final Object defaultValue = this.getParserFor(flagDefinition.getType()).parseDefaultValue(this, flagDefinition);
            flags.put(flagDefinition, defaultValue);
        }

        return flags;
    }

    private Collection<Object> parseArguments(final TypeDefinition[] argumentDefinitions, final StringParser stringParser) throws ParsingException
    {
        final Collection<Object> objects = new ArrayList<>(argumentDefinitions.length);

        for (final TypeDefinition definition : argumentDefinitions)
        {
            try
            {
                stringParser.skipAll(' ');

                final TypeParser<?, ?> parser = this.getParserFor(definition.getType());

                if (stringParser.isFinished())
                {
                    if (! definition.isRequired())
                    {
                        objects.add(parser.parseDefaultValue(this, definition));
                        continue;
                    }
                    else
                    {
                        throw new NotEnoughParametersException("Not enough parameters");
                    }
                }

                Object type;
                try
                {
                    type = parser.parseType(this, stringParser, definition);

                    if (! stringParser.isFinished())
                    {
                        if (stringParser.readCharacter() != ' ')
                        {
                            throw new ParsingException("Space is required between parameters");
                        }
                    }
                }
                catch (final UseDefaultTypeException e)
                {
                    if (definition.defaultValue() == null)
                    {
                        throw new NoDefaultValueException("No default value for this definition");
                    }

                    type = parser.parseDefaultValue(this, definition);
                }

                objects.add(type);
            }
            catch (final ParsingException e)
            {
                e.setDefinition(definition);
                throw e;
            }
        }

        return objects;
    }
}
