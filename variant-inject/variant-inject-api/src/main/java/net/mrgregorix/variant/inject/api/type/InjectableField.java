package net.mrgregorix.variant.inject.api.type;

import java.lang.reflect.Field;

/**
 * Represents a field annotated with an @Inject annotations.
 *
 * @see InjectableElement
 */
public interface InjectableField extends InjectableElement
{
    /**
     * @return a reflection Field that corresponds to this injectable element
     */
    Field getHandle();
}
