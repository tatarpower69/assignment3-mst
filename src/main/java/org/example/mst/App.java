package org.example.mst;

import com.google.gson.*;
import org.example.mst.algorithms.*;
import org.example.mst.graph.*;
import org.example.mst.util.OperationCounter;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class App {
    public static void main(String[] args) throws IOException {
        if (args.length < 2 || args.length % 2 != 0) {
            System.err.println("Usage: java -jar mst.jar <input1.json> <output1.json> [<input2.json> <output2.json> ...]");
            System.exit(1);
        }

        System.out.println("=== MST Comparison (Prim vs Kruskal) ===");

        // input/output pares
        for (int i = 0; i < args.length; i += 2) {
            String inputPath = args[i];
            String outputPath = args[i + 1];

            System.out.println("\n--- Processing file pair ---");
            System.out.println("Input:  " + inputPath);
            System.out.println("Output: " + outputPath);

            var graphs = GraphUtils.readGraphsFromFile(inputPath);
            JsonArray results = new JsonArray();

            for (int gIndex = 0; gIndex < graphs.size(); gIndex++) {
                Graph g = graphs.get(gIndex);
                System.out.println("\nGraph #" + (gIndex + 1) + " (" + g.vertices() + " vertices, " + g.edgesCount() + " edges)");

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

                // forming JSON
                JsonObject resultObj = new JsonObject();
                resultObj.addProperty("graph_id", gIndex + 1);


                JsonObject inputStats = new JsonObject();
                inputStats.addProperty("vertices", g.vertices());
                inputStats.addProperty("edges", g.edgesCount());
                resultObj.add("input_stats", inputStats);

                JsonObject primObj = new JsonObject();
                primObj.add("mst_edges", new Gson().toJsonTree(primRes.mst));
                primObj.addProperty("total_cost", primRes.cost);
                primObj.addProperty("operations_count", primRes.operations);
                primObj.addProperty("execution_time_ms", (endPrim - startPrim) / 1_000_000.0);
                resultObj.add("prim", primObj);

                JsonObject kruskalObj = new JsonObject();
                kruskalObj.add("mst_edges", new Gson().toJsonTree(kruskalRes.mst));
                kruskalObj.addProperty("total_cost", kruskalRes.cost);
                kruskalObj.addProperty("operations_count", kruskalRes.operations);
                kruskalObj.addProperty("execution_time_ms", (endKruskal - startKruskal) / 1_000_000.0);
                resultObj.add("kruskal", kruskalObj);

                results.add(resultObj);
            }

            JsonObject root = new JsonObject();
            root.add("results", results);


            Path outPath = Paths.get(outputPath).getParent();
            if (outPath != null && !Files.exists(outPath)) {
                Files.createDirectories(outPath);
            }

            GraphUtils.writeOutput(outputPath, root);
            System.out.println(" Results saved to " + outputPath);
        }

        System.out.println("\n All files processed successfully!");
    }
}
