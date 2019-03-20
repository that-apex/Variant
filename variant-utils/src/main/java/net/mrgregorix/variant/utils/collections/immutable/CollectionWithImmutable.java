package net.mrgregorix.variant.utils.collections.immutable;

import java.util.Collection;

import com.google.common.collect.ImmutableCollection;

/**
 * A collection that is a wrapper for another collection and contains an immutable copy of the wrapped collection that is updated every time the wrapped collection is updated.
 * <p>
 * <pre>
 * For example. {@code var collection = new WrappedCollectionWithImmutable(new ArrayList<String>());
 * collection.add("test");
 * var imm1 = collection.getImmutable();
 * var imm2 = collection.getImmutable();
 * collection.add("test2");
 * var imm3 = collection.getImmutable();
 * // imm1 == imm2 - since the wrapped collection data wasn't changed the immutable
 * // instance was cached
 * // imm1 != imm3 - since the wrapped collection data has changed the immutable instance was recreated
 * }
 * </pre>
 *
 * @param <E> type of collection elements
 * @param <C> type of used ImmutableCollection subclass
 */
public interface CollectionWithImmutable <E, C extends ImmutableCollection<E>> extends Collection<E>
{
    /**
     * Returns an immutable collection that is an immutable copy of the wrapped collection. This method may return the same ImmutableCollection instance if no changes were made to the wrapped
     * collection since the previous function call.
     *
     * @return an immutable collection
     */
    C getImmutable();
}