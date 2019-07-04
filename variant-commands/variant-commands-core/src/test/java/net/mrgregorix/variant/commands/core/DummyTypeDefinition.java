package net.mrgregorix.variant.commands.core;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import net.mrgregorix.variant.commands.api.parser.TypeDefinition;

public class DummyTypeDefinition implements TypeDefinition
{
    private final String       name;
    private final Class<?>     type;
    private final Annotation[] annotations;
    private final String       defaultValue;

    public DummyTypeDefinition(final String name, final Class<?> type, final Annotation[] annotations)
    {
        this(name, type, annotations, null);
    }

    public DummyTypeDefinition(final String name, final Class<?> type, final Annotation[] annotations, final String defaultValue)
    {
        this.name = name;
        this.type = type;
        this.annotations = annotations;
        this.defaultValue = defaultValue;
    }


    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public Class<?> getType()
    {
        return this.type;
    }

    @Override
    public Annotation[] getDeclaredAnnotations()
    {
        return this.annotations;
    }

    @Override
    public boolean isRequired()
    {
        return true;
    }

    @Override
    public String defaultValue()
    {
        return this.defaultValue;
    }

    @Override
    public String toString()
    {
        return "DummyTypeDefinition{" +
               "type=" + this.type +
               ", annotations=" + Arrays.toString(this.annotations) +
               '}';
    }
}
