package net.mrgregorix.variant.commands.api.annotation.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.annotation.Command;

/**
 * States that the annotation annotated by this is able to provide a value for a {@link Command} annotated method while executing a command.
 * <p>Only one {@link ParameterDescription} annotation is allowed for a single method parameter.</p>
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterDescription
{
}
