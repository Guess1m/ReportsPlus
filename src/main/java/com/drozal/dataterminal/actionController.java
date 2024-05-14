package com.drozal.dataterminal;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.Impound.ImpoundLogEntry;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.logs.Incident.IncidentLogEntry;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import com.drozal.dataterminal.logs.Patrol.PatrolLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import com.drozal.dataterminal.logs.Search.SearchLogEntry;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationLogEntry;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReportLogs;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopLogEntry;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportLogs;
import com.drozal.dataterminal.util.LogUtils;
import com.drozal.dataterminal.util.controllerUtils;
import com.drozal.dataterminal.util.dropdownInfo;
import com.drozal.dataterminal.util.server.ClientUtils;
import com.drozal.dataterminal.util.stringUtil;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.drozal.dataterminal.util.LogUtils.log;
import static com.drozal.dataterminal.util.LogUtils.logError;
import static com.drozal.dataterminal.util.controllerUtils.*;
import static com.drozal.dataterminal.util.reportCreationUtil.*;
import static com.drozal.dataterminal.util.server.recordUtils.grabPedData;
import static com.drozal.dataterminal.util.server.recordUtils.grabVehicleData;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.updateUtil.*;
import static com.drozal.dataterminal.util.windowUtils.*;

public class actionController {


    //<editor-fold desc="FXML Elements">


    public static String notesText;
    public static SimpleIntegerProperty needRefresh = new SimpleIntegerProperty();
    public static Stage IDStage = null;
    public static Stage CalloutStage = null;
    public static ClientController clientController;
    public static Stage notesStage = null;
    public static Stage clientStage = null;
    static double minColumnWidth = 185.0;
    private static Stage mapStage = null;
    @javafx.fxml.FXML
    public Button notesButton;
    @javafx.fxml.FXML
    public Button shiftInfoBtn;
    @javafx.fxml.FXML
    public AnchorPane shiftInformationPane;
    @javafx.fxml.FXML
    public TextField OfficerInfoName;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoDivision;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoAgency;
    @javafx.fxml.FXML
    public TextField OfficerInfoCallsign;
    @javafx.fxml.FXML
    public TextField OfficerInfoNumber;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoRank;
    @javafx.fxml.FXML
    public Label generatedDateTag;
    @javafx.fxml.FXML
    public Label generatedByTag;
    @javafx.fxml.FXML
    public AnchorPane infoPane;
    @javafx.fxml.FXML
    public Label updatedNotification;
    @javafx.fxml.FXML
    public AnchorPane vbox;
    @javafx.fxml.FXML
    public BarChart reportChart;
    @javafx.fxml.FXML
    public AnchorPane topPane;
    @javafx.fxml.FXML
    public AnchorPane sidepane;
    @javafx.fxml.FXML
    public Label mainColor8;
    @javafx.fxml.FXML
    public Label mainColor9Bkg;
    @javafx.fxml.FXML
    public Button updateInfoBtn;
    @javafx.fxml.FXML
    public MenuButton settingsDropdown;
    actionController controller;
    private NotesViewController notesViewController;
    @javafx.fxml.FXML
    private Label secondaryColor2;
    @javafx.fxml.FXML
    private Label secondaryColor5;
    @javafx.fxml.FXML
    private Label secondaryColor3;
    @javafx.fxml.FXML
    private Label secondaryColor4;
    @javafx.fxml.FXML
    private Label secondaryColor3Bkg;
    @javafx.fxml.FXML
    private TextField citcounty;
    private CalloutLogEntry calloutEntry;
    private PatrolLogEntry patrolEntry;
    private TrafficStopLogEntry trafficStopEntry;
    @javafx.fxml.FXML
    private Label mainColor6;
    @javafx.fxml.FXML
    private Label mainColor7Bkg;
    @javafx.fxml.FXML
    private Label secondaryColor4Bkg;
    @javafx.fxml.FXML
    private Label secondaryColor5Bkg;
    @javafx.fxml.FXML
    private Button logsButton;
    @javafx.fxml.FXML
    private Button mapButton;
    @javafx.fxml.FXML
    private MenuButton createReportBtn;
    @javafx.fxml.FXML
    private MenuItem searchReportButton;
    @javafx.fxml.FXML
    private MenuItem trafficReportButton;
    @javafx.fxml.FXML
    private MenuItem impoundReportButton;
    @javafx.fxml.FXML
    private MenuItem incidentReportButton;
    @javafx.fxml.FXML
    private MenuItem patrolReportButton;
    @javafx.fxml.FXML
    private MenuItem calloutReportButton;
    @javafx.fxml.FXML
    private MenuItem arrestReportButton;
    @javafx.fxml.FXML
    private MenuItem trafficCitationReportButton;
    @javafx.fxml.FXML
    private RadioMenuItem startupFullscreenToggleBtn;
    @javafx.fxml.FXML
    private AreaChart areaReportChart;
    @javafx.fxml.FXML
    private TextField searchbreathresult;
    @javafx.fxml.FXML
    private HBox patrolInfo;
    @javafx.fxml.FXML
    private TextField citvehother;
    @javafx.fxml.FXML
    private TextField trafcolor;
    @javafx.fxml.FXML
    private TextField trafmodel;
    @javafx.fxml.FXML
    private TextField searchnum;
    @javafx.fxml.FXML
    private TextField patstarttime;
    @javafx.fxml.FXML
    private TextField searchperson;
    @javafx.fxml.FXML
    private TextField arrestdetails;
    @javafx.fxml.FXML
    private Tab searchTab;
    @javafx.fxml.FXML
    private TextField citplatenum;
    @javafx.fxml.FXML
    private TextField traftype;
    @javafx.fxml.FXML
    private Label trafupdatedlabel;
    @javafx.fxml.FXML
    private TextField inccomments;
    @javafx.fxml.FXML
    private TextField trafcounty;
    @javafx.fxml.FXML
    private TextField citcharges;
    @javafx.fxml.FXML
    private TableView searchTable;
    @javafx.fxml.FXML
    private TextField arrestmedinfo;
    @javafx.fxml.FXML
    private TextField searchmethod;
    @javafx.fxml.FXML
    private HBox incidentInfo;
    @javafx.fxml.FXML
    private TextField impcolor;
    @javafx.fxml.FXML
    private TextField arrestaddress;
    @javafx.fxml.FXML
    private ToggleButton showManagerToggle;
    @javafx.fxml.FXML
    private AnchorPane lowerPane;
    @javafx.fxml.FXML
    private TextField trafstreet;
    @javafx.fxml.FXML
    private Label citupdatedlabel;
    @javafx.fxml.FXML
    private Tab arrestTab;
    @javafx.fxml.FXML
    private Label calupdatedlabel;
    @javafx.fxml.FXML
    private TextField citcolor;
    @javafx.fxml.FXML
    private TextField searchseizeditems;
    @javafx.fxml.FXML
    private TextField patvehicle;
    @javafx.fxml.FXML
    private TextField searchtype;
    @javafx.fxml.FXML
    private TextField patstoptime;
    @javafx.fxml.FXML
    private TextField searchcomments;
    @javafx.fxml.FXML
    private TextField impnum;
    @javafx.fxml.FXML
    private TextField arrestgender;
    @javafx.fxml.FXML
    private TextField searchbreathused;
    @javafx.fxml.FXML
    private TextField impmodel;
    @javafx.fxml.FXML
    private TextField citcomments;
    @javafx.fxml.FXML
    private TextField patcomments;
    @javafx.fxml.FXML
    private TextField calnotes;
    @javafx.fxml.FXML
    private TextField searchstreet;
    @javafx.fxml.FXML
    private TextField incstatement;
    @javafx.fxml.FXML
    private TextField citaddress;
    @javafx.fxml.FXML
    private TextField arrestnum;
    @javafx.fxml.FXML
    private TextField arrestcharges;
    @javafx.fxml.FXML
    private TextField calnum;
    @javafx.fxml.FXML
    private TextField incnum;
    @javafx.fxml.FXML
    private HBox impoundInfo;
    @javafx.fxml.FXML
    private Label incupdatedlabel;
    @javafx.fxml.FXML
    private TextField trafotherinfo;
    @javafx.fxml.FXML
    private TextField caladdress;
    @javafx.fxml.FXML
    private AnchorPane logPane;
    @javafx.fxml.FXML
    private TableView trafficStopTable;
    @javafx.fxml.FXML
    private TableView arrestTable;
    @javafx.fxml.FXML
    private TextField trafcomments;
    @javafx.fxml.FXML
    private TextField citname;
    @javafx.fxml.FXML
    private TableView impoundTable;
    @javafx.fxml.FXML
    private TableView citationTable;
    @javafx.fxml.FXML
    private TextField arrestcounty;
    @javafx.fxml.FXML
    private TextField searcharea;
    @javafx.fxml.FXML
    private TextField searchgrounds;
    @javafx.fxml.FXML
    private TextField citdesc;
    @javafx.fxml.FXML
    private TextField searchwitness;
    @javafx.fxml.FXML
    private TextField impname;
    @javafx.fxml.FXML
    private TextField citage;
    @javafx.fxml.FXML
    private Tab citationTab;
    @javafx.fxml.FXML
    private TextField citarea;
    @javafx.fxml.FXML
    private TextField trafage;
    @javafx.fxml.FXML
    private TextField searchbacmeasure;
    @javafx.fxml.FXML
    private TextField arrestdesc;
    @javafx.fxml.FXML
    private TextField arrestarea;
    @javafx.fxml.FXML
    private TextField citgender;
    @javafx.fxml.FXML
    private TextField incactionstaken;
    @javafx.fxml.FXML
    private TextField arrestambulance;
    @javafx.fxml.FXML
    private TextField citstreet;
    @javafx.fxml.FXML
    private TextField arrestname;
    @javafx.fxml.FXML
    private TableView calloutTable;
    @javafx.fxml.FXML
    private TextField patnum;
    @javafx.fxml.FXML
    private TextField incarea;
    @javafx.fxml.FXML
    private HBox trafficStopInfo;
    @javafx.fxml.FXML
    private TextField citmodel;
    @javafx.fxml.FXML
    private TextField calcounty;
    @javafx.fxml.FXML
    private TextField calgrade;
    @javafx.fxml.FXML
    private TextField inccounty;
    @javafx.fxml.FXML
    private Label impupdatedLabel;
    @javafx.fxml.FXML
    private TextField impgender;
    @javafx.fxml.FXML
    private TextField caltype;
    @javafx.fxml.FXML
    private Tab calloutTab;
    @javafx.fxml.FXML
    private TextField impplatenum;
    @javafx.fxml.FXML
    private TextField arrestage;
    @javafx.fxml.FXML
    private Tab patrolTab;
    @javafx.fxml.FXML
    private TextField searchcounty;
    @javafx.fxml.FXML
    private TextField cittype;
    @javafx.fxml.FXML
    private Label searchupdatedlabel;
    @javafx.fxml.FXML
    private TextField imptype;
    @javafx.fxml.FXML
    private TextField trafname;
    @javafx.fxml.FXML
    private TextField incstreet;
    @javafx.fxml.FXML
    private TextField impage;
    @javafx.fxml.FXML
    private HBox searchInfo;
    @javafx.fxml.FXML
    private TextField trafdesc;
    @javafx.fxml.FXML
    private Tab incidentTab;
    @javafx.fxml.FXML
    private Label patupdatedlabel;
    @javafx.fxml.FXML
    private TextField impcomments;
    @javafx.fxml.FXML
    private HBox calloutInfo;
    @javafx.fxml.FXML
    private TextField incvictims;
    @javafx.fxml.FXML
    private Tab trafficStopTab;
    @javafx.fxml.FXML
    private TextField trafnum;
    @javafx.fxml.FXML
    private TextField arreststreet;
    @javafx.fxml.FXML
    private TextField trafaddress;
    @javafx.fxml.FXML
    private TextField citnumber;
    @javafx.fxml.FXML
    private TextField patlength;
    @javafx.fxml.FXML
    private Label arrestupdatedlabel;
    @javafx.fxml.FXML
    private TextField trafarea;
    @javafx.fxml.FXML
    private Tab impoundTab;
    @javafx.fxml.FXML
    private TextField trafgender;
    private IncidentLogEntry incidentEntry;
    private ImpoundLogEntry impoundEntry;
    private SearchLogEntry searchEntry;
    private ArrestLogEntry arrestEntry;
    private TrafficCitationLogEntry citationEntry;
    @javafx.fxml.FXML
    private Label detailsLabelFill;
    @javafx.fxml.FXML
    private Label logManagerLabelBkg;
    @javafx.fxml.FXML
    private Label reportPlusLabelFill;
    @javafx.fxml.FXML
    private Button btn8;
    @javafx.fxml.FXML
    private Button btn6;
    @javafx.fxml.FXML
    private Button btn7;
    @javafx.fxml.FXML
    private Button btn4;
    @javafx.fxml.FXML
    private Button btn5;
    @javafx.fxml.FXML
    private Button btn2;
    @javafx.fxml.FXML
    private Button btn3;
    @javafx.fxml.FXML
    private Button btn1;
    @javafx.fxml.FXML
    private HBox citationInfo;
    @javafx.fxml.FXML
    private TableView patrolTable;
    @javafx.fxml.FXML
    private TextField calarea;
    @javafx.fxml.FXML
    private TextField impaddress;
    @javafx.fxml.FXML
    private TextField arresttaser;
    @javafx.fxml.FXML
    private TextField trafplatenum;
    @javafx.fxml.FXML
    private HBox arrestInfo;
    @javafx.fxml.FXML
    private TabPane tabPane;
    @javafx.fxml.FXML
    private TableView incidentTable;
    @javafx.fxml.FXML
    private TextField incwitness;
    @javafx.fxml.FXML
    private Label serverStatusLabel;
    @javafx.fxml.FXML
    private Button showIDBtn;
    @javafx.fxml.FXML
    private Button showCalloutBtn;
    @javafx.fxml.FXML
    private MenuItem vehLookupBtn;
    @javafx.fxml.FXML
    private TextField vehSearchField;
    @javafx.fxml.FXML
    private Button pedSearchBtn;
    @javafx.fxml.FXML
    private TextField pedSearchField;
    @javafx.fxml.FXML
    private AnchorPane pedLookupPane;
    @javafx.fxml.FXML
    private MenuItem pedLookupBtn;
    @javafx.fxml.FXML
    private Button vehSearchBtn;
    @javafx.fxml.FXML
    private AnchorPane vehLookupPane;
    @javafx.fxml.FXML
    private TextField pedgenfield;
    @javafx.fxml.FXML
    private TextField peddobfield;
    @javafx.fxml.FXML
    private TextField pedlicensefield;
    @javafx.fxml.FXML
    private TextField pedwantedfield;
    @javafx.fxml.FXML
    private TextField pedlnamefield;
    @javafx.fxml.FXML
    private TextField pedfnamefield;
    @javafx.fxml.FXML
    private TextField vehinsfield;
    @javafx.fxml.FXML
    private TextField vehownerfield;
    @javafx.fxml.FXML
    private TextField vehregfield;
    @javafx.fxml.FXML
    private TextField vehstolenfield;
    @javafx.fxml.FXML
    private TextField vehmodelfield;
    @javafx.fxml.FXML
    private Label vehplatefield;
    @javafx.fxml.FXML
    private AnchorPane vehcolordisplay;
    @javafx.fxml.FXML
    private TextField vehplatefield2;
    @javafx.fxml.FXML
    private AnchorPane vehRecordPane;
    @javafx.fxml.FXML
    private Label vehnocolorlabel;
    @javafx.fxml.FXML
    private Label versionLabel;
    @javafx.fxml.FXML
    private Label pedrecordnamefield;
    @javafx.fxml.FXML
    private Label noRecordFoundLabelVeh;
    @javafx.fxml.FXML
    private AnchorPane pedRecordPane;
    @javafx.fxml.FXML
    private Label noRecordFoundLabelPed;


    //</editor-fold>


    //<editor-fold desc="Utils">


