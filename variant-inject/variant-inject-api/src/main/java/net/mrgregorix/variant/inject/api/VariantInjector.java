package net.mrgregorix.variant.inject.api;

import java.util.Collection;

import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.inject.api.annotation.info.InjectableSingleton;
import net.mrgregorix.variant.inject.api.injector.CustomInjector;
import net.mrgregorix.variant.inject.api.injector.SingletonInjectorAlreadyRegisteredException;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;
import net.mrgregorix.variant.utils.annotation.Nullable;
import net.mrgregorix.variant.utils.exception.AmbiguousException;

/**
 * The core of the Variant Injection system.
 */
@InjectableSingleton
@ModuleImplementation("net.mrgregorix.variant.inject.core.VariantInjectorImpl")
public interface VariantInjector extends VariantModule
{
    /**
     * @return an immutable list of {@link CustomInjector} used by this injector.
     */
    @CollectionMayBeImmutable
    Collection<CustomInjector> getCustomInjectors();

    /**
     * Registers a new custom injector for this injector.
     *
     * @param injector a custom injector to be registered
     *
     * @throws IllegalArgumentException                    when the requested the requested custom injector is already registered
     * @throws SingletonInjectorAlreadyRegisteredException when an injector with such type was already registered and was marked as a singleton injector
     */
    void registerCustomInjector(CustomInjector injector) throws IllegalArgumentException, SingletonInjectorAlreadyRegisteredException;

    /**
     * Searches for a custom singleton injector
     *
     * @param type type of a injector to get
     * @param <T>  type of the injector
     *
     * @return an optional that may contain an injector or an empty optional if none found
     *
     * @throws AmbiguousException when there are multiple injectors found for the given type
     */
    @Nullable
    <T extends CustomInjector> T getSingletonInjector(Class<T> type) throws AmbiguousException;
}