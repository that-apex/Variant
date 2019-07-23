package net.mrgregorix.variant.rpc.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.rpc.api.service.RpcService;
import net.mrgregorix.variant.scanner.api.Managed;

/**
 * A {@link Managed}-like annotation for a {@link RpcService} implementations.
 * <p>  This implementation will be automatically added as exposed to all servers, that have groups matching group specified in {@link #groups()}.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Managed
public @interface RpcServiceImplementation
{
    /**
     * Group that this implementation should be added to.
     *
     * @return group that this implementation should be added to.
     */
    String[] groups();

    /**
     * The type of service that this implementation implements.
     *
     * @return the type of service that this implementation implements
     */
    Class<? extends RpcService> service();
}
