package com.drozal.dataterminal;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Arrest.ArrestLogEntry;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.ChargesData;
import com.drozal.dataterminal.logs.CitationsData;
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
import com.drozal.dataterminal.util.ResizeHelper;
import com.drozal.dataterminal.util.controllerUtils;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.PopOver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.*;
import static com.drozal.dataterminal.util.controllerUtils.*;
import static com.drozal.dataterminal.util.reportCreationUtil.newPatrol;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.treeViewUtils.*;
import static com.drozal.dataterminal.util.windowUtils.*;

public class actionController {

    public static String notesText;

    //<editor-fold desc="FXML Elements">
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
    @javafx.fxml.FXML
    public AnchorPane calloutReportPane;
    private double xOffset = 0;
    private double yOffset = 0;
    private MedicalInformation medicalInformationController;
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
    private TextField calloutReportResponseCounty;
    @javafx.fxml.FXML
    private TextField calloutReportName;
    @javafx.fxml.FXML
    private Label calloutincompleteLabel;
    @javafx.fxml.FXML
    private MenuItem searchReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportAgency;
    @javafx.fxml.FXML
    private MenuItem trafficReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportResponseAddress;
    @javafx.fxml.FXML
    private MenuItem impoundReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportResponseGrade;
    @javafx.fxml.FXML
    private TextArea calloutReportNotesTextArea;
    @javafx.fxml.FXML
    private Button calloutReportSubmitBtn;
    @javafx.fxml.FXML
    private MenuItem incidentReportButton;
    @javafx.fxml.FXML
    private MenuItem patrolReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportTime;
    @javafx.fxml.FXML
    private MenuItem calloutReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportDivision;
    @javafx.fxml.FXML
    private Spinner calloutReportSpinner;
    @javafx.fxml.FXML
    private TextField calloutReportResponeType;
    @javafx.fxml.FXML
    private TextField calloutReportResponseArea;
    @javafx.fxml.FXML
    private MenuItem arrestReportButton;
    @javafx.fxml.FXML
    private MenuItem trafficCitationReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportNumber;
    @javafx.fxml.FXML
    private TextField calloutReportDate;
    @javafx.fxml.FXML
    private TextField calloutReportRank;
    @javafx.fxml.FXML
    private TextField patrolDate;
    @javafx.fxml.FXML
    private TextField patrolLength;
    @javafx.fxml.FXML
    private TextField patrolofficerNumber;
    @javafx.fxml.FXML
    private TextField patrolStopTime;
    @javafx.fxml.FXML
    private AnchorPane patrolReportPane;
    @javafx.fxml.FXML
    private TextField patrolofficerDivision;
    @javafx.fxml.FXML
    private Label patrolincompleteLabel;
    @javafx.fxml.FXML
    private TextField patrolofficerVehicle;
    @javafx.fxml.FXML
    private TextField patrolStartTime;
    @javafx.fxml.FXML
    private TextArea patrolComments;
    @javafx.fxml.FXML
    private Spinner patrolSpinnerNumber;
    @javafx.fxml.FXML
    private TextField patrolofficerRank;
    @javafx.fxml.FXML
    private TextField patrolofficerAgency;
    @javafx.fxml.FXML
    private TextField patrolofficerName;
    @javafx.fxml.FXML
    private Button incidentReportSubmitBtn;
    @javafx.fxml.FXML
    private TextArea incidentComments;
    @javafx.fxml.FXML
    private TextField incidentofficerName;
    @javafx.fxml.FXML
    private TextField incidentofficerAgency;
    @javafx.fxml.FXML
    private AnchorPane incidentReportPane;
    @javafx.fxml.FXML
    private TextField incidentofficerDivision;
    @javafx.fxml.FXML
    private TextField incidentVictims;
    @javafx.fxml.FXML
    private TextField incidentofficerRank;
    @javafx.fxml.FXML
    private TextField incidentCounty;
    @javafx.fxml.FXML
    private TextField incidentWitnesses;
    @javafx.fxml.FXML
    private TextArea incidentStatement;
    @javafx.fxml.FXML
    private TextField incidentofficerNumber;
    @javafx.fxml.FXML
    private TextField incidentStreet;
    @javafx.fxml.FXML
    private TextArea incidentActionsTaken;
    @javafx.fxml.FXML
    private Label incidentincompleteLabel;
    @javafx.fxml.FXML
    private TextField incidentArea;
    @javafx.fxml.FXML
    private TextField incidentReportTime;
    @javafx.fxml.FXML
    private Spinner incidentNumber;
    @javafx.fxml.FXML
    private TextField incidentReportdate;
    @javafx.fxml.FXML
    private Button searchReportSubmitBtn;
    @javafx.fxml.FXML
    private TextArea searchComments;
    @javafx.fxml.FXML
    private TextField searchTime;
    @javafx.fxml.FXML
    private TextField searchofficerName;
    @javafx.fxml.FXML
    private Spinner SearchNumber;
    @javafx.fxml.FXML
    private TextField searchofficerDivision;
    @javafx.fxml.FXML
    private TextField searchWitnesses;
    @javafx.fxml.FXML
    private Button searchpopOverBtn;
    @javafx.fxml.FXML
    private AnchorPane searchReportPane;
    @javafx.fxml.FXML
    private TextField searchCounty;
    @javafx.fxml.FXML
    private TextField searchedPersons;
    @javafx.fxml.FXML
    private ComboBox searchType;
    @javafx.fxml.FXML
    private TextField searchofficerRank;
    @javafx.fxml.FXML
    private TextField searchofficerNumber;
    @javafx.fxml.FXML
    private ComboBox searchMethod;
    @javafx.fxml.FXML
    private Label searchincompleteLabel;
    @javafx.fxml.FXML
    private TextField searchArea;
    @javafx.fxml.FXML
    private TextField searchStreet;
    @javafx.fxml.FXML
    private TextField searchDate;
    @javafx.fxml.FXML
    private TextField searchGrounds;
    @javafx.fxml.FXML
    private TextArea searchSeizedItems;
    @javafx.fxml.FXML
    private TextField searchofficerAgency;
    private DUIInformationController duiInformationController;
    @javafx.fxml.FXML
    private ComboBox impoundColor;
    @javafx.fxml.FXML
    private TextField impoundofficerDivision;
    @javafx.fxml.FXML
    private TextField impoundownerGender;
    @javafx.fxml.FXML
    private TextField impoundofficerNumber;
    @javafx.fxml.FXML
    private TextArea impoundComments;
    @javafx.fxml.FXML
    private ComboBox impoundType;
    @javafx.fxml.FXML
    private TextField impoundDate;
    @javafx.fxml.FXML
    private TextField impoundownerAge;
    @javafx.fxml.FXML
    private TextField impoundofficerAgency;
    @javafx.fxml.FXML
    private TextField impoundofficerRank;
    @javafx.fxml.FXML
    private Spinner impoundNumber;
    @javafx.fxml.FXML
    private AnchorPane impoundReportPane;
    @javafx.fxml.FXML
    private TextField impoundownerName;
    @javafx.fxml.FXML
    private Label impoundincompleteLabel;
    @javafx.fxml.FXML
    private TextField impoundModel;
    @javafx.fxml.FXML
    private TextField impoundPlateNumber;
    @javafx.fxml.FXML
    private TextField impoundownerAddress;
    @javafx.fxml.FXML
    private TextField impoundTime;
    @javafx.fxml.FXML
    private TextField impoundofficerName;
    @javafx.fxml.FXML
    private Label trafficStopIncompleteLabel;
    @javafx.fxml.FXML
    private AnchorPane trafficStopReportPane;
    @javafx.fxml.FXML
    private TextField trafficStopCounty;
    @javafx.fxml.FXML
    private TextField trafficStopofficerNumber;
    @javafx.fxml.FXML
    private ComboBox trafficStopColor;
    @javafx.fxml.FXML
    private TextField trafficStopownerGender;
    @javafx.fxml.FXML
    private Spinner trafficStopNumber;
    @javafx.fxml.FXML
    private TextArea trafficStopComments;
    @javafx.fxml.FXML
    private ComboBox trafficStopType;
    @javafx.fxml.FXML
    private TextField trafficStopownerAge;
    @javafx.fxml.FXML
    private TextField trafficStopOtherInfo;
    @javafx.fxml.FXML
    private TextField trafficStopownerName;
    @javafx.fxml.FXML
    private TextField trafficStopofficerDivision;
    @javafx.fxml.FXML
    private TextField trafficStopModel;
    @javafx.fxml.FXML
    private TextField trafficStopofficerAgency;
    @javafx.fxml.FXML
    private TextField trafficStopTime;
    @javafx.fxml.FXML
    private TextField trafficStopownerDescription;
    @javafx.fxml.FXML
    private TextField trafficStopArea;
    @javafx.fxml.FXML
    private TextField trafficStopofficerRank;
    @javafx.fxml.FXML
    private TextField trafficStopPlateNumber;
    @javafx.fxml.FXML
    private TextField trafficStopDate;
    @javafx.fxml.FXML
    private TextField trafficStopofficerName;
    @javafx.fxml.FXML
    private TextField trafficStopStreet;
    @javafx.fxml.FXML
    private TextField trafficStopownerAddress;
    @javafx.fxml.FXML
    private TextField arrestProbationChance;
    @javafx.fxml.FXML
    private TextField arrestMinMonths;
    @javafx.fxml.FXML
    private TableView arrestChargeTable;
    @javafx.fxml.FXML
    private TextField arrestChargeField;
    @javafx.fxml.FXML
    private TextArea arrestComments;
    @javafx.fxml.FXML
    private TextField arrestOwnerAge;
    @javafx.fxml.FXML
    private AnchorPane arrestYearsPane;
    @javafx.fxml.FXML
    private TableColumn arrestChargeColumn;
    @javafx.fxml.FXML
    private TextField arrestMaxMonths;
    @javafx.fxml.FXML
    private TextField arrestMaxSuspension;
    @javafx.fxml.FXML
    private TextField arrestOfficerRank;
    @javafx.fxml.FXML
    private TextField arrestMinSuspension;
    @javafx.fxml.FXML
    private TextField arrestOwnerGender;
    @javafx.fxml.FXML
    private AnchorPane arrestMonthsPane;
    @javafx.fxml.FXML
    private TextField arrestOwnerAddress;
    @javafx.fxml.FXML
    private AnchorPane arrestReportPane;
    @javafx.fxml.FXML
    private TextField arrestDate;
    @javafx.fxml.FXML
    private TextField arrestOwnerName;
    @javafx.fxml.FXML
    private TextField arrestOfficerName;
    @javafx.fxml.FXML
    private TreeView arrestTreeView;
    @javafx.fxml.FXML
    private Spinner arrestNumber;
    @javafx.fxml.FXML
    private TextField arrestOfficerNumber;
    @javafx.fxml.FXML
    private Label arrestIncompleteLabel;
    @javafx.fxml.FXML
    private TextField arrestStreet;
    @javafx.fxml.FXML
    private TextField arrestOfficerAgency;
    @javafx.fxml.FXML
    private TextField arrestArea;
    @javafx.fxml.FXML
    private TextField arrestOwnerDescription;
    @javafx.fxml.FXML
    private TextField arrestTime;
    @javafx.fxml.FXML
    private TextField arrestOfficerDivision;
    @javafx.fxml.FXML
    private TextField arrestCounty;
    @javafx.fxml.FXML
    private TextField arrestMinYears;
    @javafx.fxml.FXML
    private ScrollPane arrestChargeScrollPane;
    @javafx.fxml.FXML
    private TextField arrestMaxYears;
    @javafx.fxml.FXML
    private Button arrestMedInfoBtn;
    @javafx.fxml.FXML
    private AnchorPane arrestTrafficChargeInfoPane;
    @javafx.fxml.FXML
    private TitledPane arrestAccordionInformation;
    @javafx.fxml.FXML
    private TextField citationOwnerGender;
    @javafx.fxml.FXML
    private TextField citationCounty;
    @javafx.fxml.FXML
    private TableColumn citationColumn;
    @javafx.fxml.FXML
    private TextField citationOwnerAge;
    @javafx.fxml.FXML
    private TitledPane citationAccordionInformation;
    @javafx.fxml.FXML
    private TextField citationTime;
    @javafx.fxml.FXML
    private TextField citationArea;
    @javafx.fxml.FXML
    private TextField citationStreet;
    @javafx.fxml.FXML
    private TextField citationOwnerAddress;
    @javafx.fxml.FXML
    private TextField citationOfficerNumber;
    @javafx.fxml.FXML
    private Spinner citationNumber;
    @javafx.fxml.FXML
    private TextField citationOfficerAgency;
    @javafx.fxml.FXML
    private ScrollPane citationScrollPane;
    @javafx.fxml.FXML
    private TextField citationFine;
    @javafx.fxml.FXML
    private Label citationIncompleteLabel;
    @javafx.fxml.FXML
    private TableView citationTable;
    @javafx.fxml.FXML
    private TextField citationOfficerDivision;
    @javafx.fxml.FXML
    private TextField citationOfficerName;
    @javafx.fxml.FXML
    private TextField citationField;
    @javafx.fxml.FXML
    private AnchorPane citationReportPane;
    @javafx.fxml.FXML
    private TextField citationOwnerName;
    @javafx.fxml.FXML
    private TextField citationOfficerRank;
    @javafx.fxml.FXML
    private TextField citationOwnerDescription;
    @javafx.fxml.FXML
    private TextField citationDate;
    @javafx.fxml.FXML
    private TextArea citationComments;
    @javafx.fxml.FXML
    private TreeView citationTreeView;
    @javafx.fxml.FXML
    private TextField citationOwnerVehiclePlate;
    @javafx.fxml.FXML
    private TextField citationOwnerVehicleModel;
    @javafx.fxml.FXML
    private TextField citationOwnerVehicleOther;
    @javafx.fxml.FXML
    private ComboBox citationOwnerVehicleColor;
    @javafx.fxml.FXML
    private ComboBox citationOwnerVehicleType;
    @javafx.fxml.FXML
    private RadioMenuItem startupFullscreenToggleBtn;
    @javafx.fxml.FXML
    private AreaChart areaReportChart;
    //</editor-fold>


