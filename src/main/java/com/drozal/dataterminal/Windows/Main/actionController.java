package com.drozal.dataterminal.Windows.Main;

import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Apps.LogViewController;
import com.drozal.dataterminal.Windows.Other.NotesViewController;
import com.drozal.dataterminal.Windows.Server.ClientController;
import com.drozal.dataterminal.Windows.Settings.settingsController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.controllerUtils;
import com.drozal.dataterminal.util.Misc.dropdownInfo;
import com.drozal.dataterminal.util.Misc.stringUtil;
import com.drozal.dataterminal.util.server.ClientUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static com.drozal.dataterminal.Windows.Other.NotesViewController.notesTabList;
import static com.drozal.dataterminal.logs.Accident.AccidentReportUtils.newAccident;
import static com.drozal.dataterminal.logs.Arrest.ArrestReportUtils.newArrest;
import static com.drozal.dataterminal.logs.Callout.CalloutReportUtils.newCallout;
import static com.drozal.dataterminal.logs.Death.DeathReportUtils.newDeathReport;
import static com.drozal.dataterminal.logs.Impound.ImpoundReportUtils.newImpound;
import static com.drozal.dataterminal.logs.Incident.IncidentReportUtils.newIncident;
import static com.drozal.dataterminal.logs.Patrol.PatrolReportUtils.newPatrol;
import static com.drozal.dataterminal.logs.Search.SearchReportUtils.newSearch;
import static com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportUtils.newTrafficStop;
import static com.drozal.dataterminal.util.Misc.LogUtils.*;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Misc.updateUtil.checkForUpdates;
import static com.drozal.dataterminal.util.Misc.updateUtil.gitVersion;
import static com.drozal.dataterminal.util.Window.windowUtils.centerStageOnMainApp;

public class actionController {
	
	//<editor-fold desc="FXML Elements">
	
	@FXML
	private MenuItem deathReportButton;
	@FXML
	public Button notesButton;
	@FXML
	public Button shiftInfoBtn;
	@FXML
	public AnchorPane shiftInformationPane;
	@FXML
	public TextField OfficerInfoName;
	@FXML
	public ComboBox OfficerInfoDivision;
	@FXML
	public ComboBox OfficerInfoAgency;
	@FXML
	public TextField OfficerInfoCallsign;
	@FXML
	public TextField OfficerInfoNumber;
	@FXML
	public ComboBox OfficerInfoRank;
	@FXML
	public Label generatedDateTag;
	@FXML
	public Label generatedByTag;
	@FXML
	public Label updatedNotification;
	@FXML
	public AnchorPane vbox;
	@FXML
	public BarChart reportChart;
	@FXML
	public AnchorPane sidepane;
	@FXML
	public Label mainColor8;
	@FXML
	public Button updateInfoBtn;
	public static NotesViewController notesViewController;
	actionController controller;
	@FXML
	private Label secondaryColor3Bkg;
	@FXML
	private Label secondaryColor4Bkg;
	@FXML
	private Label secondaryColor5Bkg;
	@FXML
	private Button logsButton;
	@FXML
	private Button mapButton;
	@FXML
	private MenuButton createReportBtn;
	@FXML
	private MenuItem searchReportButton;
	@FXML
	private MenuItem trafficReportButton;
	@FXML
	private MenuItem impoundReportButton;
	@FXML
	private MenuItem incidentReportButton;
	@FXML
	private MenuItem patrolReportButton;
	@FXML
	private MenuItem calloutReportButton;
	@FXML
	private MenuItem arrestReportButton;
	@FXML
	private MenuItem trafficCitationReportButton;
	@FXML
	private AreaChart areaReportChart;
	@FXML
	private Label serverStatusLabel;
	@FXML
	private Button showIDBtn;
	@FXML
	private Button showCalloutBtn;
	@FXML
	private Label versionLabel;
	@FXML
	private VBox bkgclr1;
	@FXML
	private Button showCourtCasesBtn;
	@FXML
	private Button showLookupBtn;
	@FXML
	private MenuItem accidentReportButton;
	@FXML
	private Label locationDataLabel;
	@FXML
	private Circle userCircle;
	@FXML
	private Label userLabel;
	
	//</editor-fold>
	
