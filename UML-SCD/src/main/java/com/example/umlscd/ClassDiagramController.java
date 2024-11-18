package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import java.io.IOException;
import java.util.ArrayList;

public class ClassDiagramController {

    @FXML
    private Pane drawingPane;

    @FXML
    private ListView<String> diagramListView, modelExplorerListView, propertiesListView;

    @FXML
    private Button btnClass, btnInterface, btnAssociation, btnDirectedAssociation, btnAggregation, btnComposition, btnDependency, btnGeneralization, btnInterfaceRealization;

    @FXML
    private Button homeButton; // Reference to the Home button

    @FXML
    private VBox editorsPane; // Editor pane for selected class editor

    @FXML
    private VBox modelExplorerPane; // Model Explorer pane

    private Stage primaryStage;

    private int classCounter = 1;
    private int interfaceCounter = 1;
    private ArrayList<Node> elements = new ArrayList<>(); // to store elements for connections
    private VBox firstSelectedElement = null;

    @FXML
    private void initialize() {
        setupToolboxHandlers();
        loadWorkingDiagrams();

        // Set action for the Home button to navigate back to the WelcomePage
        homeButton.setOnAction(event -> handleHome());
    }

    // Method to navigate back to WelcomePage
    @FXML
    private void handleHome() {
        try {
            Stage stage = (Stage) homeButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/welcome.fxml"));
            Parent root = loader.load();
            Scene welcomeScene = new Scene(root, 1000, 600);
            stage.setScene(welcomeScene);
            stage.setTitle("Welcome to UML Editor");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadWorkingDiagrams() {
        diagramListView.getItems().addAll("Main Model", "ClassDiagram1", "UseCaseDiagram1");
    }

    private void setupToolboxHandlers() {
        btnClass.setOnAction(e -> handleToolSelection("Class"));
        btnInterface.setOnAction(e -> handleToolSelection("Interface"));
        btnAssociation.setOnAction(e -> handleToolSelection("Association"));
        btnDirectedAssociation.setOnAction(e -> handleToolSelection("Directed Association"));
        btnAggregation.setOnAction(e -> handleToolSelection("Aggregation"));
        btnComposition.setOnAction(e -> handleToolSelection("Composition"));
        btnDependency.setOnAction(e -> handleToolSelection("Dependency"));
        btnGeneralization.setOnAction(e -> handleToolSelection("Generalization"));
        btnInterfaceRealization.setOnAction(e -> handleToolSelection("Interface Realization"));
    }

    private void handleToolSelection(String tool) {
        if ("Class".equals(tool)) {
            createClassBox("Class" + classCounter++);
        } else if ("Interface".equals(tool)) {
            createInterfaceBox("Interface" + interfaceCounter++);
        } else if ("Association".equals(tool)) {
            enableAssociationMode();
        }
    }

    private void createClassBox(String name) {
        VBox classBox = new VBox();
        classBox.setStyle("-fx-border-color: black; -fx-background-color: white;");
        classBox.getStyleClass().add("class-box");

        Label className = new Label(name);
        className.setStyle("-fx-font-weight: bold; -fx-padding: 5;");

        VBox attributes = new VBox();
        attributes.setStyle("-fx-border-color: black; -fx-padding: 5;");
        VBox methods = new VBox();
        methods.setStyle("-fx-border-color: black; -fx-padding: 5;");

        classBox.getChildren().addAll(className, attributes, methods);
        classBox.setLayoutX(100 + classCounter * 50);
        classBox.setLayoutY(100 + classCounter * 50);

        // Add click handler to open the class editor
        classBox.setOnMouseClicked(event -> {
            showClassEditor(classBox);
            highlightSelectedClass(classBox);
        });

        drawingPane.getChildren().add(classBox);
        elements.add(classBox);

        makeDraggable(classBox);
    }

    private void renameObject(VBox classBox, Label className) {
        TextField textField = new TextField(className.getText());
        classBox.getChildren().set(0, textField); // Replace label with textfield
        textField.setOnAction(e -> {
            className.setText(textField.getText());
            classBox.getChildren().set(0, className); // Replace textfield with label
        });
    }

    private void createInterfaceBox(String name) {
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
            showClassEditor(interfaceBox);
            highlightSelectedClass(interfaceBox);
        });
        makeDraggable(interfaceBox);
    }

    private void enableAssociationMode() {
        drawingPane.setOnMouseClicked(event -> {
            Node target = event.getPickResult().getIntersectedNode();
            if (target instanceof VBox && elements.contains(target)) {
                if (firstSelectedElement == null) {
                    firstSelectedElement = (VBox) target;
                } else {
                    VBox secondSelectedElement = (VBox) target;
                    createAssociation(firstSelectedElement, secondSelectedElement);
                    firstSelectedElement = null;
                }
            }
        });
    }

    private void createAssociation(VBox start, VBox end) {
        Line line = new Line();
        line.startXProperty().bind(start.layoutXProperty().add(start.widthProperty().divide(2)));
        line.startYProperty().bind(start.layoutYProperty().add(start.heightProperty().divide(2)));
        line.endXProperty().bind(end.layoutXProperty().add(end.widthProperty().divide(2)));
        line.endYProperty().bind(end.layoutYProperty().add(end.heightProperty().divide(2)));
        drawingPane.getChildren().add(line);
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

    // Method to show the class editor for the selected class box in the Editors section
    private void showClassEditor(VBox selectedClassBox) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/ClassEditor.fxml"));
            VBox editor = loader.load();

            // Get the controller and pass the selected class box to it
            ClassEditorController controller = loader.getController();
            controller.setClassBox(selectedClassBox);

            // Only update the editor section (no change to the Model Explorer)
            editorsPane.getChildren().clear();
            editorsPane.getChildren().add(editor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to highlight the selected class with a purple border
    private void highlightSelectedClass(VBox classBox) {
        for (Node element : drawingPane.getChildren()) {
            if (element instanceof VBox) {
                element.setStyle("-fx-border-color: black; -fx-background-color: white;");
            }
        }
        classBox.setStyle("-fx-border-color: purple; -fx-background-color: white;");
    }
}
