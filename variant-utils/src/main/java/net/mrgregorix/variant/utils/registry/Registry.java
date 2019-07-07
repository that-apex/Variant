package net.mrgregorix.variant.utils.registry;

import java.util.Collection;

import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;

/**
 * Represents a registry that can store objects and be used to register and unregister them.
 *
 * @param <T> type of the stored objects
 */
public interface Registry <T>
{
    /**
     * Returns all the objects registered to this registry.
     *
     * @return all the objects registered to this registry
     */
    @CollectionMayBeImmutable
    Collection<T> getRegisteredObjects();

    /**
     * Registers a new object to this registry
     *
     * @param object object to register
     *
     * @return true if the object was registered successfully
     */
    boolean register(T object);

    /**
     * Unregisters an object from this registry
     *
     * @param object object to unregister
     *
     * @return true if the object was unregistered successfully
     */
    boolean unregister(T object);

    /**
     * Unregisters all objects from this registry
     */
    void unregisterAll();
}
