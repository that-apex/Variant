package net.mrgregorix.variant.api.proxy;

import java.util.function.Supplier;

/**
 * A helper class for getting data from {@link Proxy#getAdditionalProxyData()}.
 *
 * @param <T> type of the data
 */
public class ProxyDataSpec <T>
{
    private final String                name;
    private final Supplier<? extends T> defaultValueSupplier;

    /**
     * Constructs a new {@link ProxyDataSpec} using the given name.
     *
     * @param name                 name to be used. This will be used as a key in additional proxy data map
     * @param defaultValueSupplier supplier that will be called to get a default value for a spec.
     */
    public ProxyDataSpec(final String name, final Supplier<? extends T> defaultValueSupplier)
    {
        this.name = name;
        this.defaultValueSupplier = defaultValueSupplier;
    }

    /**
     * Reads a value described by this spec from the given proxy.
     *
     * @param proxy proxy to read the value from
     *
     * @return value read from the proxy
     */
    public T from(final Proxy proxy)
    {
        return proxy.getAdditionalProxyData(this.name, this.defaultValueSupplier);
    }

    /**
     * Sets a value described by this spec for the given proxy
     *
     * @param proxy  proxy to set the value at
     * @param object new value
     */
    public void set(final Proxy proxy, final T object)
    {
        proxy.getAdditionalProxyData().put(this.name, object);
    }

    /**
     * Checks whether any data described by this spec is present in the given proxy data.
     *
     * @param proxy proxy to check data from
     *
     * @return whether this spec's data is present
     */
    public boolean isPresent(final Proxy proxy)
    {
        return proxy.getAdditionalProxyData().containsKey(this.name);
    }
}
