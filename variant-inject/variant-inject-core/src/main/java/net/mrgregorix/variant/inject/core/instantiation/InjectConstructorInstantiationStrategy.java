package net.mrgregorix.variant.inject.core.instantiation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.Optional;

import net.mrgregorix.variant.api.instantiation.InstantiationStrategy;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatch;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatchType;
import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.inject.api.injector.InjectionValueProvider;
import net.mrgregorix.variant.inject.api.type.InjectableConstructorParameter;
import net.mrgregorix.variant.inject.core.type.impl.InjectableConstructorParameterImpl;
import net.mrgregorix.variant.utils.exception.AmbiguousException;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

public class InjectConstructorInstantiationStrategy extends AbstractModifiablePrioritizable<InstantiationStrategy> implements InstantiationStrategy
{
    private final InjectionValueProvider injectionValueProvider;

    /**
     * Constructs a new InjectConstructorInstantiationStrategy
     *
     * @param injectionValueProvider provider for injected values
     */
    public InjectConstructorInstantiationStrategy(InjectionValueProvider injectionValueProvider)
    {
        this.injectionValueProvider = injectionValueProvider;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> InstantiationStrategyMatch<T> findMatch(final Class<T> type)
    {
        InstantiationStrategyMatch<T> match = new InstantiationStrategyMatch<>(null, InstantiationStrategyMatchType.NONE);

        for (final Constructor<?> declaredConstructor : type.getDeclaredConstructors())
        {
            if (declaredConstructor.getDeclaredAnnotation(Inject.class) == null)
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
        final Parameter[] parameters = Objects.requireNonNull(match.getConstructor()).getParameters();
        final Object[] values = new Object[parameters.length];

        for (int i = 0; i < parameters.length; i++)
        {
            final InjectableConstructorParameter injectableConstructorParameter = new InjectableConstructorParameterImpl(parameters[i]);
            final Optional<Object> value = this.injectionValueProvider.provideValueForInjection(injectableConstructorParameter);

            if (! value.isPresent())
            {
                throw new IllegalArgumentException("no value to inject into " + injectableConstructorParameter.getHandle() + " was found");
            }

            values[i] = value.get();
        }

        return values;
    }
}
