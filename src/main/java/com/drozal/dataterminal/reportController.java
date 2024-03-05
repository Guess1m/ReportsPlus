package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    public boolean mouseEnteredScreenAlready = false;
    public TextField calloutReportResponseArea;
    public TextField calloutReportResponseCounty;
    public TextField calloutReportResponseAddress;
    public VBox vbox;

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
        // Load existing logs from XML
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

    public void onMouseExit(MouseEvent mouseEvent) {
        mouseEnteredScreenAlready = true;
    }
}
