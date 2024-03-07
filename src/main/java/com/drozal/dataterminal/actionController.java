package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.Impound.ImpoundLogEntry;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.logs.Incident.IncidentLogEntry;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import com.drozal.dataterminal.logs.ParkingCitation.ParkingCitationLogEntry;
import com.drozal.dataterminal.logs.ParkingCitation.ParkingCitationReportLogs;
import com.drozal.dataterminal.logs.Patrol.PatrolLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import com.drozal.dataterminal.logs.Search.SearchLogEntry;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationLogEntry;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReportLogs;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopLogEntry;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportLogs;
import com.drozal.dataterminal.util.dropdownInfo;
import com.drozal.dataterminal.util.stringUtil;
import jakarta.xml.bind.JAXBException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.createSpinner;

public class actionController {
    public StackPane codesPane;
    public Button notesButton;
    public Button codeButton;
    public StackPane notesPane;
    public Button shiftInfoBtn;
    public StackPane shiftInformationPane;
    public TextField OfficerInfoName;
    public ComboBox OfficerInfoDivision;
    public TextArea ShiftInfoNotesTextArea;
    public ComboBox OfficerInfoAgency;
    public TextField OfficerInfoCallsign;
    public TextField OfficerInfoNumber;
    public ComboBox OfficerInfoRank;
    public Label generatedDateTag;
    public Label generatedByTag;
    public Spinner calloutSpinner;
    public Spinner trafficStopSpinner;
    public Spinner IncidentSpinner;
    public Spinner SearchesSpinner;
    public Spinner ArrestsSpinner;
    public Spinner PatrolsSpinner;
    public StackPane infoPane;
    public TextArea notepadTextArea;
    public Label updatedNotification;
    public StackPane logPane;
    public TableView trafficStopTable;
    public TableView incidentTable;
    public TableView searchTable;
    public TableView arrestTable;
    public TableView patrolTable;
    public TableView citationTable;
    public TableView impoundTable;
    public TableView parkingCitationTable;

    public TableView getCalloutTable() {
        return calloutTable;
    }

    public VBox vbox;
    public TableView calloutTable;
    boolean hasEntered = false;
    private double xOffset = 0;
    private double yOffset = 0;

    public StackPane getLogPane() {
        return logPane;
    }

    public void initialize() {
    }


    public void setDisable(StackPane pane) {
        pane.setVisible(false);
        pane.setDisable(true);
    }

    public void setActive(StackPane pane) {
        pane.setVisible(true);
        pane.setDisable(false);
    }

    public void onCodeButtonClick(ActionEvent actionEvent) {
        setActive(codesPane);
        logPane.setVisible(false);
        logPane.setDisable(true);
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        setDisable(infoPane);

    }

