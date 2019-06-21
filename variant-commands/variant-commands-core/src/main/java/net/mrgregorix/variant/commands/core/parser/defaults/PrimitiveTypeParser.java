package net.mrgregorix.variant.commands.core.parser.defaults;

public abstract class PrimitiveTypeParser <T> extends AbstractDefaultTypeParser<T, PrimitiveTypeParser<T>>
{
    private final Class<T> wrapperType;
    private final Class<?> primitiveType;

    protected PrimitiveTypeParser(final Class<T> wrapperType, final Class<?> primitiveType)
    {
        this.wrapperType = wrapperType;
        this.primitiveType = primitiveType;
    }

    public Class<T> getWrapperType()
    {
        return this.wrapperType;
    }

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