    private void loadTheme() throws IOException {
        changeBarColors(getReportChart());
        changeStatisticColors(areaReportChart);
        //Main
        String mainclr = ConfigReader.configRead("mainColor");
        topPane.setStyle("-fx-background-color: " + mainclr + ";");
        mainColor6.setStyle("-fx-text-fill: " + mainclr + ";");
        mainColor8.setStyle("-fx-text-fill: " + mainclr + ";");
        mainColor7Bkg.setStyle("-fx-background-color: " + mainclr + ";");
        mainColor9Bkg.setStyle("-fx-background-color: " + mainclr + ";");
        logManagerLabelBkg.setStyle("-fx-background-color: " + mainclr + ";");
        detailsLabelFill.setStyle("-fx-text-fill: " + mainclr + ";");
        //Secondary
        String secclr = ConfigReader.configRead("secondaryColor");
        sidepane.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor2.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor3.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor4.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor5.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor3Bkg.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor4Bkg.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor5Bkg.setStyle("-fx-background-color: " + secclr + ";");
        reportPlusLabelFill.setStyle("-fx-text-fill: " + secclr + ";");
        //Accent
        String accclr = ConfigReader.configRead("accentColor");
        //Buttons
        String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
        String initialStyle = "-fx-background-color: transparent;";
        String nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("accentColor") + ";";
        updateInfoBtn.setStyle(nonTransparentBtn);
        showManagerToggle.setStyle(nonTransparentBtn);
        btn1.setStyle(nonTransparentBtn);
        btn2.setStyle(nonTransparentBtn);
        btn3.setStyle(nonTransparentBtn);
        btn4.setStyle(nonTransparentBtn);
        btn5.setStyle(nonTransparentBtn);
        btn6.setStyle(nonTransparentBtn);
        btn7.setStyle(nonTransparentBtn);
        btn8.setStyle(nonTransparentBtn);

        // Add hover event handling
        btn1.setOnMouseEntered(e -> btn1.setStyle(hoverStyle));
        btn1.setOnMouseExited(e -> btn1.setStyle(nonTransparentBtn));
        btn2.setOnMouseEntered(e -> btn2.setStyle(hoverStyle));
        btn2.setOnMouseExited(e -> btn2.setStyle(nonTransparentBtn));
        btn3.setOnMouseEntered(e -> btn3.setStyle(hoverStyle));
        btn3.setOnMouseExited(e -> btn3.setStyle(nonTransparentBtn));
        btn4.setOnMouseEntered(e -> btn4.setStyle(hoverStyle));
        btn4.setOnMouseExited(e -> btn4.setStyle(nonTransparentBtn));
        btn5.setOnMouseEntered(e -> btn5.setStyle(hoverStyle));
        btn5.setOnMouseExited(e -> btn5.setStyle(nonTransparentBtn));
        btn6.setOnMouseEntered(e -> btn6.setStyle(hoverStyle));
        btn6.setOnMouseExited(e -> btn6.setStyle(nonTransparentBtn));
        btn7.setOnMouseEntered(e -> btn7.setStyle(hoverStyle));
        btn7.setOnMouseExited(e -> btn7.setStyle(nonTransparentBtn));
        btn8.setOnMouseEntered(e -> btn8.setStyle(hoverStyle));
        btn8.setOnMouseExited(e -> btn8.setStyle(nonTransparentBtn));
        showManagerToggle.setOnMouseEntered(e -> showManagerToggle.setStyle(hoverStyle));
        showManagerToggle.setOnMouseExited(e -> showManagerToggle.setStyle(nonTransparentBtn));
        shiftInfoBtn.setOnMouseEntered(e -> shiftInfoBtn.setStyle(hoverStyle));
        shiftInfoBtn.setOnMouseExited(e -> shiftInfoBtn.setStyle(initialStyle));
        settingsDropdown.setOnMouseEntered(e -> settingsDropdown.setStyle("-fx-background-color: " + secclr + ";"));
        settingsDropdown.setOnMouseExited(e -> settingsDropdown.setStyle(initialStyle));
        notesButton.setOnMouseEntered(e -> notesButton.setStyle(hoverStyle));
        notesButton.setOnMouseExited(e -> notesButton.setStyle(initialStyle));
        createReportBtn.setOnMouseEntered(e -> createReportBtn.setStyle(hoverStyle));
        createReportBtn.setOnMouseExited(e -> createReportBtn.setStyle(initialStyle));
        logsButton.setOnMouseEntered(e -> logsButton.setStyle(hoverStyle));
        logsButton.setOnMouseExited(e -> logsButton.setStyle(initialStyle));
        mapButton.setOnMouseEntered(e -> mapButton.setStyle(hoverStyle));
        mapButton.setOnMouseExited(e -> mapButton.setStyle(initialStyle));
        showIDBtn.setOnMouseEntered(e -> showIDBtn.setStyle(hoverStyle));
        showIDBtn.setOnMouseExited(e -> showIDBtn.setStyle(initialStyle));

        updateInfoBtn.setOnMouseEntered(e -> updateInfoBtn.setStyle(hoverStyle));
        updateInfoBtn.setOnMouseExited(e -> {
            updateInfoBtn.setStyle(nonTransparentBtn);
        });
    }

