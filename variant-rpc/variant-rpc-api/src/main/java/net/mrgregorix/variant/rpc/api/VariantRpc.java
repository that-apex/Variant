package net.mrgregorix.variant.rpc.api;

import java.util.Collection;
import javax.annotation.concurrent.ThreadSafe;

import net.mrgregorix.variant.api.module.ModuleImplementation;
import net.mrgregorix.variant.api.module.VariantModule;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.DefaultSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

@ThreadSafe
@ModuleImplementation("net.mrgregorix.variant.rpc.core.VariantRpcImpl")
public interface VariantRpc extends VariantModule
{
    @CollectionMayBeImmutable
    Collection<RpcService> getRegisteredServices();

    @CollectionMayBeImmutable
    Collection<RpcService> getRegisteredServiceImplementations();

    <T extends RpcService> T getService(String name);

    <T extends RpcService> T getService(Class<T> type);

    DataSerializer getDataSerializer();

    DataSerializer setDataSerializer();

    boolean isDefaultSerializerAvailable();

    DefaultSerializer getDefaultSerializer();

    @CollectionMayBeImmutable
    Collection<RpcNetworkServer> getNetworkServers();

    RpcNetworkServer setupServer(String name, String address, int port, Collection<ServiceImplementationDetail<?, ?>> serviceImplementationDetails);

    String getService(RpcNetworkServer server);

    void disposeServer(RpcNetworkServer server);

    void disposeAllServers();

    @CollectionMayBeImmutable
    Collection<RpcNetworkClient> getNetworkClients();

    RpcNetworkClient setupClient(String name, String address, Collection<Class<? extends RpcService>> services);

    RpcNetworkClient setupClient(String name, String address, int port, Collection<Class<? extends RpcService>> services);
}
