package net.mrgregorix.variant.utils.reflect;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class PrimitiveUtils
{
    private static final List<Class<?>>            PRIMITIVES             = Arrays.asList(boolean.class, char.class, byte.class, short.class, int.class, long.class, float.class, double.class);
    private static final BiMap<Class<?>, Class<?>> WRAPPERS_TO_PRIMITIVES = HashBiMap.create(PRIMITIVES.size());

    static
    {
        WRAPPERS_TO_PRIMITIVES.put(Boolean.class, boolean.class);
        WRAPPERS_TO_PRIMITIVES.put(Character.class, char.class);
        WRAPPERS_TO_PRIMITIVES.put(Byte.class, byte.class);
        WRAPPERS_TO_PRIMITIVES.put(Short.class, short.class);
        WRAPPERS_TO_PRIMITIVES.put(Integer.class, int.class);
        WRAPPERS_TO_PRIMITIVES.put(Long.class, long.class);
        WRAPPERS_TO_PRIMITIVES.put(Float.class, float.class);
        WRAPPERS_TO_PRIMITIVES.put(Double.class, double.class);
    }

    public static Class<?> wrapperToPrimitive(final Class<?> wrapper)
    {
        return Objects.requireNonNull(WRAPPERS_TO_PRIMITIVES.get(wrapper));
    }

    public static Class<?> primitiveToWrapper(final Class<?> wrapper)
    {
        return Objects.requireNonNull(WRAPPERS_TO_PRIMITIVES.inverse().get(wrapper));
    }

    public static Class<?> getById(final int id)
    {
        return PRIMITIVES.get(id);
    }

    public static int getId(final Class<?> type)
    {
        for (int i = 0; i < PRIMITIVES.size(); i++)
        {
            if (PRIMITIVES.get(i) == type)
            {
                return i;
            }
        }

        throw new IllegalArgumentException(type.getName());
    }
}
