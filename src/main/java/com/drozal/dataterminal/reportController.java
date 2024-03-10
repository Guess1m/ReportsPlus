package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
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

import static com.drozal.dataterminal.DataTerminalHomeApplication.createSpinner;

public class reportController {
    public Spinner calloutReportSpinner;
    public Button calloutReportSubmitBtn;
    public TextArea calloutReportNotesTextArea;
    public TextField calloutReportResponseGrade;
    public TextField calloutReportResponeType;
    public TextField calloutReportTime;
    public TextField calloutReportDate;
    public TextField calloutReportDivision;
    public TextField calloutReportAgency;
    public TextField calloutReportNumber;
    public TextField calloutReportRank;
    public TextField calloutReportName;
    public TextField calloutReportResponseArea;
    public TextField calloutReportResponseCounty;
    public TextField calloutReportResponseAddress;
    public VBox vbox;
    public boolean mouseEnteredScreenAlready = false;
    public Label incompleteLabel;
    private double xOffset = 0;
    private double yOffset = 0;

    public Spinner getCalloutReportSpinner() {
        return calloutReportSpinner;
    }

    public TextField getCalloutReportResponseGrade() {
        return calloutReportResponseGrade;
    }

    public TextField getCalloutReportResponeType() {
        return calloutReportResponeType;
    }

    public TextField getCalloutReportTime() {
        return calloutReportTime;
    }

    public TextField getCalloutReportDate() {
        return calloutReportDate;
    }

    public TextField getCalloutReportDivision() {
        return calloutReportDivision;
    }

    public TextField getCalloutReportAgency() {
        return calloutReportAgency;
    }

    public TextField getCalloutReportNumber() {
        return calloutReportNumber;
    }

    public TextField getCalloutReportRank() {
        return calloutReportRank;
    }

    public TextField getCalloutReportName() {
        return calloutReportName;
    }

    public void onCalloutReportSubmitBtnClick(ActionEvent actionEvent) {
        if (calloutReportSpinner.getValue() == null
                /*|| calloutReportNotesTextArea.getText().isEmpty()
                || calloutReportResponseGrade.getText().isEmpty()
                || calloutReportResponeType.getText().isEmpty()
                || calloutReportTime.getText().isEmpty()
                || calloutReportDate.getText().isEmpty()
                || calloutReportDivision.getText().isEmpty()
                || calloutReportAgency.getText().isEmpty()
                || calloutReportNumber.getText().isEmpty()
                || calloutReportRank.getText().isEmpty()
                || calloutReportName.getText().isEmpty()
                || calloutReportResponseArea.getText().isEmpty()
                || calloutReportResponseCounty.getText().isEmpty()
                || calloutReportResponseAddress.getText().isEmpty()*/) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {// Load existing logs from XML
            CalloutReportLogs calloutReportLogs = new CalloutReportLogs();
            List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new CalloutLogEntry(
                    calloutReportDate.getText(),
                    calloutReportTime.getText(),
                    calloutReportName.getText(),
                    calloutReportRank.getText(),
                    calloutReportNumber.getText(),
                    calloutReportDivision.getText(),
                    calloutReportAgency.getText(),
                    calloutReportResponeType.getText(),
                    calloutReportResponseGrade.getText(),
                    calloutReportSpinner.getValue().toString(),
                    calloutReportNotesTextArea.getText(),
                    calloutReportResponseAddress.getText(),
                    calloutReportResponseCounty.getText(),
                    calloutReportResponseArea.getText()

            ));
            // Save logs to XML
            CalloutReportLogs.saveLogsToXML(logs);

            Stage stag = (Stage) vbox.getScene().getWindow();
            stag.close();
        }
    }

    public void onMouseEntered(MouseEvent mouseEvent) throws IOException {
        //values set
        if (mouseEnteredScreenAlready) {

        } else {
            //CODE HERE
            createSpinner(calloutReportSpinner, 0, 999, 0);

            String name = ConfigReader.configRead("Name");
            String division = ConfigReader.configRead("Division");
            String rank = ConfigReader.configRead("Rank");
            String number = ConfigReader.configRead("Number");
            String agency = ConfigReader.configRead("Agency");

            calloutReportName.setText(name);
            calloutReportDivision.setText(division);
            calloutReportRank.setText(rank);
            calloutReportAgency.setText(agency);
            calloutReportNumber.setText(number);

            calloutReportDate.setText(DataTerminalHomeApplication.getDate());
            calloutReportTime.setText(DataTerminalHomeApplication.getTime());
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

    public void onMouseExit(MouseEvent mouseEvent) {
        mouseEnteredScreenAlready = true;
    }

    public void onExitButtonClick(MouseEvent actionEvent) {
        // Get the window associated with the scene
        Window window = vbox.getScene().getWindow();

        // Close the window
        window.hide(); // or window.close() if you want to force close
    }
}
