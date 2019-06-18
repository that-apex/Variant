package net.mrgregorix.variant.rpc.api.network.provider;

import java.util.Collection;
import java.util.concurrent.Future;

import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;

/**
 * A provider for network servers and client.
 */
public interface RpcNetworkingProvider
{
    /**
     * Setups a new network server. The server must be started manually using {@link RpcNetworkServer#start()} or {@link RpcNetworkServer#startBlocking()}
     *
     * @param name                        a unique name of the server
     * @param address                     address that the server will bind to
     * @param port                        port that the server will bind to
     * @param supportedServices           services that this server support
     * @param handler                     {@link RpcServerHandler} for handling call requests and authorizing connections.
     * @param persistentDataSerializer    {@link DataSerializer} used for persistent connection
     * @param nonPersistentDataSerializer {@link DataSerializer} used for non-persistent connection
     *
     * @return a newly created server
     */
    RpcNetworkServer setupServer(String name, String address, int port, Collection<Class<? extends RpcService>> supportedServices, RpcServerHandler handler, DataSerializer persistentDataSerializer, DataSerializer nonPersistentDataSerializer);

    /**
     * Setups a new network client. This client must be started manually using {@link RpcNetworkClient#start()} or {@link RpcNetworkClient#startBlocking()}
     *
     * @param name       a unique name of the server
     * @param address    address that the client will connect to
     * @param port       port that the client will use
     * @param services   services that the client wishes to use
     * @param serializer serializer that will be used for the client
     *
     * @return a newly created client.
     */
    RpcNetworkClient setupClient(String name, String address, int port, Collection<Class<? extends RpcService>> services, DataSerializer serializer);

    /**
     * Shutdowns & disposes the given server so its no longer usable.
     *
     * @param server server to dispose
     *
     * @return a {@link Future} that is completed when the server fully shutdowns.
     */
    Future<Void> disposeServer(RpcNetworkServer server);

    /**
     * Shutdowns & disposes the given client so its no longer usable.
     *
     * @param client client to dispose
     *
     * @return a {@link Future} that is completed when the client fully shutdowns.
     */
    Future<Void> disposeClient(RpcNetworkClient client);
}
