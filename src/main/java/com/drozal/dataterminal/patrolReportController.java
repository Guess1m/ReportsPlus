package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Patrol.PatrolLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;

public class patrolReportController {
    public VBox vbox;
    public Spinner patrolNumber;
    public TextField patrolDate;
    public TextField patrolLength;
    public TextField patrolStartTime;
    public TextField patrolStopTime;
    public TextField officerRank;
    public TextField officerName;
    public TextField officerNumber;
    public TextField officerDivision;
    public TextField officerAgency;
    public TextField officerVehicle;
    public TextArea patrolComments;
    public Label incompleteLabel;
    boolean hasEntered = false;
    private double xOffset = 0;
    private double yOffset = 0;

    public void onMouseEntered(MouseEvent mouseEvent) throws IOException {
        if (hasEntered) {
        } else {
            String name = ConfigReader.configRead("Name");
            String division = ConfigReader.configRead("Division");
            String rank = ConfigReader.configRead("Rank");
            String number = ConfigReader.configRead("Number");
            String agency = ConfigReader.configRead("Agency");
            officerName.setText(name);
            officerDivision.setText(division);
            officerRank.setText(rank);
            officerAgency.setText(agency);
            officerNumber.setText(number);
            createSpinner(patrolNumber, 0, 999, 0);
            patrolStopTime.setText(getTime());
            patrolDate.setText(getDate());
        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered = true;
    }

    public void onStopTimeBtnPress(ActionEvent actionEvent) {
        patrolStopTime.setText(getTime());
    }

    public void onArrestReportSubmitBtnClick(ActionEvent actionEvent) {
        if (patrolNumber.getValue() == null
                /*|| patrolDate.getText().isEmpty()
                || patrolLength.getText().isEmpty()
                || patrolStartTime.getText().isEmpty()
                || patrolStopTime.getText().isEmpty()
                || officerRank.getText().isEmpty()
                || officerName.getText().isEmpty()
                || officerNumber.getText().isEmpty()
                || officerDivision.getText().isEmpty()
                || officerAgency.getText().isEmpty()
                || officerVehicle.getText().isEmpty()
                || patrolComments.getText().isEmpty()*/) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            PatrolReportLogs searchReportLogs = new PatrolReportLogs();
            List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new PatrolLogEntry(
                    patrolNumber.getValue().toString(),
                    patrolDate.getText(),
                    patrolLength.getText(),
                    patrolStartTime.getText(),
                    patrolStopTime.getText(),
                    officerRank.getText(),
                    officerName.getText(),
                    officerNumber.getText(),
                    officerDivision.getText(),
                    officerAgency.getText(),
                    officerVehicle.getText(),
                    patrolComments.getText()
            ));
            // Save logs to XML
            PatrolReportLogs.saveLogsToXML(logs);
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
