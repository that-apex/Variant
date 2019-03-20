package net.mrgregorix.variant.utils.priority;

/**
 * Represents an object that has a priority
 */
public interface Prioritizable
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
}
