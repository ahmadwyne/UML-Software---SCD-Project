package com.example.umlscd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles serialization and deserialization of the UML Class Diagram.
 */
public class ClassDiagramSerializer {
    private final ObjectMapper objectMapper;

    public ClassDiagramSerializer() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Serializes the ClassDiagramD object to a JSON file.
     *
     * @param diagram The ClassDiagramD to serialize.
     * @param file    The file to write the JSON to.
     * @throws IOException If an I/O error occurs.
     */
    public void serialize(ClassDiagramD diagram, File file) throws IOException {
        objectMapper.writeValue(file, diagram);
    }

    /**
     * Deserializes a JSON file into a ClassDiagramD object.
     *
     * @param file The JSON file to deserialize.
     * @return The deserialized ClassDiagramD.
     * @throws IOException If an I/O error occurs.
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
     * @param diagram The deserialized ClassDiagramD.
     * @param manager The ClassDiagramManager to update.
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

    private void restoreClasses(List<UMLClassBox> classes, ClassDiagramManager manager) {
        System.out.println("Restoring classes...");

        List<UMLClassBox> classBoxesCopy = new ArrayList<>(classes);
        for (UMLClassBox umlClass : classBoxesCopy) {
            System.out.println("Restoring class: " + umlClass.getName());
            manager.reCreateClassBox(umlClass.getName(), umlClass.getX(), umlClass.getY(), umlClass.getAttributes(), umlClass.getAttributes());
        }
    }

    private void restoreInterfaces(List<UMLInterfaceBox> interfaces, ClassDiagramManager manager) {
        System.out.println("Restoring interfaces...");

        List<UMLInterfaceBox> interfaceBoxesCopy = new ArrayList<>(interfaces);
        for (UMLInterfaceBox umlInterface : interfaceBoxesCopy) {
            System.out.println("Restoring interface: " + umlInterface.getName());
            manager.reCreateInterfaceBox(umlInterface.getName(), umlInterface.getX(), umlInterface.getY());
        }
    }

    private void restoreRelationships(List<UMLRelationship> relationships, ClassDiagramManager manager) {
        System.out.println("Restoring relationships...");

        List<UMLRelationship> relationshipsCopy = new ArrayList<>(relationships);
        for (UMLRelationship relationship : relationshipsCopy) {
            System.out.println("Restoring relationship: " + relationship.getName());
            manager.createRelationshipFromSerialization(relationship);
        }

    }


    /**
     * Creates and returns a VBox to represent the UML class.
     *
     * @param name       The class name.
     * @param x          The x-coordinate for the class box.
     * @param y          The y-coordinate for the class box.
     * @param attributes List of class attributes.
     * @param methods    List of class methods.
     * @return The VBox representing the class.
     */
    public VBox createClassVBox(String name, double x, double y, List<String> attributes, List<String> methods) {
        VBox vbox = new VBox();
        Label classNameLabel = new Label(name);
        vbox.getChildren().add(classNameLabel);

        // Add attributes and methods to the VBox
        VBox attributesBox = new VBox();
        for (String attribute : attributes) {
            attributesBox.getChildren().add(new Label(attribute));
        }
        vbox.getChildren().add(attributesBox);

        VBox methodsBox = new VBox();
        for (String method : methods) {
            methodsBox.getChildren().add(new Label(method));
        }
        vbox.getChildren().add(methodsBox);

        // Set the position of the class box
        vbox.setLayoutX(x);
        vbox.setLayoutY(y);

        return vbox;
    }

    /**
     * Creates and returns a VBox to represent the UML interface.
     *
     * @param name The interface name.
     * @param x    The x-coordinate for the interface box.
     * @param y    The y-coordinate for the interface box.
     * @return The VBox representing the interface.
     */
    public VBox createInterfaceVBox(String name, double x, double y) {
        VBox vbox = new VBox();
        Label interfaceNameLabel = new Label(name);
        vbox.getChildren().add(interfaceNameLabel);

        // Set the position of the interface box
        vbox.setLayoutX(x);
        vbox.setLayoutY(y);

        return vbox;
    }
}
