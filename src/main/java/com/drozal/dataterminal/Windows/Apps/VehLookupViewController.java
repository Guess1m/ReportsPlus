package com.drozal.dataterminal.Windows.Apps;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.History.Vehicle;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NoteTab;
import jakarta.xml.bind.JAXBException;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopControllerObj;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.createFakeWindow;
import static com.drozal.dataterminal.Windows.Other.NotesViewController.*;
import static com.drozal.dataterminal.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.drozal.dataterminal.logs.Impound.ImpoundReportUtils.newImpound;
import static com.drozal.dataterminal.util.History.Vehicle.VehicleHistoryUtils.findVehicleByNumber;
import static com.drozal.dataterminal.util.History.Vehicle.VehicleHistoryUtils.generateInspectionStatus;
import static com.drozal.dataterminal.util.Misc.AudioUtil.playSound;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.updateRecentSearches;
import static com.drozal.dataterminal.util.Misc.dropdownInfo.vehicleTypes;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.Misc.stringUtil.vehImageFolderURL;
import static com.drozal.dataterminal.util.server.recordUtils.grabVehicleData;

public class VehLookupViewController {
	
	public static VehLookupViewController vehLookupViewController;
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
	
	public void initialize() throws IOException {
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
	public void onLookupProbabilitySettingsClick(ActionEvent actionEvent) {
		createFakeWindow(mainDesktopControllerObj.getDesktopContainer(),
		                 "Windows/Settings/probability-settings-view.fxml", "Lookup Probability Config", false, 2, true,
		                 false, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/setting.png"))));
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
}
