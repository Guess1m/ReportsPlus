package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class actionController {

    public StackPane defaultPane;
    public StackPane codesPane;
    public Button notesButton;
    public Button codeButton;
    public StackPane notesPane;
    public Button shiftInfoBtn;
    public TextField numberField;
    public TextField nameField;
    public TextField rankField;
    public TextField divisionField;
    public TextField agencyField;
    public Button submitLoginBtn;
    public StackPane shiftInformationPane;
    public TextField OfficerInfoName;
    public TextField OfficerInfoDivision;
    public TextField PatrolInfoStartTime;
    public TextField PatrolInfoStopTime;
    public Button PatrolInfoStartTimeBtn;
    public Button PatrolInfoStopTimeBtn;
    public TextArea ShiftInfoNotesTextArea;
    public TextField OfficerInfoAgency;
    public TextField OfficerInfoCallsign;
    public TextField OfficerInfoNumber;
    public TextField OfficerInfoRank;
    public Label generatedDateTag;
    public Label generatedByTag;
    public Spinner calloutSpinner;
    public Spinner trafficStopSpinner;
    public Spinner IncidentSpinner;
    public Spinner SearchesSpinner;
    public Spinner ArrestsSpinner;
    public Spinner PatrolsSpinner;


    @FXML
    protected void onHelloButtonClick() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("calloutReport-view.fxml"));
        Scene scene = new Scene(root);

        mainStage.mainRT.setScene(scene);
        mainStage.mainRT.setTitle("Second Window");
        mainStage.mainRT.show();
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
        if (DataTerminalHomeApplication.isLoggedIn){
        setDisable(defaultPane);
        setActive(codesPane);
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        } else {
            System.out.println("not logged in to press button");
        }

    }

    public void onNotesButtonClicked(ActionEvent actionEvent) {
        if (DataTerminalHomeApplication.isLoggedIn){
        setDisable(codesPane);
        setDisable(defaultPane);
        setActive(notesPane);
        setDisable(shiftInformationPane);
        } else {
            System.out.println("not logged in to press button");
        }
    }


    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
        if (DataTerminalHomeApplication.isLoggedIn){
            setDisable(defaultPane);
            setDisable(codesPane);
            setDisable(notesPane);
            setActive(shiftInformationPane);
        } else {
            System.out.println("not logged in to press button");
        }

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

    public Button getPatrolInfoStartTimeBtn() {
        return PatrolInfoStartTimeBtn;
    }

    public Button getPatrolInfoStopTimeBtn() {
        return PatrolInfoStopTimeBtn;
    }

    public TextField getPatrolInfoStartTime() {
        return PatrolInfoStartTime;
    }

    public TextField getPatrolInfoStopTime() {
        return PatrolInfoStopTime;
    }

    public StackPane getCodesPane() {
        return codesPane;
    }

    public TextField getOfficerInfoAgency() {
        return OfficerInfoAgency;
    }

    public TextField getOfficerInfoCallsign() {
        return OfficerInfoCallsign;
    }

    public TextField getOfficerInfoDivision() {
        return OfficerInfoDivision;
    }

    public TextField getOfficerInfoName() {
        return OfficerInfoName;
    }

    public TextField getOfficerInfoNumber() {
        return OfficerInfoNumber;
    }

    public TextField getOfficerInfoRank() {
        return OfficerInfoRank;
    }

    public Label getGeneratedByTag() {
        return generatedByTag;
    }

    public Label getGeneratedDateTag() {
        return generatedDateTag;
    }

    public StackPane getDefaultPane() {
        return defaultPane;
    }

    public StackPane getNotesPane() {
        return notesPane;
    }

    public StackPane getShiftInformationPane() {
        return shiftInformationPane;
    }

    public void onSubmitLoginButtonClicked(ActionEvent actionEvent) throws IOException {
        DataTerminalHomeApplication.isLoggedIn=true;
        setDisable(defaultPane);
        setDisable(codesPane);
        setDisable(notesPane);
        setActive(shiftInformationPane);

        ConfigWriter.configwrite("Agency", agencyField.getText());
        ConfigWriter.configwrite("Division", divisionField.getText());
        ConfigWriter.configwrite("Name", nameField.getText());
        ConfigWriter.configwrite("Rank", rankField.getText());
        ConfigWriter.configwrite("Number", numberField.getText());

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        OfficerInfoName.setText(name);
        OfficerInfoDivision.setText(division);
        OfficerInfoRank.setText(rank);
        OfficerInfoAgency.setText(agency);
        OfficerInfoNumber.setText(number);

        //Top Generations
        generatedByTag.setText("Generated By: "+ name);
        String date = DataTerminalHomeApplication.getDate();
        generatedDateTag.setText("Generated On: "+ date);


    }

    public void onPatrolInfoStopTimeBtnClick(ActionEvent actionEvent) {
        PatrolInfoStopTime.setText(DataTerminalHomeApplication.getTime());
    }

    public void onPatrolInfoStartTimeBtnClick(ActionEvent actionEvent) {
        PatrolInfoStartTime.setText(DataTerminalHomeApplication.getTime());
    }

    public void onCalloutReportButtonClick(ActionEvent actionEvent) {
    }
}
