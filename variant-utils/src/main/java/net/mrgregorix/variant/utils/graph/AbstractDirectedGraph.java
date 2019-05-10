package net.mrgregorix.variant.utils.graph;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A helper class for creating {@link DirectedGraph} subclasses
 *
 * @param <T> type of a graph vertices
 */
public abstract class AbstractDirectedGraph <T> implements DirectedGraph<T>
{
    /**
     * Add an edge without adding vertices
     *
     * @param edge edge to be added
     *
     * @return whether the edge was successfully added
     */
    protected abstract boolean addEdgeOnly(Edge<T> edge);

    /**
     * Removes an edge without removing orphaned vertices
     *
     * @param edge edge to be removed
     *
     * @return whether the edge was successfully removed
     */
    protected abstract boolean removeEdgeOnly(Edge<T> edge);

    /**
     * Removes a vertex without removing orphaned edges
     *
     * @param vertex vertex to be removed
     *
     * @return whether the vertex was successfully removed
     */
    protected abstract boolean removeVertexOnly(T vertex);

    /**
     * Delete all orphaned edges vertices in the graph
     */
    protected abstract void deleteOrphanedEdgesAndVertices();

    /**
     * Add missing vertices to the graph
     */
    protected abstract void addMissingVertices();

    /**
     * Executes the {@code function} with {@code value} and then executes {@code end} if necessary
     *
     * @param function function to be performed on the value, it should return whether the function has successfully performed its task
     * @param value    value of what the function should be performed
     * @param end      method to call after the function returned true
     * @param <S>      type of the value
     *
     * @return whether the function call was successful
     */
    private <S> boolean doOnce(final Predicate<S> function, final S value, final Runnable end)
    {
        final boolean success = function.test(value);

        if (success)
        {
            end.run();
        }

        return success;
    }

    /**
     * Executes the {@code function} for every value in {@code collection} and then executes {@code end} if necessary
     *
     * @param function   function to be performed on every element, it should return whether the function has successfully performed its task
     * @param collection collection of values that will be passed to function
     * @param end        method to call after all function calls if any of them was successful
     * @param <S>        type of values in the collection
     *
     * @return amount of successfully performed functions
     */
    private <S> int doBatch(final Predicate<S> function, final Collection<S> collection, final Runnable end)
    {
        final int successes = (int) collection.stream().filter(function).count();

        if (successes > 0)
        {
            end.run();
        }

        return successes;
    }

    @Override
    public boolean addEdge(final Edge<T> edge)
    {
        if (Objects.equals(edge.getPointingVertex(), edge.getPointedVertex()))
        {
            throw new IllegalArgumentException("vertex cannot point to itself");
        }

        return this.doOnce(this::addEdgeOnly, edge, this::addMissingVertices);
    }

    @Override
    public int addEdges(final Collection<? extends Edge<T>> edges)
    {
        return this.doBatch(this::addEdgeOnly, edges, this::addMissingVertices);
    }

    @Override
    public boolean removeEdge(final Edge<T> edge)
    {
        return this.doOnce(this::removeEdgeOnly, edge, this::deleteOrphanedEdgesAndVertices);
    }

    @Override
    public int removeEdges(final Collection<Edge<T>> edges)
    {
        return this.doBatch(this::removeEdgeOnly, edges, this::deleteOrphanedEdgesAndVertices);
    }

    @Override
    public boolean removeVertex(final T vertex)
    {
        return this.doOnce(this::removeVertexOnly, vertex, this::deleteOrphanedEdgesAndVertices);
    }

    @Override
    public int removeVertices(final Collection<T> vertices)
    {
        return this.doBatch(this::removeVertexOnly, vertices, this::deleteOrphanedEdgesAndVertices);
    }
}
