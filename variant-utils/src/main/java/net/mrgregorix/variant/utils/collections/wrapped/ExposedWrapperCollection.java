package net.mrgregorix.variant.utils.collections.wrapped;

import java.util.Collection;

/**
 * A type of {@link WrappedCollection} that exposes its underlying collection publicly using {@link #getWrappedCollection()} method
 *
 * @param <E> underlying collection type
 */
public interface ExposedWrapperCollection <E>
{
    Collection<E> getWrappedCollection();
}
