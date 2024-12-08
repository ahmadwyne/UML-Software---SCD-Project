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
import java.util.Objects;
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
    TextField interfaceNameField;

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


    // Edit method logic
    private void editMethod() {
        Dialog<InterfaceEditorUI.MethodsDetails> dialog = new Dialog<>();
        dialog.setTitle("Edit Method");
        dialog.setHeaderText("Enter the method to edit and the updated method details.");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Text fields for old and new method name
        TextField oldMethodField = new TextField();
        oldMethodField.setPromptText("Existing Method Name");
        TextField newMethodField = new TextField();
        newMethodField.setPromptText("Updated Method Name");

        // Text field for parameters
        TextField parametersField = new TextField();
        parametersField.setPromptText("Method Parameters");

        // Dropdowns for visibility and return type
        ComboBox<String> visibilityDropdown = new ComboBox<>();
        visibilityDropdown.getItems().addAll("+ public", "- private", "# protected");
        visibilityDropdown.setValue("Visibility");  // Default placeholder value for visibility

        ComboBox<String> returnTypeDropdown = new ComboBox<>();
        returnTypeDropdown.getItems().addAll("int", "String", "double", "boolean", "char", "float", "Custom");
        returnTypeDropdown.setValue("ReturnType");  // Default placeholder value for return type

        // Pre-fill with current method values (if any)
        String currentMethod = methodsArea.getSelectedText(); // Get the selected method text
        if (currentMethod != null && !currentMethod.isEmpty()) {
            oldMethodField.setText(currentMethod);

            // Set the dropdowns to the current values if method exists
            String visibility = parseVisibility(currentMethod); // Parse visibility
            String returnType = parseReturnType(currentMethod); // Parse return type
            String methodName = parseMethodName(currentMethod); // Parse method name
            String parameters = parseParameters(currentMethod); // Parse method parameters

            // Set the visibility and return type to current values if available
            if (visibility != null && !visibility.isEmpty() && !visibility.equals("Visibility")) {
                visibilityDropdown.setValue(visibility);
            }

            if (returnType != null && !returnType.isEmpty() && !returnType.equals("Return Type")) {
                returnTypeDropdown.setValue(returnType);
            }

            if (methodName != null && !methodName.isEmpty()) {
                oldMethodField.setText(methodName);
            }

            // Pre-fill the parameters field
            parametersField.setText(parameters);
        }

        // Handle Custom return type selection
        returnTypeDropdown.setOnAction(event -> {
            if ("Custom".equals(returnTypeDropdown.getValue())) {
                InterfaceEditorManager.handleCustomDataType(returnTypeDropdown); // Call custom data type handler
            }
        });

        // Layout for the dialog content
        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(
                new Label("Existing Method Name:"), oldMethodField,
                new Label("Updated Method Name:"), newMethodField,
                new Label("Parameters (Each Parameter should be written comma separated and like id:int"), parametersField,
                new Label("Visibility:"), visibilityDropdown,
                new Label("Return Type:"), returnTypeDropdown
        );
        dialog.getDialogPane().setContent(dialogContent);

        // Result converter to handle dialog inputs
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                // Return the updated details, including custom data type if selected
                return new MethodsDetails(
                        oldMethodField.getText(),
                        newMethodField.getText(),
                        visibilityDropdown.getValue(),
                        returnTypeDropdown.getValue(),
                        parametersField.getText() // Include the parameters
                );
            }
            return null;
        });

        Optional<InterfaceEditorUI.MethodsDetails> result = dialog.showAndWait();
        if (result.isPresent()) {
            InterfaceEditorUI.MethodsDetails methodsDetails = result.get();
            String oldMethod = methodsDetails.oldName.trim();
            String newMethod = methodsDetails.newName.trim();
            String visibility = methodsDetails.visibility;
            String returnType = methodsDetails.returnType;
            String parameters = methodsDetails.parameters.trim();

            if (!oldMethod.isEmpty()) {
                String currentText = methodsArea.getText();
                String[] lines = currentText.split("\n");
                boolean found = false;
                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].contains(oldMethod)) {
                        // Parse the current method to keep unchanged values
                        String currentVisibility = parseVisibility(lines[i]);
                        String currentReturnType = parseReturnType(lines[i]);
                        String currentName = parseMethodName(lines[i]);
                        String currentParameters = parseParameters(lines[i]);

                        // Replace only updated components
                        String updatedVisibility = visibility != null && !visibility.equals("Visibility") ? parseVisibilitySymbol(visibility) : parseVisibilitySymbol(currentVisibility);
                        String updatedReturnType = (returnType != null && !returnType.equals("ReturnType") && !returnType.equals("Custom")) ? returnType : currentReturnType;
                        String updatedParameters = !parameters.isEmpty() ? parameters : currentParameters;
                        String updatedName = !newMethod.trim().isEmpty() ? newMethod : currentName;

                        // If Custom Data Type was used, set the custom data type
                        if (returnType.equals("Custom") && !updatedReturnType.equals("ReturnType")) {
                            updatedReturnType = returnType; // Use the custom data type entered by user
                        }

                        String updatedAttribute = updatedVisibility + updatedName + "(" + updatedParameters + "): " + updatedReturnType;
                        lines[i] = updatedAttribute;
                        found = true;
                        break;
                    }
                }

                if (found) {
                    // Join the lines back and set the updated text
                    methodsArea.setText(String.join("\n", lines));

                    // Show success dialog
                    showSuccessDialog("Method Updated", "The method has been updated successfully.");
                } else {
                    // Show error message if the method is not found
                    showErrorDialog("Method Not Found", "The provided existing method name does not match any existing methods.");
                }
            }
        }
    }

    // MethodsDetails class now includes parameters as well
    public static class MethodsDetails {
        String oldName;
        String newName;
        String visibility;
        String returnType;
        String parameters; // Store the parameters

        MethodsDetails(String oldName, String newName, String visibility, String returnType, String parameters) {
            this.oldName = oldName;
            this.newName = newName;
            this.visibility = visibility;
            this.returnType = returnType;
            this.parameters = parameters;
        }
    }
    // Parse visibility from the attribute string
    private String parseVisibility(String attribute) {
        if (attribute.startsWith("+")) return "+ public";
        if (attribute.startsWith("-")) return "- private";
        if (attribute.startsWith("#")) return "# protected";
        return "+ public"; // Default to public
    }

    // Parse visibility symbol for saving the updated attribute
    private String parseVisibilitySymbol(String visibility) {
        return switch (visibility) {
            case "+ public" -> "+";
            case "- private" -> "-";
            case "# protected" -> "#";
            default -> "+";
        };
    }
    // Show error dialog
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header for the error dialog
        alert.setContentText(content);  // Content of the error message
        alert.showAndWait();  // Show the alert and wait for user to close it
    }

    // Show success dialog
    private void showSuccessDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Parse method parameters from the method signature string
    private String parseParameters(String method) {
        // Remove any leading/trailing whitespace
        method = method.trim();

        // Find the opening and closing parentheses
        int startIndex = method.indexOf('(');
        int endIndex = method.indexOf(')');

        // If both parentheses are found, extract the part between them
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            return method.substring(startIndex + 1, endIndex).trim();
        }

        // Return empty string if no parameters are found
        return "";
    }


    // Parse return type from the method signature string
    private String parseReturnType(String method) {
        // Ensure the method is not empty and has valid format
        if (method == null || method.isEmpty()) {
            return "void";  // Return default value for empty input
        }

        int startIndex = method.indexOf('(');
        int endIndex = method.indexOf(')');
        if (startIndex != -1 && endIndex != -1 && endIndex < method.length() - 1) {
            try {
                String returnType = method.substring(endIndex + 1).trim(); // Extract return type after parentheses
                // If there is a colon, remove it
                if (returnType.startsWith(":")) {
                    returnType = returnType.substring(1).trim(); // Remove the colon and trim spaces
                }
                return returnType.isEmpty() ? "void" : returnType;
                // Fallback to "void" if empty
            } catch (StringIndexOutOfBoundsException e) {
                // Log or handle the error gracefully
                System.err.println("Error parsing return type: " + method);
                return "void";  // Fallback to "void"
            }

        }
        return "void";  // Return default value if the method signature is not in expected format
    }
    // Parse method name from the method signature string
    private String parseMethodName(String method) {
        // Trim leading/trailing whitespaces
        method = method.trim();

        // Find the position of the opening parenthesis '(' to locate the end of the method name
        int startIndex = method.indexOf('(');

        // If '(' is found, the method name is everything before it
        if (startIndex != -1) {
            return method.substring(1, startIndex).trim();
        }

        // If '(' is not found, return the method string as is (could be an invalid format or no parameters)
        return method;
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
