package net.mrgregorix.variant.inject.api.injector;

import java.util.Set;

import net.mrgregorix.variant.inject.api.VariantInjector;

/**
 * A {@link CustomInjector} that is registered by default to all {@link VariantInjector} instances.
 * <p>
 * It can be used for providing simple singleton values for injectors.
 */
public interface SimpleSingletonCustomInjector extends CustomInjector
{
    /**
     * Register new value to be injected by this {@link SimpleSingletonCustomInjector}
     *
     * @param value value
     */
    void registerValue(Object value);

    /**
     * Unregister a value to be injected by this {@link SimpleSingletonCustomInjector}
     *
     * @param value value
     */
    void unregisterValue(Object value);

    /**
     * Get all values registered to this {@link SimpleSingletonCustomInjector}
     *
     * @return all values registered to this {@link SimpleSingletonCustomInjector}
     */
    Set<Object> getValues();
}
