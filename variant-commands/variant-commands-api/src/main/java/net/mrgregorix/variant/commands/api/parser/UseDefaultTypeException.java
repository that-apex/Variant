package net.mrgregorix.variant.commands.api.parser;

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
