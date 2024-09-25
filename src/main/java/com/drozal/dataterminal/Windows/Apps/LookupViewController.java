package com.drozal.dataterminal.Windows.Apps;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.History.Ped;
import com.drozal.dataterminal.util.History.Vehicle;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NoteTab;
import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.drozal.dataterminal.Windows.Other.NotesViewController.*;
import static com.drozal.dataterminal.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.drozal.dataterminal.logs.Arrest.ArrestReportUtils.newArrest;
import static com.drozal.dataterminal.logs.Impound.ImpoundReportUtils.newImpound;
import static com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.drozal.dataterminal.util.History.Ped.PedHistoryUtils.findPedByName;
import static com.drozal.dataterminal.util.History.Vehicle.VehicleHistoryUtils.findVehicleByNumber;
import static com.drozal.dataterminal.util.History.Vehicle.VehicleHistoryUtils.generateInspectionStatus;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.dropdownInfo.vehicleTypes;
import static com.drozal.dataterminal.util.Misc.stringUtil.*;
import static com.drozal.dataterminal.util.server.recordUtils.grabPedData;
import static com.drozal.dataterminal.util.server.recordUtils.grabVehicleData;

public class LookupViewController {
	
	public static LookupViewController lookupViewController;
	private final List<String> recentPedSearches = new ArrayList<>();
	private final List<String> recentVehicleSearches = new ArrayList<>();
	@javafx.fxml.FXML
	private Label plt4;
	@javafx.fxml.FXML
	private ImageView vehImageView;
	@javafx.fxml.FXML
	private Label plt5;
	@javafx.fxml.FXML
	private TextField vehpolicefield;
	@javafx.fxml.FXML
	private Label plt6;
	@javafx.fxml.FXML
	private Label plt7;
	@javafx.fxml.FXML
	private TextField pedgenfield;
	@javafx.fxml.FXML
	private Label plt1;
	@javafx.fxml.FXML
	private Label plt2;
	@javafx.fxml.FXML
	private Label plt3;
	@javafx.fxml.FXML
	private TextField pedaffiliationfield;
	@javafx.fxml.FXML
	private TextField pedlnamefield;
	@javafx.fxml.FXML
	private TextField pedfnamefield;
	@javafx.fxml.FXML
	private AnchorPane pedRecordPane;
	@javafx.fxml.FXML
	private TextField pedgunlicensetypefield;
	@javafx.fxml.FXML
	private Button vehSearchBtn;
	@javafx.fxml.FXML
	private TextField pedgunlicensestatusfield;
	@javafx.fxml.FXML
	private Label noPedImageFoundlbl;
	@javafx.fxml.FXML
	private Button orientationBtn;
	@javafx.fxml.FXML
	private TextField pedprobationstatusfield;
	@javafx.fxml.FXML
	private TextField vehregfield;
	@javafx.fxml.FXML
	private TextField pedfishinglicstatusfield;
	@javafx.fxml.FXML
	private TextField vehplatefield2;
	@javafx.fxml.FXML
	private TextField peddobfield;
	@javafx.fxml.FXML
	private Label ped13;
	@javafx.fxml.FXML
	private Label ped12;
	@javafx.fxml.FXML
	private Label ped15;
	@javafx.fxml.FXML
	private Label ped14;
	@javafx.fxml.FXML
	private Label ped17;
	@javafx.fxml.FXML
	private ScrollPane pedPane;
	@javafx.fxml.FXML
	private Label ped16;
	@javafx.fxml.FXML
	private ComboBox vehSearchField;
	@javafx.fxml.FXML
	private Label ped19;
	@javafx.fxml.FXML
	private Label ped18;
	@javafx.fxml.FXML
	private Label ped3;
	@javafx.fxml.FXML
	private Label ped4;
	@javafx.fxml.FXML
	private Label ped5;
	@javafx.fxml.FXML
	private TextField pedlicnumfield;
	@javafx.fxml.FXML
	private Label ped6;
	@javafx.fxml.FXML
	private SplitPane lookupSplitPane;
	@javafx.fxml.FXML
	private Label ped1;
	@javafx.fxml.FXML
	private Label ped11;
	@javafx.fxml.FXML
	private Label noVehImageFoundlbl;
	@javafx.fxml.FXML
	private Label ped2;
	@javafx.fxml.FXML
	private TextField pedboatinglicstatusfield;
	@javafx.fxml.FXML
	private Label ped10;
	@javafx.fxml.FXML
	private TextField peddescfield;
	@javafx.fxml.FXML
	private Label ped7;
	@javafx.fxml.FXML
	private Label ped8;
	@javafx.fxml.FXML
	private Label ped9;
	@javafx.fxml.FXML
	private ListView pedcitationpriorslistview;
	@javafx.fxml.FXML
	private Label noRecordFoundLabelPed;
	@javafx.fxml.FXML
	private TextField pedlicensefield;
	@javafx.fxml.FXML
	private TextField pedwantedfield;
	@javafx.fxml.FXML
	private TextField pedparolestatusfield;
	@javafx.fxml.FXML
	private ComboBox vehtypecombobox;
	@javafx.fxml.FXML
	private Label ped23;
	@javafx.fxml.FXML
	private AnchorPane vehLookupPane;
	@javafx.fxml.FXML
	private Label plt10;
	@javafx.fxml.FXML
	private CheckBox lookupVehCheckbox;
	@javafx.fxml.FXML
	private TextField pedhuntinglicstatusfield;
	@javafx.fxml.FXML
	private TextField pedgunlicenseclassfield;
	@javafx.fxml.FXML
	private AnchorPane vehRecordPane;
	@javafx.fxml.FXML
	private Label ped20;
	@javafx.fxml.FXML
	private AnchorPane vehcolordisplay;
	@javafx.fxml.FXML
	private Label ped22;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private Label ped21;
	@javafx.fxml.FXML
	private Button pedSearchBtn;
	@javafx.fxml.FXML
	private ComboBox pedSearchField;
	@javafx.fxml.FXML
	private Label noRecordFoundLabelVeh;
	@javafx.fxml.FXML
	private TextField pedaddressfield;
	@javafx.fxml.FXML
	private Label lookupmainlbl;
	@javafx.fxml.FXML
	private TextField vehinsfield;
	@javafx.fxml.FXML
	private Label vehnocolorlabel;
	@javafx.fxml.FXML
	private CheckBox lookupPedCheckbox;
	@javafx.fxml.FXML
	private TextField vehstolenfield;
	@javafx.fxml.FXML
	private AnchorPane lookupmainlblpane;
	@javafx.fxml.FXML
	private TextField pedflagfield;
	@javafx.fxml.FXML
	private TextField pedaliasfield;
	@javafx.fxml.FXML
	private ImageView pedImageView;
	@javafx.fxml.FXML
	private TextField vehmodelfield;
	@javafx.fxml.FXML
	private ListView pedarrestpriorslistview;
	@javafx.fxml.FXML
	private Label plt8;
	@javafx.fxml.FXML
	private Label plt9;
	@javafx.fxml.FXML
	private Button probabilitySettingsBtn;
	@javafx.fxml.FXML
	private TextField vehinspectionfield;
	@javafx.fxml.FXML
	private ScrollPane vehPane;
	@javafx.fxml.FXML
	private TextField vehownerfield;
	@javafx.fxml.FXML
	private AnchorPane pedLookupPane;
	@javafx.fxml.FXML
	private AnchorPane lookupPane;
	@javafx.fxml.FXML
	private TextField pedtimesstoppedfield;
	
