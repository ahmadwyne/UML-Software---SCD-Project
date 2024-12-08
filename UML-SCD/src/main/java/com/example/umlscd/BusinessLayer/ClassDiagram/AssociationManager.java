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
import javafx.scene.text.Text;
import javafx.scene.shape.Line;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * <h1>Association Manager</h1>
 *
 * <p>The {@code AssociationManager} is responsible for creating and rendering association relationships between
 * classes in a UML class diagram. This class handles the creation of association lines, multiplicity labels, and
 * relationship labels, as well as dynamically updating these elements when classes are moved.</p>
 *
 * <p>It inherits from {@link ClassDiagramRelationsManager} and implements the abstract method {@link #createRelationship},
 * which is used to create an association relationship between two classes.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-04</p>
 */
public class AssociationManager extends ClassDiagramRelationsManager {

    private final ClassDiagramManager classDiagramManager;
    private UMLRelationshipBox lastRelationshipBox;

    /**
     * Constructs an {@code AssociationManager} instance.
     * <p>This constructor initializes the manager with the specified {@link ClassDiagramManager} and enables the
     * association mode by default.</p>
     *
     * @param manager The {@link ClassDiagramManager} instance managing the overall diagram.
     */
    public AssociationManager(ClassDiagramManager manager) {
        this.classDiagramManager = manager;
        enableAssociationMode();
    }

    /**
     * Creates an association relationship between two classes.
     * <p>This method calculates the closest boundary points of the two given {@code VBox} elements representing the
     * classes and creates an association line between them. It also creates labels for the association name and the
     * multiplicity of both ends of the relationship. The line and labels are added to the {@code drawingPane}.</p>
     *
     * @param start The starting class (VBox) for the association.
     * @param end The ending class (VBox) for the association.
     * @param drawingPane The pane where the association line and labels will be drawn.
     * @param associationName The name of the association.
     * @param startMultiplicity The multiplicity at the start of the association (e.g., "1", "0..*").
     * @param endMultiplicity The multiplicity at the end of the association (e.g., "1", "0..*").
     */
    @Override
    public void createRelationship(VBox start, VBox end, Pane drawingPane, String associationName, String startMultiplicity, String endMultiplicity) {
        disableAssociationMode();

        if (associationName == null || associationName.isEmpty()) associationName = "Association";
        if (startMultiplicity == null || startMultiplicity.isEmpty()) startMultiplicity = "1";
        if (endMultiplicity == null || endMultiplicity.isEmpty()) endMultiplicity = "1";

        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        Line line = new Line();
        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());
        line.setEndX(endPoint.getX());
        line.setEndY(endPoint.getY());
        line.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
        drawingPane.getChildren().add(line);

        Text associationLabel = new Text(associationName);
        associationLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
        bindAssociationLabelToLine(associationLabel, line);
        drawingPane.getChildren().add(associationLabel);

        Text startMultiplicityText = new Text(startMultiplicity);
        Text endMultiplicityText = new Text(endMultiplicity);
        startMultiplicityText.setStyle("-fx-font-size: 10;");
        endMultiplicityText.setStyle("-fx-font-size: 10;");
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
        drawingPane.getChildren().addAll(startMultiplicityText, endMultiplicityText);

        addDynamicUpdateListener(line, start, end, associationLabel, startMultiplicityText, endMultiplicityText);

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

        addEditDialogOnClick(associationLabel, "Edit Association Name", newName -> {
            associationLabel.setText(newName);
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
     * This function adds a dialog to edit association name and multiplicity labels.
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
     * Creates an association relationship from a UMLRelationship model object during deserialization.
     * <p>This method recreates an association relationship using data from a {@link UMLRelationship} object and
     * adds the elements (line, labels) to the specified {@code drawingPane}.</p>
     *
     * @param umlRelationship The {@link UMLRelationship} data containing information about the association.
     * @param drawingPane The pane where the association elements will be drawn.
     */
    public void createRelationshipFromModel(UMLRelationship umlRelationship, Pane drawingPane) {
        String startName = umlRelationship.getStartElementName();
        String endName = umlRelationship.getEndElementName();
        String name = umlRelationship.getName();
        String startMultiplicity = umlRelationship.getStartMultiplicity();
        String endMultiplicity = umlRelationship.getEndMultiplicity();

        UMLElementBoxInterface startClass = classDiagramManager.getClassBoxMap().get(startName);
        UMLElementBoxInterface endClass = classDiagramManager.getClassBoxMap().get(endName);

        if (startClass == null || endClass == null) {
            System.err.println("Cannot create association. One of the elements is missing.");
            return;
        }

        VBox startBox = startClass.getVisualRepresentation();
        VBox endBox = endClass.getVisualRepresentation();

        if (startBox == null || endBox == null) {
            System.err.println("Visual representation not available for one of the classes.");
            return;
        }

        double startX = startBox.getLayoutX() + startBox.getWidth() / 2;
        double startY = startBox.getLayoutY() + startBox.getHeight() / 2;

        double endX = endBox.getLayoutX() + endBox.getWidth() / 2;
        double endY = endBox.getLayoutY() + endBox.getHeight() / 2;

        Line associationLine = new Line(startX, startY, endX, endY);
        associationLine.setStroke(Color.BLACK);
        associationLine.setStrokeWidth(2);

        Text label = new Text(name);
        label.setX((startX + endX) / 2);
        label.setY((startY + endY) / 2 - 5);

        Text startMultiplicityText = new Text(startMultiplicity);
        Text endMultiplicityText = new Text(endMultiplicity);
        startMultiplicityText.setX(startX - 10);
        startMultiplicityText.setY(startY - 10);

        endMultiplicityText.setX(endX + 10);
        endMultiplicityText.setY(endY - 10);

        drawingPane.getChildren().addAll(associationLine, label, startMultiplicityText, endMultiplicityText);

        addDynamicUpdateListener(associationLine, startBox, endBox, label, startMultiplicityText, endMultiplicityText);

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

        classDiagramManager.addRelationshipBox(relationshipBox);
        lastRelationshipBox = relationshipBox;

        System.out.println("Created association: " + name + " between " + startName + " and " + endName);
    }

    /**
     * Retrieves the name of the UML element from the VBox.
     * <p>This method extracts the name of the UML element by checking the first label in the {@code VBox}, which
     * typically contains the name of the class or interface.</p>
     *
     * @param box The {@code VBox} representing the UML element.
     * @return The name of the element.
     */
    private String getElementName(VBox box) {
        if (box.getChildren().isEmpty()) return "Unknown";

        Node firstNode = box.getChildren().get(0);
        if (firstNode instanceof Label) {
            Label firstLabel = (Label) firstNode;
            if (firstLabel.getText().equals("<<Interface>>")) {
                if (box.getChildren().size() > 1 && box.getChildren().get(1) instanceof Label) {
                    Label nameLabel = (Label) box.getChildren().get(1);
                    return nameLabel.getText();
                }
            } else {
                return firstLabel.getText();
            }
        }

        return "Unknown";
    }

    /**
     * Retrieves the last created UMLRelationshipBox.
     * <p>This method returns the last created {@link UMLRelationshipBox}, which contains information about the
     * most recently created relationship.</p>
     *
     * @return The last {@link UMLRelationshipBox} created.
     */
    public UMLRelationshipBox getLastRelationshipBox() {
        return lastRelationshipBox;
    }

    /**
     * Adds dynamic listeners to update the association line and labels when classes are moved.
     * <p>This method attaches listeners to the {@code VBox} elements representing the classes involved in the
     * association. The listeners update the position of the association line and labels when the classes are moved.</p>
     *
     * @param line The association line to be updated.
     * @param startBox The {@code VBox} representing the starting class.
     * @param endBox The {@code VBox} representing the ending class.
     * @param associationLabel The label for the association name.
     * @param startMultiplicityText The label for the start multiplicity.
     * @param endMultiplicityText The label for the end multiplicity.
     */
    public void addDynamicUpdateListener(Line line, VBox startBox, VBox endBox, Text associationLabel, Text startMultiplicityText, Text endMultiplicityText) {
        startBox.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, startBox, endBox, associationLabel, startMultiplicityText, endMultiplicityText));
        startBox.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, startBox, endBox, associationLabel, startMultiplicityText, endMultiplicityText));

        endBox.layoutXProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, startBox, endBox, associationLabel, startMultiplicityText, endMultiplicityText));
        endBox.layoutYProperty().addListener((obs, oldVal, newVal) -> updateLinePosition(line, startBox, endBox, associationLabel, startMultiplicityText, endMultiplicityText));
    }

    /**
     * Updates the position of the association line and its associated labels when either class is moved.
     * <p>This method recalculates the closest boundary points between the two {@code VBox} elements representing
     * the classes, updates the position of the association line, and rebinds the labels (association name and
     * multiplicity) to the updated line.</p>
     *
     * @param line The association line to be updated.
     * @param start The {@code VBox} representing the starting class.
     * @param end The {@code VBox} representing the ending class.
     * @param associationLabel The label for the association name.
     * @param startMultiplicityText The label for the start multiplicity.
     * @param endMultiplicityText The label for the end multiplicity.
     */
    private void updateLinePosition(Line line, VBox start, VBox end, Text associationLabel, Text startMultiplicityText, Text endMultiplicityText) {
        Point startPoint = getClosestBoundaryPoint(start, end);
        Point endPoint = getClosestBoundaryPoint(end, start);

        line.setStartX(startPoint.getX());
        line.setStartY(startPoint.getY());
        line.setEndX(endPoint.getX());
        line.setEndY(endPoint.getY());

        bindAssociationLabelToLine(associationLabel, line);
        bindMultiplicityLabelToLine(startMultiplicityText, line, true);
        bindMultiplicityLabelToLine(endMultiplicityText, line, false);
    }

}
