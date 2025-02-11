package com.Guess.ReportsPlus.util.Report;

import com.Guess.ReportsPlus.Launcher;
import com.Guess.ReportsPlus.config.ConfigReader;
import com.Guess.ReportsPlus.logs.ChargesData;
import com.Guess.ReportsPlus.logs.CitationsData;
import com.Guess.ReportsPlus.util.Misc.LogUtils;
import com.Guess.ReportsPlus.util.Strings.dropdownInfo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Guess.ReportsPlus.Desktop.Utils.WindowUtils.WindowManager.createCustomWindow;
import static com.Guess.ReportsPlus.Launcher.localization;
import static com.Guess.ReportsPlus.MainApplication.*;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.log;
import static com.Guess.ReportsPlus.util.Misc.LogUtils.logError;
import static com.Guess.ReportsPlus.util.Other.controllerUtils.getJarPath;
import static com.Guess.ReportsPlus.util.Report.nestedReportUtils.FieldType.*;
import static com.Guess.ReportsPlus.util.Report.treeViewUtils.findXMLValue;
import static com.Guess.ReportsPlus.util.Report.treeViewUtils.parseTreeXML;

//TODO: controlsfx searchable combobox

public class reportUtil {
    private static String getPrimaryColor() {
        String primaryColor;
        try {
            primaryColor = ConfigReader.configRead("reportSettings", "reportSecondary");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return primaryColor;
    }

    private static String getSecondaryColor() {
        String secondaryColor;
        try {
            secondaryColor = ConfigReader.configRead("reportSettings", "reportAccent");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return secondaryColor;
    }

    private static String getAccentColor() {
        String accentColor;
        try {
            accentColor = ConfigReader.configRead("reportSettings", "reportBackground");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return accentColor;
    }

    public static Map<String, Object> createReportWindow(String reportName, nestedReportUtils.TransferConfig transferConfig, nestedReportUtils.SectionConfig... sectionConfigs) {
        String placeholder;
        try {
            placeholder = ConfigReader.configRead("reportSettings", "reportHeading");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BorderPane mainRoot = new BorderPane();
        mainRoot.setStyle("-fx-border-color: black; -fx-border-width: 1.5;");

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label warningLabel = new Label("Please fill out the form");
        warningLabel.setVisible(false);
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-family: 'Inter 24pt Regular'; -fx-font-size: 14;");

        for (int i = 0; i < 12; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100 / 12.0);
            gridPane.getColumnConstraints().add(column);
        }

        Label mainHeaderLabel = new Label(
                localization.getLocalizedMessage("ReportWindows.NewLabel", "New") + " " + reportName);
        mainHeaderLabel.setStyle(
                "-fx-font-size: 29px; -fx-text-fill: " + placeholder + "; -fx-font-family: \"Inter 28pt Bold\";");

        Label statusLabel = new Label(
                localization.getLocalizedMessage("Callout_Manager.CalloutStatus", "Status:") + " ");
        statusLabel.setStyle(
                "-fx-font-size: 15.5px;-fx-text-fill: " + placeholder + "; -fx-font-family: \"Inter 28pt Medium\";");

        ComboBox<String> statusValue = new ComboBox<>();
        statusValue.getStyleClass().add("comboboxnew");
        statusValue.setStyle("-fx-background-color: " + getPrimaryColor() + "; -fx-font-size: 10;");
        statusValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                statusValue.setStyle("-fx-background-color: " + getSecondaryColor() + "; -fx-font-size: 10;");
            } else {
                statusValue.setStyle("-fx-background-color: " + getPrimaryColor() + "; -fx-font-size: 10;");
            }
        });
        statusValue.getItems().addAll("Closed", "In Progress", "Pending", "Reopened", "Cancelled");
        statusValue.setValue("Closed");
        statusValue.setPromptText("Report Status");
        statusValue.setButtonCell(new ListCell() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
                } else {
                    if (item.equals("Closed")) {
                        setStyle("-fx-text-fill: #FF8E69;");
                    } else if (item.equals("In Progress")) {
                        setStyle("-fx-text-fill: #79AFFF;");
                    } else if (item.equals("Reopened")) {
                        setStyle("-fx-text-fill: #B05EF3;");
                    } else if (item.equals("Pending")) {
                        setStyle("-fx-text-fill: #F3C95E;");
                    } else if (item.equals("Cancelled")) {
                        setStyle("-fx-text-fill: #F35645;");
                    } else {
                        setStyle("-fx-text-fill: " + placeholder + ";");
                    }
                    setText(item.toString());
                }
            }
        });
        statusValue.setMaxWidth(Region.USE_COMPUTED_SIZE);

        HBox statusBox = new HBox(statusLabel, statusValue);
        statusBox.setAlignment(Pos.CENTER_RIGHT);

        GridPane topHeaderGridPane = new GridPane();
        topHeaderGridPane.add(mainHeaderLabel, 0, 0);
        topHeaderGridPane.add(statusBox, 1, 0);
        GridPane.setHalignment(statusBox, HPos.RIGHT);
        GridPane.setHalignment(mainHeaderLabel, HPos.CENTER);
        GridPane.setColumnSpan(mainHeaderLabel, GridPane.REMAINING);
        ColumnConstraints col3 = new ColumnConstraints();
        col3.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        topHeaderGridPane.getColumnConstraints().addAll(col2, col3);

        Map<String, Object> fieldsMap = new HashMap<>();
        int rowIndex = 1;

        for (nestedReportUtils.SectionConfig sectionConfig : sectionConfigs) {

            Label sectionLabel = new Label(sectionConfig.getSectionTitle());
            sectionLabel.setStyle(
                    "-fx-font-size: 16px; -fx-text-fill: " + placeholder + "; -fx-font-family: 'Inter 28pt Bold'; -fx-background-color: transparent; -fx-padding: 0px 40px;");
            gridPane.add(sectionLabel, 0, rowIndex, 12, 1);
            rowIndex++;

            for (nestedReportUtils.RowConfig rowConfig : sectionConfig.getRowConfigs()) {
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

        Button submitBtn = new Button(
                localization.getLocalizedMessage("ReportWindows.SubmitReportButton", "Submit Report"));
        submitBtn.setMinWidth(Region.USE_PREF_SIZE);
        submitBtn.getStyleClass().add("incidentformButton");
        submitBtn.setStyle("-fx-background-color: " + getPrimaryColor());
        submitBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                submitBtn.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
            } else {
                submitBtn.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
            }
        });

        MenuButton pullNotesBtn = new MenuButton(
                localization.getLocalizedMessage("ReportWindows.PullFromNotesButton", "Pull From Notes"));
        pullNotesBtn.setMinWidth(Region.USE_PREF_SIZE);
        pullNotesBtn.getStyleClass().add("incidentformButton");
        pullNotesBtn.setStyle("-fx-background-color: " + getPrimaryColor() + "; -fx-padding: 5px 2px;");
        pullNotesBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                pullNotesBtn.setStyle("-fx-background-color: " + getSecondaryColor() + "; -fx-padding: 5px 2px;");
            } else {
                pullNotesBtn.setStyle("-fx-background-color: " + getPrimaryColor() + "; -fx-padding: 5px 2px;");
            }
        });

        Button delBtn = new Button(
                localization.getLocalizedMessage("ReportWindows.DeleteReportButton", "Delete Report"));
        delBtn.setVisible(false);
        delBtn.setDisable(true);
        delBtn.getStyleClass().add("incidentformButton");
        delBtn.setStyle("-fx-padding: 15; -fx-border-color:red; -fx-border-width: 1;");
        delBtn.setStyle("-fx-background-color: " + getPrimaryColor() + "; -fx-border-color:red; -fx-border-width: 1;");
        delBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                delBtn.setStyle(
                        "-fx-background-color: " + getSecondaryColor() + "; -fx-border-color:red; -fx-border-width: 1;");
            } else {
                delBtn.setStyle(
                        "-fx-background-color: " + getPrimaryColor() + "; -fx-border-color:red; -fx-border-width: 1;");
            }
        });

        HBox buttonBox = new HBox(10, delBtn, warningLabel, pullNotesBtn, submitBtn);
        buttonBox.setAlignment(Pos.BASELINE_RIGHT);
        VBox root = new VBox(10, topHeaderGridPane, gridPane);

        if (transferConfig != null) {
            TitledPane titledPane = new TitledPane();
            titledPane.setExpanded(false);
            titledPane.setText(transferConfig.getTitle());
            titledPane.getStyleClass().add("paneoptions");

            GridPane paneGrid = new GridPane();
            paneGrid.setHgap(10);
            paneGrid.setVgap(10);

            paneGrid.getColumnConstraints().clear();
            for (int i = 0; i < 12; i++) {
                ColumnConstraints columnConstraints = new ColumnConstraints();
                columnConstraints.setPercentWidth(100.0 / 12);
                paneGrid.getColumnConstraints().add(columnConstraints);
            }

            int rowIndex1 = 0;
            for (nestedReportUtils.RowConfig rowConfig : transferConfig.getRowConfigs()) {
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
            accordion.setMaxHeight(Region.USE_COMPUTED_SIZE);

            paneGrid.setStyle(
                    "-fx-background-color: " + getSecondaryColor() + "; -fx-border-color: " + getSecondaryColor() + ";");
            accordion.setStyle(
                    "-fx-background-color: " + getSecondaryColor() + "; -fx-border-color: " + getSecondaryColor() + ";");
            titledPane.setStyle(
                    "-fx-background-color: " + getSecondaryColor() + "; -fx-border-color: " + getSecondaryColor() + ";");

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
        mainRoot.setCenter(scrollPane);

        try {
            if (ConfigReader.configRead("reportSettings", "reportWindowDarkMode").equalsIgnoreCase("true")) {
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/light/formFields.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/light/formTextArea.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/light/formButton.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/light/formComboBox.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/light/Logscrollpane.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/light/tableCss.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/light/formTitledPane.css").toExternalForm());
            } else {
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/dark/formFields.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/dark/formTextArea.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/dark/formButton.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/dark/formComboBox.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/dark/Logscrollpane.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/dark/tableCss.css").toExternalForm());
                mainRoot.getStylesheets().add(Launcher.class.getResource(
                        "/com/Guess/ReportsPlus/css/form/dark/formTitledPane.css").toExternalForm());
            }
        } catch (IOException e) {
            logError("Could not add stylesheets to reports: ", e);
        }

        scrollPane.getStyleClass().add("formPane");
        scrollPane.setStyle(
                "-fx-background-color: " + getAccentColor() + "; " + "-fx-focus-color: " + getAccentColor() + ";");

        Map<String, Object> result = new HashMap<>();
        result.put(reportName + " Map", fieldsMap);

        result.put("delBtn", delBtn);
        result.put("pullNotesBtn", pullNotesBtn);
        result.put("statusValue", statusValue);
        result.put("warningLabel", warningLabel);
        result.put("submitBtn", submitBtn);
        result.put("root", mainRoot);

        BorderPane root1 = (BorderPane) result.get("root");
        StringProperty reportNameProperty = new SimpleStringProperty(reportName);
        root1.getProperties().put("reportName", reportNameProperty);

        mainRoot.setPrefHeight(570.0);
        mainRoot.setPrefWidth(780.0);

        createCustomWindow(mainDesktopControllerObj.getDesktopContainer(), mainRoot, reportName, true, 1, true, false,
                mainDesktopControllerObj.getTaskBarApps(), new Image(Objects.requireNonNull(
                        Launcher.class.getResourceAsStream("/com/Guess/ReportsPlus/imgs/icons/newReport.png"))));
        return result;
    }

    private static void addRowToGridPane(GridPane gridPane, nestedReportUtils.RowConfig rowConfig, int rowIndex, Map<String, Object> fieldsMap) {
        String placeholder;
        try {
            if (ConfigReader.configRead("reportSettings", "reportWindowDarkMode").equalsIgnoreCase("true")) {
                placeholder = "black";
            } else {
                placeholder = "white";
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int totalSize = 0;
        int columnIndex = 0;
        for (nestedReportUtils.FieldConfig fieldConfig : rowConfig.getFieldConfigs()) {
            if (fieldConfig.getSize() <= 0 || totalSize + fieldConfig.getSize() > 12) {
                log(fieldConfig.getFieldName(), LogUtils.Severity.ERROR);
                log(String.valueOf(fieldConfig.getSize()), LogUtils.Severity.ERROR);
                throw new IllegalArgumentException("Invalid field size configuration");
            }
            totalSize += fieldConfig.getSize();
            switch (fieldConfig.getFieldType()) {
                case OFFICER_NAME:
                case OFFICER_RANK:
                case OFFICER_AGENCY:
                case OFFICER_NUMBER:
                case OFFICER_CALLSIGN:
                case OFFICER_DIVISION:
                case NUMBER_FIELD:
                case DATE_FIELD:
                case TIME_FIELD:
                case TEXT_FIELD:
                case COUNTY_FIELD:
                    TextField textField = new TextField();
                    textField.getStyleClass().add("formField3");
                    textField.setStyle("-fx-background-color: " + getPrimaryColor());

                    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        textField.setStyle(
                                "-fx-background-color: " + (newValue ? getSecondaryColor() : getPrimaryColor()) + ";");
                    });

                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue != null && !newValue.equals(newValue.toUpperCase())) {
                            textField.setText(newValue.toUpperCase());
                        }
                    });

                    textField.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    textField.setPrefWidth(200);
                    gridPane.add(textField, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), textField);

                    try {
                        if (fieldConfig.getFieldType().equals(OFFICER_NAME)) {
                            textField.setText(ConfigReader.configRead("userInfo", "Name"));
                        } else if (fieldConfig.getFieldType().equals(OFFICER_RANK)) {
                            textField.setText(ConfigReader.configRead("userInfo", "Rank"));
                        } else if (fieldConfig.getFieldType().equals(OFFICER_DIVISION)) {
                            textField.setText(ConfigReader.configRead("userInfo", "Division"));
                        } else if (fieldConfig.getFieldType().equals(OFFICER_AGENCY)) {
                            textField.setText(ConfigReader.configRead("userInfo", "Agency"));
                        } else if (fieldConfig.getFieldType().equals(OFFICER_NUMBER)) {
                            textField.setText(ConfigReader.configRead("userInfo", "Number"));
                        } else if (fieldConfig.getFieldType().equals(OFFICER_CALLSIGN)) {
                            textField.setText(ConfigReader.configRead("userInfo", "Callsign"));
                        } else if (fieldConfig.getFieldType().equals(NUMBER_FIELD)) {
                            textField.setText(generateReportNumber());
                        } else if (fieldConfig.getFieldType().equals(DATE_FIELD)) {
                            textField.setText(getDate());
                        } else if (fieldConfig.getFieldType().equals(TIME_FIELD)) {
                            textField.setText(getTime(false));
                        }
                    } catch (Exception e) {
                        logError("Could not set text from config for fieldName [" + fieldConfig.getFieldName() + "]: ",
                                e);
                    }
                    break;
                case CUSTOM_DROPDOWN:
                case COMBO_BOX_SEARCH_METHOD:
                case COMBO_BOX_COLOR:
                case COMBO_BOX_SEARCH_TYPE:
                case COMBO_BOX_TYPE:
                    ComboBox<String> box = new ComboBox<>();
                    box.getStyleClass().add("comboboxnew");
                    box.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                    box.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            box.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            box.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    box.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    box.setButtonCell(new ListCell() {
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
                    box.setMaxWidth(Double.MAX_VALUE);

                    if (fieldConfig.getFieldType().equals(COMBO_BOX_SEARCH_METHOD)) {
                        box.getItems().addAll(dropdownInfo.searchMethods);
                    } else if (fieldConfig.getFieldType().equals(COMBO_BOX_COLOR)) {
                        box.getItems().addAll(dropdownInfo.carColors);
                    } else if (fieldConfig.getFieldType().equals(COMBO_BOX_SEARCH_TYPE)) {
                        box.getItems().addAll(dropdownInfo.searchTypes);
                    } else if (fieldConfig.getFieldType().equals(COMBO_BOX_TYPE)) {
                        box.getItems().addAll(dropdownInfo.vehicleTypes);
                    }

                    gridPane.add(box, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), box);
                    break;
                case COMBO_BOX_AREA:
                case COMBO_BOX_STREET:
                    ComboBox<String> comboBox = new ComboBox<>();
                    new AutoCompleteComboBoxListener<>(comboBox);
                    comboBox.getStyleClass().add("comboboxnew");
                    comboBox.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                    comboBox.getEditor().setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                    comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            comboBox.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            comboBox.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            comboBox.getEditor().setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            comboBox.getEditor().setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    comboBox.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    comboBox.setButtonCell(new ListCell() {
                        @Override
                        protected void updateItem(Object item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
                            } else {
                                setStyle("-fx-text-fill: " + placeholder + ";");
                                comboBox.getEditor().setStyle("-fx-text-fill: " + placeholder + ";");
                                setText(item.toString());
                            }
                        }
                    });
                    comboBox.setMaxWidth(Double.MAX_VALUE);

                    if (fieldConfig.getFieldType().equals(COMBO_BOX_AREA)) {
                        comboBox.getItems().addAll(dropdownInfo.areaList);
                    } else if (fieldConfig.getFieldType().equals(COMBO_BOX_STREET)) {
                        comboBox.getItems().addAll(dropdownInfo.streetList);
                    }

                    gridPane.add(comboBox, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), comboBox);
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
                    textArea.setMinHeight(150);
                    textArea.setPrefHeight(150);
                    textArea.setMaxHeight(Region.USE_COMPUTED_SIZE);
                    gridPane.add(textArea, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), textArea);

                    GridPane.setHgrow(textArea, Priority.ALWAYS);
                    GridPane.setVgrow(textArea, Priority.ALWAYS);
                    break;
                case CITATION_TREE_VIEW:
                    TreeView<String> treeView = new TreeView<>();
                    treeView.setPrefHeight(350);
                    treeView.setMinHeight(350);
                    treeView.setMaxHeight(350);

                    TextField searchCitationField = new TextField();
                    searchCitationField.setPromptText(
                            localization.getLocalizedMessage("ReportWindows.SearchCitationPrompt", "Search Citation"));
                    searchCitationField.setStyle("-fx-background-color: " + getPrimaryColor());
                    searchCitationField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            searchCitationField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            searchCitationField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    searchCitationField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.isBlank()) {
                            searchTreeAndExpand(treeView.getRoot(), newValue.toLowerCase(), treeView);
                        }
                    });
                    searchCitationField.getStyleClass().add("formField3");
                    searchCitationField.setStyle("-fx-background-color: " + getPrimaryColor());
                    searchCitationField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            searchCitationField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            searchCitationField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });

                    gridPane.add(searchCitationField, columnIndex, rowIndex, fieldConfig.getSize(), 1);

                    rowIndex += 1;

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
                    expandTreeItem(rootItem);

                    TextField citationNameField = new TextField();
                    citationNameField.setPromptText(
                            localization.getLocalizedMessage("ReportWindows.CitationNamePrompt", "Citation Name"));
                    TextField citationFineField = new TextField();
                    citationFineField.setPromptText(
                            localization.getLocalizedMessage("ReportWindows.CitationMaxFinePrompt",
                                    "Citation Maximum Fine"));

                    Button addButton = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add"));
                    Button removeButton = new Button(
                            localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove"));

                    Label citationInfoLabel = new Label(
                            localization.getLocalizedMessage("ReportWindows.CitationInformationHeader",
                                    "Citation Information"));
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

                    gridPane.add(citationInfoLabel, additionalColumnIndex, rowIndex - 2, remainingColumns, 1);
                    GridPane.setHalignment(citationInfoLabel, HPos.CENTER);
                    gridPane.add(citationNameField, additionalColumnIndex, rowIndex - 1, remainingColumns, 1);
                    gridPane.add(citationFineField, additionalColumnIndex, rowIndex + 1, remainingColumns, 1);

                    HBox buttonBox = new HBox(40, addButton, removeButton);
                    buttonBox.setAlignment(Pos.CENTER);
                    gridPane.add(buttonBox, additionalColumnIndex, rowIndex + 3, remainingColumns, 1);

                    gridPane.add(citationTableView, additionalColumnIndex, rowIndex + 4, remainingColumns, 1);

                    ComboBox<String> citationTypeDropdown = new ComboBox<>();
                    citationTypeDropdown.getStyleClass().add("comboboxnew");
                    citationTypeDropdown.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                    citationTypeDropdown.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            citationTypeDropdown.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            citationTypeDropdown.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    citationTypeDropdown.getItems().addAll(
                            localization.getLocalizedMessage("ReportWindows.CitationTypeNonPrinted", "Non-Printed"),
                            localization.getLocalizedMessage("ReportWindows.CitationTypePrinted", "Printed Citation"),
                            localization.getLocalizedMessage("ReportWindows.CitationTypeParking", "Parking Citation"));
                    citationTypeDropdown.setPromptText(
                            localization.getLocalizedMessage("ReportWindows.CitationTypePrompt", "Citation Type"));
                    citationTypeDropdown.setButtonCell(new ListCell() {
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
                    citationTypeDropdown.setMaxWidth(Double.MAX_VALUE);

                    gridPane.add(citationTypeDropdown, additionalColumnIndex, rowIndex + 5, remainingColumns, 1);

                    fieldsMap.put("CitationNameField", citationNameField);
                    fieldsMap.put("CitationFineField", citationFineField);
                    fieldsMap.put("AddButton", addButton);
                    fieldsMap.put("RemoveButton", removeButton);
                    fieldsMap.put("CitationType", citationTypeDropdown);
                    fieldsMap.put("CitationTableView", citationTableView);
                    fieldsMap.put(fieldConfig.getFieldName(), treeView);

                    citationInfoLabel.setStyle(
                            "-fx-font-size: 17px; -fx-text-fill: " + placeholder + ";-fx-font-family: 'Inter 28pt Bold'; -fx-background-color: transparent; -fx-padding: 0px 40px;");
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
                            citationFineField.setText(
                                    findXMLValue(selectedItem.getValue(), "fine", "data/Citations.xml"));
                        } else {
                            citationNameField.setText("");
                            citationFineField.setText("");
                        }
                    });
                    addButton.setOnMouseClicked(event -> {
                        String citation = citationNameField.getText();
                        if (!(citation.isBlank() || citation.isEmpty())) {
                            CitationsData formData = null;

                            String fine = findXMLValue(citation, "fine", "data/Citations.xml");
                            if (fine != null) {
                                formData = new CitationsData(citation);
                            } else {
                                log("Added Ciation via Custom Citation Value: " + citation + " fine: " + citationFineField.getText(),
                                        LogUtils.Severity.DEBUG);
                                formData = new CitationsData(citation + " MaxFine:" + citationFineField.getText());
                            }

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

                    TextField searchChargeField = new TextField();
                    searchChargeField.setPromptText(
                            localization.getLocalizedMessage("ReportWindows.SearchChargePrompt", "Search Charge"));
                    searchChargeField.setStyle("-fx-background-color: " + getPrimaryColor());
                    searchChargeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            searchChargeField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            searchChargeField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });
                    searchChargeField.textProperty().addListener((observable, oldValue, newValue) -> {
                        if (!newValue.isBlank()) {
                            searchTreeAndExpand(chargestreeView.getRoot(), newValue.toLowerCase(), chargestreeView);
                        }
                    });
                    searchChargeField.getStyleClass().add("formField3");
                    searchChargeField.setStyle("-fx-background-color: " + getPrimaryColor());
                    searchChargeField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            searchChargeField.setStyle("-fx-background-color: " + getSecondaryColor() + ";");
                        } else {
                            searchChargeField.setStyle("-fx-background-color: " + getPrimaryColor() + ";");
                        }
                    });

                    gridPane.add(searchChargeField, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    gridPane.add(chargestreeView, columnIndex, rowIndex + 1, fieldConfig.getSize(), 5);

                    rowIndex += 1;

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
                    expandTreeItem(rootItem2);

                    TextField chargeNameField = new TextField();
                    chargeNameField.setEditable(false);
                    chargeNameField.setPromptText(
                            localization.getLocalizedMessage("ReportWindows.ChargeNamePrompt", "Charge Name"));

                    Button addButton2 = new Button(localization.getLocalizedMessage("ReportWindows.AddButton", "Add"));
                    Button removeButton2 = new Button(
                            localization.getLocalizedMessage("ReportWindows.RemoveButton", "Remove"));

                    Label chargeInfoLabel = new Label(
                            localization.getLocalizedMessage("ReportWindows.ChargeInformationHeader",
                                    "Charge Information"));
                    chargeInfoLabel.setAlignment(Pos.CENTER);

                    TableView<ChargesData> chargeTableView = new TableView<>();
                    chargeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    chargeTableView.getStyleClass().add("calloutTABLE");

                    TableColumn<ChargesData, String> chargeColumn = new TableColumn<>(
                            localization.getLocalizedMessage("ReportWindows.chargeColumn", "Charge"));
                    chargeColumn.setCellValueFactory(new PropertyValueFactory<>("charge"));
                    chargeTableView.setTableMenuButtonVisible(false);

                    chargeTableView.getColumns().add(chargeColumn);

                    int additionalColumnIndex2 = columnIndex + fieldConfig.getSize();

                    int remainingColumns2 = gridPane.getColumnCount() - additionalColumnIndex2;

                    gridPane.add(chargeInfoLabel, additionalColumnIndex2, rowIndex - 2, remainingColumns2, 1);
                    GridPane.setHalignment(chargeInfoLabel, HPos.CENTER);
                    gridPane.add(chargeNameField, additionalColumnIndex2, rowIndex - 1, remainingColumns2, 1);

                    HBox buttonBox2 = new HBox(40, addButton2, removeButton2);
                    buttonBox2.setAlignment(Pos.CENTER);
                    gridPane.add(buttonBox2, additionalColumnIndex2, rowIndex + 2, remainingColumns2, 1);

                    gridPane.add(chargeTableView, additionalColumnIndex2, rowIndex + 4, remainingColumns2, 1);

                    fieldsMap.put("ChargeNameField", chargeNameField);
                    fieldsMap.put("AddButton", addButton2);
                    fieldsMap.put("RemoveButton", removeButton2);
                    fieldsMap.put("ChargeTableView", chargeTableView);
                    fieldsMap.put(fieldConfig.getFieldName(), chargestreeView);

                    chargeInfoLabel.setStyle(
                            "-fx-font-size: 17px; -fx-text-fill: " + placeholder + ";-fx-font-family: 'Inter 28pt Bold'; -fx-background-color: transparent; -fx-padding: 0px 40px;");
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
                default:
                    log("Unknown field type: " + fieldConfig.getFieldType(), LogUtils.Severity.ERROR);
                    break;

            }
            columnIndex += fieldConfig.getSize();

        }
    }

    private static void collapseTree(TreeItem<String> node) {
        for (TreeItem<String> child : node.getChildren()) {
            collapseTree(child);
        }
        node.setExpanded(false);
    }

    private static void expandTreeItem(TreeItem<String> item) {
        TreeItem<String> parent = item.getParent();
        while (parent != null) {
            parent.setExpanded(true);
            parent = parent.getParent();
        }
        item.setExpanded(true);
    }

    private static void searchTreeAndExpand(TreeItem<String> root, String query, TreeView chargestreeView) {
        chargestreeView.getSelectionModel().clearSelection();

        collapseTree(root);

        if (searchAndSelect(root, query, chargestreeView)) {
            chargestreeView.scrollTo(chargestreeView.getSelectionModel().getSelectedIndex());
        }
    }

    private static boolean searchAndSelect(TreeItem<String> node, String query, TreeView chargestreeView) {
        if (node.getValue().toLowerCase().contains(query)) {
            chargestreeView.getSelectionModel().select(node);
            expandTreeItem(node);
            return true;
        }

        for (TreeItem<String> child : node.getChildren()) {
            if (searchAndSelect(child, query, chargestreeView)) {
                return true;
            }
        }
        return false;
    }

    public static String extractMaxFine(String citation) {
        if (citation == null) {
            citation = "";
        }
        Pattern pattern = Pattern.compile("MaxFine:(\\S+)");
        Matcher matcher = pattern.matcher(citation);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    public static String generateReportNumber() {
        int num_length = 7;
        StringBuilder DeathReportNumber = new StringBuilder();
        for (int i = 0; i < num_length; i++) {
            SecureRandom RANDOM = new SecureRandom();
            int digit = RANDOM.nextInt(10);
            DeathReportNumber.append(digit);
        }
        return DeathReportNumber.toString();
    }
}
