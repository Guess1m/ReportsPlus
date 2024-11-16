package com.Guess.ReportsPlus.Windows.Settings;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Windows.Server.trafficStopController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Misc.CalloutManager;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.windows;
import static com.Guess.ReportsPlus.Desktop.mainDesktopController.updateDesktopBackground;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopStage;
import static com.Guess.ReportsPlus.Windows.Apps.CalloutViewController.calloutViewController;
import static com.Guess.ReportsPlus.Windows.Apps.CourtViewController.courtViewController;
import static com.Guess.ReportsPlus.Windows.Apps.LogViewController.logController;
import static com.Guess.ReportsPlus.Windows.Apps.PedLookupViewController.pedLookupViewController;
import static com.Guess.ReportsPlus.Windows.Apps.VehLookupViewController.vehLookupViewController;
import static com.Guess.ReportsPlus.Windows.Misc.UserManagerController.userManagerController;
import static com.Guess.ReportsPlus.Windows.Server.ClientController.clientController;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Server.ClientUtils.isConnected;

public class settingsController {
	
	private static final String UILightColor = "rgb(255,255,255,0.75)";
	private static final String UIDarkColor = "rgb(0,0,0,0.75)";
	public static settingsController SettingsController;
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
	private Button clrLogsBtn;
	@javafx.fxml.FXML
	private Button clrSaveDataBtn;
	@javafx.fxml.FXML
	private ComboBox calloutDurComboBox;
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
	private Label headingLabelReport;
	@javafx.fxml.FXML
	private ComboBox presetComboBoxReport;
	@javafx.fxml.FXML
	private ComboBox idDurComboBox;
	@javafx.fxml.FXML
	private ColorPicker bkgPicker;
	@javafx.fxml.FXML
	private Label bkgLabel;
	@javafx.fxml.FXML
	private ComboBox textClrComboBox;
	@javafx.fxml.FXML
	private TextField broadcastPortField;
	@javafx.fxml.FXML
	private Button resetReportDefaultsBtn;
	@javafx.fxml.FXML
	private TextField socketTimeoutField;
	@javafx.fxml.FXML
	private AnchorPane paneAudio;
	@javafx.fxml.FXML
	private AnchorPane paneApplication;
	@javafx.fxml.FXML
	private AnchorPane paneDeveloper;
	@javafx.fxml.FXML
	private AnchorPane paneNotification;
	@javafx.fxml.FXML
	private AnchorPane paneReport;
	@javafx.fxml.FXML
	private AnchorPane paneWindow;
	@javafx.fxml.FXML
	private AnchorPane paneNetworking;
	@javafx.fxml.FXML
	private ComboBox notiPosCombobox;
	@javafx.fxml.FXML
	private Button resetNotiDefaultsBtn;
	@javafx.fxml.FXML
	private TextField notiFadeOutDurField;
	@javafx.fxml.FXML
	private Button previewNotificationBtn;
	@javafx.fxml.FXML
	private ColorPicker notiPrimPicker;
	@javafx.fxml.FXML
	private TextField notiDisplayDurField;
	@javafx.fxml.FXML
	private ColorPicker notiTextColorPicker;
	@javafx.fxml.FXML
	private Button saveDisplayDurBtn;
	@javafx.fxml.FXML
	private Button saveFadeDurBtn;
	@javafx.fxml.FXML
	private ComboBox notificationComboBox;
	@javafx.fxml.FXML
	private Button audioBtn;
	@javafx.fxml.FXML
	private Button serverBtn;
	@javafx.fxml.FXML
	private Button notiSettingsBtn;
	@javafx.fxml.FXML
	private Button reportDesignBtn;
	@javafx.fxml.FXML
	private Button appDesignBtn;
	@javafx.fxml.FXML
	private Button devBtn;
	@javafx.fxml.FXML
	private Button windowSettingsBtn;
	@javafx.fxml.FXML
	private ToggleButton enableNotiTB;
	@javafx.fxml.FXML
	private BorderPane notiDisplayPane;
	@javafx.fxml.FXML
	private ToggleButton audioLookupWarningCheckbox;
	@javafx.fxml.FXML
	private ToggleButton audioReportCreate;
	@javafx.fxml.FXML
	private ToggleButton audioCalloutCheckbox;
	@javafx.fxml.FXML
	private ToggleButton audioReportDeleteCheckbox;
	@javafx.fxml.FXML
	private ToggleButton serverAutoconnectTogglebox;
	@javafx.fxml.FXML
	private ToggleButton enableSoundCheckbox;
	@javafx.fxml.FXML
	private ToggleButton enableIDPopupsCheckbox;
	@javafx.fxml.FXML
	private ToggleButton enableCalloutPopupsCheckbox;
	@javafx.fxml.FXML
	private ToggleButton enableTrafficStopPopupsCheckbox;
	@javafx.fxml.FXML
	private ComboBox trafficStopDurComboBox;
	@javafx.fxml.FXML
	private ComboBox windowDisplaySettingCombobox;
	@javafx.fxml.FXML
	private ToggleButton alwaysOnTopCheckbox;
	@javafx.fxml.FXML
	private Button desktopSettingsBtn;
	@javafx.fxml.FXML
	private AnchorPane paneDesktop;
	@javafx.fxml.FXML
	private ToggleGroup DesktopSetting;
	@javafx.fxml.FXML
	private ToggleButton solidColorToggle;
	@javafx.fxml.FXML
	private ToggleButton backgroundToggle;
	@javafx.fxml.FXML
	private ColorPicker desktopBackgroundPicker;
	@javafx.fxml.FXML
	private ColorPicker topBarPicker;
	@javafx.fxml.FXML
	private ColorPicker topBarTextPicker;
	@javafx.fxml.FXML
	private Button centerWindowsBtn;
	@javafx.fxml.FXML
	private Label mainSettingsSubheader;
	@javafx.fxml.FXML
	private Label mainHeader;
	@javafx.fxml.FXML
	private Label MiscSettingsSubheader;
	@javafx.fxml.FXML
	private Label developerSubheader;
	@javafx.fxml.FXML
	private Label colorSettingsSubheader;
	@javafx.fxml.FXML
	private Label trafficStopPopupsLabel;
	@javafx.fxml.FXML
	private Label IDDisplayDurLabel;
	@javafx.fxml.FXML
	private Label CalDisplayDurTT;
	@javafx.fxml.FXML
	private Label IDPopupsTT;
	@javafx.fxml.FXML
	private Label trafficStopPopupsTT;
	@javafx.fxml.FXML
	private Label aotLabelTT;
	@javafx.fxml.FXML
	private Label winSettingsHeader;
	@javafx.fxml.FXML
	private Label trafficStopDurLabel;
	@javafx.fxml.FXML
	private Label appDisLabelTT;
	@javafx.fxml.FXML
	private Label enableCalloutTT;
	@javafx.fxml.FXML
	private Label CalDisplayDurLabel;
	@javafx.fxml.FXML
	private Label trafficStopDurTT;
	@javafx.fxml.FXML
	private Label appDisLabel;
	@javafx.fxml.FXML
	private Label IDPopupsLabel;
	@javafx.fxml.FXML
	private Label IDDisplayDurTT;
	@javafx.fxml.FXML
	private Label aotLabel;
	@javafx.fxml.FXML
	private Label enableCalloutLabel;
	@javafx.fxml.FXML
	private Label selNotiLabel;
	@javafx.fxml.FXML
	private Label notiPrimColorTT;
	@javafx.fxml.FXML
	private Label notiPosTT;
	@javafx.fxml.FXML
	private Label fadeOutTimeTT;
	@javafx.fxml.FXML
	private Label recNotiTT;
	@javafx.fxml.FXML
	private Label displayDurLabelTT;
	@javafx.fxml.FXML
	private Label displayDurLabel;
	@javafx.fxml.FXML
	private Label notiTextColorLabel;
	@javafx.fxml.FXML
	private Label notiPreviewLabel;
	@javafx.fxml.FXML
	private Label notiPosLabel;
	@javafx.fxml.FXML
	private Label notiTextColorTT;
	@javafx.fxml.FXML
	private Label notiPrimColorLabel;
	@javafx.fxml.FXML
	private Label selNotiTT;
	@javafx.fxml.FXML
	private Label fadeOutTimeLabel;
	@javafx.fxml.FXML
	private Label recNotiLabel;
	@javafx.fxml.FXML
	private Label notiSettingsHeader;
	@javafx.fxml.FXML
	private Label desktopTopBarClrTT;
	@javafx.fxml.FXML
	private Label desktopImageChooserLabel;
	@javafx.fxml.FXML
	private Label desktopBackgroundClrTT;
	@javafx.fxml.FXML
	private Label desktopTopBarTextClrTT;
	@javafx.fxml.FXML
	private Label desktopSetingsHeader;
	@javafx.fxml.FXML
	private Button chooseImageFileBtn;
	@javafx.fxml.FXML
	private Label useDesktopBackgroundImageLabel;
	@javafx.fxml.FXML
	private Label useDesktopBackgroundImageTT;
	@javafx.fxml.FXML
	private Label desktopTopBarTextClrLabel;
	@javafx.fxml.FXML
	private Label desktopUseSolidClrLabel;
	@javafx.fxml.FXML
	private Label desktopUseSolidClrTT;
	@javafx.fxml.FXML
	private Label desktopImageChooserTT;
	@javafx.fxml.FXML
	private Label desktopBackgroundClrLabel;
	@javafx.fxml.FXML
	private Label desktopTopBarClrLabel;
	@javafx.fxml.FXML
	private Label clearLogsLabel;
	@javafx.fxml.FXML
	private Label centerWindowsLabel;
	@javafx.fxml.FXML
	private Label clearSaveDataLabel;
	@javafx.fxml.FXML
	private Label clearLogsTT;
	@javafx.fxml.FXML
	private Label clearSaveDataTT;
	@javafx.fxml.FXML
	private Label devSettingsHeader;
	@javafx.fxml.FXML
	private Label centerWindowsTT;
	@javafx.fxml.FXML
	private Label createReportSoundLabel;
	@javafx.fxml.FXML
	private Label enableSoundsLabel;
	@javafx.fxml.FXML
	private Label enableSoundsTT;
	@javafx.fxml.FXML
	private Label delReportTT;
	@javafx.fxml.FXML
	private Label createReportSoundTT;
	@javafx.fxml.FXML
	private Label audioSettingsHeader;
	@javafx.fxml.FXML
	private Label calloutSoundLabel;
	@javafx.fxml.FXML
	private Label lookupWarningSoundLabel;
	@javafx.fxml.FXML
	private Label calloutSoundTT;
	@javafx.fxml.FXML
	private Label lookupWarningSoundTT;
	@javafx.fxml.FXML
	private Label delReportLabel;
	@javafx.fxml.FXML
	private Label autoConnectServerTT;
	@javafx.fxml.FXML
	private Label broadcastPortTT;
	@javafx.fxml.FXML
	private Label broadcastPortLabel;
	@javafx.fxml.FXML
	private Label autoConnectServerLabel;
	@javafx.fxml.FXML
	private Label socketTimeoutLabel;
	@javafx.fxml.FXML
	private Label socketTimeoutTT;
	@javafx.fxml.FXML
	private Label networkingSettingsHeader;
	@javafx.fxml.FXML
	private Label secTT;
	@javafx.fxml.FXML
	private Label appResetDefaultsLabel;
	@javafx.fxml.FXML
	private Label primTT;
	@javafx.fxml.FXML
	private Label accTT;
	@javafx.fxml.FXML
	private Label appTextColorTT;
	@javafx.fxml.FXML
	private Label appPresetTT;
	@javafx.fxml.FXML
	private Label bkgTT;
	@javafx.fxml.FXML
	private Label appResetDefaultsTT;
	@javafx.fxml.FXML
	private Label appWindowCustomizationHeader;
	@javafx.fxml.FXML
	private Label appTextColorLabel;
	@javafx.fxml.FXML
	private Label appPresetLabel;
	@javafx.fxml.FXML
	private Label reportResetDefaultsTT;
	@javafx.fxml.FXML
	private Label reportResetDefaultsLabel;
	@javafx.fxml.FXML
	private Label backgroundLabelReportTT;
	@javafx.fxml.FXML
	private Label headingLabelReportTT;
	@javafx.fxml.FXML
	private Label secLabelReportTT;
	@javafx.fxml.FXML
	private Label reportDesignPresetLabel;
	@javafx.fxml.FXML
	private Label reportTextColorTT;
	@javafx.fxml.FXML
	private Label reportTextColorLabel;
	@javafx.fxml.FXML
	private Label reportWindowCustomizationHeader;
	@javafx.fxml.FXML
	private Label reportDesignPresetTT;
	@javafx.fxml.FXML
	private Label accentLabelReportTT;
	
