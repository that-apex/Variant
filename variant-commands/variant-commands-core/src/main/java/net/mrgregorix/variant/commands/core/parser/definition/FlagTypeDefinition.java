package net.mrgregorix.variant.commands.core.parser.definition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

import net.mrgregorix.variant.commands.api.annotation.Flag;
import net.mrgregorix.variant.commands.api.parser.TypeDefinition;

public class FlagTypeDefinition implements TypeDefinition
{
    private final Parameter parameter;
    private final Flag      flag;

    public FlagTypeDefinition(final Parameter parameter, final Flag flag)
    {
        this.parameter = parameter;
        this.flag = flag;
    }

    @Override
    public String getName()
    {
        return this.flag.name().isEmpty() ? this.parameter.getName() : this.flag.name();
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
        return false;
    }

    @Override
    public String defaultValue()
    {
        return this.parameter.getType() == boolean.class ? "false" : this.flag.defaultValue();
    }

    @Override
    public String toString()
    {
        return "FlagTypeDefinition{" +
               "method=" + this.parameter.getDeclaringExecutable() + ", " +
               "parameter=" + this.parameter +
               '}';
    }
}
