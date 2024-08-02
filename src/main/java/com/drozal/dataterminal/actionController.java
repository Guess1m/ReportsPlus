package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
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
import com.drozal.dataterminal.util.Misc.*;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.Window.windowUtils;
import com.drozal.dataterminal.util.server.ClientUtils;
import com.drozal.dataterminal.util.server.Objects.CourtData.Case;
import com.drozal.dataterminal.util.server.Objects.CourtData.CourtCases;
import com.drozal.dataterminal.util.server.Objects.CourtData.CourtUtils;
import com.drozal.dataterminal.util.server.Objects.CourtData.CustomCaseCell;
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
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.logs.Arrest.ArrestReportUtils.newArrest;
import static com.drozal.dataterminal.logs.Callout.CalloutReportUtils.newCallout;
import static com.drozal.dataterminal.logs.Death.DeathReportUtils.newDeathReport;
import static com.drozal.dataterminal.logs.Impound.ImpoundReportUtils.newImpound;
import static com.drozal.dataterminal.logs.Incident.IncidentReportUtils.newIncident;
import static com.drozal.dataterminal.logs.Patrol.PatrolReportUtils.newPatrol;
import static com.drozal.dataterminal.logs.Search.SearchReportUtils.newSearch;
import static com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportUtils.newTrafficStop;
import static com.drozal.dataterminal.util.Misc.CalloutManager.handleSelectedNodeActive;
import static com.drozal.dataterminal.util.Misc.CalloutManager.handleSelectedNodeHistory;
import static com.drozal.dataterminal.util.Misc.InitTableColumns.*;
import static com.drozal.dataterminal.util.Misc.LogUtils.*;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.Misc.updateUtil.checkForUpdates;
import static com.drozal.dataterminal.util.Misc.updateUtil.gitVersion;
import static com.drozal.dataterminal.util.Report.treeViewUtils.addChargesToTable;
import static com.drozal.dataterminal.util.Report.treeViewUtils.addCitationsToTable;
import static com.drozal.dataterminal.util.Window.windowUtils.*;
import static com.drozal.dataterminal.util.server.recordUtils.grabPedData;
import static com.drozal.dataterminal.util.server.recordUtils.grabVehicleData;

public class actionController {
	
	public void initialize() throws IOException {
		lookupBtn.setVisible(false);
		showCalloutBtn.setVisible(false);
		showIDBtn.setVisible(false);
		
		blankCourtInfoPane.setVisible(true);
		courtInfoPane.setVisible(false);
		
		if (ConfigReader.configRead("uiSettings", "firstLogin").equals("true")) {
			ConfigWriter.configwrite("uiSettings", "firstLogin", "false");
			
			log("First Login, Showing Tutorial", LogUtils.Severity.DEBUG);
			tutorialOverlay.setVisible(true);
			tutorialOverlay.setOnMouseClicked(mouseEvent -> {
				tutorialOverlay.setVisible(false);
			});
		} else {
			tutorialOverlay.setVisible(false);
			log("Not First Login", LogUtils.Severity.DEBUG);
		}
		
		titlebar = reportUtil.createTitleBar("Reports Plus");
		
		vbox.getChildren().add(titlebar);
		
		AnchorPane.setTopAnchor(titlebar, 0.0);
		AnchorPane.setLeftAnchor(titlebar, 0.0);
		AnchorPane.setRightAnchor(titlebar, 0.0);
		titlebar.setPrefHeight(30);
		
		checkForUpdates();
		
		setDisable(logPane, pedLookupPane, vehLookupPane, calloutPane, courtPane);
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
							setAlignment(javafx.geometry.Pos.CENTER);
							
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
				FXMLLoader loader = new FXMLLoader(getClass().getResource("updates-view.fxml"));
				Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e) {
					logError("Error starting VersionStage: ", e);
				}
				Scene newScene = new Scene(root);
				versionStage.setTitle("Version Information");
				versionStage.setScene(newScene);
				
				versionStage.show();
				versionStage.centerOnScreen();
				windowUtils.centerStageOnMainApp(versionStage);
				
