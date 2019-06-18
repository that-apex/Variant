package net.mrgregorix.variant.rpc.inject;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.inject.api.VariantInjector;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.inject.annotation.InjectRpc;
import net.mrgregorix.variant.rpc.inject.injector.RpcServiceInjector;

/**
 * A simple module for the {@link VariantRpc} scanner &amp; inject integration. Adds {@link RpcService}s injecting via the {@link InjectRpc} annotation.
 */
public class VariantScannerIntegration implements VariantModule
{
    public static final String MODULE_NAME = "Variant::RPC::ScannerIntegration";

    @Override
    public String getName()
    {
        return VariantScannerIntegration.MODULE_NAME;
    }

    @Override
    public void initialize(final Variant variant)
    {
        variant.getModule(VariantInjector.class).registerCustomInjector(new RpcServiceInjector(variant.getModule(VariantRpc.class)));
    }
}
