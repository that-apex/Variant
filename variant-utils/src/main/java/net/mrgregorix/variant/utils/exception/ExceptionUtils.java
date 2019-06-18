package net.mrgregorix.variant.utils.exception;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Utilities for manipulating exceptions.
 */
public class ExceptionUtils
{
    /**
     * Checks whether it is preferred to not alter any exceptions.
     * <p>
     * This can be changed using the 'variant.preferRealExceptions' property.
     *
     * @return whether is is preferred to not alter any exceptions.
     */
    public static boolean preferRealExceptions()
    {
        return System.getProperty("variant.preferRealExceptions", "false").equalsIgnoreCase("true");
    }

    /**
     * An utility functions. This only calls the given runnable. Used for {@link #getCallsAfterMarker(StackTraceElement[])} and {@link #getCallsUntilMarker(StackTraceElement[])}
     *
     * @param runnable runnable to run
     * @param <T>      return type
     *
     * @return value returned by this runnable
     *
     * @throws Exception rethrown from {@link Callable#call()}
     */
    public static <T> T marker(final Callable<T> runnable) throws Exception
    {
        return runnable.call();
    }

    /**
     * Gets all the stacktrace elements before (below) the first (top-most) call to {@link #marker(Callable)} is found in the stacktrace.
     *
     * @param stackTrace stack trace to filter
     *
     * @return stacktrace elements until marker.
     */
    public static StackTraceElement[] getCallsUntilMarker(final StackTraceElement[] stackTrace)
    {
        for (int i = 0; i < stackTrace.length; i++)
        {
            if (ExceptionUtils.class.getName().equals(stackTrace[i].getClassName()) && "marker".equals(stackTrace[i].getMethodName()))
            {
                final int length = stackTrace.length - i - 1;
                final StackTraceElement[] newStackTrace = new StackTraceElement[length];
                System.arraycopy(stackTrace, i + 1, newStackTrace, 0, length);
                return newStackTrace;
            }
        }

        return stackTrace;
    }


    /**
     * Gets all the stacktrace elements after (above) the last (bottom-most) call to {@link #marker(Callable)} is found in the stacktrace.
     *
     * @param stackTrace stack trace to filter
     *
     * @return stacktrace elements after marker.
     */
    public static StackTraceElement[] getCallsAfterMarker(final StackTraceElement[] stackTrace)
    {
        for (int i = stackTrace.length - 1; i >= 0; i--)
        {
            if (ExceptionUtils.class.getName().equals(stackTrace[i].getClassName()) && "marker".equals(stackTrace[i].getMethodName()))
            {
                final int length = i - 1;
                final StackTraceElement[] newStackTrace = new StackTraceElement[length];
                System.arraycopy(stackTrace, 0, newStackTrace, 0, length);
                return newStackTrace;
            }
        }

        return stackTrace;
    }

    /**
     * Appends one stacktrace to another.
     *
     * @param stackTrace stack trace to be on top
     * @param toAppend   stack trace to bo on bottom
     * @param startIndex index at which to start copying from {@code toAppend}
     *
     * @return concatenated stacktraces
     */
    public static StackTraceElement[] appendStacktrace(final StackTraceElement[] stackTrace, final StackTraceElement[] toAppend, final int startIndex)
    {
        final StackTraceElement[] newStackTrace = new StackTraceElement[stackTrace.length + toAppend.length - startIndex];
        System.arraycopy(stackTrace, 0, newStackTrace, 0, stackTrace.length);
        System.arraycopy(toAppend, startIndex, newStackTrace, stackTrace.length, toAppend.length - startIndex);
        return newStackTrace;
    }

    /**
     * Removes all internal reflect calls from the stack trace. Useful when for example {@link Method#invoke(Object, Object...)} is used and the internal calls have to be removed from the stack trace.
     *
     * @param currentStackTrace stack trace to clear
     *
     * @return cleared stack trace
     */
    public static StackTraceElement[] removeReflectCalls(final StackTraceElement[] currentStackTrace)
    {
        final List<StackTraceElement> output = new ArrayList<>();
        for (final StackTraceElement stackTraceElement : currentStackTrace)
        {
            final String className = stackTraceElement.getClassName();

            if (className.startsWith("jdk.internal.reflect") || className.startsWith("java.lang.reflect"))
            {
                continue;
            }

            output.add(stackTraceElement);
        }

        return output.toArray(new StackTraceElement[0]);
    }

    private ExceptionUtils()
    {
    }
}
