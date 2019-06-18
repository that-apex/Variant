package net.mrgregorix.variant.inject.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.mrgregorix.variant.api.annotations.OnlyForProxiedClasses;
import net.mrgregorix.variant.inject.api.injector.SimpleSingletonCustomInjector;

/**
 * Annotates that an injection should be performed on an annotated element when the object is creating using the Injector
 * <p>
 * Either a constructor or a field can be annotated with this annotation, but using only constructor injection is strongly encouraged.
 */
@Target({ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@OnlyForProxiedClasses
public @interface Inject
{
    /**
     * Defines whether the annotated annotation will work as a simple injection annotation.
     * <p>
     * This means that an annotated injectable element is available for simple injection. Simple injection is an injection that will simply inject a known, singleton value (i.e. a Service or a common global or environmental instance) by the {@link
     * SimpleSingletonCustomInjector}. This is used when sorting to find the best instantiation order. If X requires Y to be simply-injected and Y is not yet found, X instantiation will be postponed until Y is instantiated. This will only work if X
     * and Y are instantiated in one request.
     * <p>
     * This property has effect only when @Inject annotation is used on another annotation. It does nothing if its on field or on a constructor. All fields and constructors with an @Inject annotation stated explicitly are treated as simple injections
     * and there is no way to change that.
     *
     * @return whether the annotated annotation will work as a simple injection annotation.
     */
    boolean simpleInjection() default false;
}