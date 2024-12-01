package com.example.umlscd;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;

public class AssociationManager extends ClassDiagramRelationsManager {

    // Constructor
    public AssociationManager() {
        // Enable association mode by default
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
