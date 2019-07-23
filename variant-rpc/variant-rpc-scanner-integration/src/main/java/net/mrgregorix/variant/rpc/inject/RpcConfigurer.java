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

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import net.mrgregorix.variant.api.Variant;
import net.mrgregorix.variant.rpc.api.VariantRpc;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkComponent;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.api.service.ServiceImplementationDetail;
import net.mrgregorix.variant.rpc.inject.annotation.RpcServiceImplementation;
import net.mrgregorix.variant.rpc.inject.annotation.RpcServiceInfo;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigAuthentication;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigAuthentications;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigClient;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigComponent;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigGroup;
import net.mrgregorix.variant.rpc.inject.config.RpcConfigServer;
import net.mrgregorix.variant.rpc.inject.config.RpcConfiguration;
import net.mrgregorix.variant.rpc.inject.config.authentications.IpAuthenticationConfig;
import net.mrgregorix.variant.rpc.inject.config.authentications.PublicKeyAuthenticationConfig;
import net.mrgregorix.variant.rpc.inject.deserializer.RpcConfigAuthenticationsDeserializer;
import net.mrgregorix.variant.scanner.api.VariantScanner;
import net.mrgregorix.variant.utils.annotation.AnnotationUtils;

/**
 * A helper class for configuring {@link VariantRpc} using a provided config.
 */
public class RpcConfigurer
{
    private static final RpcConfigAuthenticationsDeserializer deserializer = new RpcConfigAuthenticationsDeserializer();
    private static final XmlMapper                            xmlMapper    = new XmlMapper();
    private final        RpcConfiguration                     configuration;

    static
    {
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(RpcConfigAuthentications.class, deserializer);
        xmlMapper.registerModule(module);

        // defaults
        registerAuthenticationType("IpAuthentication", IpAuthenticationConfig.class);
        registerAuthenticationType("PublicKeyAuthentication", PublicKeyAuthenticationConfig.class);
    }

    /**
     * Registers a new type of authenticator that can be configured in XML
     *
     * @param name name of the XML tag that will be used to configure the authenticator
     * @param type type that the XML tree will be deserialized as
     */
    public static void registerAuthenticationType(final String name, final Class<? extends RpcConfigAuthentication> type)
    {
        deserializer.registerType(name, type);
    }

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
        final Map<String, List<Class<? extends RpcService>>> services = new HashMap<>();

        for (final Object object : variant.getModule(VariantScanner.class).getAllObjects())
        {
            if (! (object instanceof RpcService))
            {
                continue;
            }

            final RpcServiceInfo infoAnnotation = AnnotationUtils.getAnnotation(variant.asProxy(object).getProxyBaseClass(), RpcServiceInfo.class);

            if (infoAnnotation != null)
            {
                for (final String group : infoAnnotation.groups())
                {
                    services.computeIfAbsent(group, x -> new ArrayList<>()).add((Class<? extends RpcService>) variant.asProxy(object).getProxyBaseClass());
                }
            }

            final RpcServiceImplementation implAnnotation = AnnotationUtils.getAnnotation(variant.asProxy(object).getProxyBaseClass(), RpcServiceImplementation.class);

            if (implAnnotation != null)
            {
                for (final String group : implAnnotation.groups())
                {
                    implementations.computeIfAbsent(group, x -> new ArrayList<>()).add(new ServiceImplementationDetail(implAnnotation.service(), (RpcService) object));
                }
            }
        }

        final List<RpcNetworkComponent> components = new ArrayList<>();
        for (final RpcConfigGroup group : this.configuration.getGroups())
        {
            if (group.getClient() != null)
            {
                final RpcConfigClient client = group.getClient();
                final List<Class<? extends RpcService>> thisServices = services.get(group.getId());
                if (thisServices == null)
                {
                    throw new IllegalArgumentException("No details for client in group " + group.getId() + ". Possibly missing: @RpcServiceInfo annotation");
                }

                components.add(this.initAuth(rpc.setupClient(group.getId(), client.getIp(), client.getPort(), thisServices, true), client));
            }

            if (group.getServer() != null)
            {
                final List<ServiceImplementationDetail<?, ?>> details = implementations.get(group.getId());
                if (details == null)
                {
                    throw new IllegalArgumentException("No implementation details for server in group " + group.getId() + ". Possibly missing: @RpcServiceImplementation annotation");
                }
                final RpcConfigServer server = group.getServer();
                components.add(this.initAuth(rpc.setupServer(group.getId(), server.getIp(), server.getPort(), details), server));
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

    private RpcNetworkComponent initAuth(final RpcNetworkComponent component, final RpcConfigComponent config)
    {
        if (config.getAuthentication() != null)
        {
            for (final RpcConfigAuthentication<?> authentication : config.getAuthentication().getAuthentications())
            {
                component.getAuthenticatorRegistry().register(authentication.createAuthenticator());
            }
        }

        return component;
    }
}
