package net.mrgregorix.variant.utils.collections.wrapped;

import java.util.Collection;

/**
 * A type of {@link WrappedCollection<E>} that exposes its underlying collection publicly using {@link #getWrappedCollection()} method
 * <p>
 * Every change of the underlying collection will result in a call to {@link #collectionChanged()}
 *
 * @param <E> underlying collection type
 */
public class SimpleExposedWrappedCollection <E> extends WrappedCollection<E> implements ExposedWrapperCollection<E>
{
    /**
     * Create a new {@link SimpleExposedWrappedCollection}
     *
     * @param wrappedCollection collection to be wrapped
     */
    public SimpleExposedWrappedCollection(final Collection<E> wrappedCollection)
    {
        super(wrappedCollection);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<E> getWrappedCollection()
    {
        return super.getWrappedCollection();
    }
}
