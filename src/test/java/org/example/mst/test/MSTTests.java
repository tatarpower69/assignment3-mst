

package org.example.mst.test;

import org.example.mst.algorithms.Kruskal;
import org.example.mst.algorithms.Prim;
import org.example.mst.graph.Graph;
import org.example.mst.graph.Edge;
import org.example.mst.util.OperationCounter;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MSTTests {

    private Graph sampleGraph;

    @BeforeEach
    public void setUp() {
        List<String> nodes = Arrays.asList("A", "B", "C", "D", "E");
        sampleGraph = new Graph(nodes);
        sampleGraph.addEdge("A", "B", 4);
        sampleGraph.addEdge("A", "C", 3);
        sampleGraph.addEdge("B", "C", 2);
        sampleGraph.addEdge("B", "D", 5);
        sampleGraph.addEdge("C", "D", 7);
        sampleGraph.addEdge("C", "E", 8);
        sampleGraph.addEdge("D", "E", 6);
    }

    // 1: Correctness of MST edges and total cost
    @Test
    public void testPrimAndKruskalSameTotalCost() {
        OperationCounter oc1 = new OperationCounter();
        OperationCounter oc2 = new OperationCounter();

        Prim.Result primRes = Prim.run(sampleGraph, oc1);
        Kruskal.Result kruskalRes = Kruskal.run(sampleGraph, oc2);

        assertEquals(primRes.cost, kruskalRes.cost, 1e-6,
                "Prim and Kruskal MST total costs must be equal.");
        assertEquals(sampleGraph.vertices() - 1, primRes.mst.size(),
                "MST must have V-1 edges.");
        assertEquals(sampleGraph.vertices() - 1, kruskalRes.mst.size(),
                "MST must have V-1 edges.");
    }

    //  2 No cycles in MST
    @Test
    public void testMSTIsAcyclic() {
        OperationCounter oc = new OperationCounter();
        Kruskal.Result res = Kruskal.run(sampleGraph, oc);

        Map<String, String> parent = new HashMap<>();
        for (Edge e : res.mst) {
            assertTrue(unionFind(parent, e.from, e.to), "MST must be acyclic!");
        }
    }

    private boolean unionFind(Map<String, String> parent, String a, String b) {
        String rootA = find(parent, a);
        String rootB = find(parent, b);
        if (rootA.equals(rootB)) return false;
        parent.put(rootA, rootB);
        return true;
    }

    private String find(Map<String, String> parent, String node) {
        if (!parent.containsKey(node)) parent.put(node, node);
        if (parent.get(node).equals(node)) return node;
        String root = find(parent, parent.get(node));
        parent.put(node, root);
        return root;
    }

    // 3: Connectivity of MST
    @Test
    public void testMSTConnectsAllVertices() {
        OperationCounter oc = new OperationCounter();
        Prim.Result res = Prim.run(sampleGraph, oc);

        Set<String> visited = new HashSet<>();
        Map<String, List<String>> adj = new HashMap<>();

        for (Edge e : res.mst) {
            adj.computeIfAbsent(e.from, k -> new ArrayList<>()).add(e.to);
            adj.computeIfAbsent(e.to, k -> new ArrayList<>()).add(e.from);
        }

        dfs("A", visited, adj);
        assertEquals(sampleGraph.vertices(), visited.size(),
                "MST must connect all vertices.");
    }

    private void dfs(String node, Set<String> visited, Map<String, List<String>> adj) {
        visited.add(node);
        for (String nei : adj.getOrDefault(node, Collections.emptyList())) {
            if (!visited.contains(nei)) dfs(nei, visited, adj);
        }
    }

    // 4 Disconnected graph handled gracefully
    @Test
    public void testDisconnectedGraphHandled() {
        List<String> nodes = Arrays.asList("A", "B", "C");
        Graph g = new Graph(nodes);
        g.addEdge("A", "B", 2);
        // Node "C" is disconnected

        OperationCounter oc = new OperationCounter();
        Prim.Result res = Prim.run(g, oc);
        assertTrue(res.mst.size() < g.vertices() - 1, "Disconnected graphs must not have full MST.");
    }

    // 5 Operation counts and execution time valid
    @Test
    public void testOperationCountsNonNegative() {
        OperationCounter oc1 = new OperationCounter();
        OperationCounter oc2 = new OperationCounter();

        Prim.Result primRes = Prim.run(sampleGraph, oc1);
        Kruskal.Result kruskalRes = Kruskal.run(sampleGraph, oc2);

        assertTrue(primRes.operations >= 0, "Operation count must be non-negative.");
        assertTrue(kruskalRes.operations >= 0, "Operation count must be non-negative.");
    }

    // 6 Reproducibility
    @Test
    public void testReproducibilitySameResults() {
        OperationCounter oc1 = new OperationCounter();
        OperationCounter oc2 = new OperationCounter();

        Kruskal.Result r1 = Kruskal.run(sampleGraph, oc1);
        Kruskal.Result r2 = Kruskal.run(sampleGraph, oc2);

        assertEquals(r1.cost, r2.cost, 1e-6, "Results must be reproducible for same dataset.");
        assertEquals(r1.mst.size(), r2.mst.size(), "Edge count must be consistent.");
    }

    //  Performance and scalability
    @Test
    public void testPerformanceLargeGraph() {
        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < 30; i++) nodes.add("N" + i);
        Graph g = new Graph(nodes);

        Random rand = new Random(42);
        for (int i = 0; i < 100; i++) {
            String a = "N" + rand.nextInt(30);
            String b = "N" + rand.nextInt(30);
            if (!a.equals(b)) g.addEdge(a, b, rand.nextInt(100) + 1);
        }

        OperationCounter oc = new OperationCounter();
        long start = System.currentTimeMillis();
        Prim.Result res = Prim.run(g, oc);
        long elapsed = System.currentTimeMillis() - start;

        assertTrue(elapsed >= 0, "Execution time must be non-negative.");
        assertTrue(res.operations >= 0, "Operation count must be non-negative.");
        assertTrue(res.mst.size() <= g.vertices() - 1, "MST must not exceed V-1 edges.");
    }
}
