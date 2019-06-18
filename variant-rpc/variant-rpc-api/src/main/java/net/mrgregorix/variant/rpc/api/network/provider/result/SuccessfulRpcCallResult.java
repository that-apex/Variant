package net.mrgregorix.variant.rpc.api.network.provider.result;

import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Represents a successful RPC call.
 */
public class SuccessfulRpcCallResult implements RpcServiceCallResult
{
    private final int    callId;
    @Nullable
    private final Object result;

    /**
     * Creates a new SuccessfulRpcCallResult
     *
     * @param callId ID of the call that this result is for
     * @param result return value of the call
     */
    public SuccessfulRpcCallResult(final int callId, @Nullable final Object result)
    {
        this.callId = callId;
        this.result = result;
    }

    @Override
    public int getCallId()
    {
        return this.callId;
    }

    @Override
    public boolean wasSuccessful()
    {
        return true;
    }

    @Override
    @Nullable
    public Object getResult()
    {
        return this.result;
    }

    @Override
    public Exception getException()
    {
        throw new IllegalStateException("Call was successful");
    }
}
