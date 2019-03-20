package net.mrgregorix.variant.utils.collections.immutable;

import java.util.Collection;

import com.google.common.collect.ImmutableCollection;
import net.mrgregorix.variant.utils.collections.wrapped.WrappedCollection;

/**
 * A helper class for creating CollectionWithImmutable for wrapped collections
 *
 * @param <E> type of the collection elements
 */
public abstract class AbstractWrappedCollectionWithImmutable <E, C extends ImmutableCollection<E>> extends WrappedCollection<E> implements CollectionWithImmutable<E, C>
{
    private boolean dirty = true;

    /**
     * Construct a new AbstractWrappedCollectionWithImmutable
     *
     * @param wrappedCollection wrapped collection
     */
    protected AbstractWrappedCollectionWithImmutable(final Collection<E> wrappedCollection)
    {
        super(wrappedCollection);
    }

    /**
     * @return if this collection is dirty, that means it was modified but the immutable collection has not been yet updated
     */
    public boolean isDirty()
    {
        return this.dirty;
    }

    /**
     * Marks this collection as dirty
     */
    public void markAsDirty()
    {
        this.dirty = true;
    }

    @Override
    protected void collectionChanged()
    {
        this.markAsDirty();
    }

    /**
     * If the dirty flag is true, sets its to false and returns true. If the dirty flag is false, returns false.
     *
     * @return if this collection is dirty
     */
    protected boolean getAndResetDirtyFlag()
    {
        final boolean dirty = this.dirty;
        this.dirty = false;
        return dirty;
    }
}
