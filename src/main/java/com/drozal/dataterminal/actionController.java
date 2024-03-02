package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.CalloutLogEntry;
import com.drozal.dataterminal.util.dropdownInfo;
import com.drozal.dataterminal.util.stringUtil;
import jakarta.xml.bind.JAXBException;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
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
import static com.drozal.dataterminal.util.LoggingUtils.extractLogEntries;

public class actionController {

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
        stage.setTitle("Data Terminal");
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
            System.out.println("Mouse hasn't entered, setting values");
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

            profileRank.setText(rank);
            profileName.setText(name);

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
            System.out.println(getOfficerInfoAgency().getValue());
            System.out.println(getOfficerInfoDivision().getValue());
            System.out.println(getOfficerInfoRank().getValue());
            System.out.println(getOfficerInfoName().getText());
            System.out.println(getOfficerInfoNumber().getText());

            ConfigWriter.configwrite("Agency", getOfficerInfoAgency().getValue().toString());
            ConfigWriter.configwrite("Division", getOfficerInfoDivision().getValue().toString());
            ConfigWriter.configwrite("Name", getOfficerInfoName().getText());
            ConfigWriter.configwrite("Rank", getOfficerInfoRank().getValue().toString());
            ConfigWriter.configwrite("Number", getOfficerInfoNumber().getText());

            generatedByTag.setText("Generated By:" + " " + getOfficerInfoName().getText());

            profileRank.setText(getOfficerInfoRank().getValue().toString());
            profileName.setText(getOfficerInfoName().getText());

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

// Check if the file exists before extracting log entries
        if (Files.exists(Paths.get(stringUtil.calloutLogURL))) {
            // Extract log entries from XML file
            List<CalloutLogEntry> logEntries = extractLogEntries(stringUtil.calloutLogURL);

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

        logGrid.add(new Label("Log Index: ") {{
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
}