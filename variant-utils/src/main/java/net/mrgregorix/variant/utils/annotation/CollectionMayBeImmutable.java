package net.mrgregorix.variant.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * States that a collection MAY be immutable.
 * <p>
 * Annotating a method with such annotation means that the returned collection can be immutable.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface CollectionMayBeImmutable
{
}
