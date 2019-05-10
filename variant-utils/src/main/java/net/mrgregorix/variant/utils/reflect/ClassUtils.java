package net.mrgregorix.variant.utils.reflect;

/**
 * Utilities for related to the reflect {@link Class}
 */
public class ClassUtils
{

    /**
     * Checks if a class can be found using the given ClassLoader.
     *
     * @param className   name of the class to be found
     * @param classLoader class loader to use
     *
     * @return if the class can be found
     */
    public static boolean classExists(final String className, final ClassLoader classLoader)
    {
        try
        {
            Class.forName(className, false, classLoader);
            return true;
        }
        catch (final ClassNotFoundException e)
        {
            return false;
        }
    }
}
