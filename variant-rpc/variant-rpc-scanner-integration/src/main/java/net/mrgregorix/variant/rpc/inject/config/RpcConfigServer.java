package net.mrgregorix.variant.rpc.inject.config;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "Server")
public class RpcConfigServer extends RpcConfigComponent
{
    public RpcConfigServer()
    {
    }

    public RpcConfigServer(final String ip, final int port, final String comment)
    {
        super(ip, port, comment);
    }
}
