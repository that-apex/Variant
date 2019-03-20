package net.mrgregorix.variant.utils.graph;

import java.util.Objects;

/**
 * Represents a very simple immutable implementation of {@link Edge} holding two constant values for the pointing and pointed vertex
 *
 * @param <T>
 */
public final class SimpleEdge <T> implements Edge<T>
{
    private final T pointingVertex;
    private final T pointedVertex;

    /**
     * Create a new {@link SimpleEdge}
     *
     * @param pointingVertex vertex where the edge originates from (points from)
     * @param pointedVertex  vertex where the edge points to
     */
    public SimpleEdge(final T pointingVertex, final T pointedVertex)
    {
        this.pointingVertex = Objects.requireNonNull(pointingVertex, "pointingVertex");
        this.pointedVertex = Objects.requireNonNull(pointedVertex, "pointedVertex");
    }

    @Override
    public T getPointingVertex()
    {
        return this.pointingVertex;
    }

    @Override
    public T getPointedVertex()
    {
        return this.pointedVertex;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if ((o == null) || (this.getClass() != o.getClass()))
        {
            return false;
        }

        final var that = (SimpleEdge<?>) o;
        return Objects.equals(this.pointingVertex, that.pointingVertex) &&
               Objects.equals(this.pointedVertex, that.pointedVertex);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.pointingVertex, this.pointedVertex);
    }

    @Override
    public String toString()
    {
        return "SimpleEdge{" + "pointingVertex=" + this.pointingVertex + ", pointedVertex=" + this.pointedVertex + '}';
    }
}
