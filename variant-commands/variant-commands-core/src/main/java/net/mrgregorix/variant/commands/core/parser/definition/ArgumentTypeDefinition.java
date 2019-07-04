package net.mrgregorix.variant.commands.core.parser.definition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import net.mrgregorix.variant.commands.api.annotation.Argument;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;

/**
 * A {@link TypeDefinition} for a command argument.
 */
public class ArgumentTypeDefinition implements TypeDefinition
{
    private final Parameter parameter;
    private final Argument  argument;

    /**
     * Creates a new FlagTypeDefinition
     *
     * @param parameter parameter annotated with {@link Argument}
     * @param argument  the {@link Argument} annotation
     */
    public ArgumentTypeDefinition(final Parameter parameter, final Argument argument)
    {
        this.parameter = parameter;
        this.argument = argument;
    }

    @Override
    public String getName()
    {
        return this.argument.name().isEmpty() ? this.parameter.getName() : this.argument.name();
    }

    @Override
    public Class<?> getType()
    {
        return this.parameter.getType();
    }

    @Override
    public Annotation[] getDeclaredAnnotations()
    {
        return this.parameter.getDeclaredAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(final Class<T> type)
    {
        return this.parameter.getDeclaredAnnotation(type);
    }

    @Override
    public boolean isRequired()
    {
        return this.argument.required();
    }

    @Override
    public String defaultValue()
    {
        return this.argument.defaultValue();
    }

    @Override
    public String toString()
    {
        return "ArgumentTypeDefinition{" +
               "method=" + this.parameter.getDeclaringExecutable() + ", " +
               "parameter=" + this.parameter +
               '}';
    }
}
