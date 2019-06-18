package net.mrgregorix.variant.rpc.inject.impl;

import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.rpc.inject.GreetingServiceRpc;
import net.mrgregorix.variant.rpc.inject.PublicGreeting;
import net.mrgregorix.variant.rpc.inject.annotation.InjectRpc;
import net.mrgregorix.variant.scanner.api.Managed;

@Managed
public class PublicGreetingImpl implements PublicGreeting
{
    private final GreetingServiceRpc greetingServiceRpc;

    @Inject
    public PublicGreetingImpl(@InjectRpc("greeting") final GreetingServiceRpc greetingServiceRpc)
    {
        this.greetingServiceRpc = greetingServiceRpc;
    }

    @Override
    public void greet(final String name)
    {
        this.greetingServiceRpc.greet(name);
    }
}
