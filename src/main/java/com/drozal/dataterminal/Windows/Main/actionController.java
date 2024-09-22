package com.drozal.dataterminal.Windows.Main;

import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Apps.LogViewController;
import com.drozal.dataterminal.Windows.Other.NotesViewController;
import com.drozal.dataterminal.Windows.Server.ClientController;
import com.drozal.dataterminal.Windows.Settings.settingsController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.History.Ped;
import com.drozal.dataterminal.util.History.Vehicle;
import com.drozal.dataterminal.util.Misc.NoteTab;
import com.drozal.dataterminal.util.Misc.controllerUtils;
import com.drozal.dataterminal.util.Misc.dropdownInfo;
import com.drozal.dataterminal.util.Misc.stringUtil;
import com.drozal.dataterminal.util.server.ClientUtils;
import jakarta.xml.bind.JAXBException;
import javafx.animation.KeyFrame;
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
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import static com.drozal.dataterminal.util.History.Ped.PedHistoryUtils.findPedByName;
import static com.drozal.dataterminal.util.History.Vehicle.VehicleHistoryUtils.findVehicleByNumber;
import static com.drozal.dataterminal.util.History.Vehicle.VehicleHistoryUtils.generateInspectionStatus;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.LogUtils.*;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.dropdownInfo.vehicleTypes;
import static com.drozal.dataterminal.util.Misc.stringUtil.*;
import static com.drozal.dataterminal.util.Misc.updateUtil.checkForUpdates;
import static com.drozal.dataterminal.util.Misc.updateUtil.gitVersion;
import static com.drozal.dataterminal.util.Window.windowUtils.centerStageOnMainApp;
import static com.drozal.dataterminal.util.server.recordUtils.grabPedData;
import static com.drozal.dataterminal.util.server.recordUtils.grabVehicleData;

public class actionController {
	
	//<editor-fold desc="FXML Elements">
	
	@FXML
	private TextField vehpolicefield;
	@FXML
	private Label plt8;
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
	public Button notesButton;
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
	private VBox bkgclr1;
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
		showLookupBtn.setVisible(true); //todo undo
		showCalloutBtn.setVisible(false);
		showIDBtn.setVisible(false);
		
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
		
		checkForUpdates();
		
		setDisable(lookupPane);
		setActive(shiftInformationPane);
		
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
				// todo implemented as an app
				/*if (versionStage != null && versionStage.isShowing()) {
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
				
				versionStage.setOnHidden(event1 -> versionStage = null);*/
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
		
		lookupPedCheckbox.setSelected(
				Boolean.parseBoolean(ConfigReader.configRead("lookupWindow", "pedLookupVisible")));
		lookupVehCheckbox.setSelected(
				Boolean.parseBoolean(ConfigReader.configRead("lookupWindow", "vehLookupVisible")));
		if (ConfigReader.configRead("lookupWindow", "lookupOrientation").equalsIgnoreCase("horizontal")) {
			lookupSplitPane.setOrientation(Orientation.HORIZONTAL);
		} else {
			lookupSplitPane.setOrientation(Orientation.VERTICAL);
		}
		
		if (notesTabList == null) {
			notesTabList = new ArrayList<>();
		}
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
	
	public static int getNeedRefresh() {
		return LogViewController.needRefresh.get();
	}
	
