package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Windows.Settings.settingsController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.createCustomWindow;
import static com.Guess.ReportsPlus.Desktop.mainDesktopController.vehLookupAppObj;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.MainApplication.mainRT;
import static com.Guess.ReportsPlus.Windows.Apps.VehLookupViewController.vehLookupViewController;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UIDarkColor;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UILightColor;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.*;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationWarning;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.toHexString;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.serverALPRFileURL;

public class ALPRViewController {
	private static final DateTimeFormatter TIMESTAMP_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd'T'HH:mm:ss").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).appendOffset("+HH:mm", "Z").toFormatter();
	Image defImage = new Image(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/LicensePlate.png"));
	public static ALPRViewController alprViewController;
	private static ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
	private static Set<String> alertedPlates;
	private static Set<String> vehicleIdentifiers;
	
	@FXML
	private BorderPane root;
	@FXML
	private Label licensePlateNumbers;
	@FXML
	private Label flagsSubLabel;
	@FXML
	private VBox flagsBox;
	@FXML
	private TextField plateTypeField;
	@FXML
	private TextField timeScannedField;
	@FXML
	private TextField speedField;
	@FXML
	private TextField distanceField;
	@FXML
	private ListView plateListView;
	@FXML
	private TextField scannerUsedField;
	@FXML
	private Button searchDMVButton;
	@FXML
	private Label timestampSubLabel;
	@FXML
	private Label scannerInfoSubLabel;
	@FXML
	private Label speedSubLabel;
	@FXML
	private Label distanceSubLabel;
	@FXML
	private Label scannerUsedSubLabel;
	@FXML
	private Label scannedPlatesSubLabel;
	@FXML
	private Label plateTypeSubLabel;
	@FXML
	private Button clearButton;
	@FXML
	private ImageView licensePlateImageView;
	@FXML
	private Button settingsBtn;
	
	public static void loadData() {
		if (alprViewController == null) {
			return;
		}
		File alprFile = new File(serverALPRFileURL);
		if (!alprFile.exists()) {
			return;
		}
		Platform.runLater(() -> {
			try {
				String data = Files.readString(Paths.get(serverALPRFileURL));
				if (data == null || data.isEmpty()) {
					return;
				}
				List<Vehicle> parsed = parseVehicleData(data);
				parsed.forEach(vehicle -> alprViewController.addVehicle(vehicle));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
	public static List<Vehicle> parseVehicleData(String input) {
		List<Vehicle> newVehicles = new ArrayList<>();
		String[] entries = input.split("\\|");
		
		for (String entry : entries) {
			if (entry.trim().isEmpty()) {
				continue;
			}
			
			Vehicle vehicle = parseVehicleEntry(entry);
			if (vehicle == null || !vehicle.isValid()) {
				logWarn("Skipping invalid vehicle entry: " + entry);
				continue;
			}
			
			newVehicles.add(vehicle);
		}
		return newVehicles;
	}
	
	private static Vehicle parseVehicleEntry(String entry) {
		Vehicle vehicle = new Vehicle();
		Arrays.stream(entry.split("&")).filter(pair -> pair.contains("=")).forEach(pair -> processKeyValuePair(pair, vehicle));
		
		if (!vehicle.isValid()) {
			logWarn("Skipping invalid vehicle entry: " + entry);
			return null;
		}
		
		return vehicle;
	}
	
	private static void clearData() {
		if (alprViewController == null) {
			return;
		}
		Platform.runLater(() -> {
			var nodesToDelete = new ArrayList<Node>();
			for (Node node : alprViewController.getFlagsBox().getChildren()) {
				if (node instanceof BorderPane) {
					nodesToDelete.add(node);
				}
			}
			alprViewController.getFlagsBox().getChildren().removeAll(nodesToDelete);
			alprViewController.getLicensePlateNumbers().setText("");
			alprViewController.getPlateTypeField().setText("");
			alprViewController.getSpeedField().setText("");
			alprViewController.getDistanceField().setText("");
			alprViewController.getScannerUsedField().setText("");
			alprViewController.getTimeScannedField().setText("");
		});
	}
	
	private static void processKeyValuePair(String pair, Vehicle vehicle) {
		String[] parts = pair.split("=", 2);
		if (parts.length != 2) {
			return;
		}
		
		String key = parts[0];
		String value = parts[1];
		
		switch (key) {
			case "licenseplate":
				vehicle.licenseplate = value;
				break;
			case "plateType":
				vehicle.plateType = value;
				break;
			case "speed":
				vehicle.speed = parseDouble(value);
				break;
			case "distance":
				vehicle.distance = parseDouble(value);
				break;
			case "scanner":
				vehicle.scanner = value;
				break;
			case "flags":
				vehicle.flags = parseFlags(value);
				break;
			case "timescanned":
				vehicle.timescanned = parseTimestamp(value);
				break;
		}
	}
	
	private static double parseDouble(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}
	
	private static List<String> parseFlags(String value) {
		if (value == null || value.trim().isEmpty()) {
			return new ArrayList<>();
		}
		return Arrays.stream(value.split("\\R")).map(String::trim).filter(flag -> !flag.isEmpty()).collect(Collectors.toList());
	}
	
	private static ZonedDateTime parseTimestamp(String value) {
		try {
			return ZonedDateTime.parse(value, TIMESTAMP_FORMATTER);
		} catch (Exception e) {
			return ZonedDateTime.now();
		}
	}
	
	public void initialize() {
		alprViewController = this;
		
		updateLicensePlateImage();
		
		alertedPlates = new HashSet<>();
		vehicleIdentifiers = Collections.synchronizedSet(new HashSet<>());
		
		plateListView.setCellFactory(lv -> new VehicleListCell());
		plateListView.setItems(vehicleList);
		plateListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			updateFields((Vehicle) newVal);
		});
		
		loadData();
		loadLocale();
	}
	
	private void loadLocale() {
		timestampSubLabel.setText(localization.getLocalizedMessage("ALPR.timestampSubLabel", "Timestamp:"));
		scannerInfoSubLabel.setText(localization.getLocalizedMessage("ALPR.scannerInfoSubLabel", "ALPR Information:"));
		speedSubLabel.setText(localization.getLocalizedMessage("ALPR.speedSubLabel", "Speed:"));
		distanceSubLabel.setText(localization.getLocalizedMessage("ALPR.distanceSubLabel", "Distance from Scanner:"));
		scannerUsedSubLabel.setText(localization.getLocalizedMessage("ALPR.scannerUsedSubLabel", "Scanner Used:"));
		scannedPlatesSubLabel.setText(localization.getLocalizedMessage("ALPR.scannedPlatesSubLabel", "Scanned Plates:"));
		plateTypeSubLabel.setText(localization.getLocalizedMessage("ALPR.scannedPlateSubLabel", "Plate Type:"));
		flagsSubLabel.setText(localization.getLocalizedMessage("ALPR.flagsSubLabel", "Vehicle Flags:"));
		searchDMVButton.setText(localization.getLocalizedMessage("TrafficStopWindow.SearchPlateButton", "Search D.M.V. Lookup"));
		clearButton.setText(localization.getLocalizedMessage("ALPR.clearButton", "Clear Old"));
		settingsBtn.setText(localization.getLocalizedMessage("Settings.MainHeader", "Settings"));
	}
	
	public void addVehicle(Vehicle vehicle) {
		if (vehicle == null || !vehicle.isValid()) {
			logWarn("Skipping invalid vehicle: One or more fields are null or empty");
			return;
		}
		
		String vehicleId = vehicle.licenseplate + "|" + vehicle.timescanned.format(DateTimeFormatter.ISO_INSTANT);
		
		Platform.runLater(() -> {
			synchronized (vehicleIdentifiers) {
				if (vehicleIdentifiers.contains(vehicleId)) {
					return;
				}
				vehicleIdentifiers.add(vehicleId);
			}
			
			vehicleList.add(0, vehicle);
			
			if (!vehicle.flags.isEmpty() && !alertedPlates.contains(vehicle.licenseplate)) {
				showNotificationWarning("ALPR Alert [" + vehicle.licenseplate + "]", "Flag(s) detected: " + vehicle.flags);
				if (alprViewController != null) {
					try {
						if (ConfigReader.configRead("soundSettings", "playLookupWarning").equalsIgnoreCase("true")) {
							playSound(getJarPath() + "/sounds/alert-wanted.wav");
						}
					} catch (IOException e) {
						logError("Error getting configValue for playLookupWarning: ", e);
					}
				}
				alertedPlates.add(vehicle.licenseplate);
			}
		});
	}
	
	private void updateFields(Vehicle vehicle) {
		if (vehicle == null) {
			clearData();
			return;
		}
		var nodesToDelete = new ArrayList<Node>();
		for (Node node : alprViewController.getFlagsBox().getChildren()) {
			if (node instanceof BorderPane) {
				nodesToDelete.add(node);
			}
		}
		alprViewController.getFlagsBox().getChildren().removeAll(nodesToDelete);
		
		licensePlateNumbers.setText(vehicle.licenseplate);
		plateTypeField.setText(vehicle.plateType);
		speedField.setText(String.format("%.2f", vehicle.speed));
		distanceField.setText(String.format("%.1f", vehicle.distance));
		scannerUsedField.setText(vehicle.scanner);
		timeScannedField.setText(vehicle.timescanned.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		vehicle.flags.forEach(flag -> {
			Color color = determineFlagColor(flag);
			flagsBox.getChildren().add(Flag(color, flag));
		});
		if (vehicle.flags.isEmpty()) {
			flagsBox.getChildren().add(Flag(Color.rgb(200, 200, 200), "No flags"));
		}
	}
	
	private Color determineFlagColor(String flag) {
		if (flag.contains("Stolen") || flag.contains("No")) {
			return Color.rgb(255, 104, 99);
		}
		if (flag.contains("Expired")) {
			return Color.rgb(255, 130, 76);
		}
		return Color.rgb(200, 200, 200);
	}
	
	@FXML
	public void searchDMVButtonClick(ActionEvent actionEvent) {
		if (licensePlateNumbers.getText() == null || licensePlateNumbers.getText().isEmpty()) {
			return;
		}
		CustomWindow vehWindow = WindowManager.getWindow("Vehicle Lookup");
		if (vehWindow == null) {
			CustomWindow vehApp = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Apps/lookup-veh-view.fxml", "Vehicle Lookup", true, 1, true, false, mainDesktopControllerObj.getTaskBarApps(), vehLookupAppObj.getImage());
			if (vehApp != null && vehApp.controller != null) {
				vehLookupViewController = (VehLookupViewController) vehApp.controller;
			}
			try {
				settingsController.loadTheme();
			} catch (IOException e) {
				logError("Error loading theme from alpr", e);
			}
			vehWindow = vehApp;
		}
		if (vehWindow != null) {
			if (vehWindow.isMinimized) {
				vehWindow.restoreWindow(vehWindow.title);
			}
			if (vehLookupViewController != null) {
				vehLookupViewController.getVehSearchField().getEditor().setText(licensePlateNumbers.getText());
				try {
					vehLookupViewController.onVehSearchBtnClick(new ActionEvent());
					vehWindow.bringToFront();
					logDebug("Bringing up veh search for: " + licensePlateNumbers.getText() + " from alpr");
				} catch (IOException e) {
					logError("Error searching plate from alpr window, plate: " + licensePlateNumbers.getText(), e);
				}
			}
		}
	}
	
	@FXML
	public void clearButtonClick(ActionEvent actionEvent) {
		plateListView.getSelectionModel().clearSelection();
		vehicleList.clear();
		alertedPlates.clear();
		vehicleIdentifiers.clear();
		clearData();
		
		File alprFile = new File(serverALPRFileURL);
		if (alprFile.exists()) {
			try {
				Files.write(Paths.get(serverALPRFileURL), new byte[0]);
			} catch (IOException e) {
				logError("Error clearing alpr data file:", e);
			}
		}
		
		showNotificationInfo("ALPR Alert", "Successfully Cleared ALPR Data");
	}
	
	private BorderPane Flag(Color color, String flagText) {
		BorderPane flag = new BorderPane();
		Label flagLable = new Label(flagText);
		flag.setCenter(flagLable);
		flagLable.setStyle("-fx-font-family: 'Inter 28pt Medium';  -fx-text-fill: white;");
		flag.setStyle("-fx-background-color: " + toHexString(color) + "; -fx-background-radius: 7px; -fx-padding: 3px; -fx-opacity: 0.7;");
		flag.setMinWidth(Region.USE_PREF_SIZE);
		return flag;
	}
	
	public VBox getFlagsBox() {
		return flagsBox;
	}
	
	public TextField getDistanceField() {
		return distanceField;
	}
	
	public TextField getSpeedField() {
		return speedField;
	}
	
	public TextField getPlateTypeField() {
		return plateTypeField;
	}
	
	public TextField getScannerUsedField() {
		return scannerUsedField;
	}
	
	public TextField getTimeScannedField() {
		return timeScannedField;
	}
	
	public Label getLicensePlateNumbers() {
		return licensePlateNumbers;
	}
	
	public BorderPane getRoot() {
		return root;
	}
	
	public Label getDistanceSubLabel() {
		return distanceSubLabel;
	}
	
	public Label getFlagsSubLabel() {
		return flagsSubLabel;
	}
	
	public Label getSpeedSubLabel() {
		return speedSubLabel;
	}
	
	public Label getTimestampSubLabel() {
		return timestampSubLabel;
	}
	
	public Label getPlateTypeSubLabel() {
		return plateTypeSubLabel;
	}
	
	public Label getScannedPlatesSubLabel() {
		return scannedPlatesSubLabel;
	}
	
	public Label getScannerInfoSubLabel() {
		return scannerInfoSubLabel;
	}
	
	public Label getScannerUsedSubLabel() {
		return scannerUsedSubLabel;
	}
	
	public Button getSearchDMVButton() {
		return searchDMVButton;
	}
	
	public Button getClearButton() {
		return clearButton;
	}
	
	@FXML
	public void settingsBtnPress(ActionEvent actionEvent) {
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(3));
		
		CheckBox useDefaultCheckBox = new CheckBox("Use Default Image");
		
		Label filePathLabel = new Label("Image Path:");
		TextField filePathField = new TextField();
		filePathField.setPromptText("Select an image file");
		filePathField.setEditable(false);
		filePathField.setDisable(true);
		HBox.setHgrow(filePathField, Priority.ALWAYS);
		
		Button browseButton = new Button("Browse...");
		browseButton.setDisable(true);
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Image File");
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"), new FileChooser.ExtensionFilter("All Files", "*.*"));
		
		HBox filePathBox = new HBox(15);
		filePathBox.setAlignment(Pos.CENTER_LEFT);
		filePathBox.getChildren().addAll(filePathLabel, filePathField, browseButton);
		
		VBox centerContent = new VBox(20);
		centerContent.setPadding(new Insets(15));
		centerContent.setAlignment(Pos.CENTER_LEFT);
		centerContent.getChildren().addAll(useDefaultCheckBox, filePathBox);
		root.setCenter(centerContent);
		
		Button saveButton = new Button("Save");
		HBox buttonBox = new HBox();
		buttonBox.setAlignment(Pos.CENTER_RIGHT);
		buttonBox.setPadding(new Insets(10, 0, 0, 0));
		buttonBox.getChildren().add(saveButton);
		root.setBottom(buttonBox);
		
		boolean useDefault = true;
		String savedImagePath = "";
		try {
			String useDefaultStr = ConfigReader.configRead("alprSettings", "useDefaultImage");
			if (useDefaultStr != null) {
				useDefault = Boolean.parseBoolean(useDefaultStr);
			}
			if (!useDefault) {
				savedImagePath = ConfigReader.configRead("alprSettings", "licensePlateImagePath");
			}
		} catch (Exception e) {
			logError("ALPR; Error reading configValue for useDefaultImage or parsing boolean. Defaulting to use default image.", e);
			useDefault = true;
		}
		useDefaultCheckBox.setSelected(useDefault);
		
		filePathField.setDisable(useDefault);
		browseButton.setDisable(useDefault);
		if (useDefault) {
			filePathField.clear();
			filePathField.setPromptText("Using default image");
		} else {
			filePathField.setText(savedImagePath);
			filePathField.setPromptText("Select an image file");
		}
		
		root.setPrefHeight(175);
		root.setPrefWidth(400);
		
		useDefaultCheckBox.setOnAction(event -> {
			boolean useDefault2 = useDefaultCheckBox.isSelected();
			filePathField.setDisable(useDefault2);
			browseButton.setDisable(useDefault2);
			if (useDefault2) {
				filePathField.clear();
				filePathField.setPromptText("Using default image");
			} else {
				filePathField.setPromptText("Select an image file");
			}
		});
		
		browseButton.setOnAction(event -> {
			File selectedFile = fileChooser.showOpenDialog((javafx.stage.Window) mainRT);
			if (selectedFile != null) {
				filePathField.setText(selectedFile.getAbsolutePath());
			}
		});
		CustomWindow window = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), root, "ALPR Settings", true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(),
		                                         new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png"))));
		
		saveButton.setOnAction(event -> {
			boolean useDefault2 = useDefaultCheckBox.isSelected();
			String imagePath = filePathField.getText();
			
			logInfo("--- ALPR Settings Saved ---");
			logInfo("Use Default Image: " + useDefault2);
			logInfo("Image Path: " + (useDefault2 ? "Default" : imagePath));
			logInfo("---------------------");
			
			ConfigWriter.configwrite("alprSettings", "useDefaultImage", useDefault2 ? "true" : "false");
			ConfigWriter.configwrite("alprSettings", "licensePlateImagePath", useDefault2 ? "" : imagePath);
			
			updateLicensePlateImage();
			
			if (window != null) {
				window.closeWindow();
			}
		});
	}
	
	private void updateLicensePlateImage() {
		boolean useDefaultImageConfig = true;
		
		try {
			String useDefaultStr = ConfigReader.configRead("alprSettings", "useDefaultImage");
			if (useDefaultStr != null) {
				useDefaultImageConfig = Boolean.parseBoolean(useDefaultStr);
			} else {
			}
		} catch (Exception e) {
			logError("ALPR; Error reading configValue for useDefaultImage or parsing boolean. Defaulting to use default image.", e);
			useDefaultImageConfig = true;
		}
		
		if (useDefaultImageConfig) {
			logDebug("ALPR; Using default image for ALPR license plate (either by configuration or due to error in 'useDefaultImage' config).");
			licensePlateImageView.setImage(defImage);
		} else {
			boolean customImageLoadedSuccessfully = false;
			String filePath;
			
			try {
				filePath = ConfigReader.configRead("alprSettings", "licensePlateImagePath");
				
				if (filePath != null && !filePath.isEmpty()) {
					File file = new File(filePath);
					if (file.exists() && file.isFile()) {
						try {
							licensePlateImageView.setImage(new Image(new FileInputStream(file)));
							logDebug("ALPR; Successfully loaded license plate image from file path [" + filePath + "]");
							customImageLoadedSuccessfully = true;
						} catch (Exception imageLoadEx) {
							logError("ALPR; Failed to load image from existing file [" + filePath + "]. Using default image.", imageLoadEx);
						}
					} else {
						logError("ALPR; Custom image file path [" + (filePath == null ? "null" : filePath) + "] does not exist or is not a file. Using default image.");
					}
				} else {
					logError("ALPR; Custom image file path is null or empty in config. Using default image.");
				}
			} catch (IOException configReadEx) {
				logError("ALPR; IOException while reading 'licensePlateImagePath' from config. Using default image.", configReadEx);
			} catch (Exception ex) {
				logError("ALPR; Unexpected error occurred while trying to load custom license plate image. Using default image.", ex);
			}
			
			if (!customImageLoadedSuccessfully) {
				logDebug("ALPR; Falling back to default image for license plate due to issues with loading the custom image.");
				licensePlateImageView.setImage(defImage);
			}
		}
	}
	
	public static class Vehicle {
		public String licenseplate;
		public String plateType;
		public double speed;
		public double distance;
		public String scanner;
		public List<String> flags = new ArrayList<>();
		public ZonedDateTime timescanned;
		
		public Vehicle() {
			licenseplate = "";
			plateType = "";
			scanner = "";
			timescanned = ZonedDateTime.now();
		}
		
		public boolean isValid() {
			return licenseplate != null && !licenseplate.isEmpty() && plateType != null && !plateType.isEmpty() && scanner != null && !scanner.isEmpty() && timescanned != null && flags != null;
		}
	}
	
	private class VehicleListCell extends ListCell<Vehicle> {
		private final BorderPane container = new BorderPane();
		private final Label plateLabel = new Label();
		private final Label detailsLabel = new Label();
		private final HBox flagContainer = new HBox();
		private final HBox timeBox = new HBox();
		String fontcolor = UIDarkColor;
		
		public VehicleListCell() {
			try {
				if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("false")) {
					fontcolor = UILightColor;
				}
			} catch (IOException e) {
				logError("error loading styles for vehicle list: ", e);
			}
			
			setupLayout();
			setupStyling();
		}
		
		private void setupLayout() {
			VBox leftBox = new VBox(4);
			leftBox.getChildren().addAll(plateLabel, detailsLabel, flagContainer);
			
			timeBox.setAlignment(Pos.CENTER_RIGHT);
			
			container.setLeft(leftBox);
			container.setRight(timeBox);
			container.setPadding(new Insets(5));
		}
		
		private void setupStyling() {
			plateLabel.setStyle("-fx-text-fill: " + fontcolor + "; -fx-font-family: \"Inter 28pt Bold\"; -fx-font-size: 14px;");
			detailsLabel.setStyle("-fx-text-fill: " + fontcolor + "; -fx-opacity: 0.8; -fx-font-family: \"Inter 24pt Regular\"; -fx-font-size: 12px;");
			flagContainer.setSpacing(5);
			container.setStyle("-fx-background-color: transparent; -fx-border-color: #eee; -fx-border-width: 0;");
		}
		
		@Override
		protected void updateItem(Vehicle vehicle, boolean empty) {
			super.updateItem(vehicle, empty);
			if (empty || vehicle == null) {
				setGraphic(null);
			} else {
				plateLabel.setText(vehicle.licenseplate);
				String details = String.format("%.2f mph | %.1f m | %s", vehicle.speed, vehicle.distance, vehicle.scanner);
				detailsLabel.setText(details);
				flagContainer.getChildren().clear();
				if (vehicle.flags != null && !vehicle.flags.isEmpty()) {
					vehicle.flags.forEach(flag -> {
						Label flagLabel = new Label(flag);
						flagLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: white; -fx-padding: 2 5 2 5;");
						flagLabel.setBackground(new Background(new BackgroundFill(determineFlagColor(flag), new CornerRadii(3), Insets.EMPTY)));
						flagContainer.getChildren().add(flagLabel);
					});
				}
				Label timeLabel = new Label(vehicle.timescanned.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
				timeLabel.setStyle(" -fx-text-fill: " + fontcolor + ";-fx-font-family: \"Inter 24pt Regular\"; -fx-font-size: 11px;");
				timeBox.getChildren().setAll(timeLabel);
				
				setGraphic(container);
			}
		}
	}
}
