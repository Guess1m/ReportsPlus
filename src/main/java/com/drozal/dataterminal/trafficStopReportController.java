package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopLogEntry;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportLogs;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;

public class trafficStopReportController {
    public ComboBox ResponseColor;
    public ComboBox ReponseType;
    public TextField ReponsePlateNumber;
    public javafx.scene.control.Spinner Spinner;
    public TextField ResponseStreet;
    public TextField ResponseArea;
    public TextField ResponseCounty;
    public TextField Agency;
    public TextField Division;
    public TextField Number;
    public TextField Rank;
    public TextField Name;
    public TextField calloutReportTime;
    public TextField calloutReportDate;
    public TextField actionsTakenNotes;
    public TextArea comments;
    public TextArea violationsNotes;
    public VBox vbox;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean hasEntered = false;

    public ComboBox getReponseType() {
        return ReponseType;
    }

    public ComboBox getResponseColor() {
        return ResponseColor;
    }

    public void onMouseEntered(MouseEvent mouseEvent) throws IOException {
        if (hasEntered) {

        } else {
            String name = ConfigReader.configRead("Name");
            String division = ConfigReader.configRead("Division");
            String rank = ConfigReader.configRead("Rank");
            String number = ConfigReader.configRead("Number");
            String agency = ConfigReader.configRead("Agency");

            getReponseType().getItems().addAll(dropdownInfo.vehicleTypes);
            getResponseColor().getItems().addAll(dropdownInfo.carColors);

            Name.setText(name);
            Division.setText(division);
            Rank.setText(rank);
            Agency.setText(agency);
            Number.setText(number);

            createSpinner(Spinner, 0, 999, 0);

            calloutReportTime.setText(getTime());
            calloutReportDate.setText(getDate());

        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered = true;
    }

    public void onCalloutReportSubmitBtnClick(ActionEvent actionEvent) {
        // Load existing logs from XML
        TrafficStopReportLogs calloutReportLogs = new TrafficStopReportLogs();
        List<TrafficStopLogEntry> logs = TrafficStopReportLogs.loadLogsFromXML();

        // Add new entry
        logs.add(new TrafficStopLogEntry(
                calloutReportDate.getText(),
                calloutReportTime.getText(),
                Name.getText(),
                Rank.getText(),
                Number.getText(),
                Division.getText(),
                Agency.getText(),
                Spinner.getValue().toString(),
                violationsNotes.getText(),
                comments.getText(),
                actionsTakenNotes.getText(),
                ResponseStreet.getText(),
                ResponseCounty.getText(),
                ResponseArea.getText(),
                ReponsePlateNumber.getText(),
                getResponseColor().getValue().toString(),
                getReponseType().getValue().toString()
        ));
        // Save logs to XML
        TrafficStopReportLogs.saveLogsToXML(logs);

        Stage stag = (Stage) vbox.getScene().getWindow();
        stag.close();

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
