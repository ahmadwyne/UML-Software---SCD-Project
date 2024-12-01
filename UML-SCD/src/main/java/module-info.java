module com.example.umlscd {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    requires org.junit.jupiter.api;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires javafx.swing;

    opens com.example.umlscd to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example.umlscd;
}