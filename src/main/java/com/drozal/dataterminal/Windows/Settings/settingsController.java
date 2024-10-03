package com.drozal.dataterminal.Windows.Settings;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.CalloutManager;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NotificationManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopControllerObj;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.createFakeWindow;
import static com.drozal.dataterminal.Windows.Apps.CalloutViewController.calloutViewController;
import static com.drozal.dataterminal.Windows.Apps.CourtViewController.courtViewController;
import static com.drozal.dataterminal.Windows.Apps.LogViewController.logController;
import static com.drozal.dataterminal.Windows.Apps.PedLookupViewController.pedLookupViewController;
import static com.drozal.dataterminal.Windows.Apps.VehLookupViewController.vehLookupViewController;
import static com.drozal.dataterminal.Windows.Misc.UserManagerController.userManagerController;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.server.ClientUtils.isConnected;

public class settingsController {
	
	private static final String UILightColor = "rgb(255,255,255,0.75)";
	private static final String UIDarkColor = "rgb(0,0,0,0.75)";
	private static AtomicReference<String> selectedNotification;
	private boolean isInitialized = false;
	
	//<editor-fold desc="FXML">
	
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private ComboBox reportStyleComboBox;
	@javafx.fxml.FXML
	private ComboBox themeComboBox;
	@javafx.fxml.FXML
	private ColorPicker primPicker;
	@javafx.fxml.FXML
	private ColorPicker secPicker;
	@javafx.fxml.FXML
	private ColorPicker accPicker;
	@javafx.fxml.FXML
	private Label secLabel;
	@javafx.fxml.FXML
	private Label primLabel;
	@javafx.fxml.FXML
	private Label accLabel;
	@javafx.fxml.FXML
	private Button resetDefaultsBtn;
	@javafx.fxml.FXML
	private Button debugLogBtn;
	@javafx.fxml.FXML
	private Button clrLogsBtn;
	@javafx.fxml.FXML
	private Button clrSaveDataBtn;
	@javafx.fxml.FXML
	private Label lbl3;
	@javafx.fxml.FXML
	private ComboBox calloutDurComboBox;
	@javafx.fxml.FXML
	private Label lbl5;
	@javafx.fxml.FXML
	private CheckBox AOTReport;
	@javafx.fxml.FXML
	private CheckBox AOTSettings;
	@javafx.fxml.FXML
	private CheckBox AOTNotes;
	@javafx.fxml.FXML
	private CheckBox AOTCallout;
	@javafx.fxml.FXML
	private CheckBox AOTID;
	@javafx.fxml.FXML
	private CheckBox AOTMap;
	@javafx.fxml.FXML
	private CheckBox AOTDebug;
	@javafx.fxml.FXML
	private CheckBox AOTClient;
	@javafx.fxml.FXML
	private ColorPicker headingPickerReport;
	@javafx.fxml.FXML
	private ColorPicker secPickerReport;
	@javafx.fxml.FXML
	private ColorPicker backgroundPickerReport;
	@javafx.fxml.FXML
	private Label accentLabelReport;
	@javafx.fxml.FXML
	private Label backgroundLabelReport;
	@javafx.fxml.FXML
	private Label secLabelReport;
	@javafx.fxml.FXML
	private ColorPicker accentPickerReport;
	@javafx.fxml.FXML
	private Label lbl6;
	@javafx.fxml.FXML
	private Label headingLabelReport;
	@javafx.fxml.FXML
	private ComboBox presetComboBoxReport;
	@javafx.fxml.FXML
	private ComboBox idDurComboBox;
	@javafx.fxml.FXML
	private Label lbl7;
	@javafx.fxml.FXML
	private ColorPicker bkgPicker;
	@javafx.fxml.FXML
	private Label bkgLabel;
	@javafx.fxml.FXML
	private TabPane tabpane;
	@javafx.fxml.FXML
	private ComboBox textClrComboBox;
	@javafx.fxml.FXML
	private CheckBox serverAutoconnectCheckbox;
	@javafx.fxml.FXML
	private TextField broadcastPortField;
	@javafx.fxml.FXML
	private Label lbl8;
	@javafx.fxml.FXML
	private Button resetReportDefaultsBtn;
	@javafx.fxml.FXML
	private Label tt10;
	@javafx.fxml.FXML
	private Label tt5;
	@javafx.fxml.FXML
	private Label tt4;
	@javafx.fxml.FXML
	private Label tt7;
	@javafx.fxml.FXML
	private Label tt6;
	@javafx.fxml.FXML
	private Label tt9;
	@javafx.fxml.FXML
	private Label tt8;
	@javafx.fxml.FXML
	private Label tt11;
	@javafx.fxml.FXML
	private TextField socketTimeoutField;
	@javafx.fxml.FXML
	private CheckBox saveCalloutLocationCheckbox;
	@javafx.fxml.FXML
	private CheckBox saveIDLocationCheckbox;
	@javafx.fxml.FXML
	private GridPane colorPageTwo;
	@javafx.fxml.FXML
	private GridPane colorPageOne;
	@javafx.fxml.FXML
	private Label lbl9;
	@javafx.fxml.FXML
	private Button colorPageTwoBtn;
	@javafx.fxml.FXML
	private Button colorPageOneBtn;
	@javafx.fxml.FXML
	private Label tt15;
	@javafx.fxml.FXML
	private Label tt16;
	@javafx.fxml.FXML
	private Label tt12;
	@javafx.fxml.FXML
	private Button previewNotificationBtn;
	@javafx.fxml.FXML
	private ColorPicker notiPrimPicker;
	@javafx.fxml.FXML
	private ColorPicker notiTextColorPicker;
	@javafx.fxml.FXML
	private ComboBox notificationComboBox;
	@javafx.fxml.FXML
	private Button resetNotiDefaultsBtn;
	@javafx.fxml.FXML
	private ComboBox notiPosCombobox;
	@javafx.fxml.FXML
	private Label tt17;
	@javafx.fxml.FXML
	private Label tt13;
	@javafx.fxml.FXML
	private Label tt14;
	@javafx.fxml.FXML
	private TextField notiFadeOutDurField;
	@javafx.fxml.FXML
	private TextField notiDisplayDurField;
	@javafx.fxml.FXML
	private Button saveDisplayDurBtn;
	@javafx.fxml.FXML
	private Button saveFadeDurBtn;
	@javafx.fxml.FXML
	private CheckBox saveReportLocationCheckbox;
	@javafx.fxml.FXML
	private CheckBox saveNotesLocationCheckbox;
	@javafx.fxml.FXML
	private CheckBox enableIDPopupsCheckbox;
	@javafx.fxml.FXML
	private CheckBox enableCalloutPopupsCheckbox;
	@javafx.fxml.FXML
	private GridPane windowPageOne;
	@javafx.fxml.FXML
	private GridPane windowPageTwo;
	@javafx.fxml.FXML
	private Button windowPageTwoBtn;
	@javafx.fxml.FXML
	private Button windowPageOneBtn;
	@javafx.fxml.FXML
	private Label lbl10;
	@javafx.fxml.FXML
	private CheckBox enableNotificationsCheckbox;
	@javafx.fxml.FXML
	private CheckBox enableSoundCheckbox;
	@javafx.fxml.FXML
	private Label lbl11;
	@javafx.fxml.FXML
	private CheckBox audioCalloutCheckbox;
	@javafx.fxml.FXML
	private CheckBox audioReportDeleteCheckbox;
	@javafx.fxml.FXML
	private CheckBox audioReportCreate;
	@javafx.fxml.FXML
	private CheckBox audioLookupWarningCheckbox;
	@javafx.fxml.FXML
	private AnchorPane root2;
	
	//</editor-fold>
	
