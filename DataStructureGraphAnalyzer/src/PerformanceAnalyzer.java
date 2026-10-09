import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PerformanceAnalyzer - runs REAL algorithms (the ones implemented in this
 * project) on real or generated data and reports:
 *      operation, algorithm/data structure, result,
 *      comparisons/steps, execution time (ns), expected Big-O.
 *
 * Every measurement is stored in a history list so that the main menu's
 * "Display All Results" option can show all past performance runs.
 *
 * IMPORTANT (shown to the user too): measured time depends on input size,
 * hardware, JVM warm-up and other factors. A single measurement never
 * proves one algorithm is always faster - Big-O theory is shown separately.
 */
public class PerformanceAnalyzer {

    /** One recorded performance run. */
    private static class PerformanceRecord {
        final String timestamp;
        final String operation;
        final String structure;
        final String result;
        final int steps;             // comparisons / operations counted (-1 = n/a)
        final long nanoseconds;
        final String bigO;

        PerformanceRecord(String timestamp, String operation, String structure, String result,
                          int steps, long nanoseconds, String bigO) {
            this.timestamp = timestamp;
            this.operation = operation;
            this.structure = structure;
            this.result = result;
            this.steps = steps;
            this.nanoseconds = nanoseconds;
            this.bigO = bigO;
        }
    }

    private final List<PerformanceRecord> history = new ArrayList<>();

    /** Shared instances of the real structures so we benchmark actual code. */
    private final SearchingAlgorithms searcher;

    public PerformanceAnalyzer(SearchingAlgorithms searcher) {
        this.searcher = searcher;
    }

    private void record(String operation, String structure, String result,
                        int steps, long nanoseconds, String bigO) {
        history.add(new PerformanceRecord(
                java.time.LocalTime.now().toString().substring(0, 8),
                operation, structure, result, steps, nanoseconds, bigO));
    }

    private static void printHeader() {
        System.out.printf("%-22s %-18s %-24s %-9s %-13s %s%n",
                "Operation", "Structure", "Result", "Steps", "Time (ns)", "Big-O");
        System.out.println("------------------------------------------------------------------------------------------");
    }