    private void showSettingsWindow() {
        Stage settingsStage = new Stage();

        ComboBox<String> mainWindowComboBox = new ComboBox<>();
        ComboBox<String> notesWindowComboBox = new ComboBox<>();
        ComboBox<String> ReportWindowComboBox = new ComboBox<>();
        ComboBox<String> reportDarkLight = new ComboBox<>();

        ComboBox<String> themeComboBox = new ComboBox<>();

        Button resetBtn = new Button("Reset Defaults");
        resetBtn.getStyleClass().add("purpleButton");
        resetBtn.setStyle("-fx-label-padding: 1 7; -fx-padding: 1;");

        ColorPicker primPicker = new ColorPicker();
        ColorPicker secPicker = new ColorPicker();
        ColorPicker accPicker = new ColorPicker();
        Label primaryLabel = new Label("Primary Color:");
        primaryLabel.getStyleClass().add("headingBig");
        Label secondaryLabel = new Label("Secondary Color:");
        secondaryLabel.getStyleClass().add("headingBig");
        Label accentLabel = new Label("Accent Color:");
        accentLabel.getStyleClass().add("headingBig");

        Label reportColorLabel = new Label("Report Style:");
        primaryLabel.getStyleClass().add("headingBig");
        reportColorLabel.getStyleClass().add("headingBig");
        reportColorLabel.setStyle("-fx-text-fill: #404040;");

        Label themeLabel = new Label("Theme:");
        themeLabel.getStyleClass().add("headingBig");
        themeLabel.setStyle("-fx-text-fill: #404040;");

        Runnable loadColors = () -> {
            try {
                Color primary = Color.valueOf(ConfigReader.configRead("mainColor"));
                Color secondary = Color.valueOf(ConfigReader.configRead("secondaryColor"));
                Color accent = Color.valueOf(ConfigReader.configRead("accentColor"));

                primPicker.setValue(primary);
                secPicker.setValue(secondary);
                accPicker.setValue(accent);
                primaryLabel.setStyle("-fx-text-fill: " + toHexString(primary) + ";");
                secondaryLabel.setStyle("-fx-text-fill: " + toHexString(secondary) + ";");
                accentLabel.setStyle("-fx-text-fill: " + toHexString(accent) + ";");

                try {
                    String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
                    String nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("accentColor") + ";";
                    resetBtn.setStyle(nonTransparentBtn);
                    resetBtn.setOnMouseEntered(e -> resetBtn.setStyle(hoverStyle));
                    resetBtn.setOnMouseExited(e -> resetBtn.setStyle(nonTransparentBtn));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } catch (IOException e) {
                logError("LoadTheme IO Error Code 1 ", e);
            }
        };

        loadColors.run();

        resetBtn.setOnAction(event -> {
            updateMain(Color.valueOf("#524992"));
            updateSecondary(Color.valueOf("#665cb6"));
            updateAccent(Color.valueOf("#9c95d0"));
            try {
                loadTheme();
                loadColors.run();
            } catch (IOException e) {
                logError("LoadTheme IO Error Code 2 ", e);
            }
        });

        primPicker.valueProperty().addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observable, Color oldValue, Color newValue) {
                Color selectedColor = newValue;
                updateMain(selectedColor);
                try {
                    loadTheme();
                    loadColors.run();
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
                    loadColors.run();
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
                    loadColors.run();
                } catch (IOException e) {
                    logError("LoadTheme IO Error Code 5 ", e);
                }
            }
        });

        String[] reportdarklight = {"dark", "light"};
        String[] themes = {"dark", "purple", "blue", "grey", "green"};
        String[] displayPlacements = {"Default", "Top Left", "Top Right", "Bottom Left", "Bottom Right", "\n", "Full Left", "Full Right"};
        mainWindowComboBox.getItems().addAll(displayPlacements);
        notesWindowComboBox.getItems().addAll(displayPlacements);
        ReportWindowComboBox.getItems().addAll(displayPlacements);
        reportDarkLight.getItems().addAll(reportdarklight);

        themeComboBox.getItems().addAll(themes);

        try {
            if (ConfigReader.configRead("reportWindowDarkMode").equals("true")) {
                reportDarkLight.getSelectionModel().selectFirst();
            } else {
                reportDarkLight.getSelectionModel().selectLast();
            }
        } catch (IOException e) {
            logError("DarkMode IO Error Code 1 ", e);

        }

        try {
            mainWindowComboBox.setValue(ConfigReader.configRead("mainWindowLayout"));
            notesWindowComboBox.setValue(ConfigReader.configRead("notesWindowLayout"));
            ReportWindowComboBox.setValue(ConfigReader.configRead("reportWindowLayout"));
        } catch (IOException e) {
            logError("DarkMode IO Error Code 2 ", e);
        }

        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(15);
        root.setVgap(15);

        Label displayPlacementsLabel = new Label("Display Placements");
        displayPlacementsLabel.setStyle("-fx-font-size: 20; -fx-font-family: Segoe UI Black;");
        Label colorsLabel = new Label("Colors");
        colorsLabel.setStyle("-fx-font-size: 20; -fx-font-family: Arial; -fx-font-weight: bold; -fx-text-fill: black;");

        // Add headings for display settings
        root.addRow(0, displayPlacementsLabel);
        root.addRow(1, new Label("Main Window Placement:"), mainWindowComboBox);
        root.addRow(2, new Label("Notes Window Placement:"), notesWindowComboBox);
        root.addRow(3, new Label("Report Window Placement:"), ReportWindowComboBox);

        // Add empty row
        root.addRow(4, new Label());

        // Add headings for color settings
        root.addRow(5, colorsLabel);
        root.addRow(6, themeLabel, themeComboBox);
        root.addRow(7, primaryLabel, primPicker);
        root.addRow(8, secondaryLabel, secPicker);
        root.addRow(9, accentLabel, accPicker);
        root.addRow(10, reportColorLabel, reportDarkLight);

        // Add reset button
        root.addRow(11, resetBtn);

        EventHandler<ActionEvent> comboBoxHandler = event -> {
            ComboBox<String> comboBox = (ComboBox<String>) event.getSource();
            String selectedPlacement = comboBox.getSelectionModel().getSelectedItem();

            if (comboBox == mainWindowComboBox) {
                if ("Default".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("mainWindowLayout", "Default");
                } else if ("Top Left".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("mainWindowLayout", "TopLeft");
                } else if ("Top Right".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("mainWindowLayout", "TopRight");
                } else if ("Bottom Left".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("mainWindowLayout", "BottomLeft");
                } else if ("Bottom Right".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("mainWindowLayout", "BottomRight");
                } else if ("Full Left".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("mainWindowLayout", "FullLeft");
                } else if ("Full Right".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("mainWindowLayout", "FullRight");
                }
            }

            if (comboBox == notesWindowComboBox) {
                if ("Default".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("notesWindowLayout", "Default");
                } else if ("Top Left".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("notesWindowLayout", "TopLeft");
                } else if ("Top Right".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("notesWindowLayout", "TopRight");
                } else if ("Bottom Left".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("notesWindowLayout", "BottomLeft");
                } else if ("Bottom Right".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("notesWindowLayout", "BottomRight");
                } else if ("Full Left".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("notesWindowLayout", "FullLeft");
                } else if ("Full Right".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("notesWindowLayout", "FullRight");
                }
            }

            if (comboBox == ReportWindowComboBox) {
                if ("Default".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("reportWindowLayout", "Default");
                } else if ("Top Left".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("reportWindowLayout", "TopLeft");
                } else if ("Top Right".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("reportWindowLayout", "TopRight");
                } else if ("Bottom Left".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("reportWindowLayout", "BottomLeft");
                } else if ("Bottom Right".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("reportWindowLayout", "BottomRight");
                } else if ("Full Left".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("reportWindowLayout", "FullLeft");
                } else if ("Full Right".equals(selectedPlacement)) {
                    ConfigWriter.configwrite("reportWindowLayout", "FullRight");
                }
            }
        };

        themeComboBox.setOnAction(actionEvent -> {
            String selectedTheme = themeComboBox.getSelectionModel().getSelectedItem();

            switch (selectedTheme) {
                case "dark" -> {
                    log("Dark Theme Selected", LogUtils.Severity.DEBUG);
                    updateMain(Color.valueOf("#263238"));
                    updateSecondary(Color.valueOf("#323C41"));
                    updateAccent(Color.valueOf("#505d62"));
                    try {
                        loadTheme();
                        loadColors.run();
                    } catch (IOException e) {
                        logError("LoadTheme Error", e);
                    }
                }
                case "purple" -> {
                    log("Purple Theme Selected", LogUtils.Severity.DEBUG);
                    updateMain(Color.valueOf("#524992"));
                    updateSecondary(Color.valueOf("#665cb6"));
                    updateAccent(Color.valueOf("#9c95d0"));
                    try {
                        loadTheme();
                        loadColors.run();
                    } catch (IOException e) {
                        logError("LoadTheme Error", e);
                    }
                }
                case "blue" -> {
                    log("Blue Theme Selected", LogUtils.Severity.DEBUG);
                    updateMain(Color.valueOf("#4d66cc"));
                    updateSecondary(Color.valueOf("#6680e6"));
                    updateAccent(Color.valueOf("#b3ccff"));
                    try {
                        loadTheme();
                        loadColors.run();
                    } catch (IOException e) {
                        logError("LoadTheme Error", e);
                    }
                }
                case "grey" -> {
                    log("Grey Theme Selected", LogUtils.Severity.DEBUG);
                    updateMain(Color.valueOf("#666666"));
                    updateSecondary(Color.valueOf("#808080"));
                    updateAccent(Color.valueOf("#4d4d4d"));
                    try {
                        loadTheme();
                        loadColors.run();
                    } catch (IOException e) {
                        logError("LoadTheme Error", e);
                    }
                }
                case "green" -> {
                    log("Green Theme Selected", LogUtils.Severity.DEBUG);
                    updateMain(Color.valueOf("#4d804d"));
                    updateSecondary(Color.valueOf("#669966"));
                    updateAccent(Color.valueOf("#99cc99"));
                    try {
                        loadTheme();
                        loadColors.run();
                    } catch (IOException e) {
                        logError("LoadTheme Error", e);
                    }
                }

            }

        });

        mainWindowComboBox.setOnAction(comboBoxHandler);
        notesWindowComboBox.setOnAction(comboBoxHandler);
        ReportWindowComboBox.setOnAction(comboBoxHandler);
        reportDarkLight.setOnAction(event -> {
            if (reportDarkLight.getSelectionModel().getSelectedItem().equals("dark")) {
                ConfigWriter.configwrite("reportWindowDarkMode", "true");
            } else {
                ConfigWriter.configwrite("reportWindowDarkMode", "false");
            }
        });

        Scene scene = new Scene(root);
        settingsStage.setScene(scene);
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formLabels.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/menu/menuStyles.css").toExternalForm());

        settingsStage.setTitle("UI Settings");
        settingsStage.initStyle(StageStyle.UTILITY);
        settingsStage.setResizable(false);
        settingsStage.show();
        settingsStage.setAlwaysOnTop(true);
    }

    private void updateConnectionStatus(boolean isConnected) {
        if (!isConnected) {
            Platform.runLater(() -> {
                LogUtils.log("No Connection", LogUtils.Severity.WARN);
                serverStatusLabel.setText("No Connection");
                serverStatusLabel.setStyle("-fx-text-fill: #ff5a5a;");
                if (clientController != null) {
                    clientController.getPortField().setText("");
                    clientController.getInetField().setText("");
                    clientController.getStatusLabel().setText("Not Connected");
                    clientController.getStatusLabel().setStyle("-fx-background-color: #ff5e5e;");
                    serverStatusLabel.setStyle("-fx-text-fill: #ff5e5e;");
                }
            });
        } else {
            Platform.runLater(() -> {
                serverStatusLabel.setText("Connected");
                serverStatusLabel.setStyle("-fx-text-fill: #00da16;");
                if (clientController != null) {
                    clientController.getPortField().setText(ClientUtils.port);
                    clientController.getInetField().setText(ClientUtils.inet);
                    clientController.getStatusLabel().setText("Connected");
                    clientController.getStatusLabel().setStyle("-fx-background-color: green;");
                }
            });
        }
    }

    public void initialize() throws IOException {

        checkForUpdates();

        setDisable(infoPane, logPane, pedLookupPane, vehLookupPane);
        setActive(shiftInformationPane);
        needRefresh.set(0);
        needRefresh.addListener((obs, oldValue, newValue) -> {
            if (newValue.equals(1)) {
                loadLogs();
                needRefresh.set(0);
            }
        });

        startupFullscreenToggleBtn.setSelected(ConfigReader.configRead("fullscreenOnStartup").equals("true"));

        notesText = "";

        refreshChart();
        updateChartIfMismatch(reportChart);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        getOfficerInfoRank().getItems().addAll(dropdownInfo.ranks);
        getOfficerInfoDivision().getItems().addAll(dropdownInfo.divisions);
        getOfficerInfoAgency().getItems().addAll(dropdownInfo.agencies);

        OfficerInfoName.setText(name);
        OfficerInfoDivision.setValue(division);
        OfficerInfoRank.setValue(rank);
        OfficerInfoAgency.setValue(agency);
        OfficerInfoNumber.setText(number);
        generatedByTag.setText("Generated By:" + " " + name);
        String time = DataTerminalHomeApplication.getTime();
        generatedDateTag.setText("Generated at: " + time);

        areaReportChart.getData().add(parseEveryLog("area"));

        getOfficerInfoDivision().setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                            setAlignment(javafx.geometry.Pos.CENTER);

                            // Check if the item contains "=" and set it as bold
                            if (item.contains("=")) {
                                setStyle("-fx-font-weight: bold;");
                            } else {
                                setStyle("-fx-font-weight: none;");
                            }
                        }
                    }
                };
            }
        });
        loadTheme();

        initializeCalloutColumns();
        initializeArrestColumns();
        initializeCitationColumns();
        initializeImpoundColumns();
        initializeIncidentColumns();
        initializePatrolColumns();
        initializeSearchColumns();
        initializeTrafficStopColumns();
        loadLogs();

        calloutInfo.setVisible(true);
        lowerPane.setPrefHeight(0);
        lowerPane.setMaxHeight(0);
        lowerPane.setMinHeight(0);
        lowerPane.setVisible(false);

        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            calloutInfo.setVisible(newTab != null && "calloutTab".equals(newTab.getId()));
            patrolInfo.setVisible(newTab != null && "patrolTab".equals(newTab.getId()));
            trafficStopInfo.setVisible(newTab != null && "trafficStopTab".equals(newTab.getId()));
            incidentInfo.setVisible(newTab != null && "incidentTab".equals(newTab.getId()));
            impoundInfo.setVisible(newTab != null && "impoundTab".equals(newTab.getId()));
            arrestInfo.setVisible(newTab != null && "arrestTab".equals(newTab.getId()));
            searchInfo.setVisible(newTab != null && "searchTab".equals(newTab.getId()));
            citationInfo.setVisible(newTab != null && "citationTab".equals(newTab.getId()));
        });

        Platform.runLater(() -> {
            versionLabel.setText(stringUtil.version);

            vbox.getScene().getWindow().setOnHiding(event -> handleClose());

            if (!stringUtil.version.equals(gitVersion)) {
                versionLabel.setText(gitVersion + " Available!");
                versionLabel.setStyle("-fx-text-fill: red;");
                versionLabel.setOnMouseClicked(event -> openWebpage("https://github.com/zainrd123/DataTerminal/releases"));
            }
        });

    }

    public void refreshChart() throws IOException {
        // Clear existing data from the chart
        reportChart.getData().clear();
        String[] categories = {"Callout", "Arrests", "Traffic Stops", "Patrols", "Searches", "Incidents", "Impounds", "Citations"};
        CategoryAxis xAxis = (CategoryAxis) getReportChart().getXAxis();

        xAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(categories)));
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Series 1");

        String color = ConfigReader.configRead("mainColor");
        for (String category : categories) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(category, 1); // Value doesn't matter, just need to add a data point
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " + color + ";");
                }
            });
            series1.getData().add(data);
        }

        getReportChart().getData().add(series1);
    }

    private void handleClose() {
        Platform.exit();
        System.exit(0);
    }


    //</editor-fold>


    //<editor-fold desc="Getters">


    public ComboBox getOfficerInfoAgency() {
        return OfficerInfoAgency;
    }

    public AnchorPane getShiftInformationPane() {
        return shiftInformationPane;
    }

    public ComboBox getOfficerInfoDivision() {
        return OfficerInfoDivision;
    }

    public TextField getOfficerInfoName() {
        return OfficerInfoName;
    }

    public TextField getOfficerInfoNumber() {
        return OfficerInfoNumber;
    }

    public ComboBox getOfficerInfoRank() {
        return OfficerInfoRank;
    }

    public AnchorPane getTopPane() {
        return topPane;
    }

    public AnchorPane getInfoPane() {
        return infoPane;
    }

    public BarChart getReportChart() {
        return reportChart;
    }


    //</editor-fold>


    //<editor-fold desc="WindowUtils">


    @javafx.fxml.FXML
    public void onMinimizeBtnClick(Event event) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        stage.setIconified(true);
    }

    @javafx.fxml.FXML
    public void onFullscreenBtnClick(Event event) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        if (stage != null) {
            toggleWindowedFullscreen(stage, 1150, 800);
        }
    }

    @javafx.fxml.FXML
    public void onExitButtonClick(MouseEvent actionEvent) {
        handleClose();
    }


    //</editor-fold>


    //<editor-fold desc="Side Button Events">


    @javafx.fxml.FXML
    public void onShowIDButtonClick(ActionEvent actionEvent) throws IOException {
        if (IDStage != null && IDStage.isShowing()) {
            IDStage.toFront();
            IDStage.requestFocus();
            return;
        }
        IDStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("currentID-view.fxml"));
        Parent root = loader.load();
        BorderlessScene newScene = new BorderlessScene(IDStage, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
        AnchorPane topbar = CurrentIDViewController.getTitleBar();
        newScene.setMoveControl(topbar);
        IDStage.setTitle("Current ID");
        IDStage.setScene(newScene);
        //IDStage.initOwner(DataTerminalHomeApplication.getMainRT());
        IDStage.show();
        IDStage.centerOnScreen();
        showButtonAnimation(showIDBtn);

        IDStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                IDStage = null;
            }
        });
    }

    @javafx.fxml.FXML
    public void onMapButtonClick(ActionEvent actionEvent) throws IOException {
        if (mapStage != null && mapStage.isShowing()) {
            mapStage.toFront();
            return;
        }

        mapStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("map-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        mapStage.setTitle("Los Santos Map");
        mapStage.setScene(newScene);
        mapStage.initStyle(StageStyle.UTILITY);
        mapStage.setResizable(false);
        mapStage.show();
        mapStage.centerOnScreen();
        mapStage.setAlwaysOnTop(true);
        showButtonAnimation(mapButton);

        mapStage.setOnHidden(event -> {
            mapStage = null;
        });
    }

    @javafx.fxml.FXML
    public void onNotesButtonClicked(ActionEvent actionEvent) throws IOException {
        if (notesStage != null && notesStage.isShowing()) {
            notesStage.toFront();
            notesStage.requestFocus();
            return;
        }

        notesStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("notes-view.fxml"));
        Parent root = loader.load();
        notesViewController = loader.getController();
        BorderlessScene newScene = new BorderlessScene(notesStage, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
        notesStage.setTitle("Notes");
        notesStage.setScene(newScene);
        notesStage.setResizable(true);
        //notesStage.initOwner(DataTerminalHomeApplication.getMainRT());
        notesStage.show();

        String startupValue = ConfigReader.configRead("notesWindowLayout");
        switch (startupValue) {
            case "TopLeft" -> snapToTopLeft(notesStage);
            case "TopRight" -> snapToTopRight(notesStage);
            case "BottomLeft" -> snapToBottomLeft(notesStage);
            case "BottomRight" -> snapToBottomRight(notesStage);
            case "FullLeft" -> snapToLeft(notesStage);
            case "FullRight" -> snapToRight(notesStage);
            default -> {
                notesStage.centerOnScreen();
                notesStage.setMinHeight(300);
                notesStage.setMinWidth(300);
            }
        }
        notesStage.getScene().getStylesheets().add(getClass().getResource("css/notification-styles.css").toExternalForm());
        showButtonAnimation(notesButton);
        AnchorPane topbar = notesViewController.getTitlebar();
        newScene.setMoveControl(topbar);
        notesStage.setAlwaysOnTop(true);

        notesStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                notesStage = null;
                actionController.notesText = notesViewController.getNotepadTextArea().getText();
            }
        });
    }

    @javafx.fxml.FXML
    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
        setDisable(infoPane, logPane, pedLookupPane, vehLookupPane);
        setActive(shiftInformationPane);
        showButtonAnimation(shiftInfoBtn);
        controllerUtils.refreshChart(areaReportChart, "area");
    }

    @javafx.fxml.FXML
    public void onLogsButtonClick(ActionEvent actionEvent) {
        showButtonAnimation(logsButton);
        setDisable(infoPane, shiftInformationPane, pedLookupPane, vehLookupPane);
        setActive(logPane);
    }

    @javafx.fxml.FXML
    public void onShowCalloutButtonClick(ActionEvent actionEvent) throws IOException {
        if (CalloutStage != null && CalloutStage.isShowing()) {
            CalloutStage.toFront();
            CalloutStage.requestFocus();
            return;
        }
        CalloutStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("callout-view.fxml"));
        Parent root = loader.load();
        BorderlessScene newScene = new BorderlessScene(CalloutStage, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
        AnchorPane topbar = calloutController.getTopBar();
        newScene.setMoveControl(topbar);
        CalloutStage.setTitle("Callout Display");
        CalloutStage.setScene(newScene);
        CalloutStage.show();
        CalloutStage.centerOnScreen();
        showButtonAnimation(showCalloutBtn);

        CalloutStage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                CalloutStage = null;
            }
        });
    }

    @javafx.fxml.FXML
    public void onVehLookupBtnClick(ActionEvent actionEvent) {
        setDisable(infoPane, logPane, pedLookupPane, shiftInformationPane);
        vehRecordPane.setVisible(false);
        noRecordFoundLabelVeh.setVisible(false);
        setActive(vehLookupPane);
    }

    @javafx.fxml.FXML
    public void onPedLookupBtnClick(ActionEvent actionEvent) {
        setDisable(infoPane, logPane, vehLookupPane, shiftInformationPane);
        pedRecordPane.setVisible(false);
        noRecordFoundLabelPed.setVisible(false);
        setActive(pedLookupPane);
    }


    //</editor-fold>


    //<editor-fold desc="Settings Button Events">


    @javafx.fxml.FXML
    public void logOutputBtnPress(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("output-view.fxml"));
        Parent root = loader.load();
        BorderlessScene newScene = new BorderlessScene(stage, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
        AnchorPane topbar = OutputViewController.getTitlebar();
        newScene.setMoveControl(topbar);
        stage.setTitle("Report Manager");
        stage.setScene(newScene);
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void UISettingsBtnClick(ActionEvent actionEvent) {
        showSettingsWindow();
    }

    @javafx.fxml.FXML
    public void onStartupFullscreenPress(ActionEvent actionEvent) {
        if (startupFullscreenToggleBtn.isSelected()) {
            ConfigWriter.configwrite("fullscreenOnStartup", "true");
            startupFullscreenToggleBtn.setSelected(true);
        } else {
            ConfigWriter.configwrite("fullscreenOnStartup", "false");
            startupFullscreenToggleBtn.setSelected(false);
        }
    }

    @javafx.fxml.FXML
    public void testBtnPress(ActionEvent actionEvent) throws IOException {

        //ClientUtils.sendInfoToServer("licensePlate=43HOV967&model=POUNDER&isStolen=False&isPolice=False&owner=Barrie Sheen&driver=Barrie Sheen&registration=None&insurance=Valid&color=31-34-38,licensePlate=42WDS770&model=RUINER&isStolen=False&isPolice=False&owner=Caitlyne Woods&driver=Caitlyne Woods&registration=Valid&insurance=Valid&color=15-31-21,licensePlate=81WPP452&model=SEMINOLE&isStolen=False&isPolice=False&owner=Kobe Ray&driver=Kobe Ray&registration=Expired&insurance=Valid&color=38-3-11,licensePlate=61VMA233&model=RUINER&isStolen=False&isPolice=False&owner=Lamar Davis&driver=Chloe Jones&registration=Valid&insurance=Valid&color=50-59-71,licensePlate=83KAX485&model=SEMINOLE&isStolen=False&isPolice=False&owner=Eric Beck&driver=Eric Beck&registration=None&insurance=Expired&color=28-30-33,licensePlate=40OGQ656&model=SEMINOLE&isStolen=False&isPolice=False&owner=Banko Jerem&driver=&registration=Valid&insurance=Valid&color=38-3-11,licensePlate=45SMY089&model=EMPEROR2&isStolen=False&isPolice=False&owner=Emmitt Williams&driver=Emmitt Williams&registration=Valid&insurance=Valid&color=42-55-84,licensePlate=82MNI572&model=BLIMP&isStolen=False&isPolice=False&owner=David Bordiga&driver=David Bordiga&registration=Valid&insurance=Valid&color=236-234-234,licensePlate=06GPL092&model=SABREGT&isStolen=False&isPolice=False&owner=Megan Clegg&driver=Megan Clegg&registration=Valid&insurance=Valid&color=8-8-8,licensePlate=20ISB581&model=SEMINOLE&isStolen=False&isPolice=False&owner=Barry Wakenbake&driver=Barry Wakenbake&registration=Valid&insurance=Valid&color=8-8-8,licensePlate=87YEL919&model=HUNTLEY&isStolen=False&isPolice=False&owner=Jamie Simpson&driver=&registration=Valid&insurance=Expired&color=8-8-8,licensePlate=04ATW667&model=PEYOTE&isStolen=False&isPolice=False&owner=Grandle Cooper&driver=Grandle Cooper&registration=Valid&insurance=Valid&color=107-0-0,licensePlate=63GIA616&model=SEMINOLE&isStolen=False&isPolice=False&owner=Katherine Brooks&driver=&registration=Valid&insurance=Valid&color=207-204-204,licensePlate=60GTT189&model=TACO&isStolen=False&isPolice=False&owner=Altair Labadiena&driver=Altair Labadiena&registration=Valid&insurance=Valid&color=207-204-204,licensePlate=83JMJ371&model=SEMINOLE&isStolen=False&isPolice=False&owner=Heather Hills&driver=&registration=Valid&insurance=Valid&color=97-95-85,licensePlate=04KUE036&model=SABREGT&isStolen=False&isPolice=False&owner=Hannah Kamore&driver=Hannah Kamore&registration=Valid&insurance=Valid&color=8-8-8,licensePlate=05DAJ436&model=SEMINOLE&isStolen=False&isPolice=False&owner=Hector Martinez&driver=&registration=Valid&insurance=Valid&color=81-84-89,licensePlate=02KCV600&model=SEMINOLE&isStolen=False&isPolice=False&owner=Ryan Lamberti&driver=&registration=Valid&insurance=Valid&color=8-8-8,licensePlate=45BVO158&model=MULE&isStolen=False&isPolice=False&owner=Bruce Dickinson&driver=&registration=Valid&insurance=Expired&color=207-204-204,licensePlate=06DGF092&model=SABREGT&isStolen=False&isPolice=False&owner=Kevin Roberts&driver=Kevin Roberts&registration=Valid&insurance=Valid&color=183-67-52,licensePlate=20BAI829&model=RUINER&isStolen=False&isPolice=False&owner=Mario Gonzales&driver=Mario Gonzales&registration=Expired&insurance=Valid&color=0-26-11,licensePlate=09NBY773&model=SEMINOLE&isStolen=False&isPolice=False&owner=Tahlil Chowdhury&driver=&registration=Valid&insurance=Valid&color=81-84-89,licensePlate=87EXT765&model=PEYOTE&isStolen=False&isPolice=False&owner=Bobby Wilson&driver=Bobby Wilson&registration=Expired&insurance=Valid&color=8-8-8,licensePlate=45LNL190&model=SEMINOLE&isStolen=False&isPolice=False&owner=Collin Bay&driver=&registration=Valid&insurance=Valid&color=97-95-85,licensePlate=42ZTJ582&model=SEMINOLE&isStolen=False&isPolice=False&owner=Tahlil Chowdhury&driver=&registration=Expired&insurance=Valid&color=38-3-11,licensePlate=69DDF172&model=MANANA&isStolen=False&isPolice=False&owner=Alena Angel&driver=&registration=Valid&insurance=Valid&color=207-204-204,licensePlate=60SMV911&model=HUNTLEY&isStolen=False&isPolice=False&owner=Logan Black&driver=&registration=Valid&insurance=Valid&color=207-204-204,licensePlate=62BGP130&model=EMPEROR2&isStolen=False&isPolice=False&owner=Marielle Erikson&driver=Marielle Erikson&registration=Valid&insurance=Valid&color=89-37-37,licensePlate=85REY188&model=MANANA&isStolen=False&isPolice=False&owner=Rosie Water&driver=Rosie Water&registration=None&insurance=Valid&color=38-3-11,licensePlate=06VBG175&model=SEMINOLE&isStolen=False&isPolice=False&owner=Carlos Dryzz&driver=Carlos Dryzz&registration=Valid&insurance=Valid&color=8-8-8,licensePlate=43ODL830&model=EMPEROR2&isStolen=False&isPolice=False&owner=Giuseppe Palminteri&driver=Giuseppe Palminteri&registration=Valid&insurance=Expired&color=48-60-94,licensePlate=45PLD142&model=MANANA&isStolen=False&isPolice=False&owner=Lilly Black&driver=&registration=Valid&insurance=Valid&color=41-44-46,licensePlate=45SLM510&model=SEMINOLE&isStolen=False&isPolice=False&owner=Joe Goldenburg&driver=Joe Goldenburg&registration=Valid&insurance=Valid&color=97-95-85,licensePlate=83CIH859&model=MANANA&isStolen=False&isPolice=False&owner=Gary wells&driver=Gary wells&registration=Valid&insurance=Valid&color=15-16-18,licensePlate=40HDM059&model=RUINER&isStolen=False&isPolice=False&owner=Alena Angel&driver=&registration=Valid&insurance=Valid&color=119-124-135,licensePlate=26OVA515&model=SEMINOLE&isStolen=False&isPolice=False&owner=Michael Stracci&driver=Christopher Falco&registration=Valid&insurance=Valid&color=90-94-102,licensePlate=04SCZ417&model=SEMINOLE&isStolen=False&isPolice=False&owner=Eleanor Disdier&driver=&registration=Expired&insurance=Valid&color=8-8-8,licensePlate=29ETH423&model=SEMINOLE&isStolen=False&isPolice=False&owner=Alexander Ramirez&driver=Jim Koks&registration=Valid&insurance=Valid&color=51-33-17,licensePlate=07JKW250&model=HUNTLEY&isStolen=False&isPolice=False&owner=Albert Ford&driver=&registration=Valid&insurance=Expired&color=207-204-204,licensePlate=47IKE161&model=RUINER&isStolen=False&isPolice=False&owner=Mark Adams&driver=Anthony Farstan&registration=Valid&insurance=Valid&color=81-84-89,licensePlate=28CJC460&model=EMPEROR2&isStolen=False&isPolice=False&owner=Kate Oria&driver=Kate Oria&registration=Valid&insurance=Valid&color=59-103-150,licensePlate=26NLQ870&model=POUNDER&isStolen=False&isPolice=False&owner=Tim Hook&driver=Tim Hook&registration=Valid&insurance=Valid&color=82-0-0,licensePlate=86QKS912&model=PEYOTE&isStolen=False&isPolice=False&owner=Lara Collins&driver=Lara Collins&registration=Valid&insurance=Valid&color=31-34-38,licensePlate=25ANO626&model=SEMINOLE&isStolen=False&isPolice=False&owner=Lucia Andrews&driver=&registration=Valid&insurance=Expired&color=28-30-33,licensePlate=67ROA612&model=RUINER&isStolen=False&isPolice=False&owner=Luke Kearney&driver=Luke Kearney&registration=Valid&insurance=Valid&color=8-8-8,licensePlate=26KCM416&model=SEMINOLE&isStolen=False&isPolice=False&owner=Max Smith&driver=&registration=Valid&insurance=Valid&color=35-41-46,licensePlate=00CPX351&model=MULE&isStolen=False&isPolice=False&owner=Adam Cipriani&driver=Adam Cipriani&registration=Expired&insurance=Expired&color=119-124-135,licensePlate=24BUW792&model=HUNTLEY&isStolen=False&isPolice=False&owner=Ben Sherman&driver=&registration=Valid&insurance=Valid&color=39-45-59,licensePlate=09XJU121&model=SEMINOLE&isStolen=False&isPolice=False&owner=Julia Ramirez&driver=&registration=Valid&insurance=Valid&color=207-204-204,licensePlate=81NEQ808&model=TORNADO&isStolen=False&isPolice=False&owner=Arthur Kane&driver=Arthur Kane&registration=Valid&insurance=None&color=41-44-46,licensePlate=06ETI242&model=SEMINOLE&isStolen=False&isPolice=False&owner=Jeff Macniel&driver=&registration=Valid&insurance=Valid&color=99-0-18,licensePlate=20WCX738&model=RUINER&isStolen=False&isPolice=False&owner=Kate Diamond&driver=Kate Diamond&registration=Valid&insurance=Expired&color=15-15-15,licensePlate=42CJS068&model=MANANA&isStolen=False&isPolice=False&owner=Jessica Woods&driver=Jessica Woods&registration=None&insurance=Valid&color=8-8-8,licensePlate=02AXT425&model=AMBULANCE&isStolen=False&isPolice=False&owner=Los Santos County Fire Department (EMS)&driver=&registration=Valid&insurance=Valid&color=219-223-234,licensePlate=44OTH054&model=POUNDER&isStolen=False&isPolice=False&owner=Julian Benjamin&driver=Julian Benjamin&registration=Expired&insurance=Valid&color=8-8-8,licensePlate=48ZWV037&model=TORNADO&isStolen=False&isPolice=False&owner=Daniel Evans&driver=Daniel Evans&registration=Valid&insurance=Valid&color=71-14-14,licensePlate=40DKY930&model=TORNADO&isStolen=False&isPolice=False&owner=Bob McKay&driver=Bob McKay&registration=Valid&insurance=Valid&color=207-204-204,licensePlate=46LOD088&model=SABREGT&isStolen=False&isPolice=False&owner=Marc Wright&driver=Andrew McCnight&registration=Valid&insurance=Valid&color=184-113-12,licensePlate=44BSP791&model=SEMINOLE&isStolen=False&isPolice=False&owner=Amanda Johnson&driver=&registration=Valid&insurance=Valid&color=0-56-5,licensePlate=65NHF066&model=SABREGT&isStolen=False&isPolice=False&owner=David Murdoch&driver=David Murdoch&registration=Valid&insurance=None&color=56-12-0,licensePlate=28MOT078&model=SEMINOLE&isStolen=False&isPolice=False&owner=Kyle Mason&driver=Jason Baye&registration=Valid&insurance=Expired&color=28-49-64,licensePlate=62ZWJ746&model=POLICE3&isStolen=False&isPolice=True&owner=Los Santos Police Department&driver=Laura Davis&registration=Valid&insurance=Valid&color=207-204-204,licensePlate=84XBW693&model=MULE&isStolen=False&isPolice=False&owner=Johanna Detop&driver=Hairy Rod&registration=Valid&insurance=Expired&color=182-181-181,licensePlate=04IYP384&model=SEMINOLE&isStolen=False&isPolice=False&owner=Kate Cana&driver=&registration=Valid&insurance=Valid&color=35-41-46,licensePlate=06OGL739&model=SEMINOLE&isStolen=False&isPolice=False&owner=Meek Mill&driver=&registration=Valid&insurance=Expired&color=41-44-46,licensePlate=22IMX391&model=SEMINOLE&isStolen=False&isPolice=False&owner=Chloe Jones&driver=&registration=Valid&insurance=Valid&color=0-56-5");
        /*Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("reportManager-view.fxml"));
        Parent root = loader.load();
        BorderlessScene newScene = new BorderlessScene(stage, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
        AnchorPane topbar = ReportManagerViewController.getTitlebar();
        newScene.setMoveControl(topbar);
        stage.setTitle("Report Manager");
        stage.setScene(newScene);
        stage.show();
        stage.centerOnScreen();*/
    }

    @javafx.fxml.FXML
    public void aboutBtnClick(ActionEvent actionEvent) {
        setDisable(shiftInformationPane, logPane, pedLookupPane, vehLookupPane);
        setActive(infoPane);
    }

    @javafx.fxml.FXML
    public void clearLogsBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        confirmLogClearDialog(stage, reportChart, areaReportChart);
        showNotification("Log Manager", "Logs have been cleared.", vbox);
    }

    @javafx.fxml.FXML
    public void clearAllSaveDataBtnClick(ActionEvent actionEvent) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        confirmSaveDataClearDialog(stage);
    }


    //</editor-fold>


    //<editor-fold desc="Open Report Button Events">


    @javafx.fxml.FXML
    public void onCalloutReportButtonClick(ActionEvent actionEvent) throws IOException {
        newCallout(reportChart, areaReportChart, vbox, notesViewController);
    }

    @javafx.fxml.FXML
    public void trafficStopReportButtonClick(ActionEvent actionEvent) throws IOException {
        newTrafficStop(reportChart, areaReportChart, vbox, notesViewController);
    }

    @javafx.fxml.FXML
    public void onIncidentReportBtnClick(ActionEvent actionEvent) throws IOException {
        newIncident(reportChart, areaReportChart, vbox, notesViewController);
    }

    @javafx.fxml.FXML
    public void onSearchReportBtnClick(ActionEvent actionEvent) throws IOException {
        newSearch(reportChart, areaReportChart, vbox, notesViewController);
    }

    @javafx.fxml.FXML
    public void onArrestReportBtnClick(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException {
        newArrest(reportChart, areaReportChart, vbox, notesViewController);
    }

    @javafx.fxml.FXML
    public void onCitationReportBtnClick(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException {
        newCitation(reportChart, areaReportChart, vbox, notesViewController);
    }

    @javafx.fxml.FXML
    public void onPatrolButtonClick(ActionEvent actionEvent) throws IOException {
        newPatrol(reportChart, areaReportChart, vbox, notesViewController);
    }

    @javafx.fxml.FXML
    public void onImpoundReportBtnClick(ActionEvent actionEvent) throws IOException {
        newImpound(reportChart, areaReportChart, vbox, notesViewController);
    }


    //</editor-fold>


    //<editor-fold desc="Misc.">

    @javafx.fxml.FXML
    public void onServerStatusLabelClick(Event event) throws IOException {

        if (clientStage != null && clientStage.isShowing()) {
            clientStage.toFront();
            return;
        }

        if (!ClientUtils.isConnected) {
            clientStage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("client-view.fxml"));
            Parent root = loader.load();
            BorderlessScene newScene = new BorderlessScene(clientStage, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
            AnchorPane topbar = ClientController.getTitleBar();
            newScene.setMoveControl(topbar);
            clientStage.setTitle("Client Interface");
            clientStage.setScene(newScene);
            clientStage.initStyle(StageStyle.UNDECORATED);
            clientStage.setResizable(false);
            clientStage.show();
            clientStage.centerOnScreen();
            clientStage.setAlwaysOnTop(true);

            clientStage.setOnHidden(event1 -> {
                clientStage = null;
            });

            clientController = loader.getController();
            ClientUtils.setStatusListener(this::updateConnectionStatus);
        }
    }

    @javafx.fxml.FXML
    public void updateInfoButtonClick(ActionEvent actionEvent) {
        if (getOfficerInfoAgency().getValue() == null || getOfficerInfoDivision().getValue() == null ||
                getOfficerInfoRank().getValue() == null || getOfficerInfoName().getText().isEmpty() ||
                getOfficerInfoNumber().getText().isEmpty()) {
            updatedNotification.setText("Fill Out Form.");
            updatedNotification.setStyle("-fx-text-fill: red;");
            updatedNotification.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline1.play();
        } else {
            ConfigWriter.configwrite("Agency", getOfficerInfoAgency().getValue().toString());
            ConfigWriter.configwrite("Division", getOfficerInfoDivision().getValue().toString());
            ConfigWriter.configwrite("Name", getOfficerInfoName().getText());
            ConfigWriter.configwrite("Rank", getOfficerInfoRank().getValue().toString());
            ConfigWriter.configwrite("Number", getOfficerInfoNumber().getText());
            generatedByTag.setText("Generated By:" + " " + getOfficerInfoName().getText());
            updatedNotification.setText("updated.");
            updatedNotification.setStyle("-fx-text-fill: green;");
            updatedNotification.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline.play();
        }
        showButtonAnimation(updateInfoBtn);
    }

    @javafx.fxml.FXML
    public void onVehSearchBtnClick(ActionEvent actionEvent) throws IOException {
        String searchedPlate = vehSearchField.getText();

        Map<String, String> vehData = grabVehicleData(getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldCars.data", searchedPlate);

        String licensePlate = vehData.getOrDefault("licensePlate", "Not available");
        if (!licensePlate.equals("Not available")) {
            vehRecordPane.setVisible(true);
            noRecordFoundLabelVeh.setVisible(false);
            String model = vehData.getOrDefault("model", "Not available");
            String isStolen = vehData.getOrDefault("isStolen", "Not available");
            String owner = vehData.getOrDefault("owner", "Not available");
            String registration = vehData.getOrDefault("registration", "Not available");
            String insurance = vehData.getOrDefault("insurance", "Not available");
            String colorValue = vehData.getOrDefault("color", "Not available");
            String[] rgb = colorValue.split("-");
            String color = "Not available";

            if (rgb.length == 3) {
                color = "rgb(" + rgb[0] + "," + rgb[1] + "," + rgb[2] + ")";
            }

            vehplatefield.setText(licensePlate);
            vehplatefield2.setText(licensePlate);
            vehmodelfield.setText(model);
            vehstolenfield.setText(isStolen);
            vehownerfield.setText(owner);
            vehregfield.setText(registration);
            vehinsfield.setText(insurance);
            if (!color.equals("Not available")) {
                vehnocolorlabel.setVisible(false);
                vehcolordisplay.setStyle("-fx-background-color: " + color + ";" + "-fx-border-color: grey;");
            } else {
                vehnocolorlabel.setVisible(true);
                vehcolordisplay.setStyle("-fx-background-color: #f2f2f2;" + "-fx-border-color: grey;");
            }
        } else {
            vehRecordPane.setVisible(false);
            noRecordFoundLabelVeh.setVisible(true);
        }
    }

    @javafx.fxml.FXML
    public void onPedSearchBtnClick(ActionEvent actionEvent) throws IOException {
        String searchedName = pedSearchField.getText();


        Map<String, String> pedData = grabPedData(getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldPeds.data", searchedName);
        String gender = pedData.getOrDefault("gender", "Not available");
        String birthday = pedData.getOrDefault("birthday", "Not available");
        String isWanted = pedData.getOrDefault("iswanted", "Not available");
        String licenseStatus = pedData.getOrDefault("licensestatus", "Not available");
        String name = pedData.getOrDefault("name", "Not available");
        String[] parts = name.split(" ");
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[1] : "";
        if (!name.equals("Not available")) {
            pedRecordPane.setVisible(true);
            noRecordFoundLabelPed.setVisible(false);

            pedrecordnamefield.setText(name);
            pedfnamefield.setText(firstName);
            pedlnamefield.setText(lastName);
            pedgenfield.setText(gender);
            peddobfield.setText(birthday);
            pedwantedfield.setText(isWanted);
            pedlicensefield.setText(licenseStatus);

            if (pedlicensefield.getText().equals("Expired")) {
                pedlicensefield.setText("EXPIRED");
                pedlicensefield.setStyle("-fx-text-fill: red !important;");
            } else if (pedlicensefield.getText().equals("Suspended")) {
                pedlicensefield.setText("SUSPENDED");
                pedlicensefield.setStyle("-fx-text-fill: red !important;");
            } else {
                pedlicensefield.setText("Valid");
                pedlicensefield.setStyle("-fx-text-fill: black !important;");
            }

            if (pedwantedfield.getText().equals("True")) {
                pedwantedfield.setText("WANTED");
                pedwantedfield.setStyle("-fx-text-fill: red !important;");
            } else {
                pedwantedfield.setText("False");
                pedwantedfield.setStyle("-fx-text-fill: black !important;");
            }
        } else {
            pedRecordPane.setVisible(false);
            noRecordFoundLabelPed.setVisible(true);
        }
    }


    //</editor-fold>


    //<editor-fold desc="Log Browser">


    private void loadLogs() {
        //ImpoundTable
        List<ImpoundLogEntry> impoundLogEntryList = ImpoundReportLogs.extractLogEntries(stringUtil.impoundLogURL);
        impoundLogUpdate(impoundLogEntryList);
        //TrafficCitationTable
        List<TrafficCitationLogEntry> citationLogEntryList = TrafficCitationReportLogs.extractLogEntries(stringUtil.trafficCitationLogURL);
        citationLogUpdate(citationLogEntryList);
        //PatrolTable
        List<PatrolLogEntry> patrolLogEntryList = PatrolReportLogs.extractLogEntries(stringUtil.patrolLogURL);
        patrolLogUpdate(patrolLogEntryList);
        //ArrestTable
        List<ArrestLogEntry> arrestLogEntryList = ArrestReportLogs.extractLogEntries(stringUtil.arrestLogURL);
        arrestLogUpdate(arrestLogEntryList);
        //SearchTable
        List<SearchLogEntry> searchLogEntryList = SearchReportLogs.extractLogEntries(stringUtil.searchLogURL);
        searchLogUpdate(searchLogEntryList);
        //IncidentTable
        List<IncidentLogEntry> incidentLogEntryList = IncidentReportLogs.extractLogEntries(stringUtil.incidentLogURL);
        incidentLogUpdate(incidentLogEntryList);
        //TrafficStopTable
        List<TrafficStopLogEntry> trafficLogEntryList = TrafficStopReportLogs.extractLogEntries(stringUtil.trafficstopLogURL);
        trafficStopLogUpdate(trafficLogEntryList);
        //CalloutTable
        List<CalloutLogEntry> calloutLogEntryList = CalloutReportLogs.extractLogEntries(stringUtil.calloutLogURL);
        calloutLogUpdate(calloutLogEntryList);
    }

    public void impoundLogUpdate(List<ImpoundLogEntry> logEntries) {
        // Clear existing data
        impoundTable.getItems().clear();
        // Add data to the table
        impoundTable.getItems().addAll(logEntries);
    }

    public void citationLogUpdate(List<TrafficCitationLogEntry> logEntries) {
        // Clear existing data
        citationTable.getItems().clear();
        citationTable.getItems().addAll(logEntries);
    }

    public void patrolLogUpdate(List<PatrolLogEntry> logEntries) {
        // Clear existing data
        patrolTable.getItems().clear();
        patrolTable.getItems().addAll(logEntries);
    }

    public void arrestLogUpdate(List<ArrestLogEntry> logEntries) {
        // Clear existing data
        arrestTable.getItems().clear();
        arrestTable.getItems().addAll(logEntries);
    }

    public void searchLogUpdate(List<SearchLogEntry> logEntries) {
        // Clear existing data
        searchTable.getItems().clear();
        searchTable.getItems().addAll(logEntries);
    }

    public void incidentLogUpdate(List<IncidentLogEntry> logEntries) {
        // Clear existing data
        incidentTable.getItems().clear();
        incidentTable.getItems().addAll(logEntries);
    }

    public void trafficStopLogUpdate(List<TrafficStopLogEntry> logEntries) {
        // Clear existing data
        trafficStopTable.getItems().clear();
        trafficStopTable.getItems().addAll(logEntries);
    }

    public void calloutLogUpdate(List<CalloutLogEntry> logEntries) {
        // Clear existing data
        calloutTable.getItems().clear();
        calloutTable.getItems().addAll(logEntries);
    }

    public void initializeImpoundColumns() {
        // Create columns for each property of ImpoundLogEntry
        TableColumn<ImpoundLogEntry, String> impoundNumberColumn = new TableColumn<>("Impound #");
        impoundNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundNumber"));

        TableColumn<ImpoundLogEntry, String> impoundDateColumn = new TableColumn<>("Impound Date");
        impoundDateColumn.setCellValueFactory(new PropertyValueFactory<>("impoundDate"));

        TableColumn<ImpoundLogEntry, String> impoundTimeColumn = new TableColumn<>("Impound Time");
        impoundTimeColumn.setCellValueFactory(new PropertyValueFactory<>("impoundTime"));

        TableColumn<ImpoundLogEntry, String> ownerNameColumn = new TableColumn<>("Owner Name");
        ownerNameColumn.setCellValueFactory(new PropertyValueFactory<>("ownerName"));

        TableColumn<ImpoundLogEntry, String> ownerAgeColumn = new TableColumn<>("Owner Age");
        ownerAgeColumn.setCellValueFactory(new PropertyValueFactory<>("ownerAge"));

        TableColumn<ImpoundLogEntry, String> ownerGenderColumn = new TableColumn<>("Owner Gender");
        ownerGenderColumn.setCellValueFactory(new PropertyValueFactory<>("ownerGender"));

        TableColumn<ImpoundLogEntry, String> ownerAddressColumn = new TableColumn<>("Owner Address");
        ownerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("ownerAddress"));

        TableColumn<ImpoundLogEntry, String> impoundPlateNumberColumn = new TableColumn<>("Veh. Plate #");
        impoundPlateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("impoundPlateNumber"));

        TableColumn<ImpoundLogEntry, String> impoundModelColumn = new TableColumn<>("Veh. Model");
        impoundModelColumn.setCellValueFactory(new PropertyValueFactory<>("impoundModel"));

        TableColumn<ImpoundLogEntry, String> impoundTypeColumn = new TableColumn<>("Veh. Type");
        impoundTypeColumn.setCellValueFactory(new PropertyValueFactory<>("impoundType"));

        TableColumn<ImpoundLogEntry, String> impoundColorColumn = new TableColumn<>("Veh. Color");
        impoundColorColumn.setCellValueFactory(new PropertyValueFactory<>("impoundColor"));

        TableColumn<ImpoundLogEntry, String> impoundCommentsColumn = new TableColumn<>("Comments");
        impoundCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("impoundComments"));

        TableColumn<ImpoundLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<ImpoundLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<ImpoundLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<ImpoundLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<ImpoundLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        // Add columns to the table
        // Create a list to hold all the TableColumn instances for impoundTable
        ObservableList<TableColumn<ImpoundLogEntry, ?>> impoundColumns = FXCollections.observableArrayList(
                impoundNumberColumn,
                impoundDateColumn,
                impoundTimeColumn,
                ownerNameColumn,
                ownerAgeColumn,
                ownerGenderColumn,
                ownerAddressColumn,
                impoundPlateNumberColumn,
                impoundModelColumn,
                impoundTypeColumn,
                impoundColorColumn,
                impoundCommentsColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerDivisionColumn,
                officerAgencyColumn
        );

// Add all columns to impoundTable
        impoundTable.getColumns().addAll(impoundColumns);

// Set minimum width for each column
        for (TableColumn<ImpoundLogEntry, ?> column : impoundColumns) {
            column.setMinWidth(minColumnWidth);
        }
        setSmallColumnWidth(impoundNumberColumn);
        setSmallColumnWidth(impoundDateColumn);
        setSmallColumnWidth(impoundTimeColumn);
        setSmallColumnWidth(ownerAgeColumn);
        setSmallColumnWidth(ownerGenderColumn);
        setSmallColumnWidth(impoundPlateNumberColumn);
        setSmallColumnWidth(impoundModelColumn);
        setSmallColumnWidth(impoundColorColumn);
        setSmallColumnWidth(officerNumberColumn);
    }

    public void initializePatrolColumns() {

        // Create columns for each property of PatrolLogEntry
        TableColumn<PatrolLogEntry, String> patrolNumberColumn = new TableColumn<>("Patrol #");
        patrolNumberColumn.setCellValueFactory(new PropertyValueFactory<>("patrolNumber"));

        TableColumn<PatrolLogEntry, String> patrolDateColumn = new TableColumn<>("Date");
        patrolDateColumn.setCellValueFactory(new PropertyValueFactory<>("patrolDate"));

        TableColumn<PatrolLogEntry, String> patrolLengthColumn = new TableColumn<>("Length");
        patrolLengthColumn.setCellValueFactory(new PropertyValueFactory<>("patrolLength"));

        TableColumn<PatrolLogEntry, String> patrolStartTimeColumn = new TableColumn<>("Start Time");
        patrolStartTimeColumn.setCellValueFactory(new PropertyValueFactory<>("patrolStartTime"));

        TableColumn<PatrolLogEntry, String> patrolStopTimeColumn = new TableColumn<>("Stop Time");
        patrolStopTimeColumn.setCellValueFactory(new PropertyValueFactory<>("patrolStopTime"));

        TableColumn<PatrolLogEntry, String> officerRankColumn = new TableColumn<>("Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<PatrolLogEntry, String> officerNameColumn = new TableColumn<>("Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<PatrolLogEntry, String> officerNumberColumn = new TableColumn<>("Number");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<PatrolLogEntry, String> officerDivisionColumn = new TableColumn<>("Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<PatrolLogEntry, String> officerAgencyColumn = new TableColumn<>("Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        TableColumn<PatrolLogEntry, String> officerVehicleColumn = new TableColumn<>("Vehicle");
        officerVehicleColumn.setCellValueFactory(new PropertyValueFactory<>("officerVehicle"));

        TableColumn<PatrolLogEntry, String> patrolCommentsColumn = new TableColumn<>("Comments");
        patrolCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("patrolComments"));

        // Create a list to hold all the TableColumn instances for patrolTable
        ObservableList<TableColumn<PatrolLogEntry, ?>> patrolColumns = FXCollections.observableArrayList(
                patrolNumberColumn,
                patrolDateColumn,
                patrolLengthColumn,
                patrolStartTimeColumn,
                patrolStopTimeColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerDivisionColumn,
                officerAgencyColumn,
                officerVehicleColumn,
                patrolCommentsColumn
        );

// Add all columns to patrolTable
        patrolTable.getColumns().addAll(patrolColumns);

// Set minimum width for each column
        for (TableColumn<PatrolLogEntry, ?> column : patrolColumns) {
            column.setMinWidth(minColumnWidth);
        }
        setSmallColumnWidth(patrolNumberColumn);
        setSmallColumnWidth(patrolDateColumn);
        setSmallColumnWidth(patrolLengthColumn);
        setSmallColumnWidth(patrolStartTimeColumn);
        setSmallColumnWidth(patrolStopTimeColumn);
        setSmallColumnWidth(officerNumberColumn);
    }

    public void initializeCitationColumns() {

        // Create columns for each property of TrafficCitationLogEntry
        TableColumn<TrafficCitationLogEntry, String> citationNumberColumn = new TableColumn<>("Citation #");
        citationNumberColumn.setCellValueFactory(new PropertyValueFactory<>("citationNumber"));

        TableColumn<TrafficCitationLogEntry, String> citationDateColumn = new TableColumn<>("Citation Date");
        citationDateColumn.setCellValueFactory(new PropertyValueFactory<>("citationDate"));

        TableColumn<TrafficCitationLogEntry, String> citationTimeColumn = new TableColumn<>("Citation Time");
        citationTimeColumn.setCellValueFactory(new PropertyValueFactory<>("citationTime"));

        TableColumn<TrafficCitationLogEntry, String> citationChargesColumn = new TableColumn<>("Charges");
        citationChargesColumn.setCellValueFactory(new PropertyValueFactory<>("citationCharges"));

        TableColumn<TrafficCitationLogEntry, String> citationCountyColumn = new TableColumn<>("County");
        citationCountyColumn.setCellValueFactory(new PropertyValueFactory<>("citationCounty"));

        TableColumn<TrafficCitationLogEntry, String> citationAreaColumn = new TableColumn<>("Area");
        citationAreaColumn.setCellValueFactory(new PropertyValueFactory<>("citationArea"));

        TableColumn<TrafficCitationLogEntry, String> citationStreetColumn = new TableColumn<>("Street");
        citationStreetColumn.setCellValueFactory(new PropertyValueFactory<>("citationStreet"));

        TableColumn<TrafficCitationLogEntry, String> offenderNameColumn = new TableColumn<>("Sus. Name");
        offenderNameColumn.setCellValueFactory(new PropertyValueFactory<>("offenderName"));

        TableColumn<TrafficCitationLogEntry, String> offenderGenderColumn = new TableColumn<>("Sus. Gender");
        offenderGenderColumn.setCellValueFactory(new PropertyValueFactory<>("offenderGender"));

        TableColumn<TrafficCitationLogEntry, String> offenderAgeColumn = new TableColumn<>("Sus. Age");
        offenderAgeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderAge"));

        TableColumn<TrafficCitationLogEntry, String> offenderHomeAddressColumn = new TableColumn<>("Sus. Address");
        offenderHomeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("offenderHomeAddress"));

        TableColumn<TrafficCitationLogEntry, String> offenderDescriptionColumn = new TableColumn<>("Sus. Description");
        offenderDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("offenderDescription"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehicleModelColumn = new TableColumn<>("Sus. Veh. Model");
        offenderVehicleModelColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleModel"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehicleColorColumn = new TableColumn<>("Sus. Veh. Color");
        offenderVehicleColorColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleColor"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehicleTypeColumn = new TableColumn<>("Sus. Veh. Type");
        offenderVehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleType"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehiclePlateColumn = new TableColumn<>("Sus. Veh. Plate");
        offenderVehiclePlateColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehiclePlate"));

        TableColumn<TrafficCitationLogEntry, String> offenderVehicleOtherColumn = new TableColumn<>("Sus. Veh. Other");
        offenderVehicleOtherColumn.setCellValueFactory(new PropertyValueFactory<>("offenderVehicleOther"));

        TableColumn<TrafficCitationLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<TrafficCitationLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<TrafficCitationLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<TrafficCitationLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<TrafficCitationLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        TableColumn<TrafficCitationLogEntry, String> citationCommentsColumn = new TableColumn<>("Comments");
        citationCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("citationComments"));

        // Create a list to hold all the TableColumn instances for citationTable
        ObservableList<TableColumn<TrafficCitationLogEntry, ?>> citationColumns = FXCollections.observableArrayList(
                citationNumberColumn,
                citationDateColumn,
                citationTimeColumn,
                citationChargesColumn,
                citationCountyColumn,
                citationAreaColumn,
                citationStreetColumn,
                offenderNameColumn,
                offenderGenderColumn,
                offenderAgeColumn,
                offenderHomeAddressColumn,
                offenderDescriptionColumn,
                offenderVehicleModelColumn,
                offenderVehicleColorColumn,
                offenderVehicleTypeColumn,
                offenderVehiclePlateColumn,
                offenderVehicleOtherColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerDivisionColumn,
                officerAgencyColumn,
                citationCommentsColumn
        );

// Add all columns to citationTable
        citationTable.getColumns().addAll(citationColumns);

// Set minimum width for each column
        for (TableColumn<TrafficCitationLogEntry, ?> column : citationColumns) {
            column.setMinWidth(minColumnWidth);
        }
        setSmallColumnWidth(citationNumberColumn);
        setSmallColumnWidth(citationDateColumn);
        setSmallColumnWidth(citationTimeColumn);
        setSmallColumnWidth(offenderGenderColumn);
        setSmallColumnWidth(offenderAgeColumn);
        setSmallColumnWidth(offenderVehicleColorColumn);
        setSmallColumnWidth(offenderVehiclePlateColumn);
        setSmallColumnWidth(officerNumberColumn);
    }

    public void initializeArrestColumns() {

        // Create columns for each property of ArrestLogEntry
        TableColumn<ArrestLogEntry, String> arrestNumberColumn = new TableColumn<>("Arrest #");
        arrestNumberColumn.setCellValueFactory(new PropertyValueFactory<>("arrestNumber"));

        TableColumn<ArrestLogEntry, String> arrestDateColumn = new TableColumn<>("Arrest Date");
        arrestDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDate"));

        TableColumn<ArrestLogEntry, String> arrestTimeColumn = new TableColumn<>("Arrest Time");
        arrestTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrestTime"));

        TableColumn<ArrestLogEntry, String> arrestChargesColumn = new TableColumn<>("Charges");
        arrestChargesColumn.setCellValueFactory(new PropertyValueFactory<>("arrestCharges"));

        TableColumn<ArrestLogEntry, String> arrestCountyColumn = new TableColumn<>("County");
        arrestCountyColumn.setCellValueFactory(new PropertyValueFactory<>("arrestCounty"));

        TableColumn<ArrestLogEntry, String> arrestAreaColumn = new TableColumn<>("Area");
        arrestAreaColumn.setCellValueFactory(new PropertyValueFactory<>("arrestArea"));

        TableColumn<ArrestLogEntry, String> arrestStreetColumn = new TableColumn<>("Street");
        arrestStreetColumn.setCellValueFactory(new PropertyValueFactory<>("arrestStreet"));

        TableColumn<ArrestLogEntry, String> arresteeNameColumn = new TableColumn<>("Sus. Name");
        arresteeNameColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeName"));

        TableColumn<ArrestLogEntry, String> arresteeAgeColumn = new TableColumn<>("Sus. Age/DOB");
        arresteeAgeColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeAge"));

        TableColumn<ArrestLogEntry, String> arresteeGenderColumn = new TableColumn<>("Sus. Gender");
        arresteeGenderColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeGender"));

        TableColumn<ArrestLogEntry, String> arresteeDescriptionColumn = new TableColumn<>("Sus. Description");
        arresteeDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeDescription"));

        TableColumn<ArrestLogEntry, String> ambulanceYesNoColumn = new TableColumn<>("Ambulance (Y/N)");
        ambulanceYesNoColumn.setCellValueFactory(new PropertyValueFactory<>("ambulanceYesNo"));

        TableColumn<ArrestLogEntry, String> taserYesNoColumn = new TableColumn<>("Taser (Y/N)");
        taserYesNoColumn.setCellValueFactory(new PropertyValueFactory<>("TaserYesNo"));

        TableColumn<ArrestLogEntry, String> arresteeMedicalInformationColumn = new TableColumn<>("Med. Info.");
        arresteeMedicalInformationColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeMedicalInformation"));

        TableColumn<ArrestLogEntry, String> arresteeHomeAddressColumn = new TableColumn<>("Sus. Address");
        arresteeHomeAddressColumn.setCellValueFactory(new PropertyValueFactory<>("arresteeHomeAddress"));

        TableColumn<ArrestLogEntry, String> arrestDetailsColumn = new TableColumn<>("Details");
        arrestDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("arrestDetails"));

        TableColumn<ArrestLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<ArrestLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<ArrestLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<ArrestLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<ArrestLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        // Create a list to hold all the TableColumn instances for arrestTable
        ObservableList<TableColumn<ArrestLogEntry, ?>> arrestColumns = FXCollections.observableArrayList(
                arrestNumberColumn,
                arrestDateColumn,
                arrestTimeColumn,
                arrestChargesColumn,
                arrestCountyColumn,
                arrestAreaColumn,
                arrestStreetColumn,
                arresteeNameColumn,
                arresteeAgeColumn,
                arresteeGenderColumn,
                arresteeDescriptionColumn,
                ambulanceYesNoColumn,
                taserYesNoColumn,
                arresteeMedicalInformationColumn,
                arresteeHomeAddressColumn,
                arrestDetailsColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerDivisionColumn,
                officerAgencyColumn
        );

// Add all columns to arrestTable
        arrestTable.getColumns().addAll(arrestColumns);

// Set minimum width for each column
        for (TableColumn<ArrestLogEntry, ?> column : arrestColumns) {
            column.setMinWidth(minColumnWidth);
        }
        setSmallColumnWidth(arrestNumberColumn);
        setSmallColumnWidth(arrestDateColumn);
        setSmallColumnWidth(arrestTimeColumn);
        setSmallColumnWidth(arresteeAgeColumn);
        setSmallColumnWidth(arresteeGenderColumn);
        setSmallColumnWidth(ambulanceYesNoColumn);
        setSmallColumnWidth(taserYesNoColumn);
        setSmallColumnWidth(officerNumberColumn);
    }

    public void initializeIncidentColumns() {
        // Create columns for each property of IncidentLogEntry
        TableColumn<IncidentLogEntry, String> incidentNumberColumn = new TableColumn<>("Incident #");
        incidentNumberColumn.setCellValueFactory(new PropertyValueFactory<>("incidentNumber"));

        TableColumn<IncidentLogEntry, String> incidentDateColumn = new TableColumn<>("Date");
        incidentDateColumn.setCellValueFactory(new PropertyValueFactory<>("incidentDate"));

        TableColumn<IncidentLogEntry, String> incidentTimeColumn = new TableColumn<>("Time");
        incidentTimeColumn.setCellValueFactory(new PropertyValueFactory<>("incidentTime"));

        TableColumn<IncidentLogEntry, String> incidentStatementColumn = new TableColumn<>("Statement");
        incidentStatementColumn.setCellValueFactory(new PropertyValueFactory<>("incidentStatement"));

        TableColumn<IncidentLogEntry, String> incidentWitnessesColumn = new TableColumn<>("Suspects");
        incidentWitnessesColumn.setCellValueFactory(new PropertyValueFactory<>("incidentWitnesses"));

        TableColumn<IncidentLogEntry, String> incidentVictimsColumn = new TableColumn<>("Victims/Witnesses");
        incidentVictimsColumn.setCellValueFactory(new PropertyValueFactory<>("incidentVictims"));

        TableColumn<IncidentLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<IncidentLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<IncidentLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<IncidentLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        TableColumn<IncidentLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<IncidentLogEntry, String> incidentStreetColumn = new TableColumn<>("Street");
        incidentStreetColumn.setCellValueFactory(new PropertyValueFactory<>("incidentStreet"));

        TableColumn<IncidentLogEntry, String> incidentAreaColumn = new TableColumn<>("Area");
        incidentAreaColumn.setCellValueFactory(new PropertyValueFactory<>("incidentArea"));

        TableColumn<IncidentLogEntry, String> incidentCountyColumn = new TableColumn<>("County");
        incidentCountyColumn.setCellValueFactory(new PropertyValueFactory<>("incidentCounty"));

        TableColumn<IncidentLogEntry, String> incidentActionsTakenColumn = new TableColumn<>("Details");
        incidentActionsTakenColumn.setCellValueFactory(new PropertyValueFactory<>("incidentActionsTaken"));

        TableColumn<IncidentLogEntry, String> incidentCommentsColumn = new TableColumn<>("Comments");
        incidentCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("incidentComments"));

        // Create a list to hold all the TableColumn instances for incidentTable
        ObservableList<TableColumn<IncidentLogEntry, ?>> incidentColumns = FXCollections.observableArrayList(
                incidentNumberColumn,
                incidentDateColumn,
                incidentTimeColumn,
                incidentStatementColumn,
                incidentWitnessesColumn,
                incidentVictimsColumn,
                officerNameColumn,
                officerRankColumn,
                officerNumberColumn,
                officerAgencyColumn,
                officerDivisionColumn,
                incidentStreetColumn,
                incidentAreaColumn,
                incidentCountyColumn,
                incidentActionsTakenColumn,
                incidentCommentsColumn
        );

