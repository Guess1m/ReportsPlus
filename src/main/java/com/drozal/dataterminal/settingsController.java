package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.CalloutManager;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.NotificationManager;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.Window.windowUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.server.ClientUtils.isConnected;

public class settingsController {

    private static final String UILightColor = "rgb(255,255,255,0.75)";
    private static final String UIDarkColor = "rgb(0,0,0,0.75)";
    private static AtomicReference<String> selectedNotification;

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
    @javafx.fxml.FXML
    private Button resetReportDefaultsBtn;
    @javafx.fxml.FXML
    private Label tt10;
    @javafx.fxml.FXML
    private Label tt1;
    @javafx.fxml.FXML
    private Label tt3;
    @javafx.fxml.FXML
    private Label tt2;
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
        controllerVar.getCasePrim1().setStyle("-fx-text-fill: " + mainclr + ";");
        controllerVar.getCaseprim1().setStyle("-fx-text-fill: " + mainclr + ";");
        controllerVar.getCaseprim2().setStyle("-fx-text-fill: " + mainclr + ";");
        controllerVar.getCaseprim3().setStyle("-fx-text-fill: " + mainclr + ";");

        String secclr = ConfigReader.configRead("uiColors", "secondaryColor");
        controllerVar.getServerStatusLabel().setStyle("-fx-border-color: " + secclr + "; -fx-label-padding: 5; -fx-border-radius: 5;");
        controllerVar.sidepane.setStyle("-fx-background-color: " + secclr + ";");
        controllerVar.getSecondaryColor3Bkg().setStyle("-fx-background-color: " + secclr + ";");
        controllerVar.getSecondaryColor4Bkg().setStyle("-fx-background-color: " + secclr + ";");
        controllerVar.getSecondaryColor5Bkg().setStyle("-fx-background-color: " + secclr + ";");
        controllerVar.getCaseSec1().setStyle("-fx-text-fill: " + secclr + ";");
        controllerVar.getCaseSec2().setStyle("-fx-text-fill: " + secclr + ";");
        controllerVar.getCasesec1().setStyle("-fx-text-fill: " + secclr + ";");
        controllerVar.getCasesec2().setStyle("-fx-text-fill: " + secclr + ";");
        controllerVar.getCasesec3().setStyle("-fx-text-fill: " + secclr + ";");
        controllerVar.getCasesec4().setStyle("-fx-text-fill: " + secclr + ";");
        controllerVar.getCaseSuspensionDurationlbl().setStyle("-fx-text-fill: " + secclr + ";");

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
        controllerVar.getCourtPane().setStyle("-fx-background-color: " + bkgclr + ";");
        controllerVar.getBlankCourtInfoPane().setStyle("-fx-background-color: " + bkgclr + ";");

        String accclr = ConfigReader.configRead("uiColors", "accentColor");
        controllerVar.getReportPlusLabelFill().setStyle("-fx-text-fill: " + accclr + ";");
        controllerVar.mainColor8.setStyle("-fx-text-fill: " + accclr + ";");

        CalloutManager.loadActiveCallouts(controllerVar.getCalActiveList());
        CalloutManager.loadHistoryCallouts(controllerVar.getCalHistoryList());

        controllerVar.getCurrentCalPane().setStyle(updateStyleProperty(controllerVar.getCurrentCalPane(), "-fx-border-color", accclr));

        controllerVar.getCalActiveList().setStyle(updateStyleProperty(controllerVar.getCalActiveList(), "-fx-border-color", accclr));
        controllerVar.getCalHistoryList().setStyle(updateStyleProperty(controllerVar.getCalHistoryList(), "-fx-border-color", accclr));

        controllerVar.getActivecalfill().setStyle(updateStyleProperty(controllerVar.getActivecalfill(), "-fx-border-color", mainclr));
        controllerVar.getCalfill().setStyle(updateStyleProperty(controllerVar.getCalfill(), "-fx-border-color", mainclr));

