package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.controlsfx.tools.Utils;

import java.io.IOException;
import java.util.*;

public class webviewtestapp extends Application {

    private static final Map<String, Map<String, String>> reportData = new HashMap<>();
    static String primaryColor = "#323c41";
    static String secondaryColor = "#263238"; //Darkest
    static String accentColor = "#505d62"; //Lightest

    static {
        try {
            primaryColor = ConfigReader.configRead("secondaryColor");
            secondaryColor = ConfigReader.configRead("mainColor");
            accentColor = ConfigReader.configRead("accentColor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Method to create a report window with specified rows
    public static Map<String, Object> createReportWindow(String reportName, SectionConfig... sectionConfigs) {
        // Create a grid pane to hold the form fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(5);

        Label warningLabel = new Label("Please fill out the form");
        warningLabel.setVisible(false);
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 13;");

        // Add column constraints
        for (int i = 0; i < 12; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100 / 12.0); // Each column takes 1/12 of the available width
            gridPane.getColumnConstraints().add(column);
        }

        // Add main header label
        Label mainHeaderLabel = new Label("New " + reportName);
        mainHeaderLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");
        mainHeaderLabel.setAlignment(Pos.CENTER); // Align the header text to the center
        GridPane.setColumnSpan(mainHeaderLabel, 12); // Span the header across all columns
        gridPane.add(mainHeaderLabel, 0, 0);

        // Add rows dynamically
        Map<String, Object> fieldsMap = new HashMap<>();
        int rowIndex = 1; // Start the row index after the main header

        // Loop through each section configuration
        for (SectionConfig sectionConfig : sectionConfigs) {
            Label sectionLabel = new Label(sectionConfig.getSectionTitle());

            // Customize the label appearance
            sectionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent; -fx-padding: 0px 40px;");
            sectionLabel.setFont(Font.font("Segoe UI Black"));

            // Add the label to the grid pane
            gridPane.add(sectionLabel, 0, rowIndex, 12, 1); // Assuming the section spans across all columns

            rowIndex++; // Increment rowIndex to start the next row below the section

            // Loop through each row configuration in the section
            for (RowConfig rowConfig : sectionConfig.getRowConfigs()) {
                // Add rows dynamically
                addRowToGridPane(gridPane, rowConfig, rowIndex, fieldsMap);
                rowIndex++;
            }

            // Add gap between sections
            rowIndex += 3; // Increase rowIndex to create a gap of two rows between sections
        }

        // Create a button to collect field values
        Button collectButton = new Button("Collect Values");
        collectButton.getStyleClass().add("incidentformButton");
        collectButton.setStyle("-fx-padding: 15;");
        collectButton.setStyle("-fx-background-color: " + primaryColor);
        collectButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                collectButton.setStyle("-fx-background-color: " + secondaryColor + ";");
            } else {
                collectButton.setStyle("-fx-background-color: " + primaryColor + ";");
            }
        });

        // Create a VBox to hold the grid pane and the button
        HBox buttonBox = new HBox(10, warningLabel, collectButton);
        buttonBox.setAlignment(Pos.BASELINE_RIGHT); // Align the button and label to the baseline
        VBox root = new VBox(10, mainHeaderLabel, gridPane, buttonBox);
        root.setAlignment(Pos.CENTER); // Align the VBox content to the center
        root.setPadding(new Insets(18));

        root.setStyle("-fx-background-color: " + accentColor + ";");
        root.setPadding(new Insets(18));

