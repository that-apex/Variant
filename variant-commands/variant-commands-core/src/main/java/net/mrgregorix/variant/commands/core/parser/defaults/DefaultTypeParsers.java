package net.mrgregorix.variant.commands.core.parser.defaults;

import java.util.ArrayList;
import java.util.List;

import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.TypeParser;
import net.mrgregorix.variant.commands.core.parser.defaults.primitive.BooleanTypeParser;
import net.mrgregorix.variant.commands.core.parser.defaults.primitive.CharacterTypeParser;
import net.mrgregorix.variant.commands.core.parser.defaults.primitive.NumberTypeParser;

/**
 * Helper class for registering default type parsers
 */
public class DefaultTypeParsers
{
    private static final List<TypeParser<?, ?>> PARSERS = new ArrayList<>();

    static
    {
        PARSERS.add(new NumberTypeParser<>(Byte.class, byte.class, false, Byte::parseByte));
        PARSERS.add(new NumberTypeParser<>(Short.class, short.class, false, Short::parseShort));
        PARSERS.add(new NumberTypeParser<>(Integer.class, int.class, false, Integer::parseInt));
        PARSERS.add(new NumberTypeParser<>(Long.class, long.class, false, Long::parseLong));
        PARSERS.add(new NumberTypeParser<>(Float.class, float.class, true, (s, i) -> Float.parseFloat(s)));
        PARSERS.add(new NumberTypeParser<>(Double.class, double.class, true, (s, i) -> Double.parseDouble(s)));
        PARSERS.add(new BooleanTypeParser());
        PARSERS.add(new CharacterTypeParser());
        PARSERS.add(new StringTypeParser());
        PARSERS.add(new CollectionTypeParser());
        PARSERS.add(new HelpPageTypeParser());
    }

    /**
     * Registers all the default type parsers to the given argument parser
     *
     * @param argumentParser {@link ArgumentParser} to registered the default types for
     */
    public static void register(final ArgumentParser argumentParser)
    {
        PARSERS.forEach(argumentParser.getTypeParserRegistry()::register);
    }
}
