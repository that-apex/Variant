package net.mrgregorix.variant.utils.graph;

/**
 * Represents an edge of a {@link DirectedGraph}
 *
 * @param <T> type of graph vertices
 */
public interface Edge <T>
{
    /**
     * @return the vertex where the edge originates from (points from)
     */
    T getPointingVertex();

    /**
     * @return the vertex where the edge points to
     */
    T getPointedVertex();
}
