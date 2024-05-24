package com.drozal.dataterminal.util;

import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.NotesViewController;
import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
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
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.drozal.dataterminal.DataTerminalHomeApplication.getDate;
import static com.drozal.dataterminal.DataTerminalHomeApplication.getTime;
import static com.drozal.dataterminal.util.LogUtils.log;
import static com.drozal.dataterminal.util.LogUtils.logError;
import static com.drozal.dataterminal.util.controllerUtils.*;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.treeViewUtils.*;
import static com.drozal.dataterminal.util.windowUtils.*;

public class reportCreationUtil {

    private static final Map<String, Map<String, String>> reportData = new HashMap<>();
    static double windowX = 0;
    static double windowY = 0;
    private static double xOffset;
    private static double yOffset;

    private static String getPrimaryColor() { //non selected textarea
        String primaryColor;
        try {
            if (ConfigReader.configRead("reportWindowDarkMode").equals("true")) {
                primaryColor = "#323c41";//DARK medium
            } else {
                primaryColor = "#f2f2f2";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return primaryColor;
    }

    private static String getSecondaryColor() { // selected textarea
        String secondaryColor;
        try {
            if (ConfigReader.configRead("reportWindowDarkMode").equals("true")) {
                secondaryColor = "#263238";//DARK Darkest
            } else {
                secondaryColor = "#d2d2d2";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return secondaryColor;
    }

    private static String getAccentColor() { // background
        String accentColor;
        try {
            if (ConfigReader.configRead("reportWindowDarkMode").equals("true")) {
                accentColor = "#505d62";//DARK Lightest
            } else {
                accentColor = "white";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accentColor;
    }

    /* EXAMPLE

      -n Jefferson Amato -a 28 -g Male -add 192 Forum Drive -desc Short Blonde Hair, Brown Eyes, tall

      -search Handgun Loaded

      -num 3

      -cnty Blaine -ar Sandy Shores -st 9028 Grapeseed Lane

      -model Infernus -plate PSLD9283

      -cmt additional units were needed, corresponding reports #3

      */

    /*
    Pull from Config

    static {
        try {
            primaryColor = ConfigReader.configRead("getSecondaryColor()");
            getSecondaryColor() = ConfigReader.configRead("mainColor");
            getAccentColor() = ConfigReader.configRead("getAccentColor()");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    */


    //<editor-fold desc="Creation">

    public static AnchorPane createTitleBar(String titleText) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1.0);
        colorAdjust.setBrightness(-0.45);

        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleLabel.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(titleLabel, (double) 0);
        AnchorPane.setRightAnchor(titleLabel, (double) 0);
        AnchorPane.setTopAnchor(titleLabel, (double) 0);
        AnchorPane.setBottomAnchor(titleLabel, (double) 0);
        titleLabel.setEffect(colorAdjust);
        titleLabel.setMouseTransparent(true);

        AnchorPane titleBar = new AnchorPane(titleLabel);
        titleBar.setMinHeight(30);
        titleBar.setStyle("-fx-background-color: #383838;");

        Image placeholderImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"));
        ImageView placeholderImageView = new ImageView(placeholderImage);
        placeholderImageView.setFitWidth(49);
        placeholderImageView.setFitHeight(49);
        AnchorPane.setLeftAnchor(placeholderImageView, 0.0);
        AnchorPane.setTopAnchor(placeholderImageView, -10.0);
        AnchorPane.setBottomAnchor(placeholderImageView, -10.0);
        placeholderImageView.setEffect(colorAdjust);

        Image closeImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/cross.png"));
        ImageView closeImageView = new ImageView(closeImage);
        closeImageView.setFitWidth(15);
        closeImageView.setFitHeight(15);
        AnchorPane.setRightAnchor(closeImageView, 15.0);
        AnchorPane.setTopAnchor(closeImageView, 7.0);
        closeImageView.setEffect(colorAdjust);

        Image maximizeImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/maximize.png"));
        ImageView maximizeImageView = new ImageView(maximizeImage);
        maximizeImageView.setFitWidth(15);
        maximizeImageView.setFitHeight(15);
        AnchorPane.setRightAnchor(maximizeImageView, 42.5);
        AnchorPane.setTopAnchor(maximizeImageView, 7.0);
        maximizeImageView.setEffect(colorAdjust);

        Image minimizeImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/minimize.png"));
        ImageView minimizeImageView = new ImageView(minimizeImage);
        minimizeImageView.setFitWidth(15);
        minimizeImageView.setFitHeight(15);
        AnchorPane.setRightAnchor(minimizeImageView, 70.0);
        AnchorPane.setTopAnchor(minimizeImageView, 7.0);
        minimizeImageView.setEffect(colorAdjust);

        Rectangle closeRect = new Rectangle(20, 20);
        Rectangle maximizeRect = new Rectangle(20, 20);
        Rectangle minimizeRect = new Rectangle(20, 20);

        closeRect.setFill(Color.TRANSPARENT);
        minimizeRect.setFill(Color.TRANSPARENT);
        maximizeRect.setFill(Color.TRANSPARENT);

        closeRect.setOnMouseClicked(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.close();
        });

        minimizeRect.setOnMouseClicked(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setIconified(true);
        });
        maximizeRect.setOnMouseClicked(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            windowUtils.toggleWindowedFullscreen(stage, 850, 750);
        });

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        AnchorPane.setRightAnchor(closeRect, 12.5);
        AnchorPane.setTopAnchor(closeRect, 6.3);
        AnchorPane.setRightAnchor(minimizeRect, 70.0);
        AnchorPane.setTopAnchor(minimizeRect, 6.3);
        AnchorPane.setRightAnchor(maximizeRect, 42.5);
        AnchorPane.setTopAnchor(maximizeRect, 6.3);

        titleBar.getChildren().addAll(placeholderImageView, closeRect, maximizeRect, minimizeRect, closeImageView, maximizeImageView, minimizeImageView);
        Platform.runLater(() -> {
            Stage stage1 = (Stage) titleBar.getScene().getWindow();
            ResizeHelper.addResizeListener(stage1);
        });
        closeRect.toFront();
        minimizeRect.toFront();
        maximizeRect.toFront();
        return titleBar;
    }


    public static AnchorPane createSimpleTitleBar(String titleText, boolean resiable) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1.0);
        colorAdjust.setBrightness(-0.45);

        Label titleLabel = new Label(titleText);
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        titleLabel.setAlignment(Pos.CENTER);
        AnchorPane.setLeftAnchor(titleLabel, (double) 0);
        AnchorPane.setRightAnchor(titleLabel, (double) 0);
        AnchorPane.setTopAnchor(titleLabel, (double) 0);
        AnchorPane.setBottomAnchor(titleLabel, (double) 0);
        titleLabel.setEffect(colorAdjust);
        titleLabel.setMouseTransparent(true);

        AnchorPane titleBar = new AnchorPane(titleLabel);
        titleBar.setMinHeight(30);
        titleBar.setStyle("-fx-background-color: #383838;");

        Image placeholderImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/Logo.png"));
        ImageView placeholderImageView = new ImageView(placeholderImage);
        placeholderImageView.setFitWidth(49);
        placeholderImageView.setFitHeight(49);
        AnchorPane.setLeftAnchor(placeholderImageView, 0.0);
        AnchorPane.setTopAnchor(placeholderImageView, -10.0);
        AnchorPane.setBottomAnchor(placeholderImageView, -10.0);
        placeholderImageView.setEffect(colorAdjust);

        Image closeImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/cross.png"));
        ImageView closeImageView = new ImageView(closeImage);
        closeImageView.setFitWidth(15);
        closeImageView.setFitHeight(15);
        AnchorPane.setRightAnchor(closeImageView, 15.0);
        AnchorPane.setTopAnchor(closeImageView, 7.0);
        closeImageView.setEffect(colorAdjust);

        Image minimizeImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/minimize.png"));
        ImageView minimizeImageView = new ImageView(minimizeImage);
        minimizeImageView.setFitWidth(15);
        minimizeImageView.setFitHeight(15);
        AnchorPane.setRightAnchor(minimizeImageView, 42.5);
        AnchorPane.setTopAnchor(minimizeImageView, 7.0);
        minimizeImageView.setEffect(colorAdjust);

        Rectangle closeRect = new Rectangle(20, 20);
        Rectangle minimizeRect = new Rectangle(20, 20);

        closeRect.setFill(Color.TRANSPARENT);
        minimizeRect.setFill(Color.TRANSPARENT);

