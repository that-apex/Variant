package net.mrgregorix.variant.rpc.inject.config;

import java.util.Collections;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import net.mrgregorix.variant.utils.annotation.Nullable;

public class RpcConfigComponent
{
    @JacksonXmlProperty(localName = "Ip")
    private String ip;

    @JacksonXmlProperty(localName = "Port")
    private int port;

    @JacksonXmlProperty(localName = "Comment")
    @Nullable
    private String comment;

    @JacksonXmlProperty(localName = "Authentication")
    @Nullable
    private RpcConfigAuthentications authentication;

    public RpcConfigComponent()
    {
    }

    public RpcConfigComponent(final String ip, final int port, @Nullable final String comment, @Nullable final RpcConfigAuthentications authentication)
    {
        this.ip = ip;
        this.port = port;
        this.comment = comment;
        this.authentication = authentication;
    }

    public String getIp()
    {
        return this.ip;
    }

    public int getPort()
    {
        return this.port;
    }

    @Nullable
    public String getComment()
    {
        return this.comment;
    }

    public RpcConfigAuthentications getAuthentication()
    {
        if (this.authentication == null)
        {
            this.authentication = new RpcConfigAuthentications(Collections.emptyList());
        }

        return this.authentication;
    }
}
