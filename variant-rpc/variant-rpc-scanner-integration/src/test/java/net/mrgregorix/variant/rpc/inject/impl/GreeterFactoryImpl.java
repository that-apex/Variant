package net.mrgregorix.variant.rpc.inject.impl;

import net.mrgregorix.variant.rpc.inject.GreeterFactory;
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
