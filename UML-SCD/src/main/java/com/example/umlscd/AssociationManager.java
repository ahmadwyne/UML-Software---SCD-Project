package com.example.umlscd;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;

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

        /*VBox startBox = classDiagramManager.findClassBoxByName(startName);
        if (startBox == null) startBox = classDiagramManager.findInterfaceBoxByName(startName);
        VBox endBox = classDiagramManager.findClassBoxByName(endName);
        if (endBox == null) endBox = classDiagramManager.findInterfaceBoxByName(endName);

        if (startBox == null || endBox == null) {
            System.err.println("Could not find elements for relationship: " + name);
            return;
        }

        // Create UI elements without invoking further deserialization
        Line line = new Line();
        line.startXProperty().bind(startBox.layoutXProperty().add(startBox.widthProperty().divide(2)));
        line.startYProperty().bind(startBox.layoutYProperty().add(startBox.heightProperty()));
        line.endXProperty().bind(endBox.layoutXProperty().add(endBox.widthProperty().divide(2)));
        line.endYProperty().bind(endBox.layoutYProperty());

        Text label = new Text(name);
        label.layoutXProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
        label.layoutYProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2));

        Text startMultiplicityText = new Text(startMultiplicity);
        startMultiplicityText.layoutXProperty().bind(line.startXProperty());
        startMultiplicityText.layoutYProperty().bind(line.startYProperty().subtract(5));

        Text endMultiplicityText = new Text(endMultiplicity);
        endMultiplicityText.layoutXProperty().bind(line.endXProperty());
        endMultiplicityText.layoutYProperty().bind(line.endYProperty().subtract(5));

        drawingPane.getChildren().addAll(line, label, startMultiplicityText, endMultiplicityText);

        // Create UMLRelationshipBox
        UMLRelationshipBox relationshipBox = new UMLRelationshipBox(
                "Association",
                startName,
                endName,
                name,
                startMultiplicity,
                endMultiplicity,
                line,
                label,
                startMultiplicityText,
                endMultiplicityText
        );

        // Add the relationship to the model without triggering deserialization
        classDiagramManager.addRelationshipBox(relationshipBox);
        lastRelationshipBox = relationshipBox;*/
    }

    /**
     * Retrieves the name of the element represented by the VBox.
     *
     * @param box The VBox representing the UML element.
     * @return The name of the element.
     */
    private String getElementName(VBox box) {
        // Assuming the first child is the name label
        if (box.getChildren().isEmpty()) return "Unknown";
        Node node = box.getChildren().get(0);
        if (node instanceof Label) {
            return ((Label) node).getText();
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

    // Add listeners to update the line dynamically when classes are moved
    private void addDynamicUpdateListener(Line line, VBox start, VBox end, Text associationLabel, Text startMultiplicityText, Text endMultiplicityText) {
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, start, end, associationLabel, startMultiplicityText, endMultiplicityText));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, start, end, associationLabel, startMultiplicityText, endMultiplicityText));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, start, end, associationLabel, startMultiplicityText, endMultiplicityText));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, start, end, associationLabel, startMultiplicityText, endMultiplicityText));
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
