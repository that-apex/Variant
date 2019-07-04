package net.mrgregorix.variant.commands.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.annotation.meta.ParameterDescription;

/**
 * A {@link ParameterDescription} for plain command arguments. Argument will be ordered in the same order as they are placed in the method definition.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@ParameterDescription
public @interface Argument
{
    /**
     * Name of the argument.
     *
     * @return name of the argument
     */
    String name() default "";

    /**
     * Is the argument required?
     *
     * @return is the argument required
     */
    boolean required() default true;

    /**
     * The default value to be used
     *
     * @return default value to be used
     */
    String defaultValue() default "";
}
