package net.mrgregorix.variant.rpc.network.netty.component.client;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.exception.CallTimeoutException;
import net.mrgregorix.variant.rpc.api.network.provider.result.FailedRpcCallResult;
import net.mrgregorix.variant.rpc.api.network.provider.result.RpcServiceCallResult;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.NettyRpcNetworkingProvider;
import net.mrgregorix.variant.rpc.network.netty.component.AbstractNettyNetworkComponent;
import net.mrgregorix.variant.rpc.network.netty.component.proto.init.InitPacket;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.nonpersistent.NonPersistentCallRequestPacket;
import net.mrgregorix.variant.rpc.network.netty.component.proto.requestcall.persistent.PersistentCallRequestPacket;
import net.mrgregorix.variant.rpc.network.netty.configuration.ConfigurationFactory;

/**
 * A {@link RpcNetworkClient} that uses Netty for networking.
 *
 * @see NettyRpcNetworkingProvider#setupClient(String, String, int, Collection, DataSerializer)
 */
public class NettyRpcNetworkClient extends AbstractNettyNetworkComponent implements RpcNetworkClient
{
    private final AtomicInteger                     callId      = new AtomicInteger(0);
    private final ReadWriteLock                     resultsLock = new ReentrantReadWriteLock();
    private final Collection<RpcServiceCallResult>  results     = new ArrayList<>(); // todo: remove old entries!
    private final List<Class<? extends RpcService>> services;
    private final DataSerializer                    dataSerializer;
    private       EventLoopGroup                    workerGroup;
    private       Channel                           channel;
    private       Method[]                          methodIds;

    /**
     * Create a new NettyRpcNetworkClient
     *
     * @param configurationFactory {@link ConfigurationFactory} to use by this client
     * @param name                 a unique name of the server
     * @param address              address that the client will connect to
     * @param port                 port that the client will use
     * @param services             services that the client wishes to use
     * @param dataSerializer       serializer that will be used for the client
     */
    public NettyRpcNetworkClient(final ConfigurationFactory configurationFactory, final String name, final String address, final int port, final Collection<Class<? extends RpcService>> services, final DataSerializer dataSerializer)
    {
        super(configurationFactory, name, address, port);
        this.services = new ArrayList<>(services);
        this.dataSerializer = dataSerializer;
    }

    @Override
    public boolean isRunning()
    {
        return this.channel.isOpen();
    }

    @Override
    public void start()
    {
        this.doStart();
    }

    @Override
    public void startBlocking()
    {
        this.doStart().syncUninterruptibly();

        while (this.dataSerializer.isPersistent() && this.methodIds == null)
        {
            synchronized (this)
            {
                try
                {
                    this.wait(1000);
                }
                catch (InterruptedException e)
                {
                    throw new RuntimeException("Interrupted", e);
                }
            }
        }
    }

    private ChannelFuture doStart()
    {
        this.workerGroup = this.getConfigurationFactory().createWorkerGroup();

        final ChannelFuture future = new Bootstrap()
            .group(this.workerGroup)
            .handler(new NettyClientChannelInitializer(this))
            .channel(this.getConfigurationFactory().getClientChannel())
            .connect(this.getAddress(), this.getPort());

        this.channel = future.channel();
        future.addListener(f -> this.channel.writeAndFlush(new InitPacket(this.services, Collections.emptyList(), new HashMap<>(), this.dataSerializer.isPersistent()))); // todo data

        return future;
    }

    @Override
    public void stop()
    {
        this.channel.close();
    }

    @Override
    public void stopBlocking()
    {
        this.channel.close().syncUninterruptibly();
    }

    @Override
    public Future<Void> dispose()
    {
        return CompletableFuture.runAsync(() -> {
            if (this.channel != null && this.channel.isOpen())
            {
                this.channel.close().syncUninterruptibly();
            }

            if (this.workerGroup != null)
            {
                this.workerGroup.shutdownGracefully().syncUninterruptibly();
            }
        });
    }

    @Override
    public CompletableFuture<RpcServiceCallResult> call(final Class<? extends RpcService> service, final Method method, final Object[] arguments)
    {
        // todo: reconnecting
        final int callId = this.callId.incrementAndGet();

        // prepare arguments
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(bytes);
        try
        {
            dataOutputStream.writeShort(arguments.length);

            for (final Object argument : arguments)
            {
                this.dataSerializer.serialize(dataOutputStream, argument);
            }
        }
        catch (final IOException e)
        {
            throw new AssertionError("This should never happen", e);
        }

        if (this.dataSerializer.isPersistent())
        {

            int methodId = - 1;
            for (int i = 0; i < this.methodIds.length; i++)
            {
                if (this.methodIds[i].equals(method))
                {
                    methodId = i;
                    break;
                }
            }

            if (methodId == - 1)
            {
                throw new IllegalArgumentException("Invalid method: " + method);
            }

            int serviceId = - 1;
            for (int i = 0; i < this.services.size(); i++)
            {
                if (this.services.get(i) == service)
                {
                    serviceId = i;
                }
            }

            if (serviceId == - 1)
            {
                throw new IllegalArgumentException("Invalid service: " + service);
            }

            // request packet
            this.channel.writeAndFlush(new PersistentCallRequestPacket(callId, serviceId, methodId, bytes.toByteArray()));
        }
        else
        {
            this.channel.writeAndFlush(new NonPersistentCallRequestPacket(callId, service, method, bytes.toByteArray()));
        }

        // wait for response
        return CompletableFuture.supplyAsync(() -> {
            final long requested = System.currentTimeMillis();

            while (true)
            {
                this.resultsLock.readLock().lock();
                try
                {
                    for (final RpcServiceCallResult result : this.results)
                    {
                        if (result.getCallId() == callId)
                        {
                            return result;
                        }
                    }
                }
                finally
                {
                    this.resultsLock.readLock().unlock();
                }

                if (System.currentTimeMillis() > requested + TimeUnit.SECONDS.toMillis(10)) // todo: configurable timeout?
                {
                    return new FailedRpcCallResult(callId, new CallTimeoutException("Timeout when calling the method: " + method));
                }

                try
                {
                    synchronized (this)
                    {
                        this.wait(1000L);
                    }
                }
                catch (final InterruptedException e)
                {
                    throw new RuntimeException("Interrupted", e);
                }
            }
        }); // todo: executor?
    }

    @Override
    public List<Class<? extends RpcService>> getServices()
    {
        return this.services;
    }

    /**
     * Notify about receiving a new call result. This may be blocking.
     *
     * @param rpcServiceCallResult the received call result
     *
     * @see RpcServiceCallResult
     */
    public void notifyResult(final RpcServiceCallResult rpcServiceCallResult)
    {
        this.resultsLock.writeLock().lock();
        try
        {
            this.results.add(rpcServiceCallResult);
        }
        finally
        {
            this.resultsLock.writeLock().unlock();
        }

        synchronized (this)
        {
            this.notifyAll();
        }
    }

    /**
     * Gets the {@link DataSerializer} used by this client.
     *
     * @return the {@link DataSerializer} used by this client.
     */
    public DataSerializer getDataSerializer()
    {
        return this.dataSerializer;
    }

    /**
     * Sets the method IDs cache that will be used for identifying methods when calling {@link #call(Class, Method, Object[])}.
     *
     * @param methodIds the method IDs cache
     */
    public void setMethodIds(final Method[] methodIds)
    {
        this.methodIds = methodIds;

        synchronized (this)
        {
            this.notifyAll();
        }
    }
}
