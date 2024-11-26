package com.example.umlscd;

import java.io.Serializable;
import java.util.ArrayList;

public class UseCaseDiagramManager implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<UseCaseDiagramObject> objects;
    private ArrayList<Association> associations;
    private String systemBoundaryName; // Add field for system boundary name

    public UseCaseDiagramManager(ArrayList<UseCaseDiagramObject> objects, ArrayList<Association> associations, String systemBoundaryName) {
        this.objects = objects;
        this.associations = associations;
        this.systemBoundaryName = systemBoundaryName; // Initialize system boundary name
    }

    public ArrayList<UseCaseDiagramObject> getObjects() {
        return objects;
    }

    public ArrayList<Association> getAssociations() {
        return associations;
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
}
