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
    useCaseLoadSerializer
    requires org.junit.jupiter.api;

    requires java.desktop;
    main

    opens com.example.umlscd to javafx.fxml;
    exports com.example.umlscd;
}