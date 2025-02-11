package com.Guess.ReportsPlus.Windows.Apps;

import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.TrafficCitation.TrafficCitationUtils;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Report.Database.DynamicDB;
import jakarta.xml.bind.JAXBException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.parseAndPopulateMap;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UIDarkColor;
import static com.Guess.ReportsPlus.Windows.Settings.settingsController.UILightColor;
import static com.Guess.ReportsPlus.logs.Accident.AccidentReportUtils.loadAccidentReports;
import static com.Guess.ReportsPlus.logs.Arrest.ArrestReportUtils.loadArrestReports;
import static com.Guess.ReportsPlus.logs.Callout.CalloutReportUtils.loadCalloutReports;
import static com.Guess.ReportsPlus.logs.Death.DeathReportUtils.loadDeathReports;
import static com.Guess.ReportsPlus.logs.Impound.ImpoundReportUtils.loadImpoundReports;
import static com.Guess.ReportsPlus.logs.Incident.IncidentReportUtils.loadIncidentReports;
import static com.Guess.ReportsPlus.logs.Patrol.PatrolReportUtils.loadPatrolReports;
import static com.Guess.ReportsPlus.logs.Search.SearchReportUtils.loadSearchReports;
import static com.Guess.ReportsPlus.logs.TrafficStop.TrafficStopReportUtils.loadTrafficStopReports;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.getPrimaryKeyColumn;

public class ReportStatisticsController {
    public static ReportStatisticsController reportStatisticsController;
    public static NumberAxis yAxis = null;
    public static CategoryAxis xAxis = null;
    public static AreaChart<String, Number> chart = null;
    @FXML
    private BorderPane borderPane;
    @FXML
    private BorderPane root;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private ComboBox reportsByCombobox;
    @FXML
    private Label reportsByLabel;

    public static NumberAxis getyAxis() {
        return yAxis;
    }

    public static AreaChart<String, Number> getChart() {
        return chart;
    }

    public static CategoryAxis getxAxis() {
        return xAxis;
    }

    private HashMap<String, Integer> parseDatabase(String path, String typeToGet) {
        HashMap<String, Integer> counts = new HashMap<>();

        path = path.replace(".db", "");
        createFolderIfNotExists(getCustomDataLogsFolderPath());

        if (path.isEmpty()) {
            log("[ReportStats] Database Path is Empty", LogUtils.Severity.ERROR);
        }

        String primaryKey = null;
        try {
            primaryKey = getPrimaryKeyColumn(path, "data");
        } catch (SQLException e) {
            logError("[ReportStats] Error getting primary key column", e);
        }

        Map<String, String> layoutScheme = null;
        Map<String, String> reportSchema = null;
        try {
            layoutScheme = DynamicDB.getTableColumnsDefinition(path, "layout");
            reportSchema = DynamicDB.getTableColumnsDefinition(path, "data");
        } catch (SQLException e2) {
            logError("[ReportStats] Failed to extract field names", e2);
        }

        String layoutContent = null;
        DynamicDB DatabaseLayout = new DynamicDB(path, "layout", "key", layoutScheme);
        if (DatabaseLayout.initDB()) {
            log("[ReportStats] Layout Database for: [" + path + "] Initialized", LogUtils.Severity.INFO);
            try {
                Map<String, Object> layoutRecord = DatabaseLayout.getRecord("1");
                if (layoutRecord != null) {
                    Object layoutValue = layoutRecord.get("layoutData");
                    layoutContent = (String) layoutValue;
                } else {
                    log("[ReportStats] layout record null for report: " + path, LogUtils.Severity.ERROR);
                }
            } catch (SQLException e2) {
                logError("[ReportStats] Error adding/replacing record: " + layoutScheme, e2);
            } finally {
                try {
                    DatabaseLayout.close();
                } catch (SQLException e2) {
                    logError("[ReportStats] Error closing Database: [" + DatabaseLayout.getTableName() + "]", e2);
                }
            }
        } else {
            log("[ReportStats] Layout Database not initialized!", LogUtils.Severity.ERROR);
        }

        DynamicDB dbManager = null;
        try {
            dbManager = new DynamicDB(path, "data", primaryKey, reportSchema);
            dbManager.initDB();

            Map<String, Map<String, List<String>>> mainMap = parseAndPopulateMap(layoutContent);
            Map<String, List<String>> fieldMap = mainMap.getOrDefault("selectedType", new HashMap<>());
            for (Map.Entry<String, List<String>> entry : fieldMap.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                if (value.toString().toLowerCase().contains(typeToGet.toLowerCase())) {
                    List<Map<String, Object>> allDataRecords = dbManager.getAllRecords();
                    for (Map<String, Object> record : allDataRecords) {
                        Object dataValue = record.get(key);
                        if (dataValue != null && !dataValue.toString().equalsIgnoreCase("n/a") && !dataValue.toString().equalsIgnoreCase("")) {
                            counts.put(dataValue.toString(), counts.getOrDefault(dataValue.toString(), 0) + 1);
                        }
                    }
                }
            }

        } catch (Exception e2) {
            logError("Failed to extract field names", e2);
        } finally {
            try {
                dbManager.close();
            } catch (SQLException e2) {
                logError("Failed to close database connection, null", e2);
            }
        }

        return counts;

    }

