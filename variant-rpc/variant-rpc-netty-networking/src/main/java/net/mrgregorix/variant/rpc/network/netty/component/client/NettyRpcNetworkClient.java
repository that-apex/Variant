package net.mrgregorix.variant.rpc.network.netty.component.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import net.mrgregorix.variant.rpc.api.network.RpcNetworkClient;
import net.mrgregorix.variant.rpc.api.network.authenticator.RpcAuthenticator;
import net.mrgregorix.variant.rpc.api.network.authenticator.result.DataAuthenticationResult;
import net.mrgregorix.variant.rpc.api.network.exception.AuthenticationFailureException;
import net.mrgregorix.variant.rpc.api.network.exception.CallTimeoutException;
import net.mrgregorix.variant.rpc.api.network.exception.ConnectionFailureException;
import net.mrgregorix.variant.rpc.api.network.provider.result.FailedRpcCallResult;
import net.mrgregorix.variant.rpc.api.network.provider.result.RpcServiceCallResult;
import net.mrgregorix.variant.rpc.api.serialize.DataSerializer;
import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.rpc.network.netty.NettyRpcNetworkingProvider;
import net.mrgregorix.variant.rpc.network.netty.component.AbstractNettyNetworkComponent;
import net.mrgregorix.variant.rpc.network.netty.component.proto.auth.AuthPacket;
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
    private static final long MISSED_RESULTS_REFRESH_TIME = TimeUnit.SECONDS.toMillis(1);
    private static final long CONNECT_WAIT_TIME           = TimeUnit.SECONDS.toMillis(1);

    private final AtomicInteger                        callId             = new AtomicInteger(0);
    private final AtomicBoolean                        connectingNow      = new AtomicBoolean();
    private final ReadWriteLock                        resultsLock        = new ReentrantReadWriteLock();
    private final Lock                                 channelLock        = new ReentrantLock();
    private final Map<Integer, RpcAuthenticator>       authenticatorCache = new HashMap<>();
    private final long                                 timeout;
    private final Cache<Integer, RpcServiceCallResult> results;
    private final List<Class<? extends RpcService>>    services;
    private final DataSerializer                       dataSerializer;
    private final Executor                             waitingExecutor;
    private       Throwable                            connectionError;
    private       EventLoopGroup                       workerGroup;
    private       Channel                              channel;
    private       Method[]                             methodIds;

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
        this.waitingExecutor = configurationFactory.createWaitingExecutor();
        this.timeout = configurationFactory.getCallTimeout();
        this.results = CacheBuilder.newBuilder().expireAfterWrite(this.timeout + MISSED_RESULTS_REFRESH_TIME * 2, TimeUnit.SECONDS).build();
    }

    @Override
    public boolean isRunning()
    {
        return this.channel != null && this.channel.isActive();
    }

    @Override
    public void start()
    {
        this.doStart();
    }

    @Override
    public void startBlocking()
    {
        try
        {
            this.doStart().syncUninterruptibly();
        }
        catch (final Exception e)
        {
            throw new ConnectionFailureException("Connect failed", e);
        }

        while (this.connectingNow.get())
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
        this.channelLock.lock();
        try
        {
            if (this.channel != null)
            {
                if (this.channel.isActive())
                {
                    throw new IllegalStateException("Already started");
                }

                if (this.channel.isOpen())
                {
                    this.channel.close();
                }
            }

            this.results.invalidateAll();
            this.authenticatorCache.clear();

            if (this.workerGroup == null)
            {
                this.workerGroup = this.getConfigurationFactory().createWorkerGroup();
            }

            this.connectingNow.set(true);

            final ChannelFuture future = new Bootstrap()
                .group(this.workerGroup)
                .handler(new NettyClientChannelInitializer(this))
                .channel(this.getConfigurationFactory().getClientChannel())
                .connect(this.getAddress(), this.getPort());

            this.channel = future.channel();
            future.addListener(f -> {
                if (f.isSuccess())
                {
                    this.channel.writeAndFlush(new InitPacket(this.services, Collections.emptyList(), this.dataSerializer.isPersistent()));
                }
                else
                {
                    this.connectionError = f.cause();
                    this.connectingNow.set(false);
                }

                synchronized (this)
                {
                    this.notifyAll();
                }
            });

            final Channel currentChannel = this.channel;
            currentChannel.closeFuture().addListener(f -> {
                if (this.channel == currentChannel)
                {
                    this.connectingNow.set(false);

                    synchronized (this)
                    {
                        this.notifyAll();
                    }
                }
            });

            return future;
        }
        finally
        {
            this.channelLock.unlock();
        }
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
        return CompletableFuture.supplyAsync(() -> {
            try
            {
                this.ensureConnectivity();
            }
            catch (final Exception e)
            {
                return new FailedRpcCallResult(FailedRpcCallResult.CONNECTION_NOT_ATTEMPTED, e);
            }

            return this.doCall0(service, method, arguments);
        }, this.waitingExecutor);
    }

    private void ensureConnectivity()
    {
        this.channelLock.lock();

        try
        {
            boolean wasConnecting = this.waitIfConnecting();

            if (this.channel == null || ! this.channel.isActive())
            {
                if (wasConnecting)
                {
                    throw new ConnectionFailureException("Connection failed", this.connectionError);
                }

                try
                {
                    this.doStart().syncUninterruptibly();
                    this.waitIfConnecting();
                }
                catch (final Exception e)
                {
                    throw new ConnectionFailureException("Connection failed", e);
                }
            }
        }
        finally
        {
            this.channelLock.unlock();
        }
    }

    private boolean waitIfConnecting()
    {
        boolean wasConnecting = false;

        while (this.connectingNow.get())
        {
            wasConnecting = true;

            synchronized (this)
            {
                try
                {
                    this.wait(CONNECT_WAIT_TIME);
                }
                catch (final InterruptedException e)
                {
                    throw new RuntimeException("Interrupted", e);
                }
            }
        }

        return wasConnecting;
    }

    private RpcServiceCallResult doCall0(final Class<? extends RpcService> service, final Method method, final Object[] arguments)
    {
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
        final long requested = System.currentTimeMillis();

        while (true)
        {
            this.resultsLock.readLock().lock();
            try
            {
                final RpcServiceCallResult result = this.results.getIfPresent(callId);

                if (result != null)
                {
                    return result;
                }
            }
            finally
            {
                this.resultsLock.readLock().unlock();
            }

            if (System.currentTimeMillis() > requested + this.timeout)
            {
                return new FailedRpcCallResult(callId, new CallTimeoutException("Timeout when calling the method: " + method));
            }

            try
            {
                synchronized (this)
                {
                    this.wait(MISSED_RESULTS_REFRESH_TIME);
                }
            }
            catch (final InterruptedException e)
            {
                throw new RuntimeException("Interrupted", e);
            }
        }
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
            this.results.put(rpcServiceCallResult.getCallId(), rpcServiceCallResult);
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
        this.connectingNow.set(false);

        synchronized (this)
        {
            this.notifyAll();
        }
    }

    /**
     * Notifies the client that authentication was successful
     */
    public void authenticated()
    {
        if (! this.dataSerializer.isPersistent())
        {
            this.connectingNow.set(false);
        }

        synchronized (this)
        {
            this.notifyAll();
        }
    }

    /**
     * Handles authentication data
     *
     * @param issuerId id of the issuer (sender) of the data
     * @param data     received data
     *
     * @throws IOException when an IOException occurs
     */
    public synchronized void authDataReceived(final int issuerId, final byte[] data) throws IOException
    {
        if (! this.authenticatorCache.containsKey(issuerId))
        {
            for (final RpcAuthenticator authenticator : this.getAuthenticatorRegistry().getRegisteredObjects())
            {
                final DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));

                if (authenticator.matchDataReceivedFromServer(stream))
                {
                    this.authenticatorCache.put(issuerId, authenticator);

                    if (stream.available() > 0)
                    {
                        final ByteArrayOutputStream output = new ByteArrayOutputStream();
                        this.authenticatorCache.get(issuerId).dataReceivedFromServer(stream, new DataOutputStream(output));
                        this.channel.writeAndFlush(new AuthPacket(new DataAuthenticationResult(output.toByteArray()), issuerId));
                    }

                    return;
                }
            }

            this.stopBlocking();
            throw new AuthenticationFailureException("No authenticator found to handle the auth message");
        }

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        this.authenticatorCache.get(issuerId).dataReceivedFromServer(new DataInputStream(new ByteArrayInputStream(data)), new DataOutputStream(output));
        this.channel.writeAndFlush(new AuthPacket(new DataAuthenticationResult(output.toByteArray()), issuerId));
    }
}
