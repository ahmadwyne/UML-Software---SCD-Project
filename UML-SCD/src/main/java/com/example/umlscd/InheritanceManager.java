package com.example.umlscd;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class InheritanceManager extends ClassDiagramRelationsManager {

    // Constructor
    public InheritanceManager() {
        // Enable inheritance mode by default
        enableInheritanceMode();
    }

    @Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String inheritanceName, String startMultiplicity, String endMultiplicity) {
        // Disable inheritance mode immediately after starting the process
        disableInheritanceMode();

        // Find the closest boundary points between the two classes
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Create the triangle (inheritance) shape
        Polygon inheritanceTriangle = new Polygon();
        inheritanceTriangle.getPoints().addAll(
                10.0, 0.0,  // Tip of the triangle (top point)
                -10.0, 20.0, // Bottom-left point (base)
                30.0, 20.0   // Bottom-right point (base)
        );
        inheritanceTriangle.setFill(Color.WHITE);
        inheritanceTriangle.setStroke(Color.BLACK);

        // Position the tip of the triangle to the closest boundary point of Class 2
        double tipX = endPoint.getX();
        double tipY = endPoint.getY();

        // Offset the inheritanceTriangle to ensure its tip touches the class boundary
        inheritanceTriangle.setLayoutX(tipX - 10); // Offset to position the inheritance triangle correctly
        inheritanceTriangle.setLayoutY(tipY - 10); // Offset to position the inheritance triangle correctly

        // Create the line connecting the first selected element (start) to the inheritance triangle (base)
        Line inheritanceLine = new Line();
        inheritanceLine.setStartX(start.getLayoutX() + start.getWidth() / 2); // Start at the center of the first class
        inheritanceLine.setStartY(start.getLayoutY() + start.getHeight() / 2);

        // Calculate the middle of the base of the inheritance triangle (where the second point of the line should attach)
        double baseMiddleX = (inheritanceTriangle.getPoints().get(2) + inheritanceTriangle.getPoints().get(4)) / 2 + inheritanceTriangle.getLayoutX();
        double baseMiddleY = (inheritanceTriangle.getPoints().get(3) + inheritanceTriangle.getPoints().get(5)) / 2 + inheritanceTriangle.getLayoutY();

        // Set the second endpoint of the line to the middle of the base of the inheritance triangle
        inheritanceLine.setEndX(baseMiddleX);
        inheritanceLine.setEndY(baseMiddleY);

        // Add the inheritance triangle and the line to the drawing pane
        drawingPane.getChildren().addAll(inheritanceTriangle, inheritanceLine);

        // Add listeners to update the line and inheritance triangle dynamically when either class is moved
        addDynamicUpdateListener(inheritanceLine, inheritanceTriangle, start, end, null, null, null);
    }

    // Add listeners to update the line and inheritance triangle dynamically when classes are moved
    private void addDynamicUpdateListener(Line line, Polygon inheritanceTriangle, VBox start, VBox end, Text InheritanceLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Update position when the start or end class is moved
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndInheritanceTrianglePosition(line, inheritanceTriangle, start, end, InheritanceLabel, startMultiplicityText, endMultiplicityText));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndInheritanceTrianglePosition(line, inheritanceTriangle, start, end, InheritanceLabel, startMultiplicityText, endMultiplicityText));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndInheritanceTrianglePosition(line, inheritanceTriangle, start, end, InheritanceLabel, startMultiplicityText, endMultiplicityText));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndInheritanceTrianglePosition(line, inheritanceTriangle, start, end, InheritanceLabel, startMultiplicityText, endMultiplicityText));
    }

    // Function to find the closest point on the inheritance triangle from a given point
    private Point getClosestPointOnInheritanceTriangle(Polygon inheritanceTriangle, double startX, double startY) {
        double closestDistance = Double.MAX_VALUE;
        Point closestPoint = null;

        // Iterate through the vertices of the inheritance triangle
        for (int i = 0; i < inheritanceTriangle.getPoints().size(); i += 2) {
            double x = inheritanceTriangle.getPoints().get(i) + inheritanceTriangle.getLayoutX();
            double y = inheritanceTriangle.getPoints().get(i + 1) + inheritanceTriangle.getLayoutY();

            // Calculate the distance from the point to the start point
            double distance = Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2));

            if (distance < closestDistance) {
                closestDistance = distance;
                closestPoint = new Point(x, y);
            }
        }

        return closestPoint;
    }

    // Update the line and inheritance triangle's position when either class is moved
    private void updateLineAndInheritanceTrianglePosition(Line line, Polygon inheritanceTriangle, VBox start, VBox end, Text InheritanceLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Recalculate the closest boundary points
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Update the line's position
        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());

        // Calculate the middle of the base of the inheritance triangle (where the second point of the line should attach)
        double baseMiddleX = (inheritanceTriangle.getPoints().get(2) + inheritanceTriangle.getPoints().get(4)) / 2 + inheritanceTriangle.getLayoutX();
        double baseMiddleY = (inheritanceTriangle.getPoints().get(3) + inheritanceTriangle.getPoints().get(5)) / 2 + inheritanceTriangle.getLayoutY();

        // Set the line's endpoint to the middle of the base of the inheritance triangle
        line.setEndX(baseMiddleX);
        line.setEndY(baseMiddleY);

        // Update the inheritance triangle position, keeping its tip attached to the end class boundary
        inheritanceTriangle.setLayoutX(endPoint.getX() - 10); // Offset to position the inheritance triangle correctly
        inheritanceTriangle.setLayoutY(endPoint.getY() - 10); // Offset to position the inheritance triangle correctly

        // Rebind the association label and multiplicity labels to the new line
        bindAssociationLabelToLine(InheritanceLabel, line);
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
    }
}

   /* @Override
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
*/
