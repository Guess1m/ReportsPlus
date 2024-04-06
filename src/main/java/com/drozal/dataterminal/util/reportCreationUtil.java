package com.drozal.dataterminal.util;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.NotesViewController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.CitationsData;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import static com.drozal.dataterminal.util.controllerUtils.*;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;
import static com.drozal.dataterminal.util.treeViewUtils.*;

public class reportCreationUtil {

    private static final Map<String, Map<String, String>> reportData = new HashMap<>();
    static String primaryColor = "#323c41";
    static String secondaryColor = "#263238"; //Darkest
    static String accentColor = "#505d62"; //Lightest
    private static double xOffset = 0;
    private static double yOffset = 0;

    /*static {
        try {
            primaryColor = ConfigReader.configRead("secondaryColor");
            secondaryColor = ConfigReader.configRead("mainColor");
            accentColor = ConfigReader.configRead("accentColor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

    // Method to create a report window with specified rows
    public static Map<String, Object> createReportWindow(String reportName, SectionConfig... sectionConfigs) {
        // Create a BorderPane to hold the content
        BorderPane borderPane = new BorderPane();

        // Apply color overlay to make the image white
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1.0); // Set saturation to make it grey
        colorAdjust.setBrightness(-0.2); // Set brightness to make it darker

// Create a Label for the title bar
        Label titleLabel = new Label("Report Manager");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #999999;");
        titleLabel.setAlignment(Pos.CENTER); // Align the header text to the center
        AnchorPane.setLeftAnchor(titleLabel, (double) 0);
        AnchorPane.setRightAnchor(titleLabel, (double) 0);
        AnchorPane.setTopAnchor(titleLabel, (double) 0);
        AnchorPane.setBottomAnchor(titleLabel, (double) 0);
        titleLabel.setEffect(colorAdjust);
        titleLabel.setMouseTransparent(true);

// Create a pane for the title bar
        AnchorPane titleBar = new AnchorPane(titleLabel);
        titleBar.setMinHeight(30);
        titleBar.setStyle("-fx-background-color: #383838;");
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(mouseEvent -> {
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setX(mouseEvent.getScreenX() - xOffset);
            stage.setY(mouseEvent.getScreenY() - yOffset);
        });


// Load close image (white)
        Image closeImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/cross.png"));
        ImageView closeImageView = new ImageView(closeImage);
        closeImageView.setFitWidth(15); // Set width of the image
        closeImageView.setFitHeight(15); // Set height of the image
        AnchorPane.setRightAnchor(closeImageView, 15.0); // Adjust the padding from the right
        AnchorPane.setTopAnchor(closeImageView, 7.0); // Center vertically
        closeImageView.setEffect(colorAdjust);

// Load maximize image
        Image maximizeImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/maximize.png"));
        ImageView maximizeImageView = new ImageView(maximizeImage);
        maximizeImageView.setFitWidth(15); // Set width of the image
        maximizeImageView.setFitHeight(15); // Set height of the image
        AnchorPane.setRightAnchor(maximizeImageView, 42.5); // Adjust the padding from the right
        AnchorPane.setTopAnchor(maximizeImageView, 7.0); // Center vertically
        maximizeImageView.setEffect(colorAdjust);

// Load minimize image
        Image minimizeImage = new Image(Launcher.class.getResourceAsStream("/com/drozal/dataterminal/imgs/icons/minimize.png"));
        ImageView minimizeImageView = new ImageView(minimizeImage);
        minimizeImageView.setFitWidth(15); // Set width of the image
        minimizeImageView.setFitHeight(15); // Set height of the image
        AnchorPane.setRightAnchor(minimizeImageView, 70.0); // Adjust the padding from the right
        AnchorPane.setTopAnchor(minimizeImageView, 7.0); // Center vertically
        minimizeImageView.setEffect(colorAdjust);

        // Create transparent rectangles to overlay the image views
        Rectangle closeRect = new Rectangle(20, 20);
        Rectangle maximizeRect = new Rectangle(20, 20);
        Rectangle minimizeRect = new Rectangle(20, 20);

// Set the rectangles to be transparent
        closeRect.setFill(Color.TRANSPARENT);
        minimizeRect.setFill(Color.TRANSPARENT);
        maximizeRect.setFill(Color.TRANSPARENT);

// Set mouse event handlers on the rectangles
        closeRect.setOnMouseClicked(event -> {
            Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.close();
        });

        minimizeRect.setOnMouseClicked(event -> {
            Stage stage = (Stage) borderPane.getScene().getWindow();
            stage.setIconified(true);
        });
        maximizeRect.setOnMouseClicked(event -> {
            Stage stage = (Stage) borderPane.getScene().getWindow();
            windowUtils.toggleWindowedFullscreen(stage, 850, 750);
        });

// Position the rectangles over the image views
        AnchorPane.setRightAnchor(closeRect, 12.5);
        AnchorPane.setTopAnchor(closeRect, 6.3);
        AnchorPane.setRightAnchor(minimizeRect, 70.0);
        AnchorPane.setTopAnchor(minimizeRect, 6.3);
        AnchorPane.setRightAnchor(maximizeRect, 42.5);
        AnchorPane.setTopAnchor(maximizeRect, 6.3);

// Add images to title bar
        titleBar.getChildren().addAll(closeRect, maximizeRect, minimizeRect, closeImageView, maximizeImageView, minimizeImageView);
        closeRect.toFront();
        minimizeRect.toFront();
        maximizeRect.toFront();

// Set the title bar at the top
        borderPane.setTop(titleBar);
        // Create a GridPane to hold the form fields
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label warningLabel = new Label("Please fill out the form");
        warningLabel.setVisible(false);
        warningLabel.setStyle("-fx-text-fill: red; -fx-font-family: Arial; -fx-font-weight: bold; -fx-font-size: 14;");

        // Add column constraints
        for (int i = 0; i < 12; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(100 / 12.0); // Each column takes 1/12 of the available width
            gridPane.getColumnConstraints().add(column);
        }

        // Add main header label
        Label mainHeaderLabel = new Label("New " + reportName);
        mainHeaderLabel.setStyle("-fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-font-family: Segoe UI Black;");
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

            // Add larger gap after the TreeView section
            if (sectionConfig.getSectionTitle().equals("Citation Treeview")) {
                rowIndex += 5;
            } else {
                // Add regular gap for other sections
                rowIndex += 2;
            }
        }


        // Create a button to collect field values
        Button submitBtn = new Button("Collect Values");
        submitBtn.getStyleClass().add("incidentformButton");
        submitBtn.setStyle("-fx-padding: 15;");
        submitBtn.setStyle("-fx-background-color: " + primaryColor);
        submitBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                submitBtn.setStyle("-fx-background-color: " + secondaryColor + ";");
            } else {
                submitBtn.setStyle("-fx-background-color: " + primaryColor + ";");
            }
        });

        // Create a button to pull from notes
        Button pullNotesBtn = new Button("Pull From Notes");
        pullNotesBtn.getStyleClass().add("incidentformButton");
        pullNotesBtn.setStyle("-fx-padding: 15;");
        pullNotesBtn.setStyle("-fx-background-color: " + primaryColor);
        pullNotesBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                pullNotesBtn.setStyle("-fx-background-color: " + secondaryColor + ";");
            } else {
                pullNotesBtn.setStyle("-fx-background-color: " + primaryColor + ";");
            }
        });

        // Create a VBox to hold the grid pane and the button
        HBox buttonBox = new HBox(10, pullNotesBtn, warningLabel, submitBtn);
        buttonBox.setAlignment(Pos.BASELINE_RIGHT); // Align the button and label to the baseline
        VBox root = new VBox(10, mainHeaderLabel, gridPane, buttonBox);
        root.setAlignment(Pos.CENTER); // Align the VBox content to the center
        root.setStyle("-fx-background-color: " + accentColor + ";");
        Insets insets = new Insets(20, 25, 15, 25);
        root.setPadding(insets);


        // Wrap the BorderPane with a ScrollPane
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true); // Allow the ScrollPane to resize horizontally
        scrollPane.setFitToHeight(true); // Allow the ScrollPane to resize vertically
        borderPane.setCenter(scrollPane);

        // Add listener to toggle visibility of ScrollPane based on stage's size
        Stage stage = new Stage();

        // Return the ScrollPane containing the content
        Scene scene = new Scene(borderPane);
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formFields.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formTextArea.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formButton.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formComboBox.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/main/Logscrollpane.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/tableCss.css").toExternalForm());
        scrollPane.getStyleClass().add("formPane");
        scrollPane.setStyle("-fx-background-color: " + accentColor + "; " + "-fx-focus-color: " + accentColor + ";");

        stage.setScene(scene);
        stage.setTitle(reportName);

        // Get the primary screen
        Screen screen = Screen.getPrimary();
        // Get the visual bounds of the primary screen
        double screenHeight = screen.getVisualBounds().getHeight();
        double screenWidth = screen.getVisualBounds().getWidth();

        stage.setMaxWidth(screenWidth);
        stage.setMaxHeight(screenHeight);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setAlwaysOnTop(true);
        stage.show();
        ResizeHelper.addResizeListener(stage);
        stage.setHeight(750);
        stage.setWidth(850);
        stage.setMinHeight(stage.getHeight() - 300);
        stage.setMinWidth(stage.getWidth() - 300);
        stage.centerOnScreen();
        Map<String, Object> result = new HashMap<>();
        result.put(reportName + " Map", fieldsMap);
        result.put("pullNotesBtn", pullNotesBtn);
        result.put("warningLabel", warningLabel);
        result.put("submitBtn", submitBtn);
        result.put("root", borderPane);
        return result;
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
                        if (newValue != null) {
                            textField.setText(newValue.toUpperCase());
                        }
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

                    // Set the HGrow property to make only the TextArea resize with the window
                    GridPane.setHgrow(textArea, Priority.ALWAYS);
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
                    comboBox.getItems().addAll(dropdownInfo.carColors);

                    comboBox.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    comboBox.setButtonCell(new ListCell() {

                        @Override
                        protected void updateItem(Object item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                // styled like -fx-prompt-text-fill:
                                setStyle("-fx-text-fill: derive(-fx-control-inner-background,-40%)");
                            } else {
                                setStyle("-fx-text-fill: white;");
                                setText(item.toString());
                            }
                        }

                    });
                    comboBox.setMaxWidth(Double.MAX_VALUE); // Set TextArea to occupy full width
                    gridPane.add(comboBox, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), comboBox);
                    break;
                case CITATION_TREE_VIEW:
                    // Add TreeView
                    TreeView<String> treeView = new TreeView<>();
                    // Configure TreeView properties based on fieldConfig
                    treeView.setPrefHeight(350);
                    treeView.setMinHeight(350);
                    treeView.setMaxHeight(350);

                    //Tree View
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

                    // Add TextFields
                    TextField citationNameField = new TextField();
                    citationNameField.setPromptText("Citation Name");
                    TextField citationFineField = new TextField();
                    citationFineField.setPromptText("Citation Fine");

                    // Add Buttons
                    Button addButton = new Button("Add");
                    Button removeButton = new Button("Remove");

                    // Add Label
                    Label citationInfoLabel = new Label("Citation Information");
                    citationInfoLabel.setAlignment(Pos.CENTER); // Center align the label

                    // Add TableView
                    TableView<CitationsData> citationTableView = new TableView<>();
                    citationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    citationTableView.getStyleClass().add("calloutTABLE");

                    // Create a TableColumn and associate it with the "citation" property
                    TableColumn<CitationsData, String> citationColumn = new TableColumn<>("Citation");
                    citationColumn.setCellValueFactory(new PropertyValueFactory<>("citation"));
                    citationTableView.setTableMenuButtonVisible(false);

                    // Add the TableColumn to the TableView
                    citationTableView.getColumns().add(citationColumn);

                    // Add TreeView to gridPane
                    gridPane.add(treeView, columnIndex, rowIndex, fieldConfig.getSize(), 5); // Span 5 rows

                    // Calculate the column index for the additional components
                    int additionalColumnIndex = columnIndex + fieldConfig.getSize();

                    // Calculate the number of remaining columns in the GridPane
                    int remainingColumns = gridPane.getColumnCount() - additionalColumnIndex;

                    // Add Citation Information Label
                    gridPane.add(citationInfoLabel, additionalColumnIndex, rowIndex, remainingColumns, 1); // Span remaining columns
                    GridPane.setHalignment(citationInfoLabel, HPos.CENTER); // Center align the label horizontally
                    // Add Citation Name Field
                    gridPane.add(citationNameField, additionalColumnIndex, rowIndex + 1, remainingColumns, 1); // Span remaining columns
                    // Add Citation Fine Field
                    gridPane.add(citationFineField, additionalColumnIndex, rowIndex + 2, remainingColumns, 1); // Span remaining columns

                    // Create an HBox for the Add and Remove buttons with spacing of 20 pixels
                    HBox buttonBox = new HBox(40, addButton, removeButton);
                    buttonBox.setAlignment(Pos.CENTER); // Center align the buttons
                    // Add Add Button
                    gridPane.add(buttonBox, additionalColumnIndex, rowIndex + 3, remainingColumns, 1); // Span remaining columns

                    // Add Citation TableView
                    gridPane.add(citationTableView, additionalColumnIndex, rowIndex + 4, remainingColumns, 1); // Span remaining columns

                    // Put all fields into fieldsMap for access from outside the method
                    fieldsMap.put("CitationNameField", citationNameField);
                    fieldsMap.put("CitationFineField", citationFineField);
                    fieldsMap.put("AddButton", addButton);
                    fieldsMap.put("RemoveButton", removeButton);
                    fieldsMap.put("CitationTableView", citationTableView);
                    fieldsMap.put(fieldConfig.getFieldName(), treeView);

                    // Customize the label appearance
                    citationInfoLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent; -fx-padding: 0px 40px;");
                    citationInfoLabel.setFont(Font.font("Segoe UI Black"));
                    addButton.getStyleClass().add("incidentformButton");
                    addButton.setStyle("-fx-padding: 15;");
                    addButton.setStyle("-fx-background-color: " + primaryColor);
                    addButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            addButton.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            addButton.setStyle("-fx-background-color: " + primaryColor + ";");
                        }
                    });
                    removeButton.getStyleClass().add("incidentformButton");
                    removeButton.setStyle("-fx-padding: 15;");
                    removeButton.setStyle("-fx-background-color: " + primaryColor);
                    removeButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            removeButton.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            removeButton.setStyle("-fx-background-color: " + primaryColor + ";");
                        }
                    });
                    citationTableView.setStyle("-fx-background-color: " + primaryColor);
                    citationTableView.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            citationTableView.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            citationTableView.setStyle("-fx-background-color: " + primaryColor + ";");
                        }
                    });
                    citationNameField.getStyleClass().add("formField3");
                    citationNameField.setStyle("-fx-background-color: " + primaryColor);
                    citationNameField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            citationNameField.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            citationNameField.setStyle("-fx-background-color: " + primaryColor + ";");
                        }
                    });
                    citationFineField.getStyleClass().add("formField3");
                    citationFineField.setStyle("-fx-background-color: " + primaryColor);
                    citationFineField.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            citationFineField.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            citationFineField.setStyle("-fx-background-color: " + primaryColor + ";");
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
                            // Assuming citationTableView has a column with "citation" property
                            citationTableView.getItems().add(formData);
                        }
                    });
                    removeButton.setOnMouseClicked(event -> {
                        CitationsData selectedItem = citationTableView.getSelectionModel().getSelectedItem();
                        if (selectedItem != null) {
                            // Remove the selected item from the TableView
                            citationTableView.getItems().remove(selectedItem);
                        }
                    });
                    // Increment the row index for the subsequent section
                    rowIndex += 6; // Adjust this value according to your layout

                    break;

            }
            columnIndex += fieldConfig.getSize(); // Increment column index based on field size
        }
    }

    static Map<String, Object> calloutLayout() {
        Map<String, Object> calloutReport = createReportWindow("Callout Report",
                new reportCreationUtil.SectionConfig("Officer Information",
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("name", 5, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("rank", 5, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("number", 2, reportCreationUtil.FieldType.TEXT_FIELD)),
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("division", 6, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("agency", 6, reportCreationUtil.FieldType.TEXT_FIELD))
                ),
                new reportCreationUtil.SectionConfig("Location Information",
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("county", 3, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("area", 4, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("street", 5, reportCreationUtil.FieldType.TEXT_FIELD))
                ),
                new reportCreationUtil.SectionConfig("Callout Information",
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("date", 6, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("time", 6, reportCreationUtil.FieldType.TEXT_FIELD)),
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("type", 4, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("code", 4, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("calloutnumber", 4, reportCreationUtil.FieldType.TEXT_FIELD))
                ),
                new reportCreationUtil.SectionConfig("Callout Notes",
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("notes", 12, reportCreationUtil.FieldType.TEXT_AREA))
                )
        );
        return calloutReport;
    }

    public static void newCallout(BarChart<String, Number> reportChart, AreaChart areaReportChart, Object vbox, NotesViewController notesViewController) {
        Map<String, Object> calloutReport = calloutLayout();

        //Access calloutReportMap
        Map<String, Object> calloutReportMap = (Map<String, Object>) calloutReport.get("Callout Report Map");
        //Access specific fields
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

        //change action of pullnotesbutton
        Button pullNotesBtn = (Button) calloutReport.get("pullNotesBtn");
        // Change the action of the pullNotesBtn button
        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(calloutarea, notesViewController.getNotepadTextArea(), "-area");
                updateTextFromNotepad(calloutcounty, notesViewController.getNotepadTextArea(), "-county");
                updateTextFromNotepad(calloutstreet, notesViewController.getNotepadTextArea(), "-street");
                updateTextFromNotepad(calloutnum, notesViewController.getNotepadTextArea(), "-number");
                updateTextFromNotepad(calloutnotes, notesViewController.getNotepadTextArea(), "-notes");
            } else {
                System.out.println("NotesViewController Is Null");
            }
        });

        //change action of pullnotesbutton
        Button submitBtn = (Button) calloutReport.get("submitBtn");
        // Change the action of the pullNotesBtn button
        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : calloutReportMap.keySet()) {
                Object field = calloutReportMap.get(fieldName);
                // Check if the field is a ComboBox
                if (field instanceof ComboBox<?>) {
                    ComboBox<?> comboBox = (ComboBox<?>) field;
                    if (comboBox.getValue() == null || comboBox.getValue().toString().trim().isEmpty()) {
                        allFieldsFilled = false;
                        break;
                    }
                }
            }
            if (!allFieldsFilled) {
                // Display warning message
                warningLabel.setVisible(true);
                // Hide the warning label after 3 seconds
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        warningLabel.setVisible(false);
                    }
                }, 3000);
                return; // Exit the action event handler
            }
            List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();

            // Add new entry
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
            // Save logs to XML
            CalloutReportLogs.saveLogsToXML(logs);
            updateChartIfMismatch(reportChart);
            controllerUtils.refreshChart(areaReportChart, "area");
            showNotification("Reports", "A new Callout Report has been submitted.", vbox);

            Stage rootstage = (Stage) root.getScene().getWindow();
            rootstage.close();
        });
    }

    public enum FieldType {
        TEXT_FIELD,
        TEXT_AREA,
        COMBO_BOX_COLOR,
        CITATION_TREE_VIEW
    }

    // Class to hold row configuration (array of field configurations)
    public static class SectionConfig {
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

    public static class RowConfig {
        private final List<FieldConfig> fieldConfigs;

        public RowConfig(FieldConfig... fieldConfigs) {
            this.fieldConfigs = Arrays.asList(fieldConfigs);
        }

        public List<FieldConfig> getFieldConfigs() {
            return fieldConfigs;
        }
    }

    // Class to hold field configuration (field name and size)
    public static class FieldConfig {
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
