package net.mrgregorix.variant.commands.api.parser;

import java.util.Collection;

import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

public interface ArgumentParser
{
    @CollectionMayBeImmutable
    Collection<TypeParser<?, ?>> getTypeParsers();

    boolean registerTypeParser(TypeParser<?, ?> parser);

    boolean unregisterTypeParser(TypeParser<?, ?> parser);

    /**
     * Gets parser that is able to parse the given type
     *
     * @param type type to find the parser for
     * @param <T>  type to find the parser for
     *
     * @return the found parser
     *
     * @throws IllegalArgumentException when no parser is found
     */
    <T> TypeParser<T, ?> getParserFor(Class<T> type);

    /**
     * Parsers arguments from the given string using the given definitions
     *
     * @param argumentDefinitions {@link TypeDefinition} of the arguments
     * @param flagDefinitions     {@link TypeDefinition} of the flags
     * @param string              string to parse
     *
     * @return result of parsing
     *
     * @throws ParsingException when an error occurred while parsing the string
     */
    ParsingResult parse(TypeDefinition[] argumentDefinitions, TypeDefinition[] flagDefinitions, String string) throws ParsingException;
}
