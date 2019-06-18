package net.mrgregorix.variant.rpc.inject.config;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RpcConfigComponent
{
    @JacksonXmlProperty(localName = "Ip")
    private String ip;

    @JacksonXmlProperty(localName = "Port")
    private int port;

    @JacksonXmlProperty(localName = "Comment")
    private String comment;

    public RpcConfigComponent()
    {
    }

    public RpcConfigComponent(final String ip, final int port, final String comment)
    {
        this.ip = ip;
        this.port = port;
        this.comment = comment;
    }

    public String getIp()
    {
        return this.ip;
    }

    public int getPort()
    {
        return this.port;
    }

    public String getComment()
    {
        return this.comment;
    }
}
