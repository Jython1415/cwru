import org.junit.Assert;
import org.junit.Test;

public class WeightedGraphTester extends GraphTester {
    
    /**
     * Unit tests for addNode
     */
    @Test
    public void testAddNode() {
        Assert.assertTrue(true);
    }

    /**
     * Unit tests for addNodes
     */
    @Test
    public void testAddNodes() {

    }

    /**
     * Unit tests for addEdge
     */
    @Test
    public void testAddEdge() {

    }

    /**
     * Unit tests for addEdges
     */
    @Test
    public void testAddEdges() {

    }

    /**
     * Unit tests for removeNode
     */
    @Test
    public void testRemoveNode() {

    }

    /**
     * Unit tests for removeNodes
     */
    @Test
    public void testRemoveNodes() {

    }

    protected static WeightedGraph createFromWeightedAdjacencyFormat(String input) throws Exception{
        WeightedGraph g1 = new WeightedGraph();
        String fileName = createAdjacencyTextFile(input);
        g1.read(fileName);
        deleteFile(fileName);

        return g1;
    }

    /**
     * Unit tests for read
     */
    @Test
    public void testRead() throws Exception {
        WeightedGraph g1;
        String fileName;

        // empty file
        g1 = new WeightedGraph();
        fileName = createAdjacencyTextFile("");
        g1.read(fileName);
        deleteFile(fileName);
        Assert.assertEquals("", g1.toString());

        // file with just nodes
        g1 = new WeightedGraph();
        fileName = createAdjacencyTextFile("A\nB\nC");
        g1.read(fileName);
        deleteFile(fileName);
        Assert.assertEquals("A\nB\nC", g1.toString());

        // file with nodes and edges
        g1 = new WeightedGraph();
        fileName = createAdjacencyTextFile("A 1 C\nB 2 A\nC");
        g1.read(fileName);
        deleteFile(fileName);
        Assert.assertEquals("A 1 C\nB 2 A\nC", g1.toString());

        // add to an existing graph
        g1 = new WeightedGraph();
        fileName = createAdjacencyTextFile("A 1 C\nB 2 A\nC");
        g1.read(fileName);
        deleteFile(fileName);
        fileName = createAdjacencyTextFile("B 3 C\nD 4 A");
        g1.read(fileName);
        deleteFile(fileName);
        Assert.assertEquals("A 1 C\nB 2 A 3 C\nC\nD 4 A", g1.toString());
    }

    /**
     * Unit tests for addWeightedEdge
     */
    @Test
    public void testAddWeightedEdge() {
        WeightedGraph g1;

        // add to an empty graph
        g1 = new WeightedGraph();
        Assert.assertFalse(g1.addWeightedEdge("A", "B", 1));
        Assert.assertEquals("", g1.toString());

        // add to a node that does not exist
        g1 = new WeightedGraph();
        g1.addNode("A");
        Assert.assertFalse(g1.addWeightedEdge("A", "B", 1));
        Assert.assertFalse(g1.addWeightedEdge("B", "C", 2));
        Assert.assertEquals("A", g1.toString());

        // add a meaningless edge ("A" -> "A" for example)
        g1 = new WeightedGraph();
        g1.addNode("A");
        Assert.assertFalse(g1.addWeightedEdge("A", "A", 1));
        Assert.assertEquals("A", g1.toString());

        // successfully add an edge
        g1 = new WeightedGraph();
        g1.addNode("A");
        g1.addNode("B");
        Assert.assertTrue(g1.addWeightedEdge("A", "B", 1));
        Assert.assertEquals("A 1 B\nB", g1.toString());

        // add a duplicate edge
        g1 = new WeightedGraph();
        g1.addNode("A");
        g1.addNode("B");
        g1.addWeightedEdge("A", "B", 1);
        Assert.assertFalse(g1.addWeightedEdge("A", "B", 1));
        Assert.assertTrue(g1.addWeightedEdge("B", "A", 2));
        Assert.assertEquals("A 1 B\nB 2 A", g1.toString());
    }

