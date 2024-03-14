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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
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
    public VBox vbox;
    public TableView calloutTable;
    boolean hasEntered = false;
    double minColumnWidth = 200.0; // Adjust this value according to your needs
    private double xOffset = 0;
    private double yOffset = 0;

    public static String getDataLogsFolderPath() {
        try {
            // Get the location of the JAR file
            String jarPath = stringUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();

            // Construct the path for the DataLogs folder
            return jarDir + File.separator + "DataLogs" + File.separator;
        } catch (URISyntaxException e) {
            // Handle exception if URI syntax is incorrect
            e.printStackTrace();
            return ""; // Return empty string if an error occurs
        }
    }

    public static String getJarDirectoryPath() {
        try {
            // Get the location of the JAR file
            String jarPath = actionController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Extract the directory path from the JAR path
            return new File(jarPath).getParent();
        } catch (Exception e) {
            // Handle exception if URI syntax is incorrect
            e.printStackTrace();
            return ""; // Return empty string if an error occurs
        }
    }

    public static void clearConfig() {
        try {
            // Get the path to the config.properties file
            String configFilePath = getJarDirectoryPath() + File.separator + "config.properties";
            File configFile = new File(configFilePath);

            // Check if the config.properties file exists
            if (configFile.exists() && configFile.isFile()) {
                // Delete the config.properties file
                try {
                    Files.deleteIfExists(configFile.toPath());
                    System.out.println("Deleted config.properties file.");
                } catch (IOException e) {
                    System.err.println("Failed to delete config.properties file: " + e.getMessage());
                }
            } else {
                System.out.println("config.properties file does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the application after deleting the files
            Platform.exit();
        }
    }

    public StackPane getLogPane() {
        return logPane;
    }

    public void initialize() {
        initializeCalloutColumns();
        initializeArrestColumns();
        initializeCitationColumns();
        initializeImpoundColumns();
        initializeIncidentColumns();
        initializePatrolColumns();
        initializeSearchColumns();
        initializeTrafficStopColumns();
        initializeParkingCitationColumns();
        loadLogs();
    }

    private void loadLogs() {
        //ParkingCitationTable
        List<ParkingCitationLogEntry> parkingCitationLogEntryList = ParkingCitationReportLogs.extractLogEntries(stringUtil.parkingCitationLogURL);
        parkingCitationLogUpdate(parkingCitationLogEntryList);
        //ImpoundTable
        List<ImpoundLogEntry> impoundLogEntryList = ImpoundReportLogs.extractLogEntries(stringUtil.impoundLogURL);
        impoundLogUpdate(impoundLogEntryList);
        //TrafficCitationTable
        List<TrafficCitationLogEntry> citationLogEntryList = TrafficCitationReportLogs.extractLogEntries(stringUtil.trafficCitationLogURL);
        citationLogUpdate(citationLogEntryList);
        //PatrolTable
        List<PatrolLogEntry> patrolLogEntryList = PatrolReportLogs.extractLogEntries(stringUtil.patrolLogURL);
        patrolLogUpdate(patrolLogEntryList);
        //ArrestTable
        List<ArrestLogEntry> arrestLogEntryList = ArrestReportLogs.extractLogEntries(stringUtil.arrestLogURL);
        arrestLogUpdate(arrestLogEntryList);
        //SearchTable
        List<SearchLogEntry> searchLogEntryList = SearchReportLogs.extractLogEntries(stringUtil.searchLogURL);
        searchLogUpdate(searchLogEntryList);
        //IncidentTable
        List<IncidentLogEntry> incidentLogEntryList = IncidentReportLogs.extractLogEntries(stringUtil.incidentLogURL);
        incidentLogUpdate(incidentLogEntryList);
        //TrafficStopTable
        List<TrafficStopLogEntry> trafficLogEntryList = TrafficStopReportLogs.extractLogEntries(stringUtil.trafficstopLogURL);
        trafficStopLogUpdate(trafficLogEntryList);
        List<CalloutLogEntry> calloutLogEntryList = CalloutReportLogs.extractLogEntries(stringUtil.calloutLogURL);
        calloutLogUpdate(calloutLogEntryList);
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
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
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
    }

    public void parkingCitationLogUpdate(List<ParkingCitationLogEntry> logEntries) {
        // Clear existing data
        parkingCitationTable.getItems().clear();
        parkingCitationTable.getItems().addAll(logEntries);
    }

    public void initializeParkingCitationColumns() {


        // Create columns for each property of ParkingCitationLogEntry
        TableColumn<ParkingCitationLogEntry, String> citationNumberColumn = new TableColumn<>("Citation #");
        citationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("citationNumber"));

        TableColumn<ParkingCitationLogEntry, String> citationDateColumn = new TableColumn<>("Citation Date");
        citationDateColumn.setCellValueFactory(new PropertyValueFactory<>("citationDate"));

        TableColumn<ParkingCitationLogEntry, String> citationTimeColumn = new TableColumn<>("Citation Time");
        citationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("citationTime"));

        TableColumn<ParkingCitationLogEntry, String> meterNumberColumn = new TableColumn<>("Meter #");
        meterNumberColumn.setCellValueFactory(new PropertyValueFactory<>("meterNumber"));

        TableColumn<ParkingCitationLogEntry, String> citationCountyColumn = new TableColumn<>("County");
        citationCountyColumn.setCellValueFactory(new PropertyValueFactory<>("citationCounty"));

        TableColumn<ParkingCitationLogEntry, String> citationAreaColumn = new TableColumn<>("Area");
        citationAreaColumn.setCellValueFactory(new PropertyValueFactory<>("citationArea"));

        TableColumn<ParkingCitationLogEntry, String> citationStreetColumn = new TableColumn<>("Street");
        citationStreetColumn.setCellValueFactory(new PropertyValueFactory<>("citationStreet"));

        TableColumn<ParkingCitationLogEntry, String> offenderNameColumn = new TableColumn<>("Offender Name");
        offenderNameColumn.setCellValueFactory(new PropertyValueFactory<>("offenderName"));

        TableColumn<ParkingCitationLogEntry, String> offenderGenderColumn = new TableColumn<>("Offender Gender");
        offenderGenderColumn.setCellValueFactory(new PropertyValueFactory<>("offenderGender"));

        TableColumn<ParkingCitationLogEntry, String> offenderEthnicityColumn = new TableColumn<>("Offender Ethnicity");
        offenderEthnicityColumn.setCellValueFactory(new PropertyValueFactory<>("offenderEthnicity"));

        TableColumn<ParkingCitationLogEntry, String> offenderAgeColumn = new TableColumn<>("Offender Age");
        offenderAgeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderAge"));

        TableColumn<ParkingCitationLogEntry, String> offenderDescriptionColumn = new TableColumn<>("Offender Desc.");
        offenderDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("offenderDescription"));

        TableColumn<ParkingCitationLogEntry, String> offenderVehicleMakeColumn = new TableColumn<>("Vehicle Make");
        offenderVehicleMakeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleMake"));

        TableColumn<ParkingCitationLogEntry, String> offenderVehicleModelColumn = new TableColumn<>("Vehicle Model");
        offenderVehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleModel"));

        TableColumn<ParkingCitationLogEntry, String> offenderVehicleColorColumn = new TableColumn<>("Vehicle Color");
        offenderVehicleColorColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleColor"));

        TableColumn<ParkingCitationLogEntry, String> offenderVehicleTypeColumn = new TableColumn<>("Vehicle Type");
        offenderVehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleType"));

        TableColumn<ParkingCitationLogEntry, String> offenderVehiclePlateColumn = new TableColumn<>("License Plate");
        offenderVehiclePlateColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehiclePlate"));

        TableColumn<ParkingCitationLogEntry, String> offenderVehicleOtherColumn = new TableColumn<>("Other Veh. Info");
        offenderVehicleOtherColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleOther"));

        TableColumn<ParkingCitationLogEntry, String> offenderViolationsColumn = new TableColumn<>("Violations");
        offenderViolationsColumn.setCellValueFactory(new PropertyValueFactory<>("offenderViolations"));

        TableColumn<ParkingCitationLogEntry, String> offenderActionsTakenColumn = new TableColumn<>("Actions Taken");
        offenderActionsTakenColumn.setCellValueFactory(new PropertyValueFactory<>("offenderActionsTaken"));

        TableColumn<ParkingCitationLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<ParkingCitationLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<ParkingCitationLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<ParkingCitationLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<ParkingCitationLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        TableColumn<ParkingCitationLogEntry, String> citationCommentsColumn = new TableColumn<>("Comments");
        citationCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("citationComments"));

        // Add columns to the table
        // Create a list to hold all the TableColumn instances for parkingCitationTable
        ObservableList<TableColumn<ParkingCitationLogEntry, ?>> parkingCitationColumns = FXCollections.observableArrayList(
                citationNumberColumn,
                citationDateColumn,
                citationTimeColumn,
                meterNumberColumn,
                citationCountyColumn,
                citationAreaColumn,
                citationStreetColumn,
                offenderNameColumn,
                offenderGenderColumn,
                offenderEthnicityColumn,
                offenderAgeColumn,
                offenderDescriptionColumn,
                offenderVehicleMakeColumn,
                offenderVehicleModelColumn,
                offenderVehicleColorColumn,
                offenderVehicleTypeColumn,
                offenderVehiclePlateColumn,
                offenderVehicleOtherColumn,
                offenderViolationsColumn,
                offenderActionsTakenColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerDivisionColumn,
                officerAgencyColumn,
                citationCommentsColumn
        );

