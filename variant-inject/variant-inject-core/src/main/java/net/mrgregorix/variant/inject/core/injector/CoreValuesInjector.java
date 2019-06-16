package net.mrgregorix.variant.inject.core.injector;

import java.util.Optional;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.inject.api.injector.CustomInjector;
import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;
import net.mrgregorix.variant.utils.priority.PriorityConstants;

public class CoreValuesInjector extends AbstractModifiablePrioritizable<CustomInjector> implements CustomInjector
{
    private final Variant variant;

    public CoreValuesInjector(final Variant variant)
    {
        this.variant = variant;
        this.setPriority(PriorityConstants.HIGHEST);
    }

    @Override
    public boolean isSingletonInjector()
    {
        return true;
    }

    @Override
    public Optional<Object> provideValueForInjection(final InjectableElement element)
    {
        if (Variant.class.isAssignableFrom(element.getType()))
        {
            return Optional.of(this.variant);
        }

        return Optional.empty();
    }
}
