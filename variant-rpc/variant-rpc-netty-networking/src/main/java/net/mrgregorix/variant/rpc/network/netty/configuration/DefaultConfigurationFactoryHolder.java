package net.mrgregorix.variant.rpc.network.netty.configuration;

import java.util.Objects;

import io.netty.channel.epoll.Epoll;
import net.mrgregorix.variant.rpc.network.netty.NettyRpcNetworkingProvider;

/**
 * Holder class for {@link ConfigurationFactory} that will be used as a default configuration factory for every new {@link NettyRpcNetworkingProvider}
 */
public class DefaultConfigurationFactoryHolder
{
    private static ConfigurationFactory configurationFactory = Epoll.isAvailable() ? new EpollConfigurationFactory() : new NioConfigurationFactory();

    /**
     * Gets the default {@link ConfigurationFactory}
     *
     * @return the default {@link ConfigurationFactory}
     */
    public static ConfigurationFactory getConfigurationFactory()
    {
        return configurationFactory;
    }

    /**
     * Sets the default {@link ConfigurationFactory}
     *
     * @param configurationFactory the default {@link ConfigurationFactory}
     */
    public static void setConfigurationFactory(final ConfigurationFactory configurationFactory)
    {
        DefaultConfigurationFactoryHolder.configurationFactory = Objects.requireNonNull(configurationFactory, "configurationFactory");
    }
}