// Add all columns to parkingCitationTable
        parkingCitationTable.getColumns().addAll(parkingCitationColumns);

// Set minimum width for each column
        for (TableColumn<ParkingCitationLogEntry, ?> column : parkingCitationColumns) {
            column.setMinWidth(minColumnWidth);
        }
    }

    public void impoundLogUpdate(List<ImpoundLogEntry> logEntries) {
        // Clear existing data
        impoundTable.getItems().clear();
        // Add data to the table
        impoundTable.getItems().addAll(logEntries);
    }

    public void initializeImpoundColumns() {


        // Create columns for each property of ImpoundLogEntry
        TableColumn<ImpoundLogEntry, String> impoundNumberColumn = new TableColumn<>("Impound #");
        impoundNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundNumber"));

        TableColumn<ImpoundLogEntry, String> impoundDateColumn = new TableColumn<>("Impound Date");
        impoundDateColumn.setCellValueFactory(new PropertyValueFactory<>("impoundDate"));

        TableColumn<ImpoundLogEntry, String> impoundTimeColumn = new TableColumn<>("Impound Time");
        impoundTimeColumn.setCellValueFactory(new PropertyValueFactory<>("impoundTime"));

        TableColumn<ImpoundLogEntry, String> ownerNameColumn = new TableColumn<>("Owner Name");
        ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));

        TableColumn<ImpoundLogEntry, String> ownerAgeColumn = new TableColumn<>("Owner Age");
        ownerAgeColumn.setCellValueFactory(new PropertyValueFactory<>("ownerAge"));

        TableColumn<ImpoundLogEntry, String> ownerGenderColumn = new TableColumn<>("Owner Gender");
        ownerGenderColumn.setCellValueFactory(new PropertyValueFactory<>("ownerGender"));

        TableColumn<ImpoundLogEntry, String> ownerAddressColumn = new TableColumn<>("Owner Address");
        ownerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("ownerAddress"));

        TableColumn<ImpoundLogEntry, String> impoundPlateNumberColumn = new TableColumn<>("Impound Plate Number");
        impoundPlateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundPlateNumber"));

        TableColumn<ImpoundLogEntry, String> impoundMakeColumn = new TableColumn<>("Impound Make");
        impoundMakeColumn.setCellValueFactory(new PropertyValueFactory<>("impoundMake"));

        TableColumn<ImpoundLogEntry, String> impoundModelColumn = new TableColumn<>("Impound Model");
        impoundModelColumn.setCellValueFactory(new PropertyValueFactory<>("impoundModel"));

        TableColumn<ImpoundLogEntry, String> impoundTypeColumn = new TableColumn<>("Impound Type");
        impoundTypeColumn.setCellValueFactory(new PropertyValueFactory<>("impoundType"));

        TableColumn<ImpoundLogEntry, String> impoundColorColumn = new TableColumn<>("Impound Color");
        impoundColorColumn.setCellValueFactory(new PropertyValueFactory<>("impoundColor"));

        TableColumn<ImpoundLogEntry, String> impoundCommentsColumn = new TableColumn<>("Impound Comments");
        impoundCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("impoundComments"));

        TableColumn<ImpoundLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<ImpoundLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<ImpoundLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<ImpoundLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<ImpoundLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        // Add columns to the table
        // Create a list to hold all the TableColumn instances for impoundTable
        ObservableList<TableColumn<ImpoundLogEntry, ?>> impoundColumns = FXCollections.observableArrayList(
                impoundNumberColumn,
                impoundDateColumn,
                impoundTimeColumn,
                ownerNameColumn,
                ownerAgeColumn,
                ownerGenderColumn,
                ownerAddressColumn,
                impoundPlateNumberColumn,
                impoundMakeColumn,
                impoundModelColumn,
                impoundTypeColumn,
                impoundColorColumn,
                impoundCommentsColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerDivisionColumn,
                officerAgencyColumn
        );

