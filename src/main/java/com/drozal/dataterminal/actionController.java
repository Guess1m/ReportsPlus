package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.Incident.IncidentLogEntry;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import com.drozal.dataterminal.logs.Search.SearchLogEntry;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
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
    private double xOffset = 0;
    private double yOffset = 0;

    public StackPane codesPane;
    public Button notesButton;
    public Button codeButton;
    public StackPane notesPane;
    public Button shiftInfoBtn;
    public StackPane shiftInformationPane;
    public TextField OfficerInfoName;
    public ComboBox OfficerInfoDivision;
    public TextField PatrolInfoStartTime;
    public TextField PatrolInfoStopTime;
    public Button PatrolInfoStartTimeBtn;
    public Button PatrolInfoStopTimeBtn;
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
    public Label profileName;
    public Label profileRank;
    public Label updatedNotification;
    public TabPane logPane;
    public GridPane logGrid;
    public GridPane trafficStopGrid;
    public GridPane incidentGrid;
    public GridPane searchGrid;
    boolean hasEntered = false;


    public TabPane getLogPane() {
        return logPane;
    }

    public GridPane getLogGrid() {
        return logGrid;
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

    public void onPatrolInfoStopTimeBtnClick(ActionEvent actionEvent) {
        PatrolInfoStopTime.setText(DataTerminalHomeApplication.getTime());
    }

    public void onPatrolInfoStartTimeBtnClick(ActionEvent actionEvent) {
        PatrolInfoStartTime.setText(DataTerminalHomeApplication.getTime());
    }

    public void onCalloutReportButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("calloutReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Callout Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UTILITY);
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
            OfficerInfoDivision.setPromptText(division);
            OfficerInfoRank.setPromptText(rank);
            OfficerInfoAgency.setPromptText(agency);
            OfficerInfoNumber.setText(number);

            createSpinner(calloutSpinner, 0, 999, 0);
            createSpinner(getArrestsSpinner(), 0, 999, 0);
            createSpinner(getSearchesSpinner(), 0, 999, 0);
            createSpinner(getIncidentSpinner(), 0, 999, 0);
            createSpinner(getPatrolsSpinner(), 0, 999, 0);
            createSpinner(trafficStopSpinner, 0, 999, 0);

            //profileRank.setText(rank);
            //profileName.setText(name);

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

            //profileRank.setText(getOfficerInfoRank().getValue().toString());
            //profileName.setText(getOfficerInfoName().getText());

            updatedNotification.setText("updated.");
            updatedNotification.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline.play();

        }
    }

    public void onExitButtonClick(ActionEvent actionEvent) {
        Platform.exit();

    }

    public void onLogsButtonClick(ActionEvent actionEvent) throws IOException, JAXBException, ParserConfigurationException, TransformerException {
        setDisable(codesPane);
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        setDisable(infoPane);
        logPane.setVisible(true);
        logPane.setDisable(false);

        logGrid.getChildren().clear();
        trafficStopGrid.getChildren().clear();
        incidentGrid.getChildren().clear();
        searchGrid.getChildren().clear();


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
            System.out.println("File does not exist.");
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
            System.out.println("File does not exist.");
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
        }

        // Check if the file exists before extracting log entries
        if (Files.exists(Paths.get(stringUtil.trafficstopLogURL))) {
            // Extract log entries from XML file
            List<TrafficStopLogEntry> logEntries = TrafficStopReportLogs.extractLogEntries(stringUtil.trafficstopLogURL);

            // Add log entries to the GridPane
            int row = 1;
            for (TrafficStopLogEntry logEntry : logEntries) {
                trafficStopGrid.add(new Label(logEntry.StopNumber), 0, row);
                trafficStopGrid.add(new Label(logEntry.Date), 1, row);
                trafficStopGrid.add(new Label(logEntry.Time), 2, row);
                trafficStopGrid.add(new Label(logEntry.PlateNumber), 3, row);
                trafficStopGrid.add(new Label(logEntry.Color), 4, row);
                trafficStopGrid.add(new Label(logEntry.Type), 5, row);
                trafficStopGrid.add(new Label(logEntry.Rank), 6, row);
                trafficStopGrid.add(new Label(logEntry.Name), 7, row);
                trafficStopGrid.add(new Label(logEntry.Division), 8, row);
                trafficStopGrid.add(new Label(logEntry.Agency), 9, row);
                trafficStopGrid.add(new Label(logEntry.Number), 10, row);
                trafficStopGrid.add(new Label(logEntry.Street), 11, row);
                trafficStopGrid.add(new Label(logEntry.County), 12, row);
                trafficStopGrid.add(new Label(logEntry.Area), 13, row);
                trafficStopGrid.add(new Label(logEntry.ViolationsTextArea), 14, row);
                trafficStopGrid.add(new Label(logEntry.ActionsTextArea), 15, row);
                trafficStopGrid.add(new Label(logEntry.CommentsTextArea), 16, row);
                row++;
            }
        } else {
            System.out.println("File does not exist.");
        }
        // Add ColumnConstraints for all columns
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setHgrow(Priority.ALWAYS);
        columnConstraints1.setFillWidth(true);
        trafficStopGrid.getColumnConstraints().add(columnConstraints1);
        trafficStopGrid.add(new Label("Traffic Stop #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 0, 0);
        trafficStopGrid.add(new Label("Date: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 1, 0);
        trafficStopGrid.add(new Label("Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 2, 0);
        trafficStopGrid.add(new Label("Plate #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 3, 0);
        trafficStopGrid.add(new Label("Color: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 4, 0);
        trafficStopGrid.add(new Label("Type: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 5, 0);
        trafficStopGrid.add(new Label("Rank: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 6, 0);
        trafficStopGrid.add(new Label("Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 7, 0);
        trafficStopGrid.add(new Label("Division: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 8, 0);
        trafficStopGrid.add(new Label("Agency: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 9, 0);
        trafficStopGrid.add(new Label("Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 10, 0);
        trafficStopGrid.add(new Label("Street: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 11, 0);
        trafficStopGrid.add(new Label("County: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 12, 0);
        trafficStopGrid.add(new Label("Area: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 13, 0);
        trafficStopGrid.add(new Label("Violations: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 14, 0);
        trafficStopGrid.add(new Label("Action Taken: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 15, 0);
        trafficStopGrid.add(new Label("Comments: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 16, 0);
        // Set row constraints to apply to all rows
        RowConstraints rowConstraints1 = new RowConstraints();
        rowConstraints1.setVgrow(Priority.ALWAYS); // Allow vertical growth
        rowConstraints1.setMinHeight(100); // Set the minimum height
        rowConstraints1.setPrefHeight(100); // Set the preferred height
        rowConstraints1.setMaxHeight(100); // Set the maximum height
        // Add row constraints to all rows in the GridPane
        int numRows1 = trafficStopGrid.getRowConstraints().size();
        for (int i = 0; i < numRows1; i++) {
            trafficStopGrid.getRowConstraints().add(rowConstraints1);
        }


        // Check if the file exists before extracting log entries
        if (Files.exists(Paths.get(stringUtil.calloutLogURL))) {
            List<CalloutLogEntry> logEntries = CalloutReportLogs.extractLogEntries(stringUtil.calloutLogURL);

            // Add log entries to the GridPane
            int row = 1;
            for (CalloutLogEntry logEntry : logEntries) {
                logGrid.add(new Label(logEntry.CalloutNumber), 0, row);
                logGrid.add(new Label(logEntry.Date), 1, row);
                logGrid.add(new Label(logEntry.Time), 2, row);
                logGrid.add(new Label(logEntry.ResponeType), 3, row);
                logGrid.add(new Label(logEntry.ResponseGrade), 4, row);
                logGrid.add(new Label(logEntry.Address), 5, row);
                logGrid.add(new Label(logEntry.County), 6, row);
                logGrid.add(new Label(logEntry.Area), 7, row);
                logGrid.add(new Label(logEntry.Rank), 8, row);
                logGrid.add(new Label(logEntry.Name), 9, row);
                logGrid.add(new Label(logEntry.Number), 10, row);
                logGrid.add(new Label(logEntry.Agency), 11, row);
                logGrid.add(new Label(logEntry.Division), 12, row);
                logGrid.add(new Label(logEntry.NotesTextArea), 13, row);
                row++;
            }
        } else {
            System.out.println("File does not exist.");
        }
        // Add ColumnConstraints for all columns
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHgrow(Priority.ALWAYS);
        columnConstraints.setFillWidth(true);
        logGrid.getColumnConstraints().add(columnConstraints);
        logGrid.add(new Label("Callout #: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 0, 0);
        logGrid.add(new Label("Date: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 1, 0);
        logGrid.add(new Label("Time: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 2, 0);
        logGrid.add(new Label("Response Type: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 3, 0);
        logGrid.add(new Label("Response Grade: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 4, 0);
        logGrid.add(new Label("Address: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 5, 0);
        logGrid.add(new Label("County: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 6, 0);
        logGrid.add(new Label("Area: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 7, 0);
        logGrid.add(new Label("Rank: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 8, 0);
        logGrid.add(new Label("Name: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 9, 0);
        logGrid.add(new Label("Number: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 10, 0);
        logGrid.add(new Label("Agency: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 11, 0);
        logGrid.add(new Label("Division: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 12, 0);
        logGrid.add(new Label("Notes: ") {{
            setStyle("-fx-font-weight: bold; -fx-font-size: 15px;");
        }}, 13, 0);
        // Set row constraints to apply to all rows
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setVgrow(Priority.ALWAYS); // Allow vertical growth
        rowConstraints.setMinHeight(100); // Set the minimum height
        rowConstraints.setPrefHeight(100); // Set the preferred height
        rowConstraints.setMaxHeight(100); // Set the maximum height
        // Add row constraints to all rows in the GridPane
        int numRows = logGrid.getRowConstraints().size();
        for (int i = 0; i < numRows; i++) {
            logGrid.getRowConstraints().add(rowConstraints);
        }
    }

    public void trafficStopReportButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("trafficStopReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Traffic Stop Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UTILITY);
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
        stage.initStyle(StageStyle.UTILITY);
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
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.show();
    }

    public void onTopCLick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onTopDrag(MouseEvent mouseEvent) {
        mainStage.mainRT.setX(mouseEvent.getScreenX() - xOffset);
        mainStage.mainRT.setY(mouseEvent.getScreenY() - yOffset);
    }

    public void onSideClick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onSideDrag(MouseEvent mouseEvent) {
        mainStage.mainRT.setX(mouseEvent.getScreenX() - xOffset);
        mainStage.mainRT.setY(mouseEvent.getScreenY() - yOffset);
    }
}