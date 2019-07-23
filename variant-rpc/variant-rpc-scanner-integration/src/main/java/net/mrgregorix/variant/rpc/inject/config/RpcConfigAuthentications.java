package net.mrgregorix.variant.rpc.inject.config;

import java.util.List;

public class RpcConfigAuthentications
{
    private final List<RpcConfigAuthentication<?>> authentications;

    public RpcConfigAuthentications(final List<RpcConfigAuthentication<?>> authentications)
    {
        this.authentications = authentications;
    }

    public List<RpcConfigAuthentication<?>> getAuthentications()
    {
        return this.authentications;
    }
}
