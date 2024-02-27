package com.drozal.dataterminal;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class mainStage extends  Stage{

    public static Stage mainRT;

    public mainStage() throws IOException {
        mainRT = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("DataTerminalHome-view.fxml"));
        mainRT.setTitle("Welcome");
        mainRT.setScene(new Scene(root, 300, 275));
        mainRT.show();

    }



}
