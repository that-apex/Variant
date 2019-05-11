package net.mrgregorix.variant.inject.core;


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.instantiation.AfterInstantiationHandler;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategy;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.inject.api.injector.CustomInjector;
import net.mrgregorix.variant.inject.api.injector.SingletonInjectorAlreadyRegisteredException;
import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.inject.api.type.provider.InjectableElementProvider;
import net.mrgregorix.variant.inject.core.injector.CoreValuesInjector;
import net.mrgregorix.variant.inject.core.injector.SimpleSingletonCustomInjectorImpl;
import net.mrgregorix.variant.inject.core.injector.VariantModuleInjector;
import net.mrgregorix.variant.inject.core.instantiation.FieldInjectionAfterInstantiationHandler;
import net.mrgregorix.variant.inject.core.instantiation.InjectConstructorInstantiationStrategy;
import net.mrgregorix.variant.inject.core.type.provider.InjectableConstructorParameterProvider;
import net.mrgregorix.variant.inject.core.type.provider.InjectableFieldProvider;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;
import net.mrgregorix.variant.utils.exception.AmbiguousException;

/**
 * A basic implementation for the injector
 */
@ThreadSafe
public class VariantInjectorImpl implements VariantInjector
{
    public static final String MODULE_NAME = "Variant::Inject::Core";

    private final ReadWriteLock                                                                                      providerLock                            = new ReentrantReadWriteLock();
    private final CollectionWithImmutable<InjectableElementProvider<?>, ImmutableList<InjectableElementProvider<?>>> providers                               = WrappedCollectionWithImmutable.withImmutableList(new ArrayList<>());
    private final ReadWriteLock                                                                                      injectorLock                            = new ReentrantReadWriteLock();
    private final CollectionWithImmutable<CustomInjector, ImmutableList<CustomInjector>>                             customInjectors                         = WrappedCollectionWithImmutable.withImmutableList(new TreeSet<>());
    private final InjectConstructorInstantiationStrategy                                                             constructorInstantiationStrategy        = new InjectConstructorInstantiationStrategy(this);
    private final FieldInjectionAfterInstantiationHandler                                                            fieldInjectionAfterInstantiationHandler = new FieldInjectionAfterInstantiationHandler(this);
    private final Collection<Class<? extends CustomInjector>>                                                        singletonInjectors                      = new ArrayList<>();
    private final Map<Class<? extends CustomInjector>, Optional<CustomInjector>>                                     injectorCache                           = new HashMap<>();
    private       boolean                                                                                            injectorCacheDirty                      = true;

    @Override
    public String getName()
    {
        return VariantInjectorImpl.MODULE_NAME;
    }

    @Override
    public void initialize(final Variant variant)
    {
        this.registerCustomInjector(new CoreValuesInjector(variant));
        this.registerCustomInjector(new SimpleSingletonCustomInjectorImpl());
        this.registerCustomInjector(new VariantModuleInjector(variant));

        this.registerInjectableElementProvider(new InjectableFieldProvider());
        this.registerInjectableElementProvider(new InjectableConstructorParameterProvider(this.constructorInstantiationStrategy));
    }

    @Override
    public Collection<InjectableElementProvider<?>> getInjectableElementProviders()
    {
        this.providerLock.readLock().lock();

        try
        {
            return this.providers.getImmutable();
        }
        finally
        {
            this.providerLock.readLock().unlock();
        }
    }

    @Override
    public void registerInjectableElementProvider(final InjectableElementProvider<?> provider) throws IllegalArgumentException
    {
        this.providerLock.writeLock().lock();

        try
        {
            if (! this.providers.add(provider))
            {
                throw new IllegalArgumentException("Already registered!");
            }
        }
        finally
        {
            this.providerLock.writeLock().unlock();
        }
    }

