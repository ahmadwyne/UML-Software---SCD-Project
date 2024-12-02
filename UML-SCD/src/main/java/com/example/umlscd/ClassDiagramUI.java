package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class ClassDiagramUI {

    @FXML
    private Pane drawingPane;

    @FXML
    private ListView<String> diagramListView, modelExplorerListView;

    @FXML
    private Button btnClass, btnInterface, btnAssociation, btnDrag;
    @FXML private Button btnAggregation;
    @FXML private Button btnComposition;
    @FXML private Button btnInheritance;

    @FXML
    private VBox editorsPane;

    private ClassDiagramManager classDiagramManager;

    public ClassDiagramUI() {
        this.classDiagramManager = new ClassDiagramManager(this);
    }

    @FXML
    private void initialize() {
        setupToolboxHandlers();
        loadWorkingDiagrams();
    }

    @FXML
    private void applyHoverEffect(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #C0C0C0; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    @FXML
    private void removeHoverEffect(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #AFAFAF; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    @FXML
    private void applyClickEffect(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #8C8C8C; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }
    private void loadWorkingDiagrams() {
        diagramListView.getItems().addAll("Main Model", "ClassDiagram1", "UseCaseDiagram1");
    }

    private void setupToolboxHandlers() {
        btnClass.setOnAction(e -> handleToolSelection("Class"));
        btnInterface.setOnAction(e -> handleToolSelection("Interface"));
        btnAssociation.setOnAction(e -> handleToolSelection("Association"));
        btnDrag.setOnAction(e -> handleToolSelection("Drag"));
        // Add event handlers for Aggregation and Composition
        btnAggregation.setOnAction(event -> {
            classDiagramManager.handleToolSelection("Aggregation", drawingPane, editorsPane);
        });

        btnComposition.setOnAction(event -> {
            classDiagramManager.handleToolSelection("Composition", drawingPane, editorsPane);
        });

        btnInheritance.setOnAction(event -> {
            classDiagramManager.handleToolSelection("Inheritance", drawingPane, editorsPane);
        });
    }

    private void handleToolSelection(String tool) {
        classDiagramManager.handleToolSelection(tool, drawingPane, editorsPane);
    }

    public void openClassEditor(VBox classBox) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/ClassEditor.fxml"));
            VBox editor = loader.load();

            ClassEditorUI controller = loader.getController();
            controller.setClassBox(classBox);

            editorsPane.getChildren().clear();
            editorsPane.getChildren().add(editor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void openInterfaceEditor(VBox interfaceBox) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/InterfaceEditor.fxml"));
            VBox editor = loader.load();

            InterfaceEditorUI controller = loader.getController();
            controller.setInterface(interfaceBox);

            editorsPane.getChildren().clear();
            editorsPane.getChildren().add(editor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyHoverEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #C0C0C0; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    public void removeHoverEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #AFAFAF; -fx-font-size: 12px;  -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    public void applyClickEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #8C8C8C; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }
}
