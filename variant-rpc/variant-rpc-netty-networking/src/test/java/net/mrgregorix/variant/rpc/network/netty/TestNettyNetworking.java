package net.mrgregorix.variant.rpc.network.netty;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.core.builder.VariantBuilder;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.core.tests.scenario.TestServices;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestNettyNetworking
{
    private VariantRpc rpc;

    @BeforeAll
    public static void initAll()
    {
    }

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
        TestServices.testServices(this.rpc, true);
    }

    @Test
    public void testNonPersistent()
    {
        TestServices.testServices(this.rpc, false);
    }
}
