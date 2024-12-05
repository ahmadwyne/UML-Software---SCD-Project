package com.example.umlscd.BuisnessLayer.ClasDiagram;

import com.example.umlscd.Models.ClassDiagram.UMLElementBoxInterface;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;
import com.example.umlscd.Models.ClassDiagram.UMLRelationshipBox;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * Manages the creation and rendering of Inheritance relationships.
 */
public class InheritanceManager extends ClassDiagramRelationsManager {

    private final ClassDiagramManager classDiagramManager;
    private UMLRelationshipBox lastRelationshipBox;

    /**
     * Constructor that initializes the InheritanceManager with a ClassDiagramManager.
     *
     * @param manager The ClassDiagramManager instance.
     */
    public InheritanceManager(ClassDiagramManager manager) {
        this.classDiagramManager = manager;
        enableInheritanceMode();
    }

    /**
     * Creates an inheritance relationship between two UML elements.
     *
     * @param start            The starting UML element (child).
     * @param end              The ending UML element (parent).
     * @param drawingPane      The pane where the relationship is drawn.
     * @param inheritanceName  The name of the inheritance relationship (optional).
     * @param startMultiplicity The multiplicity at the start end (optional, typically not used in inheritance).
     * @param endMultiplicity   The multiplicity at the end end (optional, typically not used in inheritance).
     */
    @Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String inheritanceName, String startMultiplicity, String endMultiplicity) {
        // Disable inheritance mode immediately after starting the process
        disableInheritanceMode();

        // Set default values if fields are empty
        if (inheritanceName == null || inheritanceName.isEmpty()) inheritanceName = "Inheritance";
        if (startMultiplicity == null || startMultiplicity.isEmpty()) startMultiplicity = "";
        if (endMultiplicity == null || endMultiplicity.isEmpty()) endMultiplicity = "";

        // Find the closest boundary points between the two classes
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Create the inheritance triangle (hollow triangle)
        Polygon inheritanceTriangle = new Polygon();
        inheritanceTriangle.getPoints().addAll(
                0.0, 0.0,      // Tip of the triangle (top point)
                -10.0, 15.0,    // Bottom-left point (base)
                10.0, 15.0      // Bottom-right point (base)
        );
        inheritanceTriangle.setFill(Color.WHITE); // Hollow triangle
        inheritanceTriangle.setStroke(Color.BLACK); // Black border

        // Attach triangle to the parent class boundary (tip to the class boundary)
        double tipX = endPoint.getX();
        double tipY = endPoint.getY();

        // Offset the inheritanceTriangle to ensure its tip touches the class boundary
        inheritanceTriangle.setLayoutX(tipX); // Position tip at endPoint
        inheritanceTriangle.setLayoutY(tipY - 15); // Adjust Y to position the base below

        // Create the line connecting the child class (start) to the parent class (end)
        Line inheritanceLine = new Line();
        inheritanceLine.setStartX(startPoint.getX()); // Start at the closest boundary point of the child class
        inheritanceLine.setStartY(startPoint.getY());
        inheritanceLine.setEndX(tipX); // End at the tip of the inheritance triangle
        inheritanceLine.setEndY(tipY);

        // Style the inheritance line
        inheritanceLine.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

        // Add the inheritance triangle and line to the drawing pane
        drawingPane.getChildren().addAll(inheritanceTriangle, inheritanceLine);

        // Optionally create the inheritance label if a name is provided
        Text inheritanceLabel = null;
        if (!inheritanceName.equals("Inheritance")) {
            inheritanceLabel = new Text(inheritanceName);
            inheritanceLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
            bindAssociationLabelToLine(inheritanceLabel, inheritanceLine);
            drawingPane.getChildren().add(inheritanceLabel);
        }

        // Typically, multiplicity labels are not used in inheritance, but included here for consistency
        Text startMultiplicityText = null;
        Text endMultiplicityText = null;
        if (!startMultiplicity.isEmpty()) {
            startMultiplicityText = new Text(startMultiplicity);
            startMultiplicityText.setStyle("-fx-font-size: 10;");
            bindMultiplicityLabelToLine(startMultiplicityText, inheritanceLine, true);
            drawingPane.getChildren().add(startMultiplicityText);
        }
        if (!endMultiplicity.isEmpty()) {
            endMultiplicityText = new Text(endMultiplicity);
            endMultiplicityText.setStyle("-fx-font-size: 10;");
            bindMultiplicityLabelToLine(endMultiplicityText, inheritanceLine, false);
            drawingPane.getChildren().add(endMultiplicityText);
        }

        // Add listeners to update the line and inheritance triangle when either class is moved
        addDynamicUpdateListener(inheritanceLine, inheritanceTriangle, start, end, inheritanceLabel, startMultiplicityText, endMultiplicityText);

        // Create UMLRelationshipBox
        UMLRelationshipBox relationshipBox = new UMLRelationshipBox(
                "Inheritance",
                getElementName(start),
                getElementName(end),
                inheritanceName,
                startMultiplicity,
                endMultiplicity,
                inheritanceLine,
                inheritanceLabel,
                startMultiplicityText,
                endMultiplicityText
        );

        // Add the relationship to the manager
        classDiagramManager.addRelationshipBox(relationshipBox);

        // Store the last created relationship
        lastRelationshipBox = relationshipBox;

        // Debug Logging
        System.out.println("Created inheritance: " + inheritanceName + " between " + getElementName(start) + " and " + getElementName(end));
    }

    /**
     * Creates an inheritance relationship from a UMLRelationship model object during deserialization.
     *
     * @param umlRelationship The UMLRelationship data.
     * @param drawingPane     The pane where the relationship is drawn.
     */
    public void createRelationshipFromModel(UMLRelationship umlRelationship, Pane drawingPane) {
        String startName = umlRelationship.getStartElementName();
        String endName = umlRelationship.getEndElementName();
        String name = umlRelationship.getName();
        String startMultiplicity = umlRelationship.getStartMultiplicity();
        String endMultiplicity = umlRelationship.getEndMultiplicity();

        // Retrieve UMLElementBox objects from the map
        UMLElementBoxInterface startElement = classDiagramManager.getClassBoxMap().get(startName);
        UMLElementBoxInterface endElement = classDiagramManager.getClassBoxMap().get(endName);

        if (startElement == null || endElement == null) {
            System.err.println("Cannot create inheritance. One of the elements is missing.");
            return;
        }

        // Retrieve the VBox visual representations
        VBox startBox = startElement.getVisualRepresentation();
        VBox endBox = endElement.getVisualRepresentation();

        if (startBox == null || endBox == null) {
            System.err.println("Visual representation not available for one of the classes.");
            return;
        }

        // Find the closest boundary points between the two classes
        Point startPoint = getClosestBoundaryPoint(startBox, endBox);
        Point endPoint = getClosestBoundaryPoint(endBox, startBox);

        // Create the inheritance triangle (hollow triangle)
        Polygon inheritanceTriangle = new Polygon();
        inheritanceTriangle.getPoints().addAll(
                0.0, 0.0,      // Tip of the triangle (top point)
                -10.0, 15.0,    // Bottom-left point (base)
                10.0, 15.0      // Bottom-right point (base)
        );
        inheritanceTriangle.setFill(Color.WHITE); // Hollow triangle
        inheritanceTriangle.setStroke(Color.BLACK); // Black border

        // Attach triangle to the parent class boundary (tip to the class boundary)
        double tipX = endPoint.getX();
        double tipY = endPoint.getY();

        // Offset the inheritanceTriangle to ensure its tip touches the class boundary
        inheritanceTriangle.setLayoutX(tipX); // Position tip at endPoint
        inheritanceTriangle.setLayoutY(tipY - 15); // Adjust Y to position the base below

        // Create the line connecting the child class (start) to the parent class (end)
        Line inheritanceLine = new Line();
        inheritanceLine.setStartX(startPoint.getX()); // Start at the closest boundary point of the child class
        inheritanceLine.setStartY(startPoint.getY());
        inheritanceLine.setEndX(tipX); // End at the tip of the inheritance triangle
        inheritanceLine.setEndY(tipY);

        // Style the inheritance line
        inheritanceLine.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

        // Add the inheritance triangle and line to the drawing pane
        drawingPane.getChildren().addAll(inheritanceTriangle, inheritanceLine);

        // Optionally create the inheritance label if a name is provided
        Text inheritanceLabel = null;
        if (!name.equals("Inheritance")) {
            inheritanceLabel = new Text(name);
            inheritanceLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
            bindAssociationLabelToLine(inheritanceLabel, inheritanceLine);
            drawingPane.getChildren().add(inheritanceLabel);
        }

        // Typically, multiplicity labels are not used in inheritance, but included here for consistency
        Text startMultiplicityText = null;
        Text endMultiplicityText = null;
        if (!startMultiplicity.isEmpty()) {
            startMultiplicityText = new Text(startMultiplicity);
            startMultiplicityText.setStyle("-fx-font-size: 10;");
            bindMultiplicityLabelToLine(startMultiplicityText, inheritanceLine, true);
            drawingPane.getChildren().add(startMultiplicityText);
        }
        if (!endMultiplicity.isEmpty()) {
            endMultiplicityText = new Text(endMultiplicity);
            endMultiplicityText.setStyle("-fx-font-size: 10;");
            bindMultiplicityLabelToLine(endMultiplicityText, inheritanceLine, false);
            drawingPane.getChildren().add(endMultiplicityText);
        }

        // Add listeners to update the line and inheritance triangle when either class is moved
        addDynamicUpdateListener(inheritanceLine, inheritanceTriangle, startBox, endBox, inheritanceLabel, startMultiplicityText, endMultiplicityText);

        // Create UMLRelationshipBox
        UMLRelationshipBox relationshipBox = new UMLRelationshipBox(
                "Inheritance",
                startName,
                endName,
                name,
                startMultiplicity,
                endMultiplicity,
                inheritanceLine,
                inheritanceLabel,
                startMultiplicityText,
                endMultiplicityText
        );

        // Add the relationship to the manager
        classDiagramManager.addRelationshipBox(relationshipBox);

        // Store the last created relationship
        lastRelationshipBox = relationshipBox;

        // Debug Logging
        System.out.println("Created inheritance: " + name + " between " + startName + " and " + endName);
    }

    /**
     * Retrieves the name of the UML element from the VBox.
     *
     * @param box The VBox representing the UML element.
     * @return The name of the element.
     */
    private String getElementName(VBox box) {
        if (box.getChildren().isEmpty()) return "Unknown";
        Node node = box.getChildren().get(0);
        if (node instanceof Label) {
            Label firstLabel = (Label) node;
            if (firstLabel.getText().equals("<<Interface>>")) {
                // For interfaces, the actual name is the second label
                if (box.getChildren().size() > 1 && box.getChildren().get(1) instanceof Label) {
                    Label nameLabel = (Label) box.getChildren().get(1);
                    return nameLabel.getText();
                }
            } else {
                // For classes, the first label is the name
                return firstLabel.getText();
            }
        }
        return "Unknown";
    }

    /**
     * Gets the last created UMLRelationshipBox.
     *
     * @return The last UMLRelationshipBox.
     */
    public UMLRelationshipBox getLastRelationshipBox() {
        return lastRelationshipBox;
    }

    /**
     * Adds dynamic listeners to update the inheritance line and triangle when classes are moved.
     *
     * @param line                 The inheritance line.
     * @param inheritanceTriangle  The inheritance triangle.
     * @param start                The starting class box.
     * @param end                  The ending class box.
     * @param inheritanceLabel     The label for the inheritance (optional).
     * @param startMultiplicityText The label for the start multiplicity (optional).
     * @param endMultiplicityText   The label for the end multiplicity (optional).
     */
    private void addDynamicUpdateListener(Line line, Polygon inheritanceTriangle, VBox start, VBox end, Text inheritanceLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Update position when the start or end class is moved
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndInheritanceTrianglePosition(line, inheritanceTriangle, start, end, inheritanceLabel, startMultiplicityText, endMultiplicityText));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndInheritanceTrianglePosition(line, inheritanceTriangle, start, end, inheritanceLabel, startMultiplicityText, endMultiplicityText));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndInheritanceTrianglePosition(line, inheritanceTriangle, start, end, inheritanceLabel, startMultiplicityText, endMultiplicityText));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndInheritanceTrianglePosition(line, inheritanceTriangle, start, end, inheritanceLabel, startMultiplicityText, endMultiplicityText));
    }

    /**
     * Updates the position of the inheritance line and triangle when either class is moved.
     *
     * @param line                 The inheritance line.
     * @param inheritanceTriangle  The inheritance triangle.
     * @param start                The starting class box.
     * @param end                  The ending class box.
     * @param inheritanceLabel     The label for the inheritance (optional).
     * @param startMultiplicityText The label for the start multiplicity (optional).
     * @param endMultiplicityText   The label for the end multiplicity (optional).
     */
    protected void updateLineAndInheritanceTrianglePosition(Line line, Polygon inheritanceTriangle, VBox start, VBox end, Text inheritanceLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Recalculate the closest boundary points
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Update the line's start position
        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());

        // Update the line's end position to the tip of the inheritance triangle
        line.setEndX(endPoint.getX());
        line.setEndY(endPoint.getY());

        // Update the inheritance triangle position, keeping its tip attached to the end class boundary
        inheritanceTriangle.setLayoutX(endPoint.getX()); // Position tip at endPoint
        inheritanceTriangle.setLayoutY(endPoint.getY() - 15); // Adjust Y to position the base below

        // Rebind the inheritance label and multiplicity labels to the new line positions
        if (inheritanceLabel != null) {
            bindAssociationLabelToLine(inheritanceLabel, line);
        }
        if (startMultiplicityText != null) {
            bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        }
        if (endMultiplicityText != null) {
            bindMultiplicityLabelToLine(endMultiplicityText, line, false);
        }

        // Optional: Update the rotation of the triangle based on the angle between start and end
        double angle = Math.atan2(endPoint.getY() - startPoint.getY(), endPoint.getX() - startPoint.getX());
        inheritanceTriangle.setRotate(Math.toDegrees(angle) + 90); // +90 to align the triangle correctly
    }
}