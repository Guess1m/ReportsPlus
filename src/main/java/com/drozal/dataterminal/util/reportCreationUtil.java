package com.drozal.dataterminal.util;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.NotesViewController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.CitationsData;
import com.drozal.dataterminal.logs.Impound.ImpoundLogEntry;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.logs.Patrol.PatrolLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationLogEntry;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReportLogs;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import static com.drozal.dataterminal.util.windowUtils.*;

public class reportCreationUtil {

    private static final Map<String, Map<String, String>> reportData = new HashMap<>();
    static String primaryColor = "#323c41";
    static String secondaryColor = "#263238"; //Darkest
    static String accentColor = "#505d62"; //Lightest

    static double windowX = 0;
    static double windowY = 0;

    /*
    Pull from Config

    static {
        try {
            primaryColor = ConfigReader.configRead("secondaryColor");
            secondaryColor = ConfigReader.configRead("mainColor");
            accentColor = ConfigReader.configRead("accentColor");
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

        AnchorPane.setRightAnchor(closeRect, 12.5);
        AnchorPane.setTopAnchor(closeRect, 6.3);
        AnchorPane.setRightAnchor(minimizeRect, 70.0);
        AnchorPane.setTopAnchor(minimizeRect, 6.3);
        AnchorPane.setRightAnchor(maximizeRect, 42.5);
        AnchorPane.setTopAnchor(maximizeRect, 6.3);

        titleBar.getChildren().addAll(placeholderImageView, closeRect, maximizeRect, minimizeRect, closeImageView, maximizeImageView, minimizeImageView);
        closeRect.toFront();
        minimizeRect.toFront();
        maximizeRect.toFront();
        return titleBar;
    }


    public static Map<String, Object> createReportWindow(String reportName, int numWidthUnits, int numHeightUnits, TransferConfig transferConfig, SectionConfig... sectionConfigs) {
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        double preferredWidth = screenWidth / 12 * numWidthUnits;
        double preferredHeight = screenHeight / 12 * numHeightUnits;

        BorderPane borderPane = new BorderPane();
        borderPane.setStyle("-fx-border-color: black");

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
        mainHeaderLabel.setStyle("-fx-font-size: 29px; -fx-font-weight: bold; -fx-text-fill: #ffffff; -fx-font-family: Segoe UI Black;");
        mainHeaderLabel.setAlignment(Pos.CENTER);
        GridPane.setColumnSpan(mainHeaderLabel, 12);
        gridPane.add(mainHeaderLabel, 0, 0);

        Map<String, Object> fieldsMap = new HashMap<>();
        int rowIndex = 1;

        for (SectionConfig sectionConfig : sectionConfigs) {
            Label sectionLabel = new Label(sectionConfig.getSectionTitle());

            if (sectionConfig.getRequired()) {
                sectionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent; -fx-padding: 0px 40px;");
            } else {
                sectionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #a6a6a6; -fx-background-color: transparent; -fx-padding: 0px 40px;");
            }
            sectionLabel.setFont(Font.font("Segoe UI Black"));

            gridPane.add(sectionLabel, 0, rowIndex, 12, 1);

            rowIndex++;

            for (RowConfig rowConfig : sectionConfig.getRowConfigs()) {
                addRowToGridPane(gridPane, rowConfig, rowIndex, fieldsMap);
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
        submitBtn.setStyle("-fx-background-color: " + primaryColor);
        submitBtn.hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                submitBtn.setStyle("-fx-background-color: " + secondaryColor + ";");
            } else {
                submitBtn.setStyle("-fx-background-color: " + primaryColor + ";");
            }
        });

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

            paneGrid.setStyle("-fx-background-color: " + secondaryColor + "; -fx-border-color: " + secondaryColor + ";");
            accordion.setStyle("-fx-background-color: " + secondaryColor + "; -fx-border-color: " + secondaryColor + ";");
            titledPane.setStyle("-fx-background-color: " + secondaryColor + "; -fx-border-color: " + secondaryColor + ";");

            root.getChildren().addAll(spacerPane1, titledPane, spacerPane2);
        }

        root.getChildren().add(buttonBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + accentColor + ";");
        Insets insets = new Insets(20, 25, 15, 25);
        root.setPadding(insets);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        borderPane.setCenter(scrollPane);

        Stage stage = new Stage();

        BorderlessScene scene = new BorderlessScene(stage, StageStyle.TRANSPARENT, borderPane, Color.TRANSPARENT);
        scene.setMoveControl(titleBar);
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formFields.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formTextArea.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formButton.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formComboBox.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/main/Logscrollpane.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/tableCss.css").toExternalForm());
        scene.getStylesheets().add(Launcher.class.getResource("/com/drozal/dataterminal/css/form/formTitledPane.css").toExternalForm());
        scrollPane.getStyleClass().add("formPane");
        scrollPane.setStyle("-fx-background-color: " + accentColor + "; " + "-fx-focus-color: " + accentColor + ";");

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
        return result;
    }


    private static void addRowToGridPane(GridPane gridPane, RowConfig rowConfig, int rowIndex, Map<String, Object> fieldsMap) {
        int totalSize = 0;
        int columnIndex = 0;
        for (FieldConfig fieldConfig : rowConfig.getFieldConfigs()) {
            if (fieldConfig.getSize() <= 0 || totalSize + fieldConfig.getSize() > 12) {
                throw new IllegalArgumentException("Invalid field size configuration");
            }
            totalSize += fieldConfig.getSize();

            switch (fieldConfig.getFieldType()) {
                case TEXT_FIELD:
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
                    textField.setPrefWidth(200);
                    gridPane.add(textField, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), textField);
                    break;
                case TEXT_AREA:
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
                    textArea.setPrefRowCount(5);
                    textArea.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(textArea, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), textArea);

                    GridPane.setHgrow(textArea, Priority.ALWAYS);
                    break;
                case COMBO_BOX_COLOR:
                    ComboBox<String> comboBoxColor = new ComboBox<>();
                    comboBoxColor.getStyleClass().add("comboboxnew");
                    comboBoxColor.setStyle("-fx-background-color: " + primaryColor + ";");
                    comboBoxColor.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            comboBoxColor.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            comboBoxColor.setStyle("-fx-background-color: " + primaryColor + ";");
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
                                setStyle("-fx-text-fill: white;");
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
                    comboBoxType.setStyle("-fx-background-color: " + primaryColor + ";");
                    comboBoxType.focusedProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            comboBoxType.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            comboBoxType.setStyle("-fx-background-color: " + primaryColor + ";");
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
                                setStyle("-fx-text-fill: white;");
                                setText(item.toString());
                            }
                        }

                    });
                    comboBoxType.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(comboBoxType, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), comboBoxType);
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
                    transferButton.setStyle("-fx-background-color: " + accentColor);
                    transferButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            transferButton.setStyle("-fx-background-color: " + primaryColor + ";");
                        } else {
                            transferButton.setStyle("-fx-background-color: " + accentColor + ";");
                        }
                    });
                    gridPane.add(transferButton, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), transferButton);
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
                System.out.println("NotesViewController Is Null");
            }
        });

        Button submitBtn = (Button) calloutReport.get("submitBtn");
        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : calloutReportMap.keySet()) {
                Object field = calloutReportMap.get(fieldName);
                if (field instanceof ComboBox<?>) {
                    ComboBox<?> comboBox = (ComboBox<?>) field;
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
                System.out.println("NotesViewController Is Null");
            }
        });

        Button submitBtn = (Button) impoundReport.get("submitBtn");
        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : impoundReportMap.keySet()) {
                Object field = impoundReportMap.get(fieldName);
                if (field instanceof ComboBox<?>) {
                    ComboBox<?> comboBox = (ComboBox<?>) field;
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
        stoptime.setText(getDate());

        Button pullNotesBtn = (Button) patrolReport.get("pullNotesBtn");

        pullNotesBtn.setOnAction(event -> {
            if (notesViewController != null) {
                updateTextFromNotepad(patrolnum, notesViewController.getNotepadTextArea(), "-number");
                updateTextFromNotepad(notes, notesViewController.getNotepadTextArea(), "-comments");
            } else {
                System.out.println("NotesViewController Is Null");
            }
        });

        Button submitBtn = (Button) patrolReport.get("submitBtn");

        submitBtn.setOnAction(event -> {
            boolean allFieldsFilled = true;
            for (String fieldName : patrolReportMap.keySet()) {
                Object field = patrolReportMap.get(fieldName);
                if (field instanceof ComboBox<?>) {
                    ComboBox<?> comboBox = (ComboBox<?>) field;
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
                System.out.println("NotesViewController Is Null");
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
                    System.out.println("NotesViewController Is Null");
                }
            });

            Button submitBtnimp = (Button) impoundReport.get("submitBtn");
            submitBtnimp.setOnAction(event2 -> {
                boolean allFieldsFilled = true;
                for (String fieldName : impoundReportMap.keySet()) {
                    Object field = impoundReportMap.get(fieldName);
                    if (field instanceof ComboBox<?>) {
                        ComboBox<?> comboBox = (ComboBox<?>) field;
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
                if (field instanceof ComboBox<?>) {
                    ComboBox<?> comboBox = (ComboBox<?>) field;
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
                updateChartIfMismatch(reportChart);
                controllerUtils.refreshChart(areaReportChart, "area");
                showNotification("Reports", "A new Citation Report has been submitted.", vbox);
                stage.close();
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
        CITATION_TREE_VIEW,
        TRANSFER_BUTTON;
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
