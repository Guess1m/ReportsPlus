package com.Guess.ReportsPlus.Windows.Server;

import static com.Guess.ReportsPlus.Desktop.mainDesktopController.vehLookupAppObj;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.Windows.Apps.VehLookupViewController.vehLookupViewController;
import static com.Guess.ReportsPlus.Windows.Server.CurrentIDViewController.defaultPedImagePath;
import static com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReportUtils.newTrafficStop;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logDebug;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getServerDataFolderPath;
import static com.Guess.ReportsPlus.util.Server.recordUtils.grabTrafficStop;
import static com.Guess.ReportsPlus.util.Strings.URLStrings.vehImageFolderURL;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Windows.Apps.VehLookupViewController;
import com.Guess.ReportsPlus.Windows.Settings.settingsController;
import com.Guess.ReportsPlus.config.ConfigReader;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class trafficStopController {
	public static trafficStopController trafficStopController;
	@javafx.fxml.FXML
	private TextField insurance;
	@javafx.fxml.FXML
	private TextField area;
	@javafx.fxml.FXML
	private TextField owner;
	@javafx.fxml.FXML
	private TextField stolen;
	@javafx.fxml.FXML
	private AnchorPane color;
	@javafx.fxml.FXML
	private TextField street;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private TextField model;
	@javafx.fxml.FXML
	private TextField registration;
	@javafx.fxml.FXML
	private TextField platenum;
	@javafx.fxml.FXML
	private Label noColorFoundlbl;
	@javafx.fxml.FXML
	private Label header;
	@javafx.fxml.FXML
	private Label col;
	@javafx.fxml.FXML
	private Label mod;
	@javafx.fxml.FXML
	private Label own;
	@javafx.fxml.FXML
	private Label ins;
	@javafx.fxml.FXML
	private Label str;
	@javafx.fxml.FXML
	private Label ar;
	@javafx.fxml.FXML
	private Label stln;
	@javafx.fxml.FXML
	private Label reg;
	@javafx.fxml.FXML
	private Button searchDMVButton;
	@javafx.fxml.FXML
	private Label plt;
	@javafx.fxml.FXML
	private Button createTrafficStopBtn;
	@javafx.fxml.FXML
	private ImageView modelImageView;
	@javafx.fxml.FXML
	private Label noImageFoundLbl;

	public void initialize() {
		noColorFoundlbl.setVisible(true);
		color.setStyle("-fx-background-color: white; -fx-border-color: black;");
		try {
			updateTrafficStopFields();
		} catch (IOException e) {
			System.err.println("Could not update traffic stop fields");
		}
		addLocalization();
	}

	private void addLocalization() {
		header.setText(localization.getLocalizedMessage("TrafficStopWindow.MainHeader", "Traffic Stop Data:"));
		plt.setText(localization.getLocalizedMessage("TrafficStopWindow.PlateNumberLabel", "Plate Number:"));
		mod.setText(localization.getLocalizedMessage("TrafficStopWindow.ModelLabel", "Vehicle Model:"));
		col.setText(localization.getLocalizedMessage("TrafficStopWindow.ColorLabel", "Vehicle Color"));
		noColorFoundlbl.setText(localization.getLocalizedMessage("TrafficStopWindow.NoColorFound", "No Color Found"));
		stln.setText(localization.getLocalizedMessage("TrafficStopWindow.IsStolenLabel", "Stolen Vehicle:"));
		reg.setText(localization.getLocalizedMessage("TrafficStopWindow.RegistrationLabel", "Registration Status:"));
		ins.setText(localization.getLocalizedMessage("TrafficStopWindow.InsuranceLabel", "Insurance Status:"));
		str.setText(localization.getLocalizedMessage("TrafficStopWindow.StreetLabel", "Traffic Stop Street:"));
		ar.setText(localization.getLocalizedMessage("TrafficStopWindow.AreaLabel", "Traffic Stop Area:"));
		own.setText(localization.getLocalizedMessage("TrafficStopWindow.OwnerLabel", "Registered Owner:"));
		searchDMVButton.setText(
				localization.getLocalizedMessage("TrafficStopWindow.SearchPlateButton", "Search D.M.V. Lookup"));
		createTrafficStopBtn.setText(localization.getLocalizedMessage("TrafficStopWindow.CreateTrafficStopButton",
				"New Traffic Stop Report"));
	}

	public void updateTrafficStopFields() throws IOException {
		String filePath = getServerDataFolderPath() + "ServerTrafficStop.data";
		Map<String, String> vehData = grabTrafficStop(filePath);
		String licensePlate = vehData.getOrDefault("licenseplate", "no value provided");
		String model = vehData.getOrDefault("model", "no value provided");
		String isStolen = vehData.getOrDefault("isstolen", "no value provided");
		String registration = vehData.getOrDefault("registration", "no value provided");
		String insurance = vehData.getOrDefault("insurance", "no value provided");
		String colorValue = vehData.getOrDefault("color", "no value provided");
		String owner = vehData.getOrDefault("owner", "no value provided");
		String street = vehData.getOrDefault("street", "no value provided");
		String area = vehData.getOrDefault("area", "no value provided");
		String[] rgb = colorValue.split("-");
		String color = "Not Found";
		if (rgb.length == 3) {
			color = "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")";
			getColor().setStyle("-fx-background-color: " + color + "; -fx-border-color: black;");
			getNoColorFoundlbl().setVisible(false);
		} else {
			getNoColorFoundlbl().setVisible(true);
			getColor().setStyle("-fx-background-color: white; -fx-border-color: black;");
		}
		setStyleBasedOnCondition(registration, getRegistration());
		setStyleBasedOnCondition(insurance, getInsurance());
		setStyleBasedOnCondition(isStolen, getStolen());
		getPlatenum().setText(licensePlate);
		getModel().setText(model);
		getRegistration().setText(registration);
		getInsurance().setText(insurance);
		getStolen().setText(isStolen);
		getOwner().setText(owner);
		getStreet().setText(street);
		getArea().setText(area);
		if (model != null && !model.equalsIgnoreCase("Not Found") && !model.equalsIgnoreCase("no value provided")) {
			File pedImgFolder = new File(vehImageFolderURL);
			if (pedImgFolder.exists()) {
				logDebug("TrafficStopController; Detected vehImage folder..");
				try {
					if (ConfigReader.configRead("uiSettings", "enablePedVehImages").equalsIgnoreCase("true")) {
						File[] matchingFiles = pedImgFolder
								.listFiles((dir, name) -> name.equalsIgnoreCase(model + ".jpg"));
						if (matchingFiles != null && matchingFiles.length > 0) {
							File matchingFile = matchingFiles[0];
							logInfo("TrafficStopController; Matching vehImage found: " + matchingFile.getName());
							try {
								String fileURI = matchingFile.toURI().toString();
								modelImageView.setImage(new Image(fileURI));
								noImageFoundLbl.setVisible(true);
								noImageFoundLbl.setText("Vehicle Model Found On File:");
							} catch (Exception e) {
								Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
								modelImageView.setImage(defImage);
								noImageFoundLbl.setVisible(true);
								noImageFoundLbl.setText(localization.getLocalizedMessage(
										"VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
								logError("TrafficStopController; Could not set vehImage: ", e);
							}
						} else {
							logWarn("TrafficStopController; No matching vehImage found for the model: " + model
									+ ", displaying no vehImage found.");
							Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
							modelImageView.setImage(defImage);
							noImageFoundLbl.setVisible(true);
							noImageFoundLbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
									"No Image Found In System"));
						}
					} else {
						logWarn("TrafficStopController; enablePedVehImages is disabled in settings so not displaying veh image");
						Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
						modelImageView.setImage(defImage);
						noImageFoundLbl.setVisible(true);
						noImageFoundLbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
								"No Image Found In System"));
					}
				} catch (IOException e) {
					logError("TrafficStopController; Error getting configValue for playLookupWarning: ", e);
				}
			} else {
				Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
				modelImageView.setImage(defImage);
				noImageFoundLbl.setVisible(true);
				noImageFoundLbl.setText(localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl",
						"No Image Found In System"));
			}
		} else {
			Image defImage = new Image(Launcher.class.getResourceAsStream(defaultPedImagePath));
			modelImageView.setImage(defImage);
			noImageFoundLbl.setVisible(true);
			noImageFoundLbl.setText(
					localization.getLocalizedMessage("VehicleLookup.NoVehImageFoundlbl", "No Image Found In System"));
		}
	}

	private void setStyleBasedOnCondition(String condition, TextInputControl control) {
		if (condition.equalsIgnoreCase("expired") || condition.equalsIgnoreCase("suspended")
				|| condition.equalsIgnoreCase("none") || condition.equalsIgnoreCase("true")) {
			control.setStyle("-fx-text-fill: red !important;");
		} else {
			control.setStyle("-fx-text-fill: rgb(140, 140, 140) !important;");
		}
	}

	public Label getNoColorFoundlbl() {
		return noColorFoundlbl;
	}

	public TextField getPlatenum() {
		return platenum;
	}

	public TextField getRegistration() {
		return registration;
	}

	public TextField getModel() {
		return model;
	}

	public BorderPane getRoot() {
		return root;
	}

	public TextField getStreet() {
		return street;
	}

	public AnchorPane getColor() {
		return color;
	}

	public TextField getStolen() {
		return stolen;
	}

	public TextField getOwner() {
		return owner;
	}

	public TextField getArea() {
		return area;
	}

	public TextField getInsurance() {
		return insurance;
	}

	@javafx.fxml.FXML
	public void searchDMV(ActionEvent actionEvent) throws IOException {
		CustomWindow vehWindow = WindowManager.getWindow("Vehicle Lookup");
		if (vehWindow == null) {
			CustomWindow vehApp = WindowManager.createCustomWindow(mainDesktopControllerObj.getDesktopContainer(),
					"Windows/Apps/lookup-veh-view.fxml", "Vehicle Lookup", true, 1, true, false,
					mainDesktopControllerObj.getTaskBarApps(), vehLookupAppObj.getImage());
			if (vehApp != null && vehApp.controller != null) {
				vehLookupViewController = (VehLookupViewController) vehApp.controller;
			}
			try {
				settingsController.loadTheme();
			} catch (IOException e) {
				logError("Error loading theme from searchDMV", e);
			}
			vehWindow = vehApp;
		}
		if (vehWindow != null) {
			if (vehWindow.isMinimized) {
				vehWindow.restoreWindow(vehWindow.title);
			}
			if (vehLookupViewController != null) {
				vehLookupViewController.getDatabaseInfoPane().setVisible(false);
				vehLookupViewController.getDatabaseSearchPane().setVisible(true);
				vehLookupViewController.getVehSearchField().setText(platenum.getText());
				vehWindow.bringToFront();
				logDebug("Bringing up veh search for: " + platenum.getText() +
						" from trafficstop");
			}
		}
	}

	@javafx.fxml.FXML
	public void createTrafficStop(ActionEvent actionEvent) {
		Map<String, Object> trafficStopReportObj = newTrafficStop();
		Map<String, Object> trafficStopReportMap = (Map<String, Object>) trafficStopReportObj
				.get(localization.getLocalizedMessage("ReportWindows.TrafficStopReportTitle", "Traffic Stop Report")
						+ " Map");
		TextField offenderNamets = (TextField) trafficStopReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
		TextField plateNumberts = (TextField) trafficStopReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
		TextField modelts = (TextField) trafficStopReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
		ComboBox areats = (ComboBox) trafficStopReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
		ComboBox streetts = (ComboBox) trafficStopReportMap
				.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
		String filePath = getServerDataFolderPath() + "ServerTrafficStop.data";
		Map<String, String> vehData;
		try {
			vehData = grabTrafficStop(filePath);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String licensePlate = vehData.getOrDefault("licenseplate", "no value provided");
		String model = vehData.getOrDefault("model", "no value provided");
		String owner = vehData.getOrDefault("owner", "no value provided");
		String street = vehData.getOrDefault("street", "no value provided");
		String area = vehData.getOrDefault("area", "no value provided");
		plateNumberts.setText(licensePlate);
		areats.setValue(area);
		streetts.setValue(street);
		offenderNamets.setText(owner);
		modelts.setText(model);
	}
}
