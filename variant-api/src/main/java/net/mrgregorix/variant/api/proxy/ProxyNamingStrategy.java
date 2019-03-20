package net.mrgregorix.variant.api.proxy;

import net.mrgregorix.variant.api.Variant;

/**
 * Defines how proxy classes are named.
 */
public interface ProxyNamingStrategy
{
    /**
     * Creates a name for a proxy class
     *
     * @param variant          {@link Variant} instance that is used to create the proxy
     * @param classToBeProxied class to be proxied
     *
     * @return name for the proxy class
     */
    String nameProxyClass(Variant variant, Class<?> classToBeProxied);
}
