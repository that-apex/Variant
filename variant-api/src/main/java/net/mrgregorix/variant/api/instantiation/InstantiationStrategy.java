package net.mrgregorix.variant.api.instantiation;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.utils.priority.ModifiablePrioritizable;
import net.mrgregorix.variant.utils.priority.Prioritizable;

/**
 * A strategy to instantiate a class.
 * <p>
 * Used by {@link Variant#instantiate(Class)} to determine the best way to instantiate an object.
 */
public interface InstantiationStrategy <I extends InstantiationStrategy<I>> extends ModifiablePrioritizable<I>
{
    /**
     * Called to find a suitable constructor for injection.
     * <p>
     * If no valid constructor is found the {@link InstantiationStrategyMatch#getMatchType()} will be set to {@link InstantiationStrategyMatchType#NONE} and the constructor will be set to null
     * <p>
     * If there are multiple suitable constructors the ones marked {@link InstantiationStrategyMatchType#CERTAIN} will be prioritized above the ones marked with {@link
     * InstantiationStrategyMatchType#LAX}. If there are multiple CERTAIN type constructors it is up to the strategy to select one or return a {@link InstantiationStrategyMatchType#NONE} match.
     *
     * @param type type that is to be constructed
     *
     * @return information about the found constructor if any and a match type
     */
    <T> InstantiationStrategyMatch<T> findMatch(Class<T> type);

    /**
     * Gets the parameters that will be used for instantiating an object using a constructor previously find by {@link #findMatch(Class)}.
     *
     * @param match a match previously returned by {@link #findMatch(Class)}
     *
     * @return the parameters to be used for instantiation
     */
    Object[] getInstantiationParameters(InstantiationStrategyMatch<?> match);
}
