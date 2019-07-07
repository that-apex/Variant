package net.mrgregorix.variant.rpc.api.network.authenticator.result;

/**
 * Helper implementation of {@link AuthenticationResult}
 */
abstract class AbstractAuthenticationResult implements AuthenticationResult
{
    protected AbstractAuthenticationResult()
    {
    }

    @Override
    public boolean isSuccessful()
    {
        return false;
    }

    @Override
    public boolean isFailure()
    {
        return false;
    }

    @Override
    public boolean hasData()
    {
        return false;
    }

    @Override
    public byte[] getData()
    {
        throw new IllegalStateException("No data");
    }

    @Override
    public String getFailReason()
    {
        throw new IllegalStateException("Not failed");
    }
}
