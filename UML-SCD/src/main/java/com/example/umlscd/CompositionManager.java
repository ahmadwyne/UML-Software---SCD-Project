package com.example.umlscd;


import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;

public class CompositionManager extends ClassDiagramRelationsManager {

    // Constructor
    public CompositionManager() {
        // Enable aggregation mode by default
        enableCompositionMode();
    }

    @Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String compositionName, String startMultiplicity, String endMultiplicity) {
        // Disable association mode immediately after starting the process
        disableCompositionMode();

        // Set default values if fields are empty
        if (compositionName == null || compositionName.isEmpty()) compositionName = "Aggregation";
        if (startMultiplicity == null || startMultiplicity.isEmpty()) startMultiplicity = "1";
        if (endMultiplicity == null || endMultiplicity.isEmpty()) endMultiplicity = "1";

        // Find the closest boundary points between the two classes
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Create the diamond (aggregation) shape
        Polygon diamond = new Polygon();
        diamond.getPoints().addAll(
                0.0, 0.0,  // top
                10.0, -10.0,  // top-left
                20.0, 0.0,  // bottom-right
                10.0, 10.0  // bottom-left
        );
        diamond.setFill(Color.BLACK);
        diamond.setStroke(Color.BLACK); // Optionally add a border to the diamond

        // Attach diamond to the second class boundary (tip to the class boundary)
        double secondElementCenterX = end.getLayoutX() + end.getWidth() / 2;
        double secondElementCenterY = end.getLayoutY() + end.getHeight() / 2;
        double tipX = secondElementCenterX;
        double tipY = end.getLayoutY(); // Position tip at the top of the second class

        // Offset the diamond to ensure its tip touches the class boundary (not center)
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Create the line connecting the first selected element (source) and the diamond (aggregation point)
        Line compositionLine = new Line();
        compositionLine.setStartX(start.getLayoutX() + start.getWidth() / 2); // Start at the center of the first class
        compositionLine.setStartY(start.getLayoutY() + start.getHeight() / 2);
        compositionLine.setEndX(tipX); // End at the diamond's tip
        compositionLine.setEndY(tipY); // End at the diamond's tip

        // Add the diamond and line to the drawing pane
        drawingPane.getChildren().addAll(diamond, compositionLine);

        // Create the aggregation label
        Text compositionLabel = new Text(compositionName);
        compositionLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
        bindAssociationLabelToLine(compositionLabel, compositionLine);
        drawingPane.getChildren().add(compositionLabel);

        // Create multiplicity labels
        Text startMultiplicityText = new Text(startMultiplicity);
        Text endMultiplicityText = new Text(endMultiplicity);
        startMultiplicityText.setStyle("-fx-font-size: 10;");
        endMultiplicityText.setStyle("-fx-font-size: 10;");
        bindMultiplicityLabelToLine(startMultiplicityText, compositionLine, true);
        bindMultiplicityLabelToLine(endMultiplicityText, compositionLine, false);
        drawingPane.getChildren().addAll(startMultiplicityText, endMultiplicityText);

        // Add listeners to update the line and diamond when either class is moved
        addDynamicUpdateListener(compositionLine, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText);
    }

    // Add listeners to update the line and diamond dynamically when classes are moved
    private void addDynamicUpdateListener(Line line, Polygon diamond, VBox start, VBox end, Text compositionLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Update position when the start or end class is moved
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
    }

    // Update the line and diamond's position when either class is moved
    private void updateLineAndDiamondPosition(Line line, Polygon diamond, VBox start, VBox end, Text compositionLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Recalculate the closest boundary points
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Update the line's position
        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());

        // Position the line end at the tip of the diamond
        double secondElementCenterX = end.getLayoutX() + end.getWidth() / 2;
        double tipX = secondElementCenterX;
        double tipY = end.getLayoutY(); // Tip should be attached to the class boundary

        line.setEndX(tipX);
        line.setEndY(tipY);

        // Update the diamond position, keeping its tip attached to the end class boundary
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Rebind the association label and multiplicity labels to the new line
        bindAssociationLabelToLine(compositionLabel, line);
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
    }
}

// Update the line and labels when either class is moved
    /*private void updateLinePosition(Line line, VBox start, VBox end, Text associationLabel, Text startMultiplicityText, Text endMultiplicityText) {
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
}*/