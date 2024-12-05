package com.example.umlscd.Models.ClassDiagram;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class UMLInterfaceBox implements UMLElementBoxInterface {
    @JsonProperty("name")
    private String name;

    @JsonProperty("x")
    private double x;

    @JsonProperty("y")
    private double y;

    @JsonProperty("methods")
    private List<String> methods = new ArrayList<>(); // Added methods list

    @JsonIgnore
    private VBox visualRepresentation;

    // Default constructor for Jackson
    public UMLInterfaceBox() {
    }

    public UMLInterfaceBox(String name, double x, double y, VBox visualRepresentation) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.visualRepresentation = visualRepresentation;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public VBox getVisualRepresentation() {
        return visualRepresentation;
    }

    public List<String> getMethods() { // Added getter for methods
        return methods;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVisualRepresentation(VBox visualRepresentation) {
        this.visualRepresentation = visualRepresentation;
    }

    public void setMethods(List<String> methods) { // Added setter for methods
        this.methods = methods;
        if (visualRepresentation != null) {
            VBox methodsBox = (VBox) visualRepresentation.getChildren().get(2); // Assuming methodsBox is at index 2
            methodsBox.getChildren().clear();
            for (String method : methods) {
                methodsBox.getChildren().add(new Label(method));
            }
        }
    }
}

