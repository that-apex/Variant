package net.mrgregorix.variant.commands.api.annotation.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.CommandListener;
import net.mrgregorix.variant.commands.api.annotation.Command;
import net.mrgregorix.variant.commands.api.manager.ForTypeMismatchException;

/**
 * Ensures that a parameter annotated with an annotation that has {@link ForType} on it is of type specifed by {@link #value()}.
 * <p>
 * Works only on {@link Command} annotated methods in registered {@link CommandListener}s
 * </p>
 *
 * @see ForTypeMismatchException
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ForType
{
    /**
     * Returns type that is required.
     *
     * @return type that is required
     */
    Class<?> value();
}