    @Override
    public boolean unregisterCustomInjector(InjectableElementProvider<?> provider)
    {
        this.providerLock.writeLock().lock();

        try
        {
            return this.providers.remove(provider);
        }
        finally
        {
            this.providerLock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends InjectableElement> Collection<E> getElements(final Class<?> clazz, final Class<E> type)
    {
        final Collection<E> output = new ArrayList<>();

        for (final InjectableElementProvider<?> provider : this.getInjectableElementProviders())
        {
            if (! type.isAssignableFrom(provider.getType()))
            {
                continue;
            }

            output.addAll((Collection<? extends E>) provider.provide(clazz));
        }

        return output;
    }

    @Override
    public Collection<CustomInjector> getCustomInjectors()
    {
        this.injectorLock.readLock().lock();

        try
        {
            return this.customInjectors.getImmutable();
        }
        finally
        {
            this.injectorLock.readLock().unlock();
        }
    }

    @Override
    public void registerCustomInjector(final CustomInjector injector) throws IllegalArgumentException, SingletonInjectorAlreadyRegisteredException
    {
        this.injectorLock.writeLock().lock();

        try
        {
            if (injector.isSingletonInjector())
            {
                if (this.singletonInjectors.contains(injector.getClass()))
                {
                    throw new SingletonInjectorAlreadyRegisteredException(injector.getClass());
                }

                this.singletonInjectors.add(injector.getClass());
            }

            if (! this.customInjectors.add(injector))
            {
                throw new IllegalArgumentException("Already registered!");
            }

            this.injectorCacheDirty = true;
        }
        finally
        {
            this.injectorLock.writeLock().unlock();
        }
    }

    @Override
    public boolean unregisterCustomInjector(final CustomInjector injector)
    {
        this.injectorLock.writeLock().lock();

        try
        {
            if (! this.customInjectors.remove(injector))
            {
                return false;
            }

            if (injector.isSingletonInjector())
            {
                this.singletonInjectors.remove(injector.getClass());
            }

            this.injectorCacheDirty = true;
            return true;
        }
        finally
        {
            this.injectorLock.writeLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public synchronized <T extends CustomInjector> T getSingletonInjector(final Class<T> type) throws AmbiguousException
    {
        this.injectorLock.readLock().lock();

        if (this.injectorCacheDirty)
        {
            this.injectorLock.readLock().unlock();
            this.injectorLock.writeLock().lock();

            try
            {
                // final classes may remain untouched, since they cannot be extended and no injector with the same type
                // can be registered
                this.injectorCache.entrySet().removeIf(it -> ! Modifier.isFinal(it.getKey().getModifiers()));
                this.injectorCacheDirty = false;
            }
            finally
            {
                this.injectorLock.writeLock().unlock();
            }

            this.injectorLock.readLock().lock();
        }

        try
        {
            return (T) this.injectorCache.computeIfAbsent(type, ignored ->
            {
                final var list = this.customInjectors.stream()
                                                     .filter(CustomInjector::isSingletonInjector)
                                                     .filter(it -> type.isAssignableFrom(it.getClass()))
                                                     .collect(Collectors.toList());

                if (list.size() > 1)
                {
                    throw new AmbiguousException("More that one injector matches for type " + type.getName());
                }

                return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
            }).orElseThrow(() -> new IllegalArgumentException("No such singleton was found"));
        }
        finally
        {
            this.injectorLock.readLock().unlock();
        }
    }

    @Override
    public Optional<Object> provideValueForInjection(final InjectableElement element)
    {
        for (final CustomInjector customInjector : this.getCustomInjectors())
        {
            final Optional<Object> value = customInjector.provideValueForInjection(element);

            if (value.isPresent())
            {
                return value;
            }
        }

        return Optional.empty();
    }

    @Override
    public Collection<InstantiationStrategy> getInstantiationStrategies()
    {
        return Collections.singletonList(this.constructorInstantiationStrategy);
    }

    @Override
    public Collection<AfterInstantiationHandler> getAfterInstantiationHandlers()
    {
        return Collections.singletonList(this.fieldInjectionAfterInstantiationHandler);
    }

    @Override
    public InjectConstructorInstantiationStrategy getConstructorInstantiationStrategy()
    {
        return this.constructorInstantiationStrategy;
    }

    @Override
    public FieldInjectionAfterInstantiationHandler getFieldInjectionAfterInstantiationHandler()
    {
        return this.fieldInjectionAfterInstantiationHandler;
    }
}