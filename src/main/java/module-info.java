module com.arthur.biblioteca.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    exports com.arthur.biblioteca.biblioteca.controller;
    opens com.arthur.biblioteca.biblioteca.controller to javafx.fxml;
    exports com.arthur.biblioteca.biblioteca.view;
    opens com.arthur.biblioteca.biblioteca.view to javafx.fxml;
}