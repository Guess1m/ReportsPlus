package com.Guess.ReportsPlus.Windows.Other;

import com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.CustomWindow;
import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Report.Database.DynamicDB;
import com.Guess.ReportsPlus.util.Report.nestedReportUtils.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.*;
import java.util.function.BiFunction;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.createCustomWindow;
import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.getWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.mainDesktopControllerObj;
import static com.Guess.ReportsPlus.MainApplication.mainRT;
import static com.Guess.ReportsPlus.Windows.Other.NotesViewController.notesViewController;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Misc.NotificationManager.*;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.*;
import static com.Guess.ReportsPlus.util.Report.Database.DynamicDB.isValidDatabase;
import static com.Guess.ReportsPlus.util.Report.reportUtil.createReportWindow;
import static com.Guess.ReportsPlus.util.Strings.customizationDataLoader.*;

public class LayoutBuilderController {

    public static LayoutBuilderController layoutBuilderController;
    CustomWindow addDropdownWindow = null;

    @FXML
    private VBox sectionContainer;
    @FXML
    private Button addSectionButton;
    @FXML
    private Button buildLayoutButton;
    @FXML
    private TextField reportTitleField;
    @FXML
    private Button addTransferButton;
    @FXML
    private VBox transferContainer;
    @FXML
    private Button customDropdownButton;
    @FXML
    private Button importExportButton;
    @FXML
    private Label heading;

