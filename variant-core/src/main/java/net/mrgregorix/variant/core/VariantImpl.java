package net.mrgregorix.variant.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.VariantInstantiationException;
import net.mrgregorix.variant.api.annotations.NoProxy;
import net.mrgregorix.variant.api.instantiation.AfterInstantiationHandler;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategy;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatch;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatchType;
import net.mrgregorix.variant.api.module.ModuleHasNoImplementationException;
import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.api.proxy.ProxyCache;
import net.mrgregorix.variant.api.proxy.ProxyInvocationHandler;
import net.mrgregorix.variant.api.proxy.ProxyMatchResult;
import net.mrgregorix.variant.api.proxy.ProxyNamingStrategy;
import net.mrgregorix.variant.api.proxy.ProxyProvider;
import net.mrgregorix.variant.api.proxy.ProxySpecification;
import net.mrgregorix.variant.core.builder.VariantBuilder;
import net.mrgregorix.variant.utils.Pair;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.SynchronizedWrappedCollectionWithImmutable;
import net.mrgregorix.variant.utils.exception.AmbiguousException;
import net.mrgregorix.variant.utils.reflect.MemberUtils;

/**
 * Implementation of {@link Variant} class.
 *
 * <p>
 * To create your own instance use {@link VariantBuilder}
 */
@ThreadSafe
public class VariantImpl implements Variant
{
    private final CollectionWithImmutable<VariantModule, ImmutableCollection<VariantModule>> modules                    = new SynchronizedWrappedCollectionWithImmutable<>(new LinkedHashSet<>(), ImmutableList::copyOf);
    private final ReentrantReadWriteLock                                                     dataLock                   = new ReentrantReadWriteLock(true);
    private final Collection<ProxySpecification>                                             proxySpecifications        = new TreeSet<>();
    private final Collection<InstantiationStrategy>                                          instantiationStrategies    = new TreeSet<>();
    private final Collection<AfterInstantiationHandler>                                      afterInstantiationHandlers = new TreeSet<>();
    private final ClassLoader                                                                classLoader;
    private final ProxyCache                                                                 proxyCache;
    private final ProxyProvider                                                              proxyProvider;
    private final ProxyNamingStrategy                                                        proxyNamingStrategy;

    /**
     * Constructs a new {@link VariantImpl} instance.
     *
     * @param proxyCache          cache that will be used for caching the proxied classes
     * @param classLoader         class loader that will be used for creating proxy classes
     * @param proxyProvider       provider for creating proxy class instances
     * @param proxyNamingStrategy strategy that will be used to name the created proxy classes
     * @param modules             modules to be registered when creating this instance
     */
    public VariantImpl(final ProxyCache proxyCache, final ClassLoader classLoader, final ProxyProvider proxyProvider, final ProxyNamingStrategy proxyNamingStrategy,
                       final Collection<VariantModule> modules)
    {
        this.proxyCache = Objects.requireNonNull(proxyCache, "proxyCache");
        this.classLoader = Objects.requireNonNull(classLoader, "classLoader");
        this.proxyProvider = Objects.requireNonNull(proxyProvider, "proxyProvider");
        this.proxyNamingStrategy = Objects.requireNonNull(proxyNamingStrategy, "proxyNamingStrategy");

        modules.forEach(this::registerModule);
    }

