package net.mrgregorix.variant.core.proxy;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.proxy.ProxyNamingStrategy;

/**
 * Generates unique proxy names using hash code of a {@link Variant} instance.
 */
public class UniqueProxyNamingStrategy implements ProxyNamingStrategy
{
    @SuppressWarnings("MagicNumber")
    @Override
    public String nameProxyClass(final Variant variant, final Class<?> classToBeProxied)
    {
        return classToBeProxied.getName() + "$VariantProxy_" + Integer.toString(variant.hashCode(), 16);
    }
}
