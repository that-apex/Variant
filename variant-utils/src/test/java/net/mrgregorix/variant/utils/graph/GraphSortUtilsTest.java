package net.mrgregorix.variant.utils.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.RepeatedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GraphSortUtilsTest
{
    private static final Random random = new Random();

    @RepeatedTest(10)
    public void testTopologicalSort()
    {
        // prepare data
        final DirectedGraph<String> baseGraph = new SetBasedDirectedGraph<>();
        baseGraph.addEdge("DependencyA", "Main");
        baseGraph.addEdge("DependencyB", "Main");
        baseGraph.addEdge("DependencyAA1", "DependencyA");
        baseGraph.addEdge("DependencyAA2", "DependencyA");
        baseGraph.addEdge("DependencyAB", "DependencyB");
        baseGraph.addEdge("DependencyBoth", "DependencyA");
        baseGraph.addEdge("DependencyBoth", "DependencyB");

        // randomize
        final List<Edge<String>> edges = new ArrayList<>(baseGraph.getEdges());
        Collections.shuffle(edges, random);
        final DirectedGraph<String> shuffleGraph = new SetBasedDirectedGraph<>(new LinkedHashSet<>(edges));

        // sort
        final List<String> result = GraphSortUtils.sortTopological(shuffleGraph);

        // assert
        assertThat("some items were omitted during sorting", result, containsInAnyOrder("Main", "DependencyA", "DependencyB", "DependencyAA1", "DependencyAA2", "DependencyAB", "DependencyBoth"));
        assertThat("size of the result is invalid", result, hasSize(7));

        for (final Edge<String> edge : baseGraph.getEdges())
        {
            assertThat(result.indexOf(edge.getPointingVertex()), is(lessThan(result.indexOf(edge.getPointedVertex()))));
        }
    }
}