	public static LookupViewController getLookupViewController() {
		return lookupViewController;
	}
	
	public void initialize() throws IOException {
		noPedImageFoundlbl.setVisible(false);
		noVehImageFoundlbl.setVisible(false);
		pedRecordPane.setVisible(false);
		noRecordFoundLabelPed.setVisible(false);
		vehRecordPane.setVisible(false);
		noRecordFoundLabelVeh.setVisible(false);
		
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
		
		lookupPedCheckbox.setSelected(
				Boolean.parseBoolean(ConfigReader.configRead("lookupWindow", "pedLookupVisible")));
		lookupVehCheckbox.setSelected(
				Boolean.parseBoolean(ConfigReader.configRead("lookupWindow", "vehLookupVisible")));
		if (ConfigReader.configRead("lookupWindow", "lookupOrientation").equalsIgnoreCase("horizontal")) {
			lookupSplitPane.setOrientation(Orientation.HORIZONTAL);
		} else {
			lookupSplitPane.setOrientation(Orientation.VERTICAL);
		}
		
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
		
	}
	
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
				log("Detected pedImage folder..", LogUtils.Severity.DEBUG);
				
				File[] matchingFiles = pedImgFolder.listFiles((dir, name) -> name.equalsIgnoreCase(pedModel + ".jpg"));
				
