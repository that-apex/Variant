package net.mrgregorix.variant.commands.api.parser;

/**
 * This exception should only be thrown from {@link TypeParser#parseType(ArgumentParser, StringParser, TypeDefinition)}.
 * <p>Throwing this exception will cause the parser to use the default value for the current parsed definition</p>
 */
public class UseDefaultTypeException extends RuntimeException
{
    public UseDefaultTypeException()
    {
    }

    @Override
    public Throwable fillInStackTrace()
    {
        return this;
    }
}
