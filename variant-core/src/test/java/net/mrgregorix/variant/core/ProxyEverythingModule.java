package net.mrgregorix.variant.core;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.api.proxy.ProxyInvocationHandler;
import net.mrgregorix.variant.api.proxy.ProxyMatchResult;
import net.mrgregorix.variant.api.proxy.ProxySpecification;

public class ProxyEverythingModule implements VariantModule
{
    private final ProxyInvocationHandler<?> handler;

    public ProxyEverythingModule(ProxyInvocationHandler<?> handler)
    {
        this.handler = handler;
    }

    @Override
    public String getName()
    {
        return "TestOnly::ProxyEverythingModule";
    }

    @Override
    public void initialize(Variant variant)
    {
    }

    @Override
    public Collection<ProxySpecification> getProxySpecifications()
    {
        return Collections.singleton(new DummyProxySpecification(this.handler));
    }

    private static final class DummyProxySpecification implements ProxySpecification
    {
        private final ProxyInvocationHandler<?> handler;

        public DummyProxySpecification(final ProxyInvocationHandler<?> handler)
        {
            this.handler = handler;
        }

        @Override
        public ProxyMatchResult shouldProxy(Method method)
        {
            return method.getDeclaringClass() == Object.class ? ProxyMatchResult.noMatch() : ProxyMatchResult.match(this.handler);
        }

        @Override
        public void setPriority(int priority)
        {

        }
    }
}