// Add all columns to impoundTable
        impoundTable.getColumns().addAll(impoundColumns);

// Set minimum width for each column
        for (TableColumn<ImpoundLogEntry, ?> column : impoundColumns) {
            column.setMinWidth(minColumnWidth);
        }
    }

    public void citationLogUpdate(List<TrafficCitationLogEntry> logEntries) {
        // Clear existing data
        citationTable.getItems().clear();
        citationTable.getItems().addAll(logEntries);
    }

    public void initializeCitationColumns() {

        // Create columns for each property of TrafficCitationLogEntry
        TableColumn<TrafficCitationLogEntry, String> citationNumberColumn = new TableColumn<>("Citation #");
        citationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("citationNumber"));

        TableColumn<TrafficCitationLogEntry, String> citationDateColumn = new TableColumn<>("Citation Date");
        citationDateColumn.setCellValueFactory(new PropertyValueFactory<>("citationDate"));

        TableColumn<TrafficCitationLogEntry, String> citationTimeColumn = new TableColumn<>("Citation Time");
        citationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("citationTime"));

        TableColumn<TrafficCitationLogEntry, String> citationCountyColumn = new TableColumn<>("Citation County");
        citationCountyColumn.setCellValueFactory(new PropertyValueFactory<>("citationCounty"));

        TableColumn<TrafficCitationLogEntry, String> citationAreaColumn = new TableColumn<>("Citation Area");
        citationAreaColumn.setCellValueFactory(new PropertyValueFactory<>("citationArea"));

        TableColumn<TrafficCitationLogEntry, String> citationStreetColumn = new TableColumn<>("Citation Street");
        citationStreetColumn.setCellValueFactory(new PropertyValueFactory<>("citationStreet"));

        TableColumn<TrafficCitationLogEntry, String> offenderNameColumn = new TableColumn<>("Offender Name");
        offenderNameColumn.setCellValueFactory(new PropertyValueFactory<>("offenderName"));

        TableColumn<TrafficCitationLogEntry, String> offenderGenderColumn = new TableColumn<>("Offender Gender");
        offenderGenderColumn.setCellValueFactory(new PropertyValueFactory<>("offenderGender"));

        TableColumn<TrafficCitationLogEntry, String> offenderEthnicityColumn = new TableColumn<>("Offender Ethnicity");
        offenderEthnicityColumn.setCellValueFactory(new PropertyValueFactory<>("offenderEthnicity"));

        TableColumn<TrafficCitationLogEntry, String> offenderAgeColumn = new TableColumn<>("Offender Age");
        offenderAgeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderAge"));

        TableColumn<TrafficCitationLogEntry, String> offenderDescriptionColumn = new TableColumn<>("Offender Description");
        offenderDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("offenderDescription"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehicleMakeColumn = new TableColumn<>("Offender Vehicle Make");
        offenderVehicleMakeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleMake"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehicleModelColumn = new TableColumn<>("Offender Vehicle Model");
        offenderVehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleModel"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehicleColorColumn = new TableColumn<>("Offender Vehicle Color");
        offenderVehicleColorColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleColor"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehicleTypeColumn = new TableColumn<>("Offender Vehicle Type");
        offenderVehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleType"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehiclePlateColumn = new TableColumn<>("Offender Vehicle Plate");
        offenderVehiclePlateColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehiclePlate"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehicleOtherColumn = new TableColumn<>("Offender Vehicle Other");
        offenderVehicleOtherColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleOther"));

        TableColumn<TrafficCitationLogEntry, String> offenderViolationsColumn = new TableColumn<>("Offender Violations");
        offenderViolationsColumn.setCellValueFactory(new PropertyValueFactory<>("offenderViolations"));

        TableColumn<TrafficCitationLogEntry, String> offenderActionsTakenColumn = new TableColumn<>("Offender Actions Taken");
        offenderActionsTakenColumn.setCellValueFactory(new PropertyValueFactory<>("offenderActionsTaken"));

        TableColumn<TrafficCitationLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<TrafficCitationLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<TrafficCitationLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<TrafficCitationLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<TrafficCitationLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        TableColumn<TrafficCitationLogEntry, String> citationCommentsColumn = new TableColumn<>("Comments");
        citationCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("citationComments"));

        // Create a list to hold all the TableColumn instances for citationTable
        ObservableList<TableColumn<TrafficCitationLogEntry, ?>> citationColumns = FXCollections.observableArrayList(
                citationNumberColumn,
                citationDateColumn,
                citationTimeColumn,
                citationCountyColumn,
                citationAreaColumn,
                citationStreetColumn,
                offenderNameColumn,
                offenderGenderColumn,
                offenderEthnicityColumn,
                offenderAgeColumn,
                offenderDescriptionColumn,
                offenderVehicleMakeColumn,
                offenderVehicleModelColumn,
                offenderVehicleColorColumn,
                offenderVehicleTypeColumn,
                offenderVehiclePlateColumn,
                offenderVehicleOtherColumn,
                offenderViolationsColumn,
                offenderActionsTakenColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerDivisionColumn,
                officerAgencyColumn,
                citationCommentsColumn
        );

