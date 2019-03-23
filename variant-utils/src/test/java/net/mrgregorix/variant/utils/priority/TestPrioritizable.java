package net.mrgregorix.variant.utils.priority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestPrioritizable
{
    private static class CustomPrioritizable implements Prioritizable<CustomPrioritizable>
    {
        private final int priority;

        public CustomPrioritizable(int priority)
        {
            this.priority = priority;
        }

        @Override
        public int getPriority()
        {
            return this.priority;
        }
    }

    @Test
    public void testSamePriorities()
    {
        final Set<CustomPrioritizable> customPrioritizables = new TreeSet<>();

        final CustomPrioritizable c1 = new CustomPrioritizable(10);
        final CustomPrioritizable c2 = new CustomPrioritizable(10);
        final CustomPrioritizable c3 = new CustomPrioritizable(10);

        customPrioritizables.add(c1);
        customPrioritizables.add(c2);
        customPrioritizables.add(c3);

        Assertions.assertTrue(customPrioritizables.contains(c1), "c1 is not contained in the set");
        Assertions.assertTrue(customPrioritizables.contains(c2), "c2 is not contained in the set");
        Assertions.assertTrue(customPrioritizables.contains(c3), "c3 is not contained in the set");
    }

    @Test
    public void testPriorities()
    {
        final CustomPrioritizable highestPriority = new CustomPrioritizable(50);
        final CustomPrioritizable normalPriority = new CustomPrioritizable(30);
        final CustomPrioritizable lowestPriority = new CustomPrioritizable(10);

        final List<CustomPrioritizable> allPrioritizables = new ArrayList<>();
        allPrioritizables.add(highestPriority);
        allPrioritizables.add(normalPriority);
        allPrioritizables.add(lowestPriority);

        for (int i = 0; i < 5; i++)
        {
            Collections.shuffle(allPrioritizables);

            final Set<CustomPrioritizable> customPrioritizables = new TreeSet<>(allPrioritizables);
            final Iterator<CustomPrioritizable> iterator = customPrioritizables.iterator();

            Assertions.assertSame(highestPriority, iterator.next(), "Prioritizables were sorted incorrectly");
            Assertions.assertSame(normalPriority, iterator.next(), "Prioritizables were sorted incorrectly");
            Assertions.assertSame(lowestPriority, iterator.next(), "Prioritizables were sorted incorrectly");
        }
    }
}
