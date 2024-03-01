package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;

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
    boolean hasEntered = false;

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
            setDisable(notesPane);
            setDisable(shiftInformationPane);
            setDisable(infoPane);

    }

    public void onNotesButtonClicked(ActionEvent actionEvent) {
            setDisable(codesPane);
            setActive(notesPane);
            setDisable(shiftInformationPane);
        setDisable(infoPane);

    }


    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
            setDisable(codesPane);
            setDisable(notesPane);
            setActive(shiftInformationPane);
        setDisable(infoPane);


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
        hasEntered=true;
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

        if (getOfficerInfoAgency().getValue()==null || getOfficerInfoDivision().getValue()==null ||
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
}