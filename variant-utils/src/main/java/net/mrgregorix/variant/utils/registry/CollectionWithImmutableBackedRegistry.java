package net.mrgregorix.variant.utils.registry;

import java.util.Collection;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.mrgregorix.variant.utils.collections.immutable.CollectionWithImmutable;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;

/**
 * A {@link Registry} implementation that used a backing {@link CollectionWithImmutable} to store the data
 *
 * @param <T> type of the stored objects
 */
public class CollectionWithImmutableBackedRegistry <T, C extends ImmutableCollection<T>> extends CollectionBackedRegistry<T>
{
    private final CollectionWithImmutable<T, ? extends C> backingCollection;

    /**
     * Creates a new CollectionWithImmutableBackedRegistry with the supplied backing collection
     *
     * @param backingCollection backed collection
     */
    public CollectionWithImmutableBackedRegistry(final CollectionWithImmutable<T, ? extends C> backingCollection)
    {
        super(backingCollection);
        this.backingCollection = backingCollection;
    }

    @Override
    public C getRegisteredObjects()
    {
        return this.backingCollection.getImmutable();
    }

    /**
     * Creates a new {@link CollectionWithImmutableBackedRegistry} using an {@link WrappedCollectionWithImmutable#withImmutableList(Collection)} as the backing collection
     *
     * @param collection collection to be used for wrapping with {@link WrappedCollectionWithImmutable}
     * @param <E>        type of collection's elements
     *
     * @return a newly created CollectionWithImmutableBackedRegistry
     */
    public static <E> CollectionWithImmutableBackedRegistry<E, ImmutableList<E>> withImmutableList(final Collection<E> collection)
    {
        return new CollectionWithImmutableBackedRegistry<>(WrappedCollectionWithImmutable.withImmutableList(collection));
    }

    /**
     * Creates a new {@link CollectionWithImmutableBackedRegistry} using an {@link WrappedCollectionWithImmutable#withImmutableSet(Collection)} as the backing collection
     *
     * @param collection collection to be used for wrapping with {@link WrappedCollectionWithImmutable}
     * @param <E>        type of collection's elements
     *
     * @return a newly created CollectionWithImmutableBackedRegistry
     */
    public static <E> CollectionWithImmutableBackedRegistry<E, ImmutableSet<E>> withImmutableSet(final Collection<E> collection)
    {
        return new CollectionWithImmutableBackedRegistry<>(WrappedCollectionWithImmutable.withImmutableSet(collection));
    }
}
