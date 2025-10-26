package org.example.mst.algorithms;



import org.example.mst.graph.*;
import org.example.mst.util.OperationCounter;

import java.util.*;

public class Kruskal {
    public static class Result {
        public final List<Edge> mst;
        public final double cost;
        public final long operations;

        public Result(List<Edge> mst, double cost, long operations) {
            this.mst = mst; this.cost = cost; this.operations = operations;
        }
    }

    private static class DSU {
        int[] p, r;
        OperationCounter oc;
        DSU(int n, OperationCounter oc) { p = new int[n]; r = new int[n]; this.oc = oc; for (int i=0;i<n;i++) p[i]=i; }
        int find(int a){ oc.incComparisons(); if (p[a]==a) return a; return p[a]=find(p[a]); }
        boolean union(int a,int b){
            a=find(a); b=find(b);
            if (a==b) return false;
            oc.incUnions();
            if (r[a]<r[b]) p[a]=b;
            else if (r[b]<r[a]) p[b]=a;
            else { p[b]=a; r[a]++; }
            return true;
        }
    }

    public static Result run(Graph g, OperationCounter oc) {
        List<Edge> edges = new ArrayList<>(g.getEdges());
        edges.sort(Comparator.naturalOrder());
        DSU dsu = new DSU(g.vertices(), oc);
        List<Edge> out = new ArrayList<>();
        double cost = 0;
        for (Edge e : edges) {
            oc.incOther();
            if (dsu.union(e.u, e.v)) {
                out.add(e);
                cost += e.weight;
                if (out.size() == g.vertices()-1) break;
            }
        }
        return new Result(out, cost, oc.total());
    }
}
