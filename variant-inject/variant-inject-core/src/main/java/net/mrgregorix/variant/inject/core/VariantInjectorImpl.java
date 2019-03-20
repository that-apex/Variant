package net.mrgregorix.variant.inject.core;


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.inject.api.injector.CustomInjector;
import net.mrgregorix.variant.inject.api.injector.SingletonInjectorAlreadyRegisteredException;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;
import net.mrgregorix.variant.utils.exception.AmbiguousException;

/**
 * A basic implementation for the injector
 */
public class VariantInjectorImpl implements VariantInjector
{
    public static final String MODULE_NAME = "Variant::Inject::Core";

    private final CollectionWithImmutable<CustomInjector, ImmutableList<CustomInjector>> customInjectors = WrappedCollectionWithImmutable.withImmutableList(new LinkedHashSet<>());

    private final Collection<Class<? extends CustomInjector>>                    singletonInjectors = new ArrayList<>();
    private final Map<Class<? extends CustomInjector>, Optional<CustomInjector>> injectorCache      = new HashMap<>();
    private       boolean                                                        injectorCacheDirty = true;

    @Override
    public String getName()
    {
        return VariantInjectorImpl.MODULE_NAME;
    }

    @Override
    public Collection<CustomInjector> getCustomInjectors()
    {
        return this.customInjectors.getImmutable();
    }

    @Override
    public void registerCustomInjector(final CustomInjector injector) throws IllegalArgumentException, SingletonInjectorAlreadyRegisteredException
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

    @SuppressWarnings("unchecked")
    @Override
    public <T extends CustomInjector> T getSingletonInjector(final Class<T> type) throws AmbiguousException
    {
        if (this.injectorCacheDirty)
        {
            // final classes may remain untouched, since they cannot be extended and no injector with the same type
            // can be registered
            this.injectorCache.entrySet().removeIf(it -> ! Modifier.isFinal(it.getKey().getModifiers()));
            this.injectorCacheDirty = false;
        }

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
        }).orElse(null);
    }
}