    //<editor-fold desc="Utils">


    public void initialize() throws IOException {
        setDisable(citationReportPane, infoPane, patrolReportPane, calloutReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(shiftInformationPane);

        startupFullscreenToggleBtn.setSelected(ConfigReader.configRead("fullscreenOnStartup").equals("true"));

        notesText = "";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/DUIInformation.fxml"));
        loader.load();
        duiInformationController = loader.getController();

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
        trafficStopType.getItems().addAll(dropdownInfo.vehicleTypes);
        trafficStopColor.getItems().addAll(dropdownInfo.carColors);
        citationOwnerVehicleType.getItems().addAll(dropdownInfo.vehicleTypes);
        citationOwnerVehicleColor.getItems().addAll(dropdownInfo.carColors);
        impoundType.getItems().addAll(dropdownInfo.vehicleTypes);
        impoundColor.getItems().addAll(dropdownInfo.carColors);
        searchType.getItems().addAll(dropdownInfo.searchTypes);
        searchMethod.getItems().addAll(dropdownInfo.searchMethods);

        OfficerInfoName.setText(name);
        OfficerInfoDivision.setValue(division);
        OfficerInfoRank.setValue(rank);
        OfficerInfoAgency.setValue(agency);
        OfficerInfoNumber.setText(number);
        generatedByTag.setText("Generated By:" + " " + name);
        String time = DataTerminalHomeApplication.getTime();
        generatedDateTag.setText("Generated at: " + time);

        createSpinnerNumListener(calloutReportSpinner);
        createSpinnerNumListener(patrolSpinnerNumber);
        createSpinnerNumListener(citationNumber);
        createSpinnerNumListener(impoundNumber);
        createSpinnerNumListener(incidentNumber);
        createSpinnerNumListener(trafficStopNumber);
        createSpinnerNumListener(arrestNumber);
        createSpinnerNumListener(SearchNumber);

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
        //Accent
        String accclr = ConfigReader.configRead("accentColor");
        //Buttons
        String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
        String initialStyle = "-fx-background-color: transparent;";
        String nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("accentColor") + ";";
        updateInfoBtn.setStyle(nonTransparentBtn);

        // Add hover event handling
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

            } catch (IOException e) {
                throw new RuntimeException(e);
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
                throw new RuntimeException(e);
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
                    throw new RuntimeException(e);
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
                    throw new RuntimeException(e);
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
                    throw new RuntimeException(e);
                }
            }
        });

        String[] displayPlacements = {"Default", "Top Left", "Top Right", "Bottom Left", "Bottom Right", "\n", "Full Left", "Full Right"};
        mainWindowComboBox.getItems().addAll(displayPlacements);
        notesWindowComboBox.getItems().addAll(displayPlacements);
        ReportWindowComboBox.getItems().addAll(displayPlacements);

        try {
            mainWindowComboBox.setValue(ConfigReader.configRead("mainWindowLayout"));
            notesWindowComboBox.setValue(ConfigReader.configRead("notesWindowLayout"));
            ReportWindowComboBox.setValue(ConfigReader.configRead("reportWindowLayout"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(15);
        root.setVgap(15);

        // Add headings for display settings
        root.addRow(0, new Label("Display Placements"));
        root.addRow(1, new Label("Main Window Placement:"), mainWindowComboBox);
        root.addRow(2, new Label("Notes Window Placement:"), notesWindowComboBox);
        root.addRow(3, new Label("Report Window Placement:"), ReportWindowComboBox);

        // Add empty row
        root.addRow(4, new Label());

        // Add headings for color settings
        root.addRow(5, new Label("Colors"));
        root.addRow(6, primaryLabel, primPicker);
        root.addRow(7, secondaryLabel, secPicker);
        root.addRow(8, accentLabel, accPicker);

        // Add reset button
        root.addRow(9, resetBtn);

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

        mainWindowComboBox.setOnAction(comboBoxHandler);
        notesWindowComboBox.setOnAction(comboBoxHandler);
        ReportWindowComboBox.setOnAction(comboBoxHandler);

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


    //</editor-fold>


    //<editor-fold desc="Getters">

    public AnchorPane getTopPane() {
        return topPane;
    }

    public BarChart getReportChart() {
        return reportChart;
    }

    public ComboBox getOfficerInfoAgency() {
        return OfficerInfoAgency;
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

    public AnchorPane getInfoPane() {
        return infoPane;
    }

    public AnchorPane getShiftInformationPane() {
        return shiftInformationPane;
    }


    //</editor-fold>


    //<editor-fold desc="WindowUtils">

    @javafx.fxml.FXML
    public void onExitButtonClick(MouseEvent actionEvent) {
        Platform.exit();
    }

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


    //</editor-fold>


    //<editor-fold desc="Side Button Events">

    @javafx.fxml.FXML
    public void onMapButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("map-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Los Santos Map");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.show();
        stage.centerOnScreen();
        showButtonAnimation(mapButton);
    }

    @javafx.fxml.FXML
    public void onNotesButtonClicked(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("notes-view.fxml"));
        Parent root = loader.load();
        notesViewController = loader.getController();
        BorderlessScene newScene = new BorderlessScene(stage, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
        stage.setTitle("Notes");
        stage.setScene(newScene);
        stage.setResizable(true);
        stage.initOwner(DataTerminalHomeApplication.getMainRT());
        stage.show();

        String startupValue = ConfigReader.configRead("notesWindowLayout");
        switch (startupValue) {
            case "TopLeft" -> snapToTopLeft(stage);
            case "TopRight" -> snapToTopRight(stage);
            case "BottomLeft" -> snapToBottomLeft(stage);
            case "BottomRight" -> snapToBottomRight(stage);
            case "FullLeft" -> snapToLeft(stage);
            case "FullRight" -> snapToRight(stage);
            default -> {
                stage.centerOnScreen();
                stage.setMinHeight(300);
                stage.setMinWidth(300);
            }
        }
        stage.getScene().getStylesheets().add(getClass().getResource("css/notification-styles.css").toExternalForm());
        showButtonAnimation(notesButton);
        AnchorPane topbar = notesViewController.getTitlebar();
        newScene.setMoveControl(topbar);
        stage.setAlwaysOnTop(true);
    }

    @javafx.fxml.FXML
    public void onShiftInfoBtnClicked(ActionEvent actionEvent) throws IOException {
        setDisable(citationReportPane, infoPane, patrolReportPane, calloutReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(shiftInformationPane);
        showButtonAnimation(shiftInfoBtn);
        controllerUtils.refreshChart(areaReportChart, "area");
    }

    @javafx.fxml.FXML
    public void onLogsButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logBrowser.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Log Browser");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setMinHeight(300);
        stage.setMinWidth(300);
        ResizeHelper.addResizeListener(stage);
        showButtonAnimation(logsButton);
    }


    //</editor-fold>


    //<editor-fold desc="Settings Button Events">

    @javafx.fxml.FXML
    public void testBtnPress(ActionEvent actionEvent) {
        //newCallout(reportChart, areaReportChart, vbox, notesViewController);
        newPatrol(reportChart, areaReportChart, vbox, notesViewController);
    }

    @javafx.fxml.FXML
    public void aboutBtnClick(ActionEvent actionEvent) {
        setDisable(citationReportPane, shiftInformationPane, calloutReportPane, patrolReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
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
    // UI Settings Events

    @javafx.fxml.FXML
    public void UISettingsBtnClick(ActionEvent actionEvent) {
        showSettingsWindow();
    }

    @Deprecated
    public void resetDefaultsBtnPress(ActionEvent actionEvent) throws IOException {
    }

    @Deprecated
    public void onColorSelectMainPress(ActionEvent actionEvent) throws IOException {

    }


    //</editor-fold>


    //<editor-fold desc="Open Report Button Events">

    @Deprecated
    public void onColorSelectSecondaryPress(ActionEvent actionEvent) throws IOException {

    }

    @Deprecated
    public void onColorSelectAccentPress(ActionEvent actionEvent) throws IOException {

    }

    @javafx.fxml.FXML
    public void onCalloutReportButtonClick(ActionEvent actionEvent) throws IOException {
        setDisable(citationReportPane, shiftInformationPane, infoPane, patrolReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(calloutReportPane);

        createSpinner(calloutReportSpinner, 0, 9999, 0);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        calloutReportName.setText(name);
        calloutReportDivision.setText(division);
        calloutReportRank.setText(rank);
        calloutReportAgency.setText(agency);
        calloutReportNumber.setText(number);

        calloutReportDate.setText(DataTerminalHomeApplication.getDate());
        calloutReportTime.setText(DataTerminalHomeApplication.getTime());
    }

    @javafx.fxml.FXML
    public void trafficStopReportButtonClick(ActionEvent actionEvent) throws IOException {
        setDisable(citationReportPane, shiftInformationPane, infoPane, patrolReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, calloutReportPane, arrestReportPane);
        setActive(trafficStopReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");


        trafficStopofficerName.setText(name);
        trafficStopofficerDivision.setText(division);
        trafficStopofficerRank.setText(rank);
        trafficStopofficerAgency.setText(agency);
        trafficStopofficerNumber.setText(number);

        createSpinner(trafficStopNumber, 0, 9999, 0);

        trafficStopTime.setText(getTime());
        trafficStopDate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onIncidentReportBtnClick(ActionEvent actionEvent) throws IOException {
        setDisable(citationReportPane, shiftInformationPane, infoPane, patrolReportPane, calloutReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(incidentReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        incidentofficerName.setText(name);
        incidentofficerDivision.setText(division);
        incidentofficerRank.setText(rank);
        incidentofficerAgency.setText(agency);
        incidentofficerNumber.setText(number);

        createSpinner(incidentNumber, 0, 9999, 0);
        incidentReportTime.setText(getTime());
        incidentReportdate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onSearchReportBtnClick(ActionEvent actionEvent) throws IOException {
        setDisable(citationReportPane, shiftInformationPane, infoPane, patrolReportPane, calloutReportPane, incidentReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(searchReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");


        searchofficerName.setText(name);
        searchofficerDivision.setText(division);
        searchofficerRank.setText(rank);
        searchofficerAgency.setText(agency);
        searchofficerNumber.setText(number);

        createSpinner(SearchNumber, 0, 9999, 0);
        searchTime.setText(getTime());
        searchDate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onArrestReportBtnClick(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException {
        setDisable(citationReportPane, shiftInformationPane, infoPane, patrolReportPane, calloutReportPane, incidentReportPane, impoundReportPane, trafficStopReportPane, searchReportPane);
        setActive(arrestReportPane);
        arrestAccordionInformation.setExpanded(true);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/medicalInformation.fxml"));
        loader.load();
        medicalInformationController = loader.getController();

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        arrestOfficerName.setText(name);
        arrestOfficerDivision.setText(division);
        arrestOfficerRank.setText(rank);
        arrestOfficerAgency.setText(agency);
        arrestOfficerNumber.setText(number);
        createSpinner(arrestNumber, 0, 9999, 0);
        arrestTime.setText(getTime());
        arrestDate.setText(getDate());

        //Tree View
        File file = new File(getJarPath() + "/data/Charges.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().parse(file);

        Element root = document.getDocumentElement();

        TreeItem<String> rootItem = new TreeItem<>(root.getNodeName());

        parseTreeXML(root, rootItem);
        arrestTreeView.setRoot(rootItem);
        expandTreeItem(rootItem, "Charges");
        arrestMonthsPane.setVisible(true);
        arrestYearsPane.setVisible(false);
    }

    @javafx.fxml.FXML
    public void onCitationReportBtnClick(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException {
        setDisable(shiftInformationPane, infoPane, patrolReportPane, calloutReportPane, incidentReportPane, impoundReportPane, trafficStopReportPane, searchReportPane, arrestReportPane);
        setActive(citationReportPane);
        citationAccordionInformation.setExpanded(true);
        createSpinner(citationNumber, 0, 9999, 0);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        citationOfficerName.setText(name);
        citationOfficerDivision.setText(division);
        citationOfficerRank.setText(rank);
        citationOfficerAgency.setText(agency);
        citationOfficerNumber.setText(number);
        citationTime.setText(getTime());
        citationDate.setText(getDate());

        //Tree View
        File file = new File(getJarPath() + "/data/Citations.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().parse(file);

        Element root = document.getDocumentElement();

        TreeItem<String> rootItem = new TreeItem<>(root.getNodeName());

        parseTreeXML(root, rootItem);
        citationTreeView.setRoot(rootItem);
        expandTreeItem(rootItem, "Citations");
    }


    //</editor-fold>


    //<editor-fold desc="Submit Report Button Events">

    @javafx.fxml.FXML
    public void onPatrolButtonClick(ActionEvent actionEvent) throws IOException {
        setDisable(citationReportPane, shiftInformationPane, infoPane, calloutReportPane, incidentReportPane, searchReportPane, impoundReportPane, trafficStopReportPane, arrestReportPane);
        setActive(patrolReportPane);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        patrolofficerName.setText(name);
        patrolofficerDivision.setText(division);
        patrolofficerRank.setText(rank);
        patrolofficerAgency.setText(agency);
        patrolofficerNumber.setText(number);
        createSpinner(patrolSpinnerNumber, 0, 9999, 0);
        patrolStopTime.setText(getTime());
        patrolDate.setText(getDate());
    }

    @javafx.fxml.FXML
    public void onImpoundReportBtnClick(ActionEvent actionEvent) throws IOException {
        setDisable(citationReportPane, shiftInformationPane, infoPane, calloutReportPane, incidentReportPane, searchReportPane, patrolReportPane, trafficStopReportPane, arrestReportPane);
        setActive(impoundReportPane);
        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");
        impoundofficerName.setText(name);
        impoundofficerDivision.setText(division);
        impoundofficerRank.setText(rank);
        impoundofficerAgency.setText(agency);
        impoundofficerNumber.setText(number);
        impoundTime.setText(getTime());
        impoundDate.setText(getDate());
        createSpinner(impoundNumber, 0, 9999, 0);
    }

    @javafx.fxml.FXML
    public void onCalloutReportSubmitBtnClick(ActionEvent actionEvent) {
        if (calloutReportSpinner.getValue() == null) {
            calloutincompleteLabel.setText("Fill Out Form.");
            calloutincompleteLabel.setStyle("-fx-text-fill: red;");
            calloutincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                calloutincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {// Load existing logs from XML
            List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new CalloutLogEntry(
                    calloutReportDate.getText(),
                    calloutReportTime.getText(),
                    calloutReportName.getText(),
                    calloutReportRank.getText(),
                    calloutReportNumber.getText(),
                    calloutReportDivision.getText(),
                    calloutReportAgency.getText(),
                    calloutReportResponeType.getText(),
                    calloutReportResponseGrade.getText(),
                    calloutReportSpinner.getValue().toString(),
                    calloutReportNotesTextArea.getText(),
                    calloutReportResponseAddress.getText(),
                    calloutReportResponseCounty.getText(),
                    calloutReportResponseArea.getText()

            ));
            // Save logs to XML
            CalloutReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(calloutReportPane);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            calloutReportDate.setText("");
            calloutReportTime.setText("");
            calloutReportName.setText("");
            calloutReportRank.setText("");
            calloutReportNumber.setText("");
            calloutReportDivision.setText("");
            calloutReportAgency.setText("");
            calloutReportResponeType.setText("");
            calloutReportResponseGrade.setText("");
            calloutReportSpinner.getEditor().setText("0");
            calloutReportNotesTextArea.setText("");
            calloutReportResponseAddress.setText("");
            calloutReportResponseCounty.setText("");
            calloutReportResponseArea.setText("");
            showNotification("Reports", "A new Callout Report has been submitted.", vbox);
        }
    }

    @javafx.fxml.FXML
    public void onPatrolReportSubmitBtnClick(ActionEvent actionEvent) {
        if (patrolSpinnerNumber.getValue() == null) {
            patrolincompleteLabel.setText("Fill Out Form.");
            patrolincompleteLabel.setStyle("-fx-text-fill: red;");
            patrolincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                patrolincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new PatrolLogEntry(
                    patrolSpinnerNumber.getValue().toString(),
                    patrolDate.getText(),
                    patrolLength.getText(),
                    patrolStartTime.getText(),
                    patrolStopTime.getText(),
                    patrolofficerRank.getText(),
                    patrolofficerName.getText(),
                    patrolofficerNumber.getText(),
                    patrolofficerDivision.getText(),
                    patrolofficerAgency.getText(),
                    patrolofficerVehicle.getText(),
                    patrolComments.getText()
            ));
            // Save logs to XML
            PatrolReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(patrolReportPane);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            showNotification("Reports", "A new Patrol Report has been submitted.", vbox);
            patrolSpinnerNumber.getEditor().setText("0");
            patrolDate.setText("");
            patrolLength.setText("");
            patrolStartTime.setText("");
            patrolStopTime.setText("");
            patrolofficerRank.setText("");
            patrolofficerName.setText("");
            patrolofficerNumber.setText("");
            patrolofficerDivision.setText("");
            patrolofficerAgency.setText("");
            patrolofficerVehicle.setText("");
            patrolComments.setText("");
        }
    }

    @javafx.fxml.FXML
    public void onIncidentReportSubmitBtnClick(ActionEvent actionEvent) {
        if (incidentNumber.getValue() == null) {
            incidentincompleteLabel.setText("Fill Out Form.");
            incidentincompleteLabel.setStyle("-fx-text-fill: red;");
            incidentincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incidentincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new IncidentLogEntry(
                    incidentNumber.getValue().toString(),
                    incidentReportdate.getText(),
                    incidentReportTime.getText(),
                    incidentStatement.getText(),
                    incidentWitnesses.getText(),
                    incidentVictims.getText(),
                    incidentofficerName.getText(),
                    incidentofficerRank.getText(),
                    incidentofficerNumber.getText(),
                    incidentofficerAgency.getText(),
                    incidentofficerDivision.getText(),
                    incidentStreet.getText(),
                    incidentArea.getText(),
                    incidentCounty.getText(),
                    incidentActionsTaken.getText(),
                    incidentComments.getText()
            ));

            // Save logs to XML
            IncidentReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(incidentReportPane);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            showNotification("Reports", "A new Incident Report has been submitted.", vbox);
            incidentNumber.getEditor().setText("0");
            incidentReportdate.setText("");
            incidentReportTime.setText("");
            incidentStatement.setText("");
            incidentWitnesses.setText("");
            incidentVictims.setText("");
            incidentofficerName.setText("");
            incidentofficerRank.setText("");
            incidentofficerNumber.setText("");
            incidentofficerAgency.setText("");
            incidentofficerDivision.setText("");
            incidentStreet.setText("");
            incidentArea.setText("");
            incidentCounty.setText("");
            incidentActionsTaken.setText("");
            incidentComments.setText("");
        }
    }

    @javafx.fxml.FXML
    public void onSearchReportSubmitBtnClick(ActionEvent actionEvent) {
        if (SearchNumber.getValue() == null
                || searchType.getSelectionModel().isEmpty() || searchType.getValue() == null
                || searchMethod.getSelectionModel().isEmpty() || searchMethod.getValue() == null) {
            searchincompleteLabel.setText("Fill Out Form.");
            searchincompleteLabel.setStyle("-fx-text-fill: red;");
            searchincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                searchincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();
            String BreathalyzerUsed;
            String ResultPassFail;
            String BACMeasurement = duiInformationController.getBacMeasure().getText();

            if (duiInformationController.getBreathalyzerUsedNo().isSelected()) {
                BreathalyzerUsed = "no";
                ResultPassFail = "n/a";
                BACMeasurement = "n/a";
            } else {
                BreathalyzerUsed = "yes";
                if (duiInformationController.getBreathalyzerResultFail().isSelected()) {
                    ResultPassFail = "Failed";
                } else {
                    ResultPassFail = "Passed";
                }
            }

            // Add new entry
            logs.add(new SearchLogEntry(
                    SearchNumber.getValue().toString(),
                    searchedPersons.getText(),
                    searchDate.getText(),
                    searchTime.getText(),
                    searchSeizedItems.getText(),
                    searchGrounds.getText(),
                    searchType.getValue().toString(),
                    searchMethod.getValue().toString(),
                    searchWitnesses.getText(),
                    searchofficerRank.getText(),
                    searchofficerName.getText(),
                    searchofficerNumber.getText(),
                    searchofficerAgency.getText(),
                    searchofficerDivision.getText(),
                    searchStreet.getText(),
                    searchArea.getText(),
                    searchCounty.getText(),
                    searchComments.getText(),
                    BreathalyzerUsed,
                    ResultPassFail,
                    BACMeasurement
            ));

            // Save logs to XML
            SearchReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(searchReportPane);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            SearchNumber.getEditor().setText("0");
            searchedPersons.setText("");
            searchDate.setText("");
            searchTime.setText("");
            searchSeizedItems.setText("");
            searchGrounds.setText("");
            searchWitnesses.setText("");
            searchofficerRank.setText("");
            searchofficerName.setText("");

            searchType.getSelectionModel().selectFirst();
            searchMethod.getSelectionModel().selectFirst();

            searchofficerNumber.setText("");
            searchofficerAgency.setText("");
            searchofficerDivision.setText("");
            searchStreet.setText("");
            searchArea.setText("");
            searchCounty.setText("");
            searchComments.setText("");
            showNotification("Reports", "A new Search Report has been submitted.", vbox);
        }
    }

    @javafx.fxml.FXML
    public void onImpoundReportSubmitBtnClick(ActionEvent actionEvent) {
        if (impoundNumber.getValue() == null ||
                impoundType.getValue() == null ||
                impoundColor.getValue() == null) {
            impoundincompleteLabel.setText("Fill Out Form.");
            impoundincompleteLabel.setStyle("-fx-text-fill: red;");
            impoundincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                impoundincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new ImpoundLogEntry(
                    impoundNumber.getValue().toString(),
                    impoundDate.getText(),
                    impoundTime.getText(),
                    impoundownerName.getText(),
                    impoundownerAge.getText(),
                    impoundownerGender.getText(),
                    impoundownerAddress.getText(),
                    impoundPlateNumber.getText(),
                    impoundModel.getText(),
                    impoundType.getValue().toString(),
                    impoundColor.getValue().toString(),
                    impoundComments.getText(),
                    impoundofficerRank.getText(),
                    impoundofficerName.getText(),
                    impoundofficerNumber.getText(),
                    impoundofficerDivision.getText(),
                    impoundofficerAgency.getText()
            ));
            // Save logs to XML
            ImpoundReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(impoundReportPane);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            impoundNumber.getEditor().setText("0");
            impoundDate.setText("");
            impoundTime.setText("");
            impoundownerName.setText("");
            impoundownerAge.setText("");
            impoundownerGender.setText("");
            impoundownerAddress.setText("");
            impoundPlateNumber.setText("");
            impoundModel.setText("");
            impoundComments.setText("");

            impoundType.getSelectionModel().selectFirst();
            impoundColor.getSelectionModel().selectFirst();

            impoundofficerRank.setText("");
            impoundofficerName.setText("");
            impoundofficerNumber.setText("");
            impoundofficerDivision.setText("");
            impoundofficerAgency.setText("");
            showNotification("Reports", "A new Impound Report has been submitted.", vbox);
        }
    }

    @javafx.fxml.FXML
    public void onTrafficStopReportSubmitBtnClick(ActionEvent actionEvent) {
        if (trafficStopColor.getSelectionModel().isEmpty() || trafficStopColor.getValue() == null
                || trafficStopType.getSelectionModel().isEmpty() || trafficStopType.getValue() == null
                || trafficStopNumber.getValue() == null) {
            trafficStopIncompleteLabel.setText("Fill Out Form.");
            trafficStopIncompleteLabel.setStyle("-fx-text-fill: red;");
            trafficStopIncompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                trafficStopIncompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<TrafficStopLogEntry> logs = TrafficStopReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new TrafficStopLogEntry(
                    trafficStopDate.getText(),
                    trafficStopTime.getText(),
                    trafficStopModel.getText(),
                    trafficStopOtherInfo.getText(),
                    trafficStopownerName.getText(),
                    trafficStopownerAge.getText(),
                    trafficStopownerAddress.getText(),
                    trafficStopownerDescription.getText(),
                    trafficStopownerGender.getText(),
                    trafficStopofficerName.getText(),
                    trafficStopofficerRank.getText(),
                    trafficStopofficerNumber.getText(),
                    trafficStopofficerDivision.getText(),
                    trafficStopofficerAgency.getText(),
                    trafficStopNumber.getValue().toString(),
                    trafficStopComments.getText(),
                    trafficStopStreet.getText(),
                    trafficStopCounty.getText(),
                    trafficStopArea.getText(),
                    trafficStopPlateNumber.getText(),
                    trafficStopColor.getValue().toString(),
                    trafficStopType.getValue().toString()
            ));
            // Save logs to XML
            TrafficStopReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(trafficStopReportPane);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            trafficStopDate.setText("");
            trafficStopTime.setText("");
            trafficStopModel.setText("");
            trafficStopOtherInfo.setText("");
            trafficStopownerName.setText("");
            trafficStopownerAge.setText("");
            trafficStopownerAddress.setText("");
            trafficStopownerDescription.setText("");
            trafficStopownerGender.setText("");
            trafficStopofficerName.setText("");
            trafficStopofficerRank.setText("");
            trafficStopofficerNumber.setText("");
            trafficStopofficerDivision.setText("");
            trafficStopofficerAgency.setText("");
            trafficStopNumber.getEditor().setText("0");
            trafficStopComments.setText("");
            trafficStopStreet.setText("");
            trafficStopCounty.setText("");
            trafficStopArea.setText("");
            trafficStopPlateNumber.setText("");
            trafficStopColor.getSelectionModel().selectFirst();
            trafficStopType.getSelectionModel().selectFirst();
            showNotification("Reports", "A new Traffic Stop Report has been submitted.", vbox);
        }
    }


    //</editor-fold>


    //<editor-fold desc="Misc.">

    @javafx.fxml.FXML
    public void onArrestReportSubmitBtnClick(ActionEvent actionEvent) {
        if (arrestNumber.getValue() == null) {
            arrestIncompleteLabel.setText("Fill Out Form.");
            arrestIncompleteLabel.setStyle("-fx-text-fill: red;");
            arrestIncompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                arrestIncompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();
            String TaserYesNo;
            String AmbulanceYesNo;
            if (medicalInformationController.getTaserNo().isSelected()) {
                TaserYesNo = "no";
            } else {
                TaserYesNo = "yes";
            }
            if (medicalInformationController.getAmbulanceNo().isSelected()) {
                AmbulanceYesNo = "no";
            } else {
                AmbulanceYesNo = "yes";
            }

            ObservableList<ChargesData> formDataList = arrestChargeTable.getItems();
            // StringBuilder to build the string with commas
            StringBuilder stringBuilder = new StringBuilder();
            // Iterate through formDataList to access each FormData object
            for (ChargesData formData : formDataList) {
                // Append the values to the StringBuilder, separated by commas
                stringBuilder.append(formData.getCharge()).append(" | ");
                // Add more values as needed, separated by commas
            }
            // Remove the trailing comma and space
            if (stringBuilder.length() > 0) {
                stringBuilder.setLength(stringBuilder.length() - 2);
            }

            // Add new entry
            logs.add(new ArrestLogEntry(
                    arrestNumber.getValue().toString(),
                    arrestDate.getText(),
                    arrestTime.getText(),
                    stringBuilder.toString(),
                    arrestCounty.getText(),
                    arrestArea.getText(),
                    arrestStreet.getText(),
                    arrestOwnerName.getText(),
                    arrestOwnerAge.getText(),
                    arrestOwnerGender.getText(),
                    arrestOwnerDescription.getText(),
                    AmbulanceYesNo,
                    TaserYesNo,
                    medicalInformationController.getArresteeMedicalInformation().getText(),
                    arrestOwnerAddress.getText(),
                    arrestComments.getText(),
                    arrestOfficerRank.getText(),
                    arrestOfficerName.getText(),
                    arrestOfficerNumber.getText(),
                    arrestOfficerDivision.getText(),
                    arrestOfficerAgency.getText()
            ));
            // Save logs to XML
            ArrestReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(arrestReportPane);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            arrestNumber.getEditor().setText("0");
            arrestDate.setText("");
            arrestTime.setText("");
            arrestChargeTable.getItems().clear();
            /*stringBuilder.setLength(0);*/
            arrestCounty.setText("");
            arrestArea.setText("");
            arrestStreet.setText("");
            arrestOwnerName.setText("");
            arrestOwnerAge.setText("");
            arrestOwnerGender.setText("");
            arrestOwnerDescription.setText("");
            /*AmbulanceYesNo = "";
            TaserYesNo = "";*/
            medicalInformationController.getArresteeMedicalInformation().setText("");
            arrestOwnerAddress.setText("");
            arrestComments.setText("");
            arrestOfficerRank.setText("");
            arrestOfficerName.setText("");
            arrestOfficerNumber.setText("");
            arrestOfficerDivision.setText("");
            arrestOfficerAgency.setText("");
            arrestChargeField.setText("");
            arrestProbationChance.setText("");
            arrestMinMonths.setText("");
            arrestMaxMonths.setText("");
            arrestMinYears.setText("");
            arrestMaxYears.setText("");
            arrestMinSuspension.setText("");
            arrestMaxSuspension.setText("");
            showNotification("Reports", "A new Arrest Report has been submitted.", vbox);
        }
    }

    @javafx.fxml.FXML
    public void onCitationReportSubmitBtnClick(ActionEvent actionEvent) {
        if (citationNumber.getValue() == null
                || citationOwnerVehicleColor.getValue() == null
                || citationOwnerVehicleType.getValue() == null) {
            citationIncompleteLabel.setText("Fill Out Form.");
            citationIncompleteLabel.setStyle("-fx-text-fill: red;");
            citationIncompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                citationIncompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            // Load existing logs from XML
            List<TrafficCitationLogEntry> logs = TrafficCitationReportLogs.loadLogsFromXML();

            ObservableList<CitationsData> formDataList = citationTable.getItems();
            // StringBuilder to build the string with commas
            StringBuilder stringBuilder = new StringBuilder();
            // Iterate through formDataList to access each FormData object
            for (CitationsData formData : formDataList) {
                // Append the values to the StringBuilder, separated by commas
                stringBuilder.append(formData.getCitation()).append(" | ");
                // Add more values as needed, separated by commas
            }
            // Remove the trailing comma and space
            if (stringBuilder.length() > 0) {
                stringBuilder.setLength(stringBuilder.length() - 2);
            }

            // Add new entry
            logs.add(new TrafficCitationLogEntry(
                    citationNumber.getValue().toString(),
                    citationDate.getText(),
                    citationTime.getText(),
                    stringBuilder.toString(),
                    citationCounty.getText(),
                    citationArea.getText(),
                    citationStreet.getText(),
                    citationOwnerName.getText(),
                    citationOwnerGender.getText(),
                    citationOwnerAge.getText(),
                    citationOwnerAddress.getText(),
                    citationOwnerDescription.getText(),
                    citationOwnerVehicleModel.getText(),
                    citationOwnerVehicleColor.getValue().toString(),
                    citationOwnerVehicleType.getValue().toString(),
                    citationOwnerVehiclePlate.getText(),
                    citationOwnerVehicleOther.getText(),
                    citationOfficerRank.getText(),
                    citationOfficerName.getText(),
                    citationOfficerNumber.getText(),
                    citationOfficerDivision.getText(),
                    citationOfficerAgency.getText(),
                    citationComments.getText()
            ));
            // Save logs to XML
            TrafficCitationReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            setDisable(citationReportPane);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            citationNumber.getEditor().setText("0");
            citationDate.setText("");
            citationTime.setText("");
            citationTable.getItems().clear();
            citationCounty.setText("");
            citationArea.setText("");
            citationStreet.setText("");
            citationOwnerName.setText("");
            citationOwnerGender.setText("");
            citationOwnerAge.setText("");
            citationOwnerAddress.setText("");
            citationOwnerDescription.setText("");
            citationOwnerVehicleModel.setText("");
            citationOwnerVehicleColor.getSelectionModel().selectFirst();
            citationOwnerVehicleType.getSelectionModel().selectFirst();
            citationOwnerVehiclePlate.setText("");
            citationOwnerVehicleOther.setText("");
            citationOfficerRank.setText("");
            citationOfficerName.setText("");
            citationOfficerNumber.setText("");
            citationOfficerDivision.setText("");
            citationOfficerAgency.setText("");
            citationComments.setText("");
            citationField.setText("");
            citationFine.setText("");
            citationTable.getItems().clear();
            showNotification("Reports", "A new Citation Report has been submitted.", vbox);
        }
    }

    @javafx.fxml.FXML
    public void onSearchPopOverBtnPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/DUIInformation.fxml"));
        Parent root = loader.load();
        duiInformationController = loader.getController();

        // Create a PopOver and set the content node
        PopOver popOver = new PopOver();
        popOver.setContentNode(root);

        // Optionally configure other properties of the PopOver
        popOver.setDetachable(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
        popOver.cornerRadiusProperty().setValue(23);
        popOver.setFadeInDuration(Duration.seconds(0.5));
        popOver.setFadeOutDuration(Duration.seconds(0.4));
        popOver.setTitle("Breathalyzer Information");
        popOver.setHeaderAlwaysVisible(true);
        popOver.show(searchpopOverBtn);
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
    public void TrafficStopToTrafficCitationBtnClick(ActionEvent actionEvent) throws IOException, ParserConfigurationException, SAXException {
        setActive(citationReportPane);
        citationAccordionInformation.setExpanded(true);
        createSpinner(citationNumber, 0, 9999, 0);
        citationOfficerName.setText(trafficStopofficerName.getText());
        citationOfficerDivision.setText(trafficStopofficerDivision.getText());
        citationOfficerRank.setText(trafficStopofficerRank.getText());
        citationOfficerAgency.setText(trafficStopofficerAgency.getText());
        citationOfficerNumber.setText(trafficStopofficerNumber.getText());
        citationTime.setText(trafficStopTime.getText());
        citationDate.setText(trafficStopDate.getText());
        citationOwnerVehicleType.getSelectionModel().select(trafficStopType.getSelectionModel().getSelectedItem());
        citationOwnerVehicleColor.getSelectionModel().select(trafficStopColor.getSelectionModel().getSelectedItem());
        citationOwnerName.setText(trafficStopownerName.getText());
        citationOwnerAddress.setText(trafficStopownerAddress.getText());
        citationOwnerGender.setText(trafficStopownerGender.getText());
        citationOwnerAge.setText(trafficStopownerAge.getText());
        citationOwnerDescription.setText(trafficStopownerDescription.getText());
        citationArea.setText(trafficStopArea.getText());
        citationCounty.setText(trafficStopCounty.getText());
        citationStreet.setText(trafficStopStreet.getText());
        citationOwnerVehicleModel.setText(trafficStopModel.getText());
        citationOwnerVehiclePlate.setText(trafficStopPlateNumber.getText());
        citationOwnerVehicleOther.setText(trafficStopOtherInfo.getText());
        citationNumber.getEditor().setText(trafficStopNumber.getValue().toString());


        //Tree View
        File file = new File(getJarPath() + "/data/Citations.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().parse(file);

        Element root = document.getDocumentElement();

        TreeItem<String> rootItem = new TreeItem<>(root.getNodeName());

        parseTreeXML(root, rootItem);
        citationTreeView.setRoot(rootItem);
        expandTreeItem(rootItem, "Citations");
    }

    @javafx.fxml.FXML
    public void ArrestToImpoundBtnClick(ActionEvent actionEvent) {
        setActive(impoundReportPane);
        createSpinner(impoundNumber, 0, 9999, 0);
        impoundofficerName.setText(arrestOfficerName.getText());
        impoundofficerDivision.setText(arrestOfficerDivision.getText());
        impoundofficerRank.setText(arrestOfficerRank.getText());
        impoundofficerAgency.setText(arrestOfficerAgency.getText());
        impoundofficerNumber.setText(arrestOfficerNumber.getText());
        impoundTime.setText(arrestTime.getText());
        impoundDate.setText(arrestDate.getText());
        impoundNumber.getEditor().setText(arrestNumber.getValue().toString());
        impoundownerAddress.setText(arrestOwnerAddress.getText());
        impoundownerName.setText(arrestOwnerName.getText());
        impoundownerAge.setText(arrestOwnerAge.getText());
        impoundownerGender.setText(arrestOwnerGender.getText());
    }

    @javafx.fxml.FXML
    public void ArrestToIncidentBtnClick(ActionEvent actionEvent) {
        setActive(incidentReportPane);
        createSpinner(incidentNumber, 0, 9999, 0);
        incidentofficerName.setText(arrestOfficerName.getText());
        incidentofficerDivision.setText(arrestOfficerDivision.getText());
        incidentofficerRank.setText(arrestOfficerRank.getText());
        incidentofficerAgency.setText(arrestOfficerAgency.getText());
        incidentofficerNumber.setText(arrestOfficerNumber.getText());
        incidentReportTime.setText(arrestTime.getText());
        incidentReportdate.setText(arrestDate.getText());
        incidentNumber.getEditor().setText(arrestNumber.getValue().toString());
        incidentCounty.setText(arrestCounty.getText());
        incidentArea.setText(arrestArea.getText());
        incidentStreet.setText(arrestStreet.getText());
    }

    @javafx.fxml.FXML
    public void ArrestToSearchBtnClick(ActionEvent actionEvent) {
        setActive(searchReportPane);
        createSpinner(SearchNumber, 0, 9999, 0);
        searchofficerName.setText(arrestOfficerName.getText());
        searchofficerDivision.setText(arrestOfficerDivision.getText());
        searchofficerRank.setText(arrestOfficerRank.getText());
        searchofficerAgency.setText(arrestOfficerAgency.getText());
        searchofficerNumber.setText(arrestOfficerNumber.getText());
        searchTime.setText(arrestTime.getText());
        searchDate.setText(arrestDate.getText());
        searchedPersons.setText(arrestOwnerName.getText());
        searchCounty.setText(arrestCounty.getText());
        searchArea.setText(arrestArea.getText());
        searchStreet.setText(arrestStreet.getText());
        SearchNumber.getEditor().setText(arrestNumber.getValue().toString());
    }

    @javafx.fxml.FXML
    public void onTreeViewMouseClick(Event event) {
        TreeItem<String> selectedItem = (TreeItem<String>) arrestTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.isLeaf()) {
            arrestChargeField.setText(selectedItem.getValue());
            arrestProbationChance.setText(findXMLValue(selectedItem.getValue(), "probation_chance", "data/Charges.xml"));
            arrestMinYears.setText(findXMLValue(selectedItem.getValue(), "min_years", "data/Charges.xml"));
            arrestMaxYears.setText(findXMLValue(selectedItem.getValue(), "max_years", "data/Charges.xml"));
            arrestMinMonths.setText(findXMLValue(selectedItem.getValue(), "min_months", "data/Charges.xml"));
            arrestMaxMonths.setText(findXMLValue(selectedItem.getValue(), "max_months", "data/Charges.xml"));
            if (findXMLValue(selectedItem.getValue(), "min_years", "data/Charges.xml").isBlank()) {
                arrestMonthsPane.setVisible(true);
                arrestYearsPane.setVisible(false);
            } else {
                arrestMonthsPane.setVisible(false);
                arrestYearsPane.setVisible(true);
            }
            if (findXMLValue(selectedItem.getValue(), "traffic", "data/Charges.xml").matches("true")) {
                arrestMinSuspension.setText(findXMLValue(selectedItem.getValue(), "min_susp", "data/Charges.xml"));
                arrestMaxSuspension.setText(findXMLValue(selectedItem.getValue(), "max_susp", "data/Charges.xml"));
            } else {
                arrestMinSuspension.setText("");
                arrestMaxSuspension.setText("");
            }
        }
    }

    @javafx.fxml.FXML
    public void addChargeBtnPress(ActionEvent actionEvent) {
        String charge = arrestChargeField.getText();
        if (!(charge.isBlank() || charge.isEmpty())) {
            // Create a new FormData object
            ChargesData formData = new ChargesData(charge);
            // Add the FormData object to the TableView
            arrestChargeColumn.setCellValueFactory(new PropertyValueFactory<>("charge"));
            arrestChargeTable.getItems().add(formData);
        }
    }

    @javafx.fxml.FXML
    public void removeChargeBtnPress(ActionEvent actionEvent) {
        // Get the selected item
        ChargesData selectedItem = (ChargesData) arrestChargeTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Remove the selected item from the TableView
            arrestChargeTable.getItems().remove(selectedItem);
        }
    }

    @javafx.fxml.FXML
    public void onArrestMedInfoBtnPress(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("popOvers/medicalInformation.fxml"));
        Parent root = loader.load();
        medicalInformationController = loader.getController();

        // Create a PopOver and set the content node
        PopOver popOver = new PopOver();
        popOver.setContentNode(root);

        // Optionally configure other properties of the PopOver
        popOver.setDetachable(false);
        popOver.setArrowLocation(PopOver.ArrowLocation.BOTTOM_RIGHT);
        popOver.cornerRadiusProperty().setValue(23);
        popOver.setFadeInDuration(Duration.seconds(0.5));
        popOver.setFadeOutDuration(Duration.seconds(0.4));
        popOver.setTitle("Medical Information");
        popOver.setHeaderAlwaysVisible(true);
        popOver.show(arrestMedInfoBtn);
    }

    @javafx.fxml.FXML
    public void removeCitationBtnPress(ActionEvent actionEvent) {
        // Get the selected item
        CitationsData selectedItem = (CitationsData) citationTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Remove the selected item from the TableView
            citationTable.getItems().remove(selectedItem);
        }
    }

    @javafx.fxml.FXML
    public void addCitationBtnPress(ActionEvent actionEvent) {
        String citation = citationField.getText();
        if (!(citation.isBlank() || citation.isEmpty())) {
            // Create a new FormData object
            CitationsData formData = new CitationsData(citation);
            // Add the FormData object to the TableView
            citationColumn.setCellValueFactory(new PropertyValueFactory<>("citation"));
            citationTable.getItems().add(formData);
        }
    }

    @javafx.fxml.FXML
    public void onCitationTreeViewMouseClick(Event event) {
        TreeItem<String> selectedItem = (TreeItem<String>) citationTreeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.isLeaf()) {
            citationField.setText(selectedItem.getValue());
            citationFine.setText(findXMLValue(selectedItem.getValue(), "fine", "data/Citations.xml"));
        }
    }

    @javafx.fxml.FXML
    public void onStartupFullscreenPress(ActionEvent actionEvent) throws IOException {
        if (startupFullscreenToggleBtn.isSelected()) {
            ConfigWriter.configwrite("fullscreenOnStartup", "true");
            startupFullscreenToggleBtn.setSelected(true);
        } else {
            ConfigWriter.configwrite("fullscreenOnStartup", "false");
            startupFullscreenToggleBtn.setSelected(false);
        }
    }

    @javafx.fxml.FXML
    public void CitationToImpoundBtnClick(ActionEvent actionEvent) {
        setActive(impoundReportPane);
        createSpinner(impoundNumber, 0, 9999, 0);
        impoundofficerName.setText(citationOfficerName.getText());
        impoundofficerDivision.setText(citationOfficerDivision.getText());
        impoundofficerRank.setText(citationOfficerRank.getText());
        impoundofficerAgency.setText(citationOfficerAgency.getText());
        impoundofficerNumber.setText(citationOfficerNumber.getText());
        impoundTime.setText(citationTime.getText());
        impoundDate.setText(citationDate.getText());
        impoundownerAddress.setText(citationOwnerAddress.getText());
        impoundownerName.setText(citationOwnerName.getText());
        impoundownerAge.setText(citationOwnerAge.getText());
        impoundownerGender.setText(citationOwnerGender.getText());
        impoundPlateNumber.setText(citationOwnerVehiclePlate.getText());
        impoundModel.setText(citationOwnerVehicleModel.getText());
        impoundType.getSelectionModel().select(citationOwnerVehicleType.getSelectionModel().getSelectedItem());
        impoundColor.getSelectionModel().select(citationOwnerVehicleColor.getSelectionModel().getSelectedItem());
        impoundNumber.getEditor().setText(trafficStopNumber.getValue().toString());
    }

    @javafx.fxml.FXML
    public void TrafficStopToArrestBtnClick(ActionEvent actionEvent) throws ParserConfigurationException, IOException, SAXException {
        setActive(arrestReportPane);
        arrestAccordionInformation.setExpanded(true);
        createSpinner(arrestNumber, 0, 9999, 0);
        arrestOfficerName.setText(trafficStopofficerName.getText());
        arrestOfficerDivision.setText(trafficStopofficerDivision.getText());
        arrestOfficerRank.setText(trafficStopofficerRank.getText());
        arrestOfficerAgency.setText(trafficStopofficerAgency.getText());
        arrestOfficerNumber.setText(trafficStopofficerNumber.getText());
        arrestTime.setText(trafficStopTime.getText());
        arrestDate.setText(trafficStopDate.getText());
        arrestOwnerName.setText(trafficStopownerName.getText());
        arrestOwnerAddress.setText(trafficStopownerAddress.getText());
        arrestOwnerGender.setText(trafficStopownerGender.getText());
        arrestOwnerAge.setText(trafficStopownerAge.getText());
        arrestOwnerDescription.setText(trafficStopownerDescription.getText());
        arrestArea.setText(trafficStopArea.getText());
        arrestCounty.setText(trafficStopCounty.getText());
        arrestStreet.setText(trafficStopStreet.getText());
        arrestNumber.getEditor().setText(trafficStopNumber.getValue().toString());


        //Tree View
        File file = new File(getJarPath() + "/data/Charges.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = factory.newDocumentBuilder().parse(file);

        Element root = document.getDocumentElement();

        TreeItem<String> rootItem = new TreeItem<>(root.getNodeName());

        parseTreeXML(root, rootItem);
        arrestTreeView.setRoot(rootItem);
        expandTreeItem(rootItem, "Charges");
    }


    //</editor-fold>


    //<editor-fold desc="PullFromNotes">

    @javafx.fxml.FXML
    public void pullFromNotesCitation(ActionEvent actionEvent) {
        updateTextFromNotepad(citationArea, notesViewController.getNotepadTextArea(), "-area");
        updateTextFromNotepad(citationCounty, notesViewController.getNotepadTextArea(), "-county");
        updateTextFromNotepad(citationStreet, notesViewController.getNotepadTextArea(), "-street");
        updateTextFromNotepad(citationOwnerName, notesViewController.getNotepadTextArea(), "-name");
        updateTextFromNotepad(citationOwnerAge, notesViewController.getNotepadTextArea(), "-age");
        updateTextFromNotepad(citationOwnerGender, notesViewController.getNotepadTextArea(), "-gender");
        updateTextFromNotepad(citationOwnerDescription, notesViewController.getNotepadTextArea(), "-description");
        updateTextFromNotepad(citationComments, notesViewController.getNotepadTextArea(), "-comments");
        updateTextFromNotepad(citationOwnerAddress, notesViewController.getNotepadTextArea(), "-address");
        updateTextFromNotepad(citationOwnerVehicleModel, notesViewController.getNotepadTextArea(), "-model");
        updateTextFromNotepad(citationOwnerVehiclePlate, notesViewController.getNotepadTextArea(), "-plate");
        updateTextFromNotepad(citationNumber, notesViewController.getNotepadTextArea(), "-number");
    }

    @javafx.fxml.FXML
    public void pullFromNotesPatrol(ActionEvent actionEvent) {
        updateTextFromNotepad(patrolSpinnerNumber, notesViewController.getNotepadTextArea(), "-number");
        updateTextFromNotepad(patrolComments, notesViewController.getNotepadTextArea(), "-comments");
    }

    @javafx.fxml.FXML
    public void pullFromNotesTrafficStop(ActionEvent actionEvent) {
        updateTextFromNotepad(trafficStopArea, notesViewController.getNotepadTextArea(), "-area");
        updateTextFromNotepad(trafficStopCounty, notesViewController.getNotepadTextArea(), "-county");
        updateTextFromNotepad(trafficStopStreet, notesViewController.getNotepadTextArea(), "-street");
        updateTextFromNotepad(trafficStopownerName, notesViewController.getNotepadTextArea(), "-name");
        updateTextFromNotepad(trafficStopownerAge, notesViewController.getNotepadTextArea(), "-age");
        updateTextFromNotepad(trafficStopownerGender, notesViewController.getNotepadTextArea(), "-gender");
        updateTextFromNotepad(trafficStopownerDescription, notesViewController.getNotepadTextArea(), "-description");
        updateTextFromNotepad(trafficStopComments, notesViewController.getNotepadTextArea(), "-comments");
        updateTextFromNotepad(trafficStopownerAddress, notesViewController.getNotepadTextArea(), "-address");
        updateTextFromNotepad(trafficStopModel, notesViewController.getNotepadTextArea(), "-model");
        updateTextFromNotepad(trafficStopPlateNumber, notesViewController.getNotepadTextArea(), "-plate");
        updateTextFromNotepad(trafficStopNumber, notesViewController.getNotepadTextArea(), "-number");
    }

    @javafx.fxml.FXML
    public void pullFromNotesImpound(ActionEvent actionEvent) {
        updateTextFromNotepad(impoundownerName, notesViewController.getNotepadTextArea(), "-name");
        updateTextFromNotepad(impoundownerAge, notesViewController.getNotepadTextArea(), "-age");
        updateTextFromNotepad(impoundownerGender, notesViewController.getNotepadTextArea(), "-gender");
        updateTextFromNotepad(impoundComments, notesViewController.getNotepadTextArea(), "-comments");
        updateTextFromNotepad(impoundownerAddress, notesViewController.getNotepadTextArea(), "-address");
        updateTextFromNotepad(impoundModel, notesViewController.getNotepadTextArea(), "-model");
        updateTextFromNotepad(impoundPlateNumber, notesViewController.getNotepadTextArea(), "-plate");
        updateTextFromNotepad(impoundNumber, notesViewController.getNotepadTextArea(), "-number");
    }

    @javafx.fxml.FXML
    public void pullFromNotesArrest(ActionEvent actionEvent) {
        updateTextFromNotepad(arrestArea, notesViewController.getNotepadTextArea(), "-area");
        updateTextFromNotepad(arrestCounty, notesViewController.getNotepadTextArea(), "-county");
        updateTextFromNotepad(arrestStreet, notesViewController.getNotepadTextArea(), "-street");
        updateTextFromNotepad(arrestOwnerName, notesViewController.getNotepadTextArea(), "-name");
        updateTextFromNotepad(arrestOwnerAge, notesViewController.getNotepadTextArea(), "-age");
        updateTextFromNotepad(arrestOwnerGender, notesViewController.getNotepadTextArea(), "-gender");
        updateTextFromNotepad(arrestOwnerDescription, notesViewController.getNotepadTextArea(), "-description");
        updateTextFromNotepad(arrestComments, notesViewController.getNotepadTextArea(), "-comments");
        updateTextFromNotepad(arrestOwnerAddress, notesViewController.getNotepadTextArea(), "-address");
        updateTextFromNotepad(arrestNumber, notesViewController.getNotepadTextArea(), "-number");
    }

    @javafx.fxml.FXML
    public void pullFromNotesSearch(ActionEvent actionEvent) {
        updateTextFromNotepad(searchArea, notesViewController.getNotepadTextArea(), "-area");
        updateTextFromNotepad(searchCounty, notesViewController.getNotepadTextArea(), "-county");
        updateTextFromNotepad(searchStreet, notesViewController.getNotepadTextArea(), "-street");
        updateTextFromNotepad(searchedPersons, notesViewController.getNotepadTextArea(), "-name");
        updateTextFromNotepad(searchComments, notesViewController.getNotepadTextArea(), "-comments");
        updateTextFromNotepad(SearchNumber, notesViewController.getNotepadTextArea(), "-number");
        updateTextFromNotepad(searchSeizedItems, notesViewController.getNotepadTextArea(), "-searchitems");
    }

    @javafx.fxml.FXML
    public void pullFromNotesIncident(ActionEvent actionEvent) {
        updateTextFromNotepad(incidentArea, notesViewController.getNotepadTextArea(), "-area");
        updateTextFromNotepad(incidentCounty, notesViewController.getNotepadTextArea(), "-county");
        updateTextFromNotepad(incidentStreet, notesViewController.getNotepadTextArea(), "-street");
        updateTextFromNotepad(incidentVictims, notesViewController.getNotepadTextArea(), "-name");
        updateTextFromNotepad(incidentComments, notesViewController.getNotepadTextArea(), "-comments");
        updateTextFromNotepad(incidentNumber, notesViewController.getNotepadTextArea(), "-number");
    }

    @javafx.fxml.FXML
    public void pullFromNotesCallout(ActionEvent actionEvent) throws IOException {
        updateTextFromNotepad(calloutReportResponseArea, notesViewController.getNotepadTextArea(), "-area");
        updateTextFromNotepad(calloutReportResponseCounty, notesViewController.getNotepadTextArea(), "-county");
        updateTextFromNotepad(calloutReportResponseAddress, notesViewController.getNotepadTextArea(), "-street");
        updateTextFromNotepad(calloutReportSpinner, notesViewController.getNotepadTextArea(), "-number");
        updateTextFromNotepad(calloutReportNotesTextArea, notesViewController.getNotepadTextArea(), "-notes");
    }

    /* EXAMPLE

      -n Jefferson Amato -a 28 -g Male -add 192 Forum Drive -desc Short Blonde Hair, Brown Eyes, tall

      -search Handgun Loaded

      -num 3

      -cnty Blaine -ar Sandy Shores -st 9028 Grapeseed Lane

      -model Infernus -plate PSLD9283

      -cmt additional units were needed, corresponding reports #3

      */


    //</editor-fold>
}