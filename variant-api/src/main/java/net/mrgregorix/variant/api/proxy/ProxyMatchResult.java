package net.mrgregorix.variant.api.proxy;

import java.util.Objects;

import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Represents a result of a proxy match.
 */
public class ProxyMatchResult
{
    private static final    ProxyMatchResult          NO_MATCH = new ProxyMatchResult(false, null);
    private final           boolean                   match;
    private @Nullable final ProxyInvocationHandler<?> handler;

    /**
     * Creates a new {@link ProxyMatchResult}
     *
     * @param match   was there a match found
     * @param handler handler to be used to proxy the method
     */
    protected ProxyMatchResult(final boolean match, @Nullable final ProxyInvocationHandler<?> handler)
    {
        this.match = match;
        this.handler = handler;
    }

    /**
     * Was there a match?
     *
     * @return was there a match
     */
    public boolean isMatch()
    {
        return this.match;
    }

    /**
     * Gets the handler to be used to proxy this method.
     * <p>
     * May be {@code null} if {@link #isMatch()} is false.
     *
     * @return the handler to be used to proxy this method.
     */
    @Nullable
    public ProxyInvocationHandler<?> getHandler()
    {
        return this.handler;
    }

    /**
     * Returns a simple instance of {@link ProxyMatchResult} with no match found.
     *
     * @return {@link ProxyMatchResult} with no match
     */
    public static ProxyMatchResult noMatch()
    {
        return ProxyMatchResult.NO_MATCH;
    }

    /**
     * Creates a simple instance of {@link ProxyMatchResult} with a match found and a handler.
     *
     * @param handler handler to be used to proxy the method, must not be null.
     *
     * @return a {@link ProxyMatchResult} with a match.
     */
    public static ProxyMatchResult match(final ProxyInvocationHandler<?> handler)
    {
        return new ProxyMatchResult(true, Objects.requireNonNull(handler, "handler"));
    }
}
