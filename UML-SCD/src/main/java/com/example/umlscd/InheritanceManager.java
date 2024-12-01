package com.example.umlscd;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Line;

public class InheritanceManager extends ClassDiagramRelationsManager {

    // Constructor
    public InheritanceManager() {
        // Enable inheritance mode by default
        enableInheritanceMode();
    }

    @Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String inheritanceName, String startMultiplicity, String endMultiplicity) {
        // Disable association mode immediately after starting the process
        disableInheritanceMode();


        // Create the triangle (inheritance) shape
        Polygon inheritanceTriangle = new Polygon();
        inheritanceTriangle.getPoints().addAll(
                0.0, 0.0,  // Top point
                10.0, 20.0, // Bottom left point
                20.0, 0.0   // Bottom right point
        );
        inheritanceTriangle.setFill(Color.WHITE);
        inheritanceTriangle.setStroke(Color.BLACK); // Border color of the triangle

        // Create the line connecting the first selected element (source) and the inheritance triangle (inheritance point)
        Line inheritanceLine = new Line();

        // Add the triangle and line to the drawing pane
        drawingPane.getChildren().addAll(inheritanceTriangle, inheritanceLine);

        // Add listeners to update the line and triangle position dynamically when classes are moved
        addDynamicUpdateListener(inheritanceTriangle, inheritanceLine, start, end, drawingPane);
    }

    // Add listeners to update the line and triangle dynamically when classes are moved
    private void addDynamicUpdateListener(Polygon inheritanceTriangle, Line inheritanceLine, VBox start, VBox end, Pane drawingPane) {
        // Listen to the X and Y properties of both classes (start and end) to update the line and triangle positions
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndTrianglePosition(inheritanceTriangle, inheritanceLine, start, end, drawingPane));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndTrianglePosition(inheritanceTriangle, inheritanceLine, start, end, drawingPane));

        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndTrianglePosition(inheritanceTriangle, inheritanceLine, start, end, drawingPane));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndTrianglePosition(inheritanceTriangle, inheritanceLine, start, end, drawingPane));

        // Initial position update when the diagram is first created
        updateLineAndTrianglePosition(inheritanceTriangle, inheritanceLine, start, end, drawingPane);
    }

    // Update the line and triangle position dynamically
    private void updateLineAndTrianglePosition(Polygon inheritanceTriangle, Line inheritanceLine, VBox start, VBox end, Pane drawingPane) {
        // Get the closest points of both classes
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Position the inheritance triangle at the boundary of the second class (end)
        double secondElementCenterX = end.getLayoutX() + end.getWidth() / 2;
        double secondElementCenterY = end.getLayoutY() + end.getHeight() / 2;
        double triangleX = secondElementCenterX - 10; // Adjust to center the triangle on the boundary
        double triangleY = end.getLayoutY(); // Position triangle at the top boundary of the class

        // Set the inheritance triangle's position to the calculated point
        inheritanceTriangle.setLayoutX(triangleX);
        inheritanceTriangle.setLayoutY(triangleY - 10); // Offset to position the triangle correctly

        // Update the line's start and end positions
        inheritanceLine.setStartX(start.getLayoutX() + start.getWidth() / 2);
        inheritanceLine.setStartY(start.getLayoutY() + start.getHeight() / 2);
        inheritanceLine.setEndX(triangleX); // End at the inheritance triangle's top point
        inheritanceLine.setEndY(triangleY); // End at the inheritance triangle's top point

    }
}

