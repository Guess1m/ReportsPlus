package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class mainStage extends Stage {

    public static Stage mainRT;



    public mainStage() throws IOException {
        mainRT = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        mainRT.setTitle("Data Terminal");
        mainRT.setScene(newScene);
        mainRT.initStyle(StageStyle.UTILITY);
        mainRT.show();

    }


}
