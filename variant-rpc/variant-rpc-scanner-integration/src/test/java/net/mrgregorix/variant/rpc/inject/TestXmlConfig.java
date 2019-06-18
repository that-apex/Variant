package net.mrgregorix.variant.rpc.inject;

import java.io.IOException;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigGroup;
import net.mrgregorix.variant.rpc.inject.config.RpcConfiguration;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestXmlConfig
{
    @Test
    public void testConfig() throws IOException
    {
        final XmlMapper mapper = new XmlMapper();
        final RpcConfiguration config = mapper.readValue(TestXmlConfig.class.getResourceAsStream("/VariantRpc.xml"), RpcConfiguration.class);

        assertThat("invalid amount of groups", config.getGroups(), hasSize(2));
        final RpcConfigGroup serverGroup = config.getGroups().stream().filter(it -> "test-server".equals(it.getId())).findAny().orElseThrow();
        final RpcConfigGroup clientGroup = config.getGroups().stream().filter(it -> "test-client".equals(it.getId())).findAny().orElseThrow();

        assertThat("server group has no server", serverGroup.getServer(), is(notNullValue()));
        assertThat("server group has a client", serverGroup.getClient(), is(nullValue()));
        assertThat("client group has no client", clientGroup.getClient(), is(notNullValue()));
        assertThat("client group has a server", clientGroup.getServer(), is(nullValue()));

        assertThat("Invalid server IP", serverGroup.getServer().getIp(), equalTo("174.125.16.7"));
        assertThat("Invalid server port", serverGroup.getServer().getPort(), equalTo(8445));
        assertThat("Invalid server comment", serverGroup.getServer().getComment(), is(emptyOrNullString()));

        assertThat("Invalid client IP", clientGroup.getClient().getIp(), equalTo("174.125.16.7"));
        assertThat("Invalid client port", clientGroup.getClient().getPort(), equalTo(8445));
        assertThat("Invalid client comment", clientGroup.getClient().getComment(), equalTo("Test comment"));
    }
}
