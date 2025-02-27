package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.Accident.AccidentReport;
import com.Guess.ReportsPlus.logs.Accident.AccidentReportUtils;
import com.Guess.ReportsPlus.logs.Accident.AccidentReports;
import com.Guess.ReportsPlus.logs.Arrest.ArrestReport;
import com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils;
import com.Guess.ReportsPlus.logs.Arrest.ArrestReports;
import com.Guess.ReportsPlus.logs.Callout.CalloutReport;
import com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils;
import com.Guess.ReportsPlus.logs.Callout.CalloutReports;
import com.Guess.ReportsPlus.logs.ChargesData;
import com.Guess.ReportsPlus.logs.CitationsData;
import com.Guess.ReportsPlus.logs.Death.DeathReport;
import com.Guess.ReportsPlus.logs.Death.DeathReportUtils;
import com.Guess.ReportsPlus.logs.Death.DeathReports;
import com.Guess.ReportsPlus.logs.Impound.ImpoundReport;
import com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils;
import com.Guess.ReportsPlus.logs.Impound.ImpoundReports;
import com.Guess.ReportsPlus.logs.Incident.IncidentReport;
import com.Guess.ReportsPlus.logs.Incident.IncidentReportUtils;
import com.Guess.ReportsPlus.logs.Incident.IncidentReports;
import com.Guess.ReportsPlus.logs.Patrol.PatrolReport;
import com.Guess.ReportsPlus.logs.Patrol.PatrolReportUtils;
import com.Guess.ReportsPlus.logs.Patrol.PatrolReports;
import com.Guess.ReportsPlus.logs.Search.SearchReport;
import com.Guess.ReportsPlus.logs.Search.SearchReportUtils;
import com.Guess.ReportsPlus.logs.Search.SearchReports;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationReport;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationReports;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils;
import com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReport;
import com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReportUtils;
import com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReports;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Report.Database.CustomReport;
import com.Guess.ReportsPlus.util.Report.Database.DynamicDB;
import jakarta.xml.bind.JAXBException;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.parseAndPopulateMap;
import static com.Guess.ReportsPlus.logs.Accident.AccidentReportUtils.newAccident;
import static com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils.newArrest;
import static com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils.newCallout;
import static com.Guess.ReportsPlus.logs.Death.DeathReportUtils.newDeathReport;
import static com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils.newImpound;
import static com.Guess.ReportsPlus.logs.Incident.IncidentReportUtils.newIncident;
import static com.Guess.ReportsPlus.logs.Patrol.PatrolReportUtils.newPatrol;
import static com.Guess.ReportsPlus.logs.Search.SearchReportUtils.newSearch;
import static com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils.newCitation;
import static com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReportUtils.newTrafficStop;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationInfo;
import static com.Guess.ReportsPlus.util.Other.InitTableColumns.*;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.getPrimaryKeyColumn;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.isValidDatabase;
import static com.Guess.ReportsPlus.util.Report.treeViewUtils.addChargesToTable;
import static com.Guess.ReportsPlus.util.Report.treeViewUtils.addCitationsToTable;

public class LogViewController {
    public static LogViewController logController;
    TableView currentTable;
    List<Label> sideButtons = new ArrayList<>();
    private Label activePane;

    @FXML
    private AnchorPane logPane;
    @FXML
    private TableView trafficStopTable;
    @FXML
    private TableView arrestTable;
    @FXML
    private TableView deathReportTable;
    @FXML
    private TableView impoundTable;
    @FXML
    private BorderPane root;
    @FXML
    private TableView citationTable;
    @FXML
    private TableView calloutTable;
    @FXML
    private TableView patrolTable;
    @FXML
    private TableView accidentReportTable;
    @FXML
    private TableView incidentTable;
    @FXML
    private TableView searchTable;
    @FXML
    private Label reportsInProgressLabel;
    @FXML
    private Label totalReportsHeader;
    @FXML
    private Label totalReportsLabel;
    @FXML
    private ProgressBar closedReportsProgressBar;
    @FXML
    private Label closedReportsHeader;
    @FXML
    private Label reportsInProgressHeader;
    @FXML
    private Label closedReportsLabel;
    @FXML
    private StackPane tableStackPane;
    @FXML
    private ProgressBar reportsInProgressBar;
    @FXML
    private Label subHeading;
    @FXML
    private TitledPane customReportsTitlePane;
    @FXML
    private TitledPane reportsTitlePane;
    @FXML
    private ScrollPane sp;
    @FXML
    private VBox customReportsVBox;
    @FXML
    private Label callout;
    @FXML
    private Label death;
    @FXML
    private Label arrest;
    @FXML
    private Label accident;
    @FXML
    private Label search;
    @FXML
    private Label patrol;
    @FXML
    private Label impound;
    @FXML
    private Label citation;
    @FXML
    private Label trafficstop;
    @FXML
    private Label incident;

    public void initialize() {
        initializeCalloutColumns(calloutTable);
        initializeArrestColumns(arrestTable);
        initializeCitationColumns(citationTable);
        initializeImpoundColumns(impoundTable);
        initializeIncidentColumns(incidentTable);
        initializePatrolColumns(patrolTable);
        initializeSearchColumns(searchTable);
        initializeTrafficStopColumns(trafficStopTable);
        initializeDeathReportColumns(deathReportTable);
        initializeAccidentColumns(accidentReportTable);

        addHoverListener(calloutTable, callout);
        addHoverListener(arrestTable, arrest);
        addHoverListener(deathReportTable, death);
        addHoverListener(accidentReportTable, accident);
        addHoverListener(searchTable, search);
        addHoverListener(patrolTable, patrol);
        addHoverListener(trafficStopTable, trafficstop);
        addHoverListener(citationTable, citation);
        addHoverListener(impoundTable, impound);
        addHoverListener(incidentTable, incident);

        Platform.runLater(() -> {
            loadLogs();

            sideButtons.addAll(Arrays.asList(callout, arrest, citation, impound, incident, patrol, search, trafficstop, death, accident));
            for (Node child : customReportsVBox.getChildren()) {
                if (child instanceof Label) {
                    System.out.println("added: " + ((Label) child).getText());
                    sideButtons.add((Label) child);
                }
            }

            List<Node> nodesToRemove = new ArrayList<>();
            for (Node table : tableStackPane.getChildren()) {
                if (table instanceof TableView) {
                    nodesToRemove.add(table);
                }
            }

            setActive(calloutTable, callout);

            for (Label otherPane : sideButtons) {
                otherPane.setStyle("-fx-background-color: transparent;");
            }

            activePane = callout;
            callout.setStyle("-fx-background-color: rgb(0,0,0,0.1); -fx-background-radius: 7 0 0 7;");
        });

        subHeading.setText(localization.getLocalizedMessage("LogBrowser.reportDatabaseLabel", "Report Database"));
        totalReportsHeader.setText(localization.getLocalizedMessage("LogBrowser.TotalReports", "Total Reports:"));
        reportsInProgressHeader.setText(localization.getLocalizedMessage("LogBrowser.reportsInProgressHeader", "Reports In Progress:"));
        closedReportsHeader.setText(localization.getLocalizedMessage("LogBrowser.closedReportsHeader", "Reports Closed:"));
    }

    public void loadLogs() {
        impoundLogUpdate();
        citationLogUpdate();
        patrolLogUpdate();
        arrestLogUpdate();
        searchLogUpdate();
        incidentLogUpdate();
        trafficStopLogUpdate();
        calloutLogUpdate();
        deathReportLogUpdate();
        accidentReportUpdate();

        String dataFolderPath = getCustomDataLogsFolderPath();
        createFolderIfNotExists(dataFolderPath);
        File dataFolder = new File(dataFolderPath);
        if (dataFolder.exists() && dataFolder.isDirectory()) {
            customReportsVBox.getChildren().clear();

            File[] files = dataFolder.listFiles((dir, filen) -> filen.endsWith(".db"));
            if (files != null && files.length != 0) {
                log("LogViewer; Found " + files.length + " Database(s)", LogUtils.Severity.INFO);
                for (File dbFile : files) {
                    String fileNameWithoutExt = dbFile.getName().replaceFirst("[.][^.]+$", "");
                    String dbFilePath = dbFile.getAbsolutePath();
                    log("LogViewer; [" + dbFile.getName() + "] Being Checked", LogUtils.Severity.INFO);

                    if (isValidDatabase(dbFilePath, dbFile.getName())) {
                        log("LogViewer; [" + dbFile.getName() + "] Valid", LogUtils.Severity.INFO);

                        Label reportBtn = new Label();
                        reportBtn.setAlignment(Pos.CENTER);
                        reportBtn.setText(fileNameWithoutExt);
                        reportBtn.getStyleClass().add("side-button");
                        reportBtn.setOnMouseClicked(event -> {
                            loadTableForCustomReport(dbFilePath, fileNameWithoutExt);
                        });

                        customReportsVBox.getChildren().add(reportBtn);

                    } else {
                        log("LogViewer; Invalid database file: " + dbFilePath, LogUtils.Severity.WARN);
                    }
                }
            }
        }
    }

