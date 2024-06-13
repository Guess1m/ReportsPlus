package com.drozal.dataterminal;

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
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.controllerUtils;
import com.drozal.dataterminal.util.Misc.dropdownInfo;
import com.drozal.dataterminal.util.Misc.stringUtil;
import com.drozal.dataterminal.util.Report.reportUtil;
import com.drozal.dataterminal.util.Window.windowUtils;
import com.drozal.dataterminal.util.server.ClientUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
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

import static com.drozal.dataterminal.util.Misc.LogUtils.*;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.Misc.updateUtil.*;
import static com.drozal.dataterminal.util.Report.reportCreationUtil.*;
import static com.drozal.dataterminal.util.Window.windowUtils.*;
import static com.drozal.dataterminal.util.server.recordUtils.grabPedData;
import static com.drozal.dataterminal.util.server.recordUtils.grabVehicleData;

public class actionController {
	
	
	//<editor-fold desc="VARS">
	
	public static String notesText;
	public static SimpleIntegerProperty needRefresh = new SimpleIntegerProperty();
	public static Stage IDStage = null;
	public static Stage settingsStage = null;
	public static Stage CalloutStage = null;
	public static ClientController clientController;
	public static Stage notesStage = null;
	public static Stage clientStage = null;
	static double minColumnWidth = 185.0;
	private static Stage mapStage = null;
	
	
	//</editor-fold>
	
	
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
	actionController controller;
	AnchorPane titlebar;
	private NotesViewController notesViewController;
	@javafx.fxml.FXML
	private Label secondaryColor3Bkg;
	@javafx.fxml.FXML
	private TextField citcounty;
	private CalloutLogEntry calloutEntry;
	private PatrolLogEntry patrolEntry;
	private TrafficStopLogEntry trafficStopEntry;
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
	@javafx.fxml.FXML
	private MenuButton lookupBtn;
	@javafx.fxml.FXML
	private Button settingsBtn;
	@javafx.fxml.FXML
	private TextField pedaddressfield;
	@javafx.fxml.FXML
	private AnchorPane tutorialOverlay;
	
	
	//</editor-fold>
	
	
	//<editor-fold desc="Getters">
	
	public static void handleClose() {
		log("Stop Request Recieved", LogUtils.Severity.DEBUG);
		endLog();
		ClientUtils.disconnectFromService();
		Platform.exit();
		System.exit(0);
	}
	
	public Label getServerStatusLabel() {
		return serverStatusLabel;
	}
	
	public ToggleButton getShowManagerToggle() {
		return showManagerToggle;
	}
	
	public Button getShowIDBtn() {
		return showIDBtn;
	}
	
	public MenuButton getCreateReportBtn() {
		return createReportBtn;
	}
	
	public Button getLogsButton() {
		return logsButton;
	}
	
	public Button getMapButton() {
		return mapButton;
	}
	
	public Button getShowCalloutBtn() {
		return showCalloutBtn;
	}
	
	public MenuButton getLookupBtn() {
		return lookupBtn;
	}
	
	public Button getBtn1() {
		return btn1;
	}
	
	public Button getBtn2() {
		return btn2;
	}
	
	public Button getBtn3() {
		return btn3;
	}
	
	public Button getBtn4() {
		return btn4;
	}
	
	public Button getBtn5() {
		return btn5;
	}
	
	public Button getBtn6() {
		return btn6;
	}
	
	public Button getBtn7() {
		return btn7;
	}
	
	public Button getBtn8() {
		return btn8;
	}
	
