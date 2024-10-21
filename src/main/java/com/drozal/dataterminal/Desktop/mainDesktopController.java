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
import jakarta.xml.bind.JAXBException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
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
import static com.drozal.dataterminal.Windows.Apps.CourtViewController.scheduleOutcomeRevealsForPendingCases;
import static com.drozal.dataterminal.Windows.Apps.PedLookupViewController.pedLookupViewController;
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
import static com.drozal.dataterminal.util.Misc.NotificationManager.showNotificationInfo;
import static com.drozal.dataterminal.util.Misc.NotificationManager.showNotificationWarning;
import static com.drozal.dataterminal.util.Misc.controllerUtils.handleClose;
import static com.drozal.dataterminal.util.Misc.updateUtil.checkForUpdates;
import static com.drozal.dataterminal.util.Misc.updateUtil.gitVersion;

public class mainDesktopController {

    public static CustomWindow userManager;
    public static DesktopApp profileAppObj;
    public static DesktopApp calloutManagerAppObj;
    public static DesktopApp courtAppObj;
    public static DesktopApp pedLookupAppObj;
    public static DesktopApp vehLookupAppObj;
    public static DesktopApp connectionAppObj;
    public static DesktopApp showIDAppObj;
    private final ContextMenu reportMenuOptions = createReportMenu();
    @FXML
    private Button button1;
    @FXML
    private HBox taskBarApps;
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
    private BorderPane taskBar;
    @FXML
    private HBox infoHBox;
    @FXML
    private Label infoLabelRight;
    @FXML
    private Label infoLabelLeft;
    @FXML
    private Label officerInfoName;

