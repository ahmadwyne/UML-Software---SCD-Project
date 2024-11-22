package com.example.umlscd;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
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
        drawingPane.setOnMouseClicked(event -> {
            Node target = getParentVBox(event.getPickResult().getIntersectedNode());
            if (target instanceof VBox && elements.contains(target)) {
                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                } else if (secondSelectedElement == null && target != firstSelectedElement) {
                    secondSelectedElement = (VBox) target;

                    TextInputDialog dialog = new TextInputDialog("Association Name");
                    dialog.setTitle("Association Name");
                    dialog.setHeaderText("Enter Association Name:");
                    dialog.setContentText("Name:");

                    Optional<String> result = dialog.showAndWait();
                    result.ifPresent(associationName ->
                            relationsManager.createAssociation(firstSelectedElement, secondSelectedElement, drawingPane, associationName));

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
}
