package com.drozal.dataterminal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class newOfficerApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Stage newOfficerStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("newOfficer-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        newOfficerStage.setTitle("New Officer Sign In");
        newOfficerStage.setScene(newScene);
        newOfficerStage.initStyle(StageStyle.TRANSPARENT);
        newOfficerStage.setResizable(false);
        // Load the icon image
        Image icon = new Image(getClass().getResourceAsStream("imgs/icons/icon.png"));
        // Set the icon for the primary stage
        newOfficerStage.getIcons().add(icon);
        newOfficerStage.show();
    }
}
