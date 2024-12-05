package com.example.umlscd.Models.UseCaseDiagram;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;

public class UseCaseDiagramData {

    private ArrayList<UseCaseDiagramObject> objects;
    private ArrayList<Association> associations;
    private String systemBoundaryName;

    public UseCaseDiagramData(ArrayList<UseCaseDiagramObject> objects, ArrayList<Association> associations, String systemBoundaryName) {
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

    public String getSystemBoundaryName() {
        return systemBoundaryName;
    }

    // Convert DiagramData object to JSON string
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    // Convert JSON string to DiagramData object
    public static UseCaseDiagramData fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, UseCaseDiagramData.class);
    }
}
