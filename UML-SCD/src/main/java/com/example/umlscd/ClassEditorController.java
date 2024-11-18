package com.example.umlscd;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class ClassEditorController {

    @FXML
    private TextField classNameField;
    @FXML
    private TextArea attributesArea;
    @FXML
    private ComboBox<String> dataTypeDropdown;
    @FXML
    private TextArea methodsArea;
    @FXML
    private ComboBox<String> returnTypeDropdown;
    @FXML
    private Button applyChangesButton;

    private VBox classBox;
    private Label classNameLabel;
    private VBox attributesBox;
    private VBox methodsBox;

    public void setClassBox(VBox classBox) {
        this.classBox = classBox;
        this.classNameLabel = (Label) classBox.getChildren().get(0);
        this.attributesBox = (VBox) classBox.getChildren().get(1);
        this.methodsBox = (VBox) classBox.getChildren().get(2);

        classNameField.setText(classNameLabel.getText());
    }

    @FXML
    private void initialize() {
        dataTypeDropdown.setItems(FXCollections.observableArrayList("int", "String", "double", "float", "boolean", "Custom..."));
        returnTypeDropdown.setItems(FXCollections.observableArrayList("void", "int", "String", "double", "float", "Custom..."));

        applyChangesButton.setOnAction(event -> applyChanges());
    }

    private void applyChanges() {
        classNameLabel.setText(classNameField.getText());

        attributesBox.getChildren().clear();
        for (String line : attributesArea.getText().split("\\n")) {
            Label attribute = new Label(line);
            attributesBox.getChildren().add(attribute);
        }

        methodsBox.getChildren().clear();
        for (String line : methodsArea.getText().split("\\n")) {
            Label method = new Label(line);
            methodsBox.getChildren().add(method);
        }
    }
}