	public void initialize() throws IOException {
		showLookupBtn.setVisible(true); //todo undo
		showCalloutBtn.setVisible(false);
		showIDBtn.setVisible(false);
		
		locationDataLabel.setVisible(false);
		
		if (ConfigReader.configRead("uiSettings", "firstLogin").equals("true")) {
			ConfigWriter.configwrite("uiSettings", "firstLogin", "false");
			log("First Login...", Severity.DEBUG);
		} else {
			log("Not First Login...", Severity.DEBUG);
		}
		
		checkForUpdates();
		
		setActive(shiftInformationPane);
		
		notesText = "";
		
		refreshChart();
		updateChartIfMismatch(reportChart);
		
		String name = ConfigReader.configRead("userInfo", "Name");
		String division = ConfigReader.configRead("userInfo", "Division");
		String rank = ConfigReader.configRead("userInfo", "Rank");
		String number = ConfigReader.configRead("userInfo", "Number");
		String agency = ConfigReader.configRead("userInfo", "Agency");
		String callsign = ConfigReader.configRead("userInfo", "Callsign");
		
		getOfficerInfoRank().getItems().addAll(dropdownInfo.ranks);
		getOfficerInfoDivision().getItems().addAll(dropdownInfo.divisions);
		getOfficerInfoAgency().getItems().addAll(dropdownInfo.agencies);
		
		OfficerInfoName.setText(name);
		userLabel.setText(String.valueOf(name.charAt(0)).toUpperCase());
		OfficerInfoDivision.setValue(division);
		OfficerInfoRank.setValue(rank);
		OfficerInfoAgency.setValue(agency);
		OfficerInfoNumber.setText(number);
		getOfficerInfoCallsign().setText(callsign);
		
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
							setAlignment(Pos.CENTER);
							
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
		
		ClientUtils.setStatusListener(this::updateConnectionStatus);
		
		Platform.runLater(() -> {
			
			versionLabel.setText(stringUtil.version);
			Stage stge = (Stage) vbox.getScene().getWindow();
			
			stge.setOnHiding(event -> handleClose());
			
			versionLabel.setOnMouseClicked(event -> {
				// todo implemented as an app
				/*if (versionStage != null && versionStage.isShowing()) {
					versionStage.close();
					versionStage = null;
					return;
				}
				versionStage = new Stage();
				versionStage.initStyle(StageStyle.UNDECORATED);
				FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Misc/updates-view.fxml"));
				Parent root = null;
				try {
					root = loader.load();
				} catch (IOException e) {
					logError("Error starting VersionStage: ", e);
				}
				Scene newScene = new Scene(root);
				versionStage.setTitle("Version Information");
				versionStage.setScene(newScene);
				versionStage.setAlwaysOnTop(true);
				
				versionStage.show();
				centerStageOnMainApp(versionStage);
				
				versionStage.setOnHidden(event1 -> versionStage = null);*/
			});
			
			if (!stringUtil.version.equals(gitVersion)) {
				if (gitVersion == null) {
					versionLabel.setText("New Version Available!");
					versionLabel.setStyle("-fx-text-fill: red;");
				} else {
					versionLabel.setText(gitVersion + " Available!");
					versionLabel.setStyle("-fx-text-fill: red;");
				}
			}
			
			try {
				settingsController.loadTheme();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			try {
				if (ConfigReader.configRead("connectionSettings", "serverAutoConnect").equals("true")) {
					Platform.runLater(() -> {
						log("Searching For Server...", Severity.DEBUG);
						new Thread(ClientUtils::listenForServerBroadcasts).start();
					});
				}
			} catch (IOException e) {
				logError("Not able to read serverautoconnect: ", e);
			}
			locationDataLabel.setOnMouseClicked(mouseEvent -> {
				if (locationDataLabel.isVisible()) {
					Clipboard clipboard = Clipboard.getSystemClipboard();
					ClipboardContent content = new ClipboardContent();
					content.putString(locationDataLabel.getText().split(",")[0]);
					clipboard.setContent(content);
				}
			});
		});
		
		if (notesTabList == null) {
			notesTabList = new ArrayList<>();
		}
	}
	
	//<editor-fold desc="Utils">
	
	private void updateConnectionStatus(boolean isConnected) {
		Platform.runLater(() -> {
			if (!isConnected) {
				showLookupBtn.setVisible(false);
				showCalloutBtn.setVisible(false);
				showIDBtn.setVisible(false);
				locationDataLabel.setVisible(false);
				log("No Connection", Severity.WARN);
				serverStatusLabel.setText("No Connection");
				serverStatusLabel.setStyle(
						"-fx-text-fill: #ff5a5a; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
				if (clientController != null) {
					clientController.getPortField().setText("");
					clientController.getInetField().setText("");
					clientController.getStatusLabel().setText("Not Connected");
					clientController.getStatusLabel().setStyle("-fx-background-color: #ff5e5e;");
					serverStatusLabel.setStyle(
							"-fx-text-fill: #ff5e5e; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
				}
			} else {
				showLookupBtn.setVisible(true);
				showCalloutBtn.setVisible(true);
				showIDBtn.setVisible(true);
				serverStatusLabel.setText("Connected");
				
				serverStatusLabel.setStyle(
						"-fx-text-fill: #00da16; -fx-border-color: #665CB6; -fx-label-padding: 5; -fx-border-radius: 5;");
				if (clientController != null) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					clientController.getPortField().setText(ClientUtils.port);
					clientController.getInetField().setText(ClientUtils.inet);
					clientController.getStatusLabel().setText("Connected");
					clientController.getStatusLabel().setStyle("-fx-background-color: green;");
				}
			}
		});
	}
	
	public void refreshChart() throws IOException {
		
		reportChart.getData().clear();
		String[] categories = {"Callout", "Arrests", "Traffic Stops", "Patrols", "Searches", "Incidents", "Impounds", "Citations", "Death Reports", "Accident Reports"};
		CategoryAxis xAxis = (CategoryAxis) getReportChart().getXAxis();
		
		xAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(categories)));
		XYChart.Series<String, Number> series1 = new XYChart.Series<>();
		series1.setName("Series 1");
		
		String color = ConfigReader.configRead("uiColors", "mainColor");
		for (String category : categories) {
			XYChart.Data<String, Number> data = new XYChart.Data<>(category, 1);
			data.nodeProperty().addListener((obs, oldNode, newNode) -> {
				if (newNode != null) {
					newNode.setStyle("-fx-bar-fill: " + color + ";");
				}
			});
			series1.getData().add(data);
		}
		
		getReportChart().getData().add(series1);
	}
	
