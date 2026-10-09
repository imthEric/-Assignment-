import java.util.Arrays;

/**
 * SearchingAlgorithms - implements LINEAR SEARCH and BINARY SEARCH on an
 * int array, counting comparisons and measuring execution time with
 * System.nanoTime().
 *
 * Key idea:
 *   - Linear search checks every element one by one  -> O(n)
 *   - Binary search repeatedly halves a SORTED array -> O(log n)
 */
public class SearchingAlgorithms {

    /** Small result object so callers can see position, comparisons and time together. */
    public static class SearchResult {
        public final int position;       // 0-based index in the searched array, -1 if missing
        public final int comparisons;    // number of element comparisons performed
        public final long nanoseconds;   // measured execution time

        public SearchResult(int position, int comparisons, long nanoseconds) {
            this.position = position;
            this.comparisons = comparisons;
            this.nanoseconds = nanoseconds;
        }
    }

    private int[] data;          // the original (unsorted) data
    private int[] sortedData;    // a separate sorted copy for binary search

    public SearchingAlgorithms() {
        this.data = new int[0];
        this.sortedData = new int[0];
    }

    /**
     * Sets the dataset. The ORIGINAL order is preserved in 'data' for linear
     * search; a sorted CLONE is made for binary search so the user's original
     * data is never destroyed.
     */
    public void setData(int[] input) {
        this.data = Arrays.copyOf(input, input.length);      // preserve original
        this.sortedData = Arrays.copyOf(input, input.length); // separate copy
        Arrays.sort(this.sortedData);                          // sort only the copy
    }

    public boolean hasData() {
        return data.length > 0;
    }

    public int getDataSize() {
        return data.length;
    }

    public int[] getOriginalData() {
        return Arrays.copyOf(data, data.length);
    }

    public int[] getSortedData() {
        return Arrays.copyOf(sortedData, sortedData.length);
    }

    /**
     * LINEAR SEARCH on the original data.
     * Walks from index 0 to n-1 comparing each element with the key.
     * Time complexity: O(n).
     */
    public SearchResult linearSearch(int key) {
        int comparisons = 0;
        long start = System.nanoTime();
        int position = -1;
        for (int i = 0; i < data.length; i++) {
            comparisons++;
            if (data[i] == key) {
                position = i;
                break;
            }
        }
        long elapsed = System.nanoTime() - start;
        return new SearchResult(position, comparisons, elapsed);
    }

    /**
     * BINARY SEARCH on the sorted copy.
     * Repeatedly compares the middle element and discards half of the range.
     * Time complexity: O(log n). Requires sorted data!
     */
    public SearchResult binarySearch(int key) {
        int comparisons = 0;
        long start = System.nanoTime();
        int low = 0;
        int high = sortedData.length - 1;
        int position = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2; // avoids overflow vs (low+high)/2
            comparisons++;
            if (sortedData[mid] == key) {
                position = mid;
                break;
            } else if (key < sortedData[mid]) {
                high = mid - 1;               // key is in the left half
            } else {
                low = mid + 1;                // key is in the right half
            }
        }
        long elapsed = System.nanoTime() - start;
        return new SearchResult(position, comparisons, elapsed);
    }

    /** Prints a friendly single-search result line. */
    public static void printResult(String algorithm, SearchResult result, boolean zeroBasedNote) {
        if (result.position >= 0) {
            System.out.println(">> " + algorithm + ": value FOUND at index "
                    + result.position + " (position " + (result.position + 1) + ").");
        } else {
            System.out.println(">> " + algorithm + ": value NOT FOUND.");
        }
        System.out.println("   Comparisons : " + result.comparisons);
        System.out.println("   Time taken  : " + result.nanoseconds + " ns");
    }

    /**
     * Runs both searches on the same key and prints a side-by-side comparison
     * including the theoretical Big-O explanation.
     */
    public void compareBothSearches(int key) {
        SearchResult linear = linearSearch(key);
        SearchResult binary = binarySearch(key);

        System.out.println();
        System.out.println("--------------------------------------------------");
        System.out.println(" SEARCH COMPARISON for value: " + key
                + "  (n = " + data.length + " elements)");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-16s %-12s %-14s %-12s%n",
                "Algorithm", "Found?", "Comparisons", "Time (ns)");
        System.out.printf("%-16s %-12s %-14d %-12d%n", "Linear Search",
                linear.position >= 0 ? "YES idx " + linear.position : "NO",
                linear.comparisons, linear.nanoseconds);
        System.out.printf("%-16s %-12s %-14d %-12d%n", "Binary Search",
                binary.position >= 0 ? "YES idx " + binary.position : "NO",
                binary.comparisons, binary.nanoseconds);
        System.out.println("--------------------------------------------------");
        System.out.println(" THEORY: Linear search = O(n)  - worst case checks all n items.");
        System.out.println("         Binary search = O(log n) - halves the range each step.");
        System.out.println("         For n=" + data.length + ", log2(n) ~ "
                + String.format("%.2f", (data.length > 0 ? Math.log(data.length) / Math.log(2) : 0))
                + ", so binary search needs far fewer comparisons on large data.");
        System.out.println(" NOTE: Binary search REQUIRES sorted data (a sorted copy of the");
        System.out.println("       original array was used; the original order is preserved).");
        System.out.println(" NOTE: Actual times vary with hardware, JVM warm-up and input size;");
        System.out.println("       a single measurement does not prove one is always faster.");
    }
}
