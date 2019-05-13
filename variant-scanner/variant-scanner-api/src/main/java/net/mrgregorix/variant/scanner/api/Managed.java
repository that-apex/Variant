package net.mrgregorix.variant.scanner.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A @Managed annotation class will be automatically instantiated when the {@link VariantScanner#instantiate(String)} will find it in the scanned package.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Managed
{
}
