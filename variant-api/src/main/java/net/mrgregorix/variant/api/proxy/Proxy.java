package net.mrgregorix.variant.api.proxy;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * <p>Base interface for all classes that are proxied.</p>
 *
 * <p>When creating a proxy class using a {@link ProxyProvider} the created class MUST extends this <code>Proxy</code>
 * interface. </p>
 */
public interface Proxy
{
    /**
     * <p>Returns the base class that is being proxied</p>
     *
     * <p>
     * This should always return a class that was used in {@link ProxyProvider#createProxy(ClassLoader, Class, String, Map)}
     * </p>
     *
     * <p>
     * The default implementation returns <code>this</code> object's superclass
     * </p>
     *
     * @return the base class that is being proxied
     */
    default Class<?> getProxyBaseClass()
    {
        for (final Class<?> anInterface : this.getClass().getInterfaces())
        {
            if (! Proxy.class.isAssignableFrom(anInterface))
            {
                return anInterface;
            }
        }

        return this.getClass().getSuperclass();
    }

    /**
     * Calls {@link Callable#call()} on the given runnable immediately. The first proxy method call in that callable will be called omitting the proxy
     * <p>
     * This method is 100% thread safe.
     *
     * @param callable callable to call
     * @param <R>      return type of the callable
     *
     * @return value returned by {@link Callable#call()}
     */
    <R> R callWithoutProxy(Callable<R> callable);

    /**
     * Calls {@link Runnable#run()} on the given runnable immediately. The first proxy method call in that runnable will be called omitting the proxy
     * <p>
     * This method is 100% thread safe.
     *
     * @param runnable runnable to call
     */
    @SuppressWarnings("ReturnOfNull")
    default void runWithoutProxy(final Runnable runnable)
    {
        this.callWithoutProxy(() ->
                              {
                                  runnable.run();
                                  return null;
                              });
    }

    /**
     * Returns a map that is initialized to null in every new proxy instance. You can hold any data in there.
     * <p>
     * Every key should be in format: {moduleName}::{anyIdentifier}. I. e. Variant::Core::MyData
     *
     * @return map of data
     */
    Map<String, Object> getAdditionalProxyData();

    /**
     * Returns data from the data map or adds a default value if no key is found and returns it.
     *
     * @param key             key to find
     * @param defaultSupplier supplier for default value, will be called only if key was not found
     *
     * @return found data or default value
     *
     * @see #getAdditionalProxyData()
     */
    @SuppressWarnings("unchecked")
    default <T> T getAdditionalProxyData(final String key, final Supplier<T> defaultSupplier)
    {
        return (T) this.getAdditionalProxyData().computeIfAbsent(key, (ignored) -> defaultSupplier.get());
    }
}
