package net.mrgregorix.variant.utils;

import java.util.Objects;

/**
 * Represents a pair of values
 */
public class Pair <L, R>
{
    private final L left;
    private final R right;

    /**
     * Creates a new pair
     *
     * @param left  the first element of the pair
     * @param right the second element of the pair
     */
    public Pair(final L left, final R right)
    {
        this.left = left;
        this.right = right;
    }

    /**
     * Get the first element of the pair
     *
     * @return the first element of the pair
     */
    public L getLeft()
    {
        return this.left;
    }

    /**
     * Get the second element of the pair
     *
     * @return the second element of the pair
     */
    public R getRight()
    {
        return this.right;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(this.left, pair.left) &&
               Objects.equals(this.right, pair.right);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.left, this.right);
    }

    @Override
    public String toString()
    {
        return "Pair{" +
               "left=" + this.left +
               ", right=" + this.right +
               '}';
    }
}
