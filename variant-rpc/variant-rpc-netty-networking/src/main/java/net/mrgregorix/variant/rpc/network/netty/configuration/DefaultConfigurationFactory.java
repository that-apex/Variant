package net.mrgregorix.variant.rpc.network.netty.configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

abstract class DefaultConfigurationFactory implements ConfigurationFactory
{
    public static final long DEFAULT_TIMEOUT = TimeUnit.SECONDS.toMillis(10);

    private static final AtomicInteger instanceId = new AtomicInteger();

    private final AtomicInteger workerId     = new AtomicInteger();
    private final AtomicInteger waiterId     = new AtomicInteger();
    private final int           thisInstance = instanceId.incrementAndGet();

    protected ThreadFactory createBossThreadFactory()
    {
        return runnable -> new Thread(runnable, "VariantRPC" + this.thisInstance + "-Netty-Boss");
    }

    protected ThreadFactory createWorkerThreadFactory()
    {
        return runnable -> new Thread(runnable, "VariantRPC" + this.thisInstance + "-Netty-Worker-" + this.workerId.incrementAndGet());
    }

    @Override
    public Executor createWaitingExecutor()
    {
        return Executors.newCachedThreadPool(runnable -> new Thread(runnable, "VariantRPC" + this.thisInstance + "-WaitingThread-" + this.waiterId.incrementAndGet()));
    }

    @Override
    public long getCallTimeout()
    {
        return DEFAULT_TIMEOUT;
    }
}
