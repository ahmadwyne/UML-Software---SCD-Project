package com.example.umlscd.PresentationLayer.ClassDiagram;

import com.example.umlscd.BusinessLayer.ClassDiagram.ClassDiagramManager;
import com.example.umlscd.BusinessLayer.ClassDiagram.InterfaceEditorManager;
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

    /**
     * Deletes a method from the list of methods.
     * <p>This method prompts the user to enter the name of the method they want to delete. If the method with the provided
     * name is found, it will be removed from the list. The updated list is then displayed in the `methodsArea`.</p>
     *
     * <p>The deletion works by searching for a method that contains the specified method name in the list of methods.
     * If multiple methods with similar names exist, all of them will be deleted. Ensure that method names are unique to
     * avoid unintentional deletions.</p>
     */
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

    /**
     * Edits an existing method in the code editor.
     * <p> This method displays a dialog where the user can enter the old method name they wish to edit,
     * the updated method name, method parameters, visibility modifier, and return type. The dialog pre-fills
     * with the currently selected method from the editor, allowing the user to update individual components.</p>
     * <p> The method utilizes helper methods such as {@link #parseVisibility(String)}, {@link #parseReturnType(String)},
     * {@link #parseMethodName(String)}, and {@link #parseParameters(String)} to extract and parse the method details from
     * the selected method text. </p>
     */
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

    /**
     * A class to store method details including the old and new method names, visibility modifier, return type,
     * and parameters.
     * <p>
     * This class is used to manage method details while editing methods in the code editor.
     * It holds information about a method's current and updated values, which are passed to the editor when making changes.
     * </p>
     */
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

    /**
     * Parses the visibility from a method signature string.
     * <p>
     * This method examines the beginning of a method string to determine its visibility. It checks for the following symbols:
     * If no specific visibility is found, it defaults to public visibility.
     * </p>
     *
     * @param attribute the method signature string to parse
     * @return the visibility as a string, e.g., "+ public", "- private", or "# protected"
     */
    private String parseVisibility(String attribute) {
        if (attribute.startsWith("+")) return "+ public";
        if (attribute.startsWith("-")) return "- private";
        if (attribute.startsWith("#")) return "# protected";
        return "+ public"; // Default to public
    }

    /**
     * Parses the visibility symbol for saving an updated method attribute.
     * <p>
     * This method converts the full visibility string (e.g., "+ public") into the corresponding symbol
     * ("+" for public, "-" for private, "#" for protected).
     * </p>
     *
     * @param visibility the full visibility string (e.g., "+ public")
     * @return the visibility symbol corresponding to the provided string
     */
    private String parseVisibilitySymbol(String visibility) {
        return switch (visibility) {
            case "+ public" -> "+";
            case "- private" -> "-";
            case "# protected" -> "#";
            default -> "+";
        };
    }

    /**
     * Displays an error dialog with the specified title and content.
     * <p>
     * This method creates an error dialog that displays the provided title and message content.
     * It is typically used when a method update or deletion fails, or when an invalid input is provided.
     * </p>
     *
     * @param title the title of the error dialog
     * @param content the content of the error dialog
     */    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);  // No header for the error dialog
        alert.setContentText(content);  // Content of the error message
        alert.showAndWait();  // Show the alert and wait for user to close it
    }

    /**
     * Displays a success dialog with the specified title and content.
     * <p>
     * This method creates an information dialog that displays the provided title and success message content.
     * It is typically used after successfully updating or deleting a method.
     * </p>
     *
     * @param title the title of the success dialog
     * @param content the content of the success dialog
     */
    private void showSuccessDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Parses method parameters from a method signature string.
     * <p>
     * This method extracts the parameters of a method from its signature by finding the portion between the
     * opening and closing parentheses. If no parameters are found, it returns an empty string.
     * </p>
     *
     * @param method the method signature string to parse
     * @return the method's parameters as a string, or an empty string if no parameters are present
     */
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


    /**
     * Parses the return type from a method signature string.
     * <p>
     * This method extracts the return type of a method by looking for the portion of the method signature
     * that follows the closing parenthesis. If no return type is found or if the input is invalid, it returns "void".
     * </p>
     *
     * @param method the method signature string to parse
     * @return the method's return type as a string, or "void" if no return type is found
     */
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

    /**
     * Parses the method name from a method signature string.
     * <p>
     * This method extracts the method name by finding the portion of the string before the opening parenthesis.
     * If no parenthesis is found, it returns the entire method string (which may be in an invalid format).
     * </p>
     *
     * @param method the method signature string to parse
     * @return the method's name as a string
     */
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