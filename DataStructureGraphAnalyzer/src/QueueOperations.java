/**
 * QueueOperations - a FIFO (First-In-First-Out) queue implemented as a
 * CIRCULAR ARRAY so that space freed by dequeue operations is reused.
 *
 * How the circular trick works:
 *   front  = index of the first element
 *   count  = number of elements currently stored
 *   The slot for the next enqueue is (front + count) % capacity,
 *   which "wraps around" the end of the array back to the beginning.
 *
 * Operations: enqueue, dequeue, peek (front), display.
 */
public class QueueOperations {

    private final int[] items;     // internal circular storage
    private final int capacity;    // maximum number of elements
    private int front;             // index of the front element
    private int count;             // current number of elements

    public QueueOperations(int capacity) {
        this.capacity = capacity;
        this.items = new int[capacity];
        this.front = 0;
        this.count = 0;
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == capacity;
    }

    public int getSize() {
        return count;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Adds a value at the rear of the queue.
     * @return true on success, false if the queue is full.
     */
    public boolean enqueue(int value) {
        if (isFull()) {
            System.out.println(">> Queue FULL! (" + capacity + " elements). Cannot enqueue "
                    + value + ".");
            return false;
        }
        int rearIndex = (front + count) % capacity; // wrap-around position
        items[rearIndex] = value;
        count++;
        System.out.println(">> Enqueued " + value + " at index " + rearIndex + ".");
        return true;
    }

    /**
     * Removes and returns the front element.
     * @return the dequeued value, or Integer.MIN_VALUE if the queue is empty.
     */
    public int dequeue() {
        if (isEmpty()) {
            System.out.println(">> Queue is EMPTY. Nothing to dequeue.");
            return Integer.MIN_VALUE;
        }
        int value = items[front];
        items[front] = 0;                       // clear the used slot
        front = (front + 1) % capacity;         // move front forward with wrap-around
        count--;
        System.out.println(">> Dequeued " + value + " from the front of the queue.");
        return value;
    }

    /**
     * Returns the front element WITHOUT removing it.
     * @return the front value, or Integer.MIN_VALUE if the queue is empty.
     */
    public int peekFront() {
        if (isEmpty()) {
            System.out.println(">> Queue is EMPTY. Nothing to peek.");
            return Integer.MIN_VALUE;
        }
        System.out.println(">> Front element (peek) = " + items[front]);
        return items[front];
    }

    /** Displays the queue from front to rear in logical order. */
    public void display() {
        if (isEmpty()) {
            System.out.println(">> Queue is EMPTY. (capacity = " + capacity + ", size = 0)");
            return;
        }
        System.out.print(">> Queue [" + count + "/" + capacity + "] FRONT -> REAR: ");
        for (int i = 0; i < count; i++) {
            int index = (front + i) % capacity;  // follow the circle
            System.out.print(items[index]);
            if (i < count - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();
    }
}
