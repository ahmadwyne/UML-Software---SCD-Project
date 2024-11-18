package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClassDiagram {

    @FXML
    private Pane drawingPane;

    @FXML
    private ListView<String> diagramListView, modelExplorerListView, propertiesListView;

    @FXML
    private Button btnClass, btnInterface, btnAssociation, btnDirectedAssociation, btnAggregation, btnComposition, btnDependency, btnGeneralization, btnInterfaceRealization;

    private Stage primaryStage;

    @FXML
    private void initialize() {
        setupToolboxHandlers();
        loadWorkingDiagrams();
    }

    // Sample method for Home button
    @FXML
    private void handleHome() {
        System.out.println("Navigating to Home");
        // Logic to navigate back to the welcome screen
    }

    // Load sample data in "Working Diagrams"
    private void loadWorkingDiagrams() {
        diagramListView.getItems().addAll("Main Model", "ClassDiagram1", "UseCaseDiagram1");
    }

    // Set up handlers for Toolbox buttons
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
        System.out.println("Selected Tool: " + tool);
        // Add logic to activate the tool and update the drawing mode
    }
}
