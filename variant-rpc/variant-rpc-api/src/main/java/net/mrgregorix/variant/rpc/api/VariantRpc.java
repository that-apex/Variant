package net.mrgregorix.variant.rpc.api;

import java.util.Collection;
import javax.annotation.concurrent.ThreadSafe;

import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.network.provider.RpcNetworkingProvider;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.SerializerSpec;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

/**
 * The Variant RPC module. Simple Remote Procedure Calls using plain Java interfaces.
 */
@ThreadSafe
@ModuleImplementation("net.mrgregorix.variant.rpc.core.VariantRpcImpl")
public interface VariantRpc extends VariantModule
{
    /**
     * Returns all {@link RpcService} registered to this RPC.
     *
     * @return all {@link RpcService} registered to this RPC.
     */
    @CollectionMayBeImmutable
    Collection<RpcService> getRegisteredServices();


    /**
     * Returns all {@link RpcService} implementations registered to this RPC.
     *
     * @return all {@link RpcService} implementations registered to this RPC.
     */
    @CollectionMayBeImmutable
    Collection<RpcService> getRegisteredServiceImplementations();

    /**
     * Returns a {@link RpcService} that is of the given type.
     *
     * @param type type to be searched for
     * @param <T>  type of the service
     *
     * @return the service that was found
     *
     * @throws IllegalArgumentException when no service matching the given type was found.
     */
    <T extends RpcService> T getService(Class<T> type);

    /**
     * Returns the {@link DataSerializer} used for persistent connections
     *
     * @return the {@link DataSerializer} used for persistent connections
     */
    DataSerializer getPersistentDataSerializer();

    /**
     * Returns the {@link DataSerializer} used for non-persistent connections
     *
     * @return the {@link DataSerializer} used for non-persistent connections
     */
    DataSerializer getNonPersistentDataSerializer();

    /**
     * Sets the {@link DataSerializer}s used by this RPC. Both {@link DataSerializer} must use the same {@link SerializerSpec} (both may be {@code null})
     *
     * @param persistentSerializer    a {@link DataSerializer} used for persistent connections
     * @param nonPersistentSerializer a {@link DataSerializer} used for non-persistent connections
     */
    void setDataSerializers(DataSerializer persistentSerializer, DataSerializer nonPersistentSerializer);

    /**
     * Returns whether a {@link SerializerSpec} is available and both {@link DataSerializer}s are supporting it.
     *
     * @return whether a {@link SerializerSpec} is available and both {@link DataSerializer}s are supporting it.
     *
     * @see #getSerializerSpec()
     */
    boolean isDataSerializerSpecAvailable();

    /**
     * Returns the {@link SerializerSpec} used by both of the {@link DataSerializer}s
     *
     * @return the {@link SerializerSpec} used by both of the {@link DataSerializer}s
     *
     * @throws NullPointerException when no spec is used
     */
    SerializerSpec getSerializerSpec();

    /**
     * Returns a service of the given type that is implemented by a client with the given name.
     * <p>
     * If no such service exists, a new instance will be created.
     *
     * @param clientName name of the client to use by the service
     * @param service    type of the service
     * @param <T>        type of the service
     *
     * @return the service instance
     */
    <T extends RpcService> T getService(String clientName, Class<T> service);

    /**
     * Returns a {@link RpcNetworkingProvider} that provides networking functionalities for this RPC.
     *
     * @return a {@link RpcNetworkingProvider} used by this RPC
     */
    RpcNetworkingProvider getNetworkingProvider();

    /**
     * Sets the {@link RpcNetworkingProvider} that will be used for providing networking functionalities for this RPC
     *
     * @param networkingProvider {@link RpcNetworkingProvider} to use
     */
    void setNetworkingProvider(RpcNetworkingProvider networkingProvider);

    /**
     * Returns all the servers currently managed by this RPC
     *
     * @return all the servers currently managed by this RPC
     */
    @CollectionMayBeImmutable
    Collection<RpcNetworkServer> getNetworkServers();

    /**
     * Setups a new network server managed by this RPC. The server must be started manually using {@link RpcNetworkServer#start()} or {@link RpcNetworkServer#startBlocking()}
     *
     * @param name                         a unique name of the server
     * @param address                      address that the server will bind to
     * @param port                         port that the server will bind to
     * @param serviceImplementationDetails services that this server will expose
     *
     * @return a newly created, managed server
     */
    RpcNetworkServer setupServer(String name, String address, int port, Collection<ServiceImplementationDetail<?, ?>> serviceImplementationDetails);

    /**
     * Disposes the given server. The server will be shutdown before if its running. After disposal the server is no longer managed by this RPC.
     * <p>
     * This is a blocking call. It will block until the server is disposed fully.
     *
     * @param server server to dispose
     */
    void disposeServer(RpcNetworkServer server);

    /**
     * Equivalent of calling {@link #disposeServer(RpcNetworkServer)} on all servers but this method disposes all servers in parallel and blocks until all are disposed.
     *
     * @see #disposeServer(RpcNetworkServer)
     */
    void disposeAllServers();

    /**
     * Returns all the clients currently managed by this RPC
     *
     * @return all the clients currently managed by this RPC
     */
    @CollectionMayBeImmutable
    Collection<RpcNetworkClient> getNetworkClients();

    /**
     * Setups a new network client managed by this RPC. This client must be started manually using {@link RpcNetworkServer#start()} or {@link RpcNetworkServer#startBlocking()}
     *
     * @param name           a unique name of the server
     * @param address        address that the client will connect to
     * @param port           port that the client will use
     * @param services       services that the client wishes to use
     * @param persistentMode if this client's connection should be initialized in a persistent (stateful) mode. (See {@link DataSerializer#isPersistent()})
     *
     * @return a newly created, managed client
     */
    RpcNetworkClient setupClient(String name, String address, int port, Collection<Class<? extends RpcService>> services, boolean persistentMode);

    /**
     * Disposes the given client. The client will be shutdown before if its running. After disposal the client is no longer managed by this RPC.
     * <p>
     * This is a blocking call. It will block until the client is disposed fully.
     *
     * @param client server to dispose
     */
    void disposeClient(RpcNetworkClient client);

    /**
     * Equivalent of calling {@link #disposeClient(RpcNetworkClient)} on all clients but this method disposes all clients in parallel and blocks until all are disposed.
     *
     * @see #disposeClient(RpcNetworkClient)
     */
    void disposeAllClients();

    /**
     * Equivalent of calling both {@link #disposeAllClients()} and {@link #disposeAllServers()} but everything is disposed in parallel.
     *
     * @see #disposeAllClients()
     * @see #disposeAllServers()
     */
    void disposeAll();
}
