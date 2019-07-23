package net.mrgregorix.variant.rpc.inject.config;

import net.mrgregorix.variant.rpc.api.network.authenticator.RpcAuthenticator;

/**
 * Represents a configuration of a {@link RpcAuthenticator}
 */
public abstract class RpcConfigAuthentication<T extends RpcAuthenticator>
{
    /**
     * Create a {@link RpcAuthenticator} based on this configuration
     *
     * @return a new instance of the authenticator
     */
    public abstract T createAuthenticator();
}
