package net.mrgregorix.variant.inject.api.type.provider;

import java.util.Collection;

import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

/**
 * Provider for creating {@link InjectableElement} values for classes.
 *
 * @param <E> {@link InjectableElement} subtype that is created by this provider.
 *
 * @see InjectableElement
 */
public interface InjectableElementProvider <E extends InjectableElement>
{
    /**
     * Returns the base type of {@link InjectableElement} that this provider is capable of creating.
     *
     * @return the base type of {@link InjectableElement} that this provider is capable of creating.
     */
    Class<E> getType();

    /**
     * Returns a collection of {@link InjectableElement}s created from the given class.
     *
     * @param clazz clazz to created elements from.
     *
     * @return collection of {@link InjectableElement}
     */
    @CollectionMayBeImmutable
    Collection<E> provide(Class<?> clazz);
}
