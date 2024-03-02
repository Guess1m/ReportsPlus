package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class newOfficerController {
    public TextField numberField;
    public TextField nameField;
    public ComboBox rankDropdown;
    public ComboBox agencyDropDown;
    public ComboBox divisionDropDown;
    public VBox vboxid;
    boolean hasEntered = false;

    public void onMouseEnter(MouseEvent mouseEvent) {
        if (hasEntered) {
        } else {
            System.out.println("mouse hasnt entered, setting values");
            rankDropdown.getItems().addAll(dropdownInfo.ranks);
            divisionDropDown.getItems().addAll(dropdownInfo.divisions);
            agencyDropDown.getItems().addAll(dropdownInfo.agencies);
        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered = true;
    }

    public void loginButtonClick(ActionEvent actionEvent) throws IOException {
        // Check if any of the ComboBox values are null
        if (agencyDropDown.getValue() == null || divisionDropDown.getValue() == null ||
                rankDropdown.getValue() == null || nameField.getText().isEmpty() ||
                numberField.getText().isEmpty()) {
            System.out.println("Some fields are empty");
        } else {
            // Access the values only if they are not null
            String agency = agencyDropDown.getValue().toString();
            String division = divisionDropDown.getValue().toString();
            String rank = rankDropdown.getValue().toString();

            // Proceed with further processing
            ConfigWriter.configwrite("Agency", agency);
            ConfigWriter.configwrite("Division", division);
            ConfigWriter.configwrite("Name", nameField.getText());
            ConfigWriter.configwrite("Rank", rank);
            ConfigWriter.configwrite("Number", numberField.getText());

            Stage stag = (Stage) vboxid.getScene().getWindow();
            stag.close();


            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            stage.setTitle("Data Terminal");
            stage.setScene(newScene);
            stage.initStyle(StageStyle.UTILITY);
            stage.setResizable(false);
            stage.show();

            actionController actionController = loader.getController();
            actionController.getInfoPane().setDisable(true);
            actionController.getInfoPane().setVisible(false);
            actionController.getShiftInformationPane().setDisable(false);
            actionController.getShiftInformationPane().setVisible(true);
        }
    }
}
