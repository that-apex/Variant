package net.mrgregorix.variant.rpc.core.tests.scenario;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.network.authenticator.RpcAuthenticator;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;
import net.mrgregorix.variant.rpc.core.authenticator.IpAuthentication;
import net.mrgregorix.variant.rpc.core.authenticator.publickey.RsaPublicKeyAuthentication;
import net.mrgregorix.variant.rpc.core.tests.custom.DateSerializer;
import net.mrgregorix.variant.utils.annotation.Nullable;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestServices
{
    @SuppressWarnings("MagicNumber")
    public static void testServices(final VariantRpc variantRpc, boolean persistent, int port)
    {
        try
        {
            final int testValue = new Random().nextInt();

            variantRpc.getSerializerSpec().registerSerializer(new DateSerializer());

            final RpcNetworkServer server = variantRpc.setupServer("test-server", "127.0.0.1", port, Collections.singletonList(new ServiceImplementationDetail<>(SimpleRpcService.class, new SimpleRpcServiceImpl())));
            final RpcNetworkClient client = variantRpc.setupClient("test-client", "127.0.0.1", port, Collections.singletonList(SimpleRpcService.class), persistent);
            server.startBlocking();
            client.startBlocking();

            final SimpleRpcService service = variantRpc.getService(client.getName(), SimpleRpcService.class);
            assertThat("no RpcService found", service, is(notNullValue()));
            assertThat("invalid found by name", service, is(sameInstance(variantRpc.getService(client.getName(), SimpleRpcService.class))));

            final Collection<RpcService> registeredServiceImplementations = variantRpc.getRegisteredServiceImplementations();
            assertThat("invalid implementations count", registeredServiceImplementations, hasSize(1));
            assertThat("invalid implementations", registeredServiceImplementations, contains(instanceOf(SimpleRpcServiceImpl.class)));

            final SimpleRpcServiceImpl implementation = (SimpleRpcServiceImpl) registeredServiceImplementations.iterator().next();
            assertThat("implementation is directly implemented by service", service, is(not(implementation)));

            implementation.setResource("resource-value", Integer.toString(testValue));
            assertThat("invalid resource", service.getResource("resource-value"), equalTo(Integer.toString(testValue)));
            assertThat("invalid resource", service.getResource("resource-other"), equalTo(null));

            final Date date = new Date();
            assertThat("invalid date received", date.getTime(), is(lessThanOrEqualTo(service.getRemoteTime().getTime())));
            assertThat("remote method execution incorrect", service.addNumbers(2, 6), is(8));

            assertThat("invalid overload call", 0, is(service.testOverloads()));
            assertThat("invalid overload call", 17, is(service.testOverloads(17)));

            assertThat("invalid throw call", 17, is(service.testThrow(17)));

            try
            {
                service.testThrow(2);
                assertThat("this code should never be called", false);
            }
            catch (final IllegalArgumentException e)
            {
                assertThat("invalid exception", "That's illegal!", equalTo(e.getMessage()));
                e.printStackTrace();
            }
        }
        finally
        {
            variantRpc.disposeAll();
        }
    }

    @SuppressWarnings("MagicNumber")
    public static void testReconnecting(final VariantRpc rpc, final int port)
    {
        final RpcNetworkServer server = rpc.setupServer("test-server", "127.0.0.1", port, Collections.singletonList(new ServiceImplementationDetail<>(SimpleRpcService.class, new SimpleRpcServiceImpl())));
        final RpcNetworkClient client = rpc.setupClient("test-client", "127.0.0.1", port, Collections.singletonList(SimpleRpcService.class), true);

        try
        {
            testAuthenticator0(server, client, IpAuthentication.withBlacklist(Collections.singleton("127.0.0.1")), null, false, port);
            testAuthenticator0(server, client, IpAuthentication.withBlacklist(Collections.singleton("187.0.0.1")), null, true, port);
            testAuthenticator0(server, client, IpAuthentication.withWhitelist(Collections.singleton("127.0.0.1")), null, true, port);
            testAuthenticator0(server, client, IpAuthentication.withWhitelist(Collections.singleton("187.0.0.1")), null, false, port);

            testAuthenticator0(server, client, new CustomAuthenticator(), new CustomAuthenticator(), true, 2736);

            {
                final KeyPairGenerator generator;
                try
                {
                    generator = KeyPairGenerator.getInstance("RSA");
                    generator.initialize(1024);
                    final KeyPair keyPair = generator.genKeyPair();
                    final RsaPublicKeyAuthentication serverAuthenticator = new RsaPublicKeyAuthentication((RSAPublicKey) keyPair.getPublic());
                    final RsaPublicKeyAuthentication clientAuthenticator = new RsaPublicKeyAuthentication((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());

                    testAuthenticator0(server, client, serverAuthenticator, clientAuthenticator, true, 2736);
                }
                catch (final NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                    // skip the test
                }
            }
        }
        finally
        {
            rpc.disposeAll();
        }
    }

    public static void testAuthenticator0(final RpcNetworkServer server, final RpcNetworkClient client, final RpcAuthenticator serverAuthenticator, @Nullable final RpcAuthenticator clientAuthenticator, final boolean successExpected, final int port)
    {
        if (server.isRunning())
        {
            server.stopBlocking();
        }

        if (client.isRunning())
        {
            client.stopBlocking();
        }

        server.getAuthenticatorRegistry().unregisterAll();
        client.getAuthenticatorRegistry().unregisterAll();

        server.getAuthenticatorRegistry().register(serverAuthenticator);

        if (clientAuthenticator != null)
        {
            client.getAuthenticatorRegistry().register(clientAuthenticator);
        }

        server.startBlocking();
        client.startBlocking();

        try
        {
            if (successExpected)
            {
                Assertions.assertTrue(client.isRunning(), "Authentication failed when it was supposed to succeed");
            }
            else
            {
                Assertions.assertFalse(client.isRunning(), "Authentication succeed when it was supposed failed ");
            }
        }
        finally
        {
            server.stopBlocking();
            client.stopBlocking();
        }
    }

    private TestServices()
    {
    }
}
