package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class actionController {

    public StackPane defaultPane;
    public StackPane codesPane;
    public Button notesButton;
    public Button codeButton;
    public StackPane notesPane;
    public Button shiftInfoBtn;
    public TextField numberField;
    public TextField nameField;
    public TextField rankField;
    public TextField divisionField;
    public TextField agencyField;
    public Button submitLoginBtn;

    @FXML
    protected void onHelloButtonClick() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("second-view.fxml"));
        Scene scene = new Scene(root);

        mainStage.mainRT.setScene(scene);
        mainStage.mainRT.setTitle("Second Window");
        mainStage.mainRT.show();
    }

    public void setDisable(StackPane pane){
        pane.setVisible(false);
        pane.setDisable(true);
    }
    public void setActive(StackPane pane){
        pane.setVisible(true);
        pane.setDisable(false);
    }

    public void onCodeButtonClick(ActionEvent actionEvent) {
        setDisable(defaultPane);
        setActive(codesPane);
        setDisable(notesPane);

    }

    public void onNotesButtonClicked(ActionEvent actionEvent) {
        setDisable(codesPane);
        setDisable(defaultPane);
        setActive(notesPane);
    }


    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
        //
        setDisable(codesPane);
        setDisable(notesPane);
    }

    public StackPane getCodesPane() {
        return codesPane;
    }

    public StackPane getDefaultPane() {
        return defaultPane;
    }

    public StackPane getNotesPane() {
        return notesPane;
    }

    public void onSubmitLoginButtonClicked(ActionEvent actionEvent) {
        ConfigWriter.configwrite("Agency",agencyField.getText());
        ConfigWriter.configwrite("Division",divisionField.getText());
        ConfigWriter.configwrite("Name",nameField.getText());
        ConfigWriter.configwrite("Rank",rankField.getText());
        ConfigWriter.configwrite("Number",numberField.getText());
    }
}
