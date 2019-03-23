package net.mrgregorix.variant.utils.collections;

import java.util.ArrayList;
import java.util.Collection;

import net.mrgregorix.variant.utils.collections.wrapped.WrappedCollection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestWrappedCollections
{
    private static class WrappedCollectionTest <T> extends WrappedCollection<T>
    {
        private boolean hasChanged;

        public WrappedCollectionTest(Collection<T> wrappedCollection)
        {
            super(wrappedCollection);
        }

        @Override
        protected void collectionChanged()
        {
            this.hasChanged = true;
        }

        public boolean getAndResetChangedFlag()
        {
            final boolean value = this.hasChanged;
            this.hasChanged = false;
            return value;
        }
    }

    @Test
    public void testWrappedCollections()
    {
        final WrappedCollectionTest<String> wrappedCollection = new WrappedCollectionTest<>(new ArrayList<>(5));

        wrappedCollection.add("abc");
        Assertions.assertTrue(wrappedCollection.getAndResetChangedFlag(), "A collection modification was not flagged as modification by the WrappedCollection (add)");
        wrappedCollection.add("def");
        Assertions.assertTrue(wrappedCollection.getAndResetChangedFlag(), "A collection modification was not flagged as modification by the WrappedCollection (add)");
        wrappedCollection.remove("abc");
        Assertions.assertTrue(wrappedCollection.getAndResetChangedFlag(), "A collection modification was not flagged as modification by the WrappedCollection (remove)");
        wrappedCollection.remove("abc");
        Assertions.assertFalse(wrappedCollection.getAndResetChangedFlag(), "A call that did not cause modification was incorrectly flagged as modification by the WrappedCollection (remove)");
    }
}
