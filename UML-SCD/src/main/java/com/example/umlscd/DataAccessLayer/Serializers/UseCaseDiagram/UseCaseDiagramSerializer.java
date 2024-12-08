package com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram;

import com.example.umlscd.BusinessLayer.UseCaseDiagram.UseCaseDiagramManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * <h1>Use Case Diagram Serializer</h1>
 *
 * <p>The {@code UseCaseDiagramSerializer} class provides functionalities to serialize and deserialize
 * {@code UseCaseDiagramManager} instances to and from JSON files. It leverages the Jackson {@code ObjectMapper}
 * to convert the diagram manager objects into JSON format for saving and to reconstruct them from JSON files
 * when loading. This facilitates interoperability, ease of sharing, and human-readable storage of use case diagrams.</p>
 *
 * <p>By utilizing JSON serialization, this class ensures that the state of a use case diagram, including its
 * objects and associations, can be persisted and retrieved efficiently. The class supports pretty printing to
 * enhance the readability of the generated JSON files.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class UseCaseDiagramSerializer {

    /**
     * The {@code ObjectMapper} instance used for JSON serialization and deserialization.
     *
     * <p>This object is thread-safe after configuration and can be used to perform multiple operations.</p>
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves the provided {@code UseCaseDiagramManager} instance to a JSON file at the specified file path.
     *
     * <p>This method serializes the {@code UseCaseDiagramManager} object into JSON format and writes it to the
     * designated file. It utilizes pretty printing to ensure the JSON output is well-formatted and easily readable.</p>
     *
     * <p><b>Example Usage:</b></p>
     * <pre>
     * UseCaseDiagramManager manager = new UseCaseDiagramManager();
     * // ... populate manager with objects and associations ...
     * UseCaseDiagramSerializer.saveDiagram(manager, "diagram.json");
     * </pre>
     *
     * @param diagramManager The {@code UseCaseDiagramManager} instance to be serialized and saved.
     * @param filePath       The file path where the JSON representation of the diagram will be saved.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    public static void saveDiagram(UseCaseDiagramManager diagramManager, String filePath) throws IOException {
        // Use pretty print to format the JSON output
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), diagramManager);
    }

    /**
     * Loads a {@code UseCaseDiagramManager} instance from a JSON file at the specified file path.
     *
     * <p>This method reads the JSON file, deserializes its content, and reconstructs the {@code UseCaseDiagramManager}
     * object, including all its contained objects and associations. It ensures that the diagram's state is accurately
     * restored from the JSON representation.</p>
     *
     * <p><b>Example Usage:</b></p>
     * <pre>
     * UseCaseDiagramManager manager = UseCaseDiagramSerializer.loadDiagram("diagram.json");
     * if (manager != null) {
     *     // ... use the loaded manager ...
     * }
     * </pre>
     *
     * @param filePath The file path from which the JSON representation of the diagram will be read and deserialized.
     * @return The reconstructed {@code UseCaseDiagramManager} instance if successful; {@code null} otherwise.
     * @throws IOException If an I/O error occurs during the reading process or if the JSON is malformed.
     */
    public static UseCaseDiagramManager loadDiagram(String filePath) throws IOException {
        // Deserialize the JSON file back into the UseCaseDiagramManager object
        return objectMapper.readValue(new File(filePath), UseCaseDiagramManager.class);
    }
}
