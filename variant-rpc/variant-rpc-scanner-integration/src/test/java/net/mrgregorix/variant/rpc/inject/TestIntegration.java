package net.mrgregorix.variant.rpc.inject;

import java.util.Collection;
import java.util.Collections;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.core.builder.VariantBuilder;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigClient;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigGroup;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigServer;
import net.mrgregorix.variant.rpc.inject.config.RpcConfiguration;
import net.mrgregorix.variant.rpc.network.netty.NettyRpcNetworkingProvider;
import net.mrgregorix.variant.scanner.api.VariantScanner;
import org.junit.jupiter.api.Test;

public class TestIntegration
{
    @Test
    public void testIntegration()
    {
        final Variant variant = new VariantBuilder().build();
        final VariantInjector injector = variant.registerModule(VariantInjector.class);
        final VariantScanner scanner = variant.registerModule(VariantScanner.class);
        final VariantRpc rpc = variant.registerModule(VariantRpc.class);
        variant.registerModule(VariantScannerIntegration.class);

        rpc.setNetworkingProvider(new NettyRpcNetworkingProvider());

        final RpcConfiguration configuration = new RpcConfiguration(
            Collections.singletonList(
                new RpcConfigGroup("greeting", new RpcConfigServer("127.0.0.1", 3051, ""), new RpcConfigClient("127.0.0.1", 3051, ""))
            )
        );

        final Collection<Object> objects = scanner.instantiate(TestIntegration.class.getPackageName());
        new RpcConfigurer(configuration).configure(variant);

        objects
            .stream()
            .filter(it -> it instanceof PublicGreeting)
            .findAny()
            .map(it -> (PublicGreeting) it)
            .orElseThrow()
            .greet("John");
    }
}
