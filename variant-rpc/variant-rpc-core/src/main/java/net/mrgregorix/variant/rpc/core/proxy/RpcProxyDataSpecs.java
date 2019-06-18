package net.mrgregorix.variant.rpc.core.proxy;

import net.mrgregorix.variant.api.proxy.ProxyDataSpec;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.service.RpcService;

/**
 * Holder for {@link ProxyDataSpec} used by the RPC module.
 */
public class RpcProxyDataSpecs
{
    /**
     * Holds the {@link RpcNetworkClient} used by the {@link RpcService}. May be not set if no client with the name specified by {@link #CLIENT_NAME_SPEC} is found.
     */
    public static final ProxyDataSpec<RpcNetworkClient> CLIENT_SPEC = new ProxyDataSpec<>("Variant::RPC::Core::ClientSpec", () -> {throw new IllegalStateException("Not allowed"); });

    /**
     * Holds the name of a {@link RpcNetworkClient} that this {@link RpcService} should use. Must be always set.
     */
    public static final ProxyDataSpec<String> CLIENT_NAME_SPEC = new ProxyDataSpec<>("Variant::RPC::Core::ClientNameSpec", () -> {throw new IllegalStateException("Not allowed"); });

    private RpcProxyDataSpecs()
    {
    }
}
