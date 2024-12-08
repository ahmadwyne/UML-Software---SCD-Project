package com.example.umlscd.DataAccessLayer.Serializers.ClassDiagram;

import com.example.umlscd.BusinessLayer.ClassDiagram.ClassDiagramManager;
import com.example.umlscd.Models.ClassDiagram.ClassDiagramD;
import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import com.example.umlscd.Models.ClassDiagram.UMLInterfaceBox;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Platform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Class Diagram Serializer</h1>
 *
 * <p>The {@code ClassDiagramSerializer} class is responsible for handling the serialization and deserialization
 * of the UML Class Diagram within the UML Editor application. It leverages the Jackson library to convert
 * the {@code ClassDiagramD} model into JSON format for persistence and to reconstruct the model from JSON data.
 * Additionally, it facilitates the restoration of the diagram within the user interface by interfacing with
 * the {@code ClassDiagramManager}.</p>
 *
 * <p>Key functionalities include:</p>
 * <ul>
 *     <li>Serializing the {@code ClassDiagramD} object to a JSON file.</li>
 *     <li>Deserializing a JSON file back into a {@code ClassDiagramD} object.</li>
 *     <li>Restoring the diagram in the UI based on the deserialized data.</li>
 *     <li>Ensuring thread safety by performing UI updates on the JavaFX application thread.</li>
 * </ul>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class ClassDiagramSerializer {

    /**
     * The {@code ObjectMapper} instance from the Jackson library used for JSON processing.
     *
     * <p>This mapper is configured to produce indented (pretty-printed) JSON output for better readability.</p>
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructs a {@code ClassDiagramSerializer} with a configured {@code ObjectMapper}.
     *
     * <p>The {@code ObjectMapper} is set to enable {@code SerializationFeature.INDENT_OUTPUT} to produce
     * nicely formatted JSON output.</p>
     */
    public ClassDiagramSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Serializes the {@code ClassDiagramD} object to a JSON file.
     *
     * <p>This method converts the provided {@code ClassDiagramD} instance into JSON format and writes it
     * to the specified file. It throws an {@code IOException} if an I/O error occurs during the process.</p>
     *
     * @param diagram The {@code ClassDiagramD} instance to serialize.
     * @param file    The {@code File} object representing the destination JSON file.
     * @throws IOException If an I/O error occurs during serialization.
     */
    public void serialize(ClassDiagramD diagram, File file) throws IOException {
        objectMapper.writeValue(file, diagram);
    }

    /**
     * Deserializes a JSON file into a {@code ClassDiagramD} object.
     *
     * <p>This method reads the JSON content from the specified file and converts it back into a
     * {@code ClassDiagramD} instance. It throws an {@code IOException} if an I/O error occurs during
     * the process.</p>
     *
     * @param file The {@code File} object representing the source JSON file.
     * @return The deserialized {@code ClassDiagramD} instance.
     * @throws IOException If an I/O error occurs during deserialization.
     */
    public ClassDiagramD deserialize(File file) throws IOException {
        ClassDiagramD diagram = objectMapper.readValue(file, ClassDiagramD.class);
        System.out.println("Deserialized diagram: ");
        System.out.println("Classes: " + diagram.getClasses());
        System.out.println("Interfaces: " + diagram.getInterfaces());
        System.out.println("Relationships: " + diagram.getRelationships());
        return diagram;
    }

    /**
     * Restores the diagram in the UI based on the deserialized data.
     *
     * <p>This method takes a deserialized {@code ClassDiagramD} object and uses the provided
     * {@code ClassDiagramManager} to recreate the UML classes, interfaces, and relationships in the
     * user interface. It ensures that all UI updates occur on the JavaFX application thread by utilizing
     * {@code Platform.runLater}.</p>
     *
     * @param diagram The deserialized {@code ClassDiagramD} object containing UML diagram data.
     * @param manager The {@code ClassDiagramManager} responsible for updating the UI.
     */
    public void restoreDiagram(ClassDiagramD diagram, ClassDiagramManager manager) {
        System.out.println("Restoring diagram...");

        // Restore classes (ensure we do this on JavaFX application thread)
        Platform.runLater(() -> {
            restoreClasses(diagram.getClasses(), manager);
            restoreInterfaces(diagram.getInterfaces(), manager);
            restoreRelationships(diagram.getRelationships(), manager);
        });
    }

    /**
     * Restores the UML classes in the UI based on the provided list.
     *
     * <p>This helper method iterates over the list of {@code UMLClassBox} instances and delegates
     * the recreation of each class to the {@code ClassDiagramManager}. It also logs the restoration
     * process for debugging purposes.</p>
     *
     * @param classes The list of {@code UMLClassBox} instances to restore.
     * @param manager The {@code ClassDiagramManager} responsible for updating the UI.
     */
    private void restoreClasses(List<UMLClassBox> classes, ClassDiagramManager manager) {
        System.out.println("Restoring classes...");

        List<UMLClassBox> classBoxesCopy = new ArrayList<>(classes);
        for (UMLClassBox umlClass : classBoxesCopy) {
            System.out.println("Restoring class: " + umlClass.getName());
            manager.reCreateClassBox(
                    umlClass.getName(),
                    umlClass.getX(),
                    umlClass.getY(),
                    umlClass.getAttributes(),
                    umlClass.getMethods()
            );
        }

        // Debugging: Print the classBoxMap contents
        System.out.println("classBoxMap after restoring classes: " + manager.getClassBoxMap().keySet());
    }

    /**
     * Restores the UML interfaces in the UI based on the provided list.
     *
     * <p>This helper method iterates over the list of {@code UMLInterfaceBox} instances and delegates
     * the recreation of each interface to the {@code ClassDiagramManager}. It also logs the restoration
     * process for debugging purposes.</p>
     *
     * @param interfaces The list of {@code UMLInterfaceBox} instances to restore.
     * @param manager    The {@code ClassDiagramManager} responsible for updating the UI.
     */
    private void restoreInterfaces(List<UMLInterfaceBox> interfaces, ClassDiagramManager manager) {
        System.out.println("Restoring interfaces...");

        List<UMLInterfaceBox> interfaceBoxesCopy = new ArrayList<>(interfaces);
        for (UMLInterfaceBox umlInterface : interfaceBoxesCopy) {
            System.out.println("Restoring interface: " + umlInterface.getName());
            manager.reCreateInterfaceBox(umlInterface.getName(), umlInterface.getX(), umlInterface.getY(), umlInterface.getMethods());
        }

        // Debugging: Print the classBoxMap contents
        System.out.println("classBoxMap after restoring interfaces: " + manager.getClassBoxMap().keySet());
    }

    /**
     * Restores the UML relationships in the UI based on the provided list.
     *
     * <p>This helper method iterates over the list of {@code UMLRelationship} instances and delegates
     * the recreation of each relationship to the {@code ClassDiagramManager}. It also logs the restoration
     * process for debugging purposes.</p>
     *
     * @param relationships The list of {@code UMLRelationship} instances to restore.
     * @param manager       The {@code ClassDiagramManager} responsible for updating the UI.
     */
    private void restoreRelationships(List<UMLRelationship> relationships, ClassDiagramManager manager) {
        System.out.println("Restoring relationships...");

        List<UMLRelationship> relationshipsCopy = new ArrayList<>(relationships);
        for (UMLRelationship relationship : relationshipsCopy) {
            System.out.println("Restoring relationship: " + relationship.getName());
            manager.createRelationshipFromSerialization(relationship);
        }
    }
}
