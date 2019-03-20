package net.mrgregorix.variant.api.proxy;

import java.lang.reflect.Method;

import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Represents a result of {@link ProxyInvocationHandler#beforeInvocation(Proxy, Method, Object[])} call.
 * <p>
 * This result may decide if a super method is called or if the function should return immediately.
 */
public class BeforeInvocationResult
{
    private static final BeforeInvocationResult PROCEED = new BeforeInvocationResult(true, null);
    private final        boolean                proceed;
    private final        Object                 returnValue;

    private BeforeInvocationResult(final boolean proceed, @Nullable final Object returnValue)
    {
        this.proceed = proceed;
        this.returnValue = returnValue;
    }

    /**
     * Should the function call proceed and the next handler invoked (or the super method if this was the last handler)
     * <p>
     * Returning false will result in the proxy method immediately returning with the return value of {@link #getReturnValue()}
     *
     * @return true to proceed, false to return immediately.
     */
    public boolean shouldProceed()
    {
        return this.proceed;
    }

    /**
     * Gets the value to be returned from the function if the {@link #shouldProceed()} is set to false.
     * <p>
     * The return value of this function is unspecified if the {@link #shouldProceed()} returns true.
     *
     * @return value to be returned
     */
    @Nullable
    public Object getReturnValue()
    {
        return this.returnValue;
    }

    /**
     * Gets a {@link BeforeInvocationResult} that makes the call to proceed to the next handler (or super method)
     *
     * @return a proceed handler
     */
    public static BeforeInvocationResult proceed()
    {
        return BeforeInvocationResult.PROCEED;
    }

    /**
     * Gets a {@link BeforeInvocationResult} that makes the function return immediately with the given {@code returnValue}
     *
     * @param returnValue value to be returned
     *
     * @return a "return now" result
     */
    public static BeforeInvocationResult returnNow(@Nullable final Object returnValue)
    {
        return new BeforeInvocationResult(false, returnValue);
    }
}
