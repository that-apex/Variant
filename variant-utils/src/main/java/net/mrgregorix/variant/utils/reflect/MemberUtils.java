package net.mrgregorix.variant.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Utilities for gathering members of reflect classes.
 */
public class MemberUtils
{
    /**
     * Gets all methods of the class and its super classes recursively.
     *
     * @return the collection of all methods
     */
    public static Set<Method> getAllMethods(final Class<?> type)
    {
        final Set<Method> methods = new HashSet<>();
        MemberUtils.findMembersRecursively(methods, type, Class::getDeclaredMethods);
        return methods;
    }

    /**
     * Gets all fields of the class and its super classes recursively.
     *
     * @return the collection of all fields
     */
    public static Set<Field> getAllFields(final Class<?> type)
    {
        final Set<Field> fields = new HashSet<>();
        MemberUtils.findMembersRecursively(fields, type, Class::getDeclaredFields);
        return fields;
    }

    private static <T extends Member> void findMembersRecursively(final Set<T> output, final Class<?> clazz, final Function<Class<?>, T[]> retrieve)
    {
        Collections.addAll(output, retrieve.apply(clazz));

        if (clazz == Object.class)
        {
            return;
        }

        if (clazz.getSuperclass() != null)
        {
            findMembersRecursively(output, clazz.getSuperclass(), retrieve);
        }

        for (final Class<?> anInterface : clazz.getInterfaces())
        {
            findMembersRecursively(output, anInterface, retrieve);
        }
    }
}
