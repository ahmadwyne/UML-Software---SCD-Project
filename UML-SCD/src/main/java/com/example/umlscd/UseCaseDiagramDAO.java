package com.example.umlscd;

import java.io.*;

public class UseCaseDiagramDAO {

    private static final String FILE_PATH = "usecase_diagram.ser";

    public static void saveDiagram(UseCaseDiagramManager manager, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(manager);
            System.out.println("Diagram saved successfully at: " + filePath);
        } catch (IOException e) {
            System.err.println("Error saving diagram: " + e.getMessage());
        }
    }

    public static UseCaseDiagramManager loadDiagram() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (UseCaseDiagramManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading diagram: " + e.getMessage());
            return null;
        }
    }

    public static UseCaseDiagramManager loadDiagram(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (UseCaseDiagramManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading diagram: " + e.getMessage());
            return null;
        }
    }

}
