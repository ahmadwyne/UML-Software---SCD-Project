package com.example.umlscd.BusinessLayer.ClassDiagram;

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
 * <h1>Aggregation Manager</h1>
 *
 * <p>The {@code AggregationManager} is responsible for creating and rendering aggregation relationships between
 * classes in a UML class diagram. This class handles the creation of aggregation lines, multiplicity labels, and
 * relationship labels, as well as dynamically updating these elements when classes are moved.</p>
 *
 * <p>It inherits from {@link ClassDiagramRelationsManager} and implements the abstract method {@link #createRelationship},
 * which is used to create an aggregation relationship between two classes.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-04</p>
 */
public class AggregationManager extends ClassDiagramRelationsManager {

    private final ClassDiagramManager classDiagramManager;
    private UMLRelationshipBox lastRelationshipBox;

    /**
     * Constructs an {@code AggregationManager} instance.
     * <p>This constructor initializes the manager with the specified {@link ClassDiagramManager} and enables the
     * aggregation mode by default.</p>
     *
     * @param manager The {@link ClassDiagramManager} instance managing the overall diagram.
     */
    public AggregationManager(ClassDiagramManager manager) {
        this.classDiagramManager = manager;
        enableAggregationMode();
    }

    /**
     * Creates an aggregation relationship between two classes.
     * <p>This method calculates the closest boundary points of the two given {@code VBox} elements representing the
     * classes and creates an aggregation line between them. It also creates labels for the composition name and the
     * multiplicity of both ends of the relationship. The line and labels are added to the {@code drawingPane}.</p>
     *
     * @param start The starting class (VBox) for the aggregation.
     * @param end The ending class (VBox) for the aggregation diamond.
     * @param drawingPane The pane where the aggregation line and labels will be drawn.
     * @param aggregationName The name of the aggregation.
     * @param startMultiplicity The multiplicity at the start of the aggregation (e.g., "1", "0..*").
     * @param endMultiplicity The multiplicity at the end of the aggregation (e.g., "1", "0..*").
     */
    @Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String aggregationName, String startMultiplicity, String endMultiplicity) {
        // Disable aggregation mode immediately after starting the process
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
                0.0, 0.0,      // Top
                10.0, -10.0,   // Top-left
                20.0, 0.0,     // Bottom-right
                10.0, 10.0     // Bottom-left
        );
        diamond.setFill(Color.WHITE);
        diamond.setStroke(Color.BLACK); // Optionally add a border to the diamond

        // Attach diamond to the second class boundary (tip to the class boundary)
        double tipX = endPoint.getX();
        double tipY = endPoint.getY();

        // Offset the diamond to ensure its tip touches the class boundary
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Create the line connecting the first selected element (source) and the diamond (aggregation point)
        Line aggregationLine = new Line();
        aggregationLine.setStartX(startPoint.getX()); // Start at the closest boundary point of the first class
        aggregationLine.setStartY(startPoint.getY());
        aggregationLine.setEndX(tipX); // End at the diamond's tip
        aggregationLine.setEndY(tipY);

        // Style the aggregation line
        aggregationLine.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

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

        // Create UMLRelationshipBox
        UMLRelationshipBox relationshipBox = new UMLRelationshipBox(
                "Aggregation",
                getElementName(start),
                getElementName(end),
                aggregationName,
                startMultiplicity,
                endMultiplicity,
                aggregationLine,
                aggregationLabel,
                startMultiplicityText,
                endMultiplicityText
        );
        addEditDialogOnClick(aggregationLabel, "Edit Aggregation Name", newName -> {
            aggregationLabel.setText(newName);
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
        classDiagramManager.addRelationshipBox(relationshipBox);
        lastRelationshipBox = relationshipBox;
    }

    /**
     * This function add a dialog to edit aggregation name and multiplicity labels.
     * <p>Sets up a double-click event handler on the given <code>Text</code> element to display a text input dialog for editing the text value.
     * When the user double-clicks the <code>Text</code> element, a dialog is shown to allow them to enter a new value.
     * If the user enters a new value and presses "OK", the provided <code>onSave</code> consumer is called with the new value.</p>
     *
     * @param textElement The <code>Text</code> element that will trigger the edit dialog on double-click.
     * @param dialogTitle The title of the text input dialog.
     * @param onSave A <code>Consumer&lt;String&gt;</code> that will be called with the new value entered by the user. This consumer allows
     *               for custom handling of the new value (e.g., saving it or updating the UI).
     */
    public void addEditDialogOnClick(Text textElement, String dialogTitle, Consumer<String> onSave) {
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
     * Creates an aggregation relationship from a UMLRelationship model object during deserialization.
     * <p>This method recreates an aggregation relationship using data from a {@link UMLRelationship} object and
     * adds the elements (line, labels) to the specified {@code drawingPane}.</p>
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

        // Retrieve UMLElementBox objects from the map
        UMLElementBoxInterface startElement = classDiagramManager.getClassBoxMap().get(startName);
        UMLElementBoxInterface endElement = classDiagramManager.getClassBoxMap().get(endName);

        if (startElement == null || endElement == null) {
            System.err.println("Cannot create aggregation. One of the elements is missing.");
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

        // Create the diamond (aggregation) shape
        Polygon diamond = new Polygon();
        diamond.getPoints().addAll(
                0.0, 0.0,      // Top
                10.0, -10.0,   // Top-left
                20.0, 0.0,     // Bottom-right
                10.0, 10.0     // Bottom-left
        );
        diamond.setFill(Color.WHITE);
        diamond.setStroke(Color.BLACK); // Optionally add a border to the diamond

        // Attach diamond to the second class boundary (tip to the class boundary)
        double tipX = endPoint.getX();
        double tipY = endPoint.getY();

        // Offset the diamond to ensure its tip touches the class boundary
        diamond.setLayoutX(tipX - 10); // Offset to position the diamond correctly
        diamond.setLayoutY(tipY - 10); // Offset to position the diamond correctly

        // Create the line connecting the first selected element (source) and the diamond (aggregation point)
        Line aggregationLine = new Line();
        aggregationLine.setStartX(startPoint.getX()); // Start at the closest boundary point of the first class
        aggregationLine.setStartY(startPoint.getY());
        aggregationLine.setEndX(tipX); // End at the diamond's tip
        aggregationLine.setEndY(tipY);

        // Style the aggregation line
        aggregationLine.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");

        // Add the diamond and line to the drawing pane
        drawingPane.getChildren().addAll(diamond, aggregationLine);

        // Create the aggregation label
        Text aggregationLabel = new Text(name);
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
        addDynamicUpdateListener(aggregationLine, diamond, startBox, endBox, aggregationLabel, startMultiplicityText, endMultiplicityText);

        // Create UMLRelationshipBox
        UMLRelationshipBox relationshipBox = new UMLRelationshipBox(
                "Aggregation",
                startName,
                endName,
                name,
                startMultiplicity,
                endMultiplicity,
                aggregationLine,
                aggregationLabel,
                startMultiplicityText,
                endMultiplicityText
        );

        // Add the relationship to the manager
        classDiagramManager.addRelationshipBox(relationshipBox);

        // Store the last created relationship
        lastRelationshipBox = relationshipBox;

        // **Debug Logging**
        System.out.println("Created aggregation: " + name + " between " + startName + " and " + endName);
    }

    /**
     * Retrieves the name of the UML element from the VBox.
     * <p>This method extracts the name of the UML element by checking the first label in the {@code VBox}, which
     * typically contains the name of the class or interface.</p>
     *
     * @param box The VBox representing the UML element.
     * @return The name of the element.
     */
    public String getElementName(VBox box) {
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
     * Gets the last created UMLRelationshipBox.
     *
     * @return The last UMLRelationshipBox.
     */
    public UMLRelationshipBox getLastRelationshipBox() {
        return lastRelationshipBox;
    }

    /**
     * Adds dynamic listeners to update the aggregation line and diamond when classes are moved.
     * <p>This method attaches listeners to the {@code VBox} elements representing the classes involved in the
     * aggregation. The listeners update the position of the aggregation line and labels when the classes are moved.</p>
     *
     * @param line                   The aggregation line.
     * @param diamond                The aggregation diamond.
     * @param start                  The starting class box.
     * @param end                    The ending class box.
     * @param aggregationLabel       The label for the aggregation.
     * @param startMultiplicityText  The label for the start multiplicity.
     * @param endMultiplicityText    The label for the end multiplicity.
     */
    private void addDynamicUpdateListener(Line line, Polygon diamond, VBox start, VBox end, Text aggregationLabel, Text startMultiplicityText, Text endMultiplicityText) {
        // Update position when the start or end class is moved
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLineAndDiamondPosition(line, diamond, start, end, aggregationLabel, startMultiplicityText, endMultiplicityText));
    }

    /**
     * Finds the closest point on the diamond from a given point.
     * <p>This method calculates the closest points between the first {@code VBox} element representing
     * the class 1 and the diamond corner attached to the second {@code VBox} element.</p>
     *
     * @param diamond The aggregation diamond.
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
     * Updates the position of the aggregation line and diamond when either class is moved.
     * <p>This method recalculates the closest boundary points between the two {@code VBox} elements representing
     * the classes, updates the position of the aggregation line and diamond, and rebinds the labels (aggregation name and
     * multiplicity) to the updated line.</p>
     *
     * @param line                   The aggregation line.
     * @param diamond                The aggregation diamond.
     * @param start                  The starting class box.
     * @param end                    The ending class box.
     * @param aggregationLabel       The label for the aggregation.
     * @param startMultiplicityText  The label for the start multiplicity.
     * @param endMultiplicityText    The label for the end multiplicity.
     */
    private void updateLineAndDiamondPosition(Line line, Polygon diamond, VBox start, VBox end, Text aggregationLabel, Text startMultiplicityText, Text endMultiplicityText) {
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

        // Rebind the aggregation label and multiplicity labels to the new line positions
        bindAssociationLabelToLine(aggregationLabel, line);
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
    }
}