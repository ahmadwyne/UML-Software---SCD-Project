package com.example.umlscd;

import javafx.scene.layout.VBox;

public interface UMLElementBoxInterface {
    String getName();
    double getX();
    double getY();
    VBox getVisualRepresentation();
    void setName(String name);
    void setX(double x);
    void setY(double y);
    void setVisualRepresentation(VBox visualRepresentation);
}
