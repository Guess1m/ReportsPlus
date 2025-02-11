package com.Guess.ReportsPlus.Desktop;

import com.Guess.ReportsPlus.Desktop.Utils.AppUtils.DesktopApp;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.Windows.Apps.*;
import com.Guess.ReportsPlus.Windows.Misc.NewUserManagerController;
import com.Guess.ReportsPlus.Windows.Other.NotesViewController;
import com.Guess.ReportsPlus.Windows.Server.ClientController;
import com.Guess.ReportsPlus.Windows.Settings.settingsController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.config.ConfigWriter;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.Threading.WorkerThread;
import com.Guess.ReportsPlus.util.Server.ClientUtils;
import com.Guess.ReportsPlus.util.Strings.updateStrings;
import jakarta.xml.bind.JAXBException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppConfig.appConfig.appConfigRead;
import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppConfig.appConfig.appConfigWrite;
import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppUtils.DesktopApps;
import static com.Guess.ReportsPlus.Desktop.Utils.AppUtils.AppUtils.editableDesktop;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.getTime;
import static com.Guess.ReportsPlus.Windows.Apps.CourtViewController.scheduleOutcomeRevealsForPendingCases;
import static com.Guess.ReportsPlus.Windows.Apps.PedLookupViewController.pedLookupViewController;
import static com.Guess.ReportsPlus.Windows.Misc.NewUserManagerController.newUserManagerController;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesTabList;
import static com.Guess.ReportsPlus.Windows.Server.ClientController.clientController;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.SettingsController;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.*;
import static com.Guess.ReportsPlus.util.Misc.updateUtil.checkForUpdates;
import static com.Guess.ReportsPlus.util.Misc.updateUtil.gitVersion;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.handleClose;

public class mainDesktopController {
    public static DesktopApp profileAppObj;
    public static DesktopApp reportStatisticsAppObj;
    public static DesktopApp layoutBuilderAppObj;
    public static DesktopApp calloutManagerAppObj;
    public static DesktopApp courtAppObj;
    public static DesktopApp pedLookupAppObj;
    public static DesktopApp vehLookupAppObj;
    public static DesktopApp newReportAppObj;
    public static DesktopApp connectionAppObj;
    public static DesktopApp showIDAppObj;
    public static DesktopApp settingsAppObj;
    public static DesktopApp updatesAppObj;
    public static CustomWindow newReportWindow;
    public static boolean isInputLocked = false;

    @FXML
    private Button button1;
    @FXML
    private HBox taskBarApps;
    @FXML
    private AnchorPane desktopContainer;
    @FXML
    private VBox container;
    @FXML
    private Label serverStatusLabel;
    @FXML
    private Label versionLabel;
    @FXML
    private AnchorPane topBar;
    @FXML
    private Label timeLabel;
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
    @FXML
    private Label topBar2;
    @FXML
    private Label topBar1;
    @FXML
    private HBox topBarHboxRight;
    @FXML
    private GridPane bottomBar;
    @FXML
    private Button shutdownBtn;
    @FXML
    private AnchorPane inputLock;
    @FXML
    private ImageView inputLockButton;
    @FXML
    private BorderPane mainDesktop;
    @FXML
    private HBox locationDataLabel;
    @FXML
    private Label locationCountyLabel;
    @FXML
    private Label locationAreaLabel;
    @FXML
    private Label locationStreetLabel;
    @FXML
    private Label lookupLabel;

    public static void updateDesktopBackground(VBox container) {
        if (container != null) {
            try {
                if (ConfigReader.configRead("desktopSettings", "useBackground").equalsIgnoreCase("true")) {
                    String path = ConfigReader.configRead("desktopSettings", "backgroundPath");
                    if (path != null && !path.isEmpty()) {
                        Image image = loadImageFromAbsolutePath(path);
                        log("Loading image from path: " + path, LogUtils.Severity.DEBUG);

                        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, false));

                        container.setBackground(new Background(backgroundImage));
                    } else {
                        Image errorImg = new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/IMGNotFound.png")));
                        BackgroundImage errorBkgImg = new BackgroundImage(errorImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, false, false));
                        container.setBackground(new Background(errorBkgImg));
                        log("Cant update image, path is empty, showing noImgFound bkg: " + path, LogUtils.Severity.WARN);

                    }
                } else {
                    String color = ConfigReader.configRead("desktopSettings", "desktopColor");
                    BackgroundFill background_fill = new BackgroundFill(Color.web(color), CornerRadii.EMPTY, Insets.EMPTY);

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

        String imagePath = file.toURI().toString();

        return new Image(imagePath);
    }

