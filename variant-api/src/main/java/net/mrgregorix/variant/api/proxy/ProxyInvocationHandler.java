package net.mrgregorix.variant.api.proxy;

import java.lang.reflect.Method;

import net.mrgregorix.variant.utils.priority.Prioritizable;

/**
 * A handler to handle the before and after invocations of the proxied methods.
 * <p>
 * This handler will be called every time when a proxied method is called (and no previous handler has already cancelled the call).
 */
public interface ProxyInvocationHandler extends Prioritizable
{
    /**
     * Called before an invocation of the super method.
     *
     * @param proxy     the object that the function is called on
     * @param method    the original {@link Method} that is being proxied
     * @param arguments array of arguments passed to this method call, this can be altered to alter the arguments for the super method call
     *
     * @return a result describing what to do with this invocation, either {@link BeforeInvocationResult#proceed()} or {@link BeforeInvocationResult#returnNow(Object)} may be used
     *
     * @throws Throwable any exception thrown by this method will just be rethrown
     */
    BeforeInvocationResult beforeInvocation(Proxy proxy, Method method, Object[] arguments) throws Throwable;

    /**
     * Called after an invocation of the super method (or instantly after the {@link #beforeInvocation(Proxy, Method, Object[])} call in case of the super method being abstract).
     *
     * @param proxy       the object that the function is called on
     * @param method      the original {@link Method} that is being proxied
     * @param arguments   array of arguments that the super method was called on (this may not be the original arguments passed to the method but instead be already modified by beforeInvocation call)
     * @param returnValue the return value that has been returned by the super method (this may have been altered by the previous handler)
     *
     * @return the value to be returned from proxied function, if you do not wish to alter the return value just return a value passed from {@code returnValue} parameter.
     *
     * @throws Throwable any exception thrown by this method will just be rethrown
     */
    Object afterInvocation(Proxy proxy, Method method, Object[] arguments, Object returnValue) throws Throwable;
}
