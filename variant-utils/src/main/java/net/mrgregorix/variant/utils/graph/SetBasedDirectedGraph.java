package net.mrgregorix.variant.utils.graph;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import net.mrgregorix.variant.utils.annotation.Nullable;

/**
 * A {@link DirectedGraph} that stores edges in a simple set
 *
 * @param <T> type of edge vertices
 */
public class SetBasedDirectedGraph <T> extends AbstractDirectedGraph<T>
{
    private final Set<Edge<T>> edges;
    private final Set<T>       vertices;

    /**
     * Creates a new SetBasedDirectedGraph using a default list implementation
     */
    public SetBasedDirectedGraph()
    {
        this(new LinkedHashSet<>());
    }

    /**
     * Create a new SetBasedDirectedGraph using supplied list for edge storage
     *
     * @param edges list that will be used for storing edges
     */
    public SetBasedDirectedGraph(final Set<Edge<T>> edges)
    {
        this.edges = Objects.requireNonNull(edges, "edges");
        this.vertices = new LinkedHashSet<>();
        this.addMissingVertices();
    }

    @Override
    protected boolean addEdgeOnly(final Edge<T> edge)
    {
        return this.edges.add(edge);
    }

    @Override
    protected boolean removeEdgeOnly(final Edge<T> edge)
    {
        return this.edges.remove(edge);
    }

    @Override
    protected boolean removeVertexOnly(final T vertex)
    {
        return this.vertices.remove(vertex);
    }

    @Override
    protected void deleteOrphanedEdgesAndVertices()
    {
        this.edges.removeIf(edge -> ! this.vertices.contains(edge.getPointedVertex()) ||
                                    ! this.vertices.contains(edge.getPointingVertex()));
        this.vertices.clear();
        this.addMissingVertices();
    }

    @Override
    protected void addMissingVertices()
    {
        for (final Edge<T> edge : this.edges)
        {
            this.vertices.add(edge.getPointedVertex());
            this.vertices.add(edge.getPointingVertex());
        }
    }

    @Override
    @Nullable
    public Edge<T> addEdge(final T vertex, final T vertexToPoint)
    {
        final Edge<T> edge = new SimpleEdge<>(vertex, vertexToPoint);
        return this.addEdge(edge) ? edge : null;
    }

    @Override
    public Set<Edge<T>> getEdges()
    {
        return this.edges;
    }

    @Override
    public Set<T> getVertices()
    {
        return this.vertices;
    }
}
