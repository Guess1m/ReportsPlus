package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class actionController {

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
    public ImageView losSantosMap;
    public StackPane infoPane;
    boolean hasEntered = false;


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
            setActive(codesPane);
            setDisable(notesPane);
            setDisable(shiftInformationPane);

    }

    public void onNotesButtonClicked(ActionEvent actionEvent) {
            setDisable(codesPane);
            setActive(notesPane);
            setDisable(shiftInformationPane);
    }


    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
            setDisable(codesPane);
            setDisable(notesPane);
            setActive(shiftInformationPane);

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

    public StackPane getNotesPane() {
        return notesPane;
    }

    public StackPane getShiftInformationPane() {
        return shiftInformationPane;
    }

    public void onSubmitLoginButtonClicked(ActionEvent actionEvent) throws IOException {

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
        generatedByTag.setText("Generated By: " + name);
        String date = DataTerminalHomeApplication.getDate();
        generatedDateTag.setText("Generated On: " + date);


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

            OfficerInfoName.setText(name);
            OfficerInfoDivision.setText(division);
            OfficerInfoRank.setText(rank);
            OfficerInfoAgency.setText(agency);
            OfficerInfoNumber.setText(number);

            //Top Generations
            generatedByTag.setText("Generated By: " + name);
            String date = DataTerminalHomeApplication.getDate();
            generatedDateTag.setText("Generated On: " + date);
        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered=true;
    }
}