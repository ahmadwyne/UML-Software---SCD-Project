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

import java.awt.event.MouseEvent;
import java.io.File;

import java.io.IOException;

/**
 * <h1>Class Diagram User Interface Controller</h1>
 *
 * <p>The {@code ClassDiagramUI} class serves as the controller for the Class Diagram editor within the UML Editor application.
 * It manages the user interface components, handles user interactions, and facilitates the creation, manipulation,
 * and visualization of class diagrams. The class interacts with the {@code ClassDiagramManager} to perform operations
 * such as adding classes, interfaces, associations, and exporting diagrams as images.</p>
 *
 * <p>Key functionalities include:
 * <ul>
 *     <li>Setting up event handlers for toolbox buttons (Class, Interface, Association, Aggregation, Composition, Inheritance, Drag)</li>
 *     <li>Loading and displaying existing diagrams</li>
 *     <li>Handling save and load operations for diagrams</li>
 *     <li>Exporting diagrams as image files</li>
 *     <li>Managing editors for classes and interfaces</li>
 *     <li>Providing user feedback through alerts</li>
 * </ul>
 * </p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class ClassDiagramUI {

    /**
     * The pane where the class diagram is drawn.
     */
    @FXML
    private Pane drawingPane;

    /**
     * The list view displaying available diagrams.
     */
    @FXML
    private ListView<String> diagramListView, modelExplorerListView;

    /**
     * Buttons for selecting various tools in the toolbox.
     */
    @FXML
    private Button btnClass, btnInterface, btnAssociation, btnDrag;

    @FXML
    private Button btnAggregation;

    @FXML
    private Button btnComposition;

    @FXML
    private Button btnInheritance;

    @FXML
    private Button btnSave, btnLoad, btnExportImage,btnCode; // Added Save and Load buttons

    /**
     * The pane containing editors for classes and interfaces.
     */
    @FXML
    private VBox editorsPane;

    /**
     * The manager responsible for handling class diagram operations.
     */
    private ClassDiagramManager classDiagramManager;

    /**
     * Reference to the primary stage for file chooser dialogs.
     */
    private Stage primaryStage; // Reference to primary stage for FileChooser


    /**
     * Constructs a {@code ClassDiagramUI} instance and initializes the {@code ClassDiagramManager}.
     */
    public ClassDiagramUI() {
        this.classDiagramManager = new ClassDiagramManager(this);
    }

    /**
     * Initializes the UI components, sets up event handlers, and loads existing diagrams.
     *
     * <p>This method is automatically called after the FXML file has been loaded. It configures the toolbox handlers,
     * loads predefined diagrams into the list view, and assigns actions to the save, load, and export buttons.</p>
     */
    @FXML
    private void initialize() {
        setupToolboxHandlers();
        loadWorkingDiagrams();

        // Setup Save and Load handlers
        btnSave.setOnAction(e -> handleSave());
        btnLoad.setOnAction(e -> handleLoad());

        // Setup Export as Image Handler
        btnExportImage.setOnAction(e -> handleExportAsImage());
        btnCode.setOnAction(e -> handleGenerateCode());
    }

    /**
     * Applies a hover effect to a button when the mouse enters its area.
     *
     * <p>This method changes the button's background color and scales it slightly to indicate a hover state.</p>
     *
     * @param event The {@code MouseEvent} triggered by the mouse entering the button area.
     */
    @FXML
    private void applyHoverEffect(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #C0C0C0; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    /**
     * Removes the hover effect from a button when the mouse exits its area.
     *
     * <p>This method reverts the button's background color and scale to its default state.</p>
     *
     * @param event The {@code MouseEvent} triggered by the mouse exiting the button area.
     */
    @FXML
    private void removeHoverEffect(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #AFAFAF; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    /**
     * Applies a click effect to a button when it is clicked.
     *
     * <p>This method changes the button's background color to indicate a clicked state.</p>
     *
     * @param event The {@code MouseEvent} triggered by the button click.
     */
    @FXML
    private void applyClickEffect(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #8C8C8C; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    /**
     * Loads predefined diagrams into the diagram list view.
     *
     * <p>This method populates the {@code diagramListView} with a set of predefined diagram names for demonstration purposes.</p>
     */
    private void loadWorkingDiagrams() {
        diagramListView.getItems().addAll("Main Model", "ClassDiagram1", "UseCaseDiagram1");
    }

    /**
     * Sets up event handlers for toolbox buttons.
     *
     * <p>This method assigns actions to each toolbox button, enabling the selection of different tools
     * such as Class, Interface, Association, Aggregation, Composition, Inheritance, and Drag modes.</p>
     */
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

    /**
     * Handles the selection of a tool from the toolbox.
     *
     * <p>This method delegates the tool selection to the {@code ClassDiagramManager}, specifying the tool type
     * and providing references to the drawing and editors panes.</p>
     *
     * @param tool The name of the tool selected (e.g., "Class", "Interface", "Association", "Drag").
     */
    private void handleToolSelection(String tool) {
        classDiagramManager.handleToolSelection(tool, drawingPane, editorsPane);
    }

    /**
     * Opens the class editor for the specified class box.
     *
     * <p>This method loads the Class Editor UI, associates it with the corresponding {@code UMLClassBox},
     * and displays it within the editors pane.</p>
     *
     * @param classBox The {@code VBox} representing the class to be edited.
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
     * <p>This method loads the Interface Editor UI and displays it within the editors pane.</p>
     *
     * @param interfaceBox The {@code VBox} representing the interface to be edited.
     */
    public void openInterfaceEditor(VBox interfaceBox) {
        // Retrieve the corresponding UMLClassBox
        UMLInterfaceBox umlInterfaceBox = classDiagramManager.getInterfaceByVBox(interfaceBox);
        if (umlInterfaceBox == null) {
            showErrorAlert("Failed to find the corresponding UMLInterfaceBox.");
            return;
        }

        // Load the InterfaceEditorUI (Assuming you're using FXML)
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/umlscd/InterfaceEditor.fxml"));
            VBox editor = loader.load();

            // Get the controller and set the interfaceBox and UMLInterfaceBox
            InterfaceEditorUI editorUI = loader.getController();
            editorUI.setInterfaceBox(interfaceBox, umlInterfaceBox);
            editorUI.setClassDiagramManager(classDiagramManager); // Inject ClassDiagramManager

            editorsPane.getChildren().clear();
            editorsPane.getChildren().add(editor);

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Failed to open Class Editor.");
        }
    }

    /**
     * Sets the reference to the primary stage of the application.
     *
     * <p>This method should be called from the main application class after loading the FXML to provide
     * a reference to the primary stage, enabling the use of file chooser dialogs.</p>
     *
     * @param primaryStage The primary {@code Stage} of the application.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Handles the Export as Image action.
     *
     * <p>This method opens a file chooser dialog, allowing the user to select the destination file and format.
     * It then delegates the export process to the {@code ClassDiagramManager} and provides user feedback based
     * on the success or failure of the operation.</p>
     */
    @FXML
    private void handleExportAsImage() {
        // Retrieve the Stage from the drawingPane (or any other node)
        Stage stage = (Stage) drawingPane.getScene().getWindow();

        if (stage == null) {
            showErrorAlert("Cannot retrieve the primary stage.");
            return;
        }

        // Create a FileChooser for saving the image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export UML Diagram as Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Image", "*.png"),
                new FileChooser.ExtensionFilter("JPEG Image", "*.jpg"),
                new FileChooser.ExtensionFilter("All Images", "*.png", "*.jpg", "*.jpeg")
        );

        // Show the save dialog
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                classDiagramManager.exportAsImage(file);
                showInformationAlert("Diagram exported successfully to " + file.getAbsolutePath());
            } catch (IllegalStateException e) {
                e.printStackTrace();
                showErrorAlert("Failed to export diagram: " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                showErrorAlert("An error occurred while saving the image.");
            }
        }
    }

    /**
     * Handles the Save action.
     *
     * <p>This method opens a file chooser dialog, allowing the user to select the destination file path.
     * It then delegates the save process to the {@code ClassDiagramManager} and provides user feedback upon completion.</p>
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
     * Handles the Load action.
     *
     * <p>This method opens a file chooser dialog, allowing the user to select a JSON file containing a saved UML diagram.
     * It then delegates the load process to the {@code ClassDiagramManager} and provides user feedback upon completion.</p>
     */
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
     * Displays an error alert to the user with the specified message.
     *
     * <p>This method creates and shows an alert dialog of type {@code ERROR}, providing feedback to the user
     * in case of failures or issues.</p>
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
     * Displays an information alert to the user with the specified message.
     *
     * <p>This method creates and shows an alert dialog of type {@code INFORMATION}, providing feedback to the user
     * upon successful operations.</p>
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
     * <p>This method returns the {@code Pane} where the class diagram is drawn, allowing other components
     * or controllers to interact with it.</p>
     *
     * @return The {@code Pane} representing the drawing area of the class diagram.
     */
    public Pane getDrawingPane() {
        return drawingPane;
    }

    /**
     * Applies a hover effect to a button when the mouse enters its area.
     *
     * <p>This method changes the button's background color, font size, font weight, font family, and scaling
     * to indicate a hover state.</p>
     *
     * @param mouseEvent The {@code MouseEvent} triggered by the mouse entering the button area.
     */
    public void applyHoverEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #C0C0C0; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    /**
     * Removes the hover effect from a button when the mouse exits its area.
     *
     * <p>This method reverts the button's background color, font size, font weight, font family, and scaling
     * to its default state.</p>
     *
     * @param mouseEvent The {@code MouseEvent} triggered by the mouse exiting the button area.
     */
    public void removeHoverEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #AFAFAF; -fx-font-size: 12px;  -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    /**
     * Applies a click effect to a button when it is clicked.
     *
     * <p>This method changes the button's background color and ensures its scaling is reset, indicating a clicked state.</p>
     *
     * @param mouseEvent The {@code MouseEvent} triggered by the button click.
     */
    public void applyClickEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #8C8C8C; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }
}
