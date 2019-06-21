package net.mrgregorix.variant.commands.core.parser.defaults.primitive;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import com.google.common.primitives.Chars;
import net.mrgregorix.variant.commands.api.annotation.types.Radix;
import net.mrgregorix.variant.commands.api.parser.ArgumentParser;
import net.mrgregorix.variant.commands.api.parser.StringParser;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;
import net.mrgregorix.variant.commands.api.parser.UseDefaultTypeException;
import net.mrgregorix.variant.commands.api.parser.exception.ParsingException;
import net.mrgregorix.variant.commands.api.parser.exception.RadixNotSupported;
import net.mrgregorix.variant.commands.api.parser.exception.ValueSyntaxException;
import net.mrgregorix.variant.commands.core.parser.defaults.PrimitiveTypeParser;

public class NumberTypeParser <N extends Number> extends PrimitiveTypeParser<N>
{
    private static final List<Character> ALLOWED_CHARACTERS_INTEGER        = Chars.asList("0123456789-".toCharArray());
    private static final List<Character> ALLOWED_CHARACTERS_FLOATING_POINT = Chars.asList("-.,".toCharArray());

    private final boolean                        isFloatingPoint;
    private final BiFunction<String, Integer, N> parseInteger;

    public NumberTypeParser(final Class<N> wrapperType, final Class<?> primitiveType, final boolean isFloatingPoint, final BiFunction<String, Integer, N> parseInteger)
    {
        super(wrapperType, primitiveType);

        this.isFloatingPoint = isFloatingPoint;
        this.parseInteger = parseInteger;
    }

    private int radix(final TypeDefinition definition) throws RadixNotSupported
    {
        final int radix = Optional.ofNullable(definition.getAnnotation(Radix.class)).map(Radix::value).orElse(10);

        if (radix != 10 && this.isFloatingPoint)
        {
            throw new RadixNotSupported("Non-10 radix is not supported for floating points");
        }

        return radix;
    }

    @Override
    public N parseType(final ArgumentParser argumentParser, final StringParser parser, final TypeDefinition typeDefinition) throws ParsingException
    {
        if (parser.peekCharacter() == '_')
        {
            parser.readCharacter();
            throw new UseDefaultTypeException();
        }

        final StringBuilder stringValue = new StringBuilder();
        boolean allowPoint = this.isFloatingPoint;

        while (!parser.isFinished() && (ALLOWED_CHARACTERS_INTEGER.contains(parser.peekCharacter()) || (allowPoint && ALLOWED_CHARACTERS_FLOATING_POINT.contains(parser.peekCharacter()))))
        {
            if (ALLOWED_CHARACTERS_FLOATING_POINT.contains(parser.peekCharacter()))
            {
                allowPoint = false;
            }

            stringValue.append(parser.readCharacter());
        }

        if (stringValue.length() == 0)
        {
            throw new ValueSyntaxException(parser.readUntil(' '), this.getPrimitiveType());
        }

        try
        {
            return this.parseInteger.apply(stringValue.toString(), 10);
        }
        catch (final NumberFormatException e)
        {
            throw new ValueSyntaxException(e, this.getPrimitiveType());
        }
    }

    @Override
    public N parseDefaultValue(final ArgumentParser argumentParser, final TypeDefinition defaultValue) throws ParsingException
    {
        try
        {
            return this.parseInteger.apply(defaultValue.defaultValue(), this.radix(defaultValue));
        }
        catch (final NumberFormatException e)
        {
            throw new ValueSyntaxException(e, this.getPrimitiveType());
        }
    }
}
