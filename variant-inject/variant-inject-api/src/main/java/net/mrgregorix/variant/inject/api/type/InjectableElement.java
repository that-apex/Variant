package net.mrgregorix.variant.inject.api.type;

import java.lang.annotation.Annotation;

/**
 * Represents a 'place' where an injected value can be stored.
 * <p>
 * In normal case it's either a field or a constructor parameter
 */
public interface InjectableElement
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
}
