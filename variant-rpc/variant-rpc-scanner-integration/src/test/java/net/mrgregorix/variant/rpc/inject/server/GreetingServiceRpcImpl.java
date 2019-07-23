package net.mrgregorix.variant.rpc.inject.server;

import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.rpc.inject.annotation.RpcServiceImplementation;
import net.mrgregorix.variant.rpc.inject.client.GreetingServiceRpc;

@RpcServiceImplementation(groups = "test-server", service = GreetingServiceRpc.class)
public class GreetingServiceRpcImpl implements GreetingServiceRpc
{
    private final GreeterFactory greeterFactory;

    @Inject
    public GreetingServiceRpcImpl(final GreeterFactory greeterFactory)
    {
        this.greeterFactory = greeterFactory;
    }

    @Override
    public void greet(final String name)
    {
        final String hello = this.greeterFactory.hello(name);
        System.out.println("[GreetingService] " + hello);
    }
}
