package com.example.umlscd;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class UseCaseDiagramSerializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Method to save the diagram to a JSON file
    public static void saveDiagram(UseCaseDiagramManager diagramManager, String filePath) throws IOException {
        // Use pretty print to format the JSON output
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), diagramManager);
    }

    // Method to load the diagram from a JSON file
    public static UseCaseDiagramManager loadDiagram(String filePath) throws IOException {
        // Deserialize the JSON file back into the UseCaseDiagramManager object
        return objectMapper.readValue(new File(filePath), UseCaseDiagramManager.class);
    }
}