    public void initialize() throws IOException {
        infoHBox.setVisible(false);

        button1.setOnAction(event -> {
            editableDesktop = !editableDesktop;
            if (!editableDesktop) {
                for (DesktopApp desktopApp : DesktopApps) {
                    appConfigWrite(desktopApp.getName(), "x", String.valueOf(desktopApp.getX()));
                    appConfigWrite(desktopApp.getName(), "y", String.valueOf(desktopApp.getY()));
                    desktopApp.getMainPane().setStyle("-fx-background-color: transparent;");
                    infoHBox.setVisible(false);
                }
            } else {
                for (DesktopApp desktopApp : DesktopApps) {
                    desktopApp.getMainPane().setStyle("-fx-background-color: rgb(0,0,0,0.25);");
                    infoHBox.setVisible(true);
                    infoLabelLeft.setText(localization.getLocalizedMessage("Desktop.CurrentModeLabel", "Current Mode:"));
                    infoLabelRight.setText(localization.getLocalizedMessage("Desktop.EditingMode", "Editing"));
                    infoLabelRight.setStyle("-fx-text-fill: darkred;");
                    infoLabelRight.setUnderline(true);
                }
            }
        });

        inputLockButton.setOnMouseClicked(event -> {
            isInputLocked = !isInputLocked;
            if (isInputLocked) {
                log("Input Locked!", LogUtils.Severity.INFO);
            } else {
                log("Input Unlocked!", LogUtils.Severity.INFO);
            }
            inputLock.setDisable(isInputLocked);
        });

        NotesViewController.notesText = "";

        updateDesktopBackground(container);

        getTopBarHboxRight().getChildren().remove(locationDataLabel);
        getTopBarHboxRight().getChildren().remove(lookupLabel);

        if (ConfigReader.configRead("uiSettings", "firstLogin").equalsIgnoreCase("true")) {
            ConfigWriter.configwrite("uiSettings", "firstLogin", "false");
            log("First Login...", LogUtils.Severity.DEBUG);
        } else {
            log("Not First Login...", LogUtils.Severity.DEBUG);
        }

        addApps();

        ClientUtils.setStatusListener(this::updateConnectionStatus);

        updateTime();
        updateDate();

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> updateTime()), new KeyFrame(Duration.seconds(5)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        locationStreetLabel.setOnMouseClicked(mouseEvent -> {
            if (getTopBarHboxRight().getChildren().contains(locationDataLabel)) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(locationStreetLabel.getText().replace(",", "").trim());
                log("Clipboard content: " + content.getString(), LogUtils.Severity.INFO);
                clipboard.setContent(content);
            }
        });

