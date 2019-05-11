package net.mrgregorix.variant.inject.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import net.mrgregorix.variant.inject.api.type.InjectableField;

/**
 * A simple implementation of {@link InjectableField}
 */
public class InjectableFieldImpl implements InjectableField
{
    private final Field handle;

    /**
     * Constructs new InjectableFieldImpl
     *
     * @param field field
     */
    public InjectableFieldImpl(final Field field)
    {
        this.handle = field;
    }

    @Override
    public Field getHandle()
    {
        return this.handle;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
    {
        return this.handle.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations()
    {
        return this.handle.getDeclaredAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations()
    {
        return this.handle.getDeclaredAnnotations();
    }

    @Override
    public Class<?> getType()
    {
        return this.handle.getType();
    }

    @Override
    public Class<?> getDeclaringType()
    {
        return this.handle.getDeclaringClass();
    }


}
