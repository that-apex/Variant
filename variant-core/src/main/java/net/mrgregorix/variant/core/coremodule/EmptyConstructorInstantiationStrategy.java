package net.mrgregorix.variant.core.coremodule;

import java.lang.reflect.Constructor;

import net.mrgregorix.variant.api.instantiation.InstantiationStrategy;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatch;
import net.mrgregorix.variant.api.instantiation.InstantiationStrategyMatchType;

/**
 * An instantiation strategy that provides instantiation for empty constructors.
 */
public class EmptyConstructorInstantiationStrategy implements InstantiationStrategy<EmptyConstructorInstantiationStrategy>
{
    private int priority = Integer.MIN_VALUE;

    @SuppressWarnings("unchecked")
    @Override
    public <T> InstantiationStrategyMatch<T> findMatch(final Class<T> type)
    {
        for (final Constructor<?> declaredConstructor : type.getDeclaredConstructors())
        {
            if (declaredConstructor.getParameters().length == 0)
            {
                return new InstantiationStrategyMatch<>((Constructor<T>) declaredConstructor, InstantiationStrategyMatchType.LAX);
            }
        }

        return new InstantiationStrategyMatch<>(null, InstantiationStrategyMatchType.NONE);
    }

    @Override
    public Object[] getInstantiationParameters(InstantiationStrategyMatch<?> match)
    {
        return new Object[0];
    }

    @Override
    public int getPriority()
    {
        return this.priority;
    }

    @Override
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}
