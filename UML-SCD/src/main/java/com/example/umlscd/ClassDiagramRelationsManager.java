package com.example.umlscd;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import javafx.scene.layout.VBox;

public abstract class ClassDiagramRelationsManager {

    public boolean enabledAssociationModel = false;
    public boolean enabledAggregationModel = false;
    public boolean enabledCompositionModel = false;
    public boolean inheritanceModeEnabled = false;

    // Enable/Disable association mode
    public void enableAssociationMode() {
        this.enabledAssociationModel = true;
    }

    public void disableAssociationMode() {
        this.enabledAssociationModel = false;
    }



    // Enable/Disable aggregation mode
    public void enableAggregationMode() {
        this.enabledAggregationModel = true;
    }

    public void disableAggregationMode() {
        this.enabledAggregationModel = false;
    }


    // Enable/Disable composition mode
    public void enableCompositionMode() {
        this.enabledCompositionModel = true;
    }

    public void disableCompositionMode() {
        this.enabledCompositionModel = false;
    }



    // Enable inheritance mode
    public void enableInheritanceMode() {
        inheritanceModeEnabled = true;
    }

    // Disable inheritance mode
    public void disableInheritanceMode() {
        inheritanceModeEnabled = false;
    }


    // Abstract method to create relationships
    public abstract void createRelationship(VBox start, VBox end, Pane drawingPane, String relationshipName, String startMultiplicity, String endMultiplicity);

    // Common method to find the closest boundary point on the start class to the closest boundary point on the end class
    protected Point getClosestBoundaryPoint(VBox startClass, VBox endClass) {
        Point closestPoint = null;
        double minDistance = Double.MAX_VALUE;

        // Get all boundary points for the start and end class
        Point[] startPoints = getBoundaryPoints(startClass);
        Point[] endPoints = getBoundaryPoints(endClass);

        // Check the distance between all pairs of points (one from start and one from end)
        for (Point startPoint : startPoints) {
            for (Point endPoint : endPoints) {
                double distance = calculateDistance(startPoint, endPoint);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestPoint = startPoint;
                }
            }
        }

        return closestPoint;
    }

    // Get the four boundary points of a class (VBox)
    protected Point[] getBoundaryPoints(VBox classBox) {
        double x = classBox.getLayoutX();
        double y = classBox.getLayoutY();
        double width = classBox.getWidth();
        double height = classBox.getHeight();

        // Boundary points: top, right, bottom, left
        return new Point[] {
                new Point(x + width / 2, y),            // Top center
                new Point(x + width, y + height / 2),   // Right center
                new Point(x + width / 2, y + height),   // Bottom center
                new Point(x, y + height / 2)            // Left center
        };
    }

    // Calculate the Euclidean distance between two points
    protected double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    // Method to bind the relationship label to the midpoint of the line
    protected void bindAssociationLabelToLine(Text label, Line line) {
        label.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
        label.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(10)); // Slightly above the line
    }

    // Method to bind multiplicity labels to the ends of the line
    protected void bindMultiplicityLabelToLine(Text label, Line line, boolean isStart) {
        if (isStart) {
            label.xProperty().bind(line.startXProperty().subtract(-15));
            label.yProperty().bind(line.startYProperty().subtract(5));
        } else {
            label.xProperty().bind(line.endXProperty().subtract(15));
            label.yProperty().bind(line.endYProperty().subtract(5));
        }
    }

    // Point class to represent a coordinate in 2D space
    public static class Point {
        private final double x;
        private final double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }
    }
}
