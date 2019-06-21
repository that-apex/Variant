package net.mrgregorix.variant.commands.api.parser;

import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.utils.priority.ModifiablePrioritizable;

public interface TypeParser <T, P extends TypeParser<T, P>> extends ModifiablePrioritizable<P>
{
    Class<T> getBaseType();

    default boolean matches(Class<?> type)
    {
        return this.getBaseType().isAssignableFrom(type);
    }

    T parseType(ArgumentParser argumentParser, StringParser parser, TypeDefinition typeDefinition) throws ParsingException;

    T parseDefaultValue(ArgumentParser argumentParser, TypeDefinition defaultValue) throws ParsingException;
}
