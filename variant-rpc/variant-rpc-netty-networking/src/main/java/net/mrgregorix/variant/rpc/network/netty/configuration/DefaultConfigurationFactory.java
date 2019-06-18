package net.mrgregorix.variant.rpc.network.netty.configuration;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

abstract class DefaultConfigurationFactory implements ConfigurationFactory
{
    private static final AtomicInteger instanceId = new AtomicInteger();

    private final AtomicInteger workerId     = new AtomicInteger();
    private final int           thisInstance = instanceId.incrementAndGet();

    protected ThreadFactory createBossThreadFactory()
    {
        return runnable -> new Thread(runnable, "VariantRPC" + this.thisInstance + "-Netty-Boss");
    }

    protected ThreadFactory createWorkerThreadFactory()
    {
        return runnable -> new Thread(runnable, "VariantRPC" + this.thisInstance + "-Netty-Worker-" + this.workerId.incrementAndGet());
    }
}