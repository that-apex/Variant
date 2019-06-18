package net.mrgregorix.variant.rpc.core.proxy;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.mrgregorix.variant.api.proxy.ProxyMatchResult;
import net.mrgregorix.variant.api.proxy.ProxySpecification;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

/**
 * A {@link ProxySpecification} used for proxying the {@link RpcService} instances.
 */
public class RpcServiceProxySpecification extends AbstractModifiablePrioritizable<ProxySpecification> implements ProxySpecification
{
    private final VariantRpc variantRpc;

    /**
     * Crates a new RpcServiceProxySpecification
     *
     * @param variantRpc {@link VariantRpc} module that owns this specification.
     */
    public RpcServiceProxySpecification(final VariantRpc variantRpc)
    {
        this.variantRpc = variantRpc;
    }

    @Override
    public ProxyMatchResult shouldProxy(final Method method)
    {
        if (Modifier.isAbstract(method.getModifiers()) && RpcService.class.isAssignableFrom(method.getDeclaringClass()))
        {
            return ProxyMatchResult.match(new RpcMethodInvocationHandler(this.variantRpc));
        }

        return ProxyMatchResult.noMatch();
    }
}