// Add all columns to citationTable
        citationTable.getColumns().addAll(citationColumns);

// Set minimum width for each column
        for (TableColumn<TrafficCitationLogEntry, ?> column : citationColumns) {
            column.setMinWidth(minColumnWidth);
        }
    }

    public void patrolLogUpdate(List<PatrolLogEntry> logEntries) {
        // Clear existing data
        patrolTable.getItems().clear();
        patrolTable.getItems().addAll(logEntries);
    }

    public void initializePatrolColumns() {

        // Create columns for each property of PatrolLogEntry
        TableColumn<PatrolLogEntry, String> patrolNumberColumn = new TableColumn<>("Patrol #");
        patrolNumberColumn.setCellValueFactory(new PropertyValueFactory<>("patrolNumber"));

        TableColumn<PatrolLogEntry, String> patrolDateColumn = new TableColumn<>("Patrol Date");
        patrolDateColumn.setCellValueFactory(new PropertyValueFactory<>("patrolDate"));

        TableColumn<PatrolLogEntry, String> patrolLengthColumn = new TableColumn<>("Patrol Length");
        patrolLengthColumn.setCellValueFactory(new PropertyValueFactory<>("patrolLength"));

        TableColumn<PatrolLogEntry, String> patrolStartTimeColumn = new TableColumn<>("Patrol Start Time");
        patrolStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("patrolStartTime"));

        TableColumn<PatrolLogEntry, String> patrolStopTimeColumn = new TableColumn<>("Patrol Stop Time");
        patrolStopTimeColumn.setCellValueFactory(new PropertyValueFactory<>("patrolStopTime"));

        TableColumn<PatrolLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<PatrolLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<PatrolLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<PatrolLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<PatrolLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        TableColumn<PatrolLogEntry, String> officerVehicleColumn = new TableColumn<>("Officer Vehicle");
        officerVehicleColumn.setCellValueFactory(new PropertyValueFactory<>("officerVehicle"));

        TableColumn<PatrolLogEntry, String> patrolCommentsColumn = new TableColumn<>("Patrol Comments");
        patrolCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("patrolComments"));

        // Create a list to hold all the TableColumn instances for patrolTable
        ObservableList<TableColumn<PatrolLogEntry, ?>> patrolColumns = FXCollections.observableArrayList(
                patrolNumberColumn,
                patrolDateColumn,
                patrolLengthColumn,
                patrolStartTimeColumn,
                patrolStopTimeColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerDivisionColumn,
                officerAgencyColumn,
                officerVehicleColumn,
                patrolCommentsColumn
        );

