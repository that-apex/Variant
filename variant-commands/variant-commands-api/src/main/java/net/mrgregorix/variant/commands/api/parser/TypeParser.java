package net.mrgregorix.variant.commands.api.parser;

import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.utils.priority.ModifiablePrioritizable;

/**
 * Parser for a single type
 *
 * @param <T> type that this parser can parse
 * @param <P> this type parser type
 */
public interface TypeParser <T, P extends TypeParser<T, P>> extends ModifiablePrioritizable<P>
{
    /**
     * Returns the type that this parser can parse.
     *
     * @return the type that this parser can parse
     */
    Class<T> getBaseType();

    /**
     * Returns whether or not the given type can be parsed by this parser
     *
     * @param type type to check
     *
     * @return whether or not this parser can parse the given type
     */
    default boolean matches(Class<?> type)
    {
        return this.getBaseType().isAssignableFrom(type);
    }

    /**
     * Parses a value from the given parser
     *
     * @param argumentParser {@link ArgumentParser} that requested this parsing
     * @param parser         parser to use for parsing the type
     * @param typeDefinition definition of the parsed type
     *
     * @return the parsed value
     *
     * @throws ParsingException when an error occurred while parsing the value
     */
    T parseType(ArgumentParser argumentParser, StringParser parser, TypeDefinition typeDefinition) throws ParsingException;

    /**
     * Parses a default value from the given definition. (See {@link TypeDefinition#defaultValue()}
     *
     * @param argumentParser {@link ArgumentParser} that requested this parsing
     * @param defaultValue   definition of the value.
     *
     * @return the parsed default value
     *
     * @throws ParsingException when an error occurred while parsing the default value
     */
    T parseDefaultValue(ArgumentParser argumentParser, TypeDefinition defaultValue) throws ParsingException;
}