	//</editor-fold>
	
	//<editor-fold desc="Getters">
	
	public Circle getUserCircle() {
		return userCircle;
	}
	
	public Label getUserLabel() {
		return userLabel;
	}
	
	public Button getShowLookupBtn() {
		return showLookupBtn;
	}
	
	public Label getSecondaryColor5Bkg() {
		return secondaryColor5Bkg;
	}
	
	public Button getShowCourtCasesBtn() {
		return showCourtCasesBtn;
	}
	
	public static Stage getCalloutStage() {
		return CalloutStage;
	}
	
	public static ClientController getClientController() {
		return clientController;
	}
	
	public static Stage getClientStage() {
		return clientStage;
	}
	
	public static Stage getIDStage() {
		return IDStage;
	}
	
	public static int getNeedRefresh() {
		return LogViewController.needRefresh.get();
	}
	
	public static SimpleIntegerProperty needRefreshProperty() {
		return LogViewController.needRefresh;
	}
	
	public static Stage getNotesStage() {
		return notesStage;
	}
	
	public static String getNotesText() {
		return notesText;
	}
	
	public static Stage getSettingsStage() {
		return settingsStage;
	}
	
	public static Stage getVersionStage() {
		return versionStage;
	}
	
	public MenuItem getArrestReportButton() {
		return arrestReportButton;
	}
	
	public MenuItem getCalloutReportButton() {
		return calloutReportButton;
	}
	
	public actionController getController() {
		return controller;
	}
	
	public Label getGeneratedByTag() {
		return generatedByTag;
	}
	
	public Label getGeneratedDateTag() {
		return generatedDateTag;
	}
	
	public MenuItem getImpoundReportButton() {
		return impoundReportButton;
	}
	
	public MenuItem getIncidentReportButton() {
		return incidentReportButton;
	}
	
	public Label getMainColor8() {
		return mainColor8;
	}
	
	public Button getNotesButton() {
		return notesButton;
	}
	
	public NotesViewController getNotesViewController() {
		return notesViewController;
	}
	
	public MenuItem getPatrolReportButton() {
		return patrolReportButton;
	}
	
	public Label getLocationDataLabel() {
		return locationDataLabel;
	}
	
	public MenuItem getSearchReportButton() {
		return searchReportButton;
	}
	
	public Button getShiftInfoBtn() {
		return shiftInfoBtn;
	}
	
	public AnchorPane getShiftInformationPane() {
		return shiftInformationPane;
	}
	
	public AnchorPane getSidepane() {
		return sidepane;
	}
	
	public MenuItem getTrafficCitationReportButton() {
		return trafficCitationReportButton;
	}
	
