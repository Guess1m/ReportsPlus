package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.ParkingCitation.ParkingCitationLogEntry;
import com.drozal.dataterminal.logs.ParkingCitation.ParkingCitationReportLogs;
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

public class ParkingCitationController {
    public VBox vbox;
    public Slider offenderAgeSlider;
    public Label incompleteLabel;
    public Spinner citationNumber;
    public TextField citationDate;
    public TextField citationTime;
    public Spinner meterNumber;
    public TextField offenderVehiclePlate;
    public TextField offenderVehicleMake;
    public TextField offenderVehicleModel;
    public ComboBox offenderVehicleType;
    public ComboBox offenderVehicleColor;
    public TextField offenderVehicleOther;
    public TextField citationStreet;
    public TextField citationArea;
    public TextField citationCounty;
    public TextField offenderName;
    public Label offenderAge;
    public ComboBox offenderGender;
    public TextField offenderEthnicity;
    public TextField offenderDescription;
    public TextField officerRank;
    public TextField officerName;
    public TextField officerNumber;
    public TextField officerDivision;
    public TextField officerAgency;
    public TextArea offenderViolations;
    public TextField offenderActionsTaken;
    public TextArea citationComments;
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
            createSpinner(citationNumber, 0, 999, 0);
            createSpinner(meterNumber, 0, 999, 0);
        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered = true;
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

    public void onArrestReportSubmitBtnClick(ActionEvent actionEvent) {
        if (citationNumber.getValue() == null
                /*|| citationDate.getText().isEmpty()
                || citationTime.getText().isEmpty()
                || citationCounty.getText().isEmpty()
                || citationArea.getText().isEmpty()
                || citationStreet.getText().isEmpty()
                || offenderName.getText().isEmpty()*/
                || offenderGender.getValue() == null
                /*|| offenderEthnicity.getText().isEmpty()
                || offenderAge.getText().isEmpty()
                || offenderDescription.getText().isEmpty()
                || offenderVehicleMake.getText().isEmpty()
                || offenderVehicleModel.getText().isEmpty()*/
                || offenderVehicleColor.getValue() == null
                || offenderVehicleType.getValue() == null
                /*|| offenderVehiclePlate.getText().isEmpty()
                || offenderVehicleOther.getText().isEmpty()
                || offenderViolations.getText().isEmpty()
                || offenderActionsTaken.getText().isEmpty()
                || officerRank.getText().isEmpty()
                || officerName.getText().isEmpty()
                || officerNumber.getText().isEmpty()
                || officerDivision.getText().isEmpty()
                || officerAgency.getText().isEmpty()
                || citationComments.getText().isEmpty()*/) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            ParkingCitationReportLogs searchReportLogs = new ParkingCitationReportLogs();
            List<ParkingCitationLogEntry> logs = ParkingCitationReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new ParkingCitationLogEntry(
                    citationNumber.getValue().toString(),
                    citationDate.getText(),
                    citationTime.getText(),
                    meterNumber.getValue().toString(),
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
            ParkingCitationReportLogs.saveLogsToXML(logs);
            // Close the stage
            Stage stage = (Stage) vbox.getScene().getWindow();
            stage.close();
        }
    }
}
