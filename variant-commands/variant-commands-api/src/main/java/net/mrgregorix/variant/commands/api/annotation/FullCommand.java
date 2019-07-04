package net.mrgregorix.variant.commands.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.annotation.meta.ForType;
import net.mrgregorix.variant.commands.api.annotation.meta.ParameterDescription;

/**
 * A {@link ParameterDescription} that will provide a full string containing all method parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ParameterDescription
@ForType(String.class)
public @interface FullCommand
{
}
