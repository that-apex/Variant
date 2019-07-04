package net.mrgregorix.variant.commands.core.parser.defaults;

/**
 * Parsers for primitive values and their wrapper types
 *
 * @param <T> type of the parser
 */
public abstract class PrimitiveTypeParser <T> extends AbstractDefaultTypeParser<T, PrimitiveTypeParser<T>>
{
    private final Class<T> wrapperType;
    private final Class<?> primitiveType;

    /**
     * Creates a new PrimitiveTypeParser
     *
     * @param wrapperType   wrapper type of the parsed primitive
     * @param primitiveType primitive type of this parser
     */
    protected PrimitiveTypeParser(final Class<T> wrapperType, final Class<?> primitiveType)
    {
        this.wrapperType = wrapperType;
        this.primitiveType = primitiveType;
    }

    /**
     * Returns wrapper type of the parsed primitive.
     *
     * @return wrapper type of the parsed primitive
     */
    public Class<T> getWrapperType()
    {
        return this.wrapperType;
    }

    /**
     * Returns primitive type of this parser.
     *
     * @return primitive type of this parser
     */
    public Class<?> getPrimitiveType()
    {
        return this.primitiveType;
    }

    @Override
    public Class<T> getBaseType()
    {
        return this.wrapperType;
    }

    @Override
    public boolean matches(final Class<?> type)
    {
        return this.primitiveType == type || this.wrapperType == type;
    }
}