    public static String extractNumberField(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root;
        try {
            root = mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            logError("Error parsing JSON: " + jsonString, e);
            return null;
        }

        if (root.isArray()) {
            for (JsonNode sectionNode : root) {
                Iterator<Map.Entry<String, JsonNode>> fields = sectionNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    JsonNode valueNode = entry.getValue();

                    if (valueNode.isArray()) {
                        for (JsonNode elementNode : valueNode) {
                            JsonNode fieldConfigs = elementNode.get("fieldConfigs");
                            if (fieldConfigs != null && fieldConfigs.isArray()) {
                                for (JsonNode configNode : fieldConfigs) {
                                    JsonNode fieldTypeNode = configNode.get("fieldType");
                                    if (fieldTypeNode != null && fieldTypeNode.isTextual() && "NUMBER_FIELD".equals(fieldTypeNode.asText())) {
                                        JsonNode fieldNameNode = configNode.get("fieldName");
                                        if (fieldNameNode != null && fieldNameNode.isTextual()) {
                                            return fieldNameNode.asText();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static Map<String, String> extractFieldNames(String jsonString) throws Exception {
        Map<String, String> reportSchema = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonString);

        if (root.isArray()) {
            for (JsonNode sectionNode : root) {
                Iterator<Map.Entry<String, JsonNode>> fields = sectionNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String key = entry.getKey();
                    JsonNode valueNode = entry.getValue();

                    if (valueNode.isArray()) {
                        for (JsonNode elementNode : valueNode) {
                            JsonNode fieldConfigs = elementNode.get("fieldConfigs");
                            if (fieldConfigs != null && fieldConfigs.isArray()) {
                                for (JsonNode configNode : fieldConfigs) {
                                    JsonNode fieldNameNode = configNode.get("fieldName");
                                    if (fieldNameNode != null && fieldNameNode.isTextual()) {
                                        String fieldName = fieldNameNode.asText();
                                        reportSchema.put(fieldName, "TEXT");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return reportSchema;
    }

    public static Map<String, Map<String, List<String>>> parseAndPopulateMap(String jsonString) {
        try {
            Map<String, Map<String, List<String>>> map = new HashMap<>();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonString);

            for (JsonNode section : rootNode) {
                for (JsonNode rowConfig : section.get("rowConfigs")) {
                    for (JsonNode fieldConfig : rowConfig.get("fieldConfigs")) {
                        String fieldName = fieldConfig.get("fieldName").asText();
                        String nodeType = fieldConfig.get("nodeType").asText();
                        String fieldType = fieldConfig.get("fieldType").asText();
                        String dropdownType = fieldConfig.get("dropdownType").asText();
                        String populateKey = fieldConfig.has("populateKey") && !fieldConfig.get("populateKey").isNull() ? fieldConfig.get("populateKey").asText() : "null";

                        map.computeIfAbsent("dropdownType", k -> new HashMap<>()).computeIfAbsent(fieldName, k -> new ArrayList<>()).add(dropdownType);

                        map.computeIfAbsent("fieldNames", k -> new HashMap<>()).computeIfAbsent(fieldName, k -> new ArrayList<>());

                        map.computeIfAbsent("nodeType", k -> new HashMap<>()).computeIfAbsent(fieldName, k -> new ArrayList<>()).add(nodeType);

                        map.computeIfAbsent("selectedType", k -> new HashMap<>()).computeIfAbsent(fieldName, k -> new ArrayList<>()).add(fieldType);

                        map.computeIfAbsent("keyMap", k -> new HashMap<>()).computeIfAbsent(populateKey, k -> new ArrayList<>()).add(fieldName);
                    }
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<SectionConfig> loadLayoutFromJSON(String data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<SectionConfig> layout = objectMapper.readValue(data, new TypeReference<List<SectionConfig>>() {
            });
            return layout;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static TransferConfig loadTransferConfigFromJSON(String data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TransferConfig transferConfig = objectMapper.readValue(data, TransferConfig.class);
            return transferConfig;
        } catch (IOException e) {
            logError("Error loading transferConfig from data [" + data + "]:", e);
        }
        return null;
    }

    public void initialize() {
        addSectionButton.setOnAction(event -> addSection());
        addTransferButton.setOnAction(event -> addTransfer());
        buildLayoutButton.setOnAction(this::handleBuildLayoutClick);
        importExportButton.setOnAction(this::handleImportExportClick);

        customDropdownButton.setOnAction(event -> {
            openCustomDropdownWindow();
        });

        addLocale();
    }

    private void addLocale() {
        customDropdownButton.setText(localization.getLocalizedMessage("LayoutBuilder.CustomDropdownButton", "Custom Dropdowns"));
        importExportButton.setText(localization.getLocalizedMessage("LayoutBuilder.ImportExportButton", "Import / Export"));
        heading.setText(localization.getLocalizedMessage("LayoutBuilder.Heading", "Custom Layout Designer"));
        buildLayoutButton.setText(localization.getLocalizedMessage("LayoutBuilder.BuildLayoutButton", "View Report Layout"));
    }

    private void handleImportExportClick(ActionEvent actionEvent) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #F4F4F4; -fx-border-radius: 10; -fx-background-radius: 10;");

        Label titleLabel = new Label(localization.getLocalizedMessage("ImportExport.title", "JSON Import/Export"));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-family: 'Inter 28pt Bold'; -fx-text-fill: #2c3e50;");

        Label descriptionLabel = new Label(localization.getLocalizedMessage("ImportExport.description", "Import or export layout configuration from/to JSON format"));
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle("-fx-text-fill: #7f8c8d;");

        TextArea jsonTextArea = new TextArea();
        jsonTextArea.setWrapText(false);
        jsonTextArea.setPromptText("Paste JSON here...");
        jsonTextArea.setStyle("-fx-font-family: Consolas; -fx-font-size: 12px;");

        ScrollPane jsonScrollPane = new ScrollPane(jsonTextArea);
        VBox.setVgrow(jsonScrollPane, Priority.ALWAYS);
        jsonScrollPane.setFitToWidth(true);
        jsonScrollPane.setFitToHeight(true);
        jsonScrollPane.setStyle("-fx-background-color: transparent;");

        HBox buttonContainer = new HBox(10);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);

        HBox fileButtons = new HBox(10);
        fileButtons.setAlignment(Pos.CENTER_LEFT);

        Button loadFileButton = new Button(localization.getLocalizedMessage("ImportExport.importFromFile", "Import from File"));
        loadFileButton.setOnAction(e -> loadJsonFromFile(jsonTextArea));

        Button saveFileButton = new Button(localization.getLocalizedMessage("ImportExport.exportToFile", "Export Current Layout to File"));
        saveFileButton.setOnAction(e -> saveJsonToFile());

        fileButtons.getChildren().addAll(loadFileButton, saveFileButton);

        HBox importButtonBox = new HBox();
        importButtonBox.setAlignment(Pos.CENTER_RIGHT);

        Button importButton = new Button(localization.getLocalizedMessage("ImportExport.applyLayout", "Apply Layout"));
        importButton.setOnAction(e -> handleImport(jsonTextArea));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonContainer.getChildren().addAll(fileButtons, spacer, importButtonBox);
        importButtonBox.getChildren().add(importButton);

        layout.getChildren().addAll(titleLabel, descriptionLabel, jsonScrollPane, buttonContainer);

        BorderPane window = new BorderPane();
        window.setCenter(layout);
        window.setPrefSize(700, 400);

        window.getStylesheets().add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/newReport/newReport.css").toExternalForm());

        addDropdownWindow = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), window, localization.getLocalizedMessage("ImportExport.windowTitle", "Layout Import/Export"), true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));
    }

    private void loadJsonFromFile(TextArea jsonTextArea) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(localization.getLocalizedMessage("ImportExport.openFileTitle", "Open JSON File"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        File file = fileChooser.showOpenDialog(mainRT);
        if (file != null) {
            try {
                String content = new String(Files.readAllBytes(file.toPath()));
                jsonTextArea.setText(content);
            } catch (IOException e) {
                showNotificationWarning("File Error", "Could not read file: " + e.getMessage());
                logError("Could not read file: ", e);
            }
        } else {
            showNotificationWarning("File Error", "File selection canceled");
            log("File selection canceled", LogUtils.Severity.WARN);
        }
    }

    private void saveJsonToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(localization.getLocalizedMessage("ImportExport.saveFileTitle", "Save JSON File"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));

        String jsonExportText;
        try {
            FullLayoutConfig config = new FullLayoutConfig();
            config.setLayout(buildLayout());
            config.setTransfer(buildTransferConfig());

            ObjectMapper mapper = new ObjectMapper();
            jsonExportText = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(config);
            showNotificationInfo("Export Successful", "Configuration exported successfully");
        } catch (Exception e) {
            showNotificationWarning("Export Error", "Failed to generate JSON: " + e.getMessage());
            return;
        }

        File file = fileChooser.showSaveDialog(mainRT);
        if (file != null) {
            try {
                Files.write(file.toPath(), jsonExportText.getBytes());
                showNotificationInfo("Save Successful", "File saved successfully");
            } catch (IOException e) {
                showNotificationWarning("Save Error", "Could not save file: " + e.getMessage());
                logError("Could not save file: ", e);
            }
        } else {
            log("File save canceled", LogUtils.Severity.WARN);
            showNotificationWarning("Save Error", "File save canceled");
        }
    }

    private void openCustomDropdownWindow() {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));
        layout.setStyle("-fx-background-color: #F4F4F4; -fx-border-radius: 10; -fx-background-radius: 10;");

        ComboBox<String> dropdownSelector = new ComboBox<>();
        dropdownSelector.setPromptText(localization.getLocalizedMessage("DropdownCreator.selectPrompt", "Select Dropdown"));
        dropdownSelector.setPrefWidth(250);

        List<String> fields = getCustomizationFields();
        dropdownSelector.getItems().setAll("New Dropdown");
        dropdownSelector.getItems().addAll(fields);
        dropdownSelector.setValue("New Dropdown");

        Label dropdownTitleLabel = new Label(localization.getLocalizedMessage("DropdownCreator.titleLabel", "Dropdown Title:"));
        dropdownTitleLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\";");
        TextField dropdownTitleField = new TextField();
        dropdownTitleField.setPrefWidth(250);

        Label dropdownItemsLabel = new Label(localization.getLocalizedMessage("DropdownCreator.itemsLabel", "Dropdown Items:"));
        dropdownItemsLabel.setStyle("-fx-font-family: \"Inter 28pt Bold\";");
        VBox dropdownItemsBox = new VBox(5);
        dropdownItemsBox.setStyle("-fx-background-color: #FFFFFF; -fx-border-color: #CCC; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-padding: 10;");

        Button addItemBtn = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add"));

        Button saveBtn = new Button(localization.getLocalizedMessage("Settings.NotiSaveButton", "Save"));

        Button deleteBtn = new Button(localization.getLocalizedMessage("Settings.NotiDeleteButton", "Delete"));
        deleteBtn.setDisable(true);

        Button importBtn = new Button("Import");
        Button exportBtn = new Button("Export");

        HBox selectorBox = new HBox(10, dropdownSelector, importBtn, exportBtn);
        selectorBox.setAlignment(Pos.CENTER_LEFT);

        BiFunction<String, Boolean, HBox> createItemRow = (value, deletable) -> {
            HBox row = new HBox(5);
            row.setAlignment(Pos.CENTER_LEFT);

            TextField field = new TextField(value);
            field.setPromptText("Item Name");
            HBox.setHgrow(field, Priority.ALWAYS);

            Button upBtn = new Button("↑");
            upBtn.setFocusTraversable(false);
            upBtn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold;");
            Button downBtn = new Button("↓");
            downBtn.setFocusTraversable(false);
            downBtn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold;");
            Button itemDelBtn = new Button("×");
            itemDelBtn.setFocusTraversable(false);
            itemDelBtn.setStyle("-fx-background-color: transparent; -fx-font-weight: bold; -fx-text-fill: #ff4444;");

            upBtn.setOnAction(e -> {
                int index = dropdownItemsBox.getChildren().indexOf(row);
                if (index > 0) {
                    dropdownItemsBox.getChildren().remove(index);
                    dropdownItemsBox.getChildren().add(index - 1, row);
                }
            });

            downBtn.setOnAction(e -> {
                int index = dropdownItemsBox.getChildren().indexOf(row);
                if (index < dropdownItemsBox.getChildren().size() - 1) {
                    dropdownItemsBox.getChildren().remove(index);
                    dropdownItemsBox.getChildren().add(index + 1, row);
                }
            });

            itemDelBtn.setOnAction(e -> dropdownItemsBox.getChildren().remove(row));

            if (deletable) {
                row.getChildren().addAll(field, upBtn, downBtn, itemDelBtn);
            } else {
                row.getChildren().add(field);
            }

            return row;
        };

        addItemBtn.setOnAction(e -> {
            HBox itemRow = createItemRow.apply("", true);
            dropdownItemsBox.getChildren().add(itemRow);
        });

        dropdownSelector.valueProperty().addListener((obs, oldVal, selectedDropdown) -> {
            if (selectedDropdown == null || selectedDropdown.equals("New Dropdown")) {
                dropdownTitleField.clear();
                dropdownItemsBox.getChildren().clear();
                deleteBtn.setDisable(true);
            } else {
                dropdownTitleField.setText(selectedDropdown);
                dropdownItemsBox.getChildren().clear();
                getValuesForField(selectedDropdown).forEach(item -> {
                    if (!item.equals("N/A")) {
                        dropdownItemsBox.getChildren().add(createItemRow.apply(item, true));
                    }
                });
                deleteBtn.setDisable(false);
            }
        });

        saveBtn.setOnAction(e -> {
            String title = dropdownTitleField.getText().trim();
            String originalName = dropdownSelector.getValue();

            if (title.isEmpty()) {
                showNotificationWarning("Validation Error", "Title cannot be empty");
                return;
            }

            List<String> items = new ArrayList<>();
            for (Node node : dropdownItemsBox.getChildren()) {
                HBox row = (HBox) node;
                TextField field = (TextField) row.getChildren().get(0);
                String text = field.getText().trim();
                if (!text.isEmpty()) items.add(text);
            }

            if (items.isEmpty()) {
                showNotificationWarning("Validation Error", "Must have at least one item");
                return;
            }

            try {

                if (originalName != null && !originalName.equals("New Dropdown")) {
                    deleteCustomField(originalName);
                }

                if (addCustomField(title, items)) {

                    List<String> updatedFields = getCustomizationFields();
                    dropdownSelector.getItems().setAll("New Dropdown");
                    dropdownSelector.getItems().addAll(updatedFields);
                    dropdownSelector.setValue(title);
                }
            } catch (IOException ex) {
                logError("Dropdown save failed", ex);
            }
        });

        deleteBtn.setOnAction(e -> {
            String dropdownName = dropdownSelector.getValue();

            if (dropdownName == null || dropdownName.equals("New Dropdown")) {
                showNotificationError("Invalid Selection", "No dropdown selected");
                return;
            }

            Label message = new Label("Are you sure you want to delete '" + dropdownName + "'?");
            message.setStyle("-fx-font-family: \"Inter 28pt Bold\"; -fx-font-size: 14;");

            Button yesBtn = new Button("Yes");
            yesBtn.setStyle("-fx-background-color: #ff4444; -fx-text-fill: white;");
            Button noBtn = new Button("No");
            noBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

            HBox buttonBox = new HBox(10, yesBtn, noBtn);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);

            VBox dialogContent = new VBox(15, message, buttonBox);
            dialogContent.setPadding(new Insets(20));
            dialogContent.setStyle("-fx-background-color: #F4F4F4;");

            BorderPane layoutPane = new BorderPane();
            layoutPane.setCenter(dialogContent);
            layoutPane.setPrefSize(450, 150);

            CustomWindow confirmDialog = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), layoutPane, "Confirm", true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));

            yesBtn.setOnAction(event -> {
                confirmDialog.closeWindow();
                try {
                    if (deleteCustomField(dropdownName)) {
                        showNotificationInfo("Success", "Deleted '" + dropdownName + "' successfully");

                        List<String> updatedFields = getCustomizationFields();
                        dropdownSelector.getItems().setAll("New Dropdown");
                        dropdownSelector.getItems().addAll(updatedFields);
                        dropdownSelector.setValue("New Dropdown");
                        dropdownTitleField.clear();
                        dropdownItemsBox.getChildren().clear();
                    } else {
                        showNotificationError("Error", "Failed to delete '" + dropdownName + "'");
                    }
                } catch (IOException ex) {
                    logError("Dropdown deletion failed: ", ex);
                    showNotificationError("Error", "Failed to delete '" + dropdownName + "'");
                }
            });

            noBtn.setOnAction(event -> confirmDialog.closeWindow());

        });

        exportBtn.setOnAction(e -> {
            List<String> customFields = getCustomizationFields();
            if (customFields.isEmpty()) {
                showNotificationWarning("Export Error", "No custom dropdowns to export");
                return;
            }

            ListView<String> listView = new ListView<>();
            listView.getItems().addAll(customFields);
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            Button exportSelectedBtn = new Button("Export Selected");

            VBox exportLayout = new VBox(10, listView, exportSelectedBtn);
            exportLayout.setPadding(new Insets(15));

            BorderPane exportBorderPane = new BorderPane(exportLayout);
            exportBorderPane.setPrefSize(350, 400);

            CustomWindow exportDialog = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), exportBorderPane, "Export", false, 1, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));

            exportSelectedBtn.setOnAction(event -> {
                List<String> selected = listView.getSelectionModel().getSelectedItems();
                if (selected.isEmpty()) {
                    showNotificationWarning("Export Error", "No dropdowns selected");
                    return;
                }

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Save Export File");
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
                File file = fileChooser.showSaveDialog(mainRT);
                if (file == null) return;

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode exportData = mapper.createObjectNode();

                    for (String field : selected) {
                        List<String> items = getValuesForField(field);
                        ArrayNode arrayNode = mapper.createArrayNode();
                        items.forEach(arrayNode::add);
                        exportData.set(field, arrayNode);
                    }

                    mapper.writerWithDefaultPrettyPrinter().writeValue(file, exportData);
                    showNotificationInfo("Export Successful", "Exported " + selected.size() + " dropdown(s)");
                } catch (IOException ex) {
                    logError("Export failed: ", ex);
                    showNotificationError("Export Error", "Failed to export dropdowns");
                }

                exportDialog.closeWindow();
            });
        });

        importBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Dropdowns");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            File file = fileChooser.showOpenDialog(null);
            if (file == null) return;

            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(file);

                if (!rootNode.isObject()) {
                    showNotificationError("Import Error", "Invalid file format");
                    return;
                }

                List<String> existingFields = getCustomizationFields();
                Map<String, List<String>> importCandidates = new LinkedHashMap<>();

                Iterator<Map.Entry<String, JsonNode>> fieldsTwo = rootNode.fields();
                while (fieldsTwo.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fieldsTwo.next();
                    String fieldName = entry.getKey();
                    JsonNode itemsNode = entry.getValue();

                    if (DEFAULT_FIELDS.contains(fieldName) || existingFields.contains(fieldName)) continue;
                    if (!itemsNode.isArray()) continue;

                    Set<String> uniqueItems = new LinkedHashSet<>();
                    uniqueItems.add("N/A");
                    itemsNode.forEach(itemNode -> {
                        String item = itemNode.asText().trim();
                        if (!item.isEmpty() && !item.equalsIgnoreCase("N/A")) {
                            uniqueItems.add(item);
                        }
                    });

                    if (uniqueItems.size() > 1) {
                        importCandidates.put(fieldName, new ArrayList<>(uniqueItems));
                    }
                }

                if (importCandidates.isEmpty()) {
                    showNotificationInfo("Import", "No new dropdowns to import");
                    return;
                }

                ListView<String> listView = new ListView<>();
                listView.getItems().addAll(importCandidates.keySet());

                Button confirmImportBtn = new Button("Import");

                VBox confirmLayout = new VBox(10, new Label("The following dropdowns will be imported:"), listView, confirmImportBtn);
                confirmLayout.setPadding(new Insets(15));

                BorderPane layoutPane = new BorderPane(confirmLayout);
                layoutPane.setPrefSize(350, 400);

                CustomWindow confirmDialog = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), layoutPane, "Import", false, 1, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));

