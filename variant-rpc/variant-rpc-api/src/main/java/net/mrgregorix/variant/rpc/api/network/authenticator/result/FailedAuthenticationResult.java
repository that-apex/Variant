package net.mrgregorix.variant.rpc.api.network.authenticator.result;

/**
 * Represents a {@link AuthenticationResult} that fails the client's authentication and disconnects them.
 */
public class FailedAuthenticationResult extends AbstractAuthenticationResult
{
    private final String failReason;

    /**
     * Creates a FailedAuthenticationResult
     *
     * @param failReason reason for the failure
     */
    public FailedAuthenticationResult(final String failReason)
    {
        this.failReason = failReason;
    }

    @Override
    public boolean isFailure()
    {
        return true;
    }

    @Override
    public String getFailReason()
    {
        return this.failReason;
    }
}
