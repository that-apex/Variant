package net.mrgregorix.variant.utils.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.meta.TypeQualifierDefault;

/**
 * Every field, return value of a method and parameter in the annotated package will be treated as if it has a {@link NotNull} annotation.
 */
@Target({ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@NotNull
@TypeQualifierDefault({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
public @interface EverythingNotNullByDefault
{
}
