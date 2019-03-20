package net.mrgregorix.variant.api.module;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate a default implementation class for the given module.
 * <p>
 * Should only be used on an interface that implements {@link VariantModule}
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleImplementation
{
    /**
     * @return class name of the default implementation
     */
    String value();
}