        String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor");
        String initialStyle = "-fx-background-color: transparent;";
        String nonTransparentBtn = "-fx-background-color: " + accclr + ";";
        controllerVar.updateInfoBtn.setStyle(nonTransparentBtn);
        controllerVar.shiftInfoBtn.setOnMouseEntered(e -> controllerVar.shiftInfoBtn.setStyle(hoverStyle));
        controllerVar.shiftInfoBtn.setOnMouseExited(e -> controllerVar.shiftInfoBtn.setStyle(initialStyle));
        controllerVar.getSettingsBtn().setOnMouseEntered(e -> controllerVar.getSettingsBtn().setStyle("-fx-background-color: " + secclr + ";"));
        controllerVar.getSettingsBtn().setOnMouseExited(e -> controllerVar.getSettingsBtn().setStyle(initialStyle));
        controllerVar.notesButton.setOnMouseEntered(e -> controllerVar.notesButton.setStyle(hoverStyle));
        controllerVar.notesButton.setOnMouseExited(e -> controllerVar.notesButton.setStyle(initialStyle));
        controllerVar.getCreateReportBtn().setOnMouseEntered(e -> controllerVar.getCreateReportBtn().setStyle(hoverStyle));
        controllerVar.getCreateReportBtn().setOnMouseExited(e -> controllerVar.getCreateReportBtn().setStyle(initialStyle));
        controllerVar.getLogsButton().setOnMouseEntered(e -> controllerVar.getLogsButton().setStyle(hoverStyle));
        controllerVar.getLogsButton().setOnMouseExited(e -> controllerVar.getLogsButton().setStyle(initialStyle));
        controllerVar.getMapButton().setOnMouseEntered(e -> controllerVar.getMapButton().setStyle(hoverStyle));
        controllerVar.getMapButton().setOnMouseExited(e -> controllerVar.getMapButton().setStyle(initialStyle));
        controllerVar.getShowIDBtn().setOnMouseEntered(e -> controllerVar.getShowIDBtn().setStyle(hoverStyle));
        controllerVar.getShowIDBtn().setOnMouseExited(e -> controllerVar.getShowIDBtn().setStyle(initialStyle));
        controllerVar.getShowCalloutBtn().setOnMouseEntered(e -> controllerVar.getShowCalloutBtn().setStyle(hoverStyle));
        controllerVar.getShowCalloutBtn().setOnMouseExited(e -> controllerVar.getShowCalloutBtn().setStyle(initialStyle));
        controllerVar.getLookupBtn().setOnMouseEntered(e -> controllerVar.getLookupBtn().setStyle(hoverStyle));
        controllerVar.getLookupBtn().setOnMouseExited(e -> controllerVar.getLookupBtn().setStyle(initialStyle));
        controllerVar.getShowCourtCasesBtn().setOnMouseEntered(e -> controllerVar.getShowCourtCasesBtn().setStyle(hoverStyle));
        controllerVar.getShowCourtCasesBtn().setOnMouseExited(e -> controllerVar.getShowCourtCasesBtn().setStyle(initialStyle));

        controllerVar.updateInfoBtn.setOnMouseEntered(e -> controllerVar.updateInfoBtn.setStyle(hoverStyle));
        controllerVar.updateInfoBtn.setOnMouseExited(e -> {
            controllerVar.updateInfoBtn.setStyle(nonTransparentBtn);
        });

