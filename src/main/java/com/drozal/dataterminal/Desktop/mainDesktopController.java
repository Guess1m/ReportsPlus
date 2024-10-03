package com.drozal.dataterminal.Desktop;

import com.drozal.dataterminal.Desktop.Utils.AppUtils.AppUtils;
import com.drozal.dataterminal.Desktop.Utils.AppUtils.DesktopApp;
import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Apps.*;
import com.drozal.dataterminal.Windows.Misc.UserManagerController;
import com.drozal.dataterminal.Windows.Other.NotesViewController;
import com.drozal.dataterminal.Windows.Server.ClientController;
import com.drozal.dataterminal.Windows.Settings.settingsController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.stringUtil;
import com.drozal.dataterminal.util.server.ClientUtils;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppConfig.appConfig.appConfigRead;
import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppConfig.appConfig.appConfigWrite;
import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppUtils.editableDesktop;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.createFakeWindow;
import static com.drozal.dataterminal.Windows.Misc.UserManagerController.userManagerController;
import static com.drozal.dataterminal.Windows.Other.NotesViewController.notesTabList;
import static com.drozal.dataterminal.Windows.Server.ClientController.clientController;
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
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.handleClose;
import static com.drozal.dataterminal.util.Misc.updateUtil.checkForUpdates;
import static com.drozal.dataterminal.util.Misc.updateUtil.gitVersion;

public class mainDesktopController {
	
	private static final ContextMenu reportMenuOptions = createReportMenu();
	public static CustomWindow userManager;
	@FXML
	private Button button1;
	@FXML
	private BorderPane taskBar;
	@FXML
	private HBox taskBarApps;
	@FXML
	private AnchorPane bottomBar;
	@FXML
	private AnchorPane desktopContainer;
	@FXML
	private VBox container;
	@FXML
	private Label locationDataLabel;
	@FXML
	private Label serverStatusLabel;
	@FXML
	private Label versionLabel;
	@FXML
	private Button createReportBtn;
	@FXML
	private AnchorPane topBar;
	@FXML
	private Label timeLabel;
	private DateTimeFormatter timeFormatter;
	private DateTimeFormatter dateFormatter;
	@FXML
	private Label dateLabel;
	@FXML
	private AnchorPane sideMenu;
	@FXML
	private Label sideDivision;
	@FXML
	private Label sideName;
	@FXML
	private Label sideRank;
	@FXML
	private Label sideNumber;
	@FXML
	private Label sideAgency;
	
	private static ContextMenu createReportMenu() {
		ContextMenu reportContextMenu = new ContextMenu();
		
		MenuItem accident = new MenuItem("Accident");
		MenuItem arrest = new MenuItem("Arrest");
		MenuItem callout = new MenuItem("Callout");
		MenuItem death = new MenuItem("Death");
		MenuItem impound = new MenuItem("Impound");
		MenuItem incident = new MenuItem("Incident");
		MenuItem patrol = new MenuItem("Patrol");
		MenuItem search = new MenuItem("Search");
		MenuItem trafficCitation = new MenuItem("Traffic Citation");
		MenuItem trafficStop = new MenuItem("Traffic Stop");
		
		accident.setOnAction(event -> newAccident());
		arrest.setOnAction(event -> newArrest());
		callout.setOnAction(event -> newCallout());
		death.setOnAction(event -> newDeathReport());
		impound.setOnAction(event -> newImpound());
		incident.setOnAction(event -> newIncident());
		patrol.setOnAction(event -> newPatrol());
		search.setOnAction(event -> newSearch());
		trafficCitation.setOnAction(event -> newCitation());
		trafficStop.setOnAction(event -> newTrafficStop());
		
		reportContextMenu.getItems().addAll(accident, arrest, callout, death, impound, incident, patrol, search,
		                                    trafficCitation, trafficStop);
		
		return reportContextMenu;
	}
	
