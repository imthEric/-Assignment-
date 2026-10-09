import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * GraphOperations - an UNDIRECTED graph stored as an ADJACENCY LIST using
 * Java collections (HashMap of vertex name -> ArrayList of neighbour names).
 *
 * Operations: addVertex, addEdge, display, searchVertex, BFS, DFS.
 * BFS uses a Queue (level-by-level); DFS uses a Stack / recursion-style
 * LIFO order (depth-first). Both count visited vertices and edge
 * examinations and measure time with System.nanoTime().
 */
public class GraphOperations {

    /** adjacency list: each vertex maps to the list of its neighbours */
    private final Map<String, List<String>> adjacencyList;

    /** Result object for traversals: order, counts and timing. */
    public static class TraversalResult {
        public final List<String> order;      // visit order
        public final int visitedCount;        // number of vertices visited
        public final int edgeExaminations;    // neighbour checks performed
        public final long nanoseconds;        // measured execution time

        public TraversalResult(List<String> order, int visitedCount,
                               int edgeExaminations, long nanoseconds) {
            this.order = order;
            this.visitedCount = visitedCount;
            this.edgeExaminations = edgeExaminations;
            this.nanoseconds = nanoseconds;
        }
    }

    public GraphOperations() {
        this.adjacencyList = new HashMap<>();
    }

    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    public int getVertexCount() {
        return adjacencyList.size();
    }

    /** Total undirected edges = sum of all adjacency-list sizes / 2. */
    public int getEdgeCount() {
        int total = 0;
        for (List<String> neighbours : adjacencyList.values()) {
            total += neighbours.size();
        }
        return total / 2;
    }

    public Set<String> getVertexNames() {
        return adjacencyList.keySet();
    }

    /**
     * Adds a named vertex. Duplicate vertices are rejected.
     * @return true if added, false if it already exists.
     */
    public boolean addVertex(String name) {
        String key = normalize(name);
        if (key.isEmpty()) {
            System.out.println(">> Error: Vertex name cannot be empty.");
            return false;
        }
        if (adjacencyList.containsKey(key)) {
            System.out.println(">> Vertex '" + key + "' ALREADY EXISTS. Duplicate ignored.");
            return false;
        }
        adjacencyList.put(key, new ArrayList<>());
        System.out.println(">> Vertex '" + key + "' added.");
        return true;
    }

    /**
     * Adds an undirected edge between two existing vertices.
     * Validates that both vertices exist and rejects duplicates/self-loops.
     */
    public boolean addEdge(String v1, String v2) {
        String a = normalize(v1);
        String b = normalize(v2);
        if (!adjacencyList.containsKey(a)) {
            System.out.println(">> Error: Vertex '" + a + "' does not exist. Add it first.");
            return false;
        }
        if (!adjacencyList.containsKey(b)) {
            System.out.println(">> Error: Vertex '" + b + "' does not exist. Add it first.");
            return false;
        }
        if (a.equals(b)) {
            System.out.println(">> Error: Self-loops ('" + a + "' to itself) are not allowed.");
            return false;
        }
        if (adjacencyList.get(a).contains(b)) {
            System.out.println(">> Edge (" + a + " - " + b + ") ALREADY EXISTS. Duplicate ignored.");
            return false;
        }
        adjacencyList.get(a).add(b);
        adjacencyList.get(b).add(a); // undirected: add in both directions
        System.out.println(">> Undirected edge (" + a + " - " + b + ") added.");
        return true;
    }

    /** Searches for a vertex by name (case-insensitive). */
    public boolean searchVertex(String name) {
        String key = normalize(name);
        if (isEmpty()) {
            System.out.println(">> The graph is EMPTY - nothing to search.");
            return false;
        }
        boolean found = adjacencyList.containsKey(key);
        if (found) {
            System.out.println(">> Vertex '" + key + "' FOUND. Degree = "
                    + adjacencyList.get(key).size() + " neighbour(s): "
                    + adjacencyList.get(key));
        } else {
            System.out.println(">> Vertex '" + key + "' NOT FOUND in the graph.");
        }
        return found;
    }

