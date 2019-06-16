package net.mrgregorix.variant.inject.core.injector;

import java.util.Optional;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.inject.api.injector.CustomInjector;
import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;
import net.mrgregorix.variant.utils.priority.PriorityConstants;

public class VariantModuleInjector extends AbstractModifiablePrioritizable<CustomInjector> implements CustomInjector
{
    private final Variant variant;

    public VariantModuleInjector(Variant variant)
    {
        this.variant = variant;
        this.setPriority(PriorityConstants.HIGHEST);
    }

    @Override
    public boolean isSingletonInjector()
    {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<Object> provideValueForInjection(final InjectableElement element)
    {
        if (! VariantModule.class.isAssignableFrom(element.getType()))
        {
            return Optional.empty();
        }

        return Optional.of(this.variant.getModule((Class<? extends VariantModule>) element.getType()));
    }
}
