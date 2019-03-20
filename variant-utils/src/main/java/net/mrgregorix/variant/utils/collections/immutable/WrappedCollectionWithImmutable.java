package net.mrgregorix.variant.utils.collections.immutable;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Function;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * An implementation of {@link CollectionWithImmutable} that uses an existing collection.
 */
public class WrappedCollectionWithImmutable <E, C extends ImmutableCollection<E>> extends AbstractWrappedCollectionWithImmutable<E, C>
{
    protected final   Function<Collection<E>, C> creator;
    private @Nullable C                          immutableCollection;

    /**
     * Construct a new WrappedCollectionWithImmutable
     *
     * @param wrappedCollection a collection that should be wrapped by this collection. The wrapped collection should never be modified directly.
     * @param creator           a function that will create an ImmutableCollection from an existing Collection
     */
    public WrappedCollectionWithImmutable(final Collection<E> wrappedCollection, final Function<Collection<E>, C> creator)
    {
        super(wrappedCollection);
        this.creator = Objects.requireNonNull(creator, "creator");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public C getImmutable()
    {
        if (this.getAndResetDirtyFlag() || this.immutableCollection == null)
        {
            this.immutableCollection = this.creator.apply(this.getWrappedCollection());
        }

        return this.immutableCollection;
    }

    /**
     * Creates a new {@link WrappedCollectionWithImmutable} using an {@link ImmutableList} as the immutable collection
     *
     * @param <E>        type of collection's elements
     * @param collection collection to be wrapper
     *
     * @return new {@link WrappedCollectionWithImmutable} instance
     */
    public static <E> WrappedCollectionWithImmutable<E, ImmutableList<E>> withImmutableList(final Collection<E> collection)
    {
        return new WrappedCollectionWithImmutable<>(collection, ImmutableList::copyOf);
    }
}
