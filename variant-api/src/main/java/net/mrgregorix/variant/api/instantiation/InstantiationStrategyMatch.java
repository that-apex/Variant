package net.mrgregorix.variant.api.instantiation;

import java.lang.reflect.Constructor;
import java.util.Objects;

import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Represents a result of {@link InstantiationStrategy#findMatch(Class)} query
 *
 * @param <T> type of the queried class
 */
public class InstantiationStrategyMatch <T>
{
    private @Nullable final Constructor<T>                 constructor;
    private final           InstantiationStrategyMatchType matchType;

    /**
     * @param constructor a constructor, may be null only if {@code matchType} is {@link InstantiationStrategyMatchType#NONE}
     * @param matchType   what type of match was found.
     */
    public InstantiationStrategyMatch(final @Nullable Constructor<T> constructor, final InstantiationStrategyMatchType matchType)
    {
        this.matchType = matchType;

        if (this.matchType == InstantiationStrategyMatchType.NONE)
        {
            this.constructor = null;
        }
        else
        {
            this.constructor = Objects.requireNonNull(constructor, "constructor must be not null if MatchType is not NONE");
        }
    }

    /**
     * Returns a suitable constructor that may be used to instantiate the queried class.
     * <p>
     * Will be null if no constructor was found (the {@link #getMatchType()} is {@link InstantiationStrategyMatchType#NONE})
     *
     * @return a suitable constructor that may be used to instantiate the queried class.
     */
    @Nullable
    public Constructor<T> getConstructor()
    {
        return this.constructor;
    }

    /**
     * Returns type of this match.
     *
     * @return type of this match
     *
     * @see InstantiationStrategyMatchType
     */
    public InstantiationStrategyMatchType getMatchType()
    {
        return this.matchType;
    }
}
