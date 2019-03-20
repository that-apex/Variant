package net.mrgregorix.variant.inject.api.type;

import java.lang.reflect.Parameter;

/**
 * Represents a constructor parameter where the constructor is annotated with an @Inject annotations
 *
 * @see InjectableElement
 */
public interface InjectableConstructorParameter extends InjectableElement
{
    /**
     * @return a reflection Parameter that corresponds to this injectable element
     */
    Parameter getHandle();
}
