package net.mrgregorix.variant.api.proxy;

import java.util.function.Function;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * <p>A proxy cache is a cache that contains all proxies created by a specific {@link Variant} instance. </p>
 */
public interface ProxyCache
{
    /**
     * <p>Searches a proxy for the given type in this cache</p>
     *
     * @param baseType base type
     * @param <T>      type of the base class
     *
     * @return proxy type of the given base type, or null if no proxy is type is found
     */
    @Nullable
    <T> Class<? extends T> findProxy(Class<T> baseType);

    /**
     * <p>Checks whether the given base type has an associated cache entry</p>
     *
     * @param type base type
     *
     * @return true if there is a cache entry for the given base type, false if otherwise
     */
    boolean isPresentInCache(Class<?> type);

    /**
     * <p>Adds the given base type and its associated proxy type to the cache</p>
     *
     * @param baseType  base type
     * @param proxyType proxy type, it must extends base type and {@link Proxy}
     * @param <T>       type of the base class
     *
     * @return true if the cache was modified as the result of this method, false if there already was exactly the same entry as the one provided
     *
     * @throws IllegalArgumentException thrown when proxyType does not implements {@link Proxy} interface
     */
    <T> boolean addToCache(Class<T> baseType, Class<? extends T> proxyType);

    /**
     * <p>Add the given base type and its associated proxy type to the cache only if
     * an entry witch such base type does not yet exists in this cache</p>
     *
     * @param baseType  base type
     * @param proxyType supplier for the proxy type, it will be called only if there is no entry with the same base class as provided. The function's argument is the same as <code>baseType</code>.
     *                  This cannot return null.
     * @param <T>       type of the base class
     *
     * @return the current (existing or computed) proxy for the base type
     */
    default <T> Class<? extends T> addIfAbsent(final Class<T> baseType,
                                               final Function<Class<T>, Class<? extends T>> proxyType)
    {
        final Class<? extends T> proxy = this.findProxy(baseType);
        if (proxy != null)
        {
            return proxy;
        }

        final Class<? extends T> newType = proxyType.apply(baseType);
        this.addToCache(baseType, newType);
        return newType;
    }
}
