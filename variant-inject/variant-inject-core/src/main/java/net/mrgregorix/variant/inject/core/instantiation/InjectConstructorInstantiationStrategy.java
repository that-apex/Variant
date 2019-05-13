package net.mrgregorix.variant.inject.core.instantiation;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

import net.mrgregorix.variant.api.instantiation.InstantiationStrategy;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatch;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatchType;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.inject.api.injector.InjectionException;
import net.mrgregorix.variant.inject.api.type.InjectableConstructorParameter;
import net.mrgregorix.variant.utils.annotation.AnnotationUtils;
import net.mrgregorix.variant.utils.exception.AmbiguousException;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

public class InjectConstructorInstantiationStrategy extends AbstractModifiablePrioritizable<InstantiationStrategy> implements InstantiationStrategy
{
    private final VariantInjector variantInjector;

    /**
     * Constructs a new InjectConstructorInstantiationStrategy
     *
     * @param variantInjector provider for injected values
     */
    public InjectConstructorInstantiationStrategy(final VariantInjector variantInjector)
    {
        this.variantInjector = variantInjector;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> InstantiationStrategyMatch<T> findMatch(final Class<T> type)
    {
        InstantiationStrategyMatch<T> match = new InstantiationStrategyMatch<>(null, InstantiationStrategyMatchType.NONE);

        for (final Constructor<?> declaredConstructor : type.getDeclaredConstructors())
        {
            if (AnnotationUtils.getAnnotation(declaredConstructor, Inject.class) == null)
            {
                continue;
            }

            if (match.getMatchType() != InstantiationStrategyMatchType.NONE)
            {
                throw new AmbiguousException("Two or more constructors with @Inject annotations were found: (" + declaredConstructor + " and " + match.getConstructor() + ")");
            }

            match = new InstantiationStrategyMatch<>((Constructor<T>) declaredConstructor, InstantiationStrategyMatchType.CERTAIN);
        }

        return match;
    }

    @Override
    public Object[] getInstantiationParameters(final InstantiationStrategyMatch<?> match)
    {
        final Collection<InjectableConstructorParameter> parameters = this.variantInjector.getElements(Objects.requireNonNull(match.getConstructor()).getDeclaringClass(), InjectableConstructorParameter.class);
        final Object[] values = new Object[parameters.size()];
        int i = 0;

        for (final InjectableConstructorParameter parameter : parameters)
        {
            final Optional<Object> value = this.variantInjector.provideValueForInjection(parameter);

            if (! value.isPresent())
            {
                throw new InjectionException("no value to inject into " + parameter.getHandle() + " was found");
            }

            values[i++] = value.get();
        }

        return values;
    }
}
