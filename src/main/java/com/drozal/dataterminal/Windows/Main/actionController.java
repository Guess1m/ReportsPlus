package com.drozal.dataterminal.Windows.Main;

import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Other.NotesViewController;
import com.drozal.dataterminal.Windows.Server.ClientController;
import com.drozal.dataterminal.Windows.Settings.settingsController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Accident.AccidentReport;
import com.drozal.dataterminal.logs.Accident.AccidentReportUtils;
import com.drozal.dataterminal.logs.Accident.AccidentReports;
import com.drozal.dataterminal.logs.Arrest.ArrestReport;
import com.drozal.dataterminal.logs.Arrest.ArrestReportUtils;
import com.drozal.dataterminal.logs.Arrest.ArrestReports;
import com.drozal.dataterminal.logs.Callout.CalloutReport;
import com.drozal.dataterminal.logs.Callout.CalloutReportUtils;
import com.drozal.dataterminal.logs.Callout.CalloutReports;
import com.drozal.dataterminal.logs.ChargesData;
import com.drozal.dataterminal.logs.CitationsData;
import com.drozal.dataterminal.logs.Death.DeathReport;
import com.drozal.dataterminal.logs.Death.DeathReportUtils;
import com.drozal.dataterminal.logs.Death.DeathReports;
import com.drozal.dataterminal.logs.Impound.ImpoundReport;
import com.drozal.dataterminal.logs.Impound.ImpoundReportUtils;
import com.drozal.dataterminal.logs.Impound.ImpoundReports;
import com.drozal.dataterminal.logs.Incident.IncidentReport;
import com.drozal.dataterminal.logs.Incident.IncidentReportUtils;
import com.drozal.dataterminal.logs.Incident.IncidentReports;
import com.drozal.dataterminal.logs.Patrol.PatrolReport;
import com.drozal.dataterminal.logs.Patrol.PatrolReportUtils;
import com.drozal.dataterminal.logs.Patrol.PatrolReports;
import com.drozal.dataterminal.logs.Search.SearchReport;
import com.drozal.dataterminal.logs.Search.SearchReportUtils;
import com.drozal.dataterminal.logs.Search.SearchReports;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReport;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReports;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReport;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportUtils;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReports;
import com.drozal.dataterminal.util.CourtData.Case;
import com.drozal.dataterminal.util.CourtData.CourtCases;
import com.drozal.dataterminal.util.CourtData.CourtUtils;
import com.drozal.dataterminal.util.CourtData.CustomCaseCell;
import com.drozal.dataterminal.util.History.Ped;
import com.drozal.dataterminal.util.History.Vehicle;
import com.drozal.dataterminal.util.Misc.*;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.server.ClientUtils;
import jakarta.xml.bind.JAXBException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.Windows.Other.NotesViewController.createNoteTabs;
import static com.drozal.dataterminal.Windows.Other.NotesViewController.notesTabList;
import static com.drozal.dataterminal.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.drozal.dataterminal.logs.Accident.AccidentReportUtils.newAccident;
import static com.drozal.dataterminal.logs.Arrest.ArrestReportUtils.newArrest;
import static com.drozal.dataterminal.logs.Callout.CalloutReportUtils.newCallout;
import static com.drozal.dataterminal.logs.Death.DeathReportUtils.newDeathReport;
import static com.drozal.dataterminal.logs.Impound.ImpoundReportUtils.newImpound;
import static com.drozal.dataterminal.logs.Incident.IncidentReportUtils.newIncident;
import static com.drozal.dataterminal.logs.Patrol.PatrolReportUtils.newPatrol;
import static com.drozal.dataterminal.logs.Search.SearchReportUtils.newSearch;
import static com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportUtils.newTrafficStop;
import static com.drozal.dataterminal.util.CourtData.CourtUtils.*;
import static com.drozal.dataterminal.util.History.Ped.PedHistoryUtils.findPedByName;
import static com.drozal.dataterminal.util.History.Vehicle.VehicleHistoryUtils.findVehicleByNumber;
import static com.drozal.dataterminal.util.History.Vehicle.VehicleHistoryUtils.generateInspectionStatus;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.CalloutManager.handleSelectedNodeActive;
import static com.drozal.dataterminal.util.Misc.CalloutManager.handleSelectedNodeHistory;
import static com.drozal.dataterminal.util.Misc.InitTableColumns.*;
import static com.drozal.dataterminal.util.Misc.LogUtils.*;
import static com.drozal.dataterminal.util.Misc.NotificationManager.showNotificationInfo;
import static com.drozal.dataterminal.util.Misc.NotificationManager.showNotificationWarning;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.dropdownInfo.vehicleTypes;
import static com.drozal.dataterminal.util.Misc.stringUtil.*;
import static com.drozal.dataterminal.util.Misc.updateUtil.checkForUpdates;
import static com.drozal.dataterminal.util.Misc.updateUtil.gitVersion;
import static com.drozal.dataterminal.util.Report.treeViewUtils.addChargesToTable;
import static com.drozal.dataterminal.util.Report.treeViewUtils.addCitationsToTable;
import static com.drozal.dataterminal.util.Window.windowUtils.*;
import static com.drozal.dataterminal.util.server.recordUtils.grabPedData;
import static com.drozal.dataterminal.util.server.recordUtils.grabVehicleData;

public class actionController {
	
	//<editor-fold desc="FXML Elements">
	
	@FXML
	private TextField vehpolicefield;
	@FXML
	private Label plt8;
	@FXML
	private Button revealOutcomeBtn;
	@FXML
	private Label caldetlbl2;
	@FXML
	private Label caldetlbl1;
	@FXML
	private Label caldetlbl4;
	@FXML
	private Label caldetlbl3;
	@FXML
	private Label caldetlbl6;
	@FXML
	private Label caldetlbl5;
	@FXML
	private Label caldetlbl8;
	@FXML
	private Label caldetlbl7;
	@FXML
	private Label caldetlbl9;
	@FXML
	private TextField pedgunlicensestatusfield;
	@FXML
	private TextField pedprobationstatusfield;
	@FXML
	private TextField pedfishinglicstatusfield;
	@FXML
	private TextField pedboatinglicstatusfield;
	@FXML
	private TextField pedgunlicenseclassfield;
	@FXML
	private TextField pedgunlicensetypefield;
	@FXML
	private TextField pedhuntinglicstatusfield;
	@FXML
	private TextField pedaffiliationfield;
	@FXML
	private TextField pedlicnumfield;
	@FXML
	private TextField peddescfield;
	@FXML
	private TextField pedparolestatusfield;
	@FXML
	private TextField pedaliasfield;
	@FXML
	private TextField pedtimesstoppedfield;
	@FXML
	private Label ped13;
	@FXML
	private Label ped12;
	@FXML
	private Label ped15;
	@FXML
	private Label ped14;
	@FXML
	private Label ped17;
	@FXML
	private Label ped16;
	@FXML
	private Label ped19;
	@FXML
	private Label ped18;
	@FXML
	private Label ped11;
	@FXML
	private Label ped10;
	@FXML
	private Label ped8;
	@FXML
	private Label ped9;
	@FXML
	private Label ped20;
	@FXML
	private Label ped22;
	@FXML
	private Label ped21;
	@FXML
	private ListView pedarrestpriorslistview;
	@FXML
	private ListView pedcitationpriorslistview;
	@FXML
	private MenuItem deathReportButton;
	@FXML
	private Tab deathTab;
	@FXML
	private TableView deathReportTable;
	@FXML
	public Button notesButton;
	@FXML
	private Label casesec4;
	@FXML
	private Label casesec3;
	@FXML
	private Label casesec2;
	@FXML
	private Label casesec1;
	@FXML
	private Label caseprim1;
	@FXML
	private GridPane caseVerdictPane;
	@FXML
	private Label caseprim2;
	@FXML
	private Label caseprim3;
	@FXML
	private Label caseTotalProbationLabel;
	@FXML
	private Label caseSuspensionDuration;
	@FXML
	private Label caseLicenseStatLabel;
	@FXML
	private Label caseTotalJailTimeLabel;
	@FXML
	private Label caseSuspensionDurationlbl;
	@FXML
	public Button shiftInfoBtn;
	@FXML
	public AnchorPane shiftInformationPane;
	@FXML
	public TextField OfficerInfoName;
	@FXML
	public ComboBox OfficerInfoDivision;
	@FXML
	public ComboBox OfficerInfoAgency;
	@FXML
	public TextField OfficerInfoCallsign;
	@FXML
	public TextField OfficerInfoNumber;
	@FXML
	public ComboBox OfficerInfoRank;
	@FXML
	public Label generatedDateTag;
	@FXML
	public Label generatedByTag;
	@FXML
	public Label updatedNotification;
	@FXML
	public AnchorPane vbox;
	@FXML
	public BarChart reportChart;
	@FXML
	public AnchorPane sidepane;
	@FXML
	public Label mainColor8;
	@FXML
	public Button updateInfoBtn;
	public static NotesViewController notesViewController;
	actionController controller;
	AnchorPane titlebar;
	@FXML
	private Label secondaryColor3Bkg;
	@FXML
	private Label secondaryColor4Bkg;
	@FXML
	private Label secondaryColor5Bkg;
	@FXML
	private Button logsButton;
	@FXML
	private Button mapButton;
	@FXML
	private MenuButton createReportBtn;
	@FXML
	private MenuItem searchReportButton;
	@FXML
	private MenuItem trafficReportButton;
	@FXML
	private MenuItem impoundReportButton;
	@FXML
	private MenuItem incidentReportButton;
	@FXML
	private MenuItem patrolReportButton;
	@FXML
	private MenuItem calloutReportButton;
	@FXML
	private MenuItem arrestReportButton;
	@FXML
	private MenuItem trafficCitationReportButton;
	@FXML
	private AreaChart areaReportChart;
	@FXML
	private Tab searchTab;
	@FXML
	private TableView searchTable;
	@FXML
	private Tab arrestTab;
	@FXML
	private AnchorPane logPane;
	@FXML
	private TableView trafficStopTable;
	@FXML
	private TableView arrestTable;
	@FXML
	private TableView impoundTable;
	@FXML
	private TableView citationTable;
	@FXML
	private Tab citationTab;
	@FXML
	private TableView calloutTable;
	@FXML
	private Tab calloutTab;
	@FXML
	private Tab patrolTab;
	@FXML
	private Tab incidentTab;
	@FXML
	private Tab trafficStopTab;
	@FXML
	private Tab impoundTab;
	@FXML
	private Label reportPlusLabelFill;
	@FXML
	private TableView patrolTable;
	@FXML
	private TabPane tabPane;
	@FXML
	private TableView incidentTable;
	@FXML
	private Label serverStatusLabel;
	@FXML
	private Button showIDBtn;
	@FXML
	private Button showCalloutBtn;
	@FXML
	private Button pedSearchBtn;
	@FXML
	private AnchorPane pedLookupPane;
	@FXML
	private Button vehSearchBtn;
	@FXML
	private AnchorPane vehLookupPane;
	@FXML
	private TextField pedgenfield;
	@FXML
	private TextField peddobfield;
	@FXML
	private TextField pedlicensefield;
	@FXML
	private TextField pedwantedfield;
	@FXML
	private TextField pedlnamefield;
	@FXML
	private TextField pedfnamefield;
	@FXML
	private TextField vehinsfield;
	@FXML
	private TextField vehownerfield;
	@FXML
	private TextField vehregfield;
	@FXML
	private TextField vehstolenfield;
	@FXML
	private TextField vehmodelfield;
	@FXML
	private AnchorPane vehcolordisplay;
	@FXML
	private TextField vehplatefield2;
	@FXML
	private AnchorPane vehRecordPane;
	@FXML
	private Label vehnocolorlabel;
	@FXML
	private Label versionLabel;
	@FXML
	private Label noRecordFoundLabelVeh;
	@FXML
	private AnchorPane pedRecordPane;
	@FXML
	private Label noRecordFoundLabelPed;
	@FXML
	private TextField pedaddressfield;
	@FXML
	private AnchorPane calloutPane;
	@FXML
	private ListView calHistoryList;
	@FXML
	private ListView calActiveList;
	@FXML
	private AnchorPane currentCalPane;
	@FXML
	private ToggleButton showCurrentCalToggle;
	@FXML
	private TextField calPriority;
	@FXML
	private TextField calCounty;
	@FXML
	private TextField calDate;
	@FXML
	private TextField calNum;
	@FXML
	private TextField calTime;
	@FXML
	private TextField calStreet;
	@FXML
	private Label calloutInfoTitle;
	@FXML
	private TextField calArea;
	@FXML
	private TextArea calDesc;
	@FXML
	private TextField calType;
	@FXML
	private Label calfill;
	@FXML
	private Label activecalfill;
	@FXML
	private VBox bkgclr1;
	@FXML
	private VBox bkgclr2;
	@FXML
	private Label logbrwsrlbl;
	@FXML
	private Label plt4;
	@FXML
	private Label plt5;
	@FXML
	private Label plt6;
	@FXML
	private Label plt7;
	@FXML
	private Label plt1;
	@FXML
	private Label plt2;
	@FXML
	private Label plt3;
	@FXML
	private Label ped3;
	@FXML
	private Label ped4;
	@FXML
	private Label ped5;
	@FXML
	private Label ped6;
	@FXML
	private Label ped1;
	@FXML
	private Label ped2;
	@FXML
	private Label ped7;
	@FXML
	private Button showCourtCasesBtn;
	@FXML
	private Label caseTotalLabel;
	@FXML
	private TextField caseNumField;
	@FXML
	private ListView caseOffencesListView;
	@FXML
	private ListView caseList;
	@FXML
	private TextField caseCourtDateField;
	@FXML
	private AnchorPane courtPane;
	@FXML
	private TextField caseAgeField;
	@FXML
	private ListView caseOutcomesListView;
	@FXML
	private TextField caseOffenceDateField;
	@FXML
	private TextField caseStreetField;
	@FXML
	private TextField caseFirstNameField;
	@FXML
	private TextArea caseNotesField;
	@FXML
	private TextField caseAddressField;
	@FXML
	private TextField caseLastNameField;
	@FXML
	private TextField caseGenderField;
	@FXML
	private TextField caseAreaField;
	@FXML
	private TextField caseCountyField;
	@FXML
	private Label caseSec1;
	@FXML
	private Label caseSec2;
	@FXML
	private Label casePrim1;
	@FXML
	private Label caselbl5;
	@FXML
	private Label caselbl4;
	@FXML
	private Label caselbl3;
	@FXML
	private Label caselbl2;
	@FXML
	private Label caselbl1;
	@FXML
	private Label caselbl9;
	@FXML
	private Label caselbl8;
	@FXML
	private Label caselbl7;
	@FXML
	private Label caselbl6;
	@FXML
	private Label caselbl12;
	@FXML
	private Label caselbl11;
	@FXML
	private Label caselbl10;
	@FXML
	private Button deleteCaseBtn;
	@FXML
	private Label noCourtCaseSelectedlbl;
	@FXML
	private AnchorPane blankCourtInfoPane;
	@FXML
	private AnchorPane courtInfoPane;
	@FXML
	private SplitPane lookupSplitPane;
	@FXML
	private AnchorPane lookupPane;
	@FXML
	private CheckBox lookupVehCheckbox;
	@FXML
	private CheckBox lookupPedCheckbox;
	@FXML
	private ScrollPane vehPane;
	@FXML
	private ScrollPane pedPane;
	@FXML
	private Label lookupmainlbl;
	@FXML
	private AnchorPane lookupmainlblpane;
	@FXML
	private Button orientationBtn;
	@FXML
	private Button probabilitySettingsBtn;
	@FXML
	private Button showLookupBtn;
	@FXML
	private ComboBox vehSearchField;
	@FXML
	private ComboBox pedSearchField;
	@FXML
	private MenuItem accidentReportButton;
	@FXML
	private Tab accidentTab;
	@FXML
	private TableView accidentReportTable;
	@FXML
	private Label locationDataLabel;
	@FXML
	private Circle userCircle;
	@FXML
	private Label userLabel;
	@FXML
	private ImageView pedImageView;
	@FXML
	private Label noPedImageFoundlbl;
	@FXML
	private TextField pedflagfield;
	@FXML
	private ImageView vehImageView;
	@FXML
	private Label noVehImageFoundlbl;
	@FXML
	private Label plt10;
	@FXML
	private ComboBox vehtypecombobox;
	@FXML
	private Label plt9;
	@FXML
	private Label ped23;
	@FXML
	private TextField vehinspectionfield;
	
	//</editor-fold>
	
