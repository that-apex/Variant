package net.mrgregorix.variant.inject.api.injector;

import net.mrgregorix.variant.utils.priority.ModifiablePrioritizable;

/**
 * A custom injector is a base provider for injected values.
 * <p>
 * Such injector is responsible for provide a value when a new managed object instance is created.
 */
public interface CustomInjector extends InjectionValueProvider, ModifiablePrioritizable<CustomInjector>
{
    /**
     * Is this a singleton injector? If so only one instance if this injector is allowed to be registered in an injector.
     *
     * @return if this is a singleton injector
     */
    boolean isSingletonInjector();
}
