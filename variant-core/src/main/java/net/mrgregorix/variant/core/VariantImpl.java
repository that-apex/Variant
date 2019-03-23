package net.mrgregorix.variant.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.VariantInstantiationException;
import net.mrgregorix.variant.api.annotations.NoProxy;
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
import net.mrgregorix.variant.utils.collections.immutable.SynchronizedWrapperCollectionWithImmutable;
import net.mrgregorix.variant.utils.reflect.MemberUtils;

/**
 * Implementation of {@link Variant} class.
 *
 * <p>
 * To create your own instance use {@link VariantBuilder}
 */
public class VariantImpl implements Variant
{
    private final CollectionWithImmutable<VariantModule, ImmutableCollection<VariantModule>> modules                 = new SynchronizedWrapperCollectionWithImmutable<>(
        new LinkedHashSet<>(), ImmutableList::copyOf);
    private final Collection<ProxySpecification>                                             proxySpecifications     = new ArrayList<>();
    private final Collection<InstantiationStrategy<?>>                                       instantiationStrategies = new TreeSet<>();
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
        final InstantiationStrategy<?> instantiationStrategy;
        final InstantiationStrategyMatch<T> instantiationStrategyMatch;

        {
            final Pair<InstantiationStrategy, InstantiationStrategyMatch<T>> pair = this.findInstantiationStrategy(type);
            instantiationStrategy = pair.getLeft();
            instantiationStrategyMatch = pair.getRight();
        }

        final Class<? extends T> proxyType = this.proxyCache.addIfAbsent(type, this::createProxy);

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
            return proxyConstructor.newInstance(instantiationStrategy.getInstantiationParameters(instantiationStrategyMatch));
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

    private <T> Pair<InstantiationStrategy, InstantiationStrategyMatch<T>> findInstantiationStrategy(final Class<T> type)
    {
        Pair<InstantiationStrategy, InstantiationStrategyMatch<T>> validLaxMatch = null;

        synchronized (this.instantiationStrategies)
        {
            for (final InstantiationStrategy<?> instantiationStrategy : this.instantiationStrategies)
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

        if (validLaxMatch == null)
        {
            throw new VariantInstantiationException("Couldn't find a valid InstantiationStrategy for type " + type.getName());
        }

        return validLaxMatch;
    }

    private <T> Class<? extends T> createProxy(final Class<T> type)
    {
        Preconditions.checkArgument(! Modifier.isFinal(type.getModifiers()), "cannot proxy a final class");

        if (Proxy.class.isAssignableFrom(type))
        {
            return type;
        }

        final Map<Method, Collection<ProxyInvocationHandler>> invocationHandlers = new HashMap<>();

        synchronized (this.proxySpecifications)
        {
            for (final Method method : MemberUtils.getAllMethods(type))
            {
                if (Modifier.isFinal(method.getModifiers()) || Modifier.isPrivate(method.getModifiers()))
                {
                    continue;
                }

                if (method.getDeclaredAnnotation(NoProxy.class) != null)
                {
                    continue;
                }

                Set<ProxyInvocationHandler> handlers = null;

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

        return this.proxyProvider.createProxy(
            this.classLoader,
            type,
            this.proxyNamingStrategy.nameProxyClass(this, type),
            invocationHandlers
        );
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
        return this.modules.getImmutable();
    }

    @Override
    public <T extends VariantModule> T registerModule(final T module)
    {
        synchronized (this.modules)
        {
            this.modules.add(module);

            synchronized (this.proxySpecifications)
            {
                this.proxySpecifications.clear();
                this.modules
                    .forEach(m -> this.proxySpecifications.addAll(m.getProxySpecifications()));
            }

            synchronized (this.instantiationStrategies)
            {
                this.instantiationStrategies.clear();
                this.modules
                    .forEach(m -> this.instantiationStrategies.addAll(m.getInstantiationStrategies()));
            }
        }

        return module;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends VariantModule> T registerModule(final Class<T> moduleClass) throws ModuleHasNoImplementationException, ClassNotFoundException
    {
        Class<? extends T> classToInstantiate = moduleClass;

        final ModuleImplementation moduleImplementation = moduleClass.getDeclaredAnnotation(ModuleImplementation.class);
        if (moduleImplementation != null)
        {
            classToInstantiate = (Class<? extends T>) Class.forName(moduleImplementation.value());
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
