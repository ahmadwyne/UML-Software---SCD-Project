package com.example.umlscd;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.util.Pair;

public class ClassEditorUI {

    @FXML
    private TextField classNameField;
    @FXML
    private TextArea attributesArea;
    @FXML
    private ComboBox<String> dataTypeDropdown;
    @FXML
    private ComboBox<String> visibilityDropdown;
    @FXML
    private TextField attributeNameField;
    @FXML
    private TextArea methodsArea;
    @FXML
    private ComboBox<String> returnTypeDropdown;
    @FXML
    private ComboBox<String> methodVisibilityDropdown;
    @FXML
    private TextField methodNameField;
    @FXML
    private Button addAttributeButton;
    @FXML
    private Button addMethodButton;
    @FXML
    private Button addParameterButton;
    @FXML
    private Button applyChangesButton;

    private ClassEditorManager classEditorManager;
    private List<String> currentParameters = new ArrayList<>();
    //private ArrayList<String> currentParameters = new ArrayList<>(); // List to store parameters

    public ClassEditorUI() {
        this.classEditorManager = new ClassEditorManager();
    }

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
        addAttributeButton.setOnAction(event -> addAttribute());
        addMethodButton.setOnAction(event -> addMethod());
        addParameterButton.setOnAction(event -> addParameter());
        applyChangesButton.setOnAction(event -> applyChanges());
    }

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


    public void setClassBox(VBox classBox, UMLClassBox umlClassBox) {
        classEditorManager.setClassBox(classBox, umlClassBox);
        classNameField.setText(umlClassBox.getName());
        attributesArea.setText(String.join("\n", umlClassBox.getAttributes()));
        methodsArea.setText(String.join("\n", umlClassBox.getMethods()));
    }

    private void applyChanges() {
        classEditorManager.applyChanges(
                classNameField.getText(),
                attributesArea.getText(),
                methodsArea.getText()
        );
    }
}