package net.mrgregorix.variant.inject.core.injector;

import java.util.Optional;

import net.mrgregorix.variant.inject.api.injector.CustomInjector;
import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

public class TestInjector extends AbstractModifiablePrioritizable<CustomInjector> implements CustomInjector
{
    @Override
    public boolean isSingletonInjector()
    {
        return false;
    }

    @Override
    public Optional<Object> provideValueForInjection(InjectableElement element)
    {
        return Optional.empty();
    }
}
