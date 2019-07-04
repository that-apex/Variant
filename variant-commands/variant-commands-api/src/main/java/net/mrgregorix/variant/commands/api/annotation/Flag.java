package net.mrgregorix.variant.commands.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.annotation.meta.ParameterDescription;

/**
 * A {@link ParameterDescription} for command flags. Flags can be placed in any order.
 * <p>Flag will not require an argument only if the parameter is of type {@code boolean}</p>
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParameterDescription
public @interface Flag
{
    /**
     * Name of the flag.
     *
     * @return name of the flag
     */
    String name() default "";

    /**
     * The default value of the flag.
     *
     * @return default value of the flag
     */
    String defaultValue() default "";
}
