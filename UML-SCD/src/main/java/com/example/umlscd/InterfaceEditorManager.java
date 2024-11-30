package com.example.umlscd;


import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InterfaceEditorManager {

    private VBox interfaceBox;
    private List<String> parameters = new ArrayList<>(); // Temporary storage for parameters

    public void setInterfaceBox(VBox interfaceBox) {
        this.interfaceBox = interfaceBox;
    }

    public String getInterfaceName() {
        if (interfaceBox != null && !interfaceBox.getChildren().isEmpty()) {
            Label interfaceNameLabel = (Label) interfaceBox.getChildren().get(1);
            return interfaceNameLabel.getText();
        }
        return "";
    }

    public String getMethods() {
        if (interfaceBox != null && interfaceBox.getChildren().size() > 1) {
            VBox methodsBox = (VBox) interfaceBox.getChildren().get(2);
            StringBuilder methods = new StringBuilder();
            for (var node : methodsBox.getChildren()) {
                if (node instanceof Label) {
                    methods.append(((Label) node).getText()).append("\n");
                }
            }
            return methods.toString().trim();
        }
        return "";
    }
    public String getMethodsFromInterfaceBox(VBox interfaceBox) {
        StringBuilder methods = new StringBuilder();

        // Check if the interfaceBox has a methods section
        if (interfaceBox != null && interfaceBox.getChildren().size() > 2) {
            VBox methodsBox = (VBox) interfaceBox.getChildren().get(2); // Methods section is the third child
            for (var node : methodsBox.getChildren()) {
                if (node instanceof Text) {
                    // Append the method from Text node to methods string
                    methods.append(((Text) node).getText()).append("\n");
                }
            }
        }
        return methods.toString().trim(); // Return the methods as a single string
    }

    public void applyChanges(String interfaceName, String methodsText) {
        if (interfaceBox != null && !interfaceBox.getChildren().isEmpty()) {
            // Update the interface name label
            Label interfaceNameLabel = (Label) interfaceBox.getChildren().get(1);
            interfaceNameLabel.setText(interfaceName);

            // Get the methods VBox
            VBox methodsBox = (VBox) interfaceBox.getChildren().get(2);
            methodsBox.getChildren().clear(); // Clear existing methods

            // Split the methods text into individual lines and add each method as italicized text
            for (String line : methodsText.split("\\n")) {
                Text methodText = new Text(line); // Create a new Text node for each method
                methodText.setStyle("-fx-font-style: italic;"); // Apply italic style
                methodsBox.getChildren().add(methodText); // Add the Text node to the VBox
            }
        }
    }


    // Method to handle adding a custom data type to a dropdown
    public void handleCustomDataType(ComboBox<String> dropdown) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Custom Data Type");
        dialog.setHeaderText("Enter Custom Data Type:");
        dialog.setContentText("Data Type:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(customType -> {
            if (!dropdown.getItems().contains(customType)) {
                dropdown.getItems().add(customType);
            }
            dropdown.setValue(customType); // Set the custom type as the selected value
        });
    }

    // Add parameter to the temporary list of parameters for the current method
    public void addParameterToMethod(String parameter) {
        parameters.add(parameter);
    }

    // Finalize the method signature with parameters
    public String finalizeMethod(String methodName, String returnType, String visibility) {
        StringBuilder methodSignature = new StringBuilder(visibility.charAt(0) + methodName + "(");
        for (int i = 0; i < parameters.size(); i++) {
            methodSignature.append(parameters.get(i));
            if (i < parameters.size() - 1) {
                methodSignature.append(", ");
            }
        }
        methodSignature.append(") : ").append(returnType);
        return methodSignature.toString();
    }

    // Reset the temporary parameters after adding the method
    public void resetParameters() {
        parameters.clear();
    }
}