// Add all columns to incidentTable
        incidentTable.getColumns().addAll(incidentColumns);
        for (TableColumn<IncidentLogEntry, ?> column : incidentColumns) {
            column.setMinWidth(minColumnWidth);
        }
        setSmallColumnWidth(incidentNumberColumn);
        setSmallColumnWidth(incidentDateColumn);
        setSmallColumnWidth(incidentTimeColumn);
        setSmallColumnWidth(officerNumberColumn);
    }

    public void initializeSearchColumns() {

        // Create columns for each property of SearchLogEntry
        TableColumn<SearchLogEntry, String> searchNumberColumn = new TableColumn<>("Search #");
        searchNumberColumn.setCellValueFactory(new PropertyValueFactory<>("SearchNumber"));

        TableColumn<SearchLogEntry, String> searchDateColumn = new TableColumn<>("Date");
        searchDateColumn.setCellValueFactory(new PropertyValueFactory<>("searchDate"));

        TableColumn<SearchLogEntry, String> searchTimeColumn = new TableColumn<>("Time");
        searchTimeColumn.setCellValueFactory(new PropertyValueFactory<>("searchTime"));

        TableColumn<SearchLogEntry, String> searchSeizedItemsColumn = new TableColumn<>("Details/Field Sob.");
        searchSeizedItemsColumn.setCellValueFactory(new PropertyValueFactory<>("searchSeizedItems"));

        TableColumn<SearchLogEntry, String> searchGroundsColumn = new TableColumn<>("Grounds");
        searchGroundsColumn.setCellValueFactory(new PropertyValueFactory<>("searchGrounds"));

        TableColumn<SearchLogEntry, String> searchTypeColumn = new TableColumn<>("Type");
        searchTypeColumn.setCellValueFactory(new PropertyValueFactory<>("searchType"));

        TableColumn<SearchLogEntry, String> searchMethodColumn = new TableColumn<>("Method");
        searchMethodColumn.setCellValueFactory(new PropertyValueFactory<>("searchMethod"));

        TableColumn<SearchLogEntry, String> searchWitnessesColumn = new TableColumn<>("Witnesses");
        searchWitnessesColumn.setCellValueFactory(new PropertyValueFactory<>("searchWitnesses"));

        TableColumn<SearchLogEntry, String> officerRankColumn = new TableColumn<>("Officer Rank");
        officerRankColumn.setCellValueFactory(new PropertyValueFactory<>("officerRank"));

        TableColumn<SearchLogEntry, String> officerNameColumn = new TableColumn<>("Officer Name");
        officerNameColumn.setCellValueFactory(new PropertyValueFactory<>("officerName"));

        TableColumn<SearchLogEntry, String> officerNumberColumn = new TableColumn<>("Officer #");
        officerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("officerNumber"));

        TableColumn<SearchLogEntry, String> officerAgencyColumn = new TableColumn<>("Officer Agency");
        officerAgencyColumn.setCellValueFactory(new PropertyValueFactory<>("officerAgency"));

        TableColumn<SearchLogEntry, String> officerDivisionColumn = new TableColumn<>("Officer Division");
        officerDivisionColumn.setCellValueFactory(new PropertyValueFactory<>("officerDivision"));

        TableColumn<SearchLogEntry, String> searchStreetColumn = new TableColumn<>("Street");
        searchStreetColumn.setCellValueFactory(new PropertyValueFactory<>("searchStreet"));

        TableColumn<SearchLogEntry, String> searchAreaColumn = new TableColumn<>("Area");
        searchAreaColumn.setCellValueFactory(new PropertyValueFactory<>("searchArea"));

        TableColumn<SearchLogEntry, String> searchCountyColumn = new TableColumn<>("County");
        searchCountyColumn.setCellValueFactory(new PropertyValueFactory<>("searchCounty"));

        TableColumn<SearchLogEntry, String> searchCommentsColumn = new TableColumn<>("Comments");
        searchCommentsColumn.setCellValueFactory(new PropertyValueFactory<>("searchComments"));

        TableColumn<SearchLogEntry, String> searchedPersonsColumn = new TableColumn<>("Sus. Searched");
        searchedPersonsColumn.setCellValueFactory(new PropertyValueFactory<>("searchedPersons"));

        TableColumn<SearchLogEntry, String> testsConductedColumn = new TableColumn<>("Test(s) Cond.");
        testsConductedColumn.setCellValueFactory(new PropertyValueFactory<>("testsConducted"));

        TableColumn<SearchLogEntry, String> resultsColumn = new TableColumn<>("Result(s)");
        resultsColumn.setCellValueFactory(new PropertyValueFactory<>("testResults"));

        TableColumn<SearchLogEntry, String> BACMeasurementColumn = new TableColumn<>("BAC");
        BACMeasurementColumn.setCellValueFactory(new PropertyValueFactory<>("breathalyzerBACMeasure"));

        // Create a list to hold all the TableColumn instances for searchTable
        ObservableList<TableColumn<SearchLogEntry, ?>> searchColumns = FXCollections.observableArrayList(
                searchNumberColumn,
                searchDateColumn,
                searchTimeColumn,
                searchSeizedItemsColumn,
                searchGroundsColumn,
                searchTypeColumn,
                searchMethodColumn,
                searchWitnessesColumn,
                officerRankColumn,
                officerNameColumn,
                officerNumberColumn,
                officerAgencyColumn,
                officerDivisionColumn,
                searchStreetColumn,
                searchAreaColumn,
                searchCountyColumn,
                searchCommentsColumn,
                searchedPersonsColumn,
                testsConductedColumn,
                resultsColumn,
                BACMeasurementColumn
        );

        searchTable.getColumns().addAll(searchColumns);

        for (TableColumn<SearchLogEntry, ?> column : searchColumns) {
            column.setMinWidth(minColumnWidth);
        }
        setSmallColumnWidth(searchNumberColumn);
        setSmallColumnWidth(searchDateColumn);
        setSmallColumnWidth(searchTimeColumn);
        setSmallColumnWidth(officerNumberColumn);
        setSmallColumnWidth(testsConductedColumn);
        setSmallColumnWidth(resultsColumn);
        setSmallColumnWidth(BACMeasurementColumn);
    }

    public void initializeTrafficStopColumns() {// Create columns for each property of TrafficStopLogEntry
        TableColumn<TrafficStopLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<TrafficStopLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<TrafficStopLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<TrafficStopLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<TrafficStopLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<TrafficStopLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<TrafficStopLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<TrafficStopLogEntry, String> stopNumberColumn = new TableColumn<>("Stop #");
        stopNumberColumn.setCellValueFactory(new PropertyValueFactory<>("StopNumber"));

        TableColumn<TrafficStopLogEntry, String> commentsTextAreaColumn = new TableColumn<>("Comments");
        commentsTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("CommentsTextArea"));

        TableColumn<TrafficStopLogEntry, String> streetColumn = new TableColumn<>("Street");
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("Street"));

        TableColumn<TrafficStopLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<TrafficStopLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        TableColumn<TrafficStopLogEntry, String> plateNumberColumn = new TableColumn<>("Plate #");
        plateNumberColumn.setCellValueFactory(new PropertyValueFactory<>("PlateNumber"));

        TableColumn<TrafficStopLogEntry, String> colorColumn = new TableColumn<>("Color");
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("Color"));

        TableColumn<TrafficStopLogEntry, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));

        TableColumn<TrafficStopLogEntry, String> modelColumn = new TableColumn<>("Model");
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseModel"));

        TableColumn<TrafficStopLogEntry, String> otherInfoColumn = new TableColumn<>("Other Info.");
        otherInfoColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseOtherInfo"));

        TableColumn<TrafficStopLogEntry, String> operatorNameColumn = new TableColumn<>("Operator Name");
        operatorNameColumn.setCellValueFactory(new PropertyValueFactory<>("operatorName"));

        TableColumn<TrafficStopLogEntry, String> operatorAgeColumn = new TableColumn<>("Operator Age");
        operatorAgeColumn.setCellValueFactory(new PropertyValueFactory<>("operatorAge"));

        TableColumn<TrafficStopLogEntry, String> operatorGenderColumn = new TableColumn<>("Operator Gender");
        operatorGenderColumn.setCellValueFactory(new PropertyValueFactory<>("operatorGender"));

        TableColumn<TrafficStopLogEntry, String> operatorDescriptionColumn = new TableColumn<>("Operator Description");
        operatorDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("operatorDescription"));

        TableColumn<TrafficStopLogEntry, String> operatorAddressColumn = new TableColumn<>("Operator Address");
        operatorAddressColumn.setCellValueFactory(new PropertyValueFactory<>("operatorAddress"));

        // Create a list to hold all the TableColumn instances for trafficStopTable
        ObservableList<TableColumn<TrafficStopLogEntry, ?>> trafficStopColumns = FXCollections.observableArrayList(
                stopNumberColumn,
                dateColumn,
                timeColumn,
                modelColumn,
                otherInfoColumn,
                operatorNameColumn,
                operatorAgeColumn,
                operatorAddressColumn,
                operatorDescriptionColumn,
                operatorGenderColumn,
                nameColumn,
                rankColumn,
                numberColumn,
                divisionColumn,
                agencyColumn,
                commentsTextAreaColumn,
                streetColumn,
                countyColumn,
                areaColumn,
                plateNumberColumn,
                colorColumn,
                typeColumn
        );

        // Add all columns to trafficStopTable
        trafficStopTable.getColumns().addAll(trafficStopColumns);
        for (TableColumn<TrafficStopLogEntry, ?> column : trafficStopColumns) {
            column.setMinWidth(minColumnWidth);
        }
        setSmallColumnWidth(stopNumberColumn);
        setSmallColumnWidth(dateColumn);
        setSmallColumnWidth(timeColumn);
        setSmallColumnWidth(operatorAgeColumn);
        setSmallColumnWidth(operatorGenderColumn);
        setSmallColumnWidth(numberColumn);
        setSmallColumnWidth(plateNumberColumn);
        setSmallColumnWidth(colorColumn);
        setSmallColumnWidth(numberColumn);

    }

    public void initializeCalloutColumns() {
        // Create columns for each property of CalloutLogEntry
        TableColumn<CalloutLogEntry, String> calloutNumberColumn = new TableColumn<>("Callout #");
        calloutNumberColumn.setCellValueFactory(new PropertyValueFactory<>("CalloutNumber"));

        TableColumn<CalloutLogEntry, String> notesTextAreaColumn = new TableColumn<>("Notes");
        notesTextAreaColumn.setCellValueFactory(new PropertyValueFactory<>("NotesTextArea"));

        TableColumn<CalloutLogEntry, String> responseGradeColumn = new TableColumn<>("Grade");
        responseGradeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponseGrade"));

        TableColumn<CalloutLogEntry, String> responseTypeColumn = new TableColumn<>("Type");
        responseTypeColumn.setCellValueFactory(new PropertyValueFactory<>("ResponeType"));

        TableColumn<CalloutLogEntry, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("Time"));

        TableColumn<CalloutLogEntry, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));

        TableColumn<CalloutLogEntry, String> divisionColumn = new TableColumn<>("Division");
        divisionColumn.setCellValueFactory(new PropertyValueFactory<>("Division"));

        TableColumn<CalloutLogEntry, String> agencyColumn = new TableColumn<>("Agency");
        agencyColumn.setCellValueFactory(new PropertyValueFactory<>("Agency"));

        TableColumn<CalloutLogEntry, String> numberColumn = new TableColumn<>("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("Number"));

        TableColumn<CalloutLogEntry, String> rankColumn = new TableColumn<>("Rank");
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("Rank"));

        TableColumn<CalloutLogEntry, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));

        TableColumn<CalloutLogEntry, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("Address"));

        TableColumn<CalloutLogEntry, String> countyColumn = new TableColumn<>("County");
        countyColumn.setCellValueFactory(new PropertyValueFactory<>("County"));

        TableColumn<CalloutLogEntry, String> areaColumn = new TableColumn<>("Area");
        areaColumn.setCellValueFactory(new PropertyValueFactory<>("Area"));

        // Create a list to hold all the TableColumn instances
        ObservableList<TableColumn<CalloutLogEntry, ?>> columns = FXCollections.observableArrayList(
                calloutNumberColumn,
                dateColumn,
                timeColumn,
                notesTextAreaColumn,
                responseGradeColumn,
                responseTypeColumn,
                divisionColumn,
                agencyColumn,
                numberColumn,
                rankColumn,
                nameColumn,
                addressColumn,
                countyColumn,
                areaColumn
        );
        calloutTable.getColumns().addAll(columns);
        for (TableColumn<CalloutLogEntry, ?> column : columns) {
            column.setMinWidth(minColumnWidth);
        }
        setSmallColumnWidth(calloutNumberColumn);
        setSmallColumnWidth(dateColumn);
        setSmallColumnWidth(timeColumn);
        setSmallColumnWidth(numberColumn);
        setSmallColumnWidth(responseGradeColumn);
    }

    @javafx.fxml.FXML
    public void onManagerToggle(ActionEvent actionEvent) {
        if (!showManagerToggle.isSelected()) {
            // Define the height values
            double fromHeight = lowerPane.getPrefHeight();
            double toHeight = 0;

            // Create a timeline for the animation
            Timeline timeline = new Timeline();

            // Define keyframes for animation
            KeyValue keyValuePrefHeight = new KeyValue(lowerPane.prefHeightProperty(), toHeight);
            KeyValue keyValueMaxHeight = new KeyValue(lowerPane.maxHeightProperty(), toHeight);
            KeyValue keyValueMinHeight = new KeyValue(lowerPane.minHeightProperty(), toHeight);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight, keyValueMinHeight);

            // Add keyframes to the timeline
            timeline.getKeyFrames().add(keyFrame);

            // Play the animation
            timeline.play();
            lowerPane.setVisible(false);
        } else {
            // Define the height values
            double fromHeight = lowerPane.getPrefHeight();
            double toHeight = 356;

            // Create a timeline for the animation
            Timeline timeline = new Timeline();

            // Define keyframes for animation
            KeyValue keyValuePrefHeight = new KeyValue(lowerPane.prefHeightProperty(), toHeight);
            KeyValue keyValueMaxHeight = new KeyValue(lowerPane.maxHeightProperty(), toHeight);
            KeyValue keyValueMinHeight = new KeyValue(lowerPane.minHeightProperty(), toHeight);
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight, keyValueMinHeight);

            // Add keyframes to the timeline
            timeline.getKeyFrames().add(keyFrame);

            // Play the animation
            timeline.play();
            lowerPane.setVisible(true);
        }
    }
    // Callout Section

    @javafx.fxml.FXML
    public void onCalloutRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            calloutEntry = (CalloutLogEntry) calloutTable.getSelectionModel().getSelectedItem();
            if (calloutEntry != null) {
                calnum.setText(calloutEntry.getCalloutNumber());
                caladdress.setText(calloutEntry.getAddress());
                calnotes.setText(calloutEntry.getNotesTextArea());
                calcounty.setText(calloutEntry.getCounty());
                calgrade.setText(calloutEntry.getResponseGrade());
                calarea.setText(calloutEntry.getArea());
                caltype.setText(calloutEntry.getResponeType());
                calloutTable.getSelectionModel().clearSelection();
            } else {
                calnum.setText("");
                caladdress.setText("");
                calnotes.setText("");
                calcounty.setText("");
                calgrade.setText("");
                calarea.setText("");
                caltype.setText("");
            }
        }
    }

    @javafx.fxml.FXML
    public void onCalUpdateValues(ActionEvent actionEvent) {
        if (calloutEntry != null) {
            calupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                calupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            // Update the selected log entry with values from text fields
            calloutEntry.CalloutNumber = calnum.getText();
            calloutEntry.Address = caladdress.getText();
            calloutEntry.NotesTextArea = calnotes.getText();
            calloutEntry.County = calcounty.getText();
            calloutEntry.ResponseGrade = calgrade.getText();
            calloutEntry.Area = calarea.getText();
            calloutEntry.ResponeType = caltype.getText();

            // Load existing logs from XML
            List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (CalloutLogEntry entry : logs) {
                if (entry.getDate().equals(calloutEntry.getDate()) &&
                        entry.getTime().equals(calloutEntry.getTime())) {
                    entry.CalloutNumber = calnum.getText();
                    entry.Address = caladdress.getText();
                    entry.NotesTextArea = calnotes.getText();
                    entry.County = calcounty.getText();
                    entry.ResponseGrade = calgrade.getText();
                    entry.Area = calarea.getText();
                    entry.ResponeType = caltype.getText();
                    break;
                }
            }

            // Save the updated logs back to XML
            CalloutReportLogs.saveLogsToXML(logs);

            // Optionally, you might want to update the TableView here
            calloutTable.refresh();

        }
    }
    // Patrol Section

    @javafx.fxml.FXML
    public void onPatUpdateValues(ActionEvent actionEvent) {
        if (patrolEntry != null) {
            patupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                patupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            // Update the selected log entry with values from text fields
            patrolEntry.patrolNumber = patnum.getText();
            patrolEntry.patrolComments = patcomments.getText();
            patrolEntry.patrolLength = patlength.getText();
            patrolEntry.patrolStartTime = patstarttime.getText();
            patrolEntry.patrolStopTime = patstoptime.getText();
            patrolEntry.officerVehicle = patvehicle.getText();

            // Load existing logs from XML
            List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (PatrolLogEntry entry : logs) {
                if (entry.getPatrolDate().equals(patrolEntry.getPatrolDate())) {
                    entry.patrolNumber = patnum.getText();
                    entry.patrolComments = patcomments.getText();
                    entry.patrolLength = patlength.getText();
                    entry.patrolStartTime = patstarttime.getText();
                    entry.patrolStopTime = patstoptime.getText();
                    entry.officerVehicle = patvehicle.getText();

                    break;
                }
            }

            // Save the updated logs back to XML
            PatrolReportLogs.saveLogsToXML(logs);

            // Optionally, you might want to update the TableView here
            patrolTable.refresh();

        }
    }

    @javafx.fxml.FXML
    public void onPatrolRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            patrolEntry = (PatrolLogEntry) patrolTable.getSelectionModel().getSelectedItem();
            if (patrolEntry != null) {
                patnum.setText(patrolEntry.getPatrolNumber());
                patcomments.setText(patrolEntry.getPatrolComments());
                patlength.setText(patrolEntry.getPatrolLength());
                patstarttime.setText(patrolEntry.getPatrolStartTime());
                patstoptime.setText(patrolEntry.getPatrolStopTime());
                patvehicle.setText(patrolEntry.getOfficerVehicle());
                patrolTable.getSelectionModel().clearSelection();
            } else {
                patnum.setText("");
                patcomments.setText("");
                patlength.setText("");
                patstarttime.setText("");
                patstoptime.setText("");
                patvehicle.setText("");
            }
        }
    }
    // Traffic Stop Section

    @javafx.fxml.FXML
    public void onTrafUpdateValues(ActionEvent actionEvent) {
        if (trafficStopEntry != null) {
            trafupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                trafupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            trafficStopEntry.PlateNumber = trafplatenum.getText();
            trafficStopEntry.Color = trafcolor.getText();
            trafficStopEntry.Type = traftype.getText();
            trafficStopEntry.StopNumber = trafnum.getText();
            trafficStopEntry.ResponseModel = trafmodel.getText();
            trafficStopEntry.ResponseOtherInfo = trafotherinfo.getText();
            trafficStopEntry.CommentsTextArea = trafcomments.getText();
            trafficStopEntry.County = trafcounty.getText();
            trafficStopEntry.Area = trafarea.getText();
            trafficStopEntry.Street = trafstreet.getText();
            trafficStopEntry.operatorName = trafname.getText();
            trafficStopEntry.operatorAge = trafage.getText();
            trafficStopEntry.operatorDescription = trafdesc.getText();
            trafficStopEntry.operatorAddress = trafaddress.getText();
            trafficStopEntry.operatorGender = trafgender.getText();

            // Load existing logs from XML
            List<TrafficStopLogEntry> logs = TrafficStopReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (TrafficStopLogEntry entry : logs) {
                if (entry.getDate().equals(trafficStopEntry.getDate()) &&
                        entry.getTime().equals(trafficStopEntry.getTime())) {
                    entry.PlateNumber = trafplatenum.getText();
                    entry.Color = trafcolor.getText();
                    entry.Type = traftype.getText();
                    entry.StopNumber = trafnum.getText();
                    entry.ResponseModel = trafmodel.getText();
                    entry.ResponseOtherInfo = trafotherinfo.getText();
                    entry.CommentsTextArea = trafcomments.getText();
                    entry.County = trafcounty.getText();
                    entry.Area = trafarea.getText();
                    entry.Street = trafstreet.getText();
                    entry.operatorName = trafname.getText();
                    entry.operatorAge = trafage.getText();
                    entry.operatorDescription = trafdesc.getText();
                    entry.operatorAddress = trafaddress.getText();
                    entry.operatorGender = trafgender.getText();
                    break;
                }
            }

            // Save the updated logs back to XML
            TrafficStopReportLogs.saveLogsToXML(logs);

            // Optionally, you might want to update the TableView here
            trafficStopTable.refresh();

        }
    }

    @javafx.fxml.FXML
    public void onTrafficStopRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            trafficStopEntry = (TrafficStopLogEntry) trafficStopTable.getSelectionModel().getSelectedItem();
            if (trafficStopEntry != null) {
                trafstreet.setText(trafficStopEntry.getStreet());
                trafotherinfo.setText(trafficStopEntry.getResponseOtherInfo());
                trafname.setText(trafficStopEntry.getOperatorName());
                trafcomments.setText(trafficStopEntry.getCommentsTextArea());
                trafdesc.setText(trafficStopEntry.getOperatorDescription());
                trafcolor.setText(trafficStopEntry.getColor());
                trafnum.setText(trafficStopEntry.getStopNumber());
                trafmodel.setText(trafficStopEntry.getResponseModel());
                trafaddress.setText(trafficStopEntry.getOperatorAddress());
                trafarea.setText(trafficStopEntry.getArea());
                trafgender.setText(trafficStopEntry.getOperatorGender());
                trafage.setText(trafficStopEntry.getOperatorAge());
                traftype.setText(trafficStopEntry.getType());
                trafplatenum.setText(trafficStopEntry.getPlateNumber());
                trafcounty.setText(trafficStopEntry.getCounty());
                trafficStopTable.getSelectionModel().clearSelection();
            } else {
                trafstreet.setText("");
                trafotherinfo.setText("");
                trafname.setText("");
                trafcomments.setText("");
                trafdesc.setText("");
                trafcolor.setText("");
                trafnum.setText("");
                trafmodel.setText("");
                trafaddress.setText("");
                trafarea.setText("");
                trafgender.setText("");
                trafage.setText("");
                traftype.setText("");
                trafplatenum.setText("");
                trafcounty.setText("");
            }
        }
    }
    // Incident Section

    @javafx.fxml.FXML
    public void onIncUpdateValues(ActionEvent actionEvent) {
        if (incidentEntry != null) {
            incupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            incidentEntry.incidentNumber = incnum.getText();
            incidentEntry.incidentActionsTaken = incactionstaken.getText();
            incidentEntry.incidentArea = incarea.getText();
            incidentEntry.incidentCounty = inccounty.getText();
            incidentEntry.incidentComments = inccomments.getText();
            incidentEntry.incidentStreet = incstreet.getText();
            incidentEntry.incidentVictims = incvictims.getText();
            incidentEntry.incidentStatement = incstatement.getText();
            incidentEntry.incidentWitnesses = incwitness.getText();

            // Load existing logs from XML
            List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (IncidentLogEntry entry : logs) {
                if (entry.getIncidentDate().equals(incidentEntry.getIncidentDate()) && entry.getIncidentTime().equals(incidentEntry.getIncidentTime())) {
                    entry.incidentNumber = incnum.getText();
                    entry.incidentStatement = incstatement.getText();
                    entry.incidentWitnesses = incwitness.getText();
                    entry.incidentVictims = incvictims.getText();
                    entry.incidentStreet = incstreet.getText();
                    entry.incidentArea = incarea.getText();
                    entry.incidentCounty = inccounty.getText();
                    entry.incidentActionsTaken = incactionstaken.getText();
                    entry.incidentComments = inccomments.getText();
                    break;
                }
            }

            // Save the updated logs back to XML
            IncidentReportLogs.saveLogsToXML(logs);

            // Optionally, you might want to update the TableView here
            incidentTable.refresh();
        }
    }

    @javafx.fxml.FXML
    public void onIncidentRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            incidentEntry = (IncidentLogEntry) incidentTable.getSelectionModel().getSelectedItem();
            if (incidentEntry != null) {
                incnum.setText(incidentEntry.incidentNumber);
                incactionstaken.setText(incidentEntry.incidentActionsTaken);
                incarea.setText(incidentEntry.incidentArea);
                inccounty.setText(incidentEntry.incidentCounty);
                inccomments.setText(incidentEntry.incidentComments);
                incstreet.setText(incidentEntry.incidentStreet);
                incvictims.setText(incidentEntry.incidentVictims);
                incstatement.setText(incidentEntry.incidentStatement);
                incwitness.setText(incidentEntry.incidentWitnesses);
                incidentTable.getSelectionModel().clearSelection();
            } else {
                incnum.setText("");
                incactionstaken.setText("");
                incarea.setText("");
                inccounty.setText("");
                inccomments.setText("");
                incstreet.setText("");
                incvictims.setText("");
                incstatement.setText("");
                incwitness.setText("");
            }
        }
    }
    // Impound Section

    @javafx.fxml.FXML
    public void onImpUpdateValues(ActionEvent actionEvent) {
        if (impoundEntry != null) {
            impupdatedLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                impupdatedLabel.setVisible(false);
            }));
            timeline1.play();

            impoundEntry.impoundPlateNumber = impplatenum.getText();
            impoundEntry.impoundColor = impcolor.getText();
            impoundEntry.impoundType = imptype.getText();
            impoundEntry.impoundNumber = impnum.getText();
            impoundEntry.impoundModel = impmodel.getText();
            impoundEntry.impoundComments = impcomments.getText();
            impoundEntry.ownerName = impname.getText();
            impoundEntry.ownerAddress = impaddress.getText();
            impoundEntry.ownerGender = impgender.getText();
            impoundEntry.ownerAge = impage.getText();

            // Load existing logs from XML
            List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (ImpoundLogEntry entry : logs) {
                if (entry.getImpoundDate().equals(impoundEntry.getImpoundDate()) && entry.getImpoundTime().equals(impoundEntry.getImpoundTime())) {
                    entry.impoundPlateNumber = impplatenum.getText();
                    entry.impoundColor = impcolor.getText();
                    entry.impoundType = imptype.getText();
                    entry.impoundNumber = impnum.getText();
                    entry.impoundModel = impmodel.getText();
                    entry.impoundComments = impcomments.getText();
                    entry.ownerName = impname.getText();
                    entry.ownerAddress = impaddress.getText();
                    entry.ownerGender = impgender.getText();
                    entry.ownerAge = impage.getText();
                    break;
                }
            }

            // Save the updated logs back to XML
            ImpoundReportLogs.saveLogsToXML(logs);

            // Optionally, you might want to update the TableView here
            impoundTable.refresh();
        }
    }

    @javafx.fxml.FXML
    public void onImpoundRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            impoundEntry = (ImpoundLogEntry) impoundTable.getSelectionModel().getSelectedItem();
            if (impoundEntry != null) {
                impnum.setText(impoundEntry.impoundNumber);
                impname.setText(impoundEntry.ownerName);
                impgender.setText(impoundEntry.ownerGender);
                impcolor.setText(impoundEntry.impoundColor);
                impplatenum.setText(impoundEntry.impoundPlateNumber);
                imptype.setText(impoundEntry.impoundType);
                impage.setText(impoundEntry.ownerAge);
                impcomments.setText(impoundEntry.impoundComments);
                impmodel.setText(impoundEntry.impoundModel);
                impaddress.setText(impoundEntry.ownerAddress);
                impoundTable.getSelectionModel().clearSelection();
            } else {
                impnum.setText("");
                impname.setText("");
                impgender.setText("");
                impcolor.setText("");
                impplatenum.setText("");
                imptype.setText("");
                impage.setText("");
                impcomments.setText("");
                impmodel.setText("");
                impaddress.setText("");
            }
        }
    }
    // Citation Section

    @javafx.fxml.FXML
    public void onCitationRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            citationEntry = (TrafficCitationLogEntry) citationTable.getSelectionModel().getSelectedItem();
            if (citationEntry != null) {
                citnumber.setText(citationEntry.citationNumber);
                citvehother.setText(citationEntry.offenderVehicleOther);
                citplatenum.setText(citationEntry.offenderVehiclePlate);
                citcharges.setText(citationEntry.citationCharges);
                citcolor.setText(citationEntry.offenderVehicleColor);
                citcomments.setText(citationEntry.citationComments);
                citaddress.setText(citationEntry.offenderHomeAddress);
                citname.setText(citationEntry.offenderName);
                citdesc.setText(citationEntry.offenderDescription);
                citage.setText(citationEntry.offenderAge);
                citarea.setText(citationEntry.citationArea);
                citgender.setText(citationEntry.offenderGender);
                citstreet.setText(citationEntry.citationStreet);
                citmodel.setText(citationEntry.offenderVehicleModel);
                cittype.setText(citationEntry.offenderVehicleType);
                citcounty.setText(citationEntry.citationCounty);
                citationTable.getSelectionModel().clearSelection();
            } else {
                citnumber.setText("");
                citvehother.setText("");
                citplatenum.setText("");
                citcharges.setText("");
                citcolor.setText("");
                citcomments.setText("");
                citaddress.setText("");
                citname.setText("");
                citdesc.setText("");
                citage.setText("");
                citarea.setText("");
                citgender.setText("");
                citstreet.setText("");
                citmodel.setText("");
                cittype.setText("");
                citcounty.setText("");
            }
        }
    }

    @javafx.fxml.FXML
    public void onCitationUpdateValues(ActionEvent actionEvent) {
        if (citationEntry != null) {
            citupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                citupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            citationEntry.citationNumber = citnumber.getText();
            citationEntry.offenderVehicleOther = citvehother.getText();
            citationEntry.offenderVehiclePlate = citplatenum.getText();
            citationEntry.citationCharges = citcharges.getText();
            citationEntry.offenderVehicleColor = citcolor.getText();
            citationEntry.citationComments = citcomments.getText();
            citationEntry.offenderHomeAddress = citaddress.getText();
            citationEntry.offenderName = citname.getText();
            citationEntry.offenderDescription = citdesc.getText();
            citationEntry.offenderAge = citage.getText();
            citationEntry.citationArea = citarea.getText();
            citationEntry.offenderGender = citgender.getText();
            citationEntry.citationStreet = citstreet.getText();
            citationEntry.offenderVehicleModel = citmodel.getText();
            citationEntry.offenderVehicleType = cittype.getText();
            citationEntry.citationCounty = citcounty.getText();

            // Load existing logs from XML
            List<TrafficCitationLogEntry> logs = TrafficCitationReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (TrafficCitationLogEntry entry : logs) {
                if (entry.getCitationDate().equals(citationEntry.getCitationDate()) && entry.getCitationTime().equals(citationEntry.getCitationTime())) {
                    entry.citationNumber = citnumber.getText();
                    entry.offenderVehicleOther = citvehother.getText();
                    entry.offenderVehiclePlate = citplatenum.getText();
                    entry.citationCharges = citcharges.getText();
                    entry.offenderVehicleColor = citcolor.getText();
                    entry.citationComments = citcomments.getText();
                    entry.offenderHomeAddress = citaddress.getText();
                    entry.offenderName = citname.getText();
                    entry.offenderDescription = citdesc.getText();
                    entry.offenderAge = citage.getText();
                    entry.citationArea = citarea.getText();
                    entry.offenderGender = citgender.getText();
                    entry.citationStreet = citstreet.getText();
                    entry.offenderVehicleModel = citmodel.getText();
                    entry.offenderVehicleType = cittype.getText();
                    entry.citationCounty = citcounty.getText();
                    break;
                }
            }
            //Save the updated logs back to XML
            TrafficCitationReportLogs.saveLogsToXML(logs);
            //Optionally, you might want to update the TableView here
            citationTable.refresh();
        }
    }
    // Search Section

    @javafx.fxml.FXML
    public void onSearchRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            searchEntry = (SearchLogEntry) searchTable.getSelectionModel().getSelectedItem();
            if (searchEntry != null) {
                searchnum.setText(searchEntry.SearchNumber);
                searchperson.setText(searchEntry.searchedPersons);
                searchmethod.setText(searchEntry.searchMethod);
                searchseizeditems.setText(searchEntry.searchSeizedItems);
                searchtype.setText(searchEntry.searchType);
                searchcomments.setText(searchEntry.searchComments);
                searchbreathused.setText(searchEntry.testsConducted);
                searchbreathresult.setText(searchEntry.testResults);
                searchstreet.setText(searchEntry.searchStreet);
                searcharea.setText(searchEntry.searchArea);
                searchgrounds.setText(searchEntry.searchGrounds);
                searchwitness.setText(searchEntry.searchWitnesses);
                searchbacmeasure.setText(searchEntry.breathalyzerBACMeasure);
                searchcounty.setText(searchEntry.searchCounty);
                searchTable.getSelectionModel().clearSelection();
            } else {
                searchnum.setText("");
                searchperson.setText("");
                searchmethod.setText("");
                searchseizeditems.setText("");
                searchtype.setText("");
                searchcomments.setText("");
                searchbreathused.setText("");
                searchbreathresult.setText("");
                searchstreet.setText("");
                searcharea.setText("");
                searchgrounds.setText("");
                searchwitness.setText("");
                searchbacmeasure.setText("");
                searchcounty.setText("");
            }
        }
    }

    @javafx.fxml.FXML
    public void onSearchUpdateValues(ActionEvent actionEvent) {
        if (searchEntry != null) {
            searchupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                searchupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            searchEntry.SearchNumber = searchnum.getText();
            searchEntry.searchedPersons = searchperson.getText();
            searchEntry.searchMethod = searchmethod.getText();
            searchEntry.searchSeizedItems = searchseizeditems.getText();
            searchEntry.searchType = searchtype.getText();
            searchEntry.searchComments = searchcomments.getText();
            searchEntry.testsConducted = searchbreathused.getText();
            searchEntry.searchStreet = searchstreet.getText();
            searchEntry.searchArea = searcharea.getText();
            searchEntry.searchGrounds = searchgrounds.getText();
            searchEntry.searchWitnesses = searchwitness.getText();
            searchEntry.breathalyzerBACMeasure = searchbacmeasure.getText();
            searchEntry.searchCounty = searchcounty.getText();
            searchEntry.testResults = searchbreathresult.getText();

            // Load existing logs from XML
            List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (SearchLogEntry entry : logs) {
                if (entry.getSearchDate().equals(searchEntry.getSearchDate()) && entry.getSearchTime().equals(searchEntry.getSearchTime())) {
                    entry.SearchNumber = searchnum.getText();
                    entry.searchedPersons = searchperson.getText();
                    entry.searchMethod = searchmethod.getText();
                    entry.searchSeizedItems = searchseizeditems.getText();
                    entry.searchType = searchtype.getText();
                    entry.searchComments = searchcomments.getText();
                    entry.testsConducted = searchbreathused.getText();
                    entry.searchStreet = searchstreet.getText();
                    entry.searchArea = searcharea.getText();
                    entry.searchGrounds = searchgrounds.getText();
                    entry.searchWitnesses = searchwitness.getText();
                    entry.breathalyzerBACMeasure = searchbacmeasure.getText();
                    entry.searchCounty = searchcounty.getText();
                    entry.testResults = searchbreathresult.getText();
                    break;
                }
            }
            //Save the updated logs back to XML
            SearchReportLogs.saveLogsToXML(logs);
            //Optionally, you might want to update the TableView here
            searchTable.refresh();
        }
    }
    // Arrest Section

    @javafx.fxml.FXML
    public void onArrestRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) { // single click
            arrestEntry = (ArrestLogEntry) arrestTable.getSelectionModel().getSelectedItem();
            if (arrestEntry != null) {
                arrestnum.setText(arrestEntry.arrestNumber);
                arrestcounty.setText(arrestEntry.arrestCounty);
                arrestdesc.setText(arrestEntry.arresteeDescription);
                arrestarea.setText(arrestEntry.arrestArea);
                arrestambulance.setText(arrestEntry.ambulanceYesNo);
                arrestname.setText(arrestEntry.arresteeName);
                arrestdetails.setText(arrestEntry.arrestDetails);
                arrestmedinfo.setText(arrestEntry.arresteeMedicalInformation);
                arrestaddress.setText(arrestEntry.arresteeHomeAddress);
                arrestage.setText(arrestEntry.arresteeAge);
                arrestgender.setText(arrestEntry.arresteeGender);
                arreststreet.setText(arrestEntry.arrestStreet);
                arresttaser.setText(arrestEntry.TaserYesNo);
                arrestcharges.setText(arrestEntry.arrestCharges);
                arrestTable.getSelectionModel().clearSelection();
            } else {
                arrestnum.setText("");
                arrestcounty.setText("");
                arrestdesc.setText("");
                arrestarea.setText("");
                arrestambulance.setText("");
                arrestname.setText("");
                arrestdetails.setText("");
                arrestmedinfo.setText("");
                arrestaddress.setText("");
                arrestage.setText("");
                arrestgender.setText("");
                arreststreet.setText("");
                arresttaser.setText("");
                arrestcharges.setText("");

            }
        }
    }

    @javafx.fxml.FXML
    public void onArrUpdateValues(ActionEvent actionEvent) {
        if (arrestEntry != null) {
            arrestupdatedlabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                arrestupdatedlabel.setVisible(false);
            }));
            timeline1.play();

            arrestEntry.arrestNumber = arrestnum.getText();
            arrestEntry.arrestCharges = arrestcharges.getText();
            arrestEntry.arrestCounty = arrestcounty.getText();
            arrestEntry.arresteeDescription = arrestdesc.getText();
            arrestEntry.arrestArea = arrestarea.getText();
            arrestEntry.ambulanceYesNo = arrestambulance.getText();
            arrestEntry.arresteeName = arrestname.getText();
            arrestEntry.arrestDetails = arrestdetails.getText();
            arrestEntry.arresteeMedicalInformation = arrestmedinfo.getText();
            arrestEntry.arresteeHomeAddress = arrestaddress.getText();
            arrestEntry.arresteeAge = arrestage.getText();
            arrestEntry.arresteeGender = arrestgender.getText();
            arrestEntry.arrestStreet = arreststreet.getText();
            arrestEntry.TaserYesNo = arresttaser.getText();

            // Load existing logs from XML
            List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();

            // Update the corresponding log entry
            for (ArrestLogEntry entry : logs) {
                if (entry.getArrestDate().equals(arrestEntry.getArrestDate()) && entry.getArrestTime().equals(arrestEntry.getArrestTime())) {
                    entry.arrestNumber = arrestnum.getText();
                    entry.arrestCharges = arrestcharges.getText();
                    entry.arrestCounty = arrestcounty.getText();
                    entry.arresteeDescription = arrestdesc.getText();
                    entry.arrestArea = arrestarea.getText();
                    entry.ambulanceYesNo = arrestambulance.getText();
                    entry.arresteeName = arrestname.getText();
                    entry.arrestDetails = arrestdetails.getText();
                    entry.arresteeMedicalInformation = arrestmedinfo.getText();
                    entry.arresteeHomeAddress = arrestaddress.getText();
                    entry.arresteeAge = arrestage.getText();
                    entry.arresteeGender = arrestgender.getText();
                    entry.arrestStreet = arreststreet.getText();
                    entry.TaserYesNo = arresttaser.getText();
                    break;
                }
            }
            //Save the updated logs back to XML
            ArrestReportLogs.saveLogsToXML(logs);
            //Optionally, you might want to update the TableView here
            arrestTable.refresh();
        }
    }


    //</editor-fold>

}