	public Button getSettingsBtn() {
		return settingsBtn;
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
	
	public BarChart getReportChart() {
		return reportChart;
	}
	
	public AreaChart getAreaReportChart() {
		return areaReportChart;
	}
	
	public Label getLogManagerLabelBkg() {
		return logManagerLabelBkg;
	}
	
	public Label getDetailsLabelFill() {
		return detailsLabelFill;
	}
	
	public Label getReportPlusLabelFill() {
		return reportPlusLabelFill;
	}
	
	public Label getSecondaryColor3Bkg() {
		return secondaryColor3Bkg;
	}
	
	public Label getSecondaryColor4Bkg() {
		return secondaryColor4Bkg;
	}
	
	
	//</editor-fold>
	
	
	//<editor-fold desc="Events">
	
	public Label getSecondaryColor5Bkg() {
		return secondaryColor5Bkg;
	}
	
	@javafx.fxml.FXML
	public void onShowIDButtonClick(ActionEvent actionEvent) throws IOException {
		if (IDStage != null && IDStage.isShowing()) {
			IDStage.close();
			IDStage = null;
			return;
		}
		IDStage = new Stage();
		IDStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("currentID-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		IDStage.setTitle("Current ID");
		IDStage.setScene(newScene);
		
		IDStage.show();
		IDStage.centerOnScreen();
		IDStage.setAlwaysOnTop(ConfigReader.configRead("AOTID")
		                                   .equals("true"));
		showAnimation(showIDBtn);
		windowUtils.centerStageOnMainApp(IDStage);
		
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
			mapStage.close();
			mapStage = null;
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
		mapStage.setAlwaysOnTop(ConfigReader.configRead("AOTMap")
		                                    .equals("true"));
		showAnimation(mapButton);
		
		windowUtils.centerStageOnMainApp(mapStage);
		
		mapStage.setOnHidden(event -> {
			mapStage = null;
		});
	}
	
	@javafx.fxml.FXML
	public void onNotesButtonClicked(ActionEvent actionEvent) throws IOException {
		if (notesStage != null && notesStage.isShowing()) {
			notesStage.close();
			notesStage = null;
			return;
		}
		
		notesStage = new Stage();
		notesStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("notes-view.fxml"));
		Parent root = loader.load();
		notesViewController = loader.getController();
		Scene newScene = new Scene(root);
		notesStage.setTitle("Notes");
		notesStage.setScene(newScene);
		notesStage.setResizable(true);
		
		notesStage.show();
		
		windowUtils.centerStageOnMainApp(notesStage);
		
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
		notesStage.getScene()
		          .getStylesheets()
		          .add(getClass().getResource("css/notification-styles.css")
		                         .toExternalForm());
		showAnimation(notesButton);
		notesStage.setAlwaysOnTop(ConfigReader.configRead("AOTNotes")
		                                      .equals("true"));
		
		notesStage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				notesStage = null;
				actionController.notesText = notesViewController.getNotepadTextArea()
				                                                .getText();
			}
		});
	}
	
	@javafx.fxml.FXML
	public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
		setDisable(logPane, pedLookupPane, vehLookupPane);
		setActive(shiftInformationPane);
		showAnimation(shiftInfoBtn);
		controllerUtils.refreshChart(areaReportChart, "area");
	}
	
	@javafx.fxml.FXML
	public void onLogsButtonClick(ActionEvent actionEvent) {
		showAnimation(logsButton);
		setDisable(shiftInformationPane, pedLookupPane, vehLookupPane);
		setActive(logPane);
	}
	
	@javafx.fxml.FXML
	public void onShowCalloutButtonClick(ActionEvent actionEvent) throws IOException {
		if (CalloutStage != null && CalloutStage.isShowing()) {
			CalloutStage.close();
			CalloutStage = null;
			return;
		}
		CalloutStage = new Stage();
		CalloutStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("callout-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		CalloutStage.setTitle("Callout Display");
		CalloutStage.setScene(newScene);
		CalloutStage.show();
		CalloutStage.centerOnScreen();
		CalloutStage.setAlwaysOnTop(ConfigReader.configRead("AOTCallout")
		                                        .equals("true"));
		showAnimation(showCalloutBtn);
		
		windowUtils.centerStageOnMainApp(CalloutStage);
	}
	
	@javafx.fxml.FXML
	public void onVehLookupBtnClick(ActionEvent actionEvent) {
		setDisable(logPane, pedLookupPane, shiftInformationPane);
		vehRecordPane.setVisible(false);
		noRecordFoundLabelVeh.setVisible(false);
		setActive(vehLookupPane);
	}
	
	@javafx.fxml.FXML
	public void onPedLookupBtnClick(ActionEvent actionEvent) {
		setDisable(logPane, vehLookupPane, shiftInformationPane);
		pedRecordPane.setVisible(false);
		noRecordFoundLabelPed.setVisible(false);
		setActive(pedLookupPane);
	}
	
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
	
	@javafx.fxml.FXML
	public void onServerStatusLabelClick(Event event) throws IOException {
		
		if (clientStage != null && clientStage.isShowing()) {
			clientStage.close();
			clientStage = null;
			return;
		}
		
		if (!ClientUtils.isConnected) {
			clientStage = new Stage();
			clientStage.initStyle(StageStyle.UNDECORATED);
			FXMLLoader loader = new FXMLLoader(getClass().getResource("client-view.fxml"));
			Parent root = loader.load();
			Scene newScene = new Scene(root);
			clientStage.setTitle("Client Interface");
			clientStage.setScene(newScene);
			clientStage.initStyle(StageStyle.UNDECORATED);
			clientStage.setResizable(false);
			clientStage.show();
			clientStage.centerOnScreen();
			clientStage.setAlwaysOnTop(ConfigReader.configRead("AOTClient")
			                                       .equals("true"));
			
			windowUtils.centerStageOnMainApp(clientStage);
			
			clientStage.setOnHidden(event1 -> {
				clientStage = null;
			});
			
			clientController = loader.getController();
			ClientUtils.setStatusListener(this::updateConnectionStatus);
		}
	}
	
	@javafx.fxml.FXML
	public void updateInfoButtonClick(ActionEvent actionEvent) {
		if (getOfficerInfoAgency().getValue() == null || getOfficerInfoDivision().getValue() == null || getOfficerInfoRank().getValue() == null || getOfficerInfoName().getText()
		                                                                                                                                                               .isEmpty() || getOfficerInfoNumber().getText()
		                                                                                                                                                                                                   .isEmpty()) {
			updatedNotification.setText("Fill Out Form.");
			updatedNotification.setStyle("-fx-text-fill: red;");
			updatedNotification.setVisible(true);
			Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
				updatedNotification.setVisible(false);
			}));
			timeline1.play();
		} else {
			ConfigWriter.configwrite("Agency", getOfficerInfoAgency().getValue()
			                                                         .toString());
			ConfigWriter.configwrite("Division", getOfficerInfoDivision().getValue()
			                                                             .toString());
			ConfigWriter.configwrite("Name", getOfficerInfoName().getText());
			ConfigWriter.configwrite("Rank", getOfficerInfoRank().getValue()
			                                                     .toString());
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
		showAnimation(updateInfoBtn);
	}
	
	@javafx.fxml.FXML
	public void onVehSearchBtnClick(ActionEvent actionEvent) throws IOException {
		String searchedPlate = vehSearchField.getText();
		
		Map<String, String> vehData = grabVehicleData(
				getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldCars.data", searchedPlate);
		
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
		
		Map<String, String> pedData = grabPedData(
				getJarPath() + File.separator + "serverData" + File.separator + "ServerWorldPeds.data", searchedName);
		String gender = pedData.getOrDefault("gender", "Not available");
		String birthday = pedData.getOrDefault("birthday", "Not available");
		String address = pedData.getOrDefault("address", "Not available");
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
			pedaddressfield.setText(address);
			pedwantedfield.setText(isWanted);
			pedlicensefield.setText(licenseStatus);
			
			if (pedlicensefield.getText()
			                   .equals("Expired")) {
				pedlicensefield.setText("EXPIRED");
				pedlicensefield.setStyle("-fx-text-fill: red !important;");
			} else if (pedlicensefield.getText()
			                          .equals("Suspended")) {
				pedlicensefield.setText("SUSPENDED");
				pedlicensefield.setStyle("-fx-text-fill: red !important;");
			} else {
				pedlicensefield.setText("Valid");
				pedlicensefield.setStyle("-fx-text-fill: black !important;");
			}
			
			if (pedwantedfield.getText()
			                  .equals("True")) {
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
	
	
	//<editor-fold desc="Log Methods">
	
	@javafx.fxml.FXML
	public void onSettingsBtnClick(ActionEvent actionEvent) throws IOException {
		if (settingsStage != null && settingsStage.isShowing()) {
			settingsStage.close();
			settingsStage = null;
			return;
		}
		settingsStage = new Stage();
		settingsStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("settings-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		settingsStage.setTitle("Settings");
		settingsStage.setScene(newScene);
		settingsStage.show();
		settingsStage.centerOnScreen();
		settingsStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings")
		                                         .equals("true"));
		showAnimation(settingsBtn);
		
		windowUtils.centerStageOnMainApp(settingsStage);
		
		settingsStage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				settingsStage = null;
			}
		});
	}
	
	private void loadLogs() {
		
		List<ImpoundLogEntry> impoundLogEntryList = ImpoundReportLogs.extractLogEntries(stringUtil.impoundLogURL);
		impoundLogUpdate(impoundLogEntryList);
		
		List<TrafficCitationLogEntry> citationLogEntryList = TrafficCitationReportLogs.extractLogEntries(
				stringUtil.trafficCitationLogURL);
		citationLogUpdate(citationLogEntryList);
		
		List<PatrolLogEntry> patrolLogEntryList = PatrolReportLogs.extractLogEntries(stringUtil.patrolLogURL);
		patrolLogUpdate(patrolLogEntryList);
		
		List<ArrestLogEntry> arrestLogEntryList = ArrestReportLogs.extractLogEntries(stringUtil.arrestLogURL);
		arrestLogUpdate(arrestLogEntryList);
		
		List<SearchLogEntry> searchLogEntryList = SearchReportLogs.extractLogEntries(stringUtil.searchLogURL);
		searchLogUpdate(searchLogEntryList);
		
		List<IncidentLogEntry> incidentLogEntryList = IncidentReportLogs.extractLogEntries(stringUtil.incidentLogURL);
		incidentLogUpdate(incidentLogEntryList);
		
		List<TrafficStopLogEntry> trafficLogEntryList = TrafficStopReportLogs.extractLogEntries(
				stringUtil.trafficstopLogURL);
		trafficStopLogUpdate(trafficLogEntryList);
		
		List<CalloutLogEntry> calloutLogEntryList = CalloutReportLogs.extractLogEntries(stringUtil.calloutLogURL);
		calloutLogUpdate(calloutLogEntryList);
	}
	
	public void impoundLogUpdate(List<ImpoundLogEntry> logEntries) {
		impoundTable.getItems()
		            .clear();
		
		impoundTable.getItems()
		            .addAll(logEntries);
	}
	
	public void citationLogUpdate(List<TrafficCitationLogEntry> logEntries) {
		
		citationTable.getItems()
		             .clear();
		citationTable.getItems()
		             .addAll(logEntries);
	}
	
	public void patrolLogUpdate(List<PatrolLogEntry> logEntries) {
		
		patrolTable.getItems()
		           .clear();
		patrolTable.getItems()
		           .addAll(logEntries);
	}
	
	public void arrestLogUpdate(List<ArrestLogEntry> logEntries) {
		
		arrestTable.getItems()
		           .clear();
		arrestTable.getItems()
		           .addAll(logEntries);
	}
	
	public void searchLogUpdate(List<SearchLogEntry> logEntries) {
		
		searchTable.getItems()
		           .clear();
		searchTable.getItems()
		           .addAll(logEntries);
	}
	
	public void incidentLogUpdate(List<IncidentLogEntry> logEntries) {
		
		incidentTable.getItems()
		             .clear();
		incidentTable.getItems()
		             .addAll(logEntries);
	}
	
	public void trafficStopLogUpdate(List<TrafficStopLogEntry> logEntries) {
		
		trafficStopTable.getItems()
		                .clear();
		trafficStopTable.getItems()
		                .addAll(logEntries);
	}
	
	public void calloutLogUpdate(List<CalloutLogEntry> logEntries) {
		
		calloutTable.getItems()
		            .clear();
		calloutTable.getItems()
		            .addAll(logEntries);
	}
	
	public void initializeImpoundColumns() {
		
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
		
		ObservableList<TableColumn<ImpoundLogEntry, ?>> impoundColumns = FXCollections.observableArrayList(
				impoundNumberColumn, impoundDateColumn, impoundTimeColumn, ownerNameColumn, ownerAgeColumn,
				ownerGenderColumn, ownerAddressColumn, impoundPlateNumberColumn, impoundModelColumn, impoundTypeColumn,
				impoundColorColumn, impoundCommentsColumn, officerRankColumn, officerNameColumn, officerNumberColumn,
				officerDivisionColumn, officerAgencyColumn);
		
		impoundTable.getColumns()
		            .addAll(impoundColumns);
		
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
		
		ObservableList<TableColumn<PatrolLogEntry, ?>> patrolColumns = FXCollections.observableArrayList(
				patrolNumberColumn, patrolDateColumn, patrolLengthColumn, patrolStartTimeColumn, patrolStopTimeColumn,
				officerRankColumn, officerNameColumn, officerNumberColumn, officerDivisionColumn, officerAgencyColumn,
				officerVehicleColumn, patrolCommentsColumn);
		
		patrolTable.getColumns()
		           .addAll(patrolColumns);
		
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
		
		ObservableList<TableColumn<TrafficCitationLogEntry, ?>> citationColumns = FXCollections.observableArrayList(
				citationNumberColumn, citationDateColumn, citationTimeColumn, citationChargesColumn,
				citationCountyColumn, citationAreaColumn, citationStreetColumn, offenderNameColumn,
				offenderGenderColumn, offenderAgeColumn, offenderHomeAddressColumn, offenderDescriptionColumn,
				offenderVehicleModelColumn, offenderVehicleColorColumn, offenderVehicleTypeColumn,
				offenderVehiclePlateColumn, offenderVehicleOtherColumn, officerRankColumn, officerNameColumn,
				officerNumberColumn, officerDivisionColumn, officerAgencyColumn, citationCommentsColumn);
		
		citationTable.getColumns()
		             .addAll(citationColumns);
		
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
		
		ObservableList<TableColumn<ArrestLogEntry, ?>> arrestColumns = FXCollections.observableArrayList(
				arrestNumberColumn, arrestDateColumn, arrestTimeColumn, arrestChargesColumn, arrestCountyColumn,
				arrestAreaColumn, arrestStreetColumn, arresteeNameColumn, arresteeAgeColumn, arresteeGenderColumn,
				arresteeDescriptionColumn, ambulanceYesNoColumn, taserYesNoColumn, arresteeMedicalInformationColumn,
				arresteeHomeAddressColumn, arrestDetailsColumn, officerRankColumn, officerNameColumn,
				officerNumberColumn, officerDivisionColumn, officerAgencyColumn);
		
		arrestTable.getColumns()
		           .addAll(arrestColumns);
		
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
		
		ObservableList<TableColumn<IncidentLogEntry, ?>> incidentColumns = FXCollections.observableArrayList(
				incidentNumberColumn, incidentDateColumn, incidentTimeColumn, incidentStatementColumn,
				incidentWitnessesColumn, incidentVictimsColumn, officerNameColumn, officerRankColumn,
				officerNumberColumn, officerAgencyColumn, officerDivisionColumn, incidentStreetColumn,
				incidentAreaColumn, incidentCountyColumn, incidentActionsTakenColumn, incidentCommentsColumn);
		
		incidentTable.getColumns()
		             .addAll(incidentColumns);
		for (TableColumn<IncidentLogEntry, ?> column : incidentColumns) {
			column.setMinWidth(minColumnWidth);
		}
		setSmallColumnWidth(incidentNumberColumn);
		setSmallColumnWidth(incidentDateColumn);
		setSmallColumnWidth(incidentTimeColumn);
		setSmallColumnWidth(officerNumberColumn);
	}
	
	public void initializeSearchColumns() {
		
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
		
		ObservableList<TableColumn<SearchLogEntry, ?>> searchColumns = FXCollections.observableArrayList(
				searchNumberColumn, searchDateColumn, searchTimeColumn, searchSeizedItemsColumn, searchGroundsColumn,
				searchTypeColumn, searchMethodColumn, searchWitnessesColumn, officerRankColumn, officerNameColumn,
				officerNumberColumn, officerAgencyColumn, officerDivisionColumn, searchStreetColumn, searchAreaColumn,
				searchCountyColumn, searchCommentsColumn, searchedPersonsColumn, testsConductedColumn, resultsColumn,
				BACMeasurementColumn);
		
		searchTable.getColumns()
		           .addAll(searchColumns);
		
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
	
	public void initializeTrafficStopColumns() {
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
		
		ObservableList<TableColumn<TrafficStopLogEntry, ?>> trafficStopColumns = FXCollections.observableArrayList(
				stopNumberColumn, dateColumn, timeColumn, modelColumn, otherInfoColumn, operatorNameColumn,
				operatorAgeColumn, operatorAddressColumn, operatorDescriptionColumn, operatorGenderColumn, nameColumn,
				rankColumn, numberColumn, divisionColumn, agencyColumn, commentsTextAreaColumn, streetColumn,
				countyColumn, areaColumn, plateNumberColumn, colorColumn, typeColumn);
		
		trafficStopTable.getColumns()
		                .addAll(trafficStopColumns);
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
		
		ObservableList<TableColumn<CalloutLogEntry, ?>> columns = FXCollections.observableArrayList(calloutNumberColumn,
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
		                                                                                            areaColumn);
		calloutTable.getColumns()
		            .addAll(columns);
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
			
			double fromHeight = lowerPane.getPrefHeight();
			double toHeight = 0;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(lowerPane.prefHeightProperty(), toHeight);
			KeyValue keyValueMaxHeight = new KeyValue(lowerPane.maxHeightProperty(), toHeight);
			KeyValue keyValueMinHeight = new KeyValue(lowerPane.minHeightProperty(), toHeight);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
			                                 keyValueMinHeight);
			
			timeline.getKeyFrames()
			        .add(keyFrame);
			
			timeline.play();
			lowerPane.setVisible(false);
		} else {
			
			double fromHeight = lowerPane.getPrefHeight();
			double toHeight = 356;
			
			Timeline timeline = new Timeline();
			
			KeyValue keyValuePrefHeight = new KeyValue(lowerPane.prefHeightProperty(), toHeight);
			KeyValue keyValueMaxHeight = new KeyValue(lowerPane.maxHeightProperty(), toHeight);
			KeyValue keyValueMinHeight = new KeyValue(lowerPane.minHeightProperty(), toHeight);
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
			                                 keyValueMinHeight);
			
			timeline.getKeyFrames()
			        .add(keyFrame);
			
			timeline.play();
			lowerPane.setVisible(true);
		}
	}
	
	@javafx.fxml.FXML
	public void onCalloutRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			calloutEntry = (CalloutLogEntry) calloutTable.getSelectionModel()
			                                             .getSelectedItem();
			if (calloutEntry != null) {
				calnum.setText(calloutEntry.getCalloutNumber());
				caladdress.setText(calloutEntry.getAddress());
				calnotes.setText(calloutEntry.getNotesTextArea());
				calcounty.setText(calloutEntry.getCounty());
				calgrade.setText(calloutEntry.getResponseGrade());
				calarea.setText(calloutEntry.getArea());
				caltype.setText(calloutEntry.getResponeType());
				calloutTable.getSelectionModel()
				            .clearSelection();
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
			
			calloutEntry.CalloutNumber = calnum.getText();
			calloutEntry.Address = caladdress.getText();
			calloutEntry.NotesTextArea = calnotes.getText();
			calloutEntry.County = calcounty.getText();
			calloutEntry.ResponseGrade = calgrade.getText();
			calloutEntry.Area = calarea.getText();
			calloutEntry.ResponeType = caltype.getText();
			
			List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();
			
			for (CalloutLogEntry entry : logs) {
				if (entry.getDate()
				         .equals(calloutEntry.getDate()) && entry.getTime()
				                                                 .equals(calloutEntry.getTime())) {
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
			
			CalloutReportLogs.saveLogsToXML(logs);
			
			calloutTable.refresh();
			
		}
	}
	
	@javafx.fxml.FXML
	public void onPatUpdateValues(ActionEvent actionEvent) {
		if (patrolEntry != null) {
			patupdatedlabel.setVisible(true);
			Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
				patupdatedlabel.setVisible(false);
			}));
			timeline1.play();
			
			patrolEntry.patrolNumber = patnum.getText();
			patrolEntry.patrolComments = patcomments.getText();
			patrolEntry.patrolLength = patlength.getText();
			patrolEntry.patrolStartTime = patstarttime.getText();
			patrolEntry.patrolStopTime = patstoptime.getText();
			patrolEntry.officerVehicle = patvehicle.getText();
			
			List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();
			
			for (PatrolLogEntry entry : logs) {
				if (entry.getPatrolDate()
				         .equals(patrolEntry.getPatrolDate())) {
					entry.patrolNumber = patnum.getText();
					entry.patrolComments = patcomments.getText();
					entry.patrolLength = patlength.getText();
					entry.patrolStartTime = patstarttime.getText();
					entry.patrolStopTime = patstoptime.getText();
					entry.officerVehicle = patvehicle.getText();
					
					break;
				}
			}
			
			PatrolReportLogs.saveLogsToXML(logs);
			
			patrolTable.refresh();
			
		}
	}
	
	@javafx.fxml.FXML
	public void onPatrolRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			patrolEntry = (PatrolLogEntry) patrolTable.getSelectionModel()
			                                          .getSelectedItem();
			if (patrolEntry != null) {
				patnum.setText(patrolEntry.getPatrolNumber());
				patcomments.setText(patrolEntry.getPatrolComments());
				patlength.setText(patrolEntry.getPatrolLength());
				patstarttime.setText(patrolEntry.getPatrolStartTime());
				patstoptime.setText(patrolEntry.getPatrolStopTime());
				patvehicle.setText(patrolEntry.getOfficerVehicle());
				patrolTable.getSelectionModel()
				           .clearSelection();
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
			
			List<TrafficStopLogEntry> logs = TrafficStopReportLogs.loadLogsFromXML();
			
			for (TrafficStopLogEntry entry : logs) {
				if (entry.getDate()
				         .equals(trafficStopEntry.getDate()) && entry.getTime()
				                                                     .equals(trafficStopEntry.getTime())) {
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
			
			TrafficStopReportLogs.saveLogsToXML(logs);
			
			trafficStopTable.refresh();
			
		}
	}
	
	@javafx.fxml.FXML
	public void onTrafficStopRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			trafficStopEntry = (TrafficStopLogEntry) trafficStopTable.getSelectionModel()
			                                                         .getSelectedItem();
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
				trafficStopTable.getSelectionModel()
				                .clearSelection();
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
			
			List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();
			
			for (IncidentLogEntry entry : logs) {
				if (entry.getIncidentDate()
				         .equals(incidentEntry.getIncidentDate()) && entry.getIncidentTime()
				                                                          .equals(incidentEntry.getIncidentTime())) {
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
			
			IncidentReportLogs.saveLogsToXML(logs);
			
			incidentTable.refresh();
		}
	}
	
	@javafx.fxml.FXML
	public void onIncidentRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			incidentEntry = (IncidentLogEntry) incidentTable.getSelectionModel()
			                                                .getSelectedItem();
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
				incidentTable.getSelectionModel()
				             .clearSelection();
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
			
			List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();
			
			for (ImpoundLogEntry entry : logs) {
				if (entry.getImpoundDate()
				         .equals(impoundEntry.getImpoundDate()) && entry.getImpoundTime()
				                                                        .equals(impoundEntry.getImpoundTime())) {
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
			
			ImpoundReportLogs.saveLogsToXML(logs);
			
			impoundTable.refresh();
		}
	}
	
	@javafx.fxml.FXML
	public void onImpoundRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			impoundEntry = (ImpoundLogEntry) impoundTable.getSelectionModel()
			                                             .getSelectedItem();
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
				impoundTable.getSelectionModel()
				            .clearSelection();
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
	
	@javafx.fxml.FXML
	public void onCitationRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			citationEntry = (TrafficCitationLogEntry) citationTable.getSelectionModel()
			                                                       .getSelectedItem();
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
				citationTable.getSelectionModel()
				             .clearSelection();
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
			
			List<TrafficCitationLogEntry> logs = TrafficCitationReportLogs.loadLogsFromXML();
			
			for (TrafficCitationLogEntry entry : logs) {
				if (entry.getCitationDate()
				         .equals(citationEntry.getCitationDate()) && entry.getCitationTime()
				                                                          .equals(citationEntry.getCitationTime())) {
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
			
			TrafficCitationReportLogs.saveLogsToXML(logs);
			
			citationTable.refresh();
		}
	}
	
	@javafx.fxml.FXML
	public void onSearchRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			searchEntry = (SearchLogEntry) searchTable.getSelectionModel()
			                                          .getSelectedItem();
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
				searchTable.getSelectionModel()
				           .clearSelection();
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
			
			List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();
			
			for (SearchLogEntry entry : logs) {
				if (entry.getSearchDate()
				         .equals(searchEntry.getSearchDate()) && entry.getSearchTime()
				                                                      .equals(searchEntry.getSearchTime())) {
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
			
			SearchReportLogs.saveLogsToXML(logs);
			
			searchTable.refresh();
		}
	}
	
	@javafx.fxml.FXML
	public void onArrestRowClick(MouseEvent event) {
		if (event.getClickCount() == 1) {
			arrestEntry = (ArrestLogEntry) arrestTable.getSelectionModel()
			                                          .getSelectedItem();
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
				arrestTable.getSelectionModel()
				           .clearSelection();
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
	
	
	//</editor-fold>
	
	
	//<editor-fold desc="Utils">
	
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
			
			List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();
			
			for (ArrestLogEntry entry : logs) {
				if (entry.getArrestDate()
				         .equals(arrestEntry.getArrestDate()) && entry.getArrestTime()
				                                                      .equals(arrestEntry.getArrestTime())) {
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
			
			ArrestReportLogs.saveLogsToXML(logs);
			
			arrestTable.refresh();
		}
	}
	
	private void updateConnectionStatus(boolean isConnected) {
		Platform.runLater(() -> {
			if (!isConnected) {
				LogUtils.log("No Connection", LogUtils.Severity.WARN);
				serverStatusLabel.setText("No Connection");
				serverStatusLabel.setStyle(
						"-fx-text-fill: #ff5a5a; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
				if (clientController != null) {
					clientController.getPortField()
					                .setText("");
					clientController.getInetField()
					                .setText("");
					clientController.getStatusLabel()
					                .setText("Not Connected");
					clientController.getStatusLabel()
					                .setStyle("-fx-background-color: #ff5e5e;");
					serverStatusLabel.setStyle(
							"-fx-text-fill: #ff5e5e; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
				}
			} else {
				serverStatusLabel.setText("Connected");
				
				serverStatusLabel.setStyle(
						"-fx-text-fill: #00da16; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
				if (clientController != null) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					clientController.getPortField()
					                .setText(ClientUtils.port);
					clientController.getInetField()
					                .setText(ClientUtils.inet);
					clientController.getStatusLabel()
					                .setText("Connected");
					clientController.getStatusLabel()
					                .setStyle("-fx-background-color: green;");
				}
			}
		});
	}
	
	public void refreshChart() throws IOException {
		
		reportChart.getData()
		           .clear();
		String[] categories = {"Callout", "Arrests", "Traffic Stops", "Patrols", "Searches", "Incidents", "Impounds", "Citations"};
		CategoryAxis xAxis = (CategoryAxis) getReportChart().getXAxis();
		
		xAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(categories)));
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName("Series 1");
		
		String color = ConfigReader.configRead("mainColor");
		for (String category : categories) {
			XYChart.Data<String, Number> data = new XYChart.Data<>(category, 1);
			data.nodeProperty()
			    .addListener((obs, oldNode, newNode) -> {
				    if (newNode != null) {
					    newNode.setStyle("-fx-bar-fill: " + color + ";");
				    }
			    });
			series1.getData()
			       .add(data);
		}
		
		getReportChart().getData()
		                .add(series1);
	}
	
	private void loadTheme() throws IOException {
		changeBarColors(getReportChart());
		changeStatisticColors(areaReportChart);
		
		String mainclr = ConfigReader.configRead("mainColor");
		topPane.setStyle("-fx-background-color: " + mainclr + ";");
		mainColor8.setStyle("-fx-text-fill: " + mainclr + ";");
		mainColor9Bkg.setStyle("-fx-background-color: " + mainclr + ";");
		logManagerLabelBkg.setStyle("-fx-background-color: " + mainclr + ";");
		detailsLabelFill.setStyle("-fx-text-fill: " + mainclr + ";");
		
		String secclr = ConfigReader.configRead("secondaryColor");
		getServerStatusLabel().setStyle(
				"-fx-border-color: " + secclr + "; -fx-label-padding: 5; -fx-border-radius: 5;");
		sidepane.setStyle("-fx-background-color: " + secclr + ";");
		secondaryColor3Bkg.setStyle("-fx-background-color: " + secclr + ";");
		secondaryColor4Bkg.setStyle("-fx-background-color: " + secclr + ";");
		secondaryColor5Bkg.setStyle("-fx-background-color: " + secclr + ";");
		reportPlusLabelFill.setStyle("-fx-text-fill: " + secclr + ";");
		
		String accclr = ConfigReader.configRead("accentColor");
		
		String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
		String initialStyle = "-fx-background-color: transparent;";
		String nonTransparentBtn = "-fx-background-color: " + accclr + ";";
		updateInfoBtn.setStyle(nonTransparentBtn);
		showManagerToggle.setStyle(nonTransparentBtn);
		settingsBtn.setStyle(initialStyle);
		btn1.setStyle(nonTransparentBtn);
		btn2.setStyle(nonTransparentBtn);
		btn3.setStyle(nonTransparentBtn);
		btn4.setStyle(nonTransparentBtn);
		btn5.setStyle(nonTransparentBtn);
		btn6.setStyle(nonTransparentBtn);
		btn7.setStyle(nonTransparentBtn);
		btn8.setStyle(nonTransparentBtn);
		
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
		settingsBtn.setOnMouseEntered(e -> settingsBtn.setStyle("-fx-background-color: " + secclr + ";"));
		settingsBtn.setOnMouseExited(e -> settingsBtn.setStyle(initialStyle));
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
		showCalloutBtn.setOnMouseEntered(e -> showCalloutBtn.setStyle(hoverStyle));
		showCalloutBtn.setOnMouseExited(e -> showCalloutBtn.setStyle(initialStyle));
		lookupBtn.setOnMouseEntered(e -> lookupBtn.setStyle(hoverStyle));
		lookupBtn.setOnMouseExited(e -> lookupBtn.setStyle(initialStyle));
		
		updateInfoBtn.setOnMouseEntered(e -> updateInfoBtn.setStyle(hoverStyle));
		updateInfoBtn.setOnMouseExited(e -> {
			updateInfoBtn.setStyle(nonTransparentBtn);
		});
	}
	
	
	//</editor-fold>
	
	public void initialize() throws IOException {
		
		if (ConfigReader.configRead("firstLogin")
		                .equals("true")) {
			ConfigWriter.configwrite("firstLogin", "false");
			
			log("First Login, Showing Tutorial", LogUtils.Severity.DEBUG);
			tutorialOverlay.setVisible(true);
			tutorialOverlay.setOnMouseClicked(mouseEvent -> {
				tutorialOverlay.setVisible(false);
			});
		} else {
			tutorialOverlay.setVisible(false);
			log("Not First Login", LogUtils.Severity.DEBUG);
		}
		
		titlebar = reportUtil.createTitleBar("Reports Plus");
		
		vbox.getChildren()
		    .add(titlebar);
		
		AnchorPane.setTopAnchor(titlebar, 0.0);
		AnchorPane.setLeftAnchor(titlebar, 0.0);
		AnchorPane.setRightAnchor(titlebar, 0.0);
		titlebar.setPrefHeight(30);
		
		checkForUpdates();
		
		setDisable(logPane, pedLookupPane, vehLookupPane);
		setActive(shiftInformationPane);
		needRefresh.set(0);
		needRefresh.addListener((obs, oldValue, newValue) -> {
			if (newValue.equals(1)) {
				loadLogs();
				needRefresh.set(0);
			}
		});
		
		notesText = "";
		
		refreshChart();
		updateChartIfMismatch(reportChart);
		
		String name = ConfigReader.configRead("Name");
		String division = ConfigReader.configRead("Division");
		String rank = ConfigReader.configRead("Rank");
		String number = ConfigReader.configRead("Number");
		String agency = ConfigReader.configRead("Agency");
		
		getOfficerInfoRank().getItems()
		                    .addAll(dropdownInfo.ranks);
		getOfficerInfoDivision().getItems()
		                        .addAll(dropdownInfo.divisions);
		getOfficerInfoAgency().getItems()
		                      .addAll(dropdownInfo.agencies);
		
		OfficerInfoName.setText(name);
		OfficerInfoDivision.setValue(division);
		OfficerInfoRank.setValue(rank);
		OfficerInfoAgency.setValue(agency);
		OfficerInfoNumber.setText(number);
		generatedByTag.setText("Generated By:" + " " + name);
		String time = DataTerminalHomeApplication.getTime();
		generatedDateTag.setText("Generated at: " + time);
		
		areaReportChart.getData()
		               .add(parseEveryLog("area"));
		
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
		
		vehSearchField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				try {
					onVehSearchBtnClick(new ActionEvent());
				} catch (IOException e) {
					logError("Error executing vehsearch from Enter: ", e);
				}
			}
		});
		pedSearchField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
			if (event.getCode() == KeyCode.ENTER) {
				try {
					onPedSearchBtnClick(new ActionEvent());
				} catch (IOException e) {
					logError("Error executing pedsearch from Enter: ", e);
				}
			}
		});
		
		
		tabPane.getSelectionModel()
		       .selectedItemProperty()
		       .addListener((obs, oldTab, newTab) -> {
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
			Stage stge = (Stage) vbox.getScene()
			                         .getWindow();
			
			stge.setOnHiding(event -> handleClose());
			
			versionLabel.setOnMouseClicked(event -> openWebpage("https://github.com/Guess1m/ReportsPlus/releases"));
			
			if (!stringUtil.version.equals("dev")) {
				if (!stringUtil.version.equals(gitVersion)) {
					if (gitVersion.equals("null")) {
						versionLabel.setText("New Version Available!");
						versionLabel.setStyle("-fx-text-fill: red;");
					} else {
						versionLabel.setText(gitVersion + " Available!");
						versionLabel.setStyle("-fx-text-fill: red;");
					}
				}
			} else {
				versionLabel.setText("V1.0.2-alpha- Development Version");
			}
		});
		
	}
	
}