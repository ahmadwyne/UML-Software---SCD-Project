package com.example.umlscd;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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

    /**
     * TextField for entering the interface name.
     *
     * <p>This field allows the user to enter the name of the interface being edited.</p>
     */
    @FXML
    private TextField interfaceNameField;

    /**
     * TextFlow for displaying the methods of the interface.
     *
     * <p>This TextFlow contains all the methods in the interface, and each method is displayed as a {@code Text} node.</p>
     */
    @FXML
    private TextFlow methodsFlow;

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
            String methodSignature = visibility.charAt(0) + methodName + "(" + String.join(", ", currentParameters) + "): " + returnType;
            addMethodToUI(methodSignature);  // Add method to TextFlow
            methodNameField.clear();
            currentParameters.clear();  // Clear parameters after adding the method
        }
    }

    /**
     * Adds a method signature to the {@code methodsFlow}.
     *
     * <p>This method creates a new {@code Text} node containing the method signature and adds it to the {@code methodsFlow}
     * for display. The method signature is shown in italic style.</p>
     *
     * @param methodSignature The method signature to be added.
     */
    private void addMethodToUI(String methodSignature) {
        Text methodText = new Text(methodSignature + "\n");
        methodText.setStyle("-fx-font-style: italic;"); // Apply italic style
        methodsFlow.getChildren().add(methodText); // Add to TextFlow
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
     * Applies the changes made in the editor to the interface model.
     *
     * <p>This method collects the methods from the {@code methodsFlow}, concatenates their content, and sends the
     * data to the {@code interfaceEditorManager} to update the interface model with the new methods and name.</p>
     */
    private void applyChanges() {
        // Collect text content from each Text node in methodsFlow
        StringBuilder methodsContent = new StringBuilder();

        // Iterate through all Text nodes in the TextFlow and concatenate their content
        for (var child : methodsFlow.getChildren()) {
            if (child instanceof Text) {
                methodsContent.append(((Text) child).getText());
            }
        }

        // Apply changes to the interface (e.g., save changes to the model or update UI)
        interfaceEditorManager.applyChanges(interfaceNameField.getText(), methodsContent.toString());
    }

    /**
     * Sets the current interface for editing and initializes the editor with its details.
     *
     * <p>This method takes the current interface box, retrieves its methods, and displays them in the {@code methodsFlow}.
     * It also sets the name of the interface in the {@code interfaceNameField}.</p>
     *
     * @param interfaceBox The VBox representing the current interface to edit.
     */
    public void setInterface(VBox interfaceBox) {
        // Set the interface box in the manager
        interfaceEditorManager.setInterfaceBox(interfaceBox);

        // Initialize the interface name in the editor
        interfaceNameField.setText(interfaceEditorManager.getInterfaceName());

        // Clear the methods flow before populating
        methodsFlow.getChildren().clear();

        // Get the methods from the interfaceBox and display them in the methodsFlow (using Labels)
        String methodsText = interfaceEditorManager.getMethods();
        for (String method : methodsText.split("\\n")) {
            Text methodLabel = new Text(method + "\n");
            methodLabel.setStyle("-fx-font-style: italic;"); // Apply italic style
            methodsFlow.getChildren().add(methodLabel); // Add to methodsFlow
        }
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
}
