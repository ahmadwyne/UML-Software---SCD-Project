package com.example.umlscd;

import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDiagramRelationsManager {

    private final List<int[]> classBoundaryOccupations = new ArrayList<>(); // Track occupied boundary points for each class

        private boolean enabledAssociationModel = false;
        private boolean enabledAggregationModel = false;
        private boolean enabledCompositionModel = false;
        private boolean inheritanceModeEnabled = false;

        // Enable/Disable association mode
        public void enableAssociationMode() {
            this.enabledAssociationModel = true;
        }

        public void disableAssociationMode() {
            this.enabledAssociationModel = false;
        }

        public boolean isAssociationModeEnabled() {
            return enabledAssociationModel;
        }

        // Enable/Disable aggregation mode
        public void enableAggregationMode() {
            this.enabledAggregationModel = true;
        }

        public void disableAggregationMode() {
            this.enabledAggregationModel = false;
        }

        public boolean isAggregationModeEnabled() {
            return enabledAggregationModel;
        }

        // Enable/Disable composition mode
        public void enableCompositionMode() {
            this.enabledCompositionModel = true;
        }

        public void disableCompositionMode() {
            this.enabledCompositionModel = false;
        }

        public boolean isCompositionModeEnabled() {
            return enabledCompositionModel;
        }

    // Enable inheritance mode
    public void enableInheritanceMode() {
        inheritanceModeEnabled = true;
    }

    // Disable inheritance mode
    public void disableInheritanceMode() {
        inheritanceModeEnabled = false;
    }

    public boolean isInheritanceModeEnabled() {
        return inheritanceModeEnabled;
    }


        public void createAggregation(VBox start, VBox end, Pane drawingPane, String aggregationName, String startMultiplicity, String endMultiplicity) {
        // Disable aggregation mode immediately after starting the process
        disableAggregationMode();

        // Set default values if fields are empty
        if (aggregationName == null || aggregationName.isEmpty()) aggregationName = "aggregationName";
        if (startMultiplicity == null || startMultiplicity.isEmpty()) startMultiplicity = "1";
        if (endMultiplicity == null || endMultiplicity.isEmpty()) endMultiplicity = "1";

            // Find the nearest available boundary points for both start and end classes
            int startBoundaryIndex = getAvailableBoundaryPoint(start);
            int endBoundaryIndex = getAvailableBoundaryPoint(end);

            // If no available boundary points, abort creating the aggregation
            if (startBoundaryIndex == -1 || endBoundaryIndex == -1) {
                System.out.println("No available boundary points for aggregation.");
                return;
            }

            // Create the aggregation line (hollow diamond)
            Line line = new Line();
            bindLineToClassBoundaries(start, end, startBoundaryIndex, endBoundaryIndex, line);
            line.setStyle("-fx-stroke: black; -fx-stroke-width: 2;"); // Styling the line
            drawingPane.getChildren().add(line);

            // Create the hollow diamond (aggregation)
            Polygon aggregationDiamond = new Polygon();
            aggregationDiamond.getPoints().addAll(
                    0.0, 10.0,  // top point
                    10.0, 0.0,  // right point
                    20.0, 10.0,  // bottom point
                    10.0, 20.0   // left point
            );
            aggregationDiamond.setFill(Color.WHITE);  // Hollow (transparent)
            aggregationDiamond.setStroke(Color.BLACK);     // Black border

            // Bind the aggregation diamond to the line and align it to the class boundary
            bindAggregationDiamondToLine(aggregationDiamond, line, end);
            drawingPane.getChildren().add(aggregationDiamond);

            // Create labels for multiplicities and association name
            Text aggregationLabel = new Text(aggregationName);
            aggregationLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
            bindAggregationLabelToLine(aggregationLabel, line); // Bind label to line position
            drawingPane.getChildren().add(aggregationLabel);

            // Place multiplicities before the diamond
            Text startMultiplicityLabel = new Text(startMultiplicity);
            Text endMultiplicityLabel = new Text(endMultiplicity);

            startMultiplicityLabel.setStyle("-fx-font-size: 10;");
            endMultiplicityLabel.setStyle("-fx-font-size: 10;");

            // Place multiplicity labels before the diamond (before the line's start and end)
            bindMultiplicityLabelToLine(startMultiplicityLabel, line, true);
            bindMultiplicityLabelToLine(endMultiplicityLabel, line, false);
            drawingPane.getChildren().addAll(startMultiplicityLabel, endMultiplicityLabel);

            // Update the boundary occupation status for the classes
            updateBoundaryOccupation(start, startBoundaryIndex);
            updateBoundaryOccupation(end, endBoundaryIndex);

            // Allow double-click editing for multiplicity and aggregation name
            setupLabelEditing(startMultiplicityLabel, "Edit Start Multiplicity:");
            setupLabelEditing(endMultiplicityLabel, "Edit End Multiplicity:");
            setupLabelEditing(aggregationLabel, "Edit Aggregation Name:");

            // Add listeners to update the line dynamically when either class is moved
            addDynamicUpdateListener(line, start, end, aggregationLabel, startMultiplicityLabel, endMultiplicityLabel);
        }
    // Create the inheritance relationship (triangle shape)
    public void createInheritance(VBox start, VBox end, Pane drawingPane) {
        // Disable inheritance mode immediately after starting the process
        disableInheritanceMode();

        // Create the inheritance line
        Line line = new Line();
        bindLineToClassBoundaries(start, end, 0, 1, line);  // You can modify this binding for custom positioning

        line.setStyle("-fx-stroke: black; -fx-stroke-width: 2;"); // Styling the line
        drawingPane.getChildren().add(line);

        // Create the triangle (inheritance) shape
        Polygon inheritanceTriangle = new Polygon();
        inheritanceTriangle.getPoints().addAll(
                0.0, 0.0,   // top point
                10.0, 20.0, // bottom right point
                -10.0, 20.0 // bottom left point
        );
        inheritanceTriangle.setFill(Color.WHITE);  // Filled triangle
        inheritanceTriangle.setStroke(Color.BLACK);  // Black border
        bindInheritanceTriangleToLine(inheritanceTriangle, line,end);  // Bind triangle to the line
        drawingPane.getChildren().add(inheritanceTriangle);
    }

    // Bind the inheritance triangle to the second selected class boundary
    private void bindInheritanceTriangleToLine(Polygon inheritanceTriangle, Line line,VBox end) {
        double endX = line.getEndX();
        double endY = line.getEndY();

        double boundaryX = endX;
        double boundaryY = endY;

        // Move the diamond such that its edge touches the boundary point of the end class
        inheritanceTriangle.setTranslateX(boundaryX - inheritanceTriangle.getBoundsInLocal().getWidth() / 2);  // Align horizontally
        inheritanceTriangle.setTranslateY(boundaryY - inheritanceTriangle.getBoundsInLocal().getHeight() / 2); // Align vertically

        // Update dynamic position if the line is moved
        line.endXProperty().addListener((observable, oldValue, newValue) -> {
            updateAggregationDiamondPosition(inheritanceTriangle, line, end);
        });

        line.endYProperty().addListener((observable, oldValue, newValue) -> {
            updateAggregationDiamondPosition(inheritanceTriangle, line, end);
        });
    }

    private void updateInheritanceTrianglePosition(Polygon inheritanceTriangle, Line line) {
        // Update triangle's position to the end of the line (second selected class boundary)
        double lineEndX = line.getEndX();
        double lineEndY = line.getEndY();

        // Reposition triangle based on the line's endpoint
        inheritanceTriangle.setTranslateX(lineEndX - 10); // Adjust for boundary X
        inheritanceTriangle.setTranslateY(lineEndY - 10); // Adjust for boundary Y
    }

        public void createComposition(VBox start, VBox end, Pane drawingPane, String compositionName, String startMultiplicity, String endMultiplicity) {
        // Disable composition mode immediately after starting the process
        disableCompositionMode();
        // Set default values if fields are empty

        // Set default values if fields are empty
        if (compositionName == null || compositionName.isEmpty()) compositionName = "compositionName";
        if (startMultiplicity == null || startMultiplicity.isEmpty()) startMultiplicity = "1";
        if (endMultiplicity == null || endMultiplicity.isEmpty()) endMultiplicity = "1";


        // Find the nearest available boundary points for both start and end classes
        int startBoundaryIndex = getAvailableBoundaryPoint(start);
        int endBoundaryIndex = getAvailableBoundaryPoint(end);

        // If no available boundary points, abort creating the composition
        if (startBoundaryIndex == -1 || endBoundaryIndex == -1) {
            System.out.println("No available boundary points for composition.");
            return;
        }

        // Create the composition line (filled diamond)
        Line line = new Line();
        bindLineToClassBoundaries(start, end, startBoundaryIndex, endBoundaryIndex, line);
        line.setStyle("-fx-stroke: black; -fx-stroke-width: 2;");
        drawingPane.getChildren().add(line);

        // Create a hollow diamond (aggregation) shape
        Polygon aggregationDiamond = new Polygon();
        aggregationDiamond.getPoints().addAll(
                0.0, 10.0,   // top point
                10.0, 0.0,   // right point
                20.0, 10.0,   // bottom point
                10.0, 20.0    // left point
        );
        aggregationDiamond.setFill(Color.BLACK);
        aggregationDiamond.setStroke(Color.BLACK);     // Black border
        bindAggregationDiamondToLine(aggregationDiamond, line,end);
        drawingPane.getChildren().add(aggregationDiamond);
        // Create a label for the composition (filled diamond)
        Text compositionLabel = new Text("Composition");
        compositionLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");
        bindCompositionLabelToLine(compositionLabel, line); // Bind label to line position
        drawingPane.getChildren().add(compositionLabel);

        // Create multiplicity labels for the start and end of the line
        Text startMultiplicityLabel = new Text("1");
        Text endMultiplicityLabel = new Text("1");

        startMultiplicityLabel.setStyle("-fx-font-size: 10;");
        endMultiplicityLabel.setStyle("-fx-font-size: 10;");

        // Bind the multiplicity labels to the ends of the line
        bindMultiplicityLabelToLine(startMultiplicityLabel, line, true);
        bindMultiplicityLabelToLine(endMultiplicityLabel, line, false);
        drawingPane.getChildren().addAll(startMultiplicityLabel, endMultiplicityLabel);

        // Update the boundary occupation status for the classes
        updateBoundaryOccupation(start, startBoundaryIndex);
        updateBoundaryOccupation(end, endBoundaryIndex);

        // Allow double-click editing for multiplicity and composition name
        setupLabelEditing(startMultiplicityLabel, "Edit Start Multiplicity:");
        setupLabelEditing(endMultiplicityLabel, "Edit End Multiplicity:");
        setupLabelEditing(compositionLabel, "Edit Composition Name:");

        // Add listeners to update the line dynamically when either class is moved
        addDynamicUpdateListener(line, start, end, compositionLabel, startMultiplicityLabel, endMultiplicityLabel);
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
    public void bindAggregationDiamondToLine(Polygon aggregationDiamond, Line line, VBox end) {
        // Get the boundary point of the end class (second selected class)
        double endX = line.getEndX();
        double endY = line.getEndY();

        // Adjust the position of the aggregation diamond so its edge touches the boundary point of the class
        double boundaryX = endX;
        double boundaryY = endY;

        // Move the diamond such that its edge touches the boundary point of the end class
        aggregationDiamond.setTranslateX(boundaryX - aggregationDiamond.getBoundsInLocal().getWidth() / 2);  // Align horizontally
        aggregationDiamond.setTranslateY(boundaryY - aggregationDiamond.getBoundsInLocal().getHeight() / 2); // Align vertically

        // Update dynamic position if the line is moved
        line.endXProperty().addListener((observable, oldValue, newValue) -> {
            updateAggregationDiamondPosition(aggregationDiamond, line, end);
        });

        line.endYProperty().addListener((observable, oldValue, newValue) -> {
            updateAggregationDiamondPosition(aggregationDiamond, line, end);
        });
    }

    private void updateAggregationDiamondPosition(Polygon aggregationDiamond, Line line, VBox end) {
        // Get the updated position of the end point of the line
        double endX = line.getEndX();
        double endY = line.getEndY();

        // Update the diamond’s position to match the updated line’s end position
        aggregationDiamond.setTranslateX(endX - aggregationDiamond.getBoundsInLocal().getWidth() / 2); // Adjust for correct alignment
        aggregationDiamond.setTranslateY(endY - aggregationDiamond.getBoundsInLocal().getHeight() / 2); // Adjust vertically
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
    public void bindAggregationLabelToLine(Text aggregationLabel, Line line) {
        // Bind label to the midpoint of the line
        aggregationLabel.setTranslateX((line.getStartX() + line.getEndX()) / 2);
        aggregationLabel.setTranslateY((line.getStartY() + line.getEndY()) / 2 - 20);  // Position above the line (20px offset)

        // Bind the label's position to the line's midpoint dynamically
        line.startXProperty().addListener((observable, oldValue, newValue) -> {
            updateAggregationLabelPosition(aggregationLabel, line);
        });
        line.startYProperty().addListener((observable, oldValue, newValue) -> {
            updateAggregationLabelPosition(aggregationLabel, line);
        });
        line.endXProperty().addListener((observable, oldValue, newValue) -> {
            updateAggregationLabelPosition(aggregationLabel, line);
        });
        line.endYProperty().addListener((observable, oldValue, newValue) -> {
            updateAggregationLabelPosition(aggregationLabel, line);
        });
    }

    private void updateAggregationLabelPosition(Text aggregationLabel, Line line) {
        // Update the label's position to the midpoint of the line
        aggregationLabel.setTranslateX((line.getStartX() + line.getEndX()) / 2);
        aggregationLabel.setTranslateY((line.getStartY() + line.getEndY()) / 2 - 20);  // Keep it above the line (20px offset)
    }

    public void bindCompositionLabelToLine(Text compositionLabel, Line line) {
        // Bind label to the midpoint of the line
        compositionLabel.setTranslateX((line.getStartX() + line.getEndX()) / 2);
        compositionLabel.setTranslateY((line.getStartY() + line.getEndY()) / 2 - 20);  // Position above the line (20px offset)

        // Bind the label's position to the line's midpoint dynamically
        line.startXProperty().addListener((observable, oldValue, newValue) -> {
            updateCompositionLabelPosition(compositionLabel, line);
        });
        line.startYProperty().addListener((observable, oldValue, newValue) -> {
            updateCompositionLabelPosition(compositionLabel, line);
        });
        line.endXProperty().addListener((observable, oldValue, newValue) -> {
            updateCompositionLabelPosition(compositionLabel, line);
        });
        line.endYProperty().addListener((observable, oldValue, newValue) -> {
            updateCompositionLabelPosition(compositionLabel, line);
        });
    }

    private void updateCompositionLabelPosition(Text compositionLabel, Line line) {
        // Update the label's position to the midpoint of the line
        compositionLabel.setTranslateX((line.getStartX() + line.getEndX()) / 2);
        compositionLabel.setTranslateY((line.getStartY() + line.getEndY()) / 2 - 20);  // Keep it above the line (20px offset)
    }



    private void updateLinePosition(Line line, VBox start, VBox end, Text label, Text startMultiplicity, Text endMultiplicity) {
        // Update the position of the line and labels when either class is moved
        int startBoundaryIndex = getAvailableBoundaryPoint(start);
        int endBoundaryIndex = getAvailableBoundaryPoint(end);
        bindLineToClassBoundaries(start, end, startBoundaryIndex, endBoundaryIndex, line);
        bindAggregationLabelToLine(label, line);
        bindMultiplicityLabelToLine(startMultiplicity, line, true);
        bindMultiplicityLabelToLine(endMultiplicity, line, false);
    }

}
