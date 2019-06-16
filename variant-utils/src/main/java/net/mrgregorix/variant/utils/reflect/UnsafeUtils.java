package net.mrgregorix.variant.utils.reflect;

import java.lang.reflect.Constructor;

import net.mrgregorix.variant.utils.annotation.Nullable;
import sun.misc.Unsafe;

/**
 * Utilities for getting an {@link Unsafe} instance. Do not use this if you don't know what it is.
 */
public class UnsafeUtils
{
    private static final @Nullable Unsafe    unsafe;
    private static final @Nullable Exception unavailabilityCause;

    /**
     * Returns an {@link Unsafe instance}. Works only if {@link #isAvailable()} returns true.
     *
     * @return a global Unsafe instance.
     *
     * @throws NullPointerException when no unsafe instance is available.
     */
    public static Unsafe getUnsafe()
    {
        if (unsafe == null)
        {
            throw new NullPointerException("unsafe");
        }

        return unsafe;
    }

    /**
     * Checks what exception was thrown when trying to create the {@link Unsafe} object. If the {@link Unsafe} instance was created successfully this holds {@code null}.
     *
     * @return exception thrown when creating the unsafe exception, or {@code null} if everything was successful.
     */
    @Nullable
    public static Exception getUnavailabilityCause()
    {
        return unavailabilityCause;
    }

    /**
     * Checks if the unsafe instance is available. If it's {@code false} you can use {@link #getUnavailabilityCause()} to check the cause.
     *
     * @return if the unsafe instance is availabel
     */
    public static boolean isAvailable()
    {
        return unavailabilityCause == null;
    }

    // initializer
    static
    {
        Unsafe instance = null;
        Exception exception = null;
        try
        {
            final Constructor<Unsafe> constructor = Unsafe.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            instance = constructor.newInstance();
        }
        catch (final Exception e)
        {
            exception = e;
        }

        unsafe = instance;
        unavailabilityCause = exception;
    }
}