    /**
     * Unit tests for addWeightedEdges
     */
    @Test
    public void testAddWeightedEdges() {
        WeightedGraph g1;

        // empty graph
        g1 = new WeightedGraph();
        Assert.assertFalse(g1.addWeightedEdges("A", new String[]{"B", "C"}, new int[]{1, 2}));
        Assert.assertEquals("", g1.toString());


        // node does not exist
        g1 = new WeightedGraph();
        g1.addNode("C");
        Assert.assertFalse(g1.addWeightedEdges("A", new String[]{"B", "D"}, new int[]{1, 2}));
        Assert.assertEquals("C", g1.toString());

        // some edges are already there
        g1 = new WeightedGraph();
        g1.addNodes(new String[]{"A", "B", "C", "D"});
        g1.addWeightedEdge("A", "C", 1);
        Assert.assertFalse(g1.addWeightedEdges("A", new String[]{"B", "C", "D"}, new int[]{1, 2, 3}));
        Assert.assertEquals("A 1 B 1 C 3 D\nB\nC\nD", g1.toString());

        // all edges are successfully added
        g1 = new WeightedGraph();
        g1.addNodes(new String[]{"A", "B", "C", "D"});
        Assert.assertTrue(g1.addWeightedEdges("A", new String[]{"B", "C", "D"}, new int[]{1, 2, 3}));
        Assert.assertEquals("A 1 B 2 C 3 D\nB\nC\nD", g1.toString());
    }

    /**
     * Unit tests for printWeightedGraph
     */
    @Test
    public void testPrintWeightedGraph() {
        WeightedGraph g1;

        // empty graph
        g1 = new WeightedGraph();
        Assert.assertEquals("", g1.toString());

        // only nodes
        g1 = new WeightedGraph();
        g1.addNodes(new String[]{"A", "B", "C"});
        Assert.assertEquals("A\nB\nC", g1.toString());

        // nodes with edges
        g1 = new WeightedGraph();
        g1.addNodes(new String[]{"A", "B", "C"});
        g1.addWeightedEdges("A", new String[]{"B", "C"}, new int[]{1, 2});
        g1.addWeightedEdge("B", "C", 3);
        Assert.assertEquals("A 1 B 2 C\nB 3 C\nC", g1.toString());
    }

    /**
     * Unit tests for shortedPath
     */
    @Test
    public void testShortestPath() throws Exception {
        WeightedGraph g1;

        // empty graph
        g1 = new WeightedGraph();
        Assert.assertArrayEquals(new String[]{}, g1.shortestPath("A", "B"));

        // just 1 node
        g1 = createFromWeightedAdjacencyFormat("A");
        Assert.assertArrayEquals(new String[]{"A"}, g1.shortestPath("A", "A"));
        Assert.assertArrayEquals(new String[]{}, g1.shortestPath("A", "B"));

        // only 1 path
        g1 = createFromWeightedAdjacencyFormat("A 1 B\nB 1 C\nC 1 D\nD 1 E\nE");
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D", "E"}, g1.shortestPath("A", "E"));
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D"}, g1.shortestPath("A", "D"));
        Assert.assertArrayEquals(new String[]{"A", "B", "C"}, g1.shortestPath("A", "C"));
        Assert.assertArrayEquals(new String[]{"A", "B"}, g1.shortestPath("A", "B"));

        // circular graph
        g1 = createFromWeightedAdjacencyFormat("A 1 B 1 E\nB 1 A 1 C\nC 1 D 1 B\nD 1 E 1 C\nE 1 A 1 D");
        Assert.assertArrayEquals(new String[]{"A", "E"}, g1.shortestPath("A", "E"));
        Assert.assertArrayEquals(new String[]{"A", "E", "D"}, g1.shortestPath("A", "D"));
        Assert.assertArrayEquals(new String[]{"A", "B", "C"}, g1.shortestPath("A", "C"));
        Assert.assertArrayEquals(new String[]{"A", "B"}, g1.shortestPath("A", "B"));

        // fully connected
        g1 = createFromWeightedAdjacencyFormat("A 1 B 2 C\nB 3 A 4 C\nC 5 A 6 B");
        Assert.assertArrayEquals(new String[]{"A", "B"}, g1.shortestPath("A", "B"));
        Assert.assertArrayEquals(new String[]{"A", "C"}, g1.shortestPath("A", "C"));
        Assert.assertArrayEquals(new String[]{"B", "A"}, g1.shortestPath("B", "A"));
        Assert.assertArrayEquals(new String[]{"B", "C"}, g1.shortestPath("B", "C"));
        Assert.assertArrayEquals(new String[]{"C", "A"}, g1.shortestPath("C", "A"));
        Assert.assertArrayEquals(new String[]{"C", "B"}, g1.shortestPath("C", "B"));
        g1 = createFromWeightedAdjacencyFormat("A 1 B 2 C\nB 3 A 4 C\nC 1 A 6 B");
        Assert.assertArrayEquals(new String[]{"C", "A", "B"}, g1.shortestPath("C", "B"));

        // complicated graph
    }

