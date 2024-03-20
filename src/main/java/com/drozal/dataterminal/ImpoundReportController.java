package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Impound.ImpoundLogEntry;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;

public class ImpoundReportController {
    public Spinner impoundNumber;
    public TextField impoundDate;
    public TextField impoundTime;
    public TextField ownerName;
    public Label ownerAge;
    public ComboBox ownerGender;
    public TextField ownerAddress;
    public TextField impoundPlateNumber;
    public TextField impoundMake;
    public TextField impoundModel;
    public ComboBox impoundType;
    public ComboBox impoundColor;
    public TextArea impoundComments;
    public TextField officerRank;
    public TextField officerName;
    public TextField officerNumber;
    public TextField officerDivision;
    public TextField officerAgency;
    public Label incompleteLabel;
    public Slider ownerAgeSlider;
    public VBox vbox;
    private double xOffset = 0;
    private double yOffset = 0;

    public TextField getImpoundDate() {
        return impoundDate;
    }

    public Spinner getImpoundNumber() {
        return impoundNumber;
    }

    public TextField getOwnerName() {
        return ownerName;
    }

    public TextField getOwnerAddress() {
        return ownerAddress;
    }

    public TextField getImpoundTime() {
        return impoundTime;
    }

    public TextField getOfficerRank() {
        return officerRank;
    }

    public TextField getOfficerName() {
        return officerName;
    }

    public TextField getOfficerNumber() {
        return officerNumber;
    }

    public TextField getOfficerDivision() {
        return officerDivision;
    }

    public TextField getOfficerAgency() {
        return officerAgency;
    }

    public void initialize() throws IOException {
        ownerAgeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            ownerAge.setText(String.valueOf(newValue.intValue()));
        });
        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        ownerGender.getItems().addAll(dropdownInfo.genders);
        impoundType.getItems().addAll(dropdownInfo.vehicleTypes);
        impoundColor.getItems().addAll(dropdownInfo.carColors);
        officerName.setText(name);
        officerDivision.setText(division);
        officerRank.setText(rank);
        officerAgency.setText(agency);
        officerNumber.setText(number);
        impoundTime.setText(getTime());
        impoundDate.setText(getDate());
        createSpinner(impoundNumber, 0, 9999, 0);
    }


    public void onArrestReportSubmitBtnClick(ActionEvent actionEvent) {
        if (impoundNumber.getValue() == null ||
                ownerGender.getValue() == null ||
                impoundType.getValue() == null ||
                impoundColor.getValue() == null) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            ImpoundReportLogs searchReportLogs = new ImpoundReportLogs();
            List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new ImpoundLogEntry(
                    impoundNumber.getValue().toString(),
                    impoundDate.getText(),
                    impoundTime.getText(),
                    ownerName.getText(),
                    ownerAge.getText(),
                    ownerGender.getValue().toString(),
                    ownerAddress.getText(),
                    impoundPlateNumber.getText(),
                    impoundMake.getText(),
                    impoundModel.getText(),
                    impoundType.getValue().toString(),
                    impoundColor.getValue().toString(),
                    impoundComments.getText(),
                    officerRank.getText(),
                    officerName.getText(),
                    officerNumber.getText(),
                    officerDivision.getText(),
                    officerAgency.getText()
            ));
            // Save logs to XML
            ImpoundReportLogs.saveLogsToXML(logs);
            // Close the stage
            Stage stage = (Stage) vbox.getScene().getWindow();
            stage.close();
        }
    }


    public void onMouseDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    public void onMousePress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onExitButtonClick(MouseEvent actionEvent) {
        // Get the window associated with the scene
        Window window = vbox.getScene().getWindow();

        // Close the window
        window.hide(); // or window.close() if you want to force close
    }
}
