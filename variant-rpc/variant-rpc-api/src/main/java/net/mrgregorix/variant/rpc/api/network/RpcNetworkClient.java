package net.mrgregorix.variant.rpc.api.network;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.mrgregorix.variant.rpc.api.network.provider.RpcNetworkingProvider;
import net.mrgregorix.variant.rpc.api.network.provider.result.RpcServiceCallResult;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;

/**
 * Represents an RPC client that can connect to an RPC server to request calls.
 *
 * @see RpcNetworkingProvider#setupClient(String, String, int, Collection, DataSerializer)
 */
public interface RpcNetworkClient extends RpcNetworkComponent
{
    /**
     * Request a RPC call from the server.
     *
     * @param service   service to call
     * @param method    method to call
     * @param arguments array of arguments to pass to the method
     *
     * @return {@link CompletableFuture} that yields the result of the call.
     */
    CompletableFuture<RpcServiceCallResult> call(Class<? extends RpcService> service, Method method, Object[] arguments);

    /**
     * Returns a list of services that this client uses.
     *
     * @return a list of services that this client uses.
     */
    List<Class<? extends RpcService>> getServices();
}