        collectButton.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : fieldsMap.keySet()) {
                Object field = fieldsMap.get(fieldName);
                String value = "";
                if (field instanceof TextField) {
                    value = ((TextField) field).getText();
                } else if (field instanceof TextArea) {
                    value = ((TextArea) field).getText();
                } else if (field instanceof ComboBox<?>) {
                    ComboBox<?> comboBox = (ComboBox<?>) field;
                    if (comboBox.getValue() != null) {
                        value = comboBox.getValue().toString();
                    }
                }
                if (value.isEmpty() || value.isBlank()) {
                    allFieldsFilled = false;
                    break;
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
                return; // Exit the action event handler
            }
            // Proceed with form submission
            Map<String, String> fieldValues = new HashMap<>();
            for (String fieldName : fieldsMap.keySet()) {
                Object field = fieldsMap.get(fieldName);
                String value = "";
                if (field instanceof TextField) {
                    value = ((TextField) field).getText();
                } else if (field instanceof TextArea) {
                    value = ((TextArea) field).getText();
                } else if (field instanceof ComboBox) {
                    value = ((ComboBox) field).getValue().toString();
                }
                fieldValues.put(fieldName, value);
            }
            reportData.put(reportName, fieldValues);
            // Example: Print the collected data
            System.out.println("Data collected for " + reportName + ": " + fieldValues);

            Window window = Utils.getWindow(root);

            // Close the window
            window.hide(); // or window.close() if you want to force close
        });

        // Create a scene and add the VBox to it
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formFields.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formTextArea.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formButton.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formComboBox.css").toExternalForm());

        // Set up the stage
        Stage stage = new Stage();

        stage.setScene(scene);
        stage.setTitle(reportName);
        stage.setMaxWidth(850);
        stage.initStyle(StageStyle.UTILITY);
        stage.show();
        stage.setMaxHeight(stage.getHeight());
        stage.setMinHeight(stage.getHeight() - 100);
        stage.setMinWidth(stage.getWidth() - 300);
        return fieldsMap; // Return the map of fields
    }

    // Method to add a row to the grid pane with specified field configurations
    private static void addRowToGridPane(GridPane gridPane, RowConfig rowConfig, int rowIndex, Map<String, Object> fieldsMap) {
        int totalSize = 0;
        int columnIndex = 0; // Track the current column index
        for (FieldConfig fieldConfig : rowConfig.getFieldConfigs()) {
            if (fieldConfig.getSize() <= 0 || totalSize + fieldConfig.getSize() > 12) {
                throw new IllegalArgumentException("Invalid field size configuration");
            }
            totalSize += fieldConfig.getSize();

            // Determine the field type
            switch (fieldConfig.getFieldType()) {
                case TEXT_FIELD:
                    // Add TextField
                    TextField textField = new TextField();
                    textField.getStyleClass().add("formField3");
                    textField.setStyle("-fx-background-color: " + primaryColor);
                    textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            textField.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            textField.setStyle("-fx-background-color: " + primaryColor + ";");
                        }
                    });
                    textField.textProperty().addListener((observable, oldValue, newValue) -> {
                        textField.setText(newValue.toUpperCase());
                    });


                    textField.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    textField.setPrefWidth(200); // Set a default width for the TextField
                    gridPane.add(textField, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), textField);
                    break;
                case TEXT_AREA:
                    // Add TextArea
                    TextArea textArea = new TextArea();
                    textArea.setWrapText(true);
                    textArea.getStyleClass().add("othertextarea");
                    textArea.setStyle("-fx-background-color: " + primaryColor);
                    textArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            textArea.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            textArea.setStyle("-fx-background-color: " + primaryColor + ";");
                        }
                    });
                    textArea.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    textArea.setPrefRowCount(5); // Set preferred rows for TextArea
                    textArea.setMaxWidth(Double.MAX_VALUE); // Set TextArea to occupy full width
                    gridPane.add(textArea, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), textArea);
                    break;
                case COMBO_BOX_COLOR:
                    // Add ComboBox
                    ComboBox<String> comboBox = new ComboBox<>();
                    comboBox.getStyleClass().add("comboboxnew");
                    comboBox.setStyle("-fx-background-color: " + primaryColor + ";");
                    comboBox.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            comboBox.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            comboBox.setStyle("-fx-background-color: " + primaryColor + ";");
                        }
                    });

                    // Add items to the ComboBox (replace the example items with your own)
                    comboBox.getItems().addAll("Option 1", "Option 2", "Option 3");

                    comboBox.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    comboBox.setMaxWidth(Double.MAX_VALUE); // Set TextArea to occupy full width
                    gridPane.add(comboBox, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), comboBox);
                    break;
            }
            columnIndex += fieldConfig.getSize(); // Increment column index based on field size
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        /*// Example usage
        Map<String, Object> reportFields = createReportWindow("Callout Report",
                new SectionConfig("Officer Information",
                        new RowConfig(new FieldConfig("officer name", 5, FieldType.TEXT_FIELD), new FieldConfig("officer rank", 5, FieldType.TEXT_FIELD), new FieldConfig("number", 2, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("division", 6, FieldType.TEXT_FIELD), new FieldConfig("agency", 6, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Location Information",
                        new RowConfig(new FieldConfig("county", 3, FieldType.TEXT_FIELD), new FieldConfig("area", 4, FieldType.TEXT_FIELD), new FieldConfig("street", 5, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Callout Information",
                        new RowConfig(new FieldConfig("date", 6, FieldType.TEXT_FIELD), new FieldConfig("time", 6, FieldType.TEXT_FIELD)),
                        new RowConfig(new FieldConfig("type", 4, FieldType.TEXT_FIELD), new FieldConfig("code", 4, FieldType.TEXT_FIELD), new FieldConfig("number", 4, FieldType.TEXT_FIELD))
                ),
                new SectionConfig("Callout Notes", new RowConfig(new FieldConfig("Notes", 12, FieldType.TEXT_AREA))
                )
        );

         //Access specific fields
         TextField nameField = (TextField) reportFields.get("Name");
         TextArea notesArea = (TextArea) reportFields.get("Notes");

         //Example: Set text of specific fields
         nameField.setText("John Doe");
         notesArea.setText("Enter your notes here...");*/
    }


    enum FieldType {
        TEXT_FIELD,
        TEXT_AREA,
        COMBO_BOX_COLOR
    }

    // Class to hold row configuration (array of field configurations)
    static class SectionConfig {
        private final String sectionTitle;
        private final List<RowConfig> rowConfigs;

        public SectionConfig(String sectionTitle, RowConfig... rowConfigs) {
            this.sectionTitle = sectionTitle;
            this.rowConfigs = Arrays.asList(rowConfigs);
        }

        public String getSectionTitle() {
            return sectionTitle;
        }

        public List<RowConfig> getRowConfigs() {
            return rowConfigs;
        }
    }

    static class RowConfig {
        private final List<FieldConfig> fieldConfigs;

        public RowConfig(FieldConfig... fieldConfigs) {
            this.fieldConfigs = Arrays.asList(fieldConfigs);
        }

        public List<FieldConfig> getFieldConfigs() {
            return fieldConfigs;
        }
    }

    // Class to hold field configuration (field name and size)
    static class FieldConfig {
        private final String fieldName;
        private final int size;
        private final FieldType fieldType; // Add field type

        public FieldConfig(String fieldName, int size, FieldType fieldType) {
            this.fieldName = fieldName;
            this.size = size;
            this.fieldType = fieldType; // Initialize field type
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
}