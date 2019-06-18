package net.mrgregorix.variant.rpc.inject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.api.annotations.OnlyForProxiedClasses;
import net.mrgregorix.variant.inject.api.annotation.Inject;
import net.mrgregorix.variant.rpc.api.service.RpcService;

/**
 * Annotates that an injection of a {@link RpcService} should be performed on the given element.
 * <p>
 * Can be used instead of a {@link Inject} annotation.
 */
@Inject
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@OnlyForProxiedClasses
public @interface InjectRpc
{
    /**
     * Returns the name of the client that should be used to handle this service.
     *
     * @return the name of the client that should be used to handle this service
     */
    String value();
}