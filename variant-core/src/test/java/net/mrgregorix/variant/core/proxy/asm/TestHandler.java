package net.mrgregorix.variant.core.proxy.asm;

import java.lang.reflect.Method;

import net.mrgregorix.variant.api.proxy.BeforeInvocationResult;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.api.proxy.ProxyInvocationHandler;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

class TestHandler extends AbstractModifiablePrioritizable<TestHandler> implements ProxyInvocationHandler<TestHandler>
{
    @Override
    public BeforeInvocationResult beforeInvocation(Proxy proxy, Method method, Object[] arguments)
    {
        arguments[0] = (int) arguments[0] + 5;
        return BeforeInvocationResult.proceed();
    }

    @Override
    public Object afterInvocation(Proxy proxy, Method method, Object[] arguments, Object returnValue)
    {
        return returnValue;
    }
}