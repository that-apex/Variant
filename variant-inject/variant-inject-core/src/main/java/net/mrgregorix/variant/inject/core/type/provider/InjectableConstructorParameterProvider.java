package net.mrgregorix.variant.inject.core.type.provider;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import net.mrgregorix.variant.api.instantiation.InstantiationStrategy;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatch;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatchType;
import net.mrgregorix.variant.inject.api.type.InjectableConstructorParameter;
import net.mrgregorix.variant.inject.api.type.provider.InjectableElementProvider;
import net.mrgregorix.variant.inject.core.type.InjectableConstructorParameterImpl;

public class InjectableConstructorParameterProvider implements InjectableElementProvider<InjectableConstructorParameter>
{
    private final InstantiationStrategy constructorInstantiationStrategy;

    public InjectableConstructorParameterProvider(final InstantiationStrategy constructorInstantiationStrategy)
    {
        this.constructorInstantiationStrategy = constructorInstantiationStrategy;
    }

    @Override
    public Class<InjectableConstructorParameter> getType()
    {
        return InjectableConstructorParameter.class;
    }

    @Override
    public Collection<InjectableConstructorParameter> provide(final Class<?> clazz)
    {
        final InstantiationStrategyMatch<?> match = this.constructorInstantiationStrategy.findMatch(clazz);
        if (match.getMatchType() != InstantiationStrategyMatchType.CERTAIN)
        {
            return Collections.emptyList();
        }

        return Arrays.stream(Objects.requireNonNull(match.getConstructor()).getParameters())
                     .map(InjectableConstructorParameterImpl::new)
                     .collect(Collectors.toUnmodifiableList());
    }
}