                confirmImportBtn.setOnAction(event -> {
                    int successCount = 0;
                    for (Map.Entry<String, List<String>> entry : importCandidates.entrySet()) {
                        try {
                            if (addCustomField(entry.getKey(), entry.getValue())) {
                                successCount++;
                            }
                        } catch (IOException ex) {
                            logError("Import failed for " + entry.getKey() + ": ", ex);
                        }
                    }

                    List<String> updatedFields = getCustomizationFields();
                    dropdownSelector.getItems().setAll("New Dropdown");
                    dropdownSelector.getItems().addAll(updatedFields);

                    showNotificationInfo("Import Successful", "Imported " + successCount + " dropdown(s)");
                    confirmDialog.closeWindow();
                });

            } catch (IOException ex) {
                logError("Import failed: ", ex);
                showNotificationError("Import Error", "Failed to import dropdowns");
            }
        });

        HBox buttonBox = new HBox(10, saveBtn, deleteBtn);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        ScrollPane dropdownScrollPane = new ScrollPane(dropdownItemsBox);
        VBox.setVgrow(dropdownScrollPane, Priority.ALWAYS);
        dropdownScrollPane.getStylesheets().add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/main/transparentScrollPane.css").toExternalForm());
        dropdownScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        dropdownScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        dropdownScrollPane.setFitToWidth(true);
        dropdownScrollPane.setFitToHeight(true);
        dropdownScrollPane.setPannable(true);

        layout.getStylesheets().add(Launcher.class.getResource("/com/Guess/ReportsPlus/css/newReport/newReport.css").toExternalForm());
        layout.getChildren().addAll(selectorBox, dropdownTitleLabel, dropdownTitleField, dropdownItemsLabel, dropdownScrollPane, addItemBtn, buttonBox);

        BorderPane window = new BorderPane();
        window.setCenter(layout);
        window.setPrefSize(500, 550);

        addDropdownWindow = createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), window, localization.getLocalizedMessage("DropdownCreator.windowTitle", "Dropdown Manager"), true, 1, true, true, mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/report.png"))));
    }

    public String saveLayoutToJSON() {
        List<SectionConfig> layout = buildLayout();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String layoutJson = objectMapper.writeValueAsString(layout);
            return layoutJson;
        } catch (JsonProcessingException e) {
            logError("Error saving layout to json: " + layout, e);
        }

        return null;
    }

    public String saveTransferConfigToJSON() {
        TransferConfig layout = buildTransferConfig();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String layoutJson = objectMapper.writeValueAsString(layout);
            return layoutJson;
        } catch (JsonProcessingException e) {
            logError("Error saving transferConfig to json: " + layout, e);
        }

        return null;
    }

    private TransferConfig buildTransferConfig() {
        if (transferContainer.getChildren().isEmpty()) {
            return null;
        }

        TransferPane transferPane = (TransferPane) transferContainer.getChildren().get(0);
        String transferName = transferPane.transferNameField.getText();

        List<RowConfig> rowConfigs = new ArrayList<>();
        for (Node node : transferPane.elementContainer.getChildren()) {
            if (node instanceof ElementPane) {
                ElementPane elementPane = (ElementPane) node;
                String fieldName = elementPane.elementNameField.getText().trim();
                String fieldReport = elementPane.reportComboBox.getValue().trim();
                FieldConfig fieldConfig = new FieldConfig("transfer_" + fieldName + "_" + fieldReport, 12, FieldType.TRANSFER_BUTTON);
                fieldConfig.setNodeType("BUTTON");
                rowConfigs.add(new RowConfig(fieldConfig));
            }
        }
        return new TransferConfig(transferName, rowConfigs.toArray(new RowConfig[0]));
    }

    private List<SectionConfig> buildLayout() {
        List<SectionConfig> sections = new ArrayList<>();
        for (Node node : sectionContainer.getChildren()) {
            if (node instanceof SectionPane) {
                sections.add(((SectionPane) node).buildSectionConfig());
            }
        }
        return sections;
    }

    private void addSection() {
        SectionPane sectionPane = new SectionPane();
        sectionContainer.getChildren().add(sectionPane);
    }

    private void addTransfer() {
        if (!transferContainer.getChildren().isEmpty()) {
            return;
        }
        TransferPane transferPane = new TransferPane();
        transferContainer.getChildren().add(transferPane);
    }

    private void handleBuildLayoutClick(ActionEvent event) {
        String reportTitle = reportTitleField.getText().trim();

        if (!validateFields()) {
            log("LayoutBuilder; " + "Could not validate fields", LogUtils.Severity.ERROR);
            return;
        }
        String dataFolderPath = getCustomDataLogsFolderPath();
        createFolderIfNotExists(dataFolderPath);
        File[] files = new File(dataFolderPath).listFiles((dir, name) -> name.endsWith(".db"));
        if (files != null || files.length != 0) {
            for (File file : files) {
                if (file.getName().equalsIgnoreCase(reportTitle + ".db")) {
                    log("NewReport; Report title already exists: " + reportTitle, LogUtils.Severity.ERROR);
                    showNotificationWarning("Report Creation Utility", "Report title already exists: " + reportTitle);
                    return;
                }
            }
        }

        String data = saveLayoutToJSON();
        List<SectionConfig> layout = loadLayoutFromJSON(data);
        String transferData = saveTransferConfigToJSON();
        TransferConfig transferConfig = loadTransferConfigFromJSON(transferData);

        Map<String, Map<String, List<String>>> newMap = parseAndPopulateMap(data);

        var numFieldCount = newMap.getOrDefault("selectedType", new HashMap<>()).values().stream().flatMap(List::stream).filter(type -> type.equalsIgnoreCase("NUMBER_FIELD")).count();
        if (numFieldCount != 1) {
            showNotificationWarning("Report Creation Utility", "Exactly one NUMBER_FIELD is required, found: " + numFieldCount);
            log("LayoutBuilder; Exactly one NUMBER_FIELD is required, found: " + numFieldCount, LogUtils.Severity.ERROR);
            newMap.put("selectedType", null);
            return;
        }

        Map<String, Object> reportWindow = createReportWindow(reportTitle, transferConfig, layout.toArray(new SectionConfig[0]));
        Map<String, Object> reportMap = (Map<String, Object>) reportWindow.get(reportTitle + " Map");

        MenuButton pullNotesBtn = (MenuButton) reportWindow.get("pullNotesBtn");
        Button submitBtn = (Button) reportWindow.get("submitBtn");

        submitBtn.setText("Create New Report Type: " + reportTitle);

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
                            if (!value.equalsIgnoreCase("null")) {
                                box.getItems().addAll(getValuesForField(value));
                            }
                        }
                    }
                }
            }
        }

        for (String name : reportMap.keySet()) {
            if (name.startsWith("transfer_")) {
                String[] values = name.split("_");
                Button btn = (Button) reportMap.get(name);
                btn.setText(values[1]);
            }
        }

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

                                String key = newMap.getOrDefault("keyMap", new HashMap<>()).entrySet().stream().filter(entry -> entry.getValue().contains(field)).map(Map.Entry::getKey).findFirst().orElse(null);

                                if (key == null) {
                                    continue;
                                }

                                if (fieldValue instanceof TextField) {
                                    updateTextFromNotepad((TextField) fieldValue, noteArea, "-" + key);
                                } else if (fieldValue instanceof ComboBox) {
                                    updateTextFromNotepad(((ComboBox<?>) fieldValue).getEditor(), noteArea, "-" + key);
                                } else if (fieldValue instanceof TextArea) {
                                    updateTextFromNotepad((TextArea) fieldValue, noteArea, "-" + key);
                                } else {
                                    log("LayoutBuilder; Unknown field type: " + fieldValue.getClass().getSimpleName(), LogUtils.Severity.ERROR);
                                }
                            }
                        });
                    }
                }
            }
        });

        submitBtn.setOnAction(submitEvent -> {
            log("layoutBuilder; trying to create DB: " + reportTitle, LogUtils.Severity.INFO);

            Map<String, String> layoutScheme = new HashMap<>();
            layoutScheme.put("key", "TEXT");
            layoutScheme.put("layoutData", "TEXT");
            layoutScheme.put("transferData", "TEXT");
            Map<String, Object> layoutMap = new HashMap<>();
            layoutMap.put("key", "1");
            layoutMap.put("layoutData", data);
            Map<String, Object> transferMap = new HashMap<>();
            transferMap.put("key", "2");
            transferMap.put("transferData", transferData);

            Map<String, String> reportSchema = new HashMap<>();
            DynamicDB dbManager = null;
            try {
                reportSchema = extractFieldNames(data);
                reportSchema.put("report_status", "TEXT");
                dbManager = new DynamicDB(getCustomDataLogsFolderPath() + reportTitle, "data", extractNumberField(data), reportSchema);
                dbManager.initDB();
            } catch (Exception e) {
                logError("Failed to extract field names", e);
            } finally {
                try {
                    dbManager.close();
                } catch (SQLException e) {
                    logError("Failed to close database connection, null", e);
                }
            }

            DynamicDB DatabaseLayout = new DynamicDB(getCustomDataLogsFolderPath() + reportTitle, "layout", "key", layoutScheme);
            if (DatabaseLayout.initDB()) {
                log("LayoutBuilder; Layout Database for: [" + reportTitle + "] Initialized", LogUtils.Severity.INFO);
                try {
                    DatabaseLayout.addOrReplaceRecord(layoutMap);
                    DatabaseLayout.addOrReplaceRecord(transferMap);
                } catch (SQLException e) {
                    logError("Error adding/replacing record: " + layoutScheme, e);
                } finally {
                    try {
                        DatabaseLayout.close();
                    } catch (SQLException e) {
                        logError("Error closing Database: [" + DatabaseLayout.getTableName() + "]", e);
                    }
                }
            } else {
                log("LayoutBuilder; Layout Database not initialized!", LogUtils.Severity.ERROR);
                showNotificationError("Report Creation Utility", "Error initializing layout database!");
            }
            for (String fieldName : reportMap.keySet()) {
                Object field = reportMap.get(fieldName);
                if (field instanceof ComboBox<?> comboBox) {
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        comboBox.getSelectionModel().selectFirst();
                    }
                }
            }

            DynamicDB Database = new DynamicDB(getCustomDataLogsFolderPath() + reportTitle, "data", extractNumberField(data), reportSchema);
            try {
                if (Database.initDB()) {
                    log("LayoutBuilder; Database initialized", LogUtils.Severity.INFO);
                } else {
                    log("LayoutBuilder; Database not initialized!", LogUtils.Severity.ERROR);
                    showNotificationError("Report Creation Utility", "Error initializing database!");
                }
            } finally {
                try {
                    Database.close();
                } catch (SQLException e) {
                    logError("Error closing Database: [" + Database.getTableName() + "]", e);
                }
            }

            showNotificationInfo("Report Manager", "New Report Type [" + reportTitle + "] Created!");
            CustomWindow window = getWindow(reportTitle);
            if (window != null) {
                window.closeWindow();
            }
        });
    }

    private boolean containsNumbers(String str) {
        return str.matches(".*\\d.*");
    }

    private boolean validateFields() {
        Set<String> usedNames = new HashSet<>();
        String reportTitle = reportTitleField.getText().trim();
        if (reportTitle.isEmpty()) {
            showNotificationWarning("Report Creation Utility", "Report Title Field is Empty");
            log("LayoutBuilder; Report Title Field is Empty", LogUtils.Severity.ERROR);
            return false;
        }
        if (usedNames.contains(reportTitle)) {
            showNotificationWarning("Report Creation Utility", "Report Title must be unique.");
            log("LayoutBuilder; Duplicate Report Title: " + reportTitle, LogUtils.Severity.ERROR);
            return false;
        }
        usedNames.add(reportTitle);

        if (!transferContainer.getChildren().isEmpty()) {
            TransferPane transferPane = (TransferPane) transferContainer.getChildren().get(0);
            String transferName = transferPane.transferNameField.getText().trim();
            if (transferName.isEmpty()) {
                log("Transfer Name cannot be empty.", LogUtils.Severity.ERROR);
                showNotificationWarning("Report Creation Utility", "Transfer Name cannot be empty.");
                return false;
            }
            if (containsNumbers(transferName)) {
                log("Transfer Name cannot contain numbers.", LogUtils.Severity.ERROR);
                showNotificationWarning("Report Creation Utility", "Transfer Name cannot contain numbers.");
                return false;
            }
            if (usedNames.contains(transferName)) {
                showNotificationWarning("Report Creation Utility", "Transfer Name must be unique.");
                log("LayoutBuilder; Duplicate Transfer Name: " + transferName, LogUtils.Severity.ERROR);
                return false;
            }
            usedNames.add(transferName);

            for (Node node : transferPane.elementContainer.getChildren()) {
                if (node instanceof ElementPane) {
                    ElementPane elementPane = (ElementPane) node;
                    String elementName = elementPane.elementNameField.getText().trim();
                    if (elementName.isEmpty()) {
                        log("Element Name cannot be empty.", LogUtils.Severity.ERROR);
                        showNotificationWarning("Report Creation Utility", "Element Name cannot be empty.");
                        return false;
                    }
                    if (containsNumbers(elementName)) {
                        log("Element Name cannot contain numbers.", LogUtils.Severity.ERROR);
                        showNotificationWarning("Report Creation Utility", "Element Name cannot contain numbers.");
                        return false;
                    }
                    if (usedNames.contains(elementName)) {
                        showNotificationWarning("Report Creation Utility", "Element Name must be unique.");
                        log("LayoutBuilder; Duplicate Element Name: " + elementName, LogUtils.Severity.ERROR);
                        return false;
                    }
                    usedNames.add(elementName);

                    String reportValue = elementPane.reportComboBox.getValue() != null ? elementPane.reportComboBox.getValue().trim() : "";
                    if (reportValue.isEmpty()) {
                        log("Report ComboBox cannot be empty.", LogUtils.Severity.ERROR);
                        showNotificationWarning("Report Creation Utility", "Report ComboBox cannot be empty.");
                        return false;
                    }
                    if (containsNumbers(reportValue)) {
                        log("Report ComboBox value cannot contain numbers.", LogUtils.Severity.ERROR);
                        showNotificationWarning("Report Creation Utility", "Report ComboBox value cannot contain numbers.");
                        return false;
                    }
                }
            }
        }

        for (Node node : sectionContainer.getChildren()) {
            if (node instanceof SectionPane) {
                SectionPane sectionPane = (SectionPane) node;
                String sectionTitle = sectionPane.sectionTitleField.getText().trim();
                if (sectionTitle.isEmpty()) {
                    log("Section Title cannot be empty.", LogUtils.Severity.ERROR);
                    showNotificationWarning("Report Creation Utility", "Section Title cannot be empty.");
                    return false;
                }
                if (containsNumbers(sectionTitle)) {
                    log("Section Title cannot contain numbers.", LogUtils.Severity.ERROR);
                    showNotificationWarning("Report Creation Utility", "Section Title cannot contain numbers.");
                    return false;
                }
                if (usedNames.contains(sectionTitle)) {
                    showNotificationWarning("Report Creation Utility", "Section Title must be unique.");
                    log("LayoutBuilder; Duplicate Section Title: " + sectionTitle, LogUtils.Severity.ERROR);
                    return false;
                }
                usedNames.add(sectionTitle);

                for (Node rowNode : sectionPane.rowContainer.getChildren()) {
                    if (rowNode instanceof RowPane) {
                        RowPane rowPane = (RowPane) rowNode;
                        for (Node fieldNode : rowPane.fieldContainer.getChildren()) {
                            if (fieldNode instanceof FieldPane) {
                                FieldPane fieldPane = (FieldPane) fieldNode;
                                String fieldName = fieldPane.fieldNameField.getText().trim();
                                String normalizedFieldName = fieldName.toLowerCase();

                                if (fieldName.isEmpty()) {
                                    log("Field Name cannot be empty.", LogUtils.Severity.ERROR);
                                    showNotificationWarning("Report Creation Utility", "Field Name cannot be empty.");
                                    return false;
                                }
                                if (!fieldName.matches("^[\\w ]+$")) {
                                    showNotificationWarning("Invalid Characters", "Use only letters, and underscores");
                                    return false;
                                }
                                if (fieldName.equalsIgnoreCase("report_status")) {
                                    log("Field Name cannot be 'report_status'.", LogUtils.Severity.ERROR);
                                    showNotificationWarning("Report Creation Utility", "Field Name cannot be 'report_status' (RESERVED)");
                                }
                                if (usedNames.contains(normalizedFieldName)) {
                                    showNotificationWarning("Duplicate Field", "Field name '" + fieldName + "' conflicts with another");
                                    log("Duplicate field name: " + fieldName, LogUtils.Severity.ERROR);
                                    return false;
                                }
                                usedNames.add(normalizedFieldName);

                                String fieldType = fieldPane.fieldTypeComboBox.getValue() != null ? fieldPane.fieldTypeComboBox.getValue().trim() : "";
                                if (fieldType.isEmpty()) {
                                    log("Field Type ComboBox cannot be empty.", LogUtils.Severity.ERROR);
                                    showNotificationWarning("Report Creation Utility", "Field Type ComboBox cannot be empty.");
                                    return false;
                                }
                                if (containsNumbers(fieldType)) {
                                    log("Field Type ComboBox value cannot contain numbers.", LogUtils.Severity.ERROR);
                                    showNotificationWarning("Report Creation Utility", "Field Type ComboBox value cannot contain numbers.");
                                    return false;
                                }
                                if (fieldType.contains(" ")) {
                                    log("Field Type cannot contain spaces.", LogUtils.Severity.ERROR);
                                    showNotificationWarning("Report Creation Utility", "Field Type cannot contain spaces.");
                                    return false;
                                }

                                String keyFieldName = fieldPane.populateKeyField.getText().trim();
                                if (containsNumbers(keyFieldName)) {
                                    log("Key Field cannot contain numbers.", LogUtils.Severity.ERROR);
                                    showNotificationWarning("Report Creation Utility", "Key Field cannot contain numbers.");
                                    return false;
                                }
                                if (keyFieldName.contains(" ")) {
                                    log("Key Field cannot contain spaces.", LogUtils.Severity.ERROR);
                                    showNotificationWarning("Report Creation Utility", "Key Field cannot contain spaces.");
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private TransferPane createTransferPaneFromConfig(TransferConfig config) {
        TransferPane pane = new TransferPane();
        pane.transferNameField.setText(config.getTitle());
        for (RowConfig rowConfig : config.getRowConfigs()) {

            FieldConfig elementConfig = rowConfig.getFieldConfigs()[0];
            ElementPane elementPane = createElementPaneFromConfig(elementConfig);
            pane.elementContainer.getChildren().add(elementPane);
        }
        return pane;
    }

    private ElementPane createElementPaneFromConfig(FieldConfig config) {
        ElementPane pane = new ElementPane();

        String[] parts = config.getFieldName().split("_");
        if (parts.length >= 3) {
            pane.elementNameField.setText(parts[1]);
            String reportName = parts[2];
            pane.reportComboBox.setValue(reportName);

            if (!pane.reportComboBox.getItems().contains(reportName)) {
                pane.reportComboBox.getItems().add(reportName);
            }
        }
        return pane;
    }

    private boolean validateJsonStructure(String json) {
        try {
            JsonNode root = new ObjectMapper().readTree(json);
            return root.has("layout") || root.has("transfer");
        } catch (IOException e) {
            return false;
        }
    }

    private void handleImport(TextArea jsonTextArea) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            FullLayoutConfig config = mapper.readValue(jsonTextArea.getText(), FullLayoutConfig.class);

            if (!validateJsonStructure(jsonTextArea.getText())) {
                logError("Invalid JSON format", new IllegalArgumentException("Invalid JSON Structure"));
            }

            sectionContainer.getChildren().clear();
            transferContainer.getChildren().clear();

            if (config.getLayout() != null) {
                for (SectionConfig sectionConfig : config.getLayout()) {
                    SectionPane sectionPane = createSectionPaneFromConfig(sectionConfig);
                    sectionContainer.getChildren().add(sectionPane);
                }
            }

            if (config.getTransfer() != null) {
                TransferPane transferPane = createTransferPaneFromConfig(config.getTransfer());
                transferContainer.getChildren().add(transferPane);
            }

            Platform.runLater(() -> {
                for (Node sectionNode : sectionContainer.getChildren()) {
                    if (sectionNode instanceof SectionPane) {
                        SectionPane sectionPane = (SectionPane) sectionNode;
                        for (Node rowNode : sectionPane.rowContainer.getChildren()) {
                            if (rowNode instanceof RowPane) {
                                ((RowPane) rowNode).recalcWidthLimits();
                            }
                        }
                    }
                }
            });

            showNotificationInfo("Import Successful", "Configuration loaded successfully");
        } catch (Exception e) {
            showNotificationWarning("Import Error", "Invalid JSON format: " + e.getMessage());
        }
    }

    private SectionPane createSectionPaneFromConfig(SectionConfig config) {
        SectionPane pane = new SectionPane();
        pane.sectionTitleField.setText(config.getSectionTitle());
        for (RowConfig rowConfig : config.getRowConfigs()) {
            RowPane rowPane = createRowPaneFromConfig(rowConfig);
            pane.rowContainer.getChildren().add(rowPane);
        }
        return pane;
    }

    private RowPane createRowPaneFromConfig(RowConfig config) {
        RowPane pane = new RowPane();
        for (FieldConfig fieldConfig : config.getFieldConfigs()) {
            FieldPane fieldPane = createFieldPaneFromConfig(fieldConfig);
            pane.fieldContainer.getChildren().add(fieldPane);
        }
        pane.recalcWidthLimits();
        return pane;
    }

    private FieldPane createFieldPaneFromConfig(FieldConfig config) {
        FieldPane pane = new FieldPane(config.getSize());
        pane.fieldNameField.setText(config.getFieldName());
        if (config.getPopulateKey() != null) {
            pane.populateKeyField.setText(config.getPopulateKey());
        }
        pane.fieldTypeComboBox.setValue(config.getFieldType().name());

        if (config.getFieldType() == FieldType.CUSTOM_DROPDOWN) {
            pane.customDropdownComboBox.setValue(config.getDropdownType());
        }

        pane.widthSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            RowPane rowPane = (RowPane) pane.getParent().getParent();
            rowPane.recalcWidthLimits();
        });

        return pane;
    }

    private class SectionPane extends VBox {
        private TextField sectionTitleField;
        private VBox rowContainer;
        private Button addRowButton;

        private Button removeSectionButton;

        SectionPane() {
            setSpacing(5);
            setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-padding: 10; -fx-background-color: rgba(0,0,0,0.04);");
            HBox header = new HBox(10);
            sectionTitleField = new TextField();
            sectionTitleField.setPromptText(localization.getLocalizedMessage("ReportWindows.SectionButton", "Section") + " " + localization.getLocalizedMessage("Login_Window.NamePromptText", "Name"));

            Button moveUpButton = new Button("↑");
            Button moveDownButton = new Button("↓");
            moveUpButton.setFocusTraversable(false);
            moveDownButton.setFocusTraversable(false);
            moveUpButton.getStyleClass().add("moveButton");
            moveDownButton.getStyleClass().add("moveButton");

            moveUpButton.setOnAction(event -> {
                int index = sectionContainer.getChildren().indexOf(this);
                if (index > 0) {
                    sectionContainer.getChildren().remove(index);
                    sectionContainer.getChildren().add(index - 1, this);
                }
            });

            moveDownButton.setOnAction(event -> {
                int index = sectionContainer.getChildren().indexOf(this);
                if (index < sectionContainer.getChildren().size() - 1) {
                    sectionContainer.getChildren().remove(index);
                    sectionContainer.getChildren().add(index + 1, this);
                }
            });

            removeSectionButton = new Button(localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove") + " " + localization.getLocalizedMessage("ReportWindows.SectionButton", "Section"));
            removeSectionButton.setFocusTraversable(false);
            removeSectionButton.getStyleClass().add("removeButton");
            removeSectionButton.setOnAction(event -> sectionContainer.getChildren().remove(this));
            header.getChildren().addAll(new Label(localization.getLocalizedMessage("ReportWindows.SectionButton", "Section") + ":"), sectionTitleField, moveUpButton, moveDownButton, removeSectionButton);
            rowContainer = new VBox(5);
            rowContainer.setStyle("-fx-background-color: transparent;");
            addRowButton = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add") + " " + localization.getLocalizedMessage("ReportWindows.RowButton", "Row"));
            addRowButton.getStyleClass().add("addButton");
            addRowButton.setOnAction(event -> addRow());

            addRowButton.setFocusTraversable(false);
            getChildren().addAll(header, rowContainer, addRowButton);
        }

        private void addRow() {
            RowPane rowPane = new RowPane();
            rowContainer.getChildren().add(rowPane);
        }

        private SectionConfig buildSectionConfig() {
            String title = sectionTitleField.getText();

            List<RowConfig> rowConfigs = new ArrayList<>();
            for (Node node : rowContainer.getChildren()) {
                if (node instanceof RowPane) {
                    rowConfigs.add(((RowPane) node).buildRowConfig());
                }
            }
            return new SectionConfig(title, rowConfigs.toArray(new RowConfig[0]));
        }

    }

    private class RowPane extends VBox {
        private HBox fieldContainer;
        private Button addFieldButton;

        private Button removeRowButton;

        RowPane() {
            setSpacing(5);
            setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-padding: 10; -fx-background-color: rgba(0,0,0,0.04);");
            fieldContainer = new HBox(10);
            addFieldButton = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add") + " " + localization.getLocalizedMessage("ReportWindows.FieldButton", "Field"));
            addFieldButton.getStyleClass().add("addButton");
            addFieldButton.setOnAction(event -> addField());
            removeRowButton = new Button(localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove") + " " + localization.getLocalizedMessage("ReportWindows.RowButton", "Row"));
            removeRowButton.getStyleClass().add("removeButton");
            removeRowButton.setOnAction(event -> {
                VBox parent = (VBox) getParent();
                parent.getChildren().remove(this);
                recalcWidthLimits();
            });
            Button moveUpButton = new Button("↑");
            Button moveDownButton = new Button("↓");
            moveUpButton.getStyleClass().add("moveButton");
            moveDownButton.getStyleClass().add("moveButton");

            moveUpButton.setFocusTraversable(false);
            moveDownButton.setFocusTraversable(false);
            removeRowButton.setFocusTraversable(false);
            addFieldButton.setFocusTraversable(false);

            moveUpButton.setOnAction(event -> {
                VBox parent = (VBox) getParent();
                int index = parent.getChildren().indexOf(this);
                if (index > 0) {
                    parent.getChildren().remove(index);
                    parent.getChildren().add(index - 1, this);
                }
            });

            moveDownButton.setOnAction(event -> {
                VBox parent = (VBox) getParent();
                int index = parent.getChildren().indexOf(this);
                if (index < parent.getChildren().size() - 1) {
                    parent.getChildren().remove(index);
                    parent.getChildren().add(index + 1, this);
                }
            });

            HBox controlBox = new HBox(10, addFieldButton, moveUpButton, moveDownButton, removeRowButton);
            getChildren().addAll(fieldContainer, controlBox);
            recalcWidthLimits();
        }

        private void addField() {
            int currentTotal = 0;
            for (Node node : fieldContainer.getChildren()) {
                if (node instanceof FieldPane) {
                    currentTotal += ((FieldPane) node).widthSpinner.getValue();
                }
            }
            int available = 12 - currentTotal;
            if (available < 1) {
                addFieldButton.setDisable(true);
                return;
            }
            FieldPane fieldPane = new FieldPane(available);
            fieldContainer.getChildren().add(fieldPane);
            fieldPane.widthSpinner.valueProperty().addListener((obs, oldVal, newVal) -> recalcWidthLimits());
            recalcWidthLimits();
        }

        private void recalcWidthLimits() {
            int currentTotal = 0;
            List<FieldPane> fields = new ArrayList<>();
            for (Node node : fieldContainer.getChildren()) {
                if (node instanceof FieldPane) {
                    FieldPane field = (FieldPane) node;
                    fields.add(field);
                    currentTotal += field.widthSpinner.getValue();
                }
            }
            int remaining = 12 - currentTotal;
            for (FieldPane field : fields) {
                int currentValue = field.widthSpinner.getValue();
                int allowedMax = currentValue + remaining;
                field.widthSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, allowedMax, currentValue));
            }
            addFieldButton.setDisable(remaining == 0);
        }

        private RowConfig buildRowConfig() {
            List<FieldConfig> fieldConfigs = new ArrayList<>();
            for (Node node : fieldContainer.getChildren()) {
                if (node instanceof FieldPane) {
                    fieldConfigs.add(((FieldPane) node).buildFieldConfig());
                }
            }
            return new RowConfig(fieldConfigs.toArray(new FieldConfig[0]));
        }

    }

    private class FieldPane extends VBox {
        private TextField fieldNameField;
        private TextField populateKeyField;
        private Spinner<Integer> widthSpinner;
        private ComboBox<String> fieldTypeComboBox;
        private ComboBox<String> customDropdownComboBox;
        private Button removeFieldButton;
        private Button moveLeftButton;
        private Button moveRightButton;

        FieldPane() {
            this(5);
        }

        FieldPane(int defaultWidth) {
            setSpacing(3);
            setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-padding: 10; -fx-background-color: rgba(0,0,0,0.04);");
            VBox fieldRow = new VBox(10);

            fieldNameField = new TextField();
            fieldNameField.setPromptText(localization.getLocalizedMessage("ReportWindows.FieldButton", "Field") + " " + localization.getLocalizedMessage("Login_Window.NamePromptText", "Name"));

            populateKeyField = new TextField();
            populateKeyField.setPromptText("Populate Key (optional)");

            widthSpinner = new Spinner<>(1, defaultWidth, defaultWidth);

            fieldTypeComboBox = new ComboBox<>();
            fieldTypeComboBox.getItems().add("NUMBER_FIELD");
            fieldTypeComboBox.getItems().addAll("TEXT_FIELD", "CUSTOM_DROPDOWN", "COMBO_BOX_STREET", "COMBO_BOX_AREA", "COUNTY_FIELD", "TEXT_AREA", "TIME_FIELD", "DATE_FIELD", "OFFICER_NAME", "OFFICER_RANK", "OFFICER_DIVISION", "OFFICER_AGENCY", "OFFICER_NUMBER", "OFFICER_CALLSIGN");
            fieldTypeComboBox.getSelectionModel().selectFirst();

            fieldTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if ("CUSTOM_DROPDOWN".equals(newVal)) {
                    if (customDropdownComboBox == null) {
                        customDropdownComboBox = new ComboBox<>();
                        List<String> customFields = getCustomizationFields();
                        customDropdownComboBox.getItems().setAll(customFields);
                        customDropdownComboBox.setPromptText(localization.getLocalizedMessage("ReportWindows.customField", "Select Custom Field"));
                        getChildren().add(customDropdownComboBox);
                    }
                } else {
                    if (customDropdownComboBox != null) {
                        getChildren().remove(customDropdownComboBox);
                        customDropdownComboBox = null;
                    }
                }
            });

            removeFieldButton = new Button("Remove Field");
            removeFieldButton.setMinWidth(USE_PREF_SIZE);
            removeFieldButton.setOnAction(event -> {
                HBox parent = (HBox) getParent();
                parent.getChildren().remove(this);
                RowPane row = (RowPane) parent.getParent();
                row.recalcWidthLimits();
            });
            removeFieldButton.getStyleClass().add("removeButton");

            moveLeftButton = new Button("←");
            moveRightButton = new Button("→");
            moveLeftButton.setMinWidth(USE_PREF_SIZE);
            moveRightButton.setMinWidth(USE_PREF_SIZE);
            moveLeftButton.getStyleClass().add("moveButton");
            moveRightButton.getStyleClass().add("moveButton");

            moveLeftButton.setFocusTraversable(false);
            moveRightButton.setFocusTraversable(false);
            removeFieldButton.setFocusTraversable(false);
            widthSpinner.setFocusTraversable(false);
            fieldTypeComboBox.setFocusTraversable(false);
            if (customDropdownComboBox != null) customDropdownComboBox.setFocusTraversable(false);

            HBox buttonBox = new HBox(5, moveLeftButton, moveRightButton, removeFieldButton);

            fieldRow.getChildren().addAll(new Label(localization.getLocalizedMessage("Login_Window.NamePromptText", "Name") + ": "), fieldNameField, new Label("Populate Key:"), populateKeyField, new Label(localization.getLocalizedMessage("ReportWindows.WidthLabel", "Width:")), widthSpinner, new Label(localization.getLocalizedMessage("ReportWindows.TypeLabel", "Type:")), fieldTypeComboBox, buttonBox);

            moveLeftButton.setOnAction(event -> moveField(-1));
            moveRightButton.setOnAction(event -> moveField(1));

            getChildren().add(fieldRow);
        }

        private void moveField(int direction) {
            HBox parentHBox = (HBox) getParent();
            if (parentHBox == null) return;

            int currentIndex = parentHBox.getChildren().indexOf(this);
            int newIndex = currentIndex + direction;

            if (newIndex >= 0 && newIndex < parentHBox.getChildren().size()) {
                parentHBox.getChildren().remove(currentIndex);
                parentHBox.getChildren().add(newIndex, this);
            }
        }

        private FieldConfig buildFieldConfig() {
            String name = fieldNameField.getText();
            String populateKey = populateKeyField.getText().isEmpty() ? null : populateKeyField.getText();
            int width = widthSpinner.getValue();
            FieldType type = FieldType.valueOf(fieldTypeComboBox.getValue());
            if (name == null) {
                name = "";
            }
            FieldConfig config = new FieldConfig(name, width, type);
            if ("CUSTOM_DROPDOWN".equals(fieldTypeComboBox.getValue()) && customDropdownComboBox != null) {
                String customField = customDropdownComboBox.getValue();
                if (customField != null && !customField.trim().isEmpty()) {
                    config.setDropdownType(customField);
                }
            }
            config.setNodeType(type.getValue());
            if (populateKey == null) {
                populateKey = "";
            }
            if (populateKey.length() > 0) {
                config.setPopulateKey(populateKey);
            }
            return config;
        }
    }

    private class TransferPane extends VBox {
        private TextField transferNameField;
        private VBox elementContainer;
        private Button addElementButton;

        private Button removeTransferButton;

        TransferPane() {
            setSpacing(5);
            setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-padding: 10; -fx-background-color: rgba(0,0,0,0.04);");
            HBox header = new HBox(10);
            transferNameField = new TextField();
            transferNameField.setPromptText(localization.getLocalizedMessage("ReportWindows.TransferButton", "Transfer") + " " + localization.getLocalizedMessage("Login_Window.NamePromptText", "Name"));
            removeTransferButton = new Button(localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove") + " " + localization.getLocalizedMessage("ReportWindows.TransferButton", "Transfer"));
            removeTransferButton.getStyleClass().add("removeButton");
            removeTransferButton.setOnAction(event -> transferContainer.getChildren().remove(this));
            header.getChildren().addAll(new Label("Transfer:"), transferNameField, removeTransferButton);
            elementContainer = new VBox(5);
            elementContainer.setStyle("-fx-background-color: rgba(0,0,0,0.05);");
            addElementButton = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add") + " " + localization.getLocalizedMessage("ReportWindows.ElementButton", "Element"));
            addElementButton.getStyleClass().add("addButton");
            addElementButton.setOnAction(event -> addElement());
            addElementButton.setFocusTraversable(false);
            removeTransferButton.setFocusTraversable(false);
            getChildren().addAll(header, elementContainer, addElementButton);
        }

        private void addElement() {
            ElementPane elementPane = new ElementPane();
            elementContainer.getChildren().add(elementPane);
        }
    }

    private class ElementPane extends HBox {
        private TextField elementNameField;
        private ComboBox<String> reportComboBox;

        private Button removeElementButton;

        ElementPane() {
            setSpacing(5);
            setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-border-radius: 7; -fx-background-radius: 7; -fx-padding: 10; -fx-background-color: rgba(0,0,0,0.02);");
            elementNameField = new TextField();
            elementNameField.setPromptText(localization.getLocalizedMessage("ReportWindows.ElementButton", "Element") + " " + localization.getLocalizedMessage("Login_Window.NamePromptText", "Name"));
            reportComboBox = new ComboBox<>();
            String dataFolderPath = getCustomDataLogsFolderPath();

            ArrayList<String> dbArray = new ArrayList<>();
            File dataFolder = new File(dataFolderPath);
            if (dataFolder.exists() && dataFolder.isDirectory()) {
                File[] files = dataFolder.listFiles((dir, filen) -> filen.endsWith(".db"));
                if (files != null && files.length != 0) {
                    log("LayoutBuilder; Found " + files.length + " Databases For Transfer List", LogUtils.Severity.INFO);
                    for (File dbFile : files) {
                        String fileNameWithoutExt = dbFile.getName().replaceFirst("[.][^.]+$", "");
                        String dbFilePath = dbFile.getAbsolutePath();
                        if (isValidDatabase(dbFilePath, dbFile.getName())) {
                            log("LayoutBuilder; [" + fileNameWithoutExt + "] Added To Transfer List", LogUtils.Severity.INFO);
                            dbArray.add(fileNameWithoutExt);
                        } else {
                            log("LayoutBuilder; Invalid database file: " + dbFilePath, LogUtils.Severity.WARN);
                        }
                    }
                }
            }

            reportComboBox.getItems().addAll(dbArray);
            removeElementButton = new Button(localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove") + " " + localization.getLocalizedMessage("ReportWindows.ElementButton", "Element"));
            removeElementButton.getStyleClass().add("removeButton");
            removeElementButton.setOnAction(event -> ((VBox) getParent()).getChildren().remove(this));
            Button moveUpButton = new Button("↑");
            Button moveDownButton = new Button("↓");
            moveUpButton.getStyleClass().add("moveButton");
            moveDownButton.getStyleClass().add("moveButton");

            moveUpButton.setFocusTraversable(false);
            moveDownButton.setFocusTraversable(false);
            removeElementButton.setFocusTraversable(false);
            reportComboBox.setFocusTraversable(false);

            moveUpButton.setOnAction(event -> {
                VBox parent = (VBox) getParent();
                int index = parent.getChildren().indexOf(this);
                if (index > 0) {
                    parent.getChildren().remove(index);
                    parent.getChildren().add(index - 1, this);
                }
            });

            moveDownButton.setOnAction(event -> {
                VBox parent = (VBox) getParent();
                int index = parent.getChildren().indexOf(this);
                if (index < parent.getChildren().size() - 1) {
                    parent.getChildren().remove(index);
                    parent.getChildren().add(index + 1, this);
                }
            });
            getChildren().addAll(new Label(localization.getLocalizedMessage("ReportWindows.ElementButton", "Element") + ":"), elementNameField, new Label(localization.getLocalizedMessage("ReportWindows.ReportLabel", "Report") + ":"), reportComboBox, removeElementButton, moveUpButton, moveDownButton);
        }

    }
}