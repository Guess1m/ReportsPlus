package com.Guess.ReportsPlus.Windows.Apps;

import static com.Guess.ReportsPlus.Desktop.mainDesktopController.pedLookupAppObj;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Apps.PedLookupViewController.loadColorFromConfig;
import static com.Guess.ReportsPlus.Windows.Apps.PedLookupViewController.pedLookupViewController;
import static com.Guess.ReportsPlus.Windows.Apps.PedLookupViewController.toWebString;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.createNoteTabs;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesTabList;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils.newImpound;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.calculateTrueFalseProbability;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.femaleFirstNames;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateExpiredLicenseExpirationDate;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateLicenseNumber;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateRandomCoverage;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateRandomPlate;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateRegStatus;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateValidLicenseExpirationDate;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.generateVin;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.maleFirstNames;
import static com.Guess.ReportsPlus.util.History.PedHistoryMath.parseExpirationDate;
import static com.Guess.ReportsPlus.util.History.Vehicle.VehicleHistoryUtils.addVehicle;
import static com.Guess.ReportsPlus.util.History.Vehicle.VehicleHistoryUtils.findVehicleByNumber;
import static com.Guess.ReportsPlus.util.History.Vehicle.VehicleHistoryUtils.generateInspectionStatus;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.toTitleCase;
import static com.Guess.ReportsPlus.util.Server.recordUtils.getAllVehicles;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.vehImageFolderURL;
import static com.Guess.ReportsPlus.util.Strings.dropdownInfo.vehicleTypes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.IShutdownable;
import com.Guess.ReportsPlus.Windows.Settings.settingsController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.LookupObjects.VehicleObject;
import com.Guess.ReportsPlus.util.History.PedHistoryMath;
import com.Guess.ReportsPlus.util.History.Vehicle;
import com.Guess.ReportsPlus.util.Misc.Threading.WorkerThread;
import com.Guess.ReportsPlus.util.Other.NoteTab;

