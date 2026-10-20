# DAA Assignment 3 — Graph Algorithms

## 1. Project Overview

This project implements and evaluates graph algorithms for the Design and Analysis of Algorithms (DAA) course.

The project includes:

- Disjoint Set Union (DSU)
- Minimum Spanning Tree (MST) algorithms: Kruskal and Prim
- Strongly Connected Components (SCC): Kosaraju's algorithm
- Topological sorting using Kahn's algorithm
- Shortest paths in Directed Acyclic Graphs (DAGs)
- Graph generation and performance benchmarking
- Automated unit tests
- Benchmark visualization

## 2. Technologies

- Java 17
- Maven
- JUnit 5
- IntelliJ IDEA

## 3. Project Structure

```text
DAA_Assignment3/
├── pom.xml
├── README.md
├── REPORT.md
├── .gitignore
├── results/
│   ├── benchmark.csv
│   └── plots/
│       ├── dag_algorithms.png
│       ├── directed_algorithms.png
│       └── mst_comparison.png
└── src/
    ├── main/
    │   └── java/com/daa/
    │       ├── algorithms/
    │       │   ├── DAGShortestPath.java
    │       │   ├── DAGShortestPathResult.java
    │       │   ├── DSU.java
    │       │   ├── Kahn.java
    │       │   ├── KahnResult.java
    │       │   ├── Kosaraju.java
    │       │   ├── Kruskal.java
    │       │   ├── Metrics.java
    │       │   ├── MSTResult.java
    │       │   ├── Prim.java
    │       │   └── SCCResult.java
    │       ├── benchmark/
    │       │   ├── Benchmark.java
    │       │   ├── GraphGenerator.java
    │       │   └── GraphPlotter.java
    │       └── graph/
    │           ├── Edge.java
    │           └── Graph.java
    └── test/
        └── java/com/daa/
            ├── algorithms/
            ├── benchmark/
            └── graph/
```

## 4. Requirements

- JDK 17 or later
- Maven
- IntelliJ IDEA or another Java IDE

Verify the Java and Maven installations:

```bash
java -version
mvn -version
```

## 5. Build and Run Tests

Open the project directory in a terminal and execute:

```bash
mvn clean test
```

To compile the project and package it:

```bash
mvn clean package
```

The unit tests cover graph operations, algorithm behavior, and graph generation, including edge cases.

## 6. Algorithms

### 6.1 Disjoint Set Union (DSU)

DSU maintains a collection of disjoint sets. It supports finding the representative of a set and merging two sets.

It is used by Kruskal's algorithm to detect whether adding an edge would create a cycle.

### 6.2 Kruskal's Algorithm

Kruskal's algorithm constructs a minimum spanning forest by processing edges in increasing order of weight and adding edges that connect different components.

Expected time complexity: \(O(E \log E)\).

### 6.3 Prim's Algorithm

Prim's algorithm constructs a minimum spanning tree by repeatedly selecting the minimum-weight edge that connects the current tree to another vertex.

With an appropriate priority queue and adjacency-list representation, its expected time complexity is \(O(E \log V)\).

### 6.4 Kosaraju's Algorithm

Kosaraju's algorithm finds strongly connected components in a directed graph using depth-first search and the reversed graph.

Time complexity: \(O(V+E)\).

### 6.5 Kahn's Algorithm

Kahn's algorithm performs topological sorting using vertex in-degrees and a queue of vertices with zero in-degree.

Time complexity: \(O(V+E)\).

### 6.6 DAG Shortest Paths

The DAG shortest-path algorithm processes vertices in topological order and relaxes outgoing edges.

Time complexity: \(O(V+E)\).

Because the graph is acyclic, negative edge weights can be supported without the negative-cycle problem encountered in general shortest-path graphs.

## 7. Benchmarking

The benchmark implementation is located in:

```text
src/main/java/com/daa/benchmark/Benchmark.java
```

The graph generator is located in:

```text
src/main/java/com/daa/benchmark/GraphGenerator.java
```

The benchmark evaluates the graph algorithms using generated graphs of different sizes and densities.

The measurements are saved to:

```text
results/benchmark.csv
```

The CSV file contains benchmark measurements and algorithm-related counters.

### Algorithms evaluated

- Kruskal and Prim on undirected graphs
- Kosaraju on directed graphs
- Kahn's algorithm on DAGs
- DAG shortest paths on DAGs

### Running the benchmark

In IntelliJ IDEA:

1. Open `Benchmark.java`.
2. Find the `main` method.
3. Click the Run icon next to the method or class.
4. Wait for the benchmark to finish.
5. Check `results/benchmark.csv`.

Run the benchmark after the source code has been compiled and tested.

## 8. Benchmark Plots

The graph plotting implementation is located in:

```text
src/main/java/com/daa/benchmark/GraphPlotter.java
```

The generated plots are stored in:

```text
results/plots/
```

The current plot files are:

- `mst_comparison.png` — MST algorithm comparison
- `directed_algorithms.png` — directed graph algorithm results
- `dag_algorithms.png` — DAG algorithm results

Run `GraphPlotter.java` using IntelliJ IDEA if the plots need to be regenerated. If the class requires arguments or a particular execution order, follow its implementation.

## 9. Complexity Summary

Let \(V\) represent the number of vertices and \(E\) the number of edges.

| Algorithm | Expected time complexity |
|---|---|
| DSU | Amortized \(O(\alpha(V))\) per operation with path compression and union by rank or size |
| Kruskal | \(O(E \log E)\) |
| Prim | \(O(E \log V)\) with a suitable priority queue and adjacency-list representation |
| Kosaraju | \(O(V+E)\) |
| Kahn | \(O(V+E)\) |
| DAG shortest paths | \(O(V+E)\) |

Actual complexity depends on the data structures and implementation details.

## 10. Correctness and Testing

Correctness is checked using automated unit tests.

Important properties include:

- DSU correctly finds and merges connected components.
- Kruskal and Prim produce consistent MST results on the same undirected graph.
- Kosaraju identifies strongly connected components.
- Kahn's algorithm returns a valid topological ordering for a DAG.
- DAG shortest paths calculate distances according to the graph's edge weights.
- Graph generation respects the specified graph type and edge constraints.

Run all tests before submitting:

```bash
mvn clean test
```

## 11. Reproducibility

The graph generator uses a random seed to make graph generation reproducible when the same parameters and seed are used.

Benchmark timings can vary depending on hardware, operating system, Java runtime, and system load. For meaningful comparisons, run experiments under similar conditions.

## 12. AI Assistance Acknowledgment

AI assistance was used to support implementation planning, code explanations, debugging, and documentation. The final implementation and results should be reviewed and verified by the student.

This statement should accurately reflect the assistance used and comply with the course's academic integrity policy.

## 13. Submission Checklist

- [ ] Run `mvn clean test` and verify that all tests pass.
- [ ] Run the benchmark and verify `results/benchmark.csv`.
- [ ] Verify that all three plot files exist and display correctly.
- [ ] Complete `REPORT.md` using actual benchmark measurements.
- [ ] Check that the README instructions match the implementation.
- [ ] Commit the required project files to Git.
- [ ] Verify the required GitHub repository and `main` branch.
- [ ] Create the required Git tag `v1.0`.
- [ ] Create the submission archive using the required format: `DAA_Assignment3_name_surname_group.zip`.
- [ ] Upload the ZIP archive to Moodle and verify the GitHub submission.

