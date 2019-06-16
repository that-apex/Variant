package net.mrgregorix.variant.utils.priority;

/**
 * A {@link Prioritizable} which priority can be adjusted programatically.
 *
 * @param <T> type of the class extending this {@link ModifiablePrioritizable}
 */
public interface ModifiablePrioritizable <T extends ModifiablePrioritizable<T>> extends Prioritizable<T>
{
    /**
     * Sets the priority for the current object, the lower the value the lower the priority.
     *
     * @param priority priority for the current object
     *
     * @see PriorityConstants
     */
    void setPriority(int priority);
}
