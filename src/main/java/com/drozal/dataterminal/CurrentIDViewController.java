package com.drozal.dataterminal;

import com.drozal.dataterminal.util.reportCreationUtil;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.awt.*;

public class CurrentIDViewController {
    @javafx.fxml.FXML
    private BorderPane root;
    private static AnchorPane titleBar;
    @javafx.fxml.FXML
    private Label cursiveName;
    @javafx.fxml.FXML
    private Label genNum2;
    @javafx.fxml.FXML
    private TextField last;
    @javafx.fxml.FXML
    private TextField gender;
    @javafx.fxml.FXML
    private Label genNum1;
    @javafx.fxml.FXML
    private TextField dob;
    @javafx.fxml.FXML
    private TextField first;

    public static AnchorPane getTitleBar() {
        return titleBar;
    }

    public void initialize(){
        titleBar = reportCreationUtil.createTitleBar("Current ID");
        root.setTop(titleBar);

        cursiveName.setStyle("-fx-font-family: 'Signerica Medium';");
    }
}
