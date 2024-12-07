package com.example.umlscd.PresentationLayer.ClassDiagram;

import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassEditorManager;
import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.util.Pair;

/**
 * <h1>Class Editor UI</h1>
 *
 * <p>The {@code ClassEditorUI} class represents the user interface for editing UML class details in the
 * UML Class Diagram Editor. It provides functionality to manage class attributes, methods, and parameters,
 * as well as apply changes to the UML model.</p>
 *
 * <p>This class manages various UI components such as text fields, combo boxes, and buttons, and interacts
 * with the {@code ClassEditorManager} to facilitate the creation and modification of UML class elements.
 * It also supports custom data types for attributes and methods and allows users to add parameters to methods.</p>
 *
 * <p><b>Authors:</b> Ahmad Wyne, Wahaj Asif, Muhammad Muneeb</p>
 *
 * <p><b>Version:</b> 1.0</p>
 * <p><b>Since:</b> 2024-12-03</p>
 */
public class ClassEditorUI {

    // UI Components from FXML
    @FXML private VBox root; // Root node with fx:id="root"
    @FXML private TextField classNameField;
    @FXML private TextArea attributesArea;
    @FXML private ComboBox<String> dataTypeDropdown;
    @FXML private ComboBox<String> visibilityDropdown;
    @FXML private TextField attributeNameField;
    @FXML private TextArea methodsArea;
    @FXML private ComboBox<String> returnTypeDropdown;
    @FXML private ComboBox<String> methodVisibilityDropdown;
    @FXML private TextField methodNameField;
    @FXML private Button addAttributeButton;
    @FXML private Button addMethodButton;
    @FXML private Button addParameterButton;
    @FXML private Button applyChangesButton;
    @FXML private Button deleteAttributeButton;
    @FXML private Button deleteMethodButton;
    @FXML private Button editAttributeButton;
    @FXML private Button editMethodButton;



    // Internal state for managing class attributes, methods, and parameters
    private ClassEditorManager classEditorManager;
    private List<String> currentParameters = new ArrayList<>();
    private ClassDiagramManager classDiagramManager;

    /**
     * Default constructor required by FXMLLoader.
     *
     * <p>This constructor initializes the {@code ClassEditorManager} and will be injected with the
     * {@code ClassDiagramManager} later through the {@code setClassDiagramManager()} method.</p>
     */
    public ClassEditorUI() {
        this.classEditorManager = new ClassEditorManager();
    }

    /**
     * Initializes the UI components with default values and sets up listeners for dynamic interactions.
     *
     * <p>This method is automatically called after the FXML file is loaded. It configures the drop-down lists
     * for data types, visibility modifiers, return types, and method visibility, and sets up event listeners
     * for handling custom data type input and adding attributes, methods, and parameters.</p>
     */
    @FXML
    private void initialize() {
        dataTypeDropdown.setItems(FXCollections.observableArrayList("int", "String", "double", "float", "boolean", "Custom..."));
        dataTypeDropdown.setValue("int");

        visibilityDropdown.setItems(FXCollections.observableArrayList("+ public", "- private", "# protected"));
        visibilityDropdown.setValue("+ public");

        returnTypeDropdown.setItems(FXCollections.observableArrayList("void", "int", "String", "double", "float", "Custom..."));
        returnTypeDropdown.setValue("void");

        methodVisibilityDropdown.setItems(FXCollections.observableArrayList("+ public", "- private", "# protected"));
        methodVisibilityDropdown.setValue("+ public");

        // Add listener to handle custom data type input for attributes
        dataTypeDropdown.setOnAction(event -> {
            if ("Custom...".equals(dataTypeDropdown.getValue())) {
                classEditorManager.handleCustomDataType(dataTypeDropdown); // Call custom data type handler
            }
        });

        // Add listener to handle custom return type input for methods
        returnTypeDropdown.setOnAction(event -> {
            if ("Custom...".equals(returnTypeDropdown.getValue())) {
                classEditorManager.handleCustomDataType(returnTypeDropdown); // Call custom data type handler
            }
        });

        // Add event handlers for buttons
        addAttributeButton.setOnAction(event -> addAttribute());
        addMethodButton.setOnAction(event -> addMethod());
        addParameterButton.setOnAction(event -> addParameter());
        applyChangesButton.setOnAction(event -> applyChanges());

        // Set actions for the delete buttons
        deleteAttributeButton.setOnAction(event -> deleteAttribute());
        deleteMethodButton.setOnAction(event -> deleteMethod());

        editAttributeButton.setOnAction(event -> editAttribute());
        editMethodButton.setOnAction(event -> editMethod());


    }

