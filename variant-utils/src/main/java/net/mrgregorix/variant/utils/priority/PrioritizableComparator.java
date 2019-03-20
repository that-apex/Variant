package net.mrgregorix.variant.utils.priority;

import java.util.Comparator;

/**
 * Comparator for {@link Prioritizable} types.
 */
public class PrioritizableComparator <T extends Prioritizable> implements Comparator<T>
{
    @Override
    public int compare(final T o1, final T o2)
    {
        if (o1 == o2)
        {
            return 0;
        }

        if (o1.getPriority() == o2.getPriority())
        {
            return o1.hashCode() - o2.hashCode();
        }

        return o1.getPriority() - o2.getPriority();
    }
}
