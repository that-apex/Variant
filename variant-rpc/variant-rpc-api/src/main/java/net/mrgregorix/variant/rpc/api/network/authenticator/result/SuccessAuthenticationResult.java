package net.mrgregorix.variant.rpc.api.network.authenticator.result;

import com.google.common.base.MoreObjects;

/**
 * Represents a {@link AuthenticationResult} that successfully authenticates client and lets him connect.
 */
public class SuccessAuthenticationResult extends AbstractAuthenticationResult
{
    /**
     * A shared instance of {@link SuccessAuthenticationResult}, should be used instead of creating a new instance every time
     */
    public static final SuccessAuthenticationResult INSTANCE = new SuccessAuthenticationResult();

    @Override
    public boolean isSuccessful()
    {
        return true;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
                          .toString();
    }
}
