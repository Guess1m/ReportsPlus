package com.drozal.dataterminal;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.controllerUtils;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Arrays;

import static com.drozal.dataterminal.util.controllerUtils.*;
import static com.drozal.dataterminal.util.reportCreationUtil.*;
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
    private DUIInformationController duiInformationController;
    @javafx.fxml.FXML
    private RadioMenuItem startupFullscreenToggleBtn;
    @javafx.fxml.FXML
    private AreaChart areaReportChart;
    //</editor-fold>


    //<editor-fold desc="Utils">


    public void initialize() throws IOException {
        setDisable(infoPane);
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
        ComboBox<String> reportDarkLight = new ComboBox<>();

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

        String[] reportdarklight = {"dark", "light"};
        String[] displayPlacements = {"Default", "Top Left", "Top Right", "Bottom Left", "Bottom Right", "\n", "Full Left", "Full Right"};
        mainWindowComboBox.getItems().addAll(displayPlacements);
        notesWindowComboBox.getItems().addAll(displayPlacements);
        ReportWindowComboBox.getItems().addAll(displayPlacements);
        reportDarkLight.getItems().addAll(reportdarklight);
        try {
            if (ConfigReader.configRead("reportWindowDarkMode").equals("true")) {
                reportDarkLight.getSelectionModel().selectFirst();
            } else {
                reportDarkLight.getSelectionModel().selectLast();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

        Label displayPlacementsLabel = new Label("Display Placements");
        displayPlacementsLabel.setStyle("-fx-font-size: 20; -fx-font-family: Segoe UI Black;");
        Label colorsLabel = new Label("Colors");
        colorsLabel.setStyle("-fx-font-size: 20; -fx-font-family: Arial; -fx-font-weight: bold;");

        // Add headings for display settings
        root.addRow(0, displayPlacementsLabel);
        root.addRow(1, new Label("Main Window Placement:"), mainWindowComboBox);
        root.addRow(2, new Label("Notes Window Placement:"), notesWindowComboBox);
        root.addRow(3, new Label("Report Window Placement:"), ReportWindowComboBox);

        // Add empty row
        root.addRow(4, new Label());

        // Add headings for color settings
        root.addRow(5, colorsLabel);
        root.addRow(6, primaryLabel, primPicker);
        root.addRow(7, secondaryLabel, secPicker);
        root.addRow(8, accentLabel, accPicker);
        root.addRow(9, reportColorLabel, reportDarkLight);

        // Add reset button
        root.addRow(10, resetBtn);

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

        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                actionController.notesText = notesViewController.getNotepadTextArea().getText();
            }
        });
    }

    @javafx.fxml.FXML
    public void onShiftInfoBtnClicked(ActionEvent actionEvent) throws IOException {
        setDisable(infoPane);
        setActive(shiftInformationPane);
        showButtonAnimation(shiftInfoBtn);
        controllerUtils.refreshChart(areaReportChart, "area");
    }

    @javafx.fxml.FXML
    public void onLogsButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logBrowser.fxml"));
        Parent root = loader.load();
        LogBrowserController logBrowserController = loader.getController();
        BorderlessScene newScene = new BorderlessScene(stage, StageStyle.TRANSPARENT, root, Color.TRANSPARENT);
        stage.setTitle("Log Browser");
        stage.setScene(newScene);
        stage.setResizable(true);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
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
        showButtonAnimation(logsButton);
        AnchorPane topbar = logBrowserController.getTitlebar();
        newScene.setMoveControl(topbar);
        stage.setAlwaysOnTop(true);

        stage.setOnHidden(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if (notesViewController != null) {
                    actionController.notesText = notesViewController.getNotepadTextArea().getText();
                }
            }
        });
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
        setDisable(shiftInformationPane);
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
    public void onStartupFullscreenPress(ActionEvent actionEvent) throws IOException {
        if (startupFullscreenToggleBtn.isSelected()) {
            ConfigWriter.configwrite("fullscreenOnStartup", "true");
            startupFullscreenToggleBtn.setSelected(true);
        } else {
            ConfigWriter.configwrite("fullscreenOnStartup", "false");
            startupFullscreenToggleBtn.setSelected(false);
        }
    }


    //</editor-fold>


}