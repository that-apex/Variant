package net.mrgregorix.variant.rpc.inject.impl;

import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.rpc.inject.GreeterFactory;
import net.mrgregorix.variant.rpc.inject.GreetingServiceRpc;
import net.mrgregorix.variant.rpc.inject.annotation.RpcServiceImplementation;

@RpcServiceImplementation(group = "greeting", service = GreetingServiceRpc.class)
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
