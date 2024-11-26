package com.example.umlscd;

import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Optional;

public class ClassDiagramRelationsManager {

    private boolean associationModeEnabled = false; // Flag to track association mode

    public void enableAssociationMode() {
        associationModeEnabled = true;
    }

    public void disableAssociationMode() {
        associationModeEnabled = false;
    }

    public boolean isAssociationModeEnabled() {
        return associationModeEnabled;
    }

    public void createAssociation(VBox start, VBox end, Pane drawingPane, String associationName, String startMultiplicity, String endMultiplicity) {
        // Disable association mode immediately after starting the process
        disableAssociationMode();

        // Set default values if fields are empty
        if (associationName == null || associationName.isEmpty()) associationName = "associationName";
        if (startMultiplicity == null || startMultiplicity.isEmpty()) startMultiplicity = "1";
        if (endMultiplicity == null || endMultiplicity.isEmpty()) endMultiplicity = "1";

        // Create the association line
        Line line = new Line();
        bindLineToClassBoundaries(start, end, line);
        line.setStyle("-fx-stroke: black; -fx-stroke-width: 2;"); // Styling the line
        drawingPane.getChildren().add(line);

        // Create a label for the association name
        Text associationLabel = new Text(associationName);
        associationLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
        bindAssociationLabelToLine(associationLabel, line);
        drawingPane.getChildren().add(associationLabel);

        // Create multiplicity labels for the start and end of the line
        Text startMultiplicityLabel = new Text(startMultiplicity);
        Text endMultiplicityLabel = new Text(endMultiplicity);

        startMultiplicityLabel.setStyle("-fx-font-size: 10;");
        endMultiplicityLabel.setStyle("-fx-font-size: 10;");

        // Bind the multiplicity labels to the ends of the line
        bindMultiplicityLabelToLine(startMultiplicityLabel, line, true);
        bindMultiplicityLabelToLine(endMultiplicityLabel, line, false);
        drawingPane.getChildren().addAll(startMultiplicityLabel, endMultiplicityLabel);

        // Allow double-click editing for multiplicity and association name
        setupLabelEditing(startMultiplicityLabel, "Edit Start Multiplicity:");
        setupLabelEditing(endMultiplicityLabel, "Edit End Multiplicity:");
        setupLabelEditing(associationLabel, "Edit Association Name:");
    }

    private void bindLineToClassBoundaries(VBox start, VBox end, Line line) {
        // Bind the start and end points of the line to the boundary edges of the classes
        line.startXProperty().bind(start.layoutXProperty().add(start.widthProperty().divide(1)));
        line.startYProperty().bind(start.layoutYProperty().add(start.heightProperty().divide(2)));
        line.endXProperty().bind(end.layoutXProperty().add(end.widthProperty().divide(14)));
        line.endYProperty().bind(end.layoutYProperty().add(end.heightProperty().divide(2)));
    }

    private void bindAssociationLabelToLine(Text label, Line line) {
        // Bind the association label to the midpoint of the line
        label.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
        label.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(10)); // Slightly above the line
    }

    private void bindMultiplicityLabelToLine(Text label, Line line, boolean isStart) {
        if (isStart) {
            // Bind to the start of the line (slightly outside the boundary of the class)
            label.xProperty().bind(line.startXProperty().subtract(-15)); // Adjust position
            label.yProperty().bind(line.startYProperty().subtract(5));  // Adjust position
        } else {
            // Bind to the end of the line (slightly outside the boundary of the class)
            label.xProperty().bind(line.endXProperty().subtract(15)); // Adjust position
            label.yProperty().bind(line.endYProperty().subtract(5));  // Adjust position
        }
    }

    private void setupLabelEditing(Text label, String dialogTitle) {
        label.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TextInputDialog dialog = new TextInputDialog(label.getText());
                dialog.setTitle(dialogTitle);
                dialog.setHeaderText(dialogTitle);
                dialog.setContentText("New Value:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(label::setText);
            }
        });
    }
}