    /**
     * Unit tests for secondShortestPath
     */
    @Test
    public void testSecondShortestPath() throws Exception {
        WeightedGraph g1;

        // empty graph
        g1 = new WeightedGraph();
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "B"));

        // just 1 node
        g1 = createFromWeightedAdjacencyFormat("A");
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "A"));
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "B"));

        // only 1 path
        g1 = createFromWeightedAdjacencyFormat("A 1 B\nB 1 C\nC 1 D\nD 1 E\nE");
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "E"));
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "D"));
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "C"));
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "B"));

        // circular graph
        g1 = createFromWeightedAdjacencyFormat("A 1 B 1 E\nB 1 A 1 C\nC 1 D 1 B\nD 1 E 1 C\nE 1 A 1 D");
        // Assert.assertArrayEquals(new String[]{"A", "B", "C", "D", "E"}, g1.secondShortestPath("A", "E"));
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D"}, g1.secondShortestPath("A", "D"));
        Assert.assertArrayEquals(new String[]{"A", "E", "D", "C"}, g1.secondShortestPath("A", "C"));
        // Assert.assertArrayEquals(new String[]{"A", "E", "D", "C", "B"}, g1.secondShortestPath("A", "B"));

        // fully connected
        g1 = createFromWeightedAdjacencyFormat("A 1 B 2 C\nB 3 A 4 C\nC 5 A 6 B");
        Assert.assertArrayEquals(new String[]{"A", "C", "B"}, g1.secondShortestPath("A", "B"));
        Assert.assertArrayEquals(new String[]{"A", "B", "C"}, g1.secondShortestPath("A", "C"));
        Assert.assertArrayEquals(new String[]{"B", "C", "A"}, g1.secondShortestPath("B", "A"));
        Assert.assertArrayEquals(new String[]{"B", "A", "C"}, g1.secondShortestPath("B", "C"));
        Assert.assertArrayEquals(new String[]{"C", "B", "A"}, g1.secondShortestPath("C", "A"));
        Assert.assertArrayEquals(new String[]{"C", "A", "B"}, g1.secondShortestPath("C", "B"));
        g1 = createFromWeightedAdjacencyFormat("A 1 B 2 C\nB 3 A 4 C\nC 1 A 6 B");
        Assert.assertArrayEquals(new String[]{"C", "B"}, g1.secondShortestPath("C", "B"));

        // complicated graph
        g1 = createFromWeightedAdjacencyFormat("A 5 B 3 C\nB 2 C 1 G 3 E\nC 7 E\nD 6 F 1 A\nE 1 F 2 D\nF\nG 1 E");
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "B"));
        Assert.assertArrayEquals(new String[]{"A", "B", "C"}, g1.secondShortestPath("A", "C"));
        Assert.assertArrayEquals(new String[]{"A", "C", "D"}, g1.secondShortestPath("A", "D"));
        Assert.assertArrayEquals(new String[]{"A", "B", "E"}, g1.secondShortestPath("A", "E"));
        Assert.assertArrayEquals(new String[]{"A", "B", "E", "F"}, g1.secondShortestPath("A", "F"));
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "G"));
    }
}
