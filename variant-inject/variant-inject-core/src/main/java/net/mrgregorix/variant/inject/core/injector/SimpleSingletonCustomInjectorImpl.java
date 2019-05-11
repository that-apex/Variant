package net.mrgregorix.variant.inject.core.injector;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.inject.api.injector.CustomInjector;
import net.mrgregorix.variant.inject.api.injector.SimpleSingletonCustomInjector;
import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.utils.annotation.AnnotationUtils;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;
import net.mrgregorix.variant.utils.exception.AmbiguousException;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

public class SimpleSingletonCustomInjectorImpl extends AbstractModifiablePrioritizable<CustomInjector> implements SimpleSingletonCustomInjector
{
    private final CollectionWithImmutable<Object, ImmutableSet<Object>> values = WrappedCollectionWithImmutable.withImmutableSet(new HashSet<>());

    public SimpleSingletonCustomInjectorImpl()
    {
        this.setPriority(Integer.MIN_VALUE);
    }

    @Override
    public boolean isSingletonInjector()
    {
        return true;
    }

    @Override
    public Optional<Object> provideValueForInjection(final InjectableElement element)
    {
        if (! SimpleSingletonCustomInjectorImpl.isSimplyInjectable(element))
        {
            return Optional.empty();
        }

        Optional<Object> match = Optional.empty();

        for (final Object value : this.values)
        {
            if (! element.getType().isInstance(value))
            {
                continue;
            }

            if (match.isPresent())
            {
                throw new AmbiguousException("Multiple values found while injecting for " + element.getHandle());
            }

            match = Optional.of(value);
        }

        return match;
    }

    @Override
    public void registerValue(final Object value)
    {
        this.values.add(value);
    }

    @Override
    public void unregisterValue(final Object value)
    {
        this.values.remove(value);
    }

    @Override
    public Set<Object> getValues()
    {
        return this.values.getImmutable();
    }

    public static boolean isSimplyInjectable(final InjectableElement injectableElement)
    {
        if (injectableElement.isAnnotationPresent(Inject.class))
        {
            return true;
        }

        final Inject annotation = AnnotationUtils.getAnnotation(injectableElement, Inject.class);
        return annotation != null && annotation.simpleInjection();
    }
}
