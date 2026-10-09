/**
 * StackOperations - a LIFO (Last-In-First-Out) stack implemented with a
 * fixed-size array.
 *
 * Operations: push, pop, peek, display.
 * Overflow happens when pushing onto a full stack; underflow happens when
 * popping/peeking an empty stack. Both are handled gracefully.
 */
public class StackOperations {

    private final int[] items;     // internal storage
    private final int capacity;    // maximum number of elements
    private int top;               // index of the top element (-1 = empty)

    public StackOperations(int capacity) {
        this.capacity = capacity;
        this.items = new int[capacity];
        this.top = -1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == capacity - 1;
    }

    public int getSize() {
        return top + 1;
    }

    public int getCapacity() {
        return capacity;
    }

    /**
     * Pushes a value onto the top of the stack.
     * @return true on success, false if the stack overflows.
     */
    public boolean push(int value) {
        if (isFull()) {
            System.out.println(">> Stack OVERFLOW! Stack is full (" + capacity
                    + " elements). Cannot push " + value + ".");
            return false;
        }
        top++;
        items[top] = value;
        System.out.println(">> Pushed " + value + " onto the stack. (top index = " + top + ")");
        return true;
    }

    /**
     * Removes and returns the top element.
     * @return the popped value, or Integer.MIN_VALUE if the stack is empty.
     */
    public int pop() {
        if (isEmpty()) {
            System.out.println(">> Stack UNDERFLOW! The stack is EMPTY. Nothing to pop.");
            return Integer.MIN_VALUE;
        }
        int value = items[top];
        items[top] = 0;      // clear reference for cleanliness
        top--;
        System.out.println(">> Popped " + value + " from the stack.");
        return value;
    }

    /**
     * Returns the top element WITHOUT removing it.
     * @return the top value, or Integer.MIN_VALUE if the stack is empty.
     */
    public int peek() {
        if (isEmpty()) {
            System.out.println(">> The stack is EMPTY. Nothing to peek.");
            return Integer.MIN_VALUE;
        }
        System.out.println(">> Top element (peek) = " + items[top]);
        return items[top];
    }

    /** Displays the stack from top to bottom. */
    public void display() {
        if (isEmpty()) {
            System.out.println(">> Stack is EMPTY. (capacity = " + capacity + ", size = 0)");
            return;
        }
        System.out.println(">> Stack [" + getSize() + "/" + capacity + "] (TOP first):");
        for (int i = top; i >= 0; i--) {
            System.out.println("     [ " + items[i] + " ]" + (i == top ? "  <-- TOP" : ""));
        }
    }
}
