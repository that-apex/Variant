package net.mrgregorix.variant.rpc.api.network.provider;

import java.lang.reflect.Method;
import java.util.Map;

import net.mrgregorix.variant.rpc.api.network.provider.result.RpcServiceCallResult;
import net.mrgregorix.variant.rpc.api.service.RpcService;

/**
 * Handler for new RPC connection and service call requests.
 */
public interface RpcServerHandler
{
    /**
     * Called when a new client connects to the server
     *
     * @param data           the new connection data
     * @param connectionData additional data sent in the init packet
     */
    void newConnection(RpcConnectionData data, Map<String, byte[]> connectionData);

    /**
     * Called when a RPC call is requested.
     *
     * @param callId      a unique call ID. The returned result MUSt have a call ID equal to this.
     * @param connection  connection that requested the call
     * @param serviceType service that the call should be performed on
     * @param method      method that is requested
     * @param arguments   arguments to be passed to the given method.
     *
     * @return result of the call
     */
    RpcServiceCallResult requestedServiceCall(int callId, RpcConnectionData connection, Class<? extends RpcService> serviceType, Method method, Object[] arguments);
}
