package net.mrgregorix.variant.rpc.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.scanner.api.Managed;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Managed
public @interface RpcServiceImplementation
{
}