    @Override
    public <T> T instantiate(final Class<T> type)
    {
        final Class<? extends T> proxyType = this.proxyCache.addIfAbsent(type, this::createProxy);

        final T newInstance;

        if (type.isInterface())
        {
            try
            {
                newInstance = proxyType.getDeclaredConstructor().newInstance();
            }
            catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e)
            {
                throw new AssertionError("This should never happen", e);
            }
        }
        else
        {
            final InstantiationStrategy instantiationStrategy;
            final InstantiationStrategyMatch<T> instantiationStrategyMatch;

            {
                final Pair<InstantiationStrategy, InstantiationStrategyMatch<T>> pair = this.findInstantiationStrategy(type);
                instantiationStrategy = pair.getLeft();
                instantiationStrategyMatch = pair.getRight();
            }

            final Constructor<? extends T> proxyConstructor;
            try
            {
                proxyConstructor = proxyType.getDeclaredConstructor(Objects.requireNonNull(instantiationStrategyMatch.getConstructor()).getParameterTypes());
            }
            catch (final NoSuchMethodException e)
            {
                throw new VariantInstantiationException(
                    "Invalid proxy was created by " + this.proxyProvider.getClass().getName() + ". Couldn't find constructor " + instantiationStrategyMatch.getConstructor() + "for the proxied type: " +
                    type.getName() + ", proxy: " + proxyType.getName());
            }
            proxyConstructor.setAccessible(true);

            try
            {
                newInstance = proxyConstructor.newInstance(instantiationStrategy.getInstantiationParameters(instantiationStrategyMatch));
            }
            catch (final InstantiationException e)
            {
                throw new ModuleHasNoImplementationException("Class " + type.getName() + " is not instantiatable", e);
            }
            catch (final IllegalAccessException e)
            {
                throw new VariantInstantiationException("Constructor " + instantiationStrategyMatch.getConstructor() + " is invalid due to not being accessible", e);
            }
            catch (final InvocationTargetException e)
            {
                throw new VariantInstantiationException("Exception while calling " + instantiationStrategyMatch.getConstructor(), e.getCause());
            }
        }

        try
        {
            this.dataLock.readLock().lock();
            this.afterInstantiationHandlers.forEach(handler -> handler.afterInstantiationHandler((Proxy) newInstance));
        }
        finally
        {
            this.dataLock.readLock().unlock();
        }

