package net.mrgregorix.variant.inject.api.type.provider;

import java.util.Collection;

import net.mrgregorix.variant.inject.api.type.InjectableElement;
import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

public interface InjectableElementProvider <E extends InjectableElement>
{
    Class<E> getType();

    @CollectionMayBeImmutable
    Collection<E> provide(Class<?> clazz);
}