    /** Displays the whole adjacency list. */
    public void display() {
        if (isEmpty()) {
            System.out.println(">> Graph is EMPTY. Add some vertices first.");
            return;
        }
        System.out.println(">> Graph (Adjacency List) - " + getVertexCount()
                + " vertices, " + getEdgeCount() + " edges:");
        // Sort names so output order is stable and easy to read.
        List<String> names = new ArrayList<>(adjacencyList.keySet());
        names.sort(String.CASE_INSENSITIVE_ORDER);
        for (String name : names) {
            System.out.println("     " + name + " -> " + adjacencyList.get(name));
        }
    }

    /**
     * BREADTH-FIRST SEARCH: explores level by level using a FIFO queue.
     * Only the connected component containing the start vertex is visited.
     */
    public TraversalResult bfs(String startName) {
        String start = normalize(startName);
        List<String> order = new ArrayList<>();
        if (!adjacencyList.containsKey(start)) {
            System.out.println(">> BFS failed: start vertex '" + start + "' does not exist.");
            return new TraversalResult(order, 0, 0, 0);
        }
        long startTime = System.nanoTime();
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        int edgeExaminations = 0;

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            String current = queue.remove();   // dequeue (FIFO) -> nearest first
            order.add(current);
            for (String neighbour : adjacencyList.get(current)) {
                edgeExaminations++;            // one meaningful traversal operation
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    queue.add(neighbour);      // enqueue for later levels
                }
            }
        }
        long elapsed = System.nanoTime() - startTime;
        return new TraversalResult(order, visited.size(), edgeExaminations, elapsed);
    }

    /**
     * DEPTH-FIRST SEARCH: explores as deep as possible first, using an
     * explicit LIFO stack (iterative version of recursion).
     * Neighbours are pushed in reverse order so they pop left-to-right,
     * matching the displayed adjacency list.
     */
    public TraversalResult dfs(String startName) {
        String start = normalize(startName);
        List<String> order = new ArrayList<>();
        if (!adjacencyList.containsKey(start)) {
            System.out.println(">> DFS failed: start vertex '" + start + "' does not exist.");
            return new TraversalResult(order, 0, 0, 0);
        }
        long startTime = System.nanoTime();
        Set<String> visited = new HashSet<>();
        java.util.Deque<String> stack = new java.util.ArrayDeque<>();
        int edgeExaminations = 0;

        stack.push(start);

        while (!stack.isEmpty()) {
            String current = stack.pop();      // LIFO -> go deep first
            if (visited.contains(current)) {
                continue;                      // skip nodes already handled
            }
            visited.add(current);
            order.add(current);
            List<String> neighbours = adjacencyList.get(current);
            // Push in reverse so the FIRST neighbour is explored first.
            for (int i = neighbours.size() - 1; i >= 0; i--) {
                edgeExaminations++;
                String neighbour = neighbours.get(i);
                if (!visited.contains(neighbour)) {
                    stack.push(neighbour);
                }
            }
        }
        long elapsed = System.nanoTime() - startTime;
        return new TraversalResult(order, visited.size(), edgeExaminations, elapsed);
    }

    /**
     * Prints a traversal result in a friendly format.
     * @param totalVertices total vertices in the graph, used to warn when the
     *        traversal could not reach every vertex (disconnected graph).
     */
    public static void printTraversal(String algorithm, TraversalResult result,
                                      String start, int totalVertices) {
        if (result.order.isEmpty()) {
            System.out.println(">> " + algorithm + " from '" + start + "' produced no result.");
            return;
        }
        System.out.println(">> " + algorithm + " starting at '" + start + "':");
        System.out.print("   Visit order : ");
        for (int i = 0; i < result.order.size(); i++) {
            System.out.print(result.order.get(i));
            if (i < result.order.size() - 1) {
                System.out.print(" -> ");
            }
        }
        System.out.println();
        System.out.println("   Vertices visited      : " + result.visitedCount);
        System.out.println("   Edge examinations     : " + result.edgeExaminations);
        System.out.println("   Execution time        : " + result.nanoseconds + " ns");
        if (result.visitedCount < totalVertices) {
            System.out.println("   NOTE: only " + result.visitedCount + " of " + totalVertices
                    + " vertices were reachable");
            System.out.println("         from the start vertex (the rest are disconnected).");
        }
    }

    /** Removes trailing/leading spaces and trims the input name. */
    private String normalize(String name) {
        return name == null ? "" : name.trim();
    }
}
