package com.drozal.dataterminal.Desktop;

import com.drozal.dataterminal.Desktop.Utils.AppUtils.DesktopApp;
import com.drozal.dataterminal.Desktop.Utils.WindowUtils.CustomWindow;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.Windows.Apps.CalloutViewController;
import com.drozal.dataterminal.Windows.Apps.CourtViewController;
import com.drozal.dataterminal.Windows.Apps.LogViewController;
import com.drozal.dataterminal.Windows.Apps.LookupViewController;
import com.drozal.dataterminal.Windows.Other.NotesViewController;
import com.drozal.dataterminal.Windows.Server.ClientController;
import com.drozal.dataterminal.Windows.Settings.settingsController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.stringUtil;
import com.drozal.dataterminal.util.server.ClientUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static com.drozal.dataterminal.Desktop.Utils.AppUtils.AppUtils.editableDesktop;
import static com.drozal.dataterminal.Desktop.Utils.WindowUtils.WindowManager.createFakeWindow;
import static com.drozal.dataterminal.Windows.Other.NotesViewController.notesTabList;
import static com.drozal.dataterminal.Windows.Server.ClientController.clientController;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.handleClose;
import static com.drozal.dataterminal.util.Misc.updateUtil.checkForUpdates;
import static com.drozal.dataterminal.util.Misc.updateUtil.gitVersion;

public class mainDesktopController {
	
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
	private VBox taskBarLeftVbox;
	
	private void addAppToDesktop(AnchorPane root, AnchorPane newApp, double x, double y) {
		AnchorPane.setLeftAnchor(newApp, x);
		AnchorPane.setTopAnchor(newApp, y);
		root.getChildren().add(newApp);
	}
	
	public void initialize() throws IOException {
		button1.setOnAction(event -> editableDesktop = !editableDesktop);
		
		NotesViewController.notesText = "";
		
		// todo add ability for custom image
		Image image = new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/desktopBackground.jpg")));
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
		                                                      BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
		                                                      new BackgroundSize(100, 100, true, true, false, true));
		
		container.setBackground(new Background(backgroundImage));
		
		/*todo requires more testing with removing locationDataLabel from parent locationDataLabel.setVisible(false);*/
		taskBarLeftVbox.getChildren().remove(locationDataLabel);
		
		if (ConfigReader.configRead("uiSettings", "firstLogin").equals("true")) {
			ConfigWriter.configwrite("uiSettings", "firstLogin", "false");
			log("First Login...", LogUtils.Severity.DEBUG);
		} else {
			log("Not First Login...", LogUtils.Severity.DEBUG);
		}
		
