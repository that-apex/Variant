package net.mrgregorix.variant.utils.collections;

import java.util.ArrayList;

import com.google.common.collect.ImmutableList;
import net.mrgregorix.variant.utils.collections.immutable.WrappedCollectionWithImmutable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestImmutableCollections
{
    @Test
    public void testWrappedCollectionWithImmutable()
    {
        final WrappedCollectionWithImmutable<String, ImmutableList<String>> collection = new WrappedCollectionWithImmutable<>(new ArrayList<>(), ImmutableList::copyOf);
        collection.add("test");
        final ImmutableList<String> imm1 = collection.getImmutable();
        final ImmutableList<String> imm2 = collection.getImmutable();
        collection.add("test2");
        final ImmutableList<String> imm3 = collection.getImmutable();

        Assertions.assertSame(imm1, imm2, "an ImmutableCollection was recreated even when there was no change");
        Assertions.assertNotSame(imm1, imm3, "an ImmutableCollection was  not recreated even when there was a change");
    }
}
