package net.mrgregorix.variant.rpc.inject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkComponent;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;
import net.mrgregorix.variant.rpc.inject.annotation.RpcServiceImplementation;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigClient;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigGroup;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigServer;
import net.mrgregorix.variant.rpc.inject.config.RpcConfiguration;
import net.mrgregorix.variant.scanner.api.VariantScanner;
import net.mrgregorix.variant.utils.annotation.AnnotationUtils;

/**
 * A helper class for configuring {@link VariantRpc} using a provided config.
 */
public class RpcConfigurer
{
    private static final XmlMapper        xmlMapper = new XmlMapper();
    private final        RpcConfiguration configuration;

    /**
     * Reads an XML config from the given stream and creates a new RpcConfigurer using it.
     *
     * @param xmlConfig stream containing the XML config
     *
     * @throws IOException rethrown from {@link XmlMapper#readValue(InputStream, Class)}
     */
    public RpcConfigurer(final InputStream xmlConfig) throws IOException
    {
        this(xmlMapper.readValue(xmlConfig, RpcConfiguration.class));
    }

    /**
     * Creates a new RpcConfigurer
     *
     * @param configuration config to use
     */
    public RpcConfigurer(final RpcConfiguration configuration)
    {
        this.configuration = configuration;
    }

    /**
     * Gets the configuration that this configurer was initialized with.
     *
     * @return the configuration that this configurer was initialized with
     */
    public RpcConfiguration getConfiguration()
    {
        return this.configuration;
    }

    /**
     * Configures the given {@link Variant} instance based on this config.
     * <p>
     * This instance must have {@link VariantRpc} and {@link VariantScanner} modules registered, and all the implementations already instantiated by the scanner.
     *
     * @param variant {@link Variant} instance to use
     */
    @SuppressWarnings({"unchecked", "ReturnOfNull"})
    public void configure(final Variant variant)
    {
        final VariantRpc rpc = variant.getModule(VariantRpc.class);
        final Map<String, List<ServiceImplementationDetail<?, ?>>> implementations = new HashMap<>();

        for (final Object object : variant.getModule(VariantScanner.class).getAllObjects())
        {
            if (! (object instanceof RpcService))
            {
                continue;
            }

            final RpcServiceImplementation annotation = AnnotationUtils.getAnnotation(variant.asProxy(object).getProxyBaseClass(), RpcServiceImplementation.class);

            if (annotation == null)
            {
                continue;
            }

            implementations.computeIfAbsent(annotation.group(), x -> new ArrayList<>()).add(new ServiceImplementationDetail(annotation.service(), (RpcService) object));
        }

        final List<RpcNetworkComponent> components = new ArrayList<>();
        for (final RpcConfigGroup group : this.configuration.getGroups())
        {
            final List<ServiceImplementationDetail<?, ?>> details = implementations.get(group.getId());

            if (group.getClient() != null)
            {
                final RpcConfigClient client = group.getClient();
                final List<Class<? extends RpcService>> services = details.stream().map(ServiceImplementationDetail::getService).collect(Collectors.toList());
                components.add(rpc.setupClient(group.getId(), client.getIp(), client.getPort(), services, true));
            }

            if (group.getServer() != null)
            {
                final RpcConfigServer server = group.getServer();
                components.add(rpc.setupServer(group.getId(), server.getIp(), server.getPort(), details));
            }
        }

        final ExecutorService service = Executors.newFixedThreadPool(components.size());

        final CompletableFuture[] array = components
            .stream()
            .map(it -> CompletableFuture.supplyAsync(() -> {
                it.startBlocking();
                return null;
            }, service))
            .toArray(CompletableFuture[]::new);

        try
        {
            CompletableFuture.allOf(array).get();
        }
        catch (InterruptedException | ExecutionException e)
        {
            throw new RuntimeException("Failed to start all the components", e);
        }
    }
}
