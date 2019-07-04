package net.mrgregorix.variant.commands.api.annotation.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.annotation.meta.ForType;

/**
 * Specifies a radix for a {@link Number} type parameter. Default is always 10. Cannot be used for doubles and floats.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ForType(Number.class)
public @interface Radix
{
    /**
     * Returns the radix.
     *
     * @return the radix
     */
    int value();
}
