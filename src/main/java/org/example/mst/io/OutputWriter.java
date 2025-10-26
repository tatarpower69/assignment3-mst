package org.example.mst.io;

import com.google.gson.JsonObject;
import org.example.mst.graph.GraphUtils;

import java.io.IOException;

public class OutputWriter {
    public static void save(String path, JsonObject data) throws IOException {
        GraphUtils.writeOutput(path, data);
    }
}
