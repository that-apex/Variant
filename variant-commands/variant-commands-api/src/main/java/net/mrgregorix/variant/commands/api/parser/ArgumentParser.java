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

    <T> TypeParser<T, ?> getParserFor(Class<T> type);

    ParsingResult parse(TypeDefinition[] argumentDefinitions, TypeDefinition[] flagDefinitions, String string) throws ParsingException;
}