	public static void loadTheme() throws IOException {
		String mainclr = ConfigReader.configRead("uiColors", "mainColor");
		String secclr = ConfigReader.configRead("uiColors", "secondaryColor");
		String bkgclr = ConfigReader.configRead("uiColors", "bkgColor");
		String accclr = ConfigReader.configRead("uiColors", "accentColor");
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor");
		String nonTransparentBtn = "-fx-background-color: " + accclr + ";";
		
		if (userManagerController != null) {
			userManagerController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
		}
		if (vehLookupViewController != null) {
			vehLookupViewController.getLookupmainlblpane().setStyle(
					"-fx-border-color: " + secclr + ";-fx-border-width: 0 0 2 0;");
			vehLookupViewController.getLookupmainlbl().setStyle("-fx-text-fill: " + mainclr + ";-fx-font-size: 25;");
			vehLookupViewController.getLbl1().setStyle("-fx-text-fill: " + secclr + ";-fx-font-size: 25;");
			vehLookupViewController.getLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
			vehLookupViewController.getVehLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
			vehLookupViewController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
			
			vehLookupViewController.getInfo1().setStyle("-fx-background-color: " + mainclr + ";");
			vehLookupViewController.getInfo2().setStyle("-fx-background-color: " + mainclr + ";");
			
			vehLookupViewController.getProbabilitySettingsBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;");
			vehLookupViewController.getProbabilitySettingsBtn().setOnMouseEntered(
					e -> vehLookupViewController.getProbabilitySettingsBtn().setStyle(
							hoverStyle + ";-fx-text-fill: white;"));
			vehLookupViewController.getProbabilitySettingsBtn().setOnMouseExited(
					e -> vehLookupViewController.getProbabilitySettingsBtn().setStyle(
							nonTransparentBtn + "-fx-text-fill: white;"));
			
			vehLookupViewController.getVehSearchBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;");
			vehLookupViewController.getVehSearchBtn().setOnMouseEntered(
					e -> vehLookupViewController.getVehSearchBtn().setStyle(hoverStyle + ";-fx-text-fill: white;"));
			vehLookupViewController.getVehSearchBtn().setOnMouseExited(
					e -> vehLookupViewController.getVehSearchBtn().setStyle(
							nonTransparentBtn + "-fx-text-fill: white;"));
			
			vehLookupViewController.getAddDataToNotesBtn().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			vehLookupViewController.getAddDataToNotesBtn().setOnMouseEntered(
					e -> vehLookupViewController.getAddDataToNotesBtn().setStyle(
							hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			vehLookupViewController.getAddDataToNotesBtn().setOnMouseExited(
					e -> vehLookupViewController.getAddDataToNotesBtn().setStyle(
							nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			vehLookupViewController.getBtninfo1().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			vehLookupViewController.getBtninfo1().setOnMouseEntered(e -> vehLookupViewController.getBtninfo1().setStyle(
					hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			vehLookupViewController.getBtninfo1().setOnMouseExited(e -> vehLookupViewController.getBtninfo1().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			vehLookupViewController.getBtninfo2().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			vehLookupViewController.getBtninfo2().setOnMouseEntered(e -> vehLookupViewController.getBtninfo2().setStyle(
					hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			vehLookupViewController.getBtninfo2().setOnMouseExited(e -> vehLookupViewController.getBtninfo2().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
		}
		if (pedLookupViewController != null) {
			pedLookupViewController.getLookupmainlblpane().setStyle(
					"-fx-border-color: " + secclr + ";-fx-border-width: 0 0 2 0;");
			pedLookupViewController.getLookupmainlbl().setStyle("-fx-text-fill: " + mainclr + ";-fx-font-size: 25;");
			pedLookupViewController.getLbl1().setStyle("-fx-text-fill: " + secclr + ";-fx-font-size: 25;");
			pedLookupViewController.getLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
			pedLookupViewController.getPedLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
			pedLookupViewController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
			
			pedLookupViewController.getInfo1().setStyle("-fx-background-color: " + mainclr + ";");
			pedLookupViewController.getInfo2().setStyle("-fx-background-color: " + mainclr + ";");
			pedLookupViewController.getInfo3().setStyle("-fx-background-color: " + mainclr + ";");
			pedLookupViewController.getInfo4().setStyle("-fx-background-color: " + mainclr + ";");
			pedLookupViewController.getInfo5().setStyle("-fx-background-color: " + mainclr + ";");
			
			pedLookupViewController.getProbabilitySettingsBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;");
			pedLookupViewController.getProbabilitySettingsBtn().setOnMouseEntered(
					e -> pedLookupViewController.getProbabilitySettingsBtn().setStyle(
							hoverStyle + ";-fx-text-fill: white;"));
			pedLookupViewController.getProbabilitySettingsBtn().setOnMouseExited(
					e -> pedLookupViewController.getProbabilitySettingsBtn().setStyle(
							nonTransparentBtn + "-fx-text-fill: white;"));
			
			pedLookupViewController.getPedSearchBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;");
			pedLookupViewController.getPedSearchBtn().setOnMouseEntered(
					e -> pedLookupViewController.getPedSearchBtn().setStyle(hoverStyle + ";-fx-text-fill: white;"));
			pedLookupViewController.getPedSearchBtn().setOnMouseExited(
					e -> pedLookupViewController.getPedSearchBtn().setStyle(
							nonTransparentBtn + "-fx-text-fill: white;"));
			
			pedLookupViewController.getAddDataToNotesBtn().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			pedLookupViewController.getAddDataToNotesBtn().setOnMouseEntered(
					e -> pedLookupViewController.getAddDataToNotesBtn().setStyle(
							hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			pedLookupViewController.getAddDataToNotesBtn().setOnMouseExited(
					e -> pedLookupViewController.getAddDataToNotesBtn().setStyle(
							nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			pedLookupViewController.getInfobtn1().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			pedLookupViewController.getInfobtn1().setOnMouseEntered(e -> pedLookupViewController.getInfobtn1().setStyle(
					hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			pedLookupViewController.getInfobtn1().setOnMouseExited(e -> pedLookupViewController.getInfobtn1().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			pedLookupViewController.getInfobtn2().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			pedLookupViewController.getInfobtn2().setOnMouseEntered(e -> pedLookupViewController.getInfobtn2().setStyle(
					hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			pedLookupViewController.getInfobtn2().setOnMouseExited(e -> pedLookupViewController.getInfobtn2().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			pedLookupViewController.getInfobtn3().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			pedLookupViewController.getInfobtn3().setOnMouseEntered(e -> pedLookupViewController.getInfobtn3().setStyle(
					hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			pedLookupViewController.getInfobtn3().setOnMouseExited(e -> pedLookupViewController.getInfobtn3().setStyle(
					nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
		}
		if (courtViewController != null) {
			courtViewController.getCaseSec1().setStyle("-fx-text-fill: " + secclr + ";");
			courtViewController.getCasePrim1().setStyle(
					"-fx-text-fill: " + mainclr + "; -fx-border-color: black; -fx-border-width: 0 0 1.5 0;");
			courtViewController.getCaseprim1().setStyle("-fx-text-fill: " + mainclr + ";");
			courtViewController.getCaseprim2().setStyle("-fx-text-fill: " + mainclr + ";");
			courtViewController.getCaseprim3().setStyle("-fx-text-fill: " + mainclr + ";");
			courtViewController.getCaseSec2().setStyle("-fx-text-fill: " + secclr + ";");
			courtViewController.getCasesec1().setStyle("-fx-text-fill: " + secclr + ";");
			courtViewController.getCasesec2().setStyle("-fx-text-fill: " + secclr + ";");
			courtViewController.getCasesec3().setStyle("-fx-text-fill: " + secclr + ";");
			courtViewController.getCasesec4().setStyle("-fx-text-fill: " + secclr + ";");
			courtViewController.getCaseSuspensionDurationlbl().setStyle("-fx-text-fill: " + secclr + ";");
			courtViewController.getCourtPane().setStyle("-fx-background-color: " + bkgclr + ";");
			courtViewController.getBlankCourtInfoPane().setStyle("-fx-background-color: " + bkgclr + ";");
			courtViewController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
		}
		if (logController != null) {
			logController.getReportPlusLabelFill().setStyle("-fx-text-fill: " + accclr + ";");
			logController.getBkgclr2().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getTabPane().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getArrestTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getCalloutTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getCitationTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getImpoundTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getIncidentTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getPatrolTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getSearchTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getTrafficStopTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getDeathReportTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getAccidentReportTable().setStyle("-fx-background-color: " + bkgclr + ";");
			logController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
		}
		if (calloutViewController != null) {
			calloutViewController.getCalloutInfoTitle().setStyle("-fx-background-color: " + mainclr + ";");
			calloutViewController.getCalloutPane().setStyle("-fx-background-color: " + bkgclr + ";");
			CalloutManager.loadActiveCallouts(calloutViewController.getCalActiveList());
			CalloutManager.loadHistoryCallouts(calloutViewController.getCalHistoryList());
			
			calloutViewController.getCurrentCalPane().setStyle(
					updateStyleProperty(calloutViewController.getCurrentCalPane(), "-fx-border-color", accclr));
			
			calloutViewController.getCalActiveList().setStyle(
					updateStyleProperty(calloutViewController.getCalActiveList(), "-fx-border-color", accclr));
			calloutViewController.getCalHistoryList().setStyle(
					updateStyleProperty(calloutViewController.getCalHistoryList(), "-fx-border-color", accclr));
			
			calloutViewController.getActivecalfill().setStyle(
					updateStyleProperty(calloutViewController.getActivecalfill(), "-fx-border-color", mainclr));
			calloutViewController.getCalfill().setStyle(
					updateStyleProperty(calloutViewController.getCalfill(), "-fx-border-color", mainclr));
			
			calloutViewController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
			
		}
		if (mainDesktopControllerObj != null) {
			mainDesktopControllerObj.getServerStatusLabel().setStyle("-fx-label-padding: 5; -fx-border-radius: 5;");
			if (isConnected) {
				mainDesktopControllerObj.getServerStatusLabel().setStyle(
						"-fx-text-fill: green; -fx-label-padding: 5; -fx-border-radius: 5;");
			} else {
				mainDesktopControllerObj.getServerStatusLabel().setStyle(
						"-fx-text-fill: #ff5e5e; -fx-label-padding: 5; -fx-border-radius: 5;");
			}
		}
		if (ConfigReader.configRead("uiColors", "UIDarkMode").equals("true")) {
			addDarkStyles();
		} else {
			addLightStyles();
		}
	}
	
	private static void addDarkStyles() {
		if (vehLookupViewController != null) {
			vehLookupViewController.getPlt1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getPlt2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getPlt3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getPlt4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getPlt5().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getPlt6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getPlt7().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getPlt8().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getPlt9().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getPlt10().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			vehLookupViewController.getNoVehImageFoundlbl().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		}
		if (pedLookupViewController != null) {
			pedLookupViewController.getNoPedImageFoundlbl().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed5().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed7().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed8().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed9().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed10().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed11().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed12().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed13().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed14().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed15().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed16().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed17().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed18().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed19().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed20().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed21().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed22().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			pedLookupViewController.getPed23().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		}
		if (courtViewController != null) {
			courtViewController.getCaseNotesField().getStyleClass().clear();
			courtViewController.getCaseNotesField().getStyleClass().add("text-area-dark");
			courtViewController.getNoCourtCaseSelectedlbl().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl7().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl8().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl9().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl10().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl11().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			courtViewController.getCaselbl12().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		}
		if (logController != null) {
			logController.getTabPane().getStyleClass().clear();
			logController.getTabPane().getStyleClass().add("darktabpane");
			logController.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		}
		if (calloutViewController != null) {
			calloutViewController.getCaldetlbl1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			calloutViewController.getCaldetlbl2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			calloutViewController.getCaldetlbl3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			calloutViewController.getCaldetlbl4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			calloutViewController.getCaldetlbl5().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			calloutViewController.getCaldetlbl6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			calloutViewController.getCaldetlbl7().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			calloutViewController.getCaldetlbl8().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			calloutViewController.getCaldetlbl9().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			
			calloutViewController.getActivecalfill().setStyle(
					updateStyleProperty(calloutViewController.getActivecalfill(), "-fx-text-fill", UIDarkColor));
			calloutViewController.getCalfill().setStyle(
					updateStyleProperty(calloutViewController.getCalfill(), "-fx-text-fill", UIDarkColor));
			
		}
	}
	
	private static void addLightStyles() {
		if (vehLookupViewController != null) {
			vehLookupViewController.getPlt1().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getPlt2().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getPlt3().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getPlt4().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getPlt5().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getPlt6().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getPlt7().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getPlt8().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getPlt9().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getPlt10().setStyle("-fx-text-fill: " + UILightColor + ";");
			vehLookupViewController.getNoVehImageFoundlbl().setStyle("-fx-text-fill: " + UILightColor + ";");
		}
		if (pedLookupViewController != null) {
			pedLookupViewController.getNoPedImageFoundlbl().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed1().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed2().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed3().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed4().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed5().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed6().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed7().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed8().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed9().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed10().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed11().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed12().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed13().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed14().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed15().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed16().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed17().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed18().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed19().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed20().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed21().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed22().setStyle("-fx-text-fill: " + UILightColor + ";");
			pedLookupViewController.getPed23().setStyle("-fx-text-fill: " + UILightColor + ";");
		}
		if (courtViewController != null) {
			courtViewController.getCaseNotesField().getStyleClass().clear();
			courtViewController.getCaseNotesField().getStyleClass().add("text-area-light");
			courtViewController.getNoCourtCaseSelectedlbl().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl1().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl2().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl4().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl6().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl7().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl8().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl9().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl10().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl11().setStyle("-fx-text-fill: " + UILightColor + ";");
			courtViewController.getCaselbl12().setStyle("-fx-text-fill: " + UILightColor + ";");
		}
		if (logController != null) {
			logController.getTabPane().getStyleClass().clear();
			logController.getTabPane().getStyleClass().add("lighttabpane");
			logController.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UILightColor + ";");
		}
		if (calloutViewController != null) {
			calloutViewController.getCaldetlbl1().setStyle("-fx-text-fill: " + UILightColor + ";");
			calloutViewController.getCaldetlbl2().setStyle("-fx-text-fill: " + UILightColor + ";");
			calloutViewController.getCaldetlbl3().setStyle("-fx-text-fill: " + UILightColor + ";");
			calloutViewController.getCaldetlbl4().setStyle("-fx-text-fill: " + UILightColor + ";");
			calloutViewController.getCaldetlbl5().setStyle("-fx-text-fill: " + UILightColor + ";");
			calloutViewController.getCaldetlbl6().setStyle("-fx-text-fill: " + UILightColor + ";");
			calloutViewController.getCaldetlbl7().setStyle("-fx-text-fill: " + UILightColor + ";");
			calloutViewController.getCaldetlbl8().setStyle("-fx-text-fill: " + UILightColor + ";");
			calloutViewController.getCaldetlbl9().setStyle("-fx-text-fill: " + UILightColor + ";");
			
			calloutViewController.getActivecalfill().setStyle(
					updateStyleProperty(calloutViewController.getActivecalfill(), "-fx-text-fill", UILightColor));
			calloutViewController.getCalfill().setStyle(
					updateStyleProperty(calloutViewController.getCalfill(), "-fx-text-fill", UILightColor));
			
		}
	}
	
	public void initialize() {
		try {
			addActionEventsAndComboBoxes();
		} catch (IOException e) {
			logError("Error Loading Action Events: ", e);
		}
		addEventFilters();
		setupListeners();
		try {
			addDefaultCheckboxSelections();
		} catch (IOException e) {
			logError("Error Loading Default Checkbox Values: ", e);
		}
		try {
			loadTheme();
		} catch (IOException e) {
			logError("Error Loading Theme From Init: ", e);
		}
		loadColors();
		addTooltips();
		
		colorPageOne.setVisible(true);
		colorPageTwo.setVisible(false);
		windowPageOne.setVisible(true);
		windowPageTwo.setVisible(false);
		
		Platform.runLater(() -> {
			Stage stage = (Stage) root.getScene().getWindow();
			stage.setMinWidth(stage.getWidth());
			stage.setMinHeight(stage.getHeight());
			
			previewNotificationBtn.setOnAction(actionEvent -> {
				if (selectedNotification.get().equals("Information")) {
					NotificationManager.showNotificationInfo("Sample Info Notification",
					                                         "Lorum ipsum dolor sit amet, consectetur adipiscing elit.");
				}
				if (selectedNotification.get().equals("Warning")) {
					NotificationManager.showNotificationWarning("Sample Warning Notification",
					                                            "Lorum ipsum dolor sit amet, consectetur adipiscing elit.");
				}
			});
		});
		
		isInitialized = true;
	}
	
	private void loadColors() {
		try {
			Color primary = Color.valueOf(ConfigReader.configRead("uiColors", "mainColor"));
			Color secondary = Color.valueOf(ConfigReader.configRead("uiColors", "secondaryColor"));
			Color accent = Color.valueOf(ConfigReader.configRead("uiColors", "accentColor"));
			Color bkg = Color.valueOf(ConfigReader.configRead("uiColors", "bkgColor"));
			
			Color reportBackground = Color.valueOf(ConfigReader.configRead("reportSettings", "reportBackground"));
			Color reportSecondary = Color.valueOf(ConfigReader.configRead("reportSettings", "reportSecondary"));
			Color reportAccent = Color.valueOf(ConfigReader.configRead("reportSettings", "reportAccent"));
			Color reportHeading = Color.valueOf(ConfigReader.configRead("reportSettings", "reportHeading"));
			
			primPicker.setValue(primary);
			secPicker.setValue(secondary);
			accPicker.setValue(accent);
			bkgPicker.setValue(bkg);
			primLabel.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			secLabel.setStyle("-fx-text-fill: " + toHexString(secondary) + ";");
			accLabel.setStyle("-fx-text-fill: " + toHexString(accent) + ";");
			
			if (toHexString(bkg).equalsIgnoreCase("#ffffff") || toHexString(bkg).equalsIgnoreCase(
					"#f2f2f2") || toHexString(bkg).equalsIgnoreCase("#e6e6e6") || toHexString(bkg).equalsIgnoreCase(
					"#cccccc")) {
				bkgLabel.setStyle("-fx-text-fill: black;");
			} else {
				bkgLabel.setStyle("-fx-text-fill: " + toHexString(bkg) + ";");
			}
			
			tabpane.setStyle("-fx-background-color: " + toHexString(bkg));
			
			lbl3.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl5.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl6.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl7.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl8.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl9.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl10.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			
			backgroundPickerReport.setValue(reportBackground);
			accentPickerReport.setValue(reportAccent);
			headingPickerReport.setValue(reportHeading);
			secPickerReport.setValue(reportSecondary);
			
			if (selectedNotification.get().equals("Information")) {
				notiTextColorPicker.setValue(
						Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationInfoTextColor")));
				notiPrimPicker.setValue(
						Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationInfoPrimary")));
			} else {
				notiTextColorPicker.setValue(
						Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationWarnTextColor")));
				notiPrimPicker.setValue(
						Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationWarnPrimary")));
			}
			
			backgroundLabelReport.setStyle("-fx-text-fill: " + toHexString(reportBackground) + ";");
			accentLabelReport.setStyle("-fx-text-fill: " + toHexString(reportAccent) + ";");
			secLabelReport.setStyle("-fx-text-fill: " + toHexString(reportSecondary) + ";");
			
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equals("true")) {
				tabpane.getStyleClass().clear();
				tabpane.getStyleClass().add("darktabpane");
			} else {
				tabpane.getStyleClass().clear();
				tabpane.getStyleClass().add("lighttabpane");
			}
			
			try {
				String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor");
				String nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("uiColors",
				                                                                              "accentColor") + ";";
				resetNotiDefaultsBtn.setStyle(nonTransparentBtn);
				resetNotiDefaultsBtn.setOnMouseEntered(e -> resetNotiDefaultsBtn.setStyle(hoverStyle));
				resetNotiDefaultsBtn.setOnMouseExited(e -> resetNotiDefaultsBtn.setStyle(nonTransparentBtn));
				previewNotificationBtn.setStyle(nonTransparentBtn);
				previewNotificationBtn.setOnMouseEntered(e -> previewNotificationBtn.setStyle(hoverStyle));
				previewNotificationBtn.setOnMouseExited(e -> previewNotificationBtn.setStyle(nonTransparentBtn));
				resetDefaultsBtn.setStyle(nonTransparentBtn);
				resetDefaultsBtn.setOnMouseEntered(e -> resetDefaultsBtn.setStyle(hoverStyle));
				resetDefaultsBtn.setOnMouseExited(e -> resetDefaultsBtn.setStyle(nonTransparentBtn));
				resetReportDefaultsBtn.setStyle(nonTransparentBtn);
				resetReportDefaultsBtn.setOnMouseEntered(e -> resetReportDefaultsBtn.setStyle(hoverStyle));
				resetReportDefaultsBtn.setOnMouseExited(e -> resetReportDefaultsBtn.setStyle(nonTransparentBtn));
				clrLogsBtn.setStyle(nonTransparentBtn);
				clrLogsBtn.setOnMouseEntered(e -> clrLogsBtn.setStyle(hoverStyle));
				clrLogsBtn.setOnMouseExited(e -> clrLogsBtn.setStyle(nonTransparentBtn));
				clrSaveDataBtn.setStyle(nonTransparentBtn);
				clrSaveDataBtn.setOnMouseEntered(e -> clrSaveDataBtn.setStyle(hoverStyle));
				clrSaveDataBtn.setOnMouseExited(e -> clrSaveDataBtn.setStyle(nonTransparentBtn));
				debugLogBtn.setStyle(nonTransparentBtn);
				debugLogBtn.setOnMouseEntered(e -> debugLogBtn.setStyle(hoverStyle));
				debugLogBtn.setOnMouseExited(e -> debugLogBtn.setStyle(nonTransparentBtn));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			logError("LoadTheme IO Error Code 917 ", e);
		}
	}
	
	private void addDefaultCheckboxSelections() throws IOException {
		notiDisplayDurField.setText(ConfigReader.configRead("notificationSettings", "displayDuration"));
		notiFadeOutDurField.setText(ConfigReader.configRead("notificationSettings", "fadeOutDuration"));
		broadcastPortField.setText(ConfigReader.configRead("connectionSettings", "broadcastPort"));
		socketTimeoutField.setText(ConfigReader.configRead("connectionSettings", "socketTimeout"));
		
		audioLookupWarningCheckbox.setSelected(
				ConfigReader.configRead("soundSettings", "playLookupWarning").equalsIgnoreCase("true"));
		audioCalloutCheckbox.setSelected(
				ConfigReader.configRead("soundSettings", "playCallout").equalsIgnoreCase("true"));
		audioReportCreate.setSelected(
				ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true"));
		audioReportDeleteCheckbox.setSelected(
				ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true"));
		enableNotificationsCheckbox.setSelected(
				ConfigReader.configRead("notificationSettings", "enabled").equalsIgnoreCase("true"));
		enableCalloutPopupsCheckbox.setSelected(
				ConfigReader.configRead("uiSettings", "enableCalloutPopup").equalsIgnoreCase("true"));
		enableSoundCheckbox.setSelected(ConfigReader.configRead("uiSettings", "enableSounds").equalsIgnoreCase("true"));
		enableIDPopupsCheckbox.setSelected(
				ConfigReader.configRead("uiSettings", "enableIDPopup").equalsIgnoreCase("true"));
		serverAutoconnectCheckbox.setSelected(
				ConfigReader.configRead("connectionSettings", "serverAutoConnect").equalsIgnoreCase("true"));
		saveCalloutLocationCheckbox.setSelected(
				ConfigReader.configRead("layout", "rememberCalloutLocation").equalsIgnoreCase("true"));
		saveReportLocationCheckbox.setSelected(
				ConfigReader.configRead("layout", "rememberReportLocation").equalsIgnoreCase("true"));
		saveIDLocationCheckbox.setSelected(
				ConfigReader.configRead("layout", "rememberIDLocation").equalsIgnoreCase("true"));
		saveNotesLocationCheckbox.setSelected(
				ConfigReader.configRead("layout", "rememberNotesLocation").equalsIgnoreCase("true"));
		AOTNotes.setSelected(ConfigReader.configRead("AOTSettings", "AOTNotes").equalsIgnoreCase("true"));
		AOTReport.setSelected(ConfigReader.configRead("AOTSettings", "AOTReport").equalsIgnoreCase("true"));
		AOTMap.setSelected(ConfigReader.configRead("AOTSettings", "AOTMap").equalsIgnoreCase("true"));
		AOTID.setSelected(ConfigReader.configRead("AOTSettings", "AOTID").equalsIgnoreCase("true"));
		AOTCallout.setSelected(ConfigReader.configRead("AOTSettings", "AOTCallout").equalsIgnoreCase("true"));
		AOTSettings.setSelected(ConfigReader.configRead("AOTSettings", "AOTSettings").equalsIgnoreCase("true"));
		AOTClient.setSelected(ConfigReader.configRead("AOTSettings", "AOTClient").equalsIgnoreCase("true"));
		AOTDebug.setSelected(ConfigReader.configRead("AOTSettings", "AOTDebug").equalsIgnoreCase("true"));
	}
	
	private void addEventFilters() {
		broadcastPortField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
			String character = event.getCharacter();
			String text = broadcastPortField.getText();
			
			if (!character.matches("[0-9]")) {
				event.consume();
				return;
			}
			
			String newText = text + character;
			
			if (newText.length() > 5) {
				event.consume();
				return;
			}
			
			int newValue;
			try {
				newValue = Integer.parseInt(newText);
			} catch (NumberFormatException e) {
				event.consume();
				return;
			}
			
			ConfigWriter.configwrite("connectionSettings", "broadcastPort", newText);
		});
		notiDisplayDurField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
			String character = event.getCharacter();
			String text = notiDisplayDurField.getText();
			
			if (!character.matches("[0-9]") && !character.equals(".")) {
				event.consume();
				return;
			}
			
			if (character.equals(".") && text.contains(".")) {
				event.consume();
				return;
			}
			
			if (text.length() >= 5) {
				event.consume();
				return;
			}
			
			String newText = text + character;
			
			try {
				if (!newText.equals(".") && !newText.endsWith(".")) {
					Double.parseDouble(newText);
				}
			} catch (NumberFormatException e) {
				event.consume();
			}
			
		});
		notiFadeOutDurField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
			String character = event.getCharacter();
			String text = notiFadeOutDurField.getText();
			
			if (!character.matches("[0-9]") && !character.equals(".")) {
				event.consume();
				return;
			}
			
			if (character.equals(".") && text.contains(".")) {
				event.consume();
				return;
			}
			
			if (text.length() >= 5) {
				event.consume();
				return;
			}
			
			String newText = text + character;
			
			try {
				if (!newText.equals(".") && !newText.endsWith(".")) {
					Double.parseDouble(newText);
				}
			} catch (NumberFormatException e) {
				event.consume();
			}
		});
		socketTimeoutField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
			String character = event.getCharacter();
			String text = socketTimeoutField.getText();
			
			if (!character.matches("[0-9]")) {
				event.consume();
				return;
			}
			
			String newText = text + character;
			
			if (newText.length() > 5) {
				event.consume();
				return;
			}
			
			int newValue;
			try {
				newValue = Integer.parseInt(newText);
			} catch (NumberFormatException e) {
				event.consume();
				return;
			}
			
			ConfigWriter.configwrite("connectionSettings", "socketTimeout", newText);
		});
	}
	
	private void setupListeners() {
		notiPrimPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			if (selectedNotification.get().equals("Information")) {
				updateInfoNotiPrim(selectedColor);
			}
			if (selectedNotification.get().equals("Warning")) {
				updateWarnNotiPrim(selectedColor);
			}
		});
		
		notiTextColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			if (selectedNotification.get().equals("Information")) {
				updateInfoNotiTextColor(selectedColor);
			}
			if (selectedNotification.get().equals("Warning")) {
				updateWarnNotiTextColor(selectedColor);
			}
		});
		
		bkgPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			updatebackground(selectedColor);
			try {
				loadTheme();
				loadColors();
			} catch (IOException e) {
				logError("LoadTheme IO Error Code 33", e);
			}
		});
		
		primPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			updateMain(selectedColor);
			try {
				loadTheme();
				loadColors();
			} catch (IOException e) {
				logError("LoadTheme IO Error Code 3", e);
			}
		});
		
		secPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			updateSecondary(selectedColor);
			try {
				loadTheme();
				loadColors();
			} catch (IOException e) {
				logError("LoadTheme IO Error Code 4", e);
			}
		});
		
		accPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			updateAccent(selectedColor);
			try {
				loadTheme();
				loadColors();
			} catch (IOException e) {
				logError("LoadTheme IO Error Code 5", e);
			}
		});
		
		backgroundPickerReport.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			updateReportBackground(selectedColor);
			loadColors();
		});
		
		accentPickerReport.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			updateReportAccent(selectedColor);
			loadColors();
		});
		
		headingPickerReport.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			updateReportHeading(selectedColor);
			loadColors();
		});
		
		secPickerReport.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			updateReportSecondary(selectedColor);
			loadColors();
		});
	}
	
	private void addActionEventsAndComboBoxes() throws IOException {
		String[] reportdarklight = {"dark", "light"};
		String[] uidarklight = {"dark", "light"};
		
		reportStyleComboBox.getItems().addAll(reportdarklight);
		textClrComboBox.getItems().addAll(uidarklight);
		
		try {
			if (ConfigReader.configRead("reportSettings", "reportWindowDarkMode").equals("true")) {
				reportStyleComboBox.getSelectionModel().selectFirst();
			} else {
				reportStyleComboBox.getSelectionModel().selectLast();
			}
		} catch (IOException e) {
			logError("DarkMode IO Error Code 1 ", e);
			
		}
		try {
			if (ConfigReader.configRead("uiColors", "UIDarkMode").equals("true")) {
				textClrComboBox.getSelectionModel().selectFirst();
				if (logController != null) {
					logController.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UIDarkColor + ";");
				}
			} else {
				textClrComboBox.getSelectionModel().selectLast();
				if (logController != null) {
					logController.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UILightColor + ";");
				}
			}
		} catch (IOException e) {
			logError("DarkMode IO Error Code 1 ", e);
			
		}
		
