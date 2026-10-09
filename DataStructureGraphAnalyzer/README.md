# Data Structure & Graph Performance Analyzer (Java)

**CIT300 – Data Structures and Algorithms – Graded Practical Assignment 2**

A console-based Java application that implements core data structures,
searching algorithms, graph operations (BFS/DFS), and a performance /
Big-O complexity analyzer — all integrated behind one interactive menu.

## Features

| Menu | Component | What it does |
|------|-----------|--------------|
| 1 | Array Operations | Fixed-capacity array with logical size: insert, delete, search (with comparison count + `System.nanoTime()` timing), display. Handles full/empty arrays, invalid positions, missing values. |
| 2 | Stack Operations | Array-based LIFO stack: push, pop, peek, display. Handles overflow and underflow. |
| 3 | Queue Operations | **Circular array queue** (space reused after dequeue): enqueue, dequeue, peek/front, display. Handles full/empty conditions. |
| 4 | Linked List Operations | Singly linked list built from scratch with a custom `Node` class: insert (begin/end/position), delete (by value/position), search, display. Handles empty lists and missing values. |
| 5 | Searching Operations | Linear Search O(n) and Binary Search O(log n). Binary search runs on a **sorted copy** so the original data is preserved. Reports found position, comparison counts, and nanosecond timings, plus a side-by-side comparison table. |
| 6 | Graph Operations | Undirected graph using an **adjacency list** (`HashMap<String, ArrayList<String>>`): add vertex, add edge (validates vertices, rejects duplicates/self-loops), display, search vertex, **BFS** (FIFO queue) and **DFS** (LIFO stack) with start-vertex selection, visit order, visited counts, edge examinations, and `System.nanoTime()` timing. Detects disconnected vertices. |
| 7 | Performance Comparison | Runs the **real implemented algorithms**: Linear vs Binary search, BFS vs DFS on the same graph and start vertex, and array/stack/queue/linked-list operation benchmarks. Shows operation, structure, result, steps, time (ns), and expected Big-O. Measured results are always presented separately from theory, with a note that actual times depend on hardware, JVM warm-up, and input size. |
| 8 | Display All Results | Shows the current state of every structure (safe when empty) plus the full performance history table. |
| 9 | Exit | Clean shutdown; closes the input resource. |

Input validation: non-numeric input is rejected with friendly messages,
invalid menu choices never crash the program, and the user can always
return to the main menu (option `0` in every submenu).

## Requirements

* **Java 8 or newer** (developed and tested with OpenJDK 17).
* No external libraries — pure standard Java (`java.util.Scanner`, collections used only for the graph adjacency list).

## Project Structure

```
DataStructureGraphAnalyzer/
├── src/
│   ├── Main.java                  # menu loop, wires everything together
│   ├── ArrayOperations.java       # fixed-capacity array with logical size
│   ├── StackOperations.java       # array-based LIFO stack
│   ├── QueueOperations.java       # circular array queue
│   ├── LinkedListOperations.java  # singly linked list with custom Node
│   ├── SearchingAlgorithms.java   # linear + binary search with timing
│   ├── GraphOperations.java       # adjacency-list graph, BFS, DFS
│   ├── PerformanceAnalyzer.java   # real benchmarks + history table
│   └── InputHandler.java          # safe validated Scanner input helper
├── bin/                           # compiled .class files (created by you)
└── README.md
```

## How to Compile and Run

From a terminal, inside the `DataStructureGraphAnalyzer` folder:

```bash
# 1. Create the output folder for class files
mkdir -p bin

# 2. Compile every source file
javac -d bin src/*.java

# 3. Run the application
java -cp bin Main
```

(If your JDK tools are not on the PATH, replace `javac`/`java` with the
full path, e.g. `/usr/lib/jvm/java-17-openjdk/bin/javac`.)

## Quick Test Script

After starting the program you can try this flow:

1. Choose `6` → add vertices `A B C D E`, edges `A-B, B-C, C-D, A-C`,
   then run BFS and DFS from `A` (vertex `E` stays disconnected — the
   program notes this).
2. Choose `5` → option `1` to enter a dataset (e.g. `5 10 3 8 25 42 17 8`)
   → option `4` to compare linear vs binary search for `25`.
3. Choose `7` → options `1`, `2`, `3` to run the performance comparisons.
4. Choose `8` → view all structures and the performance history.
5. Choose `9` → exit.

## Notes on Performance Measurements

* Times are measured with `System.nanoTime()` around the real algorithm calls.
* Comparison/step counts (search comparisons, edge examinations, node visits)
  are counted inside the algorithms themselves.
* The program deliberately shows measured numbers **separately** from the
  theoretical Big-O analysis and warns that a single measurement cannot
  prove one algorithm is always faster — input size, hardware, JIT
  compilation and garbage collection all affect wall-clock time.

## Design / Code Quality

* Object-oriented: each component is its own class with encapsulated state
  (`private` fields, public operations).
* Beginner-friendly: plain loops, clear variable names, explanatory comments
  on every algorithm (circular-queue wrap-around, binary-search halving,
  BFS queue vs DFS stack, etc.).
* All classes live in the default package so the project compiles with a
  single `javac` command from a clean checkout.

## License

Provided for academic submission purposes (CIT300 Assignment 2).
