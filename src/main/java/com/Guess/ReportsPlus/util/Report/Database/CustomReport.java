package com.Guess.ReportsPlus.util.Report.Database;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Windows.Apps.LogViewController;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Misc.NotificationManager;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils;
import javafx.animation.PauseTransition;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Windows.Other.LayoutBuilderController.*;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.AudioUtil.playSound;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.showNotificationWarning;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.getPrimaryKeyColumn;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.isValidDatabase;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.getValuesForField;

public class CustomReport {
    Map<String, Map<String, List<String>>> mainMap;
    Map<String, Object> reportMap;

    public CustomReport(String reportTitle, String dataTablePrimaryKey, Map<String, String> layoutScheme, Map<String, String> reportSchema) {
        String dataFolderPath = getCustomDataLogsFolderPath();
        createFolderIfNotExists(dataFolderPath);

        if (reportTitle.isEmpty()) {
            log("LayoutBuilder; Report Title Field is Empty", LogUtils.Severity.ERROR);
            return;
        }

        DynamicDB dbManager = null;
        try {
            dbManager = new DynamicDB(dataFolderPath + reportTitle, "data", dataTablePrimaryKey, reportSchema);
            dbManager.initDB();
        } catch (Exception e2) {
            logError("Failed to extract field names", e2);
        } finally {
            try {
                dbManager.close();
            } catch (SQLException e2) {
                logError("Failed to close database connection, null", e2);
            }
        }

        String layoutContent = null;
        String transferContent = null;
        DynamicDB DatabaseLayout = new DynamicDB(dataFolderPath + reportTitle, "layout", "key", layoutScheme);
        if (DatabaseLayout.initDB()) {
            log("LayoutBuilder: Layout Database for: [" + reportTitle + "] Initialized", LogUtils.Severity.INFO);
            try {
                Map<String, Object> layoutRecord = DatabaseLayout.getRecord("1");
                if (layoutRecord != null) {
                    Object layoutValue = layoutRecord.get("layoutData");
                    layoutContent = (String) layoutValue;
                } else {
                    log("layout record null for report: " + reportTitle, LogUtils.Severity.ERROR);
                }
                Map<String, Object> transferRecord = DatabaseLayout.getRecord("2");
                if (transferRecord != null) {
                    Object layoutValue = transferRecord.get("transferData");
                    transferContent = (String) layoutValue;
                } else {
                    log("transfer record null for report: " + reportTitle, LogUtils.Severity.ERROR);
                }
            } catch (SQLException e2) {
                logError("Error adding/replacing record: " + layoutScheme, e2);
            } finally {
                try {
                    DatabaseLayout.close();
                } catch (SQLException e2) {
                    logError("Error closing Database: [" + DatabaseLayout.getTableName() + "]", e2);
                }
            }
        } else {
            log("LayoutBuilder: Layout Database not initialized!", LogUtils.Severity.ERROR);
            showNotificationError("Report Creation Utility", "Error initializing layout database!");
        }

        Map<String, Map<String, List<String>>> newMap = parseAndPopulateMap(layoutContent);
        var layout = loadLayoutFromJSON(layoutContent);
        nestedReportUtils.TransferConfig transferConfig = loadTransferConfigFromJSON(transferContent);

        Map<String, Object> reportWindow = createReportWindow(reportTitle, transferConfig,
                layout.toArray(new nestedReportUtils.SectionConfig[0]));
        Map<String, Object> reportMap = (Map<String, Object>) reportWindow.get(
                reportTitle + " Map");

        this.mainMap = newMap;
        this.reportMap = reportMap;

        if (newMap.getOrDefault("selectedType", new HashMap<>()).values().stream()
                .flatMap(List::stream)
                .filter(type -> type.equalsIgnoreCase("NUMBER_FIELD"))
                .count() != 1) {
            log("LayoutBuilder: Exactly one NUMBER_FIELD is required, found: " + newMap.getOrDefault("selectedType", new HashMap<>()).values().stream()
                            .flatMap(List::stream)
                            .filter(type -> type.equalsIgnoreCase("NUMBER_FIELD"))
                            .count(),
                    LogUtils.Severity.ERROR);
            newMap.put("selectedType", null);
            return;
        }

        for (String name : reportMap.keySet()) {
            if (name.startsWith("transfer_")) {
                String[] values = name.split("_");
                Button btn = (Button) reportMap.get(name);
                btn.setText(values[1]);
                btn.setOnAction(btnAction -> {
                    File dataFolder = new File(dataFolderPath);
                    if (dataFolder.exists() && dataFolder.isDirectory()) {
                        File[] files = dataFolder.listFiles((dir, filen) -> filen.endsWith(".db"));
                        if (files != null && files.length != 0) {
                            log("CustomReport; Searching for [" + values[2] + "] Found " + files.length + " Database(s)", LogUtils.Severity.INFO);
                            boolean found = false;
                            for (File dbFile : files) {
                                String fileNameWithoutExt = dbFile.getName().replaceFirst("[.][^.]+$", "");
                                if (fileNameWithoutExt.equals(values[2])) {
                                    String dbFilePath = dbFile.getAbsolutePath();
                                    log("CustomReport; [" + dbFile.getName() + "] Being Checked", LogUtils.Severity.INFO);
                                    if (isValidDatabase(dbFilePath, dbFile.getName())) {
                                        log("CustomReport; [" + dbFile.getName() + "] Valid", LogUtils.Severity.INFO);

                                        String data = null;
                                        try {
                                            data = getPrimaryKeyColumn(dbFilePath, "data");
                                        } catch (SQLException e) {
                                            logError("Error getting primary key column [2]", e);
                                        }

                                        log("NewReport; Clicked Database: " + dbFile.getName(), LogUtils.Severity.DEBUG);
                                        createFolderIfNotExists(getCustomDataLogsFolderPath());

                                        Map<String, String> transferLayoutScheme = null;
                                        Map<String, String> transferSchema = null;
                                        try {
                                            transferLayoutScheme = DynamicDB.getTableColumnsDefinition(dbFilePath, "layout");
                                            transferSchema = DynamicDB.getTableColumnsDefinition(dbFilePath, "data");
                                        } catch (SQLException e2) {
                                            logError("Failed to extract field names", e2);
                                        }

                                        CustomReport customReport = new CustomReport(values[2], data, transferLayoutScheme, transferSchema);

                                        Map<String, Map<String, List<String>>> customReportMap = customReport.getMainMap();
                                        Map<String, Object> customReportUIMap = customReport.getReportMap();

                                        Map<String, List<String>> currentReportKeyMap = newMap.get("keyMap");
                                        Map<String, List<String>> customReportKeyMap = customReportMap.get("keyMap");

                                        for (Map.Entry<String, List<String>> entry : currentReportKeyMap.entrySet()) {
                                            String key = entry.getKey();
                                            if (customReportKeyMap.containsKey(key)) {
                                                List<String> currentFields = entry.getValue();
                                                List<String> customFields = customReportKeyMap.get(key);

                                                int minSize = Math.min(currentFields.size(), customFields.size());
                                                for (int i = 0; i < minSize; i++) {
                                                    String currentField = currentFields.get(i);
                                                    String customField = customFields.get(i);

                                                    Object currentUIComponent = reportMap.get(currentField);
                                                    String textToTransfer = extractTextFromUIComponent(currentUIComponent, newMap.get("selectedType").get(currentField));

                                                    Object customUIComponent = customReportUIMap.get(customField);
                                                    setTextInUIComponent(customUIComponent, textToTransfer, customReportMap.get("selectedType").get(customField));
                                                }
                                            }
                                        }

                                        found = true;
                                    } else {
                                        log("CustomReport; Invalid database file: " + dbFilePath, LogUtils.Severity.WARN);
                                    }
                                }
                            }
                            if (!found) {
                                showNotificationWarning("Report Creation Utility", "Database [" + values[2] + "] not found!");
                                log("CustomReport; [" + values[2] + "] Not found", LogUtils.Severity.WARN);
                            }
                        }
                    }
                });
            }
        }

        Map<String, List<String>> dropdownTypeMap = newMap.getOrDefault("dropdownType", null);
        if (dropdownTypeMap != null) {
            for (Map.Entry<String, List<String>> entry : dropdownTypeMap.entrySet()) {
                String key = entry.getKey();
                List<String> values = entry.getValue();

                for (int i = 0; i < values.size(); i++) {
                    String value = values.get(i);
                    if (value != null) {
                        Object component = reportMap.get(key);
                        if (component instanceof ComboBox box) {
                            box.getItems().addAll(getValuesForField(value));
                        }
                    }
                }
            }
        }

        Label warningLabel = (Label) reportWindow.get("warningLabel");
        MenuButton pullNotesBtn = (MenuButton) reportWindow.get("pullNotesBtn");
        Button submitBtn = (Button) reportWindow.get("submitBtn");
        ComboBox<String> statusValue = (ComboBox) reportWindow.get("statusValue");

        pullNotesBtn.setOnMouseEntered(event2 -> {
            pullNotesBtn.getItems().clear();

            if (notesViewController != null) {
                for (Tab tab : notesViewController.getTabPane().getTabs()) {
                    MenuItem menuItem = new MenuItem(tab.getText());
                    pullNotesBtn.getItems().add(menuItem);

                    AnchorPane anchorPane = (AnchorPane) tab.getContent();
                    TextArea noteArea = (TextArea) anchorPane.lookup("#notepadTextArea");

                    if (noteArea != null) {
                        menuItem.setOnAction(event3 -> {
                            if (newMap == null) {
                                log("LayoutBuilder; newMap is null", LogUtils.Severity.ERROR);
                                return;
                            }
                            for (String field : newMap.getOrDefault("nodeType", new HashMap<>()).keySet()) {
                                log("LayoutBuilder; Processing field: " + field, LogUtils.Severity.DEBUG);

                                Object fieldValue = reportMap.get(field);

                                if (fieldValue == null) {
                                    log("LayoutBuilder; Field is null: " + field, LogUtils.Severity.ERROR);
                                    continue;
                                }

                                String key = newMap.getOrDefault("keyMap", new HashMap<>())
                                        .entrySet().stream()
                                        .filter(entry -> entry.getValue().contains(field))
                                        .map(Map.Entry::getKey)
                                        .findFirst()
                                        .orElse(null);

                                if (key == null) {
                                    continue;
                                }

                                if (fieldValue instanceof TextField) {
                                    updateTextFromNotepad((TextField) fieldValue, noteArea, "-" + key);
                                } else if (fieldValue instanceof ComboBox) {
                                    updateTextFromNotepad(((ComboBox<?>) fieldValue).getEditor(), noteArea,
                                            "-" + key);
                                } else if (fieldValue instanceof TextArea) {
                                    updateTextFromNotepad((TextArea) fieldValue, noteArea, "-" + key);
                                } else {
                                    log("LayoutBuilder; Unknown field type: " + fieldValue.getClass().getSimpleName(),
                                            LogUtils.Severity.ERROR);
                                }
                            }
                        });
                    }
                }
            }
        });

        submitBtn.setOnAction(submitEvent -> {
            Map<String, List<String>> selectedTypes = newMap.getOrDefault("selectedType", new HashMap<>());
            Map<String, List<String>> fieldNames = newMap.getOrDefault("fieldNames", new HashMap<>());

            Map<String, Object> reportRecord = new HashMap<>();

            String identifier = null;

            for (String field : fieldNames.keySet()) {
                String selectedType = selectedTypes.getOrDefault(field, List.of()).isEmpty() ? "" : selectedTypes.get(field).get(0);
                if ("NUMBER_FIELD".equalsIgnoreCase(selectedType)) {
                    if (reportMap.get(field) instanceof TextField numberField) {
                        if (numberField.getText().trim().isEmpty()) {
                            warningLabel.setVisible(true);
                            warningLabel.setText("Number can't be empty!");
                            warningLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-text-fill: red;");
                            PauseTransition pause = new PauseTransition(Duration.seconds(2));
                            pause.setOnFinished(e2 -> warningLabel.setVisible(false));
                            pause.play();
                            log("LayoutBuilder: Number field blank, returning", LogUtils.Severity.WARN);
                            return;
                        } else {
                            identifier = field;
                            log("Set identifier for report [" + reportTitle + "] to: " + identifier, LogUtils.Severity.DEBUG);
                        }
                    }
                }

                Object f = reportMap.get(field);
                if (f == null) {
                    log("Field " + field + " not found in map: " + reportMap, LogUtils.Severity.ERROR);
                }

                if (f instanceof ComboBox<?>) {
                    if (selectedType.equalsIgnoreCase("combo_box_street") || selectedType.equalsIgnoreCase("combo_box_area")) {
                        reportRecord.put(field, toTitleCase(((ComboBox) f).getEditor().getText().trim()));
                    } else {
                        reportRecord.put(field, ((ComboBox) f).getValue().toString().trim());
                    }
                } else if (f instanceof TextField) {
                    reportRecord.put(field, toTitleCase(((TextInputControl) f).getText().trim()));
                } else if (f instanceof TextArea) {
                    reportRecord.put(field, ((TextInputControl) f).getText().trim());
                } else {
                    log("Unknown field type: " + f.getClass().getSimpleName(), LogUtils.Severity.ERROR);
                }
            }

            reportRecord.put("status", statusValue.getValue().trim());
            for (String fieldName : reportMap.keySet()) {
                Object field = reportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        comboBox.getSelectionModel().selectFirst();
                    }
                }
            }

            if (identifier == null) {
                log("LayoutBuilder: Identifier is null", LogUtils.Severity.ERROR);
                return;
            }

            DynamicDB Database = new DynamicDB(dataFolderPath + reportTitle, "data", dataTablePrimaryKey, reportSchema);
            if (Database.initDB()) {
                log("LayoutBuilder: Database initialized", LogUtils.Severity.INFO);
                try {
                    Database.addOrReplaceRecord(reportRecord);
                } catch (SQLException e2) {
                    logError("Error adding/replacing record: " + reportRecord, e2);
                } finally {
                    try {
                        Database.close();
                    } catch (SQLException e2) {
                        logError("Error closing Database: [" + Database.getTableName() + "]", e2);
                    }
                }
            } else {
                log("LayoutBuilder: Database not initialized!", LogUtils.Severity.ERROR);
                showNotificationError("Report Creation Utility", "Error initializing database!");
            }

            try {
                if (ConfigReader.configRead("soundSettings", "playCreateReport").equalsIgnoreCase("true")) {
                    playSound(getJarPath() + "/sounds/alert-success.wav");
                }
            } catch (IOException e) {
                logError("Error getting configValue for playCreateReport: ", e);
            }

            LogViewController.needRefresh.set(1);

            NotificationManager.showNotificationInfo("Report Manager", "A new " + reportTitle + " has been submitted.");
            CustomWindow window = getWindow(reportTitle);
            if (window != null) {
                window.closeWindow();
            }
        });
    }

    private String extractTextFromUIComponent(Object uiComponent, List<String> selectedType) {
        if (uiComponent instanceof TextInputControl) {
            return ((TextInputControl) uiComponent).getText();
        } else if (uiComponent instanceof ComboBox) {
            ComboBox<?> comboBox = (ComboBox<?>) uiComponent;
            if (selectedType.contains("COMBO_BOX_STREET") || selectedType.contains("COMBO_BOX_AREA")) {
                if (comboBox.getEditor() != null) {
                    return comboBox.getEditor().getText();
                }
            } else {
                if (comboBox.getValue() != null) {
                    return comboBox.getValue().toString();
                }
            }
        }
        return "";
    }

    private void setTextInUIComponent(Object uiComponent, String text, List<String> selectedType) {
        if (uiComponent instanceof TextInputControl) {
            ((TextInputControl) uiComponent).setText(text);
        } else if (uiComponent instanceof ComboBox) {
            ComboBox<String> comboBox = (ComboBox<String>) uiComponent;
            if (selectedType.contains("COMBO_BOX_STREET") || selectedType.contains("COMBO_BOX_AREA")) {
                if (comboBox.getEditor() != null) {
                    comboBox.getEditor().setText(text);
                }
            } else {
                comboBox.setValue(text);
            }
        }
    }

    public Map<String, Map<String, List<String>>> getMainMap() {
        return mainMap;
    }

    public Map<String, Object> getReportMap() {
        return reportMap;
    }
}