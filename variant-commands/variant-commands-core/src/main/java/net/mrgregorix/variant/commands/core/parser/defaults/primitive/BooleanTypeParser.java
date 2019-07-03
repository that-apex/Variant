package net.mrgregorix.variant.commands.core.parser.defaults.primitive;

import java.util.Arrays;
import java.util.List;

import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.TypeParser;
import net.mrgregorix.variant.commands.api.parser.UseDefaultTypeException;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.api.parser.exception.ValueSyntaxException;
import net.mrgregorix.variant.commands.core.parser.defaults.PrimitiveTypeParser;

/**
 * {@link TypeParser} parser for the {@code boolean} primitive
 */
public class BooleanTypeParser extends PrimitiveTypeParser<Boolean>
{
    private static final List<String> TRUE_VALUES  = Arrays.asList("true", "yes", "y", "1");
    private static final List<String> FALSE_VALUES = Arrays.asList("false", "no", "n", "0");

    private List<String> trueValues;
    private List<String> falseValues;

    /**
     * Constructs a simple BooleanTypeParser with predefined values
     */
    public BooleanTypeParser()
    {
        this(TRUE_VALUES, FALSE_VALUES);
    }

    /**
     * Constructs a BooleanTypeParser with the given values.
     *
     * @param trueValues  values to be interpreted as {@code true}
     * @param falseValues values to be interpreted as {@code false}
     */
    public BooleanTypeParser(final List<String> trueValues, final List<String> falseValues)
    {
        super(Boolean.class, boolean.class);
        this.trueValues = trueValues;
        this.falseValues = falseValues;
    }

    /**
     * Returns the values that are interpreted as a {@code true}.
     *
     * @return the values that are interpreted as a {@code true}
     */
    public List<String> getTrueValues()
    {
        return this.trueValues;
    }

    /**
     * Returns the values that are interpreted as a {@code false}.
     *
     * @return the values that are interpreted as a {@code false}
     */
    public List<String> getFalseValues()
    {
        return this.falseValues;
    }

    /**
     * Sets the values that are interpreted as a {@code true}.
     *
     * @param trueValues the values that are interpreted as a {@code true}
     */
    public void setTrueValues(final List<String> trueValues)
    {
        this.trueValues = trueValues;
    }

    /**
     * Sets the values that are interpreted as a {@code false}.
     *
     * @param falseValues the values that are interpreted as a {@code false}
     */
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
