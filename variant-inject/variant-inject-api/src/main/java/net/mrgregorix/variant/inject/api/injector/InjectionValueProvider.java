package net.mrgregorix.variant.inject.api.injector;

import java.util.Optional;

import net.mrgregorix.variant.inject.api.type.InjectableElement;

public interface InjectionValueProvider
{
    /**
     * Provide a value for injecting.
     *
     * @param element an element that a value is being injected into
     *
     * @return a value for the injected type or an empty optional if there's no value
     */
    Optional<Object> provideValueForInjection(InjectableElement element);
}
