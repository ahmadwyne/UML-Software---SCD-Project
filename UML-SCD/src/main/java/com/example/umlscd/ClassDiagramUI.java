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
    private Button btnClass, btnInterface, btnAssociation;

    @FXML
    private VBox editorsPane;

    private ClassDiagramManager classDiagramManager; // Business layer instance

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
    }

    private void handleToolSelection(String tool) {
        classDiagramManager.handleToolSelection(tool, drawingPane, editorsPane);
    }


        public void openClassEditor(VBox classBox) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/ClassEditor.fxml"));
                VBox editor = loader.load();

                // Get the controller and set the class box
                ClassEditorUI controller = loader.getController();
                controller.setClassBox(classBox);

                // Clear existing editor content and display the new editor
                editorsPane.getChildren().clear();
                editorsPane.getChildren().add(editor);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Other methods
    }