	public void initialize() throws IOException {
		button1.setOnAction(event -> {
			editableDesktop = !editableDesktop;
			if (!editableDesktop) {
				for (DesktopApp desktopApp : AppUtils.DesktopApps) {
					System.out.println(
							"Name: " + desktopApp.getName() + " X: " + desktopApp.getX() + " Y: " + desktopApp.getY());
					appConfigWrite(desktopApp.getName(), "x", String.valueOf(desktopApp.getX()));
					appConfigWrite(desktopApp.getName(), "y", String.valueOf(desktopApp.getY()));
				}
			}
		});
		
		NotesViewController.notesText = "";
		
		// todo add ability for custom image
		Image image = new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/desktopBackground.jpg")));
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
		                                                      BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		                                                      new BackgroundSize(100, 100, true, true, false, true));
		
		container.setBackground(new Background(backgroundImage));
		
		getTopBar().getChildren().remove(locationDataLabel);
		
		if (ConfigReader.configRead("uiSettings", "firstLogin").equals("true")) {
			ConfigWriter.configwrite("uiSettings", "firstLogin", "false");
			log("First Login...", LogUtils.Severity.DEBUG);
		} else {
			log("Not First Login...", LogUtils.Severity.DEBUG);
		}
		
		addApps();
		
		ClientUtils.setStatusListener(this::updateConnectionStatus);
		
		checkForUpdates();
		
		Platform.runLater(() -> {
			
			timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
			dateFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
			
			updateTime();
			updateDate();
			
			Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateTime()),
			                              new KeyFrame(Duration.seconds(1)));
			clock.setCycleCount(Animation.INDEFINITE);
			clock.play();
			
			versionLabel.setText(stringUtil.version);
			if (!stringUtil.version.equals(gitVersion)) {
				if (gitVersion == null) {
					versionLabel.setText("New Version Available!");
					versionLabel.setStyle("-fx-text-fill: red;");
				} else {
					versionLabel.setText(gitVersion + " Available!");
					versionLabel.setStyle("-fx-text-fill: red;");
				}
			}
			locationDataLabel.setOnMouseClicked(mouseEvent -> {
				if (getTopBar().getChildren().contains(locationDataLabel)) {
					Clipboard clipboard = Clipboard.getSystemClipboard();
					ClipboardContent content = new ClipboardContent();
					content.putString(locationDataLabel.getText().split(",")[0]);
					clipboard.setContent(content);
				}
			});
			
			Stage stge = (Stage) container.getScene().getWindow();
			
			stge.setOnHiding(event -> handleClose());
			
