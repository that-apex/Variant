package net.mrgregorix.variant.core.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

import net.mrgregorix.variant.core.VariantImpl;
import net.mrgregorix.variant.core.proxy.SimpleMapProxyCache;
import net.mrgregorix.variant.core.proxy.UniqueProxyNamingStrategy;
import net.mrgregorix.variant.core.proxy.asm.AsmProxyProvider;

/**
 * A utility class used for determine defaults values for {@link VariantBuilder}
 */
public class VariantDefaults
{
    private static final Map<VariantOption<?>, Supplier<Object>> defaultSuppliers = new HashMap<>(VariantOptions.ALL_OPTIONS.size());

    /**
     * Get a default value for the given option.
     *
     * @param option option
     *
     * @return default value
     *
     * @throws NoSuchElementException if no default value is found for the given options
     */
    public static Object getDefault(final VariantOption<?> option)
    {
        final Supplier<Object> supplier = defaultSuppliers.get(Objects.requireNonNull(option, "option"));

        if (supplier == null)
        {
            throw new NoSuchElementException("");
        }

        return supplier.get();
    }

    static
    {
        // Simple values
        defaultSuppliers.put(VariantOptions.PROXY_CACHE, SimpleMapProxyCache::new);
        defaultSuppliers.put(VariantOptions.CLASS_LOADER, VariantImpl.class::getClassLoader);
        defaultSuppliers.put(VariantOptions.PROXY_PROVIDER, AsmProxyProvider::new);
        defaultSuppliers.put(VariantOptions.PROXY_NAMING_STRATEGY, UniqueProxyNamingStrategy::new);
    }
}
