package net.mrgregorix.variant.rpc.api.network.provider.result;

import java.lang.reflect.Method;

import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Represents a result of a RPC service call
 *
 * @see RpcNetworkClient#call(Class, Method, Object[])
 */
public interface RpcServiceCallResult
{
    /**
     * The ID of the call that this result is for.
     *
     * @return the ID of the call that this result is for.
     */
    int getCallId();

    /**
     * Returns whether or not the call was successful or not.
     *
     * @return whether or not the call was successful or not
     */
    boolean wasSuccessful();

    /**
     * Gets the result of the call if the call was successful.
     *
     * @return result of the call
     *
     * @throws IllegalStateException if the call was not successful
     */
    @Nullable
    Object getResult();

    /**
     * Gets the Exception that caused this call to fail.
     *
     * @return exception that caused this call to fail
     *
     * @throws IllegalStateException if the call was successful
     */
    Exception getException();
}
