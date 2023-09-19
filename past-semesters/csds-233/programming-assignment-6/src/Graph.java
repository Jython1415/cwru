import java.util.Hashtable;
import java.util.Iterator;
import java.util.Comparator;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.List;

public class Graph {
    private Hashtable<String, Node> nodes;

    public Graph() {
        this.nodes = new Hashtable<String, Node>();
    }

    protected Hashtable<String, Node> getNodes() {
        return this.nodes;
    }

    /**
     * Adds a node to the graph and checks for duplicates
     * @param name the name of the node to add
     * @return true if successful, false otherwise
     */
    public boolean addNode(String name) {
        if (getNodes().containsKey(name)) {
            return false;
        }

        getNodes().put(name, new Node(name));

        return true;
    }

    /**
     * Adds a list of nodes to the graph and checks for duplicates
     * The method will stop at the first unsuccessful addition
     * @param names the names of the nodes to add
     * @return true is all are successful, false otherwise
     */
    public boolean addNodes(String[] names) {
        boolean result = true;

        for (String name : names) {
            if (!addNode(name)) {
                result = false;
            }
        }

        return result;
    }

    /**
     * Adds an edge from node "from" to node "to"
     * @param from the node the edge connects from
     * @param to the node the edge connects to
     * @return true if successful, false otherwise
     */
    public boolean addEdge(String from, String to) {
        if (!getNodes().containsKey(from) || !getNodes().containsKey(to)) {
            return false;
        }

        if (from.equals(to)) {
            return false;
        }

        if (getNodes().get(from).getEdges().containsKey(Edge.createEdgeString(from, to))) {
            return false;
        }

        getNodes().get(from).getEdges().put(Edge.createEdgeString(from, to), new Edge(getNodes().get(from),
                                                                                      getNodes().get(to)));
        
        addEdge(to, from);

        return true;
    }

    /**
     * Adds a list of edges from the "from" node to each of the nodes listed in "to"
     * The method will stop at the first unsuccessful addition
     * @param from the node all the edges connect from
     * @param to the nodes to connect to
     * @return true is all are successful, false otherwise
     */
    public boolean addEdges(String from, String[] to) {
        boolean result = true;

        for (String node : to) {
            if (!addEdge(from, node)) {
                result = false;
            }
        }

        return result;
    }

    /**
     * Removes a node from the graph along with all connected edges
     * @param name the node to remove
     * @return true if successful, false otherwise
     */
    public boolean removeNode(String name) {
        if (!getNodes().containsKey(name)) {
            return false;
        }

        Node nodeToRemove = getNodes().get(name);

        Iterator<Edge> i = nodeToRemove.getEdges().values().iterator();

        while (i.hasNext()) {
            Edge edge = i.next();

            Node otherNode = getNodes().get(edge.getTo().toString());

            otherNode.getEdges().remove(Edge.createEdgeString(otherNode, nodeToRemove));
        }

        getNodes().remove(name);

        return true;
    }

    /**
     * Remove a list of nodes and their edges
     * The method will stop at the first unsuccessful removal
     * @param nodeList the list of nodes to remove
     * @return true if successful, false otherwise
     */
    public boolean removeNodes(String[] nodeList) {
        boolean result = true;

        for (String node : nodeList) {
            if (!removeNode(node)) {
                result = false;
            }
        }
        return result;
    }

    /**
     * Prints the graph in the adjacency list format
     * Nodes and their neighbors are listed in alphabetical order
     */
    public String[] printGraph() {
        Node[] nodes = new Node[getNodes().size()];
        getNodes().values().toArray(nodes);
        Arrays.sort(nodes, new Node.AlphabeticalComparator());
        
        ArrayList<String> results = new ArrayList<String>();

        for (Node node : nodes) {
            StringBuilder result = new StringBuilder(node.toString());
            result.append(" ");

            Edge[] edges = new Edge[node.getEdges().size()];
            node.getEdges().values().toArray(edges);
            Arrays.sort(edges, new Edge.AlphabeticalByToNodeComparator());

            for (Edge edge : edges) {
                result.append(edge.getTo().getName()).append(" ");
            }

            if (result.length() > 0)
                result.replace(result.length() - 1, result.length(), ""); // remove the last space  
            
            results.add(result.toString());
        }

        String[] resultsArray = new String[results.size()];
        results.toArray(resultsArray);

        return resultsArray;
    }

