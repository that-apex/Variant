package net.mrgregorix.variant.rpc.inject.config;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Group")
public class RpcConfigGroup
{
    @JacksonXmlProperty(localName = "Id")
    private String id;

    @JacksonXmlProperty(localName = "Server")
    private RpcConfigServer server;

    @JacksonXmlProperty(localName = "Client")
    private RpcConfigClient client;

    public RpcConfigGroup()
    {
    }

    public RpcConfigGroup(final String id, final RpcConfigServer server, final RpcConfigClient client)
    {
        this.id = id;
        this.server = server;
        this.client = client;
    }

    public String getId()
    {
        return this.id;
    }

    public RpcConfigServer getServer()
    {
        return this.server;
    }

    public RpcConfigClient getClient()
    {
        return this.client;
    }
}
