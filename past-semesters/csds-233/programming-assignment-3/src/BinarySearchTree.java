import java.lang.Comparable;
import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * This class represents a tree of sorted information. Each bit of stored data has a "key" associated with it that determines how it should be sorted.
 * All methods are efficient: O(N)
 * @author Joshua Shew
 */
public class BinarySearchTree<T extends Comparable<? super T>, V> {
    /* stores a reference to the root node in the tree */
    private BinaryNode root;

    /**
     * Getter method for the root
     * @return the root of the tree
     */
    private BinaryNode getRoot() {
        return this.root;
    }

    /**
     * Setter method for the root
     * @param root the new root node
     */
    private void setRoot(BinaryNode root) {
        this.root = root;
    }

    /**
     * This method inserts the value into the tree based on the associated key
     * @param key the key determines how the value should be sorted within the tree
     * @param value the value to be stored in the tree
     */
    public void insert(T key, V value) {
        BinaryNode parent = findPreNode(key); // find 1 node above where the new value should be inserted
        if (parent == null) { // this means the tree is empty
            setRoot(new BinaryNode(key, value));
        }
        else if (key.compareTo(parent.getKey()) < 0) { // insert on the left side
            parent.setLeft(new BinaryNode(key, value, parent));
        }
        else { // insert on the right side
            parent.setRight(new BinaryNode(key, value, parent));
        }
    }

    /**
     * This method finds a value in the tree based on its associated key
     * If there are duplicate keys, the value that is associated with the key that is found first will be returned (no guaranteed pattern)
     * @param key the key to search for in the tree
     * @throws NoSuchElementException if a node with the key cannot be found
     * @return the value that is associated with the key
     */
    public V search(T key) throws NoSuchElementException {
        return searchTree(key, getRoot()).getValue();
    }

    private BinaryNode searchTree(T key, BinaryNode currentNode) throws NoSuchElementException {
        if (currentNode == null) { // if the node is not found
            throw new NoSuchElementException();
        }
        else if (key.compareTo(currentNode.getKey()) == 0) { // if the node is found
            return currentNode;
        }
        else { // searching for the node
            return searchTree(key, (key.compareTo(currentNode.getKey()) < 0) ?
                                   currentNode.getLeft() : currentNode.getRight());
        }
    }

    /**
     * This method removes the value associated with the input key
     * If the key is not found, then no value is removed
     * @param key the key associated with the value that should be removed
     */
    public void delete(T key) {
        delete(key, getRoot());
    }

    private void delete(T key, BinaryNode currentNode) {
        BinaryNode node = findNode(key, currentNode);
        if (node != null) { // if node is null, then the key was not found
            if (node.getLeft() != null && node.getRight() != null) { // two children
                node.reassignData(findMin(node.getRight())); // move a suitable node up to replace this one
                delete(node.getKey(), node.getRight()); // delete the node that was moved up
            }
            else if (node.getLeft() != null) { // replace with left child
                BinaryNode parentNode = node.getParent();
                node = node.getLeft();
                node.setParent(parentNode);
                if (parentNode == null) {
                    setRoot(node);
                }
                else {
                    if (key.compareTo(node.getParent().getKey()) < 0) {
                        node.getParent().setLeft(node);
                    }
                    else {
                        node.getParent().setRight(node);
                    }
                }
            }
            else if (node.getRight() != null) { // replace with right child
                BinaryNode parentNode = node.getParent();
                node = node.getRight();
                node.setParent(parentNode);
                if (parentNode == null) {
                    setRoot(node);
                }
                else {
                    if (key.compareTo(node.getParent().getKey()) < 0) {
                        node.getParent().setLeft(node);
                    }
                    else {
                        node.getParent().setRight(node);
                    }
                }
            }
            else { // remove a node with no children
                if (node.getParent() == null) {
                    setRoot(null);
                }
                else if (key.compareTo(node.getParent().getKey()) < 0) {
                    node.getParent().setLeft(null);
                }
                else {
                    node.getParent().setRight(null);
                }
            }
        }
    }

