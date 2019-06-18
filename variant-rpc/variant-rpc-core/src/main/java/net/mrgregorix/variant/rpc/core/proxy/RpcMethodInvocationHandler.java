package net.mrgregorix.variant.rpc.core.proxy;

import java.lang.reflect.Method;

import net.mrgregorix.variant.api.proxy.BeforeInvocationResult;
import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.api.proxy.ProxyInvocationHandler;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.provider.result.RpcServiceCallResult;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.utils.exception.ExceptionUtils;
import net.mrgregorix.variant.utils.priority.AbstractModifiablePrioritizable;

/**
 * The {@link ProxyInvocationHandler} for the {@link RpcService} methods.
 */
public class RpcMethodInvocationHandler extends AbstractModifiablePrioritizable<RpcMethodInvocationHandler> implements ProxyInvocationHandler<RpcMethodInvocationHandler>
{
    private final VariantRpc variantRpc;

    /**
     * Crates a new RpcMethodInvocationHandler
     *
     * @param variantRpc the owning module
     */
    public RpcMethodInvocationHandler(final VariantRpc variantRpc)
    {
        this.variantRpc = variantRpc;
    }

    @Override
    public BeforeInvocationResult beforeInvocation(final Proxy proxy, final Method method, final Object[] arguments) throws Exception
    {
        return BeforeInvocationResult.proceed();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object afterInvocation(final Proxy proxy, final Method method, final Object[] arguments, final Object returnValue) throws Exception
    {
        RpcNetworkClient client;

        if (! RpcProxyDataSpecs.CLIENT_SPEC.isPresent(proxy))
        {
            client = this.variantRpc.getNetworkClients()
                                    .stream()
                                    .filter(it -> it.getName().equals(RpcProxyDataSpecs.CLIENT_NAME_SPEC.from(proxy)))
                                    .findAny()
                                    .orElseThrow(() -> new IllegalStateException("No clients matching the given name found"));

            RpcProxyDataSpecs.CLIENT_SPEC.set(proxy, client);
        }
        else
        {
            client = RpcProxyDataSpecs.CLIENT_SPEC.from(proxy);
        }

        final RpcServiceCallResult result = client.call((Class<? extends RpcService>) proxy.getProxyBaseClass(), method, arguments).get();

        if (! result.wasSuccessful())
        {
            final Exception exception = result.getException();

            StackTraceElement[] localStackTrace = Thread.currentThread().getStackTrace();

            if (! ExceptionUtils.preferRealExceptions())
            {
                localStackTrace = ExceptionUtils.getCallsUntilMarker(localStackTrace);
            }
            localStackTrace[0] = new StackTraceElement(" ---- REMOTE", "SERVER: " + client.getName() + " ---- ", client.getAddress(), client.getPort());

            StackTraceElement[] remoteStackTrace = exception.getStackTrace();

            exception.setStackTrace(ExceptionUtils.appendStacktrace(remoteStackTrace, localStackTrace, 0));

            throw exception;
        }

        return result.getResult();
    }
}
