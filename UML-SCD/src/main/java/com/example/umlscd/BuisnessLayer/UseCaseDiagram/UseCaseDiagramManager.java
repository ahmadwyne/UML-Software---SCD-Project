package com.example.umlscd.BuisnessLayer.UseCaseDiagram;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import com.example.umlscd.Models.UseCaseDiagram.Association;
import com.example.umlscd.Models.UseCaseDiagram.UseCaseDiagramObject;
import com.example.umlscd.DataAccessLayerLayer.ImageProcessing.ExportImageService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.canvas.Canvas;

/**
 * <h1>Use Case Diagram Manager</h1>
 *
 * <p>The {@code UseCaseDiagramManager} class is responsible for managing the state and operations of a use case diagram within the UML Editor application.
 * It maintains lists of diagram objects (actors and use cases) and their associations, handles serialization and deserialization for saving and loading diagrams,
 * and provides functionality to export the diagram as an image.</p>
 *
 * <p>This class leverages Jackson annotations to facilitate JSON serialization, ensuring that the diagram's state can be accurately saved and restored.
 * Additionally, it interacts with the {@code ExportImageService} to enable exporting the visual representation of the diagram.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UseCaseDiagramManager implements Serializable {
    /**
     * Serial version UID for serialization compatibility.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * List of objects (actors and use cases) in the use case diagram.
     *
     * <p>Managed reference annotation ensures proper handling during JSON serialization to prevent infinite recursion.</p>
     */
    @JsonManagedReference // To prevent infinite recursion during serialization
    private ArrayList<UseCaseDiagramObject> objects;

    /**
     * List of associations between objects in the use case diagram.
     */
    private ArrayList<Association> associations;

    /**
     * Name of the system boundary in the use case diagram.
     */
    private String systemBoundaryName; // Add field for system boundary name

    /**
     * Default constructor initializing empty lists for objects and associations.
     */
    public UseCaseDiagramManager() {
        objects = new ArrayList<>();
        associations = new ArrayList<>();
    }

    /**
     * Parameterized constructor for initializing the UseCaseDiagramManager with specific objects, associations, and system boundary name.
     *
     * @param objects             The list of {@code UseCaseDiagramObject} instances representing actors and use cases.
     * @param associations        The list of {@code Association} instances representing relationships between objects.
     * @param systemBoundaryName  The name of the system boundary for the use case diagram.
     */
    public UseCaseDiagramManager(@JsonProperty("objects") ArrayList<UseCaseDiagramObject> objects,
                                 @JsonProperty("associations") ArrayList<Association> associations,
                                 @JsonProperty("systemBoundaryName") String systemBoundaryName) {
        this.objects = objects;
        this.associations = associations;
        this.systemBoundaryName = systemBoundaryName;
    }

    /**
     * Retrieves the list of objects (actors and use cases) in the use case diagram.
     *
     * @return An {@code ArrayList} of {@code UseCaseDiagramObject} instances.
     */
    public ArrayList<UseCaseDiagramObject> getObjects() {
        return objects;
    }

    /**
     * Retrieves the list of associations between objects in the use case diagram.
     *
     * @return An {@code ArrayList} of {@code Association} instances.
     */
    public ArrayList<Association> getAssociations() {
        return associations;
    }

    /**
     * Adds a new object (actor or use case) to the use case diagram.
     *
     * @param object The {@code UseCaseDiagramObject} instance to be added.
     */
    public void addObject(UseCaseDiagramObject object) {
        objects.add(object);
    }

    /**
     * Adds a new association between two objects in the use case diagram.
     *
     * @param association The {@code Association} instance representing the relationship to be added.
     */
    public void addAssociation(Association association) {
        associations.add(association);
    }

    /**
     * Retrieves the name of the system boundary in the use case diagram.
     *
     * @return A {@code String} representing the system boundary name.
     */
    public String getSystemBoundaryName() {
        return systemBoundaryName;
    }

    /**
     * Sets the list of objects (actors and use cases) in the use case diagram.
     *
     * @param objects An {@code ArrayList} of {@code UseCaseDiagramObject} instances to set.
     */
    public void setObjects(ArrayList<UseCaseDiagramObject> objects) {
        this.objects = objects;
    }

    /**
     * Sets the list of associations between objects in the use case diagram.
     *
     * @param associations An {@code ArrayList} of {@code Association} instances to set.
     */
    public void setAssociations(ArrayList<Association> associations) {
        this.associations = associations;
    }

    /**
     * Sets the name of the system boundary in the use case diagram.
     *
     * @param systemBoundaryName A {@code String} representing the system boundary name.
     */
    public void setSystemBoundaryName(String systemBoundaryName) {
        this.systemBoundaryName = systemBoundaryName;
    }

    /**
     * Exports the current use case diagram as an image file.
     *
     * <p>This method delegates the export process to the {@code ExportImageService}, which handles
     * the rendering of the diagram on the provided {@code Canvas} and saves it as an image file.</p>
     *
     * @param canvas The {@code Canvas} instance representing the visual area of the use case diagram.
     */
    public void exportDiagramToImage(Canvas canvas) {
        ExportImageService exportService = new ExportImageService();
        exportService.exportToImage(canvas); // Pass the current manager and canvas to the service
    }
}