    /**
     * Sets the {@code ClassDiagramManager} reference and updates the {@code ClassEditorManager}.
     *
     * <p>This method allows the {@code ClassEditorUI} to interact with the {@code ClassDiagramManager}
     * instance, which is responsible for managing the UML diagram. The {@code ClassEditorManager} is
     * also updated with the reference to allow consistent diagram editing.</p>
     *
     * @param manager The {@code ClassDiagramManager} instance to be set.
     */
    public void setClassDiagramManager(ClassDiagramManager manager) {
        this.classDiagramManager = manager;
        this.classEditorManager.setClassDiagramManager(manager);
    }

    /**
     * Adds a new attribute to the class.
     *
     * <p>This method retrieves the data type, visibility, and name of the attribute from the input fields,
     * formats it, and appends it to the {@code attributesArea} TextArea. It then clears the attribute name field
     * to prepare for the next input.</p>
     */
    private void addAttribute() {
        String dataType = dataTypeDropdown.getValue();
        String visibility = visibilityDropdown.getValue();
        String attributeName = attributeNameField.getText();

        if (!attributeName.isEmpty()) {
            String attribute = visibility.charAt(0) + attributeName + " : " + dataType ;
            attributesArea.appendText(attribute);
            attributesArea.appendText("\n");
            attributeNameField.clear();
        }
    }

    /**
     * Adds a new method to the class.
     *
     * <p>This method retrieves the return type, visibility, method name, and parameters from the input fields,
     * formats the method signature, and appends it to the {@code methodsArea} TextArea. After adding the method,
     * the method name field is cleared and the list of parameters is reset.</p>
     */
    private void addMethod() {
        String returnType = returnTypeDropdown.getValue();
        String visibility = methodVisibilityDropdown.getValue();
        String methodName = methodNameField.getText();

        if (!methodName.isEmpty()) {
            String methodSignature = visibility.charAt(0) + methodName + "(" + String.join(", ", currentParameters) + "): " + returnType+"\n" ;
            methodsArea.appendText(methodSignature);
            methodNameField.clear();
            currentParameters.clear();  // Clear parameters after adding method
        }
    }