    public static void updateDesktopBackground(VBox container) {
        if (container != null) {
            try {
                if (ConfigReader.configRead("desktopSettings", "useBackground").equalsIgnoreCase("true")) {
                    String path = ConfigReader.configRead("desktopSettings", "backgroundPath");
                    if (path != null && !path.isEmpty()) {
                        Image image = loadImageFromAbsolutePath(path);
                        log("Loading image from path: " + path, LogUtils.Severity.DEBUG);

                        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(BackgroundSize.AUTO,
                                        BackgroundSize.AUTO,
                                        true, true, false,
                                        false));

                        container.setBackground(new Background(backgroundImage));
                    } else {
                        Image errorImg = new Image(Objects.requireNonNull(
                                Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/IMGNotFound.png")));
                        BackgroundImage errorBkgImg = new BackgroundImage(errorImg, BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(BackgroundSize.AUTO,
                                        BackgroundSize.AUTO, true,
                                        true, false, false));
                        container.setBackground(new Background(errorBkgImg));
                        log("Cant update image, path is empty, showing noImgFound bkg: " + path,
                                LogUtils.Severity.WARN);

                    }
                } else {
                    String color = ConfigReader.configRead("desktopSettings", "desktopColor");
                    BackgroundFill background_fill = new BackgroundFill(Color.web(color), CornerRadii.EMPTY,
                            Insets.EMPTY);

                    Background background = new Background(background_fill);
                    container.setBackground(background);
                }
            } catch (IOException e) {
                logError("Could not get useBackground config setting, ", e);
            }
        }
    }

    public static Image loadImageFromAbsolutePath(String absolutePath) {
        File file = new File(absolutePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("The specified file does not exist: " + absolutePath);
        }
        String imagePath = file.toURI().toString(); // Convert to URI format
        return new Image(imagePath);
    }

    private ContextMenu createReportMenu() {
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
        MenuItem testBtn = new MenuItem("Test Button");

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
        testBtn.setOnAction(event -> {
            showNotificationInfo("Dev", "Test button clicked...");
        });

        reportContextMenu.getItems().addAll(accident, arrest, callout, death, impound, incident, patrol, search,
                trafficCitation, trafficStop, testBtn);

        return reportContextMenu;
    }

    public void initialize() throws IOException {
        infoHBox.setVisible(false);

        button1.setOnAction(event -> {
            editableDesktop = !editableDesktop;
            if (!editableDesktop) {
                for (DesktopApp desktopApp : AppUtils.DesktopApps) {
                    appConfigWrite(desktopApp.getName(), "x", String.valueOf(desktopApp.getX()));
                    appConfigWrite(desktopApp.getName(), "y", String.valueOf(desktopApp.getY()));
                    desktopApp.getMainPane().setStyle("-fx-background-color: transparent;");
                    infoHBox.setVisible(false);
                }
            } else {
                for (DesktopApp desktopApp : AppUtils.DesktopApps) {
                    desktopApp.getMainPane().setStyle("-fx-background-color: rgb(0,0,0,0.25);");
                    infoHBox.setVisible(true);
                    infoLabelLeft.setText("Current Mode: ");
                    infoLabelRight.setText("Editing");
                    infoLabelRight.setStyle("-fx-text-fill: darkred;");
                    infoLabelRight.setUnderline(true);
                }
            }
        });

        NotesViewController.notesText = "";

        updateDesktopBackground(container);

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
                versionLabel.setStyle("-fx-text-fill: darkred;");
            } else {
                versionLabel.setText(gitVersion + " Available!");
                versionLabel.setStyle("-fx-text-fill: darkred;");
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

        Platform.runLater(() -> {
            Stage stge = (Stage) container.getScene().getWindow();

            stge.setOnHiding(event -> handleClose());
        });

        try {
            settingsController.loadTheme();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            if (ConfigReader.configRead("connectionSettings", "serverAutoConnect").equals("true")) {
                log("Searching For Server...", LogUtils.Severity.DEBUG);
                new Thread(ClientUtils::listenForServerBroadcasts).start();
            }
        } catch (IOException e) {
            logError("Not able to read serverautoconnect: ", e);
        }

        try {
            scheduleOutcomeRevealsForPendingCases();
        } catch (JAXBException | IOException e) {
            logError("Error scheduling outcomes for cases: ", e);
        }

        try {
            officerInfoName.setText(ConfigReader.configRead("userInfo", "Name"));
        } catch (IOException e) {
            logError("Unable to read userInfo name from config (2), ", e);
        }

        if (notesTabList == null) {
            notesTabList = new ArrayList<>();
        }

    }

    private void addAppToDesktop(AnchorPane root, VBox newApp, double x, double y) {
        root.getChildren().add(newApp);
        newApp.setTranslateX(x);
        newApp.setTranslateY(y);
    }

    private void addApps() {
        DesktopApp notesAppObj = new DesktopApp("Notes", new Image(Objects.requireNonNull(
                Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/notepad.png"))));
        VBox notesApp = notesAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow mainApp = createFakeWindow(desktopContainer, "Windows/Other/notes-view.fxml", "Notes",
                            true, 1, true, false, taskBarApps, notesAppObj.getImage());
                    if (mainApp != null && mainApp.controller != null) {
                        NotesViewController.notesViewController = (NotesViewController) mainApp.controller;
                    }
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
        VBox settingsApp = settingsAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow settingsWindow = createFakeWindow(desktopContainer,
                            "Windows/Settings/settings-view.fxml",
                            "Program Settings", true, 1, true, false,
                            taskBarApps, settingsAppObj.getImage());
                    if (settingsWindow != null && settingsWindow.controller != null) {
                        settingsController.SettingsController = (settingsController) settingsWindow.controller;
                    }
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
        VBox updatesApp = updatesAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    createFakeWindow(desktopContainer, "Windows/Misc/updates-view.fxml", "Version Information", true, 1,
                            true, false, taskBarApps, updatesAppObj.getImage());
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
        VBox logBrowserApp = logBrowserAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/log-view.fxml", "Log Viewer",
                            true, 1, true, false, taskBarApps,
                            logBrowserAppObj.getImage());
                    if (logapp != null && logapp.controller != null) {
                        LogViewController.logController = (LogViewController) logapp.controller;
                    }
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

        calloutManagerAppObj = new DesktopApp("Callouts", new Image(Objects.requireNonNull(
                Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/callout.png"))));
        VBox calloutManagerApp = calloutManagerAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/callout-view.fxml",
                            "Callout Manager", true, 1, true, false, taskBarApps,
                            calloutManagerAppObj.getImage());
                    if (logapp != null && logapp.controller != null) {
                        CalloutViewController.calloutViewController = (CalloutViewController) logapp.controller;
                    }
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

        courtAppObj = new DesktopApp("CourtCase", new Image(Objects.requireNonNull(
                Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/courtIcon.png"))));
        VBox courtApp = courtAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/court-view.fxml",
                            "Court Case Manager", true, 1, true, false, taskBarApps,
                            courtAppObj.getImage());
                    if (logapp != null && logapp.controller != null) {
                        CourtViewController.courtViewController = (CourtViewController) logapp.controller;
                    }
                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from courtCaseApp", e);
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, courtApp, appConfigRead("CourtCase", "x"), appConfigRead("CourtCase", "y"));

        pedLookupAppObj = new DesktopApp("Ped Lookup", new Image(Objects.requireNonNull(
                Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/ped-search.png"))));
        VBox lookupApp = pedLookupAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/lookup-ped-view.fxml",
                            "Pedestrian Lookup", true, 1, true, false, taskBarApps,
                            pedLookupAppObj.getImage());
                    if (logapp != null && logapp.controller != null) {
                        pedLookupViewController = (PedLookupViewController) logapp.controller;
                    }
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

        vehLookupAppObj = new DesktopApp("Veh Lookup", new Image(Objects.requireNonNull(
                Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/veh-search.png"))));
        VBox vehLookupApp = vehLookupAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = createFakeWindow(desktopContainer, "Windows/Apps/lookup-veh-view.fxml",
                            "Vehicle Lookup", true, 1, true, false, taskBarApps,
                            vehLookupAppObj.getImage());
                    if (logapp != null && logapp.controller != null) {
                        VehLookupViewController.vehLookupViewController = (VehLookupViewController) logapp.controller;
                    }
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

        connectionAppObj = new DesktopApp("Server", new Image(Objects.requireNonNull(
                Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/server.png"))));
        VBox connectionApp = connectionAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow serverApp = createFakeWindow(desktopContainer, "Windows/Server/client-view.fxml",
                            "Server Connection", false, 1, true, false, taskBarApps,
                            connectionAppObj.getImage());
                    if (serverApp != null && serverApp.controller != null) {
                        clientController = (ClientController) serverApp.controller;
                    }
                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from connectionApp", e);
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, connectionApp, appConfigRead("Server", "x"), appConfigRead("Server", "y"));

        showIDAppObj = new DesktopApp("Show IDs", new Image(Objects.requireNonNull(
                Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/license.png"))));
        VBox showIDApp = showIDAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow IDApp = createFakeWindow(desktopContainer, "Windows/Server/currentID-view.fxml",
                            "Current IDs", false, 1, true, true, taskBarApps,
                            showIDAppObj.getImage());
                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from showIDApp", e);
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, showIDApp, appConfigRead("Show IDs", "x"), appConfigRead("Show IDs", "y"));

        profileAppObj = new DesktopApp("Profile", new Image(Objects.requireNonNull(
                Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Apps/profile.png"))));
        VBox profileApp = profileAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    userManager = createFakeWindow(desktopContainer, "Windows/Misc/user-manager.fxml", "Profile", false,
                            1, true, false, taskBarApps, profileAppObj.getImage());
                    userManagerController = (UserManagerController) (userManager != null ? userManager.controller : null);
                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from editUser", e);
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, profileApp, appConfigRead("Profile", "x"), appConfigRead("Profile", "y"));
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
        if (!isConnected) {
            getTopBar().getChildren().remove(locationDataLabel);
            showNotificationWarning("Server Connection", "Server Disconnected");
            log("No Connection", LogUtils.Severity.WARN);
            serverStatusLabel.setText("No Connection");
            serverStatusLabel.setStyle("-fx-text-fill: darkred; -fx-label-padding: 5; -fx-border-radius: 5;");
            if (clientController != null) {
                clientController.getPortField().setText("");
                clientController.getInetField().setText("");
                clientController.getStatusLabel().setText("Not Connected");
                clientController.getStatusLabel().setStyle("-fx-background-color: #ff5e5e;");
                serverStatusLabel.setStyle("-fx-text-fill: darkred; -fx-label-padding: 5; -fx-border-radius: 5;");
            }
        } else {
            serverStatusLabel.setText("Connected");

            showNotificationInfo("Server Connection", "Server Connection Established");
            serverStatusLabel.setStyle("-fx-text-fill: darkgreen; -fx-label-padding: 5; -fx-border-radius: 5;");
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

    public VBox getContainer() {
        return container;
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

    public HBox getInfoHBox() {
        return infoHBox;
    }

    public Label getOfficerInfoName() {
        return officerInfoName;
    }
}