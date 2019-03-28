package net.mrgregorix.variant.utils.priority;

/**
 * A {@link Prioritizable} which priority can be adjusted programatically.
 *
 * @param <T>
 */
public interface ModifiablePrioritizable <T extends ModifiablePrioritizable<T>> extends Prioritizable<T>
{
    /**
     * Sets the priority for the current object, the lower the value the lower the priority.
     *
     * @param priority priority for the current object
     */
    void setPriority(int priority);
}
