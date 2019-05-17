package net.mrgregorix.variant.rpc.core;

import java.util.Collection;

import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.DefaultSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;

public class VariantRpcImpl implements VariantRpc
{
    public static final String MODULE_NAME = "Variant::RPC::Core";

    @Override
    public String getName()
    {
        return VariantRpcImpl.MODULE_NAME;
    }

    @Override
    public void initialize(Variant variant)
    {

    }

    @Override
    public Collection<RpcService> getRegisteredServices()
    {
        return null;
    }

    @Override
    public Collection<RpcService> getRegisteredServiceImplementations()
    {
        return null;
    }

    @Override
    public <T extends RpcService> T getService(String name)
    {
        return null;
    }

    @Override
    public <T extends RpcService> T getService(Class<T> type)
    {
        return null;
    }

    @Override
    public DataSerializer getDataSerializer()
    {
        return null;
    }

    @Override
    public DataSerializer setDataSerializer()
    {
        return null;
    }

    @Override
    public boolean isDefaultSerializerAvailable()
    {
        return false;
    }

    @Override
    public DefaultSerializer getDefaultSerializer()
    {
        return null;
    }

    @Override
    public Collection<RpcNetworkServer> getNetworkServers()
    {
        return null;
    }

    @Override
    public RpcNetworkServer setupServer(String name, String address, int port, Collection<ServiceImplementationDetail<?, ?>> serviceImplementationDetails)
    {
        return null;
    }

    @Override
    public String getService(RpcNetworkServer server)
    {
        return null;
    }

    @Override
    public void disposeServer(RpcNetworkServer server)
    {

    }

    @Override
    public void disposeAllServers()
    {

    }

    @Override
    public Collection<RpcNetworkClient> getNetworkClients()
    {
        return null;
    }

    @Override
    public RpcNetworkClient setupClient(String name, String address, Collection<Class<? extends RpcService>> services)
    {
        return null;
    }

    @Override
    public RpcNetworkClient setupClient(String name, String address, int port, Collection<Class<? extends RpcService>> services)
    {
        return null;
    }
}
