package net.mrgregorix.variant.utils;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class PairTest
{
    @Test
    public void testCreate()
    {
        final String element1 = "abc";
        final String element2 = "def";

        final Pair<String, String> pair = new Pair<>(element1, element2);

        assertThat("left side of the pair is invalid", element1, sameInstance(pair.getLeft()));
        assertThat("right side of the pair is invalid", element2, sameInstance(pair.getRight()));
    }

    @Test
    public void testEquality()
    {
        final String element1 = "abc";
        final String element2 = "def";

        final Pair<String, String> pair1 = new Pair<>(element1, element2);
        final Pair<String, String> pair2 = new Pair<>(element1, element2);

        assertThat("equals did not return true for the same pair", pair1, equalTo(pair1));
        assertThat("equals returned true for null", pair1, not(equalTo(null)));

        assertThat("equals did not return true for pair with the same elements", pair1, equalTo(pair2));
        assertThat("hashCode did not return the same value for pairs with the same elements", pair1.hashCode(), equalTo(pair2.hashCode()));
        assertThat("toString did not return the same value for pairs with the same elements", pair1.toString(), equalTo(pair2.toString()));
    }
}
