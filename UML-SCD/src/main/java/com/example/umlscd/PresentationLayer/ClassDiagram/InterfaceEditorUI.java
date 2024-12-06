package com.example.umlscd.PresentationLayer.ClassDiagram;

import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import com.example.umlscd.BuisnessLayer.ClasDiagram.InterfaceEditorManager;
import com.example.umlscd.Models.ClassDiagram.UMLInterfaceBox;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <h1>Interface Editor UI</h1>
 *
 * <p>The {@code InterfaceEditorUI} class manages the user interface for editing UML interfaces.
 * It provides functionalities such as adding methods, specifying method visibility, adding method parameters,
 * and applying changes to the model based on user input.</p>
 *
 * <p>It interacts with the {@code InterfaceEditorManager} to handle the underlying logic and updates the interface's
 * properties, including its name, methods, and parameters.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-04</p>
 */
public class InterfaceEditorUI {

    @FXML private VBox root; // Root node with fx:id="root"

    /**
     * TextField for entering the interface name.
     *
     * <p>This field allows the user to enter the name of the interface being edited.</p>
     */
    @FXML
    private TextField interfaceNameField;

    @FXML
    private TextArea methodsArea;

    /**
     * ComboBox for selecting the return type of a method.
     *
     * <p>This ComboBox provides predefined data types such as "void", "int", "String", etc., and allows the user
     * to choose a return type for the method being defined.</p>
     */
    @FXML
    private ComboBox<String> returnTypeDropdown;

    /**
     * ComboBox for selecting the visibility of a method.
     *
     * <p>This ComboBox allows the user to specify the visibility of a method (e.g., public, private, or protected).</p>
     */
    @FXML
    private ComboBox<String> methodVisibilityDropdown;

    /**
     * TextField for entering the method name.
     *
     * <p>This field allows the user to specify the name of a method to be added to the interface.</p>
     */
    @FXML
    private TextField methodNameField;

    /**
     * Button for adding a new method to the interface.
     *
     * <p>This button triggers the addition of a new method (with specified return type, visibility, name, and parameters)
     * to the interface.</p>
     */
    @FXML
    private Button addMethodButton;

    /**
     * Button for adding a parameter to a method.
     *
     * <p>This button opens a dialog where the user can specify a parameter for a method. It adds the parameter
     * to the method being defined.</p>
     */
    @FXML
    private Button addParameterButton;

    /**
     * Button for applying all changes made in the editor to the interface.
     *
     * <p>This button applies changes, such as the interface name and method list, to the interface model.</p>
     */
    @FXML
    private Button applyChangesButton;
    @FXML private Button deleteMethodButton;
    @FXML private Button editMethodButton;

    /**
     * The manager responsible for handling the logic of the interface editor.
     *
     * <p>This manager is responsible for managing the interface model and synchronizing the UI with the underlying data.</p>
     */
    private InterfaceEditorManager interfaceEditorManager;

    /**
     * A list holding the parameters for the current method being defined.
     *
     * <p>This list temporarily stores the parameters while the user is adding them. After the method is finalized,
     * the list is cleared.</p>
     */
    private List<String> currentParameters = new ArrayList<>();

    private ClassDiagramManager classDiagramManager;

    /**
     * Default constructor that initializes the {@code InterfaceEditorManager}.
     *
     * <p>This constructor initializes the manager which handles the interface editing logic.</p>
     */
    public InterfaceEditorUI() {
        this.interfaceEditorManager = new InterfaceEditorManager(); // Initialize manager
    }

    /**
     * Initializes the UI components and sets up event listeners.
     *
     * <p>This method sets up the ComboBoxes with predefined values, initializes event listeners for various
     * UI components, and configures buttons to trigger corresponding actions like adding methods, adding parameters,
     * and applying changes.</p>
     */
    @FXML
    private void initialize() {
        // Set up the ComboBoxes with predefined values
        returnTypeDropdown.setItems(FXCollections.observableArrayList("void", "int", "String", "double", "float", "Custom..."));
        returnTypeDropdown.setValue("void");

        methodVisibilityDropdown.setItems(FXCollections.observableArrayList("+ public", "- private", "# protected"));
        methodVisibilityDropdown.setValue("+ public");

        // Add listeners to handle custom return type input
        returnTypeDropdown.setOnAction(event -> {
            if ("Custom...".equals(returnTypeDropdown.getValue())) {
                // Handle custom data type (you can add a handler for custom types)
                interfaceEditorManager.handleCustomDataType(returnTypeDropdown);
            }
        });

        // Add button actions
        addMethodButton.setOnAction(event -> addMethod());
        addParameterButton.setOnAction(event -> addParameter());
        applyChangesButton.setOnAction(event -> applyChanges());
        deleteMethodButton.setOnAction(event -> deleteMethod());
        editMethodButton.setOnAction(event -> editMethod());
    }

    /**
     * Sets the {@code ClassDiagramManager} reference and updates the {@code InterfaceEditorManager}.
     *
     * <p>This method allows the {@code InterfaceEditorUI} to interact with the {@code ClassDiagramManager}
     * instance, which is responsible for managing the UML diagram. The {@code ClassEditorManager} is
     * also updated with the reference to allow consistent diagram editing.</p>
     *
     * @param manager The {@code ClassDiagramManager} instance to be set.
     */
    public void setClassDiagramManager(ClassDiagramManager manager) {
        this.classDiagramManager = manager;
        this.interfaceEditorManager.setClassDiagramManager(manager);
    }

