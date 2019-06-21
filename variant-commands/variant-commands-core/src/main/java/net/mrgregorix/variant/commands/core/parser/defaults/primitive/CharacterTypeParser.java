package net.mrgregorix.variant.commands.core.parser.defaults.primitive;

import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.api.parser.exception.ValueSyntaxException;
import net.mrgregorix.variant.commands.core.parser.defaults.PrimitiveTypeParser;

public class CharacterTypeParser extends PrimitiveTypeParser<Character>
{
    public CharacterTypeParser()
    {
        super(Character.class, char.class);
    }

    @Override
    public Character parseType(final ArgumentParser argumentParser, final StringParser parser, final TypeDefinition typeDefinition)
    {
        return parser.readCharacter();
    }

    @Override
    public Character parseDefaultValue(final ArgumentParser argumentParser, final TypeDefinition defaultValue) throws ParsingException
    {
        if (defaultValue.defaultValue().length() != 1)
        {
            throw new ValueSyntaxException("'" + defaultValue.defaultValue() + "' is not a character", char.class);
        }

        return defaultValue.defaultValue().charAt(0);
    }
}