    /**
     * This method returns list with all the values in the tree in ascending order (based on their "key")
     * @return a list with all the values in the tree in ascending order
     */
    public List<V> inorderRec() {
        List<V> list = new LinkedList<V>(); // the list to add all the values to

        inorderRec(list, getRoot());

        return list;
    }

    private void inorderRec(List<V> list, BinaryNode currentNode) {
        if (currentNode != null) {
            inorderRec(list, currentNode.getLeft());
            list.add(currentNode.getValue());
            inorderRec(list, currentNode.getRight());
        }
    }

    /**
     * This method returns the value associated with the kth smallest key
     * @param k which value to return
     * @throws NoSuchElementException if k is larger than the number of values stored in the tree
     * @return the value associated with the kth smallest key
     */
    public V kthSmallest(int k) throws NoSuchElementException {
        if (k < 0) {
            throw new NoSuchElementException();
        }
        
        /* stores the number of values that have been check *outside* of the stack */
        Container<Integer> count = new Container<Integer>(1);

        /* stores the final result of the search */
        Container<V> result = new Container<V>();

        kthSmallest(k, count, result, getRoot());

        if (!result.hasValue()) { // k > # of values
            throw new NoSuchElementException();
        }
        else {
            return result.getValue();
        }
    }

    /**
     * Helper method for kthSmallest
     * @param k which value to return
     * @param count how many minimum values have been searched for
     * @param result the container to store the final result in
     * @param currentNode the current node to search from
     */
    private void kthSmallest(int k, Container<Integer> count, Container<V> result, BinaryNode currentNode) {
        if (currentNode != null) {
            if (currentNode.getLeft() != null) { // not at the minimum value
                kthSmallest(k, count, result, currentNode.getLeft());
            }
            if (!result.hasValue()) { // makes sure not to override a round value
                if (k > count.getValue()) {
                    count.setValue(count.getValue() + 1); // increment to indicate another value has been check
                    kthSmallest(k, count, result, currentNode.getRight());
                }
                else {
                    result.setValue(currentNode.getValue());
                }
            }
            
        }
    }

    /**
     * Returns a String of the tree traversed using pre-order traversal
     * @return a string representation of the tree
     */
    private String preOrderToString() {
        StringBuilder string = new StringBuilder();

        preOrderToStringTree(string, getRoot());

        return string.toString();
    }

    /**
     * Helper method for preOrderToString
     * @param string the string to build on
     * @param currentNode the current location in the tree
     */
    private void preOrderToStringTree(StringBuilder string, BinaryNode currentNode) {
        if (currentNode != null) {
            string.append(currentNode.getValue());
            if (currentNode.getLeft() != null || currentNode.getRight() != null) {
                string.append(" (");
                preOrderToStringTree(string, currentNode.getLeft());
                string.append(", ");
                preOrderToStringTree(string, currentNode.getRight());
                string.append(")");
            }
        }
    }

    /**
     * Finding the node directly above where a node with the input key would go
     * In the case of duplicates, it finds the lowest position that fulfills the above criteria
     * @param key the key to be inserted into the tree
     * @return the node above the key's correct position
     */
    private BinaryNode findPreNode(T key) {
        return findPreNode(key, getRoot());
    }

    /**
     * Helper method for findPreNode
     * @param key the key to search for
     * @param currentNode the current position in the tree
     * @return the node above the key
     */
    private BinaryNode findPreNode(T key, BinaryNode currentNode) {
        if (currentNode == null) { // when the key cannot be found
            return null;
        }
        else if (currentNode.getKey().compareTo(key) == 0) { // if the node is found
            return (currentNode.getRight() != null && key.compareTo(currentNode.getRight().getKey()) == 0) ? 
                   findPreNode(key, currentNode.getRight()) : currentNode;
        }
        else if (key.compareTo(currentNode.getKey()) < 0) { // go left
            return (currentNode.getLeft() == null) ? currentNode : findPreNode(key, currentNode.getLeft());
        }
        else { // go right
            return (currentNode.getRight() == null) ? currentNode : findPreNode(key, currentNode.getRight());
        }
    }

