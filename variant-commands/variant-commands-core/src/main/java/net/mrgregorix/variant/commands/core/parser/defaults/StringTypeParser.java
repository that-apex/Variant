package net.mrgregorix.variant.commands.core.parser.defaults;

import net.mrgregorix.variant.commands.api.annotation.types.Raw;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.TypeParser;
import net.mrgregorix.variant.commands.api.parser.UseDefaultTypeException;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.api.parser.exception.StringSyntaxErrorException;

/**
 * A {@link TypeParser} for the {@link String type}
 */
public class StringTypeParser extends AbstractDefaultTypeParser<String, StringTypeParser>
{
    @Override
    public Class<String> getBaseType()
    {
        return String.class;
    }

    @Override
    public String parseType(final ArgumentParser argumentParser, final StringParser parser, final TypeDefinition typeDefinition) throws ParsingException
    {
        if (typeDefinition.getAnnotation(Raw.class) != null)
        {
            return parser.readCharactersTo(parser.getLength());
        }

        if (parser.peekCharacter() != '"')
        {
            final String string = parser.readUntil(' ');

            if ("_".equals(string))
            {
                throw new UseDefaultTypeException();
            }

            return string;
        }
        parser.readCharacter();

        final StringBuilder output = new StringBuilder();

        while (true)
        {
            if (parser.isFinished())
            {
                throw new StringSyntaxErrorException("No ending \"");
            }

            final char character = parser.readCharacter();
            if (character == '\\')
            {
                if (parser.isFinished())
                {
                    throw new StringSyntaxErrorException("Invalid escape sequence");
                }

                output.append(parser.readCharacter());
                continue;
            }

            if (character == '"')
            {
                break;
            }

            output.append(character);
        }

        return output.toString();
    }

    @Override
    public String parseDefaultValue(final ArgumentParser argumentParser, final TypeDefinition defaultValue)
    {
        return defaultValue.defaultValue();
    }
}
