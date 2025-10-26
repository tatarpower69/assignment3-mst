package org.example.mst.graph;

import java.util.*;

public class Graph {
    private final Map<String, Integer> nameToIndex = new LinkedHashMap<>();
    private final List<String> indexToName = new ArrayList<>();
    private final List<List<Edge>> adj = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public Graph(List<String> nodes) {
        for (String n : nodes) addNode(n);
    }

    public int addNode(String name) {
        if (nameToIndex.containsKey(name)) return nameToIndex.get(name);
        int idx = indexToName.size();
        nameToIndex.put(name, idx);
        indexToName.add(name);
        adj.add(new ArrayList<>());
        return idx;
    }

    public void addEdge(String from, String to, double weight) {
        int u = addNode(from);
        int v = addNode(to);
        Edge e = new Edge(u, v, weight, from, to);
        edges.add(e);
        adj.get(u).add(e);
        // add reverse for undirected
        Edge e2 = new Edge(v, u, weight, to, from);
        adj.get(v).add(e2);
    }

    public int vertices() { return indexToName.size(); }
    public int edgesCount() { return edges.size(); }
    public List<Edge> getEdges() { return Collections.unmodifiableList(edges); }
    public List<List<Edge>> getAdj() { return adj; }
    public String nameOf(int idx) { return indexToName.get(idx); }
}