    /**
     * Adds a new parameter to the method.
     *
     * <p>This method opens a dialog to input the name and type of the parameter. When the user adds the
     * parameter, it is formatted as "name: type" and added to the list of current parameters. The dialog
     * allows the user to define multiple parameters for a method.</p>
     */
    private void addParameter() {
        // Create a new dialog with custom content
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Add Parameter");
        dialog.setHeaderText("Enter Parameter Details");

        // Set the button types
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Create the parameter name and type fields
        TextField paramNameField = new TextField();
        paramNameField.setPromptText("Parameter Name");
        TextField paramTypeField = new TextField();
        paramTypeField.setPromptText("Parameter Type");

        // Arrange the fields in a layout
        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(new Label("Parameter Name:"), paramNameField, new Label("Parameter Type:"), paramTypeField);
        dialog.getDialogPane().setContent(dialogContent);

        // Convert the result to a pair when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(paramNameField.getText(), paramTypeField.getText());
            }
            return null;
        });

        // Show the dialog and process the result
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(param -> {
            String paramName = param.getKey(); // Corrected capitalization
            String paramType = param.getValue(); // Corrected capitalization
            if (!paramName.isEmpty() && !paramType.isEmpty()) {
                // Format parameter as "name: type" and add to the current parameters list
                String parameter = paramName + ": " + paramType;
                currentParameters.add(parameter);
            }
        });
    }

    /**
     * Sets the class box and populates the editor fields with existing class data.
     *
     * <p>This method is called when an existing UML class is selected. It populates the class editor with
     * the current name, attributes, and methods of the UML class.</p>
     *
     * @param classBox    The {@code VBox} representing the class in the UI.
     * @param umlClassBox The {@code UMLClassBox} model object containing class data.
     */
    public void setClassBox(VBox classBox, UMLClassBox umlClassBox) {
        classEditorManager.setClassBox(classBox, umlClassBox);
        classNameField.setText(umlClassBox.getName());
        attributesArea.setText(String.join("\n", umlClassBox.getAttributes()));
        methodsArea.setText(String.join("\n", umlClassBox.getMethods()));
    }

    /**
     * Applies changes made in the editor to the UML model.
     *
     * <p>This method gathers the updated class name, attributes, and methods from the editor fields
     * and delegates the update process to the {@code ClassEditorManager}. It ensures that the
     * underlying UML model reflects the latest modifications made by the user.</p>
     */
    private void applyChanges() {
        classEditorManager.applyChanges(
                classNameField.getText(),
                attributesArea.getText(),
                methodsArea.getText()
        );
    }

    /**
     * Applies a hover effect to a button when the mouse enters.
     *
     * <p>This method changes the style of the button to provide visual feedback to the user
     * when they hover over it.</p>
     *
     * @param mouseEvent The {@code MouseEvent} triggered when the mouse enters the button area.
     */
    public void applyHoverEffect(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #C0C0C0; -fx-font-size: 12px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    public void applyHoverEffect1(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #C0C0C0; -fx-font-size: 11px; -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    /**
     * Removes the hover effect from a button when the mouse exits.
     *
     * <p>This method resets the style of the button to its default state when the mouse is no longer hovering over it.</p>
     *
     * @param mouseEvent The {@code MouseEvent} triggered when the mouse exits the button area.
     */
    public void removeHoverEffect(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #AFAFAF; -fx-font-size: 12px;  -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }
    public void removeHoverEffect1(MouseEvent mouseEvent) {
        Button button = (Button) mouseEvent.getSource();
        button.setStyle("-fx-background-color: #AFAFAF; -fx-font-size: 11px;  -fx-font-weight: bold; -fx-font-family: 'Verdana'; -fx-pref-width: 120; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }

    /**
     * Applies a click effect to a button when it is clicked.
     *
     * <p>This method changes the style of the button to provide visual feedback to the user
     * when they click on it.</p>
     *
     * @param mouseEvent The {@code MouseEvent} triggered when the button is clicked.
     */
    public void applyClickEffect(MouseEvent mouseEvent) {
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

    private void deleteAttribute() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Attribute");
        dialog.setHeaderText("Enter the name of the attribute to delete:");
        dialog.setContentText("Attribute Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(attributeName -> {
            List<String> updatedAttributes = new ArrayList<>();
            String[] attributes = attributesArea.getText().split("\n");

            for (String attribute : attributes) {
                if (!attribute.contains(attributeName)) {
                    updatedAttributes.add(attribute);
                }
            }

            attributesArea.setText(String.join("\n", updatedAttributes));
        });
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
    private void editAttribute() {
        Dialog<AttributeDetails> dialog = new Dialog<>();
        dialog.setTitle("Edit Attribute");
        dialog.setHeaderText("Edit the attribute details (you can leave fields unchanged).");

        ButtonType updateButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(updateButtonType, ButtonType.CANCEL);

        // Text fields for old and new attribute names
        TextField oldAttributeField = new TextField();
        oldAttributeField.setPromptText("Existing Attribute Name");
        TextField newAttributeField = new TextField();
        newAttributeField.setPromptText("Updated Attribute Name");

        // Dropdowns for visibility and data type with initial placeholder values
        ComboBox<String> visibilityDropdown = new ComboBox<>();
        visibilityDropdown.getItems().addAll("+ public", "- private", "# protected");
        visibilityDropdown.setValue("Visibility");  // Default placeholder value for visibility

        ComboBox<String> dataTypeDropdown = new ComboBox<>();
        dataTypeDropdown.getItems().addAll("int", "String", "double", "boolean", "char", "float", "Custom");
        dataTypeDropdown.setValue("DataType");  // Default placeholder value for data type

        // Pre-fill with current attribute values (if any)
        String currentAttribute = attributesArea.getSelectedText(); // Get the selected attribute text
        if (currentAttribute != null && !currentAttribute.isEmpty()) {
            oldAttributeField.setText(currentAttribute);

            // Set the dropdowns to the current values if attribute exists
            String visibility = parseVisibility(currentAttribute); // Parse visibility
            String dataType = parseDataType(currentAttribute); // Parse data type

            // Set the visibility and data type to current values if available
            if (visibility != null && !visibility.isEmpty() && !visibility.equals("Visibility")) {
                visibilityDropdown.setValue(visibility);
            }

            if (dataType != null && !dataType.isEmpty() && !dataType.equals("DataType")) {
                dataTypeDropdown.setValue(dataType);
            }
        }
        dataTypeDropdown.setOnAction(event -> {
            if ("Custom".equals(dataTypeDropdown.getValue())) {
                classEditorManager.handleCustomDataType(dataTypeDropdown); // Call custom data type handler
            }
        });

        // Layout for the dialog content
        VBox dialogContent = new VBox(10);
        dialogContent.getChildren().addAll(
                new Label("Existing Attribute Name:"), oldAttributeField,
                new Label("Updated Attribute Name:"), newAttributeField,
                new Label("Visibility:"), visibilityDropdown,
                new Label("Data Type:"), dataTypeDropdown
        );
        dialog.getDialogPane().setContent(dialogContent);

        // Result converter to handle dialog inputs
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == updateButtonType) {
                // Return the updated details, including custom data type if selected
                return new AttributeDetails(
                        oldAttributeField.getText(),
                        newAttributeField.getText(),
                        visibilityDropdown.getValue(),
                        dataTypeDropdown.getValue()
                );
            }
            return null;
        });

        Optional<AttributeDetails> result = dialog.showAndWait();
        if (result.isPresent()) {
            AttributeDetails attributeDetails = result.get();
            String oldAttribute = attributeDetails.oldName.trim();
            String newAttribute = attributeDetails.newName.trim();
            String visibility = attributeDetails.visibility;
            String dataType = attributeDetails.dataType;

            if (!oldAttribute.isEmpty()) {
                String currentText = attributesArea.getText();
                String[] lines = currentText.split("\n");
                boolean found = false;

                for (int i = 0; i < lines.length; i++) {
                    if (lines[i].contains(oldAttribute)) {
                        // Parse the current attribute to keep unchanged values
                        String currentVisibility = parseVisibility(lines[i]);
                        String currentDataType = parseDataType(lines[i]);
                        String currentName = parseAttributeName(lines[i]);

                        // Replace only updated components
                        String updatedVisibility = visibility != null && !visibility.equals("Visibility") ? parseVisibilitySymbol(visibility) : parseVisibilitySymbol(currentVisibility);
                        String updatedDataType = (dataType != null && !dataType.equals("DataType") && !dataType.equals("Custom")) ? dataType : currentDataType;
                        // Ensure name is updated or remains unchanged
                        String updatedName = !newAttribute.trim().isEmpty() ? newAttribute : currentName;

                        // If Custom Data Type was used, set the custom data type
                        if (dataType.equals("Custom") && !updatedDataType.equals("DataType")) {
                            updatedDataType = dataType; // Use the custom data type entered by user
                        }

                        String updatedAttribute = updatedVisibility + updatedName + " : " + updatedDataType;
                        lines[i] = updatedAttribute;
                        found = true;
                        break;
                    }
                }

                if (found) {
                    // Join the lines back and set the updated text
                    attributesArea.setText(String.join("\n", lines));

                    // Show success dialog
                    showSuccessDialog("Attribute Updated", "The attribute has been updated successfully.");
                } else {
                    // Show error message if the attribute is not found
                    showErrorDialog("Attribute Not Found", "The provided existing attribute name does not match any existing attributes.");
                }
            }
        }
    }


    // Helper class to encapsulate attribute details
    private static class AttributeDetails {
        String oldName;
        String newName;
        String visibility;
        String dataType;

        AttributeDetails(String oldName, String newName, String visibility, String dataType) {
            this.oldName = oldName;
            this.newName = newName;
            this.visibility = visibility;
            this.dataType = dataType;
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

    // Parse data type from the attribute string
    private String parseDataType(String attribute) {
        String[] parts = attribute.split(" : ");
        return parts.length > 1 ? parts[1].trim() : "String"; // Default to String
    }

    // Parse attribute name from the attribute string
    private String parseAttributeName(String attribute) {
        // Split the attribute by the space character
        String[] parts = attribute.split(" ");

        // Ensure there are at least 2 parts (name and type)
        if (parts.length >= 2) {
            // The first part will be the visibility modifier, so remove it
            String nameWithVisibility = parts[0].trim();

            // Remove the first character (visibility modifier) and return the attribute name
            return !nameWithVisibility.isEmpty() ? nameWithVisibility.substring(1).trim() : "attribute";
        }

        // Default case if the format is not as expected
        return "attribute"; // Default to "attribute"
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

    // Edit method logic
    private void editMethod() {
        Dialog<MethodsDetails> dialog = new Dialog<>();
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
                classEditorManager.handleCustomDataType(returnTypeDropdown); // Call custom data type handler
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

        Optional<MethodsDetails> result = dialog.showAndWait();
        if (result.isPresent()) {
            MethodsDetails methodsDetails = result.get();
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
    private static class MethodsDetails {
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
                if (returnType.startsWith(":")) {
                    returnType = returnType.substring(1).trim(); // Remove the colon and trim spaces
                }
                return returnType.isEmpty() ? "void" : returnType;  // Fallback to "void" if empty
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


}