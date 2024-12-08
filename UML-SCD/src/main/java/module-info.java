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

    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires javafx.swing;

    exports com.example.umlscd.Models.UseCaseDiagram;
    opens com.example.umlscd.Models.UseCaseDiagram to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.Models.ClassDiagram;
    opens com.example.umlscd.Models.ClassDiagram to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.PresentationLayer;
    opens com.example.umlscd.PresentationLayer to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.BusinessLayer.UseCaseDiagram;
    opens com.example.umlscd.BusinessLayer.UseCaseDiagram to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.BusinessLayer.ClassDiagram;
    opens com.example.umlscd.BusinessLayer.ClassDiagram to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.PresentationLayer.ClassDiagram;
    opens com.example.umlscd.PresentationLayer.ClassDiagram to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.PresentationLayer.UseCaseDiagram;
    opens com.example.umlscd.PresentationLayer.UseCaseDiagram to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.DataAccessLayer.Codegeneration;
    opens com.example.umlscd.DataAccessLayer.Codegeneration to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.DataAccessLayer.ImageProcessing;
    opens com.example.umlscd.DataAccessLayer.ImageProcessing to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.EntryPoint;
    opens com.example.umlscd.EntryPoint to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.DataAccessLayer.Serializers.ClassDiagram;
    opens com.example.umlscd.DataAccessLayer.Serializers.ClassDiagram to com.fasterxml.jackson.databind, javafx.fxml;
    exports com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram;
    opens com.example.umlscd.DataAccessLayer.Serializers.UseCaseDiagram to com.fasterxml.jackson.databind, javafx.fxml;
}