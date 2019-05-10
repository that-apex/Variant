package net.mrgregorix.variant.utils.graph;

import java.util.Collection;

import net.mrgregorix.variant.utils.annotation.CollectionMayBeImmutable;
import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * A representation of a directed graph.
 *
 * @param <T> type of a graph vertices
 *
 * @see <a href="https://en.wikipedia.org/wiki/Directed_graph">https://en.wikipedia.org/wiki/Directed_graph/a>
 */
public interface DirectedGraph <T>
{
    /**
     * Adds a new edge that connects {@code vertex} to {@code vertexToPoint} to the graph.
     * <p>
     * Both vertices  will be added to the graph if they are absent.
     *
     * @param vertex        vertex from where the edge points
     * @param vertexToPoint vertex that edge points to
     *
     * @return a newly created edge if the operation succeeded or null if such edge already existed
     *
     * @throws IllegalArgumentException when {@code vertex} is equal to {@code vertexToPoint}
     */
    @Nullable
    Edge<T> addEdge(T vertex, T vertexToPoint);

    /**
     * Adds a new {@code edge} to the graph.
     * <p>
     * Both vertices from the edge will be added to the graph if they are absent.
     *
     * @param edge a edge to be added
     *
     * @return true if the edge was added to the graph, false if such edge already existed
     *
     * @throws IllegalArgumentException when {@link Edge#getPointingVertex()} ()} is equal to {@link Edge#getPointedVertex()}
     */
    boolean addEdge(Edge<T> edge);

    /**
     * Adds new edge the graph.
     * <p>
     * Both vertices from every edge will be added to the graph if they are absent.
     *
     * @param edges edges to be added
     *
     * @return how much edges were successfully added
     */
    int addEdges(Collection<? extends Edge<T>> edges);

    /**
     * Removes an edge from the graph. If this edge was the only edge pointing to/from a vertex, that vertex will be removed from the graph so the graph does not contain any orphaned vertices.
     *
     * @param edges an edge to be removed
     *
     * @return true if the edge was removed from the graph, false if the supplied edge was not part of the graph
     */
    boolean removeEdge(Edge<T> edges);

    /**
     * Removes edges from the graph. This method works similar to {@link #removeEdge(Edge)} but checks for orphaned nodes to delete only after all of the edges were removed.
     *
     * @param edges edges to be removed
     *
     * @return how much edges were successfully removed
     *
     * @see #removeEdge(Edge)
     */
    int removeEdges(Collection<Edge<T>> edges);

    /**
     * Removes a vertex from the graph and all edges that was pointing to that vertex or from that vertex.
     *
     * @param vertex vertex to be removed
     *
     * @return true if the vertex was removed from the graph, false if the supplied vertex was not part of the graph
     */
    boolean removeVertex(T vertex);

    /**
     * Removes vertices from the graph. This method works similar to {@link #removeVertex(Object)} but checks for orphaned nodes to delete only after all of the vertices were removed.
     *
     * @return how much vertices were successfully removed
     *
     * @see #removeVertex(Object)
     */
    int removeVertices(Collection<T> vertices);

    /**
     * @return a collection containing all graph's edges
     */
    @CollectionMayBeImmutable
    Collection<? extends Edge<T>> getEdges();

    /**
     * @return a collection containing all graph's vertices
     */
    @CollectionMayBeImmutable
    Collection<? extends T> getVertices();
}
