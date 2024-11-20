package com.example.umlscd;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class ClassDiagramManager {

    private ArrayList<Node> elements = new ArrayList<>();
    private VBox firstSelectedElement = null;
    private int classCounter = 1;
    private int interfaceCounter = 1;

    private ClassDiagramUI uiController; // Reference to UI controller

    public ClassDiagramManager(ClassDiagramUI uiController) {
        this.uiController = uiController;
    }

    public void handleToolSelection(String tool, Pane drawingPane, VBox editorsPane) {
        if ("Class".equals(tool)) {
            createClassBox("Class" + classCounter++, drawingPane);
        } else if ("Interface".equals(tool)) {
            createInterfaceBox("Interface" + interfaceCounter++, drawingPane);
        } else if ("Association".equals(tool)) {
            enableAssociationMode(drawingPane);
        }
    }

    private void createClassBox(String name, Pane drawingPane) {
        VBox classBox = new VBox();
        classBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        classBox.setSpacing(0);

        // Centered Class Name Label
        Label classNameLabel = new Label(name);
        classNameLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5; -fx-alignment: center;"); // Center text
        classNameLabel.setMaxWidth(Double.MAX_VALUE);
        classNameLabel.setAlignment(Pos.CENTER); // Ensures the text is centered within the label

        VBox attributesBox = new VBox();
        attributesBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        VBox methodsBox = new VBox();
        methodsBox.setStyle("-fx-border-color: black; -fx-padding: 5;");

        classBox.getChildren().addAll(classNameLabel, attributesBox, methodsBox);
        classBox.setLayoutX(100 + classCounter * 50);
        classBox.setLayoutY(100 + classCounter * 50);

        // Add click handler to open the class editor
        classBox.setOnMouseClicked(event -> {
            uiController.openClassEditor(classBox); // Open editor when clicked
            highlightSelectedClass(classBox, drawingPane);
        });

        drawingPane.getChildren().add(classBox);
        elements.add(classBox);
        makeDraggable(classBox);
    }


    private void createInterfaceBox(String name, Pane drawingPane) {
        VBox interfaceBox = new VBox();
        interfaceBox.setStyle("-fx-border-color: black; -fx-background-color: white; -fx-padding: 5;");

        Label interfaceLabel = new Label(name);
        interfaceLabel.setStyle("-fx-font-weight: bold; -fx-padding: 5;");
        interfaceBox.getChildren().add(interfaceLabel);

        interfaceBox.setLayoutX(200 + interfaceCounter * 50);
        interfaceBox.setLayoutY(200 + interfaceCounter * 50);

        drawingPane.getChildren().add(interfaceBox);
        elements.add(interfaceBox);

        interfaceBox.setOnMouseClicked(event -> {
            uiController.openClassEditor(interfaceBox);
            highlightSelectedClass(interfaceBox, drawingPane);
        });
        makeDraggable(interfaceBox);
    }
    private void makeDraggable(VBox pane) {
        pane.setOnMousePressed(event -> {
            pane.setUserData(new double[]{event.getSceneX(), event.getSceneY(), pane.getLayoutX(), pane.getLayoutY()});
        });
        pane.setOnMouseDragged(event -> {
            double[] data = (double[]) pane.getUserData();
            double offsetX = event.getSceneX() - data[0];
            double offsetY = event.getSceneY() - data[1];
            pane.setLayoutX(data[2] + offsetX);
            pane.setLayoutY(data[3] + offsetY);
        });
    }

    private void enableAssociationMode(Pane drawingPane) {
        drawingPane.setOnMouseClicked(event -> {
            Node target = event.getPickResult().getIntersectedNode();
            if (target instanceof VBox && elements.contains(target)) {
                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                } else {
                    VBox secondSelectedElement = (VBox) target;
                    createAssociation(firstSelectedElement, secondSelectedElement, drawingPane);
                    firstSelectedElement = null;
                }
            }
        });
    }

    private void createAssociation(VBox start, VBox end, Pane drawingPane) {
        Line line = new Line();
        line.startXProperty().bind(start.layoutXProperty().add(start.widthProperty().divide(2)));
        line.startYProperty().bind(start.layoutYProperty().add(start.heightProperty().divide(2)));
        line.endXProperty().bind(end.layoutXProperty().add(end.widthProperty().divide(2)));
        line.endYProperty().bind(end.layoutYProperty().add(end.heightProperty().divide(2)));
        drawingPane.getChildren().add(line);
    }

    private void highlightSelectedClass(VBox classBox, Pane drawingPane) {
        for (Node element : drawingPane.getChildren()) {
            if (element instanceof VBox) {
                element.setStyle("-fx-border-color: black; -fx-background-color: white;");
            }
        }
        classBox.setStyle("-fx-border-color: purple; -fx-background-color: white;");
    }
}
