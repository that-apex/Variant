package net.mrgregorix.variant.core.proxy.asm;

import java.lang.reflect.Method;

import net.mrgregorix.variant.api.proxy.BeforeInvocationResult;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.api.proxy.ProxyInvocationHandler;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

class InterfaceTestHandler extends AbstractModifiablePrioritizable<InterfaceTestHandler> implements ProxyInvocationHandler<InterfaceTestHandler>
{
    @Override
    public BeforeInvocationResult beforeInvocation(Proxy proxy, Method method, Object[] arguments)
    {
        InterfaceTest.CallCount.VALUE++;
        return BeforeInvocationResult.proceed();
    }

    @Override
    public Object afterInvocation(Proxy proxy, Method method, Object[] arguments, Object returnValue)
    {
        return returnValue;
    }
}