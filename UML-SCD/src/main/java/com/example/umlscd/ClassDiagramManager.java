package com.example.umlscd;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassDiagramManager {

    private final ArrayList<Node> elements = new ArrayList<>();
    private VBox firstSelectedElement = null;
    private VBox secondSelectedElement = null;

    private final ClassDiagramUI uiController;
    private final ClassDiagramRelationsManager relationsManager;
    private boolean isDragEnabled = false;

    public ClassDiagramManager(ClassDiagramUI uiController) {
        this.uiController = uiController;
        this.relationsManager = new ClassDiagramRelationsManager();
    }

    public void handleToolSelection(String tool, Pane drawingPane, VBox editorsPane) {
        if ("Class".equals(tool)) {
            disableDrag();
            createClassBox("Class" + (elements.size() + 1), drawingPane);
        } else if ("Association".equals(tool)) {
            disableDrag();
            enableAssociationMode(drawingPane);
        } else if ("Drag".equals(tool)) {
            enableDragMode();
        }
    }

    private void createClassBox(String name, Pane drawingPane) {
        VBox classBox = new VBox();
        classBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        classBox.setSpacing(0);

        Label classNameLabel = new Label(name);
        classNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
        classNameLabel.setAlignment(Pos.CENTER);

        // Ensure the label takes the full width of the VBox
        classNameLabel.setMaxWidth(Double.MAX_VALUE);
        classNameLabel.setStyle("-fx-alignment: center; -fx-font-weight: bold; -fx-padding: 5;");

        VBox attributesBox = new VBox();
        attributesBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        classBox.getChildren().addAll(classNameLabel, attributesBox, methodsBox);
        classBox.setLayoutX(100 + elements.size() * 50);
        classBox.setLayoutY(100);

        classBox.setOnMouseClicked(event -> uiController.openClassEditor(classBox));
        drawingPane.getChildren().add(classBox);
        elements.add(classBox);
        setDraggable(classBox, false);
    }

    private void enableAssociationMode(Pane drawingPane) {
        relationsManager.enableAssociationMode(); // Enable association mode

        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());
            if (target instanceof VBox && elements.contains(target)) {
                if (!relationsManager.isAssociationModeEnabled()) {
                    return; // Skip if association mode is not enabled
                }

                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                    highlightClass(firstSelectedElement, true);
                } else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;
                    highlightClass(secondSelectedElement, true);

                    // Show dialog for association details
                    Dialog<List<String>> dialog = new Dialog<>();
                    dialog.setTitle("Set Association Details");
                    dialog.setHeaderText("Enter Association Name and Multiplicities");

                    TextField associationNameField = new TextField();
                    associationNameField.setPromptText("Association Name");
                    TextField startMultiplicityField = new TextField();
                    startMultiplicityField.setPromptText("Start Multiplicity");
                    TextField endMultiplicityField = new TextField();
                    endMultiplicityField.setPromptText("End Multiplicity");

                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.add(new Label("Association Name:"), 0, 0);
                    grid.add(associationNameField, 1, 0);
                    grid.add(new Label("Start Multiplicity:"), 0, 1);
                    grid.add(startMultiplicityField, 1, 1);
                    grid.add(new Label("End Multiplicity:"), 0, 2);
                    grid.add(endMultiplicityField, 1, 2);

                    dialog.getDialogPane().setContent(grid);
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == ButtonType.OK) {
                            List<String> result = new ArrayList<>();
                            result.add(associationNameField.getText());
                            result.add(startMultiplicityField.getText());
                            result.add(endMultiplicityField.getText());
                            return result;
                        }
                        return null;
                    });

                    Optional<List<String>> result = dialog.showAndWait();
                    result.ifPresent(values -> {
                        String associationName = values.get(0).isEmpty() ? "associationName" : values.get(0);
                        String startMultiplicity = values.get(1).isEmpty() ? "1" : values.get(1);
                        String endMultiplicity = values.get(2).isEmpty() ? "1" : values.get(2);

                        relationsManager.createAssociation(
                                firstSelectedElement,
                                secondSelectedElement,
                                drawingPane,
                                associationName,
                                startMultiplicity,
                                endMultiplicity
                        );
                    });

                    // Reset the selected elements
                    highlightClass(firstSelectedElement, false);
                    highlightClass(secondSelectedElement, false);
                    firstSelectedElement = null;
                    secondSelectedElement = null;
                }
            }
        });
    }

    private Node getParentVBox(Node node) {
        while (node != null && !(node instanceof VBox)) {
            node = node.getParent();
        }
        return node;
    }

    private void enableDragMode() {
        isDragEnabled = true;
        for (Node element : elements) {
            if (element instanceof VBox) {
                setDraggable((VBox) element, true);
            }
        }
    }

    private void disableDrag() {
        isDragEnabled = false;
        for (Node element : elements) {
            if (element instanceof VBox) {
                setDraggable((VBox) element, false);
            }
        }
    }

    private void setDraggable(VBox pane, boolean enable) {
        if (enable) {
            pane.setOnMousePressed(event -> pane.setUserData(new double[]{event.getSceneX(), event.getSceneY(), pane.getLayoutX(), pane.getLayoutY()}));
            pane.setOnMouseDragged(event -> {
                double[] data = (double[]) pane.getUserData();
                double offsetX = event.getSceneX() - data[0];
                double offsetY = event.getSceneY() - data[1];
                pane.setLayoutX(data[2] + offsetX);
                pane.setLayoutY(data[3] + offsetY);
            });
        } else {
            pane.setOnMousePressed(null);
            pane.setOnMouseDragged(null);
        }
    }

    private void highlightClass(VBox classBox, boolean highlight) {
        if (highlight) {
            classBox.setStyle("-fx-border-color: darkred; -fx-border-width: 2; -fx-border-style: solid;");
        } else {
            classBox.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-border-style: solid;");
        }
    }
}