// Add all columns to patrolTable
        patrolTable.getColumns().addAll(patrolColumns);

// Set minimum width for each column
        for (TableColumn<PatrolLogEntry, ?> column : patrolColumns) {
            column.setMinWidth(minColumnWidth);
        }
    }

    public void arrestLogUpdate(List<ArrestLogEntry> logEntries) {
        // Clear existing data
        arrestTable.getItems().clear();
        arrestTable.getItems().addAll(logEntries);
    }

    public void initializeArrestColumns() {


        // Create columns for each property of ArrestLogEntry
        TableColumn<ArrestLogEntry, String> arrestNumberColumn = new TableColumn<>("Arrest #");
        arrestNumberColumn.setCellValueFactory(new PropertyValueFactory<>("arrestNumber"));

        TableColumn<ArrestLogEntry, String> arrestDateColumn = new TableColumn<>("Arrest Date");
        arrestDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDate"));

        TableColumn<ArrestLogEntry, String> arrestTimeColumn = new TableColumn<>("Arrest Time");
        arrestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrestTime"));

        TableColumn<ArrestLogEntry, String> arrestCountyColumn = new TableColumn<>("Arrest County");
        arrestCountyColumn.setCellValueFactory(new PropertyValueFactory<>("arrestCounty"));

        TableColumn<ArrestLogEntry, String> arrestAreaColumn = new TableColumn<>("Arrest Area");
        arrestAreaColumn.setCellValueFactory(new PropertyValueFactory<>("arrestArea"));

        TableColumn<ArrestLogEntry, String> arrestStreetColumn = new TableColumn<>("Arrest Street");
        arrestStreetColumn.setCellValueFactory(new PropertyValueFactory<>("arrestStreet"));

        TableColumn<ArrestLogEntry, String> arresteeNameColumn = new TableColumn<>("Arrestee Name");
        arresteeNameColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeName"));

        TableColumn<ArrestLogEntry, String> arresteeAgeColumn = new TableColumn<>("Arrestee Age");
        arresteeAgeColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeAge"));

        TableColumn<ArrestLogEntry, String> arresteeGenderColumn = new TableColumn<>("Arrestee Gender");
        arresteeGenderColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeGender"));

        TableColumn<ArrestLogEntry, String> arresteeEthnicityColumn = new TableColumn<>("Arrestee Ethnicity");
        arresteeEthnicityColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeEthnicity"));

        TableColumn<ArrestLogEntry, String> arresteeDescriptionColumn = new TableColumn<>("Arrestee Description");
        arresteeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeDescription"));

        TableColumn<ArrestLogEntry, String> arresteeMedicalInformationColumn = new TableColumn<>("Medical Information");
        arresteeMedicalInformationColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeMedicalInformation"));

        TableColumn<ArrestLogEntry, String> arrestDetailsColumn = new TableColumn<>("Arrest Details");
        arrestDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDetails"));

        TableColumn<ArrestLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<ArrestLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<ArrestLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<ArrestLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<ArrestLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        // Create a list to hold all the TableColumn instances for arrestTable
        ObservableList<TableColumn<ArrestLogEntry, ?>> arrestColumns = FXCollections.observableArrayList(
                arrestNumberColumn,
                arrestDateColumn,
                arrestTimeColumn,
                arrestCountyColumn,
                arrestAreaColumn,
                arrestStreetColumn,
                arresteeNameColumn,
                arresteeAgeColumn,
                arresteeGenderColumn,
                arresteeEthnicityColumn,
                arresteeDescriptionColumn,
                arresteeMedicalInformationColumn,
                arrestDetailsColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerDivisionColumn,
                officerAgencyColumn
        );

// Add all columns to arrestTable
        arrestTable.getColumns().addAll(arrestColumns);

// Set minimum width for each column
        for (TableColumn<ArrestLogEntry, ?> column : arrestColumns) {
            column.setMinWidth(minColumnWidth);
        }

    }

    public void searchLogUpdate(List<SearchLogEntry> logEntries) {
        // Clear existing data
        searchTable.getItems().clear();
        searchTable.getItems().addAll(logEntries);
    }

    public void initializeSearchColumns() {

        // Create columns for each property of SearchLogEntry
        TableColumn<SearchLogEntry, String> searchNumberColumn = new TableColumn<>("Search #");
        searchNumberColumn.setCellValueFactory(new PropertyValueFactory<>("SearchNumber"));

        TableColumn<SearchLogEntry, String> searchDateColumn = new TableColumn<>("Search Date");
        searchDateColumn.setCellValueFactory(new PropertyValueFactory<>("searchDate"));

        TableColumn<SearchLogEntry, String> searchTimeColumn = new TableColumn<>("Search Time");
        searchTimeColumn.setCellValueFactory(new PropertyValueFactory<>("searchTime"));

        TableColumn<SearchLogEntry, String> searchSeizedItemsColumn = new TableColumn<>("Seized Items");
        searchSeizedItemsColumn.setCellValueFactory(new PropertyValueFactory<>("searchSeizedItems"));

        TableColumn<SearchLogEntry, String> searchGroundsColumn = new TableColumn<>("Grounds");
        searchGroundsColumn.setCellValueFactory(new PropertyValueFactory<>("searchGrounds"));

        TableColumn<SearchLogEntry, String> searchTypeColumn = new TableColumn<>("Search Type");
        searchTypeColumn.setCellValueFactory(new PropertyValueFactory<>("searchType"));

        TableColumn<SearchLogEntry, String> searchMethodColumn = new TableColumn<>("Search Method");
        searchMethodColumn.setCellValueFactory(new PropertyValueFactory<>("searchMethod"));

        TableColumn<SearchLogEntry, String> searchWitnessesColumn = new TableColumn<>("Witnesses");
        searchWitnessesColumn.setCellValueFactory(new PropertyValueFactory<>("searchWitnesses"));

        TableColumn<SearchLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<SearchLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<SearchLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<SearchLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        TableColumn<SearchLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<SearchLogEntry, String> searchStreetColumn = new TableColumn<>("Search Street");
        searchStreetColumn.setCellValueFactory(new PropertyValueFactory<>("searchStreet"));

        TableColumn<SearchLogEntry, String> searchAreaColumn = new TableColumn<>("Search Area");
        searchAreaColumn.setCellValueFactory(new PropertyValueFactory<>("searchArea"));

        TableColumn<SearchLogEntry, String> searchCountyColumn = new TableColumn<>("Search County");
        searchCountyColumn.setCellValueFactory(new PropertyValueFactory<>("searchCounty"));

        TableColumn<SearchLogEntry, String> searchCommentsColumn = new TableColumn<>("Comments");
        searchCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("searchComments"));

        TableColumn<SearchLogEntry, String> searchedPersonsColumn = new TableColumn<>("Searched Persons");
        searchedPersonsColumn.setCellValueFactory(new PropertyValueFactory<>("searchedPersons"));

        // Create a list to hold all the TableColumn instances for searchTable
        ObservableList<TableColumn<SearchLogEntry, ?>> searchColumns = FXCollections.observableArrayList(
                searchNumberColumn,
                searchDateColumn,
                searchTimeColumn,
                searchSeizedItemsColumn,
                searchGroundsColumn,
                searchTypeColumn,
                searchMethodColumn,
                searchWitnessesColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerAgencyColumn,
                officerDivisionColumn,
                searchStreetColumn,
                searchAreaColumn,
                searchCountyColumn,
                searchCommentsColumn,
                searchedPersonsColumn
        );

