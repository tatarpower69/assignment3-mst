package org.example.mst.graph;


public class Edge implements Comparable<Edge> {
    public final int u;
    public final int v;
    public final double weight;
    public final String from;
    public final String to;

    public Edge(int u, int v, double weight, String from, String to) {
        this.u = u;
        this.v = v;
        this.weight = weight;
        this.from = from;
        this.to = to;
    }

    @Override
    public int compareTo(Edge o) {
        return Double.compare(this.weight, o.weight);
    }

    @Override
    public String toString() {
        return String.format("{\"from\":\"%s\",\"to\":\"%s\",\"weight\":%s}", from, to, weight);
    }
}