        if (isConnected) {
            controllerVar.getServerStatusLabel().setStyle("-fx-text-fill: #00da16; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
        } else {
            controllerVar.getServerStatusLabel().setStyle("-fx-text-fill: #ff5e5e; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
        }

        if (ConfigReader.configRead("uiColors", "UIDarkMode").equals("true")) {
            addDarkStyles();
        } else {
            addLightStyles();
        }

        controllerVar.getServerStatusLabel().setStyle(updateStyleProperty(controllerVar.getServerStatusLabel(), "-fx-border-color", secclr));
    }

    private static void addDarkStyles() {
        controllerVar.getTabPane().getStyleClass().clear();
        controllerVar.getTabPane().getStyleClass().add("darktabpane");
        controllerVar.getCaseNotesField().getStyleClass().clear();
        controllerVar.getCaseNotesField().getStyleClass().add("text-area-dark");

        controllerVar.generatedByTag.setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.generatedDateTag.setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getVehplatefield().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getNoCourtCaseSelectedlbl().setStyle("-fx-text-fill: " + UIDarkColor + ";");

        controllerVar.getPlt1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPlt2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPlt3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPlt4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPlt5().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPlt6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPlt7().setStyle("-fx-text-fill: " + UIDarkColor + ";");

        controllerVar.getCaldetlbl1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaldetlbl2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaldetlbl3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaldetlbl4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaldetlbl5().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaldetlbl6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaldetlbl7().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaldetlbl8().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaldetlbl9().setStyle("-fx-text-fill: " + UIDarkColor + ";");

        controllerVar.getCaselbl1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl5().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl7().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl8().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl9().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl10().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl11().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getCaselbl12().setStyle("-fx-text-fill: " + UIDarkColor + ";");

        controllerVar.getPed1().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPed2().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPed3().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPed4().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPed5().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPed6().setStyle("-fx-text-fill: " + UIDarkColor + ";");
        controllerVar.getPed7().setStyle("-fx-text-fill: " + UIDarkColor + ";");

        controllerVar.getActivecalfill().setStyle(updateStyleProperty(controllerVar.getActivecalfill(), "-fx-text-fill", UIDarkColor));
        controllerVar.getCalfill().setStyle(updateStyleProperty(controllerVar.getCalfill(), "-fx-text-fill", UIDarkColor));

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

        addDarkForm(controllerVar.getCaseNumField());
        addDarkForm(controllerVar.getCaseCourtDateField());
        addDarkForm(controllerVar.getCaseOffenceDateField());
        addDarkForm(controllerVar.getCaseFirstNameField());
        addDarkForm(controllerVar.getCaseLastNameField());
        addDarkForm(controllerVar.getCaseAgeField());
        addDarkForm(controllerVar.getCaseGenderField());
        addDarkForm(controllerVar.getCaseAddressField());
        addDarkForm(controllerVar.getCaseStreetField());
        addDarkForm(controllerVar.getCaseAreaField());
        addDarkForm(controllerVar.getCaseCountyField());
    }

    private static void addLightStyles() {
        controllerVar.getTabPane().getStyleClass().clear();
        controllerVar.getTabPane().getStyleClass().add("lighttabpane");
        controllerVar.getCaseNotesField().getStyleClass().clear();
        controllerVar.getCaseNotesField().getStyleClass().add("text-area-light");

        controllerVar.generatedByTag.setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.generatedDateTag.setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getLogbrwsrlbl().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getVehplatefield().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getNoCourtCaseSelectedlbl().setStyle("-fx-text-fill: " + UILightColor + ";");

        controllerVar.getPlt1().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPlt2().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPlt3().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPlt4().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPlt5().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPlt6().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPlt7().setStyle("-fx-text-fill: " + UILightColor + ";");

        controllerVar.getCaldetlbl1().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaldetlbl2().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaldetlbl3().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaldetlbl4().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaldetlbl5().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaldetlbl6().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaldetlbl7().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaldetlbl8().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaldetlbl9().setStyle("-fx-text-fill: " + UILightColor + ";");

        controllerVar.getCaselbl1().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl2().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl3().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl4().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl5().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl6().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl7().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl8().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl9().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl10().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl11().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getCaselbl12().setStyle("-fx-text-fill: " + UILightColor + ";");

        controllerVar.getPed1().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPed2().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPed3().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPed4().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPed5().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPed6().setStyle("-fx-text-fill: " + UILightColor + ";");
        controllerVar.getPed7().setStyle("-fx-text-fill: " + UILightColor + ";");

        controllerVar.getActivecalfill().setStyle(updateStyleProperty(controllerVar.getActivecalfill(), "-fx-text-fill", UILightColor));
        controllerVar.getCalfill().setStyle(updateStyleProperty(controllerVar.getCalfill(), "-fx-text-fill", UILightColor));

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

        addLightForm(controllerVar.getCaseNumField());
        addLightForm(controllerVar.getCaseCourtDateField());
        addLightForm(controllerVar.getCaseOffenceDateField());
        addLightForm(controllerVar.getCaseFirstNameField());
        addLightForm(controllerVar.getCaseLastNameField());
        addLightForm(controllerVar.getCaseAgeField());
        addLightForm(controllerVar.getCaseGenderField());
        addLightForm(controllerVar.getCaseAddressField());
        addLightForm(controllerVar.getCaseStreetField());
        addLightForm(controllerVar.getCaseAreaField());
        addLightForm(controllerVar.getCaseCountyField());
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

        enableCalloutPopupsCheckbox.setSelected(ConfigReader.configRead("uiSettings", "enableCalloutPopup").equals("true"));
        enableIDPopupsCheckbox.setSelected(ConfigReader.configRead("uiSettings", "enableIDPopup").equals("true"));
        startupFullscreenCheckbox.setSelected(ConfigReader.configRead("uiSettings", "fullscreenOnStartup").equals("true"));
        serverAutoconnectCheckbox.setSelected(ConfigReader.configRead("connectionSettings", "serverAutoConnect").equals("true"));
        saveCalloutLocationCheckbox.setSelected(ConfigReader.configRead("layout", "rememberCalloutLocation").equals("true"));
        saveReportLocationCheckbox.setSelected(ConfigReader.configRead("layout", "rememberReportLocation").equals("true"));
        saveIDLocationCheckbox.setSelected(ConfigReader.configRead("layout", "rememberIDLocation").equals("true"));
        saveNotesLocationCheckbox.setSelected(ConfigReader.configRead("layout", "rememberNotesLocation").equals("true"));
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

        socketTimeoutField.setText(ConfigReader.configRead("connectionSettings", "socketTimeout"));

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

        bkgPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            updatebackground(selectedColor);
            try {
                loadTheme();
                loadColors();
            } catch (IOException e) {
                logError("LoadTheme IO Error Code 33 ", e);
            }
        });

        primPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            updateMain(selectedColor);
            try {
                loadTheme();
                loadColors();
            } catch (IOException e) {
                logError("LoadTheme IO Error Code 3 ", e);
            }
        });

        secPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            updateSecondary(selectedColor);
            try {
                loadTheme();
                loadColors();
            } catch (IOException e) {
                logError("LoadTheme IO Error Code 4 ", e);
            }
        });

        accPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            updateAccent(selectedColor);
            try {
                loadTheme();
                loadColors();
            } catch (IOException e) {
                logError("LoadTheme IO Error Code 5 ", e);
            }
        });

        backgroundPickerReport.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            updateReportBackground(selectedColor);
            try {
                loadTheme();
                loadColors();
            } catch (IOException e) {
                logError("LoadTheme IO Error Code 6 ", e);
            }
        });

        accentPickerReport.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            updateReportAccent(selectedColor);
            try {
                loadTheme();
                loadColors();
            } catch (IOException e) {
                logError("LoadTheme IO Error Code 7 ", e);
            }
        });

        headingPickerReport.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            updateReportHeading(selectedColor);
            try {
                loadTheme();
                loadColors();
            } catch (IOException e) {
                logError("LoadTheme IO Error Code 8 ", e);
            }
        });

        secPickerReport.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            updateReportSecondary(selectedColor);
            try {
                loadTheme();
                loadColors();
            } catch (IOException e) {
                logError("LoadTheme IO Error Code 9 ", e);
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

        String[] themes = {"dark", "purple", "blue", "grey", "green", "red", "orange", "pink", "teal", "brown", "magenta", "indigo"};
        themeComboBox.getItems().addAll(themes);
        themeComboBox.setOnAction(actionEvent -> {
            String selectedTheme = (String) themeComboBox.getSelectionModel().getSelectedItem();

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
        });

        String[] presets = {"dark", "light", "grey", "green", "blue", "red", "purple", "orange", "pink", "teal", "brown", "magenta", "indigo"};
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

            try {
                loadTheme();
                loadColors();
            } catch (IOException e) {
                logError("LoadTheme Error", e);
            }
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
                        notiTextColorPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationInfoTextColor")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        notiPrimPicker.setValue(Color.valueOf(ConfigReader.configRead("notificationSettings", "notificationInfoPrimary")));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

        notiDisplayDurField.setText(ConfigReader.configRead("notificationSettings", "displayDuration"));
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
        notiFadeOutDurField.setText(ConfigReader.configRead("notificationSettings", "fadeOutDuration"));
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
        saveFadeDurBtn.setOnAction(actionEvent -> ConfigWriter.configwrite("notificationSettings", "fadeOutDuration", notiFadeOutDurField.getText()));
        saveDisplayDurBtn.setOnAction(actionEvent -> ConfigWriter.configwrite("notificationSettings", "displayDuration", notiDisplayDurField.getText()));

        notiPrimPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            if (selectedNotification.get().equals("Information")) {
                updateInfoNotiPrim(selectedColor);
            }
            if (selectedNotification.get().equals("Warning")) {
                updateWarnNotiPrim(selectedColor);
            }
        });
        notiTextColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Color selectedColor = newValue;
            if (selectedNotification.get().equals("Information")) {
                updateInfoNotiTextColor(selectedColor);
            }
            if (selectedNotification.get().equals("Warning")) {
                updateWarnNotiTextColor(selectedColor);
            }
        });

        Platform.runLater(() -> {
            Stage stage = (Stage) root.getScene().getWindow();
            stage.setMinWidth(stage.getWidth());
            stage.setMinHeight(stage.getHeight());

            previewNotificationBtn.setOnAction(actionEvent -> {
                if (selectedNotification.get().equals("Information")) {
                    NotificationManager.showNotificationInfo("Sample Info Notification", "Lorum ipsum dolor sit amet, consectetur adipiscing elit.", mainRT);
                }
                if (selectedNotification.get().equals("Warning")) {
                    NotificationManager.showNotificationWarning("Sample Warning Notification", "Lorum ipsum dolor sit amet, consectetur adipiscing elit.", mainRT);
                }
            });
        });
        loadColors();
        loadTheme();

        colorPageOne.setVisible(true);
        colorPageTwo.setVisible(false);

        windowPageOne.setVisible(true);
        windowPageTwo.setVisible(false);

        addTooltip(startupFullscreenCheckbox, "Start The Application Fullscreen");
        addTooltip(serverAutoconnectCheckbox, "Try To Autoconnect To Server On Startup");
        addTooltip(saveReportLocationCheckbox, "Save Location and Size of Report Window");

        addTooltip(enableCalloutPopupsCheckbox, "Allow Callouts To Pop Up On Screen");
        addTooltip(enableIDPopupsCheckbox, "Allow IDs To Pop Up On Screen");

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

        addTooltip(tt1, "Main Window Location On Startup");
        addTooltip(tt2, "Notes Window Location On Startup");
        addTooltip(tt3, "Report Window Location On Startup");
        addTooltip(tt4, "UI Theme Presets");
        addTooltip(tt5, "UI Text Color");
        addTooltip(tt6, "Report Theme Presets");
        addTooltip(tt7, "Report TextField Color");
        addTooltip(tt8, "Duration (Sec) That Callout Window is shown");
        addTooltip(tt9, "Duration (Sec) That ID Window is shown");
        addTooltip(tt10, "Port Used To Receive Server Broadcast Info\nOnly Change If You Have Issues With Autoconnection\nMust Match With Broadcastport In Server Config");
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
        ConfigWriter.configwrite("uiColors", "UIDarkMode", "true");
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
    }

    @javafx.fxml.FXML
    public void resetReportDefaultsBtnPress(ActionEvent actionEvent) {
        updateReportBackground(Color.valueOf("#505d62"));
        updateReportSecondary(Color.valueOf("#323c41"));
        updateReportAccent(Color.valueOf("#263238"));
        updateReportHeading(Color.valueOf("white"));
        ConfigWriter.configwrite("reportSettings", "reportWindowDarkMode", "false");
        log("Reset Report Color Defaults", LogUtils.Severity.DEBUG);
        loadColors();
        presetComboBoxReport.getSelectionModel().select("dark");
        reportStyleComboBox.getSelectionModel().select("light");
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

            if (toHexString(bkg).equalsIgnoreCase("#ffffff") || toHexString(bkg).equalsIgnoreCase("#f2f2f2") || toHexString(bkg).equalsIgnoreCase("#e6e6e6") || toHexString(bkg).equalsIgnoreCase("#cccccc")) {
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
            lbl8.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
            lbl9.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
            lbl10.setStyle("-fx-text-fill: " + toHexString(primary) + ";");

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

            if (ConfigReader.configRead("uiColors", "UIDarkMode").equals("true")) {
                tabpane.getStyleClass().clear();
                tabpane.getStyleClass().add("darktabpane");
            } else {
                tabpane.getStyleClass().clear();
                tabpane.getStyleClass().add("lighttabpane");
            }

            try {
                String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("uiColors", "mainColor");
                String nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("uiColors", "accentColor") + ";";
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
}
