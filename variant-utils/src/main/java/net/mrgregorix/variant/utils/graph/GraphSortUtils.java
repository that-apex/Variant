package net.mrgregorix.variant.utils.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphSortUtils
{
    /**
     * Sorts the given graph topologically using the depth-first-search algorithm.
     *
     * @param graph graph to sort
     * @param <T>   type of the graph vertices
     *
     * @return list of sorted vertices
     */
    public static <T> List<T> sortTopological(final DirectedGraph<T> graph)
    {
        final List<T> output = new ArrayList<>();
        final List<T> marks = new ArrayList<>();

        for (final T vertex : graph.getVertices())
        {
            GraphSortUtils.sortTopologicalVisit(graph, vertex, output, marks);
        }

        return output;
    }

    private static <T> void sortTopologicalVisit(final DirectedGraph<T> graph, final T vertex, final List<T> output, final List<T> visited)
    {
        if (output.contains(vertex) || visited.contains(vertex))
        {
            return;
        }

        visited.add(vertex);

        graph.getEdges()
             .stream()
             .filter(edge -> edge.getPointingVertex() == vertex)
             .forEach(v -> GraphSortUtils.sortTopologicalVisit(graph, v.getPointedVertex(), output, visited));

        visited.remove(vertex);
        output.add(0, vertex);
    }
}

