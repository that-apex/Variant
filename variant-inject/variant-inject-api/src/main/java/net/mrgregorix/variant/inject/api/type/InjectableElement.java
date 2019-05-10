package net.mrgregorix.variant.inject.api.type;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Represents a 'place' where an injected value can be stored.
 * <p>
 * In normal case it's either a field or a constructor parameter
 */
public interface InjectableElement extends AnnotatedElement
{
    /**
     * @return all annotations that are declared on this type
     */
    Annotation[] getAnnotations();

    /**
     * @return type that this element has
     */
    Class<?> getType();

    /**
     * @return type that declared this element
     */
    Class<?> getDeclaringType();

    /**
     * The internal object that this wraps. (i.e. java.lang.reflect.Field or Parameter)
     *
     * @return an internal object that this wraps
     */
    Object getHandle();
}
