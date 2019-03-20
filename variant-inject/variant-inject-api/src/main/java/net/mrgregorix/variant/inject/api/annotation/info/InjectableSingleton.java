package net.mrgregorix.variant.inject.api.annotation.info;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * States that the annotated type is always available for simple singleton injection. Thus using {@code @Inject MyType type;} will always inject the same, registered singleton instance.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface InjectableSingleton
{
}