/**
 * ArrayOperations - demonstrates basic operations on a FIXED-CAPACITY array
 * that keeps track of a LOGICAL SIZE (how many slots are actually used).
 *
 * Operations: insert, delete, search, display.
 * All positions shown to the user are 1-based (beginner friendly),
 * while the internal storage is 0-based like normal Java arrays.
 */
public class ArrayOperations {

    private final int[] data;      // the fixed-capacity storage
    private final int capacity;    // maximum number of elements
    private int size;              // logical size (elements currently stored)

    /** Counts total comparisons made by search() - used by the performance analyzer. */
    private int lastSearchComparisons;

    public ArrayOperations(int capacity) {
        this.capacity = capacity;
        this.data = new int[capacity];
        this.size = 0;
        this.lastSearchComparisons = 0;
    }

    /** @return true if the array has reached its fixed capacity. */
    public boolean isFull() {
        return size >= capacity;
    }

    /** @return true if no elements are stored. */
    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getLastSearchComparisons() {
        return lastSearchComparisons;
    }

    /**
     * Inserts a value at a given 1-based position, shifting elements right.
     * If position equals size+1, the value is appended at the end.
     *
     * @return true if inserted, false if the array is full or position invalid.
     */
    public boolean insert(int value, int position) {
        if (isFull()) {
            System.out.println(">> Error: Array is FULL (" + capacity
                    + " elements). Cannot insert.");
            return false;
        }
        if (position < 1 || position > size + 1) {
            System.out.println(">> Error: Invalid position " + position
                    + ". Valid range is 1 to " + (size + 1) + ".");
            return false;
        }
        // Shift elements from index (position-1) onwards one slot to the right.
        int index = position - 1;
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        size++;
        System.out.println(">> Inserted " + value + " at position " + position + ".");
        return true;
    }

    /**
     * Deletes the element at a given 1-based position, shifting elements left.
     *
     * @return the deleted value, or Integer.MIN_VALUE as a "not found" flag.
     */
    public int deleteAtPosition(int position) {
        if (isEmpty()) {
            System.out.println(">> Error: Array is EMPTY. Nothing to delete.");
            return Integer.MIN_VALUE;
        }
        if (position < 1 || position > size) {
            System.out.println(">> Error: Invalid position " + position
                    + ". Valid range is 1 to " + size + ".");
            return Integer.MIN_VALUE;
        }
        int index = position - 1;
        int removed = data[index];
        // Shift elements left to fill the gap.
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = 0; // clear the now-unused slot
        size--;
        System.out.println(">> Deleted value " + removed + " from position " + position + ".");
        return removed;
    }

    /**
     * Linear search through the logical part of the array.
     * Counts every comparison so the performance analyzer can report it.
     *
     * @return the 1-based position of the first occurrence, or -1 if missing.
     */
    public int search(int value) {
        lastSearchComparisons = 0;
        for (int i = 0; i < size; i++) {
            lastSearchComparisons++;
            if (data[i] == value) {
                return i + 1; // return 1-based position
            }
        }
        return -1;
    }

    /** Displays all stored elements with their 1-based positions. */
    public void display() {
        if (isEmpty()) {
            System.out.println(">> Array is EMPTY. (capacity = " + capacity + ", size = 0)");
            return;
        }
        System.out.print(">> Array [" + size + "/" + capacity + "]: ");
        for (int i = 0; i < size; i++) {
            System.out.print(data[i]);
            if (i < size - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();
    }

    /**
     * Same as deleteAtPosition but WITHOUT printing (used by the performance
     * analyzer so its live demo does not clutter the benchmark output).
     */
    public boolean deleteAtPositionSilently(int position) {
        if (isEmpty() || position < 1 || position > size) {
            return false;
        }
        int index = position - 1;
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = 0;
        size--;
        return true;
    }

    /** Returns a copy of the stored elements (used by the performance analyzer). */
    public int[] getCopyOfElements() {
        int[] copy = new int[size];
        System.arraycopy(data, 0, copy, 0, size);
        return copy;
    }
}
