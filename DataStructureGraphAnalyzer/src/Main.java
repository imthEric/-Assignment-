/**
 * Main - the console entry point of the Data Structure & Graph Performance
 * Analyzer (CIT300 Graded Practical Assignment 2).
 *
 * Creates one shared instance of every data structure so all menus operate
 * on the same live data, then runs the main menu loop.
 */
import java.util.Random;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class Main {

    // Shared structures (persist while the program runs)
    private static final ArrayOperations array = new ArrayOperations(15);
    private static final StackOperations stack = new StackOperations(10);
    private static final QueueOperations queue = new QueueOperations(10);
    private static final LinkedListOperations list = new LinkedListOperations();
    private static final SearchingAlgorithms searcher = new SearchingAlgorithms();
    private static final GraphOperations graph = new GraphOperations();
    private static final PerformanceAnalyzer analyzer = new PerformanceAnalyzer(searcher);

    /** Set to true when input ends (Ctrl+D / end of piped file) so the
     *  program shuts down cleanly instead of crashing. */
    private static boolean inputEnded = false;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printMainMenu();
        boolean running = true;
        while (running && !inputEnded) {
            System.out.print("\nEnter your choice: ");
            String line = safeNextLine(scanner);
            if (line == null) {         // no more input -> exit gracefully
                running = false;
                break;
            }
            line = line.trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println(">> Invalid input '" + line + "'. Please enter a number 1-9.");
                continue;   // let the user try again without crashing
            }

            switch (choice) {
                case 1: arrayMenu(scanner); break;
                case 2: stackMenu(scanner); break;
                case 3: queueMenu(scanner); break;
                case 4: linkedListMenu(scanner); break;
                case 5: searchingMenu(scanner); break;
                case 6: graphMenu(scanner); break;
                case 7: performanceMenu(scanner); break;
                case 8: displayAllResults(); break;
                case 9:
                    running = false;
                    break;
                default:
                    System.out.println(">> Invalid menu choice. Please pick a number from 1 to 9.");
            }
        }

        System.out.println("\nThank you for using the Data Structure & Graph Performance Analyzer.");
        System.out.println("Program exited cleanly. Goodbye!");
        scanner.close();   // release the input resource
    }

    private static void printMainMenu() {
        System.out.println("=============================================");
        System.out.println("   DATA STRUCTURE & GRAPH PERFORMANCE ANALYZER");
        System.out.println("             CIT300 - Assignment 2");
        System.out.println("=============================================");
        System.out.println("1. Array Operations");
        System.out.println("2. Stack Operations");
        System.out.println("3. Queue Operations");
        System.out.println("4. Linked List Operations");
        System.out.println("5. Searching Operations");
        System.out.println("6. Graph Operations");
        System.out.println("7. Performance Comparison");
        System.out.println("8. Display All Results");
        System.out.println("9. Exit");
        System.out.println("=============================================");
    }

    /** Prints a submenu header and its options. */
    private static void printSubmenu(String title, String... options) {
        System.out.println("\n------------- " + title + " -------------");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Back to Main Menu");
    }

    /**
     * Reads one line, returning null when the input stream has ended
     * (Ctrl+D or end of a piped file) instead of throwing an exception.
     */
    private static String safeNextLine(Scanner scanner) {
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            }
            inputEnded = true;
            return null;
        } catch (NoSuchElementException e) {
            inputEnded = true;
            return null;
        }
    }

    /** Reads an int menu choice safely (returns -1 on bad input). */
    private static int readChoice(Scanner scanner) {
        System.out.print("Enter your choice: ");
        String line = safeNextLine(scanner);
        if (line == null) {
            return 0;   // treat end-of-input as "back to main menu"
        }
        line = line.trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException e) {
            System.out.println(">> Invalid input '" + line + "'. Enter a number from the menu.");
            return -1;
        }
    }

    /** Reads an int value from the user with validation. */
    private static int readIntValue(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = safeNextLine(scanner);
            if (line == null) {
                return 0;   // end of input: return a harmless default
            }
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println(">> That is not a whole number. Please try again.");
            }
        }
    }

    // ------------------------------------------------------------------
    // MENU 1 : ARRAY
    // ------------------------------------------------------------------
    private static void arrayMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            printSubmenu("ARRAY OPERATIONS [" + array.getSize() + "/" + array.getCapacity() + "]",
                    "Insert element", "Delete element at position",
                    "Search for element", "Display array");
            int c = readChoice(scanner);
            switch (c) {
                case 1: {
                    int value = readIntValue(scanner, "Enter value to insert: ");
                    int pos = readIntValue(scanner,
                            "Enter position (1-" + (array.getSize() + 1) + "): ");
                    array.insert(value, pos);
                    break;
                }
                case 2: {
                    int pos = readIntValue(scanner, "Enter position to delete (1-based): ");
                    array.deleteAtPosition(pos);
                    break;
                }
                case 3: {
                    if (array.isEmpty()) {
                        System.out.println(">> The array is EMPTY - nothing to search.");
                        break;
                    }
                    int value = readIntValue(scanner, "Enter value to search for: ");
                    long start = System.nanoTime();
                    int found = array.search(value);
                    long elapsed = System.nanoTime() - start;
                    if (found > 0) {
                        System.out.println(">> Value " + value + " FOUND at position " + found + ".");
                    } else {
                        System.out.println(">> Value " + value + " NOT FOUND in the array.");
                    }
                    System.out.println("   Comparisons made: " + array.getLastSearchComparisons());
                    System.out.println("   Time taken: " + elapsed + " ns");
                    break;
                }
                case 4:
                    array.display();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    if (c != -1) {
                        System.out.println(">> Invalid option. Choose 0-4.");
                    }
            }
        }
    }

    // ------------------------------------------------------------------
    // MENU 2 : STACK
    // ------------------------------------------------------------------
    private static void stackMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            printSubmenu("STACK OPERATIONS [" + stack.getSize() + "/" + stack.getCapacity() + "]",
                    "Push", "Pop", "Peek (top)", "Display stack");
            int c = readChoice(scanner);
            switch (c) {
                case 1: {
                    int value = readIntValue(scanner, "Enter value to push: ");
                    stack.push(value);
                    break;
                }
                case 2:
                    stack.pop();
                    break;
                case 3:
                    stack.peek();
                    break;
                case 4:
                    stack.display();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    if (c != -1) {
                        System.out.println(">> Invalid option. Choose 0-4.");
                    }
            }
        }
    }

    // ------------------------------------------------------------------
    // MENU 3 : QUEUE
    // ------------------------------------------------------------------
    private static void queueMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            printSubmenu("QUEUE OPERATIONS [" + queue.getSize() + "/" + queue.getCapacity() + "]",
                    "Enqueue", "Dequeue", "Peek (front)", "Display queue");
            int c = readChoice(scanner);
            switch (c) {
                case 1: {
                    int value = readIntValue(scanner, "Enter value to enqueue: ");
                    queue.enqueue(value);
                    break;
                }
                case 2:
                    queue.dequeue();
                    break;
                case 3:
                    queue.peekFront();
                    break;
                case 4:
                    queue.display();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    if (c != -1) {
                        System.out.println(">> Invalid option. Choose 0-4.");
                    }
            }
        }
    }

    // ------------------------------------------------------------------
    // MENU 4 : LINKED LIST
    // ------------------------------------------------------------------
    private static void linkedListMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            printSubmenu("LINKED LIST OPERATIONS [" + list.getSize() + " nodes]",
                    "Insert at beginning", "Insert at end", "Insert at position",
                    "Delete by value", "Delete at position",
                    "Search for value", "Display list");
            int c = readChoice(scanner);
            switch (c) {
                case 1:
                    list.insertAtBeginning(readIntValue(scanner, "Enter value: "));
                    break;
                case 2:
                    list.insertAtEnd(readIntValue(scanner, "Enter value: "));
                    break;
                case 3: {
                    int value = readIntValue(scanner, "Enter value: ");
                    int pos = readIntValue(scanner,
                            "Enter position (1-" + (list.getSize() + 1) + "): ");
                    list.insertAtPosition(value, pos);
                    break;
                }
                case 4:
                    list.deleteByValue(readIntValue(scanner, "Enter value to delete: "));
                    break;
                case 5:
                    list.deleteAtPosition(readIntValue(scanner, "Enter position to delete: "));
                    break;
                case 6: {
                    if (list.isEmpty()) {
                        System.out.println(">> The list is EMPTY - nothing to search.");
                        break;
                    }
                    int value = readIntValue(scanner, "Enter value to search for: ");
                    long start = System.nanoTime();
                    int pos = list.search(value);
                    long elapsed = System.nanoTime() - start;
                    if (pos > 0) {
                        System.out.println(">> Value " + value + " FOUND at position " + pos + ".");
                    } else {
                        System.out.println(">> Value " + value + " NOT FOUND in the list.");
                    }
                    System.out.println("   Nodes visited: " + list.getLastSearchSteps());
                    System.out.println("   Time taken: " + elapsed + " ns");
                    break;
                }
                case 7:
                    list.display();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    if (c != -1) {
                        System.out.println(">> Invalid option. Choose 0-7.");
                    }
            }
        }
    }

    // ------------------------------------------------------------------
    // MENU 5 : SEARCHING
    // ------------------------------------------------------------------
    private static void searchingMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            printSubmenu("SEARCHING OPERATIONS [dataset size = " + searcher.getDataSize() + "]",
                    "Enter / load dataset", "Linear search", "Binary search (uses sorted copy)",
                    "Compare linear vs binary", "Generate random dataset", "Show current datasets");
            int c = readChoice(scanner);
            switch (c) {
                case 1: {
                    int n = readIntValue(scanner, "How many integers? (1-1000): ");
                    if (n < 1 || n > 1000) {
                        System.out.println(">> Please use a size between 1 and 1000.");
                        break;
                    }
                    int[] values = new int[n];
                    System.out.println("   Enter " + n + " integers (one per line):");
                    for (int i = 0; i < n; i++) {
                        values[i] = readIntValue(scanner, "   [" + (i + 1) + "/" + n + "] ");
                    }
                    searcher.setData(values);
                    System.out.println(">> Dataset saved. Original order preserved; a sorted");
                    System.out.println("   copy was created automatically for binary search.");
                    break;
                }
                case 2: {
                    if (!searcher.hasData()) {
                        System.out.println(">> No dataset yet. Use option 1 or 5 first.");
                        break;
                    }
                    int key = readIntValue(scanner, "Enter value to search: ");
                    SearchingAlgorithms.SearchResult r = searcher.linearSearch(key);
                    SearchingAlgorithms.printResult("Linear Search", r, true);
                    break;
                }
                case 3: {
                    if (!searcher.hasData()) {
                        System.out.println(">> No dataset yet. Use option 1 or 5 first.");
                        break;
                    }
                    int key = readIntValue(scanner, "Enter value to search: ");
                    SearchingAlgorithms.SearchResult r = searcher.binarySearch(key);
                    SearchingAlgorithms.printResult("Binary Search", r, true);
                    break;
                }
                case 4: {
                    if (!searcher.hasData()) {
                        System.out.println(">> No dataset yet. Use option 1 or 5 first.");
                        break;
                    }
                    int key = readIntValue(scanner, "Enter value to search: ");
                    searcher.compareBothSearches(key);
                    break;
                }
                case 5: {
                    int n = readIntValue(scanner, "How many random integers? (1-100000): ");
                    if (n < 1 || n > 100000) {
                        System.out.println(">> Please use a size between 1 and 100000.");
                        break;
                    }
                    Random random = new Random();
                    int[] values = new int[n];
                    for (int i = 0; i < n; i++) {
                        values[i] = random.nextInt(n * 10); // spread out the range
                    }
                    searcher.setData(values);
                    System.out.println(">> Generated dataset of " + n + " random integers.");
                    break;
                }
                case 6: {
                    if (!searcher.hasData()) {
                        System.out.println(">> No dataset yet.");
                        break;
                    }
                    System.out.println(">> Original data (first 30 shown): "
                            + preview(searcher.getOriginalData()));
                    System.out.println(">> Sorted copy  (first 30 shown): "
                            + preview(searcher.getSortedData()));
                    break;
                }
                case 0:
                    back = true;
                    break;
                default:
                    if (c != -1) {
                        System.out.println(">> Invalid option. Choose 0-6.");
                    }
            }
        }
    }

    /** Shortens a big array for printing. */
    private static String preview(int[] arr) {
        if (arr.length <= 30) {
            return java.util.Arrays.toString(arr);
        }
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < 30; i++) {
            sb.append(arr[i]).append(i < 29 ? ", " : "");
        }
        sb.append(", ... ] (").append(arr.length).append(" total)");
        return sb.toString();
    }

    // ------------------------------------------------------------------
    // MENU 6 : GRAPH
    // ------------------------------------------------------------------
    private static void graphMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            printSubmenu("GRAPH OPERATIONS [" + graph.getVertexCount() + " vertices, "
                            + graph.getEdgeCount() + " edges]",
                    "Add vertex", "Add edge", "Display graph",
                    "Search vertex", "BFS traversal", "DFS traversal");
            int c = readChoice(scanner);
            switch (c) {
                case 1:
                    graph.addVertex(readLine(scanner, "Enter vertex name: "));
                    break;
                case 2: {
                    if (graph.isEmpty()) {
                        System.out.println(">> The graph is EMPTY. Add vertices before adding edges.");
                        break;
                    }
                    String v1 = readLine(scanner, "First vertex:  ");
                    String v2 = readLine(scanner, "Second vertex: ");
                    graph.addEdge(v1, v2);
                    break;
                }
                case 3:
                    graph.display();
                    break;
                case 4:
                    graph.searchVertex(readLine(scanner, "Enter vertex name to search: "));
                    break;
                case 5: {
                    String start = chooseStartVertex(scanner);
                    if (start != null) {
                        GraphOperations.TraversalResult r = graph.bfs(start);
                        GraphOperations.printTraversal("BREADTH-FIRST SEARCH (BFS)", r,
                                start.trim(), graph.getVertexCount());
                        System.out.println("   BFS explores NEIGHBOURS FIRST (level by level)");
                        System.out.println("   using a FIFO queue.");
                    }
                    break;
                }
                case 6: {
                    String start = chooseStartVertex(scanner);
                    if (start != null) {
                        GraphOperations.TraversalResult r = graph.dfs(start);
                        GraphOperations.printTraversal("DEPTH-FIRST SEARCH (DFS)", r,
                                start.trim(), graph.getVertexCount());
                        System.out.println("   DFS goes as DEEP as possible along each branch");
                        System.out.println("   before backtracking, using a LIFO stack.");
                    }
                    break;
                }
                case 0:
                    back = true;
                    break;
                default:
                    if (c != -1) {
                        System.out.println(">> Invalid option. Choose 0-6.");
                    }
            }
        }
    }

    /** Lists existing vertices and asks the user to pick a start vertex. */
    private static String chooseStartVertex(Scanner scanner) {
        if (graph.isEmpty()) {
            System.out.println(">> The graph is EMPTY. Add vertices first.");
            return null;
        }
        System.out.println("   Available vertices: " + graph.getVertexNames());
        String start = readLine(scanner, "Enter starting vertex: ");
        if (!graph.getVertexNames().contains(start.trim())) {
            System.out.println(">> '" + start.trim() + "' is not a vertex in this graph.");
            return null;
        }
        return start;
    }

    private static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        String line = safeNextLine(scanner);
        return line == null ? "" : line;
    }

    // ------------------------------------------------------------------
    // MENU 7 : PERFORMANCE COMPARISON
    // ------------------------------------------------------------------
    private static void performanceMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            printSubmenu("PERFORMANCE COMPARISON",
                    "Linear Search vs Binary Search",
                    "BFS vs DFS (same graph, same start vertex)",
                    "Array / Stack / Queue / Linked List operations",
                    "Run ALL comparisons");
            int c = readChoice(scanner);
            switch (c) {
                case 1: {
                    int key = readIntValue(scanner, "Enter value to search for: ");
                    analyzer.compareLinearVsBinary(key);
                    break;
                }
                case 2: {
                    String start = chooseStartVertex(scanner);
                    if (start != null) {
                        analyzer.compareBfsVsDfs(graph, start.trim());
                    }
                    break;
                }
                case 3:
                    analyzer.compareStructureOperations(array, stack, queue, list);
                    break;
                case 4:
                    if (searcher.hasData()) {
                        analyzer.compareLinearVsBinary(searcher.getSortedData()[0]);
                    } else {
                        System.out.println("\n>> Skipping search comparison (no dataset yet -"
                                + " use menu 5 first).");
                    }
                    if (!graph.isEmpty()) {
                        String firstVertex = graph.getVertexNames().iterator().next();
                        analyzer.compareBfsVsDfs(graph, firstVertex);
                    } else {
                        System.out.println("\n>> Skipping BFS/DFS comparison (empty graph -"
                                + " use menu 6 first).");
                    }
                    analyzer.compareStructureOperations(array, stack, queue, list);
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    if (c != -1) {
                        System.out.println(">> Invalid option. Choose 0-4.");
                    }
            }
        }
    }

    // ------------------------------------------------------------------
    // MENU 8 : DISPLAY ALL RESULTS
    // ------------------------------------------------------------------
    private static void displayAllResults() {
        System.out.println("\n================ CURRENT APPLICATION STATE ================");
        System.out.println("-- ARRAY --");
        array.display();
        System.out.println("-- STACK --");
        stack.display();
        System.out.println("-- QUEUE --");
        queue.display();
        System.out.println("-- LINKED LIST --");
        list.display();
        System.out.println("-- SEARCH DATASET --");
        if (searcher.hasData()) {
            System.out.println("   Size: " + searcher.getDataSize());
            System.out.println("   Original (first 30): " + preview(searcher.getOriginalData()));
            System.out.println("   Sorted copy (first 30): " + preview(searcher.getSortedData()));
        } else {
            System.out.println("   >> No dataset loaded yet (use menu 5).");
        }
        System.out.println("-- GRAPH --");
        graph.display();
        analyzer.displayHistory();
        System.out.println("============================================================");
    }
}
