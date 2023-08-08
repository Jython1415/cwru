import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Collections;
import java.util.Comparator;

public class WeightedGraph extends Graph {

    @Override
    public boolean addEdge(String from, String to) {
        return addWeightedEdge(from, to, 0);
    }

    @Override
    public boolean addEdges(String from, String[] to) {
        int[] weights = new int[to.length];
        for (int i = 0; i < weights.length; i++)
            weights[i] = 0;

        return addWeightedEdges(from, to, weights);
    }

    @Override
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
            for (int i = 1; i < line.size() - 1; i += 2) {
                addWeightedEdge(line.get(0), line.get(i + 1), Integer.parseInt(line.get(i)));
            }
        }
    }

    /**
     * Adds a weighted edge from node "from" to node "to"
     * @param from the node the edge starts at
     * @param to the node the edge ends at
     * @param weight the weight of the edge
     * @return true is successful, false otherwise
     */
    public boolean addWeightedEdge(String from, String to, int weight) {
        if (!getNodes().containsKey(from) || !getNodes().containsKey(to)) {
            return false;
        }

        if (from.equals(to)) {
            return false;
        }

        if (getNodes().get(from).getEdges().containsKey(Edge.createEdgeString(from, to))) {
            return false;
        }

        getNodes().get(from).getEdges().put(Edge.createEdgeString(from, to), new WeightedEdge(getNodes().get(from),
                                                                                              getNodes().get(to),
                                                                                              weight));
        
        return true;
    }

    /**
     * Adds a list of weighted edges from the "from" node to all the "to" nodes
     * The method will stop at the first unsuccessful addition
     * @param from node all edges start from
     * @param toList the nodes to connect to
     * @param weightList the weight of the edges
     * @return true if successful, false otherwise
     */
    public boolean addWeightedEdges(String from, String[] toList, int[] weightList) {
        boolean result = true;
        for (int i = 0; i < toList.length; i++) {
            if (!addWeightedEdge(from, toList[i], weightList[i])) {
                result = false;
            }
        }

        return result;
    }

    /**
     * Prints the graph is adjacency list format
     * The nodes and their neighbors are listed in alphabetical order
     */
    public String[] printWeightedGraph() {
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
                WeightedEdge e = (WeightedEdge)edge;
                result.append(e.getWeight()).append(" ").append(e.getTo().getName()).append(" ");
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
        for (String line : printWeightedGraph())
            result.append(line).append("\n");
        
        if (result.length() > 0)
            result.replace(result.length() - 1, result.length(), ""); // remove the last "\n"

        return result.toString();
    }

    protected class DijkstraNode {
        private Node node;
        private Node parentNode;
        private int totalCost;

        public DijkstraNode(Node node, Node parentNode, int totalCost) {
            this.node = node;
            this.parentNode = parentNode;
            this.totalCost = totalCost;
        }

        public Node getNode() {
            return this.node;
        }

        public Node getParentNode() {
            return this.parentNode;
        }

        public void setParentNode(Node parentNode) {
            this.parentNode = parentNode;
        }

        public int getTotalCost() {
            return this.totalCost;
        }

        public void setTotalCost(int totalCost) {
            this.totalCost = totalCost;
        }

        protected static class CostComparator implements Comparator<DijkstraNode> {
            @Override
            public int compare(DijkstraNode d1, DijkstraNode d2) {
                return d1.getTotalCost() - d2.getTotalCost();
            }
        }
    }

    /**
     * Finds the shortest path from node "from" to node "to"
     * @param from the start node
     * @param to the end node
     * @return a path from the start node to the end node
     */
    public String[] shortestPath(String from, String to) {

        if (!getNodes().containsKey(from) || !getNodes().containsKey(to)) {
            return new String[]{};
        }

        if (from.equals(to)) {
            return new String[]{from};
        }

        Hashtable<String, DijkstraNode> finalized = new Hashtable<String, DijkstraNode>();

        Hashtable<String, DijkstraNode> processing = new Hashtable<String, DijkstraNode>();

        Node firstNode = getNodes().get(from);
        finalized.put(getNodes().get(from).toString(), new DijkstraNode(firstNode, null, 0));
        processing.put(getNodes().get(from).toString(), new DijkstraNode(firstNode, null, 0));

        for (Edge edge : getNodes().get(from).getEdges().values()) {
            WeightedEdge e = (WeightedEdge)edge;
            Node node = edge.getTo();
            if (!processing.containsKey(node.toString())) {
                processing.put(node.toString(), new DijkstraNode(node, firstNode, e.getWeight()));
            }
        }

        int j = 0;
        while (j++ < 1000 && finalized.size() < getNodes().size()) {

            ArrayList<DijkstraNode> processingList = new ArrayList<DijkstraNode>();
            for (DijkstraNode dNode : processing.values()) {
                if (!finalized.containsKey(dNode.getNode().getName())) {
                    processingList.add(dNode);
                }
            }

            Collections.sort(processingList, new DijkstraNode.CostComparator());

            if (processingList.isEmpty()) {
                if (finalized.size() == processing.size()) {
                    return new String[]{};
                }
            }
            
            DijkstraNode finalizedNode = processingList.get(0);
            finalized.put(finalizedNode.getNode().getName(), finalizedNode);
            if (finalized.containsKey(to)) {

                ArrayList<DijkstraNode> path = new ArrayList<DijkstraNode>();
                DijkstraNode currentNode = finalized.get(to);
                path.add(currentNode);

                int k = 0;
                while (k++ < 1000 && currentNode.getParentNode() != null) {
                    currentNode = finalized.get(currentNode.getParentNode().getName());
                    path.add(currentNode);
                }

                Collections.reverse(path);

                String[] result = new String[path.size()];

                for (int i = 0; i < result.length; i++) {
                    result[i] = path.get(i).getNode().getName();
                }

                return result;
            }

            for (Edge edge : finalizedNode.getNode().getEdges().values()) {
                WeightedEdge wEdge = (WeightedEdge)edge;
                if (!finalized.containsKey(wEdge.getTo().getName())) {
                    if (!processing.containsKey(wEdge.getTo().getName())) {
                        processing.put(wEdge.getTo().getName(), new DijkstraNode(wEdge.getTo(), finalizedNode.getNode(), Integer.valueOf(finalizedNode.getTotalCost() + wEdge.getWeight())));
                    }
                    else {
                        DijkstraNode pNode = processing.get(wEdge.getTo().getName());
                        if (finalizedNode.getTotalCost() + wEdge.getWeight() < pNode.getTotalCost()) {
                            pNode.setTotalCost(finalizedNode.getTotalCost() + wEdge.getWeight());
                            pNode.setParentNode(finalizedNode.getNode());
                        }
                    }
                }
            }
        }

        return new String[]{};
    }

    public String[] shortestPath(String from, String to, String excludeEdge) {

        if (!getNodes().containsKey(from) || !getNodes().containsKey(to)) {
            return new String[]{};
        }

        if (from.equals(to)) {
            return new String[]{from};
        }

        Hashtable<String, DijkstraNode> finalized = new Hashtable<String, DijkstraNode>();

        Hashtable<String, DijkstraNode> processing = new Hashtable<String, DijkstraNode>();

        Node firstNode = getNodes().get(from);
        finalized.put(getNodes().get(from).toString(), new DijkstraNode(firstNode, null, 0));
        processing.put(getNodes().get(from).toString(), new DijkstraNode(firstNode, null, 0));

        for (Edge edge : getNodes().get(from).getEdges().values()) {
            WeightedEdge e = (WeightedEdge)edge;
            Node node = edge.getTo();
            if (!processing.containsKey(node.toString())) {
                processing.put(node.toString(), new DijkstraNode(node, firstNode, e.getWeight()));
            }
        }

        int j = 0;
        while (j++ < 10000 && finalized.size() < getNodes().size()) {

            ArrayList<DijkstraNode> processingList = new ArrayList<DijkstraNode>();
            for (DijkstraNode dNode : processing.values()) {
                if (!finalized.containsKey(dNode.getNode().getName())) {
                    processingList.add(dNode);
                }
            }

            Collections.sort(processingList, new DijkstraNode.CostComparator());

            if (processingList.isEmpty()) {
                if (finalized.size() == processing.size()) {
                    return new String[]{};
                }
            }
            
            DijkstraNode finalizedNode = processingList.get(0);
            finalized.put(finalizedNode.getNode().getName(), finalizedNode);
            if (finalized.containsKey(to)) {

                ArrayList<DijkstraNode> path = new ArrayList<DijkstraNode>();
                DijkstraNode currentNode = finalized.get(to);
                path.add(currentNode);

                int k = 0;
                while (k++ < 10000 && currentNode.getParentNode() != null) {
                    currentNode = finalized.get(currentNode.getParentNode().getName());
                    path.add(currentNode);
                }

                Collections.reverse(path);

                String[] result = new String[path.size()];

                for (int i = 0; i < result.length; i++) {
                    result[i] = path.get(i).getNode().getName();
                }

                return result;
            }

            for (Edge edge : finalizedNode.getNode().getEdges().values()) {
                if (!edge.toString().equals(excludeEdge)) {
                    WeightedEdge wEdge = (WeightedEdge)edge;
                    if (!finalized.containsKey(wEdge.getTo().getName())) {
                        if (!processing.containsKey(wEdge.getTo().getName())) {
                            processing.put(wEdge.getTo().getName(), new DijkstraNode(wEdge.getTo(), finalizedNode.getNode(), Integer.valueOf(finalizedNode.getTotalCost() + wEdge.getWeight())));
                        }
                        else {
                            DijkstraNode pNode = processing.get(wEdge.getTo().getName());
                            if (finalizedNode.getTotalCost() + wEdge.getWeight() < pNode.getTotalCost()) {
                                pNode.setTotalCost(finalizedNode.getTotalCost() + wEdge.getWeight());
                                pNode.setParentNode(finalizedNode.getNode());
                            }
                        }
                    }
                }
            }
        }

        return new String[]{};
    }

    /**
     * Finds the second shortest path from node "from" to node "to"
     * @param from the start node
     * @param to the end node
     * @return a path from the start node to the end node
     */
    @Override
    public String[] secondShortestPath(String from, String to) {
        if (!getNodes().containsKey(from) || !getNodes().containsKey(to)) {
            return new String[]{};
        }

        if (from.equals(to)) {
            return new String[]{};
        }

        // use a modified BFS that excludes 1 edge at a time
        String[] shortestPath = shortestPath(from, to);

        ArrayList<GraphPath> paths = new ArrayList<GraphPath>();

        for (int i = 0; i < shortestPath.length - 1; i++) {
            paths.add(new GraphPath(shortestPath(from, to, Edge.createEdgeString(shortestPath[i], shortestPath[i + 1]))));
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

    protected class WeightedEdge extends Edge {
        private int weight;

        public WeightedEdge(Node from, Node to, int weight) {
            super(from, to);

            this.weight = weight;
        }

        public int getWeight() {
            return this.weight;
        }
    }
}
