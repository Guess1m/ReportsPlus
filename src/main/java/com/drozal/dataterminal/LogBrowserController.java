package com.drozal.dataterminal;

import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.Impound.ImpoundLogEntry;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.logs.Incident.IncidentLogEntry;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import com.drozal.dataterminal.logs.Patrol.PatrolLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import com.drozal.dataterminal.logs.Search.SearchLogEntry;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationLogEntry;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReportLogs;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopLogEntry;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportLogs;
import com.drozal.dataterminal.util.stringUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.util.List;

import static com.drozal.dataterminal.util.controllerUtils.showButtonAnimation;
import static com.drozal.dataterminal.util.windowUtils.toggleWindowedFullscreen;

public class LogBrowserController {

    //<editor-fold desc="FXML / Vars">
    double minColumnWidth = 185.0;
    @javafx.fxml.FXML
    private TableView trafficStopTable;
    @javafx.fxml.FXML
    private TableView arrestTable;
    @javafx.fxml.FXML
    private TableView patrolTable;
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
    @javafx.fxml.FXML
    private Button refreshBtn;
    @javafx.fxml.FXML
    private TabPane tabPane;
    @javafx.fxml.FXML
    private TextField calnum;
    @javafx.fxml.FXML
    private TextField caladdress;
    @javafx.fxml.FXML
    private TextField calnotes;
    @javafx.fxml.FXML
    private TextField calcounty;
    @javafx.fxml.FXML
    private TextField calgrade;
    @javafx.fxml.FXML
    private TextField calarea;
    @javafx.fxml.FXML
    private TextField caltype;
    @javafx.fxml.FXML
    private Label calupdatedlabel;
    @javafx.fxml.FXML
    private HBox calloutInfo;

    private CalloutLogEntry calloutEntry;
    private PatrolLogEntry patrolEntry;
    private TrafficStopLogEntry trafficStopEntry;
    private IncidentLogEntry incidentEntry;

    @javafx.fxml.FXML
    private HBox patrolInfo;
    @javafx.fxml.FXML
    private TextField patvehicle;
    @javafx.fxml.FXML
    private Label patupdatedlabel;
    @javafx.fxml.FXML
    private TextField patstoptime;
    @javafx.fxml.FXML
    private TextField patstarttime;
    @javafx.fxml.FXML
    private TextField patlength;
    @javafx.fxml.FXML
    private TextField patcomments;
    @javafx.fxml.FXML
    private TextField patnum;
    @javafx.fxml.FXML
    private Tab calloutTab;
    @javafx.fxml.FXML
    private Tab patrolTab;
    @javafx.fxml.FXML
    private TextField trafstreet;
    @javafx.fxml.FXML
    private TextField trafotherinfo;
    @javafx.fxml.FXML
    private TextField trafname;
    @javafx.fxml.FXML
    private TextField trafcomments;
    @javafx.fxml.FXML
    private TextField trafdesc;
    @javafx.fxml.FXML
    private TextField trafcolor;
    @javafx.fxml.FXML
    private Tab trafficStopTab;
    @javafx.fxml.FXML
    private TextField trafnum;
    @javafx.fxml.FXML
    private TextField trafmodel;
    @javafx.fxml.FXML
    private TextField trafaddress;
    @javafx.fxml.FXML
    private TextField trafarea;
    @javafx.fxml.FXML
    private TextField trafgender;
    @javafx.fxml.FXML
    private TextField traftype;
    @javafx.fxml.FXML
    private TextField trafplatenum;
    @javafx.fxml.FXML
    private TextField trafcounty;
    @javafx.fxml.FXML
    private HBox trafficStopInfo;
    @javafx.fxml.FXML
    private Label trafupdatedlabel;
    @javafx.fxml.FXML
    private AnchorPane lowerPane;
    @javafx.fxml.FXML
    private ToggleButton showManagerToggle;
    @javafx.fxml.FXML
    private TextField incnum;
    @javafx.fxml.FXML
    private Label incupdatedlabel;
    @javafx.fxml.FXML
    private TextField incactionstaken;
    @javafx.fxml.FXML
    private TextField incarea;
    @javafx.fxml.FXML
    private TextField inccounty;
    @javafx.fxml.FXML
    private TextField inccomments;
    @javafx.fxml.FXML
    private HBox incidentInfo;
    @javafx.fxml.FXML
    private TextField incstreet;
    @javafx.fxml.FXML
    private TextField incvictims;
    @javafx.fxml.FXML
    private TextField incstatement;
    @javafx.fxml.FXML
    private TextField incwitness;
    @javafx.fxml.FXML
    private Tab incidentTab;
    //</editor-fold>

