package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class DataTerminalHomeApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Stage stage1 = new mainStage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        mainStage.mainRT.setScene(newScene);
        mainStage.mainRT.show();
        mainStage.mainRT.centerOnScreen();
        mainStage.mainRT.setY(mainStage.mainRT.getY() * 3f / 2f);


        actionController controller = loader.getController();
        StackPane defaultPane = controller.getDefaultPane();
        StackPane notesPane = controller.getNotesPane();
        StackPane codesPane = controller.getCodesPane();
        // Now you can work with the button




        //Check for previous Login
        boolean configExists = ConfigReader.doesConfigExist();

        if (configExists) {
            //already was a config
            notesPane.setVisible(true);
            notesPane.setDisable(false);

            codesPane.setVisible(false);
            codesPane.setDisable(true);
            defaultPane.setDisable(true);
            defaultPane.setVisible(false);

            System.out.println("notes should be showing since there already was a config");

        } else {
            //blank config
            defaultPane.setVisible(true);
            defaultPane.setDisable(false);

            codesPane.setVisible(false);
            codesPane.setDisable(true);
            notesPane.setDisable(true);
            notesPane.setVisible(false);


            System.out.println("login should be showing since there was no config");
        }


    }


    public static void main(String[] args) {
        launch();
    }
}
