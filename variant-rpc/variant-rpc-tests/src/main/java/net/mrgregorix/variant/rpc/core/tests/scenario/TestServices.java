package net.mrgregorix.variant.rpc.core.tests.scenario;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;
import net.mrgregorix.variant.rpc.core.tests.custom.DateSerializer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestServices
{
    public static int PORT = 2734;

    public static void testServices(final VariantRpc variantRpc, boolean persistent)
    {
        try
        {
            final int testValue = new Random().nextInt();

            variantRpc.getSerializerSpec().registerSerializer(new DateSerializer());

            final RpcNetworkServer server = variantRpc.setupServer("test-server", "127.0.0.1", PORT, Collections.singletonList(new ServiceImplementationDetail<>(SimpleRpcService.class, new SimpleRpcServiceImpl())));
            final RpcNetworkClient client = variantRpc.setupClient("test-client", "127.0.0.1", PORT, Collections.singletonList(SimpleRpcService.class), persistent);
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

    private TestServices()
    {
    }
}
