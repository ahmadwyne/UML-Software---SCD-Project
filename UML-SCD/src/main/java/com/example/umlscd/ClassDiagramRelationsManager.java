package com.example.umlscd;

import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.Optional;

public class ClassDiagramRelationsManager {

    public void createAssociation(VBox start, VBox end, Pane drawingPane, String associationName) {
        // Create the association line
        Line line = new Line();
        adjustLineBoundary(start, end, line);
        drawingPane.getChildren().add(line);

        // Create a label for the association name
        Text associationLabel = new Text(associationName);
        associationLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12;");

        // Create multiplicity labels for the start and end of the line
        Text startMultiplicityLabel = new Text("1"); // Default value
        Text endMultiplicityLabel = new Text("*");  // Default value

        startMultiplicityLabel.setStyle("-fx-font-size: 10;");
        endMultiplicityLabel.setStyle("-fx-font-size: 10;");

        // Bind the association label to the midpoint of the line
        associationLabel.xProperty().bind(line.startXProperty().add(line.endXProperty()).divide(2));
        associationLabel.yProperty().bind(line.startYProperty().add(line.endYProperty()).divide(2).subtract(10));

        // Bind multiplicity labels to the boundaries
        startMultiplicityLabel.xProperty().bind(line.startXProperty().subtract(20)); // Adjust near start
        startMultiplicityLabel.yProperty().bind(line.startYProperty().add(15)); // Slightly below the start

        endMultiplicityLabel.xProperty().bind(line.endXProperty().add(5)); // Adjust near end
        endMultiplicityLabel.yProperty().bind(line.endYProperty().add(15)); // Slightly below the end

        // Ask user for multiplicities when association is created
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Set Multiplicities");
        dialog.setHeaderText("Set Multiplicities for the Association");
        dialog.setContentText(
                "Enter multiplicities separated by a comma (e.g., 1, *):\n" +
                        "Multiplicity of " + getClassName(start) + " and " + getClassName(end));

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(input -> {
            String[] multiplicities = input.split(",");
            if (multiplicities.length == 2) {
                startMultiplicityLabel.setText(multiplicities[0].trim());
                endMultiplicityLabel.setText(multiplicities[1].trim());
            }
        });

        // Add labels and line to the drawing pane
        drawingPane.getChildren().addAll(associationLabel, startMultiplicityLabel, endMultiplicityLabel);

        // Allow double-click editing of the association name
        associationLabel.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TextInputDialog nameDialog = new TextInputDialog(associationLabel.getText());
                nameDialog.setTitle("Edit Association Name");
                nameDialog.setHeaderText("Edit Association Name:");
                nameDialog.setContentText("Name:");
                Optional<String> nameResult = nameDialog.showAndWait();
                nameResult.ifPresent(associationLabel::setText);
            }
        });
    }


    private String getClassName(VBox box) {
        if (!box.getChildren().isEmpty() && box.getChildren().get(0) instanceof Text) {
            return ((Text) box.getChildren().get(0)).getText();
        }
        return "Class";
    }

private void adjustLineBoundary(VBox start, VBox end, Line line) {
        // Dynamically calculate and bind the start and end points of the line
        line.startXProperty().bind(start.layoutXProperty().add(start.widthProperty().divide(1)));
        line.startYProperty().bind(start.layoutYProperty().add(start.heightProperty().divide(2)));
        line.endXProperty().bind(end.layoutXProperty().add(end.widthProperty().divide(12)));
        line.endYProperty().bind(end.layoutYProperty().add(end.heightProperty().divide(2)));
    }

    private void updateLineBoundary(VBox start, VBox end, Line line) {
        double[] startEdge = calculateBoundaryIntersection(start, line.getEndX(), line.getEndY());
        double[] endEdge = calculateBoundaryIntersection(end, line.getStartX(), line.getStartY());

        line.setStartX(startEdge[0]);
        line.setStartY(startEdge[1]);
        line.setEndX(endEdge[0]);
        line.setEndY(endEdge[1]);
    }
    private double[] calculateBoundaryIntersection(VBox box, double x, double y) {
        double centerX = box.getLayoutX() + box.getWidth() / 2;
        double centerY = box.getLayoutY() + box.getHeight() / 2;

        double dx = x - centerX;
        double dy = y - centerY;

        // Determine the intersection with the rectangle boundary
        double absDx = Math.abs(dx);
        double absDy = Math.abs(dy);

        double scale = absDx > absDy ? (box.getWidth() / 2) / absDx : (box.getHeight() / 2) / absDy;

        double edgeX = centerX + dx * scale;
        double edgeY = centerY + dy * scale;

        return new double[]{edgeX, edgeY};
    }

}
