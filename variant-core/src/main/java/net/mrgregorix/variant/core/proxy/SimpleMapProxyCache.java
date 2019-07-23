package net.mrgregorix.variant.core.proxy;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.api.proxy.ProxyCache;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * A {@link ProxyCache} that is based of a simple {@link Map}
 */
public class SimpleMapProxyCache implements ProxyCache
{
    private final Map<Class<?>, Class<?>> map;

    /**
     * Creates a new cache using a default {@link Map} implementation.
     * <p>
     * Default to {@link HashMap}
     */
    public SimpleMapProxyCache()
    {
        this(Maps.newHashMap());
    }

    /**
     * Creates a new cache using the supplied map as value holder.
     *
     * @param map map to be used
     */
    public SimpleMapProxyCache(final Map<Class<?>, Class<?>> map)
    {
        this.map = Objects.requireNonNull(map, "map");
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable
    <T> Class<? extends T> findProxy(Class<T> baseType)
    {
        return (Class<? extends T>) this.map.get(Objects.requireNonNull(baseType, "baseType"));
    }

    @Override
    public boolean isPresentInCache(Class<?> type)
    {
        return this.map.containsKey(type);
    }

    @Override
    public <T> boolean addToCache(Class<T> baseType, Class<? extends T> proxyType)
    {
        Preconditions.checkArgument(Proxy.class.isAssignableFrom(proxyType), "proxy !instanceof " + Proxy.class.getName());
        Preconditions.checkArgument(baseType.isAssignableFrom(proxyType), "proxy !instanceof base");

        return this.map.put(Objects.requireNonNull(baseType, "baseType"), proxyType) != proxyType;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Class<? extends T> addIfAbsent(Class<T> baseType, Function<? super Class<T>, Class<? extends T>> proxyType)
    {
        return (Class<? extends T>) this.map.computeIfAbsent(baseType, (ignored) -> proxyType.apply(baseType));
    }
}
