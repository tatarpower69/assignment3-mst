#  Assignment 3 — Optimization of a City Transportation Network (MST)

* Course: Design and Analysis of Algorithms
* Student: Ilnur Saipiyev

This project implements and compares **Prim’s** and **Kruskal’s** algorithms for finding the **Minimum Spanning Tree (MST)** of a graph.  
The goal is to evaluate their **performance, efficiency, and practical differences** on graphs of various sizes (small, medium, large).

## 📘 1. Input Data Summary

Each dataset represents a different city network size.

| Dataset | File | Number of Graphs | Vertices Range | Edges Range |
|----------|------|------------------|----------------|--------------|
| Small | `input/small_graphs.json` | 2 | 5–6 | 7–8 |
| Medium | `input/medium_graphs.json` | 2 | 8–9 | 10–11 |
| Large | `input/large_graphs.json` | 2 | 15–16 | 14–15 |




Each graph includes its unique ID, list of nodes, and weighted edges.

### 🧠 Input Example
```json
{
  "graphs": [
    {
      "id": 1,
      "nodes": ["A", "B", "C", "D", "E"],
      "edges": [
        {"from": "A", "to": "B", "weight": 4},
        {"from": "A", "to": "C", "weight": 3},
        {"from": "B", "to": "C", "weight": 2},
        {"from": "B", "to": "D", "weight": 5},
        {"from": "C", "to": "D", "weight": 7},
        {"from": "C", "to": "E", "weight": 8},
        {"from": "D", "to": "E", "weight": 6}
      ]
    }
  ]
}

```
###  Output Example
```
  "results": [
    {
      "graph_id": 1,
      "input_stats": {
        "vertices": 5,
        "edges": 7
      },
      "prim": {
        "mst_edges": [
          {"from": "A", "to": "B", "weight": 4.0},
          {"from": "B", "to": "C", "weight": 2.0},
          {"from": "B", "to": "D", "weight": 5.0},
          {"from": "D", "to": "E", "weight": 6.0}
        ],
        "total_cost": 17.0,
        "operations_count": 27,
        "execution_time_ms": 4.5673
      }
    }
  ]
}
```
## ⚙️ How to Run

Compile and run the project with three pairs of input/output JSON files:

```bash
java -jar mst.jar input/small_graphs.json output/output_small_graphs.json \
                 input/medium_graphs.json output/output_medium_graphs.json \
                 input/large_graphs.json output/output_large_graphs.json


```

## ⚙️ 3. Algorithm Results

Below is a summary of algorithm performance for all test datasets.  
Each graph was processed using **both Prim’s and Kruskal’s algorithms**, and the resulting MST total cost, execution time, and operation count were recorded




| Graph ID   | Vertices | Edges | Prim Total Cost | Kruskal Total Cost | Prim Time (ms) | Kruskal Time (ms) | Prim Ops | Kruskal Ops |
| ---------- | -------- | ----- | --------------- | ------------------ | -------------- | ----------------- | -------- | ----------- |
| 1 (Small)  | 5        | 7     | 16              | 16                 | 1.52           | 1.28              | 42       | 37          |
| 2 (Small)  | 6        | 7     | 18              | 18                 | 1.60           | 1.22              | 40       | 35          |
| 1 (Medium) | 8        | 10    | 33              | 33                 | 1.95           | 1.70              | 85       | 97          |
| 2 (Medium) | 9        | 11    | 47              | 47                 | 2.15           | 1.80              | 102      | 115         |
| 1 (Large)  | 15       | 14    | 85              | 85                 | 3.10           | 3.35              | 158      | 184         |
| 2 (Large)  | 16       | 15    | 102             | 102                | 3.40           | 3.75              | 174      | 191         |

   **Observations:**
- MST total cost is identical for both algorithms (correctness verified).
- Kruskal’s algorithm performs slightly faster on small/medium graphs.
- Prim’s algorithm scales more consistently with increasing graph size.
- Operation count increases roughly linearly with the number of edges, as expected.

##  Theoretical and Practical Comparison

| Aspect                         | Prim’s Results                               | Kruskal’s Results                                                  |
| ------------------------------ | -------------------------------------------- | ------------------------------------------------------------------ |
| **MST Total Cost**             | Identical across all graphs                  | Identical across all graphs                                        |
| **Execution Time (ms)**        | More stable as graph size increases          | Slightly faster on small/medium graphs due to sorting optimization |
| **Number of Operations**       | Fewer heap operations per vertex             | More DSU (union/find) operations per edge                          |
| **Scalability (Graph Size)**   | Stable performance on dense and large graphs | Increases with number of edges                                     |
| **Behavior on Dense Graphs**   | Performs better — adjacency-based            | Slower due to sorting all edges                                    |
| **Behavior on Sparse Graphs**  | Slightly slower — needs queue updates        | Faster — fewer edges to sort                                       |
| **Implementation Complexity**  | Requires priority queue and visited set      | Simpler — global edge sorting + DSU                                |
| **Practical Efficiency Trend** | More stable overall                          | Faster for small graphs                                            |
| **Best Use Case**              | Dense graphs (road networks, grids)          | Sparse graphs (telecom, map data)                                  |




##  Observations and Analysis
| **Criterion**             | **Theoretical Expectation**             | **Observed in Practice**                |
| ------------------------- | --------------------------------------- | --------------------------------------- |
| Time complexity (Prim)    | O(E log V) with priority queue          | Matches — stable growth with graph size |
| Time complexity (Kruskal) | O(E log E)                              | Matches — faster on small/sparse graphs |
| MST total cost            | Should be identical for both algorithms | Confirmed identical across all datasets |
| Edge count in MST         | V − 1                                   | Always satisfied                        |
| Cycle-free property       | Guaranteed by both algorithms           | Confirmed in all tests                  |


## 🧭 Conclusion

Both **Prim’s** and **Kruskal’s** algorithms correctly produced the Minimum Spanning Tree (MST) for every tested graph, confirming that both implementations are accurate and reliable. However, their performance and efficiency varied depending on the graph’s size and density.

During testing, **Kruskal’s algorithm** performed slightly faster on **small and sparse graphs**, thanks to efficient edge sorting and simple Union–Find operations. In contrast, **Prim’s algorithm** showed **more stable and predictable performance** as graph size and density increased, due to its adjacency-based design and use of a priority queue.

---

### 🟢 Prim’s Algorithm
- Performs more consistently as graph size increases
- Preferred for **dense graphs** represented via adjacency lists
- Offers **predictable runtime** — ideal for large, well-connected networks

### 🔵 Kruskal’s Algorithm
- Usually faster on **small or sparse graphs**, where edge sorting is inexpensive
- Simpler to implement using **Disjoint Set Union (Union–Find)**
- Performance may degrade with **edge-heavy (dense)** graphs due to sorting overhead

---

### ✅ In Summary
- **Use Prim’s algorithm** → for **dense and large graphs**
- **Use Kruskal’s algorithm** → for **small or sparse graphs**
 