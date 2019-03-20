package net.mrgregorix.variant.core.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.api.proxy.ProxyCache;
import net.mrgregorix.variant.api.proxy.ProxyNamingStrategy;
import net.mrgregorix.variant.api.proxy.ProxyProvider;
import net.mrgregorix.variant.core.VariantImpl;
import net.mrgregorix.variant.core.coremodule.VariantCoreModule;

/**
 * A utility class used to create instances of {@link Variant}.
 */
public class VariantBuilder
{
    private final Map<VariantOption<?>, Object> values = new HashMap<>(VariantOptions.ALL_OPTIONS.size());

    /**
     * Construct a new builder.
     */
    public VariantBuilder()
    {
        this.values.put(VariantOptions.MODULES, new ArrayList<>(1));
        this.getValue(VariantOptions.MODULES).add(new VariantCoreModule());
    }

    /**
     * Sets an option's value.
     *
     * @param option option
     * @param value  value to be set
     * @param <T>    type of the value
     *
     * @return {@code this} instance.
     *
     * @see VariantOptions
     */
    public <T> VariantBuilder withOption(final VariantOption<T> option, final T value)
    {
        this.values.put(option, value);
        return this;
    }

    /**
     * Sets cache to be used for caching proxied class instances
     *
     * @param proxyCache cache to be used
     *
     * @return {@code this} instance.
     */
    public VariantBuilder withProxyCache(final ProxyCache proxyCache)
    {
        return this.withOption(VariantOptions.PROXY_CACHE, proxyCache);
    }


    /**
     * Sets class loader for creating proxy class instances
     *
     * @param classLoader class loader to be used
     *
     * @return {@code this} instance
     */
    public VariantBuilder withClassLoader(final ClassLoader classLoader)
    {
        return this.withOption(VariantOptions.CLASS_LOADER, classLoader);
    }

    /**
     * Sets provider for creating proxy class instances
     *
     * @param proxyProvider provider to be used
     *
     * @return {@code this} instance
     */
    public VariantBuilder withProxyProvider(final ProxyProvider proxyProvider)
    {
        return this.withOption(VariantOptions.PROXY_PROVIDER, proxyProvider);
    }

    /**
     * Sets provider for proxy names.
     *
     * @param proxyNamingStrategy naming strategy to be used
     *
     * @return {@code this} instance
     */
    public VariantBuilder withProxyProvider(final ProxyNamingStrategy proxyNamingStrategy)
    {
        return this.withOption(VariantOptions.PROXY_NAMING_STRATEGY, proxyNamingStrategy);
    }

    /**
     * Add modules that will be registered automatically when the {@link Variant} is created.
     *
     * @param modules modules to be added
     *
     * @return {@code this} instance
     */
    public VariantBuilder withModules(final VariantModule... modules)
    {
        Collections.addAll(this.getValue(VariantOptions.MODULES), modules);
        return this;
    }

    /**
     * Do not register the default module automatically.
     *
     * @return {@code this} instance
     */
    public VariantBuilder noDefaultModule()
    {
        this.getValue(VariantOptions.MODULES).removeIf(VariantCoreModule.class::isInstance);
        return this;
    }

    /**
     * Finalizes the building of this {@link Variant}
     *
     * @return build {@link Variant} instance.
     */
    public Variant build()
    {
        VariantOptions.ALL_OPTIONS
            .stream()
            .filter(option -> ! this.values.containsKey(option))
            .forEach(option -> this.values.put(option, VariantDefaults.getDefault(option)));

        return new VariantImpl(
            this.getValue(VariantOptions.PROXY_CACHE),
            this.getValue(VariantOptions.CLASS_LOADER),
            this.getValue(VariantOptions.PROXY_PROVIDER),
            this.getValue(VariantOptions.PROXY_NAMING_STRATEGY),
            this.getValue(VariantOptions.MODULES)
        );
    }

    @SuppressWarnings("unchecked")
    private <T> T getValue(final VariantOption<T> option)
    {
        return (T) this.values.get(option);
    }
}

