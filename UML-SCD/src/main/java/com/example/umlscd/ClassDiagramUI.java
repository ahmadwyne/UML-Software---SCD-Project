package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

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
}
