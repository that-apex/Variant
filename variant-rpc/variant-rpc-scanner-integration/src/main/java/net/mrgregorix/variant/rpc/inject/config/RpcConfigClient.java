package net.mrgregorix.variant.rpc.inject.config;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Client")
public class RpcConfigClient extends RpcConfigComponent
{
    public RpcConfigClient()
    {
    }

    public RpcConfigClient(final String ip, final int port, final String comment)
    {
        super(ip, port, comment);
    }
}
