package com.example.umlscd.DataAccessLayerLayer.Serializers.UseCaseDiagram;

import com.example.umlscd.BuisnessLayer.UseCaseDiagram.UseCaseDiagramManager;

import java.io.*;

/**
 * <h1>Use Case Diagram Data Access Object (DAO)</h1>
 *
 * <p>The {@code UseCaseDiagramDAO} class serves as the Data Access Object for the {@code UseCaseDiagramManager} class.
 * It provides functionalities to save and load use case diagrams by serializing and deserializing the diagram
 * manager objects to and from files. This facilitates persistent storage and retrieval of diagrams,
 * enabling users to save their work and continue editing at a later time.</p>
 *
 * <p>The class includes methods for saving diagrams to a specified file path, loading diagrams from a default
 * file path, and loading diagrams from a user-specified file path. It handles exceptions related to file
 * I/O operations and ensures that appropriate messages are logged in case of errors.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class UseCaseDiagramDAO {

    /**
     * The default file path used for saving and loading use case diagrams.
     */
    private static final String FILE_PATH = "usecase_diagram.ser";

    /**
     * Saves the given {@code UseCaseDiagramManager} instance to the specified file path by serializing it.
     *
     * <p>This method serializes the {@code UseCaseDiagramManager} object and writes it to a file.
     * If the operation is successful, a confirmation message is printed to the console.
     * In case of an {@code IOException}, an error message is printed.</p>
     *
     * @param manager  The {@code UseCaseDiagramManager} instance to be saved.
     * @param filePath The file path where the diagram will be saved.
     */
    public static void saveDiagram(UseCaseDiagramManager manager, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(manager);
            System.out.println("Diagram saved successfully at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving diagram: " + e.getMessage());
        }
    }

    /**
     * Loads a {@code UseCaseDiagramManager} instance from the default file path by deserializing it.
     *
     * <p>This method attempts to read and deserialize a {@code UseCaseDiagramManager} object from the
     * default file path defined by {@code FILE_PATH}. If the operation is successful, the deserialized
     * object is returned. In case of an {@code IOException} or {@code ClassNotFoundException},
     * an error message is printed and {@code null} is returned.</p>
     *
     * @return The deserialized {@code UseCaseDiagramManager} instance if successful; {@code null} otherwise.
     */
    public static UseCaseDiagramManager loadDiagram() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (UseCaseDiagramManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading diagram: " + e.getMessage());
            return null;
        }
    }

    /**
     * Loads a {@code UseCaseDiagramManager} instance from the specified file path by deserializing it.
     *
     * <p>This method attempts to read and deserialize a {@code UseCaseDiagramManager} object from the
     * provided file path. If the operation is successful, the deserialized object is returned.
     * In case of an {@code IOException} or {@code ClassNotFoundException}, an error message is printed
     * and {@code null} is returned.</p>
     *
     * @param filePath The file path from which the diagram will be loaded.
     * @return The deserialized {@code UseCaseDiagramManager} instance if successful; {@code null} otherwise.
     */
    public static UseCaseDiagramManager loadDiagram(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (UseCaseDiagramManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading diagram: " + e.getMessage());
            return null;
        }
    }

}