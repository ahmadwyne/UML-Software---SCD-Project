package com.example.umlscd;

import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDiagramRelationsManager {

    private boolean associationModeEnabled = false; // Flag to track association mode
    private final List<int[]> classBoundaryOccupations = new ArrayList<>(); // Track occupied boundary points for each class

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

        // Find the nearest available boundary points for both start and end classes
        int startBoundaryIndex = getAvailableBoundaryPoint(start);
        int endBoundaryIndex = getAvailableBoundaryPoint(end);

        // If no available boundary points, abort creating the association
        if (startBoundaryIndex == -1 || endBoundaryIndex == -1) {
            System.out.println("No available boundary points for association.");
            return;
        }

        // Create the association line
        Line line = new Line();
        bindLineToClassBoundaries(start, end, startBoundaryIndex, endBoundaryIndex, line);
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

        // Update the boundary occupation status for the classes
        updateBoundaryOccupation(start, startBoundaryIndex);
        updateBoundaryOccupation(end, endBoundaryIndex);

        // Allow double-click editing for multiplicity and association name
        setupLabelEditing(startMultiplicityLabel, "Edit Start Multiplicity:");
        setupLabelEditing(endMultiplicityLabel, "Edit End Multiplicity:");
        setupLabelEditing(associationLabel, "Edit Association Name:");

        // Add listeners to update the line dynamically when either class is moved
        addDynamicUpdateListener(line, start, end, associationLabel, startMultiplicityLabel, endMultiplicityLabel);
    }

    private int getAvailableBoundaryPoint(VBox classBox) {
        // Check for available boundary points (0: top, 1: right, 2: bottom, 3: left)
        for (int i = 0; i < 4; i++) {
            if (!isBoundaryOccupied(classBox, i)) {
                return i;
            }
        }
        return -1;  // No available boundary points
    }

    private boolean isBoundaryOccupied(VBox classBox, int boundaryIndex) {
        // Check if this boundary point is already used by another association line
        return classBoundaryOccupations.stream()
                .anyMatch(occupation -> occupation[0] == classBox.hashCode() && occupation[1] == boundaryIndex);
    }

    private void updateBoundaryOccupation(VBox classBox, int boundaryIndex) {
        // Mark this boundary point as occupied by this association line
        classBoundaryOccupations.add(new int[]{classBox.hashCode(), boundaryIndex});
    }

    private void bindLineToClassBoundaries(VBox start, VBox end, int startBoundaryIndex, int endBoundaryIndex, Line line) {
        // Dynamically bind the start and end positions of the line to the class positions
        line.startXProperty().bind(start.layoutXProperty().add(getBoundaryX(start, startBoundaryIndex)));
        line.startYProperty().bind(start.layoutYProperty().add(getBoundaryY(start, startBoundaryIndex)));
        line.endXProperty().bind(end.layoutXProperty().add(getBoundaryX(end, endBoundaryIndex)));
        line.endYProperty().bind(end.layoutYProperty().add(getBoundaryY(end, endBoundaryIndex)));
    }

    private double getBoundaryX(VBox classBox, int boundaryIndex) {
        // Return X position based on boundaryIndex (0 = top, 1 = right, 2 = bottom, 3 = left)
        double x = classBox.getLayoutX();
        double width = classBox.getWidth();
        return switch (boundaryIndex) {
            case 0 -> // Top boundary
                    width / 2;
            case 1 -> // Right boundary
                    width;
            case 2 -> // Bottom boundary
                    width / 2;
            case 3 -> // Left boundary
                    0;
            default -> 0;
        };
    }

    private double getBoundaryY(VBox classBox, int boundaryIndex) {
        // Return Y position based on boundaryIndex (0 = top, 1 = right, 2 = bottom, 3 = left)
        double y = classBox.getLayoutY();
        double height = classBox.getHeight();
        return switch (boundaryIndex) {
            case 0 -> // Top boundary
                    0;
            case 1 -> // Right boundary
                    height / 2;
            case 2 -> // Bottom boundary
                    height;
            case 3 -> // Left boundary
                    height / 2;
            default -> 0;
        };
    }

    private void bindAssociationLabelToLine(Text label, Line line) {
        // Bind the association label to the midpoint of the line
        label.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
        label.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(10)); // Slightly above the line
    }

    private void bindMultiplicityLabelToLine(Text label, Line line, boolean isStart) {
        if (isStart) {
            label.xProperty().bind(line.startXProperty().subtract(-15));
            label.yProperty().bind(line.startYProperty().subtract(5));
        } else {
            label.xProperty().bind(line.endXProperty().subtract(15));
            label.yProperty().bind(line.endYProperty().subtract(5));
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

    private void addDynamicUpdateListener(Line line, VBox start, VBox end, Text associationLabel, Text startMultiplicityLabel, Text endMultiplicityLabel) {
        // Update the line and labels dynamically when either class is moved
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> adjustLineToBorders(line, start, end));
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> adjustLineToBorders(line, start, end));
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> adjustLineToBorders(line, start, end));
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> adjustLineToBorders(line, start, end));
    }

    private void adjustLineToBorders(Line line, VBox start, VBox end) {
        // Adjust the line based on the class boundaries
        int startBoundaryIndex = getAvailableBoundaryPoint(start);
        int endBoundaryIndex = getAvailableBoundaryPoint(end);

        // Re-bind the start and end positions
        bindLineToClassBoundaries(start, end, startBoundaryIndex, endBoundaryIndex, line);
    }
}
