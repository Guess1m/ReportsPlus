package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Windows.Settings.settingsController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.History.Vehicle;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NoteTab;
import jakarta.xml.bind.JAXBException;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Popup;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.Guess.ReportsPlus.Desktop.mainDesktopController.pedLookupAppObj;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Apps.PedLookupViewController.pedLookupViewController;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.*;
import static com.Guess.ReportsPlus.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils.newImpound;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.*;
import static com.Guess.ReportsPlus.util.History.Vehicle.VehicleHistoryUtils.findVehicleByNumber;
import static com.Guess.ReportsPlus.util.History.Vehicle.VehicleHistoryUtils.generateInspectionStatus;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.CalloutManager.createLabel;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.updateRecentSearches;
import static com.Guess.ReportsPlus.util.Misc.dropdownInfo.vehicleTypes;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.getJarPath;
import static com.Guess.ReportsPlus.util.Misc.stringUtil.vehImageFolderURL;
import static com.Guess.ReportsPlus.util.Server.recordUtils.grabVehicleData;

public class VehLookupViewController {
	
	public static VehLookupViewController vehLookupViewController;
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
	private Label plt1;
	@javafx.fxml.FXML
	private Label plt2;
	@javafx.fxml.FXML
	private Label plt3;
	@javafx.fxml.FXML
	private Button vehSearchBtn;
	@javafx.fxml.FXML
	private TextField vehregfield;
	@javafx.fxml.FXML
	private TextField vehplatefield2;
	@javafx.fxml.FXML
	private ComboBox vehSearchField;
	@javafx.fxml.FXML
	private Label noVehImageFoundlbl;
	@javafx.fxml.FXML
	private ComboBox vehtypecombobox;
	@javafx.fxml.FXML
	private AnchorPane vehLookupPane;
	@javafx.fxml.FXML
	private Label plt10;
	@javafx.fxml.FXML
	private AnchorPane vehRecordPane;
	@javafx.fxml.FXML
	private AnchorPane vehcolordisplay;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private Label noRecordFoundLabelVeh;
	@javafx.fxml.FXML
	private Label lookupmainlbl;
	@javafx.fxml.FXML
	private TextField vehinsfield;
	@javafx.fxml.FXML
	private Label vehnocolorlabel;
	@javafx.fxml.FXML
	private TextField vehstolenfield;
	@javafx.fxml.FXML
	private TextField vehmodelfield;
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
	private AnchorPane lookupPane;
	@javafx.fxml.FXML
	private Label lbl1;
	@javafx.fxml.FXML
	private Button addDataToNotesBtn;
	@javafx.fxml.FXML
	private Button btninfo1;
	@javafx.fxml.FXML
	private Button btninfo2;
	@javafx.fxml.FXML
	private Label info1;
	@javafx.fxml.FXML
	private Label info2;
	@javafx.fxml.FXML
	private Button btninfo3;
	
	public void initialize() {
		noVehImageFoundlbl.setVisible(false);
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
		
		addLocalization();
	}
	