		reportStyleComboBox.setOnAction(event -> {
			if (reportStyleComboBox.getSelectionModel().getSelectedItem().equals("dark")) {
				ConfigWriter.configwrite("reportSettings", "reportWindowDarkMode", "true");
			} else {
				ConfigWriter.configwrite("reportSettings", "reportWindowDarkMode", "false");
			}
		});
		textClrComboBox.setOnAction(event -> {
			if (textClrComboBox.getSelectionModel().getSelectedItem().equals("dark")) {
				ConfigWriter.configwrite("uiColors", "UIDarkMode", "true");
			} else {
				ConfigWriter.configwrite("uiColors", "UIDarkMode", "false");
			}
			try {
				loadTheme();
				loadColors();
			} catch (IOException e) {
				logError("loadtheme code 28939: ", e);
			}
		});
		
		String[] themes = {"dark", "purple", "blue", "grey", "green", "red", "orange", "pink", "teal", "brown", "magenta", "indigo"};
		themeComboBox.getItems().addAll(themes);
		themeComboBox.setOnAction(actionEvent -> {
			String selectedTheme = (String) themeComboBox.getSelectionModel().getSelectedItem();
			
			isInitialized = false;
			
			switch (selectedTheme) {
				case "dark" -> {
					log("Dark Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#263238"));
					updateSecondary(Color.valueOf("#323C41"));
					updateAccent(Color.valueOf("#505d62"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "purple" -> {
					log("Purple Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#524992"));
					updateSecondary(Color.valueOf("#665cb6"));
					updateAccent(Color.valueOf("#544f7f"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "blue" -> {
					log("Blue Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#4d66cc"));
					updateSecondary(Color.valueOf("#6680e6"));
					updateAccent(Color.valueOf("#516ca5"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "grey" -> {
					log("Grey Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#666666"));
					updateSecondary(Color.valueOf("#808080"));
					updateAccent(Color.valueOf("#4d4d4d"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "green" -> {
					log("Green Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#4d804d"));
					updateSecondary(Color.valueOf("#669966"));
					updateAccent(Color.valueOf("#4a6f4a"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "red" -> {
					log("Red Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#cc4d4d"));
					updateSecondary(Color.valueOf("#e65c5c"));
					updateAccent(Color.valueOf("#914f4f"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "orange" -> {
					log("Orange Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#cc804d"));
					updateSecondary(Color.valueOf("#e6994d"));
					updateAccent(Color.valueOf("#a57749"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "pink" -> {
					log("Pink Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#cc3399"));
					updateSecondary(Color.valueOf("#e64da1"));
					updateAccent(Color.valueOf("#955b78"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "teal" -> {
					log("Teal Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#339999"));
					updateSecondary(Color.valueOf("#4db3b3"));
					updateAccent(Color.valueOf("#4c8d8d"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "brown" -> {
					log("Brown Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#6c3d2c"));
					updateSecondary(Color.valueOf("#7e4e3c"));
					updateAccent(Color.valueOf("#5b3b30"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "magenta" -> {
					log("Magenta Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#c2185b"));
					updateSecondary(Color.valueOf("#e91e63"));
					updateAccent(Color.valueOf("#9d546c"));
					updatebackground(Color.valueOf("#ffffff"));
				}
				case "indigo" -> {
					log("Indigo Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#3f51b5"));
					updateSecondary(Color.valueOf("#5c6bc0"));
					updateAccent(Color.valueOf("#4b5483"));
					updatebackground(Color.valueOf("#ffffff"));
				}
			}
			
			try {
				loadTheme();
				loadColors();
			} catch (IOException e) {
				logError("LoadTheme Error", e);
			}
			isInitialized = true;
		});
		
		String[] presets = {"dark", "light", "grey", "green", "blue", "red", "purple", "orange", "pink", "teal", "brown", "magenta", "indigo"};
		presetComboBoxReport.getItems().addAll(presets);
		presetComboBoxReport.setOnAction(actionEvent -> {
			String selectedTheme = (String) presetComboBoxReport.getSelectionModel().getSelectedItem();
			isInitialized = false;
			switch (selectedTheme) {
				case "dark" -> {
					log("Dark Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#505d62"));
					updateReportSecondary(Color.valueOf("#323c41"));
					updateReportAccent(Color.valueOf("#263238"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "light" -> {
					log("Light Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#e6e6e6"));
					updateReportSecondary(Color.valueOf("#cccccc"));
					updateReportAccent(Color.valueOf("#b3b3b3"));
					updateReportHeading(Color.valueOf("#333333"));
				}
				case "grey" -> {
					log("Grey Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#4d4d4d"));
					updateReportSecondary(Color.valueOf("gray"));
					updateReportAccent(Color.valueOf("#666666"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "green" -> {
					log("Green Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#80b380"));
					updateReportSecondary(Color.valueOf("#669966"));
					updateReportAccent(Color.valueOf("#4d804d"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "blue" -> {
					log("Blue Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#8099ff"));
					updateReportSecondary(Color.valueOf("#6680e6"));
					updateReportAccent(Color.valueOf("#4d66cc"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "red" -> {
					log("Red Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#914f4f"));
					updateReportSecondary(Color.valueOf("#e65c5c"));
					updateReportAccent(Color.valueOf("#cc4d4d"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "purple" -> {
					log("Purple Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#b366ff"));
					updateReportSecondary(Color.valueOf("#994dff"));
					updateReportAccent(Color.valueOf("#7f33ff"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "orange" -> {
					log("Orange Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#a57749"));
					updateReportSecondary(Color.valueOf("#e6994d"));
					updateReportAccent(Color.valueOf("#cc804d"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "pink" -> {
					log("Pink Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#955b78"));
					updateReportSecondary(Color.valueOf("#e64da1"));
					updateReportAccent(Color.valueOf("#cc3399"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "teal" -> {
					log("Teal Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#4c8d8d"));
					updateReportSecondary(Color.valueOf("#4db3b3"));
					updateReportAccent(Color.valueOf("#339999"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "brown" -> {
					log("Brown Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#6c3d2c"));
					updateReportSecondary(Color.valueOf("#7e4e3c"));
					updateReportAccent(Color.valueOf("#5b3b30"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "magenta" -> {
					log("Magenta Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#9d546c"));
					updateReportSecondary(Color.valueOf("#e91e63"));
					updateReportAccent(Color.valueOf("#c2185b"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "indigo" -> {
					log("Indigo Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#4b5483"));
					updateReportSecondary(Color.valueOf("#5c6bc0"));
					updateReportAccent(Color.valueOf("#3f51b5"));
					updateReportHeading(Color.valueOf("white"));
				}
			}
			loadColors();
			isInitialized = true;
		});
		
		String[] calloutDurations = {"infinite", "1", "3", "5", "7", "10", "12"};
		calloutDurComboBox.getItems().addAll(calloutDurations);
		calloutDurComboBox.setValue(ConfigReader.configRead("misc", "calloutDuration"));
		calloutDurComboBox.setOnAction(actionEvent -> {
			String selectedDur = (String) calloutDurComboBox.getSelectionModel().getSelectedItem();
			ConfigWriter.configwrite("misc", "calloutDuration", selectedDur);
		});
		
		String[] idDurations = {"infinite", "1", "3", "5", "7", "10", "12"};
		idDurComboBox.getItems().addAll(idDurations);
		idDurComboBox.setValue(ConfigReader.configRead("misc", "IDDuration"));
		idDurComboBox.setOnAction(actionEvent -> {
			String selectedDur = (String) idDurComboBox.getSelectionModel().getSelectedItem();
			ConfigWriter.configwrite("misc", "IDDuration", selectedDur);
		});
		
		String[] notifications = {"Information", "Warning"};
		selectedNotification = new AtomicReference<>("Information");
		notificationComboBox.getItems().addAll(notifications);
		notificationComboBox.setValue(selectedNotification);
		notificationComboBox.setOnAction(actionEvent -> {
			String selectedItem = (String) notificationComboBox.getSelectionModel().getSelectedItem();
			
			switch (selectedItem) {
				case "Information" -> {
					selectedNotification.set("Information");
					try {
						notiTextColorPicker.setValue(Color.valueOf(
								ConfigReader.configRead("notificationSettings", "notificationInfoTextColor")));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					try {
						notiPrimPicker.setValue(Color.valueOf(
								ConfigReader.configRead("notificationSettings", "notificationInfoPrimary")));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				case "Warning" -> {
					selectedNotification.set("Warning");
					try {
						notiTextColorPicker.setValue(Color.valueOf(
								ConfigReader.configRead("notificationSettings", "notificationWarnTextColor")));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					try {
						notiPrimPicker.setValue(Color.valueOf(
								ConfigReader.configRead("notificationSettings", "notificationWarnPrimary")));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
			
		});
		
		String[] notificationPositions = {"BottomLeft", "BottomRight", "TopLeft", "TopRight"};
		notiPosCombobox.getItems().addAll(notificationPositions);
		notiPosCombobox.setValue(ConfigReader.configRead("notificationSettings", "notificationPosition"));
		notiPosCombobox.setOnAction(actionEvent -> {
			String selectedPosition = (String) notiPosCombobox.getSelectionModel().getSelectedItem();
			switch (selectedPosition) {
				case "BottomLeft" ->
						ConfigWriter.configwrite("notificationSettings", "notificationPosition", "BottomLeft");
				case "BottomRight" ->
						ConfigWriter.configwrite("notificationSettings", "notificationPosition", "BottomRight");
				case "TopLeft" -> ConfigWriter.configwrite("notificationSettings", "notificationPosition", "TopLeft");
				case "TopRight" -> ConfigWriter.configwrite("notificationSettings", "notificationPosition", "TopRight");
			}
		});
		
		saveFadeDurBtn.setOnAction(actionEvent -> ConfigWriter.configwrite("notificationSettings", "fadeOutDuration",
		                                                                   notiFadeOutDurField.getText()));
		saveDisplayDurBtn.setOnAction(actionEvent -> ConfigWriter.configwrite("notificationSettings", "displayDuration",
		                                                                      notiDisplayDurField.getText()));
	}
	
	@javafx.fxml.FXML
	public void clearSaveDataBtnClick(ActionEvent actionEvent) {
		Stage stage = (Stage) root.getScene().getWindow();
		confirmSaveDataClearDialog(stage);
	}
	
	@javafx.fxml.FXML
	public void openDebugLogsBtnClick(ActionEvent actionEvent) {
		createFakeWindow(mainDesktopControllerObj.getDesktopContainer(), "Windows/Misc/output-view.fxml",
		                 "Application Logs", false, 2, true, mainDesktopControllerObj.getTaskBarApps());
	}
	
	@javafx.fxml.FXML
	public void clearLogsBtnClick(ActionEvent actionEvent) {
		Stage stage = (Stage) root.getScene().getWindow();
		confirmLogClearDialog(stage);
		NotificationManager.showNotificationInfo("Log Manager", "Logs have been cleared.");
	}
	
	@javafx.fxml.FXML
	public void resetDefaultsBtnPress(ActionEvent actionEvent) {
		ConfigWriter.configwrite("uiColors", "UIDarkMode", "true");
		isInitialized = false;
		updateMain(Color.valueOf("#524992"));
		updateSecondary(Color.valueOf("#665cb6"));
		updateAccent(Color.valueOf("#544f7f"));
		updatebackground(Color.valueOf("#ffffff"));
		log("Reset Color Defaults", LogUtils.Severity.DEBUG);
		try {
			loadTheme();
			loadColors();
		} catch (IOException e) {
			logError("LoadTheme IO Error Code 2 ", e);
		}
		themeComboBox.getSelectionModel().select("purple");
		textClrComboBox.getSelectionModel().select("dark");
		isInitialized = true;
	}
	
	@javafx.fxml.FXML
	public void resetReportDefaultsBtnPress(ActionEvent actionEvent) {
		isInitialized = false;
		updateReportBackground(Color.valueOf("#505d62"));
		updateReportSecondary(Color.valueOf("#323c41"));
		updateReportAccent(Color.valueOf("#263238"));
		updateReportHeading(Color.valueOf("white"));
		ConfigWriter.configwrite("reportSettings", "reportWindowDarkMode", "false");
		log("Reset Report Color Defaults", LogUtils.Severity.DEBUG);
		loadColors();
		presetComboBoxReport.getSelectionModel().select("dark");
		reportStyleComboBox.getSelectionModel().select("light");
		isInitialized = true;
	}
	
	@javafx.fxml.FXML
	public void settingsAOTClick(ActionEvent actionEvent) {
		if (AOTSettings.isSelected()) {
			ConfigWriter.configwrite("AOTSettings", "AOTSettings", "true");
			AOTSettings.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTSettings", "AOTSettings", "false");
			AOTSettings.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void reportAOTClick(ActionEvent actionEvent) {
		if (AOTReport.isSelected()) {
			ConfigWriter.configwrite("AOTSettings", "AOTReport", "true");
			AOTReport.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTSettings", "AOTReport", "false");
			AOTReport.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void mapAOTClick(ActionEvent actionEvent) {
		if (AOTMap.isSelected()) {
			ConfigWriter.configwrite("AOTSettings", "AOTMap", "true");
			AOTMap.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTSettings", "AOTMap", "false");
			AOTMap.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void calloutAOTClick(ActionEvent actionEvent) {
		if (AOTCallout.isSelected()) {
			ConfigWriter.configwrite("AOTSettings", "AOTCallout", "true");
			AOTCallout.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTSettings", "AOTCallout", "false");
			AOTCallout.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void IDAOTClick(ActionEvent actionEvent) {
		if (AOTID.isSelected()) {
			ConfigWriter.configwrite("AOTSettings", "AOTID", "true");
			AOTID.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTSettings", "AOTID", "false");
			AOTID.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void NotesAOTClick(ActionEvent actionEvent) {
		if (AOTNotes.isSelected()) {
			ConfigWriter.configwrite("AOTSettings", "AOTNotes", "true");
			AOTNotes.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTSettings", "AOTNotes", "false");
			AOTNotes.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void ClientAOTClick(ActionEvent actionEvent) {
		if (AOTClient.isSelected()) {
			ConfigWriter.configwrite("AOTSettings", "AOTClient", "true");
			AOTClient.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTSettings", "AOTClient", "false");
			AOTClient.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void debugAOTClick(ActionEvent actionEvent) {
		if (AOTDebug.isSelected()) {
			ConfigWriter.configwrite("AOTSettings", "AOTDebug", "true");
			AOTDebug.setSelected(true);
		} else {
			ConfigWriter.configwrite("AOTSettings", "AOTDebug", "false");
			AOTDebug.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void serverAutoConnectClick(ActionEvent actionEvent) {
		if (serverAutoconnectCheckbox.isSelected()) {
			ConfigWriter.configwrite("connectionSettings", "serverAutoConnect", "true");
			serverAutoconnectCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("connectionSettings", "serverAutoConnect", "false");
			serverAutoconnectCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void rememberIDLocationClick(ActionEvent actionEvent) {
		if (saveIDLocationCheckbox.isSelected()) {
			ConfigWriter.configwrite("layout", "rememberIDLocation", "true");
			saveIDLocationCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("layout", "rememberIDLocation", "false");
			saveIDLocationCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void rememberCalloutLocationClick(ActionEvent actionEvent) {
		if (saveCalloutLocationCheckbox.isSelected()) {
			ConfigWriter.configwrite("layout", "rememberCalloutLocation", "true");
			saveCalloutLocationCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("layout", "rememberCalloutLocation", "false");
			saveCalloutLocationCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void colorPageOneBtnClick(ActionEvent actionEvent) {
		colorPageOne.setVisible(true);
		colorPageTwo.setVisible(false);
	}
	
	@javafx.fxml.FXML
	public void colorPageTwoBtnClick(ActionEvent actionEvent) {
		colorPageOne.setVisible(false);
		colorPageTwo.setVisible(true);
	}
	
	@javafx.fxml.FXML
	public void resetNotiDefaultsBtnPress(ActionEvent actionEvent) {
		if (selectedNotification.get().equals("Information")) {
			ConfigWriter.configwrite("notificationSettings", "notificationInfoPrimary", "#367af6");
			ConfigWriter.configwrite("notificationSettings", "notificationInfoTextColor", "#ffffff");
		} else {
			ConfigWriter.configwrite("notificationSettings", "notificationWarnPrimary", "#FFA726");
			ConfigWriter.configwrite("notificationSettings", "notificationWarnTextColor", "#ffffff");
		}
		loadColors();
	}
	
	@javafx.fxml.FXML
	public void rememberReportLocationClick(ActionEvent actionEvent) {
		if (saveReportLocationCheckbox.isSelected()) {
			ConfigWriter.configwrite("layout", "rememberReportLocation", "true");
			saveReportLocationCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("layout", "rememberReportLocation", "false");
			saveReportLocationCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void rememberNotesLocationClick(ActionEvent actionEvent) {
		if (saveNotesLocationCheckbox.isSelected()) {
			ConfigWriter.configwrite("layout", "rememberNotesLocation", "true");
			saveNotesLocationCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("layout", "rememberNotesLocation", "false");
			saveNotesLocationCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void windowPageTwoBtnClick(ActionEvent actionEvent) {
		windowPageOne.setVisible(false);
		windowPageTwo.setVisible(true);
	}
	
	@javafx.fxml.FXML
	public void windowPageOneBtnClick(ActionEvent actionEvent) {
		windowPageOne.setVisible(true);
		windowPageTwo.setVisible(false);
	}
	
	@javafx.fxml.FXML
	public void enableIDPopupClick(ActionEvent actionEvent) {
		if (enableIDPopupsCheckbox.isSelected()) {
			ConfigWriter.configwrite("uiSettings", "enableIDPopup", "true");
			enableIDPopupsCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("uiSettings", "enableIDPopup", "false");
			enableIDPopupsCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void enableCalloutPopupClick(ActionEvent actionEvent) {
		if (enableCalloutPopupsCheckbox.isSelected()) {
			ConfigWriter.configwrite("uiSettings", "enableCalloutPopup", "true");
			enableCalloutPopupsCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("uiSettings", "enableCalloutPopup", "false");
			enableCalloutPopupsCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void enableNotiCheckboxClick(ActionEvent actionEvent) {
		if (enableNotificationsCheckbox.isSelected()) {
			ConfigWriter.configwrite("notificationSettings", "enabled", "true");
			enableNotificationsCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("notificationSettings", "enabled", "false");
			enableNotificationsCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void enableSoundCheckboxClick(ActionEvent actionEvent) {
		if (enableSoundCheckbox.isSelected()) {
			ConfigWriter.configwrite("uiSettings", "enableSounds", "true");
			enableSoundCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("uiSettings", "enableSounds", "false");
			enableSoundCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void audioCalloutClick(ActionEvent actionEvent) {
		if (audioCalloutCheckbox.isSelected()) {
			ConfigWriter.configwrite("soundSettings", "playCallout", "true");
			audioCalloutCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("soundSettings", "playCallout", "false");
			audioCalloutCheckbox.setSelected(false);
		}
	}
	
	private void addTooltips() {
		addTooltip(serverAutoconnectCheckbox, "Try To Autoconnect To Server On Startup");
		addTooltip(saveReportLocationCheckbox, "Save Location and Size of Report Window");
		addTooltip(enableNotificationsCheckbox, "Allow Recieving Notifications");
		
		addTooltip(audioCalloutCheckbox, "Enable Sound When Recieving Callout");
		addTooltip(audioReportCreate, "Enable Sound When Creating Report");
		addTooltip(audioReportDeleteCheckbox, "Enable Sound When Deleting Report");
		addTooltip(audioLookupWarningCheckbox, "Enable Warning Sound When Looking Up Veh/Ped");
		
		addTooltip(enableCalloutPopupsCheckbox, "Allow Callouts To Pop Up On Screen");
		addTooltip(enableIDPopupsCheckbox, "Allow IDs To Pop Up On Screen");
		addTooltip(enableSoundCheckbox, "Requires Sound Pack From ReportsPlus LCPDFR Page");
		
		addTooltip(saveCalloutLocationCheckbox, "Keep Callout Window In Same Location");
		addTooltip(saveIDLocationCheckbox, "Keep ID Window In Same Location");
		addTooltip(saveNotesLocationCheckbox, "Keep Notes Window In Same Location");
		
		addTooltip(AOTCallout, "Keep Callout Window On Top");
		addTooltip(AOTClient, "Keep Client Window On Top");
		addTooltip(AOTID, "Keep ID Window On Top");
		addTooltip(AOTDebug, "Keep Debug Window On Top");
		addTooltip(AOTMap, "Keep Map Window On Top");
		addTooltip(AOTNotes, "Keep Notes Window On Top");
		addTooltip(AOTReport, "Keep Report Window On Top");
		addTooltip(AOTSettings, "Keep Settings Window On Top");
		
		addTooltip(tt4, "UI Theme Presets");
		addTooltip(tt5, "UI Text Color");
		addTooltip(tt6, "Report Theme Presets");
		addTooltip(tt7, "Report TextField Color");
		addTooltip(tt8, "Duration (Sec) That Callout Window is shown");
		addTooltip(tt9, "Duration (Sec) That ID Window is shown");
		addTooltip(tt10,
		           "Port Used To Receive Server Broadcast Info\nOnly Change If You Have Issues With Autoconnection\nMust Match With Broadcastport In Server Config");
		addTooltip(tt11, "Set a maximum wait time for receiving data before disconnecting");
		
		addTooltip(tt16, "Notification Type to be Modified");
		addTooltip(tt12, "Primary Notification Color");
		addTooltip(tt15, "Notification Text Color");
		addTooltip(tt13, "Duration the Notification is Displayed (Sec)");
		addTooltip(tt14, "Duration Notification takes to fade out (Sec)");
		addTooltip(tt17, "Corner Of The Window That The Notification Appears In");
		
		addTooltip(bkgLabel, "Application Background Color");
		addTooltip(primLabel, "Application Primary Color");
		addTooltip(secLabel, "Application Secondary Color");
		addTooltip(accLabel, "Application Accent Color");
		
		addTooltip(headingLabelReport, "Report Heading Color");
		addTooltip(backgroundLabelReport, "Report Background Color");
		addTooltip(secLabelReport, "Report Secondary Color");
		addTooltip(accentLabelReport, "Report Accent Color");
	}
	
	@javafx.fxml.FXML
	public void audioReportDeleteClick(ActionEvent actionEvent) {
		if (audioReportDeleteCheckbox.isSelected()) {
			ConfigWriter.configwrite("soundSettings", "playDeleteReport", "true");
			audioReportDeleteCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("soundSettings", "playDeleteReport", "false");
			audioReportDeleteCheckbox.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void audioReportCreateClick(ActionEvent actionEvent) {
		if (audioReportCreate.isSelected()) {
			ConfigWriter.configwrite("soundSettings", "playCreateReport", "true");
			audioReportCreate.setSelected(true);
		} else {
			ConfigWriter.configwrite("soundSettings", "playCreateReport", "false");
			audioReportCreate.setSelected(false);
		}
	}
	
	@javafx.fxml.FXML
	public void audioLookupWarning(ActionEvent actionEvent) {
		if (audioLookupWarningCheckbox.isSelected()) {
			ConfigWriter.configwrite("soundSettings", "playLookupWarning", "true");
			audioLookupWarningCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("soundSettings", "playLookupWarning", "false");
			audioLookupWarningCheckbox.setSelected(false);
		}
	}
}