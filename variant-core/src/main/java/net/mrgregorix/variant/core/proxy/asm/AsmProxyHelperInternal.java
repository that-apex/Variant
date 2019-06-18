package net.mrgregorix.variant.core.proxy.asm;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import net.mrgregorix.variant.api.proxy.BeforeInvocationResult;
import net.mrgregorix.variant.api.proxy.ProxyInvocationHandler;
import net.mrgregorix.variant.utils.exception.ExceptionUtils;

/**
 * Internal helper class. This should never be used directly.
 */
public class AsmProxyHelperInternal
{
    private static final AtomicInteger GLOBAL_ID = new AtomicInteger(0);

    private static final Map<Integer, Method>                   METHODS             = new HashMap<>();
    private static final Map<Integer, ProxyInvocationHandler[]> INVOCATION_HANDLERS = new HashMap<>();

    public static BeforeInvocationResult beforeInvoke(final InternalProxy proxy, final Method method, final Object[] arguments, final ProxyInvocationHandler[] handlers)
    {
        if (proxy.getProxyOmitFlag())
        {
            return BeforeInvocationResult.proceed();
        }

        for (final ProxyInvocationHandler<?> handler : handlers)
        {
            try
            {
                final BeforeInvocationResult beforeInvocationResult = handler.beforeInvocation(proxy, method, arguments);

                if (! beforeInvocationResult.shouldProceed())
                {
                    return beforeInvocationResult;
                }
            }
            catch (final Throwable throwable)
            {
                sneakyThrow(throwable);
            }
        }

        return BeforeInvocationResult.proceed();
    }

    public static Object afterInvoke(final Object returnValue, final InternalProxy proxy, final Method method, final Object[] arguments, final ProxyInvocationHandler[] handlers)
    {
        if (proxy.getProxyOmitFlag())
        {
            proxy.setProxyOmitFlag(false);
            return returnValue;
        }

        Object[] actualReturn = new Object[1];

        for (final ProxyInvocationHandler<?> handler : handlers)
        {
            try
            {
                actualReturn[0] = ExceptionUtils.marker(() -> handler.afterInvocation(proxy, method, arguments, actualReturn[0]));
            }
            catch (final Throwable throwable)
            {
                sneakyThrow(throwable);
            }
        }

        return actualReturn[0];
    }

    public static int getId()
    {
        return GLOBAL_ID.incrementAndGet();
    }

    public static void register(final int id, final Method method, final ProxyInvocationHandler[] invocationHandlers)
    {
        if (METHODS.containsKey(id) || INVOCATION_HANDLERS.containsKey(id))
        {
            throw new IllegalStateException("doubled id");
        }

        METHODS.put(id, method);
        INVOCATION_HANDLERS.put(id, invocationHandlers);
    }

    public static Method getMethod(final int id)
    {
        return METHODS.get(id);
    }

    public static ProxyInvocationHandler[] getInvocationHandlers(final int id)
    {
        return INVOCATION_HANDLERS.get(id);
    }

    /**
     * Dirty ooga booga
     */
    @SuppressWarnings("unchecked")
    public static <T extends Throwable> void sneakyThrow(final Throwable t) throws T
    {
        throw (T) t;
    }
}