				versionStage.setOnHidden(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent event) {
						versionStage = null;
					}
				});
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
	}
	
	//<editor-fold desc="VARS">
	
	public static String notesText;
	public static SimpleIntegerProperty needRefresh = new SimpleIntegerProperty();
	public static SimpleIntegerProperty needCourtRefresh = new SimpleIntegerProperty();
	public static Stage IDStage = null;
	public static Stage settingsStage = null;
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
	
	//</editor-fold>
	
	//<editor-fold desc="FXML Elements">
	
	@javafx.fxml.FXML
	private MenuItem deathReportButton;
	@javafx.fxml.FXML
	private Tab deathTab;
	@javafx.fxml.FXML
	private TableView deathReportTable;
	@javafx.fxml.FXML
	public Button notesButton;
	@javafx.fxml.FXML
	private Label casesec4;
	@javafx.fxml.FXML
	private Label casesec3;
	@javafx.fxml.FXML
	private Label casesec2;
	@javafx.fxml.FXML
	private Label casesec1;
	@javafx.fxml.FXML
	private Label caseprim1;
	@javafx.fxml.FXML
	private GridPane caseVerdictPane;
	@javafx.fxml.FXML
	private Label caseprim2;
	@javafx.fxml.FXML
	private Label caseprim3;
	@javafx.fxml.FXML
	private Label caseTotalProbationLabel;
	@javafx.fxml.FXML
	private Label caseSuspensionDuration;
	@javafx.fxml.FXML
	private Label caseLicenseStatLabel;
	@javafx.fxml.FXML
	private Label caseTotalJailTimeLabel;
	@javafx.fxml.FXML
	private Label caseSuspensionDurationlbl;
	@javafx.fxml.FXML
	public Button shiftInfoBtn;
	@javafx.fxml.FXML
	public AnchorPane shiftInformationPane;
	@javafx.fxml.FXML
	public TextField OfficerInfoName;
	@javafx.fxml.FXML
	public ComboBox OfficerInfoDivision;
	@javafx.fxml.FXML
	public ComboBox OfficerInfoAgency;
	@javafx.fxml.FXML
	public TextField OfficerInfoCallsign;
	@javafx.fxml.FXML
	public TextField OfficerInfoNumber;
	@javafx.fxml.FXML
	public ComboBox OfficerInfoRank;
	@javafx.fxml.FXML
	public Label generatedDateTag;
	@javafx.fxml.FXML
	public Label generatedByTag;
	@javafx.fxml.FXML
	public Label updatedNotification;
	@javafx.fxml.FXML
	public AnchorPane vbox;
	@javafx.fxml.FXML
	public BarChart reportChart;
	@javafx.fxml.FXML
	public AnchorPane topPane;
	@javafx.fxml.FXML
	public AnchorPane sidepane;
	@javafx.fxml.FXML
	public Label mainColor8;
	@javafx.fxml.FXML
	public Label mainColor9Bkg;
	@javafx.fxml.FXML
	public Button updateInfoBtn;
	public NotesViewController notesViewController;
	actionController controller;
	AnchorPane titlebar;
	@javafx.fxml.FXML
	private Label secondaryColor3Bkg;
	@javafx.fxml.FXML
	private Label secondaryColor4Bkg;
	@javafx.fxml.FXML
	private Label secondaryColor5Bkg;
	@javafx.fxml.FXML
	private Button logsButton;
	@javafx.fxml.FXML
	private Button mapButton;
	@javafx.fxml.FXML
	private MenuButton createReportBtn;
	@javafx.fxml.FXML
	private MenuItem searchReportButton;
	@javafx.fxml.FXML
	private MenuItem trafficReportButton;
	@javafx.fxml.FXML
	private MenuItem impoundReportButton;
	@javafx.fxml.FXML
	private MenuItem incidentReportButton;
	@javafx.fxml.FXML
	private MenuItem patrolReportButton;
	@javafx.fxml.FXML
	private MenuItem calloutReportButton;
	@javafx.fxml.FXML
	private MenuItem arrestReportButton;
	@javafx.fxml.FXML
	private MenuItem trafficCitationReportButton;
	@javafx.fxml.FXML
	private AreaChart areaReportChart;
	@javafx.fxml.FXML
	private Tab searchTab;
	@javafx.fxml.FXML
	private TableView searchTable;
	@javafx.fxml.FXML
	private Tab arrestTab;
	@javafx.fxml.FXML
	private AnchorPane logPane;
	@javafx.fxml.FXML
	private TableView trafficStopTable;
	@javafx.fxml.FXML
	private TableView arrestTable;
	@javafx.fxml.FXML
	private TableView impoundTable;
	@javafx.fxml.FXML
	private TableView citationTable;
	@javafx.fxml.FXML
	private Tab citationTab;
	@javafx.fxml.FXML
	private TableView calloutTable;
	@javafx.fxml.FXML
	private Tab calloutTab;
	@javafx.fxml.FXML
	private Tab patrolTab;
	@javafx.fxml.FXML
	private Tab incidentTab;
	@javafx.fxml.FXML
	private Tab trafficStopTab;
	@javafx.fxml.FXML
	private Tab impoundTab;
	@javafx.fxml.FXML
	private Label reportPlusLabelFill;
	@javafx.fxml.FXML
	private TableView patrolTable;
	@javafx.fxml.FXML
	private TabPane tabPane;
	@javafx.fxml.FXML
	private TableView incidentTable;
	@javafx.fxml.FXML
	private Label serverStatusLabel;
	@javafx.fxml.FXML
	private Button showIDBtn;
	@javafx.fxml.FXML
	private Button showCalloutBtn;
	@javafx.fxml.FXML
	private MenuItem vehLookupBtn;
	@javafx.fxml.FXML
	private TextField vehSearchField;
	@javafx.fxml.FXML
	private Button pedSearchBtn;
	@javafx.fxml.FXML
	private TextField pedSearchField;
	@javafx.fxml.FXML
	private AnchorPane pedLookupPane;
	@javafx.fxml.FXML
	private MenuItem pedLookupBtn;
	@javafx.fxml.FXML
	private Button vehSearchBtn;
	@javafx.fxml.FXML
	private AnchorPane vehLookupPane;
	@javafx.fxml.FXML
	private TextField pedgenfield;
	@javafx.fxml.FXML
	private TextField peddobfield;
	@javafx.fxml.FXML
	private TextField pedlicensefield;
	@javafx.fxml.FXML
	private TextField pedwantedfield;
	@javafx.fxml.FXML
	private TextField pedlnamefield;
	@javafx.fxml.FXML
	private TextField pedfnamefield;
	@javafx.fxml.FXML
	private TextField vehinsfield;
	@javafx.fxml.FXML
	private TextField vehownerfield;
	@javafx.fxml.FXML
	private TextField vehregfield;
	@javafx.fxml.FXML
	private TextField vehstolenfield;
	@javafx.fxml.FXML
	private TextField vehmodelfield;
	@javafx.fxml.FXML
	private Label vehplatefield;
	@javafx.fxml.FXML
	private AnchorPane vehcolordisplay;
	@javafx.fxml.FXML
	private TextField vehplatefield2;
	@javafx.fxml.FXML
	private AnchorPane vehRecordPane;
	@javafx.fxml.FXML
	private Label vehnocolorlabel;
	@javafx.fxml.FXML
	private Label versionLabel;
	@javafx.fxml.FXML
	private Label pedrecordnamefield;
	@javafx.fxml.FXML
	private Label noRecordFoundLabelVeh;
	@javafx.fxml.FXML
	private AnchorPane pedRecordPane;
	@javafx.fxml.FXML
	private Label noRecordFoundLabelPed;
	@javafx.fxml.FXML
	private MenuButton lookupBtn;
	@javafx.fxml.FXML
	private Button settingsBtn;
	@javafx.fxml.FXML
	private TextField pedaddressfield;
	@javafx.fxml.FXML
	private AnchorPane tutorialOverlay;
	@javafx.fxml.FXML
	private AnchorPane calloutPane;
	@javafx.fxml.FXML
	private ListView calHistoryList;
	@javafx.fxml.FXML
	private ListView calActiveList;
	@javafx.fxml.FXML
	private AnchorPane currentCalPane;
	@javafx.fxml.FXML
	private ToggleButton showCurrentCalToggle;
	@javafx.fxml.FXML
	private TextField calPriority;
	@javafx.fxml.FXML
	private TextField calCounty;
	@javafx.fxml.FXML
	private TextField calDate;
	@javafx.fxml.FXML
	private TextField calNum;
	@javafx.fxml.FXML
	private TextField calTime;
	@javafx.fxml.FXML
	private TextField calStreet;
	@javafx.fxml.FXML
	private Label calloutInfoTitle;
	@javafx.fxml.FXML
	private TextField calArea;
	@javafx.fxml.FXML
	private TextArea calDesc;
	@javafx.fxml.FXML
	private TextField calType;
	@javafx.fxml.FXML
	private Label calfill;
	@javafx.fxml.FXML
	private Label activecalfill;
	@javafx.fxml.FXML
	private VBox bkgclr1;
	@javafx.fxml.FXML
	private VBox bkgclr2;
	@javafx.fxml.FXML
	private Label logbrwsrlbl;
	@javafx.fxml.FXML
	private Label plt4;
	@javafx.fxml.FXML
	private Label plt5;
	@javafx.fxml.FXML
	private Label plt6;
	@javafx.fxml.FXML
	private Label plt7;
	@javafx.fxml.FXML
	private Label plt1;
	@javafx.fxml.FXML
	private Label plt2;
	@javafx.fxml.FXML
	private Label plt3;
	@javafx.fxml.FXML
	private Label ped3;
	@javafx.fxml.FXML
	private Label ped4;
	@javafx.fxml.FXML
	private Label ped5;
	@javafx.fxml.FXML
	private Label ped6;
	@javafx.fxml.FXML
	private Label ped1;
	@javafx.fxml.FXML
	private Label ped2;
	@javafx.fxml.FXML
	private Label ped7;
	@javafx.fxml.FXML
	private Button showCourtCasesBtn;
	@javafx.fxml.FXML
	private Label caseTotalLabel;
	@javafx.fxml.FXML
	private TextField caseNumField;
	@javafx.fxml.FXML
	private ListView caseOffencesListView;
	@javafx.fxml.FXML
	private ListView caseList;
	@javafx.fxml.FXML
	private TextField caseCourtDateField;
	@javafx.fxml.FXML
	private AnchorPane courtPane;
	@javafx.fxml.FXML
	private TextField caseAgeField;
	@javafx.fxml.FXML
	private ListView caseOutcomesListView;
	@javafx.fxml.FXML
	private TextField caseOffenceDateField;
	@javafx.fxml.FXML
	private TextField caseStreetField;
	@javafx.fxml.FXML
	private TextField caseFirstNameField;
	@javafx.fxml.FXML
	private TextArea caseNotesField;
	@javafx.fxml.FXML
	private TextField caseAddressField;
	@javafx.fxml.FXML
	private TextField caseLastNameField;
	@javafx.fxml.FXML
	private TextField caseGenderField;
	@javafx.fxml.FXML
	private TextField caseAreaField;
	@javafx.fxml.FXML
	private TextField caseCountyField;
	@javafx.fxml.FXML
	private Label caseSec1;
	@javafx.fxml.FXML
	private Label caseSec2;
	@javafx.fxml.FXML
	private Label casePrim1;
	@javafx.fxml.FXML
	private Label caselbl5;
	@javafx.fxml.FXML
	private Label caselbl4;
	@javafx.fxml.FXML
	private Label caselbl3;
	@javafx.fxml.FXML
	private Label caselbl2;
	@javafx.fxml.FXML
	private Label caselbl1;
	@javafx.fxml.FXML
	private Label caselbl9;
	@javafx.fxml.FXML
	private Label caselbl8;
	@javafx.fxml.FXML
	private Label caselbl7;
	@javafx.fxml.FXML
	private Label caselbl6;
	@javafx.fxml.FXML
	private Label caselbl12;
	@javafx.fxml.FXML
	private Label caselbl11;
	@javafx.fxml.FXML
	private Label caselbl10;
	@javafx.fxml.FXML
	private Button deleteCaseBtn;
	@javafx.fxml.FXML
	private Label noCourtCaseSelectedlbl;
	@javafx.fxml.FXML
	private AnchorPane blankCourtInfoPane;
	@javafx.fxml.FXML
	private AnchorPane courtInfoPane;
	
	//</editor-fold>
	
	//<editor-fold desc="Events">
	
	public static void handleClose() {
		log("Stop Request Recieved", LogUtils.Severity.DEBUG);
		endLog();
		ClientUtils.disconnectFromService();
		Platform.exit();
		System.exit(0);
	}
	
	@javafx.fxml.FXML
	public void onSettingsBtnClick(ActionEvent actionEvent) throws IOException {
		if (settingsStage != null && settingsStage.isShowing()) {
			settingsStage.close();
			settingsStage = null;
			return;
		}
		settingsStage = new Stage();
		settingsStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("settings-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		settingsStage.setTitle("Settings");
		settingsStage.setScene(newScene);
		settingsStage.show();
		settingsStage.centerOnScreen();
		settingsStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTSettings").equals("true"));
		showAnimation(settingsBtn);
		
		windowUtils.centerStageOnMainApp(settingsStage);
		
		settingsStage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				settingsStage = null;
			}
		});
	}
	
	@javafx.fxml.FXML
	public void deleteCaseBtnPress(ActionEvent actionEvent) {
		String selectedCaseNum;
		if (!caseNumField.getText().isEmpty() && caseNumField != null) {
			selectedCaseNum = caseNumField.getText();
			try {
				CourtUtils.deleteCase(selectedCaseNum);
			} catch (JAXBException e) {
				logError("Could not delete case, JAXBException:", e);
			} catch (IOException e) {
				logError("Could not delete case, IOException:", e);
			}
			blankCourtInfoPane.setVisible(true);
			courtInfoPane.setVisible(false);
			showNotificationWarning("Court Case Manager", "Deleted Case Number: " + selectedCaseNum, mainRT);
			loadCaseLabels(caseList);
		}
	}
	
	@javafx.fxml.FXML
	public void onShowCourtCasesButtonClick(ActionEvent actionEvent) {
		setDisable(logPane, pedLookupPane, vehLookupPane, calloutPane, courtPane, shiftInformationPane);
		setActive(courtPane);
		showAnimation(showCourtCasesBtn);
		
		loadCaseLabels(caseList);
		caseList.getSelectionModel().clearSelection();
	}
	
	@javafx.fxml.FXML
	public void onShowIDButtonClick(ActionEvent actionEvent) throws IOException {
		if (IDStage != null && IDStage.isShowing()) {
			IDStage.close();
			IDStage = null;
			return;
		}
		IDStage = new Stage();
		IDStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("currentID-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		IDStage.setTitle("Current ID");
		IDStage.setScene(newScene);
		
		IDStage.show();
		IDStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTID").equals("true"));
		showAnimation(showIDBtn);
		
		if (ConfigReader.configRead("layout", "rememberIDLocation").equals("true")) {
			if (IDFirstShown) {
				windowUtils.centerStageOnMainApp(IDStage);
				log("IDStage opened via showIDBtn, first time centered", Severity.INFO);
			} else {
				if (IDScreen != null) {
					Rectangle2D screenBounds = IDScreen.getVisualBounds();
					IDStage.setX(IDx);
					IDStage.setY(IDy);
					if (IDx < screenBounds.getMinX() || IDx > screenBounds.getMaxX() || IDy < screenBounds.getMinY() || IDy > screenBounds.getMaxY()) {
						windowUtils.centerStageOnMainApp(IDStage);
					}
				} else {
					windowUtils.centerStageOnMainApp(IDStage);
				}
				log("IDStage opened via showIDBtn, XValue: " + IDx + " YValue: " + IDy, Severity.INFO);
			}
		} else {
			windowUtils.centerStageOnMainApp(IDStage);
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
	
	@javafx.fxml.FXML
	public void onMapButtonClick(ActionEvent actionEvent) throws IOException {
		if (mapStage != null && mapStage.isShowing()) {
			mapStage.close();
			mapStage = null;
			return;
		}
		
		mapStage = new Stage();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("map-view.fxml"));
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
		
		windowUtils.centerStageOnMainApp(mapStage);
		
		mapStage.setOnHidden(event -> {
			mapStage = null;
		});
	}
	
	@javafx.fxml.FXML
	public void onNotesButtonClicked(ActionEvent actionEvent) throws IOException {
		if (notesStage != null && notesStage.isShowing()) {
			notesStage.close();
			notesStage = null;
			return;
		}
		
		notesStage = new Stage();
		notesStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("notes-view.fxml"));
		Parent root = loader.load();
		notesViewController = loader.getController();
		Scene newScene = new Scene(root);
		notesStage.setTitle("Notes");
		notesStage.setScene(newScene);
		notesStage.setResizable(true);
		
		notesStage.show();
		
		windowUtils.centerStageOnMainApp(notesStage);
		
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
						windowUtils.centerStageOnMainApp(notesStage);
						log("notesStage opened via showNotesBtn, first time centered", Severity.INFO);
					} else {
						if (NotesScreen != null) {
							Rectangle2D screenBounds = NotesScreen.getVisualBounds();
							notesStage.setX(notesx);
							notesStage.setY(notesy);
							if (notesx < screenBounds.getMinX() || notesx > screenBounds.getMaxX() || notesy < screenBounds.getMinY() || notesy > screenBounds.getMaxY()) {
								windowUtils.centerStageOnMainApp(notesStage);
							}
						} else {
							windowUtils.centerStageOnMainApp(notesStage);
						}
						log("notesStage opened via showNotesBtn, XValue: " + notesx + " YValue: " + notesy, Severity.INFO);
					}
				} else {
					windowUtils.centerStageOnMainApp(notesStage);
					notesStage.setMinHeight(300);
					notesStage.setMinWidth(300);
				}
			}
		}
		notesStage.getScene().getStylesheets().add(
				Objects.requireNonNull(getClass().getResource("css/notification-styles.css")).toExternalForm());
		showAnimation(notesButton);
		notesStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTNotes").equals("true"));
		
		notesStage.setOnHidden(new EventHandler<>() {
			@Override
			public void handle(WindowEvent event) {
				notesx = notesStage.getX();
				notesy = notesStage.getY();
				NotesScreen = Screen.getScreensForRectangle(notesx, notesy, notesStage.getWidth(),
				                                         notesStage.getHeight()).stream().findFirst().orElse(
						null);
				log("NotesStage closed via showNotesBtn, set XValue: " + notesx + " YValue: " + notesy,
				    LogUtils.Severity.DEBUG);
				NotesFirstShown = false;
				notesStage = null;
				actionController.notesText = notesViewController.getNotepadTextArea().getText();
			}
		});
	}
	
	@javafx.fxml.FXML
	public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
		setDisable(logPane, pedLookupPane, vehLookupPane, calloutPane, courtPane);
		setActive(shiftInformationPane);
		showAnimation(shiftInfoBtn);
		controllerUtils.refreshChart(areaReportChart, "area");
	}
	
	@javafx.fxml.FXML
	public void onLogsButtonClick(ActionEvent actionEvent) {
		showAnimation(logsButton);
		setDisable(shiftInformationPane, pedLookupPane, vehLookupPane, calloutPane, courtPane);
		setActive(logPane);
	}
	
	@javafx.fxml.FXML
	public void onVehLookupBtnClick(ActionEvent actionEvent) {
		setDisable(logPane, pedLookupPane, shiftInformationPane, calloutPane, courtPane);
		vehRecordPane.setVisible(false);
		noRecordFoundLabelVeh.setVisible(false);
		setActive(vehLookupPane);
	}
	
	@javafx.fxml.FXML
	public void onPedLookupBtnClick(ActionEvent actionEvent) {
		setDisable(logPane, vehLookupPane, shiftInformationPane, calloutPane, courtPane);
		pedRecordPane.setVisible(false);
		noRecordFoundLabelPed.setVisible(false);
		setActive(pedLookupPane);
	}
	
	@javafx.fxml.FXML
	public void onCalloutReportButtonClick(ActionEvent actionEvent) {
		newCallout(reportChart, areaReportChart, notesViewController);
	}
	
	@javafx.fxml.FXML
	public void trafficStopReportButtonClick(ActionEvent actionEvent) {
		newTrafficStop(reportChart, areaReportChart, notesViewController);
	}
	
	@javafx.fxml.FXML
	public void onIncidentReportBtnClick(ActionEvent actionEvent) {
		newIncident(reportChart, areaReportChart, notesViewController);
	}
	
	@javafx.fxml.FXML
	public void onSearchReportBtnClick(ActionEvent actionEvent) {
		newSearch(reportChart, areaReportChart, notesViewController);
	}
	
	@javafx.fxml.FXML
	public void onArrestReportBtnClick(ActionEvent actionEvent) {
		newArrest(reportChart, areaReportChart, notesViewController);
	}
	
	@javafx.fxml.FXML
	public void onCitationReportBtnClick(ActionEvent actionEvent) {
		newCitation(reportChart, areaReportChart, notesViewController);
	}
	
	@javafx.fxml.FXML
	public void onPatrolButtonClick(ActionEvent actionEvent) {
		newPatrol(reportChart, areaReportChart, notesViewController);
	}
	
	@javafx.fxml.FXML
	public void onImpoundReportBtnClick(ActionEvent actionEvent) {
		newImpound(reportChart, areaReportChart, notesViewController);
	}
	
	@javafx.fxml.FXML
	public void onDeathReportButtonClick(ActionEvent actionEvent) {
		newDeathReport(reportChart, areaReportChart, notesViewController);
	}
	
	@javafx.fxml.FXML
	public void onServerStatusLabelClick(Event event) throws IOException {
		
		if (clientStage != null && clientStage.isShowing()) {
			clientStage.close();
			clientStage = null;
			return;
		}
		
		if (!ClientUtils.isConnected) {
			clientStage = new Stage();
			clientStage.initStyle(StageStyle.UNDECORATED);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("client-view.fxml"));
			Parent root = loader.load();
			Scene newScene = new Scene(root);
			clientStage.setTitle("Client Interface");
			clientStage.setScene(newScene);
			clientStage.initStyle(StageStyle.UNDECORATED);
			clientStage.setResizable(false);
			clientStage.show();
			clientStage.centerOnScreen();
			clientStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTClient").equals("true"));
			
			windowUtils.centerStageOnMainApp(clientStage);
			
			clientStage.setOnHidden(event1 -> {
				clientStage = null;
			});
			
			clientController = loader.getController();
		}
	}
	
	@javafx.fxml.FXML
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
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
				updatedNotification.setVisible(false);
			}));
			timeline.play();
		}
		showAnimation(updateInfoBtn);
	}
	
	@javafx.fxml.FXML
	public void onVehSearchBtnClick(ActionEvent actionEvent) throws IOException {
		String searchedPlate = vehSearchField.getText();
		
		Map<String, String> vehData = grabVehicleData(
				getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldCars.data", searchedPlate);
		
		String licensePlate = vehData.getOrDefault("licensePlate", "Not available");
		if (!licensePlate.equals("Not available")) {
			vehRecordPane.setVisible(true);
			noRecordFoundLabelVeh.setVisible(false);
			String model = vehData.getOrDefault("model", "Not available");
			String isStolen = vehData.getOrDefault("isStolen", "Not available");
			String owner = vehData.getOrDefault("owner", "Not available");
			String registration = vehData.getOrDefault("registration", "Not available");
			String insurance = vehData.getOrDefault("insurance", "Not available");
			String colorValue = vehData.getOrDefault("color", "Not available");
			String[] rgb = colorValue.split("-");
			String color = "Not available";
			
			if (rgb.length == 3) {
				color = "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")";
			}
			
			vehplatefield.setText(licensePlate);
			vehplatefield2.setText(licensePlate);
			vehmodelfield.setText(model);
			vehstolenfield.setText(isStolen);
			vehownerfield.setText(owner);
			vehregfield.setText(registration);
			vehinsfield.setText(insurance);
			if (!color.equals("Not available")) {
				vehnocolorlabel.setVisible(false);
				vehcolordisplay.setStyle("-fx-background-color: " + color + ";" + "-fx-border-color: grey;");
			} else {
				vehnocolorlabel.setVisible(true);
				vehcolordisplay.setStyle("-fx-background-color: #f2f2f2;" + "-fx-border-color: grey;");
			}
		} else {
			vehRecordPane.setVisible(false);
			noRecordFoundLabelVeh.setVisible(true);
		}
		
	}
	
	@javafx.fxml.FXML
	public void onPedSearchBtnClick(ActionEvent actionEvent) throws IOException {
		String searchedName = pedSearchField.getText();
		
		Map<String, String> pedData = grabPedData(
				getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldPeds.data", searchedName);
		String gender = pedData.getOrDefault("gender", "Not available");
		String birthday = pedData.getOrDefault("birthday", "Not available");
		String address = pedData.getOrDefault("address", "Not available");
		String isWanted = pedData.getOrDefault("iswanted", "Not available");
		String licenseStatus = pedData.getOrDefault("licensestatus", "Not available");
		String name = pedData.getOrDefault("name", "Not available");
		String[] parts = name.split(" ");
		String firstName = parts[0];
		String lastName = parts.length > 1 ? parts[1] : "";
		if (!name.equals("Not available")) {
			pedRecordPane.setVisible(true);
			noRecordFoundLabelPed.setVisible(false);
			
			pedrecordnamefield.setText(name);
			pedfnamefield.setText(firstName);
			pedlnamefield.setText(lastName);
			pedgenfield.setText(gender);
			peddobfield.setText(birthday);
			pedaddressfield.setText(address);
			pedwantedfield.setText(isWanted);
			pedlicensefield.setText(licenseStatus);
			
			if (pedlicensefield.getText().equals("Expired")) {
				pedlicensefield.setText("EXPIRED");
				pedlicensefield.setStyle("-fx-text-fill: red !important;");
			} else if (pedlicensefield.getText().equals("Suspended")) {
				pedlicensefield.setText("SUSPENDED");
				pedlicensefield.setStyle("-fx-text-fill: red !important;");
			} else {
				pedlicensefield.setText("Valid");
				pedlicensefield.setStyle("-fx-text-fill: black !important;");
			}
			
			if (pedwantedfield.getText().equals("True")) {
				pedwantedfield.setText("WANTED");
				pedwantedfield.setStyle("-fx-text-fill: red !important;");
			} else {
				pedwantedfield.setText("False");
				pedwantedfield.setStyle("-fx-text-fill: black !important;");
			}
		} else {
			pedRecordPane.setVisible(false);
			noRecordFoundLabelPed.setVisible(true);
		}
	}
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
	public void onShowCalloutButtonClick(ActionEvent actionEvent) throws IOException {
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
		
		setDisable(shiftInformationPane, logPane, pedLookupPane, vehLookupPane, courtPane);
		setActive(calloutPane);
		
		CalloutManager.loadActiveCallouts(calActiveList);
		CalloutManager.loadHistoryCallouts(calHistoryList);
	}
	
	//</editor-fold>
	
	//<editor-fold desc="Utils">
	
	private void updateConnectionStatus(boolean isConnected) {
		Platform.runLater(() -> {
			if (!isConnected) {
				lookupBtn.setVisible(false);
				showCalloutBtn.setVisible(false);
				showIDBtn.setVisible(false);
				LogUtils.log("No Connection", LogUtils.Severity.WARN);
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
				lookupBtn.setVisible(true);
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
		String[] categories = {"Callout", "Arrests", "Traffic Stops", "Patrols", "Searches", "Incidents", "Impounds", "Citations", "Death Reports"};
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
	
	public void loadCaseLabels(ListView<String> listView) {
		listView.getItems().clear();
		try {
			CourtCases courtCases = CourtUtils.loadCourtCases();
			ObservableList<String> caseNames = FXCollections.observableArrayList();
			if (courtCases.getCaseList() != null) {
				List<Case> sortedCases = courtCases.getCaseList().stream().sorted(
						Comparator.comparing(Case::getCaseTime).reversed()).collect(Collectors.toList());
				
				for (Case case1 : sortedCases) {
					if (!case1.getName().isEmpty() && !case1.getOffences().isEmpty()) {
						caseNames.add(case1.getOffenceDate().replaceAll("-",
						                                                "/") + " " + case1.getCaseTime() + " " + case1.getName() + " " + case1.getCaseNumber());
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
										                                                  "/") + " " + case1.getCaseTime() + " " + case1.getName() + " " + case1.getCaseNumber())) {
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
							if (newValue.equals(case1.getOffenceDate().replaceAll("-",
							                                                      "/") + " " + case1.getCaseTime() + " " + case1.getName() + " " + case1.getCaseNumber())) {
								updateFields(case1);
								break;
							}
						}
					}
				});
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
	
	public static String calculateTotalTime(String input, String key) {
		String patternString = key + ": ([^\\.]+)\\.";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(input);
		
		int totalMonths = 0;
		
		while (matcher.find()) {
			String timeString = matcher.group(1).trim();
			
			Pattern yearsPattern = Pattern.compile("(\\d+) years?");
			Pattern monthsPattern = Pattern.compile("(\\d+) months?");
			
			Matcher yearsMatcher = yearsPattern.matcher(timeString);
			Matcher monthsMatcher = monthsPattern.matcher(timeString);
			
			int months = 0;
			
			if (yearsMatcher.find()) {
				int years = Integer.parseInt(yearsMatcher.group(1));
				months += years * 12;
			}
			
			if (monthsMatcher.find()) {
				months += Integer.parseInt(monthsMatcher.group(1));
			}
			
			totalMonths += months;
		}
		
		int years = totalMonths / 12;
		int months = totalMonths % 12;
		
		return (years > 0 ? years + " years " : "") + (months > 0 ? months + " months" : "").trim();
	}
	
	public List<String> parseCharges(String input, String key) {
		List<String> results = new ArrayList<>();
		
		String patternString = key + ": ([^\\.]+)\\.";
		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(input);
		
		while (matcher.find()) {
			results.add(matcher.group(1).trim());
		}
		
		return results;
	}
	
	public String extractInteger(String input) {
		Pattern pattern = Pattern.compile("-?\\d+");
		Matcher matcher = pattern.matcher(input);
		
		if (matcher.find()) {
			return matcher.group();
		} else {
			return "";
		}
	}
	
	private void updateFields(Case case1) {
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
		
		boolean areTrafficChargesPresent;
		List<String> licenseStatusList = parseCharges(case1.getOutcomes(), "License");
		String outcomeSuspension = calculateTotalTime(case1.getOutcomes(), "License Suspension Time");
		String outcomeProbation = calculateTotalTime(case1.getOutcomes(), "Probation Time");
		List<String> jailTimeList = parseCharges(case1.getOutcomes(), "Jail Time");
		String totalJailTime = calculateTotalTime(case1.getOutcomes(), "Jail Time");
		if (jailTimeList.contains("Life sentence")) {
			totalJailTime = "Life Sentence";
		}
		
		areTrafficChargesPresent = !licenseStatusList.isEmpty() || !outcomeSuspension.isEmpty();
		String licenseStatus = "";
		if (licenseStatusList.contains("Valid")) {
			licenseStatus = "N/A";
			caseLicenseStatLabel.setStyle("-fx-text-fill: gray;");
		}
		if (licenseStatusList.contains("Suspended")) {
			licenseStatus = "Suspended";
			caseLicenseStatLabel.setStyle("-fx-text-fill: #cc5200;");
		}
		if (licenseStatusList.contains("Revoked")) {
			licenseStatus = "Revoked";
			caseLicenseStatLabel.setStyle("-fx-text-fill: red;");
		}
		
		if (!totalJailTime.isEmpty()) {
			if (totalJailTime.contains("years")) {
				if (Integer.parseInt(extractInteger(totalJailTime)) >= 10) {
					caseTotalJailTimeLabel.setStyle("-fx-text-fill: red;");
				} else {
					caseTotalJailTimeLabel.setStyle("-fx-text-fill: #cc5200;");
				}
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
			caseTotalProbationLabel.setStyle("-fx-text-fill: black;");
			caseTotalProbationLabel.setText(outcomeProbation);
		} else {
			caseTotalProbationLabel.setStyle("-fx-text-fill: gray;");
			caseTotalProbationLabel.setText("None");
		}
		
		if (areTrafficChargesPresent) {
			caseLicenseStatLabel.setText(licenseStatus);
			if (!outcomeSuspension.isEmpty()) {
				if (outcomeSuspension.contains("years")) {
					if (Integer.parseInt(extractInteger(outcomeSuspension)) >= 2) {
						caseSuspensionDuration.setStyle("-fx-text-fill: red;");
					} else {
						caseSuspensionDuration.setStyle("-fx-text-fill: #cc5200;");
					}
				} else if (outcomeSuspension.contains("months")) {
					caseSuspensionDuration.setStyle("-fx-text-fill: #cc5200;");
				} else {
					caseSuspensionDuration.setStyle("-fx-text-fill: black;");
				}
				caseSuspensionDuration.setText(outcomeSuspension);
			} else {
				caseSuspensionDuration.setStyle("-fx-text-fill: gray;");
				caseSuspensionDuration.setText("None");
			}
		} else {
			caseLicenseStatLabel.setStyle("-fx-text-fill: gray;");
			caseLicenseStatLabel.setText("N/A");
			caseSuspensionDuration.setStyle("-fx-text-fill: gray;");
			caseSuspensionDuration.setText("None");
		}
		
		ObservableList<Label> offenceLabels = createLabels(case1.getOffences());
		ObservableList<Label> outcomeLabels = createLabels(case1.getOutcomes());
		
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
		
		caseOutcomesListView.setItems(outcomeLabels);
		caseOffencesListView.setItems(offenceLabels);
		
		setCellFactory(caseOutcomesListView);
		setCellFactory(caseOffencesListView);
	}
	
	private ObservableList<Label> createLabels(String text) {
		ObservableList<Label> labels = FXCollections.observableArrayList();
		if (text != null) {
			String[] items = text.split("\\|");
			for (String item : items) {
				if (!item.trim().isEmpty()) {
					Label label = new Label(item.trim());
					label.setStyle("-fx-font-family: \"Segoe UI Semibold\";");
					labels.add(label);
				}
			}
		}
		return labels;
	}
	
	private int calculateFineTotal(String outcomes) {
		int fineTotal = 0;
		if (outcomes != null) {
			Pattern FINE_PATTERN = Pattern.compile("Fined: (\\d+)");
			Matcher matcher = FINE_PATTERN.matcher(outcomes);
			while (matcher.find()) {
				fineTotal += Integer.parseInt(matcher.group(1));
			}
		}
		return fineTotal;
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
	
	@javafx.fxml.FXML
	public void onDeathReportRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			DeathReport deathReport = (DeathReport) deathReportTable.getSelectionModel().getSelectedItem();
			
			if (deathReport != null) {
				Map<String, Object> deathReportObj = DeathReportUtils.newDeathReport(getReportChart(),
				                                                                     getAreaReportChart(),
				                                                                     notesViewController);
				
				Map<String, Object> deathReport1 = (Map<String, Object>) deathReportObj.get("Death Report Map");
				
				TextField name = (TextField) deathReport1.get("name");
				TextField rank = (TextField) deathReport1.get("rank");
				TextField div = (TextField) deathReport1.get("division");
				TextField agen = (TextField) deathReport1.get("agency");
				TextField num = (TextField) deathReport1.get("number");
				TextField date = (TextField) deathReport1.get("date");
				TextField time = (TextField) deathReport1.get("time");
				TextField street = (TextField) deathReport1.get("street");
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
				street.setText(deathReport.getStreet());
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
					} catch (JAXBException e) {
						logError("Could not delete DeathReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				deathNum.setEditable(false);
				Button pullNotesBtn = (Button) deathReportObj.get("pullNotesBtn");
				pullNotesBtn.setVisible(false);
				
				Button submitBtn = (Button) deathReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				deathReportTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onCalloutRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			CalloutReport calloutReport = (CalloutReport) calloutTable.getSelectionModel().getSelectedItem();
			
			if (calloutReport != null) {
				
				Map<String, Object> calloutReportObj = CalloutReportUtils.newCallout(getReportChart(),
				                                                                     getAreaReportChart(),
				                                                                     notesViewController);
				
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
				TextField calloutstreet = (TextField) calloutReportMap.get("street");
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
				calloutstreet.setText(calloutReport.getAddress());
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
					} catch (JAXBException e) {
						logError("Could not delete CalloutReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				Button pullNotesBtn = (Button) calloutReportObj.get("pullNotesBtn");
				pullNotesBtn.setVisible(false);
				calloutnum.setEditable(false);
				
				Button submitBtn = (Button) calloutReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				calloutTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onPatrolRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			PatrolReport patrolReport = (PatrolReport) patrolTable.getSelectionModel().getSelectedItem();
			
			if (patrolReport != null) {
				
				Map<String, Object> patrolReportObj = PatrolReportUtils.newPatrol(getReportChart(),
				                                                                  getAreaReportChart(),
				                                                                  notesViewController);
				
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
					} catch (JAXBException e) {
						logError("Could not delete PatrolReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				Button pullNotesBtn = (Button) patrolReportObj.get("pullNotesBtn");
				pullNotesBtn.setVisible(false);
				patrolnum.setEditable(false);
				
				Button submitBtn = (Button) patrolReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				patrolTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onTrafficStopRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			TrafficStopReport trafficStopReport = (TrafficStopReport) trafficStopTable.getSelectionModel().getSelectedItem();
			
			if (trafficStopReport != null) {
				
				Map<String, Object> trafficStopReportObj = newTrafficStop(getReportChart(), getAreaReportChart(),
				                                                          notesViewController);
				
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
				TextField streetts = (TextField) trafficStopReportMap.get("street");
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
				streetts.setText(trafficStopReport.getStreet());
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
					} catch (JAXBException e) {
						logError("Could not delete TrafficStopReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				Button pullNotesBtn = (Button) trafficStopReportObj.get("pullNotesBtn");
				pullNotesBtn.setVisible(false);
				stopnumts.setEditable(false);
				
				Button submitBtn = (Button) trafficStopReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				trafficStopTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onIncidentRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			IncidentReport incidentReport = (IncidentReport) incidentTable.getSelectionModel().getSelectedItem();
			
			if (incidentReport != null) {
				
				Map<String, Object> incidentReportObj = IncidentReportUtils.newIncident(getReportChart(),
				                                                                        getAreaReportChart(),
				                                                                        notesViewController);
				
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
				TextField street = (TextField) incidentReportMap.get("street");
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
				
				street.setText(incidentReport.getIncidentStreet());
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
					} catch (JAXBException e) {
						logError("Could not delete IncidentReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				Button pullNotesBtn = (Button) incidentReportObj.get("pullNotesBtn");
				pullNotesBtn.setVisible(false);
				incidentnum.setEditable(false);
				
				Button submitBtn = (Button) incidentReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				incidentTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onImpoundRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			ImpoundReport impoundReport = (ImpoundReport) impoundTable.getSelectionModel().getSelectedItem();
			
			if (impoundReport != null) {
				
				Map<String, Object> impoundReportObj = ImpoundReportUtils.newImpound(getReportChart(),
				                                                                     getAreaReportChart(),
				                                                                     notesViewController);
				
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
				
				TextField num = (TextField) impoundReportMap.get("citation number");
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
					} catch (JAXBException e) {
						logError("Could not delete ImpoundReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				Button pullNotesBtn = (Button) impoundReportObj.get("pullNotesBtn");
				pullNotesBtn.setVisible(false);
				num.setEditable(false);
				
				Button submitBtn = (Button) impoundReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				impoundTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onCitationRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			TrafficCitationReport trafficCitationReport = (TrafficCitationReport) citationTable.getSelectionModel().getSelectedItem();
			
			if (trafficCitationReport != null) {
				Map<String, Object> trafficCitationObj = TrafficCitationUtils.newCitation(getReportChart(),
				                                                                          getAreaReportChart(),
				                                                                          notesViewController);
				
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
				TextField street = (TextField) citationReportMap.get("street");
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
				street.setText(trafficCitationReport.getCitationStreet());
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
					} catch (JAXBException e) {
						logError("Could not delete TrafficCitationReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				Button pullNotesBtn = (Button) trafficCitationObj.get("pullNotesBtn");
				pullNotesBtn.setVisible(false);
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
	
	@javafx.fxml.FXML
	public void onSearchRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			SearchReport searchReport = (SearchReport) searchTable.getSelectionModel().getSelectedItem();
			
			if (searchReport != null) {
				Map<String, Object> searchReportObj = SearchReportUtils.newSearch(getReportChart(),
				                                                                  getAreaReportChart(),
				                                                                  notesViewController);
				
				Map<String, Object> searchReportMap = (Map<String, Object>) searchReportObj.get("Search Report Map");
				
				TextField name = (TextField) searchReportMap.get("name");
				TextField rank = (TextField) searchReportMap.get("rank");
				TextField div = (TextField) searchReportMap.get("division");
				TextField agen = (TextField) searchReportMap.get("agency");
				TextField num = (TextField) searchReportMap.get("number");
				
				TextField searchnum = (TextField) searchReportMap.get("search num");
				TextField date = (TextField) searchReportMap.get("date");
				TextField time = (TextField) searchReportMap.get("time");
				TextField street = (TextField) searchReportMap.get("street");
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
				
				street.setText(searchReport.getSearchStreet());
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
					} catch (JAXBException e) {
						logError("Could not delete SearchReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				Button pullNotesBtn = (Button) searchReportObj.get("pullNotesBtn");
				pullNotesBtn.setVisible(false);
				searchnum.setEditable(false);
				
				Button submitBtn = (Button) searchReportObj.get("submitBtn");
				submitBtn.setText("Update Information");
				
				searchTable.getSelectionModel().clearSelection();
			}
		}
	}
	
	@javafx.fxml.FXML
	public void onArrestRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			ArrestReport arrestReport = (ArrestReport) arrestTable.getSelectionModel().getSelectedItem();
			
			if (arrestReport != null) {
				Map<String, Object> arrestReportObj = ArrestReportUtils.newArrest(reportChart, areaReportChart,
				                                                                  notesViewController);
				
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
				TextField street = (TextField) arrestReportMap.get("street");
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
				street.setText(arrestReport.getArrestStreet());
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
					} catch (JAXBException e) {
						logError("Could not delete ArrestReport #" + numToDelete + ": ", e);
					}
					if (stage != null) {
						stage.close();
					}
					actionController.needRefresh.set(1);
					updateChartIfMismatch(reportChart);
					controllerUtils.refreshChart(getAreaReportChart(), "area");
				});
				
				Button pullNotesBtn = (Button) arrestReportObj.get("pullNotesBtn");
				pullNotesBtn.setVisible(false);
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
	
	//</editor-fold>
	
	//<editor-fold desc="Getters">
	
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
	
	public Label getMainColor9Bkg() {
		return mainColor9Bkg;
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
	
	public TextField getPedlnamefield() {
		return pedlnamefield;
	}
	
	public MenuItem getPedLookupBtn() {
		return pedLookupBtn;
	}
	
	public AnchorPane getPedLookupPane() {
		return pedLookupPane;
	}
	
	public Label getPedrecordnamefield() {
		return pedrecordnamefield;
	}
	
	public AnchorPane getPedRecordPane() {
		return pedRecordPane;
	}
	
	public Button getPedSearchBtn() {
		return pedSearchBtn;
	}
	
	public TextField getPedSearchField() {
		return pedSearchField;
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
	
	public AnchorPane getTopPane() {
		return topPane;
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
	
	public AnchorPane getTutorialOverlay() {
		return tutorialOverlay;
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
	
	public MenuItem getVehLookupBtn() {
		return vehLookupBtn;
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
	
	public Label getVehplatefield() {
		return vehplatefield;
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
	
	public TextField getVehSearchField() {
		return vehSearchField;
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
	
	public MenuButton getLookupBtn() {
		return lookupBtn;
	}
	
	public Button getSettingsBtn() {
		return settingsBtn;
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
	
}