	public static SimpleIntegerProperty needRefreshProperty() {
		return LogViewController.needRefresh;
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
	
	public MenuItem getCalloutReportButton() {
		return calloutReportButton;
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
	
	public MenuItem getIncidentReportButton() {
		return incidentReportButton;
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
	
	public Button getShiftInfoBtn() {
		return shiftInfoBtn;
	}
	
	public AnchorPane getShiftInformationPane() {
		return shiftInformationPane;
	}
	
	public AnchorPane getSidepane() {
		return sidepane;
	}
	
	public MenuItem getTrafficCitationReportButton() {
		return trafficCitationReportButton;
	}
	
	public MenuItem getTrafficReportButton() {
		return trafficReportButton;
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
	
	public VBox getBkgclr1() {
		return bkgclr1;
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
	
	public Label getSecondaryColor3Bkg() {
		return secondaryColor3Bkg;
	}
	
	public Label getSecondaryColor4Bkg() {
		return secondaryColor4Bkg;
	}
	
	//</editor-fold>
	
	//<editor-fold desc="VARS">
	
	public static String notesText;
	public static Stage IDStage = null;
	public static Stage settingsStage = null;
	public static Stage CalloutStage = null;
	public static ClientController clientController;
	public static Stage notesStage = null;
	public static Stage clientStage = null;
	private static final Stage versionStage = null;
	public static boolean IDFirstShown = true;
	public static double IDx;
	public static double IDy;
	public static Screen IDScreen = null;
	public static Screen CalloutScreen = null;
	public static boolean CalloutFirstShown = true;
	public static double Calloutx;
	public static double Callouty;
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
		setDisable(shiftInformationPane);
		
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
	public void onLookupProbabilitySettingsClick(ActionEvent actionEvent) {
		// todo converted to app
		/*if (probabilitySettingsStage != null && probabilitySettingsStage.isShowing()) {
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
		
		probabilitySettingsStage.setOnHidden(event -> probabilitySettingsStage = null);*/
	}
	
	@FXML
	public void onShowCourtCasesButtonClick(ActionEvent actionEvent) {
		//todo moved to app
		/*setDisable(lookupPane, courtPane, shiftInformationPane);
		setActive(courtPane);
		showAnimation(showCourtCasesBtn);
		
		blankCourtInfoPane.setVisible(true);
		courtInfoPane.setVisible(false);
		
		loadCaseLabels(caseList);
		caseList.getSelectionModel().clearSelection();*/
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
	public void onMapButtonClick(ActionEvent actionEvent) {
		// todo removed map
	}
	
	@FXML
	public void onNotesButtonClicked(ActionEvent actionEvent) {
		// todo relocated to an app
		/*if (notesStage != null && notesStage.isShowing()) {
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
		});*/
	}
	
	@FXML
	public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
		setDisable(lookupPane);
		setActive(shiftInformationPane);
		showAnimation(shiftInfoBtn);
		controllerUtils.refreshChart(areaReportChart, "area");
	}
	
	@FXML
	public void onLogsButtonClick(ActionEvent actionEvent) {
		//todo no longer needed
		setDisable(shiftInformationPane, lookupPane);
	}
	
	@FXML
	public void onCalloutReportButtonClick(ActionEvent actionEvent) {
		newCallout();
	}
	
	@FXML
	public void trafficStopReportButtonClick(ActionEvent actionEvent) {
		newTrafficStop();
	}
	
	@FXML
	public void onIncidentReportBtnClick(ActionEvent actionEvent) {
		newIncident();
	}
	
	@FXML
	public void onSearchReportBtnClick(ActionEvent actionEvent) {
		newSearch();
	}
	
	@FXML
	public void onArrestReportBtnClick(ActionEvent actionEvent) {
		newArrest();
	}
	
	@FXML
	public void onCitationReportBtnClick(ActionEvent actionEvent) {
		newCitation();
	}
	
	@FXML
	public void onPatrolButtonClick(ActionEvent actionEvent) {
		newPatrol();
	}
	
	@FXML
	public void onImpoundReportBtnClick(ActionEvent actionEvent) {
		newImpound();
	}
	
	@FXML
	public void onDeathReportButtonClick(ActionEvent actionEvent) {
		newDeathReport();
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
	public void onShowCalloutButtonClick(ActionEvent actionEvent) {
		// todo moved to callout controller
		/*double toHeight = 0;
		
		Timeline timeline = new Timeline();
		
		KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
		KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
		KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
		                                 keyValueMinHeight);
		
		timeline.getKeyFrames().add(keyFrame);
		
		timeline.play();
		currentCalPane.setVisible(false);
		
		setDisable(shiftInformationPane, lookupPane, courtPane);
		setActive(calloutPane);
		
		CalloutManager.loadActiveCallouts(calActiveList);
		CalloutManager.loadHistoryCallouts(calHistoryList);*/
	}
	
	@FXML
	public void pedCreateCitationReport(ActionEvent actionEvent) {
		String name = pedfnamefield.getText().trim() + " " + pedlnamefield.getText().trim();
		String age = calculateAge(peddobfield.getText().trim());
		String gender = pedgenfield.getText().trim();
		String address = pedaddressfield.getText().trim();
		String desc = peddescfield.getText().trim();
		
		Map<String, Object> trafficCitationObj = newCitation();
		
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
		
		Map<String, Object> arrestReportObj = newArrest();
		
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
		
		Map<String, Object> impoundReportObj = newImpound();
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
		newAccident();
	}
	
	@FXML
	public void onSettingsBtnClick(Event event) {
		// todo relocated to an app
		/*if (settingsStage != null && settingsStage.isShowing()) {
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
		});*/
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