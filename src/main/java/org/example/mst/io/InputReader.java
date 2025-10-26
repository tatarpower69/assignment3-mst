package org.example.mst.io;

import org.example.mst.graph.Graph;
import org.example.mst.graph.GraphUtils;

import java.io.IOException;
import java.util.List;

public class InputReader {
    public static List<Graph> load(String path) throws IOException {
        return GraphUtils.readGraphsFromFile(path);
    }
}
