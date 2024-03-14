package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
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

public class ArrestReportController {

    public VBox vbox;
    public Slider arresteeAgeSlider;
    public Spinner arrestNumber;
    public TextField arrestDate;
    public TextField arrestTime;
    public TextField arrestCounty;
    public TextField arrestArea;
    public TextField arrestStreet;
    public TextField arresteeName;
    public Label arresteeAge;
    public ComboBox arresteeGender;
    public TextField arresteeEthnicity;
    public TextField arresteeDescription;
    public TextField arresteeMedicalInformation;
    public TextArea arrestDetails;
    public TextField officerRank;
    public TextField officerName;
    public TextField officerNumber;
    public TextField officerDivision;
    public TextField officerAgency;
    public Label incompleteLabel;
    boolean hasEntered = false;
    private double xOffset = 0;
    private double yOffset = 0;

    public void onMouseEntered(MouseEvent mouseEvent) throws IOException {
        if (hasEntered) {

        } else {
            arresteeAgeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                arresteeAge.setText(String.valueOf(newValue.intValue())); // Update the text of the label
            });

            String name = ConfigReader.configRead("Name");
            String division = ConfigReader.configRead("Division");
            String rank = ConfigReader.configRead("Rank");
            String number = ConfigReader.configRead("Number");
            String agency = ConfigReader.configRead("Agency");
            arresteeGender.getItems().addAll(dropdownInfo.genders);
            officerName.setText(name);
            officerDivision.setText(division);
            officerRank.setText(rank);
            officerAgency.setText(agency);
            officerNumber.setText(number);
            createSpinner(arrestNumber, 0, 999, 0);
            arrestTime.setText(getTime());
            arrestDate.setText(getDate());

        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered = true;
    }

    public void onArrestReportSubmitBtnClick(ActionEvent actionEvent) {

        if (arrestNumber.getValue() == null || arresteeGender.getValue() == null) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            ArrestReportLogs searchReportLogs = new ArrestReportLogs();
            List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new ArrestLogEntry(
                    arrestNumber.getValue().toString(),
                    arrestDate.getText(),
                    arrestTime.getText(),
                    arrestCounty.getText(),
                    arrestArea.getText(),
                    arrestStreet.getText(),
                    arresteeName.getText(),
                    arresteeAge.getText(),
                    arresteeGender.getValue().toString(),
                    arresteeEthnicity.getText(),
                    arresteeDescription.getText(),
                    arresteeMedicalInformation.getText(),
                    arrestDetails.getText(),
                    officerRank.getText(),
                    officerName.getText(),
                    officerNumber.getText(),
                    officerDivision.getText(),
                    officerAgency.getText()
            ));
            // Save logs to XML
            ArrestReportLogs.saveLogsToXML(logs);
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
