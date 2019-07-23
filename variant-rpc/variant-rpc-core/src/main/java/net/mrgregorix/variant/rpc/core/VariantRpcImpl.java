package net.mrgregorix.variant.rpc.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.api.proxy.ProxySpecification;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkComponent;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkServer;
import net.mrgregorix.variant.rpc.api.network.provider.RpcNetworkingProvider;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.serialize.SerializerSpec;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;
import net.mrgregorix.variant.rpc.core.handler.StandardServerHandler;
import net.mrgregorix.variant.rpc.core.proxy.RpcProxyDataSpecs;
import net.mrgregorix.variant.rpc.core.proxy.RpcServiceProxySpecification;
import net.mrgregorix.variant.rpc.core.serialize.SimpleSerializerSpec;
import net.mrgregorix.variant.rpc.core.serialize.nonpersistent.NonPersistentDataSerializer;
import net.mrgregorix.variant.rpc.core.serialize.persistent.PersistentDataSerializer;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;
import net.mrgregorix.variant.utils.exception.AmbiguousException;

public class VariantRpcImpl implements VariantRpc
{
    public static final String MODULE_NAME = "Variant::RPC::Core";

    private final CollectionWithImmutable<RpcService, ImmutableSet<RpcService>>             registeredServices                = WrappedCollectionWithImmutable.withImmutableSet(new HashSet<>());
    private final CollectionWithImmutable<RpcService, ImmutableSet<RpcService>>             registeredServicesImplementations = WrappedCollectionWithImmutable.withImmutableSet(new HashSet<>());
    private final CollectionWithImmutable<RpcNetworkClient, ImmutableSet<RpcNetworkClient>> networkClients                    = WrappedCollectionWithImmutable.withImmutableSet(new HashSet<>());
    private final CollectionWithImmutable<RpcNetworkServer, ImmutableSet<RpcNetworkServer>> networkServers                    = WrappedCollectionWithImmutable.withImmutableSet(new HashSet<>());

    private DataSerializer        persistentDataSerializer;
    private DataSerializer        nonPersistentDataSerializer;
    private RpcNetworkingProvider networkingProvider;
    private Variant               variant;

    public VariantRpcImpl()
    {
        final SerializerSpec spec = new SimpleSerializerSpec();
        this.setDataSerializers(new PersistentDataSerializer(spec), new NonPersistentDataSerializer(spec));
    }

    @Override
    public String getName()
    {
        return VariantRpcImpl.MODULE_NAME;
    }

    @Override
    public void initialize(Variant variant)
    {
        this.variant = variant;
    }

    @Override
    public Collection<ProxySpecification> getProxySpecifications()
    {
        return Collections.singletonList(new RpcServiceProxySpecification(this));
    }

    @Override
    public Collection<RpcService> getRegisteredServices()
    {
        return this.registeredServices.getImmutable();
    }