    /**
     * Returns a String representation of the graph in the adjacency list style
     * Sorted alphabetically
     * @return a String representation of the graph
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        /* add all the lines */
        for (String line : printGraph())
            result.append(line).append("\n");
        
        if (result.length() > 0)
            result.replace(result.length() - 1, result.length(), ""); // remove the last "\n"

        return result.toString();
    }

    /**
     * Read an graph in adjacency list format from a txt file and import that into the graph
     * @param filename the path of the file to add
     */
    public void read(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));

        ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();

        while(scanner.hasNextLine()) {
            lines.add(new ArrayList<String>(Arrays.asList(scanner.nextLine().split(" "))));
        }
        
        for (ArrayList<String> line : lines) {
            addNode(line.get(0));
        }

        for (ArrayList<String> line : lines) {
            for (int i = 1; i < line.size(); i++) {
                addEdge(line.get(0), line.get(i));
            }
        }
    }

    /**
     * Returns the path from node "from" to node "to" using DFS.
     * "neighborOrder" determines the order to consider equivalent choices: "alphabetical" or "reverse"
     * The method will return an empty array is no path exists
     * @param from the node to start at
     * @param to the node to reach
     * @param neighborOrder The order in which to consider neighbors: "alphabetical" or "reverse"
     * @return a path from the starting node to the end node
     */
    public String[] DFS(String from, String to, String neighborOrder) {
        if (!getNodes().containsKey(from) || !getNodes().containsKey(to)) {
            return new String[]{};
        }
        
        ArrayDeque<Node> stack = new ArrayDeque<Node>();
        stack.addFirst(getNodes().get(from));

        Hashtable<String, Node> visitedNodes = new Hashtable<String, Node>();

        Comparator<Node> comparator = new Node.AlphabeticalComparator();
        if (neighborOrder.equals("reverse")) {
            comparator = new Node.ReverseAlphabeticalComparator();
        }

        while (!stack.isEmpty()) {
            // check the current node
            Node currentNode = stack.getFirst();
            if (currentNode.getName().equals(to)) {

                Node[] nodeArray = new Node[stack.size()];
                List<Node> list = Arrays.asList(stack.toArray(nodeArray));
                Collections.reverse(list);

                String[] result = new String[list.size()];

                for (int i = 0; i < result.length; i++) {
                    result[i] = list.get(i).toString();
                }

                return result;
            }
            visitedNodes.put(currentNode.toString(), currentNode);

            // add unvisited edges to a list
            ArrayList<Node> unvisitedNeighbors = new ArrayList<Node>();

            for (Edge edge : currentNode.getEdges().values()) {
                if (!visitedNodes.containsKey(edge.getTo().toString())) {
                    unvisitedNeighbors.add(edge.getTo());
                }
            }

            // sort the list
            Collections.sort(unvisitedNeighbors, comparator);

            // check if this is a dead end
            if (unvisitedNeighbors.size() == 0) {
                stack.removeFirst();
            }
            else {
                stack.addFirst(unvisitedNeighbors.get(0));
            }
        }

        return new String[]{};
    }

    /**
     * Returns the path from node "from" to node "to" using BFS.
     * "neighborOrder" determines the order to consider equivalent choices: "alphabetical" or "reverse"
     * The method will return an empty array is no path exists
     * @param from the node to start at
     * @param to the node to reach
     * @param neighborOrder The order in which to consider neighbors: "alphabetical" or "reverse"
     * @return a path from the starting node to the end node
     */
    public String[] BFS(String from, String to, String neighborOrder) {
        if (!getNodes().containsKey(from) || !getNodes().containsKey(to)) {
            return new String[]{};
        }

        ArrayDeque<ArrayDeque<Node>> queue = new ArrayDeque<ArrayDeque<Node>>();
        queue.addLast(new ArrayDeque<Node>());
        queue.getFirst().addFirst(getNodes().get(from));

        Hashtable<String, Node> visitedNodes = new Hashtable<String, Node>();

        Comparator<Node> comparator = new Node.AlphabeticalComparator();
        if (neighborOrder.equals("reverse")) {
            comparator = new Node.ReverseAlphabeticalComparator();
        }

        while (!queue.isEmpty()) {
            // check the current node
            ArrayDeque<Node> currentStack = queue.getFirst();
            Node currentNode = currentStack.getFirst();
            if (currentNode.getName().equals(to)) {
                Node[] nodeArray = new Node[currentStack.size()];
                List<Node> list = Arrays.asList(currentStack.toArray(nodeArray));
                Collections.reverse(list);

                String[] result = new String[list.size()];
                
                for (int i = 0; i < result.length; i++) {
                    result[i] = list.get(i).toString();
                }

                return result;
            }
            visitedNodes.put(currentNode.toString(), currentNode);

            // add unvisited edges to a list
            ArrayList<Node> unvisitedNeighbors = new ArrayList<Node>();
            for (Edge edge : currentNode.getEdges().values()) {
                if (!visitedNodes.containsKey(edge.getTo().toString())) {
                    unvisitedNeighbors.add(edge.getTo());
                }
            }

            // sort the list
            Collections.sort(unvisitedNeighbors, comparator);

            // check if this is a dead end
            if (unvisitedNeighbors.isEmpty()) {
                queue.removeFirst();
            }
            else {
                for (Node node : unvisitedNeighbors) {
                    ArrayDeque<Node> newStack = currentStack.clone();
                    newStack.addFirst(node);

                    queue.addLast(newStack);
                }
                queue.removeFirst();
            }
        }
        
        return new String[]{};
    }

    /**
     * Returns the path from node "from" to node "to" using BFS.
     * "neighborOrder" determines the order to consider equivalent choices: "alphabetical" or "reverse"
     * The method will return an empty array is no path exists
     * @param from the node to start at
     * @param to the node to reach
     * @param neighborOrder The order in which to consider neighbors: "alphabetical" or "reverse"
     * @return a path from the starting node to the end node
     */
    public String[] BFS(String from, String to, String neighborOrder, String edgeToExclude) {
        if (!getNodes().containsKey(from) || !getNodes().containsKey(to)) {
            return new String[]{};
        }

        ArrayDeque<ArrayDeque<Node>> queue = new ArrayDeque<ArrayDeque<Node>>();
        queue.addLast(new ArrayDeque<Node>());
        queue.getFirst().addFirst(getNodes().get(from));

        Hashtable<String, Node> visitedNodes = new Hashtable<String, Node>();

        Comparator<Node> comparator = new Node.AlphabeticalComparator();
        if (neighborOrder.equals("reverse")) {
            comparator = new Node.ReverseAlphabeticalComparator();
        }

        while (!queue.isEmpty()) {
            // check the current node
            ArrayDeque<Node> currentStack = queue.getFirst();
            Node currentNode = currentStack.getFirst();
            if (currentNode.getName().equals(to)) {
                Node[] nodeArray = new Node[currentStack.size()];
                List<Node> list = Arrays.asList(currentStack.toArray(nodeArray));
                Collections.reverse(list);

                String[] result = new String[list.size()];
                
                for (int i = 0; i < result.length; i++) {
                    result[i] = list.get(i).toString();
                }

                return result;
            }
            visitedNodes.put(currentNode.toString(), currentNode);

            // add unvisited edges to a list
            ArrayList<Node> unvisitedNeighbors = new ArrayList<Node>();
            for (Edge edge : currentNode.getEdges().values()) {
                if (!visitedNodes.containsKey(edge.getTo().toString()) && !edge.toString().equals(edgeToExclude)) {
                    unvisitedNeighbors.add(edge.getTo());
                }
            }

            // sort the list
            Collections.sort(unvisitedNeighbors, comparator);

            // check if this is a dead end
            if (unvisitedNeighbors.isEmpty()) {
                queue.removeFirst();
            }
            else {
                for (Node node : unvisitedNeighbors) {
                    ArrayDeque<Node> newStack = currentStack.clone();
                    newStack.addFirst(node);

                    queue.addLast(newStack);
                }
                queue.removeFirst();
            }
        }
        
        return new String[]{};
    }

    protected class GraphPath {
        private String[] path;
        private int length;

        public GraphPath(String[] path) {
            this.path = path;
            this.length = path.length;
        }

        public int length() {
            return this.length;
        }

        public String[] getPath() {
            return this.path;
        }

        public static class LengthComparator implements Comparator<GraphPath> {
            public int compare(GraphPath p1, GraphPath p2) {
                return p1.length() - p2.length();
            }
        }
    }

    /**
     * Finds the second shortest path between nodes "from" and "to".
     * @param from the start node
     * @param to the end node
     * @return a path from the starting node to the end node
     */
    public String[] secondShortestPath(String from, String to) {
        // use a modified BFS that excludes 1 edge at a time
        String[] shortestPath = BFS(from, to, "alphabetical");

        ArrayList<GraphPath> paths = new ArrayList<GraphPath>();

        for (int i = 0; i < shortestPath.length - 1; i++) {
            paths.add(new GraphPath(BFS(from, to, "alphabetical", Edge.createEdgeString(shortestPath[i], shortestPath[i + 1]))));
        }

        Collections.sort(paths, new GraphPath.LengthComparator());

        while (!paths.isEmpty() &&
               (paths.get(0).length() == 0 ||
                paths.get(0).length() == shortestPath.length)) {
            paths.remove(0);
        }

        if (!paths.isEmpty()) {
            return paths.get(0).getPath();
        }

        return new String[]{};
    }

    protected class Node {
        /* stores the name of the node */
        private String name;

        /* stores all edges connected to the node */
        private Hashtable<String, Edge> edges;

        public Node(String name) {
            this.edges = new Hashtable<String, Edge>();

            this.name = name;
        }

        /**
         * Getter method for the name
         * @return the node of the Node
         */
        public String getName() {
            return this.name;
        }

        /**
         * Getter method for the Hashtable of edges
         * @return the Hashtable of edges
         */
        public Hashtable<String, Edge> getEdges() {
            return this.edges;
        }

        public void addEdge(Edge edge) {
            getEdges().put(edge.toString(), edge);
        }

        public void addEdge(Node from, Node to) {
            getEdges().put(Edge.createEdgeString(from, to), new Edge(from, to));
        }

        /**
         * Returns a String representation of the Node (its name)
         * @return a String representation of the Node
         */
        @Override
        public String toString() {
            return getName();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Graph)) {
                return false;
            }

            Node other = (Node)o;
            
            return this.getName().equals(other.getName());
        }

        protected static class AlphabeticalComparator implements Comparator<Node> {
            @Override
            public int compare(Node n1, Node n2) {
                if (n1.getName().equals(n2.getName())) {
                    return 0;
                }

                return n1.getName().compareTo(n2.getName());
            }
        }

        protected static class ReverseAlphabeticalComparator implements Comparator<Node> {
            @Override
            public int compare(Node n1, Node n2) {
                if (n1.getName().equals(n2.getName())) {
                    return 0;
                }

                return -1 * n1.getName().compareTo(n2.getName());
            }
        }
    }

    protected class Edge {
        /* stores a reference to the Node the edge points from */
        private Node from;

        /* stores a reference to the Node the edge points to */
        private Node to;

        /**
         * Constructor for Edge that takes Nodes for the the two ends
         * @param from the Node the edge points from
         * @param to the Node the edge points to
         */
        public Edge(Node from, Node to) {
            this.from = from;
            this.to = to;
        }

        /**
         * Getter method for the from node
         * @return the "from" Node
         */
        public Node getFrom() {
            return this.from;
        }

        /**
         * Getter method for the to node
         * @return the "to" Node
         */
        public Node getTo() {
            return this.to;
        }

        /**
         * Returns a String representation of the Edge
         * @return a String representation of the Edge
         */
        @Override
        public String toString() {
            return getFrom().toString() + " " + getTo().toString();
        }

        /**
         * Returns the String representation of the edge that would go from the from Node to the to Node
         * @param from the Node the edge would be from
         * @param to the Node the edge would go to
         * @return a String representation of a hypothetical edge
         */
        public static String createEdgeString(Node from, Node to) {
            return from.toString() + " " + to.toString();
        }

        /**
         * Returns the String representation of the edge that would go from the from Node to the to Node
         * @param from the Node the edge would be from
         * @param to the Node the edge would go to
         * @return a String representation of a hypothetical edge
         */
        public static String createEdgeString(String from, String to) {
            return from + " " + to;
        }

        protected static class AlphabeticalByToNodeComparator implements Comparator<Edge> {
            @Override
            public int compare(Edge e1, Edge e2) {
                if (e1.getTo().getName().equals(e2.getTo().getName())) {
                    return 0;
                }

                return e1.getTo().getName().compareTo(e2.getTo().getName());
            }
        }
    }
}
