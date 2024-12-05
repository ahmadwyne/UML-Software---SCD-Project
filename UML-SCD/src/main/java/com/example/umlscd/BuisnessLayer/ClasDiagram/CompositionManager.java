package com.example.umlscd.BuisnessLayer.ClasDiagram;

import com.example.umlscd.Models.ClassDiagram.UMLElementBoxInterface;
import com.example.umlscd.Models.ClassDiagram.UMLRelationship;
import com.example.umlscd.Models.ClassDiagram.UMLRelationshipBox;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Manages the creation and rendering of Composition relationships.
 */
public class CompositionManager extends ClassDiagramRelationsManager {

    private final ClassDiagramManager classDiagramManager;
    private UMLRelationshipBox lastRelationshipBox;

    /**
     * Constructor that initializes the CompositionManager with a ClassDiagramManager.
     *
     * @param manager The ClassDiagramManager instance.
     */
    public CompositionManager(ClassDiagramManager manager) {
        this.classDiagramManager = manager;
        enableCompositionMode();
    }

    /**
     * Creates a composition relationship between two UML elements.
     *
     * @param start            The starting UML element (source).
     * @param end              The ending UML element (target).
     * @param drawingPane      The pane where the relationship is drawn.
     * @param compositionName  The name of the composition relationship.
     * @param startMultiplicity The multiplicity at the start end.
     * @param endMultiplicity   The multiplicity at the end end.
     */
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

        // Create the diamond (composition) shape
        Polygon diamond = new Polygon();
        diamond.getPoints().addAll(
                0.0, 0.0,      // Top
                10.0, -10.0,   // Top-left
                20.0, 0.0,     // Bottom-right
                10.0, 10.0     // Bottom-left
        );
        diamond.setFill(Color.BLACK); // Filled black diamond for composition
        diamond.setStroke(Color.BLACK); // Black border

        // Attach diamond to the second class boundary (tip to the class boundary)
        double tipX = endPoint.getX();
        double tipY = endPoint.getY();

        // Offset the diamond to ensure its tip touches the class boundary
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Create the line connecting the first selected element (source) and the diamond (composition point)
        Line compositionLine = new Line();
        compositionLine.setStartX(startPoint.getX()); // Start at the closest boundary point of the first class
        compositionLine.setStartY(startPoint.getY());
        compositionLine.setEndX(tipX); // End at the diamond's tip
        compositionLine.setEndY(tipY);

        // Style the composition line
        compositionLine.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

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

        // Create UMLRelationshipBox
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


        addEditDialogOnClick(compositionLabel, "Edit Composition Name", newName -> {
            compositionLabel.setText(newName);
            relationshipBox.setAssociationName(newName); // Update the relationship box
        });

        addEditDialogOnClick(startMultiplicityText, "Edit Start Multiplicity", newValue -> {
            startMultiplicityText.setText(newValue);
            relationshipBox.setStartMultiplicity(newValue); // Update the relationship box
        });

        addEditDialogOnClick(endMultiplicityText, "Edit End Multiplicity", newValue -> {
            endMultiplicityText.setText(newValue);
            relationshipBox.setEndMultiplicity(newValue); // Update the relationship box
        });
        // **Correction:** Use addRelationshipBox instead of createRelationshipFromSerialization
        classDiagramManager.addRelationshipBox(relationshipBox);

        // Store the last created relationship
        lastRelationshipBox = relationshipBox;

        // **Debug Logging**
        System.out.println("Created composition: " + compositionName + " between " + getElementName(start) + " and " + getElementName(end));
    }

