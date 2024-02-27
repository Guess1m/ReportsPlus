package com.drozal.dataterminal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("second-view.fxml"));
        Scene scene =  new Scene(root);

        mainStage.mainRT.setScene(scene);
        mainStage.mainRT.setTitle("Second Window");
        mainStage.mainRT.show();}

    }
