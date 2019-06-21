package net.mrgregorix.variant.commands.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.annotation.meta.ParameterDescription;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParameterDescription
public @interface Flag
{
    String name() default "";

    String defaultValue() default "";
}