    // TODO: Controller, log entry, report logs, initializeColumns


    public void initialize() {
        initializeCalloutColumns();
        initializeArrestColumns();
        initializeCitationColumns();
        initializeImpoundColumns();
        initializeIncidentColumns();
        initializePatrolColumns();
        initializeSearchColumns();
        initializeTrafficStopColumns();
        loadLogs();

        calloutInfo.setVisible(true);
        lowerPane.setPrefHeight(0);
        lowerPane.setMaxHeight(0);
        lowerPane.setMinHeight(0);
        lowerPane.setVisible(false);

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {

            calloutInfo.setVisible(newTab != null && "calloutTab".equals(newTab.getId()));

            patrolInfo.setVisible(newTab != null && "patrolTab".equals(newTab.getId()));

            trafficStopInfo.setVisible(newTab != null && "trafficStopTab".equals(newTab.getId()));

            incidentInfo.setVisible(newTab != null && "incidentTab".equals(newTab.getId()));
        });
    }

    private void loadLogs() {
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

    //<editor-fold desc="Log Updates">
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
    //</editor-fold>

    //<editor-fold desc="Column Init.">
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

        TableColumn<TrafficCitationLogEntry, String> citationChargesColumn = new TableColumn<>("Charges");
        citationChargesColumn.setCellValueFactory(new PropertyValueFactory<>("citationCharges"));

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

        TableColumn<TrafficCitationLogEntry, String> offenderAgeColumn = new TableColumn<>("Offender Age");
        offenderAgeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderAge"));

        TableColumn<TrafficCitationLogEntry, String> offenderHomeAddressColumn = new TableColumn<>("Offender Address");
        offenderHomeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("offenderHomeAddress"));

        TableColumn<TrafficCitationLogEntry, String> offenderDescriptionColumn = new TableColumn<>("Offender Description");
        offenderDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("offenderDescription"));

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
                citationChargesColumn,
                citationCountyColumn,
                citationAreaColumn,
                citationStreetColumn,
                offenderNameColumn,
                offenderGenderColumn,
                offenderAgeColumn,
                offenderHomeAddressColumn,
                offenderDescriptionColumn,
                offenderVehicleModelColumn,
                offenderVehicleColorColumn,
                offenderVehicleTypeColumn,
                offenderVehiclePlateColumn,
                offenderVehicleOtherColumn,
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

        TableColumn<TrafficStopLogEntry, String> commentsTextAreaColumn = new TableColumn<>("Comments");
        commentsTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("CommentsTextArea"));

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
                commentsTextAreaColumn,
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
    //</editor-fold>

    //<editor-fold desc="Events">
    @javafx.fxml.FXML
    public void onRefreshButtonClick(ActionEvent actionEvent) {
        loadLogs();
        showButtonAnimation(refreshBtn);
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
    public void onManagerToggle(ActionEvent actionEvent) {
        if (!showManagerToggle.isSelected()) {
            lowerPane.setPrefHeight(0);
            lowerPane.setMaxHeight(0);
            lowerPane.setMinHeight(0);
            lowerPane.setVisible(false);
        } else {
            lowerPane.setPrefHeight(356);
            lowerPane.setMaxHeight(356);
            lowerPane.setMinHeight(356);
            lowerPane.setVisible(true);
        }
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
            toggleWindowedFullscreen(stage, 891, 674);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Info Panes">
    // Callout Section
    @javafx.fxml.FXML
    public void onCalloutRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            calloutEntry = (CalloutLogEntry) calloutTable.getSelectionModel().getSelectedItem();
            if (calloutEntry != null) {
                calnum.setText(calloutEntry.getCalloutNumber());
                caladdress.setText(calloutEntry.getAddress());
                calnotes.setText(calloutEntry.getNotesTextArea());
                calcounty.setText(calloutEntry.getCounty());
                calgrade.setText(calloutEntry.getResponseGrade());
                calarea.setText(calloutEntry.getArea());
                caltype.setText(calloutEntry.getResponeType());
            }
        }
    }

    @javafx.fxml.FXML
    public void onCalUpdateValues(ActionEvent actionEvent) {
        if (calloutEntry != null) {
            calupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                calupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            // Update the selected log entry with values from text fields
            calloutEntry.CalloutNumber = calnum.getText();
            calloutEntry.Address = caladdress.getText();
            calloutEntry.NotesTextArea = calnotes.getText();
            calloutEntry.County = calcounty.getText();
            calloutEntry.ResponseGrade = calgrade.getText();
            calloutEntry.Area = calarea.getText();
            calloutEntry.ResponeType = caltype.getText();

            // Load existing logs from XML
            List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (CalloutLogEntry entry : logs) {
                if (entry.getDate().equals(calloutEntry.getDate()) &&
                        entry.getTime().equals(calloutEntry.getTime())) {
                    entry.CalloutNumber = calnum.getText();
                    entry.Address = caladdress.getText();
                    entry.NotesTextArea = calnotes.getText();
                    entry.County = calcounty.getText();
                    entry.ResponseGrade = calgrade.getText();
                    entry.Area = calarea.getText();
                    entry.ResponeType = caltype.getText();
                    break;
                }
            }

            // Save the updated logs back to XML
            CalloutReportLogs.saveLogsToXML(logs);

            // Optionally, you might want to update the TableView here
            calloutTable.refresh();

        }
    }

    // Patrol Section
    @javafx.fxml.FXML
    public void onPatUpdateValues(ActionEvent actionEvent) {
        if (patrolEntry != null) {
            patupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                patupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            // Update the selected log entry with values from text fields
            patrolEntry.patrolNumber = patnum.getText();
            patrolEntry.patrolComments = patcomments.getText();
            patrolEntry.patrolLength = patlength.getText();
            patrolEntry.patrolStartTime = patstarttime.getText();
            patrolEntry.patrolStopTime = patstoptime.getText();
            patrolEntry.officerVehicle = patvehicle.getText();

            // Load existing logs from XML
            List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (PatrolLogEntry entry : logs) {
                if (entry.getPatrolDate().equals(patrolEntry.getPatrolDate())) {
                    entry.patrolNumber = patnum.getText();
                    entry.patrolComments = patcomments.getText();
                    entry.patrolLength = patlength.getText();
                    entry.patrolStartTime = patstarttime.getText();
                    entry.patrolStopTime = patstoptime.getText();
                    entry.officerVehicle = patvehicle.getText();

                    break;
                }
            }

            // Save the updated logs back to XML
            PatrolReportLogs.saveLogsToXML(logs);

            // Optionally, you might want to update the TableView here
            patrolTable.refresh();

        }
    }

    @javafx.fxml.FXML
    public void onPatrolRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            patrolEntry = (PatrolLogEntry) patrolTable.getSelectionModel().getSelectedItem();
            if (patrolEntry != null) {
                patnum.setText(patrolEntry.getPatrolNumber());
                patcomments.setText(patrolEntry.getPatrolComments());
                patlength.setText(patrolEntry.getPatrolLength());
                patstarttime.setText(patrolEntry.getPatrolStartTime());
                patstoptime.setText(patrolEntry.getPatrolStopTime());
                patvehicle.setText(patrolEntry.getOfficerVehicle());
            }
        }
    }

    // Traffic Stop Section
    @javafx.fxml.FXML
    public void onTrafUpdateValues(ActionEvent actionEvent) {
        if (trafficStopEntry != null) {
            trafupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                trafupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            trafficStopEntry.PlateNumber = trafplatenum.getText();
            trafficStopEntry.Color = trafcolor.getText();
            trafficStopEntry.Type = traftype.getText();
            trafficStopEntry.StopNumber = trafnum.getText();
            trafficStopEntry.ResponseModel = trafmodel.getText();
            trafficStopEntry.ResponseOtherInfo = trafotherinfo.getText();
            trafficStopEntry.CommentsTextArea = trafcomments.getText();
            trafficStopEntry.County = trafcounty.getText();
            trafficStopEntry.Area = trafarea.getText();
            trafficStopEntry.Street = trafstreet.getText();
            trafficStopEntry.operatorName = trafname.getText();
            trafficStopEntry.operatorDescription = trafdesc.getText();
            trafficStopEntry.operatorAddress = trafaddress.getText();
            trafficStopEntry.operatorGender = trafgender.getText();

            // Load existing logs from XML
            List<TrafficStopLogEntry> logs = TrafficStopReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (TrafficStopLogEntry entry : logs) {
                if (entry.getDate().equals(trafficStopEntry.getDate()) &&
                        entry.getTime().equals(trafficStopEntry.getTime())) {
                    entry.PlateNumber = trafplatenum.getText();
                    entry.Color = trafcolor.getText();
                    entry.Type = traftype.getText();
                    entry.StopNumber = trafnum.getText();
                    entry.ResponseModel = trafmodel.getText();
                    entry.ResponseOtherInfo = trafotherinfo.getText();
                    entry.CommentsTextArea = trafcomments.getText();
                    entry.County = trafcounty.getText();
                    entry.Area = trafarea.getText();
                    entry.Street = trafstreet.getText();
                    entry.operatorName = trafname.getText();
                    entry.operatorDescription = trafdesc.getText();
                    entry.operatorAddress = trafaddress.getText();
                    entry.operatorGender = trafgender.getText();
                    break;
                }
            }

            // Save the updated logs back to XML
            TrafficStopReportLogs.saveLogsToXML(logs);

            // Optionally, you might want to update the TableView here
            trafficStopTable.refresh();

        }
    }

    @javafx.fxml.FXML
    public void onTrafficStopRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            trafficStopEntry = (TrafficStopLogEntry) trafficStopTable.getSelectionModel().getSelectedItem();
            if (trafficStopEntry != null) {
                trafstreet.setText(trafficStopEntry.getStreet());
                trafotherinfo.setText(trafficStopEntry.getResponseOtherInfo());
                trafname.setText(trafficStopEntry.getOperatorName());
                trafcomments.setText(trafficStopEntry.getCommentsTextArea());
                trafdesc.setText(trafficStopEntry.getOperatorDescription());
                trafcolor.setText(trafficStopEntry.getColor());
                trafnum.setText(trafficStopEntry.getStopNumber());
                trafmodel.setText(trafficStopEntry.getResponseModel());
                trafaddress.setText(trafficStopEntry.getOperatorAddress());
                trafarea.setText(trafficStopEntry.getArea());
                trafgender.setText(trafficStopEntry.getOperatorGender());
                traftype.setText(trafficStopEntry.getType());
                trafplatenum.setText(trafficStopEntry.getPlateNumber());
                trafcounty.setText(trafficStopEntry.getCounty());
            }
        }
    }

    // Incident Section
    @javafx.fxml.FXML
    public void onIncUpdateValues(ActionEvent actionEvent) {
        if (incidentEntry != null) {
            incupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            incidentEntry.incidentNumber = incnum.getText();
            incidentEntry.incidentActionsTaken = incactionstaken.getText();
            incidentEntry.incidentArea = incarea.getText();
            incidentEntry.incidentCounty = inccounty.getText();
            incidentEntry.incidentComments = inccomments.getText();
            incidentEntry.incidentStreet = incstreet.getText();
            incidentEntry.incidentVictims = incvictims.getText();
            incidentEntry.incidentStatement = incstatement.getText();
            incidentEntry.incidentWitnesses = incwitness.getText();

            // Load existing logs from XML
            List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (IncidentLogEntry entry : logs) {
                if (entry.getIncidentDate().equals(incidentEntry.getIncidentDate()) && entry.getIncidentTime().equals(incidentEntry.getIncidentTime())) {
                    entry.incidentNumber = incnum.getText();
                    entry.incidentStatement = incstatement.getText();
                    entry.incidentWitnesses = incwitness.getText();
                    entry.incidentVictims = incvictims.getText();
                    entry.incidentStreet = incstreet.getText();
                    entry.incidentArea = incarea.getText();
                    entry.incidentCounty = inccounty.getText();
                    entry.incidentActionsTaken = incactionstaken.getText();
                    entry.incidentComments = inccomments.getText();
                    break;
                }
            }

            // Save the updated logs back to XML
            IncidentReportLogs.saveLogsToXML(logs);

            // Optionally, you might want to update the TableView here
            incidentTable.refresh();
        }
    }

    @javafx.fxml.FXML
    public void onIncidentRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            incidentEntry = (IncidentLogEntry) incidentTable.getSelectionModel().getSelectedItem();
            if (incidentEntry != null) {
                incnum.setText(incidentEntry.incidentNumber);
                incactionstaken.setText(incidentEntry.incidentActionsTaken);
                incarea.setText(incidentEntry.incidentArea);
                inccounty.setText(incidentEntry.incidentCounty);
                inccomments.setText(incidentEntry.incidentComments);
                incstreet.setText(incidentEntry.incidentStreet);
                incvictims.setText(incidentEntry.incidentVictims);
                incstatement.setText(incidentEntry.incidentStatement);
                incwitness.setText(incidentEntry.incidentWitnesses);
            }
        }
    }
    //</editor-fold>

}
