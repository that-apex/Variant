package net.mrgregorix.variant.rpc.core.scenario;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import net.mrgregorix.variant.core.builder.VariantBuilder;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestServices
{
    private static final int PORT = 2734;

    @Test
    public void testServices()
    {
        /*
        final int testValue = new Random().nextInt();
        final VariantRpc variantRpc = new VariantBuilder().build().registerModule(VariantRpc.class);

        variantRpc.getDefaultSerializer().registerSerializer(new DateSerializer());

        final RpcNetworkServer server = variantRpc.setupServer("test-server", "127.0.0.1", PORT, Collections.singletonList(new ServiceImplementationDetail<>(SimpleRpcService.class, SimpleRpcServiceImpl.class)));
        final RpcNetworkClient client = variantRpc.setupClient("test-client", "127.0.0.1", PORT, Collections.singletonList(SimpleRpcService.class));
        server.startBlocking();
        client.startBlocking();

        final SimpleRpcService service = variantRpc.getService(SimpleRpcService.class);
        assertThat("no RpcService found", service, is(notNullValue()));
        assertThat("invalid found by name", service, is(sameInstance(variantRpc.getService("simple-rpc-service"))));

        final Collection<RpcService> registeredServiceImplementations = variantRpc.getRegisteredServiceImplementations();
        assertThat("invalid implementations count", registeredServiceImplementations, hasSize(1));
        assertThat("invalid implementations", registeredServiceImplementations, contains(instanceOf(SimpleRpcServiceImpl.class)));

        final SimpleRpcServiceImpl implementation = (SimpleRpcServiceImpl) registeredServiceImplementations.iterator().next();
        assertThat("implementation is directly implemented by service", service, is(not(implementation)));

        implementation.setResource("resource-value", Integer.toString(testValue));
        assertThat("invalid resource", service.getResource("resource-value"), equalTo(Integer.toString(testValue)));
        assertThat("invalid resource", service.getResource("resource-other"), equalTo(nullValue()));

        final Date date = new Date();
        assertThat("invalid date received", date.getTime(), is(lessThanOrEqualTo(service.getRemoteTime().getTime())));
        assertThat("remote method execution incorrect", service.addNumbers(2, 6), is(8));
         */
    }
}
