package com.drozal.dataterminal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DataTerminalHomeApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Stage stage1 = new mainStage();
        Parent root = FXMLLoader.load(getClass().getResource("DataTerminalHome-view.fxml"));
        Scene scene =  new Scene(root);

        mainStage.mainRT.setScene(scene);
        mainStage.mainRT.show();
        mainStage.mainRT.centerOnScreen();
        mainStage.mainRT.setY(mainStage.mainRT.getY() * 3f / 2f);

    }



    public static void main(String[] args) {
        launch();
    }
}
