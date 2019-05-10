package net.mrgregorix.variant.core.proxy.asm;

import java.lang.reflect.Method;

import net.mrgregorix.variant.api.proxy.BeforeInvocationResult;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.api.proxy.ProxyInvocationHandler;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

public class MarkingTestHandler extends AbstractModifiablePrioritizable<MarkingTestHandler> implements ProxyInvocationHandler<MarkingTestHandler>
{
    private boolean flag;

    public boolean getAndClearFlag()
    {
        if (this.flag)
        {
            this.flag = false;
            return true;
        }

        return false;
    }

    @Override
    public BeforeInvocationResult beforeInvocation(Proxy proxy, Method method, Object[] arguments)
    {
        this.flag = true;
        return BeforeInvocationResult.proceed();
    }

    @Override
    public Object afterInvocation(Proxy proxy, Method method, Object[] arguments, Object returnValue)
    {
        this.flag = true;
        return returnValue;
    }
}
