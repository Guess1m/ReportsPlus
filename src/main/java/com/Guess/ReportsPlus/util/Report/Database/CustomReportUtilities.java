package com.Guess.ReportsPlus.util.Report.Database;

import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logInfo;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logWarn;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getCustomDataLogsFolderPath;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController;
import com.Guess.ReportsPlus.util.Other.controllerUtils;
import com.Guess.ReportsPlus.util.Strings.reportLayoutTemplates;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomReportUtilities {
    public static void addDefaultReports() {
        List<String> defaultReports = new ArrayList<String>() {
            {
                add("Callout Report");
                add("Traffic Stop Report");
                add("Impound Report");
                add("Search Report");
                add("Accident Report");
                add("Death Report");
                add("Patrol Report");
                add("Incident Report");
            };
        };
        for (String reportTitle : defaultReports) {
            logInfo("CustomReportUtility; Adding default report: " + reportTitle);

            boolean skipReport = false;

            String dataFolderPath = getCustomDataLogsFolderPath();
            controllerUtils.createFolderIfNotExists(dataFolderPath);
            File[] files = new File(dataFolderPath).listFiles((dir, name) -> name.endsWith(".db"));
            if (files != null || files.length != 0) {
                for (File file : files) {
                    if (file.getName().equalsIgnoreCase(reportTitle + ".db")) {
                        skipReport = true;
                        continue;
                    }
                }
            }

            if (skipReport) {
                logWarn("CustomReportUtility; Skipping report creation for: " + reportTitle);
                continue;
            }

            String data = null;
            try {
                data = getLayoutJsonFor(reportTitle);
            } catch (JsonProcessingException e) {
                logError("CustomReportUtility: Error getting layoutjson for [" + reportTitle + "]: ", e);
            }

            Map<String, String> layoutScheme = new HashMap<>();
            layoutScheme.put("key", "TEXT");
            layoutScheme.put("layoutData", "TEXT");
            layoutScheme.put("transferData", "TEXT");
            Map<String, Object> layoutMap = new HashMap<>();
            layoutMap.put("key", "1");
            layoutMap.put("layoutData", data);
            Map<String, Object> transferMap = new HashMap<>();
            transferMap.put("key", "2");
            transferMap.put("transferData", "null");

            Map<String, String> reportSchema = new HashMap<>();
            DynamicDB dbManager = null;
            try {
                reportSchema = LayoutBuilderController.extractFieldNames(data);
                reportSchema.put("report_status", "TEXT");
                dbManager = new DynamicDB(getCustomDataLogsFolderPath() + reportTitle, "data",
                        LayoutBuilderController.extractNumberField(data),
                        reportSchema);
                dbManager.initDB();
            } catch (Exception e) {
                logError("CustomReportUtility; Failed to extract field names", e);
            } finally {
                try {
                    dbManager.close();
                } catch (SQLException e) {
                    logError("CustomReportUtility; Failed to close database connection, null", e);
                }
            }

            DynamicDB DatabaseLayout = new DynamicDB(getCustomDataLogsFolderPath() + reportTitle, "layout", "key",
                    layoutScheme);
            if (DatabaseLayout.initDB()) {
                logInfo("CustomReportUtility; Layout Database for: [" + reportTitle + "] Initialized");
                try {
                    DatabaseLayout.addOrReplaceRecord(layoutMap);
                    DatabaseLayout.addOrReplaceRecord(transferMap);
                } catch (SQLException e) {
                    logError("CustomReportUtility; Error adding/replacing record: " + layoutScheme, e);
                } finally {
                    try {
                        DatabaseLayout.close();
                    } catch (SQLException e) {
                        logError("CustomReportUtility; Error closing Database: [" + DatabaseLayout.getTableName() + "]",
                                e);
                    }
                }
            } else {
                logError("CustomReportUtility; Layout Database not initialized!");
                showNotificationError("Report Creation Utility", "Error initializing layout database!");
            }

            DynamicDB Database = new DynamicDB(getCustomDataLogsFolderPath() + reportTitle, "data",
                    LayoutBuilderController.extractNumberField(data), reportSchema);
            try {
                if (Database.initDB()) {
                    logInfo("CustomReportUtility; Database initialized");
                } else {
                    logError("CustomReportUtility; Database not initialized!");
                    showNotificationError("Report Creation Utility", "Error initializing database!");
                }
            } finally {
                try {
                    Database.close();
                } catch (SQLException e) {
                    logError("CustomReportUtility; Error closing Database: [" + Database.getTableName() + "]", e);
                }
            }

            logInfo("New Report Type [" + reportTitle + "] Created!");
        }
    }

    private static String getLayoutJsonFor(String newValue) throws JsonProcessingException {
        switch (newValue.toLowerCase()) {
            case "callout report":
                return getLayoutFromJson(reportLayoutTemplates.calloutReport);
            case "traffic stop report":
                return getLayoutFromJson(reportLayoutTemplates.trafficStopReport);
            case "impound report":
                return getLayoutFromJson(reportLayoutTemplates.impoundReport);
            case "search report":
                return getLayoutFromJson(reportLayoutTemplates.searchReport);
            case "accident report":
                return getLayoutFromJson(reportLayoutTemplates.accidentReport);
            case "death report":
                return getLayoutFromJson(reportLayoutTemplates.deathReport);
            case "patrol report":
                return getLayoutFromJson(reportLayoutTemplates.patrolReport);
            case "incident report":
                return getLayoutFromJson(reportLayoutTemplates.incidentReport);
            default:
                return "ERROR";
        }
    }

    public static String getLayoutFromJson(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonString);

        return rootNode.get("layout").toString();
    }
}
