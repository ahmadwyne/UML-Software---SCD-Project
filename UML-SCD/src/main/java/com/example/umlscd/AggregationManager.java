package com.example.umlscd;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;

public class AggregationManager extends ClassDiagramRelationsManager {

    // Constructor
    public AggregationManager() {
        // Enable aggregation mode by default
        enableAggregationMode();
    }

    @Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String aggregationName, String startMultiplicity, String endMultiplicity) {
        // Disable association mode immediately after starting the process
        disableAggregationMode();

        // Set default values if fields are empty
        if (aggregationName == null || aggregationName.isEmpty()) aggregationName = "Aggregation";
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
        diamond.setFill(Color.WHITE);
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

        // Create the line connecting the first selected element (source) and the diamond (aggregation point)
        Line aggregationLine = new Line();
        aggregationLine.setStartX(start.getLayoutX() + start.getWidth() / 2); // Start at the center of the first class
        aggregationLine.setStartY(start.getLayoutY() + start.getHeight() / 2);

        // Find the closest point on the diamond from the start point
        Point closestPointOnDiamond = getClosestPointOnDiamond(diamond, aggregationLine.getStartX(), aggregationLine.getStartY());

        // Set the line's endpoint to this closest point on the diamond
        aggregationLine.setEndX(closestPointOnDiamond.getX());
        aggregationLine.setEndY(closestPointOnDiamond.getY());

        // Add the diamond and line to the drawing pane
        drawingPane.getChildren().addAll(diamond, aggregationLine);

        // Create the aggregation label
        Text aggregationLabel = new Text(aggregationName);
        aggregationLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
        bindAssociationLabelToLine(aggregationLabel, aggregationLine);
        drawingPane.getChildren().add(aggregationLabel);

        // Create multiplicity labels
        Text startMultiplicityText = new Text(startMultiplicity);
        Text endMultiplicityText = new Text(endMultiplicity);
        startMultiplicityText.setStyle("-fx-font-size: 10;");
        endMultiplicityText.setStyle("-fx-font-size: 10;");
        bindMultiplicityLabelToLine(startMultiplicityText, aggregationLine, true);
        bindMultiplicityLabelToLine(endMultiplicityText, aggregationLine, false);
        drawingPane.getChildren().addAll(startMultiplicityText, endMultiplicityText);

        // Add listeners to update the line and diamond when either class is moved
        addDynamicUpdateListener(aggregationLine, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText);
    }

    // Add listeners to update the line and diamond dynamically when classes are moved
    private void addDynamicUpdateListener(Line line, Polygon diamond, VBox start, VBox end, Text aggregationLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Update position when the start or end class is moved
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
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
    private void updateLineAndDiamondPosition(Line line, Polygon diamond, VBox start, VBox end, Text aggregationLabel, Text startMultiplicityText, Text endMultiplicityText) {
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
        bindAssociationLabelToLine(aggregationLabel, line);
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
    }

    /*@Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String aggregationName, String startMultiplicity, String endMultiplicity) {
        // Disable association mode immediately after starting the process
        disableAggregationMode();

        // Set default values if fields are empty
        if (aggregationName == null || aggregationName.isEmpty()) aggregationName = "Aggregation";
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
        diamond.setFill(Color.WHITE);
        diamond.setStroke(Color.BLACK); // Optionally add a border to the diamond

        // Attach diamond to the second class boundary (tip to the class boundary)
        double secondElementCenterX = end.getLayoutX() + end.getWidth() / 2;
        double secondElementCenterY = end.getLayoutY() + end.getHeight() / 2;
        //double tipX = secondElementCenterX;
        //double tipY = end.getLayoutY(); // Position tip at the top of the second class

        double tipX = endPoint.getX();
        double tipY = endPoint.getY();

        // Offset the diamond to ensure its tip touches the class boundary (not center)
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Create the line connecting the first selected element (source) and the diamond (aggregation point)
        Line aggregationLine = new Line();
        aggregationLine.setStartX(start.getLayoutX() + start.getWidth() / 2); // Start at the center of the first class
        aggregationLine.setStartY(start.getLayoutY() + start.getHeight() / 2);
        aggregationLine.setEndX(tipX); // End at the diamond's tip
        aggregationLine.setEndY(tipY); // End at the diamond's tip

        // Add the diamond and line to the drawing pane
        drawingPane.getChildren().addAll(diamond, aggregationLine);

        // Create the aggregation label
        Text aggregationLabel = new Text(aggregationName);
        aggregationLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
        bindAssociationLabelToLine(aggregationLabel, aggregationLine);
        drawingPane.getChildren().add(aggregationLabel);

        // Create multiplicity labels
        Text startMultiplicityText = new Text(startMultiplicity);
        Text endMultiplicityText = new Text(endMultiplicity);
        startMultiplicityText.setStyle("-fx-font-size: 10;");
        endMultiplicityText.setStyle("-fx-font-size: 10;");
        bindMultiplicityLabelToLine(startMultiplicityText, aggregationLine, true);
        bindMultiplicityLabelToLine(endMultiplicityText, aggregationLine, false);
        drawingPane.getChildren().addAll(startMultiplicityText, endMultiplicityText);

        // Add listeners to update the line and diamond when either class is moved
        addDynamicUpdateListener(aggregationLine, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText);
    }

    // Add listeners to update the line and diamond dynamically when classes are moved
    private void addDynamicUpdateListener(Line line, Polygon diamond, VBox start, VBox end, Text aggregationLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Update position when the start or end class is moved
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
    }

    // Update the line and diamond's position when either class is moved
    private void updateLineAndDiamondPosition(Line line, Polygon diamond, VBox start, VBox end, Text aggregationLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Recalculate the closest boundary points
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Update the line's position
        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());

        // Position the line end at the tip of the diamond
        double secondElementCenterX = end.getLayoutX() + end.getWidth() / 2;
        double tipX = endPoint.getX();
        double tipY = endPoint.getY(); // Tip should be attached to the class boundary

        line.setEndX(tipX);
        line.setEndY(tipY);


        // Update the diamond position, keeping its tip attached to the end class boundary
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Rebind the association label and multiplicity labels to the new line
        bindAssociationLabelToLine(aggregationLabel, line);
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
    }*/
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