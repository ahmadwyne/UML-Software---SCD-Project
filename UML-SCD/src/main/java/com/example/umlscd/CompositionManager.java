package com.example.umlscd;


import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;

public class CompositionManager extends ClassDiagramRelationsManager {

    private UMLRelationshipBox lastRelationshipBox = null; // To keep track of the last created relationship
    private ClassDiagramManager classDiagramManager;

    // Constructor
    public CompositionManager() {
        // Enable aggregation mode by default
        enableCompositionMode();
    }

    // Constructor
    public CompositionManager(ClassDiagramManager manager) {
        // Enable aggregation mode by default
        enableAggregationMode();
        this.classDiagramManager = manager;
    }
    @Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String compositionName, String startMultiplicity, String endMultiplicity) {
        // Disable composition mode immediately after starting the process
        disableCompositionMode();

        // Set default values if fields are empty
        if (compositionName == null || compositionName.isEmpty()) compositionName = "Composition";
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

        // Find the tip position to attach the diamond (closest boundary)
        double tipX = endPoint.getX();
        double tipY = endPoint.getY();

        // Offset the diamond to ensure its tip touches the class boundary
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Create the line connecting the first selected element (source) and the diamond (composition point)
        Line compositionLine = new Line();
        compositionLine.setStartX(start.getLayoutX() + start.getWidth() / 2); // Start at the center of the first class
        compositionLine.setStartY(start.getLayoutY() + start.getHeight() / 2);

        // Find the closest point on the diamond from the start point
        Point closestPointOnDiamond = getClosestPointOnDiamond(diamond, compositionLine.getStartX(), compositionLine.getStartY());

        // Set the line's endpoint to this closest point on the diamond
        compositionLine.setEndX(closestPointOnDiamond.getX());
        compositionLine.setEndY(closestPointOnDiamond.getY());

        // Add the diamond and line to the drawing pane
        drawingPane.getChildren().addAll(diamond, compositionLine);

        // Create the composition label
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

        // After creating the aggregation elements (line, diamond, labels), create UMLRelationshipBox
        UMLRelationshipBox relationshipBox = new UMLRelationshipBox(
                "Composition",
                getElementName(start),
                getElementName(end),
                compositionName,
                startMultiplicity,
                endMultiplicity,
                compositionLine,
                compositionLabel,
                startMultiplicityText,
                endMultiplicityText
        );

        // Add the relationship to the manager
        classDiagramManager.createRelationshipFromSerialization(relationshipBox.getUmlRelationship());

        // Store the last created relationship
        lastRelationshipBox = relationshipBox;
    }

    /**
     * Retrieves the name of the UML element from the VBox.
     *
     * @param box The VBox representing the UML element.
     * @return The name of the element.
     */
    private String getElementName(VBox box) {
        Label label = (Label) box.getChildren().get(0);
        return label.getText();
    }

    /**
     * Gets the last created UMLRelationshipBox.
     *
     * @return The last UMLRelationshipBox.
     */
    public UMLRelationshipBox getLastRelationshipBox() {
        return lastRelationshipBox;
    }

    // Add listeners to update the line and diamond dynamically when classes are moved
    private void addDynamicUpdateListener(Line line, Polygon diamond, VBox start, VBox end, Text compositionLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Update position when the start or end class is moved
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
    }

    // Function to find the closest point on the diamond from a given point
    private Point getClosestPointOnDiamond(Polygon diamond, double startX, double startY) {
        double closestDistance = Double.MAX_VALUE;
        Point closestPoint = null;

        // Iterate through the vertices of the diamond
        for (int i = 0; i < diamond.getPoints().size(); i += 2) {
            double x = diamond.getPoints().get(i) + diamond.getLayoutX();
            double y = diamond.getPoints().get(i + 1) + diamond.getLayoutY();

            // Calculate the distance from the point to the start point
            double distance = Math.sqrt(Math.pow(x - startX, 2) + Math.pow(y - startY, 2));

            if (distance < closestDistance) {
                closestDistance = distance;
                closestPoint = new Point(x, y);
            }
        }

        return closestPoint;
    }

    // Update the line and diamond's position when either class is moved
    private void updateLineAndDiamondPosition(Line line, Polygon diamond, VBox start, VBox end, Text compositionLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Recalculate the closest boundary points
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Update the line's position
        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());

        // Find the closest point on the diamond from the line's start
        Point closestPointOnDiamond = getClosestPointOnDiamond(diamond, line.getStartX(), line.getStartY());

        // Set the line's endpoint to this closest point on the diamond
        line.setEndX(closestPointOnDiamond.getX());
        line.setEndY(closestPointOnDiamond.getY());

        // Update the diamond position, keeping its tip attached to the end class boundary
        diamond.setLayoutX(endPoint.getX() - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(endPoint.getY() - 10); // Offset to position the diamond correctly

        // Rebind the association label and multiplicity labels to the new line
        bindAssociationLabelToLine(compositionLabel, line);
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
    }
}
    
    /*@Override
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