// Add all columns to searchTable
        searchTable.getColumns().addAll(searchColumns);

        for (TableColumn<SearchLogEntry, ?> column : searchColumns) {
            column.setMinWidth(minColumnWidth);
        }
    }

    public void incidentLogUpdate(List<IncidentLogEntry> logEntries) {
        // Clear existing data
        incidentTable.getItems().clear();
        incidentTable.getItems().addAll(logEntries);
    }

    public void initializeIncidentColumns() {
        // Create columns for each property of IncidentLogEntry
        TableColumn<IncidentLogEntry, String> incidentNumberColumn = new TableColumn<>("Incident #");
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

        // Create a list to hold all the TableColumn instances for incidentTable
        ObservableList<TableColumn<IncidentLogEntry, ?>> incidentColumns = FXCollections.observableArrayList(
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

// Add all columns to incidentTable
        incidentTable.getColumns().addAll(incidentColumns);
        for (TableColumn<IncidentLogEntry, ?> column : incidentColumns) {
            column.setMinWidth(minColumnWidth);
        }
    }

    public void trafficStopLogUpdate(List<TrafficStopLogEntry> logEntries) {
        // Clear existing data
        trafficStopTable.getItems().clear();
        trafficStopTable.getItems().addAll(logEntries);
    }

    public void initializeTrafficStopColumns() {// Create columns for each property of TrafficStopLogEntry
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

        TableColumn<TrafficStopLogEntry, String> stopNumberColumn = new TableColumn<>("Stop #");
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

        TableColumn<TrafficStopLogEntry, String> makeColumn = new TableColumn<>("Make");
        makeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseMake"));

        TableColumn<TrafficStopLogEntry, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseModel"));

        TableColumn<TrafficStopLogEntry, String> otherInfoColumn = new TableColumn<>("Other Info");
        otherInfoColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseOtherInfo"));

        TableColumn<TrafficStopLogEntry, String> operatorNameColumn = new TableColumn<>("Operator Name");
        operatorNameColumn.setCellValueFactory(new PropertyValueFactory<>("operatorName"));

        TableColumn<TrafficStopLogEntry, String> operatorGenderColumn = new TableColumn<>("Operator Gender");
        operatorGenderColumn.setCellValueFactory(new PropertyValueFactory<>("operatorGender"));

        TableColumn<TrafficStopLogEntry, String> operatorDescriptionColumn = new TableColumn<>("Operator Description");
        operatorDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("operatorDescription"));

        TableColumn<TrafficStopLogEntry, String> operatorAddressColumn = new TableColumn<>("Operator Address");
        operatorAddressColumn.setCellValueFactory(new PropertyValueFactory<>("operatorAddress"));

        // Create a list to hold all the TableColumn instances for trafficStopTable
        ObservableList<TableColumn<TrafficStopLogEntry, ?>> trafficStopColumns = FXCollections.observableArrayList(
                stopNumberColumn,
                dateColumn,
                timeColumn,
                makeColumn,
                modelColumn,
                otherInfoColumn,
                operatorNameColumn,
                operatorAddressColumn,
                operatorDescriptionColumn,
                operatorGenderColumn,
                nameColumn,
                rankColumn,
                numberColumn,
                divisionColumn,
                agencyColumn,
                violationsTextAreaColumn,
                commentsTextAreaColumn,
                actionsTextAreaColumn,
                streetColumn,
                countyColumn,
                areaColumn,
                plateNumberColumn,
                colorColumn,
                typeColumn
        );

        // Add all columns to trafficStopTable
        trafficStopTable.getColumns().addAll(trafficStopColumns);
        for (TableColumn<TrafficStopLogEntry, ?> column : trafficStopColumns) {
            column.setMinWidth(minColumnWidth);
        }
    }

    public void calloutLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        calloutTable.getItems().addAll(logEntries);
    }

    public void initializeCalloutColumns() {
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

        // Create a list to hold all the TableColumn instances
        ObservableList<TableColumn<CalloutLogEntry, ?>> columns = FXCollections.observableArrayList(
                calloutNumberColumn,
                dateColumn,
                timeColumn,
                notesTextAreaColumn,
                responseGradeColumn,
                responseTypeColumn,
                divisionColumn,
                agencyColumn,
                numberColumn,
                rankColumn,
                nameColumn,
                addressColumn,
                countyColumn,
                areaColumn
        );
        calloutTable.getColumns().addAll(columns);
        for (TableColumn<CalloutLogEntry, ?> column : columns) {
            column.setMinWidth(minColumnWidth);
        }
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
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
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
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
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
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
        ;
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
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
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
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
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
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
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
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
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
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
    }

    public void onRefreshButtonClick(ActionEvent actionEvent) {
        loadLogs();
    }

    public void aboutBtnClick(ActionEvent actionEvent) {
        setDisable(codesPane);
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        setActive(infoPane);
        logPane.setVisible(false);
        logPane.setDisable(true);
    }

    public void clearDataLogs() {
        try {
            // Get the path to the DataLogs folder
            String dataLogsFolderPath = getDataLogsFolderPath();

            // Print the path for debugging
            System.out.println("DataLogs folder path: " + dataLogsFolderPath);

            // Check if the DataLogs folder exists
            File dataLogsFolder = new File(dataLogsFolderPath);
            if (dataLogsFolder.exists() && dataLogsFolder.isDirectory()) {
                System.out.println("DataLogs folder exists.");

                // Get a list of files in the DataLogs folder
                File[] files = dataLogsFolder.listFiles();

                if (files != null) {
                    // Delete each file in the DataLogs folder
                    for (File file : files) {
                        if (file.isFile()) {
                            try {
                                Files.deleteIfExists(file.toPath());
                                System.out.println("Deleted file: " + file.getName());
                            } catch (IOException e) {
                                System.err.println("Failed to delete file: " + file.getName() + " " + e.getMessage());
                            }
                        }
                    }
                    System.out.println("All files in DataLogs folder deleted successfully.");
                } else {
                    System.out.println("DataLogs folder is empty.");
                }
            } else {
                System.out.println("DataLogs folder does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearLogsBtnClick(ActionEvent actionEvent) {
        clearDataLogs();
    }

    public void clearAllSaveDataBtnClick(ActionEvent actionEvent) {
        clearDataLogs();
        clearConfig();
    }
}