    /**
     * Adds a method to the interface.
     *
     * <p>This method retrieves the method name, visibility, return type, and parameters from the UI fields
     * and creates a method signature. The method signature is then added to the {@code methodsFlow} UI component.</p>
     */
    private void addMethod() {
        String returnType = returnTypeDropdown.getValue();
        String visibility = methodVisibilityDropdown.getValue();
        String methodName = methodNameField.getText();

        if (!methodName.isEmpty()) {
            String methodSignature = visibility.charAt(0) + methodName + "(" + String.join(", ", currentParameters) + "): " + returnType + "\n";
            methodsArea.appendText(methodSignature);
            methodNameField.clear();
            currentParameters.clear();  // Clear parameters after adding method
        }
    }

    /**
     * Adds a parameter to the current method being defined.
     *
     * <p>This method opens a dialog where the user can input a parameter name and type. The parameter is formatted
     * as "name: type" and added to the list of current parameters for the method.</p>
     */
    private void addParameter() {
        // Create a new dialog for adding parameters
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Parameter");
        dialog.setHeaderText("Enter Parameter Details");

        // Set up the dialog buttons
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create parameter name and type fields
        TextField paramNameField = new TextField();
        paramNameField.setPromptText("Parameter Name");
        TextField paramTypeField = new TextField();
        paramTypeField.setPromptText("Parameter Type");

        // Arrange the fields in a layout
        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(new Label("Parameter Name:"), paramNameField, new Label("Parameter Type:"), paramTypeField);
        dialog.getDialogPane().setContent(dialogContent);

        // Convert result to Pair when the "Add" button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(paramNameField.getText(), paramTypeField.getText());
            }
            return null;
        });

        // Show the dialog and handle the result
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(param -> {
            String paramName = param.getKey();
            String paramType = param.getValue();
            if (!paramName.isEmpty() && !paramType.isEmpty()) {
                // Format parameter as "name: type" and add it to the list
                String parameter = paramName + ": " + paramType;
                currentParameters.add(parameter);
            }
        });
    }

    /**
     * Sets the interfaceBox and populates the editor fields with existing interface data.
     *
     * <p>This method is called when an existing UML interface is selected. It populates the interface editor with
     * the current name, and methods of the UML interface.</p>
     *
     * @param interfaceBox    The {@code VBox} representing the class in the UI.
     * @param umlInterfaceBox The {@code UMLInterfaceBox} model object containing interface data.
     */
    public void setInterfaceBox(VBox interfaceBox, UMLInterfaceBox umlInterfaceBox) {
        interfaceEditorManager.setInterfaceBox(interfaceBox, umlInterfaceBox);
        interfaceNameField.setText(umlInterfaceBox.getName());
        methodsArea.setText(String.join("\n", umlInterfaceBox.getMethods()));
    }


    private void deleteMethod() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Method");
        dialog.setHeaderText("Enter the name of the method to delete:");
        dialog.setContentText("Method Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(methodName -> {
            List<String> updatedMethods = new ArrayList<>();
            String[] methods = methodsArea.getText().split("\n");

            for (String method : methods) {
                if (!method.contains(methodName)) {
                    updatedMethods.add(method);
                }
            }

            methodsArea.setText(String.join("\n", updatedMethods));
        });
    }


    private void editMethod() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Method");
        dialog.setHeaderText("Enter the method to edit and the updated method.");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        TextField oldMethodField = new TextField();
        oldMethodField.setPromptText("Existing Method");
        TextField newMethodField = new TextField();
        newMethodField.setPromptText("Updated Method");

        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(new Label("Existing Method:"), oldMethodField, new Label("Updated Method:"), newMethodField);
        dialog.getDialogPane().setContent(dialogContent);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                return new Pair<>(oldMethodField.getText(), newMethodField.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(methodPair -> {
            String oldMethod = methodPair.getKey();
            String newMethod = methodPair.getValue();

            if (!oldMethod.isEmpty() && !newMethod.isEmpty()) {
                String currentText = methodsArea.getText();
                String updatedText = currentText.replace(oldMethod, newMethod);
                methodsArea.setText(updatedText);
            }
        });
    }

    /**
     * Applies the changes made in the editor to the interface model.
     *
     * <p>This method collects the methods from the {@code methodsArea}, concatenates their content, and sends the
     * data to the {@code interfaceEditorManager} to update the interface model with the new methods and name.</p>
     */
    private void applyChanges() {
        interfaceEditorManager.applyChanges(
                interfaceNameField.getText(),
                methodsArea.getText()
        );
    }



    /**
     * Applies a hover effect to a button when the mouse is hovered over it.
     *
     * <p>This method changes the background color and scale of the button to provide a visual indication that it is being hovered over.</p>
     *
     * @param mouseEvent The mouse event triggered when the mouse is over the button.
     */
    public void applyHoverEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #C0C0C0; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    /**
     * Removes the hover effect from a button when the mouse is no longer hovering over it.
     *
     * <p>This method resets the background color and scale of the button to its default state.</p>
     *
     * @param mouseEvent The mouse event triggered when the mouse leaves the button.
     */
    public void removeHoverEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #AFAFAF; -fx-font-size: 12px;  -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    /**
     * Applies a click effect to a button when it is clicked.
     *
     * <p>This method changes the background color and style of the button when clicked to provide feedback to the user.</p>
     *
     * @param mouseEvent The mouse event triggered when the button is clicked.
     */
    public void applyClickEffect(javafx.scene.input.MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #8C8C8C; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    /**
     * Provides access to the root node of {@code ClassEditorUI}.
     *
     * <p>This method returns the root {@code VBox} node, which can be used to integrate the class editor
     * UI into other parts of the application.</p>
     *
     * @return The root {@code VBox} of {@code ClassEditorUI}.
     */
    public VBox getEditorNode() {
        return root;
    }
}
