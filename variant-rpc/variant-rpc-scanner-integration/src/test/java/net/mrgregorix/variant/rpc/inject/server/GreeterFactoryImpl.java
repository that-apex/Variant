package net.mrgregorix.variant.rpc.inject.server;

import net.mrgregorix.variant.scanner.api.Managed;

@Managed
public class GreeterFactoryImpl implements GreeterFactory
{
    @Override
    public String hello(final String name)
    {
        return "Welcome, " + name + "!";
    }
}