        return newInstance;
    }

    private <T> Pair<InstantiationStrategy, InstantiationStrategyMatch<T>> findInstantiationStrategy(final Class<T> type)
    {
        Pair<InstantiationStrategy, InstantiationStrategyMatch<T>> validLaxMatch = null;

        try
        {
            this.dataLock.readLock().lock();

            for (final InstantiationStrategy instantiationStrategy : this.instantiationStrategies)
            {
                final InstantiationStrategyMatch<T> match = instantiationStrategy.findMatch(type);

                if (match.getMatchType() == InstantiationStrategyMatchType.CERTAIN)
                {
                    return new Pair<>(instantiationStrategy, match);
                }

                if (validLaxMatch == null && match.getMatchType() == InstantiationStrategyMatchType.LAX)
                {
                    validLaxMatch = new Pair<>(instantiationStrategy, match);
                }
            }
        }
        finally
        {
            this.dataLock.readLock().unlock();
        }

        if (validLaxMatch == null)
        {
            throw new VariantInstantiationException("Couldn't find a valid InstantiationStrategy for type " + type.getName());
        }

        return validLaxMatch;
    }

    private <T> Class<? extends T> createProxy(final Class<? extends T> type)
    {
        Preconditions.checkArgument(! Modifier.isFinal(type.getModifiers()), "cannot proxy a final class");

        if (Proxy.class.isAssignableFrom(type))
        {
            return type;
        }

        final Map<Method, Collection<ProxyInvocationHandler<?>>> invocationHandlers = new HashMap<>();
        try
        {
            this.dataLock.readLock().lock();

            final Set<Method> actualMethods =
                MemberUtils.getAllMethods(type)
                           .stream()
                           .filter(method -> method.getDeclaringClass() != Object.class)
                           .filter(method -> ! Modifier.isFinal(method.getModifiers()) && ! Modifier.isPrivate(method.getModifiers()) && method.getDeclaredAnnotation(NoProxy.class) == null)
                           .map(method -> {
                               try
                               {
                                   return type.getMethod(method.getName(), method.getParameterTypes());
                               }
                               catch (final NoSuchMethodException e)
                               {
                                   return method;
                               }
                           })
                           .collect(Collectors.toSet());

            for (final Method method : actualMethods)
            {
                Set<ProxyInvocationHandler<?>> handlers = null;

                for (final ProxySpecification proxySpecification : this.proxySpecifications)
                {
                    final ProxyMatchResult proxyMatchResult = proxySpecification.shouldProxy(method);

                    if (! proxyMatchResult.isMatch())
                    {
                        continue;
                    }

                    if (handlers == null)
                    {
                        handlers = new TreeSet<>();
                    }

                    handlers.add(proxyMatchResult.getHandler());
                }

                if (handlers != null)
                {
                    invocationHandlers.put(method, handlers);
                }
            }
        }
        finally
        {
            this.dataLock.readLock().unlock();
        }

        return this.proxyProvider.createProxy(
            this.classLoader,
            type,
            this.proxyNamingStrategy.nameProxyClass(this, type),
            invocationHandlers
        );
    }

    @Override
    public Proxy asProxy(final Object object)
    {
        if (! (object instanceof Proxy))
        {
            throw new IllegalArgumentException("Not a proxy");
        }

        return (Proxy) object;
    }

    @Override
    public ProxyCache getProxyCache()
    {
        return this.proxyCache;
    }

    @Override
    public ProxyProvider getProxyProvider()
    {
        return this.proxyProvider;
    }

    @Override
    public Collection<VariantModule> getRegisteredModules()
    {
        this.dataLock.readLock().lock();

        try
        {
            return this.modules.getImmutable();
        }
        finally
        {
            this.dataLock.readLock().unlock();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends VariantModule> T getModule(Class<T> module)
    {
        T match = null;

        for (final VariantModule variantModule : this.modules)
        {
            if (! module.isInstance(variantModule))
            {
                continue;
            }

            if (match != null)
            {
                throw new AmbiguousException("Two or module modules matching " + module);
            }

            match = (T) variantModule;
        }

        if (match == null)
        {
            throw new IllegalArgumentException("No modules matching " + module);
        }

        return match;
    }

    @Override
    public <T extends VariantModule> T registerModule(final T module)
    {
        try
        {
            this.dataLock.writeLock().lock();
            this.modules.add(module);

            this.proxySpecifications.addAll(module.getProxySpecifications());
            this.instantiationStrategies.addAll(module.getInstantiationStrategies());
            this.afterInstantiationHandlers.addAll(module.getAfterInstantiationHandlers());
        }
        finally
        {
            this.dataLock.writeLock().unlock();
        }

        module.initialize(this);
        return module;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends VariantModule> T registerModule(final Class<T> moduleClass) throws ModuleHasNoImplementationException
    {
        Class<? extends T> classToInstantiate = moduleClass;

        final ModuleImplementation moduleImplementation = moduleClass.getDeclaredAnnotation(ModuleImplementation.class);
        if (moduleImplementation != null)
        {
            try
            {
                classToInstantiate = (Class<? extends T>) Class.forName(moduleImplementation.value());
            }
            catch (ClassNotFoundException e)
            {
                throw new ModuleHasNoImplementationException(e);
            }
        }

        Constructor<? extends T> constructor;
        Object[] constructorArgs;

        try
        {
            constructor = classToInstantiate.getDeclaredConstructor();
            constructorArgs = new Object[0];
        }
        catch (final NoSuchMethodException ignored)
        {
            try
            {
                constructor = classToInstantiate.getDeclaredConstructor(Variant.class);
                constructorArgs = new Object[] {this};
            }
            catch (final NoSuchMethodException ignored2)
            {
                throw new ModuleHasNoImplementationException("No valid constructor found for " + classToInstantiate.getName());
            }
        }

        try
        {
            return this.registerModule(constructor.newInstance(constructorArgs));
        }
        catch (final InstantiationException e)
        {
            throw new ModuleHasNoImplementationException("Class " + moduleClass.getName() + " is not instantiatable", e);
        }
        catch (final IllegalAccessException e)
        {
            throw new ModuleHasNoImplementationException("Constructor " + constructor + " is invalid due to not being accessible", e);
        }
        catch (final InvocationTargetException e)
        {
            throw new RuntimeException("Exception while calling " + constructor, e.getCause());
        }
    }
}
