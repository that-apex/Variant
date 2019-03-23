package net.mrgregorix.variant.utils.priority;

import net.mrgregorix.variant.utils.annotation.NotNull;

/**
 * Represents an object that has a priority
 */
public interface Prioritizable <T extends Prioritizable<T>> extends Comparable<Prioritizable<? extends T>>
{
    /**
     * Returns a priority for the current object, the lower the value the lower the priority.
     *
     * @return priority for the current object
     */
    default int getPriority()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    default int compareTo(@NotNull final Prioritizable<? extends T> another)
    {
        if (this == another)
        {
            return 0;
        }

        if (this.getPriority() == another.getPriority())
        {
            return another.hashCode() - this.hashCode();
        }

        return another.getPriority() - this.getPriority();
    }
}