    @Override
    public Collection<RpcService> getRegisteredServiceImplementations()
    {
        return this.registeredServicesImplementations.getImmutable();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RpcService> T getService(Class<T> type)
    {
        final List<RpcService> matching = this.registeredServices
            .stream()
            .filter(type::isInstance)
            .collect(Collectors.toUnmodifiableList());

        if (matching.isEmpty())
        {
            throw new IllegalArgumentException("No services matching the type " + type + " found");
        }
        else if (matching.size() > 1)
        {
            throw new AmbiguousException("Found multiple services matching the type" + type);
        }

        return (T) matching.get(0);
    }

    @Override
    public DataSerializer getNonPersistentDataSerializer()
    {
        return this.nonPersistentDataSerializer;
    }

    @Override
    public DataSerializer getPersistentDataSerializer()
    {
        return this.persistentDataSerializer;
    }

    @Override
    public void setDataSerializers(final DataSerializer persistentSerializer, final DataSerializer nonPersistentSerializer)
    {
        if (persistentSerializer.getSerializerSpec() != nonPersistentSerializer.getSerializerSpec())
        {
            throw new IllegalArgumentException("Both serializers must use the same spec. (Both may be null)");
        }

        this.persistentDataSerializer = persistentSerializer;
        this.nonPersistentDataSerializer = nonPersistentSerializer;
    }

    @Override
    public boolean isDataSerializerSpecAvailable()
    {
        return this.persistentDataSerializer.getSerializerSpec() != null;
    }

    @Override
    public RpcNetworkingProvider getNetworkingProvider()
    {
        return this.networkingProvider;
    }

    @Override
    public void setNetworkingProvider(final RpcNetworkingProvider networkingProvider)
    {
        this.networkingProvider = networkingProvider;
    }

    @Override
    public SerializerSpec getSerializerSpec()
    {
        return Objects.requireNonNull(this.persistentDataSerializer.getSerializerSpec());
    }

    @Override
    public Collection<RpcNetworkServer> getNetworkServers()
    {
        return this.networkServers.getImmutable();
    }

    @Override
    public RpcNetworkServer setupServer(final String name, final String address, final int port, final Collection<? extends ServiceImplementationDetail<?, ?>> serviceImplementationDetails)
    {
        final Collection<Class<? extends RpcService>> handlers = serviceImplementationDetails.stream().map(ServiceImplementationDetail::getService).collect(Collectors.toList());
        final Map<Class<? extends RpcService>, RpcService> implementationMap = serviceImplementationDetails
            .stream()
            .collect(Collectors.toMap(ServiceImplementationDetail::getService, ServiceImplementationDetail::getImplementation));

        this.registeredServicesImplementations.addAll(implementationMap.values());

        final RpcNetworkServer server = this.networkingProvider.setupServer(name, address, port, handlers, new StandardServerHandler(implementationMap), this.getPersistentDataSerializer(), this.getNonPersistentDataSerializer());
        this.networkServers.add(server);
        return server;
    }

    @Override
    public void disposeServer(final RpcNetworkServer server)
    {
        this.dispose(Collections.singletonList(server));
        this.networkServers.remove(server);
    }

    @Override
    public void disposeAllServers()
    {
        this.dispose(this.networkServers);
        this.networkServers.clear();
    }

    @Override
    public Collection<RpcNetworkClient> getNetworkClients()
    {
        return this.networkClients.getImmutable();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RpcService> T getService(final String clientName, final Class<T> service)
    {
        for (final RpcService registeredService : this.getRegisteredServices())
        {
            if (! service.isInstance(registeredService))
            {
                continue;
            }

            if (! RpcProxyDataSpecs.CLIENT_NAME_SPEC.from(this.variant.asProxy(registeredService)).equals(clientName))
            {
                continue;
            }

            return (T) registeredService;
        }

        final T instance = this.variant.instantiate(service);
        RpcProxyDataSpecs.CLIENT_NAME_SPEC.set(this.variant.asProxy(instance), clientName);
        this.registeredServices.add(instance);
        return instance;
    }

    @Override
    public RpcNetworkClient setupClient(final String name, final String address, final int port, final Collection<Class<? extends RpcService>> services, final boolean persistentMode)
    {
        final DataSerializer dataSerializer = (persistentMode ? this.getPersistentDataSerializer() : this.getNonPersistentDataSerializer()).makeClone();
        final RpcNetworkClient client = this.networkingProvider.setupClient(name, address, port, services, dataSerializer);
        this.networkClients.add(client);
        return client;
    }

    @Override
    public void disposeClient(final RpcNetworkClient client)
    {
        this.dispose(Collections.singletonList(client));
        this.networkClients.remove(client);
    }

    @Override
    public void disposeAllClients()
    {
        this.dispose(this.networkClients);
        this.networkClients.clear();
    }

    @Override
    public void disposeAll()
    {
        final List<RpcNetworkComponent> components = new ArrayList<>(this.networkServers);
        components.addAll(this.networkClients);
        this.dispose(components);
        this.networkServers.clear();
        this.networkClients.clear();
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    private void dispose(final Collection<? extends RpcNetworkComponent> objects)
    {
        final Collection<Future<Void>> futureList = new ArrayList<>(objects.size());

        for (final RpcNetworkComponent component : objects)
        {
            Preconditions.checkArgument(this.networkClients.contains(component) || this.networkServers.contains(component), "The component is not owned by this RPC");

            if (component instanceof RpcNetworkClient)
            {
                futureList.add(this.networkingProvider.disposeClient((RpcNetworkClient) component));
            }
            else
            {
                futureList.add(this.networkingProvider.disposeServer((RpcNetworkServer) component));
            }
        }

        for (final Future<Void> future : futureList)
        {
            try
            {
                future.get();
            }
            catch (final InterruptedException e)
            {
                throw new RuntimeException("Failed to wait for an object to dispose", e);
            }
            catch (final ExecutionException e)
            {
                throw new RuntimeException("Failed to dispose", e.getCause());
            }
        }
    }
}
