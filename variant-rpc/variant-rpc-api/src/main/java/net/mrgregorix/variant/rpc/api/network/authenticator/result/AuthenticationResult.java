package net.mrgregorix.variant.rpc.api.network.authenticator.result;

import net.mrgregorix.variant.rpc.api.network.authenticator.RpcAuthenticator;

/**
 * Represents a result of {@link RpcAuthenticator} verification.
 */
public interface AuthenticationResult
{
    /**
     * Is the request successful and the authenticator lets the client fully connect?
     *
     * @return whether the request is successful
     */
    boolean isSuccessful();

    /**
     * Is the request successful and and the requesting client should immediately disconnected?
     *
     * @return whether the request is failed
     */
    boolean isFailure();

    /**
     * Does this request has any additional data?
     *
     * @return whether or not this request has any data
     *
     * @see #hasData()
     */
    boolean hasData();

    /**
     * Returns data of this result if any.
     *
     * @return data of this result
     *
     * @throws IllegalStateException if {@link #hasData()} is {@code false}
     */
    byte[] getData();

    /**
     * Returns the failure reason if result is failed.
     *
     * @return the failure reason
     *
     * @throws IllegalStateException if {@link #isFailure()} is {@code false}
     */
    String getFailReason();
}
