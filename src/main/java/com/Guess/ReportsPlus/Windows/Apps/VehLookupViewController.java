package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Windows.Settings.settingsController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.LookupObjects.VehicleObject;
import com.Guess.ReportsPlus.util.History.Vehicle;
import com.Guess.ReportsPlus.util.Other.NoteTab;
import jakarta.xml.bind.JAXBException;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.Guess.ReportsPlus.Desktop.mainDesktopController.pedLookupAppObj;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Apps.PedLookupViewController.pedLookupViewController;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.*;
import static com.Guess.ReportsPlus.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UIDarkColor;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UILightColor;
import static com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils.newImpound;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.*;
import static com.Guess.ReportsPlus.util.History.Vehicle.VehicleHistoryUtils.*;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;
import static com.Guess.ReportsPlus.util.Other.Callout.CalloutManager.createLabel;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.updateRecentSearches;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.vehImageFolderURL;
import static com.Guess.ReportsPlus.util.Strings.dropdownInfo.vehicleTypes;

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
	@javafx.fxml.FXML
	private Label plt11;
	@javafx.fxml.FXML
	private TextField vehvinfield;
	
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
		vehnocolorlabel.setText(localization.getLocalizedMessage("VehicleLookup.NoColorFoundLabel", "No Color On Record"));
		noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
		noRecordFoundLabelVeh.setText(localization.getLocalizedMessage("VehicleLookup.NoVehFoundInSystem", "No Record Found In System"));
		lbl1.setText(localization.getLocalizedMessage("VehicleLookup.SubHeading", "Search Plate:"));
		
		vehSearchBtn.setText(localization.getLocalizedMessage("VehicleLookup.SearchButton", "Search"));
		addDataToNotesBtn.setText(localization.getLocalizedMessage("VehicleLookup.AddDataToNotesBtn", "Add Data To Notes"));
		
		btninfo3.setText(localization.getLocalizedMessage("VehicleLookup.OwnerLookupBtn", "Owner Lookup"));
		btninfo2.setText(localization.getLocalizedMessage("VehicleLookup.CreateImpoundReportBtn", "Create Impound Report"));
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
		plt11.setText(localization.getLocalizedMessage("VehicleLookup.FieldVIN", "Registered VIN:"));
		
	}
	
	@javafx.fxml.FXML
	public void onVehSearchBtnClick(ActionEvent actionEvent) throws IOException {
		boolean playAudio = false;
		boolean foundVehicle = false;
		String searchedPlate = vehSearchField.getEditor().getText().trim();
		updateRecentSearches(recentVehicleSearches, vehSearchField, searchedPlate);
		vehSearchField.getEditor().setText(searchedPlate);
		vehSearchField.getEditor().positionCaret(vehSearchField.getEditor().getText().length());
		
		logInfo("Searched Plate#: " + searchedPlate);
		
		Optional<Vehicle> historyVehicle = findVehicleByNumber(searchedPlate);
		VehicleObject worldVehicle = new VehicleObject(searchedPlate);
		
		boolean worldVehicleIsValid = worldVehicle.getPlate() != null && !worldVehicle.getPlate().equalsIgnoreCase("Not Found") && !worldVehicle.getPlate().equalsIgnoreCase("no value provided");
		
		// A Vehicle exists in either the history file or a valid worldVeh file
		if (historyVehicle.isPresent() || worldVehicleIsValid) {
			foundVehicle = true;
			vehtypecombobox.getItems().clear();
			vehtypecombobox.getItems().addAll(vehicleTypes);
			vehRecordPane.setVisible(true);
			noRecordFoundLabelVeh.setVisible(false);
		} else {
			// A Vehicle does not exist in either history file or worldVeh file
			logWarn("No Vehicle With Plate: [" + searchedPlate + "] Found Anywhere");
			vehRecordPane.setVisible(false);
			noRecordFoundLabelVeh.setVisible(true);
			return;
		}
		
		logInfo("Found Vehicle: " + foundVehicle);
		
		// Check if vehicle is present in vehHistory
		if (historyVehicle.isPresent()) {
			logDebug("Found: " + searchedPlate + " From VehHistory file");
			
			Vehicle vehicle = historyVehicle.get();
			
			processVehicleInfo(vehicle.getPlateNumber(), vehicle.getColor(), vehicle.getModel(), vehicle.getStolenStatus(), vehicle.getPoliceStatus(), vehicle.getOwner(), vehicle.getRegistration(), vehicle.getRegistrationNumber(), vehicle.getRegistrationExpiration(), vehicle.getInsurance(),
			                   vehicle.getInsuranceNumber(), vehicle.getInsuranceExpiration(), vehicle.getType(), vehicle.getVin(), vehicle.getInspection());
			
			vehtypecombobox.setValue(vehicle.getType());
			vehownerfield.setText(vehicle.getOwner());
			vehvinfield.setText(vehicle.getVin());
			vehpolicefield.setText(vehicle.getPoliceStatus());
			vehinspectionfield.setText(vehicle.getInspection());
			vehplatefield2.setText(vehicle.getPlateNumber());
			vehmodelfield.setText(vehicle.getModel());
			vehstolenfield.setText(vehicle.getStolenStatus());
			
			vehtypecombobox.getStyleClass().remove("combo-boxType");
			vehtypecombobox.getStyleClass().remove("combo-boxVehicle");
			
			if (vehicle.getType().equalsIgnoreCase("N/A")) {
				vehtypecombobox.getStyleClass().add("combo-boxType");
			} else {
				vehtypecombobox.getStyleClass().add("combo-boxVehicle");
			}
			
			if (vehicle.getStolenStatus().equalsIgnoreCase("true")) {
				vehstolenfield.setStyle("-fx-text-fill: red !important;");
				playAudio = true;
			} else {
				vehstolenfield.setStyle("-fx-text-fill: black !important;");
			}
			
			if (vehicle.getInspection().equalsIgnoreCase("expired") || vehicle.getInspection().equalsIgnoreCase("invalid")) {
				vehinspectionfield.setStyle("-fx-text-fill: red !important;");
			} else {
				vehinspectionfield.setStyle("-fx-text-fill: black !important;");
			}
			
			updateVehicleInfo(vehregfield, vehicle.getRegistration(), vehicle.getRegistrationExpiration(), vehicle.getRegistrationNumber(), "Registration", "VehLookup.RegistrationInfoTitle", vehicle);
			
			updateVehicleInfo(vehinsfield, vehicle.getInsurance(), vehicle.getInsuranceExpiration(), vehicle.getInsuranceNumber(), "Insurance", "VehLookup.InsuranceInfoTitle", vehicle);
			
			if (vehicle.getPoliceStatus().equalsIgnoreCase("true")) {
				vehpolicefield.setStyle("-fx-text-fill: green !important;");
			} else {
				vehpolicefield.setStyle("-fx-text-fill: black !important;");
			}
			
			if (vehicle.getColor() != null && !vehicle.getColor().equals("Not Found") && !vehicle.getColor().equals("no value provided")) {
				vehnocolorlabel.setVisible(false);
				vehcolordisplay.setStyle("-fx-background-color: " + vehicle.getColor() + ";" + "-fx-border-color: grey;");
			} else {
				vehnocolorlabel.setVisible(true);
				vehcolordisplay.setStyle("-fx-background-color: #F2F2F2;" + "-fx-border-color: grey;");
			}
			
			runModelUpdate(vehicle.getModel());
			
			// Check if vehicle present in worldVeh
		} else if (worldVehicleIsValid) {
			logDebug("Found: " + searchedPlate + " From WorldVeh file. WorldVehiclePlate: " + worldVehicle.getPlate());
			
			Vehicle vehicle = processVehicleInfo(worldVehicle.getPlate(), worldVehicle.getColor(), worldVehicle.getModel(), worldVehicle.getIsStolen(), worldVehicle.getIsPolice(), worldVehicle.getOwner(), worldVehicle.getRegistration(), null, worldVehicle.getRegistrationDate(),
			                                     worldVehicle.getInsurance(), null, worldVehicle.getInsuranceDate(), null, worldVehicle.getVin(), null);
			
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
			vehvinfield.setText(vehicle.getVin());
			
			vehtypecombobox.getStyleClass().remove("combo-boxVehicle");
			vehtypecombobox.getStyleClass().add("combo-boxType");
			
			updateVehicleInfo(vehregfield, vehicle.getRegistration(), vehicle.getRegistrationExpiration(), vehicle.getRegistrationNumber(), "Registration", "VehLookup.RegistrationInfoTitle", vehicle);
			
			updateVehicleInfo(vehinsfield, vehicle.getInsurance(), vehicle.getInsuranceExpiration(), vehicle.getInsuranceNumber(), "Insurance", "VehLookup.InsuranceInfoTitle", vehicle);
			
			vehinspectionfield.setText(vehicle.getInspection());
			if (vehicle.getInspection().equalsIgnoreCase("expired") || vehicle.getInspection().equalsIgnoreCase("invalid")) {
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
			if (vehicle.getColor() != null && !vehicle.getColor().equals("Not Found") && !vehicle.getColor().equals("no value provided")) {
				vehnocolorlabel.setVisible(false);
				vehcolordisplay.setStyle("-fx-background-color: " + vehicle.getColor() + ";" + "-fx-border-color: grey;");
			} else {
				vehnocolorlabel.setVisible(true);
				vehcolordisplay.setStyle("-fx-background-color: #F2F2F2;" + "-fx-border-color: grey;");
			}
			vehtypecombobox.setValue(vehicle.getType());
			
			runModelUpdate(worldVehicle.getModel());
		}
		
		// Play audio if a warning was found
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
	
	private void updateVehicleInfo(TextField field, String info, String expirationMethod, String numberMethod, String infoType, String infoTitle, Vehicle vehicle) {
		field.getStyleClass().clear();
		field.setOnMouseClicked(null);
		field.setText(info);
		
		if (info == null) {
			field.setText("None");
			field.getStyleClass().add("text-field");
			field.setStyle("-fx-text-fill: black !important;");
		} else if (info.equalsIgnoreCase("expired") || info.equalsIgnoreCase("none") || info.equalsIgnoreCase("revoked")) {
			field.getStyleClass().add("valid-field");
			field.setStyle("-fx-text-fill: red !important;");
			field.setText(info.toUpperCase());
			
			boolean updated = false;
			if (expirationMethod == null || expirationMethod.equalsIgnoreCase("no value provided")) {
				if (info.equalsIgnoreCase("expired")) {
					expirationMethod = generateExpiredLicenseExpirationDate(3);
				} else if (info.equalsIgnoreCase("None") || info.equalsIgnoreCase("revoked")) {
					expirationMethod = "None";
				}
				updated = true;
			}
			if (numberMethod == null || numberMethod.equalsIgnoreCase("no value provided")) {
				numberMethod = generateLicenseNumber();
				if (info.equalsIgnoreCase("None") || info.equalsIgnoreCase("revoked")) {
					numberMethod = "None";
				}
				updated = true;
			}
			if (updated) {
				try {
					Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
				} catch (JAXBException e) {
					logError("Error updating Vehicle for " + infoType + " info: ", e);
				}
			}
			createRegInsInfoPopup(field, localization.getLocalizedMessage(infoTitle, infoType + " Information:"), vehicle.getOwner(), expirationMethod, info, numberMethod);
		} else if (info.equalsIgnoreCase("valid")) {
			field.getStyleClass().add("valid-field");
			field.setStyle("-fx-text-fill: #060 !important;");
			field.setText("Valid");
			
			boolean updated = false;
			if (expirationMethod == null) {
				expirationMethod = generateValidLicenseExpirationDate();
				updated = true;
			}
			if (numberMethod == null) {
				numberMethod = generateLicenseNumber();
				updated = true;
			}
			if (updated) {
				try {
					Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
				} catch (JAXBException e) {
					logError("Error updating Vehicle for " + infoType + " info: ", e);
				}
			}
			createRegInsInfoPopup(field, localization.getLocalizedMessage(infoTitle, infoType + " Information:"), vehicle.getOwner(), expirationMethod, info, numberMethod);
		}
	}
	
	private void runModelUpdate(String vehModelString) {
		if (vehModelString != null && !vehModelString.equalsIgnoreCase("Not Found") && !vehModelString.equalsIgnoreCase("no value provided")) {
			File pedImgFolder = new File(vehImageFolderURL);
			if (pedImgFolder.exists()) {
				logDebug("Detected vehImage folder..");
				try {
					if (ConfigReader.configRead("uiSettings", "enablePedVehImages").equalsIgnoreCase("true")) {
						
						File[] matchingFiles = pedImgFolder.listFiles((dir, name) -> name.equalsIgnoreCase(vehModelString + ".jpg"));
						
						if (matchingFiles != null && matchingFiles.length > 0) {
							File matchingFile = matchingFiles[0];
							logInfo("Matching vehImage found: " + matchingFile.getName());
							
							try {
								String fileURI = matchingFile.toURI().toString();
								vehImageView.setImage(new Image(fileURI));
								noVehImageFoundlbl.setVisible(true);
								noVehImageFoundlbl.setText("Vehicle Model Found On File:");
							} catch (Exception e) {
								Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
								vehImageView.setImage(defImage);
								noVehImageFoundlbl.setVisible(true);
								noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
								logError("Could not set vehImage: ", e);
							}
						} else {
							logWarn("No matching vehImage found for the model: " + vehModelString + ", displaying no vehImage found.");
							Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
							vehImageView.setImage(defImage);
							noVehImageFoundlbl.setVisible(true);
							noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
						}
					} else {
						logWarn("enablePedVehImages is disabled in settings so not displaying veh image");
						Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
						vehImageView.setImage(defImage);
						noVehImageFoundlbl.setVisible(true);
						noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
					}
				} catch (IOException e) {
					logError("Error getting configValue for playLookupWarning: ", e);
				}
			} else {
				Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
				vehImageView.setImage(defImage);
				noVehImageFoundlbl.setVisible(true);
				noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
			}
		} else {
			Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
			vehImageView.setImage(defImage);
			noVehImageFoundlbl.setVisible(true);
			noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
		}
	}
	
	public void createRegInsInfoPopup(TextField label, String headerText, String name, String exp, String status, String licnum) {
		try {
			AnchorPane popupContent = new AnchorPane();
			popupContent.setPrefWidth(Region.USE_COMPUTED_SIZE);
			popupContent.getStylesheets().add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/courtCase/courtCaseCss.css").toExternalForm());
			
			Label titleLabel = new Label(headerText);
			titleLabel.setPadding(new Insets(0, 33, 0, 33));
			titleLabel.setAlignment(Pos.CENTER);
			titleLabel.setPrefHeight(33.0);
			titleLabel.setStyle("-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";");
			titleLabel.setTextFill(Paint.valueOf("WHITE"));
			titleLabel.setFont(new Font("Inter 28pt Bold", 17.0));
			AnchorPane.setTopAnchor(titleLabel, 0.0);
			AnchorPane.setLeftAnchor(titleLabel, 0.0);
			AnchorPane.setRightAnchor(titleLabel, 0.0);
			
			ImageView exitBtn = new ImageView(new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/cross.png")));
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
			
			gridPane.getColumnConstraints().addAll(new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true), new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.SOMETIMES, HPos.LEFT, true));
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
			if (status.toLowerCase().equalsIgnoreCase("expired")) {
				statusField.setText(status + " " + parseExpirationDate(exp));
			} else {
				statusField.setText(status);
			}
			licNumField.setText(licnum);
			
			Label nameLabel = createLabel(localization.getLocalizedMessage("PedLookup.NameLabel", "Name:"));
			nameLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label expDateLabel = createLabel(localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
			GridPane.setRowIndex(expDateLabel, 2);
			expDateLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label licStatusLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:"));
			GridPane.setColumnIndex(licStatusLabel, 1);
			GridPane.setRowIndex(licStatusLabel, 2);
			licStatusLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			Label licNumLabel = createLabel(localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
			GridPane.setRowIndex(licNumLabel, 4);
			licNumLabel.setMinWidth(Region.USE_PREF_SIZE);
			
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
				nameLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				Node node = expField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = statusField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = nameField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
				node = licNumField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UIDarkColor + " !important;");
			} else {
				nameLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				expDateLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licStatusLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				licNumLabel.setStyle("-fx-text-fill: " + UILightColor + ";");
				Node node = expField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = statusField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = nameField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
				node = licNumField.lookup(".text-field");
				node.setStyle("-fx-text-fill: " + UILightColor + " !important;");
			}
			
			gridPane.getChildren().addAll(nameField, expField, statusField, licNumField, nameLabel, expDateLabel, licStatusLabel, licNumLabel);
			
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
	
	private Vehicle processVehicleInfo(String plateNumber_value, String color_value, String model_value, String stolenStatus_value, String policeStatus_value, String owner_value, String registration_value, String registrationNumber_value, String registrationExpiration_value, String insurance_value, String insuranceNumber_value, String insuranceExpiration_value, String type_value, String vin_value, String inspection_value) {
		Vehicle targetVehicle = new Vehicle();
		if (plateNumber_value == null || plateNumber_value.equalsIgnoreCase("no value provided") || plateNumber_value.equalsIgnoreCase("not found")) {
			logError("processVehicleInfo; plateNumber_value was null, set as ERROR");
			targetVehicle.setPlateNumber("ERROR");
		} else {
			targetVehicle.setPlateNumber(plateNumber_value);
		}
		
		if (color_value == null || color_value.equalsIgnoreCase("no value provided") || color_value.equalsIgnoreCase("not found")) {
			logError("processVehicleInfo; color_value was null, set as ERROR");
			targetVehicle.setColor("ERROR");
		} else {
			targetVehicle.setColor(color_value);
		}
		
		if (model_value == null || model_value.equalsIgnoreCase("no value provided") || model_value.equalsIgnoreCase("not found")) {
			logError("processVehicleInfo; model_value was null, set as: no value provided");
			targetVehicle.setModel("no value provided");
		} else {
			targetVehicle.setModel(model_value);
		}
		
		if (stolenStatus_value == null || stolenStatus_value.equalsIgnoreCase("no value provided") || stolenStatus_value.equalsIgnoreCase("not found")) {
			logError("processVehicleInfo; stolenStatus_value was null, set as Unknown");
			targetVehicle.setStolenStatus("Unknown");
		} else {
			targetVehicle.setStolenStatus(stolenStatus_value);
		}
		
		if (policeStatus_value == null || policeStatus_value.equalsIgnoreCase("no value provided") || policeStatus_value.equalsIgnoreCase("not found")) {
			logError("processVehicleInfo; policeStatus_value was null, set as Unknown");
			targetVehicle.setPoliceStatus("Unknown");
		} else {
			targetVehicle.setPoliceStatus(policeStatus_value);
		}
		
		if (owner_value == null || owner_value.equalsIgnoreCase("no value provided") || owner_value.equalsIgnoreCase("not found")) {
			logError("processVehicleInfo; owner_value was null, set as Unknown");
			targetVehicle.setOwner("Unknown");
		} else {
			targetVehicle.setOwner(owner_value);
		}
		
		if (registration_value == null || registration_value.equalsIgnoreCase("no value provided") || registration_value.equalsIgnoreCase("not found")) {
			logError("processVehicleInfo; registration_value was null, set as Unknown");
			targetVehicle.setRegistration("Unknown");
		} else {
			targetVehicle.setRegistration(registration_value);
		}
		
		if (registrationNumber_value == null || registrationNumber_value.equalsIgnoreCase("no value provided") || registrationNumber_value.equalsIgnoreCase("not found")) {
			String generated = generateLicenseNumber();
			logWarn("processVehicleInfo; registrationNumber_value was null, generated: [" + generated + "]");
			targetVehicle.setRegistrationNumber(generated);
		} else {
			targetVehicle.setRegistrationNumber(registrationNumber_value);
		}
		
		if (registrationExpiration_value == null || registrationExpiration_value.equalsIgnoreCase("no value provided") || registrationExpiration_value.equalsIgnoreCase("not found")) {
			String expiration = "Not Applicable";
			switch (targetVehicle.getRegistration().toLowerCase()) {
				case "valid":
					expiration = generateValidLicenseExpirationDate();
					break;
				case "expired":
					expiration = generateExpiredLicenseExpirationDate(3);
					break;
				default:
				case "none":
				case "revoked":
					targetVehicle.setRegistrationExpiration(expiration);
					break;
			}
			
			logWarn("processVehicleInfo; registrationExpiration_value was null, generated: [" + expiration + "] based on registration status: [" + targetVehicle.getRegistration() + "]");
			targetVehicle.setRegistrationExpiration(expiration);
		} else {
			targetVehicle.setRegistrationExpiration(registrationExpiration_value);
		}
		
		if (insurance_value == null || insurance_value.equalsIgnoreCase("no value provided") || insurance_value.equalsIgnoreCase("not found")) {
			logError("processVehicleInfo; insurance_value was null, set as Unknown");
			targetVehicle.setInsurance("Unknown");
		} else {
			targetVehicle.setInsurance(insurance_value);
		}
		
		if (insuranceNumber_value == null || insuranceNumber_value.equalsIgnoreCase("no value provided") || insuranceNumber_value.equalsIgnoreCase("not found")) {
			String generated = generateLicenseNumber();
			logWarn("processVehicleInfo; insuranceNumber_value was null, generated: [" + generated + "]");
			targetVehicle.setInsuranceNumber(generated);
		} else {
			targetVehicle.setInsuranceNumber(insuranceNumber_value);
		}
		
		if (insuranceExpiration_value == null || insuranceExpiration_value.equalsIgnoreCase("no value provided") || insuranceExpiration_value.equalsIgnoreCase("not found")) {
			String expiration = "Not Applicable";
			switch (targetVehicle.getInsurance().toLowerCase()) {
				case "valid":
					expiration = generateValidLicenseExpirationDate();
					break;
				case "expired":
					expiration = generateExpiredLicenseExpirationDate(3);
					break;
				default:
				case "none":
				case "revoked":
					targetVehicle.setInsuranceExpiration(expiration);
					break;
			}
			
			logWarn("processVehicleInfo; insuranceExpiration_value was null, generated: [" + expiration + "] based on insurance status: [" + targetVehicle.getInsurance() + "]");
			targetVehicle.setInsuranceExpiration(expiration);
		} else {
			targetVehicle.setInsuranceExpiration(insuranceExpiration_value);
		}
		
		if (type_value == null || type_value.equalsIgnoreCase("no value provided") || type_value.equalsIgnoreCase("not found")) {
			logInfo("processVehicleInfo; type_value was null [always], set as N/A");
			targetVehicle.setType("N/A");
		} else {
			targetVehicle.setType(type_value);
		}
		
		if (vin_value == null || vin_value.equalsIgnoreCase("no value provided") || vin_value.equalsIgnoreCase("not found")) {
			String licNum = generateLicenseNumber();
			logWarn("processVehicleInfo; vin_value was null, generated: [" + licNum + "]");
		} else {
			targetVehicle.setVin(vin_value);
		}
		
		if (inspection_value == null || inspection_value.equalsIgnoreCase("no value provided") || inspection_value.equalsIgnoreCase("not found")) {
			String inspection = generateInspectionStatus();
			logInfo("processVehicleInfo; inspection_value was null [always], generated: [" + inspection + "]");
			targetVehicle.setInspection(inspection);
		} else {
			targetVehicle.setInspection(inspection_value);
		}
		
		try {
			addVehicle(targetVehicle);
		} catch (JAXBException e) {
			logError("Error adding vehicle from processVehicleInfo: ", e);
		}
		
		return targetVehicle;
	}
	
	@javafx.fxml.FXML
	public void vehcreateimpreport(ActionEvent actionEvent) {
		String plate = vehplatefield2.getText().trim();
		String model = vehmodelfield.getText().trim();
		String owner = vehownerfield.getText().trim();
		Object type = vehtypecombobox.getValue();
		
		Map<String, Object> impoundReportObj = newImpound();
		Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get(localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report") + " Map");
		
		TextField offenderNameimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField plateNumberimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
		TextField modelimp = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
		ComboBox vehType = (ComboBox) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
		
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
			if (vehtypecombobox != null && !vehtypecombobox.getValue().toString().isEmpty() && !vehtypecombobox.getValue().toString().equalsIgnoreCase("n/a")) {
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
	
	public Label getPlt11() {
		return plt11;
	}
	
	@javafx.fxml.FXML
	public void ownerLookup(ActionEvent actionEvent) {
		CustomWindow pedWindow = WindowManager.getWindow("Pedestrian Lookup");
		if (pedWindow == null) {
			CustomWindow pedApp = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Apps/lookup-ped-view.fxml", "Pedestrian Lookup", true, 1, true, false, mainDesktopControllerObj.getTaskBarApps(), pedLookupAppObj.getImage());
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
					logDebug("Bringing up ped search for: " + vehownerfield.getText().strip() + " from ownerLookup");
				} catch (IOException e) {
					logError("Error searching owner from ownerLookup, owner: " + vehownerfield.getText().strip(), e);
				}
			}
		}
	}
	
	// Dont rename
	public TextField getVehplatefield2() {
		return vehplatefield2;
	}
	
	// Dont rename
	public TextField getVehmodelfield() {
		return vehmodelfield;
	}
	
	// Dont rename
	public ComboBox getVehtypecombobox() {
		return vehtypecombobox;
	}
}