    /**
     * Comparison 1: Linear Search vs Binary Search on the SAME dataset.
     * Uses the searching module's real implementations.
     */
    public void compareLinearVsBinary(int key) {
        if (!searcher.hasData()) {
            System.out.println(">> No search dataset yet. Use menu 5 (option 1) to enter data first.");
            return;
        }
        int n = searcher.getDataSize();
        System.out.println("\n===== PERFORMANCE: LINEAR SEARCH vs BINARY SEARCH (n = " + n + ") =====");
        SearchingAlgorithms.SearchResult linear = searcher.linearSearch(key);
        SearchingAlgorithms.SearchResult binary = searcher.binarySearch(key);

        printHeader();
        String linRes = linear.position >= 0 ? "found at idx " + linear.position : "not found";
        String binRes = binary.position >= 0 ? "found at idx " + binary.position : "not found";
        System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                "search(" + key + ")", "Linear Search", linRes,
                linear.comparisons, linear.nanoseconds, "O(n)");
        System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                "search(" + key + ")", "Binary Search", binRes,
                binary.comparisons, binary.nanoseconds, "O(log n)");

        record("search(" + key + ")", "Linear Search", linRes,
                linear.comparisons, linear.nanoseconds, "O(n)");
        record("search(" + key + ")", "Binary Search", binRes,
                binary.comparisons, binary.nanoseconds, "O(log n)");

        System.out.println("\n THEORY: O(n) linear search may inspect all " + n
                + " items; O(log n) binary search inspects about log2(" + n + ") ~ "
                + String.format("%.1f", Math.log(Math.max(n, 1)) / Math.log(2))
                + " items on sorted data.");
        System.out.println(" REMEMBER: measured times depend on input size, hardware, JVM");
        System.out.println(" warm-up, etc. Do not conclude from one run that one algorithm");
        System.out.println(" is ALWAYS faster - the comparison above is just this sample.");
    }

    /**
     * Comparison 2: BFS vs DFS on the SAME graph with the SAME start vertex.
     */
    public void compareBfsVsDfs(GraphOperations graph, String start) {
        if (graph.isEmpty()) {
            System.out.println(">> The graph is EMPTY. Build a graph in menu 6 first.");
            return;
        }
        System.out.println("\n===== PERFORMANCE: BFS vs DFS (vertices = " + graph.getVertexCount()
                + ", edges = " + graph.getEdgeCount() + ", start = '" + start + "') =====");
        GraphOperations.TraversalResult bfs = graph.bfs(start);
        GraphOperations.TraversalResult dfs = graph.dfs(start);
        if (bfs.order.isEmpty() || dfs.order.isEmpty()) {
            System.out.println(">> Traversal could not run (check the start vertex exists).");
            return;
        }

        printHeader();
        System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                "traversal from " + start, "BFS (queue)",
                "visited " + bfs.visitedCount + " vertices",
                bfs.edgeExaminations, bfs.nanoseconds, "O(V+E)");
        System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                "traversal from " + start, "DFS (stack)",
                "visited " + dfs.visitedCount + " vertices",
                dfs.edgeExaminations, dfs.nanoseconds, "O(V+E)");

        record("traversal " + start, "BFS", "visited " + bfs.visitedCount,
                bfs.edgeExaminations, bfs.nanoseconds, "O(V+E)");
        record("traversal " + start, "DFS", "visited " + dfs.visitedCount,
                dfs.edgeExaminations, dfs.nanoseconds, "O(V+E)");

        System.out.println("\n BFS visit order: " + bfs.order);
        System.out.println(" DFS visit order: " + dfs.order);
        System.out.println(" Both have the same theoretical complexity O(V+E) on an");
        System.out.println(" adjacency list; they differ in ORDER of visiting, not in");
        System.out.println(" Big-O. Times measured here are for this graph only.");
    }

    /**
     * Comparison 3: array / stack / queue / linked-list basic operations.
     * Each operation is timed on the real structures plus a larger generated
     * workload so the numbers scale with input size.
     */
    public void compareStructureOperations(ArrayOperations array, StackOperations stack,
                                           QueueOperations queue, LinkedListOperations list) {
        System.out.println("\n===== PERFORMANCE: BASIC STRUCTURE OPERATIONS =====");
        printHeader();

        // --- Array insert at front: shifts every element -> O(n) ---
        int workload = 1000;
        int[] filler = generateRandomArray(workload, 100000);
        // Real structure demo (uses the user's current array if it has space)
        if (!array.isFull()) {
            long t0 = System.nanoTime();
            boolean ok = array.insert(999999, 1);   // silent insert at position 1
            long t1 = System.nanoTime();
            if (ok) {
                array.deleteAtPositionSilently(1);   // restore state
                System.out.printf("%-22s %-18s %-24s %-9s %-13d %s%n",
                        "insert at front", "Array (yours)", "size=" + array.getSize(), "-",
                        (t1 - t0), "O(n) shift");
                record("insert at front", "Array", "size=" + array.getSize(),
                        array.getSize(), t1 - t0, "O(n)");
            }
        } else {
            System.out.println("   (Your array is full - skipped live array-insert demo.)");
        }

        // Scaled workload using plain arrays (same shifting logic):
        long tA0 = System.nanoTime();
        int[] big = Arrays.copyOf(filler, workload + 1); // extra slot at the end
        int sizeBig = workload;
        for (int i = sizeBig; i > 0; i--) {   // one front insert = n shifts
            big[i] = big[i - 1];
        }
        big[0] = 42;
        long tA1 = System.nanoTime();
        System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                "insert at front x1", "Array (n=" + workload + ")", "shifted " + sizeBig + " elems",
                sizeBig, (tA1 - tA0), "O(n)");
        record("insert at front", "Array n=" + workload, "shifted " + sizeBig,
                sizeBig, tA1 - tA0, "O(n)");

        // --- Stack push/pop: constant time -> O(1) ---
        long tS0 = System.nanoTime();
        java.util.Deque<Integer> st = new java.util.ArrayDeque<>();
        for (int i = 0; i < workload; i++) {
            st.push(i);
        }
        for (int i = 0; i < workload; i++) {
            st.pop();
        }
        long tS1 = System.nanoTime();
        System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                "push+pop x" + workload, "Stack", "empty again", workload * 2,
                (tS1 - tS0), "O(1) each");
        record("push+pop x" + workload, "Stack", "done", workload * 2, tS1 - tS0, "O(1) per op");

        // Live demo on the user's own stack/queue if possible (non-destructive):
        if (!stack.isFull()) {
            long t0 = System.nanoTime();
            stack.push(888888);
            stack.pop();
            long t1 = System.nanoTime();
            System.out.printf("%-22s %-18s %-24s %-9s %-13d %s%n",
                    "push+pop (yours)", "Stack", "size=" + stack.getSize(), "-",
                    (t1 - t0), "O(1)");
            record("push+pop", "Stack (yours)", "size=" + stack.getSize(),
                    2, t1 - t0, "O(1) per op");
        }

        // --- Queue enqueue/dequeue: constant time in a circular array -> O(1) ---
        long tQ0 = System.nanoTime();
        int[] circ = new int[workload];
        int front = 0, count = 0;
        for (int i = 0; i < workload; i++) {
            circ[(front + count) % workload] = i;   // enqueue
            count++;
            int v = circ[front];                     // dequeue
            front = (front + 1) % workload;
            count--;
            if (v == Integer.MIN_VALUE) { System.out.print(""); } // use value (avoid dead-code hint)
        }
        long tQ1 = System.nanoTime();
        System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                "enq+deq x" + workload, "Queue (circular)", "empty again", workload * 2,
                (tQ1 - tQ0), "O(1) each");
        record("enqueue+dequeue x" + workload, "Queue", "done", workload * 2, tQ1 - tQ0, "O(1) per op");

        if (!queue.isFull()) {
            long t0 = System.nanoTime();
            queue.enqueue(777777);
            queue.dequeue();
            long t1 = System.nanoTime();
            System.out.printf("%-22s %-18s %-24s %-9s %-13d %s%n",
                    "enq+deq (yours)", "Queue", "size=" + queue.getSize(), "-",
                    (t1 - t0), "O(1)");
            record("enqueue+dequeue", "Queue (yours)", "size=" + queue.getSize(),
                    2, t1 - t0, "O(1) per op");
        }

        // --- Linked list insert at head O(1) vs insert at tail O(n) ---
        LinkedListOperations benchList = new LinkedListOperations();
        long tL0 = System.nanoTime();
        for (int i = 0; i < workload; i++) {
            benchList.insertAtBeginningSilent(i);
        }
        long tL1 = System.nanoTime();
        System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                "insert head x" + workload, "Linked List", "size=" + benchList.getSize(),
                workload, (tL1 - tL0), "O(1) each");
        record("insert head x" + workload, "Linked List", "size=" + workload,
                workload, tL1 - tL0, "O(1) per op");

        long tL2 = System.nanoTime();
        for (int i = 0; i < workload; i++) {
            benchList.insertAtEndSilent(i);   // walks the whole list each time
        }
        long tL3 = System.nanoTime();
        int walkSteps = workload * (workload + 1) / 2; // 1+2+...+n node visits
        System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                "insert tail x" + workload, "Linked List", "size=" + benchList.getSize(),
                walkSteps, (tL3 - tL2), "O(n) each");
        record("insert tail x" + workload, "Linked List", "size=" + (workload * 2),
                walkSteps, tL3 - tL2, "O(n) per op");

        // Search in the user's live list (linear) if non-empty
        if (!list.isEmpty()) {
            int probe = list.searchFirstValueForProbe();
            long t0 = System.nanoTime();
            int pos = list.search(probe);
            long t1 = System.nanoTime();
            System.out.printf("%-22s %-18s %-24s %-9d %-13d %s%n",
                    "search (yours)", "Linked List",
                    pos >= 0 ? "found at " + pos : "not found",
                    list.getLastSearchSteps(), (t1 - t0), "O(n)");
            record("search head value", "Linked List (yours)",
                    pos >= 0 ? "found at " + pos : "not found",
                    list.getLastSearchSteps(), t1 - t0, "O(n)");
        }

        System.out.println("\n OBSERVATION: on this machine/sample, note how the O(n)-per-op");
        System.out.println(" workloads (array front-insert shifts, linked-list tail inserts)");
        System.out.println(" accumulate many more STEPS than the O(1) stack/queue operations.");
        System.out.println(" CAUTION: nanosecond values vary between runs (hardware, JIT, GC).");
        System.out.println(" Compare STEP counts and Big-O theory first; time second.");
    }

    /** Generates an array of random values (used for scaled benchmarks). */
    private int[] generateRandomArray(int size, int bound) {
        int[] arr = new int[size];
        java.util.Random random = new java.util.Random(42); // fixed seed = reproducible
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(bound);
        }
        return arr;
    }

    /** Shows every recorded performance run (used by "Display All Results"). */
    public void displayHistory() {
        System.out.println("\n===== PERFORMANCE HISTORY (" + history.size() + " record(s)) =====");
        if (history.isEmpty()) {
            System.out.println(">> No performance measurements recorded yet.");
            System.out.println("   Run menu 5 (compare searches) or menu 7 to collect results.");
            return;
        }
        System.out.printf("%-9s %-22s %-18s %-24s %-9s %-13s %s%n",
                "Time", "Operation", "Structure", "Result", "Steps", "Time (ns)", "Big-O");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (PerformanceRecord r : history) {
            System.out.printf("%-9s %-22s %-18s %-24s %-9s %-13d %s%n",
                    r.timestamp, r.operation, r.structure, r.result,
                    (r.steps >= 0 ? String.valueOf(r.steps) : "-"),
                    r.nanoseconds, r.bigO);
        }
    }

    public int getHistorySize() {
        return history.size();
    }
}
