package net.mrgregorix.variant.api.proxy;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Provider for creating proxy classes at the runtime.
 *
 * <p>
 * A proxy class is able to intercept any calls to base methods of a type that is being proxied.
 */
public interface ProxyProvider
{
    /**
     * Creates a new proxy type for the given type.
     * <p>
     * The returned proxy class is guaranteed to extend the given <code>type</code> and implement the {@link Proxy} interface.
     * <p>
     * The returned proxy class must also implement the same constructors that the base class had.
     *
     * @param classLoader        class loader that will be used to load the created proxy class
     * @param type               class to be proxied
     * @param proxyClassname     how to name the proxy class (full name, with package). May be ignored if the provider does not support it.
     * @param invocationHandlers invocation handlers for method to be proxied, method not contained in this map should not be proxied.
     * @param <T>                type to proxy
     *
     * @return newly created proxy type
     */
    <T> Class<? extends T> createProxy(ClassLoader classLoader, Class<T> type, String proxyClassname, Map<Method, List<ProxyInvocationHandler>> invocationHandlers);
}
