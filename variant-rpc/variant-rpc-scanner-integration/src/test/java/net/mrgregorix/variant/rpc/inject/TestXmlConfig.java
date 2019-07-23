package net.mrgregorix.variant.rpc.inject;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import net.mrgregorix.variant.rpc.inject.config.RpcConfigAuthentication;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigGroup;
import net.mrgregorix.variant.rpc.inject.config.RpcConfiguration;
import net.mrgregorix.variant.rpc.inject.config.authentications.IpAuthenticationConfig;
import net.mrgregorix.variant.rpc.inject.config.authentications.PublicKeyAuthenticationConfig;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestXmlConfig
{
    @SuppressWarnings({"OptionalGetWithoutIsPresent", "MagicNumber"})
    @Test
    public void testConfig() throws IOException
    {
        final RpcConfiguration config = new RpcConfigurer(TestXmlConfig.class.getResourceAsStream("/VariantRpc.xml")).getConfiguration();

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

        final List<RpcConfigAuthentication<?>> serverAuthenticators = serverGroup.getServer().getAuthentication().getAuthentications();
        assertThat("Invalid amount of server authenticators", serverAuthenticators, hasSize(2));

        final Optional<IpAuthenticationConfig> serverIpAuth = serverAuthenticators.stream().filter(it -> it instanceof IpAuthenticationConfig).map(it -> (IpAuthenticationConfig) it).findAny();
        final Optional<PublicKeyAuthenticationConfig> serverPubKeyAuth = serverAuthenticators.stream().filter(it -> it instanceof PublicKeyAuthenticationConfig).map(it -> (PublicKeyAuthenticationConfig) it).findAny();

        assertThat("Server authenticator has no IpAuthentication", serverIpAuth.isPresent());
        assertThat("Server authenticator has no PublicKeyAuthentication", serverPubKeyAuth.isPresent());

        assertThat("Server IpAuthentication blacklist is invalid", serverIpAuth.get().getBlacklist(), is(nullValue()));
        assertThat("Server IpAuthentication whitelist is invalid", serverIpAuth.get().getWhitelist(), containsInAnyOrder("174.125.16.1", "174.125.16.2", "174.125.16.3"));

        assertThat("Server PublicKeyAuthentication publicKey is invalid", serverPubKeyAuth.get().getPublicKey(), equalTo("src/test/resources/test_key.pub"));
        assertThat("Server PublicKeyAuthentication privateKey is invalid", serverPubKeyAuth.get().getPrivateKey(), is(nullValue()));

        final List<RpcConfigAuthentication<?>> clientAuthenticators = clientGroup.getClient().getAuthentication().getAuthentications();
        assertThat("Invalid amount of client authenticators", clientAuthenticators, hasSize(1));

        final Optional<PublicKeyAuthenticationConfig> clientPubKeyAuth = clientAuthenticators.stream().filter(it -> it instanceof PublicKeyAuthenticationConfig).map(it -> (PublicKeyAuthenticationConfig) it).findAny();
        assertThat("Client authenticator has no PublicKeyAuthentication", clientPubKeyAuth.isPresent());
        assertThat("Client PublicKeyAuthentication publicKey is invalid", clientPubKeyAuth.get().getPublicKey(), equalTo("src/test/resources/test_key.pub"));
        assertThat("Client PublicKeyAuthentication privateKey is invalid", clientPubKeyAuth.get().getPrivateKey(), equalTo("src/test/resources/test_key"));
    }
}
