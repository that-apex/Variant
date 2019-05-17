package net.mrgregorix.variant.rpc.core.scenario;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SimpleRpcServiceImpl implements SimpleRpcService
{
    private Map<String, String> resources = new HashMap<>();

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
}
