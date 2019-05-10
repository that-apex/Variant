package net.mrgregorix.variant.inject.core.instantiation;

import java.lang.reflect.Field;
import java.util.Optional;

import net.mrgregorix.variant.api.instantiation.AfterInstantiationHandler;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.inject.api.injector.InjectionValueProvider;
import net.mrgregorix.variant.inject.core.type.impl.InjectableFieldImpl;
import net.mrgregorix.variant.utils.annotation.AnnotationUtils;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

public class FieldInjectionAfterInstantiationHandler extends AbstractModifiablePrioritizable<AfterInstantiationHandler> implements AfterInstantiationHandler
{
    private final InjectionValueProvider injectionValueProvider;

    public FieldInjectionAfterInstantiationHandler(InjectionValueProvider injectionValueProvider)
    {
        this.injectionValueProvider = injectionValueProvider;
    }

    @Override
    public void afterInstantiationHandler(final Proxy createdObject)
    {
        for (final Field field : createdObject.getProxyBaseClass().getDeclaredFields())
        {
            if (AnnotationUtils.getAnnotation(field, Inject.class) == null)
            {
                continue;
            }

            final Optional<Object> value = this.injectionValueProvider.provideValueForInjection(new InjectableFieldImpl(field));

            if (! value.isPresent())
            {
                throw new IllegalArgumentException("no value to inject into " + field + " was found");
            }

            field.setAccessible(true);
            try
            {
                field.set(createdObject, value.get());
            }
            catch (final IllegalAccessException e)
            {
                throw new RuntimeException("This should never happen", e);
            }
        }
    }
}
