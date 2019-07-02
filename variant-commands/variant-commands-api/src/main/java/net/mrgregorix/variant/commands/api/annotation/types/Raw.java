package net.mrgregorix.variant.commands.api.annotation.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.commands.api.annotation.meta.ForType;

/**
 * States that the annotated string parameter should be parsed as a raw string (ignore all special character). This must always be the last argument of a method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@ForType(String.class)
public @interface Raw
{
}
