package net.mrgregorix.variant.rpc.network.netty.component;

import java.util.concurrent.Future;

import net.mrgregorix.variant.rpc.api.network.RpcNetworkComponent;
import net.mrgregorix.variant.rpc.network.netty.configuration.ConfigurationFactory;

public abstract class AbstractNettyNetworkComponent implements RpcNetworkComponent
{
    private final ConfigurationFactory configurationFactory;
    private final String               name;
    private final String               address;
    private final int                  port;

    protected AbstractNettyNetworkComponent(final ConfigurationFactory configurationFactory, final String name, final String address, final int port)
    {
        this.configurationFactory = configurationFactory;
        this.name = name;
        this.address = address;
        this.port = port;
    }

    public ConfigurationFactory getConfigurationFactory()
    {
        return this.configurationFactory;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getAddress()
    {
        return this.address;
    }

    @Override
    public int getPort()
    {
        return this.port;
    }

    public abstract Future<Void> dispose();
}
