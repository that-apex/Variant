package net.mrgregorix.variant.utils.priority;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PrioritizableTest
{
    @Test
    public void testDefaultPriority()
    {
        class SimplePrioritizable implements Prioritizable<SimplePrioritizable>
        {
        }

        final SimplePrioritizable simplePrioritizable = new SimplePrioritizable();

        assertThat("default priority should always be 0", simplePrioritizable.getPriority(), is(0));
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void testComparison()
    {
        final TestPrioritizable lowPriority = new TestPrioritizable(1);
        final TestPrioritizable normalPriority1 = new TestPrioritizable(5);
        final TestPrioritizable normalPriority2 = new TestPrioritizable(5);
        final TestPrioritizable highPriority = new TestPrioritizable(10);

        assertThat("invalid priorities", lowPriority.compareTo(normalPriority1), greaterThan(0));
        assertThat("invalid priorities", normalPriority1.compareTo(highPriority), greaterThan(0));
        assertThat("invalid priorities", lowPriority.compareTo(highPriority), greaterThan(0));

        assertThat("invalid priorities", normalPriority1.compareTo(lowPriority), lessThan(0));
        assertThat("invalid priorities", highPriority.compareTo(normalPriority1), lessThan(0));
        assertThat("invalid priorities", highPriority.compareTo(lowPriority), lessThan(0));

        assertThat("different elements with same priority should not return 0", normalPriority1.compareTo(normalPriority2), not(is(0)));
        assertThat("same elements with same priority should return 0", normalPriority1.compareTo(normalPriority1), is(0));
    }
}
