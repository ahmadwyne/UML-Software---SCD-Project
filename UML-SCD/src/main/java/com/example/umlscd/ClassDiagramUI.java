package com.example.umlscd;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
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
    private Button btnSave, btnLoad; // Added Save and Load buttons

    @FXML private Button btnDelete;
    private boolean isDeleteModeEnabled = false;

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
        setupDeleteButtonHandler();



    }

    /*// Handle Delete Button Click
    private void setupDeleteButtonHandler() {
        btnDelete.setOnAction(e -> {
            // Toggle delete mode
            if (isDeleteModeEnabled) {
                disableDeleteMode();
            } else {
                enableDeleteMode();
            }
        });
    }


    // Enable delete mode for selecting a class to delete
    private void enableDeleteMode() {
        // Enable delete functionality on the drawing pane
        drawingPane.setOnMouseClicked(event -> {
            VBox selectedElement = getSelectedElement(event);
            if (selectedElement != null) {
                // Call the delete method from ClassDiagramManager
                classDiagramManager.deleteSelectedElement(selectedElement);
            }
        });
    }

    // Disable delete mode and reset the button
    private void disableDeleteMode() {
        isDeleteModeEnabled = false;
        btnDelete.setText("Delete");
        drawingPane.setOnMouseClicked(null); // Disable delete mode
    }

    // Get the selected element from the drawing pane (using mouse click)
    private VBox getSelectedElement(javafx.scene.input.MouseEvent event) {
        for (Node node : drawingPane.getChildren()) {
            if (node instanceof VBox && node.getBoundsInParent().contains(event.getX(), event.getY())) {
                return (VBox) node; // Return the selected class as VBox
            }
        }
        return null; // No element selected
    }*/

    // Handle Delete Button Click
    private void setupDeleteButtonHandler() {
        btnDelete.setOnAction(e -> {
            if (isDeleteModeEnabled) {
                disableDeleteMode();
            } else {
                enableDeleteMode();
            }
        });
    }

    // Enable delete mode for selecting a class to delete
    private void enableDeleteMode() {
        isDeleteModeEnabled = true;
        btnDelete.setText("Cancel Delete"); // Update button text to indicate mode
        btnDelete.setDisable(false);

        // Enable delete functionality on the drawing pane
        drawingPane.setOnMouseClicked(event -> {
            VBox selectedElement = getSelectedElement(event);
            if (selectedElement != null) {
                // Call the delete method from ClassDiagramManager
                classDiagramManager.deleteSelectedElement(selectedElement);

                // Disable delete mode after deletion
                disableDeleteMode();
            }
        });
    }

    // Disable delete mode and reset the button
    private void disableDeleteMode() {
        isDeleteModeEnabled = false;
        btnDelete.setText("Delete");
        btnDelete.setDisable(false); // Re-enable the delete button
        drawingPane.setOnMouseClicked(null); // Remove the delete click listener
    }

    // Get the selected element from the drawing pane (using mouse click)
    private VBox getSelectedElement(javafx.scene.input.MouseEvent event) {
        for (Node node : drawingPane.getChildren()) {
            if (node instanceof VBox && node.getBoundsInParent().contains(event.getX(), event.getY())) {
                return (VBox) node; // Return the selected class as VBox
            }
        }
        return null; // No element selected
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

    /**
     * Opens the class editor for the specified class box.
     *
     * @param classBox The VBox representing the class.
     */
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
            VBox editor = loader.load();

            // Get the controller and set the classBox and UMLClassBox
            ClassEditorUI editorUI = loader.getController();
            editorUI.setClassBox(classBox, umlClassBox);
            editorUI.setClassDiagramManager(classDiagramManager); // Inject ClassDiagramManager

            editorsPane.getChildren().clear();
            editorsPane.getChildren().add(editor);

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to open Class Editor.");
        }
    }

    /**
     * Opens the interface editor for the specified interface box.
     *
     * @param interfaceBox The VBox representing the interface.
     */
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
            showErrorAlert("Failed to open Interface Editor.");
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
