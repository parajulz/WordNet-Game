import helperclasses.Graph;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertTrue;

public class GraphTests {

    private Graph graph;

    public GraphTests() {
        graph = new Graph();

        graph.addNode(1);
        graph.addNode(2);
        graph.addNode(3);
        graph.addNode(4);
        graph.addEdge(1, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 3);
    }

    @Test
    public void addNodeTest() {
        graph.addNode(10);
        Set<Integer> hyponyms = graph.getHyponymValue(10);
        assertTrue(hyponyms.isEmpty());
    }

    @Test
    public void addEdgeTest() {
        Set<Integer> hyponyms = graph.getHyponymValue(1);
        assertTrue(hyponyms.contains(2));
        assertTrue(hyponyms.contains(3));
    }
}
