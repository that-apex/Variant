package net.mrgregorix.variant.utils.priority;

/**
 * A simple implementation of {@link ModifiablePrioritizable}.
 *
 * @param <T> type of the class extending this {@link ModifiablePrioritizable}
 */
public abstract class AbstractModifiablePrioritizable <T extends ModifiablePrioritizable<T>> implements ModifiablePrioritizable<T>
{
    private int priority = PriorityConstants.NORMAL;

    @Override
    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    @Override
    public int getPriority()
    {
        return this.priority;
    }
}
