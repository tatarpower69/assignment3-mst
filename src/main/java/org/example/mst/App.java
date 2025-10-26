package org.example.mst;

import com.google.gson.*;
import org.example.mst.algorithms.*;
import org.example.mst.graph.*;
import org.example.mst.util.OperationCounter;

import java.io.IOException;
import java.util.*;

public class App {
    public static void main(String[] args) throws IOException {
        System.out.println("=== MST Comparison (Prim vs Kruskal) ===");


        var graphs = GraphUtils.readGraphsFromFile("input/assign_3_input.json");
        JsonArray output = new JsonArray();

        for (int i = 0; i < graphs.size(); i++) {
            Graph g = graphs.get(i);
            System.out.println("\nProcessing graph #" + (i+1) + " with " + g.vertices() + " vertices");

            // Prim
            OperationCounter primCounter = new OperationCounter();
            long startPrim = System.nanoTime();
            var primRes = Prim.run(g, primCounter);
            long endPrim = System.nanoTime();

            // Kruskal
            OperationCounter kruskalCounter = new OperationCounter();
            long startKruskal = System.nanoTime();
            var kruskalRes = Kruskal.run(g, kruskalCounter);
            long endKruskal = System.nanoTime();


            JsonObject entry = new JsonObject();
            entry.addProperty("graph_index", i+1);
            entry.addProperty("vertices", g.vertices());
            entry.addProperty("edges", g.edgesCount());

            JsonObject primObj = new JsonObject();
            primObj.add("mst_edges", new Gson().toJsonTree(primRes.mst));
            primObj.addProperty("cost", primRes.cost);
            primObj.addProperty("operations", primRes.operations);
            primObj.addProperty("time_ms", (endPrim - startPrim) / 1_000_000.0);

            JsonObject kruskalObj = new JsonObject();
            kruskalObj.add("mst_edges", new Gson().toJsonTree(kruskalRes.mst));
            kruskalObj.addProperty("cost", kruskalRes.cost);
            kruskalObj.addProperty("operations", kruskalRes.operations);
            kruskalObj.addProperty("time_ms", (endKruskal - startKruskal) / 1_000_000.0);

            entry.add("prim", primObj);
            entry.add("kruskal", kruskalObj);

            output.add(entry);
        }

        JsonObject root = new JsonObject();
        root.add("results", output);


        GraphUtils.writeOutput("output/output.json", root);
        System.out.println("\n Results saved to output/output.json");
    }
}
