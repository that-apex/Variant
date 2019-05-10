package net.mrgregorix.variant.utils.priority;

class TestPrioritizable implements ModifiablePrioritizable<TestPrioritizable>
{
    private int priority;

    public TestPrioritizable(final int priority)
    {
        this.priority = priority;
    }

    @Override
    public int getPriority()
    {
        return this.priority;
    }

    @Override
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
}
