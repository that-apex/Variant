package net.mrgregorix.variant.rpc.api.network.authenticator.result;

import com.google.common.base.MoreObjects;

/**
 * Represents a {@link AuthenticationResult} that has additional data to be send to the client.
 */
public class DataAuthenticationResult extends AbstractAuthenticationResult
{
    private final byte[] data;

    /**
     * Creates a new DataAuthenticationResult
     *
     * @param data additional data
     */
    public DataAuthenticationResult(final byte[] data)
    {
        this.data = data;
    }

    @Override
    public boolean hasData()
    {
        return true;
    }

    @Override
    public byte[] getData()
    {
        return this.data;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .add("data", this.data)
                          .toString();
    }
}