				if (matchingFiles != null && matchingFiles.length > 0) {
					File matchingFile = matchingFiles[0];
					log("Matching pedImage found: " + matchingFile.getName(), LogUtils.Severity.INFO);
					
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
					    LogUtils.Severity.WARN);
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
	
	@javafx.fxml.FXML
	public void onVehSearchBtnClick(ActionEvent actionEvent) throws IOException {
		boolean playAudio = false;
		String searchedPlate = vehSearchField.getEditor().getText().trim();
		if (!searchedPlate.isEmpty()) {
			updateRecentSearches(recentVehicleSearches, vehSearchField, searchedPlate);
		}
		vehSearchField.getEditor().setText(searchedPlate);
		vehSearchField.getEditor().positionCaret(vehSearchField.getEditor().getText().length());
		
		log("Searched: " + searchedPlate, LogUtils.Severity.INFO);
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
			
			log("Found: " + searchedPlate + " From VehHistory file", LogUtils.Severity.DEBUG);
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
				    LogUtils.Severity.WARN);
			}
			
			if (vehicle.getType() == null) {
				vehicle.setType("N/A");
				try {
					Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
				} catch (JAXBException e) {
					logError("Could not save new vehType: ", e);
				}
				log("Set vehType as '" + vehicle.getType() + "' since it created before pedModel was added",
				    LogUtils.Severity.WARN);
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
					log("Detected vehImage folder..", LogUtils.Severity.DEBUG);
					
					File[] matchingFiles = pedImgFolder.listFiles(
							(dir, name) -> name.equalsIgnoreCase(vehModelString + ".jpg"));
					
					if (matchingFiles != null && matchingFiles.length > 0) {
						File matchingFile = matchingFiles[0];
						log("Matching vehImage found: " + matchingFile.getName(), LogUtils.Severity.INFO);
						
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
						    LogUtils.Severity.WARN);
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
			log("Found: " + searchedPlate + " From WorldVeh file", LogUtils.Severity.DEBUG);
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
					log("Detected vehImage folder..", LogUtils.Severity.DEBUG);
					
					File[] matchingFiles = pedImgFolder.listFiles(
							(dir, name) -> name.equalsIgnoreCase(vehModelString + ".jpg"));
					
					if (matchingFiles != null && matchingFiles.length > 0) {
						File matchingFile = matchingFiles[0];
						log("Matching vehImage found: " + matchingFile.getName(), LogUtils.Severity.INFO);
						
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
						    LogUtils.Severity.WARN);
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
			log("No Vehicle With Plate: [" + searchedPlate + "] Found Anywhere", LogUtils.Severity.WARN);
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
	
	@javafx.fxml.FXML
	public void onPedSearchBtnClick(ActionEvent actionEvent) throws IOException {
		String searchedName = pedSearchField.getEditor().getText().trim();
		if (!searchedName.isEmpty()) {
			updateRecentSearches(recentPedSearches, pedSearchField, searchedName);
		}
		pedSearchField.getEditor().setText(searchedName);
		pedSearchField.getEditor().positionCaret(pedSearchField.getEditor().getText().length());
		
		log("Searched: " + searchedName, LogUtils.Severity.INFO);
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
			log("Found: [" + name + "] From PedHistory file", LogUtils.Severity.DEBUG);
			Ped ped = pedOptional.get();
			if (ped.getModel() == null) {
				ped.setModel("Not available");
				try {
					Ped.PedHistoryUtils.addPed(ped);
				} catch (JAXBException e) {
					logError("Could not save new pedModel: ", e);
				}
				log("Set ped as 'not available' since it created before pedModel was added", LogUtils.Severity.WARN);
			}
			processPedData(ped.getName(), ped.getLicenseNumber(), ped.getGender(), ped.getBirthday(), ped.getAddress(),
			               ped.getWantedStatus(), ped.getLicenseStatus(), ped.getModel());
		} else if (!name.equals("Not available")) {
			log("Found: [" + name + "] From WorldPed file", LogUtils.Severity.DEBUG);
			processPedData(name, licenseNumber, gender, birthday, address, isWanted, licenseStatus, pedModel);
		} else if (owner != null && !owner.equalsIgnoreCase("not available") && !owner.equalsIgnoreCase(
				"Los Santos Police Department") && !owner.equalsIgnoreCase(
				"Los Santos Sheriff's Office") && !owner.equalsIgnoreCase(
				"Blaine County Sheriff's Office") && !owner.equalsIgnoreCase("San Andreas Highway Patrol")) {
			log("Found Vehicle Owner: [" + owner + "] From WorldVeh file, plate#: " + ownerPlateNum,
			    LogUtils.Severity.DEBUG);
			processOwnerData(owner, ownerPlateNum);
		} else {
			log("No Ped With Name: [" + searchedName + "] Found Anywhere", LogUtils.Severity.WARN);
			pedRecordPane.setVisible(false);
			noRecordFoundLabelPed.setVisible(true);
		}
	}
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
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
	
	@javafx.fxml.FXML
	public void oritentationBtnPress(ActionEvent actionEvent) {
		if (lookupSplitPane.getOrientation().equals(Orientation.HORIZONTAL)) {
			lookupSplitPane.setOrientation(Orientation.VERTICAL);
			ConfigWriter.configwrite("lookupWindow", "lookupOrientation", "vertical");
		} else {
			lookupSplitPane.setOrientation(Orientation.HORIZONTAL);
			ConfigWriter.configwrite("lookupWindow", "lookupOrientation", "horizontal");
		}
	}
	
	@javafx.fxml.FXML
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
	
	public Label getPlt1() {
		return plt1;
	}
	
	public Label getPlt2() {
		return plt2;
	}
	
	public Label getPlt3() {
		return plt3;
	}
	
	public Label getNoPedImageFoundlbl() {
		return noPedImageFoundlbl;
	}
	
	public Button getOrientationBtn() {
		return orientationBtn;
	}
	
	public Label getPed13() {
		return ped13;
	}
	
	public Label getPed12() {
		return ped12;
	}
	
	public Label getPed15() {
		return ped15;
	}
	
	public Label getPed14() {
		return ped14;
	}
	
	public Label getPed17() {
		return ped17;
	}
	
	public Label getPed16() {
		return ped16;
	}
	
	public Label getPed19() {
		return ped19;
	}
	
	public Label getPed18() {
		return ped18;
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
	
	public Label getPed1() {
		return ped1;
	}
	
	public Label getPed11() {
		return ped11;
	}
	
	public Label getNoVehImageFoundlbl() {
		return noVehImageFoundlbl;
	}
	
	public Label getPed2() {
		return ped2;
	}
	
	public Label getPed10() {
		return ped10;
	}
	
	public Label getPed7() {
		return ped7;
	}
	
	public Label getPed8() {
		return ped8;
	}
	
	public Label getPed9() {
		return ped9;
	}
	
	public Label getPed23() {
		return ped23;
	}
	
	public AnchorPane getVehLookupPane() {
		return vehLookupPane;
	}
	
	public Label getPlt10() {
		return plt10;
	}
	
	public Label getPed20() {
		return ped20;
	}
	
	public Label getPed22() {
		return ped22;
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Label getPed21() {
		return ped21;
	}
	
	public Label getLookupmainlbl() {
		return lookupmainlbl;
	}
	
	public AnchorPane getLookupmainlblpane() {
		return lookupmainlblpane;
	}
	
	public Label getPlt8() {
		return plt8;
	}
	
	public Label getPlt9() {
		return plt9;
	}
	
	public Button getProbabilitySettingsBtn() {
		return probabilitySettingsBtn;
	}
	
	public AnchorPane getPedLookupPane() {
		return pedLookupPane;
	}
	
	public AnchorPane getLookupPane() {
		return lookupPane;
	}
	
	public ImageView getVehImageView() {
		return vehImageView;
	}
	
	public TextField getVehpolicefield() {
		return vehpolicefield;
	}
	
	public TextField getPedgenfield() {
		return pedgenfield;
	}
	
	public TextField getPedaffiliationfield() {
		return pedaffiliationfield;
	}
	
	public TextField getPedlnamefield() {
		return pedlnamefield;
	}
	
	public TextField getPedfnamefield() {
		return pedfnamefield;
	}
	
	public AnchorPane getPedRecordPane() {
		return pedRecordPane;
	}
	
	public TextField getPedgunlicensetypefield() {
		return pedgunlicensetypefield;
	}
	
	public Button getVehSearchBtn() {
		return vehSearchBtn;
	}
	
	public TextField getPedgunlicensestatusfield() {
		return pedgunlicensestatusfield;
	}
	
	public TextField getPedprobationstatusfield() {
		return pedprobationstatusfield;
	}
	
	public TextField getVehregfield() {
		return vehregfield;
	}
	
	public TextField getPedfishinglicstatusfield() {
		return pedfishinglicstatusfield;
	}
	
	public TextField getVehplatefield2() {
		return vehplatefield2;
	}
	
	public TextField getPeddobfield() {
		return peddobfield;
	}
	
	public ScrollPane getPedPane() {
		return pedPane;
	}
	
	public ComboBox getVehSearchField() {
		return vehSearchField;
	}
	
	public TextField getPedlicnumfield() {
		return pedlicnumfield;
	}
	
	public SplitPane getLookupSplitPane() {
		return lookupSplitPane;
	}
	
	public TextField getPedboatinglicstatusfield() {
		return pedboatinglicstatusfield;
	}
	
	public TextField getPeddescfield() {
		return peddescfield;
	}
	
	public ListView getPedcitationpriorslistview() {
		return pedcitationpriorslistview;
	}
	
	public Label getNoRecordFoundLabelPed() {
		return noRecordFoundLabelPed;
	}
	
	public TextField getPedlicensefield() {
		return pedlicensefield;
	}
	
	public TextField getPedwantedfield() {
		return pedwantedfield;
	}
	
	public TextField getPedparolestatusfield() {
		return pedparolestatusfield;
	}
	
	public ComboBox getVehtypecombobox() {
		return vehtypecombobox;
	}
	
	public CheckBox getLookupVehCheckbox() {
		return lookupVehCheckbox;
	}
	
	public TextField getPedhuntinglicstatusfield() {
		return pedhuntinglicstatusfield;
	}
	
	public TextField getPedgunlicenseclassfield() {
		return pedgunlicenseclassfield;
	}
	
	public AnchorPane getVehRecordPane() {
		return vehRecordPane;
	}
	
	public AnchorPane getVehcolordisplay() {
		return vehcolordisplay;
	}
	
	public Button getPedSearchBtn() {
		return pedSearchBtn;
	}
	
	public ComboBox getPedSearchField() {
		return pedSearchField;
	}
	
	public Label getNoRecordFoundLabelVeh() {
		return noRecordFoundLabelVeh;
	}
	
	public TextField getPedaddressfield() {
		return pedaddressfield;
	}
	
	public TextField getVehinsfield() {
		return vehinsfield;
	}
	
	public Label getVehnocolorlabel() {
		return vehnocolorlabel;
	}
	
	public CheckBox getLookupPedCheckbox() {
		return lookupPedCheckbox;
	}
	
	public TextField getVehstolenfield() {
		return vehstolenfield;
	}
	
	public TextField getPedflagfield() {
		return pedflagfield;
	}
	
	public TextField getPedaliasfield() {
		return pedaliasfield;
	}
	
	public ImageView getPedImageView() {
		return pedImageView;
	}
	
	public TextField getVehmodelfield() {
		return vehmodelfield;
	}
	
	public ListView getPedarrestpriorslistview() {
		return pedarrestpriorslistview;
	}
	
	public TextField getVehinspectionfield() {
		return vehinspectionfield;
	}
	
	public ScrollPane getVehPane() {
		return vehPane;
	}
	
	public TextField getVehownerfield() {
		return vehownerfield;
	}
	
	public TextField getPedtimesstoppedfield() {
		return pedtimesstoppedfield;
	}
	
	public List<String> getRecentPedSearches() {
		return recentPedSearches;
	}
	
	public List<String> getRecentVehicleSearches() {
		return recentVehicleSearches;
	}
}
