import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.io.FileWriter;

public class GraphTester {
    
    /**
     * Unit tests for addNode
     */
    @Test
    public void testAddNode() {
        Graph g1;

        // add to an empty graph
        g1 = new Graph();
        Assert.assertTrue(g1.addNode("A"));
        Assert.assertEquals("A", g1.toString());

        // add to a graph with nodes but no duplicates
        g1 = new Graph();
        g1.addNode("B");
        g1.addNode("C");
        Assert.assertTrue(g1.addNode("A"));
        Assert.assertEquals("A\nB\nC", g1.toString());

        // add to a graph and has a duplicate
        g1 = new Graph();
        g1.addNode("A");
        g1.addNode("B");
        g1.addNode("C");
        Assert.assertFalse(g1.addNode("A"));
        Assert.assertEquals("A\nB\nC", g1.toString());
    }

    /**
     * Unit tests for addNodes
     */
    @Test
    public void testAddNodes() {
        Graph g1;

        // add to an empty graph
        g1 = new Graph();
        Assert.assertTrue(g1.addNodes(new String[]{"A", "B", "C"}));
        Assert.assertEquals("A\nB\nC", g1.toString());

        // add to a graph with some duplicates
        g1 = new Graph();
        g1.addNodes(new String[]{"B", "C"});
        Assert.assertFalse(g1.addNodes(new String[]{"A", "B", "D"}));
        Assert.assertEquals("A\nB\nC\nD", g1.toString());

        // add to a graph with a duplicate with the first name
        g1 = new Graph();
        g1.addNodes(new String[]{"A", "B", "C"});
        Assert.assertFalse(g1.addNodes(new String[]{"A", "D"}));
        Assert.assertEquals("A\nB\nC\nD", g1.toString());
    }

    /**
     * Unit tests for addEdge
     */
    @Test
    public void testAddEdge() {
        Graph g1;

        // add to an empty graph
        g1 = new Graph();
        Assert.assertFalse(g1.addEdge("A", "B"));
        Assert.assertEquals("", g1.toString());

        // add to a node that does not exist
        g1 = new Graph();
        g1.addNode("A");
        Assert.assertFalse(g1.addEdge("A", "B"));
        Assert.assertFalse(g1.addEdge("B", "C"));
        Assert.assertEquals("A", g1.toString());

        // add a meaningless edge ("A" -> "A" for example)
        g1 = new Graph();
        g1.addNode("A");
        Assert.assertFalse(g1.addEdge("A", "A"));
        Assert.assertEquals("A", g1.toString());

        // successfully add an edge
        g1 = new Graph();
        g1.addNode("A");
        g1.addNode("B");
        Assert.assertTrue(g1.addEdge("A", "B"));
        Assert.assertEquals("A B\nB A", g1.toString());

        // add a duplicate edge
        g1 = new Graph();
        g1.addNode("A");
        g1.addNode("B");
        g1.addEdge("A", "B");
        Assert.assertFalse(g1.addEdge("A", "B"));
        Assert.assertFalse(g1.addEdge("B", "A"));
        Assert.assertEquals("A B\nB A", g1.toString());
    }

    /**
     * Unit tests for addEdges
     */
    @Test
    public void testAddEdges() {
        Graph g1;

        // empty graph
        g1 = new Graph();
        Assert.assertFalse(g1.addEdges("A", new String[]{"B", "C"}));
        Assert.assertEquals("", g1.toString());


        // node does not exist
        g1 = new Graph();
        g1.addNode("C");
        Assert.assertFalse(g1.addEdges("A", new String[]{"B", "D"}));
        Assert.assertEquals("C", g1.toString());

        // some edges are already there
        g1 = new Graph();
        g1.addNodes(new String[]{"A", "B", "C", "D"});
        g1.addEdge("A", "C");
        Assert.assertFalse(g1.addEdges("A", new String[]{"B", "C", "D"}));
        Assert.assertEquals("A B C D\nB A\nC A\nD A", g1.toString());

        // all edges are successfully added
        g1 = new Graph();
        g1.addNodes(new String[]{"A", "B", "C", "D"});
        Assert.assertTrue(g1.addEdges("A", new String[]{"B", "C", "D"}));
        Assert.assertEquals("A B C D\nB A\nC A\nD A", g1.toString());
    }

