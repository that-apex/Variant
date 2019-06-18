package net.mrgregorix.variant.rpc.inject.config;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import net.mrgregorix.variant.rpc.inject.RpcConfigurer;

/**
 * Represents a configuration for {@link RpcConfigurer}
 * <p>
 * This can be serialized as an XML document.
 */
@JacksonXmlRootElement(localName = "VariantRpc")
public class RpcConfiguration
{
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "Group")
    private List<RpcConfigGroup> groups;

    public RpcConfiguration()
    {
    }

    public RpcConfiguration(final List<RpcConfigGroup> groups)
    {
        this.groups = groups;
    }

    public List<RpcConfigGroup> getGroups()
    {
        return this.groups;
    }
}
