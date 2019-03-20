package net.mrgregorix.variant.api.proxy;

import java.lang.reflect.Method;

/**
 * <p>A definition of how a proxy should be created</p>
 */
public interface ProxySpecification
{
    /**
     * <p>Checks if the provided <code>method</code> should be proxied by the proxy.</p>
     *
     * @param method method to be checked
     *
     * @return true if the method should be proxied, false otherwise
     */
    ProxyMatchResult shouldProxy(Method method);
}