	private void addLocalization() {
		lookupmainlbl.setText(localization.getLocalizedMessage("VehicleLookup.MainHeader", "D.M.V Vehicle Lookup"));
		vehnocolorlabel.setText(
				localization.getLocalizedMessage("VehicleLookup.NoColorFoundLabel", "No Color On Record"));
		noVehImageFoundlbl.setText(
				localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
		noRecordFoundLabelVeh.setText(
				localization.getLocalizedMessage("VehicleLookup.NoVehFoundInSystem", "No Record Found In System"));
		lbl1.setText(localization.getLocalizedMessage("VehicleLookup.SubHeading", "Search Plate:"));
		
		vehSearchBtn.setText(localization.getLocalizedMessage("VehicleLookup.SearchButton", "Search"));
		addDataToNotesBtn.setText(
				localization.getLocalizedMessage("VehicleLookup.AddDataToNotesBtn", "Add Data To Notes"));
		probabilitySettingsBtn.setText(
				localization.getLocalizedMessage("VehicleLookup.ProbabilitySettingsBtn", "Probability Settings"));
		
		btninfo3.setText(localization.getLocalizedMessage("VehicleLookup.OwnerLookupBtn", "Owner Lookup"));
		btninfo2.setText(
				localization.getLocalizedMessage("VehicleLookup.CreateImpoundReportBtn", "Create Impound Report"));
		btninfo1.setText(localization.getLocalizedMessage("VehicleLookup.UpdateVehicleTypeBtn", "Update Vehicle Type"));
		
		info1.setText(localization.getLocalizedMessage("VehicleLookup.VehicleInfoHeader", "Vehicle Information"));
		info2.setText(localization.getLocalizedMessage("VehicleLookup.OwnerInfoHeader", "Owner Information"));
		
		plt1.setText(localization.getLocalizedMessage("VehicleLookup.FieldPlateNum", "License Plate:"));
		plt2.setText(localization.getLocalizedMessage("VehicleLookup.FieldModel", "Model"));
		plt3.setText(localization.getLocalizedMessage("VehicleLookup.FieldStolen", "Vehicle Stolen:"));
		plt4.setText(localization.getLocalizedMessage("VehicleLookup.FieldColor", "Color:"));
		plt5.setText(localization.getLocalizedMessage("VehicleLookup.FieldOwner", "Registered Owner:"));
		plt6.setText(localization.getLocalizedMessage("VehicleLookup.FieldRegistrationStatus", "Registration Status:"));
		plt7.setText(localization.getLocalizedMessage("VehicleLookup.FieldInsuranceStatus", "Insurance Status:"));
		plt8.setText(localization.getLocalizedMessage("VehicleLookup.FieldPoliceVehicle", "Police Vehicle:"));
		plt9.setText(localization.getLocalizedMessage("VehicleLookup.FieldInspectionStatus", "Inspection Status:"));
		plt10.setText(localization.getLocalizedMessage("VehicleLookup.FieldType", "Vehicle Type:"));
		
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
		
		String model = vehData.getOrDefault("model", "Not Found");
		String isStolen = vehData.getOrDefault("isStolen", "Not Found");
		String isPolice = vehData.getOrDefault("isPolice", "Not Found");
		String registration = vehData.getOrDefault("registration", "Not Found");
		String insurance = vehData.getOrDefault("insurance", "Not Found");
		String colorValue = vehData.getOrDefault("color", "Not Found");
		String owner = vehData.getOrDefault("owner", "Not Found");
		String[] rgb = colorValue.split("-");
		String color = "Not Found";
		
		String licensePlate = vehData.getOrDefault("licensePlate", "Not Found");
		
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
			
			vehtypecombobox.getStyleClass().remove("combo-boxType");
			vehtypecombobox.getStyleClass().remove("combo-boxVehicle");
			if (vehicle.getType().equalsIgnoreCase("N/A")) {
				vehtypecombobox.getStyleClass().add("combo-boxType");
			} else {
				vehtypecombobox.getStyleClass().add("combo-boxVehicle");
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
			
			vehregfield.getStyleClass().clear();
			vehregfield.setOnMouseClicked(null);
			vehregfield.setText(vehicle.getRegistration());
			if (vehicle.getRegistration() == null) {
				vehregfield.setText("None");
				vehregfield.getStyleClass().add("text-field");
				vehregfield.setStyle("-fx-text-fill: black !important;");
			} else if (vehicle.getRegistration().equalsIgnoreCase(
					"expired") || vehicle.getRegistration().equalsIgnoreCase("none")) {
				vehregfield.getStyleClass().add("valid-field");
				vehregfield.setStyle("-fx-text-fill: red !important;");
				vehregfield.setText(vehicle.getRegistration().toUpperCase());
				
				boolean updated = false;
				if (vehicle.getRegistrationExpiration() == null) {
					if (vehicle.getRegistration().equalsIgnoreCase("expired")) {
						vehicle.setRegistrationExpiration(generateExpiredLicenseExpirationDate(3));
					} else if (vehicle.getRegistration().equalsIgnoreCase("None")) {
						vehicle.setRegistrationExpiration("None");
					}
					updated = true;
				}
				if (vehicle.getRegistrationNumber() == null) {
					vehicle.setRegistrationNumber(generateLicenseNumber());
					if (vehicle.getRegistration().equalsIgnoreCase("None")) {
						vehicle.setRegistrationNumber("None");
					}
					updated = true;
				}
				if (updated) {
					try {
						Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
					} catch (JAXBException e) {
						logError("Error updating Vehicle for registration info 1: ", e);
					}
				}
				createRegInsInfoPopup(vehregfield, localization.getLocalizedMessage("VehLookup.RegistrationInfoTitle",
				                                                                    "Registration Information:"),
				                      vehicle.getOwner(), vehicle.getRegistrationExpiration(),
				                      vehicle.getRegistration(), vehicle.getRegistrationNumber());
				
			} else if (vehicle.getRegistration().equalsIgnoreCase("valid")) {
				vehregfield.getStyleClass().add("valid-field");
				vehregfield.setStyle("-fx-text-fill: #006600 !important;");
				vehregfield.setText("Valid");
				
				boolean updated = false;
				if (vehicle.getRegistrationExpiration() == null) {
					vehicle.setRegistrationExpiration(generateValidLicenseExpirationDate());
					updated = true;
				}
				if (vehicle.getRegistrationNumber() == null) {
					vehicle.setRegistrationNumber(generateLicenseNumber());
					updated = true;
				}
				if (updated) {
					try {
						Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
					} catch (JAXBException e) {
						logError("Error updating Vehicle for registration info 2: ", e);
					}
				}
				createRegInsInfoPopup(vehregfield, localization.getLocalizedMessage("VehLookup.RegistrationInfoTitle",
				                                                                    "Registration Information:"),
				                      vehicle.getOwner(), vehicle.getRegistrationExpiration(),
				                      vehicle.getRegistration(), vehicle.getRegistrationNumber());
			}
			
			vehinspectionfield.setText(vehicle.getInspection());
			if (vehicle.getInspection().equalsIgnoreCase("expired") || vehicle.getInspection().equalsIgnoreCase(
					"invalid")) {
				vehinspectionfield.setStyle("-fx-text-fill: red !important;");
			} else {
				vehinspectionfield.setStyle("-fx-text-fill: black !important;");
			}
			
			vehinsfield.getStyleClass().clear();
			vehinsfield.setOnMouseClicked(null);
			vehinsfield.setText(vehicle.getInsurance());
			if (vehicle.getInsurance() == null) {
				vehinsfield.setText("None");
				vehinsfield.getStyleClass().add("text-field");
				vehinsfield.setStyle("-fx-text-fill: black !important;");
			} else if (vehicle.getInsurance().equalsIgnoreCase("expired") || vehicle.getInsurance().equalsIgnoreCase(
					"none")) {
				vehinsfield.getStyleClass().add("valid-field");
				vehinsfield.setStyle("-fx-text-fill: red !important;");
				vehinsfield.setText(vehicle.getInsurance().toUpperCase());
				
				boolean updated = false;
				if (vehicle.getInsuranceExpiration() == null) {
					if (vehicle.getInsurance().equalsIgnoreCase("expired")) {
						vehicle.setInsuranceExpiration(generateExpiredLicenseExpirationDate(3));
					} else if (vehicle.getInsurance().equalsIgnoreCase("None")) {
						vehicle.setInsuranceExpiration("None");
					}
					updated = true;
				}
				if (vehicle.getInsuranceNumber() == null) {
					vehicle.setInsuranceNumber(generateLicenseNumber());
					if (vehicle.getInsurance().equalsIgnoreCase("None")) {
						vehicle.setInsuranceNumber("None");
					}
					updated = true;
				}
				if (updated) {
					try {
						Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
					} catch (JAXBException e) {
						logError("Error updating Vehicle for Insurance info 1: ", e);
					}
				}
				createRegInsInfoPopup(vehinsfield, localization.getLocalizedMessage("VehLookup.InsuranceInfoTitle",
				                                                                    "Insurance Information:"),
				                      vehicle.getOwner(), vehicle.getInsuranceExpiration(), vehicle.getInsurance(),
				                      vehicle.getInsuranceNumber());
				
			} else if (vehicle.getInsurance().equalsIgnoreCase("valid")) {
				vehinsfield.getStyleClass().add("valid-field");
				vehinsfield.setStyle("-fx-text-fill: #006600 !important;");
				vehinsfield.setText("Valid");
				
				boolean updated = false;
				if (vehicle.getInsuranceExpiration() == null) {
					vehicle.setInsuranceExpiration(generateValidLicenseExpirationDate());
					updated = true;
				}
				if (vehicle.getInsuranceNumber() == null) {
					vehicle.setInsuranceNumber(generateLicenseNumber());
					updated = true;
				}
				if (updated) {
					try {
						Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
					} catch (JAXBException e) {
						logError("Error updating Vehicle for insurance info 2: ", e);
					}
				}
				createRegInsInfoPopup(vehinsfield, localization.getLocalizedMessage("VehLookup.InsuranceInfoTitle",
				                                                                    "Insurance Information:"),
				                      vehicle.getOwner(), vehicle.getInsuranceExpiration(), vehicle.getInsurance(),
				                      vehicle.getInsuranceNumber());
			}
			
			vehpolicefield.setText(vehicle.getPoliceStatus());
			if (vehicle.getPoliceStatus().equalsIgnoreCase("true")) {
				vehpolicefield.setStyle("-fx-text-fill: green !important;");
			} else {
				vehpolicefield.setStyle("-fx-text-fill: black !important;");
			}
			
			if (!vehicle.getColor().equals("Not Found")) {
				vehnocolorlabel.setVisible(false);
				vehcolordisplay.setStyle(
						"-fx-background-color: " + vehicle.getColor() + ";" + "-fx-border-color: grey;");
			} else {
				vehnocolorlabel.setVisible(true);
				vehcolordisplay.setStyle("-fx-background-color: #f2f2f2;" + "-fx-border-color: grey;");
			}
			
			String vehModelString = vehicle.getModel();
			if (vehModelString != null && !vehModelString.equalsIgnoreCase("Not Found")) {
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
							noVehImageFoundlbl.setText(
									localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
									                                 "No Image Found In System"));
							logError("Could not set vehImage: ", e);
						}
					} else {
						log("No matching vehImage found for the model: " + vehModelString + ", displaying no vehImage found.",
						    LogUtils.Severity.WARN);
						Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
						vehImageView.setImage(defImage);
						noVehImageFoundlbl.setVisible(true);
						noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
						                                                            "No Image Found In System"));
					}
				} else {
					Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
					vehImageView.setImage(defImage);
					noVehImageFoundlbl.setVisible(true);
					noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
					                                                            "No Image Found In System"));
				}
			} else {
				Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
				vehImageView.setImage(defImage);
				noVehImageFoundlbl.setVisible(true);
				noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
				                                                            "No Image Found In System"));
			}
			
		} else if (!licensePlate.equals("Not Found")) {
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
			
			vehregfield.getStyleClass().clear();
			vehregfield.setOnMouseClicked(null);
			vehregfield.setText(vehicle.getRegistration());
			if (vehicle.getRegistration() == null) {
				vehregfield.setText("None");
				vehregfield.getStyleClass().add("text-field");
				vehregfield.setStyle("-fx-text-fill: black !important;");
			} else if (vehicle.getRegistration().equalsIgnoreCase(
					"expired") || vehicle.getRegistration().equalsIgnoreCase("none")) {
				vehregfield.getStyleClass().add("valid-field");
				vehregfield.setStyle("-fx-text-fill: red !important;");
				vehregfield.setText(vehicle.getRegistration().toUpperCase());
				
				boolean updated = false;
				if (vehicle.getRegistrationExpiration() == null) {
					if (vehicle.getRegistration().equalsIgnoreCase("expired")) {
						vehicle.setRegistrationExpiration(generateExpiredLicenseExpirationDate(3));
					} else if (vehicle.getRegistration().equalsIgnoreCase("None")) {
						vehicle.setRegistrationExpiration("None");
					}
					updated = true;
				}
				if (vehicle.getRegistrationNumber() == null) {
					vehicle.setRegistrationNumber(generateLicenseNumber());
					if (vehicle.getRegistration().equalsIgnoreCase("None")) {
						vehicle.setRegistrationNumber("None");
					}
					updated = true;
				}
				if (updated) {
					try {
						Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
					} catch (JAXBException e) {
						logError("Error updating Vehicle for registration info 1: ", e);
					}
				}
				createRegInsInfoPopup(vehregfield, localization.getLocalizedMessage("VehLookup.RegistrationInfoTitle",
				                                                                    "Registration Information:"),
				                      vehicle.getOwner(), vehicle.getRegistrationExpiration(),
				                      vehicle.getRegistration(), vehicle.getRegistrationNumber());
				
			} else if (vehicle.getRegistration().equalsIgnoreCase("valid")) {
				vehregfield.getStyleClass().add("valid-field");
				vehregfield.setStyle("-fx-text-fill: #006600 !important;");
				vehregfield.setText("Valid");
				
				boolean updated = false;
				if (vehicle.getRegistrationExpiration() == null) {
					vehicle.setRegistrationExpiration(generateValidLicenseExpirationDate());
					updated = true;
				}
				if (vehicle.getRegistrationNumber() == null) {
					vehicle.setRegistrationNumber(generateLicenseNumber());
					updated = true;
				}
				if (updated) {
					try {
						Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
					} catch (JAXBException e) {
						logError("Error updating Vehicle for registration info 2: ", e);
					}
				}
				createRegInsInfoPopup(vehregfield, localization.getLocalizedMessage("VehLookup.RegistrationInfoTitle",
				                                                                    "Registration Information:"),
				                      vehicle.getOwner(), vehicle.getRegistrationExpiration(),
				                      vehicle.getRegistration(), vehicle.getRegistrationNumber());
			}
			
			vehinsfield.getStyleClass().clear();
			vehinsfield.setOnMouseClicked(null);
			vehinsfield.setText(vehicle.getInsurance());
			if (vehicle.getInsurance() == null) {
				vehinsfield.setText("None");
				vehinsfield.getStyleClass().add("text-field");
				vehinsfield.setStyle("-fx-text-fill: black !important;");
			} else if (vehicle.getInsurance().equalsIgnoreCase("expired") || vehicle.getInsurance().equalsIgnoreCase(
					"none")) {
				vehinsfield.getStyleClass().add("valid-field");
				vehinsfield.setStyle("-fx-text-fill: red !important;");
				vehinsfield.setText(vehicle.getInsurance().toUpperCase());
				
				boolean updated = false;
				if (vehicle.getInsuranceExpiration() == null) {
					if (vehicle.getInsurance().equalsIgnoreCase("expired")) {
						vehicle.setInsuranceExpiration(generateExpiredLicenseExpirationDate(3));
					} else if (vehicle.getInsurance().equalsIgnoreCase("None")) {
						vehicle.setInsuranceExpiration("None");
					}
					updated = true;
				}
				if (vehicle.getInsuranceNumber() == null) {
					vehicle.setInsuranceNumber(generateLicenseNumber());
					if (vehicle.getInsurance().equalsIgnoreCase("None")) {
						vehicle.setInsuranceNumber("None");
					}
					updated = true;
				}
				if (updated) {
					try {
						Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
					} catch (JAXBException e) {
						logError("Error updating Vehicle for Insurance info 1: ", e);
					}
				}
				createRegInsInfoPopup(vehinsfield, localization.getLocalizedMessage("VehLookup.InsuranceInfoTitle",
				                                                                    "Insurance Information:"),
				                      vehicle.getOwner(), vehicle.getInsuranceExpiration(), vehicle.getInsurance(),
				                      vehicle.getInsuranceNumber());
				
			} else if (vehicle.getInsurance().equalsIgnoreCase("valid")) {
				vehinsfield.getStyleClass().add("valid-field");
				vehinsfield.setStyle("-fx-text-fill: #006600 !important;");
				vehinsfield.setText("Valid");
				
				boolean updated = false;
				if (vehicle.getInsuranceExpiration() == null) {
					vehicle.setInsuranceExpiration(generateValidLicenseExpirationDate());
					updated = true;
				}
				if (vehicle.getInsuranceNumber() == null) {
					vehicle.setInsuranceNumber(generateLicenseNumber());
					updated = true;
				}
				if (updated) {
					try {
						Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
					} catch (JAXBException e) {
						logError("Error updating Vehicle for insurance info 2: ", e);
					}
				}
				createRegInsInfoPopup(vehinsfield, localization.getLocalizedMessage("VehLookup.InsuranceInfoTitle",
				                                                                    "Insurance Information:"),
				                      vehicle.getOwner(), vehicle.getInsuranceExpiration(), vehicle.getInsurance(),
				                      vehicle.getInsuranceNumber());
			}
			
			vehinspectionfield.setText(vehicle.getInspection());
			if (vehicle.getInspection().equalsIgnoreCase("expired") || vehicle.getInspection().equalsIgnoreCase(
					"invalid")) {
				vehinspectionfield.setStyle("-fx-text-fill: red !important;");
				
			} else {
				vehinspectionfield.setStyle("-fx-text-fill: black !important;");
			}
			vehpolicefield.setText(vehicle.getPoliceStatus());
			if (vehicle.getPoliceStatus().equalsIgnoreCase("true")) {
				vehpolicefield.setStyle("-fx-text-fill: green !important;");
			} else {
				vehpolicefield.setStyle("-fx-text-fill: black !important;");
			}
			if (!vehicle.getColor().equals("Not Found")) {
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
			if (vehModelString != null && !vehModelString.equalsIgnoreCase("Not Found")) {
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
							noVehImageFoundlbl.setText(
									localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
									                                 "No Image Found In System"));
							logError("Could not set vehImage: ", e);
						}
					} else {
						log("No matching vehImage found for the model: " + vehModelString + ", displaying no vehImage found.",
						    LogUtils.Severity.WARN);
						Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
						vehImageView.setImage(defImage);
						noVehImageFoundlbl.setVisible(true);
						noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
						                                                            "No Image Found In System"));
					}
				} else {
					Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
					vehImageView.setImage(defImage);
					noVehImageFoundlbl.setVisible(true);
					noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
					                                                            "No Image Found In System"));
				}
			} else {
				Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
				vehImageView.setImage(defImage);
				noVehImageFoundlbl.setVisible(true);
				noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
				                                                            "No Image Found In System"));
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
	
	public void createRegInsInfoPopup(TextField label, String headerText, String name, String exp, String status, String licnum) {
		try {
			AnchorPane popupContent = new AnchorPane();
			popupContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
			popupContent.getStylesheets().add(Launcher.class.getResource(
					"/com/Guess/ReportsPlus/css/courtCase/courtCaseCss.css").toExternalForm());
			
			Label titleLabel = new Label(headerText);
			titleLabel.setPadding(new Insets(0, 33, 0, 33));
			titleLabel.setAlignment(Pos.CENTER);
			titleLabel.setPrefHeight(33.0);
			titleLabel.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";");
			titleLabel.setTextFill(Paint.valueOf("WHITE"));
			titleLabel.setFont(new Font("Segoe UI Black", 17.0));
			AnchorPane.setTopAnchor(titleLabel, 0.0);
			AnchorPane.setLeftAnchor(titleLabel, 0.0);
			AnchorPane.setRightAnchor(titleLabel, 0.0);
			
			ImageView exitBtn = new ImageView(
					new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/cross.png")));
			exitBtn.setFitHeight(33.0);
			exitBtn.setFitWidth(15.0);
			exitBtn.setPickOnBounds(true);
			exitBtn.setPreserveRatio(true);
			exitBtn.setEffect(new ColorAdjust(0, 0, 1.0, 0));
			AnchorPane.setTopAnchor(exitBtn, 5.0);
			AnchorPane.setRightAnchor(exitBtn, 5.0);
			
			GridPane gridPane = new GridPane();
			gridPane.setPadding(new Insets(3, 10, 10, 10));
			gridPane.setHgap(15.0);
			gridPane.setVgap(3.0);
			AnchorPane.setTopAnchor(gridPane, 33.0);
			AnchorPane.setBottomAnchor(gridPane, 0.0);
			AnchorPane.setLeftAnchor(gridPane, 0.0);
			AnchorPane.setRightAnchor(gridPane, 0.0);
			
			gridPane.getColumnConstraints().addAll(
					new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true),
					new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true));
			for (int i = 0; i < 6; i++) {
				gridPane.getRowConstraints().add(new RowConstraints());
			}
			
			TextField nameField = new TextField();
			nameField.setEditable(false);
			GridPane.setRowIndex(nameField, 1);
			GridPane.setColumnSpan(nameField, 2);
			
			TextField expField = new TextField();
			expField.setEditable(false);
			GridPane.setRowIndex(expField, 3);
			
			TextField statusField = new TextField();
			statusField.setEditable(false);
			GridPane.setColumnIndex(statusField, 1);
			GridPane.setRowIndex(statusField, 3);
			
			TextField licNumField = new TextField();
			licNumField.setEditable(false);
			licNumField.setPrefColumnCount(Integer.MAX_VALUE);
			GridPane.setRowIndex(licNumField, 5);
			GridPane.setColumnSpan(licNumField, 2);
			
			nameField.setText(name);
			expField.setText(exp);
			statusField.setText(status);
			licNumField.setText(licnum);
			
			Label nameLabel = createLabel(localization.getLocalizedMessage("PedLookup.NameLabel", "Name:"));
			nameLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label expDateLabel = createLabel(
					localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
			GridPane.setRowIndex(expDateLabel, 2);
			expDateLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label licStatusLabel = createLabel(
					localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:"));
			GridPane.setColumnIndex(licStatusLabel, 1);
			GridPane.setRowIndex(licStatusLabel, 2);
			licStatusLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label licNumLabel = createLabel(
					localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
			GridPane.setRowIndex(licNumLabel, 4);
			licNumLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			final String UILightColor = "rgb(255,255,255,0.75)";
			final String UIDarkColor = "rgb(0,0,0,0.75)";
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				nameLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
			} else {
				nameLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
			}
			
			gridPane.getChildren().addAll(nameField, expField, statusField, licNumField, nameLabel, expDateLabel,
			                              licStatusLabel, licNumLabel);
			
			popupContent.getChildren().addAll(titleLabel, exitBtn, gridPane);
			
			popupContent.setOnMouseMoved(event -> {
				double x = event.getX();
				boolean isOnRightEdge = x > popupContent.getWidth() - 10;
				
				if (isOnRightEdge) {
					popupContent.setCursor(Cursor.E_RESIZE);
				} else {
					popupContent.setCursor(Cursor.DEFAULT);
				}
			});
			
			popupContent.setOnMouseDragged(event -> {
				double x = event.getX();
				
				double minWidth = 200;
				
				if (popupContent.getCursor() == Cursor.E_RESIZE) {
					popupContent.setPrefWidth(Math.max(x, minWidth));
				}
			});
			
			DropShadow dropShadow = new DropShadow();
			dropShadow.setColor(new Color(0, 0, 0, 0.3));
			dropShadow.setOffsetX(0);
			dropShadow.setOffsetY(0);
			dropShadow.setRadius(15);
			dropShadow.setSpread(0.3);
			popupContent.setEffect(dropShadow);
			popupContent.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "bkgColor") + ";");
			
			Popup popup = new Popup();
			popup.getContent().add(popupContent);
			
			final boolean[] isPopupShown = {false};
			
			exitBtn.setOnMouseClicked(event -> {
				popup.hide();
				isPopupShown[0] = false;
			});
			
			label.setOnMouseClicked(event -> {
				if (isPopupShown[0]) {
					popup.hide();
					isPopupShown[0] = false;
				} else {
					popup.show(label.getScene().getWindow(), -9999, -9999);
					
					double labelScreenX = label.localToScreen(label.getBoundsInLocal()).getMinX();
					double labelScreenY = label.localToScreen(label.getBoundsInLocal()).getMinY();
					double labelWidth = label.getWidth();
					
					double popupX = labelScreenX + (labelWidth / 2) - (popupContent.getWidth() / 2);
					double popupY = labelScreenY - popupContent.getHeight();
					
					popup.setX(popupX);
					popup.setY(popupY - 15);
					
					isPopupShown[0] = true;
				}
			});
			exitBtn.requestFocus();
		} catch (Exception e) {
			logError("Error creating license popup from field " + label.getText() + ": ", e);
		}
	}
	
	@javafx.fxml.FXML
	public void onLookupProbabilitySettingsClick(ActionEvent actionEvent) {
		WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
		                                 "Windows/Settings/probability-settings-view.fxml", "Lookup Probability Config",
		                                 true, 1, true, false, mainDesktopControllerObj.getTaskBarApps(), new Image(
						Objects.requireNonNull(Launcher.class.getResourceAsStream(
								"/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png"))));
	}
	
	@javafx.fxml.FXML
	public void vehcreateimpreport(ActionEvent actionEvent) {
		String plate = vehplatefield2.getText().trim();
		String model = vehmodelfield.getText().trim();
		String owner = vehownerfield.getText().trim();
		Object type = vehtypecombobox.getValue();
		
		Map<String, Object> impoundReportObj = newImpound();
		Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get(
				localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report") + " Map");
		
		TextField offenderNameimp = (TextField) impoundReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField plateNumberimp = (TextField) impoundReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
		TextField modelimp = (TextField) impoundReportMap.get(
				localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
		ComboBox vehType = (ComboBox) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType",
		                                                                                    localization.getLocalizedMessage(
				                                                                                    "ReportWindows.FieldType",
				                                                                                    "type")));
		
		plateNumberimp.setText(plate);
		modelimp.setText(model);
		offenderNameimp.setText(owner);
		offenderNameimp.setText(owner);
		vehType.setValue(type);
	}
	
	@javafx.fxml.FXML
	public void vehUpdateInfo(ActionEvent actionEvent) {
		String searchedPlate = vehSearchField.getEditor().getText().trim();
		Optional<Vehicle> vehOptional = findVehicleByNumber(searchedPlate);
		
		if (vehOptional.isPresent()) {
			Vehicle vehicle = vehOptional.get();
			
			vehtypecombobox.getStyleClass().remove("combo-boxType");
			vehtypecombobox.getStyleClass().remove("combo-boxVehicle");
			if (vehicle.getType() != null) {
				vehicle.setType(vehtypecombobox.getValue().toString());
				if (vehtypecombobox.getValue().toString().equalsIgnoreCase("N/A")) {
					vehtypecombobox.getStyleClass().add("combo-boxType");
				} else {
					vehtypecombobox.getStyleClass().add("combo-boxVehicle");
				}
				try {
					Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
				} catch (JAXBException e) {
					logError("Could not add ped from update fields button: ", e);
				}
			}
			
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
	
	public Label getLbl1() {
		return lbl1;
	}
	
	public Button getAddDataToNotesBtn() {
		return addDataToNotesBtn;
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
	
	public Label getNoVehImageFoundlbl() {
		return noVehImageFoundlbl;
	}
	
	public AnchorPane getVehLookupPane() {
		return vehLookupPane;
	}
	
	public Label getPlt10() {
		return plt10;
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Label getLookupmainlbl() {
		return lookupmainlbl;
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
	
	public AnchorPane getLookupPane() {
		return lookupPane;
	}
	
	public Button getVehSearchBtn() {
		return vehSearchBtn;
	}
	
	public Label getInfo1() {
		return info1;
	}
	
	public Label getInfo2() {
		return info2;
	}
	
	public Button getBtninfo1() {
		return btninfo1;
	}
	
	public Button getBtninfo2() {
		return btninfo2;
	}
	
	public Button getBtninfo3() {
		return btninfo3;
	}
	
	public ComboBox getVehSearchField() {
		return vehSearchField;
	}
	
	@javafx.fxml.FXML
	public void ownerLookup(ActionEvent actionEvent) {
		CustomWindow pedWindow = WindowManager.getWindow("Pedestrian Lookup");
		if (pedWindow == null) {
			CustomWindow pedApp = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
			                                                       "Windows/Apps/lookup-ped-view.fxml",
			                                                       "Pedestrian Lookup", true, 1, true, false,
			                                                       mainDesktopControllerObj.getTaskBarApps(),
			                                                       pedLookupAppObj.getImage());
			if (pedApp != null && pedApp.controller != null) {
				pedLookupViewController = (PedLookupViewController) pedApp.controller;
			}
			try {
				settingsController.loadTheme();
			} catch (IOException e) {
				logError("Error loading theme from ownerLookup", e);
			}
			pedWindow = pedApp;
		}
		if (pedWindow != null) {
			if (pedWindow.isMinimized) {
				pedWindow.restoreWindow(pedWindow.title);
			}
			if (pedLookupViewController != null) {
				pedLookupViewController.getPedSearchField().getEditor().setText(vehownerfield.getText().strip());
				try {
					pedLookupViewController.onPedSearchBtnClick(new ActionEvent());
					pedWindow.bringToFront();
					log("Bringing up ped search for: " + vehownerfield.getText().strip() + " from ownerLookup",
					    LogUtils.Severity.DEBUG);
				} catch (IOException e) {
					logError("Error searching owner from ownerLookup, owner: " + vehownerfield.getText().strip(), e);
				}
			}
		}
	}
}
