package org.example.mst.algorithms;


import org.example.mst.graph.*;
import org.example.mst.util.OperationCounter;

import java.util.*;

public class Prim {
    public static class Result {
        public final List<Edge> mst;
        public final double cost;
        public final long operations;
        public Result(List<Edge> m, double c, long ops) { mst = m; cost = c; operations = ops; }
    }

    public static Result run(Graph g, OperationCounter oc) {
        int n = g.vertices();
        if (n==0) return new Result(Collections.emptyList(), 0, oc.total());
        boolean[] used = new boolean[n];
        double[] minEdge = new double[n];
        int[] parent = new int[n];
        Arrays.fill(minEdge, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        minEdge[0]=0;
        pq.add(new int[]{0,0});
        List<Edge> res = new ArrayList<>();
        double cost = 0;
        while(!pq.isEmpty()){
            oc.incOther();
            int[] top = pq.poll();
            int v = top[0];
            if (used[v]) continue;
            used[v] = true;
            if (parent[v] != -1) {
                int u = parent[v];
                String from = g.nameOf(u);
                String to = g.nameOf(v);
                double weight = minEdge[v];
                res.add(new Edge(u, v, weight, from, to));
                cost += weight;
            }
            for (Edge e : g.getAdj().get(v)) {
                oc.incComparisons();
                int to = e.v;
                if (!used[to] && e.weight < minEdge[to]) {
                    minEdge[to] = e.weight;
                    parent[to] = v;
                    pq.add(new int[]{to, 0});
                    oc.incOther();
                }
            }
        }
        return new Result(res, cost, oc.total());
    }
}
