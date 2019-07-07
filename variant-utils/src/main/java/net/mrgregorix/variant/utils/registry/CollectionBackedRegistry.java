package net.mrgregorix.variant.utils.registry;

import java.util.Collection;

/**
 * A {@link Registry} implementation that used a backing {@link Collection} to store the data
 *
 * @param <T> type of the stored objects
 */
public class CollectionBackedRegistry <T> implements Registry<T>
{
    private final Collection<T> backingCollection;

    /**
     * Creates a new CollectionBackedRegistry with the supplied backing collection
     *
     * @param backingCollection backed collection
     */
    public CollectionBackedRegistry(final Collection<T> backingCollection)
    {
        this.backingCollection = backingCollection;
    }

    @Override
    public Collection<T> getRegisteredObjects()
    {
        return this.backingCollection;
    }

    @Override
    public boolean register(final T object)
    {
        return this.backingCollection.add(object);
    }

    @Override
    public boolean unregister(final T object)
    {
        return this.backingCollection.remove(object);
    }

    @Override
    public void unregisterAll()
    {
        this.backingCollection.clear();
    }
}
