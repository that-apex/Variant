package net.mrgregorix.variant.core.proxy.asm;

/**
 * An utility class for loading classes using a provided {@link ClassLoader}.
 */
class AsmClassLoadingHelper extends ClassLoader
{
    /**
     * Create a new {@link AsmClassLoadingHelper}
     *
     * @param parentClassLoader class loader that will be used to load the classes.
     */
    public AsmClassLoadingHelper(final ClassLoader parentClassLoader)
    {
        super(parentClassLoader);
    }

    /**
     * @see ClassLoader#defineClass(String, byte[], int, int)
     */
    public Class<?> defineClass(String name, byte[] b)
    {
        return this.defineClass(name, b, 0, b.length);
    }
}
