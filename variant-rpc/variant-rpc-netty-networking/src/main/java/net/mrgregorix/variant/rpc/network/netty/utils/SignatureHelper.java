package net.mrgregorix.variant.rpc.network.netty.utils;

import java.lang.reflect.Method;

import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Helper for generating unique method signatures.
 */
public class SignatureHelper
{
    /**
     * Generates a new method signature. It will be unique so no 2 signatures will match a single method in the same class.
     *
     * @param method method to generate signature from
     *
     * @return the generated signature
     */
    public static String generateSignature(final Method method)
    {
        final StringBuilder signature = new StringBuilder(method.getName()).append("(");

        final Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length > 0)
        {
            for (final Class<?> parameterType : parameterTypes)
            {
                signature.append(parameterType.toString()).append(", ");
            }

            signature.setLength(signature.length() - 2);
        }

        return signature.append(")").append(method.getReturnType()).toString();
    }

    /**
     * Finds a method that matches the given signature in the given class
     *
     * @param type      class to search for methods
     * @param signature signature to be looked for
     *
     * @return the found method or {@code null} if none found
     *
     * @see #generateSignature(Method)
     */
    @Nullable
    public static Method findBySignature(final Class<?> type, final String signature)
    {
        for (final Method declaredMethod : type.getDeclaredMethods())
        {
            if (SignatureHelper.generateSignature(declaredMethod).equals(signature))
            {
                return declaredMethod;
            }
        }

        return null;
    }


    private SignatureHelper()
    {
    }
}
