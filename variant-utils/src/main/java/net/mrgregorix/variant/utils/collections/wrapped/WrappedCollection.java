package net.mrgregorix.variant.utils.collections.wrapped;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;

import net.mrgregorix.variant.utils.annotation.NotNull;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * Wraps a collection. Every call to this collection will be forwarded to the underlying Collection.
 * <p>
 * Every change of the underlying collection will result in a call to {@link #collectionChanged()}
 *
 * @param <E> underlying collection type
 */
public class WrappedCollection <E> implements Collection<E>
{
    private final Collection<E> wrappedCollection;

    /**
     * Create a new {@link WrappedCollection}
     *
     * @param wrappedCollection collection to be wrapped
     */
    public WrappedCollection(final Collection<E> wrappedCollection)
    {
        this.wrappedCollection = Objects.requireNonNull(wrappedCollection, "wrappedCollection");
    }

    /**
     * @return the wrapped collection
     */
    protected Collection<E> getWrappedCollection()
    {
        return this.wrappedCollection;
    }

    /**
     * Called when an underlying wrapped collection is changed in any way
     */
    protected void collectionChanged()
    {
    }

    @Override
    public int size()
    {
        return this.wrappedCollection.size();
    }

    @Override
    public boolean isEmpty()
    {
        return this.wrappedCollection.isEmpty();
    }

    @Override
    public boolean contains(@Nullable final Object o)
    {
        return this.wrappedCollection.contains(o);
    }

    @NotNull
    @Override
    public Iterator<E> iterator()
    {
        return this.wrappedCollection.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray()
    {
        return this.wrappedCollection.toArray();
    }

    @NotNull
    @Override
    @SuppressWarnings("SuspiciousToArrayCall")
    public <T> T[] toArray(@NotNull final T[] a)
    {
        return this.wrappedCollection.toArray(a);
    }

    @Override
    public boolean add(@Nullable final E e)
    {
        return this.notifyCollectionChange(this.wrappedCollection.add(e));
    }

    @Override
    public boolean remove(@Nullable final Object o)
    {
        return this.notifyCollectionChange(this.wrappedCollection.remove(o));
    }

    @Override
    public boolean containsAll(@NotNull final Collection<?> c)
    {
        return this.wrappedCollection.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull final Collection<? extends E> c)
    {
        return this.notifyCollectionChange(this.wrappedCollection.addAll(c));
    }

    @Override
    public boolean removeAll(@NotNull final Collection<?> c)
    {
        return this.notifyCollectionChange(this.wrappedCollection.removeAll(c));
    }

    @Override
    public boolean removeIf(final Predicate<? super E> filter)
    {
        return this.notifyCollectionChange(this.wrappedCollection.removeIf(filter));
    }

    @Override
    public boolean retainAll(@NotNull final Collection<?> c)
    {
        return this.notifyCollectionChange(this.wrappedCollection.retainAll(c));
    }

    @Override
    public void clear()
    {
        final boolean wasEmpty = this.wrappedCollection.isEmpty();

        this.wrappedCollection.clear();

        if (! wasEmpty)
        {
            this.notifyCollectionChange(true);
        }

    }

    @Override
    public Spliterator<E> spliterator()
    {
        return this.wrappedCollection.spliterator();
    }

    @Override
    public Stream<E> stream()
    {
        return this.wrappedCollection.stream();
    }

    @Override
    public Stream<E> parallelStream()
    {
        return this.wrappedCollection.parallelStream();
    }

    private boolean notifyCollectionChange(final boolean changed)
    {
        if (changed)
        {
            this.collectionChanged();
        }

        return changed;
    }
}