    /**
     * Find the minimum node in a subtree
     * @param currentNode the node to search at
     * @return the minimum node in the subtree
     */
    private BinaryNode findMin(BinaryNode currentNode) {
        if (currentNode == null) {
            return null;
        }
        else if (currentNode.getLeft() == null) {
            return currentNode;
        }
        else {
            return findMin(currentNode.getLeft());
        }
    }

    /**
     * Finds a node based on the key
     * @param key the key to search for
     * @param currentNode the node to search from
     */
    private BinaryNode findNode(T key, BinaryNode currentNode) {
        if (currentNode == null) {
            return null;
        }
        if (key.compareTo(currentNode.getKey()) == 0) {
            return currentNode;
        }
        else {
            return findNode(key,
                            (key.compareTo(currentNode.getKey()) < 0) ? currentNode.getLeft() : currentNode.getRight());
        }
    }

    /**
     * Helper method to repeatedly call the insert method on a tree
     * @param tree the tree to call insert on
     * @param values the value to use as both key and value for the insert invocations
     * @return the tree
     */
    private static BinarySearchTree<Integer, Integer> insert(BinarySearchTree<Integer, Integer> tree, int... values) {
        for (int i : values)
            tree.insert(i, i);

        return tree;
    }

    /**
     * Class to store a singular value as an object
     * @author Joshua Shew
     */
    private class Container<U> {
        /* stores the value */
        private U value;

        /* stores whether a value has been added */
        private boolean added = false;

        /**
         * Constructor with no input
         */
        public Container() {
            this.value = null;
            this.added = false;
        }

        /**
         * Constructor with a value
         * @param value the value to be stored
         */
        public Container(U value) {
            this.value = value;
            this.added = true;
        }

        /**
         * Getter for the value stored in the container
         * @return the value
         */
        public U getValue() {
            return this.value;
        }

        /**
         * Setter for the value stored in the container
         * @param value the value to store in the container
         */
        public void setValue(U value) {
            this.value = value;
            this.added = true;
        }

        /**
         * Whether the container has a value in it or not
         * @return true if there is a value, false otherwise
         */
        public boolean hasValue() {
            return this.added;
        }
    }

    /**
     * A class to represent a node in a BinaryTree
     * @author Joshua Shew
     */
    private class BinaryNode {
        /* stores the key by which nodes are sorted in the tree */
        private T key;

        /* stores the value in the node */
        private V value;

        /* stores a reference to the left child */
        private BinaryNode left;

        /* stores a reference to the right child */
        private BinaryNode right;

        /* stores a reference to the parent node */
        private BinaryNode parent;

        /**
         * Constructor with key and value
         * @param key the key for the node
         * @param value the value of the node
         */
        public BinaryNode(T key, V value) {
            this.key = key;
            this.value = value;

            this.left = null;
            this.right = null;
            this.parent = null;
        }

        /**
         * Constructor with parent parameter included
         * @param key the key of the node
         * @param value the value of the node
         * @param parent a reference to the parent node
         */
        public BinaryNode(T key, V value, BinaryNode parent) {
            this.key = key;
            this.value = value;

            this.left = null;
            this.right = null;
            this.parent = parent;
        }

        /**
         * Getter for the key
         * @return the key
         */
        public T getKey() {
            return this.key;
        }

        /**
         * Setter for the key
         * @param key the new key
         */
        public void setKey(T key) {
            this.key = key;
        }

        /**
         * Getter for the value
         * @return the value in the node
         */
        public V getValue() {
            return this.value;
        }

        /**
         * Setter for the value
         * @param value the new value
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Getter for the left child
         * @return the left child
         */
        public BinaryNode getLeft() {
            return this.left;
        }

