package net.mrgregorix.variant.api.instantiation;

/**
 * Represents how well an {@link InstantiationStrategy} is able to instantiate a new object.
 */
public enum InstantiationStrategyMatchType
{
    /**
     * The {@link InstantiationStrategy} is not able to instantiate the provided object in any way.
     */
    NONE,

    /**
     * The {@link InstantiationStrategy} is able to instantiate the provided object, but it does not strictly met the strategy's requirements and another strategy should be used for this instantiation
     * if possible.
     */
    LAX,

    /**
     * The {@link InstantiationStrategy} is able to instantiate the provided object and it detected that the object is required to be instantiated by this very strategy.
     */
    CERTAIN
}