private void addEditDialogOnClick(Text textElement, String dialogTitle, Consumer<String> onSave) {
    textElement.setOnMouseClicked(event -> {
        if (event.getClickCount() == 2) { // Double-click to edit
            TextInputDialog dialog = new TextInputDialog(textElement.getText());
            dialog.setTitle(dialogTitle);
            dialog.setHeaderText(null);
            dialog.setContentText("Enter new value:");

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newValue -> {
                onSave.accept(newValue);
            });
        }
    });
}
    /**
     * Creates a composition relationship from a UMLRelationship model object during deserialization.
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
            System.err.println("Cannot create composition. One of the elements is missing.");
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

        // Create the diamond (composition) shape
        Polygon diamond = new Polygon();
        diamond.getPoints().addAll(
                0.0, 0.0,      // Top
                10.0, -10.0,   // Top-left
                20.0, 0.0,     // Bottom-right
                10.0, 10.0     // Bottom-left
        );
        diamond.setFill(Color.BLACK); // Filled black diamond for composition
        diamond.setStroke(Color.BLACK); // Black border

        // Attach diamond to the second class boundary (tip to the class boundary)
        double tipX = endPoint.getX();
        double tipY = endPoint.getY();

        // Offset the diamond to ensure its tip touches the class boundary
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Create the line connecting the first selected element (source) and the diamond (composition point)
        Line compositionLine = new Line();
        compositionLine.setStartX(startPoint.getX()); // Start at the closest boundary point of the first class
        compositionLine.setStartY(startPoint.getY());
        compositionLine.setEndX(tipX); // End at the diamond's tip
        compositionLine.setEndY(tipY);

        // Style the composition line
        compositionLine.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

        // Add the diamond and line to the drawing pane
        drawingPane.getChildren().addAll(diamond, compositionLine);

        // Create the composition label
        Text compositionLabel = new Text(name);
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
        addDynamicUpdateListener(compositionLine, diamond, startBox, endBox, compositionLabel, startMultiplicityText, endMultiplicityText);

        // Create UMLRelationshipBox
        UMLRelationshipBox relationshipBox = new UMLRelationshipBox(
                "Composition",
                startName,
                endName,
                name,
                startMultiplicity,
                endMultiplicity,
                compositionLine,
                compositionLabel,
                startMultiplicityText,
                endMultiplicityText
        );

        // Add the relationship to the manager
        classDiagramManager.addRelationshipBox(relationshipBox);

        // Store the last created relationship
        lastRelationshipBox = relationshipBox;

        // **Debug Logging**
        System.out.println("Created composition: " + name + " between " + startName + " and " + endName);
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
     * Adds dynamic listeners to update the composition line and diamond when classes are moved.
     *
     * @param line                   The composition line.
     * @param diamond                The composition diamond.
     * @param start                  The starting class box.
     * @param end                    The ending class box.
     * @param compositionLabel       The label for the composition.
     * @param startMultiplicityText  The label for the start multiplicity.
     * @param endMultiplicityText    The label for the end multiplicity.
     */
    private void addDynamicUpdateListener(Line line, Polygon diamond, VBox start, VBox end, Text compositionLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Update position when the start or end class is moved
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, compositionLabel, startMultiplicityText, endMultiplicityText));
    }

    /**
     * Finds the closest point on the diamond from a given point.
     *
     * @param diamond The composition diamond.
     * @param startX  The X coordinate of the start point.
     * @param startY  The Y coordinate of the start point.
     * @return The closest point on the diamond.
     */
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

    /**
     * Updates the position of the composition line and diamond when either class is moved.
     *
     * @param line                   The composition line.
     * @param diamond                The composition diamond.
     * @param start                  The starting class box.
     * @param end                    The ending class box.
     * @param compositionLabel       The label for the composition.
     * @param startMultiplicityText  The label for the start multiplicity.
     * @param endMultiplicityText    The label for the end multiplicity.
     */
    private void updateLineAndDiamondPosition(Line line, Polygon diamond, VBox start, VBox end, Text compositionLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Recalculate the closest boundary points
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        // Update the line's start position
        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());

        // Find the closest point on the diamond from the line's start
        Point closestPointOnDiamond = getClosestPointOnDiamond(diamond, line.getStartX(), line.getStartY());

        // Update the line's end position to the closest point on the diamond
        line.setEndX(closestPointOnDiamond.getX());
        line.setEndY(closestPointOnDiamond.getY());

        // Update the diamond position, keeping its tip attached to the end class boundary
        double tipX = endPoint.getX();
        double tipY = endPoint.getY();
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Rebind the composition label and multiplicity labels to the new line positions
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