        locationAreaLabel.setOnMouseClicked(mouseEvent -> {
            if (getTopBarHboxRight().getChildren().contains(locationDataLabel)) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(locationAreaLabel.getText().replace(",", "").trim());
                log("Clipboard content: " + content.getString(), LogUtils.Severity.INFO);
                clipboard.setContent(content);
            }
        });

        locationCountyLabel.setOnMouseClicked(mouseEvent -> {
            if (getTopBarHboxRight().getChildren().contains(locationDataLabel)) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(locationCountyLabel.getText().replace(",", "").trim());
                log("Clipboard content: " + content.getString(), LogUtils.Severity.INFO);
                clipboard.setContent(content);
            }
        });

        lookupLabel.setOnMouseClicked(mouseEvent -> {
            if (getTopBarHboxRight().getChildren().contains(lookupLabel)) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                String split = lookupLabel.getText().replace(localization.getLocalizedMessage("Desktop.CheckedLabel", "Checked:") + " ", "");
                content.putString(split.trim());
                log("Clipboard content: " + content.getString(), LogUtils.Severity.INFO);
                clipboard.setContent(content);
            }
        });

        try {
            if (ConfigReader.configRead("connectionSettings", "serverAutoConnect").equalsIgnoreCase("true")) {
                log("Searching For Server...", LogUtils.Severity.DEBUG);
                Runnable serverBroadcastTask = () -> {
                    ClientUtils.listenForServerBroadcasts();
                };
                WorkerThread serverBroadcastThread = new WorkerThread("ServerBroadcastThread", serverBroadcastTask);
                serverBroadcastThread.start();
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
            officerInfoName.setText(ConfigReader.configRead("userInfo", "Name") + ", " + ConfigReader.configRead("userInfo", "Agency"));
        } catch (IOException e) {
            logError("Unable to read userInfo name from config (2), ", e);
        }

        if (notesTabList == null) {
            notesTabList = new ArrayList<>();
        }

        addLocale();

        Platform.runLater(() -> {
            configureNotificationPane();

            try {
                settingsController.loadTheme();
            } catch (IOException e) {
                logError("Unable to load theme from config (01), ", e);
            }

            checkForUpdates();

            versionLabel.setText(updateStrings.version);
            if (!updateStrings.version.equalsIgnoreCase(gitVersion)) {
                if (gitVersion == null) {
                    versionLabel.setText(localization.getLocalizedMessage("Desktop.NewVersionAvailable", "New Version Available!"));
                    versionLabel.setStyle("-fx-text-fill: darkred;");
                } else {
                    versionLabel.setText(gitVersion + " " + localization.getLocalizedMessage("Desktop.AvailableLabel", "Available!"));
                    versionLabel.setStyle("-fx-text-fill: darkred;");
                }
            }

            Stage stge = (Stage) container.getScene().getWindow();
            stge.setOnHiding(event -> {
                handleClose();
            });
            stge.show();

        });

        getMainDesktop().setOnKeyPressed(event2 -> {
            KeyCode keyCode = null;
            try {
                keyCode = KeyCode.valueOf(ConfigReader.configRead("keybindings", "inputLock"));
            } catch (IOException e) {
                logError("Unable to read keybindings: ", e);
            }

            if (event2.isControlDown() && event2.getCode() == keyCode) {
                isInputLocked = !isInputLocked;
                if (isInputLocked) {
                    log("Input Locked!", LogUtils.Severity.INFO);
                } else {
                    log("Input Unlocked!", LogUtils.Severity.INFO);
                }
                getInputLock().setDisable(isInputLocked);
                getMainDesktop().requestFocus();
            }
        });
    }

    private void configureNotificationPane() {
        notificationContainerScrollPane.setPrefWidth(Region.USE_COMPUTED_SIZE);
        notificationContainerScrollPane.setPrefHeight(Region.USE_COMPUTED_SIZE);
        notificationContainerScrollPane.setFitToHeight(true);
        notificationContainerScrollPane.setFitToWidth(true);
        notificationContainerScrollPane.setContent(notificationContainer);
        notificationContainerScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        notificationContainerScrollPane.setHmax(0);
        notificationContainerScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        notificationContainerScrollPane.getStylesheets().add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/main/transparentScrollPane.css").toExternalForm());
        desktopContainer.getChildren().add(notificationContainerScrollPane);

        notificationContainer.setStyle("-fx-background-color: transparent;");
    }

    private void addLocale() {
        topBar1.setText(localization.getLocalizedMessage("Desktop.ServerStatusLabel", "Server Status:"));
        topBar2.setText(localization.getLocalizedMessage("Desktop.LoggedInLabel", "Logged In:"));
        button1.setText(localization.getLocalizedMessage("Desktop.EditModeButton", "Edit Mode"));
    }

    private void addAppToDesktop(AnchorPane root, VBox newApp, double x, double y) {
        root.getChildren().add(newApp);
        newApp.setTranslateX(x);
        newApp.setTranslateY(y);
    }

    private void addApps() {
        DesktopApp notesAppObj = new DesktopApp("Notes", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/notepad.png"))));
        VBox notesApp = notesAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow mainApp = WindowManager.createCustomWindow(desktopContainer, "Windows/Other/notes-view.fxml", "Notes", true, 1, true, false, taskBarApps, notesAppObj.getImage());
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

        settingsAppObj = new DesktopApp("Settings", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/setting.png"))));
        VBox settingsApp = settingsAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow settingsWindow = WindowManager.createCustomWindow(desktopContainer, "Windows/Settings/settings-view.fxml", "Program Settings", true, 1, true, false, taskBarApps, settingsAppObj.getImage());
                    if (settingsWindow != null && settingsWindow.controller != null) {
                        SettingsController = (settingsController) settingsWindow.controller;
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, settingsApp, appConfigRead("Settings", "x"), appConfigRead("Settings", "y"));

        updatesAppObj = new DesktopApp("Updates", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/updates.png"))));
        VBox updatesApp = updatesAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    WindowManager.createCustomWindow(desktopContainer, "Windows/Misc/updates-view.fxml", "Version Information", true, 1, true, false, taskBarApps, updatesAppObj.getImage());
                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from updatesApp", e);
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, updatesApp, appConfigRead("Updates", "x"), appConfigRead("Updates", "y"));

        DesktopApp logBrowserAppObj = new DesktopApp("Log Browser", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/logs.png"))));
        VBox logBrowserApp = logBrowserAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = WindowManager.createCustomWindow(desktopContainer, "Windows/Apps/log-view.fxml", "Log Viewer", true, 1, true, false, taskBarApps, logBrowserAppObj.getImage());
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
        addAppToDesktop(desktopContainer, logBrowserApp, appConfigRead("Log Browser", "x"), appConfigRead("Log Browser", "y"));

        calloutManagerAppObj = new DesktopApp("Callouts", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/callout.png"))));
        VBox calloutManagerApp = calloutManagerAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = WindowManager.createCustomWindow(desktopContainer, "Windows/Apps/callout-view.fxml", "Callout Manager", true, 1, true, false, taskBarApps, calloutManagerAppObj.getImage());
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
        addAppToDesktop(desktopContainer, calloutManagerApp, appConfigRead("Callouts", "x"), appConfigRead("Callouts", "y"));

        courtAppObj = new DesktopApp("CourtCase", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/courtIcon.png"))));
        VBox courtApp = courtAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = WindowManager.createCustomWindow(desktopContainer, "Windows/Apps/court-view.fxml", "Court Case Manager", true, 1, true, false, taskBarApps, courtAppObj.getImage());
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

        pedLookupAppObj = new DesktopApp("Ped Lookup", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/ped-search.png"))));
        VBox lookupApp = pedLookupAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = WindowManager.createCustomWindow(desktopContainer, "Windows/Apps/lookup-ped-view.fxml", "Pedestrian Lookup", true, 1, true, false, taskBarApps, pedLookupAppObj.getImage());
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
        addAppToDesktop(desktopContainer, lookupApp, appConfigRead("Ped Lookup", "x"), appConfigRead("Ped Lookup", "y"));

        vehLookupAppObj = new DesktopApp("Veh Lookup", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/veh-search.png"))));
        VBox vehLookupApp = vehLookupAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow logapp = WindowManager.createCustomWindow(desktopContainer, "Windows/Apps/lookup-veh-view.fxml", "Vehicle Lookup", true, 1, true, false, taskBarApps, vehLookupAppObj.getImage());
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
        addAppToDesktop(desktopContainer, vehLookupApp, appConfigRead("Veh Lookup", "x"), appConfigRead("Veh Lookup", "y"));

        newReportAppObj = new DesktopApp("New Report", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/new-report.png"))));
        VBox newReportApp = newReportAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    newReportWindow = WindowManager.createCustomWindow(desktopContainer, "Windows/Apps/new-report-view.fxml", "New Report", true, 1, true, false, taskBarApps, newReportAppObj.getImage());
                    if (newReportWindow != null && newReportWindow.controller != null) {
                        NewReportVewController.newReportVewController = (NewReportVewController) newReportWindow.controller;
                    }
                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from newReportApp", e);
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, newReportApp, appConfigRead("New Report", "x"), appConfigRead("New Report", "y"));

        connectionAppObj = new DesktopApp("Server", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/server.png"))));
        VBox connectionApp = connectionAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow serverApp = WindowManager.createCustomWindow(desktopContainer, "Windows/Server/client-view.fxml", "Server Connection", false, 1, true, false, taskBarApps, connectionAppObj.getImage());
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

        showIDAppObj = new DesktopApp("Show IDs", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/license.png"))));
        VBox showIDApp = showIDAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow IDApp = WindowManager.createCustomWindow(desktopContainer, "Windows/Server/currentID-view.fxml", "Current IDs", true, 1, true, true, taskBarApps, showIDAppObj.getImage());
                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from showIDApp", e);
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, showIDApp, appConfigRead("Show IDs", "x"), appConfigRead("Show IDs", "y"));

        profileAppObj = new DesktopApp("Profile", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/profile.png"))));
        VBox profileApp = profileAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow userManager = WindowManager.createCustomWindow(desktopContainer, "Windows/Misc/new-user-manager.fxml", "Profile", true, 1, true, false, taskBarApps, profileAppObj.getImage());
                    newUserManagerController = (NewUserManagerController) (userManager != null ? userManager.controller : null);
                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from userManager", e);
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, profileApp, appConfigRead("Profile", "x"), appConfigRead("Profile", "y"));

        reportStatisticsAppObj = new DesktopApp("Report Statistics", new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/Apps/statistics.png"))));
        VBox reportStatisticsApp = reportStatisticsAppObj.createDesktopApp(mouseEvent -> {
            if (!editableDesktop) {
                if (mouseEvent.getClickCount() == 2) {
                    CustomWindow statisticWindow = WindowManager.createCustomWindow(desktopContainer, "Windows/Apps/report-statistics-view.fxml", "Report Statistics", true, 1, true, false, taskBarApps, reportStatisticsAppObj.getImage());

                    ReportStatisticsController.reportStatisticsController = (ReportStatisticsController) (statisticWindow != null ? statisticWindow.controller : null);

                    try {
                        settingsController.loadTheme();
                    } catch (IOException e) {
                        logError("Error loading theme from statisticWindow", e);
                    }
                }
            }
        });
        addAppToDesktop(desktopContainer, reportStatisticsApp, appConfigRead("Report Statistics", "x"), appConfigRead("Report Statistics", "y"));
    }

    private void updateTime() {
        timeLabel.setText(getTime(false));
    }

    private void updateDate() {
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        dateLabel.setText(currentDate.format(dateFormatter));
    }

    @FXML
    public void shutdownButtonPress(ActionEvent actionEvent) {
        log("Shutdown button pressed!", LogUtils.Severity.DEBUG);
        handleClose();
    }

    private void updateConnectionStatus(boolean isConnected) {
        if (!isConnected) {
            getTopBarHboxRight().getChildren().remove(locationDataLabel);
            getTopBarHboxRight().getChildren().remove(lookupLabel);
            showNotificationWarning("Server Connection", "Server Disconnected");
            log("No Connection", LogUtils.Severity.WARN);
            serverStatusLabel.setText("No Connection");
            serverStatusLabel.setStyle("-fx-text-fill: darkred; -fx-label-padding: 5; -fx-border-radius: 5;");
            if (clientController != null) {
                clientController.getPortField().setText("");
                clientController.getInetField().setText("");
                clientController.getStatusLabel().setText(localization.getLocalizedMessage("ServerConnectionWindow.NotConnected", "Not Connected"));
                clientController.getStatusLabel().setStyle("-fx-background-color: #FF5E5E;");
                serverStatusLabel.setStyle("-fx-text-fill: darkred; -fx-label-padding: 5; -fx-border-radius: 5;");
            }
            try {
                if (ConfigReader.configRead("connectionSettings", "serverAutoConnect").equalsIgnoreCase("true")) {
                    log("Searching For Server...", LogUtils.Severity.DEBUG);
                    Runnable serverBroadcastTask = () -> {
                        ClientUtils.listenForServerBroadcasts();
                    };
                    WorkerThread serverBroadcastThread = new WorkerThread("ServerBroadcastThread", serverBroadcastTask);
                    serverBroadcastThread.start();
                }
            } catch (IOException e) {
                logError("Not able to read serverautoconnect: ", e);
            }
        } else {
            serverStatusLabel.setText("Connected");

            showNotificationInfo("Server Connection", "Server Connection Established");
            serverStatusLabel.setStyle("-fx-text-fill: darkgreen; -fx-label-padding: 5; -fx-border-radius: 5;");
            if (clientController != null) {
                clientController.getPortField().setText(ClientUtils.port);
                clientController.getInetField().setText(ClientUtils.inet);
                clientController.getStatusLabel().setText(localization.getLocalizedMessage("ServerConnectionWindow.Connected", "Connected"));
                clientController.getStatusLabel().setStyle("-fx-background-color: green;");
            }
        }
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

    public Label getVersionLabel() {
        return versionLabel;
    }

    public Label getDateLabel() {
        return dateLabel;
    }

    public Label getTimeLabel() {
        return timeLabel;
    }

    public Label getTopBar1() {
        return topBar1;
    }

    public Label getTopBar2() {
        return topBar2;
    }

    public HBox getTopBarHboxRight() {
        return topBarHboxRight;
    }

    public GridPane getBottomBar() {
        return bottomBar;
    }

    public Label getLookupLabel() {
        return lookupLabel;
    }

    public Button getShutdownBtn() {
        return shutdownBtn;
    }

    public AnchorPane getInputLock() {
        return inputLock;
    }

    public BorderPane getMainDesktop() {
        return mainDesktop;
    }

    public HBox getLocationDataLabel() {
        return locationDataLabel;
    }

    public Label getLocationAreaLabel() {
        return locationAreaLabel;
    }

    public Label getLocationStreetLabel() {
        return locationStreetLabel;
    }

    public Label getLocationCountyLabel() {
        return locationCountyLabel;
    }
}