package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.CalloutManager;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.Window.windowUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.server.ClientUtils.isConnected;

@SuppressWarnings({"ALL", "Convert2Diamond"})
public class settingsController {
	
	//TODO Finish broadcast port configuration
	//TODO Finish Save Button for broadcast port
	
	private static String UILightColor = "rgb(255,255,255,0.75)";
	private static String UIDarkColor = "rgb(0,0,0,0.75)";
	
	//<editor-fold desc="FXML">
	private static actionController controllerVar;
	AnchorPane topBar;
	@javafx.fxml.FXML
	private CheckBox startupFullscreenCheckbox;
	@javafx.fxml.FXML
	private ComboBox ReportWindowComboBox;
	@javafx.fxml.FXML
	private ComboBox mainWindowComboBox;
	@javafx.fxml.FXML
	private BorderPane root;
	@javafx.fxml.FXML
	private ComboBox notesWindowComboBox;
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
	private Label lbl0;
	@javafx.fxml.FXML
	private Label lbl1;
	@javafx.fxml.FXML
	private Label lbl2;
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
	
	//</editor-fold>
	
	public static void loadTheme() throws IOException {
		if (DataTerminalHomeApplication.controller != null) {
			controllerVar = DataTerminalHomeApplication.controller;
		} else if (newOfficerController.controller != null) {
			controllerVar = newOfficerController.controller;
		} else {
			log("Settings Controller Var could not be set", LogUtils.Severity.ERROR);
		}
		changeBarColors(controllerVar.getReportChart());
		changeStatisticColors(controllerVar.getAreaReportChart());
		
		String mainclr = ConfigReader.configRead("uiColors", "mainColor");
		controllerVar.getCalloutInfoTitle().setStyle("-fx-background-color: " + mainclr + ";");
		controllerVar.topPane.setStyle("-fx-background-color: " + mainclr + ";");
		controllerVar.mainColor9Bkg.setStyle("-fx-background-color: " + mainclr + ";");
		controllerVar.getLogManagerLabelBkg().setStyle("-fx-background-color: " + mainclr + ";");
		
		String secclr = ConfigReader.configRead("uiColors", "secondaryColor");
		controllerVar.getCurrentCalPane().setStyle("-fx-background-color: " + secclr + ";");
		controllerVar.getServerStatusLabel().setStyle(
				"-fx-border-color: " + secclr + "; -fx-label-padding: 5; -fx-border-radius: 5;");
		controllerVar.sidepane.setStyle("-fx-background-color: " + secclr + ";");
		controllerVar.getSecondaryColor3Bkg().setStyle("-fx-background-color: " + secclr + ";");
		controllerVar.getSecondaryColor4Bkg().setStyle("-fx-background-color: " + secclr + ";");
		controllerVar.getSecondaryColor5Bkg().setStyle("-fx-background-color: " + secclr + ";");
		
		String bkgclr = ConfigReader.configRead("uiColors", "bkgColor");
		controllerVar.getBkgclr1().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getBkgclr2().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getTabPane().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getArrestTable().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getCalloutTable().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getCitationTable().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getImpoundTable().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getIncidentTable().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getPatrolTable().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getSearchTable().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getTrafficStopTable().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getCalloutPane().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getVehLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getPedLookupPane().setStyle("-fx-background-color: " + bkgclr + ";");
		controllerVar.getLowerPane().setStyle("-fx-background-color: " + lowerPaneToToRGB(bkgclr, 0.4) + ";");
		
		String accclr = ConfigReader.configRead("uiColors", "accentColor");
		controllerVar.getReportPlusLabelFill().setStyle("-fx-text-fill: " + accclr + ";");
		controllerVar.mainColor8.setStyle("-fx-text-fill: " + accclr + ";");
		controllerVar.getDetailsLabelFill().setStyle("-fx-text-fill: " + accclr + ";");
		
		CalloutManager.loadActiveCallouts(controllerVar.getCalActiveList());
		CalloutManager.loadHistoryCallouts(controllerVar.getCalHistoryList());
		
		controllerVar.getCalActiveList().setStyle(
				updateStyleProperty(controllerVar.getCalActiveList(), "-fx-border-color", accclr));
		controllerVar.getCalHistoryList().setStyle(
				updateStyleProperty(controllerVar.getCalHistoryList(), "-fx-border-color", accclr));
		
		controllerVar.getActivecalfill().setStyle(
				updateStyleProperty(controllerVar.getActivecalfill(), "-fx-border-color", mainclr));
		controllerVar.getCalfill().setStyle(
				updateStyleProperty(controllerVar.getCalfill(), "-fx-border-color", mainclr));
		
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor");
		String initialStyle = "-fx-background-color: transparent;";
		String nonTransparentBtn = "-fx-background-color: " + accclr + ";";
		controllerVar.updateInfoBtn.setStyle(nonTransparentBtn);
		controllerVar.getShowManagerToggle().setStyle(nonTransparentBtn);
		controllerVar.getBtn1().setStyle(nonTransparentBtn);
		controllerVar.getBtn2().setStyle(nonTransparentBtn);
		controllerVar.getBtn3().setStyle(nonTransparentBtn);
		controllerVar.getBtn4().setStyle(nonTransparentBtn);
		controllerVar.getBtn5().setStyle(nonTransparentBtn);
		controllerVar.getBtn6().setStyle(nonTransparentBtn);
		controllerVar.getBtn7().setStyle(nonTransparentBtn);
		controllerVar.getBtn8().setStyle(nonTransparentBtn);
		
		controllerVar.getBtn1().setOnMouseEntered(e -> controllerVar.getBtn1().setStyle(hoverStyle));
		controllerVar.getBtn1().setOnMouseExited(e -> controllerVar.getBtn1().setStyle(nonTransparentBtn));
		controllerVar.getBtn2().setOnMouseEntered(e -> controllerVar.getBtn2().setStyle(hoverStyle));
		controllerVar.getBtn2().setOnMouseExited(e -> controllerVar.getBtn2().setStyle(nonTransparentBtn));
		controllerVar.getBtn3().setOnMouseEntered(e -> controllerVar.getBtn3().setStyle(hoverStyle));
		controllerVar.getBtn3().setOnMouseExited(e -> controllerVar.getBtn3().setStyle(nonTransparentBtn));
		controllerVar.getBtn4().setOnMouseEntered(e -> controllerVar.getBtn4().setStyle(hoverStyle));
		controllerVar.getBtn4().setOnMouseExited(e -> controllerVar.getBtn4().setStyle(nonTransparentBtn));
		controllerVar.getBtn5().setOnMouseEntered(e -> controllerVar.getBtn5().setStyle(hoverStyle));
		controllerVar.getBtn5().setOnMouseExited(e -> controllerVar.getBtn5().setStyle(nonTransparentBtn));
		controllerVar.getBtn6().setOnMouseEntered(e -> controllerVar.getBtn6().setStyle(hoverStyle));
		controllerVar.getBtn6().setOnMouseExited(e -> controllerVar.getBtn6().setStyle(nonTransparentBtn));
		controllerVar.getBtn7().setOnMouseEntered(e -> controllerVar.getBtn7().setStyle(hoverStyle));
		controllerVar.getBtn7().setOnMouseExited(e -> controllerVar.getBtn7().setStyle(nonTransparentBtn));
		controllerVar.getBtn8().setOnMouseEntered(e -> controllerVar.getBtn8().setStyle(hoverStyle));
		controllerVar.getBtn8().setOnMouseExited(e -> controllerVar.getBtn8().setStyle(nonTransparentBtn));
		controllerVar.getShowManagerToggle().setOnMouseEntered(
				e -> controllerVar.getShowManagerToggle().setStyle(hoverStyle));
		controllerVar.getShowManagerToggle().setOnMouseExited(
				e -> controllerVar.getShowManagerToggle().setStyle(nonTransparentBtn));
		controllerVar.shiftInfoBtn.setOnMouseEntered(e -> controllerVar.shiftInfoBtn.setStyle(hoverStyle));
		controllerVar.shiftInfoBtn.setOnMouseExited(e -> controllerVar.shiftInfoBtn.setStyle(initialStyle));
		controllerVar.getSettingsBtn().setOnMouseEntered(
				e -> controllerVar.getSettingsBtn().setStyle("-fx-background-color: " + secclr + ";"));
		controllerVar.getSettingsBtn().setOnMouseExited(e -> controllerVar.getSettingsBtn().setStyle(initialStyle));
		controllerVar.notesButton.setOnMouseEntered(e -> controllerVar.notesButton.setStyle(hoverStyle));
		controllerVar.notesButton.setOnMouseExited(e -> controllerVar.notesButton.setStyle(initialStyle));
		controllerVar.getCreateReportBtn().setOnMouseEntered(
				e -> controllerVar.getCreateReportBtn().setStyle(hoverStyle));
		controllerVar.getCreateReportBtn().setOnMouseExited(
				e -> controllerVar.getCreateReportBtn().setStyle(initialStyle));
		controllerVar.getLogsButton().setOnMouseEntered(e -> controllerVar.getLogsButton().setStyle(hoverStyle));
		controllerVar.getLogsButton().setOnMouseExited(e -> controllerVar.getLogsButton().setStyle(initialStyle));
		controllerVar.getMapButton().setOnMouseEntered(e -> controllerVar.getMapButton().setStyle(hoverStyle));
		controllerVar.getMapButton().setOnMouseExited(e -> controllerVar.getMapButton().setStyle(initialStyle));
		controllerVar.getShowIDBtn().setOnMouseEntered(e -> controllerVar.getShowIDBtn().setStyle(hoverStyle));
		controllerVar.getShowIDBtn().setOnMouseExited(e -> controllerVar.getShowIDBtn().setStyle(initialStyle));
		controllerVar.getShowCalloutBtn().setOnMouseEntered(
				e -> controllerVar.getShowCalloutBtn().setStyle(hoverStyle));
		controllerVar.getShowCalloutBtn().setOnMouseExited(
				e -> controllerVar.getShowCalloutBtn().setStyle(initialStyle));
		controllerVar.getLookupBtn().setOnMouseEntered(e -> controllerVar.getLookupBtn().setStyle(hoverStyle));
		controllerVar.getLookupBtn().setOnMouseExited(e -> controllerVar.getLookupBtn().setStyle(initialStyle));
		
		controllerVar.updateInfoBtn.setOnMouseEntered(e -> controllerVar.updateInfoBtn.setStyle(hoverStyle));
		controllerVar.updateInfoBtn.setOnMouseExited(e -> {
			controllerVar.updateInfoBtn.setStyle(nonTransparentBtn);
		});
		
		if (isConnected) {
			controllerVar.getServerStatusLabel().setStyle(
					"-fx-text-fill: #00da16; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
		} else {
			controllerVar.getServerStatusLabel().setStyle(
					"-fx-text-fill: #ff5e5e; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
		}
		
		if (ConfigReader.configRead("uiColors", "UIDarkMode").equals("true")) {
			addDarkStyles();
		} else {
			addLightStyles();
		}
		
		controllerVar.getServerStatusLabel().setStyle(
				updateStyleProperty(controllerVar.getServerStatusLabel(), "-fx-border-color", secclr));
	}
	
	private static void addDarkStyles() {
		controllerVar.getTabPane().getStyleClass().clear();
		controllerVar.getTabPane().getStyleClass().add("darktabpane");
		
		controllerVar.generatedByTag.setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.generatedDateTag.setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getVehplatefield().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPedrecordnamefield().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		
		controllerVar.getPlt1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPlt2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPlt3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPlt4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPlt5().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPlt6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPlt7().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		
		controllerVar.getPed1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPed2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPed3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPed4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPed5().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPed6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		controllerVar.getPed7().setStyle("-fx-text-fill: " + UIDarkColor + ";");
		
		controllerVar.getActivecalfill().setStyle(
				updateStyleProperty(controllerVar.getActivecalfill(), "-fx-text-fill", UIDarkColor));
		controllerVar.getCalfill().setStyle(
				updateStyleProperty(controllerVar.getCalfill(), "-fx-text-fill", UIDarkColor));
		
		controllerVar.getAreaReportChart().getStyleClass().clear();
		controllerVar.getAreaReportChart().getStyleClass().add("darkchart");
		controllerVar.getReportChart().getStyleClass().clear();
		controllerVar.getReportChart().getStyleClass().add("customchartdark");
		
		addDarkForm(controllerVar.getOfficerInfoName());
		addDarkForm(controllerVar.getOfficerInfoNumber());
		addDarkForm(controllerVar.getOfficerInfoCallsign());
		
		controllerVar.getOfficerInfoAgency().getStyleClass().clear();
		controllerVar.getOfficerInfoAgency().getStyleClass().add("combo-box");
		controllerVar.getOfficerInfoAgency().getStyleClass().add("combo-boxdark");
		controllerVar.getOfficerInfoRank().getStyleClass().clear();
		controllerVar.getOfficerInfoRank().getStyleClass().add("combo-box");
		controllerVar.getOfficerInfoRank().getStyleClass().add("combo-boxdark");
		controllerVar.getOfficerInfoDivision().getStyleClass().clear();
		controllerVar.getOfficerInfoDivision().getStyleClass().add("combo-box");
		controllerVar.getOfficerInfoDivision().getStyleClass().add("combo-boxdark");
		
		addDarkForm(controllerVar.getArrestaddress());
		addDarkForm(controllerVar.getArrestage());
		addDarkForm(controllerVar.getArrestambulance());
		addDarkForm(controllerVar.getArrestarea());
		addDarkForm(controllerVar.getArrestcharges());
		addDarkForm(controllerVar.getArrestcounty());
		addDarkForm(controllerVar.getArrestdesc());
		addDarkForm(controllerVar.getArrestdetails());
		addDarkForm(controllerVar.getArrestgender());
		addDarkForm(controllerVar.getArrestmedinfo());
		addDarkForm(controllerVar.getArrestname());
		addDarkForm(controllerVar.getArrestnum());
		addDarkForm(controllerVar.getArreststreet());
		addDarkForm(controllerVar.getArresttaser());
		addDarkForm(controllerVar.getCaladdress());
		addDarkForm(controllerVar.getCalarea());
		addDarkForm(controllerVar.getCalcounty());
		addDarkForm(controllerVar.getCalgrade());
		addDarkForm(controllerVar.getCalnotes());
		addDarkForm(controllerVar.getCalnum());
		addDarkForm(controllerVar.getCaltype());
		addDarkForm(controllerVar.getCitaddress());
		addDarkForm(controllerVar.getCitage());
		addDarkForm(controllerVar.getCitarea());
		addDarkForm(controllerVar.getCitcharges());
		addDarkForm(controllerVar.getCitcolor());
		addDarkForm(controllerVar.getCitcomments());
		addDarkForm(controllerVar.getCitcounty());
		addDarkForm(controllerVar.getCitdesc());
		addDarkForm(controllerVar.getCitgender());
		addDarkForm(controllerVar.getCitmodel());
		addDarkForm(controllerVar.getCitname());
		addDarkForm(controllerVar.getCitnumber());
		addDarkForm(controllerVar.getCitplatenum());
		addDarkForm(controllerVar.getCitstreet());
		addDarkForm(controllerVar.getCittype());
		addDarkForm(controllerVar.getCitvehother());
		addDarkForm(controllerVar.getImpaddress());
		addDarkForm(controllerVar.getImpage());
		addDarkForm(controllerVar.getImpcolor());
		addDarkForm(controllerVar.getImpcomments());
		addDarkForm(controllerVar.getImpgender());
		addDarkForm(controllerVar.getImpmodel());
		addDarkForm(controllerVar.getImpname());
		addDarkForm(controllerVar.getImpnum());
		addDarkForm(controllerVar.getImpplatenum());
		addDarkForm(controllerVar.getImptype());
		addDarkForm(controllerVar.getIncactionstaken());
		addDarkForm(controllerVar.getIncarea());
		addDarkForm(controllerVar.getInccomments());
		addDarkForm(controllerVar.getInccounty());
		addDarkForm(controllerVar.getIncnum());
		addDarkForm(controllerVar.getIncstatement());
		addDarkForm(controllerVar.getIncstreet());
		addDarkForm(controllerVar.getIncvictims());
		addDarkForm(controllerVar.getIncwitness());
		addDarkForm(controllerVar.getPatcomments());
		addDarkForm(controllerVar.getPatlength());
		addDarkForm(controllerVar.getPatnum());
		addDarkForm(controllerVar.getPatstarttime());
		addDarkForm(controllerVar.getPatstoptime());
		addDarkForm(controllerVar.getPatvehicle());
		addDarkForm(controllerVar.getSearcharea());
		addDarkForm(controllerVar.getSearchbacmeasure());
		addDarkForm(controllerVar.getSearchbreathresult());
		addDarkForm(controllerVar.getSearchbreathused());
		addDarkForm(controllerVar.getSearchcomments());
		addDarkForm(controllerVar.getSearchcounty());
		addDarkForm(controllerVar.getSearchgrounds());
		addDarkForm(controllerVar.getSearchmethod());
		addDarkForm(controllerVar.getSearchnum());
		addDarkForm(controllerVar.getSearchperson());
		addDarkForm(controllerVar.getSearchseizeditems());
		addDarkForm(controllerVar.getSearchstreet());
		addDarkForm(controllerVar.getSearchtype());
		addDarkForm(controllerVar.getSearchwitness());
		addDarkForm(controllerVar.getTrafaddress());
		addDarkForm(controllerVar.getTrafage());
		addDarkForm(controllerVar.getTrafarea());
		addDarkForm(controllerVar.getTrafcolor());
		addDarkForm(controllerVar.getTrafcomments());
		addDarkForm(controllerVar.getTrafcounty());
		addDarkForm(controllerVar.getTrafdesc());
		addDarkForm(controllerVar.getTrafgender());
		addDarkForm(controllerVar.getTrafmodel());
		addDarkForm(controllerVar.getTrafname());
		addDarkForm(controllerVar.getTrafnum());
		addDarkForm(controllerVar.getTrafotherinfo());
		addDarkForm(controllerVar.getTrafplatenum());
		addDarkForm(controllerVar.getTrafstreet());
		addDarkForm(controllerVar.getTraftype());
	}
	
	private static void addLightStyles() {
		controllerVar.getTabPane().getStyleClass().clear();
		controllerVar.getTabPane().getStyleClass().add("lighttabpane");
		
		controllerVar.generatedByTag.setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.generatedDateTag.setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getVehplatefield().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPedrecordnamefield().setStyle("-fx-text-fill: " + UILightColor + ";");
		
		controllerVar.getPlt1().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPlt2().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPlt3().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPlt4().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPlt5().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPlt6().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPlt7().setStyle("-fx-text-fill: " + UILightColor + ";");
		
		controllerVar.getPed1().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPed2().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPed3().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPed4().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPed5().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPed6().setStyle("-fx-text-fill: " + UILightColor + ";");
		controllerVar.getPed7().setStyle("-fx-text-fill: " + UILightColor + ";");
		
		controllerVar.getActivecalfill().setStyle(
				updateStyleProperty(controllerVar.getActivecalfill(), "-fx-text-fill", UILightColor));
		controllerVar.getCalfill().setStyle(
				updateStyleProperty(controllerVar.getCalfill(), "-fx-text-fill", UILightColor));
		
		controllerVar.getAreaReportChart().getStyleClass().clear();
		controllerVar.getAreaReportChart().getStyleClass().add("lightchart");
		controllerVar.getReportChart().getStyleClass().clear();
		controllerVar.getReportChart().getStyleClass().add("customchartlight");
		
		controllerVar.getOfficerInfoAgency().getStyleClass().clear();
		controllerVar.getOfficerInfoAgency().getStyleClass().add("combo-box");
		controllerVar.getOfficerInfoAgency().getStyleClass().add("combo-boxlight");
		controllerVar.getOfficerInfoRank().getStyleClass().clear();
		controllerVar.getOfficerInfoRank().getStyleClass().add("combo-box");
		controllerVar.getOfficerInfoRank().getStyleClass().add("combo-boxlight");
		controllerVar.getOfficerInfoDivision().getStyleClass().clear();
		controllerVar.getOfficerInfoDivision().getStyleClass().add("combo-box");
		controllerVar.getOfficerInfoDivision().getStyleClass().add("combo-boxlight");
		
		addLightForm(controllerVar.getOfficerInfoName());
		addLightForm(controllerVar.getOfficerInfoNumber());
		addLightForm(controllerVar.getOfficerInfoCallsign());
		addLightForm(controllerVar.getArrestaddress());
		addLightForm(controllerVar.getArrestage());
		addLightForm(controllerVar.getArrestambulance());
		addLightForm(controllerVar.getArrestarea());
		addLightForm(controllerVar.getArrestcharges());
		addLightForm(controllerVar.getArrestcounty());
		addLightForm(controllerVar.getArrestdesc());
		addLightForm(controllerVar.getArrestdetails());
		addLightForm(controllerVar.getArrestgender());
		addLightForm(controllerVar.getArrestmedinfo());
		addLightForm(controllerVar.getArrestname());
		addLightForm(controllerVar.getArrestnum());
		addLightForm(controllerVar.getArreststreet());
		addLightForm(controllerVar.getArresttaser());
		addLightForm(controllerVar.getCaladdress());
		addLightForm(controllerVar.getCalarea());
		addLightForm(controllerVar.getCalcounty());
		addLightForm(controllerVar.getCalgrade());
		addLightForm(controllerVar.getCalnotes());
		addLightForm(controllerVar.getCalnum());
		addLightForm(controllerVar.getCaltype());
		addLightForm(controllerVar.getCitaddress());
		addLightForm(controllerVar.getCitage());
		addLightForm(controllerVar.getCitarea());
		addLightForm(controllerVar.getCitcharges());
		addLightForm(controllerVar.getCitcolor());
		addLightForm(controllerVar.getCitcomments());
		addLightForm(controllerVar.getCitcounty());
		addLightForm(controllerVar.getCitdesc());
		addLightForm(controllerVar.getCitgender());
		addLightForm(controllerVar.getCitmodel());
		addLightForm(controllerVar.getCitname());
		addLightForm(controllerVar.getCitnumber());
		addLightForm(controllerVar.getCitplatenum());
		addLightForm(controllerVar.getCitstreet());
		addLightForm(controllerVar.getCittype());
		addLightForm(controllerVar.getCitvehother());
		addLightForm(controllerVar.getImpaddress());
		addLightForm(controllerVar.getImpage());
		addLightForm(controllerVar.getImpcolor());
		addLightForm(controllerVar.getImpcomments());
		addLightForm(controllerVar.getImpgender());
		addLightForm(controllerVar.getImpmodel());
		addLightForm(controllerVar.getImpname());
		addLightForm(controllerVar.getImpnum());
		addLightForm(controllerVar.getImpplatenum());
		addLightForm(controllerVar.getImptype());
		addLightForm(controllerVar.getIncactionstaken());
		addLightForm(controllerVar.getIncarea());
		addLightForm(controllerVar.getInccomments());
		addLightForm(controllerVar.getInccounty());
		addLightForm(controllerVar.getIncnum());
		addLightForm(controllerVar.getIncstatement());
		addLightForm(controllerVar.getIncstreet());
		addLightForm(controllerVar.getIncvictims());
		addLightForm(controllerVar.getIncwitness());
		addLightForm(controllerVar.getPatcomments());
		addLightForm(controllerVar.getPatlength());
		addLightForm(controllerVar.getPatnum());
		addLightForm(controllerVar.getPatstarttime());
		addLightForm(controllerVar.getPatstoptime());
		addLightForm(controllerVar.getPatvehicle());
		addLightForm(controllerVar.getSearcharea());
		addLightForm(controllerVar.getSearchbacmeasure());
		addLightForm(controllerVar.getSearchbreathresult());
		addLightForm(controllerVar.getSearchbreathused());
		addLightForm(controllerVar.getSearchcomments());
		addLightForm(controllerVar.getSearchcounty());
		addLightForm(controllerVar.getSearchgrounds());
		addLightForm(controllerVar.getSearchmethod());
		addLightForm(controllerVar.getSearchnum());
		addLightForm(controllerVar.getSearchperson());
		addLightForm(controllerVar.getSearchseizeditems());
		addLightForm(controllerVar.getSearchstreet());
		addLightForm(controllerVar.getSearchtype());
		addLightForm(controllerVar.getSearchwitness());
		addLightForm(controllerVar.getTrafaddress());
		addLightForm(controllerVar.getTrafage());
		addLightForm(controllerVar.getTrafarea());
		addLightForm(controllerVar.getTrafcolor());
		addLightForm(controllerVar.getTrafcomments());
		addLightForm(controllerVar.getTrafcounty());
		addLightForm(controllerVar.getTrafdesc());
		addLightForm(controllerVar.getTrafgender());
		addLightForm(controllerVar.getTrafmodel());
		addLightForm(controllerVar.getTrafname());
		addLightForm(controllerVar.getTrafnum());
		addLightForm(controllerVar.getTrafotherinfo());
		addLightForm(controllerVar.getTrafplatenum());
		addLightForm(controllerVar.getTrafstreet());
		addLightForm(controllerVar.getTraftype());
	}
	
	private static void addLightForm(TextField textField) {
		textField.getStyleClass().clear();
		textField.getStyleClass().add("formFieldlight");
	}
	
	private static void addDarkForm(TextField textField) {
		textField.getStyleClass().clear();
		textField.getStyleClass().add("formFielddark");
	}
	
	public void initialize() throws IOException {
		if (DataTerminalHomeApplication.controller != null) {
			controllerVar = DataTerminalHomeApplication.controller;
		} else if (newOfficerController.controller != null) {
			controllerVar = newOfficerController.controller;
		} else {
			log("Settings Controller Var could not be set", LogUtils.Severity.ERROR);
		}
		
		topBar = reportUtil.createSimpleTitleBar("ReportsPlus", true);
		
		root.setTop(topBar);
		
		startupFullscreenCheckbox.setSelected(
				ConfigReader.configRead("uiSettings", "fullscreenOnStartup").equals("true"));
		serverAutoconnectCheckbox.setSelected(
				ConfigReader.configRead("connectionSettings", "serverAutoConnect").equals("true"));
		AOTNotes.setSelected(ConfigReader.configRead("AOTSettings", "AOTNotes").equals("true"));
		AOTReport.setSelected(ConfigReader.configRead("AOTSettings", "AOTReport").equals("true"));
		AOTMap.setSelected(ConfigReader.configRead("AOTSettings", "AOTMap").equals("true"));
		AOTID.setSelected(ConfigReader.configRead("AOTSettings", "AOTID").equals("true"));
		AOTCallout.setSelected(ConfigReader.configRead("AOTSettings", "AOTCallout").equals("true"));
		AOTSettings.setSelected(ConfigReader.configRead("AOTSettings", "AOTSettings").equals("true"));
		AOTClient.setSelected(ConfigReader.configRead("AOTSettings", "AOTClient").equals("true"));
		AOTDebug.setSelected(ConfigReader.configRead("AOTSettings", "AOTDebug").equals("true"));
		
		String[] displayPlacements = {"Default", "Top Left", "Top Right", "Bottom Left", "Bottom Right", "\n", "Full Left", "Full Right"};
		mainWindowComboBox.getItems().addAll(displayPlacements);
		notesWindowComboBox.getItems().addAll(displayPlacements);
		ReportWindowComboBox.getItems().addAll(displayPlacements);
		
		try {
			mainWindowComboBox.setValue(ConfigReader.configRead("layout", "mainWindowLayout"));
			notesWindowComboBox.setValue(ConfigReader.configRead("layout", "notesWindowLayout"));
			ReportWindowComboBox.setValue(ConfigReader.configRead("layout", "reportWindowLayout"));
		} catch (IOException e) {
			logError("Could not set reportwindowboxes from config: ", e);
		}
		
		EventHandler<ActionEvent> comboBoxHandler = event -> {
			ComboBox<String> comboBox = (ComboBox<String>) event.getSource();
			String selectedPlacement = comboBox.getSelectionModel().getSelectedItem();
			
			if (comboBox == mainWindowComboBox) {
				if ("Default".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "mainWindowLayout", "Default");
				} else if ("Top Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "mainWindowLayout", "TopLeft");
				} else if ("Top Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "mainWindowLayout", "TopRight");
				} else if ("Bottom Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "mainWindowLayout", "BottomLeft");
				} else if ("Bottom Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "mainWindowLayout", "BottomRight");
				} else if ("Full Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "mainWindowLayout", "FullLeft");
				} else if ("Full Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "mainWindowLayout", "FullRight");
				}
			}
			
			if (comboBox == notesWindowComboBox) {
				if ("Default".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "notesWindowLayout", "Default");
				} else if ("Top Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "notesWindowLayout", "TopLeft");
				} else if ("Top Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "notesWindowLayout", "TopRight");
				} else if ("Bottom Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "notesWindowLayout", "BottomLeft");
				} else if ("Bottom Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "notesWindowLayout", "BottomRight");
				} else if ("Full Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "notesWindowLayout", "FullLeft");
				} else if ("Full Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "notesWindowLayout", "FullRight");
				}
			}
			
			if (comboBox == ReportWindowComboBox) {
				if ("Default".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "reportWindowLayout", "Default");
				} else if ("Top Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "reportWindowLayout", "TopLeft");
				} else if ("Top Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "reportWindowLayout", "TopRight");
				} else if ("Bottom Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "reportWindowLayout", "BottomLeft");
				} else if ("Bottom Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "reportWindowLayout", "BottomRight");
				} else if ("Full Left".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "reportWindowLayout", "FullLeft");
				} else if ("Full Right".equals(selectedPlacement)) {
					ConfigWriter.configwrite("layout", "reportWindowLayout", "FullRight");
				}
			}
		};
		
		mainWindowComboBox.setOnAction(comboBoxHandler);
		notesWindowComboBox.setOnAction(comboBoxHandler);
		ReportWindowComboBox.setOnAction(comboBoxHandler);
		
		broadcastPortField.setText(ConfigReader.configRead("connectionSettings", "broadcastPort"));
		
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
		
		bkgPicker.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				Color selectedColor = newValue;
				updatebackground(selectedColor);
				try {
					loadTheme();
					loadColors();
				} catch (IOException e) {
					logError("LoadTheme IO Error Code 33 ", e);
				}
			}
		});
		
		primPicker.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				Color selectedColor = newValue;
				updateMain(selectedColor);
				try {
					loadTheme();
					loadColors();
				} catch (IOException e) {
					logError("LoadTheme IO Error Code 3 ", e);
				}
			}
		});
		
		secPicker.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				Color selectedColor = newValue;
				updateSecondary(selectedColor);
				try {
					loadTheme();
					loadColors();
				} catch (IOException e) {
					logError("LoadTheme IO Error Code 4 ", e);
				}
			}
		});
		
		accPicker.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				Color selectedColor = newValue;
				updateAccent(selectedColor);
				try {
					loadTheme();
					loadColors();
				} catch (IOException e) {
					logError("LoadTheme IO Error Code 5 ", e);
				}
			}
		});
		
		backgroundPickerReport.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				Color selectedColor = newValue;
				updateReportBackground(selectedColor);
				try {
					loadTheme();
					loadColors();
				} catch (IOException e) {
					logError("LoadTheme IO Error Code 6 ", e);
				}
			}
		});
		
		accentPickerReport.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				Color selectedColor = newValue;
				updateReportAccent(selectedColor);
				try {
					loadTheme();
					loadColors();
				} catch (IOException e) {
					logError("LoadTheme IO Error Code 7 ", e);
				}
			}
		});
		
		headingPickerReport.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				Color selectedColor = newValue;
				updateReportHeading(selectedColor);
				try {
					loadTheme();
					loadColors();
				} catch (IOException e) {
					logError("LoadTheme IO Error Code 8 ", e);
				}
			}
		});
		
		secPickerReport.valueProperty().addListener(new ChangeListener<Color>() {
			@Override
			public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
				Color selectedColor = newValue;
				updateReportSecondary(selectedColor);
				try {
					loadTheme();
					loadColors();
				} catch (IOException e) {
					logError("LoadTheme IO Error Code 9 ", e);
				}
			}
		});
		
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
				controllerVar.generatedByTag.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				controllerVar.generatedDateTag.setStyle("-fx-text-fill: " + UIDarkColor + ";");
				controllerVar.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UIDarkColor + ";");
			} else {
				textClrComboBox.getSelectionModel().selectLast();
				controllerVar.generatedByTag.setStyle("-fx-text-fill: " + UILightColor + ";");
				controllerVar.generatedDateTag.setStyle("-fx-text-fill: " + UILightColor + ";");
				controllerVar.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UILightColor + ";");
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
		
		String[] themes = {"dark", "purple", "blue", "grey", "green", "red", "orange", "pink", "teal", "brown", "magenta", "lime", "indigo"};
		themeComboBox.getItems().addAll(themes);
		themeComboBox.setOnAction(actionEvent -> {
			String selectedTheme = (String) themeComboBox.getSelectionModel().getSelectedItem();
			
			switch (selectedTheme) {
				case "dark" -> {
					log("Dark Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#263238"));
					updateSecondary(Color.valueOf("#323C41"));
					updateAccent(Color.valueOf("#505d62"));
				}
				case "purple" -> {
					log("Purple Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#524992"));
					updateSecondary(Color.valueOf("#665cb6"));
					updateAccent(Color.valueOf("#9c95d0"));
				}
				case "blue" -> {
					log("Blue Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#4d66cc"));
					updateSecondary(Color.valueOf("#6680e6"));
					updateAccent(Color.valueOf("#b3ccff"));
				}
				case "grey" -> {
					log("Grey Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#666666"));
					updateSecondary(Color.valueOf("#808080"));
					updateAccent(Color.valueOf("#4d4d4d"));
				}
				case "green" -> {
					log("Green Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#4d804d"));
					updateSecondary(Color.valueOf("#669966"));
					updateAccent(Color.valueOf("#99cc99"));
				}
				case "red" -> {
					log("Red Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#cc4d4d"));
					updateSecondary(Color.valueOf("#e65c5c"));
					updateAccent(Color.valueOf("#ff6666"));
				}
				case "orange" -> {
					log("Orange Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#cc804d"));
					updateSecondary(Color.valueOf("#e6994d"));
					updateAccent(Color.valueOf("#ffb366"));
				}
				case "pink" -> {
					log("Pink Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#cc3399"));
					updateSecondary(Color.valueOf("#e64da1"));
					updateAccent(Color.valueOf("#ff66b3"));
				}
				case "teal" -> {
					log("Teal Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#339999"));
					updateSecondary(Color.valueOf("#4db3b3"));
					updateAccent(Color.valueOf("#66cccc"));
				}
				case "brown" -> {
					log("Brown Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#6c3d2c"));
					updateSecondary(Color.valueOf("#7e4e3c"));
					updateAccent(Color.valueOf("#5b3b30"));
				}
				case "magenta" -> {
					log("Magenta Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#c2185b"));
					updateSecondary(Color.valueOf("#e91e63"));
					updateAccent(Color.valueOf("#f06292"));
				}
				case "lime" -> {
					log("Lime Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#cddc39"));
					updateSecondary(Color.valueOf("#d4e157"));
					updateAccent(Color.valueOf("#dce775"));
				}
				case "indigo" -> {
					log("Indigo Theme Selected", LogUtils.Severity.DEBUG);
					updateMain(Color.valueOf("#3f51b5"));
					updateSecondary(Color.valueOf("#5c6bc0"));
					updateAccent(Color.valueOf("#7986cb"));
				}
			}
			
			try {
				loadTheme();
				loadColors();
			} catch (IOException e) {
				logError("LoadTheme Error", e);
			}
		});
		
		String[] presets = {"dark", "light", "grey", "green", "blue", "red", "purple", "orange", "pink", "teal", "brown", "magenta", "lime", "indigo"};
		presetComboBoxReport.getItems().addAll(presets);
		presetComboBoxReport.setOnAction(actionEvent -> {
			String selectedTheme = (String) presetComboBoxReport.getSelectionModel().getSelectedItem();
			
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
					updateReportBackground(Color.valueOf("#ff6666"));
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
					updateReportBackground(Color.valueOf("#ffb366"));
					updateReportSecondary(Color.valueOf("#e6994d"));
					updateReportAccent(Color.valueOf("#cc804d"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "pink" -> {
					log("Pink Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#ff66b3"));
					updateReportSecondary(Color.valueOf("#e64da1"));
					updateReportAccent(Color.valueOf("#cc3399"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "teal" -> {
					log("Teal Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#66cccc"));
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
					updateReportBackground(Color.valueOf("#f06292"));
					updateReportSecondary(Color.valueOf("#e91e63"));
					updateReportAccent(Color.valueOf("#c2185b"));
					updateReportHeading(Color.valueOf("white"));
				}
				case "lime" -> {
					log("Lime Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#dce775"));
					updateReportSecondary(Color.valueOf("#d4e157"));
					updateReportAccent(Color.valueOf("#cddc39"));
					updateReportHeading(Color.valueOf("#333333"));
				}
				case "indigo" -> {
					log("Indigo Theme Selected: Report", LogUtils.Severity.DEBUG);
					updateReportBackground(Color.valueOf("#7986cb"));
					updateReportSecondary(Color.valueOf("#5c6bc0"));
					updateReportAccent(Color.valueOf("#3f51b5"));
					updateReportHeading(Color.valueOf("white"));
				}
			}
			
			try {
				loadTheme();
				loadColors();
			} catch (IOException e) {
				logError("LoadTheme Error", e);
			}
		});


		
		/*if (reportStyleComboBox.getSelectionModel()
		                       .getSelectedItem()
		                       .equals("dark")) {
			ConfigWriter.configwrite("reportWindowDarkMode", "true");
		} else {
			ConfigWriter.configwrite("reportWindowDarkMode", "false");
		}
		
		if (textClrComboBox.getSelectionModel()
		                       .getSelectedItem()
		                       .equals("dark")) {
			ConfigWriter.configwrite("UIDarkMode", "true");
		} else {
			ConfigWriter.configwrite("UIDarkMode", "false");
		}*/
		
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
		
		Platform.runLater(() -> {
			Stage stage = (Stage) root.getScene().getWindow();
			stage.setMinWidth(stage.getWidth());
			stage.setMinHeight(stage.getHeight());
		});
		loadColors();
		loadTheme();
	}
	
	@javafx.fxml.FXML
	public void clearSaveDataBtnClick(ActionEvent actionEvent) {
		Stage stage = (Stage) root.getScene().getWindow();
		confirmSaveDataClearDialog(stage);
	}
	
	@javafx.fxml.FXML
	public void openDebugLogsBtnClick(ActionEvent actionEvent) throws IOException {
		Stage stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("output-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		stage.setTitle("Report Manager");
		stage.setScene(newScene);
		stage.show();
		stage.centerOnScreen();
		stage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTDebug").equals("true"));
		
		windowUtils.centerStageOnMainApp(stage);
	}
	
	@javafx.fxml.FXML
	public void clearLogsBtnClick(ActionEvent actionEvent) {
		Stage stage = (Stage) root.getScene().getWindow();
		confirmLogClearDialog(stage, controllerVar.getReportChart(), controllerVar.getAreaReportChart());
		showLogClearNotification("Log Manager", "Logs have been cleared.", root);
	}
	
	@javafx.fxml.FXML
	public void resetDefaultsBtnPress(ActionEvent actionEvent) {
		updateMain(Color.valueOf("#524992"));
		updateSecondary(Color.valueOf("#665cb6"));
		updateAccent(Color.valueOf("#9c95d0"));
		updatebackground(Color.valueOf("#ffffff"));
		try {
			loadTheme();
			loadColors();
		} catch (IOException e) {
			logError("LoadTheme IO Error Code 2 ", e);
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
			secPicker.setValue(secondary);
			accPicker.setValue(accent);
			bkgPicker.setValue(bkg);
			primLabel.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			secLabel.setStyle("-fx-text-fill: " + toHexString(secondary) + ";");
			accLabel.setStyle("-fx-text-fill: " + toHexString(accent) + ";");
			
			if (toHexString(bkg).toLowerCase().equals("#ffffff") || toHexString(bkg).toLowerCase().equals(
					"#f2f2f2") || toHexString(bkg).toLowerCase().equals("#e6e6e6") || toHexString(
					bkg).toLowerCase().equals("#cccccc")) {
				bkgLabel.setStyle("-fx-text-fill: black;");
			} else {
				bkgLabel.setStyle("-fx-text-fill: " + toHexString(bkg) + ";");
			}
			
			tabpane.setStyle("-fx-background-color: " + toHexString(bkg));
			
			lbl0.setStyle("-fx-background-color: " + toHexString(primary) + ";");
			lbl1.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl2.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl3.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl5.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl6.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			lbl7.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
			
			backgroundPickerReport.setValue(reportBackground);
			accentPickerReport.setValue(reportAccent);
			headingPickerReport.setValue(reportHeading);
			secPickerReport.setValue(reportSecondary);
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
				resetDefaultsBtn.setStyle(nonTransparentBtn);
				resetDefaultsBtn.setOnMouseEntered(e -> resetDefaultsBtn.setStyle(hoverStyle));
				resetDefaultsBtn.setOnMouseExited(e -> resetDefaultsBtn.setStyle(nonTransparentBtn));
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
			logError("LoadTheme IO Error Code 1 ", e);
		}
	}
	
	@javafx.fxml.FXML
	public void startupFullscreenClick(ActionEvent actionEvent) {
		if (startupFullscreenCheckbox.isSelected()) {
			ConfigWriter.configwrite("uiSettings", "fullscreenOnStartup", "true");
			startupFullscreenCheckbox.setSelected(true);
		} else {
			ConfigWriter.configwrite("uiSettings", "fullscreenOnStartup", "false");
			startupFullscreenCheckbox.setSelected(false);
		}
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
}