	public void initialize() throws IOException {
		showLookupBtn.setVisible(false);
		showCalloutBtn.setVisible(false);
		showIDBtn.setVisible(false);
		
		blankCourtInfoPane.setVisible(true);
		courtInfoPane.setVisible(false);
		noPedImageFoundlbl.setVisible(false);
		noVehImageFoundlbl.setVisible(false);
		pedRecordPane.setVisible(false);
		noRecordFoundLabelPed.setVisible(false);
		vehRecordPane.setVisible(false);
		noRecordFoundLabelVeh.setVisible(false);
		locationDataLabel.setVisible(false);
		
		if (ConfigReader.configRead("uiSettings", "firstLogin").equals("true")) {
			ConfigWriter.configwrite("uiSettings", "firstLogin", "false");
			log("First Login...", Severity.DEBUG);
		} else {
			log("Not First Login...", Severity.DEBUG);
		}
		
		titlebar = reportUtil.createTitleBar("Reports Plus");
		
		vbox.getChildren().add(titlebar);
		
		AnchorPane.setTopAnchor(titlebar, 0.0);
		AnchorPane.setLeftAnchor(titlebar, 0.0);
		AnchorPane.setRightAnchor(titlebar, 0.0);
		titlebar.setPrefHeight(30);
		
		checkForUpdates();
		
		setDisable(logPane, lookupPane, calloutPane, courtPane);
		setActive(shiftInformationPane);
		needRefresh.set(0);
		needRefresh.addListener((obs, oldValue, newValue) -> {
			if (newValue.equals(1)) {
				loadLogs();
				needRefresh.set(0);
			}
		});
		
		needCourtRefresh.set(0);
		needCourtRefresh.addListener((obs, oldValue, newValue) -> {
			if (newValue.equals(1)) {
				loadCaseLabels(caseList);
				needCourtRefresh.set(0);
			}
		});
		
		notesText = "";
		
		refreshChart();
		updateChartIfMismatch(reportChart);
		
		String name = ConfigReader.configRead("userInfo", "Name");
		String division = ConfigReader.configRead("userInfo", "Division");
		String rank = ConfigReader.configRead("userInfo", "Rank");
		String number = ConfigReader.configRead("userInfo", "Number");
		String agency = ConfigReader.configRead("userInfo", "Agency");
		String callsign = ConfigReader.configRead("userInfo", "Callsign");
		
		getOfficerInfoRank().getItems().addAll(dropdownInfo.ranks);
		getOfficerInfoDivision().getItems().addAll(dropdownInfo.divisions);
		getOfficerInfoAgency().getItems().addAll(dropdownInfo.agencies);
		
		OfficerInfoName.setText(name);
		userLabel.setText(String.valueOf(name.charAt(0)).toUpperCase());
		OfficerInfoDivision.setValue(division);
		OfficerInfoRank.setValue(rank);
		OfficerInfoAgency.setValue(agency);
		OfficerInfoNumber.setText(number);
		getOfficerInfoCallsign().setText(callsign);
		
		generatedByTag.setText("Generated By:" + " " + name);
		String time = DataTerminalHomeApplication.getTime();
		generatedDateTag.setText("Generated at: " + time);
		
		areaReportChart.getData().add(parseEveryLog("area"));
		
		getOfficerInfoDivision().setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> p) {
				return new ListCell<>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setText(null);
						} else {
							setText(item);
							setAlignment(Pos.CENTER);
							
							if (item.contains("=")) {
								setStyle("-fx-font-weight: bold;");
							} else {
								setStyle("-fx-font-weight: none;");
							}
						}
					}
				};
			}
		});
		
		initializeCalloutColumns(calloutTable);
		initializeArrestColumns(arrestTable);
		initializeCitationColumns(citationTable);
		initializeImpoundColumns(impoundTable);
		initializeIncidentColumns(incidentTable);
		initializePatrolColumns(patrolTable);
		initializeSearchColumns(searchTable);
		initializeTrafficStopColumns(trafficStopTable);
		initializeDeathReportColumns(deathReportTable);
		initializeAccidentColumns(accidentReportTable);
		loadLogs();
		
		vehSearchField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				try {
					onVehSearchBtnClick(new ActionEvent());
				} catch (IOException e) {
					logError("Error executing vehsearch from Enter: ", e);
				}
			}
		});
		pedSearchField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				try {
					onPedSearchBtnClick(new ActionEvent());
				} catch (IOException e) {
					logError("Error executing pedsearch from Enter: ", e);
				}
			}
		});
		
		ClientUtils.setStatusListener(this::updateConnectionStatus);
		
		Platform.runLater(() -> {
			
			versionLabel.setText(stringUtil.version);
			Stage stge = (Stage) vbox.getScene().getWindow();
			
			stge.setOnHiding(event -> handleClose());
			
			versionLabel.setOnMouseClicked(event -> {
				if (versionStage != null && versionStage.isShowing()) {
					versionStage.close();
					versionStage = null;
					return;
				}
				versionStage = new Stage();
				versionStage.initStyle(StageStyle.UNDECORATED);
				FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Misc/updates-view.fxml"));
				Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e) {
					logError("Error starting VersionStage: ", e);
				}
				Scene newScene = new Scene(root);
				versionStage.setTitle("Version Information");
				versionStage.setScene(newScene);
				versionStage.setAlwaysOnTop(true);
				
				versionStage.show();
				centerStageOnMainApp(versionStage);
				
				versionStage.setOnHidden(event1 -> versionStage = null);
			});
			
			if (!stringUtil.version.equals(gitVersion)) {
				if (gitVersion == null) {
					versionLabel.setText("New Version Available!");
					versionLabel.setStyle("-fx-text-fill: red;");
				} else {
					versionLabel.setText(gitVersion + " Available!");
					versionLabel.setStyle("-fx-text-fill: red;");
				}
			}
			
			try {
				settingsController.loadTheme();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			try {
				if (ConfigReader.configRead("connectionSettings", "serverAutoConnect").equals("true")) {
					Platform.runLater(() -> {
						log("Searching For Server...", Severity.DEBUG);
						new Thread(ClientUtils::listenForServerBroadcasts).start();
					});
				}
			} catch (IOException e) {
				logError("Not able to read serverautoconnect: ", e);
			}
			locationDataLabel.setOnMouseClicked(mouseEvent -> {
				if (locationDataLabel.isVisible()) {
					Clipboard clipboard = Clipboard.getSystemClipboard();
					ClipboardContent content = new ClipboardContent();
					content.putString(locationDataLabel.getText().split(",")[0]);
					clipboard.setContent(content);
				}
			});
		});
		
		currentCalPane.setPrefHeight(0);
		currentCalPane.setMaxHeight(0);
		currentCalPane.setMinHeight(0);
		currentCalPane.setVisible(false);
		calActiveList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				double toHeight = 329;
				
				Timeline timeline = new Timeline();
				
				KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
				KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
				KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
				KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
				                                 keyValueMinHeight);
				
				timeline.getKeyFrames().add(keyFrame);
				
				timeline.play();
				currentCalPane.setVisible(true);
				handleSelectedNodeActive(calActiveList, currentCalPane, calNum, calArea, calCounty, calDate, calStreet,
				                         calDesc, calType, calTime, calPriority);
				showCurrentCalToggle.setSelected(true);
			}
		});
		
		calHistoryList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				double toHeight = 329;
				
				Timeline timeline = new Timeline();
				
				KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
				KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
				KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
				KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
				                                 keyValueMinHeight);
				
				timeline.getKeyFrames().add(keyFrame);
				
				timeline.play();
				currentCalPane.setVisible(true);
				handleSelectedNodeHistory(calHistoryList, currentCalPane, calNum, calArea, calCounty, calDate,
				                          calStreet, calDesc, calType, calTime, calPriority);
				showCurrentCalToggle.setSelected(true);
			}
		});
		
		try {
			scheduleOutcomeRevealsForPendingCases();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
		
		lookupPedCheckbox.setSelected(
				Boolean.parseBoolean(ConfigReader.configRead("lookupWindow", "pedLookupVisible")));
		lookupVehCheckbox.setSelected(
				Boolean.parseBoolean(ConfigReader.configRead("lookupWindow", "vehLookupVisible")));
		if (ConfigReader.configRead("lookupWindow", "lookupOrientation").equalsIgnoreCase("horizontal")) {
			lookupSplitPane.setOrientation(Orientation.HORIZONTAL);
		} else {
			lookupSplitPane.setOrientation(Orientation.VERTICAL);
		}
		
		notesTabList = new ArrayList<>();
	}
	
	//<editor-fold desc="Utils">
	
	private void processPedData(String name, String licenseNumber, String gender, String birthday, String address, String isWanted, String licenseStatus, String pedModel) {
		Optional<Ped> searchedPed = Ped.PedHistoryUtils.findPedByNumber(licenseNumber);
		Ped ped = searchedPed.orElseGet(() -> {
			try {
				return createNewPed(name, licenseNumber, gender, birthday, address, isWanted, licenseStatus, pedModel);
			} catch (IOException e) {
				logError("Error creating new ped: ", e);
				return null;
			}
		});
		if (ped != null) {
			if (setPedRecordFields(ped)) {
				try {
					if (ConfigReader.configRead("soundSettings", "playLookupWarning").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-wanted.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playLookupWarning: ", e);
				}
			}
		}
		pedRecordPane.setVisible(true);
		noRecordFoundLabelPed.setVisible(false);
	}
	
	private void processOwnerData(String owner, String vehPlateNum) {
		Optional<Ped> searchedPed = findPedByName(owner);
		Ped ped = searchedPed.orElseGet(() -> {
			try {
				return createOwnerPed(owner, vehPlateNum);
			} catch (IOException e) {
				logError("Error creating ownerPed: ", e);
				return null;
			}
		});
		
		if (ped != null) {
			if (setPedRecordFields(ped)) {
				try {
					if (ConfigReader.configRead("soundSettings", "playLookupWarning").equalsIgnoreCase("true")) {
						playSound(getJarPath() + "/sounds/alert-wanted.wav");
					}
				} catch (IOException e) {
					logError("Error getting configValue for playLookupWarning: ", e);
				}
			}
		}
		pedRecordPane.setVisible(true);
		noRecordFoundLabelPed.setVisible(false);
	}
	
	private boolean setPedRecordFields(Ped ped) {
		boolean playAudio = false;
		pedfnamefield.setText(ped.getFirstName());
		pedlnamefield.setText(ped.getLastName());
		pedgenfield.setText(ped.getGender());
		peddobfield.setText(ped.getBirthday());
		pedaddressfield.setText(ped.getAddress());
		
		// License status fields
		pedlicensefield.setText(ped.getLicenseStatus());
		if (ped.getLicenseStatus().equalsIgnoreCase("EXPIRED") || ped.getLicenseStatus().equalsIgnoreCase(
				"SUSPENDED") || ped.getLicenseStatus().equalsIgnoreCase("REVOKED")) {
			pedlicensefield.setStyle("-fx-text-fill: red !important;");
			playAudio = true;
			
		} else {
			pedlicensefield.setStyle("-fx-text-fill: #006600 !important;");
			pedlicensefield.setText("Valid");
		}
		
		// Outstanding warrants
		pedwantedfield.setText(ped.getOutstandingWarrants() != null ? ped.getOutstandingWarrants() : "False");
		if (ped.getOutstandingWarrants() != null) {
			pedwantedfield.setStyle("-fx-text-fill: red !important;");
			playAudio = true;
			
		} else {
			pedwantedfield.setStyle("-fx-text-fill: black;");
		}
		
		// Gun license status
		pedgunlicensestatusfield.setText(ped.getGunLicenseStatus() != null ? ped.getGunLicenseStatus() : "False");
		if (ped.getGunLicenseStatus().equalsIgnoreCase("false")) {
			pedgunlicensestatusfield.setStyle("-fx-text-fill: black !important;");
		} else {
			pedgunlicensestatusfield.setStyle("-fx-text-fill: #006600 !important;");
			pedgunlicensestatusfield.setText("Valid");
		}
		
		// Probation status
		pedprobationstatusfield.setText(ped.getProbationStatus() != null ? ped.getProbationStatus() : "False");
		if (ped.getProbationStatus() != null && ped.getProbationStatus().equalsIgnoreCase("true")) {
			pedprobationstatusfield.setStyle("-fx-text-fill: red !important;");
			pedprobationstatusfield.setText("On Probation");
		} else {
			pedprobationstatusfield.setStyle("-fx-text-fill: black !important;");
		}
		
		// Fishing license status
		pedfishinglicstatusfield.setText(
				ped.getFishingLicenseStatus() != null ? ped.getFishingLicenseStatus() : "False");
		if (ped.getFishingLicenseStatus() != null && ped.getFishingLicenseStatus().equalsIgnoreCase("true")) {
			pedfishinglicstatusfield.setStyle("-fx-text-fill: #006600 !important;");
			pedfishinglicstatusfield.setText("Valid");
		} else {
			pedfishinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		}
		
		// Boating license status
		pedboatinglicstatusfield.setText(
				ped.getBoatingLicenseStatus() != null ? ped.getBoatingLicenseStatus() : "False");
		if (ped.getBoatingLicenseStatus() != null && ped.getBoatingLicenseStatus().equalsIgnoreCase("true")) {
			pedboatinglicstatusfield.setStyle("-fx-text-fill: #006600 !important;");
			pedboatinglicstatusfield.setText("Valid");
		} else {
			pedboatinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		}
		
		// Gun license class and type
		pedgunlicenseclassfield.setText(ped.getGunLicenseClass() != null ? ped.getGunLicenseClass() : "No License");
		pedgunlicensetypefield.setText(ped.getGunLicenseType() != null ? ped.getGunLicenseType() : "No License");
		
		// Hunting license status
		pedhuntinglicstatusfield.setText(
				ped.getHuntingLicenseStatus() != null ? ped.getHuntingLicenseStatus() : "False");
		if (ped.getHuntingLicenseStatus() != null && ped.getHuntingLicenseStatus().equalsIgnoreCase("true")) {
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: #006600 !important;");
			pedhuntinglicstatusfield.setText("Valid");
		} else {
			pedhuntinglicstatusfield.setStyle("-fx-text-fill: black !important;");
		}
		
		// License number
		pedlicnumfield.setText(ped.getLicenseNumber() != null ? ped.getLicenseNumber() : "No Data In System");
		pedlicnumfield.setStyle(
				ped.getLicenseNumber() == null ? "-fx-text-fill: #e65c00 !important;" : "-fx-text-fill: black;");
		
		// Affiliation
		String affiliations = ped.getAffiliations();
		if (affiliations == null || affiliations.equalsIgnoreCase("No Data In System")) {
			pedaffiliationfield.setText("No Data In System");
			pedaffiliationfield.setStyle("-fx-text-fill: #e65c00 !important;");
		} else {
			pedaffiliationfield.setText(affiliations);
			pedaffiliationfield.setStyle("-fx-text-fill: black !important;");
		}
		
		String flags = ped.getFlags();
		if (flags == null || flags.equalsIgnoreCase("No Data In System")) {
			pedflagfield.setText("No Data In System");
			pedflagfield.setStyle("-fx-text-fill: #e65c00 !important;");
		} else {
			pedflagfield.setText(flags);
			pedflagfield.setStyle("-fx-text-fill: black !important;");
		}
		
		// Description
		String description = ped.getDescription();
		if (description == null || description.equalsIgnoreCase("No Data In System")) {
			peddescfield.setText("No Data In System");
			peddescfield.setStyle("-fx-text-fill: #e65c00 !important;");
		} else {
			peddescfield.setText(description);
			peddescfield.setStyle("-fx-text-fill: black !important;");
		}
		
		// Aliases
		String aliases = ped.getAliases();
		if (aliases == null || aliases.equalsIgnoreCase("No Data In System")) {
			pedaliasfield.setText("No Data In System");
			pedaliasfield.setStyle("-fx-text-fill: #e65c00 !important;");
		} else {
			pedaliasfield.setText(aliases);
			pedaliasfield.setStyle("-fx-text-fill: black !important;");
		}
		
		// Parole status
		pedparolestatusfield.setText(ped.getParoleStatus() != null ? ped.getParoleStatus() : "False");
		if (ped.getParoleStatus() != null && ped.getParoleStatus().equalsIgnoreCase("true")) {
			pedparolestatusfield.setStyle("-fx-text-fill: red !important;");
			pedparolestatusfield.setText("On Parole");
		} else {
			pedparolestatusfield.setStyle("-fx-text-fill: black !important;");
		}
		
		// Times stopped
		pedtimesstoppedfield.setText(ped.getTimesStopped() != null ? ped.getTimesStopped() : "No Data");
		pedtimesstoppedfield.setStyle(
				ped.getTimesStopped() == null ? "-fx-text-fill: #e65c00 !important;" : "-fx-text-fill: black;");
		
		// Birthday
		ped6.setText("Birthday: (" + calculateAge(ped.getBirthday()) + ")");
		
		// Ped Image
		String pedModel = ped.getModel();
		if (pedModel != null && !pedModel.equalsIgnoreCase("not available")) {
			File pedImgFolder = new File(pedImageFolderURL);
			if (pedImgFolder.exists()) {
				log("Detected pedImage folder..", Severity.DEBUG);
				
				File[] matchingFiles = pedImgFolder.listFiles((dir, name) -> name.equalsIgnoreCase(pedModel + ".jpg"));
				
				if (matchingFiles != null && matchingFiles.length > 0) {
					File matchingFile = matchingFiles[0];
					log("Matching pedImage found: " + matchingFile.getName(), Severity.INFO);
					
					try {
						String fileURI = matchingFile.toURI().toString();
						pedImageView.setImage(new Image(fileURI));
						noPedImageFoundlbl.setVisible(true);
						noPedImageFoundlbl.setText("Image Found On File:");
					} catch (Exception e) {
						Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
						pedImageView.setImage(defImage);
						noPedImageFoundlbl.setVisible(true);
						noPedImageFoundlbl.setText("No Image Found In System");
						logError("Could not set ped image: ", e);
					}
				} else {
					log("No matching image found for the model: " + pedModel + ", displaying no image found.",
					    Severity.WARN);
					Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
					pedImageView.setImage(defImage);
					noPedImageFoundlbl.setVisible(true);
					noPedImageFoundlbl.setText("No Image Found In System");
				}
			} else {
				Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
				pedImageView.setImage(defImage);
				noPedImageFoundlbl.setVisible(true);
				noPedImageFoundlbl.setText("No Image Found In System");
			}
		} else {
			Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
			pedImageView.setImage(defImage);
			noPedImageFoundlbl.setVisible(true);
			noPedImageFoundlbl.setText("No Image Found In System");
		}
		
		String citationPriors = ped.getCitationPriors();
		if (citationPriors == null) {
			citationPriors = "";
		}
		
		Pattern pattern = Pattern.compile("MaxFine:\\S+");
		Matcher matcher = pattern.matcher(citationPriors);
		String updatedCitPriors = matcher.replaceAll("").trim();
		
		// Arrest and citation priors
		ObservableList<Label> arrestPriors = createLabels(ped.getArrestPriors());
		ObservableList<Label> citPriors = createLabels(updatedCitPriors);
		
		pedarrestpriorslistview.setItems(arrestPriors);
		pedcitationpriorslistview.setItems(citPriors);
		return playAudio;
	}
	
	private void adjustDividerPositions() {
		Platform.runLater(() -> {
			int itemCount = lookupSplitPane.getItems().size();
			if (itemCount == 2) {
				lookupSplitPane.setDividerPositions(0.5);
			} else if (itemCount == 1) {
				lookupSplitPane.setDividerPositions(0.4);
			}
		});
	}
	
	private void updateConnectionStatus(boolean isConnected) {
		Platform.runLater(() -> {
			if (!isConnected) {
				showLookupBtn.setVisible(false);
				showCalloutBtn.setVisible(false);
				showIDBtn.setVisible(false);
				locationDataLabel.setVisible(false);
				log("No Connection", Severity.WARN);
				serverStatusLabel.setText("No Connection");
				serverStatusLabel.setStyle(
						"-fx-text-fill: #ff5a5a; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
				if (clientController != null) {
					clientController.getPortField().setText("");
					clientController.getInetField().setText("");
					clientController.getStatusLabel().setText("Not Connected");
					clientController.getStatusLabel().setStyle("-fx-background-color: #ff5e5e;");
					serverStatusLabel.setStyle(
							"-fx-text-fill: #ff5e5e; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
				}
			} else {
				showLookupBtn.setVisible(true);
				showCalloutBtn.setVisible(true);
				showIDBtn.setVisible(true);
				serverStatusLabel.setText("Connected");
				
				serverStatusLabel.setStyle(
						"-fx-text-fill: #00da16; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
				if (clientController != null) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					clientController.getPortField().setText(ClientUtils.port);
					clientController.getInetField().setText(ClientUtils.inet);
					clientController.getStatusLabel().setText("Connected");
					clientController.getStatusLabel().setStyle("-fx-background-color: green;");
				}
			}
		});
	}
	
	public void refreshChart() throws IOException {
		
		reportChart.getData().clear();
		String[] categories = {"Callout", "Arrests", "Traffic Stops", "Patrols", "Searches", "Incidents", "Impounds", "Citations", "Death Reports", "Accident Reports"};
		CategoryAxis xAxis = (CategoryAxis) getReportChart().getXAxis();
		
		xAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(categories)));
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName("Series 1");
		
		String color = ConfigReader.configRead("uiColors", "mainColor");
		for (String category : categories) {
			XYChart.Data<String, Number> data = new XYChart.Data<>(category, 1);
			data.nodeProperty().addListener((obs, oldNode, newNode) -> {
				if (newNode != null) {
					newNode.setStyle("-fx-bar-fill: " + color + ";");
				}
			});
			series1.getData().add(data);
		}
		
		getReportChart().getData().add(series1);
	}
	
	public static String getNextIndex(CourtCases courtCases) {
		int highestIndex = 0;
		
		if (courtCases.getCaseList() != null) {
			for (Case c : courtCases.getCaseList()) {
				String indexString = c.getIndex();
				if (indexString != null && !indexString.isEmpty()) {
					try {
						int index = Integer.parseInt(indexString);
						highestIndex = Math.max(highestIndex, index);
					} catch (NumberFormatException e) {
						logError("Invalid index format: " + indexString, e);
					}
				}
			}
		}
		return String.valueOf(highestIndex + 1);
	}
	
	public void loadCaseLabels(ListView<String> listView) {
		listView.getItems().clear();
		try {
			CourtCases courtCases = loadCourtCases();
			ObservableList<String> caseNames = FXCollections.observableArrayList();
			
			if (courtCases.getCaseList() != null) {
				
				List<Case> sortedCases = courtCases.getCaseList().stream().sorted(Comparator.comparing((Case case1) -> {
					
					String indexString = case1.getIndex();
					if (indexString == null || indexString.isEmpty()) {
						indexString = getNextIndex(courtCases);
						case1.setIndex(indexString);
						try {
							CourtUtils.addCase(case1);
						} catch (JAXBException | IOException e) {
							throw new RuntimeException(e);
						}
					}
					
					int index = Integer.parseInt(indexString);
					
					return index;
				}).reversed()).toList();
				
				for (Case case1 : sortedCases) {
					if (!case1.getName().isEmpty() && !case1.getOffences().isEmpty()) {
						caseNames.add(
								case1.getOffenceDate().replaceAll("-", "/") + " " + case1.getCaseTime().replace(".",
								                                                                                "") + " " + case1.getName() + " " + case1.getCaseNumber());
					}
				}
				
				listView.setItems(caseNames);
				
				listView.setCellFactory(new Callback<>() {
					@Override
					public ListCell<String> call(ListView<String> param) {
						return new ListCell<>() {
							private final CustomCaseCell customCaseCell = new CustomCaseCell();
							
							@Override
							protected void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty || item == null) {
									setGraphic(null);
								} else {
									for (Case case1 : sortedCases) {
										if (item.equals(case1.getOffenceDate().replaceAll("-",
										                                                  "/") + " " + case1.getCaseTime().replace(
												".", "") + " " + case1.getName() + " " + case1.getCaseNumber())) {
											customCaseCell.updateCase(case1);
											break;
										}
									}
									setGraphic(customCaseCell);
								}
							}
						};
					}
				});
				
				listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
					if (newValue != null) {
						blankCourtInfoPane.setVisible(false);
						courtInfoPane.setVisible(true);
						for (Case case1 : sortedCases) {
							if (newValue.equals(
									case1.getOffenceDate().replaceAll("-", "/") + " " + case1.getCaseTime().replace(".",
									                                                                                "") + " " + case1.getName() + " " + case1.getCaseNumber())) {
								updateFields(case1);
								break;
							}
						}
					}
				});
				
				Map<String, Case> caseMap = new HashMap<>();
				for (Case case1 : courtCases.getCaseList()) {
					String dateTime = case1.getOffenceDate() + " " + case1.getCaseTime().replace(".", "");
					caseMap.put(dateTime, case1);
				}
				courtCases.setCaseList(new ArrayList<>(caseMap.values()));
				saveCourtCases(courtCases);
			}
		} catch (JAXBException | IOException e) {
			logError("Error loading Case labels: ", e);
		}
	}
	
	private void setCellFactory(ListView<Label> listView) {
		listView.setCellFactory(new Callback<>() {
			@Override
			public ListCell<Label> call(ListView<Label> param) {
				return new ListCell<>() {
					@Override
					protected void updateItem(Label item, boolean empty) {
						super.updateItem(item, empty);
						if (empty || item == null) {
							setText(null);
							setGraphic(null);
						} else {
							setGraphic(item);
						}
					}
				};
			}
		});
	}
	
	private void updateFields(Case case1) {
		if (case1.getStatus() != null) {
			if (case1.getStatus().equalsIgnoreCase("pending")) {
				caseTotalLabel.setText("Pending");
				caseTotalLabel.setStyle("-fx-text-fill: black;");
				caseTotalJailTimeLabel.setText("Pending");
				caseTotalJailTimeLabel.setStyle("-fx-text-fill: black;");
				caseTotalProbationLabel.setText("Pending");
				caseTotalProbationLabel.setStyle("-fx-text-fill: black;");
				caseLicenseStatLabel.setText("Pending");
				caseLicenseStatLabel.setStyle("-fx-text-fill: black;");
				caseSuspensionDuration.setText("Pending");
				caseSuspensionDuration.setStyle("-fx-text-fill: black;");
				
				String offences = case1.getOffences() != null ? case1.getOffences() : "";
				Pattern pattern = Pattern.compile("MaxFine:\\S+");
				Matcher matcher = pattern.matcher(offences);
				String updatedOffences = matcher.replaceAll("").trim();
				
				ObservableList<Label> offenceLabels = createLabels(updatedOffences);
				ObservableList<Label> outcomeLabels = createPendingLabels(case1.getOutcomes());
				
				caseOutcomesListView.setItems(outcomeLabels);
				caseOffencesListView.setItems(offenceLabels);
				
				setCellFactory(caseOutcomesListView);
				setCellFactory(caseOffencesListView);
			} else {
				revealOutcomes(case1);
			}
		} else {
			log("Could not find a caseStatus for: #" + case1.getCaseNumber(), Severity.ERROR);
			revealOutcomes(case1);
			case1.setStatus("Closed");
			try {
				modifyCase(case1.getCaseNumber(), case1);
				log("Case: #" + case1.getCaseNumber() + " has been set as closed", Severity.DEBUG);
			} catch (JAXBException | IOException e) {
				logError("Error setting case as closed (modifying): ", e);
				
			}
		}
		revealOutcomeBtn.setVisible(case1.getStatus().equalsIgnoreCase("pending"));
		caseOffenceDateField.setText(case1.getOffenceDate() != null ? case1.getOffenceDate() : "");
		caseAgeField.setText(case1.getAge() != null ? String.valueOf(case1.getAge()) : "");
		caseGenderField.setText(case1.getGender() != null ? String.valueOf(case1.getGender()) : "");
		caseAreaField.setText(case1.getArea() != null ? case1.getArea() : "");
		caseStreetField.setText(case1.getStreet() != null ? case1.getStreet() : "");
		caseCountyField.setText(case1.getCounty() != null ? case1.getCounty() : "");
		caseNotesField.setText(case1.getNotes() != null ? case1.getNotes() : "");
		caseFirstNameField.setText(case1.getFirstName() != null ? case1.getFirstName() : "");
		caseLastNameField.setText(case1.getLastName() != null ? case1.getLastName() : "");
		caseCourtDateField.setText(case1.getCourtDate() != null ? case1.getCourtDate() : "");
		caseNumField.setText(case1.getCaseNumber() != null ? case1.getCaseNumber() : "");
		caseAddressField.setText(case1.getAddress() != null ? case1.getAddress() : "");
	}
	
	public void scheduleOutcomeRevealsForPendingCases() throws JAXBException, IOException {
		long delayInSeconds = Long.parseLong(ConfigReader.configRead("pedHistory", "courtTrialDelay"));
		Random random = new Random();
		
		long minSec = delayInSeconds / 3;
		CourtCases courtCases = loadCourtCases();
		
		if (courtCases.getCaseList() != null) {
			List<Case> pendingCases = courtCases.getCaseList().stream().filter(
					c -> "pending".equalsIgnoreCase(c.getStatus())).collect(Collectors.toList());
			
			for (Case pendingCase : pendingCases) {
				long randomSec = minSec + random.nextLong(delayInSeconds - minSec + 1);
				log("Scheduled: " + pendingCase.getCaseNumber() + " for court, pending trial: " + randomSec + " Sec",
				    Severity.DEBUG);
				
				Runnable revealTask = () -> {
					
					revealOutcomes(pendingCase);
					pendingCase.setStatus("Closed");
					
					try {
						modifyCase(pendingCase.getCaseNumber(), pendingCase);
						log("Case: #" + pendingCase.getCaseNumber() + " has been closed", Severity.DEBUG);
						showNotificationInfo("Court Manager",
						                     "Case: #" + pendingCase.getCaseNumber() + " has been closed", mainRT);
					} catch (JAXBException | IOException e) {
						logError("Error modifying case from scheduleOutcomeReveals: ", e);
						
					}
				};
				
				courtPendingChargesExecutor.schedule(revealTask, randomSec, TimeUnit.SECONDS);
			}
			
		}
	}
	
	private void revealOutcomes(Case case1) {
		Platform.runLater(() -> {
			List<String> licenseStatusList = parseCharges(case1.getOutcomes(), "License");
			String outcomeSuspension = calculateTotalTime(case1.getOutcomes(), "License Suspension Time");
			String outcomeProbation = calculateTotalTime(case1.getOutcomes(), "Probation Time");
			List<String> jailTimeList = parseCharges(case1.getOutcomes(), "Jail Time");
			String totalJailTime = calculateTotalTime(case1.getOutcomes(), "Jail Time");
			if (jailTimeList.contains("Life sentence")) {
				totalJailTime = "Life Sentence";
			}
			
			boolean areTrafficChargesPresent = !licenseStatusList.isEmpty() || !outcomeSuspension.isEmpty();
			String licenseStatus = "";
			if (licenseStatusList.contains("Valid")) {
				licenseStatus = "N/A";
				caseLicenseStatLabel.setStyle("-fx-text-fill: gray;");
			} else if (licenseStatusList.contains("Suspended")) {
				licenseStatus = "Suspended";
				caseLicenseStatLabel.setStyle("-fx-text-fill: #cc5200;");
			} else if (licenseStatusList.contains("Revoked")) {
				licenseStatus = "Revoked";
				caseLicenseStatLabel.setStyle("-fx-text-fill: red;");
			}
			
			if (!totalJailTime.isEmpty()) {
				if (totalJailTime.contains("years") && Integer.parseInt(extractInteger(totalJailTime)) >= 10) {
					caseTotalJailTimeLabel.setStyle("-fx-text-fill: red;");
				} else if (totalJailTime.contains("months")) {
					caseTotalJailTimeLabel.setStyle("-fx-text-fill: black;");
				} else if (totalJailTime.contains("Life")) {
					caseTotalJailTimeLabel.setStyle("-fx-text-fill: red;");
				}
				caseTotalJailTimeLabel.setText(totalJailTime);
			} else {
				caseTotalJailTimeLabel.setStyle("-fx-text-fill: gray;");
				caseTotalJailTimeLabel.setText("None");
			}
			
			if (!outcomeProbation.isEmpty()) {
				if (outcomeProbation.contains("years")) {
					caseTotalProbationLabel.setStyle("-fx-text-fill: red;");
				} else if (outcomeProbation.contains("months") && Integer.parseInt(
						extractInteger(outcomeProbation)) >= 7) {
					caseTotalProbationLabel.setStyle("-fx-text-fill: #cc5200;");
				} else {
					caseTotalProbationLabel.setStyle("-fx-text-fill: black;");
				}
				caseTotalProbationLabel.setText(outcomeProbation);
			} else {
				caseTotalProbationLabel.setStyle("-fx-text-fill: gray;");
				caseTotalProbationLabel.setText("None");
			}
			
			if (areTrafficChargesPresent) {
				caseLicenseStatLabel.setText(licenseStatus);
				if (!outcomeSuspension.isEmpty() && !licenseStatusList.contains("Revoked")) {
					if (outcomeSuspension.contains("years") && Integer.parseInt(
							extractInteger(outcomeSuspension)) >= 2) {
						caseSuspensionDuration.setStyle("-fx-text-fill: red;");
					} else {
						caseSuspensionDuration.setStyle("-fx-text-fill: #cc5200;");
					}
					caseSuspensionDuration.setText(outcomeSuspension);
				} else {
					caseSuspensionDuration.setStyle("-fx-text-fill: #cc5200;");
					caseSuspensionDuration.setText("License Revoked");
				}
			} else {
				caseLicenseStatLabel.setStyle("-fx-text-fill: gray;");
				caseLicenseStatLabel.setText("N/A");
				caseSuspensionDuration.setStyle("-fx-text-fill: gray;");
				caseSuspensionDuration.setText("None");
			}
			
			int fineTotal = calculateFineTotal(case1.getOutcomes());
			if (fineTotal > 1500) {
				caseTotalLabel.setStyle("-fx-text-fill: red;");
				caseTotalLabel.setText("$" + fineTotal + ".00");
			} else if (fineTotal > 700) {
				caseTotalLabel.setStyle("-fx-text-fill: #cc5200;");
				caseTotalLabel.setText("$" + fineTotal + ".00");
			} else if (fineTotal > 0) {
				caseTotalLabel.setStyle("-fx-text-fill: black;");
				caseTotalLabel.setText("$" + fineTotal + ".00");
			} else {
				caseTotalLabel.setStyle("-fx-text-fill: gray;");
				caseTotalLabel.setText("$0.00");
			}
			
			String offences = case1.getOffences() != null ? case1.getOffences() : "";
			Pattern pattern = Pattern.compile("MaxFine:\\S+");
			Matcher matcher = pattern.matcher(offences);
			String updatedOffences = matcher.replaceAll("").trim();
			
			ObservableList<Label> offenceLabels = createLabels(updatedOffences);
			ObservableList<Label> outcomeLabels = createLabels(case1.getOutcomes());
			
			caseOutcomesListView.setItems(outcomeLabels);
			caseOffencesListView.setItems(offenceLabels);
			
			setCellFactory(caseOutcomesListView);
			setCellFactory(caseOffencesListView);
		});
	}
	
	//</editor-fold>
	
	//<editor-fold desc="Getters">
	
	public Label getNoVehImageFoundlbl() {
		return noVehImageFoundlbl;
	}
	
	public Label getNoPedImageFoundlbl() {
		return noPedImageFoundlbl;
	}
	
	public Circle getUserCircle() {
		return userCircle;
	}
	
	public Label getUserLabel() {
		return userLabel;
	}
	
	public Button getShowLookupBtn() {
		return showLookupBtn;
	}
	
	public Button getOrientationBtn() {
		return orientationBtn;
	}
	
	public Button getProbabilitySettingsBtn() {
		return probabilitySettingsBtn;
	}
	
	public AnchorPane getLookupmainlblpane() {
		return lookupmainlblpane;
	}
	
	public Label getLookupmainlbl() {
		return lookupmainlbl;
	}
	
	public AnchorPane getLookupPane() {
		return lookupPane;
	}
	
	public Label getCaldetlbl1() {
		return caldetlbl1;
	}
	
	public Label getCaldetlbl2() {
		return caldetlbl2;
	}
	
	public Label getCaldetlbl3() {
		return caldetlbl3;
	}
	
	public Label getCaldetlbl4() {
		return caldetlbl4;
	}
	
	public Label getCaldetlbl5() {
		return caldetlbl5;
	}
	
	public Label getCaldetlbl6() {
		return caldetlbl6;
	}
	
	public Label getCaldetlbl7() {
		return caldetlbl7;
	}
	
	public Label getCaldetlbl8() {
		return caldetlbl8;
	}
	
	public Label getCaldetlbl9() {
		return caldetlbl9;
	}
	
	public Label getCaseprim1() {
		return caseprim1;
	}
	
	public Label getCasesec1() {
		return casesec1;
	}
	
	public Label getCasesec2() {
		return casesec2;
	}
	
	public Label getCasesec3() {
		return casesec3;
	}
	
	public Label getCasesec4() {
		return casesec4;
	}
	
	public Label getCaseSuspensionDurationlbl() {
		return caseSuspensionDurationlbl;
	}
	
	public GridPane getCaseVerdictPane() {
		return caseVerdictPane;
	}
	
	public Label getCaseprim2() {
		return caseprim2;
	}
	
	public Label getCaseprim3() {
		return caseprim3;
	}
	
	public AnchorPane getBlankCourtInfoPane() {
		return blankCourtInfoPane;
	}
	
	public AnchorPane getCourtInfoPane() {
		return courtInfoPane;
	}
	
	public Label getNoCourtCaseSelectedlbl() {
		return noCourtCaseSelectedlbl;
	}
	
	public TextField getCaseAddressField() {
		return caseAddressField;
	}
	
	public TextField getCaseAreaField() {
		return caseAreaField;
	}
	
	public TextField getCaseCountyField() {
		return caseCountyField;
	}
	
	public TextField getCaseFirstNameField() {
		return caseFirstNameField;
	}
	
	public TextField getCaseGenderField() {
		return caseGenderField;
	}
	
	public TextField getCaseLastNameField() {
		return caseLastNameField;
	}
	
	public TextArea getCaseNotesField() {
		return caseNotesField;
	}
	
	public TextField getCaseStreetField() {
		return caseStreetField;
	}
	
	public Label getCaselbl10() {
		return caselbl10;
	}
	
	public Label getCaselbl11() {
		return caselbl11;
	}
	
	public Label getCaselbl12() {
		return caselbl12;
	}
	
	public Label getCaselbl1() {
		return caselbl1;
	}
	
	public Label getCaselbl2() {
		return caselbl2;
	}
	
	public Label getCaselbl3() {
		return caselbl3;
	}
	
	public Label getCaselbl4() {
		return caselbl4;
	}
	
	public Label getCaselbl5() {
		return caselbl5;
	}
	
	public Label getCaselbl6() {
		return caselbl6;
	}
	
	public Label getCaselbl7() {
		return caselbl7;
	}
	
	public Label getCaselbl8() {
		return caselbl8;
	}
	
	public Label getCaselbl9() {
		return caselbl9;
	}
	
	public AnchorPane getCourtPane() {
		return courtPane;
	}
	
	public Label getCaseTotalLabel() {
		return caseTotalLabel;
	}
	
	public ListView getCaseOutcomesListView() {
		return caseOutcomesListView;
	}
	
	public ListView getCaseOffencesListView() {
		return caseOffencesListView;
	}
	
	public TextField getCaseOffenceDateField() {
		return caseOffenceDateField;
	}
	
	public TextField getCaseNumField() {
		return caseNumField;
	}
	
	public ListView getCaseList() {
		return caseList;
	}
	
	public TextField getCaseCourtDateField() {
		return caseCourtDateField;
	}
	
	public TextField getCaseAgeField() {
		return caseAgeField;
	}
	
	public Label getSecondaryColor5Bkg() {
		return secondaryColor5Bkg;
	}
	
	public Button getShowCourtCasesBtn() {
		return showCourtCasesBtn;
	}
	
	public static Stage getCalloutStage() {
		return CalloutStage;
	}
	
	public static ClientController getClientController() {
		return clientController;
	}
	
	public static Stage getClientStage() {
		return clientStage;
	}
	
	public static Stage getIDStage() {
		return IDStage;
	}
	
	public static Stage getMapStage() {
		return mapStage;
	}
	
	public static int getNeedRefresh() {
		return needRefresh.get();
	}
	
	public static int getNeedCourtRefresh() {
		return needCourtRefresh.get();
	}
	
	public static SimpleIntegerProperty needRefreshProperty() {
		return needRefresh;
	}
	
	public static Stage getNotesStage() {
		return notesStage;
	}
	
	public static String getNotesText() {
		return notesText;
	}
	
	public static Stage getSettingsStage() {
		return settingsStage;
	}
	
	public static Stage getVersionStage() {
		return versionStage;
	}
	
	public Label getPlt1() {
		return plt1;
	}
	
	public Label getPlt2() {
		return plt2;
	}
	
	public Label getPlt3() {
		return plt3;
	}
	
	public Label getPlt4() {
		return plt4;
	}
	
	public Label getPlt5() {
		return plt5;
	}
	
	public Label getPlt6() {
		return plt6;
	}
	
	public Label getPlt7() {
		return plt7;
	}
	
	public Label getPed1() {
		return ped1;
	}
	
	public Label getPed2() {
		return ped2;
	}
	
	public Label getPed3() {
		return ped3;
	}
	
	public Label getPed4() {
		return ped4;
	}
	
	public Label getPed5() {
		return ped5;
	}
	
	public Label getPed6() {
		return ped6;
	}
	
	public Label getPed7() {
		return ped7;
	}
	
	public Label getPed10() {
		return ped10;
	}
	
	public Label getPed11() {
		return ped11;
	}
	
	public Label getPed12() {
		return ped12;
	}
	
	public Label getPed13() {
		return ped13;
	}
	
	public Label getPed14() {
		return ped14;
	}
	
	public Label getPed15() {
		return ped15;
	}
	
	public Label getPed16() {
		return ped16;
	}
	
	public Label getPed17() {
		return ped17;
	}
	
	public Label getPed18() {
		return ped18;
	}
	
	public Label getPed19() {
		return ped19;
	}
	
	public Label getPed20() {
		return ped20;
	}
	
	public Label getPed21() {
		return ped21;
	}
	
	public Label getPed22() {
		return ped22;
	}
	
	public Label getPed8() {
		return ped8;
	}
	
	public Label getPed9() {
		return ped9;
	}
	
	public Label getPlt8() {
		return plt8;
	}
	
	public Label getPlt10() {
		return plt10;
	}
	
	public Label getPlt9() {
		return plt9;
	}
	
	public Label getPed23() {
		return ped23;
	}
	
	public MenuItem getArrestReportButton() {
		return arrestReportButton;
	}
	
	public Tab getArrestTab() {
		return arrestTab;
	}
	
	public TextField getCalArea() {
		return calArea;
	}
	
	public TextField getCalCounty() {
		return calCounty;
	}
	
	public TextField getCalDate() {
		return calDate;
	}
	
	public TextArea getCalDesc() {
		return calDesc;
	}
	
	public AnchorPane getCalloutPane() {
		return calloutPane;
	}
	
	public MenuItem getCalloutReportButton() {
		return calloutReportButton;
	}
	
	public Tab getCalloutTab() {
		return calloutTab;
	}
	
	public TextField getCalNum() {
		return calNum;
	}
	
	public TextField getCalPriority() {
		return calPriority;
	}
	
	public TextField getCalStreet() {
		return calStreet;
	}
	
	public TextField getCalTime() {
		return calTime;
	}
	
	public TextField getCalType() {
		return calType;
	}
	
	public Tab getCitationTab() {
		return citationTab;
	}
	
	public actionController getController() {
		return controller;
	}
	
	public Label getGeneratedByTag() {
		return generatedByTag;
	}
	
	public Label getGeneratedDateTag() {
		return generatedDateTag;
	}
	
	public MenuItem getImpoundReportButton() {
		return impoundReportButton;
	}
	
	public Tab getImpoundTab() {
		return impoundTab;
	}
	
	public MenuItem getIncidentReportButton() {
		return incidentReportButton;
	}
	
	public Tab getIncidentTab() {
		return incidentTab;
	}
	
	public AnchorPane getLogPane() {
		return logPane;
	}
	
	public Label getMainColor8() {
		return mainColor8;
	}
	
	public Label getNoRecordFoundLabelPed() {
		return noRecordFoundLabelPed;
	}
	
	public Label getNoRecordFoundLabelVeh() {
		return noRecordFoundLabelVeh;
	}
	
	public Button getNotesButton() {
		return notesButton;
	}
	
	public NotesViewController getNotesViewController() {
		return notesViewController;
	}
	
	public MenuItem getPatrolReportButton() {
		return patrolReportButton;
	}
	
	public Tab getPatrolTab() {
		return patrolTab;
	}
	
	public TextField getPedaddressfield() {
		return pedaddressfield;
	}
	
	public TextField getPeddobfield() {
		return peddobfield;
	}
	
	public TextField getPedfnamefield() {
		return pedfnamefield;
	}
	
	public TextField getPedgenfield() {
		return pedgenfield;
	}
	
	public TextField getPedlicensefield() {
		return pedlicensefield;
	}
	
	public Label getLocationDataLabel() {
		return locationDataLabel;
	}
	
	public TextField getPedlnamefield() {
		return pedlnamefield;
	}
	
	public AnchorPane getPedLookupPane() {
		return pedLookupPane;
	}
	
	public AnchorPane getPedRecordPane() {
		return pedRecordPane;
	}
	
	public Button getPedSearchBtn() {
		return pedSearchBtn;
	}
	
	public ComboBox getPedSearchField() {
		return pedSearchField;
	}
	
	public ComboBox getVehSearchField() {
		return vehSearchField;
	}
	
	public TextField getPedwantedfield() {
		return pedwantedfield;
	}
	
	public MenuItem getSearchReportButton() {
		return searchReportButton;
	}
	
	public Tab getSearchTab() {
		return searchTab;
	}
	
	public Button getShiftInfoBtn() {
		return shiftInfoBtn;
	}
	
	public AnchorPane getShiftInformationPane() {
		return shiftInformationPane;
	}
	
	public ToggleButton getShowCurrentCalToggle() {
		return showCurrentCalToggle;
	}
	
	public AnchorPane getSidepane() {
		return sidepane;
	}
	
	public AnchorPane getTitlebar() {
		return titlebar;
	}
	
	public MenuItem getTrafficCitationReportButton() {
		return trafficCitationReportButton;
	}
	
	public MenuItem getTrafficReportButton() {
		return trafficReportButton;
	}
	
	public Tab getTrafficStopTab() {
		return trafficStopTab;
	}
	
	public Label getUpdatedNotification() {
		return updatedNotification;
	}
	
	public Button getUpdateInfoBtn() {
		return updateInfoBtn;
	}
	
	public AnchorPane getVbox() {
		return vbox;
	}
	
	public AnchorPane getVehcolordisplay() {
		return vehcolordisplay;
	}
	
	public TextField getVehinsfield() {
		return vehinsfield;
	}
	
	public AnchorPane getVehLookupPane() {
		return vehLookupPane;
	}
	
	public TextField getVehmodelfield() {
		return vehmodelfield;
	}
	
	public Label getVehnocolorlabel() {
		return vehnocolorlabel;
	}
	
	public TextField getVehownerfield() {
		return vehownerfield;
	}
	
	public TextField getVehplatefield2() {
		return vehplatefield2;
	}
	
	public AnchorPane getVehRecordPane() {
		return vehRecordPane;
	}
	
	public TextField getVehregfield() {
		return vehregfield;
	}
	
	public Button getVehSearchBtn() {
		return vehSearchBtn;
	}
	
	public TextField getVehstolenfield() {
		return vehstolenfield;
	}
	
	public Label getVersionLabel() {
		return versionLabel;
	}
	
	public TextField getOfficerInfoCallsign() {
		return OfficerInfoCallsign;
	}
	
	public Label getLogbrwsrlbl() {
		return logbrwsrlbl;
	}
	
	public TableView getArrestTable() {
		return arrestTable;
	}
	
	public TableView getCalloutTable() {
		return calloutTable;
	}
	
	public TableView getCitationTable() {
		return citationTable;
	}
	
	public TableView getImpoundTable() {
		return impoundTable;
	}
	
	public TableView getIncidentTable() {
		return incidentTable;
	}
	
	public TableView getPatrolTable() {
		return patrolTable;
	}
	
	public TableView getSearchTable() {
		return searchTable;
	}
	
	public TableView getTrafficStopTable() {
		return trafficStopTable;
	}
	
	public TabPane getTabPane() {
		return tabPane;
	}
	
	public VBox getBkgclr2() {
		return bkgclr2;
	}
	
	public VBox getBkgclr1() {
		return bkgclr1;
	}
	
	public ListView getCalHistoryList() {
		return calHistoryList;
	}
	
	public ListView getCalActiveList() {
		return calActiveList;
	}
	
	public Label getActivecalfill() {
		return activecalfill;
	}
	
	public Label getCalfill() {
		return calfill;
	}
	
	public Label getCalloutInfoTitle() {
		return calloutInfoTitle;
	}
	
	public AnchorPane getCurrentCalPane() {
		return currentCalPane;
	}
	
	public Label getServerStatusLabel() {
		return serverStatusLabel;
	}
	
	public Button getShowIDBtn() {
		return showIDBtn;
	}
	
	public MenuButton getCreateReportBtn() {
		return createReportBtn;
	}
	
	public Button getLogsButton() {
		return logsButton;
	}
	
	public Button getMapButton() {
		return mapButton;
	}
	
	public Button getShowCalloutBtn() {
		return showCalloutBtn;
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
	
	public BarChart getReportChart() {
		return reportChart;
	}
	
	public AreaChart getAreaReportChart() {
		return areaReportChart;
	}
	
	public Label getReportPlusLabelFill() {
		return reportPlusLabelFill;
	}
	
	public Label getSecondaryColor3Bkg() {
		return secondaryColor3Bkg;
	}
	
	public Label getSecondaryColor4Bkg() {
		return secondaryColor4Bkg;
	}
	
	public Label getCasePrim1() {
		return casePrim1;
	}
	
	public Label getCaseSec1() {
		return caseSec1;
	}
	
	public Label getCaseSec2() {
		return caseSec2;
	}
	
	//</editor-fold>
	
	//<editor-fold desc="Log Methods">
	
	private void loadLogs() {
		try {
			ImpoundReports impoundReports = ImpoundReportUtils.loadImpoundReports();
			List<ImpoundReport> impoundReportslist = impoundReports.getImpoundReportList();
			impoundLogUpdate(impoundReportslist);
		} catch (JAXBException e) {
			logError("Error loading ImpoundReports: ", e);
		}
		
		try {
			TrafficCitationReports trafficCitationReports = TrafficCitationUtils.loadTrafficCitationReports();
			List<TrafficCitationReport> trafficCitationList = trafficCitationReports.getTrafficCitationReportList();
			citationLogUpdate(trafficCitationList);
		} catch (JAXBException e) {
			logError("Error loading TrafficCitationReports: ", e);
		}
		
		try {
			PatrolReports patrolReports = PatrolReportUtils.loadPatrolReports();
			List<PatrolReport> patrolReportList = patrolReports.getPatrolReportList();
			patrolLogUpdate(patrolReportList);
		} catch (JAXBException e) {
			logError("Error loading PatrolReports: ", e);
		}
		
		try {
			ArrestReports arrestReports = ArrestReportUtils.loadArrestReports();
			List<ArrestReport> arrestReportList = arrestReports.getArrestReportList();
			arrestLogUpdate(arrestReportList);
		} catch (JAXBException e) {
			logError("Error loading ArrestReport: ", e);
		}
		
		try {
			SearchReports searchReports = SearchReportUtils.loadSearchReports();
			List<SearchReport> searchReportsList1 = searchReports.getSearchReportList();
			searchLogUpdate(searchReportsList1);
		} catch (JAXBException e) {
			logError("Error loading SearchReports: ", e);
		}
		
		try {
			IncidentReports incidentReports = IncidentReportUtils.loadIncidentReports();
			List<IncidentReport> incidentReportList1 = incidentReports.getIncidentReportList();
			incidentLogUpdate(incidentReportList1);
		} catch (JAXBException e) {
			logError("Error loading IncidentReports: ", e);
		}
		
		try {
			TrafficStopReports trafficStopReports = TrafficStopReportUtils.loadTrafficStopReports();
			List<TrafficStopReport> trafficStopReportList = trafficStopReports.getTrafficStopReportList();
			trafficStopLogUpdate(trafficStopReportList);
		} catch (JAXBException e) {
			logError("Error loading TrafficStopReports: ", e);
		}
		
		try {
			CalloutReports calloutReports = CalloutReportUtils.loadCalloutReports();
			List<CalloutReport> calloutReportListl = calloutReports.getCalloutReportList();
			calloutLogUpdate(calloutReportListl);
		} catch (JAXBException e) {
			logError("Error loading CalloutReports: ", e);
		}
		
		try {
			DeathReports deathReports = DeathReportUtils.loadDeathReports();
			List<DeathReport> deathReportList = deathReports.getDeathReportList();
			deathReportUpdate(deathReportList);
		} catch (JAXBException e) {
			logError("Error loading DeathReports: ", e);
		}
		
		try {
			AccidentReports accidentReports = AccidentReportUtils.loadAccidentReports();
			List<AccidentReport> accidentReportsList = accidentReports.getAccidentReportList();
			accidentReportUpdate(accidentReportsList);
		} catch (JAXBException e) {
			logError("Error loading accidentReports: ", e);
		}
	}
	
	public void citationLogUpdate(List<TrafficCitationReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		citationTable.getItems().clear();
		citationTable.getItems().addAll(logEntries);
	}
	
	public void patrolLogUpdate(List<PatrolReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		patrolTable.getItems().clear();
		patrolTable.getItems().addAll(logEntries);
	}
	
	public void arrestLogUpdate(List<ArrestReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		arrestTable.getItems().clear();
		arrestTable.getItems().addAll(logEntries);
	}
	
	public void searchLogUpdate(List<SearchReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		searchTable.getItems().clear();
		searchTable.getItems().addAll(logEntries);
	}
	
	public void incidentLogUpdate(List<IncidentReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		incidentTable.getItems().clear();
		incidentTable.getItems().addAll(logEntries);
	}
	
	public void trafficStopLogUpdate(List<TrafficStopReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		trafficStopTable.getItems().clear();
		trafficStopTable.getItems().addAll(logEntries);
	}
	
	public void calloutLogUpdate(List<CalloutReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		calloutTable.getItems().clear();
		calloutTable.getItems().addAll(logEntries);
	}
	
	public void impoundLogUpdate(List<ImpoundReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		impoundTable.getItems().clear();
		impoundTable.getItems().addAll(logEntries);
	}
	
	public void deathReportUpdate(List<DeathReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		
		deathReportTable.getItems().clear();
		deathReportTable.getItems().addAll(logEntries);
	}
	
	public void accidentReportUpdate(List<AccidentReport> logEntries) {
		if (logEntries == null) {
			logEntries = new ArrayList<>();
		}
		
		accidentReportTable.getItems().clear();
		accidentReportTable.getItems().addAll(logEntries);
	}
	
	@FXML
	public void onDeathReportRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			DeathReport deathReport = (DeathReport) deathReportTable.getSelectionModel().getSelectedItem();
			
			if (deathReport != null) {
				Map<String, Object> deathReportObj = newDeathReport(getReportChart(), getAreaReportChart());
				
				Map<String, Object> deathReport1 = (Map<String, Object>) deathReportObj.get("Death Report Map");
				
				TextField name = (TextField) deathReport1.get("name");
				TextField rank = (TextField) deathReport1.get("rank");
				TextField div = (TextField) deathReport1.get("division");
				TextField agen = (TextField) deathReport1.get("agency");
				TextField num = (TextField) deathReport1.get("number");
				TextField date = (TextField) deathReport1.get("date");
				TextField time = (TextField) deathReport1.get("time");
				ComboBox street = (ComboBox) deathReport1.get("street");
				ComboBox area = (ComboBox) deathReport1.get("area");
				TextField county = (TextField) deathReport1.get("county");
				TextField deathNum = (TextField) deathReport1.get("death num");
				TextField decedent = (TextField) deathReport1.get("decedent name");
				TextField age = (TextField) deathReport1.get("age/dob");
				TextField gender = (TextField) deathReport1.get("gender");
				TextField address = (TextField) deathReport1.get("address");
				TextField description = (TextField) deathReport1.get("description");
				TextField causeofdeath = (TextField) deathReport1.get("cause of death");
				TextField modeofdeath = (TextField) deathReport1.get("mode of death");
				TextField witnesses = (TextField) deathReport1.get("witnesses");
				TextArea notes = (TextArea) deathReport1.get("notes");
				TextField timeofdeath = (TextField) deathReport1.get("time of death");
				TextField dateofdeath = (TextField) deathReport1.get("date of death");
				
				timeofdeath.setText(deathReport.getTimeOfDeath());
				dateofdeath.setText(deathReport.getDateOfDeath());
				name.setText(deathReport.getName());
				rank.setText(deathReport.getRank());
				div.setText(deathReport.getDivision());
				agen.setText(deathReport.getAgency());
				num.setText(deathReport.getNumber());
				date.setText(deathReport.getDate());
				time.setText(deathReport.getTime());
				street.getEditor().setText(deathReport.getStreet());
				area.setValue(deathReport.getArea());
				county.setText(deathReport.getCounty());
				deathNum.setText(deathReport.getDeathReportNumber());
				decedent.setText(deathReport.getDecedent());
				age.setText(deathReport.getAge());
				gender.setText(deathReport.getGender());
				address.setText(deathReport.getAddress());
				description.setText(deathReport.getDescription());
				causeofdeath.setText(deathReport.getCauseOfDeath());
				modeofdeath.setText(deathReport.getModeOfDeath());
				witnesses.setText(deathReport.getWitnesses());
				notes.setText(deathReport.getNotesTextArea());
				
				BorderPane root = (BorderPane) deathReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) deathReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = deathNum.getText();
					try {
						DeathReportUtils.deleteDeathReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete DeathReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				deathNum.setEditable(false);
				MenuButton pullnotesbtn = (MenuButton) deathReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				
				Button submitBtn = (Button) deathReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				deathReportTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@FXML
	public void onCalloutRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			CalloutReport calloutReport = (CalloutReport) calloutTable.getSelectionModel().getSelectedItem();
			
			if (calloutReport != null) {
				
				Map<String, Object> calloutReportObj = newCallout(getReportChart(), getAreaReportChart());
				
				Map<String, Object> calloutReportMap = (Map<String, Object>) calloutReportObj.get("Callout Report Map");
				
				TextField officername = (TextField) calloutReportMap.get("name");
				TextField officerrank = (TextField) calloutReportMap.get("rank");
				TextField officerdiv = (TextField) calloutReportMap.get("division");
				TextField officeragen = (TextField) calloutReportMap.get("agency");
				TextField officernum = (TextField) calloutReportMap.get("number");
				TextField calloutnum = (TextField) calloutReportMap.get("calloutnumber");
				ComboBox calloutarea = (ComboBox) calloutReportMap.get("area");
				TextArea calloutnotes = (TextArea) calloutReportMap.get("notes");
				TextField calloutcounty = (TextField) calloutReportMap.get("county");
				ComboBox calloutstreet = (ComboBox) calloutReportMap.get("street");
				TextField calloutdate = (TextField) calloutReportMap.get("date");
				TextField callouttime = (TextField) calloutReportMap.get("time");
				TextField callouttype = (TextField) calloutReportMap.get("type");
				TextField calloutcode = (TextField) calloutReportMap.get("code");
				
				officername.setText(calloutReport.getName());
				officerrank.setText(calloutReport.getRank());
				officerdiv.setText(calloutReport.getDivision());
				officeragen.setText(calloutReport.getAgency());
				officernum.setText(calloutReport.getNumber());
				calloutdate.setText(calloutReport.getDate());
				callouttime.setText(calloutReport.getTime());
				calloutstreet.setValue(calloutReport.getAddress());
				calloutarea.setValue(calloutReport.getArea());
				calloutcounty.setText(calloutReport.getCounty());
				calloutnotes.setText(calloutReport.getNotesTextArea());
				calloutnum.setText(calloutReport.getCalloutNumber());
				callouttype.setText(calloutReport.getResponseType());
				calloutcode.setText(calloutReport.getResponseGrade());
				
				BorderPane root = (BorderPane) calloutReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) calloutReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = calloutnum.getText();
					try {
						CalloutReportUtils.deleteCalloutReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete CalloutReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				MenuButton pullnotesbtn = (MenuButton) calloutReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				calloutnum.setEditable(false);
				
				Button submitBtn = (Button) calloutReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				calloutTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@FXML
	public void onPatrolRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			PatrolReport patrolReport = (PatrolReport) patrolTable.getSelectionModel().getSelectedItem();
			
			if (patrolReport != null) {
				
				Map<String, Object> patrolReportObj = newPatrol(getReportChart(), getAreaReportChart());
				
				Map<String, Object> patrolReportMap = (Map<String, Object>) patrolReportObj.get("Patrol Report Map");
				
				TextField name = (TextField) patrolReportMap.get("name");
				TextField rank = (TextField) patrolReportMap.get("rank");
				TextField div = (TextField) patrolReportMap.get("division");
				TextField agen = (TextField) patrolReportMap.get("agency");
				TextField num = (TextField) patrolReportMap.get("number");
				TextField patrolnum = (TextField) patrolReportMap.get("patrolnumber");
				TextArea notes = (TextArea) patrolReportMap.get("notes");
				TextField date = (TextField) patrolReportMap.get("date");
				TextField starttime = (TextField) patrolReportMap.get("starttime");
				TextField stoptime = (TextField) patrolReportMap.get("stoptime");
				TextField length = (TextField) patrolReportMap.get("length");
				TextField vehicle = (TextField) patrolReportMap.get("vehicle");
				
				name.setText(patrolReport.getOfficerName());
				patrolnum.setText(patrolReport.getPatrolNumber());
				rank.setText(patrolReport.getOfficerRank());
				div.setText(patrolReport.getOfficerDivision());
				agen.setText(patrolReport.getOfficerAgency());
				num.setText(patrolReport.getOfficerNumber());
				date.setText(patrolReport.getPatrolDate());
				starttime.setText(patrolReport.getPatrolStartTime());
				stoptime.setText(patrolReport.getPatrolStopTime());
				length.setText(patrolReport.getPatrolLength());
				vehicle.setText(patrolReport.getOfficerVehicle());
				notes.setText(patrolReport.getPatrolComments());
				
				BorderPane root = (BorderPane) patrolReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) patrolReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = patrolnum.getText();
					try {
						PatrolReportUtils.deletePatrolReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete PatrolReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				MenuButton pullnotesbtn = (MenuButton) patrolReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				patrolnum.setEditable(false);
				
				Button submitBtn = (Button) patrolReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				patrolTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@FXML
	public void onTrafficStopRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			TrafficStopReport trafficStopReport = (TrafficStopReport) trafficStopTable.getSelectionModel().getSelectedItem();
			
			if (trafficStopReport != null) {
				
				Map<String, Object> trafficStopReportObj = newTrafficStop(getReportChart(), getAreaReportChart());
				
				Map<String, Object> trafficStopReportMap = (Map<String, Object>) trafficStopReportObj.get(
						"Traffic Stop Report Map");
				
				TextField officernamets = (TextField) trafficStopReportMap.get("name");
				TextField officerrankts = (TextField) trafficStopReportMap.get("rank");
				TextField officerdivts = (TextField) trafficStopReportMap.get("division");
				TextField officeragents = (TextField) trafficStopReportMap.get("agency");
				TextField officernumarrestts = (TextField) trafficStopReportMap.get("number");
				
				TextField offenderNamets = (TextField) trafficStopReportMap.get("offender name");
				TextField offenderAgets = (TextField) trafficStopReportMap.get("offender age");
				TextField offenderGenderts = (TextField) trafficStopReportMap.get("offender gender");
				TextField offenderAddressts = (TextField) trafficStopReportMap.get("offender address");
				TextField offenderDescriptionts = (TextField) trafficStopReportMap.get("offender description");
				
				ComboBox colorts = (ComboBox) trafficStopReportMap.get("color");
				ComboBox typets = (ComboBox) trafficStopReportMap.get("type");
				TextField plateNumberts = (TextField) trafficStopReportMap.get("plate number");
				TextField otherInfots = (TextField) trafficStopReportMap.get("other info");
				TextField modelts = (TextField) trafficStopReportMap.get("model");
				
				ComboBox areats = (ComboBox) trafficStopReportMap.get("area");
				ComboBox streetts = (ComboBox) trafficStopReportMap.get("street");
				TextField countyts = (TextField) trafficStopReportMap.get("county");
				TextField stopnumts = (TextField) trafficStopReportMap.get("stop number");
				TextField datets = (TextField) trafficStopReportMap.get("date");
				TextField timets = (TextField) trafficStopReportMap.get("time");
				
				TextArea notests = (TextArea) trafficStopReportMap.get("notes");
				
				stopnumts.setText(trafficStopReport.getStopNumber());
				datets.setText(trafficStopReport.getDate());
				timets.setText(trafficStopReport.getTime());
				officerrankts.setText(trafficStopReport.getRank());
				notests.setText(trafficStopReport.getCommentsTextArea());
				plateNumberts.setText(trafficStopReport.getPlateNumber());
				
				offenderDescriptionts.setText(trafficStopReport.getOperatorDescription());
				otherInfots.setText(trafficStopReport.getResponseOtherInfo());
				areats.setValue(trafficStopReport.getArea());
				streetts.getEditor().setText(trafficStopReport.getStreet());
				countyts.setText(trafficStopReport.getCounty());
				
				offenderNamets.setText(trafficStopReport.getOperatorName());
				officernamets.setText(trafficStopReport.getName());
				officerdivts.setText(trafficStopReport.getDivision());
				officeragents.setText(trafficStopReport.getAgency());
				officernumarrestts.setText(trafficStopReport.getNumber());
				offenderAgets.setText(trafficStopReport.getOperatorAge());
				offenderGenderts.setText(trafficStopReport.getOperatorGender());
				offenderAddressts.setText(trafficStopReport.getOperatorAddress());
				colorts.setValue(trafficStopReport.getColor());
				typets.setValue(trafficStopReport.getType());
				modelts.setText(trafficStopReport.getResponseModel());
				
				BorderPane root = (BorderPane) trafficStopReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) trafficStopReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = stopnumts.getText();
					try {
						TrafficStopReportUtils.deleteTrafficStopReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete TrafficStopReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				MenuButton pullnotesbtn = (MenuButton) trafficStopReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				stopnumts.setEditable(false);
				
				Button submitBtn = (Button) trafficStopReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				trafficStopTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@FXML
	public void onIncidentRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			IncidentReport incidentReport = (IncidentReport) incidentTable.getSelectionModel().getSelectedItem();
			
			if (incidentReport != null) {
				
				Map<String, Object> incidentReportObj = newIncident(getReportChart(), getAreaReportChart());
				
				Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReportObj.get(
						"Incident Report Map");
				
				TextField name = (TextField) incidentReportMap.get("name");
				TextField rank = (TextField) incidentReportMap.get("rank");
				TextField div = (TextField) incidentReportMap.get("division");
				TextField agen = (TextField) incidentReportMap.get("agency");
				TextField num = (TextField) incidentReportMap.get("number");
				
				TextField incidentnum = (TextField) incidentReportMap.get("incident num");
				TextField date = (TextField) incidentReportMap.get("date");
				TextField time = (TextField) incidentReportMap.get("time");
				ComboBox street = (ComboBox) incidentReportMap.get("street");
				ComboBox area = (ComboBox) incidentReportMap.get("area");
				TextField county = (TextField) incidentReportMap.get("county");
				
				TextField suspects = (TextField) incidentReportMap.get("suspect(s)");
				TextField vicwit = (TextField) incidentReportMap.get("victim(s) / witness(s)");
				TextArea statement = (TextArea) incidentReportMap.get("statement");
				
				TextArea summary = (TextArea) incidentReportMap.get("summary");
				TextArea notes = (TextArea) incidentReportMap.get("notes");
				
				name.setText(incidentReport.getOfficerName());
				incidentnum.setText(incidentReport.getIncidentNumber());
				rank.setText(incidentReport.getOfficerRank());
				div.setText(incidentReport.getOfficerDivision());
				agen.setText(incidentReport.getOfficerAgency());
				num.setText(incidentReport.getOfficerNumber());
				
				street.getEditor().setText(incidentReport.getIncidentStreet());
				area.setValue(incidentReport.getIncidentArea());
				county.setText(incidentReport.getIncidentCounty());
				suspects.setText(incidentReport.getIncidentWitnesses());
				vicwit.setText(incidentReport.getIncidentVictims());
				statement.setText(incidentReport.getIncidentStatement());
				
				date.setText(incidentReport.getIncidentDate());
				time.setText(incidentReport.getIncidentTime());
				summary.setText(incidentReport.getIncidentActionsTaken());
				notes.setText(incidentReport.getIncidentComments());
				
				BorderPane root = (BorderPane) incidentReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) incidentReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = incidentnum.getText();
					try {
						IncidentReportUtils.deleteIncidentReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete IncidentReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				MenuButton pullnotesbtn = (MenuButton) incidentReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				incidentnum.setEditable(false);
				
				Button submitBtn = (Button) incidentReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				incidentTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@FXML
	public void onImpoundRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			ImpoundReport impoundReport = (ImpoundReport) impoundTable.getSelectionModel().getSelectedItem();
			
			if (impoundReport != null) {
				
				Map<String, Object> impoundReportObj = newImpound(getReportChart(), getAreaReportChart());
				
				Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get("Impound Report Map");
				
				TextField officername = (TextField) impoundReportMap.get("name");
				TextField officerrank = (TextField) impoundReportMap.get("rank");
				TextField officerdiv = (TextField) impoundReportMap.get("division");
				TextField officeragen = (TextField) impoundReportMap.get("agency");
				TextField officernum = (TextField) impoundReportMap.get("number");
				
				TextField offenderName = (TextField) impoundReportMap.get("offender name");
				TextField offenderAge = (TextField) impoundReportMap.get("offender age");
				TextField offenderGender = (TextField) impoundReportMap.get("offender gender");
				TextField offenderAddress = (TextField) impoundReportMap.get("offender address");
				
				TextField num = (TextField) impoundReportMap.get("impound number");
				TextField date = (TextField) impoundReportMap.get("date");
				TextField time = (TextField) impoundReportMap.get("time");
				
				ComboBox color = (ComboBox) impoundReportMap.get("color");
				ComboBox type = (ComboBox) impoundReportMap.get("type");
				TextField plateNumber = (TextField) impoundReportMap.get("plate number");
				TextField model = (TextField) impoundReportMap.get("model");
				
				TextArea notes = (TextArea) impoundReportMap.get("notes");
				
				num.setText(impoundReport.getImpoundNumber());
				date.setText(impoundReport.getImpoundDate());
				time.setText(impoundReport.getImpoundTime());
				officerrank.setText(impoundReport.getOfficerRank());
				notes.setText(impoundReport.getImpoundComments());
				plateNumber.setText(impoundReport.getImpoundPlateNumber());
				
				offenderName.setText(impoundReport.getOwnerName());
				officername.setText(impoundReport.getOfficerName());
				officerdiv.setText(impoundReport.getOfficerDivision());
				officeragen.setText(impoundReport.getOfficerAgency());
				officernum.setText(impoundReport.getOfficerNumber());
				offenderAge.setText(impoundReport.getOwnerAge());
				offenderGender.setText(impoundReport.getOwnerGender());
				offenderAddress.setText(impoundReport.getOwnerAddress());
				color.setValue(impoundReport.getImpoundColor());
				type.setValue(impoundReport.getImpoundType());
				model.setText(impoundReport.getImpoundModel());
				
				BorderPane root = (BorderPane) impoundReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) impoundReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = num.getText();
					try {
						ImpoundReportUtils.deleteImpoundReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete ImpoundReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				MenuButton pullnotesbtn = (MenuButton) impoundReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				num.setEditable(false);
				
				Button submitBtn = (Button) impoundReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				impoundTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@FXML
	public void onCitationRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			TrafficCitationReport trafficCitationReport = (TrafficCitationReport) citationTable.getSelectionModel().getSelectedItem();
			
			if (trafficCitationReport != null) {
				Map<String, Object> trafficCitationObj = newCitation(getReportChart(), getAreaReportChart());
				
				Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get(
						"Citation Report Map");
				
				TextField officername = (TextField) citationReportMap.get("name");
				TextField officerrank = (TextField) citationReportMap.get("rank");
				TextField officerdiv = (TextField) citationReportMap.get("division");
				TextField officeragen = (TextField) citationReportMap.get("agency");
				TextField officernum = (TextField) citationReportMap.get("number");
				
				TextField offenderName = (TextField) citationReportMap.get("offender name");
				TextField offenderAge = (TextField) citationReportMap.get("offender age");
				TextField offenderGender = (TextField) citationReportMap.get("offender gender");
				TextField offenderAddress = (TextField) citationReportMap.get("offender address");
				TextField offenderDescription = (TextField) citationReportMap.get("offender description");
				
				ComboBox area = (ComboBox) citationReportMap.get("area");
				ComboBox street = (ComboBox) citationReportMap.get("street");
				TextField county = (TextField) citationReportMap.get("county");
				TextField num = (TextField) citationReportMap.get("citation number");
				TextField date = (TextField) citationReportMap.get("date");
				TextField time = (TextField) citationReportMap.get("time");
				
				ComboBox color = (ComboBox) citationReportMap.get("color");
				ComboBox type = (ComboBox) citationReportMap.get("type");
				TextField plateNumber = (TextField) citationReportMap.get("plate number");
				TextField otherInfo = (TextField) citationReportMap.get("other info");
				TextField model = (TextField) citationReportMap.get("model");
				
				TextArea notes = (TextArea) citationReportMap.get("notes");
				
				officername.setText(trafficCitationReport.getOfficerName());
				officerrank.setText(trafficCitationReport.getOfficerRank());
				officerdiv.setText(trafficCitationReport.getOfficerDivision());
				officeragen.setText(trafficCitationReport.getOfficerAgency());
				officernum.setText(trafficCitationReport.getOfficerNumber());
				street.setValue(trafficCitationReport.getCitationStreet());
				area.setValue(trafficCitationReport.getCitationArea());
				county.setText(trafficCitationReport.getCitationCounty());
				type.setValue(trafficCitationReport.getOffenderVehicleType());
				color.setValue(trafficCitationReport.getOffenderVehicleColor());
				date.setText(trafficCitationReport.getCitationDate());
				time.setText(trafficCitationReport.getCitationTime());
				notes.setText(trafficCitationReport.getCitationComments());
				num.setText(trafficCitationReport.getCitationNumber());
				plateNumber.setText(trafficCitationReport.getOffenderVehiclePlate());
				otherInfo.setText(trafficCitationReport.getOffenderVehicleOther());
				model.setText(trafficCitationReport.getOffenderVehicleModel());
				offenderName.setText(trafficCitationReport.getOffenderName());
				offenderAge.setText(trafficCitationReport.getOffenderAge());
				offenderGender.setText(trafficCitationReport.getOffenderGender());
				offenderDescription.setText(trafficCitationReport.getOffenderDescription());
				offenderAddress.setText(trafficCitationReport.getOffenderHomeAddress());
				
				BorderPane root = (BorderPane) trafficCitationObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) trafficCitationObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = num.getText();
					try {
						TrafficCitationUtils.deleteTrafficCitationReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete TrafficCitationReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				MenuButton pullnotesbtn = (MenuButton) trafficCitationObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				num.setEditable(false);
				
				TableView citationtable1 = (TableView) citationReportMap.get("CitationTableView");
				ObservableList<CitationsData> citationList = FXCollections.observableArrayList();
				citationtable1.setItems(citationList);
				
				addCitationsToTable(trafficCitationReport.getCitationCharges(), citationList);
				
				Button submitBtn = (Button) trafficCitationObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				citationTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@FXML
	public void onSearchRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			SearchReport searchReport = (SearchReport) searchTable.getSelectionModel().getSelectedItem();
			
			if (searchReport != null) {
				Map<String, Object> searchReportObj = newSearch(getReportChart(), getAreaReportChart());
				
				Map<String, Object> searchReportMap = (Map<String, Object>) searchReportObj.get("Search Report Map");
				
				TextField name = (TextField) searchReportMap.get("name");
				TextField rank = (TextField) searchReportMap.get("rank");
				TextField div = (TextField) searchReportMap.get("division");
				TextField agen = (TextField) searchReportMap.get("agency");
				TextField num = (TextField) searchReportMap.get("number");
				
				TextField searchnum = (TextField) searchReportMap.get("search num");
				TextField date = (TextField) searchReportMap.get("date");
				TextField time = (TextField) searchReportMap.get("time");
				ComboBox street = (ComboBox) searchReportMap.get("street");
				ComboBox area = (ComboBox) searchReportMap.get("area");
				TextField county = (TextField) searchReportMap.get("county");
				
				TextField grounds = (TextField) searchReportMap.get("grounds for search");
				TextField witness = (TextField) searchReportMap.get("witness(s)");
				TextField searchedindividual = (TextField) searchReportMap.get("searched individual");
				ComboBox type = (ComboBox) searchReportMap.get("search type");
				ComboBox method = (ComboBox) searchReportMap.get("search method");
				
				TextField testconducted = (TextField) searchReportMap.get("test(s) conducted");
				TextField result = (TextField) searchReportMap.get("result");
				TextField bacmeasurement = (TextField) searchReportMap.get("bac measurement");
				
				TextArea seizeditems = (TextArea) searchReportMap.get("seized item(s)");
				TextArea notes = (TextArea) searchReportMap.get("comments");
				
				name.setText(searchReport.getOfficerName());
				div.setText(searchReport.getOfficerDivision());
				agen.setText(searchReport.getOfficerAgency());
				num.setText(searchReport.getOfficerNumber());
				
				street.getEditor().setText(searchReport.getSearchStreet());
				area.setValue(searchReport.getSearchArea());
				county.setText(searchReport.getSearchCounty());
				
				testconducted.setText(searchReport.getTestsConducted());
				grounds.setText(searchReport.getSearchGrounds());
				witness.setText(searchReport.getSearchWitnesses());
				searchedindividual.setText(searchReport.getSearchedPersons());
				type.setValue(searchReport.getSearchType());
				method.setValue(searchReport.getSearchMethod());
				result.setText(searchReport.getTestResults());
				bacmeasurement.setText(searchReport.getBreathalyzerBACMeasure());
				
				searchnum.setText(searchReport.getSearchNumber());
				rank.setText(searchReport.getOfficerRank());
				date.setText(searchReport.getSearchDate());
				time.setText(searchReport.getSearchTime());
				seizeditems.setText(searchReport.getSearchSeizedItems());
				notes.setText(searchReport.getSearchComments());
				
				BorderPane root = (BorderPane) searchReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) searchReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = searchnum.getText();
					try {
						SearchReportUtils.deleteSearchReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete SearchReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				MenuButton pullnotesbtn = (MenuButton) searchReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				searchnum.setEditable(false);
				
				Button submitBtn = (Button) searchReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				searchTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@FXML
	public void onArrestRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			ArrestReport arrestReport = (ArrestReport) arrestTable.getSelectionModel().getSelectedItem();
			
			if (arrestReport != null) {
				Map<String, Object> arrestReportObj = newArrest(reportChart, areaReportChart);
				
				Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get("Arrest Report Map");
				
				TextField officername = (TextField) arrestReportMap.get("name");
				TextField officerrank = (TextField) arrestReportMap.get("rank");
				TextField officerdiv = (TextField) arrestReportMap.get("division");
				TextField officeragen = (TextField) arrestReportMap.get("agency");
				TextField officernumarrest = (TextField) arrestReportMap.get("number");
				
				TextField offenderName = (TextField) arrestReportMap.get("offender name");
				TextField offenderAge = (TextField) arrestReportMap.get("offender age");
				TextField offenderGender = (TextField) arrestReportMap.get("offender gender");
				TextField offenderAddress = (TextField) arrestReportMap.get("offender address");
				TextField offenderDescription = (TextField) arrestReportMap.get("offender description");
				
				ComboBox area = (ComboBox) arrestReportMap.get("area");
				ComboBox street = (ComboBox) arrestReportMap.get("street");
				TextField county = (TextField) arrestReportMap.get("county");
				TextField arrestnum = (TextField) arrestReportMap.get("arrest number");
				TextField date = (TextField) arrestReportMap.get("date");
				TextField time = (TextField) arrestReportMap.get("time");
				
				TextField ambulancereq = (TextField) arrestReportMap.get("ambulance required (Y/N)");
				TextField taserdep = (TextField) arrestReportMap.get("taser deployed (Y/N)");
				TextField othermedinfo = (TextField) arrestReportMap.get("other information");
				
				TextArea notes = (TextArea) arrestReportMap.get("notes");
				
				arrestnum.setText(arrestReport.getArrestNumber());
				officername.setText(arrestReport.getOfficerName());
				officerdiv.setText(arrestReport.getOfficerDivision());
				officeragen.setText(arrestReport.getOfficerAgency());
				officernumarrest.setText(arrestReport.getOfficerNumber());
				officerrank.setText(arrestReport.getOfficerRank());
				offenderName.setText(arrestReport.getArresteeName());
				offenderAge.setText(arrestReport.getArresteeAge());
				offenderGender.setText(arrestReport.getArresteeGender());
				offenderAddress.setText(arrestReport.getArresteeHomeAddress());
				offenderDescription.setText(arrestReport.getArresteeDescription());
				street.getEditor().setText(arrestReport.getArrestStreet());
				county.setText(arrestReport.getArrestCounty());
				ambulancereq.setText(arrestReport.getAmbulanceYesNo());
				taserdep.setText(arrestReport.getTaserYesNo());
				othermedinfo.setText(arrestReport.getArresteeMedicalInformation());
				area.setValue(arrestReport.getArrestArea());
				date.setText(arrestReport.getArrestDate());
				time.setText(arrestReport.getArrestTime());
				notes.setText(arrestReport.getArrestDetails());
				
				BorderPane root = (BorderPane) arrestReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) arrestReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = arrestnum.getText();
					try {
						ArrestReportUtils.deleteArrestReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete ArrestReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				MenuButton pullnotesbtn = (MenuButton) arrestReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				arrestnum.setEditable(false);
				
				TableView chargetable = (TableView) arrestReportMap.get("ChargeTableView");
				ObservableList<ChargesData> chargeList = FXCollections.observableArrayList();
				chargetable.setItems(chargeList);
				
				addChargesToTable(arrestReport.getArrestCharges(), chargeList);
				
				Button submitBtn = (Button) arrestReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				arrestTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@FXML
	public void onAccidentReportRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			AccidentReport accidentReport = (AccidentReport) accidentReportTable.getSelectionModel().getSelectedItem();
			
			if (accidentReport != null) {
				Map<String, Object> accidentReportObj = newAccident(reportChart, areaReportChart);
				
				Map<String, Object> accidentReportMap = (Map<String, Object>) accidentReportObj.get(
						"Accident Report Map");
				
				TextField name = (TextField) accidentReportMap.get("name");
				TextField rank = (TextField) accidentReportMap.get("rank");
				TextField div = (TextField) accidentReportMap.get("division");
				TextField agen = (TextField) accidentReportMap.get("agency");
				TextField num = (TextField) accidentReportMap.get("number");
				ComboBox street = (ComboBox) accidentReportMap.get("street");
				ComboBox area = (ComboBox) accidentReportMap.get("area");
				TextField county = (TextField) accidentReportMap.get("county");
				TextField date = (TextField) accidentReportMap.get("date");
				TextField time = (TextField) accidentReportMap.get("time");
				TextField accidentnum = (TextField) accidentReportMap.get("accident number");
				TextField weatherConditions = (TextField) accidentReportMap.get("weather conditions");
				TextField roadConditions = (TextField) accidentReportMap.get("road conditions");
				TextField otherVehiclesInvolved = (TextField) accidentReportMap.get("other vehicles involved");
				TextField witnesses = (TextField) accidentReportMap.get("witnesses");
				TextField injuries = (TextField) accidentReportMap.get("injuries");
				TextField damages = (TextField) accidentReportMap.get("damages");
				TextField offenderName = (TextField) accidentReportMap.get("offender name");
				TextField offenderAge = (TextField) accidentReportMap.get("offender age");
				TextField offenderGender = (TextField) accidentReportMap.get("offender gender");
				TextField offenderAddress = (TextField) accidentReportMap.get("offender address");
				TextField offenderDescription = (TextField) accidentReportMap.get("offender description");
				TextField model = (TextField) accidentReportMap.get("model");
				TextField plateNumber = (TextField) accidentReportMap.get("plate number");
				ComboBox type = (ComboBox) accidentReportMap.get("type");
				ComboBox color = (ComboBox) accidentReportMap.get("color");
				TextArea notes = (TextArea) accidentReportMap.get("notes");
				
				accidentnum.setText(accidentReport.getAccidentNumber());
				name.setText(accidentReport.getOfficerName());
				rank.setText(accidentReport.getOfficerRank());
				div.setText(accidentReport.getOfficerDivision());
				agen.setText(accidentReport.getOfficerAgency());
				num.setText(accidentReport.getOfficerNumber());
				street.setValue(accidentReport.getStreet());
				area.setValue(accidentReport.getArea());
				county.setText(accidentReport.getCounty());
				date.setText(accidentReport.getAccidentDate());
				time.setText(accidentReport.getAccidentTime());
				weatherConditions.setText(accidentReport.getWeatherConditions());
				roadConditions.setText(accidentReport.getRoadConditions());
				otherVehiclesInvolved.setText(accidentReport.getOtherVehiclesInvolved());
				witnesses.setText(accidentReport.getWitnesses());
				injuries.setText(accidentReport.getInjuriesReported());
				damages.setText(accidentReport.getDamageDetails());
				offenderName.setText(accidentReport.getOwnerName());
				offenderAge.setText(accidentReport.getOwnerAge());
				offenderGender.setText(accidentReport.getOwnerGender());
				offenderAddress.setText(accidentReport.getOwnerAddress());
				offenderDescription.setText(accidentReport.getOwnerDescription());
				model.setText(accidentReport.getModel());
				plateNumber.setText(accidentReport.getPlateNumber());
				type.setValue(accidentReport.getType());
				color.setValue(accidentReport.getColor());
				notes.setText(accidentReport.getComments());
				
				BorderPane root = (BorderPane) accidentReportObj.get("root");
				Stage stage = (Stage) root.getScene().getWindow();
				Button delBtn = (Button) accidentReportObj.get("delBtn");
				delBtn.setVisible(true);
				delBtn.setDisable(false);
				delBtn.setOnAction(actionEvent -> {
					String numToDelete = accidentnum.getText();
					try {
						AccidentReportUtils.deleteAccidentReport(numToDelete);
						showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete, mainRT);
					} catch (JAXBException e) {
						logError("Could not delete AccidentReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					try {
						if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-delete.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playDeleteReport: ", e);
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				MenuButton pullnotesbtn = (MenuButton) accidentReportObj.get("pullNotesBtn");
				pullnotesbtn.setVisible(false);
				accidentnum.setEditable(false);
				
				Button submitBtn = (Button) accidentReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				accidentReportTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	//</editor-fold>
	
	//<editor-fold desc="VARS">
	
	public static String notesText;
	public static SimpleIntegerProperty needRefresh = new SimpleIntegerProperty();
	public static SimpleIntegerProperty needCourtRefresh = new SimpleIntegerProperty();
	public static Stage IDStage = null;
	public static Stage settingsStage = null;
	public static Stage probabilitySettingsStage = null;
	public static Stage CalloutStage = null;
	public static ClientController clientController;
	public static Stage notesStage = null;
	public static Stage clientStage = null;
	private static Stage mapStage = null;
	private static Stage versionStage = null;
	public static boolean IDFirstShown = true;
	public static double IDx;
	public static double IDy;
	public static Screen IDScreen = null;
	public static Screen CalloutScreen = null;
	public static boolean CalloutFirstShown = true;
	public static double Calloutx;
	public static double Callouty;
	public static boolean NotesFirstShown = true;
	public static double notesx;
	public static double notesy;
	public static Screen NotesScreen = null;
	private static final ScheduledExecutorService courtPendingChargesExecutor = Executors.newScheduledThreadPool(2);
	private final List<String> recentPedSearches = new ArrayList<>();
	private final List<String> recentVehicleSearches = new ArrayList<>();
	
	//</editor-fold>
	
	//<editor-fold desc="Events">
	
	@FXML
	public void oritentationBtnPress(ActionEvent actionEvent) {
		if (lookupSplitPane.getOrientation().equals(Orientation.HORIZONTAL)) {
			lookupSplitPane.setOrientation(Orientation.VERTICAL);
			ConfigWriter.configwrite("lookupWindow", "lookupOrientation", "vertical");
		} else {
			lookupSplitPane.setOrientation(Orientation.HORIZONTAL);
			ConfigWriter.configwrite("lookupWindow", "lookupOrientation", "horizontal");
		}
	}
	
	@FXML
	public void onLookupBtnClick(ActionEvent actionEvent) throws IOException {
		showAnimation(showLookupBtn);
		setDisable(logPane, shiftInformationPane, calloutPane, courtPane);
		
		if (ConfigReader.configRead("lookupWindow", "vehLookupVisible").equalsIgnoreCase("true")) {
			if (!lookupSplitPane.getItems().contains(vehPane)) {
				lookupSplitPane.getItems().add(vehPane);
			}
		} else {
			lookupSplitPane.getItems().remove(vehPane);
		}
		
		if (ConfigReader.configRead("lookupWindow", "pedLookupVisible").equalsIgnoreCase("true")) {
			if (!lookupSplitPane.getItems().contains(pedPane)) {
				lookupSplitPane.getItems().add(pedPane);
			}
		} else {
			lookupSplitPane.getItems().remove(pedPane);
		}
		
		setActive(lookupPane);
	}
	
	@FXML
	public void onPedLookupCheckboxClick(ActionEvent actionEvent) {
		if (lookupPedCheckbox.isSelected()) {
			ConfigWriter.configwrite("lookupWindow", "pedLookupVisible", "true");
			if (!lookupSplitPane.getItems().contains(pedPane)) {
				lookupSplitPane.getItems().add(pedPane);
			}
		} else {
			ConfigWriter.configwrite("lookupWindow", "pedLookupVisible", "false");
			lookupSplitPane.getItems().remove(pedPane);
		}
		adjustDividerPositions();
	}
	
	@FXML
	public void onVehLookupCheckboxClick(ActionEvent actionEvent) {
		if (lookupVehCheckbox.isSelected()) {
			ConfigWriter.configwrite("lookupWindow", "vehLookupVisible", "true");
			if (!lookupSplitPane.getItems().contains(vehPane)) {
				lookupSplitPane.getItems().add(vehPane);
			}
		} else {
			ConfigWriter.configwrite("lookupWindow", "vehLookupVisible", "false");
			lookupSplitPane.getItems().remove(vehPane);
		}
		adjustDividerPositions();
	}
	
	@FXML
	public void onLookupProbabilitySettingsClick(ActionEvent actionEvent) throws IOException {
		if (probabilitySettingsStage != null && probabilitySettingsStage.isShowing()) {
			probabilitySettingsStage.close();
			probabilitySettingsStage = null;
			return;
		}
		probabilitySettingsStage = new Stage();
		probabilitySettingsStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(
				Launcher.class.getResource("Windows/Settings/probability-settings-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		probabilitySettingsStage.setTitle("Probability Settings");
		probabilitySettingsStage.setScene(newScene);
		probabilitySettingsStage.show();
		probabilitySettingsStage.centerOnScreen();
		probabilitySettingsStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTSettings").equals("true"));
		
		centerStageOnMainApp(probabilitySettingsStage);
		
		probabilitySettingsStage.setMaxHeight(probabilitySettingsStage.getHeight());
		probabilitySettingsStage.setMinHeight(probabilitySettingsStage.getHeight());
		probabilitySettingsStage.setMinWidth(probabilitySettingsStage.getWidth());
		
		probabilitySettingsStage.setOnHidden(event -> probabilitySettingsStage = null);
	}
	
	@FXML
	public void deleteCaseBtnPress(ActionEvent actionEvent) {
		String selectedCaseNum;
		if (!caseNumField.getText().isEmpty() && caseNumField != null) {
			selectedCaseNum = caseNumField.getText();
			try {
				deleteCase(selectedCaseNum);
			} catch (JAXBException e) {
				logError("Could not delete case, JAXBException:", e);
			} catch (IOException e) {
				logError("Could not delete case, IOException:", e);
			}
			blankCourtInfoPane.setVisible(true);
			courtInfoPane.setVisible(false);
			loadCaseLabels(caseList);
		}
	}
	
	@FXML
	public void revealOutcomeBtnPress(ActionEvent actionEvent) {
		String selectedCaseNum;
		if (!caseNumField.getText().isEmpty() && caseNumField != null) {
			selectedCaseNum = caseNumField.getText();
			Optional<Case> caseToUpdateOptional = findCaseByNumber(selectedCaseNum);
			if (caseToUpdateOptional.isPresent()) {
				Case caseToUpdate = caseToUpdateOptional.get();
				if (!caseToUpdate.getStatus().equalsIgnoreCase("closed")) {
					try {
						caseToUpdate.setStatus("Closed");
						modifyCase(caseToUpdate.getCaseNumber(), caseToUpdate);
						log("Case: #" + caseToUpdate.getCaseNumber() + " Outcomes Revealed", Severity.INFO);
						updateFields(caseToUpdate);
						loadCaseLabels(caseList);
						
					} catch (JAXBException e) {
						logError("Could not RevealOutcomes case#" + caseToUpdate.getCaseNumber() + ", JAXBException: ",
						         e);
					} catch (IOException e) {
						logError("Could not RevealOutcomes case#" + caseToUpdate.getCaseNumber() + ", IOException: ",
						         e);
					}
				} else {
					log("Case: #" + caseToUpdate.getCaseNumber() + " Outcomes Already Revealed!", Severity.WARN);
					showNotificationWarning("Court Manager",
					                        "Case: #" + caseToUpdate.getCaseNumber() + " Outcomes Already Revealed",
					                        mainRT);
				}
			}
		}
	}
	
	@FXML
	public void onShowCourtCasesButtonClick(ActionEvent actionEvent) {
		setDisable(logPane, lookupPane, calloutPane, courtPane, shiftInformationPane);
		setActive(courtPane);
		showAnimation(showCourtCasesBtn);
		
		blankCourtInfoPane.setVisible(true);
		courtInfoPane.setVisible(false);
		
		loadCaseLabels(caseList);
		caseList.getSelectionModel().clearSelection();
	}
	
	@FXML
	public void onShowIDButtonClick(ActionEvent actionEvent) throws IOException {
		if (IDStage != null && IDStage.isShowing()) {
			IDStage.close();
			IDStage = null;
			return;
		}
		IDStage = new Stage();
		IDStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Server/currentID-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		IDStage.setTitle("Current ID");
		IDStage.setScene(newScene);
		
		IDStage.show();
		IDStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTID").equals("true"));
		showAnimation(showIDBtn);
		
		if (ConfigReader.configRead("layout", "rememberIDLocation").equals("true")) {
			if (IDFirstShown) {
				centerStageOnMainApp(IDStage);
				log("IDStage opened via showIDBtn, first time centered", Severity.INFO);
			} else {
				if (IDScreen != null) {
					Rectangle2D screenBounds = IDScreen.getVisualBounds();
					IDStage.setX(IDx);
					IDStage.setY(IDy);
					if (IDx < screenBounds.getMinX() || IDx > screenBounds.getMaxX() || IDy < screenBounds.getMinY() || IDy > screenBounds.getMaxY()) {
						centerStageOnMainApp(IDStage);
					}
				} else {
					centerStageOnMainApp(IDStage);
				}
				log("IDStage opened via showIDBtn, XValue: " + IDx + " YValue: " + IDy, Severity.INFO);
			}
		} else {
			centerStageOnMainApp(IDStage);
		}
		
		IDStage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				IDx = IDStage.getX();
				IDy = IDStage.getY();
				IDScreen = Screen.getScreensForRectangle(IDx, IDy, IDStage.getWidth(),
				                                         IDStage.getHeight()).stream().findFirst().orElse(null);
				log("IDStage closed via showIDBtn, set XValue: " + IDx + " YValue: " + IDy, Severity.DEBUG);
				IDFirstShown = false;
				IDStage = null;
			}
		});
	}
	
	@FXML
	public void onMapButtonClick(ActionEvent actionEvent) throws IOException {
		if (mapStage != null && mapStage.isShowing()) {
			mapStage.close();
			mapStage = null;
			return;
		}
		
		mapStage = new Stage();
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Other/map-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		mapStage.setTitle("Los Santos Map");
		mapStage.setScene(newScene);
		mapStage.initStyle(StageStyle.UTILITY);
		mapStage.setResizable(false);
		mapStage.show();
		mapStage.centerOnScreen();
		mapStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTMap").equals("true"));
		showAnimation(mapButton);
		
		centerStageOnMainApp(mapStage);
		
		mapStage.setOnHidden(event -> {
			mapStage = null;
		});
	}
	
	@FXML
	public void onNotesButtonClicked(ActionEvent actionEvent) throws IOException {
		if (notesStage != null && notesStage.isShowing()) {
			notesStage.close();
			notesStage = null;
			return;
		}
		
		notesStage = new Stage();
		notesStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Other/notes-view.fxml"));
		Parent root = loader.load();
		notesViewController = loader.getController();
		Scene newScene = new Scene(root);
		notesStage.setTitle("Notes");
		notesStage.setScene(newScene);
		notesStage.setResizable(true);
		
		notesStage.show();
		
		centerStageOnMainApp(notesStage);
		
		String startupValue = ConfigReader.configRead("layout", "notesWindowLayout");
		switch (startupValue) {
			case "TopLeft" -> snapToTopLeft(notesStage);
			case "TopRight" -> snapToTopRight(notesStage);
			case "BottomLeft" -> snapToBottomLeft(notesStage);
			case "BottomRight" -> snapToBottomRight(notesStage);
			case "FullLeft" -> snapToLeft(notesStage);
			case "FullRight" -> snapToRight(notesStage);
			default -> {
				if (ConfigReader.configRead("layout", "rememberNotesLocation").equals("true")) {
					if (NotesFirstShown) {
						centerStageOnMainApp(notesStage);
						log("notesStage opened via showNotesBtn, first time centered", Severity.INFO);
					} else {
						if (NotesScreen != null) {
							Rectangle2D screenBounds = NotesScreen.getVisualBounds();
							notesStage.setX(notesx);
							notesStage.setY(notesy);
							if (notesx < screenBounds.getMinX() || notesx > screenBounds.getMaxX() || notesy < screenBounds.getMinY() || notesy > screenBounds.getMaxY()) {
								centerStageOnMainApp(notesStage);
							}
						} else {
							centerStageOnMainApp(notesStage);
						}
						log("notesStage opened via showNotesBtn, XValue: " + notesx + " YValue: " + notesy,
						    Severity.INFO);
					}
				} else {
					centerStageOnMainApp(notesStage);
					notesStage.setMinHeight(300);
					notesStage.setMinWidth(300);
				}
			}
		}
		notesStage.getScene().getStylesheets().add(
				Objects.requireNonNull(Launcher.class.getResource("css/notification-styles.css")).toExternalForm());
		showAnimation(notesButton);
		notesStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTNotes").equals("true"));
		
		notesStage.setOnHidden(new EventHandler<>() {
			@Override
			public void handle(WindowEvent event) {
				notesx = notesStage.getX();
				notesy = notesStage.getY();
				NotesScreen = Screen.getScreensForRectangle(notesx, notesy, notesStage.getWidth(),
				                                            notesStage.getHeight()).stream().findFirst().orElse(null);
				log("NotesStage closed via showNotesBtn, set XValue: " + notesx + " YValue: " + notesy, Severity.DEBUG);
				NotesFirstShown = false;
				notesStage = null;
				actionController.notesText = notesViewController.getNotepadTextArea().getText();
			}
		});
	}
	
	@FXML
	public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
		setDisable(logPane, lookupPane, calloutPane, courtPane);
		setActive(shiftInformationPane);
		showAnimation(shiftInfoBtn);
		controllerUtils.refreshChart(areaReportChart, "area");
	}
	
	@FXML
	public void onLogsButtonClick(ActionEvent actionEvent) {
		showAnimation(logsButton);
		setDisable(shiftInformationPane, lookupPane, calloutPane, courtPane);
		setActive(logPane);
	}
	
	@FXML
	public void onCalloutReportButtonClick(ActionEvent actionEvent) {
		newCallout(reportChart, areaReportChart);
	}
	
	@FXML
	public void trafficStopReportButtonClick(ActionEvent actionEvent) {
		newTrafficStop(reportChart, areaReportChart);
	}
	
	@FXML
	public void onIncidentReportBtnClick(ActionEvent actionEvent) {
		newIncident(reportChart, areaReportChart);
	}
	
	@FXML
	public void onSearchReportBtnClick(ActionEvent actionEvent) {
		newSearch(reportChart, areaReportChart);
	}
	
	@FXML
	public void onArrestReportBtnClick(ActionEvent actionEvent) {
		newArrest(reportChart, areaReportChart);
	}
	
	@FXML
	public void onCitationReportBtnClick(ActionEvent actionEvent) {
		newCitation(reportChart, areaReportChart);
	}
	
	@FXML
	public void onPatrolButtonClick(ActionEvent actionEvent) {
		newPatrol(reportChart, areaReportChart);
	}
	
	@FXML
	public void onImpoundReportBtnClick(ActionEvent actionEvent) {
		newImpound(reportChart, areaReportChart);
	}
	
	@FXML
	public void onDeathReportButtonClick(ActionEvent actionEvent) {
		newDeathReport(reportChart, areaReportChart);
	}
	
	@FXML
	public void onServerStatusLabelClick(Event event) throws IOException {
		
		if (clientStage != null && clientStage.isShowing()) {
			clientStage.close();
			clientStage = null;
			return;
		}
		
		if (!ClientUtils.isConnected) {
			clientStage = new Stage();
			clientStage.initStyle(StageStyle.UNDECORATED);
			FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Server/client-view.fxml"));
			Parent root = loader.load();
			Scene newScene = new Scene(root);
			clientStage.setTitle("Client Interface");
			clientStage.setScene(newScene);
			clientStage.initStyle(StageStyle.UNDECORATED);
			clientStage.setResizable(false);
			clientStage.show();
			clientStage.centerOnScreen();
			clientStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTClient").equals("true"));
			
			centerStageOnMainApp(clientStage);
			
			clientStage.setOnHidden(event1 -> {
				clientStage = null;
			});
			
			clientController = loader.getController();
		}
	}
	
	@FXML
	public void updateInfoButtonClick(ActionEvent actionEvent) {
		if (getOfficerInfoAgency().getValue() == null || getOfficerInfoDivision().getValue() == null || getOfficerInfoRank().getValue() == null || getOfficerInfoName().getText().isEmpty() || getOfficerInfoNumber().getText().isEmpty()) {
			updatedNotification.setText("Fill Out Form.");
			updatedNotification.setStyle("-fx-text-fill: red;");
			updatedNotification.setVisible(true);
			Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
				updatedNotification.setVisible(false);
			}));
			timeline1.play();
		} else {
			ConfigWriter.configwrite("userInfo", "Agency", getOfficerInfoAgency().getValue().toString());
			ConfigWriter.configwrite("userInfo", "Division", getOfficerInfoDivision().getValue().toString());
			ConfigWriter.configwrite("userInfo", "Name", getOfficerInfoName().getText());
			ConfigWriter.configwrite("userInfo", "Rank", getOfficerInfoRank().getValue().toString());
			ConfigWriter.configwrite("userInfo", "Number", getOfficerInfoNumber().getText());
			ConfigWriter.configwrite("userInfo", "Callsign", getOfficerInfoCallsign().getText());
			generatedByTag.setText("Generated By:" + " " + getOfficerInfoName().getText());
			updatedNotification.setText("updated.");
			updatedNotification.setStyle("-fx-text-fill: green;");
			updatedNotification.setVisible(true);
			userLabel.setText(String.valueOf(getOfficerInfoName().getText().charAt(0)).toUpperCase());
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
				updatedNotification.setVisible(false);
			}));
			timeline.play();
		}
		showAnimation(updateInfoBtn);
	}
	
	@FXML
	public void onVehSearchBtnClick(ActionEvent actionEvent) throws IOException {
		boolean playAudio = false;
		String searchedPlate = vehSearchField.getEditor().getText().trim();
		if (!searchedPlate.isEmpty()) {
			updateRecentSearches(recentVehicleSearches, vehSearchField, searchedPlate);
		}
		vehSearchField.getEditor().setText(searchedPlate);
		vehSearchField.getEditor().positionCaret(vehSearchField.getEditor().getText().length());
		
		log("Searched: " + searchedPlate, Severity.INFO);
		Map<String, String> vehData = grabVehicleData(
				getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldCars.data", searchedPlate);
		Optional<Vehicle> vehOptional = findVehicleByNumber(searchedPlate);
		
		String model = vehData.getOrDefault("model", "Not available");
		String isStolen = vehData.getOrDefault("isStolen", "Not available");
		String isPolice = vehData.getOrDefault("isPolice", "Not available");
		String registration = vehData.getOrDefault("registration", "Not available");
		String insurance = vehData.getOrDefault("insurance", "Not available");
		String colorValue = vehData.getOrDefault("color", "Not available");
		String owner = vehData.getOrDefault("owner", "Not available");
		String[] rgb = colorValue.split("-");
		String color = "Not available";
		
		String licensePlate = vehData.getOrDefault("licensePlate", "Not available");
		
		if (vehOptional.isPresent()) {
			vehtypecombobox.getItems().clear();
			vehtypecombobox.getItems().addAll(vehicleTypes);
			
			log("Found: " + searchedPlate + " From VehHistory file", Severity.DEBUG);
			vehRecordPane.setVisible(true);
			noRecordFoundLabelVeh.setVisible(false);
			Vehicle vehicle = vehOptional.get();
			
			if (vehicle.getInspection() == null) {
				vehicle.setInspection(generateInspectionStatus(
						Integer.parseInt(ConfigReader.configRead("vehicleHistory", "hasValidInspection"))));
				try {
					Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
				} catch (JAXBException e) {
					logError("Could not save new vehInspection: ", e);
				}
				log("Set vehInspection as '" + vehicle.getInspection() + "' since it created before pedModel was added",
				    Severity.WARN);
			}
			
			if (vehicle.getType() == null) {
				vehicle.setType("N/A");
				try {
					Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
				} catch (JAXBException e) {
					logError("Could not save new vehType: ", e);
				}
				log("Set vehType as '" + vehicle.getType() + "' since it created before pedModel was added",
				    Severity.WARN);
			}
			vehtypecombobox.setValue(vehicle.getType());
			
			vehplatefield2.setText(vehicle.getPlateNumber());
			vehmodelfield.setText(vehicle.getModel());
			vehstolenfield.setText(vehicle.getStolenStatus());
			if (vehicle.getStolenStatus().equalsIgnoreCase("true")) {
				vehstolenfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
			} else {
				vehstolenfield.setStyle("-fx-text-fill: black !important;");
			}
			vehownerfield.setText(vehicle.getOwner());
			vehregfield.setText(vehicle.getRegistration());
			if (vehicle.getRegistration().equalsIgnoreCase("expired") || vehicle.getRegistration().equalsIgnoreCase(
					"suspended") || vehicle.getRegistration().equalsIgnoreCase("none")) {
				vehregfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
			} else {
				vehregfield.setStyle("-fx-text-fill: black !important;");
			}
			vehinspectionfield.setText(vehicle.getInspection());
			if (vehicle.getInspection().equalsIgnoreCase("expired") || vehicle.getInspection().equalsIgnoreCase(
					"invalid")) {
				vehinspectionfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
			} else {
				vehinspectionfield.setStyle("-fx-text-fill: black !important;");
			}
			vehinsfield.setText(vehicle.getInsurance());
			if (vehicle.getInsurance().equalsIgnoreCase("expired") || vehicle.getInsurance().equalsIgnoreCase(
					"suspended") || vehicle.getInsurance().equalsIgnoreCase("none")) {
				vehinsfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
			} else {
				vehinsfield.setStyle("-fx-text-fill: black !important;");
			}
			vehpolicefield.setText(vehicle.getPoliceStatus());
			if (vehicle.getPoliceStatus().equalsIgnoreCase("true")) {
				vehpolicefield.setStyle("-fx-text-fill: green !important;");
			} else {
				vehpolicefield.setStyle("-fx-text-fill: black !important;");
			}
			
			if (!vehicle.getColor().equals("Not available")) {
				vehnocolorlabel.setVisible(false);
				vehcolordisplay.setStyle(
						"-fx-background-color: " + vehicle.getColor() + ";" + "-fx-border-color: grey;");
			} else {
				vehnocolorlabel.setVisible(true);
				vehcolordisplay.setStyle("-fx-background-color: #f2f2f2;" + "-fx-border-color: grey;");
			}
			
			String vehModelString = vehicle.getModel();
			if (vehModelString != null && !vehModelString.equalsIgnoreCase("not available")) {
				File pedImgFolder = new File(vehImageFolderURL);
				if (pedImgFolder.exists()) {
					log("Detected vehImage folder..", Severity.DEBUG);
					
					File[] matchingFiles = pedImgFolder.listFiles(
							(dir, name) -> name.equalsIgnoreCase(vehModelString + ".jpg"));
					
					if (matchingFiles != null && matchingFiles.length > 0) {
						File matchingFile = matchingFiles[0];
						log("Matching vehImage found: " + matchingFile.getName(), Severity.INFO);
						
						try {
							String fileURI = matchingFile.toURI().toString();
							vehImageView.setImage(new Image(fileURI));
							noVehImageFoundlbl.setVisible(true);
							noVehImageFoundlbl.setText("Vehicle Model Found On File:");
						} catch (Exception e) {
							Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
							vehImageView.setImage(defImage);
							noVehImageFoundlbl.setVisible(true);
							noVehImageFoundlbl.setText("No Image Found In System");
							logError("Could not set vehImage: ", e);
						}
					} else {
						log("No matching vehImage found for the model: " + vehModelString + ", displaying no vehImage found.",
						    Severity.WARN);
						Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
						vehImageView.setImage(defImage);
						noVehImageFoundlbl.setVisible(true);
						noVehImageFoundlbl.setText("No Image Found In System");
					}
				} else {
					Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
					vehImageView.setImage(defImage);
					noVehImageFoundlbl.setVisible(true);
					noVehImageFoundlbl.setText("No Image Found In System");
				}
			} else {
				Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
				vehImageView.setImage(defImage);
				noVehImageFoundlbl.setVisible(true);
				noVehImageFoundlbl.setText("No Image Found In System");
			}
			
		} else if (!licensePlate.equals("Not available")) {
			log("Found: " + searchedPlate + " From WorldVeh file", Severity.DEBUG);
			vehRecordPane.setVisible(true);
			noRecordFoundLabelVeh.setVisible(false);
			if (rgb.length == 3) {
				color = "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")";
			}
			Vehicle vehicle = new Vehicle();
			vehicle.setPlateNumber(licensePlate);
			vehicle.setType("N/A");
			vehicle.setColor(color);
			vehicle.setModel(model);
			vehicle.setOwner(owner);
			vehicle.setInsurance(insurance);
			vehicle.setPoliceStatus(isPolice);
			vehicle.setStolenStatus(isStolen);
			vehicle.setRegistration(registration);
			vehicle.setInspection(generateInspectionStatus(
					Integer.parseInt(ConfigReader.configRead("vehicleHistory", "hasValidInspection"))));
			try {
				Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
			} catch (JAXBException e) {
				logError("Could not add not vehicle from VehHistory:", e);
			}
			vehplatefield2.setText(vehicle.getPlateNumber());
			vehmodelfield.setText(vehicle.getModel());
			vehstolenfield.setText(vehicle.getStolenStatus());
			if (vehicle.getStolenStatus().equalsIgnoreCase("true")) {
				vehstolenfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
			} else {
				vehstolenfield.setStyle("-fx-text-fill: black !important;");
			}
			vehownerfield.setText(vehicle.getOwner());
			vehregfield.setText(vehicle.getRegistration());
			if (vehicle.getRegistration().equalsIgnoreCase("expired") || vehicle.getRegistration().equalsIgnoreCase(
					"suspended") || vehicle.getRegistration().equalsIgnoreCase("none")) {
				vehregfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
				
			} else {
				vehregfield.setStyle("-fx-text-fill: black !important;");
			}
			vehinsfield.setText(vehicle.getInsurance());
			if (vehicle.getInsurance().equalsIgnoreCase("expired") || vehicle.getInsurance().equalsIgnoreCase(
					"suspended") || vehicle.getInsurance().equalsIgnoreCase("none")) {
				vehinsfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
				
			} else {
				vehinsfield.setStyle("-fx-text-fill: black !important;");
			}
			vehinspectionfield.setText(vehicle.getInspection());
			if (vehicle.getInspection().equalsIgnoreCase("expired") || vehicle.getInspection().equalsIgnoreCase(
					"invalid")) {
				vehinspectionfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
				
			} else {
				vehinspectionfield.setStyle("-fx-text-fill: black !important;");
			}
			vehpolicefield.setText(vehicle.getPoliceStatus());
			if (vehicle.getPoliceStatus().equalsIgnoreCase("true")) {
				vehpolicefield.setStyle("-fx-text-fill: green !important;");
			} else {
				vehpolicefield.setStyle("-fx-text-fill: black !important;");
			}
			if (!vehicle.getColor().equals("Not available")) {
				vehnocolorlabel.setVisible(false);
				vehcolordisplay.setStyle(
						"-fx-background-color: " + vehicle.getColor() + ";" + "-fx-border-color: grey;");
			} else {
				vehnocolorlabel.setVisible(true);
				vehcolordisplay.setStyle("-fx-background-color: #f2f2f2;" + "-fx-border-color: grey;");
			}
			vehtypecombobox.getItems().clear();
			vehtypecombobox.getItems().addAll(vehicleTypes);
			vehtypecombobox.setValue(vehicle.getType());
			
			String vehModelString = vehicle.getModel();
			if (vehModelString != null && !vehModelString.equalsIgnoreCase("not available")) {
				File pedImgFolder = new File(vehImageFolderURL);
				if (pedImgFolder.exists()) {
					log("Detected vehImage folder..", Severity.DEBUG);
					
					File[] matchingFiles = pedImgFolder.listFiles(
							(dir, name) -> name.equalsIgnoreCase(vehModelString + ".jpg"));
					
					if (matchingFiles != null && matchingFiles.length > 0) {
						File matchingFile = matchingFiles[0];
						log("Matching vehImage found: " + matchingFile.getName(), Severity.INFO);
						
						try {
							String fileURI = matchingFile.toURI().toString();
							vehImageView.setImage(new Image(fileURI));
							noVehImageFoundlbl.setVisible(true);
							noVehImageFoundlbl.setText("Vehicle Model Found On File:");
						} catch (Exception e) {
							Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
							vehImageView.setImage(defImage);
							noVehImageFoundlbl.setVisible(true);
							noVehImageFoundlbl.setText("No Image Found In System");
							logError("Could not set vehImage: ", e);
						}
					} else {
						log("No matching vehImage found for the model: " + vehModelString + ", displaying no vehImage found.",
						    Severity.WARN);
						Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
						vehImageView.setImage(defImage);
						noVehImageFoundlbl.setVisible(true);
						noVehImageFoundlbl.setText("No Image Found In System");
					}
				} else {
					Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
					vehImageView.setImage(defImage);
					noVehImageFoundlbl.setVisible(true);
					noVehImageFoundlbl.setText("No Image Found In System");
				}
			} else {
				Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
				vehImageView.setImage(defImage);
				noVehImageFoundlbl.setVisible(true);
				noVehImageFoundlbl.setText("No Image Found In System");
			}
			
		} else {
			log("No Vehicle With Plate: [" + searchedPlate + "] Found Anywhere", Severity.WARN);
			vehRecordPane.setVisible(false);
			noRecordFoundLabelVeh.setVisible(true);
		}
		if (playAudio) {
			try {
				if (ConfigReader.configRead("soundSettings", "playLookupWarning").equalsIgnoreCase("true")) {
					playSound(getJarPath() + "/sounds/alert-wanted.wav");
				}
			} catch (IOException e) {
				logError("Error getting configValue for playLookupWarning: ", e);
			}
		}
	}
	
	@FXML
	public void onPedSearchBtnClick(ActionEvent actionEvent) throws IOException {
		String searchedName = pedSearchField.getEditor().getText().trim();
		if (!searchedName.isEmpty()) {
			updateRecentSearches(recentPedSearches, pedSearchField, searchedName);
		}
		pedSearchField.getEditor().setText(searchedName);
		pedSearchField.getEditor().positionCaret(pedSearchField.getEditor().getText().length());
		
		log("Searched: " + searchedName, Severity.INFO);
		String pedFilePath = getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldPeds.data";
		String carFilePath = getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldCars.data";
		
		Map<String, String> pedData = grabPedData(pedFilePath, searchedName);
		Map<String, String> ownerSearch = grabPedData(carFilePath, searchedName);
		Optional<Ped> pedOptional = findPedByName(searchedName);
		
		String gender = pedData.getOrDefault("gender", "Not available");
		String birthday = pedData.getOrDefault("birthday", "Not available");
		String address = pedData.getOrDefault("address", "Not available");
		String isWanted = pedData.getOrDefault("iswanted", "Not available");
		String licenseStatus = formatLicenseStatus(pedData.getOrDefault("licensestatus", "Not available"));
		String licenseNumber = pedData.getOrDefault("licensenumber", "Not available");
		String name = pedData.getOrDefault("name", "Not available");
		String pedModel = pedData.getOrDefault("pedmodel", "Not available");
		
		String owner = ownerSearch.getOrDefault("owner", "Not available");
		String ownerPlateNum = ownerSearch.getOrDefault("licenseplate", "Not available");
		
		if (pedOptional.isPresent()) {
			log("Found: [" + name + "] From PedHistory file", Severity.DEBUG);
			Ped ped = pedOptional.get();
			if (ped.getModel() == null) {
				ped.setModel("Not available");
				try {
					Ped.PedHistoryUtils.addPed(ped);
				} catch (JAXBException e) {
					logError("Could not save new pedModel: ", e);
				}
				log("Set ped as 'not available' since it created before pedModel was added", Severity.WARN);
			}
			processPedData(ped.getName(), ped.getLicenseNumber(), ped.getGender(), ped.getBirthday(), ped.getAddress(),
			               ped.getWantedStatus(), ped.getLicenseStatus(), ped.getModel());
		} else if (!name.equals("Not available")) {
			log("Found: [" + name + "] From WorldPed file", Severity.DEBUG);
			processPedData(name, licenseNumber, gender, birthday, address, isWanted, licenseStatus, pedModel);
		} else if (owner != null && !owner.equalsIgnoreCase("not available") && !owner.equalsIgnoreCase(
				"Los Santos Police Department") && !owner.equalsIgnoreCase(
				"Los Santos Sheriff's Office") && !owner.equalsIgnoreCase(
				"Blaine County Sheriff's Office") && !owner.equalsIgnoreCase("San Andreas Highway Patrol")) {
			log("Found Vehicle Owner: [" + owner + "] From WorldVeh file, plate#: " + ownerPlateNum, Severity.DEBUG);
			processOwnerData(owner, ownerPlateNum);
		} else {
			log("No Ped With Name: [" + searchedName + "] Found Anywhere", Severity.WARN);
			pedRecordPane.setVisible(false);
			noRecordFoundLabelPed.setVisible(true);
		}
	}
	
	@FXML
	public void onShowCurrentCalToggled(ActionEvent actionEvent) {
		calActiveList.getSelectionModel().clearSelection();
		calHistoryList.getSelectionModel().clearSelection();
		if (!showCurrentCalToggle.isSelected()) {
			double toHeight = 0;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
			KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
			KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
			                                 keyValueMinHeight);
			
			timeline.getKeyFrames().add(keyFrame);
			
			timeline.play();
			currentCalPane.setVisible(false);
		} else {
			double toHeight = 329;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
			KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
			KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
			                                 keyValueMinHeight);
			
			timeline.getKeyFrames().add(keyFrame);
			
			timeline.play();
			currentCalPane.setVisible(true);
		}
	}
	
	@FXML
	public void onShowCalloutButtonClick(ActionEvent actionEvent) {
		double toHeight = 0;
		
		Timeline timeline = new Timeline();
		
		KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
		KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
		KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
		                                 keyValueMinHeight);
		
		timeline.getKeyFrames().add(keyFrame);
		
		timeline.play();
		currentCalPane.setVisible(false);
		
		setDisable(shiftInformationPane, logPane, lookupPane, courtPane);
		setActive(calloutPane);
		
		CalloutManager.loadActiveCallouts(calActiveList);
		CalloutManager.loadHistoryCallouts(calHistoryList);
	}
	
	@FXML
	public void pedCreateCitationReport(ActionEvent actionEvent) {
		String name = pedfnamefield.getText().trim() + " " + pedlnamefield.getText().trim();
		String age = calculateAge(peddobfield.getText().trim());
		String gender = pedgenfield.getText().trim();
		String address = pedaddressfield.getText().trim();
		String desc = peddescfield.getText().trim();
		
		Map<String, Object> trafficCitationObj = newCitation(getReportChart(), getAreaReportChart());
		
		Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get("Citation Report Map");
		
		TextField offenderName = (TextField) citationReportMap.get("offender name");
		TextField offenderAge = (TextField) citationReportMap.get("offender age");
		TextField offenderGender = (TextField) citationReportMap.get("offender gender");
		TextField offenderAddress = (TextField) citationReportMap.get("offender address");
		TextField offenderDescription = (TextField) citationReportMap.get("offender description");
		
		offenderName.setText(name);
		offenderAge.setText(age);
		offenderGender.setText(gender);
		offenderAddress.setText(address);
		if (!desc.equalsIgnoreCase("No Data In System") && !desc.isEmpty()) {
			offenderDescription.setText(desc);
		}
	}
	
	@FXML
	public void pedCreateArrestReport(ActionEvent actionEvent) {
		String name = pedfnamefield.getText().trim() + " " + pedlnamefield.getText().trim();
		String age = calculateAge(peddobfield.getText().trim());
		String gender = pedgenfield.getText().trim();
		String address = pedaddressfield.getText().trim();
		String desc = peddescfield.getText().trim();
		
		Map<String, Object> arrestReportObj = newArrest(reportChart, areaReportChart);
		
		Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get("Arrest Report Map");
		
		TextField offenderName = (TextField) arrestReportMap.get("offender name");
		TextField offenderAge = (TextField) arrestReportMap.get("offender age");
		TextField offenderGender = (TextField) arrestReportMap.get("offender gender");
		TextField offenderAddress = (TextField) arrestReportMap.get("offender address");
		TextField offenderDescription = (TextField) arrestReportMap.get("offender description");
		
		offenderName.setText(name);
		offenderAge.setText(age);
		offenderGender.setText(gender);
		offenderAddress.setText(address);
		if (!desc.equalsIgnoreCase("No Data In System") && !desc.isEmpty()) {
			offenderDescription.setText(desc);
		}
	}
	
	@FXML
	public void vehcreateimpreport(ActionEvent actionEvent) {
		String plate = vehplatefield2.getText().trim();
		String model = vehmodelfield.getText().trim();
		String owner = vehownerfield.getText().trim();
		Object type = vehtypecombobox.getValue();
		
		Map<String, Object> impoundReportObj = newImpound(reportChart, areaReportChart);
		Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get("Impound Report Map");
		
		TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
		TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
		TextField modelimp = (TextField) impoundReportMap.get("model");
		ComboBox vehType = (ComboBox) impoundReportMap.get("type");
		
		plateNumberimp.setText(plate);
		modelimp.setText(model);
		offenderNameimp.setText(owner);
		offenderNameimp.setText(owner);
		vehType.setValue(type);
	}
	
	@FXML
	public void pedAddDataToNotes(ActionEvent actionEvent) throws IOException {
		if (!noRecordFoundLabelPed.isVisible()) {
			String name = "";
			String age;
			String gender;
			String address;
			String description;
			StringBuilder fullString = new StringBuilder();
			if (pedfnamefield != null && !pedfnamefield.getText().isEmpty() && pedlnamefield != null && !pedlnamefield.getText().isEmpty()) {
				name = pedfnamefield.getText().trim() + " " + pedlnamefield.getText().trim();
				fullString.append("-name ").append(name).append(" ");
			}
			if (peddobfield != null && !peddobfield.getText().isEmpty()) {
				age = peddobfield.getText().trim();
				fullString.append("-age ").append(age).append(" ");
			}
			if (pedgenfield != null && !pedgenfield.getText().isEmpty()) {
				gender = pedgenfield.getText().trim();
				fullString.append("-gender ").append(gender).append(" ");
			}
			if (pedaddressfield != null && !pedaddressfield.getText().isEmpty()) {
				address = pedaddressfield.getText().trim();
				fullString.append("-address ").append(address).append(" ");
			}
			if (peddescfield != null && !peddescfield.getText().isEmpty()) {
				if (!peddescfield.getText().equalsIgnoreCase("no data in system")) {
					description = peddescfield.getText().trim();
					fullString.append("-description ").append(description).append(" ");
				}
			}
			
			notesTabList.add(new NoteTab(name, fullString.toString()));
			
			if (notesViewController != null) {
				createNoteTabs();
			}
		}
	}
	
	@FXML
	public void vehAddDataToNotes(ActionEvent actionEvent) throws IOException {
		if (!noRecordFoundLabelVeh.isVisible()) {
			String plate = "";
			String model;
			String owner;
			StringBuilder fullString = new StringBuilder();
			if (vehplatefield2 != null && !vehplatefield2.getText().isEmpty()) {
				plate = vehplatefield2.getText().trim();
				fullString.append("-platenum ").append(plate).append(" ");
			}
			if (vehmodelfield != null && !vehmodelfield.getText().isEmpty()) {
				model = vehmodelfield.getText().trim();
				fullString.append("-model ").append(model).append(" ");
			}
			if (vehownerfield != null && !vehownerfield.getText().isEmpty()) {
				owner = vehownerfield.getText().trim();
				fullString.append("-name ").append(owner).append(" ");
			}
			if (vehtypecombobox != null && !vehtypecombobox.getValue().toString().isEmpty() && !vehtypecombobox.getValue().toString().equalsIgnoreCase(
					"n/a")) {
				owner = vehtypecombobox.getValue().toString().trim();
				fullString.append("-type ").append(owner).append(" ");
			}
			
			notesTabList.add(new NoteTab(plate, fullString.toString()));
			
			if (notesViewController != null) {
				createNoteTabs();
			}
		}
	}
	
	@FXML
	public void onAccidentReportButtonClick(ActionEvent actionEvent) {
		newAccident(reportChart, areaReportChart);
	}
	
	@FXML
	public void onSettingsBtnClick(Event event) throws IOException {
		if (settingsStage != null && settingsStage.isShowing()) {
			settingsStage.close();
			settingsStage = null;
			return;
		}
		settingsStage = new Stage();
		settingsStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Settings/settings-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		settingsStage.setTitle("Settings");
		settingsStage.setScene(newScene);
		settingsStage.show();
		settingsStage.centerOnScreen();
		settingsStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTSettings").equals("true"));
		
		centerStageOnMainApp(settingsStage);
		
		settingsStage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				settingsStage = null;
			}
		});
	}
	
	@FXML
	public void pedUpdateInfo(ActionEvent actionEvent) {
		String searchedLicenseNum = pedlicnumfield.getText();
		Optional<Ped> optionalPed = Ped.PedHistoryUtils.findPedByNumber(searchedLicenseNum);
		
		if (optionalPed.isPresent()) {
			Ped ped = optionalPed.get();
			
			// Update flags
			String pedflagfieldText = pedflagfield.getText();
			if (!pedflagfieldText.equalsIgnoreCase("No Data In System") && !pedflagfieldText.isEmpty()) {
				pedflagfield.setStyle("-fx-text-fill: black !important;");
				ped.setFlags(pedflagfieldText.trim());
			} else {
				pedflagfield.setStyle("-fx-text-fill: #e65c00 !important;");
				pedflagfield.setText("No Data In System");
				ped.setFlags(null);
			}
			
			// Update affiliations
			String affiliationText = pedaffiliationfield.getText();
			if (!affiliationText.equalsIgnoreCase("No Data In System") && !affiliationText.isEmpty()) {
				pedaffiliationfield.setStyle("-fx-text-fill: black !important;");
				ped.setAffiliations(affiliationText.trim());
			} else {
				pedaffiliationfield.setStyle("-fx-text-fill: #e65c00 !important;");
				pedaffiliationfield.setText("No Data In System");
				ped.setAffiliations(null);
			}
			
			// Update description
			String descText = peddescfield.getText();
			if (!descText.equalsIgnoreCase("No Data In System") && !descText.isEmpty()) {
				peddescfield.setStyle("-fx-text-fill: black !important;");
				ped.setDescription(descText.trim());
			} else {
				peddescfield.setStyle("-fx-text-fill: #e65c00 !important;");
				peddescfield.setText("No Data In System");
				ped.setDescription(null);
			}
			
			// Update aliases
			String aliasText = pedaliasfield.getText();
			if (!aliasText.equalsIgnoreCase("No Data In System") && !aliasText.isEmpty()) {
				pedaliasfield.setStyle("-fx-text-fill: black !important;");
				ped.setAliases(aliasText.trim());
			} else {
				pedaliasfield.setStyle("-fx-text-fill: #e65c00 !important;");
				pedaliasfield.setText("No Data In System");
				ped.setAliases(null);
			}
			
			// Save the updated Ped object
			try {
				Ped.PedHistoryUtils.addPed(ped);
			} catch (JAXBException e) {
				logError("Could not add ped from update fields button: ", e);
			}
		}
	}
	
	@FXML
	public void vehUpdateInfo(ActionEvent actionEvent) {
		String searchedPlate = vehSearchField.getEditor().getText().trim();
		Optional<Vehicle> vehOptional = findVehicleByNumber(searchedPlate);
		
		if (vehOptional.isPresent()) {
			Vehicle vehicle = vehOptional.get();
			
			if (vehtypecombobox.getValue() != null) {
				vehicle.setType(vehtypecombobox.getValue().toString());
			}
			
			try {
				Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
			} catch (JAXBException e) {
				logError("Could not add ped from update fields button: ", e);
			}
			
		}
	}
	
	//</editor-fold>
	
}