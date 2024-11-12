package com.Guess.ReportsPlus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class newOfficerApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Launcher.class.getResource("Windows/Desktop/desktop-login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("ReportsPlus Login");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setMaximized(true);

    }

}