    /**
     * Unit tests for removeNode
     */
    @Test
    public void testRemoveNode() {
        Graph g1;

        // empty graph
        g1 = new Graph();
        Assert.assertFalse(g1.removeNode("A"));

        // node does not exist
        g1 = new Graph();
        g1.addNode("B");
        Assert.assertFalse(g1.removeNode("A"));
        Assert.assertEquals("B", g1.toString());

        // remove an unlinked node
        g1 = new Graph();
        g1.addNodes(new String[]{"A", "B", "C"});
        Assert.assertTrue(g1.removeNode("B"));
        Assert.assertEquals("A\nC", g1.toString());

        // removed a node with edges
        g1 = new Graph();
        g1.addNodes(new String[]{"A", "B", "C"});
        g1.addEdges("A", new String[]{"B", "C"});
        g1.addEdge("B", "C");
        Assert.assertEquals("A B C\nB A C\nC A B", g1.toString());
        Assert.assertTrue(g1.removeNode("A"));
        Assert.assertEquals("B C\nC B", g1.toString());
    }

    /**
     * Unit tests for removeNodes
     */
    @Test
    public void testRemoveNodes() {
        Graph g1;

        // empty graph
        g1 = new Graph();
        Assert.assertFalse(g1.removeNodes(new String[]{"A", "B"}));

        // nodes do not exist
        g1 = new Graph();
        g1.addNodes(new String[]{"D", "E", "F"});
        Assert.assertFalse(g1.removeNodes(new String[]{"A", "B"}));

        // some nodes do not exist
        g1 = new Graph();
        g1.addNodes(new String[]{"A", "B", "C"});
        Assert.assertFalse(g1.removeNodes(new String[]{"A", "D", "B"}));
        Assert.assertEquals("C", g1.toString());

        // all are removed successfully
        g1 = new Graph();
        g1.addNodes(new String[]{"A", "B", "C"});
        Assert.assertTrue(g1.removeNodes(new String[]{"B", "C"}));
        Assert.assertEquals("A", g1.toString());
    }

    /**
     * Unit tests for printGraph
     */
    @Test
    public void testPrintGraph() {
        Graph g1;

        // empty graph
        g1 = new Graph();
        Assert.assertEquals("", g1.toString());

        // only nodes
        g1 = new Graph();
        g1.addNodes(new String[]{"A", "B", "C"});
        Assert.assertEquals("A\nB\nC", g1.toString());

        // nodes with edges
        g1 = new Graph();
        g1.addNodes(new String[]{"A", "B", "C"});
        g1.addEdges("A", new String[]{"B", "C"});
        g1.addEdge("B", "C");
        Assert.assertEquals("A B C\nB A C\nC A B", g1.toString());
    }

    protected static String createAdjacencyTextFile(String input) throws Exception {
        StringBuilder fileName = new StringBuilder("input-graph");

        for (char c : input.toCharArray()) {
            if (c != ' ') {
                fileName.append(c);
            }
        }

        fileName.append(".txt");
        
        File newFile = new File(fileName.toString());
        newFile.createNewFile();

        FileWriter editFile = new FileWriter(fileName.toString());
        editFile.write(input);
        editFile.close();

        return fileName.toString();
    }

    protected static void deleteFile(String fileName) {
        File fileToDelete = new File(fileName);
        fileToDelete.delete();
    }

    /**
     * Unit tests for read
     */
    @Test
    public void testRead() throws Exception {
        Graph g1;
        String fileName;

        // empty file
        g1 = new Graph();
        fileName = createAdjacencyTextFile("");
        g1.read(fileName);
        deleteFile(fileName);
        Assert.assertEquals("", g1.toString());

        // file with just nodes
        g1 = new Graph();
        fileName = createAdjacencyTextFile("A\nB\nC");
        g1.read(fileName);
        deleteFile(fileName);
        Assert.assertEquals("A\nB\nC", g1.toString());

        // file with nodes and edges
        g1 = new Graph();
        fileName = createAdjacencyTextFile("A C\nB A\nC");
        g1.read(fileName);
        deleteFile(fileName);
        Assert.assertEquals("A B C\nB A\nC A", g1.toString());

        // add to an existing graph
        g1 = new Graph();
        fileName = createAdjacencyTextFile("A C\nB A\nC");
        g1.read(fileName);
        deleteFile(fileName);
        fileName = createAdjacencyTextFile("B C\nD A");
        g1.read(fileName);
        deleteFile(fileName);
        Assert.assertEquals("A B C D\nB A C\nC A B\nD A", g1.toString());
    }

    protected static Graph createFromAdjacencyFormat(String input) throws Exception{
        Graph g1 = new Graph();
        String fileName = createAdjacencyTextFile(input);
        g1.read(fileName);
        deleteFile(fileName);

        return g1;
    }

