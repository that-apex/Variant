package net.mrgregorix.variant.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.api.Variant;

/**
 * Annotations annotated by {@link OnlyForProxiedClasses} will have any effect only on classes that are proxied by {@link Variant}.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface OnlyForProxiedClasses
{
}
