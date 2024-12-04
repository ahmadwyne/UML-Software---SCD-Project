package com.example.umlscd;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

/**
 * Manages the creation and rendering of Association relationships.
 */
public class AssociationManager extends ClassDiagramRelationsManager {

    private final ClassDiagramManager classDiagramManager;
    private UMLRelationshipBox lastRelationshipBox;

    // Constructor
    public AssociationManager(ClassDiagramManager manager) {
        // Enable association mode by default
        this.classDiagramManager = manager;
        enableAssociationMode();
    }

    @Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String associationName, String startMultiplicity, String endMultiplicity) {
        // Disable association mode immediately after starting the process
        disableAssociationMode();

        // Set default values if fields are empty
        if (associationName == null || associationName.isEmpty()) associationName = "Association";
        if (startMultiplicity == null || startMultiplicity.isEmpty()) startMultiplicity = "1";
        if (endMultiplicity == null || endMultiplicity.isEmpty()) endMultiplicity = "1";

        // Find the closest boundary points between the two classes
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Create the association line
        Line line = new Line();
        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());
        line.setEndX(endPoint.getX());
        line.setEndY(endPoint.getY());

        // Set line style (e.g., color, thickness)
        line.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
        drawingPane.getChildren().add(line);

        // Create the association label
        Text associationLabel = new Text(associationName);
        associationLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
        bindAssociationLabelToLine(associationLabel, line);
        drawingPane.getChildren().add(associationLabel);

        // Create multiplicity labels
        Text startMultiplicityText = new Text(startMultiplicity);
        Text endMultiplicityText = new Text(endMultiplicity);
        startMultiplicityText.setStyle("-fx-font-size: 10;");
        endMultiplicityText.setStyle("-fx-font-size: 10;");
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
        drawingPane.getChildren().addAll(startMultiplicityText, endMultiplicityText);

        // Add listeners to update the line when either class is moved
        addDynamicUpdateListener(line, start, end, associationLabel, startMultiplicityText, endMultiplicityText);

        // After creating the association elements (line, labels), create UMLRelationshipBox
        UMLRelationshipBox relationshipBox = new UMLRelationshipBox(
                "Association",
                getElementName(start),
                getElementName(end),
                associationName,
                startMultiplicity,
                endMultiplicity,
                line,
                associationLabel,
                startMultiplicityText,
                endMultiplicityText
        );

        // Add the relationship to the manager
        classDiagramManager.addRelationshipBox(relationshipBox);

        // Store the last created relationship
        lastRelationshipBox = relationshipBox;
    }

    /**
     * Creates an association relationship from a UMLRelationship model object during deserialization.
     *
     * @param umlRelationship The UMLRelationship data.
     * @param drawingPane     The pane where elements are drawn.
     */
    public void createRelationshipFromModel(UMLRelationship umlRelationship, Pane drawingPane) {
        String startName = umlRelationship.getStartElementName();
        String endName = umlRelationship.getEndElementName();
        String name = umlRelationship.getName();
        String startMultiplicity = umlRelationship.getStartMultiplicity();
        String endMultiplicity = umlRelationship.getEndMultiplicity();

        // Retrieve UMLClassBox objects from the map
        UMLElementBoxInterface startClass = classDiagramManager.getClassBoxMap().get(startName);
        UMLElementBoxInterface endClass = classDiagramManager.getClassBoxMap().get(endName);

        if (startClass == null || endClass == null) {
            System.err.println("Cannot create association. One of the elements is missing.");
            return;
        }

        // Retrieve the VBox visual representations
        VBox startBox = startClass.getVisualRepresentation();
        VBox endBox = endClass.getVisualRepresentation();

        if (startBox == null || endBox == null) {
            System.err.println("Visual representation not available for one of the classes.");
            return;
        }

        // Calculate the center positions of the start and end boxes
        double startX = startBox.getLayoutX() + startBox.getWidth() / 2;
        double startY = startBox.getLayoutY() + startBox.getHeight() / 2;

        double endX = endBox.getLayoutX() + endBox.getWidth() / 2;
        double endY = endBox.getLayoutY() + endBox.getHeight() / 2;

        // Create a line representing the association
        Line associationLine = new Line(startX, startY, endX, endY);
        associationLine.setStroke(Color.BLACK);
        associationLine.setStrokeWidth(2);

        // Create a label for the relationship name
        Text label = new Text(name);
        label.setX((startX + endX) / 2);
        label.setY((startY + endY) / 2 - 5); // Slightly above the line

        // Create multiplicity labels
        Text startMultiplicityText = new Text(startMultiplicity);
        Text endMultiplicityText = new Text(endMultiplicity);
        startMultiplicityText.setX(startX - 10);
        startMultiplicityText.setY(startY - 10);

        endMultiplicityText.setX(endX + 10);
        endMultiplicityText.setY(endY - 10);

        // Add the line and labels to the drawing pane
        drawingPane.getChildren().addAll(associationLine, label, startMultiplicityText, endMultiplicityText);

        // Add listeners to update the line when either class is moved
        addDynamicUpdateListener(associationLine, startBox, endBox, label, startMultiplicityText, endMultiplicityText);

        // Create UMLRelationshipBox
        UMLRelationshipBox relationshipBox = new UMLRelationshipBox(
                "Association",
                startName,
                endName,
                name,
                startMultiplicity,
                endMultiplicity,
                associationLine,
                label,
                startMultiplicityText,
                endMultiplicityText
        );

        // Add the relationship to the manager
        classDiagramManager.addRelationshipBox(relationshipBox);

        // Store the last created relationship
        lastRelationshipBox = relationshipBox;

        // **Debug Logging**
        System.out.println("Created association: " + name + " between " + startName + " and " + endName);

        /*// Add a mouse click listener to the line for selection
        associationLine.setOnMouseClicked(event -> {
            System.out.println("Line selected");
            // Toggle the selection of the line (change color to red for selection)
            if (associationLine.getStroke().equals(Color.RED)) {
                // If the line is already selected (red), unselect it (reset to black)
                associationLine.setStroke(Color.BLACK);
            } else {
                // If the line is not selected, select it by changing the color to red
                associationLine.setStroke(Color.RED);
            }

            // Optionally, you can store the selected line and keep track of it if needed
            // classDiagramManager.setSelectedRelationship(associationLine);
        });
    }*/

        // Add a mouse click listener to the line for deletion
        associationLine.setOnMouseClicked(event -> {
            System.out.println("Line clicked");
            System.out.println("Line clicked");
            if (associationLine.getStroke().equals(Color.RED)) {
                associationLine.setStroke(Color.BLACK); // Unselect
            } else {
                associationLine.setStroke(Color.RED); // Highlight
            }
            // Call deleteSelectedRelationship to remove the relationship
            classDiagramManager.deleteSelectedRelationship(associationLine);
        });
        associationLine.setPickOnBounds(true); // Enables broader click detection
       // ass.setMouseTransparent(true);
        startMultiplicityText.setMouseTransparent(true);
        endMultiplicityText.setMouseTransparent(true);


    }

    /**
     * Retrieves the name of the element represented by the VBox.
     *
     * @param box The VBox representing the UML element.
     * @return The name of the element.
     */
    /**
     * Retrieves the name of the UML element from the VBox.
     *
     * @param box The VBox representing the UML element.
     * @return The name of the element.
     */
    private String getElementName(VBox box) {
        if (box.getChildren().isEmpty()) return "Unknown";

        // Check if the first child is a label with the stereotype
        Node firstNode = box.getChildren().get(0);
        if (firstNode instanceof Label) {
            Label firstLabel = (Label) firstNode;
            if (firstLabel.getText().equals("<<Interface>>")) {
                // For interfaces, the actual name is likely the second label
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
     * Retrieves the last created UMLRelationshipBox.
     *
     * @return The last UMLRelationshipBox.
     */
    public UMLRelationshipBox getLastRelationshipBox() {
        return lastRelationshipBox;
    }

    /**
     * Adds dynamic listeners to update the association line and labels when classes are moved.
     *
     * @param line                   The association line.
     * @param startBox               The starting class box.
     * @param endBox                 The ending class box.
     * @param associationLabel       The label for the association name.
     * @param startMultiplicityText  The label for the start multiplicity.
     * @param endMultiplicityText    The label for the end multiplicity.
     */
    private void addDynamicUpdateListener(Line line, VBox startBox, VBox endBox, Text associationLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Listener for startBox position changes
        startBox.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, startBox, endBox, associationLabel, startMultiplicityText, endMultiplicityText));
        startBox.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, startBox, endBox, associationLabel, startMultiplicityText, endMultiplicityText));

        // Listener for endBox position changes
        endBox.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, startBox, endBox, associationLabel, startMultiplicityText, endMultiplicityText));
        endBox.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, startBox, endBox, associationLabel, startMultiplicityText, endMultiplicityText));
    }

    // Update the line and labels when either class is moved
    private void updateLinePosition(Line line, VBox start, VBox end, Text associationLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Recalculate the closest boundary points
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Update the line's position
        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());
        line.setEndX(endPoint.getX());
        line.setEndY(endPoint.getY());

        // Rebind the association label and multiplicity labels to the new line
        bindAssociationLabelToLine(associationLabel, line);
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
    }
}