    /**
     * Unit tests for DFS
     */ 
    @Test
    public void testDFS() throws Exception {
        Graph g1;

        // empty graph
        g1 = new Graph();
        Assert.assertArrayEquals(new String[]{}, g1.DFS("A", "B", "alphabetical"));

        // linear graph
        g1 = createFromAdjacencyFormat("A B\nB C\nC D\nD E\nE");
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D", "E"}, g1.DFS("A", "E", "alphabetical"));

        // two branches
        g1 = createFromAdjacencyFormat("A B\nB C F\nC D\nD E\nE\nF G\nG H\nH");
        Assert.assertArrayEquals(new String[]{"A", "B", "F", "G", "H"}, g1.DFS("A", "H", "alphabetical"));

        // circular graph
        g1 = createFromAdjacencyFormat("A B\nB C\nC D\nD E\nE A");
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D", "E"}, g1.DFS("A", "E", "alphabetical"));

        // fully connected graph
        g1 = createFromAdjacencyFormat("A B C D\nB C D\nC D\nD");
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D"}, g1.DFS("A", "D", "alphabetical"));        

        // demonstration of two orders
        g1 = createFromAdjacencyFormat("A B C D\nB C D\nC D\nD");
        Assert.assertArrayEquals(new String[]{"A", "D"}, g1.DFS("A", "D", "reverse")); 
        g1 = createFromAdjacencyFormat("A B\nB C\nC D\nD E\nE A");
        Assert.assertArrayEquals(new String[]{"A", "E"}, g1.DFS("A", "E", "reverse"));   

        // no connection
        g1 = createFromAdjacencyFormat("A\nB");
        Assert.assertArrayEquals(new String[]{}, g1.DFS("A", "B", "alphabetical"));  

        // node A to node A
        g1 = createFromAdjacencyFormat("A");
        Assert.assertArrayEquals(new String[]{"A"}, g1.DFS("A", "A", "alphabetical"));
    }

    /**
     * Unit tests for BFS
     */
    @Test
    public void testBFS() throws Exception {
        Graph g1;

        // empty graph
        g1 = new Graph();
        Assert.assertArrayEquals(new String[]{}, g1.BFS("A", "B", "alphabetical"));

        // linear graph
        g1 = createFromAdjacencyFormat("A B\nB C\nC D\nD E\nE");
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D", "E"}, g1.BFS("A", "E", "alphabetical"));

        // two branches
        g1 = createFromAdjacencyFormat("A B\nB C F\nC D\nD E\nE\nF G\nG H\nH");
        Assert.assertArrayEquals(new String[]{"A", "B", "F", "G", "H"}, g1.BFS("A", "H", "alphabetical"));

        // circular graph
        g1 = createFromAdjacencyFormat("A B\nB C\nC D\nD E\nE A");
        Assert.assertArrayEquals(new String[]{"A", "E"}, g1.BFS("A", "E", "alphabetical"));

        // fully connected graph
        g1 = createFromAdjacencyFormat("A B C D\nB C D\nC D\nD");
        Assert.assertArrayEquals(new String[]{"A", "D"}, g1.BFS("A", "D", "alphabetical"));

        // demonstration of two orders
        g1 = createFromAdjacencyFormat("A B\nB C\nC D\nD E\nE F\nF A");
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D"}, g1.BFS("A", "D", "alphabetical"));
        Assert.assertArrayEquals(new String[]{"A", "F", "E", "D"}, g1.BFS("A", "D", "reverse"));
        
        // node A to node A
        g1 = createFromAdjacencyFormat("A");
        Assert.assertArrayEquals(new String[]{"A"}, g1.BFS("A", "A", "alphabetical"));
    }

    /**
     * Unit tests for secondShortestPath
     */
    @Test
    public void testSecondShortestPath() throws Exception {
        Graph g1;

        // empty graph
        g1 = new Graph();
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "B"));

        // just 1 node
        g1 = createFromAdjacencyFormat("A");
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "A"));
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "B"));

        // only 1 path
        g1 = createFromAdjacencyFormat("A B\nB C\nC D\nD E\nE");
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "E"));
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "D"));
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "C"));
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "B"));

        // circular graph
        g1 = createFromAdjacencyFormat("A B\nB C\nC D\nD E\nE A");
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D", "E"}, g1.secondShortestPath("A", "E"));
        Assert.assertArrayEquals(new String[]{"A", "B", "C", "D"}, g1.secondShortestPath("A", "D"));
        Assert.assertArrayEquals(new String[]{"A", "E", "D", "C"}, g1.secondShortestPath("A", "C"));
        Assert.assertArrayEquals(new String[]{"A", "E", "D", "C", "B"}, g1.secondShortestPath("A", "B"));

        // no second shortest path
        g1 = createFromAdjacencyFormat("A B C\nB\nC\nD B C");
        Assert.assertArrayEquals(new String[]{}, g1.secondShortestPath("A", "D"));

        // complicated graph
        g1 = createFromAdjacencyFormat("A D E\nD B\nB E\nE C F\nC F\nF");
        Assert.assertArrayEquals(new String[]{"A", "E", "C", "F"}, g1.secondShortestPath("A", "F"));
    }
}
