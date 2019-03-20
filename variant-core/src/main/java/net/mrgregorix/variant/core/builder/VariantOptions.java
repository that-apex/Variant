package net.mrgregorix.variant.core.builder;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.api.proxy.ProxyCache;
import net.mrgregorix.variant.api.proxy.ProxyNamingStrategy;
import net.mrgregorix.variant.api.proxy.ProxyProvider;

/**
 * Represents all options that can be set using a {@link VariantBuilder}
 */
public final class VariantOptions
{
    /**
     * Cache that will be used for caching the proxied classes
     */
    public static final VariantOption<ProxyCache> PROXY_CACHE = new VariantOption<>();

    /**
     * Class loader for creating proxy class instances.
     */
    public static final VariantOption<ClassLoader> CLASS_LOADER = new VariantOption<>();

    /**
     * Provider for creating proxy class instances
     */
    public static final VariantOption<ProxyProvider> PROXY_PROVIDER = new VariantOption<>();

    /**
     * Provider for proxy names.
     */
    public static final VariantOption<ProxyNamingStrategy> PROXY_NAMING_STRATEGY = new VariantOption<>();

    /**
     * Modules to be registered when creating the {@link Variant} instance.
     */
    public static final VariantOption<Collection<VariantModule>> MODULES = new VariantOption<>();

    /**
     * List containing all available options
     */
    public static final ImmutableList<VariantOption<?>> ALL_OPTIONS;

    static
    {
        ALL_OPTIONS = ImmutableList.<VariantOption<?>>builder()
            .add(PROXY_CACHE)
            .add(CLASS_LOADER)
            .add(PROXY_PROVIDER)
            .add(PROXY_NAMING_STRATEGY)
            .add(MODULES)
            .build();
    }

    private VariantOptions()
    {
    }
}