		DesktopApp notesAppObj = new DesktopApp("Notes", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane notesApp = notesAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow mainApp = createFakeWindow(desktopContainer, "Windows/Other/notes-view.fxml", "Notes",
					                                        true, 2, true, taskBarApps);
					NotesViewController.notesViewController = (NotesViewController) (mainApp != null ? mainApp.controller : null);
				}
			}
		});
		addAppToDesktop(desktopContainer, notesApp, 115, 20);
		
		DesktopApp settingsAppObj = new DesktopApp("Settings", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane settingsApp = settingsAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					createFakeWindow(desktopContainer, "Windows/Settings/settings-view.fxml", "Program Settings", false,
					                 2, true, taskBarApps);
				}
			}
		});
		addAppToDesktop(desktopContainer, settingsApp, 210, 20);
		
		DesktopApp updatesAppObj = new DesktopApp("Updates", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane updatesApp = updatesAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					createFakeWindow(desktopContainer, "Windows/Misc/updates-view.fxml", "Version Information", true, 2,
					                 true, taskBarApps);
				}
			}
		});
		addAppToDesktop(desktopContainer, updatesApp, 305, 20);
		
		DesktopApp createReportAppObj = new DesktopApp("Report\nManager", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane createReportApp = createReportAppObj.createDesktopApp(mouseEvent -> {
		});
		addReportContextMenu(createReportApp); // todo FIX
		addAppToDesktop(desktopContainer, createReportApp, 400, 20);
		
		DesktopApp logBrowserAppObj = new DesktopApp("Log Browser", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane logBrowserApp = logBrowserAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/log-view.fxml", "Log Viewer",
					                                       true, 2, true, taskBarApps);
					LogViewController.logController = (LogViewController) (logapp != null ? logapp.controller : null);
				}
			}
		});
		addAppToDesktop(desktopContainer, logBrowserApp, 590, 20);
		
		DesktopApp calloutManagerAppObj = new DesktopApp("Callouts", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane calloutManagerApp = calloutManagerAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/callout-view.fxml",
					                                       "Callout Manager", true, 2, true, taskBarApps);
					CalloutViewController.calloutViewController = (CalloutViewController) (logapp != null ? logapp.controller : null);
				}
			}
		});
		addAppToDesktop(desktopContainer, calloutManagerApp, 685, 20);
		
		DesktopApp courtAppObj = new DesktopApp("CourtCase", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane courtApp = courtAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/court-view.fxml",
					                                       "Court Case Manager", true, 2, true, taskBarApps);
					CourtViewController.courtViewController = (CourtViewController) (logapp != null ? logapp.controller : null);
				}
			}
		});
		addAppToDesktop(desktopContainer, courtApp, 780, 20);
		
		DesktopApp lookupAppObj = new DesktopApp("Lookups", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane lookupApp = lookupAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/lookup-view.fxml",
					                                       "Database Lookup", true, 2, true, taskBarApps);
					LookupViewController.lookupViewController = (LookupViewController) (logapp != null ? logapp.controller : null);
				}
			}
		});
		addAppToDesktop(desktopContainer, lookupApp, 875, 20);
		
		DesktopApp connectionAppObj = new DesktopApp("Server", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane connectionApp = connectionAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow serverApp = createFakeWindow(desktopContainer, "Windows/Server/client-view.fxml",
					                                          "Server Connection", false, 2, true, taskBarApps);
					clientController = (ClientController) (serverApp != null ? serverApp.controller : null);
				}
			}
		});
		addAppToDesktop(desktopContainer, connectionApp, 970, 20);
		
		DesktopApp showIDAppObj = new DesktopApp("Show IDs", new Image(Objects.requireNonNull(
				Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"))));
		AnchorPane showIDApp = showIDAppObj.createDesktopApp(mouseEvent -> {
			if (!editableDesktop) {
				if (mouseEvent.getClickCount() == 2) {
					CustomWindow IDApp = createFakeWindow(desktopContainer, "Windows/Server/currentID-view.fxml",
					                                      "Current IDs", false, 2, true, taskBarApps);
					
				}
			}
		});
		addAppToDesktop(desktopContainer, showIDApp, 1065, 20);
		
		ClientUtils.setStatusListener(this::updateConnectionStatus);
		
		Platform.runLater(() -> {
			
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
				if (taskBarLeftVbox.getChildren().contains(locationDataLabel)) {
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
			
			checkForUpdates();
			
		});
		if (notesTabList == null) {
			notesTabList = new ArrayList<>();
		}
	}
	
	private void updateConnectionStatus(boolean isConnected) {
		Platform.runLater(() -> {
			if (!isConnected) {
				/* todo find soluation for these being not available when not connected to server
				showLookupBtn.setVisible(false);
				showCalloutBtn.setVisible(false);
				showIDBtn.setVisible(false);*/
				taskBarLeftVbox.getChildren().remove(locationDataLabel);
				
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
				showIDBtn.setVisible(true);*/
				serverStatusLabel.setText("Connected");
				
				serverStatusLabel.setStyle("-fx-text-fill: #00da16; -fx-label-padding: 5; -fx-border-radius: 5;");
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
	
	public AnchorPane getDesktopContainer() {
		return desktopContainer;
	}
	
	public Button getButton1() {
		return button1;
	}
	
	public BorderPane getTaskBar() {
		return taskBar;
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
	
	public VBox getTaskBarLeftVbox() {
		return taskBarLeftVbox;
	}
	
	private static void addReportContextMenu(AnchorPane createReportApp) {
		ContextMenu reportContextMenu = new ContextMenu();
		
		MenuItem editItem = new MenuItem("Edit Report");
		MenuItem deleteItem = new MenuItem("Delete Report");
		MenuItem openItem = new MenuItem("Open Report");
		
		editItem.setOnAction(event -> System.out.println("Edit Report selected"));
		deleteItem.setOnAction(event -> System.out.println("Delete Report selected"));
		openItem.setOnAction(event -> System.out.println("Open Report selected"));
		
		reportContextMenu.getItems().addAll(editItem, deleteItem, openItem);
		
		createReportApp.setOnMouseClicked(mouseEvent -> {
			if (mouseEvent.getClickCount() == 2) {
				reportContextMenu.show(createReportApp, mouseEvent.getScreenX(), mouseEvent.getScreenY());
			}
		});
	}
}