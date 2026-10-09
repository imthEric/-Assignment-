/**
 * LinkedListOperations - a SINGLY LINKED LIST implemented from scratch with
 * a custom Node class (no Java collections used for the core logic).
 *
 * Operations: insert (beginning / end / position), delete (by value /
 * by position), search, display. Empty-list and missing-value cases are
 * handled gracefully.
 */
public class LinkedListOperations {

    /** Custom node class: each node holds one int value and a link to the next node. */
    private static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    private Node head;      // first node of the list (null = empty list)
    private int size;       // number of nodes, kept for convenience

    /** Counts steps (node visits) made by the last search() call. */
    private int lastSearchSteps;

    public LinkedListOperations() {
        this.head = null;
        this.size = 0;
        this.lastSearchSteps = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }

    public int getLastSearchSteps() {
        return lastSearchSteps;
    }

    /** Inserts a value at the BEGINNING of the list. O(1) */
    public void insertAtBeginning(int value) {
        Node newNode = new Node(value);
        newNode.next = head;
        head = newNode;
        size++;
        System.out.println(">> Inserted " + value + " at the beginning of the list.");
    }

    /** Inserts a value at the END of the list. O(n) - must walk to the tail. */
    public void insertAtEnd(int value) {
        Node newNode = new Node(value);
        if (isEmpty()) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
        System.out.println(">> Inserted " + value + " at the end of the list.");
    }

    /**
     * Inserts a value at a given 1-based position (1 = beginning).
     * Position size+1 is allowed and appends at the end.
     *
     * @return true on success, false when the position is invalid.
     */
    public boolean insertAtPosition(int value, int position) {
        if (position < 1 || position > size + 1) {
            System.out.println(">> Error: Invalid position " + position
                    + ". Valid range is 1 to " + (size + 1) + ".");
            return false;
        }
        if (position == 1) {
            insertAtBeginning(value);
            return true;
        }
        Node newNode = new Node(value);
        Node current = head;
        // Walk to the node just BEFORE the target position.
        for (int i = 1; i < position - 1; i++) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;
        size++;
        System.out.println(">> Inserted " + value + " at position " + position + ".");
        return true;
    }

    /**
     * Deletes the FIRST node containing the given value.
     * Special care is taken when the head itself must be removed.
     *
     * @return true if a node was deleted, false if the value is missing or list empty.
     */
    public boolean deleteByValue(int value) {
        if (isEmpty()) {
            System.out.println(">> Error: List is EMPTY. Nothing to delete.");
            return false;
        }
        // Case 1: the head holds the value.
        if (head.value == value) {
            head = head.next;
            size--;
            System.out.println(">> Deleted node with value " + value + " (was the head).");
            return true;
        }
        // Case 2: search the rest of the list keeping track of the previous node.
        Node previous = head;
        Node current = head.next;
        while (current != null && current.value != value) {
            previous = current;
            current = current.next;
        }
        if (current == null) {
            System.out.println(">> Value " + value + " NOT FOUND in the list.");
            return false;
        }
        previous.next = current.next; // bypass the found node
        size--;
        System.out.println(">> Deleted node with value " + value + ".");
        return true;
    }

    /**
     * Deletes the node at a given 1-based position.
     * @return true on success, false if the list is empty or position invalid.
     */
    public boolean deleteAtPosition(int position) {
        if (isEmpty()) {
            System.out.println(">> Error: List is EMPTY. Nothing to delete.");
            return false;
        }
        if (position < 1 || position > size) {
            System.out.println(">> Error: Invalid position " + position
                    + ". Valid range is 1 to " + size + ".");
            return false;
        }
        if (position == 1) {
            int v = head.value;
            head = head.next;
            size--;
            System.out.println(">> Deleted value " + v + " at position 1.");
            return true;
        }
        Node previous = head;
        for (int i = 1; i < position - 1; i++) {
            previous = previous.next;
        }
        int v = previous.next.value;
        previous.next = previous.next.next;
        size--;
        System.out.println(">> Deleted value " + v + " at position " + position + ".");
        return true;
    }

    /**
     * Linear search through the linked list, counting node visits.
     * @return the 1-based position of the first match, or -1 if not found.
     */
    public int search(int value) {
        lastSearchSteps = 0;
        Node current = head;
        int position = 1;
        while (current != null) {
            lastSearchSteps++;
            if (current.value == value) {
                return position;
            }
            current = current.next;
            position++;
        }
        return -1;
    }

    /** Displays all nodes in order. */
    public void display() {
        if (isEmpty()) {
            System.out.println(">> Linked list is EMPTY.");
            return;
        }
        System.out.print(">> Linked list [" + size + " nodes]: HEAD -> ");
        Node current = head;
        while (current != null) {
            System.out.print("[" + current.value + "]");
            if (current.next != null) {
                System.out.print(" -> ");
            }
            current = current.next;
        }
        System.out.println(" -> NULL");
    }

    // ---- Silent helpers used by the PerformanceAnalyzer benchmarks ----

    /** Insert at beginning without printing. */
    public void insertAtBeginningSilent(int value) {
        Node newNode = new Node(value);
        newNode.next = head;
        head = newNode;
        size++;
    }

    /** Insert at end without printing. */
    public void insertAtEndSilent(int value) {
        Node newNode = new Node(value);
        if (isEmpty()) {
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /** Returns the head value so the analyzer can search for a value that exists. */
    public int searchFirstValueForProbe() {
        return isEmpty() ? Integer.MIN_VALUE : head.value;
    }
}