        /**
         * Setter for the left child
         * @param left the new left child
         */
        public void setLeft(BinaryNode left) {
            this.left = left;
        }

        /**
         * Getter for the right child
         * @return the right child
         */
        public BinaryNode getRight() {
            return this.right;
        }

        /**
         * Setter for the right child
         * @param right the new right child
         */
        public void setRight(BinaryNode right) {
            this.right = right;
        }

        /**
         * Getter for the parent node
         * @return the parent node
         */
        public BinaryNode getParent() {
            return this.parent;
        }
        
        /**
         * Setter for the parent node
         * @param parent the new parent node
         */
        public void setParent(BinaryNode parent) {
            this.parent = parent;
        }

        /**
         * Resets the key and value of this node based on the input node
         * @param node the node to copy the key and data from
         * @return this node
         */
        public BinaryNode reassignData(BinaryNode node) {
            this.setValue(node.getValue());
            this.setKey(node.getKey());

            return this;
        }
    }

    /* Main method for demonstration */
    public static void main(String[] args) {
        System.out.println("** Demonstration **");
        System.out.println(""); // new line

        System.out.println("Create a new empty BinarySearchTree\nBinarySearchTree<Integer, Integer> tree1 = new BinarySearchTree<Integer, Integer>();");
        System.out.println(""); // new line

        BinarySearchTree<Integer, Integer> tree1 = new BinarySearchTree<Integer, Integer>();

        System.out.println("Insert: 2, 1, 4, 5, 9, 3, 6, 7, 10, 12, 11");
        System.out.println("Uses: tree1.insert(x, x); // so key and value are the same");

        insert(tree1, 2, 1, 4, 5, 9, 3, 6, 7, 10, 12, 11);

        System.out.println(""); // new line
        System.out.println("Use tree1.inorderRec().toString() to display the tree");
        System.out.println(tree1.inorderRec().toString());
        System.out.println(""); // new line
        System.out.println("Here is a preorder print to show the structure of the tree");
        System.out.println(tree1.preOrderToString());

        System.out.println(""); // new line
        System.out.println("Delete 4 and 9 using: tree1.delete(x);");
        
        tree1.delete(4);
        tree1.delete(9);

        System.out.println(""); // new line
        System.out.println(tree1.inorderRec().toString());
        System.out.println(tree1.preOrderToString());

        System.out.println(""); // new line
        System.out.println("Search 12 and 4 using: tree1.search(x);");
        System.out.println(tree1.search(12).toString());
        System.out.println(""); // new line
        System.out.println("Searching 4 will raise an exception\nI will catch and print that exception");
        try {
            tree1.search(4);
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        System.out.println(""); // new line
        System.out.println("Find the 3rd smallest element by using: tree1.kthSmallest(x);");
        System.out.println(tree1.kthSmallest(3).toString());

        System.out.println(""); // new line
        System.out.println("The tree uses generics, so other comparable types can be used as keys, and any type can be the value stored in the tree");
        System.out.println("For this example I will use strings as the key, and doubles as the values");
        System.out.println(""); // new line
        System.out.println("BinarySearchTree<String, Double> tree2 = new BinarySearchTree<String, Double>();");
        System.out.println(""); // new line
        System.out.println("I will insert: (\"b\", 7.5), (\"a\", 2.0), (\"z\", 1.1), and (\"h\", 13.4)");

        BinarySearchTree<String, Double> tree2 = new BinarySearchTree<String, Double>();
        tree2.insert("b", 7.5);
        tree2.insert("a", 2.0);
        tree2.insert("z", 1.1);
        tree2.insert("h", 13.4);
        
        System.out.println(""); // new line
        System.out.println("Here is the tree printed out in 2 different ways");
        System.out.println(tree2.inorderRec().toString());
        System.out.println(tree2.preOrderToString());

        System.out.println(""); // new line
        System.out.println("Demonstration pt.8 is shown through the above tests as well");

        System.out.println(""); // new line
        System.out.println("endOfDemonstration();");
    }
}
