package net.mrgregorix.variant.inject.core.full.simple;

public class SomeServiceImpl implements SomeService
{
    private final String data;

    public SomeServiceImpl(String data)
    {
        this.data = data;
    }

    @Override
    public String getData()
    {
        return this.data;
    }
}
