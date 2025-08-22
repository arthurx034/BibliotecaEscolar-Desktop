package com.arthur.biblioteca.biblioteca.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/com/arthur/biblioteca/biblioteca/biblioteca.fxml"
        ));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Sistema de Biblioteca do ALDA");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