import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class VehLookupViewController implements IShutdownable {
	public static VehLookupViewController vehLookupViewController;
	private final ObservableList<Vehicle> masterVehicleList = FXCollections.observableArrayList();
	private final List<Vehicle> allRealVehicles = new ArrayList<>();
	private final ExecutorService searchExecutor = Executors.newSingleThreadExecutor(r -> {
		WorkerThread t = new WorkerThread("Veh-Search-Thread", r);
		t.setDaemon(true);
		return t;
	});
	private Task<?> currentSearchTask;
	// #region FXML Components
	@FXML
	private Label insInfoLabelSubHeading;
	@FXML
	private Label vehInfoSideButton;
	@FXML
	private Label ownerAndLegalSideButton;
	@FXML
	private Label ownerAndLegalHeading;
	@FXML
	private Label registrationInfoSubHeading;
	@FXML
	private VBox registrationInfoCard;
	@FXML
	private VBox insuranceInfoCard;
	@FXML
	private Label vehInsCoverageLabel;
	@FXML
	private TextField vehInsCoverageField;
	@FXML
	private Label makeLabel;
	@FXML
	private TextField vehmakefield;
	@FXML
	private Label vehRegStatusFieldLabel;
	@FXML
	private Label vehInsStatusFieldLabel;
	@FXML
	private Label vehRegNumberFieldLabel;
	@FXML
	private Label vehInsNumberFieldLabel;
	@FXML
	private Label vehRegExpFieldLabel;
	@FXML
	private Label vehInsExpFieldLabel;
	@FXML
	private TextField vehRegStatusField;
	@FXML
	private TextField vehInsStatusField;
	@FXML
	private TextField vehRegNumberField;
	@FXML
	private TextField vehInsNumberField;
	@FXML
	private TextField vehRegExpField;
	@FXML
	private TextField vehInsExpField;
	@FXML
	private BorderPane root;
	@FXML
	private Button updateotherinfobtn;
	@FXML
	private AnchorPane lookupPane;
	@FXML
	private BorderPane databaseSearchPane;
	@FXML
	private BorderPane databaseInfoPane;
	@FXML
	private TextField vehSearchField;
	@FXML
	private Label lookupmainlbl;
	@FXML
	private Label lbl1;
	@FXML
	private ListView<Vehicle> databaseListView;
	@FXML
	private Label noRecordFoundLabelVeh;
	@FXML
	private VBox sidePane;
	@FXML
	private Label backLabel;
	@FXML
	private BorderPane vehicleInfoButton;
	@FXML
	private BorderPane ownerInfoButton;
	@FXML
	private StackPane vehRecordPane;
	@FXML
	private ScrollPane ownerInfoGrid;
	@FXML
	private ScrollPane vehInfoGrid;
	@FXML
	private HBox vehImageCard;
	@FXML
	private ImageView vehImageView;
	@FXML
	private Label noVehImageFoundlbl;
	@FXML
	private Label vehPlateLabelField;
	@FXML
	private Label vehModelLabelField;
	@FXML
	private Label vehStolenLabelField;
	@FXML
	private Button addDataToNotesBtn;
	@FXML
	private VBox vehicleDetailCard;
	@FXML
	private Label info1;
	@FXML
	private Label plt1;
	@FXML
	private TextField vehplatefield2;
	@FXML
	private Label plt2;
	@FXML
	private TextField vehmodelfield;
	@FXML
	private Label plt3;
	@FXML
	private TextField vehstolenfield;
	@FXML
	private Label plt8;
	@FXML
	private TextField vehpolicefield;
	@FXML
	private Label plt4;
	@FXML
	private AnchorPane vehcolordisplay;
	@FXML
	private Label vehnocolorlabel;
	@FXML
	private VBox vehicleTypeCard;
	@FXML
	private Label plt10;
	@FXML
	private ComboBox<String> vehtypecombobox;
	@FXML
	private Button btninfo1;
	@FXML
	private VBox ownerInfoCard;
	@FXML
	private Label info2;
	@FXML
	private Label plt5;
	@FXML
	private TextField vehownerfield;
	@FXML
	private Button btninfo3;
	@FXML
	private Label plt9;
	@FXML
	private TextField vehinspectionfield;
	@FXML
	private Label plt11;
	@FXML
	private TextField vehvinfield;
	@FXML
	private Button btninfo2;

	// #endregion
	@Override
	public void shutdown() {
		logInfo("Shutting down Vehicle Lookup View and all resources...");
		if (currentSearchTask != null && currentSearchTask.isRunning()) {
			currentSearchTask.cancel();
		}
		masterVehicleList.clear();
		allRealVehicles.clear();
		if (searchExecutor != null && !searchExecutor.isShutdown()) {
			searchExecutor.shutdownNow();
		}
		searchExecutor.shutdownNow();
		if (vehLookupViewController == this) {
			vehLookupViewController = null;
		}
	}

	public void initialize() {
		vehLookupViewController = this;
		databaseSearchPane.setVisible(true);
		databaseInfoPane.setVisible(false);
		noRecordFoundLabelVeh.setVisible(false);
		setupVehListView();
		setupSearchListener();
		Platform.runLater(() -> {
			addLocalization();
			loadAllRealVehicles(true);
			loadTheming();
			logInfo("Veh Lookup View initialized successfully.");
		});
	}

	private void loadAllRealVehicles(boolean printCount) {
		Map<String, Vehicle> vehicleMap = new HashMap<>();
		List<Vehicle> serverVehicles = getAllVehicles();
		for (Vehicle vehicle : serverVehicles) {
			if (vehicle.getPlateNumber() != null && !vehicle.getPlateNumber().isEmpty()) {
				vehicleMap.put(vehicle.getPlateNumber().toUpperCase(), vehicle);
			}
		}
		try {
			Vehicle.Vehicles vehicleData = Vehicle.VehicleHistoryUtils.loadVehicles();
			if (vehicleData != null && vehicleData.getVehicleList() != null) {
				List<Vehicle> historyVehicles = vehicleData.getVehicleList();
				for (Vehicle vehicle : historyVehicles) {
					if (vehicle.getPlateNumber() != null && !vehicle.getPlateNumber().isEmpty()) {
						vehicleMap.put(vehicle.getPlateNumber().toUpperCase(), vehicle);
					}
				}
			}
		} catch (JAXBException e) {
			logError("Failed to load vehicles from VehHistory.xml", e);
		}
		allRealVehicles.clear();
		allRealVehicles.addAll(vehicleMap.values());
		if (printCount) {
			logInfo("Loaded " + allRealVehicles.size() + " unique real vehicles into memory from all sources.");
		}
	}

	private void setupSearchListener() {
		vehSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
			updateSearchResults(newValue);
		});
	}

	private void updateSearchResults(String searchText) {
		if (currentSearchTask != null && currentSearchTask.isRunning()) {
			currentSearchTask.cancel();
		}
		currentSearchTask = new Task<List<Vehicle>>() {
			@Override
			protected List<Vehicle> call() throws Exception {
				loadAllRealVehicles(false);
				if (isCancelled() || searchText == null || searchText.trim().isEmpty()) {
					return Collections.emptyList();
				}
				String upperSearchText = searchText.trim().toUpperCase();
				List<Vehicle> results = allRealVehicles.stream()
						.filter(v -> v.getPlateNumber() != null
								&& v.getPlateNumber().toUpperCase().startsWith(upperSearchText))
						.collect(Collectors.toList());
				Set<String> existingPlates = results.stream()
						.map(Vehicle::getPlateNumber)
						.collect(Collectors.toSet());
				int vehiclesToGenerate = 25 - results.size();
				if (vehiclesToGenerate > 0) {
					for (int i = 0; i < vehiclesToGenerate; i++) {
						if (isCancelled())
							break;
						String newPlate = generateRandomPlate();
						if (!existingPlates.contains(newPlate)) {
							results.add(createFakeVehicle(newPlate));
							existingPlates.add(newPlate);
						}
					}
				}
				return results;
			}
		};
		currentSearchTask.setOnSucceeded(e -> {
			masterVehicleList.setAll(((Task<List<Vehicle>>) e.getSource()).getValue());
			noRecordFoundLabelVeh.setVisible(masterVehicleList.isEmpty() && !vehSearchField.getText().trim().isEmpty());
		});
		currentSearchTask.setOnFailed(e -> {
			logError("Vehicle search task failed: ", currentSearchTask.getException());
		});
		searchExecutor.submit(currentSearchTask);
	}

	private Vehicle createFakeVehicle(String plate) {
		Vehicle fake = new Vehicle();
		fake.setPlateNumber(plate);
		fake.setVin(generateVin());
		fake.setRegistration(generateRegStatus());
		fake.setInsurance(generateRegStatus());
		fake.setStolenStatus(String.valueOf(calculateTrueFalseProbability("10")));
		fake.setModel(PedHistoryMath.vehicleModelStrings
				.get(new Random().nextInt(PedHistoryMath.vehicleModelStrings.size())));
		return fake;
	}

	private void setupVehListView() {
		databaseListView.setItems(masterVehicleList);
		databaseListView.setCellFactory(lv -> new ListCell<Vehicle>() {
			private final VBox layoutVBox = new VBox();
			private final HBox detailsBox1 = new HBox();
			private final HBox detailsBox2 = new HBox();
			private final Label plateLabel = new Label();
			private final Label modelLabel = new Label();
			private final Label stolenStatusLabel = new Label();
			private final Label vinValueLabel = new Label();
			private final Label registrationLabel = new Label();
			private final Label insuranceStatusLabel = new Label();
			{
				String textColor = "black";
				try {
					textColor = ConfigReader.configRead("uiColors", "labelColor");
				} catch (Exception e) {
					logError("Error reading label color config, using default: ", e);
				}
				String secclr;
				try {
					secclr = ConfigReader.configRead("uiColors", "headingColor");
				} catch (Exception e) {
					logError("Error reading secondary color config, using default: ", e);
					secclr = "black";
				}
				String labelstyle = "-fx-font-family: 'Inter 24pt Regular'; -fx-text-fill: " + textColor + ";";
				modelLabel.setStyle(labelstyle);
				vinValueLabel.setStyle(labelstyle);
				String titleStyle = "-fx-font-family: 'Inter 28pt Bold'; -fx-text-fill: " + secclr + ";";
				plateLabel.setStyle("-fx-font-size: 16px; " + titleStyle);
				Label modelTitle = new Label("Model:");
				modelTitle.setStyle(titleStyle);
				Label stolenTitle = new Label("Stolen:");
				stolenTitle.setStyle(titleStyle);
				Label vinTitle = new Label("VIN:");
				vinTitle.setStyle(titleStyle);
				Label regTitle = new Label("Registration:");
				regTitle.setStyle(titleStyle);
				Label insTitle = new Label("Insurance:");
				insTitle.setStyle(titleStyle);
				Pane spacer1 = new Pane();
				HBox.setHgrow(spacer1, Priority.ALWAYS);
				Pane spacer2 = new Pane();
				HBox.setHgrow(spacer2, Priority.ALWAYS);
				detailsBox1.getChildren().addAll(plateLabel, spacer1, modelTitle, modelLabel, spacer2, stolenTitle,
						stolenStatusLabel);
				Pane spacer3 = new Pane();
				HBox.setHgrow(spacer3, Priority.ALWAYS);
				Pane spacer4 = new Pane();
				HBox.setHgrow(spacer4, Priority.ALWAYS);
				detailsBox2.getChildren().addAll(vinTitle, vinValueLabel, spacer3, regTitle, registrationLabel, spacer4,
						insTitle, insuranceStatusLabel);
				detailsBox1.setSpacing(4);
				detailsBox1.setAlignment(Pos.CENTER_LEFT);
				detailsBox2.setSpacing(4);
				detailsBox2.setAlignment(Pos.CENTER_LEFT);
				layoutVBox.getChildren().addAll(detailsBox1, detailsBox2);
				layoutVBox.setSpacing(3);
				layoutVBox.setPadding(new Insets(5, 10, 5, 10));
			}

			@Override
			protected void updateItem(Vehicle vehicle, boolean empty) {
				super.updateItem(vehicle, empty);
				if (empty || vehicle == null) {
					setGraphic(null);
				} else {
					String textColor = "black";
					try {
						textColor = ConfigReader.configRead("uiColors", "labelColor");
					} catch (Exception e) {
						// ignore
					}
					plateLabel.setText(vehicle.getPlateNumber());
					modelLabel.setText(toTitleCase(vehicle.getModel()));
					vinValueLabel.setText(vehicle.getVin() != null ? vehicle.getVin() : "N/A");
					String stolenStatus = toTitleCase(vehicle.getStolenStatus());
					stolenStatusLabel.setText(stolenStatus);
					stolenStatusLabel.setStyle("-fx-font-family: 'Inter 24pt Regular'; -fx-text-fill: "
							+ ("True".equalsIgnoreCase(stolenStatus) ? "red" : textColor) + ";");
					String regStatus = toTitleCase(vehicle.getRegistration());
					registrationLabel.setText(regStatus);
					registrationLabel.setStyle("-fx-font-family: 'Inter 24pt Regular'; -fx-text-fill: "
							+ ("Valid".equalsIgnoreCase(regStatus) ? "#060" : "red") + ";");
					String insStatus = toTitleCase(vehicle.getInsurance());
					insuranceStatusLabel.setText(insStatus);
					insuranceStatusLabel.setStyle("-fx-font-family: 'Inter 24pt Regular'; -fx-text-fill: "
							+ ("Valid".equalsIgnoreCase(insStatus) ? "#060" : "red") + ";");
					setGraphic(layoutVBox);
				}
			}
		});
		databaseListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == null) {
				return;
			}
			Vehicle fullDetails = performVehicleLookup(newVal.getPlateNumber());
			if (fullDetails != null) {
				displayVehicleDetails(fullDetails);
			} else {
				logInfo("Selected a generated vehicle (" + newVal.getPlateNumber() + "). Processing it for display.");
				Vehicle processedFakeVehicle = processVehicleInfo(newVal);
				if (processedFakeVehicle != null) {
					displayVehicleDetails(processedFakeVehicle);
				} else {
					logError("Failed to process the generated vehicle: " + newVal.getPlateNumber());
					showNotificationError("Display Error", "Could not display details for the selected vehicle.");
				}
			}
		});
	}

	private void displayVehicleDetails(Vehicle vehicle) {
		boolean playAudio = false;
		databaseSearchPane.setVisible(false);
		databaseInfoPane.setVisible(true);
		setActiveGrid(vehInfoGrid);
		vehtypecombobox.getItems().setAll(vehicleTypes);
		vehplatefield2.setText(vehicle.getPlateNumber());
		vehmodelfield.setText(vehicle.getModel());
		vehmakefield.setText(vehicle.getMake());
		vehstolenfield.setText(vehicle.getStolenStatus());
		vehownerfield.setText(vehicle.getOwner());
		vehvinfield.setText(vehicle.getVin());
		vehpolicefield.setText(vehicle.getPoliceStatus());
		vehinspectionfield.setText(vehicle.getInspection());
		vehtypecombobox.setValue(vehicle.getType());
		vehPlateLabelField.setText(vehicle.getPlateNumber());
		vehModelLabelField.setText(vehicle.getModel());
		vehStolenLabelField.setText("Stolen: " + vehicle.getStolenStatus());
		String regStatus = vehicle.getRegistration();
		if (regStatus != null) {
			vehRegStatusField.setText(regStatus);
			if (regStatus.equalsIgnoreCase("EXPIRED") || regStatus.equalsIgnoreCase("SUSPENDED")
					|| regStatus.equalsIgnoreCase("REVOKED")) {
				vehRegStatusField.setText(regStatus + " " + parseExpirationDate(vehicle.getRegistrationExpiration()));
				vehRegStatusField.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
				playAudio = true;
			} else if (regStatus.equalsIgnoreCase("NONE") || regStatus.equalsIgnoreCase("UNLICENSED")) {
				vehRegStatusField.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
				playAudio = true;
			} else if (regStatus.equalsIgnoreCase("VALID")) {
				vehRegStatusField.setStyle("-fx-text-fill: #060 !important; -fx-font-family: 'Inter 28pt Bold';");
			}
			if (regStatus.equalsIgnoreCase("NONE") || regStatus.equalsIgnoreCase("UNLICENSED")) {
				vehRegExpField.setText("No Data In System");
			} else {
				vehRegExpField.setText(
						vehicle.getRegistrationExpiration() != null ? vehicle.getRegistrationExpiration() : "");
			}
		} else {
			vehRegStatusField.setText("No Data In System");
			vehRegExpField.setText("No Data In System");
		}
		vehRegNumberField.setText(
				vehicle.getRegistrationNumber() != null ? vehicle.getRegistrationNumber() : "No Data In System");
		String insStatus = vehicle.getInsurance();
		if (insStatus != null) {
			vehInsStatusField.setText(insStatus);
			vehInsCoverageField.setText(vehicle.getCoverage());
			if (insStatus.equalsIgnoreCase("EXPIRED") || insStatus.equalsIgnoreCase("SUSPENDED")
					|| insStatus.equalsIgnoreCase("REVOKED")) {
				vehInsStatusField.setText(insStatus + " " + parseExpirationDate(vehicle.getInsuranceExpiration()));
				vehInsStatusField.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
				playAudio = true;
			} else if (insStatus.equalsIgnoreCase("NONE") || insStatus.equalsIgnoreCase("UNLICENSED")) {
				vehInsStatusField.setStyle("-fx-text-fill: red !important; -fx-font-family: 'Inter 28pt Bold';");
				playAudio = true;
			} else if (insStatus.equalsIgnoreCase("VALID")) {
				vehInsStatusField.setStyle("-fx-text-fill: #060 !important; -fx-font-family: 'Inter 28pt Bold';");
			}
			if (insStatus.equalsIgnoreCase("NONE") || insStatus.equalsIgnoreCase("UNLICENSED")) {
				vehInsExpField.setText("No Data In System");
			} else {
				vehInsExpField
						.setText(vehicle.getInsuranceExpiration() != null ? vehicle.getInsuranceExpiration() : "");
			}
		} else {
			vehInsStatusField.setText("No Data In System");
			vehInsExpField.setText("No Data In System");
			vehInsCoverageField.setText("No Data In System");
		}
		vehInsNumberField
				.setText(vehicle.getInsuranceNumber() != null ? vehicle.getInsuranceNumber() : "No Data In System");
		if ("true".equalsIgnoreCase(vehicle.getStolenStatus())) {
			vehstolenfield.setStyle("-fx-text-fill: red !important;");
			vehStolenLabelField.setStyle("-fx-text-fill: red !important;");
			playAudio = true;
		} else {
			vehstolenfield.setStyle("-fx-text-fill: black !important;");
			vehStolenLabelField.setStyle("-fx-text-fill: black !important;");
		}
		if ("expired".equalsIgnoreCase(vehicle.getInspection())
				|| "invalid".equalsIgnoreCase(vehicle.getInspection())) {
			vehinspectionfield.setStyle("-fx-text-fill: red !important;");
		} else {
			vehinspectionfield.setStyle("-fx-text-fill: black !important;");
		}
		if ("true".equalsIgnoreCase(vehicle.getPoliceStatus())) {
			vehpolicefield.setStyle("-fx-text-fill: green !important;");
		} else {
			vehpolicefield.setStyle("-fx-text-fill: black !important;");
		}
		if (vehicle.getColor() != null && !vehicle.getColor().equals("Not Found")
				&& !vehicle.getColor().equals("no value provided")) {
			vehnocolorlabel.setVisible(false);
			vehcolordisplay.setStyle("-fx-background-color: " + vehicle.getColor() + "; -fx-border-color: grey;");
		} else {
			vehnocolorlabel.setVisible(true);
			vehcolordisplay.setStyle("-fx-background-color: #F2F2F2; -fx-border-color: grey;");
		}
		runModelUpdate(vehicle.getModel());
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

	private void addLocalization() {
		lookupmainlbl.setText(localization.getLocalizedMessage("VehicleLookup.MainHeader", "D.M.V Vehicle Lookup"));
		vehnocolorlabel
				.setText(localization.getLocalizedMessage("VehicleLookup.NoColorFoundLabel", "No Color On Record"));
		noVehImageFoundlbl.setText(
				localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
		noRecordFoundLabelVeh.setText(
				localization.getLocalizedMessage("VehicleLookup.NoVehFoundInSystem", "No Record Found In System"));
		lbl1.setText(localization.getLocalizedMessage("VehicleLookup.SubHeading", "Search Plate:"));
		plt1.setText(localization.getLocalizedMessage("VehicleLookup.FieldPlateNum", "License Plate:"));
		plt2.setText(localization.getLocalizedMessage("VehicleLookup.FieldModel", "Model"));
		plt3.setText(localization.getLocalizedMessage("VehicleLookup.FieldStolen", "Vehicle Stolen:"));
		plt4.setText(localization.getLocalizedMessage("VehicleLookup.FieldColor", "Color:"));
		plt5.setText(localization.getLocalizedMessage("VehicleLookup.FieldOwner", "Registered Owner:"));
		makeLabel.setText(localization.getLocalizedMessage("VehicleLookup.FieldMake", "Make:"));
		vehRegStatusFieldLabel.setText(
				localization.getLocalizedMessage("VehicleLookup.FieldRegistrationStatus", "Registration Status:"));
		vehInsStatusFieldLabel
				.setText(localization.getLocalizedMessage("VehicleLookup.FieldInsuranceStatus", "Insurance Status:"));
		vehRegExpFieldLabel.setText(
				localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
		vehInsExpFieldLabel.setText(
				localization.getLocalizedMessage("PedLookup.ExpDateLabel", "Expiration Date:"));
		vehRegNumberFieldLabel.setText(
				localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		vehInsNumberFieldLabel.setText(
				localization.getLocalizedMessage("Callout_Manager.CalloutNumber", "Number:"));
		plt8.setText(localization.getLocalizedMessage("VehicleLookup.FieldPoliceVehicle", "Police Vehicle:"));
		plt9.setText(localization.getLocalizedMessage("VehicleLookup.FieldInspectionStatus", "Inspection Status:"));
		plt10.setText(localization.getLocalizedMessage("VehicleLookup.FieldType", "Vehicle Type:"));
		plt11.setText(localization.getLocalizedMessage("VehicleLookup.FieldVIN", "Registered VIN:"));
		backLabel.setText(localization.getLocalizedMessage("PedLookup.BackButton", "Back"));
		updateotherinfobtn.setText(
				localization.getLocalizedMessage("PedLookup.UpdateOtherInfoButton", "Update Other Information"));
		info1.setText("Vehicle Information");
		info2.setText("Owner Information");
		btninfo1.setText("Update Vehicle Type");
		btninfo2.setText("Create Impound Report");
		btninfo3.setText("Owner Lookup");
		addDataToNotesBtn.setText("Add Data To Notes");
		vehInsCoverageLabel.setText("Coverage:");
	}

	public static Vehicle performVehicleLookup(String plate) {
		Optional<Vehicle> historyVehicle = findVehicleByNumber(plate);
		if (historyVehicle.isPresent()) {
			logDebug("performVehicleLookup: Found " + plate + " in VehHistory file");
			return processVehicleInfo(historyVehicle.get());
		}
		logDebug("performVehicleLookup: " + plate + " not in VehHistory. Checking world vehicle.");
		VehicleObject worldVehicle = new VehicleObject(plate);
		boolean worldVehicleIsValid = worldVehicle.getPlate() != null
				&& !worldVehicle.getPlate().equalsIgnoreCase("Not Found")
				&& !worldVehicle.getPlate().equalsIgnoreCase("no value provided");
		if (worldVehicleIsValid) {
			logDebug("performVehicleLookup: Found " + plate + " from WorldVeh file.");
			Vehicle transientVehicle = new Vehicle();
			transientVehicle.setPlateNumber(worldVehicle.getPlate());
			transientVehicle.setColor(worldVehicle.getColor());
			transientVehicle.setModel(worldVehicle.getModel());
			transientVehicle.setMake(worldVehicle.getMake());
			transientVehicle.setStolenStatus(worldVehicle.getIsStolen());
			transientVehicle.setPoliceStatus(worldVehicle.getIsPolice());
			transientVehicle.setOwner(worldVehicle.getOwner());
			transientVehicle.setOwnerAddress(worldVehicle.getOwnerAddress());
			transientVehicle.setOwnerDob(worldVehicle.getOwnerdob());
			transientVehicle.setOwnerGender(worldVehicle.getOwnerGender());
			transientVehicle.setOwnerIsWanted(worldVehicle.getOwneriswanted());
			transientVehicle.setOwnerLicenseNumber(worldVehicle.getOwnerlicensenumber());
			transientVehicle.setOwnerLicenseState(worldVehicle.getOwnerlicensestate());
			transientVehicle.setOwnerModel(worldVehicle.getOwnerModel());
			transientVehicle.setRegistration(worldVehicle.getRegistration());
			transientVehicle.setRegistrationExpiration(worldVehicle.getRegistrationDate());
			transientVehicle.setInsurance(worldVehicle.getInsurance());
			transientVehicle.setInsuranceExpiration(worldVehicle.getInsuranceDate());
			transientVehicle.setVin(worldVehicle.getVin());
			transientVehicle.setCoverage(worldVehicle.getCoverage());
			return processVehicleInfo(transientVehicle);
		}
		logWarn("performVehicleLookup: No Vehicle With Plate: [" + plate + "] Found Anywhere");
		return null;
	}

	private void runModelUpdate(String vehModelString) {
		if (vehModelString != null && !vehModelString.equalsIgnoreCase("Not Found")
				&& !vehModelString.equalsIgnoreCase("no value provided")) {
			File pedImgFolder = new File(vehImageFolderURL);
			if (pedImgFolder.exists()) {
				logDebug("Detected vehImage folder..");
				try {
					if (ConfigReader.configRead("uiSettings", "enablePedVehImages").equalsIgnoreCase("true")) {
						File[] matchingFiles = pedImgFolder
								.listFiles((dir, name) -> name.equalsIgnoreCase(vehModelString + ".jpg"));
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
								noVehImageFoundlbl.setText(localization.getLocalizedMessage(
										"VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
								logError("Could not set vehImage: ", e);
							}
						} else {
							logWarn("No matching vehImage found for the model: " + vehModelString
									+ ", displaying no vehImage found.");
							Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
							vehImageView.setImage(defImage);
							noVehImageFoundlbl.setVisible(true);
							noVehImageFoundlbl.setText(localization.getLocalizedMessage(
									"VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
						}
					} else {
						logWarn("enablePedVehImages is disabled in settings so not displaying veh image");
						Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
						vehImageView.setImage(defImage);
						noVehImageFoundlbl.setVisible(true);
						noVehImageFoundlbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
								"No Image Found In System"));
					}
				} catch (IOException e) {
					logError("Error getting configValue for playLookupWarning: ", e);
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
			noVehImageFoundlbl.setText(
					localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
		}
	}

	private static Vehicle processVehicleInfo(Vehicle vehicle) {
		boolean needsSave = false;
		if (vehicle.getPlateNumber() == null)
			return null;
		String vehicleId = vehicle.getPlateNumber();
		if (vehicle.getMake() == null) {
			logInfo("Vehicle [" + vehicleId + "] missing make. Generated: " + "Unknown");
			vehicle.setMake("Unknown");
			needsSave = true;
		}
		if (vehicle.getColor() == null) {
			List<String> colors = Arrays.asList(
					"rgb(0,0,0)", "rgb(128,128,128)", "rgb(255,255,255)", "rgb(192,192,192)", "rgb(255,0,0)",
					"rgb(0,0,255)", "rgb(210,180,140)");
			String newColor = colors.get(new Random().nextInt(colors.size()));
			logInfo("Vehicle [" + vehicleId + "] missing color. Generated: " + newColor);
			vehicle.setColor(newColor);
			needsSave = true;
		}
		if (vehicle.getModel() == null) {
			List<String> models = Arrays.asList("baller", "gresley", "seminole", "rebla", "toros", "patriot",
					"landstalker", "tailgater", "premier", "intruder", "fugitive", "glendale", "asterope", "warrener");
			String newModel = models.get(new Random().nextInt(models.size()));
			logInfo("Vehicle [" + vehicleId + "] missing model. Generated: " + newModel);
			vehicle.setModel(newModel);
			needsSave = true;
		}
		if (vehicle.getStolenStatus() == null) {
			String stolenStatus = String.valueOf(calculateTrueFalseProbability("10"));
			logInfo("Vehicle [" + vehicleId + "] missing stolen status. Generated: " + stolenStatus);
			vehicle.setStolenStatus(stolenStatus);
			needsSave = true;
		}
		if (vehicle.getPoliceStatus() == null) {
			logInfo("Vehicle [" + vehicleId + "] missing police status. Generated: false");
			vehicle.setPoliceStatus("false");
			needsSave = true;
		}
		if (vehicle.getOwner() == null) {
			Random random = new Random();
			String firstName;
			boolean isMale = random.nextBoolean();
			if (isMale && !maleFirstNames.isEmpty()) {
				firstName = maleFirstNames.get(random.nextInt(maleFirstNames.size()));
			} else if (!isMale && !femaleFirstNames.isEmpty()) {
				firstName = femaleFirstNames
						.get(random.nextInt(femaleFirstNames.size()));
			} else if (!maleFirstNames.isEmpty()) {
				firstName = maleFirstNames.get(random.nextInt(maleFirstNames.size()));
			} else if (!femaleFirstNames.isEmpty()) {
				firstName = femaleFirstNames
						.get(random.nextInt(femaleFirstNames.size()));
			} else {
				firstName = "Alex";
			}
			String lastName = PedHistoryMath.lastNames.get(random.nextInt(PedHistoryMath.lastNames.size()));
			String newOwner = firstName + " " + lastName;
			logInfo("Vehicle [" + vehicleId + "] missing owner. Generated: " + newOwner);
			vehicle.setOwner(newOwner);
			needsSave = true;
		}
		if (vehicle.getVin() == null) {
			String newVin = generateVin();
			logInfo("Vehicle [" + vehicleId + "] missing VIN. Generated: " + newVin);
			vehicle.setVin(newVin);
			needsSave = true;
		}
		if (vehicle.getType() == null) {
			logInfo("Vehicle [" + vehicleId + "] missing type. Generated: N/A");
			vehicle.setType("N/A");
			needsSave = true;
		}
		if (vehicle.getInspection() == null) {
			String newInspectionStatus = generateInspectionStatus();
			logInfo("Vehicle [" + vehicleId + "] missing inspection status. Generated: " + newInspectionStatus);
			vehicle.setInspection(newInspectionStatus);
			needsSave = true;
		}
		if (vehicle.getRegistration() == null) {
			String newRegStatus = generateRegStatus();
			logInfo("Vehicle [" + vehicleId + "] missing registration status. Generated: " + newRegStatus);
			vehicle.setRegistration(newRegStatus);
			needsSave = true;
		}
		if (vehicle.getRegistrationNumber() == null) {
			String regStatus = vehicle.getRegistration();
			if (regStatus != null && (regStatus.equalsIgnoreCase("valid") || regStatus.equalsIgnoreCase("expired"))) {
				String newRegNum = generateLicenseNumber();
				logInfo("Vehicle [" + vehicleId + "] missing registration number. Generated: " + newRegNum);
				vehicle.setRegistrationNumber(newRegNum);
				needsSave = true;
			}
		}
		if (vehicle.getRegistrationExpiration() == null) {
			String expiration = "Not Applicable";
			if ("valid".equalsIgnoreCase(vehicle.getRegistration())) {
				expiration = generateValidLicenseExpirationDate();
			} else if ("expired".equalsIgnoreCase(vehicle.getRegistration())) {
				expiration = generateExpiredLicenseExpirationDate(3);
			}
			logInfo("Vehicle [" + vehicleId + "] missing registration expiration. Generated: " + expiration);
			vehicle.setRegistrationExpiration(expiration);
			needsSave = true;
		}
		if (vehicle.getInsurance() == null) {
			String newInsStatus = generateRegStatus();
			logInfo("Vehicle [" + vehicleId + "] missing insurance status. Generated: " + newInsStatus);
			vehicle.setInsurance(newInsStatus);
			needsSave = true;
		}
		if (vehicle.getInsuranceNumber() == null) {
			String insStatus = vehicle.getInsurance();
			if (insStatus != null && (insStatus.equalsIgnoreCase("valid") || insStatus.equalsIgnoreCase("expired")
					|| insStatus.equalsIgnoreCase("revoked"))) {
				String newInsNum = generateLicenseNumber();
				logInfo("Vehicle [" + vehicleId + "] missing insurance number. Generated: " + newInsNum);
				vehicle.setInsuranceNumber(newInsNum);
				needsSave = true;
			}
		}
		if (vehicle.getInsuranceExpiration() == null) {
			String expiration = "Not Applicable";
			if ("valid".equalsIgnoreCase(vehicle.getInsurance())) {
				expiration = generateValidLicenseExpirationDate();
			} else if ("expired".equalsIgnoreCase(vehicle.getInsurance())) {
				expiration = generateExpiredLicenseExpirationDate(3);
			}
			logInfo("Vehicle [" + vehicleId + "] missing insurance expiration. Generated: " + expiration);
			vehicle.setInsuranceExpiration(expiration);
			needsSave = true;
		}
		if (vehicle.getCoverage() == null) {
			String insStatus = vehicle.getInsurance();
			if (insStatus != null && (insStatus.equalsIgnoreCase("valid") || insStatus.equalsIgnoreCase("expired")
					|| insStatus.equalsIgnoreCase("revoked"))) {
				String newInsNum = generateRandomCoverage();
				logInfo("Vehicle [" + vehicleId + "] missing insurance coverage. Generated: " + newInsNum);
				vehicle.setCoverage(newInsNum);
				needsSave = true;
			} else {
				logInfo("Vehicle [" + vehicleId
						+ "] insurance status is not valid, setting coverage to 'No Data In System'");
				vehicle.setCoverage("No Data In System");
				needsSave = true;
			}
		}
		if (needsSave) {
			logInfo("Vehicle: [" + vehicleId + "] has new generated data and needs to be saved to VehicleHistory...");
			try {
				addVehicle(vehicle);
			} catch (JAXBException e) {
				logError("Error saving updated vehicle to VehicleHistory: ", e);
			}
		}
		return vehicle;
	}

	@FXML
	public void vehcreateimpreport(ActionEvent actionEvent) {
		String plate = vehplatefield2.getText().trim();
		String model = vehmodelfield.getText().trim();
		String owner = vehownerfield.getText().trim();
		Object type = vehtypecombobox.getValue();
		Map<String, Object> impoundReportObj = newImpound();
		Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj
				.get(localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report") + " Map");
		TextField offenderNameimp = (TextField) impoundReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField plateNumberimp = (TextField) impoundReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
		TextField modelimp = (TextField) impoundReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
		ComboBox vehType = (ComboBox) impoundReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
		plateNumberimp.setText(plate);
		modelimp.setText(model);
		offenderNameimp.setText(owner);
		offenderNameimp.setText(owner);
		vehType.setValue(type);
	}

	@FXML
	public void vehUpdateInfo(ActionEvent actionEvent) {
		String searchedPlate = vehplatefield2.getText().trim();
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

	@FXML
	public void vehAddDataToNotes(ActionEvent actionEvent) throws IOException {
		if (databaseInfoPane.isVisible()) {
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
			if (vehtypecombobox != null && !vehtypecombobox.getValue().toString().isEmpty()
					&& !vehtypecombobox.getValue().toString().equalsIgnoreCase("n/a")) {
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
	public void ownerLookup(ActionEvent actionEvent) {
		CustomWindow pedWindow = WindowManager.getWindow("Pedestrian Lookup");
		if (pedWindow == null) {
			CustomWindow pedApp = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
					"Windows/Apps/lookup-ped-view.fxml", "Pedestrian Lookup", true, 1, true, false,
					mainDesktopControllerObj.getTaskBarApps(), pedLookupAppObj.getImage());
			if (pedApp != null && pedApp.controller != null) {
				pedLookupViewController = (PedLookupViewController) pedApp.controller;
				pedLookupViewController.getDatabaseInfoPane().setVisible(false);
				pedLookupViewController.getDatabaseSearchPane().setVisible(true);
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
				pedLookupViewController.getDatabaseInfoPane().setVisible(false);
				pedLookupViewController.getDatabaseSearchPane().setVisible(true);
				pedLookupViewController.getPedSearchField().setText(vehownerfield.getText().strip());
				pedWindow.bringToFront();
				logDebug("Bringing up ped search for: " + vehownerfield.getText().strip() +
						" from ownerLookup");
			}
		}
	}

	private void setActiveGrid(Node pane) {
		vehInfoGrid.setVisible(false);
		ownerInfoGrid.setVisible(false);
		if (pane != null) {
			pane.setVisible(true);
			pane.requestFocus();
		}
	}

	@FXML
	public void backLabelClicked(MouseEvent actionEvent) {
		databaseInfoPane.setVisible(false);
		databaseSearchPane.setVisible(true);
		setActiveGrid(null);
		vehSearchField.setText("");
		masterVehicleList.clear();
	}

	@FXML
	public void vehicleInfoClicked(MouseEvent actionEvent) {
		setActiveGrid(vehInfoGrid);
	}

	@FXML
	public void vehUpdateOtherInfo(ActionEvent actionEvent) {
		String searchedPlate = vehplatefield2.getText().trim();
		if (searchedPlate == null || searchedPlate.isEmpty()) {
			showNotificationError("Update Error", "Cannot update a Vehicle with no Plate Number.");
			return;
		}
		Optional<Vehicle> optionalVehicle = findVehicleByNumber(searchedPlate);
		if (!optionalVehicle.isPresent()) {
			showNotificationError("Update Error", "Could not find Vehicle with Plate #: " + searchedPlate);
			return;
		}
		Vehicle vehicle = optionalVehicle.get();
		// --- Styling and Layout ---
		String bkgColor = "#F0F2F5";
		String cardColor = "#FFFFFF";
		String saveBtnColor = "#28a745";
		String cancelBtnColor = "#6c757d";
		VBox editorContent = new VBox(20);
		editorContent.setPadding(new Insets(20));
		editorContent.setStyle("-fx-background-color: " + bkgColor + ";");
		ScrollPane scrollPane = new ScrollPane(editorContent);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background-color: transparent; -fx-background-insets: 0;");
		scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		BorderPane layoutPane = new BorderPane();
		layoutPane.setCenter(scrollPane);
		layoutPane.setPrefSize(750, 650);
		layoutPane.setStyle("-fx-background-color: " + bkgColor + ";");
		Map<String, TextField> fieldMap = new HashMap<>();
		// --- Vehicle Identification (Owner is non-editable) ---
		Map<String, String> idFields = new LinkedHashMap<>();
		idFields.put("Make", vehicle.getMake());
		idFields.put("Model", vehicle.getModel());
		idFields.put("Color", vehicle.getColor());
		idFields.put("VIN", vehicle.getVin());
		editorContent.getChildren()
				.add(createVehSection("Vehicle Identification", vehicle.getOwner(), idFields, fieldMap));
		// --- Status ---
		Map<String, String> statusFields = new LinkedHashMap<>();
		statusFields.put("Stolen Status", vehicle.getStolenStatus());
		statusFields.put("Police Status", vehicle.getPoliceStatus());
		statusFields.put("Inspection", vehicle.getInspection());
		editorContent.getChildren().add(createVehSection("Vehicle Status", null, statusFields, fieldMap));
		// --- Registration ---
		Map<String, String> regFields = new LinkedHashMap<>();
		regFields.put("Registration", vehicle.getRegistration());
		regFields.put("Registration Number", vehicle.getRegistrationNumber());
		regFields.put("Registration Expiration", vehicle.getRegistrationExpiration());
		editorContent.getChildren().add(createVehSection("Registration Details", null, regFields, fieldMap));
		// --- Insurance ---
		Map<String, String> insFields = new LinkedHashMap<>();
		insFields.put("Insurance", vehicle.getInsurance());
		insFields.put("Insurance Number", vehicle.getInsuranceNumber());
		insFields.put("Insurance Expiration", vehicle.getInsuranceExpiration());
		insFields.put("Coverage", vehicle.getCoverage());
		editorContent.getChildren().add(createVehSection("Insurance Details", null, insFields, fieldMap));
		// --- Buttons ---
		Button saveButton = new Button("Save Changes");
		saveButton.setStyle("-fx-background-color: " + saveBtnColor
				+ "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;");
		Button cancelButton = new Button("Cancel");
		cancelButton.setStyle("-fx-background-color: " + cancelBtnColor
				+ "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 8 16;");
		HBox buttonBox = new HBox(15, saveButton, cancelButton);
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.setPadding(new Insets(15, 20, 15, 20));
		buttonBox.setStyle("-fx-background-color: " + cardColor
				+ "; -fx-border-color: rgba(0,0,0,0.1) transparent transparent transparent; -fx-border-width: 1;");
		layoutPane.setBottom(buttonBox);
		// --- Window ---
		CustomWindow editorWindow = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
				layoutPane, "Edit Vehicle: " + vehicle.getPlateNumber(), true, 1, true, true,
				mainDesktopControllerObj.getTaskBarApps(),
				new Image(Objects.requireNonNull(
						Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png"))));
		// --- Actions ---
		saveButton.setOnAction(_ -> {
			logInfo("Saving changes for Vehicle: " + vehicle.getPlateNumber());
			fieldMap.forEach((key, textField) -> {
				String value = textField.getText();
				String finalValue = (value == null || value.trim().isEmpty()
						|| value.equalsIgnoreCase("No Data In System")) ? null : value.trim();
				switch (key) {
					case "Make":
						vehicle.setMake(finalValue);
						break;
					case "Model":
						vehicle.setModel(finalValue);
						break;
					case "Color":
						vehicle.setColor(finalValue);
						break;
					case "VIN":
						vehicle.setVin(finalValue);
						break;
					case "Stolen Status":
						vehicle.setStolenStatus(finalValue);
						break;
					case "Police Status":
						vehicle.setPoliceStatus(finalValue);
						break;
					case "Inspection":
						vehicle.setInspection(finalValue);
						break;
					case "Registration":
						vehicle.setRegistration(finalValue);
						break;
					case "Registration Number":
						vehicle.setRegistrationNumber(finalValue);
						break;
					case "Registration Expiration":
						vehicle.setRegistrationExpiration(finalValue);
						break;
					case "Insurance":
						vehicle.setInsurance(finalValue);
						break;
					case "Insurance Number":
						vehicle.setInsuranceNumber(finalValue);
						break;
					case "Insurance Expiration":
						vehicle.setInsuranceExpiration(finalValue);
						break;
					case "Coverage":
						vehicle.setCoverage(finalValue);
						break;
				}
			});
			try {
				Vehicle.VehicleHistoryUtils.addVehicle(vehicle);
				showNotificationInfo("Update Successful",
						"Vehicle record for '" + vehicle.getPlateNumber() + "' has been updated.");
				displayVehicleDetails(vehicle); // Refresh the main view
			} catch (JAXBException e) {
				logError("Could not save updated vehicle record: ", e);
				showNotificationError("Update Failed", "Could not save vehicle record to file.");
			}
			editorWindow.closeWindow();
		});
		cancelButton.setOnAction(_ -> editorWindow.closeWindow());
	}

	private VBox createVehSection(String title, String ownerName, Map<String, String> fields,
			Map<String, TextField> fieldMap) {
		VBox sectionBox = new VBox(15);
		sectionBox.setPadding(new Insets(20));
		sectionBox.setStyle(
				"-fx-background-color: #FFFFFF; -fx-background-radius: 8; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0.1, 0, 2);");
		Label titleLabel = new Label(title);
		titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1D2C4D;");
		titleLabel.setPadding(new Insets(0, 0, 5, 0));
		titleLabel.setBorder(new javafx.scene.layout.Border(new javafx.scene.layout.BorderStroke(Color.web("#DDDDDD"),
				javafx.scene.layout.BorderStrokeStyle.SOLID, null, new javafx.scene.layout.BorderWidths(0, 0, 1, 0))));
		titleLabel.setMaxWidth(Double.MAX_VALUE);
		sectionBox.getChildren().add(titleLabel);
		GridPane grid = new GridPane();
		grid.setVgap(12);
		grid.setHgap(15);
		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(30);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(70);
		grid.getColumnConstraints().addAll(col1, col2);
		int rowIndex = 0;
		if (ownerName != null) {
			addStyledStaticField(grid, "Registered Owner", ownerName, rowIndex++);
		}
		for (Map.Entry<String, String> entry : fields.entrySet()) {
			addStyledVehEditorField(grid, entry.getKey(), entry.getValue(), rowIndex++, fieldMap);
		}
		sectionBox.getChildren().add(grid);
		return sectionBox;
	}

	private void addStyledStaticField(GridPane grid, String labelText, String value, int row) {
		Label label = new Label(labelText + ":");
		label.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555555;");
		label.setAlignment(Pos.CENTER_LEFT);
		Label valueLabel = new Label(value != null ? value : "No Data In System");
		valueLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: normal; -fx-text-fill: #333333; -fx-padding: 5 0;");
		valueLabel.setWrapText(true);
		grid.add(label, 0, row);
		grid.add(valueLabel, 1, row);
		GridPane.setValignment(label, javafx.geometry.VPos.CENTER);
	}

	private void addStyledVehEditorField(GridPane grid, String labelText, String value, int row,
			Map<String, TextField> map) {
		Label label = new Label(labelText + ":");
		label.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #555555;");
		label.setAlignment(Pos.CENTER_LEFT);
		TextField textField = new TextField(value != null ? value : "No Data In System");
		textField.setStyle(
				"-fx-background-color: #F8F9FA; -fx-border-color: #CED4DA; -fx-border-radius: 4; -fx-padding: 5 8; -fx-font-size: 13px;");
		textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				textField.setStyle(
						"-fx-background-color: #FFFFFF; -fx-border-color: #0078D7; -fx-border-radius: 4; -fx-padding: 5 8; -fx-font-size: 13px; -fx-effect: dropshadow(gaussian, rgba(0,120,215,0.3), 5, 0, 0, 0);");
			} else {
				textField.setStyle(
						"-fx-background-color: #F8F9FA; -fx-border-color: #CED4DA; -fx-border-radius: 4; -fx-padding: 5 8; -fx-font-size: 13px;");
			}
		});
		grid.add(label, 0, row);
		grid.add(textField, 1, row);
		GridPane.setValignment(label, javafx.geometry.VPos.CENTER);
		map.put(labelText, textField);
	}

	@FXML
	public void ownerInfoClicked(MouseEvent actionEvent) {
		setActiveGrid(ownerInfoGrid);
	}

	private void loadTheming() {
		Color sidePaneTextColor = loadColorFromConfig("sidePaneTextColor", Color.WHITE);
		Color labelColor = loadColorFromConfig("labelColor", Color.BLACK);
		Color headingColor = loadColorFromConfig("headingColor", Color.web("#0078D7"));
		Color cardBkgColor = loadColorFromConfig("cardBkgColor", Color.web("#FFFFFF"));
		Color buttonColor = loadColorFromConfig("buttonColor", Color.web("#DCDCDC"));
		noVehImageFoundlbl.setStyle("-fx-text-fill: " + toWebString(labelColor)
				+ " !important; -fx-font-family: 'Inter 24pt Regular';");
		noRecordFoundLabelVeh.setStyle("-fx-text-fill: " + toWebString(labelColor)
				+ " !important; -fx-font-family: 'Inter 24pt Regular';");
		sidePane.setStyle("-fx-background-color: " + toWebString(
				loadColorFromConfig("sidePaneColor", Color.web("#3E3E3E"))) + ";");
		root.setStyle(
				"-fx-background-color: " + toWebString(loadColorFromConfig("bkgColor", Color.web("#F4F4F4")))
						+ ";");
		for (Node card : Arrays.asList(vehicleDetailCard, vehicleTypeCard, ownerInfoCard,
				registrationInfoCard, insuranceInfoCard, vehImageCard)) {
			card.setStyle("-fx-background-color: " + toWebString(cardBkgColor)
					+ "; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.08), 10, 0.1, 0, 2);");
		}
		for (Label heading : Arrays.asList(ownerAndLegalHeading, vehPlateLabelField, lbl1, lookupmainlbl)) {
			heading.setStyle(
					"-fx-text-fill: " + toWebString(headingColor)
							+ " !important; -fx-font-family: 'Inter 28pt Bold';");
		}
		backLabel.setStyle("-fx-text-fill: " + toWebString(sidePaneTextColor)
				+ " !important; -fx-font-family: 'Inter 28pt Medium'; -fx-cursor: hand; -fx-border-color: "
				+ toWebString(sidePaneTextColor) + "; -fx-border-width: 0.3; -fx-padding: 1 10;");
		for (Label sidebutton : Arrays.asList(vehInfoSideButton, ownerAndLegalSideButton)) {
			sidebutton.setStyle("-fx-text-fill: " + toWebString(sidePaneTextColor)
					+ " !important; -fx-font-family: 'Inter 28pt Medium';");
		}
		for (Label label : Arrays.asList(plt1, plt2, plt3, plt4, plt8, makeLabel, info1, plt10, info2, plt5,
				registrationInfoSubHeading, vehRegStatusFieldLabel, plt9, vehRegNumberFieldLabel, plt11,
				vehRegExpFieldLabel, insInfoLabelSubHeading, vehInsStatusFieldLabel, vehInsNumberFieldLabel,
				vehInsExpFieldLabel, vehInsCoverageLabel, vehModelLabelField, vehStolenLabelField)) {
			label.setStyle(
					"-fx-text-fill: " + toWebString(labelColor)
							+ " !important; -fx-font-family: 'Inter 24pt Regular'; -fx-padding: 1.5; -fx-border-color: "
							+ toWebString(
									loadColorFromConfig("sidePaneColor", Color.web(
											"#3E3E3E")))
							+ ";");
		}
		btninfo1.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		btninfo2.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		btninfo3.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		updateotherinfobtn.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		addDataToNotesBtn.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		ownerInfoButton.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
		vehicleInfoButton.setStyle("-fx-background-color: " + toWebString(buttonColor) + " !important;");
	}

	public Label getLbl1() {
		return lbl1;
	}

	public Label getPlt4() {
		return plt4;
	}

	public Label getPlt5() {
		return plt5;
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

	public Label getPlt10() {
		return plt10;
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

	public Label getPlt11() {
		return plt11;
	}

	public TextField getVehSearchField() {
		return vehSearchField;
	}

	public BorderPane getDatabaseInfoPane() {
		return databaseInfoPane;
	}

	public BorderPane getDatabaseSearchPane() {
		return databaseSearchPane;
	}

	// Dont rename
	public TextField getvehplatefield2() {
		return vehplatefield2;
	}

	// Dont rename
	public TextField getvehmodelfield() {
		return vehmodelfield;
	}

	// Dont rename
	public TextField getvehstolenfield() {
		return vehstolenfield;
	}

	// Dont rename
	public TextField getvehpolicefield() {
		return vehpolicefield;
	}

	// Dont rename
	public ComboBox getvehtypecombobox() {
		return vehtypecombobox;
	}

	// Dont rename
	public TextField getvehownerfield() {
		return vehownerfield;
	}

	// Dont rename
	public TextField getvehregstatusfield() {
		return vehRegStatusField;
	}

	// Dont rename
	public TextField getvehinsstatusfield() {
		return vehInsStatusField;
	}

	// Dont rename
	public TextField getvehinspectionfield() {
		return vehinspectionfield;
	}

	// Dont rename
	public TextField getvehvinfield() {
		return vehvinfield;
	}

	// Dont rename
	public TextField getvehregnumberfield() {
		return vehRegNumberField;
	}

	// Dont rename
	public TextField getvehinsnumberfield() {
		return vehInsNumberField;
	}

	// Dont rename
	public TextField getvehregexpfield() {
		return vehRegExpField;
	}

	// Dont rename
	public TextField getvehinsexpfield() {
		return vehInsExpField;
	}

	// Dont rename
	public TextField getvehinscoveragefield() {
		return vehInsCoverageField;
	}

	public TextField getvehmakefield() {
		return vehmakefield;
	}
}
