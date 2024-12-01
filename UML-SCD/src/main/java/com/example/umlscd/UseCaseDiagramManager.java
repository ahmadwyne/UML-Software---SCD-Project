package com.example.umlscd;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.canvas.Canvas;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UseCaseDiagramManager implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonManagedReference // To prevent infinite recursion during serialization
    private ArrayList<UseCaseDiagramObject> objects;
    private ArrayList<Association> associations;
    private String systemBoundaryName; // Add field for system boundary name

    public UseCaseDiagramManager() {
        objects = new ArrayList<>();
        associations = new ArrayList<>();
    }

    public UseCaseDiagramManager(@JsonProperty("objects") ArrayList<UseCaseDiagramObject> objects,
                                 @JsonProperty("associations") ArrayList<Association> associations,
                                 @JsonProperty("systemBoundaryName") String systemBoundaryName) {
        this.objects = objects;
        this.associations = associations;
        this.systemBoundaryName = systemBoundaryName;
    }

    public ArrayList<UseCaseDiagramObject> getObjects() {
        return objects;
    }

    public ArrayList<Association> getAssociations() {
        return associations;
    }

    public void addObject(UseCaseDiagramObject object) {
        objects.add(object);
    }

    public void addAssociation(Association association) {
        associations.add(association);
    }

    public String getSystemBoundaryName() {
        return systemBoundaryName;
    }

    public void setObjects(ArrayList<UseCaseDiagramObject> objects) {
        this.objects = objects;
    }

    public void setAssociations(ArrayList<Association> associations) {
        this.associations = associations;
    }

    public void setSystemBoundaryName(String systemBoundaryName) {
        this.systemBoundaryName = systemBoundaryName;
    }

    // New method to export the diagram as an image
    public void exportDiagramToImage(Canvas canvas) {
        ExportImageService exportService = new ExportImageService();
        exportService.exportToImage(canvas); // Pass the current manager and canvas to the service
    }
}