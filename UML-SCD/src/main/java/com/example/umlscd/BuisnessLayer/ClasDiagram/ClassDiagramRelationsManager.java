package com.example.umlscd.BuisnessLayer.ClasDiagram;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import javafx.scene.layout.VBox;

/**
 * <h1>Class Diagram Relations Manager</h1>
 *
 * <p>The {@code ClassDiagramRelationsManager} is responsible for managing the relationships between classes in a UML
 * diagram. This class provides methods to enable or disable various relationship modes, such as association, aggregation,
 * composition, and inheritance. It also provides utility methods to calculate the distance between class boundaries and
 * to position labels related to these relationships (such as multiplicity and association labels).</p>
 *
 * <p>Each relationship type (association, aggregation, composition, inheritance) is controlled by a flag, and the
 * appropriate relationship can be drawn by calling the {@link #createRelationship} method, which is abstract and needs
 * to be implemented by subclasses.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-04</p>
 */
public abstract class ClassDiagramRelationsManager {

    /**
     * Flag to enable or disable association relationship mode.
     */
    public boolean enabledAssociationModel = false;

    /**
     * Flag to enable or disable aggregation relationship mode.
     */
    public boolean enabledAggregationModel = false;

    /**
     * Flag to enable or disable composition relationship mode.
     */
    public boolean enabledCompositionModel = false;

    /**
     * Flag to enable or disable inheritance relationship mode.
     */
    public boolean inheritanceModeEnabled = false;

    /**
     * Enables the association relationship mode.
     * <p>This method sets {@link #enabledAssociationModel} to {@code true}, allowing the creation of association
     * relationships between classes.</p>
     */
    public void enableAssociationMode() {
        this.enabledAssociationModel = true;
    }

    /**
     * Disables the association relationship mode.
     * <p>This method sets {@link #enabledAssociationModel} to {@code false}, disabling the creation of association
     * relationships between classes.</p>
     */
    public void disableAssociationMode() {
        this.enabledAssociationModel = false;
    }

    /**
     * Enables the aggregation relationship mode.
     * <p>This method sets {@link #enabledAggregationModel} to {@code true}, allowing the creation of aggregation
     * relationships between classes.</p>
     */
    public void enableAggregationMode() {
        this.enabledAggregationModel = true;
    }

    /**
     * Disables the aggregation relationship mode.
     * <p>This method sets {@link #enabledAggregationModel} to {@code false}, disabling the creation of aggregation
     * relationships between classes.</p>
     */
    public void disableAggregationMode() {
        this.enabledAggregationModel = false;
    }

    /**
     * Enables the composition relationship mode.
     * <p>This method sets {@link #enabledCompositionModel} to {@code true}, allowing the creation of composition
     * relationships between classes.</p>
     */
    public void enableCompositionMode() {
        this.enabledCompositionModel = true;
    }

    /**
     * Disables the composition relationship mode.
     * <p>This method sets {@link #enabledCompositionModel} to {@code false}, disabling the creation of composition
     * relationships between classes.</p>
     */
    public void disableCompositionMode() {
        this.enabledCompositionModel = false;
    }

    /**
     * Enables inheritance relationship mode.
     * <p>This method sets {@link #inheritanceModeEnabled} to {@code true}, allowing the creation of inheritance
     * relationships between classes.</p>
     */
    public void enableInheritanceMode() {
        inheritanceModeEnabled = true;
    }

    /**
     * Disables inheritance relationship mode.
     * <p>This method sets {@link #inheritanceModeEnabled} to {@code false}, disabling the creation of inheritance
     * relationships between classes.</p>
     */
    public void disableInheritanceMode() {
        inheritanceModeEnabled = false;
    }

    /**
     * Abstract method to create a relationship between two classes.
     * <p>This method must be implemented by a subclass to create a specific type of relationship (association, aggregation,
     * composition, or inheritance) between two classes represented by {@code start} and {@code end}.</p>
     *
     * @param start The starting class (VBox) for the relationship.
     * @param end The ending class (VBox) for the relationship.
     * @param drawingPane The pane where the relationship will be drawn.
     * @param relationshipName The name of the relationship (e.g., "association", "aggregation").
     * @param startMultiplicity The multiplicity at the start of the relationship (e.g., "1", "0..*").
     * @param endMultiplicity The multiplicity at the end of the relationship (e.g., "1", "0..*").
     */
    public abstract void createRelationship(VBox start, VBox end, Pane drawingPane, String relationshipName, String startMultiplicity, String endMultiplicity);

    /**
     * Finds the closest boundary point between two classes (VBox).
     * <p>This method calculates the closest points on the boundaries of two classes (represented as {@code VBox}) and
     * returns the point on the starting class that is closest to the boundary of the ending class. This is useful for
     * positioning relationships between classes.</p>
     *
     * @param startClass The starting class (VBox).
     * @param endClass The ending class (VBox).
     * @return The closest boundary point from the start class.
     */
    public Point getClosestBoundaryPoint(VBox startClass, VBox endClass) {
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

    /**
     * Gets the four boundary points of a class (represented as a VBox).
     * <p>This method returns the four boundary points (top, right, bottom, left) for a given class (VBox), which are
     * used for positioning relationships between classes.</p>
     *
     * @param classBox The class (VBox) for which boundary points are needed.
     * @return An array of {@code Point} objects representing the boundary points.
     */
    public Point[] getBoundaryPoints(VBox classBox) {
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

    /**
     * Calculates the Euclidean distance between two points.
     * <p>This method calculates the straight-line distance between two points (p1 and p2) in 2D space using the
     * Euclidean distance formula.</p>
     *
     * @param p1 The first point.
     * @param p2 The second point.
     * @return The Euclidean distance between the two points.
     */
    public double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }

    /**
     * Binds a label to the midpoint of a relationship line.
     * <p>This method binds the position of a label (e.g., association label) to the midpoint of a relationship line.
     * The label will move dynamically as the line is adjusted.</p>
     *
     * @param label The label to bind.
     * @param line The relationship line to bind the label to.
     */
    public void bindAssociationLabelToLine(Text label, Line line) {
        label.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
        label.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(10)); // Slightly above the line
    }

    /**
     * Binds a multiplicity label to one end of the relationship line.
     * <p>This method binds the position of a multiplicity label (e.g., "1", "0..*") to one of the ends of the
     * relationship line, depending on whether it is the start or end of the relationship.</p>
     *
     * @param label The label to bind.
     * @param line The relationship line to bind the label to.
     * @param isStart True if the label is for the start of the relationship, false for the end.
     */
    public void bindMultiplicityLabelToLine(Text label, Line line, boolean isStart) {
        if (isStart) {
            label.xProperty().bind(line.startXProperty().subtract(-15));
            label.yProperty().bind(line.startYProperty().subtract(5));
        } else {
            label.xProperty().bind(line.endXProperty().subtract(15));
            label.yProperty().bind(line.endYProperty().subtract(5));
        }
    }

    /**
     * A simple Point class to represent a coordinate in 2D space.
     * <p>This class represents a 2D point with {@code x} and {@code y} coordinates. It is used to describe boundary
     * points and positions in the diagram.</p>
     */
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
