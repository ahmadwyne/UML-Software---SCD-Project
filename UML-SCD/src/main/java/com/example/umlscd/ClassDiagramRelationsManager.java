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
    private static class AssociationBinding {
        final Line line;
        final VBox startClass;
        final VBox endClass;
        final int startBoundaryIndex;
        final int endBoundaryIndex;

        AssociationBinding(Line line, VBox startClass, VBox endClass, int startBoundaryIndex, int endBoundaryIndex) {
            this.line = line;
            this.startClass = startClass;
            this.endClass = endClass;
            this.startBoundaryIndex = startBoundaryIndex;
            this.endBoundaryIndex = endBoundaryIndex;
        }
    }

    private boolean associationModeEnabled = false; // Flag to track association mode
    private final List<AssociationBinding> associationBindings = new ArrayList<>();

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
        disableAssociationMode(); // Exit association mode immediately

        // Set default values if fields are empty
        if (associationName == null || associationName.isEmpty()) associationName = "associationName";
        if (startMultiplicity == null || startMultiplicity.isEmpty()) startMultiplicity = "1";
        if (endMultiplicity == null || endMultiplicity.isEmpty()) endMultiplicity = "1";

        // Find the nearest available boundary points
        int startBoundaryIndex = getAvailableBoundaryPoint(start);
        int endBoundaryIndex = getAvailableBoundaryPoint(end);

        if (startBoundaryIndex == -1 || endBoundaryIndex == -1) {
            System.out.println("No available boundary points for association.");
            return; // Abort if no boundaries are available
        }

        // Create and bind the line
        Line line = new Line();
        bindLineToClassBoundaries(start, end, startBoundaryIndex, endBoundaryIndex, line);
        line.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
        drawingPane.getChildren().add(line);

        // Save the association's binding
        AssociationBinding binding = new AssociationBinding(line, start, end, startBoundaryIndex, endBoundaryIndex);
        associationBindings.add(binding);

        // Create association label
        Text associationLabel = new Text(associationName);
        associationLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
        bindAssociationLabelToLine(associationLabel, line);
        drawingPane.getChildren().add(associationLabel);

        // Create and bind multiplicity labels
        Text startMultiplicityLabel = new Text(startMultiplicity);
        Text endMultiplicityLabel = new Text(endMultiplicity);

        startMultiplicityLabel.setStyle("-fx-font-size: 10;");
        endMultiplicityLabel.setStyle("-fx-font-size: 10;");

        bindMultiplicityLabelToLine(startMultiplicityLabel, line, true);
        bindMultiplicityLabelToLine(endMultiplicityLabel, line, false);

        drawingPane.getChildren().addAll(startMultiplicityLabel, endMultiplicityLabel);

        // Setup editing and dynamic updates
        setupLabelEditing(startMultiplicityLabel, "Edit Start Multiplicity:");
        setupLabelEditing(endMultiplicityLabel, "Edit End Multiplicity:");
        setupLabelEditing(associationLabel, "Edit Association Name:");

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
        return associationBindings.stream()
                .anyMatch(binding ->
                        (binding.startClass == classBox && binding.startBoundaryIndex == boundaryIndex) ||
                                (binding.endClass == classBox && binding.endBoundaryIndex == boundaryIndex)
                );
    }

    private void bindLineToClassBoundaries(VBox start, VBox end, int startBoundaryIndex, int endBoundaryIndex, Line line) {
        line.setStartX(start.getLayoutX() + start.getWidth() * getBoundaryXMultiplier(startBoundaryIndex));
        line.setStartY(start.getLayoutY() + start.getHeight() * getBoundaryYMultiplier(startBoundaryIndex));
        line.setEndX(end.getLayoutX() + end.getWidth() * getBoundaryXMultiplier(endBoundaryIndex));
        line.setEndY(end.getLayoutY() + end.getHeight() * getBoundaryYMultiplier(endBoundaryIndex));
    }

    private double getBoundaryXMultiplier(int boundaryIndex) {
        return switch (boundaryIndex) {
            case 0, 2 -> 0.5; // Top or bottom (center horizontally)
            case 1 -> 1; // Right
            case 3 -> 0; // Left
            default -> 0;
        };
    }

    private double getBoundaryYMultiplier(int boundaryIndex) {
        return switch (boundaryIndex) {
            case 0 -> 0; // Top
            case 1, 3 -> 0.5; // Right or left (center vertically)
            case 2 -> 1; // Bottom
            default -> 0;
        };
    }

    private void bindAssociationLabelToLine(Text label, Line line) {
        label.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
        label.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(10));
    }

    private void bindMultiplicityLabelToLine(Text label, Line line, boolean isStart) {
        if (isStart) {
            label.xProperty().bind(line.startXProperty().add(5));
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
        start.layoutXProperty().addListener((obs, oldVal, newVal) -> {
            adjustLineToBorders(line);
            updateLabelsPosition(line, associationLabel, startMultiplicityLabel, endMultiplicityLabel);
        });
        start.layoutYProperty().addListener((obs, oldVal, newVal) -> {
            adjustLineToBorders(line);
            updateLabelsPosition(line, associationLabel, startMultiplicityLabel, endMultiplicityLabel);
        });
        end.layoutXProperty().addListener((obs, oldVal, newVal) -> {
            adjustLineToBorders(line);
            updateLabelsPosition(line, associationLabel, startMultiplicityLabel, endMultiplicityLabel);
        });
        end.layoutYProperty().addListener((obs, oldVal, newVal) -> {
            adjustLineToBorders(line);
            updateLabelsPosition(line, associationLabel, startMultiplicityLabel, endMultiplicityLabel);
        });
    }

    private void adjustLineToBorders(Line line) {
        AssociationBinding binding = associationBindings.stream()
                .filter(b -> b.line == line)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Binding not found for line"));

        bindLineToClassBoundaries(binding.startClass, binding.endClass, binding.startBoundaryIndex, binding.endBoundaryIndex, line);
    }

    private void updateLabelsPosition(Line line, Text associationLabel, Text startMultiplicityLabel, Text endMultiplicityLabel) {
        associationLabel.setX((line.getStartX() + line.getEndX()) / 2);
        associationLabel.setY((line.getStartY() + line.getEndY()) / 2 - 10);

        startMultiplicityLabel.setX(line.getStartX() + 5);
        startMultiplicityLabel.setY(line.getStartY() - 5);

        endMultiplicityLabel.setX(line.getEndX() - 15);
        endMultiplicityLabel.setY(line.getEndY() - 5);
    }
}
