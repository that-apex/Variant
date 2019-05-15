package net.mrgregorix.variant.utils.collections.immutable;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.concurrent.ThreadSafe;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * A thread-safe implementation of {@link CollectionWithImmutable} that uses an existing collection.
 */
@ThreadSafe
public class SynchronizedWrappedCollectionWithImmutable <E, C extends ImmutableCollection<E>> extends AbstractWrappedCollectionWithImmutable<E, C>
{
    protected final   Function<Collection<E>, C> creator;
    private @Nullable C                          immutableCollection;

    /**
     * Construct a new SynchronizedWrappedCollectionWithImmutable
     *
     * @param wrappedCollection a collection that should be wrapped by this collection. The wrapped collection should never be modified directly.
     * @param creator           a function that will create an ImmutableCollection from an existing Collection
     */
    public SynchronizedWrappedCollectionWithImmutable(final Collection<E> wrappedCollection, final Function<Collection<E>, C> creator)
    {
        super(wrappedCollection);
        this.creator = Objects.requireNonNull(creator, "creator");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized C getImmutable()
    {
        final boolean dirtyFlagState = this.getAndResetDirtyFlag();

        if (dirtyFlagState || this.immutableCollection == null)
        {
            this.immutableCollection = this.creator.apply(this.getWrappedCollection());
        }

        return this.immutableCollection;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean isDirty()
    {
        return super.isDirty();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void markAsDirty()
    {
        super.markAsDirty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected synchronized void collectionChanged()
    {
        super.collectionChanged();
    }

    /**
     * Creates a new {@link SynchronizedWrappedCollectionWithImmutable} using an {@link ImmutableList} as the immutable collection
     *
     * @param <E>        type of collection's elements
     * @param collection collection to be wrapper
     *
     * @return new {@link SynchronizedWrappedCollectionWithImmutable} instance
     */
    public static <E> SynchronizedWrappedCollectionWithImmutable<E, ImmutableList<E>> withImmutableList(final Collection<E> collection)
    {
        return new SynchronizedWrappedCollectionWithImmutable<>(collection, ImmutableList::copyOf);
    }

    /**
     * Creates a new {@link SynchronizedWrappedCollectionWithImmutable} using an {@link ImmutableSet} as the immutable collection
     *
     * @param <E>        type of collection's elements
     * @param collection collection to be wrapper
     *
     * @return new {@link SynchronizedWrappedCollectionWithImmutable} instance
     */
    public static <E> SynchronizedWrappedCollectionWithImmutable<E, ImmutableSet<E>> withImmutableSet(final Collection<E> collection)
    {
        return new SynchronizedWrappedCollectionWithImmutable<>(collection, ImmutableSet::copyOf);
    }
}
