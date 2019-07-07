package net.mrgregorix.variant.rpc.network.netty;

import java.util.Collections;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.core.builder.VariantBuilder;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.network.exception.ConnectionFailureException;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;
import net.mrgregorix.variant.rpc.core.tests.custom.DateSerializer;
import net.mrgregorix.variant.rpc.core.tests.scenario.SimpleRpcService;
import net.mrgregorix.variant.rpc.core.tests.scenario.SimpleRpcServiceImpl;
import net.mrgregorix.variant.rpc.core.tests.scenario.TestServices;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

public class TestNettyNetworking
{
    private VariantRpc rpc;

    @BeforeEach
    public void init()
    {
        final Variant variant = new VariantBuilder().build();
        this.rpc = variant.registerModule(VariantRpc.class);
        this.rpc.setNetworkingProvider(new NettyRpcNetworkingProvider());
    }

    @Test
    public void testPersistent()
    {
        TestServices.testServices(this.rpc, true, 2734);
    }

    @Test
    public void testNonPersistent()
    {
        TestServices.testServices(this.rpc, false, 2735);
    }

    @Test
    public void testAuthentication()
    {
        TestServices.testReconnecting(this.rpc, 2736);
    }

    @Test
    @EnabledIfSystemProperty(named = "variant.testReconnecting", matches = "true") // takes too long to be executed every time
    public void testReconnecting()
    {
        this.rpc.getSerializerSpec().registerSerializer(new DateSerializer());

        RpcNetworkServer server = this.rpc.setupServer("test-server", "127.0.0.1", 2736, Collections.singletonList(new ServiceImplementationDetail<>(SimpleRpcService.class, new SimpleRpcServiceImpl())));
        final RpcNetworkClient client = this.rpc.setupClient("test-client", "127.0.0.1", 2736, Collections.singletonList(SimpleRpcService.class), true);
        client.start();

        final SimpleRpcService service = this.rpc.getService(client.getName(), SimpleRpcService.class);
        boolean expectSuccess = false;

        for (int i = 0; i < 10; i++)
        {
            System.out.println("Try nr: " + i);

            if (i == 2)
            {
                System.out.println("Start Server");
                server.startBlocking();
                expectSuccess = true;
            }
            if (i == 6)
            {
                System.out.println("Stop Server");
                this.rpc.disposeServer(server);
                expectSuccess = false;
            }
            if (i == 8)
            {
                System.out.println("Start Server");
                server = this.rpc.setupServer("test-server", "127.0.0.1", 2736, Collections.singletonList(new ServiceImplementationDetail<>(SimpleRpcService.class, new SimpleRpcServiceImpl())));
                server.startBlocking();
                expectSuccess = true;
            }

            boolean success;
            try
            {
                System.out.println("Result: " + service.getRemoteTime());
                success = true;
            }
            catch (final ConnectionFailureException e)
            {
                System.out.println("Failed");
                success = false;
            }

            Assertions.assertEquals(expectSuccess, success, "The call was " + (success ? "successful" : "unsuccesful") + " when the server was supposed to be " + (expectSuccess ? "started" : "stopped"));

            try
            {
                Thread.sleep(1000);
            }
            catch (final InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        client.stopBlocking();
        server.stopBlocking();
    }
}
