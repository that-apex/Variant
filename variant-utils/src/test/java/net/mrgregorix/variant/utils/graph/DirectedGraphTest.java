package net.mrgregorix.variant.utils.graph;

import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class DirectedGraphTest
{
    @SuppressWarnings("unchecked")
    @Test
    public void testAdding()
    {
        final String dependant1 = "Dependant1";
        final String dependant2 = "Dependant2";
        final String dependency = "Dependency";
        final DirectedGraph<String> directedGraph = new SetBasedDirectedGraph<>();

        assertThat("edges are not empty by default", directedGraph.getEdges(), is(empty()));
        assertThat("vertices are not empty by default", directedGraph.getVertices(), is(empty()));

        final Edge<String> edge1 = directedGraph.addEdge(dependant1, dependency);
        final Edge<String> edge2 = directedGraph.addEdge(dependant2, dependency);
        assertThat("edges were not added", directedGraph.getEdges(), hasSize(2));
        assertThat("edges were not added", directedGraph.getEdges(), contains(edge1, edge2));

        assertThat("not all vertices are listed", directedGraph.getVertices(), containsInAnyOrder(dependant1, dependant2, dependency));
        assertThat("vertices count is invalid", directedGraph.getVertices(), hasSize(3));

        directedGraph.addEdges(Arrays.asList(edge1, edge2));
        directedGraph.addEdge(new SimpleEdge<>(dependant1, dependency));
        assertThat("graph accepts duplicated edges", directedGraph.getEdges(), hasSize(2));

        final DirectedGraph<String> newGraph = new SetBasedDirectedGraph<>();
        assertThat("not all edges were added", newGraph.addEdges(directedGraph.getEdges()), is(directedGraph.getEdges().size()));

        assertThat("edges of copied graph are not the same", directedGraph.getEdges(), containsInAnyOrder(newGraph.getEdges().toArray()));
        assertThat("vertices of copied graph are not the same", directedGraph.getVertices(), containsInAnyOrder(newGraph.getVertices().toArray()));

        assertThat("invalid amount od edges was added", newGraph.addEdges(Arrays.asList(edge1, new SimpleEdge<>("abc", "def"))), is(1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> directedGraph.addEdge("x", "x"), "vertices pointing to themselves are allowed");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRemoving()
    {
        final String dependant1 = "Dependant1";
        final String dependant2 = "Dependant2";
        final String dependency = "Dependency";
        final DirectedGraph<String> directedGraph = new SetBasedDirectedGraph<>();
        final Edge<String> edge1 = directedGraph.addEdge(dependant1, dependency);
        final Edge<String> edge2 = directedGraph.addEdge(dependant2, dependency);

        assertThat("failed to remove edge", directedGraph.removeEdge(edge1));
        assertThat("edge was not removed correctly", directedGraph.getEdges(), hasSize(1));
        assertThat("edge was not removed correctly", directedGraph.getEdges(), contains(edge2));
        assertThat("edge was not removed correctly", directedGraph.getVertices(), containsInAnyOrder(dependant2, dependency));
        assertThat("edge was not removed correctly", directedGraph.getVertices(), hasSize(2));

        assertThat("failed to re-add edge", directedGraph.addEdge(edge1));
        assertThat("failed to remove edge", directedGraph.removeEdge(edge2));
        assertThat("edge was not removed correctly", directedGraph.getEdges(), hasSize(1));
        assertThat("edge was not removed correctly", directedGraph.getEdges(), contains(edge1));
        assertThat("edge was not removed correctly", directedGraph.getVertices(), containsInAnyOrder(dependant1, dependency));
        assertThat("edge was not removed correctly", directedGraph.getVertices(), hasSize(2));

        directedGraph.addEdge(edge2);

        assertThat("failed to remove vertex", directedGraph.removeVertex(dependant1));
        assertThat("vertex was not removed correctly", directedGraph.getEdges(), hasSize(1));
        assertThat("vertex was not removed correctly", directedGraph.getEdges(), allOf(contains(edge2), not(contains(edge1))));
        directedGraph.addEdge(edge1);

        assertThat("failed to remove vertex", directedGraph.removeVertex(dependant2));
        assertThat("vertex was not removed correctly", directedGraph.getEdges(), hasSize(1));
        assertThat("vertex was not removed correctly", directedGraph.getEdges(), allOf(contains(edge1), not(contains(edge2))));
        directedGraph.addEdge(edge2);

        assertThat("failed to remove vertex", directedGraph.removeVertex(dependency));
        assertThat("vertex was not removed correctly", directedGraph.getEdges(), is(empty()));
    }
}
