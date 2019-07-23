package net.mrgregorix.variant.rpc.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.scanner.api.Managed;

/**
 * A {@link Managed}-like annotation for a {@link net.mrgregorix.variant.rpc.api.service.RpcService}.
 * <p>This service will be added as a required service to all clients that are in one of the groups</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Managed
public @interface RpcServiceInfo
{
    /**
     * Group that this service belongs to.
     *
     * @return group that this service belongs to.
     */
    String[] groups();
}
