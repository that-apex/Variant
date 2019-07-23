package net.mrgregorix.variant.rpc.inject;

import java.io.IOException;
import java.util.Collection;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.core.builder.VariantBuilder;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.inject.client.PublicGreeting;
import net.mrgregorix.variant.rpc.network.netty.NettyRpcNetworkingProvider;
import net.mrgregorix.variant.scanner.api.VariantScanner;
import org.junit.jupiter.api.Test;

public class TestIntegration
{
    private static Variant setupVariant()
    {
        final Variant variant = new VariantBuilder().build();
        variant.registerModule(VariantInjector.class);
        variant.registerModule(VariantScanner.class);
        variant.registerModule(VariantRpc.class);
        variant.registerModule(VariantScannerIntegration.class);

        variant.getModule(VariantRpc.class).setNetworkingProvider(new NettyRpcNetworkingProvider());

        return variant;
    }

    @Test
    public void testIntegration() throws IOException
    {
        {
            // setup server
            final Variant variant = setupVariant();

            variant.getModule(VariantScanner.class).instantiate(TestIntegration.class.getPackageName() + ".server");

            new RpcConfigurer(TestIntegration.class.getResourceAsStream("/ServerConfig.xml")).configure(variant);
        }

        {
            // setup client
            final Variant variant = setupVariant();

            final Collection<Object> objects = variant.getModule(VariantScanner.class).instantiate(TestIntegration.class.getPackageName() + ".client");

            new RpcConfigurer(TestIntegration.class.getResourceAsStream("/ClientConfig.xml")).configure(variant);

            // test
            objects
                .stream()
                .filter(it -> it instanceof PublicGreeting)
                .findAny()
                .map(it -> (PublicGreeting) it)
                .orElseThrow()
                .greet("John");
        }
    }
}