	public MenuItem getTrafficReportButton() {
		return trafficReportButton;
	}
	
	public Label getUpdatedNotification() {
		return updatedNotification;
	}
	
	public Button getUpdateInfoBtn() {
		return updateInfoBtn;
	}
	
	public AnchorPane getVbox() {
		return vbox;
	}
	
	public Label getVersionLabel() {
		return versionLabel;
	}
	
	public TextField getOfficerInfoCallsign() {
		return OfficerInfoCallsign;
	}
	
	public VBox getBkgclr1() {
		return bkgclr1;
	}
	
	public Label getServerStatusLabel() {
		return serverStatusLabel;
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
	
	public Label getSecondaryColor3Bkg() {
		return secondaryColor3Bkg;
	}
	
	public Label getSecondaryColor4Bkg() {
		return secondaryColor4Bkg;
	}
	
	//</editor-fold>
	
	//<editor-fold desc="VARS">
	
	public static String notesText;
	public static Stage IDStage = null;
	public static Stage settingsStage = null;
	public static Stage CalloutStage = null;
	public static ClientController clientController;
	public static Stage notesStage = null;
	public static Stage clientStage = null;
	private static final Stage versionStage = null;
	public static boolean IDFirstShown = true;
	public static double IDx;
	public static double IDy;
	public static Screen IDScreen = null;
	public static Screen CalloutScreen = null;
	public static boolean CalloutFirstShown = true;
	public static double Calloutx;
	public static double Callouty;
	
	//</editor-fold>
	
	//<editor-fold desc="Events">
	
	@FXML
	public void onLookupBtnClick(ActionEvent actionEvent) throws IOException {
		showAnimation(showLookupBtn);
		setDisable(shiftInformationPane);
	}
	
	@FXML
	public void onShowCourtCasesButtonClick(ActionEvent actionEvent) {
		//todo moved to app
		/*setDisable(lookupPane, courtPane, shiftInformationPane);
		setActive(courtPane);
		showAnimation(showCourtCasesBtn);
		
		blankCourtInfoPane.setVisible(true);
		courtInfoPane.setVisible(false);
		
		loadCaseLabels(caseList);
		caseList.getSelectionModel().clearSelection();*/
	}
	
	@FXML
	public void onShowIDButtonClick(ActionEvent actionEvent) throws IOException {
		if (IDStage != null && IDStage.isShowing()) {
			IDStage.close();
			IDStage = null;
			return;
		}
		IDStage = new Stage();
		IDStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Server/currentID-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		IDStage.setTitle("Current ID");
		IDStage.setScene(newScene);
		
		IDStage.show();
		IDStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTID").equals("true"));
		showAnimation(showIDBtn);
		
		if (ConfigReader.configRead("layout", "rememberIDLocation").equals("true")) {
			if (IDFirstShown) {
				centerStageOnMainApp(IDStage);
				log("IDStage opened via showIDBtn, first time centered", Severity.INFO);
			} else {
				if (IDScreen != null) {
					Rectangle2D screenBounds = IDScreen.getVisualBounds();
					IDStage.setX(IDx);
					IDStage.setY(IDy);
					if (IDx < screenBounds.getMinX() || IDx > screenBounds.getMaxX() || IDy < screenBounds.getMinY() || IDy > screenBounds.getMaxY()) {
						centerStageOnMainApp(IDStage);
					}
				} else {
					centerStageOnMainApp(IDStage);
				}
				log("IDStage opened via showIDBtn, XValue: " + IDx + " YValue: " + IDy, Severity.INFO);
			}
		} else {
			centerStageOnMainApp(IDStage);
		}
		
		IDStage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				IDx = IDStage.getX();
				IDy = IDStage.getY();
				IDScreen = Screen.getScreensForRectangle(IDx, IDy, IDStage.getWidth(),
				                                         IDStage.getHeight()).stream().findFirst().orElse(null);
				log("IDStage closed via showIDBtn, set XValue: " + IDx + " YValue: " + IDy, Severity.DEBUG);
				IDFirstShown = false;
				IDStage = null;
			}
		});
	}
	
	@FXML
	public void onMapButtonClick(ActionEvent actionEvent) {
		// todo removed map
	}
	
	@FXML
	public void onNotesButtonClicked(ActionEvent actionEvent) {
		// todo relocated to an app
		/*if (notesStage != null && notesStage.isShowing()) {
			notesStage.close();
			notesStage = null;
			return;
		}
		
		notesStage = new Stage();
		notesStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Other/notes-view.fxml"));
		Parent root = loader.load();
		notesViewController = loader.getController();
		Scene newScene = new Scene(root);
		notesStage.setTitle("Notes");
		notesStage.setScene(newScene);
		notesStage.setResizable(true);
		
		notesStage.show();
		
		centerStageOnMainApp(notesStage);
		
		String startupValue = ConfigReader.configRead("layout", "notesWindowLayout");
		switch (startupValue) {
			case "TopLeft" -> snapToTopLeft(notesStage);
			case "TopRight" -> snapToTopRight(notesStage);
			case "BottomLeft" -> snapToBottomLeft(notesStage);
			case "BottomRight" -> snapToBottomRight(notesStage);
			case "FullLeft" -> snapToLeft(notesStage);
			case "FullRight" -> snapToRight(notesStage);
			default -> {
				if (ConfigReader.configRead("layout", "rememberNotesLocation").equals("true")) {
					if (NotesFirstShown) {
						centerStageOnMainApp(notesStage);
						log("notesStage opened via showNotesBtn, first time centered", Severity.INFO);
					} else {
						if (NotesScreen != null) {
							Rectangle2D screenBounds = NotesScreen.getVisualBounds();
							notesStage.setX(notesx);
							notesStage.setY(notesy);
							if (notesx < screenBounds.getMinX() || notesx > screenBounds.getMaxX() || notesy < screenBounds.getMinY() || notesy > screenBounds.getMaxY()) {
								centerStageOnMainApp(notesStage);
							}
						} else {
							centerStageOnMainApp(notesStage);
						}
						log("notesStage opened via showNotesBtn, XValue: " + notesx + " YValue: " + notesy,
						    Severity.INFO);
					}
				} else {
					centerStageOnMainApp(notesStage);
					notesStage.setMinHeight(300);
					notesStage.setMinWidth(300);
				}
			}
		}
		notesStage.getScene().getStylesheets().add(
				Objects.requireNonNull(Launcher.class.getResource("css/notification-styles.css")).toExternalForm());
		showAnimation(notesButton);
		notesStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTNotes").equals("true"));
		
		notesStage.setOnHidden(new EventHandler<>() {
			@Override
			public void handle(WindowEvent event) {
				notesx = notesStage.getX();
				notesy = notesStage.getY();
				NotesScreen = Screen.getScreensForRectangle(notesx, notesy, notesStage.getWidth(),
				                                            notesStage.getHeight()).stream().findFirst().orElse(null);
				log("NotesStage closed via showNotesBtn, set XValue: " + notesx + " YValue: " + notesy, Severity.DEBUG);
				NotesFirstShown = false;
				notesStage = null;
				actionController.notesText = notesViewController.getNotepadTextArea().getText();
			}
		});*/
	}
	
	@FXML
	public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
		setActive(shiftInformationPane);
		showAnimation(shiftInfoBtn);
		controllerUtils.refreshChart(areaReportChart, "area");
	}
	
	@FXML
	public void onLogsButtonClick(ActionEvent actionEvent) {
		//todo no longer needed
		setDisable(shiftInformationPane);
	}
	
	@FXML
	public void onCalloutReportButtonClick(ActionEvent actionEvent) {
		newCallout();
	}
	
	@FXML
	public void trafficStopReportButtonClick(ActionEvent actionEvent) {
		newTrafficStop();
	}
	
	@FXML
	public void onIncidentReportBtnClick(ActionEvent actionEvent) {
		newIncident();
	}
	
	@FXML
	public void onSearchReportBtnClick(ActionEvent actionEvent) {
		newSearch();
	}
	
	@FXML
	public void onArrestReportBtnClick(ActionEvent actionEvent) {
		newArrest();
	}
	
	@FXML
	public void onCitationReportBtnClick(ActionEvent actionEvent) {
		newCitation();
	}
	
	@FXML
	public void onPatrolButtonClick(ActionEvent actionEvent) {
		newPatrol();
	}
	
	@FXML
	public void onImpoundReportBtnClick(ActionEvent actionEvent) {
		newImpound();
	}
	
	@FXML
	public void onDeathReportButtonClick(ActionEvent actionEvent) {
		newDeathReport();
	}
	
	@FXML
	public void onServerStatusLabelClick(Event event) throws IOException {
		
		if (clientStage != null && clientStage.isShowing()) {
			clientStage.close();
			clientStage = null;
			return;
		}
		
		if (!ClientUtils.isConnected) {
			clientStage = new Stage();
			clientStage.initStyle(StageStyle.UNDECORATED);
			FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Server/client-view.fxml"));
			Parent root = loader.load();
			Scene newScene = new Scene(root);
			clientStage.setTitle("Client Interface");
			clientStage.setScene(newScene);
			clientStage.initStyle(StageStyle.UNDECORATED);
			clientStage.setResizable(false);
			clientStage.show();
			clientStage.centerOnScreen();
			clientStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTClient").equals("true"));
			
			centerStageOnMainApp(clientStage);
			
			clientStage.setOnHidden(event1 -> {
				clientStage = null;
			});
			
			clientController = loader.getController();
		}
	}
	
	@FXML
	public void updateInfoButtonClick(ActionEvent actionEvent) {
		if (getOfficerInfoAgency().getValue() == null || getOfficerInfoDivision().getValue() == null || getOfficerInfoRank().getValue() == null || getOfficerInfoName().getText().isEmpty() || getOfficerInfoNumber().getText().isEmpty()) {
			updatedNotification.setText("Fill Out Form.");
			updatedNotification.setStyle("-fx-text-fill: red;");
			updatedNotification.setVisible(true);
			Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
				updatedNotification.setVisible(false);
			}));
			timeline1.play();
		} else {
			ConfigWriter.configwrite("userInfo", "Agency", getOfficerInfoAgency().getValue().toString());
			ConfigWriter.configwrite("userInfo", "Division", getOfficerInfoDivision().getValue().toString());
			ConfigWriter.configwrite("userInfo", "Name", getOfficerInfoName().getText());
			ConfigWriter.configwrite("userInfo", "Rank", getOfficerInfoRank().getValue().toString());
			ConfigWriter.configwrite("userInfo", "Number", getOfficerInfoNumber().getText());
			ConfigWriter.configwrite("userInfo", "Callsign", getOfficerInfoCallsign().getText());
			generatedByTag.setText("Generated By:" + " " + getOfficerInfoName().getText());
			updatedNotification.setText("updated.");
			updatedNotification.setStyle("-fx-text-fill: green;");
			updatedNotification.setVisible(true);
			userLabel.setText(String.valueOf(getOfficerInfoName().getText().charAt(0)).toUpperCase());
			Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
				updatedNotification.setVisible(false);
			}));
			timeline.play();
		}
		showAnimation(updateInfoBtn);
	}
	
	@FXML
	public void onShowCalloutButtonClick(ActionEvent actionEvent) {
		// todo moved to callout controller
		/*double toHeight = 0;
		
		Timeline timeline = new Timeline();
		
		KeyValue keyValuePrefHeight = new KeyValue(currentCalPane.prefHeightProperty(), toHeight);
		KeyValue keyValueMaxHeight = new KeyValue(currentCalPane.maxHeightProperty(), toHeight);
		KeyValue keyValueMinHeight = new KeyValue(currentCalPane.minHeightProperty(), toHeight);
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.3), keyValuePrefHeight, keyValueMaxHeight,
		                                 keyValueMinHeight);
		
		timeline.getKeyFrames().add(keyFrame);
		
		timeline.play();
		currentCalPane.setVisible(false);
		
		setDisable(shiftInformationPane, lookupPane, courtPane);
		setActive(calloutPane);
		
		CalloutManager.loadActiveCallouts(calActiveList);
		CalloutManager.loadHistoryCallouts(calHistoryList);*/
	}
	
	@FXML
	public void onAccidentReportButtonClick(ActionEvent actionEvent) {
		newAccident();
	}
	
	@FXML
	public void onSettingsBtnClick(Event event) {
		// todo relocated to an app
		/*if (settingsStage != null && settingsStage.isShowing()) {
			settingsStage.close();
			settingsStage = null;
			return;
		}
		settingsStage = new Stage();
		settingsStage.initStyle(StageStyle.UNDECORATED);
		FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Settings/settings-view.fxml"));
		Parent root = loader.load();
		Scene newScene = new Scene(root);
		settingsStage.setTitle("Settings");
		settingsStage.setScene(newScene);
		settingsStage.show();
		settingsStage.centerOnScreen();
		settingsStage.setAlwaysOnTop(ConfigReader.configRead("AOTSettings", "AOTSettings").equals("true"));
		
		centerStageOnMainApp(settingsStage);
		
		settingsStage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				settingsStage = null;
			}
		});*/
	}
	
	//</editor-fold>
	
}