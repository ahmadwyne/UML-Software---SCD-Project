package com.example.umlscd.PresentationLayer.ClassDiagram;

import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassDiagramManager;
import com.example.umlscd.BuisnessLayer.ClasDiagram.ClassEditorManager;
import com.example.umlscd.Models.ClassDiagram.UMLClassBox;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
            String attribute = visibility.charAt(0) + attributeName + " : " + dataType + "\n";
            attributesArea.appendText(attribute);
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
            String methodSignature = visibility.charAt(0) + methodName + "(" + String.join(", ", currentParameters) + "): " + returnType + "\n";
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


   /* private void editAttribute() {
        String currentAttributes = attributesArea.getText();
        if (currentAttributes.isEmpty()) {
            showAlert("No attributes to edit", "Please add an attribute first.");
            return;
        }

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Attribute");
        dialog.setHeaderText("Select and edit attribute details");

        // Split attributes into a list
        String[] attributeList = currentAttributes.split("\n");
        ChoiceBox<String> attributeChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(attributeList));
        attributeChoiceBox.setValue(attributeList[0]); // Select the first attribute by default

        TextField newNameField = new TextField();
        newNameField.setPromptText("New Attribute Name");

        ComboBox<String> newTypeDropdown = new ComboBox<>(FXCollections.observableArrayList("int", "String", "double", "float", "boolean", "Custom..."));
        newTypeDropdown.setValue("int");

        VBox dialogContent = new VBox(10, new Label("Select Attribute:"), attributeChoiceBox, new Label("New Name:"), newNameField, new Label("New Type:"), newTypeDropdown);
        dialog.getDialogPane().setContent(dialogContent);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(newNameField.getText(), newTypeDropdown.getValue());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            String newName = pair.getKey();
            String newType = pair.getValue();

            if (!newName.isEmpty()) {
                String selectedAttribute = attributeChoiceBox.getValue();
                String updatedAttribute = selectedAttribute.split(":")[0] + ": " + newType;

                String updatedAttributes = currentAttributes.replace(selectedAttribute, updatedAttribute);
                attributesArea.setText(updatedAttributes);
            }
        });
    }*/

    private void editAttribute() {
        String currentAttributes = attributesArea.getText();
        if (currentAttributes.isEmpty()) {
            showAlert("No attributes to edit", "Please add an attribute first.");
            return;
        }

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Attribute");
        dialog.setHeaderText("Select and edit attribute details");

        // Split attributes into a list
        String[] attributeList = currentAttributes.split("\n");
        ChoiceBox<String> attributeChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(attributeList));
        attributeChoiceBox.setValue(attributeList[0]); // Select the first attribute by default

        TextField newNameField = new TextField();
        newNameField.setPromptText("New Attribute Name");

        ComboBox<String> newTypeDropdown = new ComboBox<>(FXCollections.observableArrayList("int", "String", "double", "float", "boolean", "Custom..."));
        newTypeDropdown.setValue("int");

        // Custom data type text field (initially hidden)
        TextField customTypeField = new TextField();
        customTypeField.setPromptText("Enter custom data type");
        customTypeField.setVisible(false);

        // Show/hide the custom type field based on the dropdown selection
        newTypeDropdown.setOnAction(event -> {
            if (newTypeDropdown.getValue().equals("Custom...")) {
                customTypeField.setVisible(true);
            } else {
                customTypeField.setVisible(false);
            }
        });

        VBox dialogContent = new VBox(10,
                new Label("Select Attribute:"), attributeChoiceBox,
                new Label("New Name:"), newNameField,
                new Label("New Type:"), newTypeDropdown,
                customTypeField);
        dialogContent.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(dialogContent);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                String selectedType = newTypeDropdown.getValue();
                if (selectedType.equals("Custom...")) {
                    selectedType = customTypeField.getText().trim();
                }
                return new Pair<>(newNameField.getText(), selectedType);
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            String newName = pair.getKey();
            String newType = pair.getValue();

            if (!newName.isEmpty() && !newType.isEmpty()) {
                // Get the selected attribute's old name and type
                String selectedAttribute = attributeChoiceBox.getValue();
                String oldName = selectedAttribute.split(":")[0].trim();
                String oldType = selectedAttribute.split(":")[1].trim();

                // Update the attribute with the new name and type
                String updatedAttribute = newName + ": " + newType;

                // Replace the old attribute in the attributes list with the updated one
                String updatedAttributes = currentAttributes.replace(oldName + ": " + oldType, updatedAttribute);
                attributesArea.setText(updatedAttributes);
            } else if (newType.isEmpty()) {
                showAlert("Invalid Input", "Custom data type cannot be empty.");
            }
        });
    }



    private void editMethod() {
        String currentMethods = methodsArea.getText();
        if (currentMethods.isEmpty()) {
            showAlert("No methods to edit", "Please add a method first.");
            return;
        }

        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Edit Method");
        dialog.setHeaderText("Select and edit method details");

        // Split methods into a list
        String[] methodList = currentMethods.split("\n");
        ChoiceBox<String> methodChoiceBox = new ChoiceBox<>(FXCollections.observableArrayList(methodList));
        methodChoiceBox.setValue(methodList[0]); // Select the first method by default

        TextField newMethodNameField = new TextField();
        newMethodNameField.setPromptText("New Method Name");

        ComboBox<String> newReturnTypeDropdown = new ComboBox<>(FXCollections.observableArrayList("void", "int", "String", "double", "float", "Custom..."));
        newReturnTypeDropdown.setValue("void");

        VBox dialogContent = new VBox(10, new Label("Select Method:"), methodChoiceBox, new Label("New Name:"), newMethodNameField, new Label("New Return Type:"), newReturnTypeDropdown);
        dialog.getDialogPane().setContent(dialogContent);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Pair<>(newMethodNameField.getText(), newReturnTypeDropdown.getValue());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(pair -> {
            String newMethodName = pair.getKey();
            String newReturnType = pair.getValue();

            if (!newMethodName.isEmpty()) {
                String selectedMethod = methodChoiceBox.getValue();
                String updatedMethod = selectedMethod.split("\\(")[0] + "(" + selectedMethod.split("\\(")[1].split("\\):")[0] + "): " + newReturnType;

                String updatedMethods = currentMethods.replace(selectedMethod, updatedMethod);
                methodsArea.setText(updatedMethods);
            }
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}