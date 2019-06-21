package net.mrgregorix.variant.commands.core.parser.defaults.primitive;

import java.util.Arrays;
import java.util.List;

import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.UseDefaultTypeException;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.api.parser.exception.ValueSyntaxException;
import net.mrgregorix.variant.commands.core.parser.defaults.PrimitiveTypeParser;

public class BooleanTypeParser extends PrimitiveTypeParser<Boolean>
{
    private static final List<String> TRUE_VALUES  = Arrays.asList("true", "yes", "y", "1");
    private static final List<String> FALSE_VALUES = Arrays.asList("false", "no", "n", "0");

    public BooleanTypeParser()
    {
        super(Boolean.class, boolean.class);
    }

    @Override
    public Boolean parseType(final ArgumentParser argumentParser, final StringParser parser, final TypeDefinition typeDefinition) throws ParsingException
    {
        final String stringValue = argumentParser.getParserFor(String.class).parseType(argumentParser, parser, typeDefinition).toLowerCase();

        return this.fromString(stringValue);
    }

    private Boolean fromString(final String stringValue) throws ValueSyntaxException
    {
        if (stringValue.equals("_"))
        {
            throw new UseDefaultTypeException();
        }

        if (TRUE_VALUES.contains(stringValue.toLowerCase()))
        {
            return true;
        }
        else if (FALSE_VALUES.contains(stringValue.toLowerCase()))
        {
            return false;
        }

        throw new ValueSyntaxException("Not a boolean: " + stringValue, boolean.class);
    }

    @Override
    public Boolean parseDefaultValue(final ArgumentParser argumentParser, final TypeDefinition defaultValue) throws ParsingException
    {
        return this.fromString(defaultValue.defaultValue());
    }
}
