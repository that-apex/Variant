package net.mrgregorix.variant.inject.api;

import java.util.Collection;
import javax.annotation.concurrent.ThreadSafe;

import net.mrgregorix.variant.api.instantiation.AfterInstantiationHandler;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategy;
import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.inject.api.annotation.info.InjectableSingleton;
import net.mrgregorix.variant.inject.api.injector.CustomInjector;
import net.mrgregorix.variant.inject.api.injector.InjectionValueProvider;
import net.mrgregorix.variant.inject.api.injector.SingletonInjectorAlreadyRegisteredException;
import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.inject.api.type.provider.InjectableElementProvider;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;
import net.mrgregorix.variant.utils.exception.AmbiguousException;

/**
 * The core of the Variant Injection system.
 */
@ThreadSafe
@InjectableSingleton
@ModuleImplementation("net.mrgregorix.variant.inject.core.VariantInjectorImpl")
public interface VariantInjector extends VariantModule, InjectionValueProvider
{
    /**
     * Returns an immutable list of {@link InjectableElementProvider} used by this injector.
     *
     * @return an immutable list of {@link InjectableElementProvider} used by this injector.
     */
    @CollectionMayBeImmutable
    Collection<InjectableElementProvider<?>> getInjectableElementProviders();

    /**
     * Registers a new injectable element provider for this injector.
     *
     * @param provider provider to be registered
     *
     * @throws IllegalArgumentException when the requested the requested element provider is already registered
     */
    void registerInjectableElementProvider(final InjectableElementProvider<?> provider) throws IllegalArgumentException;

    /**
     * Unregisters the given element provider.
     *
     * @param provider provider to unregister
     *
     * @return provider if the injector was removed, false if it was not even registered
     */
    boolean unregisterCustomInjector(InjectableElementProvider<?> provider);

    /**
     * Gets all {@link InjectableElement}  that extends the given type from the given class.
     *
     * @param clazz class to be scanned for elements
     * @param type  type that {@link InjectableElement} must extend
     * @param <E>   type that {@link InjectableElement} must extend
     *
     * @return collection of retrieved {@link InjectableElement}s
     */
    @CollectionMayBeImmutable
    <E extends InjectableElement> Collection<E> getElements(Class<?> clazz, Class<E> type);

    /**
     * Returns an immutable list of {@link CustomInjector} used by this injector.
     *
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
     * Unregisters the given custom injector.
     *
     * @param injector injector to unregister
     *
     * @return true if the injector was removed, false if it was not even registered
     */
    boolean unregisterCustomInjector(CustomInjector injector);

    /**
     * Searches for a custom singleton injector
     *
     * @param type type of a injector to get
     * @param <T>  type of the injector
     *
     * @return an optional that may contain an injector or an empty optional if none found
     *
     * @throws AmbiguousException       when there are multiple injectors found for the given type
     * @throws IllegalArgumentException when there are no injectors matching the given type
     */
    <T extends CustomInjector> T getSingletonInjector(Class<T> type) throws AmbiguousException;

    /**
     * Returns an {@link InstantiationStrategy} used for finding @Inject constructors. This can be used to change the priority for the strategy
     *
     * @return an instantiation strategy for @Inject constructors
     */
    InstantiationStrategy getConstructorInstantiationStrategy();

    /**
     * Returns an {@link AfterInstantiationHandler} used for finding @Inject fields and initializing them. This can be used to change the priority for the handler.
     *
     * @return an after instantiation handler used for @Inject fields
     */
    AfterInstantiationHandler getFieldInjectionAfterInstantiationHandler();
}