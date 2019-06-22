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

    private List<String> trueValues;
    private List<String> falseValues;

    public BooleanTypeParser()
    {
        this(TRUE_VALUES, FALSE_VALUES);
    }

    public BooleanTypeParser(final List<String> trueValues, final List<String> falseValues)
    {
        super(Boolean.class, boolean.class);
        this.trueValues = trueValues;
        this.falseValues = falseValues;
    }

    public List<String> getTrueValues()
    {
        return this.trueValues;
    }

    public List<String> getFalseValues()
    {
        return this.falseValues;
    }

    public void setTrueValues(final List<String> trueValues)
    {
        this.trueValues = trueValues;
    }

    public void setFalseValues(final List<String> falseValues)
    {
        this.falseValues = falseValues;
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

        if (this.trueValues.contains(stringValue.toLowerCase()))
        {
            return true;
        }
        else if (this.falseValues.contains(stringValue.toLowerCase()))
        {
            return false;
        }

        throw new ValueSyntaxException("Not a boolean: " + stringValue, boolean.class);
    }

    @Override
    public Boolean parseDefaultValue(final ArgumentParser argumentParser, final TypeDefinition defaultValue) throws ParsingException
    {
        if (! defaultValue.getType().isPrimitive() && defaultValue.defaultValue().length() == 0)
        {
            return null;
        }

        return this.fromString(defaultValue.defaultValue());
    }
}
