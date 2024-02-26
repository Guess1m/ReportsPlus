package com.drozal.dataterminal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 700);
        stage.setTitle("Data Terminal");
        stage.setScene(scene);
        stage.show();
    } //TESTING TESTING

    public static void main(String[] args) {
        launch();
    }
}