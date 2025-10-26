package org.example.mst.graph;


import com.google.gson.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class GraphUtils {
    public static List<Graph> readGraphsFromFile(String path) throws IOException {
        String json = Files.readString(Paths.get(path));
        JsonObject root = JsonParser.parseString(json).getAsJsonObject();
        JsonArray graphs = root.getAsJsonArray("graphs");
        List<Graph> result = new ArrayList<>();
        for (JsonElement g : graphs) {
            JsonObject obj = g.getAsJsonObject();
            JsonArray nodes = obj.getAsJsonArray("nodes");
            List<String> nodeList = new ArrayList<>();
            for (JsonElement n : nodes) nodeList.add(n.getAsString());
            Graph graph = new Graph(nodeList);
            JsonArray edges = obj.getAsJsonArray("edges");
            for (JsonElement e : edges) {
                JsonObject eo = e.getAsJsonObject();
                String from = eo.get("from").getAsString();
                String to = eo.get("to").getAsString();
                double w = eo.get("weight").getAsDouble();
                graph.addEdge(from, to, w);
            }
            result.add(graph);
        }
        return result;
    }

    public static void writeOutput(String path, JsonObject obj) throws IOException {
        Files.writeString(Paths.get(path), new GsonBuilder().setPrettyPrinting().create().toJson(obj));
    }
}
