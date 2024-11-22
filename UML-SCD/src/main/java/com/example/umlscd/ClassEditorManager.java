package com.example.umlscd;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClassEditorManager {

    private VBox classBox;
    private List<String> parameters = new ArrayList<>(); // Temporary storage for parameters

    public void setClassBox(VBox classBox) {
        this.classBox = classBox;
    }

    public String getClassName() {
        if (classBox != null && !classBox.getChildren().isEmpty()) {
            Label classNameLabel = (Label) classBox.getChildren().get(0);
            return classNameLabel.getText();
        }
        return "";
    }

    public String getAttributes() {
        if (classBox != null && classBox.getChildren().size() > 1) {
            VBox attributesBox = (VBox) classBox.getChildren().get(1);
            StringBuilder attributes = new StringBuilder();
            for (var node : attributesBox.getChildren()) {
                if (node instanceof Label) {
                    attributes.append(((Label) node).getText()).append("\n");
                }
            }
            return attributes.toString().trim();
        }
        return "";
    }

    public String getMethods() {
        if (classBox != null && classBox.getChildren().size() > 2) {
            VBox methodsBox = (VBox) classBox.getChildren().get(2);
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

    public void applyChanges(String className, String attributesText, String methodsText) {
        if (classBox != null && !classBox.getChildren().isEmpty()) {
            Label classNameLabel = (Label) classBox.getChildren().get(0);
            classNameLabel.setText(className);

            VBox attributesBox = (VBox) classBox.getChildren().get(1);
            attributesBox.getChildren().clear();
            for (String line : attributesText.split("\\n")) {
                Label attribute = new Label(line);
                attributesBox.getChildren().add(attribute);
            }

            VBox methodsBox = (VBox) classBox.getChildren().get(2);
            methodsBox.getChildren().clear();
            for (String line : methodsText.split("\\n")) {
                Label method = new Label(line);
                methodsBox.getChildren().add(method);
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
