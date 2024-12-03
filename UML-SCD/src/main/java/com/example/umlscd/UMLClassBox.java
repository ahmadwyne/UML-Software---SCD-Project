package com.example.umlscd;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;

public class UMLClassBox implements UMLElementBoxInterface{
    @JsonProperty("name")
    private String name;

    @JsonProperty("x")
    private double x;

    @JsonProperty("y")
    private double y;

    @JsonProperty("attributes")
    private List<String> attributes = new ArrayList<>();

    @JsonProperty("methods")
    private List<String> methods = new ArrayList<>();

    @JsonIgnore
    private VBox visualRepresentation; // Not serializable

    // Default constructor for Jackson
    public UMLClassBox() {}

    // Constructor
    public UMLClassBox(String name, double x, double y, VBox visualRepresentation) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.visualRepresentation = visualRepresentation;
    }

    // New Constructor that accepts attributes and methods
    public UMLClassBox(String name, double x, double y, List<String> attributes, List<String> methods) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.attributes = attributes;
        this.methods = methods;
        // visualRepresentation is set separately
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

    public List<String> getAttributes() {
        return attributes;
    }

    public List<String> getMethods() {
        return methods;
    }

    public VBox getVisualRepresentation() {
        return visualRepresentation;
    }

    public void setName(String name) {
        this.name = name;
        if (visualRepresentation != null) {
            Label classNameLabel = (Label) visualRepresentation.getChildren().get(0);
            classNameLabel.setText(name);
        }
    }

    public void setX(double x) {
        this.x = x;
        if (visualRepresentation != null) {
            visualRepresentation.setLayoutX(x);
        }
    }

    public void setY(double y) {
        this.y = y;
        if (visualRepresentation != null) {
            visualRepresentation.setLayoutY(y);
        }
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
        if (visualRepresentation != null) {
            VBox attributesBox = (VBox) visualRepresentation.getChildren().get(1);
            attributesBox.getChildren().clear();
            for (String attribute : attributes) {
                attributesBox.getChildren().add(new Label(attribute));
            }
        }
    }

    public void setMethods(List<String> methods) {
        this.methods = methods;
        if (visualRepresentation != null) {
            VBox methodsBox = (VBox) visualRepresentation.getChildren().get(2);
            methodsBox.getChildren().clear();
            for (String method : methods) {
                methodsBox.getChildren().add(new Label(method));
            }
        }
    }

    public void setVisualRepresentation(VBox visualRepresentation) {
        this.visualRepresentation = visualRepresentation;
        // Optionally, update the UI based on current model data
    }
}
