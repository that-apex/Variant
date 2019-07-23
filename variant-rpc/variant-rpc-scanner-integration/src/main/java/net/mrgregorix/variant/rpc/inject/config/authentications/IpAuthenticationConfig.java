package net.mrgregorix.variant.rpc.inject.config.authentications;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import net.mrgregorix.variant.rpc.core.authenticator.IpAuthentication;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigAuthentication;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * A {@link RpcConfigAuthentication} for {@link IpAuthentication}
 */
public class IpAuthenticationConfig extends RpcConfigAuthentication<IpAuthentication>
{
    @JacksonXmlProperty(localName = "WhitelistEntry")
    @JacksonXmlElementWrapper(localName = "Whitelist")
    @Nullable
    private List<String> whitelist;

    @JacksonXmlProperty(localName = "BlacklistEntry")
    @JacksonXmlElementWrapper(localName = "Blacklist")
    @Nullable
    private List<String> blacklist;

    public IpAuthenticationConfig()
    {
    }

    public IpAuthenticationConfig(@Nullable final List<String> whitelist, @Nullable final List<String> blacklist)
    {
        this.whitelist = whitelist;
        this.blacklist = blacklist;
    }

    @Nullable
    public List<String> getWhitelist()
    {
        return this.whitelist;
    }

    @Nullable
    public List<String> getBlacklist()
    {
        return this.blacklist;
    }

    @Override
    public IpAuthentication createAuthenticator()
    {
        if (this.whitelist != null && this.blacklist != null)
        {
            throw new IllegalArgumentException("Either Blacklist or Whitelist must be defined, both is not allowed");
        }

        if (this.whitelist != null)
        {
            return IpAuthentication.withWhitelist(this.whitelist);
        }

        if (this.blacklist != null)
        {
            return IpAuthentication.withBlacklist(this.blacklist);
        }

        throw new IllegalArgumentException("Either Blacklist or Whitelist must be defined in a IpAuthentication");
    }
}
