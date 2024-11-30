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

public class InterfaceEditorUI {

    @FXML
    private TextField interfaceNameField;

    @FXML
    private TextFlow methodsFlow;  // Using TextFlow instead of TextArea

    @FXML
    private ComboBox<String> returnTypeDropdown;

    @FXML
    private ComboBox<String> methodVisibilityDropdown;

    @FXML
    private TextField methodNameField;

    @FXML
    private Button addMethodButton;

    @FXML
    private Button addParameterButton;

    @FXML
    private Button applyChangesButton;

    private InterfaceEditorManager interfaceEditorManager;  // Manager to handle logic

    private List<String> currentParameters = new ArrayList<>();

    public InterfaceEditorUI() {
        this.interfaceEditorManager = new InterfaceEditorManager(); // Initialize manager
    }

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

    private void addMethodToUI(String methodSignature) {
        Text methodText = new Text(methodSignature + "\n");
        methodText.setStyle("-fx-font-style: italic;"); // Apply italic style
        methodsFlow.getChildren().add(methodText); // Add to TextFlow
    }

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
            Label methodLabel = new Label(method);
            methodLabel.setStyle("-fx-font-style: italic;"); // Apply italic style
            methodsFlow.getChildren().add(methodLabel); // Add to methodsFlow
        }
    }
}

