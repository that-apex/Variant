package net.mrgregorix.variant.rpc.api.network.provider.result;

import java.util.Objects;

import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Represents a result of a call that failed due to an exception being thrown.
 */
public class FailedRpcCallResult implements RpcServiceCallResult
{
    private final int       callId;
    private final Exception exception;

    /**
     * Creates a new FailedRpcCallResult
     *
     * @param callId    ID of the call that this result is for
     * @param exception exception that causes the call to fail
     */
    public FailedRpcCallResult(final int callId, final Exception exception)
    {
        this.callId = callId;
        this.exception = Objects.requireNonNull(exception, "exception");
    }

    @Override
    public int getCallId()
    {
        return this.callId;
    }

    @Override
    public boolean wasSuccessful()
    {
        return false;
    }

    @Nullable
    @Override
    public Object getResult()
    {
        throw new IllegalArgumentException("Call was failed");
    }

    @Override
    public Exception getException()
    {
        return this.exception;
    }
}
