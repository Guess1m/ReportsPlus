package com.drozal.dataterminal;

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
import com.drozal.dataterminal.util.stringUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.List;

import static com.drozal.dataterminal.util.windowUtils.toggleWindowedFullscreen;

public class LogBrowserController {
    double minColumnWidth = 185.0;
    @javafx.fxml.FXML
    private TableView trafficStopTable;
    @javafx.fxml.FXML
    private TableView arrestTable;
    @javafx.fxml.FXML
    private TableView patrolTable;
    @javafx.fxml.FXML
    private TableView parkingCitationTable;
    @javafx.fxml.FXML
    private TableView impoundTable;
    @javafx.fxml.FXML
    private TableView citationTable;
    @javafx.fxml.FXML
    private TableView calloutTable;
    @javafx.fxml.FXML
    private TableView incidentTable;
    @javafx.fxml.FXML
    private TableView searchTable;
    private double xOffset = 0;
    private double yOffset = 0;
    @javafx.fxml.FXML
    private AnchorPane vbox;

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
        //CalloutTable
        List<CalloutLogEntry> calloutLogEntryList = CalloutReportLogs.extractLogEntries(stringUtil.calloutLogURL);
        calloutLogUpdate(calloutLogEntryList);
    }

    public void parkingCitationLogUpdate(List<ParkingCitationLogEntry> logEntries) {
        // Clear existing data
        parkingCitationTable.getItems().clear();
        parkingCitationTable.getItems().addAll(logEntries);
    }

    public void impoundLogUpdate(List<ImpoundLogEntry> logEntries) {
        // Clear existing data
        impoundTable.getItems().clear();
        // Add data to the table
        impoundTable.getItems().addAll(logEntries);
    }

    public void citationLogUpdate(List<TrafficCitationLogEntry> logEntries) {
        // Clear existing data
        citationTable.getItems().clear();
        citationTable.getItems().addAll(logEntries);
    }

    public void patrolLogUpdate(List<PatrolLogEntry> logEntries) {
        // Clear existing data
        patrolTable.getItems().clear();
        patrolTable.getItems().addAll(logEntries);
    }

    public void arrestLogUpdate(List<ArrestLogEntry> logEntries) {
        // Clear existing data
        arrestTable.getItems().clear();
        arrestTable.getItems().addAll(logEntries);
    }

    public void searchLogUpdate(List<SearchLogEntry> logEntries) {
        // Clear existing data
        searchTable.getItems().clear();
        searchTable.getItems().addAll(logEntries);
    }

    public void incidentLogUpdate(List<IncidentLogEntry> logEntries) {
        // Clear existing data
        incidentTable.getItems().clear();
        incidentTable.getItems().addAll(logEntries);
    }

    public void trafficStopLogUpdate(List<TrafficStopLogEntry> logEntries) {
        // Clear existing data
        trafficStopTable.getItems().clear();
        trafficStopTable.getItems().addAll(logEntries);
    }

    public void calloutLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        calloutTable.getItems().addAll(logEntries);
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

    public void initializeArrestColumns() {

        // TODO: Controller, log entry, report logs, here

        // Create columns for each property of ArrestLogEntry
        TableColumn<ArrestLogEntry, String> arrestNumberColumn = new TableColumn<>("Arrest #");
        arrestNumberColumn.setCellValueFactory(new PropertyValueFactory<>("arrestNumber"));

        TableColumn<ArrestLogEntry, String> arrestDateColumn = new TableColumn<>("Arrest Date");
        arrestDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDate"));

        TableColumn<ArrestLogEntry, String> arrestTimeColumn = new TableColumn<>("Arrest Time");
        arrestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrestTime"));

        TableColumn<ArrestLogEntry, String> arrestChargesColumn = new TableColumn<>("Charges");
        arrestChargesColumn.setCellValueFactory(new PropertyValueFactory<>("arrestCharges"));

        TableColumn<ArrestLogEntry, String> arrestCountyColumn = new TableColumn<>("Arrest County");
        arrestCountyColumn.setCellValueFactory(new PropertyValueFactory<>("arrestCounty"));

        TableColumn<ArrestLogEntry, String> arrestAreaColumn = new TableColumn<>("Arrest Area");
        arrestAreaColumn.setCellValueFactory(new PropertyValueFactory<>("arrestArea"));

        TableColumn<ArrestLogEntry, String> arrestStreetColumn = new TableColumn<>("Arrest Street");
        arrestStreetColumn.setCellValueFactory(new PropertyValueFactory<>("arrestStreet"));

        TableColumn<ArrestLogEntry, String> arresteeNameColumn = new TableColumn<>("Arrestee Name");
        arresteeNameColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeName"));

        TableColumn<ArrestLogEntry, String> arresteeAgeColumn = new TableColumn<>("Arrestee Age/DOB");
        arresteeAgeColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeAge"));

        TableColumn<ArrestLogEntry, String> arresteeGenderColumn = new TableColumn<>("Arrestee Gender");
        arresteeGenderColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeGender"));

        TableColumn<ArrestLogEntry, String> arresteeEthnicityColumn = new TableColumn<>("Arrestee Ethnicity");
        arresteeEthnicityColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeEthnicity"));

        TableColumn<ArrestLogEntry, String> arresteeDescriptionColumn = new TableColumn<>("Arrestee Description");
        arresteeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeDescription"));

        TableColumn<ArrestLogEntry, String> ambulanceYesNoColumn = new TableColumn<>("Ambulance Requested");
        ambulanceYesNoColumn.setCellValueFactory(new PropertyValueFactory<>("ambulanceYesNo"));

        TableColumn<ArrestLogEntry, String> taserYesNoColumn = new TableColumn<>("Taser Deployed");
        taserYesNoColumn.setCellValueFactory(new PropertyValueFactory<>("TaserYesNo"));

        TableColumn<ArrestLogEntry, String> arresteeMedicalInformationColumn = new TableColumn<>("Medical Information");
        arresteeMedicalInformationColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeMedicalInformation"));

        TableColumn<ArrestLogEntry, String> arresteeHomeAddressColumn = new TableColumn<>("Home Address");
        arresteeHomeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeHomeAddress"));

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
                arrestChargesColumn,
                arrestCountyColumn,
                arrestAreaColumn,
                arrestStreetColumn,
                arresteeNameColumn,
                arresteeAgeColumn,
                arresteeGenderColumn,
                arresteeEthnicityColumn,
                arresteeDescriptionColumn,
                ambulanceYesNoColumn,
                taserYesNoColumn,
                arresteeMedicalInformationColumn,
                arresteeHomeAddressColumn,
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

        TableColumn<IncidentLogEntry, String> incidentActionsTakenColumn = new TableColumn<>("Incident Details");
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

    public void initializeSearchColumns() {

        // Create columns for each property of SearchLogEntry
        TableColumn<SearchLogEntry, String> searchNumberColumn = new TableColumn<>("Search #");
        searchNumberColumn.setCellValueFactory(new PropertyValueFactory<>("SearchNumber"));

        TableColumn<SearchLogEntry, String> searchDateColumn = new TableColumn<>("Search Date");
        searchDateColumn.setCellValueFactory(new PropertyValueFactory<>("searchDate"));

        TableColumn<SearchLogEntry, String> searchTimeColumn = new TableColumn<>("Search Time");
        searchTimeColumn.setCellValueFactory(new PropertyValueFactory<>("searchTime"));

        TableColumn<SearchLogEntry, String> searchSeizedItemsColumn = new TableColumn<>("Details/Field Sobriety");
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

        TableColumn<SearchLogEntry, String> breathalyzerUsedColumn = new TableColumn<>("Breathalyzer Used(Y/N)");
        breathalyzerUsedColumn.setCellValueFactory(new PropertyValueFactory<>("breathalyzerUsed"));

        TableColumn<SearchLogEntry, String> breathalyzerResultColumn = new TableColumn<>("Result(P/F)");
        breathalyzerResultColumn.setCellValueFactory(new PropertyValueFactory<>("breathalyzerResult"));

        TableColumn<SearchLogEntry, String> BACMeasurementColumn = new TableColumn<>("BAC Measurement");
        BACMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("breathalyzerBACMeasure"));

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
                searchedPersonsColumn,
                breathalyzerUsedColumn,
                breathalyzerResultColumn,
                BACMeasurementColumn
        );

// Add all columns to searchTable
        searchTable.getColumns().addAll(searchColumns);

        for (TableColumn<SearchLogEntry, ?> column : searchColumns) {
            column.setMinWidth(minColumnWidth);
        }
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

    @javafx.fxml.FXML
    public void onRefreshButtonClick(ActionEvent actionEvent) {
        loadLogs();
    }

    @javafx.fxml.FXML
    public void onMousePress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    @javafx.fxml.FXML
    public void onMouseDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @javafx.fxml.FXML
    public void onExitButtonClick(MouseEvent actionEvent) {
        // Get the window associated with the scene
        Window window = vbox.getScene().getWindow();

        // Close the window
        window.hide(); // or window.close() if you want to force close
    }


    @javafx.fxml.FXML
    public void onFullscreenBtnClick(Event event) {
        Stage stage = (Stage) calloutTable.getScene().getWindow();
        if (stage != null) {
            toggleWindowedFullscreen(stage, 891, 472);
        }
    }


}
