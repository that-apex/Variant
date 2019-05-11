package net.mrgregorix.variant.inject.core.instantiation;

import java.util.Optional;

import net.mrgregorix.variant.api.instantiation.AfterInstantiationHandler;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.inject.api.type.InjectableField;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

public class FieldInjectionAfterInstantiationHandler extends AbstractModifiablePrioritizable<AfterInstantiationHandler> implements AfterInstantiationHandler
{
    private final VariantInjector variantInjector;

    public FieldInjectionAfterInstantiationHandler(final VariantInjector variantInjector)
    {
        this.variantInjector = variantInjector;
    }

    @Override
    public void afterInstantiationHandler(final Proxy createdObject)
    {
        for (final InjectableField field : this.variantInjector.getElements(createdObject.getProxyBaseClass(), InjectableField.class))
        {
            final Optional<Object> value = this.variantInjector.provideValueForInjection(field);

            if (! value.isPresent())
            {
                throw new IllegalArgumentException("no value to inject into " + field + " was found");
            }

            field.getHandle().setAccessible(true);

            try
            {
                field.getHandle().set(createdObject, value.get());
            }
            catch (final IllegalAccessException e)
            {
                throw new RuntimeException("This should never happen", e);
            }
        }
    }
}
