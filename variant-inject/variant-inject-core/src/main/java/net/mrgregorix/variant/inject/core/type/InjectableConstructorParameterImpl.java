package net.mrgregorix.variant.inject.core.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Preconditions;
import net.mrgregorix.variant.inject.api.type.InjectableConstructorParameter;

/**
 * A simple implementation of {@link InjectableConstructorParameter}
 */
public class InjectableConstructorParameterImpl implements InjectableConstructorParameter
{
    private final Parameter    parameter;
    private       Annotation[] annotations         = null;
    private       Annotation[] declaredAnnotations = null;

    /**
     * Constructs new InjectableConstructorParameterImpl
     *
     * @param parameter parameter
     */
    public InjectableConstructorParameterImpl(final Parameter parameter)
    {
        this.parameter = parameter;
        Preconditions.checkArgument(parameter.getDeclaringExecutable() instanceof Constructor, "only constructor parameters are allowed");
    }

    @Override
    public Parameter getHandle()
    {
        return this.parameter;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
    {
        final T parameterAnnotation = this.parameter.getAnnotation(annotationClass);
        if (parameterAnnotation != null)
        {
            return parameterAnnotation;
        }

        return this.parameter.getDeclaringExecutable().getAnnotation(annotationClass);
    }

    private Annotation[] computeAnnotations(final Annotation[] parameter, final Annotation[] executable)
    {
        final Set<Annotation> annotationSet = new HashSet<>(parameter.length);
        Collections.addAll(annotationSet, parameter);

        for (final Annotation annotation : executable)
        {
            if (annotationSet.stream().anyMatch(a -> a.annotationType() == annotation.annotationType()))
            {
                continue;
            }

            annotationSet.add(annotation);
        }

        return annotationSet.toArray(new Annotation[0]);
    }

    @Override
    public Annotation[] getAnnotations()
    {
        if (this.annotations == null)
        {
            this.annotations = this.computeAnnotations(this.parameter.getAnnotations(), this.parameter.getDeclaringExecutable().getAnnotations());
        }

        return this.annotations;
    }

    @Override
    public Annotation[] getDeclaredAnnotations()
    {
        if (this.declaredAnnotations == null)
        {
            this.declaredAnnotations = this.computeAnnotations(this.parameter.getDeclaredAnnotations(), this.parameter.getDeclaringExecutable().getDeclaredAnnotations());
        }

        return this.declaredAnnotations;
    }

    @Override
    public Class<?> getType()
    {
        return this.parameter.getType();
    }

    @Override
    public Class<?> getDeclaringType()
    {
        return this.parameter.getDeclaringExecutable().getDeclaringClass();
    }
}