        closeRect.setOnMouseClicked(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.close();
        });

        minimizeRect.setOnMouseClicked(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setIconified(true);
        });

        AnchorPane.setRightAnchor(closeRect, 12.5);
        AnchorPane.setTopAnchor(closeRect, 6.3);
        AnchorPane.setRightAnchor(minimizeRect, 42.5);
        AnchorPane.setTopAnchor(minimizeRect, 6.3);

        titleBar.getChildren().addAll(placeholderImageView, closeRect, minimizeRect, closeImageView, minimizeImageView);
        closeRect.toFront();
        minimizeRect.toFront();

        titleBar.setOnMouseDragged(event -> {
            Stage stage = (Stage) titleBar.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });

        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        if (resiable) {
            Platform.runLater(() -> {
                Stage stage1 = (Stage) titleBar.getScene().getWindow();
                ResizeHelper.addResizeListener(stage1);
            });
        }

        return titleBar;
    }


    public static Map<String, Object> createReportWindow(String reportName, int numWidthUnits, int numHeightUnits, TransferConfig transferConfig, SectionConfig... sectionConfigs) {
        String placeholder;
        try {
            if (ConfigReader.configRead("reportWindowDarkMode").equals("true")) {
                placeholder = "white";
            } else {
                placeholder = "black";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        double preferredWidth = screenWidth / 12 * numWidthUnits;
        double preferredHeight = screenHeight / 12 * numHeightUnits;

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: black; -fx-border-width: 1.5;");

        AnchorPane titleBar = createTitleBar("Report Manager");

        borderPane.setTop(titleBar);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label warningLabel = new Label("Please fill out the form");
        warningLabel.setVisible(false);
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 14;");

        for (int i = 0; i < 12; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100 / 12.0);
            gridPane.getColumnConstraints().add(column);
        }

        Label mainHeaderLabel = new Label("New " + reportName);
        mainHeaderLabel.setStyle("-fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: " + placeholder + "; -fx-font-family: Segoe UI Black;");
        mainHeaderLabel.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(mainHeaderLabel, 12);
        gridPane.add(mainHeaderLabel, 0, 0);

        Map<String, Object> fieldsMap = new HashMap<>();
        int rowIndex = 1;

        for (SectionConfig sectionConfig : sectionConfigs) {
            // Add Section Label
            Label sectionLabel = new Label(sectionConfig.getSectionTitle());
            sectionLabel.setFont(Font.font("Segoe UI Black"));
            sectionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + placeholder + "; -fx-background-color: transparent; -fx-padding: 0px 40px;");
            gridPane.add(sectionLabel, 0, rowIndex, 12, 1);
            rowIndex++;

            // Add Rows for each Config
            for (RowConfig rowConfig : sectionConfig.getRowConfigs()) {
                addRowToGridPane(gridPane, rowConfig, rowIndex, fieldsMap);
                rowIndex++;
            }

            if (sectionConfig != sectionConfigs[sectionConfigs.length - 1]) {
                Separator separator = new Separator();
                separator.setMaxWidth(Double.MAX_VALUE);

                StackPane separatorPane = new StackPane(separator);
                separatorPane.setPadding(new Insets(20, 0, 0, 0));

                GridPane.setColumnSpan(separatorPane, 12);
                gridPane.add(separatorPane, 0, rowIndex);
                rowIndex++;
            }

            if (sectionConfig.getSectionTitle().equals("Citation Treeview")) {
                rowIndex += 5;
            } else {
                rowIndex += 2;
            }
        }

        Button submitBtn = new Button("Collect Values");
        submitBtn.getStyleClass().add("incidentformButton");
        submitBtn.setStyle("-fx-padding: 15;");
        submitBtn.setStyle("-fx-background-color: " + getPrimaryColor());
        submitBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                submitBtn.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
            } else {
                submitBtn.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
            }
        });

        Button pullNotesBtn = new Button("Pull From Notes");
        pullNotesBtn.getStyleClass().add("incidentformButton");
        pullNotesBtn.setStyle("-fx-padding: 15;");
        pullNotesBtn.setStyle("-fx-background-color: " + getPrimaryColor());
        pullNotesBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                pullNotesBtn.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
            } else {
                pullNotesBtn.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
            }
        });

        HBox buttonBox = new HBox(10, pullNotesBtn, warningLabel, submitBtn);
        buttonBox.setAlignment(Pos.BASELINE_RIGHT);
        VBox root = new VBox(10, mainHeaderLabel, gridPane);

        if (transferConfig != null) {
            TitledPane titledPane = new TitledPane();
            titledPane.setExpanded(false);
            titledPane.setText(transferConfig.getTitle());
            titledPane.getStyleClass().add("paneoptions");

            GridPane paneGrid = new GridPane();
            paneGrid.setHgap(10);
            paneGrid.setVgap(10);

            paneGrid.getColumnConstraints().clear(); // Clear existing constraints if any
            for (int i = 0; i < 12; i++) {
                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setPercentWidth(100.0 / 12); // Divide the grid evenly
                paneGrid.getColumnConstraints().add(columnConstraints);
            }

            int rowIndex1 = 0;
            for (RowConfig rowConfig : transferConfig.getRowConfigs()) {
                addRowToGridPane(paneGrid, rowConfig, rowIndex1++, fieldsMap);
            }

            titledPane.setContent(paneGrid);

            Pane spacerPane1 = new Pane();
            spacerPane1.setMinHeight(20);
            spacerPane1.setPrefHeight(20);
            Pane spacerPane2 = new Pane();
            spacerPane2.setMinHeight(20);
            spacerPane1.setPrefHeight(20);

            Accordion accordion = new Accordion();
            accordion.setStyle("-fx-box-border: transparent;");
            accordion.getPanes().add(titledPane);
            accordion.setMaxWidth(Double.MAX_VALUE);
            accordion.setMinHeight(Region.USE_PREF_SIZE);
            accordion.setPrefHeight(Region.USE_COMPUTED_SIZE);
            accordion.setMaxHeight(Region.USE_PREF_SIZE);

            paneGrid.setStyle("-fx-background-color: " + getSecondaryColor() + "; -fx-border-color: " + getSecondaryColor() + ";");
            accordion.setStyle("-fx-background-color: " + getSecondaryColor() + "; -fx-border-color: " + getSecondaryColor() + ";");
            titledPane.setStyle("-fx-background-color: " + getSecondaryColor() + "; -fx-border-color: " + getSecondaryColor() + ";");

            root.getChildren().addAll(spacerPane1, titledPane, spacerPane2);
        }

        root.getChildren().add(buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + getAccentColor() + ";");
        Insets insets = new Insets(20, 25, 15, 25);
        root.setPadding(insets);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        borderPane.setCenter(scrollPane);

        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);

        Scene scene = new Scene(borderPane);

        try {
            if (ConfigReader.configRead("reportWindowDarkMode").equals("true")) {
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/dark/formFields.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/dark/formTextArea.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/dark/formButton.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/dark/formComboBox.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/dark/Logscrollpane.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/dark/tableCss.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/dark/formTitledPane.css").toExternalForm());
            } else {
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/light/formFields.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/light/formTextArea.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/light/formButton.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/light/formComboBox.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/light/Logscrollpane.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/light/tableCss.css").toExternalForm());
                scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/light/formTitledPane.css").toExternalForm());
            }
        } catch (IOException e) {
            logError("Could not add stylesheets to reports: ", e);
        }

        scrollPane.getStyleClass().add("formPane");
        scrollPane.setStyle("-fx-background-color: " + getAccentColor() + "; " + "-fx-focus-color: " + getAccentColor() + ";");

        stage.setScene(scene);
        stage.setTitle(reportName);

        stage.setMaxWidth(screenWidth);
        stage.setMaxHeight(screenHeight);

        stage.initOwner(DataTerminalHomeApplication.getMainRT());

        stage.show();
        stage.toFront();

        stage.setOnHidden(event -> {
            windowX = stage.getX();
            windowY = stage.getY();
        });

        String startupValue = null;
        try {
            startupValue = ConfigReader.configRead("reportWindowLayout");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        switch (startupValue) {
            case "TopLeft" -> snapToTopLeft(stage);
            case "TopRight" -> snapToTopRight(stage);
            case "BottomLeft" -> snapToBottomLeft(stage);
            case "BottomRight" -> snapToBottomRight(stage);
            case "FullLeft" -> snapToLeft(stage);
            case "FullRight" -> snapToRight(stage);
            default -> {
                stage.setWidth(preferredWidth);
                stage.setHeight(preferredHeight);
                stage.setMinHeight(300);
                stage.setMinWidth(300);
                if (windowX != 0 && windowY != 0) {
                    stage.setX(windowX);
                    stage.setY(windowY);
                } else {
                    stage.centerOnScreen();
                }
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put(reportName + " Map", fieldsMap);
        result.put("pullNotesBtn", pullNotesBtn);
        result.put("warningLabel", warningLabel);
        result.put("submitBtn", submitBtn);
        result.put("root", borderPane);

        stage.setAlwaysOnTop(true);
        stage.requestFocus();
        return result;
    }


    private static void addRowToGridPane(GridPane gridPane, RowConfig rowConfig, int rowIndex, Map<String, Object> fieldsMap) {
        String placeholder;
        try {
            if (ConfigReader.configRead("reportWindowDarkMode").equals("true")) {
                placeholder = "white";
            } else {
                placeholder = "black";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int totalSize = 0;
        int columnIndex = 0;
        for (FieldConfig fieldConfig : rowConfig.getFieldConfigs()) {
            if (fieldConfig.getSize() <= 0 || totalSize + fieldConfig.getSize() > 12) {
                log(fieldConfig.getFieldName(), LogUtils.Severity.ERROR);
                log(String.valueOf(fieldConfig.getSize()), LogUtils.Severity.ERROR);
                throw new IllegalArgumentException("Invalid field size configuration");
            }
            totalSize += fieldConfig.getSize();
            switch (fieldConfig.getFieldType()) {
                case TEXT_FIELD:
                    TextField textField = new TextField();
                    textField.getStyleClass().add("formField3");
                    textField.setStyle("-fx-background-color: " + getPrimaryColor());
                    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            textField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            textField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null) {
                            textField.setText(newValue.toUpperCase());
                        }
                    });
                    textField.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    textField.setPrefWidth(200);
                    gridPane.add(textField, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), textField);
                    break;
                case TEXT_AREA:
                    TextArea textArea = new TextArea();
                    textArea.setWrapText(true);
                    textArea.getStyleClass().add("othertextarea");
                    textArea.setStyle("-fx-background-color: " + getPrimaryColor());
                    textArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            textArea.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            textArea.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    textArea.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    textArea.setPrefRowCount(5);
                    textArea.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(textArea, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), textArea);

                    GridPane.setHgrow(textArea, Priority.ALWAYS);
                    break;
                case COMBO_BOX_COLOR:
                    ComboBox<String> comboBoxColor = new ComboBox<>();
                    comboBoxColor.getStyleClass().add("comboboxnew");
                    comboBoxColor.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                    comboBoxColor.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            comboBoxColor.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            comboBoxColor.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    comboBoxColor.getItems().addAll(dropdownInfo.carColors);
                    comboBoxColor.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    comboBoxColor.setButtonCell(new ListCell() {

                        @Override
                        protected void updateItem(Object item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
                            } else {
                                setStyle("-fx-text-fill: " + placeholder + ";");
                                setText(item.toString());
                            }
                        }

                    });
                    comboBoxColor.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(comboBoxColor, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), comboBoxColor);
                    break;
                case COMBO_BOX_TYPE:
                    ComboBox<String> comboBoxType = new ComboBox<>();
                    comboBoxType.getStyleClass().add("comboboxnew");
                    comboBoxType.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                    comboBoxType.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            comboBoxType.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            comboBoxType.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    comboBoxType.getItems().addAll(dropdownInfo.vehicleTypes);
                    comboBoxType.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    comboBoxType.setButtonCell(new ListCell() {

                        @Override
                        protected void updateItem(Object item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
                            } else {
                                setStyle("-fx-text-fill: " + placeholder + ";");
                                setText(item.toString());
                            }
                        }

                    });
                    comboBoxType.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(comboBoxType, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), comboBoxType);
                    break;
                case COMBO_BOX_SEARCH_TYPE:
                    ComboBox<String> comboBoxSearchType = new ComboBox<>();
                    comboBoxSearchType.getStyleClass().add("comboboxnew");
                    comboBoxSearchType.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                    comboBoxSearchType.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            comboBoxSearchType.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            comboBoxSearchType.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    comboBoxSearchType.getItems().addAll(dropdownInfo.searchTypes);
                    comboBoxSearchType.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    comboBoxSearchType.setButtonCell(new ListCell() {

                        @Override
                        protected void updateItem(Object item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
                            } else {
                                setStyle("-fx-text-fill: " + placeholder + ";");
                                setText(item.toString());
                            }
                        }

                    });
                    comboBoxSearchType.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(comboBoxSearchType, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), comboBoxSearchType);
                    break;
                case COMBO_BOX_SEARCH_METHOD:
                    ComboBox<String> comboBoxSearchMethod = new ComboBox<>();
                    comboBoxSearchMethod.getStyleClass().add("comboboxnew");
                    comboBoxSearchMethod.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                    comboBoxSearchMethod.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            comboBoxSearchMethod.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            comboBoxSearchMethod.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    comboBoxSearchMethod.getItems().addAll(dropdownInfo.searchMethods);
                    comboBoxSearchMethod.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    comboBoxSearchMethod.setButtonCell(new ListCell() {

                        @Override
                        protected void updateItem(Object item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
                            } else {
                                setStyle("-fx-text-fill: " + placeholder + ";");
                                setText(item.toString());
                            }
                        }

                    });
                    comboBoxSearchMethod.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(comboBoxSearchMethod, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), comboBoxSearchMethod);
                    break;
                case CITATION_TREE_VIEW:
                    TreeView<String> treeView = new TreeView<>();
                    treeView.setPrefHeight(350);
                    treeView.setMinHeight(350);
                    treeView.setMaxHeight(350);

                    File file = new File(getJarPath() + "/data/Citations.xml");
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    Document document = null;
                    try {
                        document = factory.newDocumentBuilder().parse(file);
                    } catch (SAXException | IOException | ParserConfigurationException e) {
                        throw new RuntimeException(e);
                    }

                    Element root = document.getDocumentElement();

                    TreeItem<String> rootItem = new TreeItem<>(root.getNodeName());

                    parseTreeXML(root, rootItem);
                    treeView.setRoot(rootItem);
                    expandTreeItem(rootItem, "Citations");

                    TextField citationNameField = new TextField();
                    citationNameField.setPromptText("Citation Name");
                    TextField citationFineField = new TextField();
                    citationFineField.setPromptText("Citation Fine");

                    Button addButton = new Button("Add");
                    Button removeButton = new Button("Remove");

                    Label citationInfoLabel = new Label("Citation Information");
                    citationInfoLabel.setAlignment(Pos.CENTER);

                    TableView<CitationsData> citationTableView = new TableView<>();
                    citationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    citationTableView.getStyleClass().add("calloutTABLE");

                    TableColumn<CitationsData, String> citationColumn = new TableColumn<>("Citation");
                    citationColumn.setCellValueFactory(new PropertyValueFactory<>("citation"));
                    citationTableView.setTableMenuButtonVisible(false);

                    citationTableView.getColumns().add(citationColumn);
                    gridPane.add(treeView, columnIndex, rowIndex, fieldConfig.getSize(), 5);

                    int additionalColumnIndex = columnIndex + fieldConfig.getSize();

                    int remainingColumns = gridPane.getColumnCount() - additionalColumnIndex;

                    gridPane.add(citationInfoLabel, additionalColumnIndex, rowIndex, remainingColumns, 1);
                    GridPane.setHalignment(citationInfoLabel, HPos.CENTER);
                    gridPane.add(citationNameField, additionalColumnIndex, rowIndex + 1, remainingColumns, 1);
                    gridPane.add(citationFineField, additionalColumnIndex, rowIndex + 2, remainingColumns, 1);

                    HBox buttonBox = new HBox(40, addButton, removeButton);
                    buttonBox.setAlignment(Pos.CENTER);
                    gridPane.add(buttonBox, additionalColumnIndex, rowIndex + 3, remainingColumns, 1);

                    gridPane.add(citationTableView, additionalColumnIndex, rowIndex + 4, remainingColumns, 1);

                    fieldsMap.put("CitationNameField", citationNameField);
                    fieldsMap.put("CitationFineField", citationFineField);
                    fieldsMap.put("AddButton", addButton);
                    fieldsMap.put("RemoveButton", removeButton);
                    fieldsMap.put("CitationTableView", citationTableView);
                    fieldsMap.put(fieldConfig.getFieldName(), treeView);

                    citationInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + placeholder + "; -fx-background-color: transparent; -fx-padding: 0px 40px;");
                    citationInfoLabel.setFont(Font.font("Segoe UI Black"));
                    addButton.getStyleClass().add("incidentformButton");
                    addButton.setStyle("-fx-padding: 15;");
                    addButton.setStyle("-fx-background-color: " + getPrimaryColor());
                    addButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            addButton.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            addButton.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    removeButton.getStyleClass().add("incidentformButton");
                    removeButton.setStyle("-fx-padding: 15;");
                    removeButton.setStyle("-fx-background-color: " + getPrimaryColor());
                    removeButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            removeButton.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            removeButton.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    citationTableView.setStyle("-fx-background-color: " + getPrimaryColor());
                    citationTableView.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            citationTableView.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            citationTableView.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    citationNameField.getStyleClass().add("formField3");
                    citationNameField.setStyle("-fx-background-color: " + getPrimaryColor());
                    citationNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            citationNameField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            citationNameField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    citationFineField.getStyleClass().add("formField3");
                    citationFineField.setStyle("-fx-background-color: " + getPrimaryColor());
                    citationFineField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            citationFineField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            citationFineField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    treeView.setOnMouseClicked(event -> {
                        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
                        if (selectedItem != null && selectedItem.isLeaf()) {
                            citationNameField.setText(selectedItem.getValue());
                            citationFineField.setText(findXMLValue(selectedItem.getValue(), "fine", "data/Citations.xml"));
                        } else {
                            citationNameField.setText("");
                            citationFineField.setText("");
                        }
                    });
                    addButton.setOnMouseClicked(event -> {
                        String citation = citationNameField.getText();
                        if (!(citation.isBlank() || citation.isEmpty())) {
                            CitationsData formData = new CitationsData(citation);
                            citationTableView.getItems().add(formData);
                        }
                    });
                    removeButton.setOnMouseClicked(event -> {
                        CitationsData selectedItem = citationTableView.getSelectionModel().getSelectedItem();
                        if (selectedItem != null) {
                            citationTableView.getItems().remove(selectedItem);
                        }
                    });
                    rowIndex += 6;
                    break;
                case TRANSFER_BUTTON:
                    Button transferButton = new Button("Transfer Information");
                    transferButton.setMaxWidth(Double.MAX_VALUE);
                    GridPane.setHgrow(transferButton, Priority.ALWAYS);
                    GridPane.setVgrow(transferButton, Priority.NEVER);

                    transferButton.setMinHeight(Button.USE_PREF_SIZE);
                    transferButton.setPrefHeight(Button.USE_COMPUTED_SIZE);
                    transferButton.setMaxHeight(Button.USE_PREF_SIZE);

                    transferButton.getStyleClass().add("incidentformButton");
                    transferButton.setStyle("-fx-background-color: " + getAccentColor());
                    transferButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            transferButton.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        } else {
                            transferButton.setStyle("-fx-background-color: " + getAccentColor() + ";");
                        }
                    });
                    gridPane.add(transferButton, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), transferButton);
                    break;
                case CHARGES_TREE_VIEW:
                    TreeView<String> chargestreeView = new TreeView<>();
                    chargestreeView.setPrefHeight(350);
                    chargestreeView.setMinHeight(350);
                    chargestreeView.setMaxHeight(350);

                    File file2 = new File(getJarPath() + "/data/Charges.xml");
                    DocumentBuilderFactory factory2 = DocumentBuilderFactory.newInstance();
                    Document document2 = null;
                    try {
                        document2 = factory2.newDocumentBuilder().parse(file2);
                    } catch (SAXException | IOException | ParserConfigurationException e) {
                        throw new RuntimeException(e);
                    }

                    Element root2 = document2.getDocumentElement();

                    TreeItem<String> rootItem2 = new TreeItem<>(root2.getNodeName());

                    parseTreeXML(root2, rootItem2);
                    chargestreeView.setRoot(rootItem2);
                    expandTreeItem(rootItem2, "Charges");

                    TextField chargeNameField = new TextField();
                    chargeNameField.setPromptText("Charge Name");

                    Button addButton2 = new Button("Add");
                    Button removeButton2 = new Button("Remove");

                    Label chargeInfoLabel = new Label("Charge Information");
                    chargeInfoLabel.setAlignment(Pos.CENTER);

                    TableView<ChargesData> chargeTableView = new TableView<>();
                    chargeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    chargeTableView.getStyleClass().add("calloutTABLE");

                    TableColumn<ChargesData, String> chargeColumn = new TableColumn<>("Charge");
                    chargeColumn.setCellValueFactory(new PropertyValueFactory<>("charge"));
                    chargeTableView.setTableMenuButtonVisible(false);

                    chargeTableView.getColumns().add(chargeColumn);
                    gridPane.add(chargestreeView, columnIndex, rowIndex, fieldConfig.getSize(), 5);

                    int additionalColumnIndex2 = columnIndex + fieldConfig.getSize();

                    int remainingColumns2 = gridPane.getColumnCount() - additionalColumnIndex2;

                    gridPane.add(chargeInfoLabel, additionalColumnIndex2, rowIndex, remainingColumns2, 1);
                    GridPane.setHalignment(chargeInfoLabel, HPos.CENTER);
                    gridPane.add(chargeNameField, additionalColumnIndex2, rowIndex + 1, remainingColumns2, 1);

                    HBox buttonBox2 = new HBox(40, addButton2, removeButton2);
                    buttonBox2.setAlignment(Pos.CENTER);
                    gridPane.add(buttonBox2, additionalColumnIndex2, rowIndex + 3, remainingColumns2, 1);

                    gridPane.add(chargeTableView, additionalColumnIndex2, rowIndex + 4, remainingColumns2, 1);

                    fieldsMap.put("ChargeNameField", chargeNameField);
                    fieldsMap.put("AddButton", addButton2);
                    fieldsMap.put("RemoveButton", removeButton2);
                    fieldsMap.put("ChargeTableView", chargeTableView);
                    fieldsMap.put(fieldConfig.getFieldName(), chargestreeView);

                    chargeInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: " + placeholder + "; -fx-background-color: transparent; -fx-padding: 0px 40px;");
                    chargeInfoLabel.setFont(Font.font("Segoe UI Black"));
                    addButton2.getStyleClass().add("incidentformButton");
                    addButton2.setStyle("-fx-padding: 15;");
                    addButton2.setStyle("-fx-background-color: " + getPrimaryColor());
                    addButton2.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            addButton2.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            addButton2.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    removeButton2.getStyleClass().add("incidentformButton");
                    removeButton2.setStyle("-fx-padding: 15;");
                    removeButton2.setStyle("-fx-background-color: " + getPrimaryColor());
                    removeButton2.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            removeButton2.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            removeButton2.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    chargeTableView.setStyle("-fx-background-color: " + getPrimaryColor());
                    chargeTableView.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            chargeTableView.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            chargeTableView.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    chargeNameField.getStyleClass().add("formField3");
                    chargeNameField.setStyle("-fx-background-color: " + getPrimaryColor());
                    chargeNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            chargeNameField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            chargeNameField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    chargestreeView.setOnMouseClicked(event -> {
                        TreeItem<String> selectedItem = chargestreeView.getSelectionModel().getSelectedItem();
                        if (selectedItem != null && selectedItem.isLeaf()) {
                            chargeNameField.setText(selectedItem.getValue());
                        } else {
                            chargeNameField.setText("");
                        }
                    });
                    addButton2.setOnMouseClicked(event -> {
                        String charge = chargeNameField.getText();
                        if (!(charge.isBlank() || charge.isEmpty())) {
                            ChargesData formData = new ChargesData(charge);
                            chargeTableView.getItems().add(formData);
                        }
                    });
                    removeButton2.setOnMouseClicked(event -> {
                        ChargesData selectedItem = chargeTableView.getSelectionModel().getSelectedItem();
                        if (selectedItem != null) {
                            chargeTableView.getItems().remove(selectedItem);
                        }
                    });
                    rowIndex += 6;
                    break;

            }
            columnIndex += fieldConfig.getSize();

        }
    }


    //</editor-fold>


    //<editor-fold desc="Report Windows">


    static Map<String, Object> calloutLayout() {
        Map<String, Object> calloutReport = createReportWindow("Callout Report", 5, 7, null,
                new SectionConfig("Officer Information", true,
                        new RowConfig(new FieldConfig("name", 5, FieldType.TEXT_FIELD), new FieldConfig("rank", 5, FieldType.TEXT_FIELD), new FieldConfig("number", 2, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("division", 6, FieldType.TEXT_FIELD), new FieldConfig("agency", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Location Information", true,
                        new RowConfig(new FieldConfig("county", 3, FieldType.TEXT_FIELD), new FieldConfig("area", 4, FieldType.TEXT_FIELD), new FieldConfig("street", 5, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Callout Information", true,
                        new RowConfig(new FieldConfig("date", 6, FieldType.TEXT_FIELD), new FieldConfig("time", 6, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("type", 4, FieldType.TEXT_FIELD), new FieldConfig("code", 4, FieldType.TEXT_FIELD), new FieldConfig("calloutnumber", 4, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Callout Notes", true,
                        new RowConfig(new FieldConfig("notes", 12, FieldType.TEXT_AREA))
                )
        );
        return calloutReport;
    }

    public static void newCallout(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
        Map<String, Object> calloutReport = calloutLayout();

        Map<String, Object> calloutReportMap = (Map<String, Object>) calloutReport.get("Callout Report Map");

        TextField officername = (TextField) calloutReportMap.get("name");
        TextField officerrank = (TextField) calloutReportMap.get("rank");
        TextField officerdiv = (TextField) calloutReportMap.get("division");
        TextField officeragen = (TextField) calloutReportMap.get("agency");
        TextField officernum = (TextField) calloutReportMap.get("number");
        TextField calloutnum = (TextField) calloutReportMap.get("calloutnumber");
        TextField calloutarea = (TextField) calloutReportMap.get("area");
        TextArea calloutnotes = (TextArea) calloutReportMap.get("notes");
        TextField calloutcounty = (TextField) calloutReportMap.get("county");
        TextField calloutstreet = (TextField) calloutReportMap.get("street");
        TextField calloutdate = (TextField) calloutReportMap.get("date");
        TextField callouttime = (TextField) calloutReportMap.get("time");
        TextField callouttype = (TextField) calloutReportMap.get("type");
        BorderPane root = (BorderPane) calloutReport.get("root");
        TextField calloutcode = (TextField) calloutReportMap.get("code");
        Label warningLabel = (Label) calloutReport.get("warningLabel");

        try {
            officername.setText(ConfigReader.configRead("Name"));
            officerrank.setText(ConfigReader.configRead("Rank"));
            officerdiv.setText(ConfigReader.configRead("Division"));
            officeragen.setText(ConfigReader.configRead("Agency"));
            officernum.setText(ConfigReader.configRead("Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        calloutdate.setText(getDate());
        callouttime.setText(getTime());

        Button pullNotesBtn = (Button) calloutReport.get("pullNotesBtn");
        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(calloutarea, notesViewController.getNotepadTextArea(), "-area");
                updateTextFromNotepad(calloutcounty, notesViewController.getNotepadTextArea(), "-county");
                updateTextFromNotepad(calloutstreet, notesViewController.getNotepadTextArea(), "-street");
                updateTextFromNotepad(calloutnum, notesViewController.getNotepadTextArea(), "-number");
                updateTextFromNotepad(calloutnotes, notesViewController.getNotepadTextArea(), "-notes");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        Button submitBtn = (Button) calloutReport.get("submitBtn");
        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : calloutReportMap.keySet()) {
                Object field = calloutReportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
            }
            if (!allFieldsFilled) {
                warningLabel.setVisible(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        warningLabel.setVisible(false);
                    }
                }, 3000);
                return;
            }
            List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();

            logs.add(new CalloutLogEntry(
                    calloutdate.getText(),
                    callouttime.getText(),
                    officername.getText(),
                    officerrank.getText(),
                    officernum.getText(),
                    officerdiv.getText(),
                    officeragen.getText(),
                    callouttype.getText(),
                    calloutcode.getText(),
                    calloutnum.getText(),
                    calloutnotes.getText(),
                    calloutstreet.getText(),
                    calloutcounty.getText(),
                    calloutarea.getText()

            ));

            CalloutReportLogs.saveLogsToXML(logs);
            actionController.needRefresh.set(1);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            showNotification("Reports", "A new Callout Report has been submitted.", vbox);
            Stage rootstage = (Stage) root.getScene().getWindow();
            rootstage.close();
        });
    }


    static Map<String, Object> impoundLayout() {
        Map<String, Object> impoundReport = createReportWindow("Impound Report", 7, 9, null,
                new SectionConfig("Officer Information", true,
                        new RowConfig(
                                new FieldConfig("name", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("rank", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("number", 2, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("division", 6, FieldType.TEXT_FIELD),
                                new FieldConfig("agency", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Location / Timestamp Information", true,
                        new RowConfig(
                                new FieldConfig("date", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("time", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("citation number", 2, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Offender Information", true,
                        new RowConfig(
                                new FieldConfig("offender name", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("offender age", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("offender gender", 4, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("offender address", 12, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Offender Vehicle Information", true,
                        new RowConfig(
                                new FieldConfig("model", 6, FieldType.TEXT_FIELD),
                                new FieldConfig("plate number", 6, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("type", 7, FieldType.COMBO_BOX_TYPE),
                                new FieldConfig("color", 5, FieldType.COMBO_BOX_COLOR))
                ),
                new SectionConfig("Citation Notes", true,
                        new RowConfig(
                                new FieldConfig("notes", 12, FieldType.TEXT_AREA))
                ));
        return impoundReport;
    }

    public static void newImpound(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
        Map<String, Object> impoundReport = impoundLayout();

        Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");

        TextField officername = (TextField) impoundReportMap.get("name");
        TextField officerrank = (TextField) impoundReportMap.get("rank");
        TextField officerdiv = (TextField) impoundReportMap.get("division");
        TextField officeragen = (TextField) impoundReportMap.get("agency");
        TextField officernum = (TextField) impoundReportMap.get("number");

        TextField offenderName = (TextField) impoundReportMap.get("offender name");
        TextField offenderAge = (TextField) impoundReportMap.get("offender age");
        TextField offenderGender = (TextField) impoundReportMap.get("offender gender");
        TextField offenderAddress = (TextField) impoundReportMap.get("offender address");

        TextField num = (TextField) impoundReportMap.get("citation number");
        TextField date = (TextField) impoundReportMap.get("date");
        TextField time = (TextField) impoundReportMap.get("time");

        ComboBox color = (ComboBox) impoundReportMap.get("color");
        ComboBox type = (ComboBox) impoundReportMap.get("type");
        TextField plateNumber = (TextField) impoundReportMap.get("plate number");
        TextField model = (TextField) impoundReportMap.get("model");

        TextArea notes = (TextArea) impoundReportMap.get("notes");

        BorderPane root = (BorderPane) impoundReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        Label warningLabel = (Label) impoundReport.get("warningLabel");
        Button pullNotesBtn = (Button) impoundReport.get("pullNotesBtn");

        try {
            officername.setText(ConfigReader.configRead("Name"));
            officerrank.setText(ConfigReader.configRead("Rank"));
            officerdiv.setText(ConfigReader.configRead("Division"));
            officeragen.setText(ConfigReader.configRead("Agency"));
            officernum.setText(ConfigReader.configRead("Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        date.setText(getDate());
        time.setText(getTime());

        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(offenderName, notesViewController.getNotepadTextArea(), "-name");
                updateTextFromNotepad(offenderAge, notesViewController.getNotepadTextArea(), "-age");
                updateTextFromNotepad(offenderGender, notesViewController.getNotepadTextArea(), "-gender");
                updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
                updateTextFromNotepad(offenderAddress, notesViewController.getNotepadTextArea(), "-address");
                updateTextFromNotepad(model, notesViewController.getNotepadTextArea(), "-model");
                updateTextFromNotepad(plateNumber, notesViewController.getNotepadTextArea(), "-plate");
                updateTextFromNotepad(num, notesViewController.getNotepadTextArea(), "-number");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        Button submitBtn = (Button) impoundReport.get("submitBtn");
        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : impoundReportMap.keySet()) {
                Object field = impoundReportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
            }
            if (!allFieldsFilled) {
                warningLabel.setVisible(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        warningLabel.setVisible(false);
                    }
                }, 3000);
            } else {
                List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();

                // Add new entry
                logs.add(new ImpoundLogEntry(
                        num.getText(),
                        date.getText(),
                        time.getText(),
                        offenderName.getText(),
                        offenderAge.getText(),
                        offenderGender.getText(),
                        offenderAddress.getText(),
                        plateNumber.getText(),
                        model.getText(),
                        type.getValue().toString(),
                        color.getValue().toString(),
                        notes.getText(),
                        officerrank.getText(),
                        officername.getText(),
                        officernum.getText(),
                        officerdiv.getText(),
                        officeragen.getText()
                ));
                ImpoundReportLogs.saveLogsToXML(logs);
                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                controllerUtils.refreshChart(areaReportChart, "area");
                showNotification("Reports", "A new Impound Report has been submitted.", vbox);
                stage.close();
            }
        });
    }


    static Map<String, Object> patrolLayout() {
        Map<String, Object> patrolReport = createReportWindow("Patrol Report", 5, 7, null,
                new SectionConfig("Officer Information", true,
                        new RowConfig(new FieldConfig("name", 5, FieldType.TEXT_FIELD), new FieldConfig("rank", 5, FieldType.TEXT_FIELD), new FieldConfig("number", 2, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("division", 6, FieldType.TEXT_FIELD), new FieldConfig("agency", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Shift Information", true,
                        new RowConfig(new FieldConfig("starttime", 3, FieldType.TEXT_FIELD), new FieldConfig("stoptime", 4, FieldType.TEXT_FIELD), new FieldConfig("patrolnumber", 5, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("length", 3, FieldType.TEXT_FIELD), new FieldConfig("date", 3, FieldType.TEXT_FIELD), new FieldConfig("vehicle", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Callout Notes", true,
                        new RowConfig(new FieldConfig("notes", 12, FieldType.TEXT_AREA))
                )
        );
        return patrolReport;
    }

    public static void newPatrol(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
        Map<String, Object> patrolReport = patrolLayout();

        Map<String, Object> patrolReportMap = (Map<String, Object>) patrolReport.get("Patrol Report Map");

        TextField name = (TextField) patrolReportMap.get("name");
        TextField rank = (TextField) patrolReportMap.get("rank");
        TextField div = (TextField) patrolReportMap.get("division");
        TextField agen = (TextField) patrolReportMap.get("agency");
        TextField num = (TextField) patrolReportMap.get("number");
        TextField patrolnum = (TextField) patrolReportMap.get("patrolnumber");
        TextArea notes = (TextArea) patrolReportMap.get("notes");
        TextField date = (TextField) patrolReportMap.get("date");
        TextField starttime = (TextField) patrolReportMap.get("starttime");
        TextField stoptime = (TextField) patrolReportMap.get("stoptime");
        TextField length = (TextField) patrolReportMap.get("length");
        TextField vehicle = (TextField) patrolReportMap.get("vehicle");

        BorderPane root = (BorderPane) patrolReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        Label warningLabel = (Label) patrolReport.get("warningLabel");

        try {
            name.setText(ConfigReader.configRead("Name"));
            rank.setText(ConfigReader.configRead("Rank"));
            div.setText(ConfigReader.configRead("Division"));
            agen.setText(ConfigReader.configRead("Agency"));
            num.setText(ConfigReader.configRead("Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stoptime.setText(getTime());

        Button pullNotesBtn = (Button) patrolReport.get("pullNotesBtn");

        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(patrolnum, notesViewController.getNotepadTextArea(), "-number");
                updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        Button submitBtn = (Button) patrolReport.get("submitBtn");

        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : patrolReportMap.keySet()) {
                Object field = patrolReportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
            }
            if (!allFieldsFilled) {
                warningLabel.setVisible(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        warningLabel.setVisible(false);
                    }
                }, 3000);
                return;
            }

            List<PatrolLogEntry> logs = PatrolReportLogs.loadLogsFromXML();

            logs.add(new PatrolLogEntry(
                    patrolnum.getText(),
                    date.getText(),
                    length.getText(),
                    starttime.getText(),
                    stoptime.getText(),
                    rank.getText(),
                    name.getText(),
                    num.getText(),
                    div.getText(),
                    agen.getText(),
                    vehicle.getText(),
                    notes.getText()
            ));

            PatrolReportLogs.saveLogsToXML(logs);
            actionController.needRefresh.set(1);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            showNotification("Reports", "A new Patrol Report has been submitted.", vbox);

            stage.close();
        });
    }


    static Map<String, Object> citationLayout() {
        Map<String, Object> citationReport = createReportWindow("Citation Report", 7, 9,
                new TransferConfig("Transfer Information To New Report",
                        new RowConfig(
                                new FieldConfig("transferimpoundbtn", 12, FieldType.TRANSFER_BUTTON))
                ),
                new SectionConfig("Officer Information", true,
                        new RowConfig(
                                new FieldConfig("name", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("rank", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("number", 2, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("division", 6, FieldType.TEXT_FIELD),
                                new FieldConfig("agency", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Location / Timestamp Information", true,
                        new RowConfig(
                                new FieldConfig("street", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("area", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("county", 4, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("date", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("time", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("citation number", 2, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Offender Information", true,
                        new RowConfig(
                                new FieldConfig("offender name", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("offender age", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("offender gender", 4, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("offender address", 6, FieldType.TEXT_FIELD),
                                new FieldConfig("offender description", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("(If Applicable) Offender Vehicle Information", false,
                        new RowConfig(
                                new FieldConfig("model", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("plate number", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("color", 4, FieldType.COMBO_BOX_COLOR)),
                        new RowConfig(
                                new FieldConfig("type", 4, FieldType.COMBO_BOX_TYPE),
                                new FieldConfig("other info", 8, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Citation Notes", true,
                        new RowConfig(
                                new FieldConfig("notes", 12, FieldType.TEXT_AREA))
                ),
                new SectionConfig("Citation(s)", true,
                        new RowConfig(
                                new FieldConfig("citationview", 6, FieldType.CITATION_TREE_VIEW))
                ));
        return citationReport;
    }

    public static void newCitation(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
        Map<String, Object> citationReport = citationLayout();

        Map<String, Object> citationReportMap = (Map<String, Object>) citationReport.get("Citation Report Map");

        TextField officername = (TextField) citationReportMap.get("name");
        TextField officerrank = (TextField) citationReportMap.get("rank");
        TextField officerdiv = (TextField) citationReportMap.get("division");
        TextField officeragen = (TextField) citationReportMap.get("agency");
        TextField officernum = (TextField) citationReportMap.get("number");

        TextField offenderName = (TextField) citationReportMap.get("offender name");
        TextField offenderAge = (TextField) citationReportMap.get("offender age");
        TextField offenderGender = (TextField) citationReportMap.get("offender gender");
        TextField offenderAddress = (TextField) citationReportMap.get("offender address");
        TextField offenderDescription = (TextField) citationReportMap.get("offender description");

        TextField area = (TextField) citationReportMap.get("area");
        TextField street = (TextField) citationReportMap.get("street");
        TextField county = (TextField) citationReportMap.get("county");
        TextField num = (TextField) citationReportMap.get("citation number");
        TextField date = (TextField) citationReportMap.get("date");
        TextField time = (TextField) citationReportMap.get("time");

        ComboBox color = (ComboBox) citationReportMap.get("color");
        ComboBox type = (ComboBox) citationReportMap.get("type");
        TextField plateNumber = (TextField) citationReportMap.get("plate number");
        TextField otherInfo = (TextField) citationReportMap.get("other info");
        TextField model = (TextField) citationReportMap.get("model");

        TextArea notes = (TextArea) citationReportMap.get("notes");

        TreeView citationtreeview = (TreeView) citationReportMap.get("citationview");
        TableView citationtable = (TableView) citationReportMap.get("CitationTableView");

        Button transferimpoundbtn = (Button) citationReportMap.get("transferimpoundbtn");
        transferimpoundbtn.setText("New Impound Report");

        BorderPane root = (BorderPane) citationReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        Label warningLabel = (Label) citationReport.get("warningLabel");
        Button pullNotesBtn = (Button) citationReport.get("pullNotesBtn");

        try {
            officername.setText(ConfigReader.configRead("Name"));
            officerrank.setText(ConfigReader.configRead("Rank"));
            officerdiv.setText(ConfigReader.configRead("Division"));
            officeragen.setText(ConfigReader.configRead("Agency"));
            officernum.setText(ConfigReader.configRead("Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        date.setText(getDate());
        time.setText(getTime());

        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(area, notesViewController.getNotepadTextArea(), "-area");
                updateTextFromNotepad(county, notesViewController.getNotepadTextArea(), "-county");
                updateTextFromNotepad(street, notesViewController.getNotepadTextArea(), "-street");
                updateTextFromNotepad(offenderName, notesViewController.getNotepadTextArea(), "-name");
                updateTextFromNotepad(offenderAge, notesViewController.getNotepadTextArea(), "-age");
                updateTextFromNotepad(offenderGender, notesViewController.getNotepadTextArea(), "-gender");
                updateTextFromNotepad(offenderDescription, notesViewController.getNotepadTextArea(), "-description");
                updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
                updateTextFromNotepad(offenderAddress, notesViewController.getNotepadTextArea(), "-address");
                updateTextFromNotepad(model, notesViewController.getNotepadTextArea(), "-model");
                updateTextFromNotepad(plateNumber, notesViewController.getNotepadTextArea(), "-plate");
                updateTextFromNotepad(num, notesViewController.getNotepadTextArea(), "-number");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        transferimpoundbtn.setOnAction(event -> {

            Map<String, Object> impoundReport = impoundLayout();

            Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");

            TextField officernameimp = (TextField) impoundReportMap.get("name");
            TextField officerrankimp = (TextField) impoundReportMap.get("rank");
            TextField officerdivimp = (TextField) impoundReportMap.get("division");
            TextField officeragenimp = (TextField) impoundReportMap.get("agency");
            TextField officernumimp = (TextField) impoundReportMap.get("number");

            TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
            TextField offenderAgeimp = (TextField) impoundReportMap.get("offender age");
            TextField offenderGenderimp = (TextField) impoundReportMap.get("offender gender");
            TextField offenderAddressimp = (TextField) impoundReportMap.get("offender address");

            TextField numimp = (TextField) impoundReportMap.get("citation number");
            TextField dateimp = (TextField) impoundReportMap.get("date");
            TextField timeimp = (TextField) impoundReportMap.get("time");

            ComboBox colorimp = (ComboBox) impoundReportMap.get("color");
            ComboBox typeimp = (ComboBox) impoundReportMap.get("type");
            TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
            TextField modelimp = (TextField) impoundReportMap.get("model");

            TextArea notesimp = (TextArea) impoundReportMap.get("notes");

            BorderPane rootimp = (BorderPane) impoundReport.get("root");
            Stage stageimp = (Stage) rootimp.getScene().getWindow();

            if (!stageimp.isFocused()) {
                stageimp.requestFocus();
            }

            Label warningLabelimp = (Label) impoundReport.get("warningLabel");
            Button pullNotesBtnimp = (Button) impoundReport.get("pullNotesBtn");

            officernameimp.setText(officername.getText());
            officerdivimp.setText(officerdiv.getText());
            officerrankimp.setText(officerrank.getText());
            officeragenimp.setText(officeragen.getText());
            officernumimp.setText(officernum.getText());
            timeimp.setText(time.getText());
            dateimp.setText(date.getText());
            offenderAddressimp.setText(offenderAddress.getText());
            offenderNameimp.setText(offenderName.getText());
            offenderAgeimp.setText(offenderAge.getText());
            offenderGenderimp.setText(offenderGender.getText());
            plateNumberimp.setText(plateNumber.getText());
            notesimp.setText(notes.getText());
            modelimp.setText(model.getText());
            typeimp.getSelectionModel().select(type.getSelectionModel().getSelectedItem());
            colorimp.getSelectionModel().select(color.getSelectionModel().getSelectedItem());
            numimp.setText(num.getText());

            pullNotesBtnimp.setOnAction(event1 -> {
                if (notesViewController != null) {
                    updateTextFromNotepad(offenderNameimp, notesViewController.getNotepadTextArea(), "-name");
                    updateTextFromNotepad(offenderAgeimp, notesViewController.getNotepadTextArea(), "-age");
                    updateTextFromNotepad(offenderGenderimp, notesViewController.getNotepadTextArea(), "-gender");
                    updateTextFromNotepad(notesimp, notesViewController.getNotepadTextArea(), "-comments");
                    updateTextFromNotepad(offenderAddressimp, notesViewController.getNotepadTextArea(), "-address");
                    updateTextFromNotepad(modelimp, notesViewController.getNotepadTextArea(), "-model");
                    updateTextFromNotepad(plateNumberimp, notesViewController.getNotepadTextArea(), "-plate");
                    updateTextFromNotepad(numimp, notesViewController.getNotepadTextArea(), "-number");
                } else {
                    log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                }
            });

            Button submitBtnimp = (Button) impoundReport.get("submitBtn");
            submitBtnimp.setOnAction(event2 -> {
                boolean allFieldsFilled = true;
                for (String fieldName : impoundReportMap.keySet()) {
                    Object field = impoundReportMap.get(fieldName);
                    if (field instanceof ComboBox<?> comboBox) {
                        if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                            allFieldsFilled = false;
                            break;
                        }
                    }
                }
                if (!allFieldsFilled) {
                    warningLabelimp.setVisible(true);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            warningLabelimp.setVisible(false);
                        }
                    }, 3000);
                } else {
                    List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();

                    // Add new entry
                    logs.add(new ImpoundLogEntry(
                            numimp.getText(),
                            dateimp.getText(),
                            timeimp.getText(),
                            offenderNameimp.getText(),
                            offenderAgeimp.getText(),
                            offenderGenderimp.getText(),
                            offenderAddressimp.getText(),
                            plateNumberimp.getText(),
                            modelimp.getText(),
                            typeimp.getValue().toString(),
                            colorimp.getValue().toString(),
                            notesimp.getText(),
                            officerrankimp.getText(),
                            officernameimp.getText(),
                            officernumimp.getText(),
                            officerdivimp.getText(),
                            officeragenimp.getText()
                    ));
                    ImpoundReportLogs.saveLogsToXML(logs);
                    actionController.needRefresh.set(1);
                    updateChartIfMismatch(reportChart);
                    controllerUtils.refreshChart(areaReportChart, "area");
                    showNotification("Reports", "A new Impound Report has been submitted.", vbox);
                    stageimp.close();
                }
            });
        });

        Button submitBtn = (Button) citationReport.get("submitBtn");
        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : citationReportMap.keySet()) {
                Object field = citationReportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
            }
            if (!allFieldsFilled) {
                warningLabel.setVisible(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        warningLabel.setVisible(false);
                    }
                }, 3000);
            } else {
                List<TrafficCitationLogEntry> logs = TrafficCitationReportLogs.loadLogsFromXML();
                ObservableList<CitationsData> formDataList = citationtable.getItems();
                StringBuilder stringBuilder = new StringBuilder();
                for (CitationsData formData : formDataList) {
                    stringBuilder.append(formData.getCitation()).append(" | ");
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.setLength(stringBuilder.length() - 2);
                }

                logs.add(new TrafficCitationLogEntry(
                        num.getText(),
                        date.getText(),
                        time.getText(),
                        stringBuilder.toString(),
                        county.getText(),
                        area.getText(),
                        street.getText(),
                        offenderName.getText(),
                        offenderGender.getText(),
                        offenderAge.getText(),
                        offenderAddress.getText(),
                        offenderDescription.getText(),
                        model.getText(),
                        color.getValue().toString(),
                        type.getValue().toString(),
                        plateNumber.getText(),
                        otherInfo.getText(),
                        officerrank.getText(),
                        officername.getText(),
                        officernum.getText(),
                        officerdiv.getText(),
                        officeragen.getText(),
                        notes.getText()
                ));
                TrafficCitationReportLogs.saveLogsToXML(logs);
                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                controllerUtils.refreshChart(areaReportChart, "area");
                showNotification("Reports", "A new Citation Report has been submitted.", vbox);
                stage.close();
            }
        });
    }


    static Map<String, Object> incidentLayout() {
        Map<String, Object> incidentReport = createReportWindow("Incident Report", 5, 7, null,
                new SectionConfig("Officer Information", true,
                        new RowConfig(new FieldConfig("name", 5, FieldType.TEXT_FIELD), new FieldConfig("rank", 5, FieldType.TEXT_FIELD), new FieldConfig("number", 2, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("division", 6, FieldType.TEXT_FIELD), new FieldConfig("agency", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Timestamp / Location Information", true,
                        new RowConfig(new FieldConfig("date", 3, FieldType.TEXT_FIELD), new FieldConfig("time", 4, FieldType.TEXT_FIELD), new FieldConfig("incident num", 5, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("street", 5, FieldType.TEXT_FIELD), new FieldConfig("area", 4, FieldType.TEXT_FIELD), new FieldConfig("county", 3, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Parties Involved", false,
                        new RowConfig(new FieldConfig("suspect(s)", 6, FieldType.TEXT_FIELD), new FieldConfig("victim(s) / witness(s)", 6, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("statement", 12, FieldType.TEXT_AREA))
                ),
                new SectionConfig("Notes / Summary", true,
                        new RowConfig(new FieldConfig("summary", 12, FieldType.TEXT_AREA)),
                        new RowConfig(new FieldConfig("notes", 12, FieldType.TEXT_AREA))
                )
        );
        return incidentReport;
    }

    public static void newIncident(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
        Map<String, Object> incidentReport = incidentLayout();

        Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReport.get("Incident Report Map");

        TextField name = (TextField) incidentReportMap.get("name");
        TextField rank = (TextField) incidentReportMap.get("rank");
        TextField div = (TextField) incidentReportMap.get("division");
        TextField agen = (TextField) incidentReportMap.get("agency");
        TextField num = (TextField) incidentReportMap.get("number");

        TextField incidentnum = (TextField) incidentReportMap.get("incident num");
        TextField date = (TextField) incidentReportMap.get("date");
        TextField time = (TextField) incidentReportMap.get("time");
        TextField street = (TextField) incidentReportMap.get("street");
        TextField area = (TextField) incidentReportMap.get("area");
        TextField county = (TextField) incidentReportMap.get("county");

        TextField suspects = (TextField) incidentReportMap.get("suspect(s)");
        TextField vicwit = (TextField) incidentReportMap.get("victim(s) / witness(s)");
        TextArea statement = (TextArea) incidentReportMap.get("statement");

        TextArea summary = (TextArea) incidentReportMap.get("summary");
        TextArea notes = (TextArea) incidentReportMap.get("notes");

        BorderPane root = (BorderPane) incidentReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        Label warningLabel = (Label) incidentReport.get("warningLabel");

        try {
            name.setText(ConfigReader.configRead("Name"));
            rank.setText(ConfigReader.configRead("Rank"));
            div.setText(ConfigReader.configRead("Division"));
            agen.setText(ConfigReader.configRead("Agency"));
            num.setText(ConfigReader.configRead("Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        date.setText(getDate());
        time.setText(getTime());

        Button pullNotesBtn = (Button) incidentReport.get("pullNotesBtn");

        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(area, notesViewController.getNotepadTextArea(), "-area");
                updateTextFromNotepad(county, notesViewController.getNotepadTextArea(), "-county");
                updateTextFromNotepad(street, notesViewController.getNotepadTextArea(), "-street");
                updateTextFromNotepad(vicwit, notesViewController.getNotepadTextArea(), "-name");
                updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
                updateTextFromNotepad(incidentnum, notesViewController.getNotepadTextArea(), "-number");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        Button submitBtn = (Button) incidentReport.get("submitBtn");

        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : incidentReportMap.keySet()) {
                Object field = incidentReportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
            }
            if (!allFieldsFilled) {
                warningLabel.setVisible(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        warningLabel.setVisible(false);
                    }
                }, 3000);
                return;
            }

            List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();

            logs.add(new IncidentLogEntry(
                    incidentnum.getText(),
                    date.getText(),
                    time.getText(),
                    statement.getText(),
                    suspects.getText(),
                    vicwit.getText(),
                    name.getText(),
                    rank.getText(),
                    num.getText(),
                    agen.getText(),
                    div.getText(),
                    street.getText(),
                    area.getText(),
                    county.getText(),
                    summary.getText(),
                    notes.getText()
            ));

            IncidentReportLogs.saveLogsToXML(logs);
            actionController.needRefresh.set(1);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            showNotification("Reports", "A new Incident Report has been submitted.", vbox);
            stage.close();
        });
    }


    static Map<String, Object> searchLayout() {
        Map<String, Object> searchReport = createReportWindow("Search Report", 5, 7, null,
                new SectionConfig("Officer Information", true,
                        new RowConfig(new FieldConfig("name", 5, FieldType.TEXT_FIELD), new FieldConfig("rank", 5, FieldType.TEXT_FIELD), new FieldConfig("number", 2, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("division", 6, FieldType.TEXT_FIELD), new FieldConfig("agency", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Timestamp / Location Information", true,
                        new RowConfig(new FieldConfig("date", 3, FieldType.TEXT_FIELD), new FieldConfig("time", 4, FieldType.TEXT_FIELD), new FieldConfig("search num", 5, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("street", 5, FieldType.TEXT_FIELD), new FieldConfig("area", 4, FieldType.TEXT_FIELD), new FieldConfig("county", 3, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Search Information", true,
                        new RowConfig(new FieldConfig("grounds for search", 6, FieldType.TEXT_FIELD), new FieldConfig("witness(s)", 6, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("searched individual", 12, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("search type", 6, FieldType.COMBO_BOX_SEARCH_TYPE), new FieldConfig("search method", 6, FieldType.COMBO_BOX_SEARCH_METHOD))
                ),
                new SectionConfig("Field Sobriety Information (If Applicable)", false,
                        new RowConfig(new FieldConfig("test(s) conducted", 4, FieldType.TEXT_FIELD), new FieldConfig("result", 4, FieldType.TEXT_FIELD), new FieldConfig("bac measurement", 4, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Notes / Summary", true,
                        new RowConfig(new FieldConfig("seized item(s)", 12, FieldType.TEXT_AREA)),
                        new RowConfig(new FieldConfig("comments", 12, FieldType.TEXT_AREA))
                )
        );
        return searchReport;
    }

    public static void newSearch(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
        Map<String, Object> searchReport = searchLayout();

        Map<String, Object> searchReportMap = (Map<String, Object>) searchReport.get("Search Report Map");

        TextField name = (TextField) searchReportMap.get("name");
        TextField rank = (TextField) searchReportMap.get("rank");
        TextField div = (TextField) searchReportMap.get("division");
        TextField agen = (TextField) searchReportMap.get("agency");
        TextField num = (TextField) searchReportMap.get("number");

        TextField searchnum = (TextField) searchReportMap.get("search num");
        TextField date = (TextField) searchReportMap.get("date");
        TextField time = (TextField) searchReportMap.get("time");
        TextField street = (TextField) searchReportMap.get("street");
        TextField area = (TextField) searchReportMap.get("area");
        TextField county = (TextField) searchReportMap.get("county");

        TextField grounds = (TextField) searchReportMap.get("grounds for search");
        TextField witness = (TextField) searchReportMap.get("witness(s)");
        TextField searchedindividual = (TextField) searchReportMap.get("searched individual");
        ComboBox type = (ComboBox) searchReportMap.get("search type");
        ComboBox method = (ComboBox) searchReportMap.get("search method");

        TextField testconducted = (TextField) searchReportMap.get("test(s) conducted");
        TextField result = (TextField) searchReportMap.get("result");
        TextField bacmeasurement = (TextField) searchReportMap.get("bac measurement");

        TextArea seizeditems = (TextArea) searchReportMap.get("seized item(s)");
        TextArea notes = (TextArea) searchReportMap.get("comments");

        BorderPane root = (BorderPane) searchReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        Label warningLabel = (Label) searchReport.get("warningLabel");

        try {
            name.setText(ConfigReader.configRead("Name"));
            rank.setText(ConfigReader.configRead("Rank"));
            div.setText(ConfigReader.configRead("Division"));
            agen.setText(ConfigReader.configRead("Agency"));
            num.setText(ConfigReader.configRead("Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        date.setText(getDate());
        time.setText(getTime());

        Button pullNotesBtn = (Button) searchReport.get("pullNotesBtn");

        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(area, notesViewController.getNotepadTextArea(), "-area");
                updateTextFromNotepad(county, notesViewController.getNotepadTextArea(), "-county");
                updateTextFromNotepad(street, notesViewController.getNotepadTextArea(), "-street");
                updateTextFromNotepad(searchedindividual, notesViewController.getNotepadTextArea(), "-name");
                updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
                updateTextFromNotepad(searchnum, notesViewController.getNotepadTextArea(), "-number");
                updateTextFromNotepad(seizeditems, notesViewController.getNotepadTextArea(), "-searchitems");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });

        Button submitBtn = (Button) searchReport.get("submitBtn");

        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : searchReportMap.keySet()) {
                Object field = searchReportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
            }
            if (!allFieldsFilled) {
                warningLabel.setVisible(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        warningLabel.setVisible(false);
                    }
                }, 3000);
                return;
            }

            List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();

            logs.add(new SearchLogEntry(
                    searchnum.getText(),
                    searchedindividual.getText(),
                    date.getText(),
                    time.getText(),
                    seizeditems.getText(),
                    grounds.getText(),
                    type.getValue().toString(),
                    method.getValue().toString(),
                    witness.getText(),
                    rank.getText(),
                    name.getText(),
                    num.getText(),
                    agen.getText(),
                    div.getText(),
                    street.getText(),
                    area.getText(),
                    county.getText(),
                    notes.getText(),
                    testconducted.getText(),
                    result.getText(),
                    bacmeasurement.getText()
            ));

            SearchReportLogs.saveLogsToXML(logs);
            actionController.needRefresh.set(1);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            showNotification("Reports", "A new Search Report has been submitted.", vbox);
            stage.close();
        });
    }


    static Map<String, Object> arrestLayout() {
        Map<String, Object> arrestReport = createReportWindow("Arrest Report", 7, 9,
                new TransferConfig("Transfer Information To New Report",
                        new RowConfig(
                                new FieldConfig("transferimpoundbtn", 4, FieldType.TRANSFER_BUTTON),
                                new FieldConfig("transferincidentbtn", 4, FieldType.TRANSFER_BUTTON),
                                new FieldConfig("transfersearchbtn", 4, FieldType.TRANSFER_BUTTON))
                ),
                new SectionConfig("Officer Information", true,
                        new RowConfig(
                                new FieldConfig("name", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("rank", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("number", 2, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("division", 6, FieldType.TEXT_FIELD),
                                new FieldConfig("agency", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Location / Timestamp Information", true,
                        new RowConfig(
                                new FieldConfig("street", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("area", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("county", 4, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("date", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("time", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("arrest number", 2, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Offender Information", true,
                        new RowConfig(
                                new FieldConfig("offender name", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("offender age", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("offender gender", 4, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("offender address", 6, FieldType.TEXT_FIELD),
                                new FieldConfig("offender description", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("(If Applicable) Offender Medical Information", false,
                        new RowConfig(
                                new FieldConfig("ambulance required (Y/N)", 6, FieldType.TEXT_FIELD),
                                new FieldConfig("taser deployed (Y/N)", 6, FieldType.TEXT_FIELD))
                        ,
                        new RowConfig(
                                new FieldConfig("other information", 12, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Charge Notes", true,
                        new RowConfig(
                                new FieldConfig("notes", 12, FieldType.TEXT_AREA))
                ),
                new SectionConfig("Charge(s)", true,
                        new RowConfig(
                                new FieldConfig("chargeview", 6, FieldType.CHARGES_TREE_VIEW))
                ));
        return arrestReport;
    }

    public static void newArrest(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
        Map<String, Object> arrestReport = arrestLayout();

        Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReport.get("Arrest Report Map");

        TextField officername = (TextField) arrestReportMap.get("name");
        TextField officerrank = (TextField) arrestReportMap.get("rank");
        TextField officerdiv = (TextField) arrestReportMap.get("division");
        TextField officeragen = (TextField) arrestReportMap.get("agency");
        TextField officernumarrest = (TextField) arrestReportMap.get("number");

        TextField offenderName = (TextField) arrestReportMap.get("offender name");
        TextField offenderAge = (TextField) arrestReportMap.get("offender age");
        TextField offenderGender = (TextField) arrestReportMap.get("offender gender");
        TextField offenderAddress = (TextField) arrestReportMap.get("offender address");
        TextField offenderDescription = (TextField) arrestReportMap.get("offender description");

        TextField area = (TextField) arrestReportMap.get("area");
        TextField street = (TextField) arrestReportMap.get("street");
        TextField county = (TextField) arrestReportMap.get("county");
        TextField arrestnum = (TextField) arrestReportMap.get("arrest number");
        TextField date = (TextField) arrestReportMap.get("date");
        TextField time = (TextField) arrestReportMap.get("time");

        TextField ambulancereq = (TextField) arrestReportMap.get("ambulance required (Y/N)");
        TextField taserdep = (TextField) arrestReportMap.get("taser deployed (Y/N)");
        TextField othermedinfo = (TextField) arrestReportMap.get("other information");

        TextArea notes = (TextArea) arrestReportMap.get("notes");

        TreeView chargetreeview = (TreeView) arrestReportMap.get("chargeview");
        TableView chargetable = (TableView) arrestReportMap.get("ChargeTableView");

        Button transferimpoundbtn = (Button) arrestReportMap.get("transferimpoundbtn");
        transferimpoundbtn.setText("New Impound Report");
        Button transferincidentbtn = (Button) arrestReportMap.get("transferincidentbtn");
        transferincidentbtn.setText("New Incident Report");
        Button transfersearchbtn = (Button) arrestReportMap.get("transfersearchbtn");
        transfersearchbtn.setText("New Search Report");

        BorderPane root = (BorderPane) arrestReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        Label warningLabel = (Label) arrestReport.get("warningLabel");
        Button pullNotesBtn = (Button) arrestReport.get("pullNotesBtn");

        try {
            officername.setText(ConfigReader.configRead("Name"));
            officerrank.setText(ConfigReader.configRead("Rank"));
            officerdiv.setText(ConfigReader.configRead("Division"));
            officeragen.setText(ConfigReader.configRead("Agency"));
            officernumarrest.setText(ConfigReader.configRead("Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        date.setText(getDate());
        time.setText(getTime());

        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(area, notesViewController.getNotepadTextArea(), "-area");
                updateTextFromNotepad(county, notesViewController.getNotepadTextArea(), "-county");
                updateTextFromNotepad(street, notesViewController.getNotepadTextArea(), "-street");
                updateTextFromNotepad(offenderName, notesViewController.getNotepadTextArea(), "-name");
                updateTextFromNotepad(offenderAge, notesViewController.getNotepadTextArea(), "-age");
                updateTextFromNotepad(offenderGender, notesViewController.getNotepadTextArea(), "-gender");
                updateTextFromNotepad(offenderDescription, notesViewController.getNotepadTextArea(), "-description");
                updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
                updateTextFromNotepad(offenderAddress, notesViewController.getNotepadTextArea(), "-address");
                updateTextFromNotepad(arrestnum, notesViewController.getNotepadTextArea(), "-number");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });


        transferimpoundbtn.setOnAction(event -> {

            Map<String, Object> impoundReport = impoundLayout();

            Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");

            TextField officernameimp = (TextField) impoundReportMap.get("name");
            TextField officerrankimp = (TextField) impoundReportMap.get("rank");
            TextField officerdivimp = (TextField) impoundReportMap.get("division");
            TextField officeragenimp = (TextField) impoundReportMap.get("agency");
            TextField officernumimp = (TextField) impoundReportMap.get("number");

            TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
            TextField offenderAgeimp = (TextField) impoundReportMap.get("offender age");
            TextField offenderGenderimp = (TextField) impoundReportMap.get("offender gender");
            TextField offenderAddressimp = (TextField) impoundReportMap.get("offender address");

            TextField numimp = (TextField) impoundReportMap.get("citation number");
            TextField dateimp = (TextField) impoundReportMap.get("date");
            TextField timeimp = (TextField) impoundReportMap.get("time");

            ComboBox colorimp = (ComboBox) impoundReportMap.get("color");
            ComboBox typeimp = (ComboBox) impoundReportMap.get("type");
            TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
            TextField modelimp = (TextField) impoundReportMap.get("model");

            TextArea notesimp = (TextArea) impoundReportMap.get("notes");

            BorderPane rootimp = (BorderPane) impoundReport.get("root");
            Stage stageimp = (Stage) rootimp.getScene().getWindow();

            if (!stageimp.isFocused()) {
                stageimp.requestFocus();
            }

            Label warningLabelimp = (Label) impoundReport.get("warningLabel");
            Button pullNotesBtnimp = (Button) impoundReport.get("pullNotesBtn");

            officernameimp.setText(officername.getText());
            officerdivimp.setText(officerdiv.getText());
            officerrankimp.setText(officerrank.getText());
            officeragenimp.setText(officeragen.getText());
            officernumimp.setText(officernumarrest.getText());
            timeimp.setText(time.getText());
            dateimp.setText(date.getText());
            offenderAddressimp.setText(offenderAddress.getText());
            offenderNameimp.setText(offenderName.getText());
            offenderAgeimp.setText(offenderAge.getText());
            offenderGenderimp.setText(offenderGender.getText());
            notesimp.setText(notes.getText());
            numimp.setText(arrestnum.getText());

            pullNotesBtnimp.setOnAction(event1 -> {
                if (notesViewController != null) {
                    updateTextFromNotepad(offenderNameimp, notesViewController.getNotepadTextArea(), "-name");
                    updateTextFromNotepad(offenderAgeimp, notesViewController.getNotepadTextArea(), "-age");
                    updateTextFromNotepad(offenderGenderimp, notesViewController.getNotepadTextArea(), "-gender");
                    updateTextFromNotepad(notesimp, notesViewController.getNotepadTextArea(), "-comments");
                    updateTextFromNotepad(offenderAddressimp, notesViewController.getNotepadTextArea(), "-address");
                    updateTextFromNotepad(modelimp, notesViewController.getNotepadTextArea(), "-model");
                    updateTextFromNotepad(plateNumberimp, notesViewController.getNotepadTextArea(), "-plate");
                    updateTextFromNotepad(numimp, notesViewController.getNotepadTextArea(), "-number");
                } else {
                    log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                }
            });

            Button submitBtnimp = (Button) impoundReport.get("submitBtn");
            submitBtnimp.setOnAction(event2 -> {
                boolean allFieldsFilled = true;
                for (String fieldName : impoundReportMap.keySet()) {
                    Object field = impoundReportMap.get(fieldName);
                    if (field instanceof ComboBox<?> comboBox) {
                        if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                            allFieldsFilled = false;
                            break;
                        }
                    }
                }
                if (!allFieldsFilled) {
                    warningLabelimp.setVisible(true);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            warningLabelimp.setVisible(false);
                        }
                    }, 3000);
                } else {
                    List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();

                    // Add new entry
                    logs.add(new ImpoundLogEntry(
                            numimp.getText(),
                            dateimp.getText(),
                            timeimp.getText(),
                            offenderNameimp.getText(),
                            offenderAgeimp.getText(),
                            offenderGenderimp.getText(),
                            offenderAddressimp.getText(),
                            plateNumberimp.getText(),
                            modelimp.getText(),
                            typeimp.getValue().toString(),
                            colorimp.getValue().toString(),
                            notesimp.getText(),
                            officerrankimp.getText(),
                            officernameimp.getText(),
                            officernumimp.getText(),
                            officerdivimp.getText(),
                            officeragenimp.getText()
                    ));
                    ImpoundReportLogs.saveLogsToXML(logs);
                    actionController.needRefresh.set(1);
                    updateChartIfMismatch(reportChart);
                    controllerUtils.refreshChart(areaReportChart, "area");
                    showNotification("Reports", "A new Impound Report has been submitted.", vbox);
                    stageimp.close();
                }
            });
        });

        transferincidentbtn.setOnAction(event -> {
            Map<String, Object> incidentReport = incidentLayout();

            Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReport.get("Incident Report Map");

            TextField nameinc = (TextField) incidentReportMap.get("name");
            TextField rankinc = (TextField) incidentReportMap.get("rank");
            TextField divinc = (TextField) incidentReportMap.get("division");
            TextField ageninc = (TextField) incidentReportMap.get("agency");
            TextField officernuminc = (TextField) incidentReportMap.get("number");

            TextField incidentnum = (TextField) incidentReportMap.get("incident num");
            TextField dateinc = (TextField) incidentReportMap.get("date");
            TextField timeinc = (TextField) incidentReportMap.get("time");
            TextField streetinc = (TextField) incidentReportMap.get("street");
            TextField areainc = (TextField) incidentReportMap.get("area");
            TextField countyinc = (TextField) incidentReportMap.get("county");

            TextField suspectsinc = (TextField) incidentReportMap.get("suspect(s)");
            TextField vicwitinc = (TextField) incidentReportMap.get("victim(s) / witness(s)");
            TextArea statementinc = (TextArea) incidentReportMap.get("statement");

            TextArea summaryinc = (TextArea) incidentReportMap.get("summary");
            TextArea notesinc = (TextArea) incidentReportMap.get("notes");

            BorderPane rootinc = (BorderPane) incidentReport.get("root");
            Stage stageinc = (Stage) rootinc.getScene().getWindow();

            if (!stageinc.isFocused()) {
                stageinc.requestFocus();
            }

            Label warningLabelinc = (Label) incidentReport.get("warningLabel");

            nameinc.setText(officername.getText());
            divinc.setText(officerdiv.getText());
            rankinc.setText(officerrank.getText());
            ageninc.setText(officeragen.getText());
            officernuminc.setText(officernumarrest.getText());
            dateinc.setText(date.getText());
            timeinc.setText(time.getText());
            incidentnum.setText(arrestnum.getText());
            countyinc.setText(county.getText());
            areainc.setText(area.getText());
            streetinc.setText(street.getText());
            suspectsinc.setText(offenderName.getText());
            notesinc.setText(notes.getText());

            Button pullNotesBtninc = (Button) incidentReport.get("pullNotesBtn");

            pullNotesBtninc.setOnAction(event1 -> {
                if (notesViewController != null) {
                    updateTextFromNotepad(areainc, notesViewController.getNotepadTextArea(), "-area");
                    updateTextFromNotepad(countyinc, notesViewController.getNotepadTextArea(), "-county");
                    updateTextFromNotepad(streetinc, notesViewController.getNotepadTextArea(), "-street");
                    updateTextFromNotepad(suspectsinc, notesViewController.getNotepadTextArea(), "-name");
                    updateTextFromNotepad(notesinc, notesViewController.getNotepadTextArea(), "-comments");
                    updateTextFromNotepad(officernuminc, notesViewController.getNotepadTextArea(), "-number");
                } else {
                    log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                }
            });

            Button submitBtninc = (Button) incidentReport.get("submitBtn");

            submitBtninc.setOnAction(event2 -> {
                boolean allFieldsFilled = true;
                for (String fieldName : incidentReportMap.keySet()) {
                    Object field = incidentReportMap.get(fieldName);
                    if (field instanceof ComboBox<?> comboBox) {
                        if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                            allFieldsFilled = false;
                            break;
                        }
                    }
                }
                if (!allFieldsFilled) {
                    warningLabelinc.setVisible(true);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            warningLabelinc.setVisible(false);
                        }
                    }, 3000);
                    return;
                }

                List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();

                logs.add(new IncidentLogEntry(
                        incidentnum.getText(),
                        dateinc.getText(),
                        timeinc.getText(),
                        statementinc.getText(),
                        suspectsinc.getText(),
                        vicwitinc.getText(),
                        nameinc.getText(),
                        rankinc.getText(),
                        officernuminc.getText(),
                        ageninc.getText(),
                        divinc.getText(),
                        streetinc.getText(),
                        areainc.getText(),
                        countyinc.getText(),
                        summaryinc.getText(),
                        notesinc.getText()
                ));
                IncidentReportLogs.saveLogsToXML(logs);
                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                controllerUtils.refreshChart(areaReportChart, "area");
                showNotification("Reports", "A new Incident Report has been submitted.", vbox);
                stageinc.close();
            });
        });

        transfersearchbtn.setOnAction(event -> {
            Map<String, Object> searchReport = searchLayout();

            Map<String, Object> searchReportMap = (Map<String, Object>) searchReport.get("Search Report Map");

            TextField namesrch = (TextField) searchReportMap.get("name");
            TextField ranksrch = (TextField) searchReportMap.get("rank");
            TextField divsrch = (TextField) searchReportMap.get("division");
            TextField agensrch = (TextField) searchReportMap.get("agency");
            TextField numsrch = (TextField) searchReportMap.get("number");

            TextField searchnum = (TextField) searchReportMap.get("search num");
            TextField datesrch = (TextField) searchReportMap.get("date");
            TextField timesrch = (TextField) searchReportMap.get("time");
            TextField streetsrch = (TextField) searchReportMap.get("street");
            TextField areasrch = (TextField) searchReportMap.get("area");
            TextField countysrch = (TextField) searchReportMap.get("county");

            TextField groundssrch = (TextField) searchReportMap.get("grounds for search");
            TextField witnesssrch = (TextField) searchReportMap.get("witness(s)");
            TextField searchedindividualsrch = (TextField) searchReportMap.get("searched individual");
            ComboBox typesrch = (ComboBox) searchReportMap.get("search type");
            ComboBox methodsrch = (ComboBox) searchReportMap.get("search method");

            TextField testconductedsrch = (TextField) searchReportMap.get("test(s) conducted");
            TextField resultsrch = (TextField) searchReportMap.get("result");
            TextField bacmeasurementsrch = (TextField) searchReportMap.get("bac measurement");

            TextArea seizeditemssrch = (TextArea) searchReportMap.get("seized item(s)");
            TextArea notessrch = (TextArea) searchReportMap.get("comments");

            BorderPane rootsrch = (BorderPane) searchReport.get("root");
            Stage stagesrch = (Stage) rootsrch.getScene().getWindow();

            if (!stagesrch.isFocused()) {
                stagesrch.requestFocus();
            }

            searchnum.setText(arrestnum.getText());
            namesrch.setText(officername.getText());
            divsrch.setText(officerdiv.getText());
            ranksrch.setText(officerrank.getText());
            agensrch.setText(officeragen.getText());
            numsrch.setText(officernumarrest.getText());
            timesrch.setText(time.getText());
            datesrch.setText(date.getText());
            searchedindividualsrch.setText(offenderName.getText());
            countysrch.setText(county.getText());
            areasrch.setText(area.getText());
            streetsrch.setText(street.getText());

            Label warningLabelsrch = (Label) searchReport.get("warningLabel");

            Button pullNotesBtnsrch = (Button) searchReport.get("pullNotesBtn");

            pullNotesBtnsrch.setOnAction(event1 -> {
                if (notesViewController != null) {
                    updateTextFromNotepad(areasrch, notesViewController.getNotepadTextArea(), "-area");
                    updateTextFromNotepad(countysrch, notesViewController.getNotepadTextArea(), "-county");
                    updateTextFromNotepad(streetsrch, notesViewController.getNotepadTextArea(), "-street");
                    updateTextFromNotepad(searchedindividualsrch, notesViewController.getNotepadTextArea(), "-name");
                    updateTextFromNotepad(notessrch, notesViewController.getNotepadTextArea(), "-comments");
                    updateTextFromNotepad(searchnum, notesViewController.getNotepadTextArea(), "-number");
                    updateTextFromNotepad(seizeditemssrch, notesViewController.getNotepadTextArea(), "-searchitems");
                } else {
                    log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                }
            });

            Button submitBtnsrch = (Button) searchReport.get("submitBtn");

            submitBtnsrch.setOnAction(event2 -> {
                boolean allFieldsFilled = true;
                for (String fieldName : searchReportMap.keySet()) {
                    Object field = searchReportMap.get(fieldName);
                    if (field instanceof ComboBox<?> comboBox) {
                        if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                            allFieldsFilled = false;
                            break;
                        }
                    }
                }
                if (!allFieldsFilled) {
                    warningLabelsrch.setVisible(true);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            warningLabelsrch.setVisible(false);
                        }
                    }, 3000);
                    return;
                }

                List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();

                logs.add(new SearchLogEntry(
                        searchnum.getText(),
                        searchedindividualsrch.getText(),
                        datesrch.getText(),
                        timesrch.getText(),
                        seizeditemssrch.getText(),
                        groundssrch.getText(),
                        typesrch.getValue().toString(),
                        methodsrch.getValue().toString(),
                        witnesssrch.getText(),
                        ranksrch.getText(),
                        namesrch.getText(),
                        numsrch.getText(),
                        agensrch.getText(),
                        divsrch.getText(),
                        streetsrch.getText(),
                        areasrch.getText(),
                        countysrch.getText(),
                        notessrch.getText(),
                        testconductedsrch.getText(),
                        resultsrch.getText(),
                        bacmeasurementsrch.getText()
                ));

                SearchReportLogs.saveLogsToXML(logs);
                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                controllerUtils.refreshChart(areaReportChart, "area");
                showNotification("Reports", "A new Search Report has been submitted.", vbox);
                stagesrch.close();
            });
        });


        Button submitBtn = (Button) arrestReport.get("submitBtn");
        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : arrestReportMap.keySet()) {
                Object field = arrestReportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
            }
            if (!allFieldsFilled) {
                warningLabel.setVisible(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        warningLabel.setVisible(false);
                    }
                }, 3000);
            } else {

                List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();

                ObservableList<ChargesData> formDataList = chargetable.getItems();
                StringBuilder stringBuilder = new StringBuilder();
                for (ChargesData formData : formDataList) {
                    stringBuilder.append(formData.getCharge()).append(" | ");
                }
                if (stringBuilder.length() > 0) {
                    stringBuilder.setLength(stringBuilder.length() - 2);
                }

                // Add new entry
                logs.add(new ArrestLogEntry(
                        arrestnum.getText(),
                        date.getText(),
                        time.getText(),
                        stringBuilder.toString(),
                        county.getText(),
                        area.getText(),
                        street.getText(),
                        offenderName.getText(),
                        offenderAge.getText(),
                        offenderGender.getText(),
                        offenderDescription.getText(),
                        ambulancereq.getText(),
                        taserdep.getText(),
                        othermedinfo.getText(),
                        offenderAddress.getText(),
                        notes.getText(),
                        officerrank.getText(),
                        officername.getText(),
                        officernumarrest.getText(),
                        officerdiv.getText(),
                        officeragen.getText()
                ));
                ArrestReportLogs.saveLogsToXML(logs);
                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                controllerUtils.refreshChart(areaReportChart, "area");
                showNotification("Reports", "A new Arrest Report has been submitted.", vbox);
                stage.close();
            }
        });
    }


    static Map<String, Object> trafficStopLayout() {
        Map<String, Object> trafficStopReport = createReportWindow("Traffic Stop Report", 6, 8,
                new TransferConfig("Transfer Information To New Report",
                        new RowConfig(
                                new FieldConfig("transferarrestbtn", 6, FieldType.TRANSFER_BUTTON),
                                new FieldConfig("transfercitationbtn", 6, FieldType.TRANSFER_BUTTON))
                ),
                new SectionConfig("Officer Information", true,
                        new RowConfig(
                                new FieldConfig("name", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("rank", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("number", 2, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("division", 6, FieldType.TEXT_FIELD),
                                new FieldConfig("agency", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Location / Timestamp Information", true,
                        new RowConfig(
                                new FieldConfig("street", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("area", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("county", 4, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("date", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("time", 5, FieldType.TEXT_FIELD),
                                new FieldConfig("stop number", 2, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Offender Information", true,
                        new RowConfig(
                                new FieldConfig("offender name", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("offender age", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("offender gender", 4, FieldType.TEXT_FIELD)),
                        new RowConfig(
                                new FieldConfig("offender address", 6, FieldType.TEXT_FIELD),
                                new FieldConfig("offender description", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Offender Vehicle Information", true,
                        new RowConfig(
                                new FieldConfig("model", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("plate number", 4, FieldType.TEXT_FIELD),
                                new FieldConfig("color", 4, FieldType.COMBO_BOX_COLOR)),
                        new RowConfig(
                                new FieldConfig("type", 4, FieldType.COMBO_BOX_TYPE),
                                new FieldConfig("other info", 8, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Comments", true,
                        new RowConfig(
                                new FieldConfig("notes", 12, FieldType.TEXT_AREA))
                ));
        return trafficStopReport;
    }

    public static void newTrafficStop(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
        Map<String, Object> trafficStopReport = trafficStopLayout();

        Map<String, Object> trafficStopReportMap = (Map<String, Object>) trafficStopReport.get("Traffic Stop Report Map");

        TextField officernamets = (TextField) trafficStopReportMap.get("name");
        TextField officerrankts = (TextField) trafficStopReportMap.get("rank");
        TextField officerdivts = (TextField) trafficStopReportMap.get("division");
        TextField officeragents = (TextField) trafficStopReportMap.get("agency");
        TextField officernumarrestts = (TextField) trafficStopReportMap.get("number");

        TextField offenderNamets = (TextField) trafficStopReportMap.get("offender name");
        TextField offenderAgets = (TextField) trafficStopReportMap.get("offender age");
        TextField offenderGenderts = (TextField) trafficStopReportMap.get("offender gender");
        TextField offenderAddressts = (TextField) trafficStopReportMap.get("offender address");
        TextField offenderDescriptionts = (TextField) trafficStopReportMap.get("offender description");

        ComboBox colorts = (ComboBox) trafficStopReportMap.get("color");
        ComboBox typets = (ComboBox) trafficStopReportMap.get("type");
        TextField plateNumberts = (TextField) trafficStopReportMap.get("plate number");
        TextField otherInfots = (TextField) trafficStopReportMap.get("other info");
        TextField modelts = (TextField) trafficStopReportMap.get("model");

        TextField areats = (TextField) trafficStopReportMap.get("area");
        TextField streetts = (TextField) trafficStopReportMap.get("street");
        TextField countyts = (TextField) trafficStopReportMap.get("county");
        TextField stopnumts = (TextField) trafficStopReportMap.get("stop number");
        TextField datets = (TextField) trafficStopReportMap.get("date");
        TextField timets = (TextField) trafficStopReportMap.get("time");

        TextArea notests = (TextArea) trafficStopReportMap.get("notes");

        Button transferarrestbtnts = (Button) trafficStopReportMap.get("transferarrestbtn");
        transferarrestbtnts.setText("New Arrest Report");
        Button transfercitationbtnts = (Button) trafficStopReportMap.get("transfercitationbtn");
        transfercitationbtnts.setText("New Citation Report");

        BorderPane rootts = (BorderPane) trafficStopReport.get("root");
        Stage stagets = (Stage) rootts.getScene().getWindow();

        Label warningLabelts = (Label) trafficStopReport.get("warningLabel");
        Button pullNotesBtnts = (Button) trafficStopReport.get("pullNotesBtn");

        try {
            officernamets.setText(ConfigReader.configRead("Name"));
            officerrankts.setText(ConfigReader.configRead("Rank"));
            officerdivts.setText(ConfigReader.configRead("Division"));
            officeragents.setText(ConfigReader.configRead("Agency"));
            officernumarrestts.setText(ConfigReader.configRead("Number"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        datets.setText(getDate());
        timets.setText(getTime());

        pullNotesBtnts.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(areats, notesViewController.getNotepadTextArea(), "-area");
                updateTextFromNotepad(countyts, notesViewController.getNotepadTextArea(), "-county");
                updateTextFromNotepad(streetts, notesViewController.getNotepadTextArea(), "-street");
                updateTextFromNotepad(offenderNamets, notesViewController.getNotepadTextArea(), "-name");
                updateTextFromNotepad(offenderAgets, notesViewController.getNotepadTextArea(), "-age");
                updateTextFromNotepad(offenderGenderts, notesViewController.getNotepadTextArea(), "-gender");
                updateTextFromNotepad(offenderDescriptionts, notesViewController.getNotepadTextArea(), "-description");
                updateTextFromNotepad(notests, notesViewController.getNotepadTextArea(), "-comments");
                updateTextFromNotepad(offenderAddressts, notesViewController.getNotepadTextArea(), "-address");
                updateTextFromNotepad(modelts, notesViewController.getNotepadTextArea(), "-model");
                updateTextFromNotepad(plateNumberts, notesViewController.getNotepadTextArea(), "-plate");
                updateTextFromNotepad(stopnumts, notesViewController.getNotepadTextArea(), "-number");
            } else {
                log("NotesViewController Is Null", LogUtils.Severity.ERROR);
            }
        });


        transferarrestbtnts.setOnAction(event -> {
            Map<String, Object> arrestReport = arrestLayout();

            Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReport.get("Arrest Report Map");

            TextField officernamearr = (TextField) arrestReportMap.get("name");
            TextField officerrankarr = (TextField) arrestReportMap.get("rank");
            TextField officerdivarr = (TextField) arrestReportMap.get("division");
            TextField officeragenarr = (TextField) arrestReportMap.get("agency");
            TextField officernumarrestarr = (TextField) arrestReportMap.get("number");

            TextField offenderNamearr = (TextField) arrestReportMap.get("offender name");
            TextField offenderAgearr = (TextField) arrestReportMap.get("offender age");
            TextField offenderGenderarr = (TextField) arrestReportMap.get("offender gender");
            TextField offenderAddressarr = (TextField) arrestReportMap.get("offender address");
            TextField offenderDescriptionarr = (TextField) arrestReportMap.get("offender description");

            TextField areaarr = (TextField) arrestReportMap.get("area");
            TextField streetarr = (TextField) arrestReportMap.get("street");
            TextField countyarr = (TextField) arrestReportMap.get("county");
            TextField arrestnumarr = (TextField) arrestReportMap.get("arrest number");
            TextField datearr = (TextField) arrestReportMap.get("date");
            TextField timearr = (TextField) arrestReportMap.get("time");

            TextField ambulancereqarr = (TextField) arrestReportMap.get("ambulance required (Y/N)");
            TextField taserdeparr = (TextField) arrestReportMap.get("taser deployed (Y/N)");
            TextField othermedinfoarr = (TextField) arrestReportMap.get("other information");

            TextArea notesarr = (TextArea) arrestReportMap.get("notes");

            TreeView chargetreeviewarr = (TreeView) arrestReportMap.get("chargeview");
            TableView chargetablearr = (TableView) arrestReportMap.get("ChargeTableView");

            Button transferimpoundbtnarr = (Button) arrestReportMap.get("transferimpoundbtn");
            transferimpoundbtnarr.setText("New Impound Report");
            Button transferincidentbtnarr = (Button) arrestReportMap.get("transferincidentbtn");
            transferincidentbtnarr.setText("New Incident Report");
            Button transfersearchbtnarr = (Button) arrestReportMap.get("transfersearchbtn");
            transfersearchbtnarr.setText("New Search Report");

            BorderPane rootarr = (BorderPane) arrestReport.get("root");
            Stage stagearr = (Stage) rootarr.getScene().getWindow();

            Label warningLabelarr = (Label) arrestReport.get("warningLabel");
            Button pullNotesBtnarr = (Button) arrestReport.get("pullNotesBtn");

            officernamearr.setText(officernamets.getText());
            officerdivarr.setText(officerdivts.getText());
            officerrankarr.setText(officerrankts.getText());
            officeragenarr.setText(officeragents.getText());
            officernumarrestarr.setText(officernumarrestts.getText());
            timearr.setText(timets.getText());
            datearr.setText(datets.getText());
            offenderNamets.setText(offenderNamets.getText());
            offenderAddressarr.setText(offenderAddressts.getText());
            offenderGenderarr.setText(offenderGenderts.getText());
            offenderAgearr.setText(offenderAgets.getText());
            offenderDescriptionarr.setText(offenderDescriptionts.getText());
            areaarr.setText(areats.getText());
            countyarr.setText(countyts.getText());
            streetarr.setText(streetts.getText());
            arrestnumarr.setText(stopnumts.getText());
            notesarr.setText(notests.getText());

            pullNotesBtnarr.setOnAction(event1 -> {
                if (notesViewController != null) {
                    updateTextFromNotepad(areaarr, notesViewController.getNotepadTextArea(), "-area");
                    updateTextFromNotepad(countyarr, notesViewController.getNotepadTextArea(), "-county");
                    updateTextFromNotepad(streetarr, notesViewController.getNotepadTextArea(), "-street");
                    updateTextFromNotepad(offenderNamearr, notesViewController.getNotepadTextArea(), "-name");
                    updateTextFromNotepad(offenderAgearr, notesViewController.getNotepadTextArea(), "-age");
                    updateTextFromNotepad(offenderGenderarr, notesViewController.getNotepadTextArea(), "-gender");
                    updateTextFromNotepad(offenderDescriptionarr, notesViewController.getNotepadTextArea(), "-description");
                    updateTextFromNotepad(notesarr, notesViewController.getNotepadTextArea(), "-comments");
                    updateTextFromNotepad(offenderAddressarr, notesViewController.getNotepadTextArea(), "-address");
                    updateTextFromNotepad(arrestnumarr, notesViewController.getNotepadTextArea(), "-number");
                } else {
                    log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                }
            });


            transferimpoundbtnarr.setOnAction(event2 -> {

                Map<String, Object> impoundReport = impoundLayout();

                Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");

                TextField officernameimp = (TextField) impoundReportMap.get("name");
                TextField officerrankimp = (TextField) impoundReportMap.get("rank");
                TextField officerdivimp = (TextField) impoundReportMap.get("division");
                TextField officeragenimp = (TextField) impoundReportMap.get("agency");
                TextField officernumimp = (TextField) impoundReportMap.get("number");

                TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
                TextField offenderAgeimp = (TextField) impoundReportMap.get("offender age");
                TextField offenderGenderimp = (TextField) impoundReportMap.get("offender gender");
                TextField offenderAddressimp = (TextField) impoundReportMap.get("offender address");

                TextField numimp = (TextField) impoundReportMap.get("citation number");
                TextField dateimp = (TextField) impoundReportMap.get("date");
                TextField timeimp = (TextField) impoundReportMap.get("time");

                ComboBox colorimp = (ComboBox) impoundReportMap.get("color");
                ComboBox typeimp = (ComboBox) impoundReportMap.get("type");
                TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
                TextField modelimp = (TextField) impoundReportMap.get("model");

                TextArea notesimp = (TextArea) impoundReportMap.get("notes");

                BorderPane rootimp = (BorderPane) impoundReport.get("root");
                Stage stageimp = (Stage) rootimp.getScene().getWindow();

                if (!stageimp.isFocused()) {
                    stageimp.requestFocus();
                }

                Label warningLabelimp = (Label) impoundReport.get("warningLabel");
                Button pullNotesBtnimp = (Button) impoundReport.get("pullNotesBtn");

                officernameimp.setText(officernamearr.getText());
                officerdivimp.setText(officerdivarr.getText());
                officerrankimp.setText(officerrankarr.getText());
                officeragenimp.setText(officeragenarr.getText());
                officernumimp.setText(officernumarrestarr.getText());
                timeimp.setText(timearr.getText());
                dateimp.setText(datearr.getText());
                offenderAddressimp.setText(offenderAddressarr.getText());
                offenderNameimp.setText(offenderNamearr.getText());
                offenderAgeimp.setText(offenderAgearr.getText());
                offenderGenderimp.setText(offenderGenderarr.getText());
                notesimp.setText(notesarr.getText());
                numimp.setText(arrestnumarr.getText());

                pullNotesBtnimp.setOnAction(event1 -> {
                    if (notesViewController != null) {
                        updateTextFromNotepad(offenderNameimp, notesViewController.getNotepadTextArea(), "-name");
                        updateTextFromNotepad(offenderAgeimp, notesViewController.getNotepadTextArea(), "-age");
                        updateTextFromNotepad(offenderGenderimp, notesViewController.getNotepadTextArea(), "-gender");
                        updateTextFromNotepad(notesimp, notesViewController.getNotepadTextArea(), "-comments");
                        updateTextFromNotepad(offenderAddressimp, notesViewController.getNotepadTextArea(), "-address");
                        updateTextFromNotepad(modelimp, notesViewController.getNotepadTextArea(), "-model");
                        updateTextFromNotepad(plateNumberimp, notesViewController.getNotepadTextArea(), "-plate");
                        updateTextFromNotepad(numimp, notesViewController.getNotepadTextArea(), "-number");
                    } else {
                        log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                    }
                });

                Button submitBtnimp = (Button) impoundReport.get("submitBtn");
                submitBtnimp.setOnAction(event3 -> {
                    boolean allFieldsFilled = true;
                    for (String fieldName : impoundReportMap.keySet()) {
                        Object field = impoundReportMap.get(fieldName);
                        if (field instanceof ComboBox<?> comboBox) {
                            if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                                allFieldsFilled = false;
                                break;
                            }
                        }
                    }
                    if (!allFieldsFilled) {
                        warningLabelimp.setVisible(true);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                warningLabelimp.setVisible(false);
                            }
                        }, 3000);
                    } else {
                        List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();

                        // Add new entry
                        logs.add(new ImpoundLogEntry(
                                numimp.getText(),
                                dateimp.getText(),
                                timeimp.getText(),
                                offenderNameimp.getText(),
                                offenderAgeimp.getText(),
                                offenderGenderimp.getText(),
                                offenderAddressimp.getText(),
                                plateNumberimp.getText(),
                                modelimp.getText(),
                                typeimp.getValue().toString(),
                                colorimp.getValue().toString(),
                                notesimp.getText(),
                                officerrankimp.getText(),
                                officernameimp.getText(),
                                officernumimp.getText(),
                                officerdivimp.getText(),
                                officeragenimp.getText()
                        ));
                        ImpoundReportLogs.saveLogsToXML(logs);
                        actionController.needRefresh.set(1);
                        updateChartIfMismatch(reportChart);
                        controllerUtils.refreshChart(areaReportChart, "area");
                        showNotification("Reports", "A new Impound Report has been submitted.", vbox);
                        stageimp.close();
                    }
                });
            });

            transferincidentbtnarr.setOnAction(event3 -> {
                Map<String, Object> incidentReport = incidentLayout();

                Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReport.get("Incident Report Map");

                TextField nameinc = (TextField) incidentReportMap.get("name");
                TextField rankinc = (TextField) incidentReportMap.get("rank");
                TextField divinc = (TextField) incidentReportMap.get("division");
                TextField ageninc = (TextField) incidentReportMap.get("agency");
                TextField officernuminc = (TextField) incidentReportMap.get("number");

                TextField incidentnum = (TextField) incidentReportMap.get("incident num");
                TextField dateinc = (TextField) incidentReportMap.get("date");
                TextField timeinc = (TextField) incidentReportMap.get("time");
                TextField streetinc = (TextField) incidentReportMap.get("street");
                TextField areainc = (TextField) incidentReportMap.get("area");
                TextField countyinc = (TextField) incidentReportMap.get("county");

                TextField suspectsinc = (TextField) incidentReportMap.get("suspect(s)");
                TextField vicwitinc = (TextField) incidentReportMap.get("victim(s) / witness(s)");
                TextArea statementinc = (TextArea) incidentReportMap.get("statement");

                TextArea summaryinc = (TextArea) incidentReportMap.get("summary");
                TextArea notesinc = (TextArea) incidentReportMap.get("notes");

                BorderPane rootinc = (BorderPane) incidentReport.get("root");
                Stage stageinc = (Stage) rootinc.getScene().getWindow();

                if (!stageinc.isFocused()) {
                    stageinc.requestFocus();
                }

                Label warningLabelinc = (Label) incidentReport.get("warningLabel");

                nameinc.setText(officernamearr.getText());
                divinc.setText(officerdivarr.getText());
                rankinc.setText(officerrankarr.getText());
                ageninc.setText(officeragenarr.getText());
                officernuminc.setText(officernumarrestarr.getText());
                dateinc.setText(datearr.getText());
                timeinc.setText(timearr.getText());
                incidentnum.setText(arrestnumarr.getText());
                countyinc.setText(countyarr.getText());
                areainc.setText(areaarr.getText());
                streetinc.setText(streetarr.getText());
                suspectsinc.setText(offenderNamearr.getText());
                notesinc.setText(notesarr.getText());

                Button pullNotesBtninc = (Button) incidentReport.get("pullNotesBtn");

                pullNotesBtninc.setOnAction(event1 -> {
                    if (notesViewController != null) {
                        updateTextFromNotepad(areainc, notesViewController.getNotepadTextArea(), "-area");
                        updateTextFromNotepad(countyinc, notesViewController.getNotepadTextArea(), "-county");
                        updateTextFromNotepad(streetinc, notesViewController.getNotepadTextArea(), "-street");
                        updateTextFromNotepad(suspectsinc, notesViewController.getNotepadTextArea(), "-name");
                        updateTextFromNotepad(notesinc, notesViewController.getNotepadTextArea(), "-comments");
                        updateTextFromNotepad(officernuminc, notesViewController.getNotepadTextArea(), "-number");
                    } else {
                        log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                    }
                });

                Button submitBtninc = (Button) incidentReport.get("submitBtn");

                submitBtninc.setOnAction(event2 -> {
                    boolean allFieldsFilled = true;
                    for (String fieldName : incidentReportMap.keySet()) {
                        Object field = incidentReportMap.get(fieldName);
                        if (field instanceof ComboBox<?> comboBox) {
                            if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                                allFieldsFilled = false;
                                break;
                            }
                        }
                    }
                    if (!allFieldsFilled) {
                        warningLabelinc.setVisible(true);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                warningLabelinc.setVisible(false);
                            }
                        }, 3000);
                        return;
                    }

                    List<IncidentLogEntry> logs = IncidentReportLogs.loadLogsFromXML();

                    logs.add(new IncidentLogEntry(
                            incidentnum.getText(),
                            dateinc.getText(),
                            timeinc.getText(),
                            statementinc.getText(),
                            suspectsinc.getText(),
                            vicwitinc.getText(),
                            nameinc.getText(),
                            rankinc.getText(),
                            officernuminc.getText(),
                            ageninc.getText(),
                            divinc.getText(),
                            streetinc.getText(),
                            areainc.getText(),
                            countyinc.getText(),
                            summaryinc.getText(),
                            notesinc.getText()
                    ));
                    IncidentReportLogs.saveLogsToXML(logs);
                    actionController.needRefresh.set(1);
                    updateChartIfMismatch(reportChart);
                    controllerUtils.refreshChart(areaReportChart, "area");
                    showNotification("Reports", "A new Incident Report has been submitted.", vbox);
                    stageinc.close();
                });
            });

            transfersearchbtnarr.setOnAction(event4 -> {
                Map<String, Object> searchReport = searchLayout();

                Map<String, Object> searchReportMap = (Map<String, Object>) searchReport.get("Search Report Map");

                TextField namesrch = (TextField) searchReportMap.get("name");
                TextField ranksrch = (TextField) searchReportMap.get("rank");
                TextField divsrch = (TextField) searchReportMap.get("division");
                TextField agensrch = (TextField) searchReportMap.get("agency");
                TextField numsrch = (TextField) searchReportMap.get("number");

                TextField searchnum = (TextField) searchReportMap.get("search num");
                TextField datesrch = (TextField) searchReportMap.get("date");
                TextField timesrch = (TextField) searchReportMap.get("time");
                TextField streetsrch = (TextField) searchReportMap.get("street");
                TextField areasrch = (TextField) searchReportMap.get("area");
                TextField countysrch = (TextField) searchReportMap.get("county");

                TextField groundssrch = (TextField) searchReportMap.get("grounds for search");
                TextField witnesssrch = (TextField) searchReportMap.get("witness(s)");
                TextField searchedindividualsrch = (TextField) searchReportMap.get("searched individual");
                ComboBox typesrch = (ComboBox) searchReportMap.get("search type");
                ComboBox methodsrch = (ComboBox) searchReportMap.get("search method");

                TextField testconductedsrch = (TextField) searchReportMap.get("test(s) conducted");
                TextField resultsrch = (TextField) searchReportMap.get("result");
                TextField bacmeasurementsrch = (TextField) searchReportMap.get("bac measurement");

                TextArea seizeditemssrch = (TextArea) searchReportMap.get("seized item(s)");
                TextArea notessrch = (TextArea) searchReportMap.get("comments");

                BorderPane rootsrch = (BorderPane) searchReport.get("root");
                Stage stagesrch = (Stage) rootsrch.getScene().getWindow();

                if (!stagesrch.isFocused()) {
                    stagesrch.requestFocus();
                }

                searchnum.setText(arrestnumarr.getText());
                namesrch.setText(officernamearr.getText());
                divsrch.setText(officerdivarr.getText());
                ranksrch.setText(officerrankarr.getText());
                agensrch.setText(officeragenarr.getText());
                numsrch.setText(officernumarrestarr.getText());
                timesrch.setText(timearr.getText());
                datesrch.setText(datearr.getText());
                searchedindividualsrch.setText(offenderNamearr.getText());
                countysrch.setText(countyarr.getText());
                areasrch.setText(areaarr.getText());
                streetsrch.setText(streetarr.getText());

                Label warningLabelsrch = (Label) searchReport.get("warningLabel");

                Button pullNotesBtnsrch = (Button) searchReport.get("pullNotesBtn");

                pullNotesBtnsrch.setOnAction(event1 -> {
                    if (notesViewController != null) {
                        updateTextFromNotepad(areasrch, notesViewController.getNotepadTextArea(), "-area");
                        updateTextFromNotepad(countysrch, notesViewController.getNotepadTextArea(), "-county");
                        updateTextFromNotepad(streetsrch, notesViewController.getNotepadTextArea(), "-street");
                        updateTextFromNotepad(searchedindividualsrch, notesViewController.getNotepadTextArea(), "-name");
                        updateTextFromNotepad(notessrch, notesViewController.getNotepadTextArea(), "-comments");
                        updateTextFromNotepad(searchnum, notesViewController.getNotepadTextArea(), "-number");
                        updateTextFromNotepad(seizeditemssrch, notesViewController.getNotepadTextArea(), "-searchitems");
                    } else {
                        log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                    }
                });

                Button submitBtnsrch = (Button) searchReport.get("submitBtn");

                submitBtnsrch.setOnAction(event2 -> {
                    boolean allFieldsFilled = true;
                    for (String fieldName : searchReportMap.keySet()) {
                        Object field = searchReportMap.get(fieldName);
                        if (field instanceof ComboBox<?> comboBox) {
                            if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                                allFieldsFilled = false;
                                break;
                            }
                        }
                    }
                    if (!allFieldsFilled) {
                        warningLabelsrch.setVisible(true);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                warningLabelsrch.setVisible(false);
                            }
                        }, 3000);
                        return;
                    }

                    List<SearchLogEntry> logs = SearchReportLogs.loadLogsFromXML();

                    logs.add(new SearchLogEntry(
                            searchnum.getText(),
                            searchedindividualsrch.getText(),
                            datesrch.getText(),
                            timesrch.getText(),
                            seizeditemssrch.getText(),
                            groundssrch.getText(),
                            typesrch.getValue().toString(),
                            methodsrch.getValue().toString(),
                            witnesssrch.getText(),
                            ranksrch.getText(),
                            namesrch.getText(),
                            numsrch.getText(),
                            agensrch.getText(),
                            divsrch.getText(),
                            streetsrch.getText(),
                            areasrch.getText(),
                            countysrch.getText(),
                            notessrch.getText(),
                            testconductedsrch.getText(),
                            resultsrch.getText(),
                            bacmeasurementsrch.getText()
                    ));

                    SearchReportLogs.saveLogsToXML(logs);
                    actionController.needRefresh.set(1);
                    updateChartIfMismatch(reportChart);
                    controllerUtils.refreshChart(areaReportChart, "area");
                    showNotification("Reports", "A new Search Report has been submitted.", vbox);
                    stagesrch.close();
                });
            });


            Button submitBtn = (Button) arrestReport.get("submitBtn");
            submitBtn.setOnAction(event5 -> {
                boolean allFieldsFilled = true;
                for (String fieldName : arrestReportMap.keySet()) {
                    Object field = arrestReportMap.get(fieldName);
                    if (field instanceof ComboBox<?> comboBox) {
                        if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                            allFieldsFilled = false;
                            break;
                        }
                    }
                }
                if (!allFieldsFilled) {
                    warningLabelarr.setVisible(true);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            warningLabelarr.setVisible(false);
                        }
                    }, 3000);
                } else {

                    List<ArrestLogEntry> logs = ArrestReportLogs.loadLogsFromXML();

                    ObservableList<ChargesData> formDataList = chargetablearr.getItems();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (ChargesData formData : formDataList) {
                        stringBuilder.append(formData.getCharge()).append(" | ");
                    }
                    if (stringBuilder.length() > 0) {
                        stringBuilder.setLength(stringBuilder.length() - 2);
                    }

                    // Add new entry
                    logs.add(new ArrestLogEntry(
                            arrestnumarr.getText(),
                            datearr.getText(),
                            timearr.getText(),
                            stringBuilder.toString(),
                            countyarr.getText(),
                            areaarr.getText(),
                            streetarr.getText(),
                            offenderNamearr.getText(),
                            offenderAgearr.getText(),
                            offenderGenderarr.getText(),
                            offenderDescriptionarr.getText(),
                            ambulancereqarr.getText(),
                            taserdeparr.getText(),
                            othermedinfoarr.getText(),
                            offenderAddressarr.getText(),
                            notesarr.getText(),
                            officerrankarr.getText(),
                            officernamearr.getText(),
                            officernumarrestarr.getText(),
                            officerdivarr.getText(),
                            officeragenarr.getText()
                    ));
                    ArrestReportLogs.saveLogsToXML(logs);
                    actionController.needRefresh.set(1);
                    updateChartIfMismatch(reportChart);
                    controllerUtils.refreshChart(areaReportChart, "area");
                    showNotification("Reports", "A new Arrest Report has been submitted.", vbox);
                    stagearr.close();
                }
            });
        });

        transfercitationbtnts.setOnAction(event -> {
            Map<String, Object> citationReport = citationLayout();

            Map<String, Object> citationReportMap = (Map<String, Object>) citationReport.get("Citation Report Map");

            TextField officernamecit = (TextField) citationReportMap.get("name");
            TextField officerrankcit = (TextField) citationReportMap.get("rank");
            TextField officerdivcit = (TextField) citationReportMap.get("division");
            TextField officeragencit = (TextField) citationReportMap.get("agency");
            TextField officernumcit = (TextField) citationReportMap.get("number");

            TextField offenderNamecit = (TextField) citationReportMap.get("offender name");
            TextField offenderAgecit = (TextField) citationReportMap.get("offender age");
            TextField offenderGendercit = (TextField) citationReportMap.get("offender gender");
            TextField offenderAddresscit = (TextField) citationReportMap.get("offender address");
            TextField offenderDescriptioncit = (TextField) citationReportMap.get("offender description");

            TextField areacit = (TextField) citationReportMap.get("area");
            TextField streetcit = (TextField) citationReportMap.get("street");
            TextField countycit = (TextField) citationReportMap.get("county");
            TextField numcit = (TextField) citationReportMap.get("citation number");
            TextField datecit = (TextField) citationReportMap.get("date");
            TextField timecit = (TextField) citationReportMap.get("time");

            ComboBox colorcit = (ComboBox) citationReportMap.get("color");
            ComboBox typecit = (ComboBox) citationReportMap.get("type");
            TextField plateNumbercit = (TextField) citationReportMap.get("plate number");
            TextField otherInfocit = (TextField) citationReportMap.get("other info");
            TextField modelcit = (TextField) citationReportMap.get("model");

            TextArea notescit = (TextArea) citationReportMap.get("notes");

            TreeView citationtreeview = (TreeView) citationReportMap.get("citationview");
            TableView citationtable = (TableView) citationReportMap.get("CitationTableView");

            Button transferimpoundbtn = (Button) citationReportMap.get("transferimpoundbtn");

            BorderPane rootcit = (BorderPane) citationReport.get("root");
            Stage stagecit = (Stage) rootcit.getScene().getWindow();

            Label warningLabelcit = (Label) citationReport.get("warningLabel");
            Button pullNotesBtncit = (Button) citationReport.get("pullNotesBtn");

            officernamecit.setText(officernamets.getText());
            officerdivcit.setText(officerdivts.getText());
            officerrankcit.setText(officerrankts.getText());
            officeragencit.setText(officeragents.getText());
            officernumcit.setText(officernumarrestts.getText());
            timecit.setText(timets.getText());
            datecit.setText(datets.getText());
            typecit.getSelectionModel().select(typets.getSelectionModel().getSelectedItem());
            colorcit.getSelectionModel().select(colorts.getSelectionModel().getSelectedItem());
            offenderNamecit.setText(offenderNamets.getText());
            offenderAddresscit.setText(offenderAddressts.getText());
            offenderGendercit.setText(offenderGenderts.getText());
            offenderAgecit.setText(offenderAgets.getText());
            offenderDescriptioncit.setText(offenderDescriptionts.getText());
            areacit.setText(areats.getText());
            countycit.setText(countyts.getText());
            streetcit.setText(streetts.getText());
            modelcit.setText(modelts.getText());
            plateNumbercit.setText(plateNumberts.getText());
            otherInfocit.setText(otherInfots.getText());
            numcit.setText(stopnumts.getText());
            notescit.setText(notests.getText());

            pullNotesBtncit.setOnAction(event1 -> {
                if (notesViewController != null) {
                    updateTextFromNotepad(areacit, notesViewController.getNotepadTextArea(), "-area");
                    updateTextFromNotepad(countycit, notesViewController.getNotepadTextArea(), "-county");
                    updateTextFromNotepad(streetcit, notesViewController.getNotepadTextArea(), "-street");
                    updateTextFromNotepad(offenderNamecit, notesViewController.getNotepadTextArea(), "-name");
                    updateTextFromNotepad(offenderAgecit, notesViewController.getNotepadTextArea(), "-age");
                    updateTextFromNotepad(offenderGendercit, notesViewController.getNotepadTextArea(), "-gender");
                    updateTextFromNotepad(offenderDescriptioncit, notesViewController.getNotepadTextArea(), "-description");
                    updateTextFromNotepad(notescit, notesViewController.getNotepadTextArea(), "-comments");
                    updateTextFromNotepad(offenderAddresscit, notesViewController.getNotepadTextArea(), "-address");
                    updateTextFromNotepad(modelcit, notesViewController.getNotepadTextArea(), "-model");
                    updateTextFromNotepad(plateNumbercit, notesViewController.getNotepadTextArea(), "-plate");
                    updateTextFromNotepad(numcit, notesViewController.getNotepadTextArea(), "-number");
                } else {
                    log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                }
            });

            transferimpoundbtn.setOnAction(event2 -> {

                Map<String, Object> impoundReport = impoundLayout();

                Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReport.get("Impound Report Map");

                TextField officernameimp = (TextField) impoundReportMap.get("name");
                TextField officerrankimp = (TextField) impoundReportMap.get("rank");
                TextField officerdivimp = (TextField) impoundReportMap.get("division");
                TextField officeragenimp = (TextField) impoundReportMap.get("agency");
                TextField officernumimp = (TextField) impoundReportMap.get("number");

                TextField offenderNameimp = (TextField) impoundReportMap.get("offender name");
                TextField offenderAgeimp = (TextField) impoundReportMap.get("offender age");
                TextField offenderGenderimp = (TextField) impoundReportMap.get("offender gender");
                TextField offenderAddressimp = (TextField) impoundReportMap.get("offender address");

                TextField numimp = (TextField) impoundReportMap.get("citation number");
                TextField dateimp = (TextField) impoundReportMap.get("date");
                TextField timeimp = (TextField) impoundReportMap.get("time");

                ComboBox colorimp = (ComboBox) impoundReportMap.get("color");
                ComboBox typeimp = (ComboBox) impoundReportMap.get("type");
                TextField plateNumberimp = (TextField) impoundReportMap.get("plate number");
                TextField modelimp = (TextField) impoundReportMap.get("model");

                TextArea notesimp = (TextArea) impoundReportMap.get("notes");

                BorderPane rootimp = (BorderPane) impoundReport.get("root");
                Stage stageimp = (Stage) rootimp.getScene().getWindow();

                if (!stageimp.isFocused()) {
                    stageimp.requestFocus();
                }

                Label warningLabelimp = (Label) impoundReport.get("warningLabel");
                Button pullNotesBtnimp = (Button) impoundReport.get("pullNotesBtn");

                officernameimp.setText(officernamecit.getText());
                officerdivimp.setText(officerdivcit.getText());
                officerrankimp.setText(officerrankcit.getText());
                officeragenimp.setText(officeragencit.getText());
                officernumimp.setText(officernumcit.getText());
                timeimp.setText(timecit.getText());
                dateimp.setText(datecit.getText());
                offenderAddressimp.setText(offenderAddresscit.getText());
                offenderNameimp.setText(offenderNamecit.getText());
                offenderAgeimp.setText(offenderAgecit.getText());
                offenderGenderimp.setText(offenderGendercit.getText());
                plateNumberimp.setText(plateNumbercit.getText());
                notesimp.setText(notescit.getText());
                modelimp.setText(modelcit.getText());
                typeimp.getSelectionModel().select(typecit.getSelectionModel().getSelectedItem());
                colorimp.getSelectionModel().select(colorcit.getSelectionModel().getSelectedItem());
                numimp.setText(numcit.getText());

                pullNotesBtnimp.setOnAction(event1 -> {
                    if (notesViewController != null) {
                        updateTextFromNotepad(offenderNameimp, notesViewController.getNotepadTextArea(), "-name");
                        updateTextFromNotepad(offenderAgeimp, notesViewController.getNotepadTextArea(), "-age");
                        updateTextFromNotepad(offenderGenderimp, notesViewController.getNotepadTextArea(), "-gender");
                        updateTextFromNotepad(notesimp, notesViewController.getNotepadTextArea(), "-comments");
                        updateTextFromNotepad(offenderAddressimp, notesViewController.getNotepadTextArea(), "-address");
                        updateTextFromNotepad(modelimp, notesViewController.getNotepadTextArea(), "-model");
                        updateTextFromNotepad(plateNumberimp, notesViewController.getNotepadTextArea(), "-plate");
                        updateTextFromNotepad(numimp, notesViewController.getNotepadTextArea(), "-number");
                    } else {
                        log("NotesViewController Is Null", LogUtils.Severity.ERROR);
                    }
                });

                Button submitBtnimp = (Button) impoundReport.get("submitBtn");
                submitBtnimp.setOnAction(event3 -> {
                    boolean allFieldsFilled = true;
                    for (String fieldName : impoundReportMap.keySet()) {
                        Object field = impoundReportMap.get(fieldName);
                        if (field instanceof ComboBox<?> comboBox) {
                            if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                                allFieldsFilled = false;
                                break;
                            }
                        }
                    }
                    if (!allFieldsFilled) {
                        warningLabelimp.setVisible(true);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                warningLabelimp.setVisible(false);
                            }
                        }, 3000);
                    } else {
                        List<ImpoundLogEntry> logs = ImpoundReportLogs.loadLogsFromXML();

                        // Add new entry
                        logs.add(new ImpoundLogEntry(
                                numimp.getText(),
                                dateimp.getText(),
                                timeimp.getText(),
                                offenderNameimp.getText(),
                                offenderAgeimp.getText(),
                                offenderGenderimp.getText(),
                                offenderAddressimp.getText(),
                                plateNumberimp.getText(),
                                modelimp.getText(),
                                typeimp.getValue().toString(),
                                colorimp.getValue().toString(),
                                notesimp.getText(),
                                officerrankimp.getText(),
                                officernameimp.getText(),
                                officernumimp.getText(),
                                officerdivimp.getText(),
                                officeragenimp.getText()
                        ));
                        ImpoundReportLogs.saveLogsToXML(logs);
                        actionController.needRefresh.set(1);
                        updateChartIfMismatch(reportChart);
                        controllerUtils.refreshChart(areaReportChart, "area");
                        showNotification("Reports", "A new Impound Report has been submitted.", vbox);
                        stageimp.close();
                    }
                });
            });
            transferimpoundbtn.setText("New Impound Report");

            Button submitBtncit = (Button) citationReport.get("submitBtn");
            submitBtncit.setOnAction(event3 -> {
                boolean allFieldsFilled = true;
                for (String fieldName : citationReportMap.keySet()) {
                    Object field = citationReportMap.get(fieldName);
                    if (field instanceof ComboBox<?> comboBox) {
                        if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                            allFieldsFilled = false;
                            break;
                        }
                    }
                }
                if (!allFieldsFilled) {
                    warningLabelcit.setVisible(true);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            warningLabelcit.setVisible(false);
                        }
                    }, 3000);
                } else {
                    List<TrafficCitationLogEntry> logs = TrafficCitationReportLogs.loadLogsFromXML();
                    ObservableList<CitationsData> formDataList = citationtable.getItems();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (CitationsData formData : formDataList) {
                        stringBuilder.append(formData.getCitation()).append(" | ");
                    }
                    if (stringBuilder.length() > 0) {
                        stringBuilder.setLength(stringBuilder.length() - 2);
                    }

                    logs.add(new TrafficCitationLogEntry(
                            numcit.getText(),
                            datecit.getText(),
                            timecit.getText(),
                            stringBuilder.toString(),
                            countycit.getText(),
                            areacit.getText(),
                            streetcit.getText(),
                            offenderNamecit.getText(),
                            offenderGendercit.getText(),
                            offenderAgecit.getText(),
                            offenderAddresscit.getText(),
                            offenderDescriptioncit.getText(),
                            modelcit.getText(),
                            colorcit.getValue().toString(),
                            typecit.getValue().toString(),
                            plateNumbercit.getText(),
                            otherInfocit.getText(),
                            officerrankcit.getText(),
                            officernamecit.getText(),
                            officernumcit.getText(),
                            officerdivcit.getText(),
                            officeragencit.getText(),
                            notescit.getText()
                    ));
                    TrafficCitationReportLogs.saveLogsToXML(logs);
                    actionController.needRefresh.set(1);
                    updateChartIfMismatch(reportChart);
                    controllerUtils.refreshChart(areaReportChart, "area");
                    showNotification("Reports", "A new Citation Report has been submitted.", vbox);
                    stagecit.close();
                }
            });
        });


        Button submitBtn = (Button) trafficStopReport.get("submitBtn");
        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : trafficStopReportMap.keySet()) {
                Object field = trafficStopReportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
            }
            if (!allFieldsFilled) {
                warningLabelts.setVisible(true);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        warningLabelts.setVisible(false);
                    }
                }, 3000);
            } else {
                List<TrafficStopLogEntry> logs = TrafficStopReportLogs.loadLogsFromXML();

                logs.add(new TrafficStopLogEntry(
                        datets.getText(),
                        timets.getText(),
                        modelts.getText(),
                        otherInfots.getText(),
                        offenderNamets.getText(),
                        offenderAgets.getText(),
                        offenderAddressts.getText(),
                        offenderDescriptionts.getText(),
                        offenderGenderts.getText(),
                        officernamets.getText(),
                        officerrankts.getText(),
                        officernumarrestts.getText(),
                        officerdivts.getText(),
                        officeragents.getText(),
                        stopnumts.getText(),
                        notests.getText(),
                        streetts.getText(),
                        countyts.getText(),
                        areats.getText(),
                        plateNumberts.getText(),
                        colorts.getValue().toString(),
                        typets.getValue().toString()
                ));
                TrafficStopReportLogs.saveLogsToXML(logs);
                actionController.needRefresh.set(1);
                updateChartIfMismatch(reportChart);
                controllerUtils.refreshChart(areaReportChart, "area");
                showNotification("Reports", "A new Traffic Stop Report has been submitted.", vbox);
                stagets.close();
            }
        });
    }


    //</editor-fold>


    //<editor-fold desc="External">


    public enum FieldType {
        TEXT_FIELD,
        TEXT_AREA,
        COMBO_BOX_COLOR,
        COMBO_BOX_TYPE,
        COMBO_BOX_SEARCH_TYPE,
        COMBO_BOX_SEARCH_METHOD,
        CITATION_TREE_VIEW,
        CHARGES_TREE_VIEW,
        TRANSFER_BUTTON
    }


    public static class SectionConfig {
        private final String sectionTitle;
        private final Boolean required;

        private final List<RowConfig> rowConfigs;

        public SectionConfig(String sectionTitle, boolean required, RowConfig... rowConfigs) {
            this.sectionTitle = sectionTitle;
            this.required = required;
            this.rowConfigs = Arrays.asList(rowConfigs);
        }

        public String getSectionTitle() {
            return sectionTitle;
        }

        public List<RowConfig> getRowConfigs() {
            return rowConfigs;
        }


        public Boolean getRequired() {
            return required;
        }
    }


    public static class TransferConfig {
        private final String title;
        private final List<RowConfig> rowConfigs;

        public TransferConfig(String title, RowConfig... rowConfigs) {
            this.title = title;
            this.rowConfigs = Arrays.asList(rowConfigs);
        }

        public String getTitle() {
            return title;
        }

        public List<RowConfig> getRowConfigs() {
            return rowConfigs;
        }
    }


    public static class RowConfig {

        private final List<FieldConfig> fieldConfigs;

        public RowConfig(FieldConfig... fieldConfigs) {
            this.fieldConfigs = Arrays.asList(fieldConfigs);
        }

        public List<FieldConfig> getFieldConfigs() {
            return fieldConfigs;
        }


    }


    public static class FieldConfig {

        private final String fieldName;

        private final int size;
        private final FieldType fieldType;

        public FieldConfig(String fieldName, int size, FieldType fieldType) {
            this.fieldName = fieldName;
            this.size = size;
            this.fieldType = fieldType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public int getSize() {
            return size;
        }

        public FieldType getFieldType() {
            return fieldType;
        }


    }


    //</editor-fold>


}