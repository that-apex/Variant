package net.mrgregorix.variant.core.proxy.asm;

import java.util.concurrent.Callable;

import net.mrgregorix.variant.api.proxy.Proxy;
import net.mrgregorix.variant.core.coremodule.VariantCoreModule;

public interface InternalProxy extends Proxy
{
    private ThreadLocal<Boolean> getThreadLocal()
    {
        return this.getAdditionalProxyData(VariantCoreModule.NAME + "::Internals::ProxyOmitFlag", () -> ThreadLocal.withInitial(() -> false));
    }

    default boolean getProxyOmitFlag()
    {
        return this.getThreadLocal().get();
    }

    default void setProxyOmitFlag(boolean flag)
    {
        this.getThreadLocal().set(flag);
    }

    @SuppressWarnings("ReturnOfNull")
    @Override
    default <R> R callWithoutProxy(final Callable<R> callable)
    {
        final boolean state = this.getProxyOmitFlag();

        if (! state)
        {
            this.setProxyOmitFlag(true);
        }

        try
        {
            return callable.call();
        }
        catch (final Exception e)
        {
            AsmProxyHelperInternal.sneakyThrow(e);
            return null;
        }
        finally
        {
            this.setProxyOmitFlag(state);
        }
    }
}
