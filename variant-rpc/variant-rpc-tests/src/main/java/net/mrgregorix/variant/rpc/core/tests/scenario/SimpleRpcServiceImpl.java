package net.mrgregorix.variant.rpc.core.tests.scenario;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleRpcServiceImpl implements SimpleRpcService
{
    private final Map<String, String> resources = new HashMap<>();

    @Override
    public Date getRemoteTime()
    {
        return new Date();
    }

    @Override
    public int addNumbers(final int x, final int y)
    {
        return x + y;
    }

    @Override
    public String getResource(final String name)
    {
        return this.resources.get(name);
    }

    public void setResource(final String name, final String value)
    {
        this.resources.put(name, value);
    }

    @Override
    public int testOverloads()
    {
        return 0;
    }

    @Override
    public int testOverloads(final int x)
    {
        return x;
    }

    @Override
    public int testThrow(final int x) throws IllegalArgumentException
    {
        if (x < 5)
        {
            throw new IllegalArgumentException("That's illegal!");
        }

        return x;
    }
}