    private void loadTableForCustomReport(String dbFilePath, String fileNameWithoutExt) {
        AtomicBoolean firstLoad = new AtomicBoolean(false);

        Platform.runLater(() -> {
            String dataFolderPath = getCustomDataLogsFolderPath();

            String reportTitle = fileNameWithoutExt.trim();

            currentTable = null;
            totalReportsLabel.setText("0");
            reportsInProgressLabel.setText("0");
            closedReportsLabel.setText("0");
            tableStackPane.getChildren().clear();

            BorderPane customReportPane = new BorderPane();
            tableStackPane.getChildren().add(customReportPane);

            currentTable = new TableView();
            currentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            currentTable.getColumns().clear();
            currentTable.getItems().clear();
            customReportPane.setCenter(currentTable);

            Map<String, String> layoutScheme = null;
            try {
                layoutScheme = DynamicDB.getTableColumnsDefinition(dbFilePath, "layout");
            } catch (SQLException e2) {
                logError("LogViewer; Failed to extract field names", e2);
            }

            String layoutContent = null;
            String columnLayoutContent = null;
            DynamicDB DatabaseLayout = new DynamicDB(dataFolderPath + reportTitle, "layout", "key", layoutScheme);
            if (DatabaseLayout.initDB()) {
                log("LogViewer; Layout Database for: [" + reportTitle + "] Initialized", LogUtils.Severity.INFO);
                try {
                    Map<String, Object> layoutRecord = DatabaseLayout.getRecord("1");
                    if (layoutRecord != null) {
                        Object layoutValue = layoutRecord.get("layoutData");
                        layoutContent = (String) layoutValue;
                    } else {
                        log("LogViewer; layout record null for report: " + reportTitle, LogUtils.Severity.ERROR);
                    }
                    Map<String, Object> columnLayoutRecord = DatabaseLayout.getRecord("3");
                    if (columnLayoutRecord != null) {
                        Object layoutValue = columnLayoutRecord.get("columnLayoutData");
                        columnLayoutContent = (String) layoutValue;
                    } else {
                        log("LogViewer; columnLayoutData record null for report: " + reportTitle, LogUtils.Severity.ERROR);
                        DatabaseLayout.addColumnIfNotExists("columnLayoutData", "TEXT");
                        Map<String, Object> columnRecord = new HashMap<>();
                        columnRecord.put("key", "3");
                        if (layoutContent != null) {
                            StringBuilder columnLayoutBuilder = new StringBuilder();
                            Map<String, Map<String, List<String>>> elementMap = parseAndPopulateMap(layoutContent);
                            Map<String, List<String>> fieldNames = elementMap.getOrDefault("fieldNames", new HashMap<>());
                            Map<String, List<String>> selectedTypes = elementMap.getOrDefault("selectedType", new HashMap<>());

                            for (String key : fieldNames.keySet()) {
                                String selectedType = selectedTypes.getOrDefault(key, List.of()).isEmpty() ? "" : selectedTypes.get(key).get(0);
                                if ("NUMBER_FIELD".equalsIgnoreCase(selectedType)) {
                                    columnLayoutBuilder.append(key + "=true");
                                } else {
                                    columnLayoutBuilder.append(key + "=false");
                                }
                                columnLayoutBuilder.append(",");
                            }
                            columnLayoutBuilder.append("report_status=true");

                            columnLayoutContent = columnLayoutBuilder.toString();
                            columnRecord.put("columnLayoutData", columnLayoutBuilder.toString());
                            DatabaseLayout.addOrReplaceRecord(columnRecord);

                            firstLoad.set(true);
                        }
                    }
                } catch (SQLException e2) {
                    logError("LogViewer; Error adding/replacing record: " + layoutScheme, e2);
                } finally {
                    try {
                        DatabaseLayout.close();
                    } catch (SQLException e2) {
                        logError("LogViewer; Error closing Database: [" + DatabaseLayout.getTableName() + "]", e2);
                    }
                }
            } else {
                log("LogViewer; Layout Database not initialized!", LogUtils.Severity.ERROR);
                showNotificationError("Report Creation Utility", "Error initializing layout database!");
            }

            if (columnLayoutContent != null) {
                try {

                    List<String> allColumns = new ArrayList<>(Arrays.asList(columnLayoutContent.split(",")));
                    List<String> visibleColumns = new ArrayList<>();
                    List<String> hiddenColumns = new ArrayList<>();

                    for (String column : allColumns) {
                        String[] parts = column.split("=");
                        if (parts.length == 2) {
                            if (parts[1].equals("true")) {
                                visibleColumns.add(parts[0]);
                            } else {
                                hiddenColumns.add(parts[0]);
                            }
                        }
                    }

                    for (String columnName : visibleColumns) {
                        TableColumn<Map<String, Object>, String> col;
                        if (columnName.equals("report_status")) {
                            col = new TableColumn<>(localization.getLocalizedMessage("LogBrowser.ReportStatus", "Status"));
                        } else {
                            col = new TableColumn<>(columnName);
                        }
                        col.setReorderable(false);
                        col.setEditable(false);
                        col.setSortable(false);
                        col.setCellValueFactory(param -> {
                            Object value = param.getValue().get(columnName);
                            return new SimpleStringProperty(value != null ? value.toString() : "");
                        });
                        col.setCellFactory(column -> new TableCell<Map<String, Object>, String>() {
                            @Override
                            protected void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty || item == null) {
                                    setText(null);
                                    setGraphic(null);
                                } else {
                                    Label label = new Label(item);
                                    String baseStyle = "-fx-background-radius: 7; -fx-padding: 1 10;" + commonTableFontSize;
                                    if (item.trim().equalsIgnoreCase("In Progress")) {
                                        label.setStyle(baseStyle + " -fx-background-color: rgba(211,94,243,0.78);-fx-text-fill: white;");
                                    } else if (item.trim().equalsIgnoreCase("Reopened")) {
                                        label.setStyle(baseStyle + " -fx-background-color: rgba(121,175,255,0.78);-fx-text-fill: white;");
                                    } else if (item.trim().equalsIgnoreCase("Pending")) {
                                        label.setStyle(baseStyle + " -fx-background-color: rgba(199,204,87,0.78);-fx-text-fill: white;");
                                    } else if (item.trim().equalsIgnoreCase("Cancelled")) {
                                        label.setStyle(baseStyle + " -fx-background-color: rgba(244,96,75,0.78);-fx-text-fill: white;");
                                    } else {
                                        label.setStyle(baseStyle);
                                    }
                                    setGraphic(label);
                                }
                            }
                        });

                        currentTable.getColumns().add(col);
                    }

                    Map<String, String> dataScheme = null;
                    try {
                        dataScheme = DynamicDB.getTableColumnsDefinition(dbFilePath, "data");
                    } catch (SQLException e2) {
                        logError("LogViewer; Failed to extract field names data table", e2);
                    }
                    String foundPrimaryKeyDataTable = null;
                    try {
                        foundPrimaryKeyDataTable = getPrimaryKeyColumn(dbFilePath, "data");
                    } catch (SQLException e) {
                        logError("LogViewer; Error getting primary key column [4]", e);
                    }
                    DynamicDB databaseData = new DynamicDB(dataFolderPath + reportTitle, "data", foundPrimaryKeyDataTable, dataScheme);
                    if (databaseData.initDB()) {
                        log("LogViewer; data Database for: [" + reportTitle + "] Initialized", LogUtils.Severity.INFO);
                        try {
                            currentTable.getItems().setAll(databaseData.getAllRecords());

                            Map<String, String> finalDataScheme = dataScheme;
                            Map<String, String> finalLayoutScheme = layoutScheme;
                            String finalFoundPrimaryKeyDataTable = foundPrimaryKeyDataTable;
                            String finalLayoutContent = layoutContent;
                            currentTable.setRowFactory(tableView -> {
                                TableRow<Map<String, Object>> row = new TableRow<>();
                                row.setOnMouseClicked(mouseEvent -> {
                                    if (mouseEvent.getClickCount() == 1 && !row.isEmpty()) {
                                        Map<String, Object> record = row.getItem();

                                        Map<String, Map<String, List<String>>> elementMap = parseAndPopulateMap(finalLayoutContent);

                                        CustomReport customReport = new CustomReport(reportTitle, finalFoundPrimaryKeyDataTable, finalLayoutScheme, finalDataScheme, record, elementMap);

                                        Map<String, List<String>> selectedTypes = customReport.getMainMap().getOrDefault("selectedType", new HashMap<>());
                                        Map<String, List<String>> fieldNames = customReport.getMainMap().getOrDefault("fieldNames", new HashMap<>());

                                        String reportNum = "";
                                        for (String field : fieldNames.keySet()) {
                                            String selectedType = selectedTypes.getOrDefault(field, List.of()).isEmpty() ? "" : selectedTypes.get(field).get(0);
                                            if ("NUMBER_FIELD".equalsIgnoreCase(selectedType)) {
                                                reportNum = record.get(field).toString();
                                                TextField number_field = (TextField) customReport.getReportMap().get(field);
                                                number_field.setEditable(false);
                                            }
                                        }

                                        Button delBtn = (Button) customReport.getReportWindowMap().get("delBtn");
                                        delBtn.setVisible(true);
                                        delBtn.setDisable(false);
                                        String numToDelete = reportNum;
                                        delBtn.setOnAction(actionEvent -> {
                                            databaseData.deleteRecord(dbFilePath, "data", finalFoundPrimaryKeyDataTable, numToDelete);
                                            CustomWindow window = getWindow(reportTitle);
                                            if (window != null) {
                                                window.closeWindow();
                                            }
                                            try {
                                                if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                                                    playSound(getJarPath() + "/sounds/alert-delete.wav");
                                                }
                                            } catch (IOException e) {
                                                logError("Error getting configValue for playDeleteReport: ", e);
                                            }
                                            showNotificationInfo("Report Manager", "Deleted [" + reportTitle + "] #: " + numToDelete);

                                        });

                                        MenuButton pullnotesbtn = (MenuButton) customReport.getReportWindowMap().get("pullNotesBtn");
                                        pullnotesbtn.setVisible(false);

                                        ComboBox<String> statusValue = (ComboBox<String>) customReport.getReportWindowMap().get("statusValue");
                                        statusValue.setValue("Reopened");

                                        Button submitBtn = (Button) customReport.getReportWindowMap().get("submitBtn");
                                        submitBtn.setText("Update Information");

                                    }
                                });
                                return row;
                            });

                        } finally {
                            try {
                                databaseData.close();
                            } catch (SQLException e2) {
                                logError("LogViewer; Error closing Database: [" + databaseData.getTableName() + "]", e2);
                            }
                        }
                    } else {
                        log("LogViewer; data Database not initialized!", LogUtils.Severity.ERROR);
                        showNotificationError("Report Creation Utility", "Error initializing data database!");
                    }

                    HBox tableSettingsHbox = new HBox(10);
                    TitledPane titledPane = new TitledPane(localization.getLocalizedMessage("LogBrowser.TableSettings", "Table Settings"), tableSettingsHbox);
                    titledPane.setStyle("-fx-background-color: rgba(255,255,255,0.25);");

                    VBox visibleColumnsBox = new VBox(5);
                    visibleColumnsBox.setMaxHeight(160);
                    Label visibleLabel = new Label(localization.getLocalizedMessage("LogBrowser.VisibleColumns", "Visible Columns"));
                    ListView<String> visibleColumnsList = new ListView<>();
                    visibleColumnsBox.getChildren().addAll(visibleLabel, visibleColumnsList);

                    VBox hiddenColumnsBox = new VBox(5);
                    hiddenColumnsBox.setMaxHeight(160);
                    Label hiddenLabel = new Label(localization.getLocalizedMessage("LogBrowser.HiddenColumns", "Hidden Columns"));
                    ListView<String> hiddenColumnsList = new ListView<>();
                    hiddenColumnsBox.getChildren().addAll(hiddenLabel, hiddenColumnsList);

                    VBox controlButtons = new VBox(5);
                    Button moveRightBtn = new Button("→");
                    Button moveLeftBtn = new Button("←");
                    Button moveUpBtn = new Button("↑");
                    Button moveDownBtn = new Button("↓");
                    Button refreshBtn = new Button(localization.getLocalizedMessage("LogBrowser.Refresh", "Refresh"));
                    moveRightBtn.getStyleClass().add("small-button");
                    moveLeftBtn.getStyleClass().add("small-button");
                    moveUpBtn.getStyleClass().add("small-button");
                    moveDownBtn.getStyleClass().add("small-button");
                    refreshBtn.getStyleClass().add("small-button");

                    String buttonStyle = "-fx-min-width: 40px;";
                    moveRightBtn.setStyle(buttonStyle);
                    moveLeftBtn.setStyle(buttonStyle);
                    moveUpBtn.setStyle(buttonStyle);
                    moveDownBtn.setStyle(buttonStyle);

                    controlButtons.getChildren().addAll(moveUpBtn, moveDownBtn, moveLeftBtn, moveRightBtn, refreshBtn);
                    controlButtons.setAlignment(Pos.BOTTOM_CENTER);

                    ObservableList<String> visibleItems = FXCollections.observableArrayList(visibleColumns);
                    ObservableList<String> hiddenItems = FXCollections.observableArrayList(hiddenColumns);
                    visibleColumnsList.setItems(visibleItems);
                    hiddenColumnsList.setItems(hiddenItems);

                    HBox.setHgrow(visibleColumnsBox, Priority.SOMETIMES);
                    HBox.setHgrow(hiddenColumnsBox, Priority.SOMETIMES);
                    HBox.setHgrow(controlButtons, Priority.NEVER);
                    controlButtons.setMinWidth(Region.USE_PREF_SIZE);

                    tableSettingsHbox.getChildren().addAll(visibleColumnsBox, controlButtons, hiddenColumnsBox);

                    Map<String, String> finalLayoutScheme1 = layoutScheme;
                    moveLeftBtn.setOnAction(e -> {
                        String selected = hiddenColumnsList.getSelectionModel().getSelectedItem();
                        if (selected != null) {
                            handleShowColumn(selected, allColumns, dataFolderPath, reportTitle, finalLayoutScheme1, dbFilePath, fileNameWithoutExt, visibleColumnsList, hiddenColumnsList, visibleItems, hiddenItems);
                        }
                    });

                    moveRightBtn.setOnAction(e -> {
                        String selected = visibleColumnsList.getSelectionModel().getSelectedItem();
                        if (selected != null) {
                            handleHideColumn(selected, allColumns, dataFolderPath, reportTitle, finalLayoutScheme1, dbFilePath, fileNameWithoutExt, visibleColumnsList, hiddenColumnsList, visibleItems, hiddenItems);
                        }
                    });

                    moveUpBtn.setOnAction(e -> {
                        String selected = visibleColumnsList.getSelectionModel().getSelectedItem();
                        if (selected != null) {
                            handleMoveColumn(selected, -1, allColumns, dataFolderPath, reportTitle, finalLayoutScheme1, dbFilePath, fileNameWithoutExt, visibleColumnsList, hiddenColumnsList, visibleItems, hiddenItems);
                        }
                    });

                    moveDownBtn.setOnAction(e -> {
                        String selected = visibleColumnsList.getSelectionModel().getSelectedItem();
                        if (selected != null) {
                            handleMoveColumn(selected, 1, allColumns, dataFolderPath, reportTitle, finalLayoutScheme1, dbFilePath, fileNameWithoutExt, visibleColumnsList, hiddenColumnsList, visibleItems, hiddenItems);
                        }
                    });

                    refreshBtn.setOnAction(e -> loadTableForCustomReport(dbFilePath, fileNameWithoutExt));

                    if (firstLoad.get()) {
                        log("LogViewer; First Initialization for [" + fileNameWithoutExt + "] Database, Refreshing..", LogUtils.Severity.INFO);
                        loadTableForCustomReport(dbFilePath, fileNameWithoutExt);
                    }

                    customReportPane.setBottom(titledPane);

                    ObservableList<Map<String, Object>> items = currentTable.getItems();

                    Map<String, Long> statusCount = items.stream().map(row -> row.get("report_status")).filter(Objects::nonNull).map(Object::toString).collect(Collectors.groupingBy(status -> {
                        if (status.equalsIgnoreCase("In Progress") || status.equalsIgnoreCase("Pending") || status.equalsIgnoreCase("Reopened")) {
                            return "In Progress";
                        } else if (status.equalsIgnoreCase("Cancelled") || status.equalsIgnoreCase("Closed")) {
                            return "Closed";
                        } else {
                            return status;
                        }
                    }, Collectors.counting()));

                    long totalReports = items.size();
                    long reportsInProgress = statusCount.getOrDefault("In Progress", 0L);
                    long closedReports = statusCount.getOrDefault("Closed", 0L);

                    Platform.runLater(() -> {
                        totalReportsLabel.setText(String.valueOf(totalReports));
                        reportsInProgressLabel.setText(String.valueOf(reportsInProgress));
                        closedReportsLabel.setText(String.valueOf(closedReports));
                        reportsInProgressBar.setProgress((double) reportsInProgress / totalReports);
                        closedReportsProgressBar.setProgress((double) closedReports / totalReports);
                    });

                } catch (Exception e) {
                    logError("LogViewer; Failed to initialize report columns", e);
                }
            }
        });
    }

    private void refresh(ListView<String> visibleColumns, ListView<String> hiddenColumns, ObservableList<String> visibleItems, ObservableList<String> hiddenItems, List<String> allColumns) {
        List<String> currentVisible = new ArrayList<>();
        List<String> currentHidden = new ArrayList<>();

        for (String entry : allColumns) {
            String[] parts = entry.split("=");
            if (parts.length == 2) {
                if (parts[1].equals("true")) {
                    currentVisible.add(parts[0]);
                } else {
                    currentHidden.add(parts[0]);
                }
            }
        }

        visibleItems.setAll(currentVisible);
        hiddenItems.setAll(currentHidden);
        visibleColumns.refresh();
        hiddenColumns.refresh();
    }

    private void updateColumnLayout(List<String> allColumns, String dataFolderPath, String reportTitle, Map<String, String> layoutScheme) {
        DynamicDB dbLayout = new DynamicDB(dataFolderPath + reportTitle, "layout", "key", layoutScheme);
        try {
            if (dbLayout.initDB()) {
                Map<String, Object> record = new HashMap<>();
                record.put("key", "3");
                record.put("columnLayoutData", String.join(",", allColumns));
                dbLayout.addOrReplaceRecord(record);
            }
        } catch (SQLException ex) {
            logError("Error updating column layout", ex);
        }
        List<String> currentVisible = new ArrayList<>();
        List<String> currentHidden = new ArrayList<>();

        for (String entry : allColumns) {
            String[] parts = entry.split("=");
            if (parts.length == 2) {
                if (parts[1].equals("true")) {
                    currentVisible.add(parts[0]);
                } else {
                    currentHidden.add(parts[0]);
                }
            }
        }
    }

    private void handleShowColumn(String columnName, List<String> allColumns, String dataFolderPath, String reportTitle, Map<String, String> layoutScheme, String dbFilePath, String fileNameWithoutExt, ListView<String> visibleColumns, ListView<String> hiddenColumns, ObservableList<String> visibleItems, ObservableList<String> hiddenItems) {
        if (columnName == null) return;

        for (int i = 0; i < allColumns.size(); i++) {
            String entry = allColumns.get(i);
            if (entry.startsWith(columnName + "=")) {
                allColumns.set(i, columnName + "=true");
                break;
            }
        }

        updateColumnLayout(allColumns, dataFolderPath, reportTitle, layoutScheme);
        loadTableForCustomReport(dbFilePath, fileNameWithoutExt);

        refresh(visibleColumns, hiddenColumns, visibleItems, hiddenItems, allColumns);

    }

    private void handleHideColumn(String columnName, List<String> allColumns, String dataFolderPath, String reportTitle, Map<String, String> layoutScheme, String dbFilePath, String fileNameWithoutExt, ListView<String> visibleColumns, ListView<String> hiddenColumns, ObservableList<String> visibleItems, ObservableList<String> hiddenItems) {
        if (columnName == null) return;

        for (int i = 0; i < allColumns.size(); i++) {
            String entry = allColumns.get(i);
            if (entry.startsWith(columnName + "=")) {
                allColumns.set(i, columnName + "=false");
                break;
            }
        }

        updateColumnLayout(allColumns, dataFolderPath, reportTitle, layoutScheme);
        loadTableForCustomReport(dbFilePath, fileNameWithoutExt);

        refresh(visibleColumns, hiddenColumns, visibleItems, hiddenItems, allColumns);
    }

    private void handleMoveColumn(String columnName, int direction, List<String> allColumns, String dataFolderPath, String reportTitle, Map<String, String> layoutScheme, String dbFilePath, String fileNameWithoutExt, ListView<String> visibleColumns, ListView<String> hiddenColumns, ObservableList<String> visibleItems, ObservableList<String> hiddenItems) {
        if (columnName == null || columnName.isEmpty()) {
            log("LogViewer; Column Move operation aborted: No column selected", LogUtils.Severity.WARN);
            return;
        }

        List<Integer> visibleIndices = new ArrayList<>();
        for (int i = 0; i < allColumns.size(); i++) {
            if (allColumns.get(i).endsWith("=true")) {
                visibleIndices.add(i);
            }
        }

        int currentVisibleIndex = -1;
        for (int i = 0; i < visibleIndices.size(); i++) {
            int physicalIndex = visibleIndices.get(i);
            if (allColumns.get(physicalIndex).startsWith(columnName + "=")) {
                currentVisibleIndex = i;
                break;
            }
        }

        if (currentVisibleIndex == -1) {
            log("LogViewer; Column Move failed: Column not found in visible columns - " + columnName, LogUtils.Severity.ERROR);
            return;
        }

        int newVisibleIndex = currentVisibleIndex + direction;
        if (newVisibleIndex < 0 || newVisibleIndex >= visibleIndices.size()) {
            log("LogViewer; Column Move out of bounds: " + newVisibleIndex + " (visible columns count: " + visibleIndices.size() + ")", LogUtils.Severity.WARN);
            return;
        }

        int currentPhysicalIndex = visibleIndices.get(currentVisibleIndex);
        int newPhysicalIndex = visibleIndices.get(newVisibleIndex);

        log(String.format("LogViewer; Moving column from physical index %d to %d", currentPhysicalIndex, newPhysicalIndex), LogUtils.Severity.DEBUG);

        Collections.swap(allColumns, currentPhysicalIndex, newPhysicalIndex);

        try {
            updateColumnLayout(allColumns, dataFolderPath, reportTitle, layoutScheme);
        } catch (Exception e) {
            logError("Failed to update column layout after move", e);
            return;
        }

        loadTableForCustomReport(dbFilePath, fileNameWithoutExt);

        refresh(visibleColumns, hiddenColumns, visibleItems, hiddenItems, allColumns);

    }

    public static void patrolLogUpdate() {
        if (logController == null) {
            return;
        }
        try {
            PatrolReports patrolReports = PatrolReportUtils.loadPatrolReports();
            List<PatrolReport> patrolReportList = patrolReports.getPatrolReportList();
            if (patrolReportList == null) {
                patrolReportList = new ArrayList<>();
            }
            logController.getPatrolTable().getItems().clear();
            logController.getPatrolTable().getItems().addAll(patrolReportList);
        } catch (JAXBException e) {
            logError("Error loading PatrolReports: ", e);
        }
    }

    public static void calloutLogUpdate() {
        if (logController == null) return;
        try {
            CalloutReports calloutReports = CalloutReportUtils.loadCalloutReports();
            List<CalloutReport> logEntries = calloutReports.getCalloutReportList();
            if (logEntries == null) logEntries = new ArrayList<>();
            logController.getCalloutTable().getItems().clear();
            logController.getCalloutTable().getItems().addAll(logEntries);
        } catch (JAXBException e) {
            logError("Error loading CalloutReports: ", e);
        }
    }

    public static void citationLogUpdate() {
        if (logController == null) return;
        try {
            TrafficCitationReports reports = TrafficCitationUtils.loadTrafficCitationReports();
            List<TrafficCitationReport> logEntries = reports.getTrafficCitationReportList();
            if (logEntries == null) logEntries = new ArrayList<>();
            logController.getCitationTable().getItems().clear();
            logController.getCitationTable().getItems().addAll(logEntries);
        } catch (JAXBException e) {
            logError("Error loading TrafficCitationReports: ", e);
        }
    }

    public static void arrestLogUpdate() {
        if (logController == null) return;
        try {
            ArrestReports reports = ArrestReportUtils.loadArrestReports();
            List<ArrestReport> logEntries = reports.getArrestReportList();
            if (logEntries == null) logEntries = new ArrayList<>();
            logController.getArrestTable().getItems().clear();
            logController.getArrestTable().getItems().addAll(logEntries);
        } catch (JAXBException e) {
            logError("Error loading ArrestReports: ", e);
        }
    }

    public static void searchLogUpdate() {
        if (logController == null) return;
        try {
            SearchReports reports = SearchReportUtils.loadSearchReports();
            List<SearchReport> logEntries = reports.getSearchReportList();
            if (logEntries == null) logEntries = new ArrayList<>();
            logController.getSearchTable().getItems().clear();
            logController.getSearchTable().getItems().addAll(logEntries);
        } catch (JAXBException e) {
            logError("Error loading SearchReports: ", e);
        }
    }

    public static void incidentLogUpdate() {
        if (logController == null) return;
        try {
            IncidentReports reports = IncidentReportUtils.loadIncidentReports();
            List<IncidentReport> logEntries = reports.getIncidentReportList();
            if (logEntries == null) logEntries = new ArrayList<>();
            logController.getIncidentTable().getItems().clear();
            logController.getIncidentTable().getItems().addAll(logEntries);
        } catch (JAXBException e) {
            logError("Error loading IncidentReports: ", e);
        }
    }

    public static void trafficStopLogUpdate() {
        if (logController == null) return;
        try {
            TrafficStopReports reports = TrafficStopReportUtils.loadTrafficStopReports();
            List<TrafficStopReport> logEntries = reports.getTrafficStopReportList();
            if (logEntries == null) logEntries = new ArrayList<>();
            logController.getTrafficStopTable().getItems().clear();
            logController.getTrafficStopTable().getItems().addAll(logEntries);
        } catch (JAXBException e) {
            logError("Error loading TrafficStopReports: ", e);
        }
    }

    public static void impoundLogUpdate() {
        if (logController == null) return;
        try {
            ImpoundReports reports = ImpoundReportUtils.loadImpoundReports();
            List<ImpoundReport> logEntries = reports.getImpoundReportList();
            if (logEntries == null) logEntries = new ArrayList<>();
            logController.getImpoundTable().getItems().clear();
            logController.getImpoundTable().getItems().addAll(logEntries);
        } catch (JAXBException e) {
            logError("Error loading ImpoundReports: ", e);
        }
    }

    public static void deathReportLogUpdate() {
        if (logController == null) return;
        try {
            DeathReports reports = DeathReportUtils.loadDeathReports();
            List<DeathReport> logEntries = reports.getDeathReportList();
            if (logEntries == null) logEntries = new ArrayList<>();
            logController.getDeathReportTable().getItems().clear();
            logController.getDeathReportTable().getItems().addAll(logEntries);
        } catch (JAXBException e) {
            logError("Error loading DeathReports: ", e);
        }
    }

    public static void accidentReportUpdate() {
        if (logController == null) return;
        try {
            AccidentReports reports = AccidentReportUtils.loadAccidentReports();
            List<AccidentReport> logEntries = reports.getAccidentReportList();
            if (logEntries == null) logEntries = new ArrayList<>();
            logController.getAccidentReportTable().getItems().clear();
            logController.getAccidentReportTable().getItems().addAll(logEntries);
        } catch (JAXBException e) {
            logError("Error loading AccidentReports: ", e);
        }
    }

    @FXML
    public void onDeathReportRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            DeathReport deathReport = (DeathReport) deathReportTable.getSelectionModel().getSelectedItem();

            if (deathReport != null) {
                Map<String, Object> deathReportObj = newDeathReport();

                Map<String, Object> deathReport1 = (Map<String, Object>) deathReportObj.get(localization.getLocalizedMessage("ReportWindows.DeathReportTitle", "Death Report") + " Map");

                TextField name = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField rank = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField div = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField agen = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField num = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
                TextField date = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField time = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
                ComboBox street = (ComboBox) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
                ComboBox area = (ComboBox) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
                TextField county = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
                TextField deathNum = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathNumField", "death num"));
                TextField decedent = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathDecedentField", "decedent name"));
                TextField age = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathAgeDOBField", "age/dob"));
                TextField gender = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathGenderField", "gender"));
                TextField address = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathReportAddressField", "address"));
                TextField description = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DeathDescField", "description"));
                TextField causeofdeath = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.CauseOfDeathField", "cause of death"));
                TextField modeofdeath = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.ModeOfDeathField", "mode of death"));
                TextField witnesses = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"));
                TextArea notes = (TextArea) deathReport1.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
                TextField timeofdeath = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.TimeOfDeathField", "time of death"));
                TextField dateofdeath = (TextField) deathReport1.get(localization.getLocalizedMessage("ReportWindows.DateOfDeathField", "date of death"));

                timeofdeath.setText(deathReport.getTimeOfDeath());
                dateofdeath.setText(deathReport.getDateOfDeath());
                name.setText(deathReport.getOfficerName());
                rank.setText(deathReport.getRank());
                div.setText(deathReport.getDivision());
                agen.setText(deathReport.getAgency());
                num.setText(deathReport.getNumber());
                date.setText(deathReport.getDate());
                time.setText(deathReport.getTime());
                street.getEditor().setText(deathReport.getStreet());
                area.setValue(deathReport.getArea());
                county.setText(deathReport.getCounty());
                deathNum.setText(deathReport.getDeathReportNumber());
                decedent.setText(deathReport.getDecedent());
                age.setText(deathReport.getAge());
                gender.setText(deathReport.getGender());
                address.setText(deathReport.getAddress());
                description.setText(deathReport.getDescription());
                causeofdeath.setText(deathReport.getCauseOfDeath());
                modeofdeath.setText(deathReport.getModeOfDeath());
                witnesses.setText(deathReport.getWitnesses());
                notes.setText(deathReport.getNotesTextArea());

                Button delBtn = (Button) deathReportObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = deathNum.getText();
                    try {
                        DeathReportUtils.deleteDeathReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete DeathReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow("Death Report");
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    deathReportLogUpdate();
                });

                deathNum.setEditable(false);
                MenuButton pullnotesbtn = (MenuButton) deathReportObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);

                ComboBox<String> statusValue = (ComboBox<String>) deathReportObj.get("statusValue");
                statusValue.setValue("Reopened");

                Button submitBtn = (Button) deathReportObj.get("submitBtn");
                submitBtn.setText("Update Information");

                deathReportTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void onCalloutRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            CalloutReport calloutReport = (CalloutReport) calloutTable.getSelectionModel().getSelectedItem();

            if (calloutReport != null) {

                Map<String, Object> calloutReportObj = newCallout();

                Map<String, Object> calloutReportMap = (Map<String, Object>) calloutReportObj.get(localization.getLocalizedMessage("ReportWindows.CalloutReportTitle", "Callout Report") + " Map");

                TextField officername = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField officerrank = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField officerdiv = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField officeragen = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField officernum = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
                TextField calloutnum = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.CalloutNumberField", "callout num"));
                ComboBox calloutarea = (ComboBox) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
                TextArea calloutnotes = (TextArea) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
                TextField calloutcounty = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
                ComboBox calloutstreet = (ComboBox) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
                TextField calloutdate = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField callouttime = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
                TextField callouttype = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
                TextField calloutcode = (TextField) calloutReportMap.get(localization.getLocalizedMessage("ReportWindows.CalloutCodeField", "code"));

                officername.setText(calloutReport.getOfficerName());
                officerrank.setText(calloutReport.getRank());
                officerdiv.setText(calloutReport.getDivision());
                officeragen.setText(calloutReport.getAgency());
                officernum.setText(calloutReport.getNumber());
                calloutdate.setText(calloutReport.getDate());
                callouttime.setText(calloutReport.getTime());
                calloutstreet.setValue(calloutReport.getAddress());
                calloutarea.setValue(calloutReport.getArea());
                calloutcounty.setText(calloutReport.getCounty());
                calloutnotes.setText(calloutReport.getNotesTextArea());
                calloutnum.setText(calloutReport.getCalloutNumber());
                callouttype.setText(calloutReport.getResponseType());
                calloutcode.setText(calloutReport.getResponseGrade());

                ComboBox<String> statusValue = (ComboBox<String>) calloutReportObj.get("statusValue");
                statusValue.setValue("Reopened");

                Button delBtn = (Button) calloutReportObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = calloutnum.getText();
                    try {
                        CalloutReportUtils.deleteCalloutReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete CalloutReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow("Callout Report");
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    calloutLogUpdate();
                });

                MenuButton pullnotesbtn = (MenuButton) calloutReportObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);
                calloutnum.setEditable(false);

                Button submitBtn = (Button) calloutReportObj.get("submitBtn");
                submitBtn.setText("Update Information");

                calloutTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void onPatrolRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            PatrolReport patrolReport = (PatrolReport) patrolTable.getSelectionModel().getSelectedItem();

            if (patrolReport != null) {

                Map<String, Object> patrolReportObj = newPatrol();

                Map<String, Object> patrolReportMap = (Map<String, Object>) patrolReportObj.get(localization.getLocalizedMessage("ReportWindows.PatrolReportTitle", "Patrol Report") + " Map");

                TextField name = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField rank = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField div = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField agen = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField num = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
                TextField patrolnum = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.PatrolNumField", "patrolnumber"));
                TextArea notes = (TextArea) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));
                TextField date = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField starttime = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.StartTimeField", "starttime"));
                TextField stoptime = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.StopTimeField", "stoptime"));
                TextField length = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.LengthField", "length"));
                TextField vehicle = (TextField) patrolReportMap.get(localization.getLocalizedMessage("ReportWindows.VehicleField", "vehicle"));

                name.setText(patrolReport.getOfficerName());
                patrolnum.setText(patrolReport.getPatrolNumber());
                rank.setText(patrolReport.getOfficerRank());
                div.setText(patrolReport.getOfficerDivision());
                agen.setText(patrolReport.getOfficerAgency());
                num.setText(patrolReport.getOfficerNumber());
                date.setText(patrolReport.getPatrolDate());
                starttime.setText(patrolReport.getPatrolStartTime());
                stoptime.setText(patrolReport.getPatrolStopTime());
                length.setText(patrolReport.getPatrolLength());
                vehicle.setText(patrolReport.getOfficerVehicle());
                notes.setText(patrolReport.getPatrolComments());

                Button delBtn = (Button) patrolReportObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = patrolnum.getText();
                    try {
                        PatrolReportUtils.deletePatrolReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete PatrolReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow("Patrol Report");
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    patrolLogUpdate();
                });

                MenuButton pullnotesbtn = (MenuButton) patrolReportObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);
                patrolnum.setEditable(false);

                ComboBox<String> statusValue = (ComboBox<String>) patrolReportObj.get("statusValue");
                statusValue.setValue("Reopened");

                Button submitBtn = (Button) patrolReportObj.get("submitBtn");
                submitBtn.setText("Update Information");

                patrolTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void onTrafficStopRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            TrafficStopReport trafficStopReport = (TrafficStopReport) trafficStopTable.getSelectionModel().getSelectedItem();

            if (trafficStopReport != null) {

                Map<String, Object> trafficStopReportObj = newTrafficStop();

                Map<String, Object> trafficStopReportMap = (Map<String, Object>) trafficStopReportObj.get(localization.getLocalizedMessage("ReportWindows.TrafficStopReportTitle", "Traffic Stop Report") + " Map");

                TextField officernamets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField officerrankts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField officerdivts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField officeragents = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField officernumarrestts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

                TextField offenderNamets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
                TextField offenderAgets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
                TextField offenderGenderts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
                TextField offenderAddressts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
                TextField offenderDescriptionts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));

                ComboBox colorts = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
                ComboBox typets = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
                TextField plateNumberts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
                TextField otherInfots = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.OtherInfoField", "other info"));
                TextField modelts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));

                ComboBox areats = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
                ComboBox streetts = (ComboBox) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
                TextField countyts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
                TextField stopnumts = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.StopNumber", "stop number"));
                TextField datets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField timets = (TextField) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));

                TextArea notests = (TextArea) trafficStopReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));

                stopnumts.setText(trafficStopReport.getStopNumber());
                datets.setText(trafficStopReport.getDate());
                timets.setText(trafficStopReport.getTime());
                officerrankts.setText(trafficStopReport.getRank());
                notests.setText(trafficStopReport.getCommentsTextArea());
                plateNumberts.setText(trafficStopReport.getPlateNumber());

                offenderDescriptionts.setText(trafficStopReport.getOperatorDescription());
                otherInfots.setText(trafficStopReport.getResponseOtherInfo());
                areats.setValue(trafficStopReport.getArea());
                streetts.getEditor().setText(trafficStopReport.getStreet());
                countyts.setText(trafficStopReport.getCounty());

                offenderNamets.setText(trafficStopReport.getOperatorName());
                officernamets.setText(trafficStopReport.getOfficerName());
                officerdivts.setText(trafficStopReport.getDivision());
                officeragents.setText(trafficStopReport.getAgency());
                officernumarrestts.setText(trafficStopReport.getNumber());
                offenderAgets.setText(trafficStopReport.getOperatorAge());
                offenderGenderts.setText(trafficStopReport.getOperatorGender());
                offenderAddressts.setText(trafficStopReport.getOperatorAddress());
                colorts.setValue(trafficStopReport.getColor());
                typets.setValue(trafficStopReport.getType());
                modelts.setText(trafficStopReport.getResponseModel());

                BorderPane root = (BorderPane) trafficStopReportObj.get("root");
                Button delBtn = (Button) trafficStopReportObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = stopnumts.getText();
                    try {
                        TrafficStopReportUtils.deleteTrafficStopReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete TrafficStopReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow("Traffic Stop Report");
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    trafficStopLogUpdate();

                });

                MenuButton pullnotesbtn = (MenuButton) trafficStopReportObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);
                stopnumts.setEditable(false);

                ComboBox<String> statusValue = (ComboBox<String>) trafficStopReportObj.get("statusValue");
                statusValue.setValue("Reopened");

                Button submitBtn = (Button) trafficStopReportObj.get("submitBtn");
                submitBtn.setText("Update Information");

                trafficStopTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void onIncidentRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            IncidentReport incidentReport = (IncidentReport) incidentTable.getSelectionModel().getSelectedItem();

            if (incidentReport != null) {

                Map<String, Object> incidentReportObj = newIncident();

                Map<String, Object> incidentReportMap = (Map<String, Object>) incidentReportObj.get(localization.getLocalizedMessage("ReportWindows.IncidentReportTitle", "Incident Report") + " Map");

                TextField name = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField rank = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField div = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField agen = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField num = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

                TextField incidentnum = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.IncidentNumField", "incident num"));
                TextField date = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField time = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
                ComboBox street = (ComboBox) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
                ComboBox area = (ComboBox) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
                TextField county = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));

                TextField suspects = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.SuspectsField", "suspect(s)"));
                TextField vicwit = (TextField) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.VictimsWitnessField", "victim(s) / witness(s)"));
                TextArea statement = (TextArea) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.StatementField", "statement"));

                TextArea summary = (TextArea) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.IncidentSummaryField", "summary"));
                TextArea notes = (TextArea) incidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));

                name.setText(incidentReport.getOfficerName());
                incidentnum.setText(incidentReport.getIncidentNumber());
                rank.setText(incidentReport.getOfficerRank());
                div.setText(incidentReport.getOfficerDivision());
                agen.setText(incidentReport.getOfficerAgency());
                num.setText(incidentReport.getOfficerNumber());

                street.getEditor().setText(incidentReport.getIncidentStreet());
                area.setValue(incidentReport.getArea());
                county.setText(incidentReport.getCounty());
                suspects.setText(incidentReport.getIncidentWitnesses());
                vicwit.setText(incidentReport.getIncidentVictims());
                statement.setText(incidentReport.getIncidentStatement());

                date.setText(incidentReport.getIncidentDate());
                time.setText(incidentReport.getIncidentTime());
                summary.setText(incidentReport.getIncidentActionsTaken());
                notes.setText(incidentReport.getIncidentComments());

                Button delBtn = (Button) incidentReportObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = incidentnum.getText();
                    try {
                        IncidentReportUtils.deleteIncidentReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete IncidentReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow("Incident Report");
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    incidentLogUpdate();

                });

                MenuButton pullnotesbtn = (MenuButton) incidentReportObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);
                incidentnum.setEditable(false);

                ComboBox<String> statusValue = (ComboBox<String>) incidentReportObj.get("statusValue");
                statusValue.setValue("Reopened");

                Button submitBtn = (Button) incidentReportObj.get("submitBtn");
                submitBtn.setText("Update Information");

                incidentTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void onImpoundRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            ImpoundReport impoundReport = (ImpoundReport) impoundTable.getSelectionModel().getSelectedItem();

            if (impoundReport != null) {

                Map<String, Object> impoundReportObj = newImpound();

                Map<String, Object> impoundReportMap = (Map<String, Object>) impoundReportObj.get(localization.getLocalizedMessage("ReportWindows.ImpoundReportTitle", "Impound Report") + " Map");

                TextField officername = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField officerrank = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField officerdiv = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField officeragen = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField officernum = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

                TextField offenderName = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
                TextField offenderAge = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
                TextField offenderGender = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
                TextField offenderAddress = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));

                TextField num = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.ImpoundNumField", "impound number"));
                TextField date = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField time = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));

                ComboBox color = (ComboBox) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
                ComboBox type = (ComboBox) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
                TextField plateNumber = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
                TextField model = (TextField) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));

                TextArea notes = (TextArea) impoundReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));

                num.setText(impoundReport.getImpoundNumber());
                date.setText(impoundReport.getImpoundDate());
                time.setText(impoundReport.getImpoundTime());
                officerrank.setText(impoundReport.getOfficerRank());
                notes.setText(impoundReport.getImpoundComments());
                plateNumber.setText(impoundReport.getImpoundPlateNumber());

                offenderName.setText(impoundReport.getOwnerName());
                officername.setText(impoundReport.getOfficerName());
                officerdiv.setText(impoundReport.getOfficerDivision());
                officeragen.setText(impoundReport.getOfficerAgency());
                officernum.setText(impoundReport.getOfficerNumber());
                offenderAge.setText(impoundReport.getOwnerAge());
                offenderGender.setText(impoundReport.getOwnerGender());
                offenderAddress.setText(impoundReport.getOwnerAddress());
                color.setValue(impoundReport.getImpoundColor());
                type.setValue(impoundReport.getImpoundType());
                model.setText(impoundReport.getImpoundModel());

                BorderPane root = (BorderPane) impoundReportObj.get("root");
                Button delBtn = (Button) impoundReportObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = num.getText();
                    try {
                        ImpoundReportUtils.deleteImpoundReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete ImpoundReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow("Impound Report");
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    impoundLogUpdate();

                });

                MenuButton pullnotesbtn = (MenuButton) impoundReportObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);
                num.setEditable(false);

                ComboBox<String> statusValue = (ComboBox<String>) impoundReportObj.get("statusValue");
                statusValue.setValue("Reopened");

                Button submitBtn = (Button) impoundReportObj.get("submitBtn");
                submitBtn.setText("Update Information");

                impoundTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void onCitationRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            TrafficCitationReport trafficCitationReport = (TrafficCitationReport) citationTable.getSelectionModel().getSelectedItem();

            if (trafficCitationReport != null) {
                Map<String, Object> trafficCitationObj = newCitation();

                Map<String, Object> citationReportMap = (Map<String, Object>) trafficCitationObj.get(localization.getLocalizedMessage("ReportWindows.CitationReportTitle", "Citation Report") + " Map");

                TextField officername = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField officerrank = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField officerdiv = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField officeragen = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField officernum = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

                TextField offenderName = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
                TextField offenderAge = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
                TextField offenderGender = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
                TextField offenderAddress = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
                TextField offenderDescription = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));

                ComboBox area = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
                ComboBox street = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
                TextField county = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
                TextField num = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.CitationNumField", "citation number"));
                TextField date = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField time = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));

                ComboBox color = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
                ComboBox type = (ComboBox) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
                TextField plateNumber = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
                TextField otherInfo = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.OtherInfoField", "other info"));
                TextField model = (TextField) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));

                TextArea notes = (TextArea) citationReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));

                officername.setText(trafficCitationReport.getOfficerName());
                officerrank.setText(trafficCitationReport.getOfficerRank());
                officerdiv.setText(trafficCitationReport.getOfficerDivision());
                officeragen.setText(trafficCitationReport.getOfficerAgency());
                officernum.setText(trafficCitationReport.getOfficerNumber());
                street.setValue(trafficCitationReport.getCitationStreet());
                area.setValue(trafficCitationReport.getArea());
                county.setText(trafficCitationReport.getCounty());
                type.setValue(trafficCitationReport.getOffenderVehicleType());
                color.setValue(trafficCitationReport.getOffenderVehicleColor());
                date.setText(trafficCitationReport.getCitationDate());
                time.setText(trafficCitationReport.getCitationTime());
                notes.setText(trafficCitationReport.getCitationComments());
                num.setText(trafficCitationReport.getCitationNumber());
                plateNumber.setText(trafficCitationReport.getOffenderVehiclePlate());
                otherInfo.setText(trafficCitationReport.getOffenderVehicleOther());
                model.setText(trafficCitationReport.getOffenderVehicleModel());
                offenderName.setText(trafficCitationReport.getOffenderName());
                offenderAge.setText(trafficCitationReport.getOffenderAge());
                offenderGender.setText(trafficCitationReport.getOffenderGender());
                offenderDescription.setText(trafficCitationReport.getOffenderDescription());
                offenderAddress.setText(trafficCitationReport.getOffenderHomeAddress());

                Button delBtn = (Button) trafficCitationObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = num.getText();
                    try {
                        TrafficCitationUtils.deleteTrafficCitationReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete TrafficCitationReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow("Citation Report");
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    citationLogUpdate();

                });

                MenuButton pullnotesbtn = (MenuButton) trafficCitationObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);
                num.setEditable(false);

                TableView citationtable1 = (TableView) citationReportMap.get("CitationTableView");
                ObservableList<CitationsData> citationList = FXCollections.observableArrayList();
                citationtable1.setItems(citationList);

                ComboBox<String> statusValue = (ComboBox<String>) trafficCitationObj.get("statusValue");
                statusValue.setValue("Reopened");

                addCitationsToTable(trafficCitationReport.getCitationCharges(), citationList);

                Button submitBtn = (Button) trafficCitationObj.get("submitBtn");
                submitBtn.setText("Update Information");

                citationTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void onSearchRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            SearchReport searchReport = (SearchReport) searchTable.getSelectionModel().getSelectedItem();

            if (searchReport != null) {
                Map<String, Object> searchReportObj = newSearch();

                Map<String, Object> searchReportMap = (Map<String, Object>) searchReportObj.get(localization.getLocalizedMessage("ReportWindows.SearchReportTitle", "Search Report") + " Map");

                TextField name = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField rank = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField div = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField agen = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField num = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

                TextField searchnum = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchNumField", "search num"));
                TextField date = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField time = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
                ComboBox street = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
                ComboBox area = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
                TextField county = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));

                TextField grounds = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.GroundsForSearchField", "grounds for search"));
                TextField witness = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.WitnessesField", "witness(s)"));
                TextField searchedindividual = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchedIndividualField", "searched individual"));
                ComboBox type = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchTypeField", "search type"));
                ComboBox method = (ComboBox) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SearchMethodField", "search method"));

                TextField testconducted = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.TestsConductedField", "test(s) conducted"));
                TextField result = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.TestResultField", "result"));
                TextField bacmeasurement = (TextField) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.BACMeasurementField", "bac measurement"));

                TextArea seizeditems = (TextArea) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.SeizedItemsField", "seized item(s)"));
                TextArea notes = (TextArea) searchReportMap.get(localization.getLocalizedMessage("ReportWindows.CommentsField", "comments"));

                name.setText(searchReport.getOfficerName());
                div.setText(searchReport.getOfficerDivision());
                agen.setText(searchReport.getOfficerAgency());
                num.setText(searchReport.getOfficerNumber());

                street.getEditor().setText(searchReport.getSearchStreet());
                area.setValue(searchReport.getArea());
                county.setText(searchReport.getCounty());

                testconducted.setText(searchReport.getTestsConducted());
                grounds.setText(searchReport.getSearchGrounds());
                witness.setText(searchReport.getSearchWitnesses());
                searchedindividual.setText(searchReport.getSearchedPersons());
                type.setValue(searchReport.getSearchType());
                method.setValue(searchReport.getSearchMethod());
                result.setText(searchReport.getTestResults());
                bacmeasurement.setText(searchReport.getBreathalyzerBACMeasure());

                searchnum.setText(searchReport.getSearchNumber());
                rank.setText(searchReport.getOfficerRank());
                date.setText(searchReport.getSearchDate());
                time.setText(searchReport.getSearchTime());
                seizeditems.setText(searchReport.getSearchSeizedItems());
                notes.setText(searchReport.getSearchComments());

                BorderPane root = (BorderPane) searchReportObj.get("root");
                Button delBtn = (Button) searchReportObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = searchnum.getText();
                    try {
                        SearchReportUtils.deleteSearchReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete SearchReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow("Search Report");
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    searchLogUpdate();

                });

                MenuButton pullnotesbtn = (MenuButton) searchReportObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);
                searchnum.setEditable(false);

                ComboBox<String> statusValue = (ComboBox<String>) searchReportObj.get("statusValue");
                statusValue.setValue("Reopened");

                Button submitBtn = (Button) searchReportObj.get("submitBtn");
                submitBtn.setText("Update Information");

                searchTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void onArrestRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            ArrestReport arrestReport = (ArrestReport) arrestTable.getSelectionModel().getSelectedItem();

            if (arrestReport != null) {
                Map<String, Object> arrestReportObj = newArrest();

                Map<String, Object> arrestReportMap = (Map<String, Object>) arrestReportObj.get(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report") + " Map");

                TextField officername = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField officerrank = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField officerdiv = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField officeragen = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField officernumarrest = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));

                TextField offenderName = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
                TextField offenderAge = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
                TextField offenderGender = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
                TextField offenderAddress = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
                TextField offenderDescription = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));

                ComboBox area = (ComboBox) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
                ComboBox street = (ComboBox) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
                TextField county = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
                TextField arrestnum = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.ArrestNumberField", "arrest number"));
                TextField date = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField time = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));

                TextField ambulancereq = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldAmbulanceRequired", "ambulance required (Y/N)"));
                TextField taserdep = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTaserDeployed", "taser deployed (Y/N)"));
                TextField othermedinfo = (TextField) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOtherInformation", "other information"));

                TextArea notes = (TextArea) arrestReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));

                arrestnum.setText(arrestReport.getArrestNumber());
                officername.setText(arrestReport.getOfficerName());
                officerdiv.setText(arrestReport.getOfficerDivision());
                officeragen.setText(arrestReport.getOfficerAgency());
                officernumarrest.setText(arrestReport.getOfficerNumber());
                officerrank.setText(arrestReport.getOfficerRank());
                offenderName.setText(arrestReport.getArresteeName());
                offenderAge.setText(arrestReport.getArresteeAge());
                offenderGender.setText(arrestReport.getArresteeGender());
                offenderAddress.setText(arrestReport.getArresteeHomeAddress());
                offenderDescription.setText(arrestReport.getArresteeDescription());
                street.getEditor().setText(arrestReport.getArrestStreet());
                county.setText(arrestReport.getCounty());
                ambulancereq.setText(arrestReport.getAmbulanceYesNo());
                taserdep.setText(arrestReport.getTaserYesNo());
                othermedinfo.setText(arrestReport.getArresteeMedicalInformation());
                area.setValue(arrestReport.getArea());
                date.setText(arrestReport.getArrestDate());
                time.setText(arrestReport.getArrestTime());
                notes.setText(arrestReport.getArrestDetails());

                BorderPane root = (BorderPane) arrestReportObj.get("root");
                Button delBtn = (Button) arrestReportObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = arrestnum.getText();
                    try {
                        ArrestReportUtils.deleteArrestReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete ArrestReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow(localization.getLocalizedMessage("ReportWindows.ArrestReportTitle", "Arrest Report"));
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    arrestLogUpdate();

                });

                MenuButton pullnotesbtn = (MenuButton) arrestReportObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);
                arrestnum.setEditable(false);

                ComboBox<String> statusValue = (ComboBox<String>) arrestReportObj.get("statusValue");
                statusValue.setValue("Reopened");

                TableView chargetable = (TableView) arrestReportMap.get("ChargeTableView");
                ObservableList<ChargesData> chargeList = FXCollections.observableArrayList();
                chargetable.setItems(chargeList);

                addChargesToTable(arrestReport.getArrestCharges(), chargeList);

                Button submitBtn = (Button) arrestReportObj.get("submitBtn");
                submitBtn.setText("Update Information");

                arrestTable.getSelectionModel().clearSelection();
            }
        }
    }

    @FXML
    public void onAccidentReportRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {
            AccidentReport accidentReport = (AccidentReport) accidentReportTable.getSelectionModel().getSelectedItem();

            if (accidentReport != null) {
                Map<String, Object> accidentReportObj = newAccident();

                Map<String, Object> accidentReportMap = (Map<String, Object>) accidentReportObj.get(localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report") + " Map");

                TextField name = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerName", "name"));
                TextField rank = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerRank", "rank"));
                TextField div = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerDivision", "division"));
                TextField agen = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerAgency", "agency"));
                TextField num = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOfficerNumber", "number"));
                ComboBox street = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldStreet", "street"));
                ComboBox area = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldArea", "area"));
                TextField county = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldCounty", "county"));
                TextField date = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldDate", "date"));
                TextField time = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldTime", "time"));
                TextField accidentnum = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentNumberField", "accident number"));
                TextField weatherConditions = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentWeatherConditionsField", "weather conditions"));
                TextField roadConditions = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentRoadConditionsField", "road conditions"));
                TextField otherVehiclesInvolved = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentOtherVehiclesField", "other vehicles involved"));
                TextField witnesses = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldWitnesses", "witnesses"));
                TextField injuries = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentInjuriesField", "injuries"));
                TextField damages = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.AccidentDamagesField", "damages"));
                TextField offenderName = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderName", "offender name"));
                TextField offenderAge = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAge", "offender age"));
                TextField offenderGender = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderGender", "offender gender"));
                TextField offenderAddress = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderAddress", "offender address"));
                TextField offenderDescription = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldOffenderDescription", "offender description"));
                TextField model = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldModel", "model"));
                TextField plateNumber = (TextField) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldPlateNumber", "plate number"));
                ComboBox type = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldType", "type"));
                ComboBox color = (ComboBox) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldColor", "color"));
                TextArea notes = (TextArea) accidentReportMap.get(localization.getLocalizedMessage("ReportWindows.FieldNotes", "Notes"));

                accidentnum.setText(accidentReport.getAccidentNumber());
                name.setText(accidentReport.getOfficerName());
                rank.setText(accidentReport.getOfficerRank());
                div.setText(accidentReport.getOfficerDivision());
                agen.setText(accidentReport.getOfficerAgency());
                num.setText(accidentReport.getOfficerNumber());
                street.setValue(accidentReport.getStreet());
                area.setValue(accidentReport.getArea());
                county.setText(accidentReport.getCounty());
                date.setText(accidentReport.getAccidentDate());
                time.setText(accidentReport.getAccidentTime());
                weatherConditions.setText(accidentReport.getWeatherConditions());
                roadConditions.setText(accidentReport.getRoadConditions());
                otherVehiclesInvolved.setText(accidentReport.getOtherVehiclesInvolved());
                witnesses.setText(accidentReport.getWitnesses());
                injuries.setText(accidentReport.getInjuriesReported());
                damages.setText(accidentReport.getDamageDetails());
                offenderName.setText(accidentReport.getOwnerName());
                offenderAge.setText(accidentReport.getOwnerAge());
                offenderGender.setText(accidentReport.getOwnerGender());
                offenderAddress.setText(accidentReport.getOwnerAddress());
                offenderDescription.setText(accidentReport.getOwnerDescription());
                model.setText(accidentReport.getModel());
                plateNumber.setText(accidentReport.getPlateNumber());
                type.setValue(accidentReport.getType());
                color.setValue(accidentReport.getColor());
                notes.setText(accidentReport.getComments());

                BorderPane root = (BorderPane) accidentReportObj.get("root");
                Button delBtn = (Button) accidentReportObj.get("delBtn");
                delBtn.setVisible(true);
                delBtn.setDisable(false);
                delBtn.setOnAction(actionEvent -> {
                    String numToDelete = accidentnum.getText();
                    try {
                        AccidentReportUtils.deleteAccidentReport(numToDelete);
                        showNotificationInfo("Report Manager", "Deleted Report#: " + numToDelete);
                    } catch (JAXBException e) {
                        logError("Could not delete AccidentReport #" + numToDelete + ": ", e);
                    }
                    CustomWindow window = getWindow(localization.getLocalizedMessage("ReportWindows.AccidentReportTitle", "Accident Report"));
                    if (window != null) {
                        window.closeWindow();
                    }
                    try {
                        if (ConfigReader.configRead("soundSettings", "playDeleteReport").equalsIgnoreCase("true")) {
                            playSound(getJarPath() + "/sounds/alert-delete.wav");
                        }
                    } catch (IOException e) {
                        logError("Error getting configValue for playDeleteReport: ", e);
                    }
                    accidentReportUpdate();

                });

                MenuButton pullnotesbtn = (MenuButton) accidentReportObj.get("pullNotesBtn");
                pullnotesbtn.setVisible(false);
                accidentnum.setEditable(false);

                ComboBox<String> statusValue = (ComboBox<String>) accidentReportObj.get("statusValue");
                statusValue.setValue("Reopened");

                Button submitBtn = (Button) accidentReportObj.get("submitBtn");
                submitBtn.setText("Update Information");

                accidentReportTable.getSelectionModel().clearSelection();
            }
        }
    }

    private void addHoverListener(TableView table, Label pane) {
        pane.setOnMouseEntered(event -> {
            if (activePane == pane) {
                return;
            }
            pane.setStyle("-fx-background-color: rgb(0,0,0,0.05); -fx-background-radius: 7 0 0 7;");
        });

        pane.setOnMouseExited(event -> {
            if (activePane == pane) {
                return;
            }
            pane.setStyle("-fx-background-color: transparent;");
        });

        pane.setOnMouseClicked(event -> {

            if (activePane == pane) {
                return;
            }
            setActive(table, pane);

            for (Label otherPane : sideButtons) {
                otherPane.setStyle("-fx-background-color: transparent;");
            }

            activePane = pane;
            pane.setStyle("-fx-background-color: rgb(0,0,0,0.1); -fx-background-radius: 7 0 0 7;");
        });
    }

    private <T> void setActive(TableView<T> table, Label sideButton) {
        tableStackPane.getChildren().clear();
        tableStackPane.getChildren().add(table);

        ObservableList<T> items = table.getItems();

        long closed = 0, inProg = 0, pending = 0, cancelled = 0, reOpen = 0;

        for (T item : items) {
            try {
                Method getStatusMethod = item.getClass().getMethod("getStatus");
                String status = (String) getStatusMethod.invoke(item);

                if ("Closed".equalsIgnoreCase(status)) {
                    closed++;
                } else if ("In Progress".equalsIgnoreCase(status)) {
                    inProg++;
                } else if ("Pending".equalsIgnoreCase(status)) {
                    pending++;
                } else if ("Cancelled".equalsIgnoreCase(status)) {
                    cancelled++;
                } else if ("Reopened".equalsIgnoreCase(status)) {
                    reOpen++;
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logError("Could not obtain getStatus method for update: ", e);
            }
        }

        long totalReports = (closed + inProg + reOpen + pending + cancelled);
        long totalInProgress = (reOpen + inProg + pending);
        long totalClosed = (closed + cancelled);

        double closedPercentage = ((double) totalClosed / totalReports) * 100;
        double inProgressPercentage = ((double) totalInProgress / totalReports) * 100;

        closedReportsProgressBar.setProgress(closedPercentage / 100);
        reportsInProgressBar.setProgress(inProgressPercentage / 100);

        reportsInProgressLabel.setText(String.valueOf(totalInProgress));
        closedReportsLabel.setText(String.valueOf(totalClosed));
        totalReportsLabel.setText(String.valueOf(totalReports));

        activePane = sideButton;
    }

    public TableView getArrestTable() {
        return arrestTable;
    }

    public TableView getCalloutTable() {
        return calloutTable;
    }

    public TableView getAccidentReportTable() {
        return accidentReportTable;
    }

    public TableView getCitationTable() {
        return citationTable;
    }

    public TableView getDeathReportTable() {
        return deathReportTable;
    }

    public TableView getImpoundTable() {
        return impoundTable;
    }

    public TableView getIncidentTable() {
        return incidentTable;
    }

    public TableView getPatrolTable() {
        return patrolTable;
    }

    public TableView getSearchTable() {
        return searchTable;
    }

    public TableView getTrafficStopTable() {
        return trafficStopTable;
    }

    public BorderPane getRoot() {
        return root;
    }
}
