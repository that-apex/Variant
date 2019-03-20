package net.mrgregorix.variant.inject.api.injector;

import java.util.Optional;

import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.inject.api.type.InjectableElement;

/**
 * A custom injector is a base provider for injected values.
 * <p>
 * Such injector is responsible for provide a value when a new managed object instance is created.
 */
public interface CustomInjector
{
    /**
     * Is this a singleton injector? If so only one instance if this injector is allowed to be registered in an injector.
     *
     * @return if this is a singleton injector
     */
    boolean isSingletonInjector();

    /**
     * Provide a value for injecting.
     *
     * @param injector injector that requested injection
     * @param element  an element that
     *
     * @return a value for the injected type or an empty optional if there's no value
     */
    Optional<Object> provideValue(VariantInjector injector, InjectableElement element);
}
