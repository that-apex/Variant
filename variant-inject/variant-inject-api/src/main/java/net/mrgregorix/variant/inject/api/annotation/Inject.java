package net.mrgregorix.variant.inject.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that an injection should be performed on an annotated element when the object is creating using the Injector
 * <p>
 * Either a constructor or a field can be annotated with this annotation, but using only constructor injection is strongly encouraged.
 */
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject
{
}