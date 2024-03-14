package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationLogEntry;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReportLogs;
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

public class TrafficCitationController {
    public Spinner citationNumber;
    public TextField citationDate;
    public TextField citationTime;
    public TextField citationCounty;
    public TextField citationArea;
    public TextField citationStreet;
    public TextField offenderName;
    public ComboBox offenderGender;
    public TextField offenderEthnicity;
    public Label offenderAge;
    public TextField offenderDescription;
    public TextField offenderVehicleMake;
    public TextField offenderVehicleModel;
    public ComboBox offenderVehicleColor;
    public ComboBox offenderVehicleType;
    public TextField offenderVehiclePlate;
    public TextField offenderVehicleOther;
    public TextArea offenderViolations;
    public TextField offenderActionsTaken;
    public TextField officerRank;
    public TextField officerName;
    public TextField officerNumber;
    public TextField officerDivision;
    public TextField officerAgency;
    public TextArea citationComments;

    public Label incompleteLabel;
    public Slider offenderAgeSlider;
    public VBox vbox;
    boolean hasEntered = false;
    private double xOffset = 0;
    private double yOffset = 0;

    public void onMouseEntered(MouseEvent mouseEvent) throws IOException {
        if (hasEntered) {
        } else {
            offenderAgeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                offenderAge.setText(String.valueOf(newValue.intValue())); // Update the text of the label
            });
            String name = ConfigReader.configRead("Name");
            String division = ConfigReader.configRead("Division");
            String rank = ConfigReader.configRead("Rank");
            String number = ConfigReader.configRead("Number");
            String agency = ConfigReader.configRead("Agency");
            offenderGender.getItems().addAll(dropdownInfo.genders);
            offenderVehicleType.getItems().addAll(dropdownInfo.vehicleTypes);
            offenderVehicleColor.getItems().addAll(dropdownInfo.carColors);
            officerName.setText(name);
            officerDivision.setText(division);
            officerRank.setText(rank);
            officerAgency.setText(agency);
            officerNumber.setText(number);
            citationTime.setText(getTime());
            citationDate.setText(getDate());
            createSpinner(citationNumber, 0, 9999, 0);
        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered = true;
    }

    public void onArrestReportSubmitBtnClick(ActionEvent actionEvent) {
        if (citationNumber.getValue() == null
                || offenderGender.getValue() == null
                || offenderVehicleColor.getValue() == null
                || offenderVehicleType.getValue() == null) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            TrafficCitationReportLogs searchReportLogs = new TrafficCitationReportLogs();
            List<TrafficCitationLogEntry> logs = TrafficCitationReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new TrafficCitationLogEntry(
                    citationNumber.getValue().toString(),
                    citationDate.getText(),
                    citationTime.getText(),
                    citationCounty.getText(),
                    citationArea.getText(),
                    citationStreet.getText(),
                    offenderName.getText(),
                    offenderGender.getValue().toString(),
                    offenderEthnicity.getText(),
                    offenderAge.getText(),
                    offenderDescription.getText(),
                    offenderVehicleMake.getText(),
                    offenderVehicleModel.getText(),
                    offenderVehicleColor.getValue().toString(),
                    offenderVehicleType.getValue().toString(),
                    offenderVehiclePlate.getText(),
                    offenderVehicleOther.getText(),
                    offenderViolations.getText(),
                    offenderActionsTaken.getText(),
                    officerRank.getText(),
                    officerName.getText(),
                    officerNumber.getText(),
                    officerDivision.getText(),
                    officerAgency.getText(),
                    citationComments.getText()

            ));
            // Save logs to XML
            TrafficCitationReportLogs.saveLogsToXML(logs);
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