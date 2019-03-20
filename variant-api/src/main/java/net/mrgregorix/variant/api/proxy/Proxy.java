package net.mrgregorix.variant.api.proxy;

import java.util.Map;

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
    default Class<?> getBaseClass()
    {
        return this.getClass().getSuperclass();
    }
}