	//</editor-fold>
	
	public static void loadTheme() throws IOException {
		String mainclr = ConfigReader.configRead("uiColors", "mainColor");
		String secclr = ConfigReader.configRead("uiColors", "secondaryColor");
		String bkgclr = ConfigReader.configRead("uiColors", "bkgColor");
		String accclr = ConfigReader.configRead("uiColors", "accentColor");
		String hoverStyle = "-fx-background-color: " + accclr;
		String nonTransparentBtn = "-fx-background-color: " + mainclr + ";";
		
		if (clientController != null) {
			clientController.getStatusLabel().setStyle("-fx-background-color: " + mainclr + ";");
			clientController.getConnectBtn().setStyle("-fx-background-color: " + mainclr + ";");
			clientController.getMainHeader().setStyle("-fx-background-color: " + mainclr + ";");
			clientController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
		}
		if (settingsController.SettingsController != null) {
			SettingsController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
		}
		if (trafficStopController.trafficStopController != null) {
			trafficStopController.trafficStopController.getRoot().setStyle("-fx-background-color: " + bkgclr);
		}
		if (userManagerController != null) {
			userManagerController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
		}
		if (vehLookupViewController != null) {
			vehLookupViewController.getLookupmainlbl().setStyle("-fx-text-fill: " + mainclr + ";-fx-font-size: 25;");
			vehLookupViewController.getLbl1().setStyle("-fx-text-fill: " + secclr + ";-fx-font-size: 25;");
			vehLookupViewController.getLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
			vehLookupViewController.getVehLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
			vehLookupViewController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
			
			vehLookupViewController.getInfo1().setStyle("-fx-background-color: " + secclr + ";");
			vehLookupViewController.getInfo2().setStyle("-fx-background-color: " + secclr + ";");
			
			vehLookupViewController.getProbabilitySettingsBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;");
			vehLookupViewController.getProbabilitySettingsBtn().setOnMouseEntered(e -> vehLookupViewController.getProbabilitySettingsBtn().setStyle(hoverStyle + ";-fx-text-fill: white;"));
			vehLookupViewController.getProbabilitySettingsBtn().setOnMouseExited(e -> vehLookupViewController.getProbabilitySettingsBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;"));
			
			vehLookupViewController.getVehSearchBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;");
			vehLookupViewController.getVehSearchBtn().setOnMouseEntered(e -> vehLookupViewController.getVehSearchBtn().setStyle(hoverStyle + ";-fx-text-fill: white;"));
			vehLookupViewController.getVehSearchBtn().setOnMouseExited(e -> vehLookupViewController.getVehSearchBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;"));
			
			vehLookupViewController.getAddDataToNotesBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			vehLookupViewController.getAddDataToNotesBtn().setOnMouseEntered(
					e -> vehLookupViewController.getAddDataToNotesBtn().setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			vehLookupViewController.getAddDataToNotesBtn().setOnMouseExited(
					e -> vehLookupViewController.getAddDataToNotesBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			vehLookupViewController.getBtninfo1().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			vehLookupViewController.getBtninfo1().setOnMouseEntered(e -> vehLookupViewController.getBtninfo1().setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			vehLookupViewController.getBtninfo1().setOnMouseExited(
					e -> vehLookupViewController.getBtninfo1().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			vehLookupViewController.getBtninfo2().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			vehLookupViewController.getBtninfo2().setOnMouseEntered(e -> vehLookupViewController.getBtninfo2().setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			vehLookupViewController.getBtninfo2().setOnMouseExited(
					e -> vehLookupViewController.getBtninfo2().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			vehLookupViewController.getBtninfo3().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			vehLookupViewController.getBtninfo3().setOnMouseEntered(e -> vehLookupViewController.getBtninfo3().setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			vehLookupViewController.getBtninfo3().setOnMouseExited(
					e -> vehLookupViewController.getBtninfo3().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
		}
		if (pedLookupViewController != null) {
			pedLookupViewController.getLookupmainlbl().setStyle("-fx-text-fill: " + mainclr + ";-fx-font-size: 25;");
			pedLookupViewController.getLbl1().setStyle("-fx-text-fill: " + secclr + ";-fx-font-size: 25;");
			pedLookupViewController.getLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
			pedLookupViewController.getPedLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
			pedLookupViewController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
			
			pedLookupViewController.getInfo1().setStyle("-fx-background-color: " + secclr + ";");
			pedLookupViewController.getInfo2().setStyle("-fx-background-color: " + secclr + ";");
			pedLookupViewController.getInfo3().setStyle("-fx-background-color: " + secclr + ";");
			pedLookupViewController.getInfo4().setStyle("-fx-background-color: " + secclr + ";");
			pedLookupViewController.getInfo5().setStyle("-fx-background-color: " + secclr + ";");
			
			pedLookupViewController.getProbabilitySettingsBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;");
			pedLookupViewController.getProbabilitySettingsBtn().setOnMouseEntered(e -> pedLookupViewController.getProbabilitySettingsBtn().setStyle(hoverStyle + ";-fx-text-fill: white;"));
			pedLookupViewController.getProbabilitySettingsBtn().setOnMouseExited(e -> pedLookupViewController.getProbabilitySettingsBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;"));
			
			pedLookupViewController.getPedSearchBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;");
			pedLookupViewController.getPedSearchBtn().setOnMouseEntered(e -> pedLookupViewController.getPedSearchBtn().setStyle(hoverStyle + ";-fx-text-fill: white;"));
			pedLookupViewController.getPedSearchBtn().setOnMouseExited(e -> pedLookupViewController.getPedSearchBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white;"));
			
			pedLookupViewController.getAddDataToNotesBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			pedLookupViewController.getAddDataToNotesBtn().setOnMouseEntered(
					e -> pedLookupViewController.getAddDataToNotesBtn().setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			pedLookupViewController.getAddDataToNotesBtn().setOnMouseExited(
					e -> pedLookupViewController.getAddDataToNotesBtn().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			pedLookupViewController.getInfobtn1().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			pedLookupViewController.getInfobtn1().setOnMouseEntered(e -> pedLookupViewController.getInfobtn1().setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			pedLookupViewController.getInfobtn1().setOnMouseExited(
					e -> pedLookupViewController.getInfobtn1().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			pedLookupViewController.getInfobtn2().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			pedLookupViewController.getInfobtn2().setOnMouseEntered(e -> pedLookupViewController.getInfobtn2().setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			pedLookupViewController.getInfobtn2().setOnMouseExited(
					e -> pedLookupViewController.getInfobtn2().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			
			pedLookupViewController.getInfobtn3().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12");
			pedLookupViewController.getInfobtn3().setOnMouseEntered(e -> pedLookupViewController.getInfobtn3().setStyle(hoverStyle + ";-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
			pedLookupViewController.getInfobtn3().setOnMouseExited(
					e -> pedLookupViewController.getInfobtn3().setStyle(nonTransparentBtn + "-fx-text-fill: white; -fx-padding: 4 10; -fx-font-size: 12"));
		}
		if (courtViewController != null) {
			courtViewController.getCaseSec1().setStyle("-fx-text-fill: " + secclr + ";");
			courtViewController.getCasePrim1().setStyle("-fx-text-fill: " + mainclr + "; -fx-border-color: black; -fx-border-width: 0 0 1.5 0;");
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
			calloutViewController.getCalloutPane().setStyle("-fx-background-color: " + bkgclr + ";");
			CalloutManager.loadActiveCallouts(calloutViewController.getCalActiveList());
			CalloutManager.loadHistoryCallouts(calloutViewController.getCalHistoryList());
			
			calloutViewController.getCalActiveList().setStyle(updateStyleProperty(calloutViewController.getCalActiveList(), "-fx-border-color", accclr));
			calloutViewController.getCalHistoryList().setStyle(updateStyleProperty(calloutViewController.getCalHistoryList(), "-fx-border-color", accclr));
			
			calloutViewController.getActivecalfill().setStyle(updateStyleProperty(calloutViewController.getActivecalfill(), "-fx-border-color", mainclr));
			calloutViewController.getCalfill().setStyle(updateStyleProperty(calloutViewController.getCalfill(), "-fx-border-color", mainclr));
			
			calloutViewController.getRoot().setStyle("-fx-background-color: " + bkgclr + ";");
			
		}
		if (mainDesktopControllerObj != null) {
			mainDesktopControllerObj.getServerStatusLabel().setStyle("-fx-label-padding: 5; -fx-border-radius: 5;");
			mainDesktopControllerObj.getTopBar().setStyle("-fx-background-color: " + ConfigReader.configRead("desktopSettings", "topBarColor") + ";");
			String topBarText = ConfigReader.configRead("desktopSettings", "topBarTextColor");
			mainDesktopControllerObj.getOfficerInfoName().setStyle("-fx-text-fill: " + topBarText + ";");
			mainDesktopControllerObj.getVersionLabel().setStyle("-fx-text-fill: " + topBarText + ";");
			mainDesktopControllerObj.getDateLabel().setStyle("-fx-text-fill: " + topBarText + ";");
			mainDesktopControllerObj.getTimeLabel().setStyle("-fx-text-fill: " + topBarText + ";");
			mainDesktopControllerObj.getTopBar1().setStyle("-fx-text-fill: " + topBarText + ";");
			mainDesktopControllerObj.getTopBar2().setStyle("-fx-text-fill: " + topBarText + ";");
			if (mainDesktopControllerObj.getLocationDataLabel() != null) {
				mainDesktopControllerObj.getLocationDataLabel().setStyle("-fx-text-fill: " + topBarText + ";");
			}
			if (isConnected) {
				mainDesktopControllerObj.getServerStatusLabel().setStyle("-fx-text-fill: darkgreen; -fx-label-padding: 5; -fx-border-radius: 5;");
			} else {
				mainDesktopControllerObj.getServerStatusLabel().setStyle("-fx-text-fill: darkred; -fx-label-padding: 5; -fx-border-radius: 5;");
			}
		}
		
		if (ConfigReader.configRead("uiColors", "UIDarkMode").equals("true")) {
			addDarkStyles();
		} else {
			addLightStyles();
		}
	}
	
	private static HBox createNotificationPreview(String type) throws IOException {
		String textClr, primClr;
		switch (type) {
			case "info":
				textClr = ConfigReader.configRead("notificationSettings", "notificationInfoTextColor");
				primClr = ConfigReader.configRead("notificationSettings", "notificationInfoPrimary");
				break;
			case "warn":
				textClr = ConfigReader.configRead("notificationSettings", "notificationWarnTextColor");
				primClr = ConfigReader.configRead("notificationSettings", "notificationWarnPrimary");
				break;
			default:
				textClr = "#ffffff";
				primClr = "#db4437";
				break;
		}
		
		Label titleLabel = new Label("Sample Notification Title");
		titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: " + textClr + ";");
		
		Label messageLabel = new Label("Lorum ipsum dolor sit amet, consectetur adipiscing.");
		messageLabel.setWrapText(true);
		messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + textClr + ";");
		
		ImageView icon = new ImageView(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("imgs/icons/warning.png"))));
		icon.setImage(changeImageColor(icon.getImage(), textClr));
		icon.setFitWidth(24);
		icon.setFitHeight(24);
		
		ImageView closeIcon = new ImageView(new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("imgs/icons/cross.png"))));
		closeIcon.setImage(changeImageColor(closeIcon.getImage(), textClr));
		closeIcon.setFitWidth(12);
		closeIcon.setFitHeight(13);
		
		Button closeButton = new Button();
		closeButton.setGraphic(closeIcon);
		closeButton.setStyle("-fx-background-color: transparent;");
		
		VBox contentBox = new VBox(5, titleLabel, messageLabel);
		contentBox.setAlignment(Pos.CENTER_LEFT);
		contentBox.setPadding(new Insets(0));
		contentBox.setStyle("-fx-background-color: " + primClr + "; -fx-background-radius: 7;");
		
		HBox mainBox = new HBox(10, icon, contentBox, closeButton);
		mainBox.setAlignment(Pos.CENTER_LEFT);
		mainBox.setPadding(new Insets(10));
		mainBox.setStyle("-fx-background-color: " + primClr + "; -fx-background-radius: 7;");
		
		mainBox.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
		
		return mainBox;
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
			calloutViewController.getActivecalfill().setStyle(updateStyleProperty(calloutViewController.getActivecalfill(), "-fx-text-fill", UIDarkColor));
			calloutViewController.getCalfill().setStyle(updateStyleProperty(calloutViewController.getCalfill(), "-fx-text-fill", UIDarkColor));
			
		}
		if (clientController != null) {
			clientController.getLbl1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			clientController.getLbl2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			clientController.getLbl3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			clientController.getLbl4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		}
		if (userManagerController != null) {
			userManagerController.getLbl1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			userManagerController.getAgencyDropDown().setStyle(updateStyleProperty(userManagerController.getAgencyDropDown(), "-fx-text-fill", UIDarkColor));
			userManagerController.getDivisionDropDown().setStyle(updateStyleProperty(userManagerController.getDivisionDropDown(), "-fx-text-fill", UIDarkColor));
			userManagerController.getRankDropdown().setStyle(updateStyleProperty(userManagerController.getRankDropdown(), "-fx-text-fill", UIDarkColor));
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
			calloutViewController.getActivecalfill().setStyle(updateStyleProperty(calloutViewController.getActivecalfill(), "-fx-text-fill", UILightColor));
			calloutViewController.getCalfill().setStyle(updateStyleProperty(calloutViewController.getCalfill(), "-fx-text-fill", UILightColor));
			
		}
		if (clientController != null) {
			clientController.getLbl1().setStyle("-fx-text-fill: " + UILightColor + ";");
			clientController.getLbl2().setStyle("-fx-text-fill: " + UILightColor + ";");
			clientController.getLbl3().setStyle("-fx-text-fill: " + UILightColor + ";");
			clientController.getLbl4().setStyle("-fx-text-fill: " + UILightColor + ";");
		}
		if (userManagerController != null) {
			userManagerController.getLbl1().setStyle("-fx-text-fill: " + UILightColor + ";");
			userManagerController.getAgencyDropDown().setStyle(updateStyleProperty(userManagerController.getAgencyDropDown(), "-fx-text-fill", UILightColor));
			userManagerController.getDivisionDropDown().setStyle(updateStyleProperty(userManagerController.getDivisionDropDown(), "-fx-text-fill", UILightColor));
			userManagerController.getRankDropdown().setStyle(updateStyleProperty(userManagerController.getRankDropdown(), "-fx-text-fill", UILightColor));
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
		
		previewNotificationBtn.setOnAction(actionEvent -> {
			if (selectedNotification.get().equals("Information")) {
				NotificationManager.showNotificationInfo("Sample Info Notification", "Lorum ipsum dolor sit amet, consectetur adipiscing elit.");
			}
			if (selectedNotification.get().equals("Warning")) {
				NotificationManager.showNotificationWarning("Sample Warning Notification", "Lorum ipsum dolor sit amet, consectetur adipiscing elit.");
			}
		});
		
		setActive(paneWindow);
		
		loadPaneActions();
		
		refreshNotificationPreview("info");
		
		isInitialized = true;
		
		addLocale();
	}
	
	private void addLocale() {
		//Main
		mainHeader.setText(localization.getLocalizedMessage("Settings.MainHeader", "Settings"));
		mainSettingsSubheader.setText(localization.getLocalizedMessage("Settings.MainHeader", "Settings"));
		colorSettingsSubheader.setText(localization.getLocalizedMessage("Settings.MainHeader", "Settings"));
		MiscSettingsSubheader.setText(localization.getLocalizedMessage("Settings.MainHeader", "Settings"));
		developerSubheader.setText(localization.getLocalizedMessage("Settings.MainHeader", "Settings"));
		
		//Window
		winSettingsHeader.setText(localization.getLocalizedMessage("Settings.WindowSettingsHeader", "WINDOW SETTINGS"));
		appDisLabel.setText(localization.getLocalizedMessage("Settings.AppDisLabel", "Application Display Setting"));
		appDisLabelTT.setText(localization.getLocalizedMessage("Settings.AppDisLabelTT", "Change application window startup size"));
		aotLabel.setText(localization.getLocalizedMessage("Settings.AOTLabel", "Application Always On Top"));
		aotLabelTT.setText(localization.getLocalizedMessage("Settings.AOTLabelTT", "Keeps application above other windows (If set to windowed fullscreen)"));
		IDPopupsLabel.setText(localization.getLocalizedMessage("Settings.IDPopupsLabel", "Enable ID Popups"));
		IDPopupsTT.setText(localization.getLocalizedMessage("Settings.IDPopupsTT", "Toggle whether IDs will pop up when received"));
		IDDisplayDurLabel.setText(localization.getLocalizedMessage("Settings.IDDisplayDurLabel", "ID Display Duration"));
		IDDisplayDurTT.setText(localization.getLocalizedMessage("Settings.IDDisplayDurTT", "Duration ID window will be displayed"));
		enableCalloutLabel.setText(localization.getLocalizedMessage("Settings.EnableCalloutLabel", "Enable Callout Popups"));
		enableCalloutTT.setText(localization.getLocalizedMessage("Settings.EnableCalloutTT", "Toggle whether callouts will pop up when received"));
		CalDisplayDurLabel.setText(localization.getLocalizedMessage("Settings.CalDisplayDurLabel", "Callout Display Duration"));
		CalDisplayDurTT.setText(localization.getLocalizedMessage("Settings.CalDisplayDurTT", "Duration callout window will be displayed"));
		trafficStopPopupsLabel.setText(localization.getLocalizedMessage("Settings.TrafficStopPopupsLabel", "Enable Traffic Stop Popups"));
		trafficStopPopupsTT.setText(localization.getLocalizedMessage("Settings.TrafficStopPopupsTT", "Toggle whether traffic stop info window will come up when a traffic stop is initiated"));
		trafficStopDurLabel.setText(localization.getLocalizedMessage("Settings.TrafficStopDurLabel", "Traffic Stop Display Duration"));
		trafficStopDurTT.setText(localization.getLocalizedMessage("Settings.TrafficStopDurTT", "Duration traffic stop window will be shown"));
		
		//Notification
		notiSettingsHeader.setText(localization.getLocalizedMessage("Settings.NotiSettingsHeader", "NOTIFICATION SETTINGS"));
		recNotiLabel.setText(localization.getLocalizedMessage("Settings.RecNotiLabel", "Receive Notifications"));
		recNotiTT.setText(localization.getLocalizedMessage("Settings.RecNotiTT", "Toggle whether ALL notifications will be enabled"));
		fadeOutTimeLabel.setText(localization.getLocalizedMessage("Settings.FadeOutTimeLabel", "Fade Out Time"));
		fadeOutTimeTT.setText(localization.getLocalizedMessage("Settings.FadeOutTimeTT", "Time it takes for notifications to fade away (Sec)"));
		displayDurLabel.setText(localization.getLocalizedMessage("Settings.DisplayDurLabel", "Display Duration"));
		displayDurLabelTT.setText(localization.getLocalizedMessage("Settings.DisplayDurLabelTT", "Time notification is displayed before fading (Sec)"));
		selNotiLabel.setText(localization.getLocalizedMessage("Settings.SelNotiLabel", "Selected Notification"));
		selNotiTT.setText(localization.getLocalizedMessage("Settings.SelNotiTT", "Notification to modify below"));
		notiPosLabel.setText(localization.getLocalizedMessage("Settings.NotiPosLabel", "Position"));
		notiPosTT.setText(localization.getLocalizedMessage("Settings.NotiPosTT", "Location of notifications"));
		notiPrimColorLabel.setText(localization.getLocalizedMessage("Settings.NotiPrimColorLabel", "Primary Color"));
		notiPrimColorTT.setText(localization.getLocalizedMessage("Settings.NotiPrimColorTT", "Main color of the notification"));
		notiTextColorLabel.setText(localization.getLocalizedMessage("Settings.NotiTextColorLabel", "Text Color"));
		notiTextColorTT.setText(localization.getLocalizedMessage("Settings.NotiTextColorTT", "Color of the text on the notification"));
		notiPreviewLabel.setText(localization.getLocalizedMessage("Settings.NotiPreviewLabel", "Preview:"));
		previewNotificationBtn.setText(localization.getLocalizedMessage("Settings.PreviewNotificationBtn", "Preview Notification"));
		resetNotiDefaultsBtn.setText(localization.getLocalizedMessage("Settings.ResetDefaultsLabel", "Reset Defaults"));
		saveDisplayDurBtn.setText(localization.getLocalizedMessage("Settings.NotiSaveButton", "Save"));
		saveFadeDurBtn.setText(localization.getLocalizedMessage("Settings.NotiSaveButton", "Save"));
		
		//Desktop
		desktopSetingsHeader.setText(localization.getLocalizedMessage("Settings.DesktopSettingsHeader", "DESKTOP SETTINGS"));
		desktopUseSolidClrLabel.setText(localization.getLocalizedMessage("Settings.DesktopUseSolidClrLabel", "Use Solid Color"));
		desktopUseSolidClrTT.setText(localization.getLocalizedMessage("Settings.DesktopUseSolidClrTT", "Desktop background will be set to a solid color"));
		desktopBackgroundClrLabel.setText(localization.getLocalizedMessage("Settings.DesktopBackgroundClrLabel", "Desktop Background Color"));
		desktopBackgroundClrTT.setText(localization.getLocalizedMessage("Settings.DesktopBackgroundClrTT", "Set the color of the desktop background (If solid color enabled)"));
		useDesktopBackgroundImageLabel.setText(localization.getLocalizedMessage("Settings.UseDesktopBackgroundImageLabel", "Use Desktop Background"));
		useDesktopBackgroundImageTT.setText(localization.getLocalizedMessage("Settings.UseDesktopBackgroundImageTT", "Desktop background will be set to a specified image"));
		desktopImageChooserLabel.setText(localization.getLocalizedMessage("Settings.DesktopImageChooserLabel", "Desktop Image Chooser"));
		desktopImageChooserTT.setText(
				localization.getLocalizedMessage("Settings.DesktopImageChooserTT", "Choose the image to use as the desktop background (Matching monitor aspect ratio will work best)"));
		chooseImageFileBtn.setText(localization.getLocalizedMessage("Settings.ChooseImageFileBtn", "Choose Image File"));
		desktopTopBarClrLabel.setText(localization.getLocalizedMessage("Settings.DesktopTopBarClrLabel", "Top Bar Color"));
		desktopTopBarClrTT.setText(localization.getLocalizedMessage("Settings.DesktopTopBarClrTT", "Set the color of the top information bar"));
		desktopTopBarTextClrLabel.setText(localization.getLocalizedMessage("Settings.DesktopTopBarTextClrLabel", "Top Bar Text Color"));
		desktopTopBarTextClrTT.setText(localization.getLocalizedMessage("Settings.DesktopTopBarTextClrTT", "Set the color of the text on the top bar"));
		
		//Report
		reportWindowCustomizationHeader.setText(localization.getLocalizedMessage("Settings.ReportWindowCustomizationHeader", "REPORT WINDOW CUSTOMIZATION"));
		reportDesignPresetLabel.setText(localization.getLocalizedMessage("Settings.ReportDesignPresetLabel", "Design Preset"));
		reportDesignPresetTT.setText(localization.getLocalizedMessage("Settings.ReportDesignPresetTT", "Preset designs for reports (Replaces what you have)"));
		reportTextColorLabel.setText(localization.getLocalizedMessage("Settings.ReportTextColorLabel", "Report Text Color"));
		reportTextColorTT.setText(localization.getLocalizedMessage("Settings.ReportTextColorTT", "Set the text color of textfields in the report window"));
		headingLabelReport.setText(localization.getLocalizedMessage("Settings.HeadingLabelReport", "Heading Color"));
		headingLabelReportTT.setText(localization.getLocalizedMessage("Settings.HeadingLabelReportTT", "Set the color of the heading / subheading text"));
		backgroundLabelReport.setText(localization.getLocalizedMessage("Settings.BackgroundLabelReport", "Background Color"));
		backgroundLabelReportTT.setText(localization.getLocalizedMessage("Settings.BackgroundLabelReportTT", "Set the color of the report window background"));
		secLabelReport.setText(localization.getLocalizedMessage("Settings.SecLabelReport", "Secondary Color"));
		secLabelReportTT.setText(localization.getLocalizedMessage("Settings.SecLabelReportTT", "Set the color of the fields in the report window"));
		accentLabelReport.setText(localization.getLocalizedMessage("Settings.AccentLabelReport", "Accent Color"));
		accentLabelReportTT.setText(localization.getLocalizedMessage("Settings.AccentLabelReportTT", "Set the color when selecting a button / field"));
		reportResetDefaultsLabel.setText(localization.getLocalizedMessage("Settings.ResetDefaultsLabel", "Reset Defaults"));
		reportResetDefaultsTT.setText(localization.getLocalizedMessage("Settings.ReportResetDefaultsTT", "Restore the default report color settings"));
		resetReportDefaultsBtn.setText(localization.getLocalizedMessage("Settings.ResetDefaultsLabel", "Reset Defaults"));
		
		//Application
		appWindowCustomizationHeader.setText(localization.getLocalizedMessage("Settings.AppWindowCustomizationHeader", "APPLICATION WINDOW CUSTOMIZATION"));
		appPresetLabel.setText(localization.getLocalizedMessage("Settings.AppPresetLabel", "Design Preset"));
		appPresetTT.setText(localization.getLocalizedMessage("Settings.AppPresetTT", "Preset designs for application (Replaces what you have)"));
		appTextColorLabel.setText(localization.getLocalizedMessage("Settings.AppTextColorLabel", "Application Text Color"));
		appTextColorTT.setText(localization.getLocalizedMessage("Settings.AppTextColorTT", "Set the text color of textfields in the application windows"));
		primLabel.setText(localization.getLocalizedMessage("Settings.PrimLabel", "Primary Color"));
		primTT.setText(localization.getLocalizedMessage("Settings.PrimTT", "Set the primary color of the application"));
		secLabel.setText(localization.getLocalizedMessage("Settings.SecLabel", "Secondary Color"));
		secTT.setText(localization.getLocalizedMessage("Settings.SecTT", "Set the secondary color of the application"));
		accLabel.setText(localization.getLocalizedMessage("Settings.AccLabel", "Accent Color"));
		accTT.setText(localization.getLocalizedMessage("Settings.AccTT", "Set the accent color of the application"));
		bkgLabel.setText(localization.getLocalizedMessage("Settings.BkgLabel", "Background Color"));
		bkgTT.setText(localization.getLocalizedMessage("Settings.BkgTT", "Set the background color of the application"));
		appResetDefaultsLabel.setText(localization.getLocalizedMessage("Settings.ResetDefaultsLabel", "Reset Defaults"));
		appResetDefaultsTT.setText(localization.getLocalizedMessage("Settings.AppResetDefaultsTT", "Restore the default application color settings"));
		resetDefaultsBtn.setText(localization.getLocalizedMessage("Settings.ResetDefaultsLabel", "Reset Defaults"));
		
		//Networking
		networkingSettingsHeader.setText(localization.getLocalizedMessage("Settings.NetworkingSettingsHeader", "SERVER / NETWORKING SETTINGS"));
		autoConnectServerLabel.setText(localization.getLocalizedMessage("Settings.AutoConnectServerLabel", "Server Autoconnect"));
		autoConnectServerTT.setText(localization.getLocalizedMessage("Settings.AutoConnectServerTT", "Toggler whether application will try to autoconnect"));
		broadcastPortLabel.setText(localization.getLocalizedMessage("Settings.BroadcastPortLabel", "Broadcast Port"));
		broadcastPortTT.setText(localization.getLocalizedMessage("Settings.BroadcastPortTT", "Default port for the autoconnect broadcast"));
		socketTimeoutLabel.setText(localization.getLocalizedMessage("Settings.SocketTimeoutLabel", "Server Socket Timeout"));
		socketTimeoutTT.setText(localization.getLocalizedMessage("Settings.SocketTimeoutTT", "Timeout for server when connection is lost (ms)"));
		broadcastPortField.setPromptText(localization.getLocalizedMessage("Settings.DefaultLabel", "Default:" + " 8888"));
		socketTimeoutField.setPromptText(localization.getLocalizedMessage("Settings.DefaultLabel", "Default:" + " 10000"));
		
		//Audio
		audioSettingsHeader.setText(localization.getLocalizedMessage("Settings.AudioSettingsHeader", "AUDIO SETTINGS"));
		enableSoundsLabel.setText(localization.getLocalizedMessage("Settings.EnableSoundsLabel", "Enable Sounds"));
		enableSoundsTT.setText(localization.getLocalizedMessage("Settings.EnableSoundsTT", "Toggle whether ALL sounds will be enabled"));
		lookupWarningSoundLabel.setText(localization.getLocalizedMessage("Settings.LookupWarningSoundLabel", "Lookup Warning Sound"));
		lookupWarningSoundTT.setText(localization.getLocalizedMessage("Settings.LookupWarningSoundTT", "Sound when suspect has warnings when looked up"));
		createReportSoundLabel.setText(localization.getLocalizedMessage("Settings.CreateReportSoundLabel", "Create Report Sound"));
		createReportSoundTT.setText(localization.getLocalizedMessage("Settings.CreateReportSoundTT", "Sound when report is created"));
		calloutSoundLabel.setText(localization.getLocalizedMessage("Settings.CalloutSoundLabel", "Callout Sound"));
		calloutSoundTT.setText(localization.getLocalizedMessage("Settings.CalloutSoundTT", "Sound when a callout is recieved"));
		delReportLabel.setText(localization.getLocalizedMessage("Settings.DelReportLabel", "Delete Report Sound"));
		delReportTT.setText(localization.getLocalizedMessage("Settings.DelReportTT", "Sound when report is deleted"));
		
		//Developer
		devSettingsHeader.setText(localization.getLocalizedMessage("Settings.DevSettingsHeader", "DEVELOPER SETTINGS / CLEAR DATA"));
		clearLogsLabel.setText(localization.getLocalizedMessage("Settings.ClearLogsLabel", "Clear Logs"));
		clearLogsTT.setText(localization.getLocalizedMessage("Settings.ClearLogsTT", "Delete all saved logs in log browser"));
		clrLogsBtn.setText(localization.getLocalizedMessage("Settings.ClearLogsBtn", "CLEAR LOGS"));
		clearSaveDataLabel.setText(localization.getLocalizedMessage("Settings.ClearSaveDataLabel", "Clear ALL Save Data"));
		clearSaveDataTT.setText(localization.getLocalizedMessage("Settings.ClearSaveDataTT", "Delete all save data / start fresh"));
		clrSaveDataBtn.setText(localization.getLocalizedMessage("Settings.ClearSaveDataBtn", "CLEAR ALL SAVE DATA"));
		centerWindowsLabel.setText(localization.getLocalizedMessage("Settings.CenterWindowsLabel", "Center All Windows"));
		centerWindowsTT.setText(localization.getLocalizedMessage("Settings.CenterWindowsTT", "Center all windows on the screen in case of issues"));
		centerWindowsBtn.setText(localization.getLocalizedMessage("Settings.CenterWindowsBtn", "CENTER WINDOWS"));
		
		//LeftButtons
		windowSettingsBtn.setText(localization.getLocalizedMessage("Settings.WindowSettingsBtn", "Window Settings"));
		notiSettingsBtn.setText(localization.getLocalizedMessage("Settings.NotiSettingsBtn", "Notification Settings"));
		desktopSettingsBtn.setText(localization.getLocalizedMessage("Settings.DesktopSettingsBtn", "Desktop Settings"));
		reportDesignBtn.setText(localization.getLocalizedMessage("Settings.ReportDesignBtn", "Report Design"));
		appDesignBtn.setText(localization.getLocalizedMessage("Settings.AppDesignBtn", "Application Design"));
		serverBtn.setText(localization.getLocalizedMessage("Settings.ServerBtn", "Networking / Server"));
		audioBtn.setText(localization.getLocalizedMessage("Settings.AudioBtn", "Audio"));
		devBtn.setText(localization.getLocalizedMessage("Settings.DevBtn", "Developer"));
		
	}
	
	@javafx.fxml.FXML
	public void clearSaveDataBtnClick(ActionEvent actionEvent) {
		Stage stage = (Stage) root.getScene().getWindow();
		confirmSaveDataClearDialog(stage);
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
	public void serverAutoConnectClick(ActionEvent actionEvent) {
		handleCheckboxClick("connectionSettings", "serverAutoConnect", serverAutoconnectTogglebox);
	}
	
	@javafx.fxml.FXML
	public void enableIDPopupClick(ActionEvent actionEvent) {
		handleCheckboxClick("uiSettings", "enableIDPopup", enableIDPopupsCheckbox);
	}
	
	@javafx.fxml.FXML
	public void enableCalloutPopupClick(ActionEvent actionEvent) {
		handleCheckboxClick("uiSettings", "enableCalloutPopup", enableCalloutPopupsCheckbox);
	}
	
	@javafx.fxml.FXML
	public void enableSoundCheckboxClick(ActionEvent actionEvent) {
		handleCheckboxClick("uiSettings", "enableSounds", enableSoundCheckbox);
	}
	
	@javafx.fxml.FXML
	public void audioCalloutClick(ActionEvent actionEvent) {
		handleCheckboxClick("soundSettings", "playCallout", audioCalloutCheckbox);
	}
	
	@javafx.fxml.FXML
	public void audioReportDeleteClick(ActionEvent actionEvent) {
		handleCheckboxClick("soundSettings", "playDeleteReport", audioReportDeleteCheckbox);
	}
	
	@javafx.fxml.FXML
	public void audioReportCreateClick(ActionEvent actionEvent) {
		handleCheckboxClick("soundSettings", "playCreateReport", audioReportCreate);
	}
	
	@javafx.fxml.FXML
	public void audioLookupWarning(ActionEvent actionEvent) {
		handleCheckboxClick("soundSettings", "playLookupWarning", audioLookupWarningCheckbox);
	}
	
	@javafx.fxml.FXML
	public void enableNoti(ActionEvent actionEvent) {
		handleCheckboxClick("notificationSettings", "enabled", enableNotiTB);
	}
	
	@javafx.fxml.FXML
	public void enableTrafficStopPopupClick(ActionEvent actionEvent) {
		handleCheckboxClick("uiSettings", "enableTrafficStopPopup", enableTrafficStopPopupsCheckbox);
	}
	
	@javafx.fxml.FXML
	public void AOTCheckbox(ActionEvent actionEvent) {
		handleCheckboxClick("uiSettings", "windowAOT", alwaysOnTopCheckbox);
	}
	
	@javafx.fxml.FXML
	public void backgroundToggle(ActionEvent actionEvent) {
		solidColorToggle.setOnAction(null);
		
		if (backgroundToggle.isSelected()) {
			ConfigWriter.configwrite("desktopSettings", "useBackground", "true");
			ConfigWriter.configwrite("desktopSettings", "useSolidColor", "false");
			solidColorToggle.setSelected(false);
		} else {
			ConfigWriter.configwrite("desktopSettings", "useSolidColor", "true");
			ConfigWriter.configwrite("desktopSettings", "useBackground", "false");
			solidColorToggle.setSelected(true);
		}
		updateDesktopBackground(mainDesktopControllerObj.getContainer());
		solidColorToggle.setOnAction(this::solidColorToggle);
	}
	
	@javafx.fxml.FXML
	public void solidColorToggle(ActionEvent actionEvent) {
		backgroundToggle.setOnAction(null);
		
		if (solidColorToggle.isSelected()) {
			ConfigWriter.configwrite("desktopSettings", "useBackground", "false");
			ConfigWriter.configwrite("desktopSettings", "useSolidColor", "true");
			backgroundToggle.setSelected(false);
		} else {
			ConfigWriter.configwrite("desktopSettings", "useSolidColor", "false");
			ConfigWriter.configwrite("desktopSettings", "useBackground", "true");
			backgroundToggle.setSelected(true);
		}
		updateDesktopBackground(mainDesktopControllerObj.getContainer());
		backgroundToggle.setOnAction(this::backgroundToggle);
	}
	
	@javafx.fxml.FXML
	public void desktopImageBtn(ActionEvent actionEvent) {
		String path = openFileSelectionDialog(mainDesktopStage);
		log("Wrote image path" + path + " to config", LogUtils.Severity.DEBUG);
		if (path != null) {
			ConfigWriter.configwrite("desktopSettings", "backgroundPath", path);
			updateDesktopBackground(mainDesktopControllerObj.getContainer());
		}
	}
	
	@javafx.fxml.FXML
	public void centerWindowsBtnClick(ActionEvent actionEvent) {
		for (CustomWindow window : windows.values()) {
			window.centerOnDesktop();
		}
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
			desktopBackgroundPicker.setValue(Color.valueOf(ConfigReader.configRead("desktopSettings", "desktopColor")));
			topBarPicker.setValue(Color.valueOf(ConfigReader.configRead("desktopSettings", "topBarColor")));
			topBarTextPicker.setValue(Color.valueOf(ConfigReader.configRead("desktopSettings", "topBarTextColor")));
			secPicker.setValue(secondary);
			accPicker.setValue(accent);
			bkgPicker.setValue(bkg);
			primLabel.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			secLabel.setStyle("-fx-text-fill: " + toHexString(secondary) + ";");
			accLabel.setStyle("-fx-text-fill: " + toHexString(accent) + ";");
			
			if (toHexString(bkg).equalsIgnoreCase("#ffffff") || toHexString(bkg).equalsIgnoreCase("#f2f2f2") || toHexString(bkg).equalsIgnoreCase("#e6e6e6") || toHexString(bkg).equalsIgnoreCase(
					"#cccccc")) {
				bkgLabel.setStyle("-fx-text-fill: black;");
			} else {
				bkgLabel.setStyle("-fx-text-fill: " + toHexString(bkg) + ";");
			}
			
			backgroundPickerReport.setValue(reportBackground);
			accentPickerReport.setValue(reportAccent);
			headingPickerReport.setValue(reportHeading);
			secPickerReport.setValue(reportSecondary);
			
			if (selectedNotification.get().equals("Information")) {
				notiTextColorPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationInfoTextColor")));
				notiPrimPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationInfoPrimary")));
			} else {
				notiTextColorPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationWarnTextColor")));
				notiPrimPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationWarnPrimary")));
			}
			
			backgroundLabelReport.setStyle("-fx-text-fill: " + toHexString(reportBackground) + ";");
			accentLabelReport.setStyle("-fx-text-fill: " + toHexString(reportAccent) + ";");
			secLabelReport.setStyle("-fx-text-fill: " + toHexString(reportSecondary) + ";");
		} catch (IOException e) {
			logError("LoadTheme IO Error Code 917 ", e);
		}
	}
	
	private void loadPaneActions() {
		audioBtn.setOnAction(actionEvent -> {
			setActive(paneAudio);
		});
		appDesignBtn.setOnAction(actionEvent -> {
			setActive(paneApplication);
		});
		devBtn.setOnAction(actionEvent -> {
			setActive(paneDeveloper);
		});
		notiSettingsBtn.setOnAction(actionEvent -> {
			setActive(paneNotification);
		});
		reportDesignBtn.setOnAction(actionEvent -> {
			setActive(paneReport);
		});
		windowSettingsBtn.setOnAction(actionEvent -> {
			setActive(paneWindow);
		});
		serverBtn.setOnAction(actionEvent -> {
			setActive(paneNetworking);
		});
		desktopSettingsBtn.setOnAction(actionEvent -> {
			setActive(paneDesktop);
		});
	}
	
	private void closeWindows() {
		paneAudio.setVisible(false);
		paneApplication.setVisible(false);
		paneDeveloper.setVisible(false);
		paneNotification.setVisible(false);
		paneReport.setVisible(false);
		paneWindow.setVisible(false);
		paneNetworking.setVisible(false);
		paneDesktop.setVisible(false);
		paneAudio.setDisable(true);
		paneApplication.setDisable(true);
		paneDeveloper.setDisable(true);
		paneNotification.setDisable(true);
		paneReport.setDisable(true);
		paneWindow.setDisable(true);
		paneNetworking.setDisable(true);
		paneDesktop.setDisable(true);
	}
	
	private void setActive(AnchorPane pane) {
		closeWindows();
		pane.setDisable(false);
		pane.setVisible(true);
	}
	
	private void addDefaultCheckboxSelections() throws IOException {
		notiDisplayDurField.setText(ConfigReader.configRead("notificationSettings", "displayDuration"));
		notiFadeOutDurField.setText(ConfigReader.configRead("notificationSettings", "fadeOutDuration"));
		broadcastPortField.setText(ConfigReader.configRead("connectionSettings", "broadcastPort"));
		socketTimeoutField.setText(ConfigReader.configRead("connectionSettings", "socketTimeout"));
		
		audioLookupWarningCheckbox.setSelected(ConfigReader.configRead("soundSettings", "playLookupWarning").equalsIgnoreCase("true"));
		
		audioCalloutCheckbox.setSelected(ConfigReader.configRead("soundSettings", "playCallout").equalsIgnoreCase("true"));
		
		audioReportCreate.setSelected(ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true"));
		
		audioReportDeleteCheckbox.setSelected(ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true"));
		
		enableCalloutPopupsCheckbox.setSelected(ConfigReader.configRead("uiSettings", "enableCalloutPopup").equalsIgnoreCase("true"));
		
		enableSoundCheckbox.setSelected(ConfigReader.configRead("uiSettings", "enableSounds").equalsIgnoreCase("true"));
		
		enableIDPopupsCheckbox.setSelected(ConfigReader.configRead("uiSettings", "enableIDPopup").equalsIgnoreCase("true"));
		
		enableTrafficStopPopupsCheckbox.setSelected(ConfigReader.configRead("uiSettings", "enableTrafficStopPopup").equalsIgnoreCase("true"));
		
		serverAutoconnectTogglebox.setSelected(ConfigReader.configRead("connectionSettings", "serverAutoConnect").equalsIgnoreCase("true"));
		
		alwaysOnTopCheckbox.setSelected(ConfigReader.configRead("uiSettings", "windowAOT").equalsIgnoreCase("true"));
		
		solidColorToggle.setSelected(ConfigReader.configRead("desktopSettings", "useSolidColor").equalsIgnoreCase("true"));
		
		backgroundToggle.setSelected(ConfigReader.configRead("desktopSettings", "useBackground").equalsIgnoreCase("true"));
		
		enableNotiTB.setSelected(ConfigReader.configRead("notificationSettings", "enabled").equalsIgnoreCase("true"));
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
				
				refreshNotificationPreview("info");
			}
			if (selectedNotification.get().equals("Warning")) {
				updateWarnNotiPrim(selectedColor);
				
				refreshNotificationPreview("warn");
			}
		});
		
		notiTextColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			Color selectedColor = newValue;
			if (selectedNotification.get().equals("Information")) {
				updateInfoNotiTextColor(selectedColor);
				
				refreshNotificationPreview("info");
			}
			if (selectedNotification.get().equals("Warning")) {
				updateWarnNotiTextColor(selectedColor);
				
				refreshNotificationPreview("warn");
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
		
		desktopBackgroundPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			String hexColor = toHexString(newValue);
			ConfigWriter.configwrite("desktopSettings", "desktopColor", hexColor);
			updateDesktopBackground(mainDesktopControllerObj.getContainer());
		});
		
		topBarPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			if (mainDesktopControllerObj.getTopBar() != null) {
				mainDesktopControllerObj.getTopBar().setStyle("-fx-background-color: " + toHexString(newValue) + ";");
			}
			ConfigWriter.configwrite("desktopSettings", "topBarColor", toHexString(newValue));
		});
		
		topBarTextPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
			if (!isInitialized) {
				return;
			}
			
			if (mainDesktopControllerObj != null) {
				mainDesktopControllerObj.getOfficerInfoName().setStyle("-fx-text-fill: " + toHexString(newValue) + ";");
				mainDesktopControllerObj.getVersionLabel().setStyle("-fx-text-fill: " + toHexString(newValue) + ";");
				mainDesktopControllerObj.getDateLabel().setStyle("-fx-text-fill: " + toHexString(newValue) + ";");
				mainDesktopControllerObj.getTimeLabel().setStyle("-fx-text-fill: " + toHexString(newValue) + ";");
				mainDesktopControllerObj.getTopBar1().setStyle("-fx-text-fill: " + toHexString(newValue) + ";");
				mainDesktopControllerObj.getTopBar2().setStyle("-fx-text-fill: " + toHexString(newValue) + ";");
				if (mainDesktopControllerObj.getLocationDataLabel() != null) {
					mainDesktopControllerObj.getLocationDataLabel().setStyle("-fx-text-fill: " + toHexString(newValue) + ";");
				}
			}
			ConfigWriter.configwrite("desktopSettings", "topBarTextColor", toHexString(newValue));
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
		
		trafficStopDurComboBox.getItems().addAll(idDurations);
		trafficStopDurComboBox.setValue(ConfigReader.configRead("misc", "TrafficStopDuration"));
		trafficStopDurComboBox.setOnAction(actionEvent -> {
			String selectedDur = (String) trafficStopDurComboBox.getSelectionModel().getSelectedItem();
			ConfigWriter.configwrite("misc", "TrafficStopDuration", selectedDur);
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
						notiTextColorPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationInfoTextColor")));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					try {
						notiPrimPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationInfoPrimary")));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					
					refreshNotificationPreview("info");
					
				}
				case "Warning" -> {
					selectedNotification.set("Warning");
					try {
						notiTextColorPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationWarnTextColor")));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					try {
						notiPrimPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationWarnPrimary")));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					refreshNotificationPreview("warn");
				}
			}
			
		});
		
		String[] notificationPositions = {"BottomLeft", "BottomRight", "TopLeft", "TopRight"};
		notiPosCombobox.getItems().addAll(notificationPositions);
		notiPosCombobox.setValue(ConfigReader.configRead("notificationSettings", "notificationPosition"));
		notiPosCombobox.setOnAction(actionEvent -> {
			String selectedPosition = (String) notiPosCombobox.getSelectionModel().getSelectedItem();
			switch (selectedPosition) {
				case "BottomLeft" -> ConfigWriter.configwrite("notificationSettings", "notificationPosition", "BottomLeft");
				case "BottomRight" -> ConfigWriter.configwrite("notificationSettings", "notificationPosition", "BottomRight");
				case "TopLeft" -> ConfigWriter.configwrite("notificationSettings", "notificationPosition", "TopLeft");
				case "TopRight" -> ConfigWriter.configwrite("notificationSettings", "notificationPosition", "TopRight");
			}
		});
		
		String[] windowSetting = {"Fullscreen", "WindowedFullscreen"};
		windowDisplaySettingCombobox.getItems().addAll(windowSetting);
		windowDisplaySettingCombobox.setValue(ConfigReader.configRead("uiSettings", "windowDisplaySetting"));
		windowDisplaySettingCombobox.setOnAction(actionEvent -> {
			String selectedItem = (String) windowDisplaySettingCombobox.getSelectionModel().getSelectedItem();
			
			switch (selectedItem) {
				case "Fullscreen" -> {
					mainDesktopStage.setFullScreen(true);
					mainDesktopStage.centerOnScreen();
					ConfigWriter.configwrite("uiSettings", "windowDisplaySetting", "Fullscreen");
				}
				case "WindowedFullscreen" -> {
					if (mainDesktopStage.isFullScreen()) {
						mainDesktopStage.setFullScreen(false);
					}
					mainDesktopStage.setMaximized(true);
					mainDesktopStage.centerOnScreen();
					try {
						mainDesktopStage.setAlwaysOnTop(ConfigReader.configRead("uiSettings", "windowAOT").equalsIgnoreCase("true"));
					} catch (IOException e) {
						logError("Couldnt get windowAOT setting, ", e);
					}
					ConfigWriter.configwrite("uiSettings", "windowDisplaySetting", "WindowedFullscreen");
				}
			}
			
		});
		
		saveFadeDurBtn.setOnAction(actionEvent -> ConfigWriter.configwrite("notificationSettings", "fadeOutDuration", notiFadeOutDurField.getText()));
		saveDisplayDurBtn.setOnAction(actionEvent -> ConfigWriter.configwrite("notificationSettings", "displayDuration", notiDisplayDurField.getText()));
	}
	
	private void handleCheckboxClick(String section, String key, ToggleButton checkbox) {
		String value = checkbox.isSelected() ? "true" : "false";
		ConfigWriter.configwrite(section, key, value);
		checkbox.setSelected(Boolean.parseBoolean(value));
	}
	
	private void refreshNotificationPreview(String type) {
		try {
			notiDisplayPane.getChildren().removeAll();
			notiDisplayPane.setCenter(createNotificationPreview(type));
		} catch (IOException e) {
			logError("Error creating {" + type + "} notification preview: ", e);
		}
	}
	
	public String openFileSelectionDialog(Stage stage) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select an Image File");
		
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg"));
		
		File selectedFile = fileChooser.showOpenDialog(stage);
		
		return (selectedFile != null) ? selectedFile.getAbsolutePath() : null;
	}
	
	public BorderPane getRoot() {
		return root;
	}
}