			try {
				settingsController.loadTheme();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			
			try {
				if (ConfigReader.configRead("connectionSettings", "serverAutoConnect").equals("true")) {
					Platform.runLater(() -> {
						log("Searching For Server...", LogUtils.Severity.DEBUG);
						new Thread(ClientUtils::listenForServerBroadcasts).start();
					});
				}
			} catch (IOException e) {
				logError("Not able to read serverautoconnect: ", e);
			}
			
		});
		
		if (notesTabList == null) {
			notesTabList = new ArrayList<>();
		}
		
		AnchorPane.setRightAnchor(sideMenu, -180.0);
		sideMenu.setOnMouseEntered(event -> slideMenu(sideMenu, 0));
		sideMenu.setOnMouseExited(event -> slideMenu(sideMenu, -180));
		
		sideName.setText(ConfigReader.configRead("userInfo", "Name"));
		sideAgency.setText(ConfigReader.configRead("userInfo", "Agency"));
		sideNumber.setText(ConfigReader.configRead("userInfo", "Number"));
		sideDivision.setText(ConfigReader.configRead("userInfo", "Division"));
		sideRank.setText(ConfigReader.configRead("userInfo", "Rank"));
	}
	
	private void addAppToDesktop(AnchorPane root, AnchorPane newApp, double x, double y) {
		root.getChildren().add(newApp);
		newApp.setTranslateX(x);
		newApp.setTranslateY(y);
	}
	
	private void addApps() {
		DesktopApp notesAppObj = new DesktopApp("Notes", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/notepad.png"))));
		AnchorPane notesApp = notesAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow mainApp = createFakeWindow(desktopContainer, "Windows/Other/notes-view.fxml", "Notes",
					                                        true, 2, true, taskBarApps);
					NotesViewController.notesViewController = (NotesViewController) (mainApp != null ? mainApp.controller : null);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from notesApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, notesApp, appConfigRead("Notes", "x"), appConfigRead("Notes", "y"));
		
		DesktopApp settingsAppObj = new DesktopApp("Settings", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/setting.png"))));
		AnchorPane settingsApp = settingsAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					createFakeWindow(desktopContainer, "Windows/Settings/settings-view.fxml", "Program Settings", false,
					                 2, true, taskBarApps);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from settingsApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, settingsApp, appConfigRead("Settings", "x"), appConfigRead("Settings", "y"));
		
		DesktopApp updatesAppObj = new DesktopApp("Updates", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/updates.png"))));
		AnchorPane updatesApp = updatesAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					createFakeWindow(desktopContainer, "Windows/Misc/updates-view.fxml", "Version Information", true, 2,
					                 true, taskBarApps);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from updatesApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, updatesApp, appConfigRead("Updates", "x"), appConfigRead("Updates", "y"));
		
		DesktopApp logBrowserAppObj = new DesktopApp("Log Browser", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/logs.png"))));
		AnchorPane logBrowserApp = logBrowserAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/log-view.fxml", "Log Viewer",
					                                       true, 2, true, taskBarApps);
					LogViewController.logController = (LogViewController) (logapp != null ? logapp.controller : null);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from logBrowserApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, logBrowserApp, appConfigRead("Log Browser", "x"),
		                appConfigRead("Log Browser", "y"));
		
		DesktopApp calloutManagerAppObj = new DesktopApp("Callouts", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/callout.png"))));
		AnchorPane calloutManagerApp = calloutManagerAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/callout-view.fxml",
					                                       "Callout Manager", true, 2, true, taskBarApps);
					CalloutViewController.calloutViewController = (CalloutViewController) (logapp != null ? logapp.controller : null);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from calloutManagerApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, calloutManagerApp, appConfigRead("Callouts", "x"),
		                appConfigRead("Callouts", "y"));
		
		DesktopApp courtAppObj = new DesktopApp("CourtCase", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/courtIcon.png"))));
		AnchorPane courtApp = courtAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/court-view.fxml",
					                                       "Court Case Manager", true, 2, true, taskBarApps);
					CourtViewController.courtViewController = (CourtViewController) (logapp != null ? logapp.controller : null);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from courtCaseApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, courtApp, appConfigRead("CourtCase", "x"), appConfigRead("CourtCase", "y"));
		
		DesktopApp pedLookupAppObj = new DesktopApp("Ped Lookup", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/ped-search.png"))));
		AnchorPane lookupApp = pedLookupAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/lookup-ped-view.fxml",
					                                       "Pedestrian Lookup", true, 2, true, taskBarApps);
					PedLookupViewController.pedLookupViewController = (PedLookupViewController) (logapp != null ? logapp.controller : null);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from lookupApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, lookupApp, appConfigRead("Ped Lookup", "x"),
		                appConfigRead("Ped Lookup", "y"));
		
		DesktopApp vehLookupAppObj = new DesktopApp("Veh Lookup", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/veh-search.png"))));
		AnchorPane vehLookupApp = vehLookupAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/lookup-veh-view.fxml",
					                                       "Vehicle Lookup", true, 2, true, taskBarApps);
					VehLookupViewController.vehLookupViewController = (VehLookupViewController) (logapp != null ? logapp.controller : null);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from lookupApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, vehLookupApp, appConfigRead("Veh Lookup", "x"),
		                appConfigRead("Veh Lookup", "y"));
		
		DesktopApp connectionAppObj = new DesktopApp("Server", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/server.png"))));
		AnchorPane connectionApp = connectionAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow serverApp = createFakeWindow(desktopContainer, "Windows/Server/client-view.fxml",
					                                          "Server Connection", false, 2, true, taskBarApps);
					clientController = (ClientController) (serverApp != null ? serverApp.controller : null);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from connectionApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, connectionApp, appConfigRead("Server", "x"), appConfigRead("Server", "y"));
		
		DesktopApp showIDAppObj = new DesktopApp("Show IDs", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/license.png"))));
		AnchorPane showIDApp = showIDAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow IDApp = createFakeWindow(desktopContainer, "Windows/Server/currentID-view.fxml",
					                                      "Current IDs", false, 2, true, taskBarApps);
					try {
						settingsController.loadTheme();
					} catch (IOException e) {
						logError("Error loading theme from showIDApp", e);
					}
				}
			}
		});
		addAppToDesktop(desktopContainer, showIDApp, appConfigRead("Show IDs", "x"), appConfigRead("Show IDs", "y"));
	}
	
	private void updateTime() {
		LocalTime currentTime = LocalTime.now();
		timeLabel.setText(currentTime.format(timeFormatter));
	}
	
	private void updateDate() {
		LocalDate currentDate = LocalDate.now();
		dateLabel.setText(currentDate.format(dateFormatter));
	}
	
	private void updateConnectionStatus(boolean isConnected) {
		Platform.runLater(() -> {
			if (!isConnected) {
				/* todo find soluation for these being not available when not connected to server
				showLookupBtn.setVisible(false);
				showCalloutBtn.setVisible(false);
				showIDBtn.setVisible(false);*/
				getTopBar().getChildren().remove(locationDataLabel);
				
				log("No Connection", LogUtils.Severity.WARN);
				serverStatusLabel.setText("No Connection");
				serverStatusLabel.setStyle("-fx-text-fill: #ff5a5a; -fx-label-padding: 5; -fx-border-radius: 5;");
				if (clientController != null) {
					clientController.getPortField().setText("");
					clientController.getInetField().setText("");
					clientController.getStatusLabel().setText("Not Connected");
					clientController.getStatusLabel().setStyle("-fx-background-color: #ff5e5e;");
					serverStatusLabel.setStyle("-fx-text-fill: #ff5e5e; -fx-label-padding: 5; -fx-border-radius: 5;");
				}
			} else {
				/*showLookupBtn.setVisible(true);
				showCalloutBtn.setVisible(true);
				showIDBtn.setVisible(true); todo find solution*/
				serverStatusLabel.setText("Connected");
				
				serverStatusLabel.setStyle("-fx-text-fill: green; -fx-label-padding: 5; -fx-border-radius: 5;");
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
	
	private void slideMenu(Pane menu, double targetX) {
		Timeline timeline = new Timeline(
				new KeyFrame(Duration.millis(300), event -> AnchorPane.setRightAnchor(menu, targetX)));
		timeline.play();
	}
	
	@FXML
	public void createReportBtn(ActionEvent actionEvent) {
		double btnWidth = createReportBtn.getWidth();
		
		Bounds bounds = createReportBtn.localToScreen(createReportBtn.getBoundsInLocal());
		
		reportMenuOptions.show(createReportBtn, 0, 0);
		reportMenuOptions.hide();
		
		double contextMenuWidth = reportMenuOptions.getWidth();
		double contextMenuHeight = reportMenuOptions.getHeight();
		
		double xPos = bounds.getMinX() + (btnWidth / 2) - (contextMenuWidth / 2);
		double yPos = bounds.getMinY() - contextMenuHeight;
		
		reportMenuOptions.show(createReportBtn, xPos + 10, yPos + 10);
	}
	
	@FXML
	public void editUser(ActionEvent actionEvent) {
		userManager = createFakeWindow(desktopContainer, "Windows/Misc/user-manager.fxml", "User Manager", false, 3,
		                               true, taskBarApps);
		userManagerController = (UserManagerController) (userManager != null ? userManager.controller : null);
		try {
			settingsController.loadTheme();
		} catch (IOException e) {
			logError("Error loading theme from editUser", e);
		}
	}
	
	public AnchorPane getDesktopContainer() {
		return desktopContainer;
	}
	
	public HBox getTaskBarApps() {
		return taskBarApps;
	}
	
	public Label getLocationDataLabel() {
		return locationDataLabel;
	}
	
	public Label getServerStatusLabel() {
		return serverStatusLabel;
	}
	
	public Button getButton1() {
		return button1;
	}
	
	public AnchorPane getTopBar() {
		return topBar;
	}
	
	public AnchorPane getSideMenu() {
		return sideMenu;
	}
	
	public Label getSideAgency() {
		return sideAgency;
	}
	
	public Label getSideDivision() {
		return sideDivision;
	}
	
	public Label getSideName() {
		return sideName;
	}
	
	public Label getSideNumber() {
		return sideNumber;
	}
	
	public Label getSideRank() {
		return sideRank;
	}
}