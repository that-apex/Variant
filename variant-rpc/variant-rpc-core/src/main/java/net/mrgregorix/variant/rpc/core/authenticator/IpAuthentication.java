package net.mrgregorix.variant.rpc.core.authenticator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Collection;

import net.mrgregorix.variant.rpc.api.network.authenticator.RpcAuthenticator;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.AuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.FailedAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.SuccessAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.provider.RpcConnectionData;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * An {@link RpcAuthenticator} that authenticates clients based on their IPs being on a whitelist or blacklist
 */
public class IpAuthentication implements RpcAuthenticator
{
    @Nullable
    private final Collection<String> whitelist;

    @Nullable
    private final Collection<String> blacklist;

    private IpAuthentication(final Collection<String> whitelist, final Collection<String> blacklist)
    {
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    @Override
    public AuthenticationResult clientConnected(final RpcConnectionData connection)
    {
        final String ip = connection.getAddress().getAddress().getHostAddress();

        if (this.whitelist != null)
        {
            if (this.whitelist.contains(ip))
            {
                return SuccessAuthenticationResult.INSTANCE;
            }
            else
            {
                return new FailedAuthenticationResult("IP " + ip + " is not whitelisted");
            }
        }

        if (this.blacklist != null)
        {
            if (this.blacklist.contains(ip))
            {
                return new FailedAuthenticationResult("IP " + ip + " is blacklisted");
            }
            else
            {
                return SuccessAuthenticationResult.INSTANCE;
            }
        }

        throw new IllegalStateException("Both whitelist and blacklist is null");
    }

    @Override
    public AuthenticationResult dataReceivedFromClient(final RpcConnectionData connection, final DataInputStream data)
    {
        throw new UnsupportedOperationException("This authenticator should not be attached to a client");
    }

    @Override
    public boolean matchDataReceivedFromServer(final DataInputStream input)
    {
        throw new UnsupportedOperationException("This authenticator should not be attached to a client");
    }

    @Override
    public void dataReceivedFromServer(final DataInputStream input, final DataOutputStream output)
    {
    }

    /**
     * Creates a new {@link IpAuthentication} that will let only clients with IPs specified on the whitelist to connect
     *
     * @param whitelist whitelist of IPs
     *
     * @return a whitelist authentication
     */
    public static IpAuthentication withWhitelist(final Collection<String> whitelist)
    {
        return new IpAuthentication(whitelist, null);
    }

    /**
     * Creates a new {@link IpAuthentication} that will let all clients to connect except clients whose IPs are on the blacklist.
     *
     * @param blacklist blacklist of IPs
     *
     * @return a blacklist authentication
     */
    public static IpAuthentication withBlacklist(final Collection<String> blacklist)
    {
        return new IpAuthentication(null, blacklist);
    }
}
