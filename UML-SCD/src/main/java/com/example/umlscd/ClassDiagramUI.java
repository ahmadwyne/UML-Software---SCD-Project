package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ClassDiagramUI {

    @FXML
    private Pane drawingPane;

    @FXML
    private ListView<String> diagramListView, modelExplorerListView;

    @FXML
    private Button btnClass, btnInterface, btnAssociation, btnDrag;

    @FXML
    private Button btnAggregation;

    @FXML
    private Button btnComposition;

    @FXML
    private Button btnInheritance;

    @FXML
    private Button btnSave, btnLoad,btnCode; // Added Save and Load buttons

    @FXML
    private VBox editorsPane;

    private ClassDiagramManager classDiagramManager;

    private Stage primaryStage; // Reference to primary stage for FileChooser


    public ClassDiagramUI() {
        this.classDiagramManager = new ClassDiagramManager(this);
    }

    @FXML
    private void initialize() {
        setupToolboxHandlers();
        loadWorkingDiagrams();

        // Setup Save and Load handlers
        btnSave.setOnAction(e -> handleSave());
        btnLoad.setOnAction(e -> handleLoad());
        btnCode.setOnAction(e -> handleGenerateCode());

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

    // Inside ClassDiagramUI or wherever the ClassEditorUI is invoked

    public void openClassEditor(VBox classBox) {
        // Retrieve the corresponding UMLClassBox
        UMLClassBox umlClassBox = classDiagramManager.getClassByVBox(classBox);
        if (umlClassBox == null) {
            showErrorAlert("Failed to find the corresponding UMLClassBox.");
            return;
        }

        // Load the ClassEditorUI (Assuming you're using FXML)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/ClassEditor.fxml"));
            Parent root = loader.load();

            // Get the controller and set the classBox and UMLClassBox
            ClassEditorUI editorUI = loader.getController();
            editorUI.setClassBox(classBox, umlClassBox);

            // Display the editor (e.g., in a new window or a modal)
            Stage stage = new Stage();
            stage.setTitle("Class Editor");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to open Class Editor.");
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

    /**
     * Sets the primary stage reference.
     * This method should be called from the main application class after loading the FXML.
     *
     * @param primaryStage The primary Stage of the application.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Handles the Save action.
     */
    private void handleSave() {
        // Retrieve the Stage from the drawingPane (or any other node)
        Stage stage = (Stage) drawingPane.getScene().getWindow();

        if (stage == null) {
            showErrorAlert("Cannot retrieve the primary stage.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save UML Diagram");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            classDiagramManager.saveDiagram(file);
            showInformationAlert("Diagram saved successfully.");
        }
    }

    private void handleLoad() {
        // Retrieve the Stage from the drawingPane (or any other node)
        Stage stage = (Stage) drawingPane.getScene().getWindow();

        if (stage == null) {
            showErrorAlert("Cannot retrieve the primary stage.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load UML Diagram");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            classDiagramManager.loadDiagram(file);
            showInformationAlert("Diagram loaded successfully.");
        }
    }
    private void handleGenerateCode() {
        // Specify the output file location (you can customize this as needed)
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            // Generate the Java code and save it to the file
            ClassDiagramCodeGenerator codeGenerator = new ClassDiagramCodeGenerator();
            codeGenerator.generateCodeFiles(classDiagramManager.getClassDiagram(), file.getAbsolutePath());
        }
    }

    /**
     * Shows an error alert to the user.
     *
     * @param message The error message to display.
     */
    public void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows an information alert to the user.
     *
     * @param message The information message to display.
     */
    public void showInformationAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Provides access to the drawing pane.
     *
     * @return The drawing Pane.
     */
    public Pane getDrawingPane() {
        return drawingPane;
    }

}