    public HashMap<String, Integer> combineCounts(HashMap<String, Integer> map1, HashMap<String, Integer> map2) {
        HashMap<String, Integer> combined = new HashMap<>();
        if (map1 != null) {
            for (Map.Entry<String, Integer> entry : map1.entrySet()) {
                combined.put(entry.getKey(), entry.getValue());
            }
        }
        if (map2 != null) {
            for (Map.Entry<String, Integer> entry : map2.entrySet()) {
                combined.put(entry.getKey(), combined.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }
        return combined;
    }

    public HashMap<String, Integer> countOccurrencesAcrossFiles(String typeToGet) {
        String dataFolderPath = getCustomDataLogsFolderPath();
        File folder = new File(dataFolderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            log("NewReport; Invalid data folder path: " + dataFolderPath, LogUtils.Severity.ERROR);
            return null;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".db"));
        if (files == null || files.length == 0) {
            log("NewReport; No database files found in: " + dataFolderPath, LogUtils.Severity.INFO);
            return null;
        }

        log("NewReport; Found " + files.length + " database files in: " + dataFolderPath, LogUtils.Severity.INFO);

        HashMap<String, Integer> totalCounts = new HashMap<>();
        for (File file : files) {
            String filePath = file.getAbsolutePath();
            HashMap<String, Integer> fileCounts = parseDatabase(filePath, typeToGet);
            for (Map.Entry<String, Integer> entry : fileCounts.entrySet()) {
                totalCounts.put(entry.getKey(), totalCounts.getOrDefault(entry.getKey(), 0) + entry.getValue());
            }
        }

        return totalCounts;
    }

    private <T> HashMap<String, Integer> parseReport(List<T> reports, String getMethod) {
        HashMap<String, Integer> counts = new HashMap<>();
        if (reports != null) {
            for (T report : reports) {
                try {
                    String value = (String) report.getClass().getMethod(getMethod).invoke(report);
                    if (value != null && !value.equalsIgnoreCase("n/a") && !value.equalsIgnoreCase("")) {
                        value = value.replaceAll("(?i)county", "").trim();
                        counts.put(value, counts.getOrDefault(value, 0) + 1);
                    }
                } catch (Exception e) {
                    log("[ReportStats] Report: [" + report.getClass().getSimpleName() + "] missing method: " + getMethod, LogUtils.Severity.WARN);
                }
            }
        }
        return counts;
    }

    private HashMap<String, Integer> parseLogReports(String getMethod) {
        HashMap<String, Integer> counts = new HashMap<>();
        try {
            log("[ReportStats] Starting to parse all reports for method: [" + getMethod + "]", LogUtils.Severity.INFO);
            mergeCounts(counts, parseReport(loadAccidentReports().getAccidentReportList(), getMethod));
            mergeCounts(counts, parseReport(loadArrestReports().getArrestReportList(), getMethod));
            mergeCounts(counts, parseReport(loadCalloutReports().getCalloutReportList(), getMethod));
            mergeCounts(counts, parseReport(loadDeathReports().getDeathReportList(), getMethod));
            mergeCounts(counts, parseReport(loadImpoundReports().getImpoundReportList(), getMethod));
            mergeCounts(counts, parseReport(loadIncidentReports().getIncidentReportList(), getMethod));
            mergeCounts(counts, parseReport(loadPatrolReports().getPatrolReportList(), getMethod));
            mergeCounts(counts, parseReport(loadSearchReports().getSearchReportList(), getMethod));
            mergeCounts(counts, parseReport(TrafficCitationUtils.loadTrafficCitationReports().getTrafficCitationReportList(), getMethod));
            mergeCounts(counts, parseReport(loadTrafficStopReports().getTrafficStopReportList(), getMethod));

        } catch (JAXBException e) {
            logError("Error parsing reports: ", e);
        }
        log("[ReportStats] Finished parsing all reports for [" + getMethod + "]; " + counts, LogUtils.Severity.INFO);
        return counts;
    }

    private void mergeCounts(HashMap<String, Integer> mainCounts, HashMap<String, Integer> additionalCounts) {
        for (Map.Entry<String, Integer> entry : additionalCounts.entrySet()) {
            mainCounts.put(entry.getKey(), mainCounts.getOrDefault(entry.getKey(), 0) + entry.getValue());
        }
    }

    private AreaChart<String, Number> createChart(String getMethod) {
        ObservableList<String> xAxisElements = FXCollections.observableArrayList();
        String getType = "";
        switch (getMethod) {
            case "getOfficerName":
                getType = "OFFICER_NAME";
                break;
            case "getCounty":
                getType = "COUNTY_FIELD";
                break;
            case "getArea":
                getType = "COMBO_BOX_AREA";
                break;
        }

        HashMap<String, Integer> fileCounts = countOccurrencesAcrossFiles(getType);
        HashMap<String, Integer> logReports = parseLogReports(getMethod);
        HashMap<String, Integer> totalCounts = combineCounts(fileCounts, logReports);
        xAxisElements.addAll(totalCounts.keySet());

        xAxis = new CategoryAxis();
        xAxis.setCategories(xAxisElements);
        xAxis.setTickLabelRotation(20);

        yAxis = new NumberAxis();
        yAxis.setTickLabelFill(Paint.valueOf("#ffffff"));

        chart = new AreaChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        for (Map.Entry<String, Integer> entry : totalCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chart.getData().add(series);

        return chart;
    }

    public void initialize() {
        reportsByCombobox.getItems().clear();
        reportsByCombobox.getItems().addAll(new ArrayList<>(Arrays.asList("Officer", "Area", "County")));

        reportsByCombobox.getSelectionModel().select(1);
        updateChartPane("getArea");

        reportsByCombobox.setOnAction(actionEvent -> {
            String choice = reportsByCombobox.getSelectionModel().getSelectedItem().toString().trim();
            switch (choice) {
                case "Officer":
                    updateChartPane("getOfficerName");
                    log("[ReportStats] Using getOfficerName for chartdata", LogUtils.Severity.INFO);
                    break;
                case "County":
                    updateChartPane("getCounty");
                    log("[ReportStats] Using getCounty for chartdata", LogUtils.Severity.INFO);
                    break;
                case "Area":
                default:
                    updateChartPane("getArea");
                    log("[ReportStats] Using getArea for chartdata", LogUtils.Severity.INFO);
                    break;
            }
        });

        reportsByLabel.setText(localization.getLocalizedMessage("ReportStatistics.reportsByLabel", "Reports By:"));
    }

    private void updateChartPane(String getMethod) {
        log("Running ChartUpdate For: [" + getMethod + "]", LogUtils.Severity.DEBUG);
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node node : borderPane.getChildren()) {
            if (node instanceof AreaChart) {
                log("[ReportStats] Added AreaChart Node to delete list", LogUtils.Severity.DEBUG);
                nodes.add(node);
            }
        }
        if (nodes.size() > 0) {
            borderPane.getChildren().remove(nodes.get(0));
            log("[ReportStats] Removed old AreaChart Node", LogUtils.Severity.DEBUG);
        }

        AreaChart<String, Number> chart = createChart(getMethod);
        try {
            updateChart(chart);
        } catch (IOException e) {
            logError("[ReportStats] Error updating chart config: ", e);
        }

        chart.setLegendVisible(false);
        borderPane.setCenter(chart);
    }

    private void updateChart(AreaChart chart) throws IOException {
        String mainclr = ConfigReader.configRead("uiColors", "mainColor");
        String secclr = ConfigReader.configRead("uiColors", "secondaryColor");
        String accclr = ConfigReader.configRead("uiColors", "accentColor");

        Set<Node> symbolNodes = chart.lookupAll(".default-color0.chart-area-symbol");
        for (Node symbolNode : symbolNodes) {
            symbolNode.setStyle("-fx-background-color: " + secclr + ";");
        }
        Node fillNode = chart.lookup(".default-color0.chart-series-area-fill");
        fillNode.setStyle("-fx-fill: " + hexToRgba(mainclr, 0.5) + ";");
        Node lineNode = chart.lookup(".default-color0.chart-series-area-line");
        lineNode.setStyle("-fx-stroke: " + accclr + ";");

        if (ConfigReader.configRead("uiColors", "UIDarkMode").equalsIgnoreCase("true")) {
            xAxis.setTickLabelFill(rgbToHexString(UIDarkColor));
            yAxis.setTickLabelFill(rgbToHexString(UIDarkColor));
            Node bkgNode = chart.lookup("AreaChart .chart-content .chart-plot-background");
            bkgNode.setStyle("-fx-background-color: rgba(0,0,0,0.05), rgba(0,0,0,0.05);");
        } else {
            xAxis.setTickLabelFill(rgbToHexString(UILightColor));
            yAxis.setTickLabelFill(rgbToHexString(UILightColor));
            Node bkgNode = chart.lookup("AreaChart .chart-content .chart-plot-background");
            bkgNode.setStyle("-fx-background-color: rgba(255,255,255, 0.1), rgba(255,255,255, 0.1);");
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public Label getReportsByLabel() {
        return reportsByLabel;
    }

}