    public void onNotesButtonClicked(ActionEvent actionEvent) {
        setDisable(codesPane);
        setActive(notesPane);
        setDisable(shiftInformationPane);
        setDisable(infoPane);
        logPane.setVisible(false);
        logPane.setDisable(true);

    }

    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
        setDisable(codesPane);
        setDisable(notesPane);
        setActive(shiftInformationPane);
        setDisable(infoPane);
        logPane.setVisible(false);
        logPane.setDisable(true);


    }

    public Spinner getCalloutSpinner() {
        return calloutSpinner;
    }

    public Spinner getArrestsSpinner() {
        return ArrestsSpinner;
    }

    public Spinner getIncidentSpinner() {
        return IncidentSpinner;
    }

    public Spinner getPatrolsSpinner() {
        return PatrolsSpinner;
    }

    public Spinner getSearchesSpinner() {
        return SearchesSpinner;
    }

    public Spinner getTrafficStopSpinner() {
        return trafficStopSpinner;
    }

    public StackPane getCodesPane() {
        return codesPane;
    }

    public ComboBox getOfficerInfoAgency() {
        return OfficerInfoAgency;
    }

    public ComboBox getOfficerInfoDivision() {
        return OfficerInfoDivision;
    }

    public TextField getOfficerInfoName() {
        return OfficerInfoName;
    }

    public TextField getOfficerInfoNumber() {
        return OfficerInfoNumber;
    }

    public ComboBox getOfficerInfoRank() {
        return OfficerInfoRank;
    }

    public Label getGeneratedByTag() {
        return generatedByTag;
    }

    public Label getGeneratedDateTag() {
        return generatedDateTag;
    }

    public StackPane getNotesPane() {
        return notesPane;
    }

    public StackPane getShiftInformationPane() {
        return shiftInformationPane;
    }

    public StackPane getInfoPane() {
        return infoPane;
    }

    public void onCalloutReportButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("calloutReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Callout Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();

    }

    public void onMapButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("map-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Los Santos Map");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.show();
    }

    public void onDataTerminalInfoButtonClick(ActionEvent actionEvent) {
        setDisable(codesPane);
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        setActive(infoPane);
        logPane.setVisible(false);
        logPane.setDisable(true);
    }

    public void onMouseEnter(MouseEvent mouseEvent) throws IOException {
        if (hasEntered) {

        } else {
            String name = ConfigReader.configRead("Name");
            String division = ConfigReader.configRead("Division");
            String rank = ConfigReader.configRead("Rank");
            String number = ConfigReader.configRead("Number");
            String agency = ConfigReader.configRead("Agency");

            getOfficerInfoRank().getItems().addAll(dropdownInfo.ranks);
            getOfficerInfoDivision().getItems().addAll(dropdownInfo.divisions);
            getOfficerInfoAgency().getItems().addAll(dropdownInfo.agencies);

            OfficerInfoName.setText(name);
            OfficerInfoDivision.setValue(division);
            OfficerInfoRank.setValue(rank);
            OfficerInfoAgency.setValue(agency);
            OfficerInfoNumber.setText(number);

            createSpinner(calloutSpinner, 0, 999, 0);
            createSpinner(getArrestsSpinner(), 0, 999, 0);
            createSpinner(getSearchesSpinner(), 0, 999, 0);
            createSpinner(getIncidentSpinner(), 0, 999, 0);
            createSpinner(getPatrolsSpinner(), 0, 999, 0);
            createSpinner(trafficStopSpinner, 0, 999, 0);

            //Top Generations
            generatedByTag.setText("Generated By:" + " " + name);
            String date = DataTerminalHomeApplication.getDate();
            generatedDateTag.setText("Generated On: " + date);

        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered = true;
    }

    public void onclearclick(ActionEvent actionEvent) {
        notepadTextArea.setText("");
    }

    public void oncopyclick(ActionEvent actionEvent) {
        String textToCopy = notepadTextArea.getText();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(textToCopy);
        Clipboard clipboard = Clipboard.getSystemClipboard();
        clipboard.setContent(clipboardContent);
    }

    public void onpasteclick(ActionEvent actionEvent) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            String clipboardContent = clipboard.getString();
            notepadTextArea.appendText(clipboardContent);
        }
    }

    public void updateInfoButtonClick(ActionEvent actionEvent) {
        if (getOfficerInfoAgency().getValue() == null || getOfficerInfoDivision().getValue() == null ||
                getOfficerInfoRank().getValue() == null || getOfficerInfoName().getText().isEmpty() ||
                getOfficerInfoNumber().getText().isEmpty()) {
            System.out.println("Some fields are empty");
            updatedNotification.setText("Fill Out Form.");
            updatedNotification.setStyle("-fx-text-fill: red;");
            updatedNotification.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline1.play();
        } else {
            ConfigWriter.configwrite("Agency", getOfficerInfoAgency().getValue().toString());
            ConfigWriter.configwrite("Division", getOfficerInfoDivision().getValue().toString());
            ConfigWriter.configwrite("Name", getOfficerInfoName().getText());
            ConfigWriter.configwrite("Rank", getOfficerInfoRank().getValue().toString());
            ConfigWriter.configwrite("Number", getOfficerInfoNumber().getText());
            generatedByTag.setText("Generated By:" + " " + getOfficerInfoName().getText());
            updatedNotification.setText("updated.");
            updatedNotification.setStyle("-fx-text-fill: green;");
            updatedNotification.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline.play();
        }
    }

    public void onExitButtonClick(MouseEvent actionEvent) {
        Platform.exit();
    }

    public void onLogsButtonClick(ActionEvent actionEvent) throws IOException, JAXBException, ParserConfigurationException, TransformerException {
        setDisable(codesPane);
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        setDisable(infoPane);
        logPane.setVisible(true);
        logPane.setDisable(false);

        /*Parking CITATION*//*
        if (Files.exists(Paths.get(stringUtil.parkingCitationLogURL))) {
            // Extract log entries from XML file
            List<ParkingCitationLogEntry> logEntries8 = ParkingCitationReportLogs.extractLogEntries(stringUtil.parkingCitationLogURL);
            // Add log entries to the GridPane
            int row = 1;
            for (ParkingCitationLogEntry logEntry : logEntries8) {
                parkingCitationGrid.add(new Label(logEntry.citationNumber), 0, row);
                parkingCitationGrid.add(new Label(logEntry.citationDate), 1, row);
                parkingCitationGrid.add(new Label(logEntry.citationTime), 2, row);
                parkingCitationGrid.add(new Label(logEntry.meterNumber), 3, row);
                parkingCitationGrid.add(new Label(logEntry.citationCounty), 4, row);
                parkingCitationGrid.add(new Label(logEntry.citationArea), 5, row);
                parkingCitationGrid.add(new Label(logEntry.citationStreet), 6, row);
                parkingCitationGrid.add(new Label(logEntry.offenderName), 7, row);
                parkingCitationGrid.add(new Label(logEntry.offenderGender), 8, row);
                parkingCitationGrid.add(new Label(logEntry.offenderEthnicity), 9, row);
                parkingCitationGrid.add(new Label(logEntry.offenderAge), 10, row);
                parkingCitationGrid.add(new Label(logEntry.offenderDescription), 11, row);
                parkingCitationGrid.add(new Label(logEntry.offenderVehicleMake), 12, row);
                parkingCitationGrid.add(new Label(logEntry.offenderVehicleModel), 13, row);
                parkingCitationGrid.add(new Label(logEntry.offenderVehicleColor), 14, row);
                parkingCitationGrid.add(new Label(logEntry.offenderVehicleType), 15, row);
                parkingCitationGrid.add(new Label(logEntry.offenderVehiclePlate), 16, row);
                parkingCitationGrid.add(new Label(logEntry.offenderVehicleOther), 17, row);
                parkingCitationGrid.add(new Label(logEntry.offenderViolations), 18, row);
                parkingCitationGrid.add(new Label(logEntry.offenderActionsTaken), 19, row);
                parkingCitationGrid.add(new Label(logEntry.officerRank), 20, row);
                parkingCitationGrid.add(new Label(logEntry.officerName), 21, row);
                parkingCitationGrid.add(new Label(logEntry.officerNumber), 22, row);
                parkingCitationGrid.add(new Label(logEntry.officerDivision), 23, row);
                parkingCitationGrid.add(new Label(logEntry.officerAgency), 24, row);
                parkingCitationGrid.add(new Label(logEntry.citationComments), 25, row);

                row++;
            }
        } else {
            //System.out.println("File does not exist.");
        }
        // Add ColumnConstraints for all columns
        ColumnConstraints columnConstraints8 = new ColumnConstraints();
        columnConstraints8.setHgrow(Priority.ALWAYS);
        columnConstraints8.setFillWidth(true);
        parkingCitationGrid.getColumnConstraints().add(columnConstraints8);
        parkingCitationGrid.add(new Label("Citation #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 0, 0);
        parkingCitationGrid.add(new Label("Date: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 1, 0);
        parkingCitationGrid.add(new Label("Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 2, 0);
        parkingCitationGrid.add(new Label("Meter #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 3, 0);
        parkingCitationGrid.add(new Label("County: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 4, 0);
        parkingCitationGrid.add(new Label("Area: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 5, 0);
        parkingCitationGrid.add(new Label("Street: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 6, 0);
        parkingCitationGrid.add(new Label("Offender Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 7, 0);
        parkingCitationGrid.add(new Label("Offender Gender: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 8, 0);
        parkingCitationGrid.add(new Label("Offender Ethnicity: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 9, 0);
        parkingCitationGrid.add(new Label("Offender Age: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 10, 0);
        parkingCitationGrid.add(new Label("Offender Description: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 11, 0);
        parkingCitationGrid.add(new Label("Make: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 12, 0);
        parkingCitationGrid.add(new Label("Model: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 13, 0);
        parkingCitationGrid.add(new Label("Color: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 14, 0);
        parkingCitationGrid.add(new Label("Type: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 15, 0);
        parkingCitationGrid.add(new Label("Plate: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 16, 0);
        parkingCitationGrid.add(new Label("Other: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 17, 0);
        parkingCitationGrid.add(new Label("Violations: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 18, 0);
        parkingCitationGrid.add(new Label("Actions Taken: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 19, 0);
        parkingCitationGrid.add(new Label("Officer Rank: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 20, 0);
        parkingCitationGrid.add(new Label("Officer Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 21, 0);
        parkingCitationGrid.add(new Label("Officer Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 22, 0);
        parkingCitationGrid.add(new Label("Officer Division: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 23, 0);
        parkingCitationGrid.add(new Label("Officer Agency: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 24, 0);
        parkingCitationGrid.add(new Label("Comments: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 25, 0);
        // Set row constraints to apply to all rows
        RowConstraints rowConstraints8 = new RowConstraints();
        rowConstraints8.setVgrow(Priority.ALWAYS); // Allow vertical growth
        rowConstraints8.setMinHeight(100); // Set the minimum height
        rowConstraints8.setPrefHeight(100); // Set the preferred height
        rowConstraints8.setMaxHeight(100); // Set the maximum height
        // Add row constraints to all rows in the GridPane
        int numRows8 = parkingCitationGrid.getRowConstraints().size();
        for (int i = 0; i < numRows8; i++) {
            parkingCitationGrid.getRowConstraints().add(rowConstraints8);
        }

        *//*Impound *//*
        if (Files.exists(Paths.get(stringUtil.impoundLogURL))) {
            // Extract log entries from XML file
            List<ImpoundLogEntry> logEntries7 = ImpoundReportLogs.extractLogEntries(stringUtil.impoundLogURL);
            // Add log entries to the GridPane
            int row = 1;
            for (ImpoundLogEntry logEntry : logEntries7) {
                impoundGrid.add(new Label(logEntry.impoundNumber), 0, row);
                impoundGrid.add(new Label(logEntry.impoundDate), 1, row);
                impoundGrid.add(new Label(logEntry.impoundTime), 2, row);
                impoundGrid.add(new Label(logEntry.ownerName), 3, row);
                impoundGrid.add(new Label(logEntry.ownerAge), 4, row);
                impoundGrid.add(new Label(logEntry.ownerGender), 5, row);
                impoundGrid.add(new Label(logEntry.ownerAddress), 6, row);
                impoundGrid.add(new Label(logEntry.impoundPlateNumber), 7, row);
                impoundGrid.add(new Label(logEntry.impoundMake), 8, row);
                impoundGrid.add(new Label(logEntry.impoundModel), 9, row);
                impoundGrid.add(new Label(logEntry.impoundType), 10, row);
                impoundGrid.add(new Label(logEntry.impoundColor), 11, row);
                impoundGrid.add(new Label(logEntry.impoundComments), 12, row);
                impoundGrid.add(new Label(logEntry.officerRank), 13, row);
                impoundGrid.add(new Label(logEntry.officerName), 14, row);
                impoundGrid.add(new Label(logEntry.officerNumber), 15, row);
                impoundGrid.add(new Label(logEntry.officerDivision), 16, row);
                impoundGrid.add(new Label(logEntry.officerAgency), 17, row);

                row++;
            }
        } else {
            //System.out.println("File does not exist.");
        }
        // Add ColumnConstraints for all columns
        ColumnConstraints columnConstraints7 = new ColumnConstraints();
        columnConstraints7.setHgrow(Priority.ALWAYS);
        columnConstraints7.setFillWidth(true);
        impoundGrid.getColumnConstraints().add(columnConstraints7);
        impoundGrid.add(new Label("Impound #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 0, 0);
        impoundGrid.add(new Label("Date: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 1, 0);
        impoundGrid.add(new Label("Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 2, 0);
        impoundGrid.add(new Label("Owner Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 3, 0);
        impoundGrid.add(new Label("Owner Age: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 4, 0);
        impoundGrid.add(new Label("Owner Gender: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 5, 0);
        impoundGrid.add(new Label("Owner Address: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 6, 0);
        impoundGrid.add(new Label("Plate Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 7, 0);
        impoundGrid.add(new Label("Make: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 8, 0);
        impoundGrid.add(new Label("Model: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 9, 0);
        impoundGrid.add(new Label("Type: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 10, 0);
        impoundGrid.add(new Label("Color: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 11, 0);
        impoundGrid.add(new Label("Comments: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 12, 0);
        impoundGrid.add(new Label("Officer Rank: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 13, 0);
        impoundGrid.add(new Label("Officer Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 14, 0);
        impoundGrid.add(new Label("Officer Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 15, 0);
        impoundGrid.add(new Label("Officer Division: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 16, 0);
        impoundGrid.add(new Label("Officer Agency: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 17, 0);
        // Set row constraints to apply to all rows
        RowConstraints rowConstraints7 = new RowConstraints();
        rowConstraints7.setVgrow(Priority.ALWAYS); // Allow vertical growth
        rowConstraints7.setMinHeight(100); // Set the minimum height
        rowConstraints7.setPrefHeight(100); // Set the preferred height
        rowConstraints7.setMaxHeight(100); // Set the maximum height
        // Add row constraints to all rows in the GridPane
        int numRows7 = impoundGrid.getRowConstraints().size();
        for (int i = 0; i < numRows7; i++) {
            impoundGrid.getRowConstraints().add(rowConstraints7);
        }

        *//*TRAFFIC CITATION*//*
        if (Files.exists(Paths.get(stringUtil.trafficCitationLogURL))) {
            // Extract log entries from XML file
            List<TrafficCitationLogEntry> logEntries6 = TrafficCitationReportLogs.extractLogEntries(stringUtil.trafficCitationLogURL);
            // Add log entries to the GridPane
            int row = 1;
            for (TrafficCitationLogEntry logEntry : logEntries6) {
                citationGrid.add(new Label(logEntry.citationNumber), 0, row);
                citationGrid.add(new Label(logEntry.citationDate), 1, row);
                citationGrid.add(new Label(logEntry.citationTime), 2, row);
                citationGrid.add(new Label(logEntry.citationCounty), 3, row);
                citationGrid.add(new Label(logEntry.citationArea), 4, row);
                citationGrid.add(new Label(logEntry.citationStreet), 5, row);
                citationGrid.add(new Label(logEntry.offenderName), 6, row);
                citationGrid.add(new Label(logEntry.offenderGender), 7, row);
                citationGrid.add(new Label(logEntry.offenderEthnicity), 8, row);
                citationGrid.add(new Label(logEntry.offenderAge), 9, row);
                citationGrid.add(new Label(logEntry.offenderDescription), 10, row);
                citationGrid.add(new Label(logEntry.offenderVehicleMake), 11, row);
                citationGrid.add(new Label(logEntry.offenderVehicleModel), 12, row);
                citationGrid.add(new Label(logEntry.offenderVehicleColor), 13, row);
                citationGrid.add(new Label(logEntry.offenderVehicleType), 14, row);
                citationGrid.add(new Label(logEntry.offenderVehiclePlate), 15, row);
                citationGrid.add(new Label(logEntry.offenderVehicleOther), 16, row);
                citationGrid.add(new Label(logEntry.offenderViolations), 17, row);
                citationGrid.add(new Label(logEntry.offenderActionsTaken), 18, row);
                citationGrid.add(new Label(logEntry.officerRank), 19, row);
                citationGrid.add(new Label(logEntry.officerName), 20, row);
                citationGrid.add(new Label(logEntry.officerNumber), 21, row);
                citationGrid.add(new Label(logEntry.officerDivision), 22, row);
                citationGrid.add(new Label(logEntry.officerAgency), 23, row);
                citationGrid.add(new Label(logEntry.citationComments), 24, row);


                row++;
            }
        } else {
            //System.out.println("File does not exist.");
        }
        // Add ColumnConstraints for all columns
        ColumnConstraints columnConstraints6 = new ColumnConstraints();
        columnConstraints6.setHgrow(Priority.ALWAYS);
        columnConstraints6.setFillWidth(true);
        citationGrid.getColumnConstraints().add(columnConstraints6);
        citationGrid.add(new Label("Citation #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 0, 0);
        citationGrid.add(new Label("Date: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 1, 0);
        citationGrid.add(new Label("Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 2, 0);
        citationGrid.add(new Label("County: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 3, 0);
        citationGrid.add(new Label("Area: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 4, 0);
        citationGrid.add(new Label("Street: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 5, 0);
        citationGrid.add(new Label("Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 6, 0);
        citationGrid.add(new Label("Gender: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 7, 0);
        citationGrid.add(new Label("Ethnicity: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 8, 0);
        citationGrid.add(new Label("Age: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 9, 0);
        citationGrid.add(new Label("Description: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 10, 0);
        citationGrid.add(new Label("Vehicle Make: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 11, 0);
        citationGrid.add(new Label("Vehicle Model: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 12, 0);
        citationGrid.add(new Label("Vehicle Color: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 13, 0);
        citationGrid.add(new Label("Vehicle Type: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 14, 0);
        citationGrid.add(new Label("Vehicle Plate: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 15, 0);
        citationGrid.add(new Label("Vehicle Other: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 16, 0);
        citationGrid.add(new Label("Violations: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 17, 0);
        citationGrid.add(new Label("Actions Taken: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 18, 0);
        citationGrid.add(new Label("Officer Rank: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 19, 0);
        citationGrid.add(new Label("Officer Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 20, 0);
        citationGrid.add(new Label("Officer Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 21, 0);
        citationGrid.add(new Label("Officer Division: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 22, 0);
        citationGrid.add(new Label("Officer Agency: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 23, 0);
        citationGrid.add(new Label("Comments: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 24, 0);
        // Set row constraints to apply to all rows
        RowConstraints rowConstraints6 = new RowConstraints();
        rowConstraints6.setVgrow(Priority.ALWAYS); // Allow vertical growth
        rowConstraints6.setMinHeight(100); // Set the minimum height
        rowConstraints6.setPrefHeight(100); // Set the preferred height
        rowConstraints6.setMaxHeight(100); // Set the maximum height
        // Add row constraints to all rows in the GridPane
        int numRows6 = citationGrid.getRowConstraints().size();
        for (int i = 0; i < numRows6; i++) {
            citationGrid.getRowConstraints().add(rowConstraints6);
        }

        // Check if the file exists before extracting log entries
        if (Files.exists(Paths.get(stringUtil.patrolLogURL))) {
            // Extract log entries from XML file
            List<PatrolLogEntry> logEntries5 = PatrolReportLogs.extractLogEntries(stringUtil.patrolLogURL);
            // Add log entries to the GridPane
            int row = 1;
            for (PatrolLogEntry logEntry : logEntries5) {
                patrolGrid.add(new Label(logEntry.patrolNumber), 0, row);
                patrolGrid.add(new Label(logEntry.patrolDate), 1, row);
                patrolGrid.add(new Label(logEntry.patrolLength), 2, row);
                patrolGrid.add(new Label(logEntry.patrolStartTime), 3, row);
                patrolGrid.add(new Label(logEntry.patrolStopTime), 4, row);
                patrolGrid.add(new Label(logEntry.officerRank), 5, row);
                patrolGrid.add(new Label(logEntry.officerName), 6, row);
                patrolGrid.add(new Label(logEntry.officerNumber), 7, row);
                patrolGrid.add(new Label(logEntry.officerDivision), 8, row);
                patrolGrid.add(new Label(logEntry.officerAgency), 9, row);
                patrolGrid.add(new Label(logEntry.officerVehicle), 10, row);
                patrolGrid.add(new Label(logEntry.patrolComments), 11, row);

                row++;
            }
        } else {
            //System.out.println("File does not exist.");
        }
        // Add ColumnConstraints for all columns
        ColumnConstraints columnConstraints5 = new ColumnConstraints();
        columnConstraints5.setHgrow(Priority.ALWAYS);
        columnConstraints5.setFillWidth(true);
        patrolGrid.getColumnConstraints().add(columnConstraints5);
        patrolGrid.add(new Label("Patrol #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 0, 0);
        patrolGrid.add(new Label("Date: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 1, 0);
        patrolGrid.add(new Label("Length: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 2, 0);
        patrolGrid.add(new Label("Start Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 3, 0);
        patrolGrid.add(new Label("Stop Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 4, 0);
        patrolGrid.add(new Label("Rank: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 5, 0);
        patrolGrid.add(new Label("Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 6, 0);
        patrolGrid.add(new Label("Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 7, 0);
        patrolGrid.add(new Label("Division: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 8, 0);
        patrolGrid.add(new Label("Agency: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 9, 0);
        patrolGrid.add(new Label("Vehicle: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 10, 0);
        patrolGrid.add(new Label("Comments: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 11, 0);
        // Set row constraints to apply to all rows
        RowConstraints rowConstraints5 = new RowConstraints();
        rowConstraints5.setVgrow(Priority.ALWAYS); // Allow vertical growth
        rowConstraints5.setMinHeight(100); // Set the minimum height
        rowConstraints5.setPrefHeight(100); // Set the preferred height
        rowConstraints5.setMaxHeight(100); // Set the maximum height
        // Add row constraints to all rows in the GridPane
        int numRows5 = patrolGrid.getRowConstraints().size();
        for (int i = 0; i < numRows5; i++) {
            patrolGrid.getRowConstraints().add(rowConstraints5);
        }


        // Check if the file exists before extracting log entries
        if (Files.exists(Paths.get(stringUtil.searchLogURL))) {
            // Extract log entries from XML file
            List<ArrestLogEntry> logEntries4 = ArrestReportLogs.extractLogEntries(stringUtil.arrestLogURL);
            // Add log entries to the GridPane
            int row = 1;
            for (ArrestLogEntry logEntry : logEntries4) {
                arrestGrid.add(new Label(logEntry.arrestNumber), 0, row);
                arrestGrid.add(new Label(logEntry.arrestDate), 1, row);
                arrestGrid.add(new Label(logEntry.arrestTime), 2, row);
                arrestGrid.add(new Label(logEntry.arrestCounty), 3, row);
                arrestGrid.add(new Label(logEntry.arrestArea), 4, row);
                arrestGrid.add(new Label(logEntry.arrestStreet), 5, row);
                arrestGrid.add(new Label(logEntry.arresteeName), 6, row);
                arrestGrid.add(new Label(logEntry.arresteeAge), 7, row);
                arrestGrid.add(new Label(logEntry.arresteeGender), 8, row);
                arrestGrid.add(new Label(logEntry.arresteeEthnicity), 9, row);
                arrestGrid.add(new Label(logEntry.arresteeDescription), 10, row);
                arrestGrid.add(new Label(logEntry.arresteeMedicalInformation), 11, row);
                arrestGrid.add(new Label(logEntry.arrestDetails), 12, row);
                arrestGrid.add(new Label(logEntry.officerRank), 13, row);
                arrestGrid.add(new Label(logEntry.officerName), 14, row);
                arrestGrid.add(new Label(logEntry.officerNumber), 15, row);
                arrestGrid.add(new Label(logEntry.officerAgency), 16, row);
                arrestGrid.add(new Label(logEntry.officerDivision), 17, row);
                row++;
            }
        } else {
            //System.out.println("File does not exist.");
        }
        // Add ColumnConstraints for all columns
        ColumnConstraints columnConstraints4 = new ColumnConstraints();
        columnConstraints4.setHgrow(Priority.ALWAYS);
        columnConstraints4.setFillWidth(true);
        arrestGrid.getColumnConstraints().add(columnConstraints4);
        arrestGrid.add(new Label("Arrest #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 0, 0);
        arrestGrid.add(new Label("Date: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 1, 0);
        arrestGrid.add(new Label("Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 2, 0);
        arrestGrid.add(new Label("County: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 3, 0);
        arrestGrid.add(new Label("Area: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 4, 0);
        arrestGrid.add(new Label("Street: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 5, 0);
        arrestGrid.add(new Label("Arrestee Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 6, 0);
        arrestGrid.add(new Label("Arrestee Age: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 7, 0);
        arrestGrid.add(new Label("Arrestee Gender: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 8, 0);
        arrestGrid.add(new Label("Arrestee Ethnicity: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 9, 0);
        arrestGrid.add(new Label("Arrestee Description: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 10, 0);
        arrestGrid.add(new Label("Arrestee Medical Information: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 11, 0);
        arrestGrid.add(new Label("Arrest Details: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 12, 0);
        arrestGrid.add(new Label("Officer Rank: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 13, 0);
        arrestGrid.add(new Label("Officer Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 14, 0);
        arrestGrid.add(new Label("Officer Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 15, 0);
        arrestGrid.add(new Label("Officer Agency: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 16, 0);
        arrestGrid.add(new Label("Officer Division: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 17, 0);
        // Set row constraints to apply to all rows
        RowConstraints rowConstraints4 = new RowConstraints();
        rowConstraints4.setVgrow(Priority.ALWAYS); // Allow vertical growth
        rowConstraints4.setMinHeight(100); // Set the minimum height
        rowConstraints4.setPrefHeight(100); // Set the preferred height
        rowConstraints4.setMaxHeight(100); // Set the maximum height
        // Add row constraints to all rows in the GridPane
        int numRows4 = arrestGrid.getRowConstraints().size();
        for (int i = 0; i < numRows4; i++) {
            arrestGrid.getRowConstraints().add(rowConstraints4);
        }


        // Check if the file exists before extracting log entries
        if (Files.exists(Paths.get(stringUtil.searchLogURL))) {
            // Extract log entries from XML file
            List<SearchLogEntry> logEntries3 = SearchReportLogs.extractLogEntries(stringUtil.searchLogURL);
            // Add log entries to the GridPane
            int row = 1;
            for (SearchLogEntry logEntry : logEntries3) {
                searchGrid.add(new Label(logEntry.SearchNumber), 0, row);
                searchGrid.add(new Label(logEntry.searchedPersons), 1, row);
                searchGrid.add(new Label(logEntry.searchDate), 2, row);
                searchGrid.add(new Label(logEntry.searchTime), 3, row);
                searchGrid.add(new Label(logEntry.searchSeizedItems), 4, row);
                searchGrid.add(new Label(logEntry.searchGrounds), 5, row);
                searchGrid.add(new Label(logEntry.searchType), 6, row);
                searchGrid.add(new Label(logEntry.searchMethod), 7, row);
                searchGrid.add(new Label(logEntry.searchWitnesses), 8, row);
                searchGrid.add(new Label(logEntry.officerRank), 9, row);
                searchGrid.add(new Label(logEntry.officerName), 10, row);
                searchGrid.add(new Label(logEntry.officerNumber), 11, row);
                searchGrid.add(new Label(logEntry.officerAgency), 12, row);
                searchGrid.add(new Label(logEntry.officerDivision), 13, row);
                searchGrid.add(new Label(logEntry.searchStreet), 14, row);
                searchGrid.add(new Label(logEntry.searchArea), 15, row);
                searchGrid.add(new Label(logEntry.searchCounty), 16, row);
                searchGrid.add(new Label(logEntry.searchComments), 17, row);
                row++;
            }
        } else {
            //System.out.println("File does not exist.");
        }
        // Add ColumnConstraints for all columns
        ColumnConstraints columnConstraints3 = new ColumnConstraints();
        columnConstraints3.setHgrow(Priority.ALWAYS);
        columnConstraints3.setFillWidth(true);
        searchGrid.getColumnConstraints().add(columnConstraints3);
        searchGrid.add(new Label("Search #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 0, 0);
        searchGrid.add(new Label("Searched Person(s): ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 1, 0);
        searchGrid.add(new Label("Date: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 2, 0);
        searchGrid.add(new Label("Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 3, 0);
        searchGrid.add(new Label("Seized Items: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 4, 0);
        searchGrid.add(new Label("Grounds: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 5, 0);
        searchGrid.add(new Label("Type: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 6, 0);
        searchGrid.add(new Label("Method: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 7, 0);
        searchGrid.add(new Label("Witnesses: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 8, 0);
        searchGrid.add(new Label("Rank: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 9, 0);
        searchGrid.add(new Label("Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 10, 0);
        searchGrid.add(new Label("Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 11, 0);
        searchGrid.add(new Label("Agency: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 12, 0);
        searchGrid.add(new Label("Division: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 13, 0);
        searchGrid.add(new Label("Street: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 14, 0);
        searchGrid.add(new Label("Area: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 15, 0);
        searchGrid.add(new Label("County: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 16, 0);
        searchGrid.add(new Label("Comments: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 17, 0);
        // Set row constraints to apply to all rows
        RowConstraints rowConstraints3 = new RowConstraints();
        rowConstraints3.setVgrow(Priority.ALWAYS); // Allow vertical growth
        rowConstraints3.setMinHeight(100); // Set the minimum height
        rowConstraints3.setPrefHeight(100); // Set the preferred height
        rowConstraints3.setMaxHeight(100); // Set the maximum height
        // Add row constraints to all rows in the GridPane
        int numRows3 = searchGrid.getRowConstraints().size();
        for (int i = 0; i < numRows3; i++) {
            searchGrid.getRowConstraints().add(rowConstraints3);
        }


        // Check if the file exists before extracting log entries
        if (Files.exists(Paths.get(stringUtil.incidentLogURL))) {
            // Extract log entries from XML file
            List<IncidentLogEntry> logEntries2 = IncidentReportLogs.extractLogEntries(stringUtil.incidentLogURL);

            // Add log entries to the GridPane
            int row = 1;
            for (IncidentLogEntry logEntry : logEntries2) {
                incidentGrid.add(new Label(logEntry.incidentNumber), 0, row);
                incidentGrid.add(new Label(logEntry.incidentDate), 1, row);
                incidentGrid.add(new Label(logEntry.incidentTime), 2, row);
                incidentGrid.add(new Label(logEntry.incidentStatement), 3, row);
                incidentGrid.add(new Label(logEntry.incidentWitnesses), 4, row);
                incidentGrid.add(new Label(logEntry.incidentVictims), 5, row);
                incidentGrid.add(new Label(logEntry.officerName), 6, row);
                incidentGrid.add(new Label(logEntry.officerRank), 7, row);
                incidentGrid.add(new Label(logEntry.officerNumber), 8, row);
                incidentGrid.add(new Label(logEntry.officerAgency), 9, row);
                incidentGrid.add(new Label(logEntry.officerDivision), 10, row);
                incidentGrid.add(new Label(logEntry.incidentStreet), 11, row);
                incidentGrid.add(new Label(logEntry.incidentArea), 12, row);
                incidentGrid.add(new Label(logEntry.incidentCounty), 13, row);
                incidentGrid.add(new Label(logEntry.incidentActionsTaken), 14, row);
                incidentGrid.add(new Label(logEntry.incidentComments), 15, row);

                row++;
            }
        } else {
            //System.out.println("File does not exist.");
        }
        // Add ColumnConstraints for all columns
        ColumnConstraints columnConstraints2 = new ColumnConstraints();
        columnConstraints2.setHgrow(Priority.ALWAYS);
        columnConstraints2.setFillWidth(true);
        incidentGrid.getColumnConstraints().add(columnConstraints2);
        incidentGrid.add(new Label("Incident #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 0, 0);
        incidentGrid.add(new Label("Date: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 1, 0);
        incidentGrid.add(new Label("Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 2, 0);
        incidentGrid.add(new Label("Statement: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 3, 0);
        incidentGrid.add(new Label("Witnesses: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 4, 0);
        incidentGrid.add(new Label("Victims: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 5, 0);
        incidentGrid.add(new Label("Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 6, 0);
        incidentGrid.add(new Label("Rank: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 7, 0);
        incidentGrid.add(new Label("Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 8, 0);
        incidentGrid.add(new Label("Agency: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 9, 0);
        incidentGrid.add(new Label("Division: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 10, 0);
        incidentGrid.add(new Label("Street: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 11, 0);
        incidentGrid.add(new Label("Area: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 12, 0);
        incidentGrid.add(new Label("County: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 13, 0);
        incidentGrid.add(new Label("Actions Taken: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 14, 0);
        incidentGrid.add(new Label("Comments: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 15, 0);

        // Set row constraints to apply to all rows
        RowConstraints rowConstraints2 = new RowConstraints();
        rowConstraints2.setVgrow(Priority.ALWAYS); // Allow vertical growth
        rowConstraints2.setMinHeight(100); // Set the minimum height
        rowConstraints2.setPrefHeight(100); // Set the preferred height
        rowConstraints2.setMaxHeight(100); // Set the maximum height

        // Add row constraints to all rows in the GridPane
        int numRows2 = incidentGrid.getRowConstraints().size();
        for (int i = 0; i < numRows2; i++) {
            incidentGrid.getRowConstraints().add(rowConstraints2);
        }*/

        //IncidentTable
        List<IncidentLogEntry> incidentLogEntryList = IncidentReportLogs.extractLogEntries(stringUtil.incidentLogURL);
        incidentLogUpdate(incidentLogEntryList);
        //TrafficStopTable
        List<TrafficStopLogEntry> trafficLogEntryList = TrafficStopReportLogs.extractLogEntries(stringUtil.trafficstopLogURL);
        trafficStopLogUpdate(trafficLogEntryList);
        //CalloutTable
        List<CalloutLogEntry> calloutLogEntryList = CalloutReportLogs.extractLogEntries(stringUtil.calloutLogURL);
        calloutLogUpdate(calloutLogEntryList);
    }

    public void parkingCitationLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        // Create columns for each property of CalloutLogEntry
        TableColumn<CalloutLogEntry, String> calloutNumberColumn = new TableColumn<>("Callout #");
        calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));

        TableColumn<CalloutLogEntry, String> notesTextAreaColumn = new TableColumn<>("Notes");
        notesTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("NotesTextArea"));

        TableColumn<CalloutLogEntry, String> responseGradeColumn = new TableColumn<>("Grade");
        responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));

        TableColumn<CalloutLogEntry, String> responseTypeColumn = new TableColumn<>("Type");
        responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponeType"));

        TableColumn<CalloutLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<CalloutLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<CalloutLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<CalloutLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<CalloutLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<CalloutLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<CalloutLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<CalloutLogEntry, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

        TableColumn<CalloutLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<CalloutLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        calloutTable.getColumns().addAll(
                calloutNumberColumn,
                notesTextAreaColumn,
                responseGradeColumn,
                responseTypeColumn,
                timeColumn,
                dateColumn,
                divisionColumn,
                agencyColumn,
                numberColumn,
                rankColumn,
                nameColumn,
                addressColumn,
                countyColumn,
                areaColumn
        );
        calloutTable.getItems().addAll(logEntries);
    }
    public void impoundLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        // Create columns for each property of CalloutLogEntry
        TableColumn<CalloutLogEntry, String> calloutNumberColumn = new TableColumn<>("Callout #");
        calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));

        TableColumn<CalloutLogEntry, String> notesTextAreaColumn = new TableColumn<>("Notes");
        notesTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("NotesTextArea"));

        TableColumn<CalloutLogEntry, String> responseGradeColumn = new TableColumn<>("Grade");
        responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));

        TableColumn<CalloutLogEntry, String> responseTypeColumn = new TableColumn<>("Type");
        responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponeType"));

        TableColumn<CalloutLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<CalloutLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<CalloutLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<CalloutLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<CalloutLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<CalloutLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<CalloutLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<CalloutLogEntry, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

        TableColumn<CalloutLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<CalloutLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        calloutTable.getColumns().addAll(
                calloutNumberColumn,
                notesTextAreaColumn,
                responseGradeColumn,
                responseTypeColumn,
                timeColumn,
                dateColumn,
                divisionColumn,
                agencyColumn,
                numberColumn,
                rankColumn,
                nameColumn,
                addressColumn,
                countyColumn,
                areaColumn
        );
        calloutTable.getItems().addAll(logEntries);
    }
    public void citationLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        // Create columns for each property of CalloutLogEntry
        TableColumn<CalloutLogEntry, String> calloutNumberColumn = new TableColumn<>("Callout #");
        calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));

        TableColumn<CalloutLogEntry, String> notesTextAreaColumn = new TableColumn<>("Notes");
        notesTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("NotesTextArea"));

        TableColumn<CalloutLogEntry, String> responseGradeColumn = new TableColumn<>("Grade");
        responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));

        TableColumn<CalloutLogEntry, String> responseTypeColumn = new TableColumn<>("Type");
        responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponeType"));

        TableColumn<CalloutLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<CalloutLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<CalloutLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<CalloutLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<CalloutLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<CalloutLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<CalloutLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<CalloutLogEntry, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

        TableColumn<CalloutLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<CalloutLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        calloutTable.getColumns().addAll(
                calloutNumberColumn,
                notesTextAreaColumn,
                responseGradeColumn,
                responseTypeColumn,
                timeColumn,
                dateColumn,
                divisionColumn,
                agencyColumn,
                numberColumn,
                rankColumn,
                nameColumn,
                addressColumn,
                countyColumn,
                areaColumn
        );
        calloutTable.getItems().addAll(logEntries);
    }
    public void patrolLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        // Create columns for each property of CalloutLogEntry
        TableColumn<CalloutLogEntry, String> calloutNumberColumn = new TableColumn<>("Callout #");
        calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));

        TableColumn<CalloutLogEntry, String> notesTextAreaColumn = new TableColumn<>("Notes");
        notesTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("NotesTextArea"));

        TableColumn<CalloutLogEntry, String> responseGradeColumn = new TableColumn<>("Grade");
        responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));

        TableColumn<CalloutLogEntry, String> responseTypeColumn = new TableColumn<>("Type");
        responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponeType"));

        TableColumn<CalloutLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<CalloutLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<CalloutLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<CalloutLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<CalloutLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<CalloutLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<CalloutLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<CalloutLogEntry, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

        TableColumn<CalloutLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<CalloutLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        calloutTable.getColumns().addAll(
                calloutNumberColumn,
                notesTextAreaColumn,
                responseGradeColumn,
                responseTypeColumn,
                timeColumn,
                dateColumn,
                divisionColumn,
                agencyColumn,
                numberColumn,
                rankColumn,
                nameColumn,
                addressColumn,
                countyColumn,
                areaColumn
        );
        calloutTable.getItems().addAll(logEntries);
    }
    public void arrestLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        // Create columns for each property of CalloutLogEntry
        TableColumn<CalloutLogEntry, String> calloutNumberColumn = new TableColumn<>("Callout #");
        calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));

        TableColumn<CalloutLogEntry, String> notesTextAreaColumn = new TableColumn<>("Notes");
        notesTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("NotesTextArea"));

        TableColumn<CalloutLogEntry, String> responseGradeColumn = new TableColumn<>("Grade");
        responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));

        TableColumn<CalloutLogEntry, String> responseTypeColumn = new TableColumn<>("Type");
        responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponeType"));

        TableColumn<CalloutLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<CalloutLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<CalloutLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<CalloutLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<CalloutLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<CalloutLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<CalloutLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<CalloutLogEntry, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

        TableColumn<CalloutLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<CalloutLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        calloutTable.getColumns().addAll(
                calloutNumberColumn,
                notesTextAreaColumn,
                responseGradeColumn,
                responseTypeColumn,
                timeColumn,
                dateColumn,
                divisionColumn,
                agencyColumn,
                numberColumn,
                rankColumn,
                nameColumn,
                addressColumn,
                countyColumn,
                areaColumn
        );
        calloutTable.getItems().addAll(logEntries);
    }
    public void searchLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        // Create columns for each property of CalloutLogEntry
        TableColumn<CalloutLogEntry, String> calloutNumberColumn = new TableColumn<>("Callout #");
        calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));

        TableColumn<CalloutLogEntry, String> notesTextAreaColumn = new TableColumn<>("Notes");
        notesTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("NotesTextArea"));

        TableColumn<CalloutLogEntry, String> responseGradeColumn = new TableColumn<>("Grade");
        responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));

        TableColumn<CalloutLogEntry, String> responseTypeColumn = new TableColumn<>("Type");
        responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponeType"));

        TableColumn<CalloutLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<CalloutLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<CalloutLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<CalloutLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<CalloutLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<CalloutLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<CalloutLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<CalloutLogEntry, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

        TableColumn<CalloutLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<CalloutLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        calloutTable.getColumns().addAll(
                calloutNumberColumn,
                notesTextAreaColumn,
                responseGradeColumn,
                responseTypeColumn,
                timeColumn,
                dateColumn,
                divisionColumn,
                agencyColumn,
                numberColumn,
                rankColumn,
                nameColumn,
                addressColumn,
                countyColumn,
                areaColumn
        );
        calloutTable.getItems().addAll(logEntries);
    }
    public void incidentLogUpdate(List<IncidentLogEntry> logEntries) {
        // Clear existing data
        incidentTable.getItems().clear();

        // Create columns for each property of IncidentLogEntry
        TableColumn<IncidentLogEntry, String> incidentNumberColumn = new TableColumn<>("Incident Number");
        incidentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("incidentNumber"));

        TableColumn<IncidentLogEntry, String> incidentDateColumn = new TableColumn<>("Date");
        incidentDateColumn.setCellValueFactory(new PropertyValueFactory<>("incidentDate"));

        TableColumn<IncidentLogEntry, String> incidentTimeColumn = new TableColumn<>("Time");
        incidentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("incidentTime"));

        TableColumn<IncidentLogEntry, String> incidentStatementColumn = new TableColumn<>("Statement");
        incidentStatementColumn.setCellValueFactory(new PropertyValueFactory<>("incidentStatement"));

        TableColumn<IncidentLogEntry, String> incidentWitnessesColumn = new TableColumn<>("Witnesses");
        incidentWitnessesColumn.setCellValueFactory(new PropertyValueFactory<>("incidentWitnesses"));

        TableColumn<IncidentLogEntry, String> incidentVictimsColumn = new TableColumn<>("Victims");
        incidentVictimsColumn.setCellValueFactory(new PropertyValueFactory<>("incidentVictims"));

        TableColumn<IncidentLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<IncidentLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<IncidentLogEntry, String> officerNumberColumn = new TableColumn<>("Officer Number");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<IncidentLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        TableColumn<IncidentLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<IncidentLogEntry, String> incidentStreetColumn = new TableColumn<>("Incident Street");
        incidentStreetColumn.setCellValueFactory(new PropertyValueFactory<>("incidentStreet"));

        TableColumn<IncidentLogEntry, String> incidentAreaColumn = new TableColumn<>("Incident Area");
        incidentAreaColumn.setCellValueFactory(new PropertyValueFactory<>("incidentArea"));

        TableColumn<IncidentLogEntry, String> incidentCountyColumn = new TableColumn<>("Incident County");
        incidentCountyColumn.setCellValueFactory(new PropertyValueFactory<>("incidentCounty"));

        TableColumn<IncidentLogEntry, String> incidentActionsTakenColumn = new TableColumn<>("Actions Taken");
        incidentActionsTakenColumn.setCellValueFactory(new PropertyValueFactory<>("incidentActionsTaken"));

        TableColumn<IncidentLogEntry, String> incidentCommentsColumn = new TableColumn<>("Comments");
        incidentCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("incidentComments"));

        // Add columns to the table view
        incidentTable.getColumns().addAll(
                incidentNumberColumn,
                incidentDateColumn,
                incidentTimeColumn,
                incidentStatementColumn,
                incidentWitnessesColumn,
                incidentVictimsColumn,
                officerNameColumn,
                officerRankColumn,
                officerNumberColumn,
                officerAgencyColumn,
                officerDivisionColumn,
                incidentStreetColumn,
                incidentAreaColumn,
                incidentCountyColumn,
                incidentActionsTakenColumn,
                incidentCommentsColumn
        );

        incidentTable.getItems().addAll(logEntries);
    }


        public void trafficStopLogUpdate(List<TrafficStopLogEntry> logEntries) {
        // Clear existing data
        trafficStopTable.getItems().clear();

        // Create columns for each property of TrafficStopLogEntry
        TableColumn<TrafficStopLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<TrafficStopLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<TrafficStopLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<TrafficStopLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<TrafficStopLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<TrafficStopLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<TrafficStopLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<TrafficStopLogEntry, String> stopNumberColumn = new TableColumn<>("Stop Number");
        stopNumberColumn.setCellValueFactory(new PropertyValueFactory<>("StopNumber"));

        TableColumn<TrafficStopLogEntry, String> violationsTextAreaColumn = new TableColumn<>("Violations");
        violationsTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("ViolationsTextArea"));

        TableColumn<TrafficStopLogEntry, String> commentsTextAreaColumn = new TableColumn<>("Comments");
        commentsTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("CommentsTextArea"));

        TableColumn<TrafficStopLogEntry, String> actionsTextAreaColumn = new TableColumn<>("Actions");
        actionsTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("ActionsTextArea"));

        TableColumn<TrafficStopLogEntry, String> streetColumn = new TableColumn<>("Street");
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("Street"));

        TableColumn<TrafficStopLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<TrafficStopLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        TableColumn<TrafficStopLogEntry, String> plateNumberColumn = new TableColumn<>("Plate Number");
        plateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("PlateNumber"));

        TableColumn<TrafficStopLogEntry, String> colorColumn = new TableColumn<>("Color");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("Color"));

        TableColumn<TrafficStopLogEntry, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));

        trafficStopTable.getColumns().addAll(
                plateNumberColumn,
                dateColumn,
                timeColumn,
                nameColumn,
                rankColumn,
                numberColumn,
                divisionColumn,
                agencyColumn,
                stopNumberColumn,
                violationsTextAreaColumn,
                commentsTextAreaColumn,
                actionsTextAreaColumn,
                streetColumn,
                countyColumn,
                areaColumn,
                colorColumn,
                typeColumn
        );
        trafficStopTable.getItems().addAll(logEntries);
    }
    public void calloutLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        // Create columns for each property of CalloutLogEntry
        TableColumn<CalloutLogEntry, String> calloutNumberColumn = new TableColumn<>("Callout #");
        calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));

        TableColumn<CalloutLogEntry, String> notesTextAreaColumn = new TableColumn<>("Notes");
        notesTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("NotesTextArea"));

        TableColumn<CalloutLogEntry, String> responseGradeColumn = new TableColumn<>("Grade");
        responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));

        TableColumn<CalloutLogEntry, String> responseTypeColumn = new TableColumn<>("Type");
        responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponeType"));

        TableColumn<CalloutLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<CalloutLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<CalloutLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<CalloutLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<CalloutLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<CalloutLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<CalloutLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<CalloutLogEntry, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

        TableColumn<CalloutLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<CalloutLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        calloutTable.getColumns().addAll(
                calloutNumberColumn,
                notesTextAreaColumn,
                responseGradeColumn,
                responseTypeColumn,
                timeColumn,
                dateColumn,
                divisionColumn,
                agencyColumn,
                numberColumn,
                rankColumn,
                nameColumn,
                addressColumn,
                countyColumn,
                areaColumn
        );
        calloutTable.getItems().addAll(logEntries);
    }


    public void trafficStopReportButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("trafficStopReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Traffic Stop Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

    public void onIncidentReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("incidentReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Incident Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

    public void onSearchReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("searchReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Search Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

    public void onTopCLick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onTopDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    public void onSideClick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onSideDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    public void onArrestReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("arrestReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Arrest Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

    public void onPatrolButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patrolReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Arrest Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

    public void onCitationReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("trafficCitationReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Citation Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

    public void onImpoundReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("impoundReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Impound Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

    public void onParkingCitationReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingCitationReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Parking Citation Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();
    }

}
