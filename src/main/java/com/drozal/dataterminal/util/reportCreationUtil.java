package com.drozal.dataterminal.util;

import com.catwithawand.borderlessscenefx.scene.BorderlessScene;
import com.drozal.dataterminal.DataTerminalHomeApplication;
import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.NotesViewController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.CitationsData;
import com.drozal.dataterminal.logs.Patrol.PatrolLogEntry;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
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

    static {
        try {
            primaryColor = ConfigReader.configRead("secondaryColor");
            secondaryColor = ConfigReader.configRead("mainColor");
            accentColor = ConfigReader.configRead("accentColor");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    GREY COLOR
    database.accentColor=\#505D62
    database.mainColor=\#263238
    database.secondaryColor=\#323C41
    */

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


    //<editor-fold desc="Creation">


    public static Map<String, Object> createReportWindow(String reportName, int numWidthUnits, int numHeightUnits, SectionConfig... sectionConfigs) {
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

            sectionLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: transparent; -fx-padding: 0px 40px;");
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
        VBox root = new VBox(10, mainHeaderLabel, gridPane, buttonBox);
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
        scrollPane.getStyleClass().add("formPane");
        scrollPane.setStyle("-fx-background-color: " + accentColor + "; " + "-fx-focus-color: " + accentColor + ";");

        stage.setScene(scene);
        stage.setTitle(reportName);

        stage.setMaxWidth(screenWidth);
        stage.setMaxHeight(screenHeight);

        stage.initOwner(DataTerminalHomeApplication.getMainRT());
        stage.show();

        stage.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                stage.toFront();
            }
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
                stage.centerOnScreen();
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
                    comboBox.getItems().addAll(dropdownInfo.carColors);

                    comboBox.setPromptText(fieldConfig.getFieldName().toUpperCase());
                    comboBox.setButtonCell(new ListCell() {

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
                    comboBox.setMaxWidth(Double.MAX_VALUE);
                    gridPane.add(comboBox, columnIndex, rowIndex, fieldConfig.getSize(), 1);
                    fieldsMap.put(fieldConfig.getFieldName(), comboBox);
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
                    Button transferButton = new Button();
                    transferButton.setMaxWidth(Double.MAX_VALUE);
                    transferButton.getStyleClass().add("incidentformButton");
                    transferButton.setStyle("-fx-background-color: " + primaryColor);
                    transferButton.hoverProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            transferButton.setStyle("-fx-background-color: " + secondaryColor + ";");
                        } else {
                            transferButton.setStyle("-fx-background-color: " + primaryColor + ";");
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


    //<editor-fold desc="External">

    static Map<String, Object> calloutLayout() {
        Map<String, Object> calloutReport = createReportWindow("Callout Report", 5, 7,
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

    static Map<String, Object> patrolLayout() {
        Map<String, Object> patrolReport = createReportWindow("Patrol Report", 5, 7,
                new reportCreationUtil.SectionConfig("Officer Information",
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("name", 5, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("rank", 5, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("number", 2, reportCreationUtil.FieldType.TEXT_FIELD)),
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("division", 6, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("agency", 6, reportCreationUtil.FieldType.TEXT_FIELD))
                ),
                new reportCreationUtil.SectionConfig("Shift Information",
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("starttime", 3, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("stoptime", 4, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("patrolnumber", 5, reportCreationUtil.FieldType.TEXT_FIELD)),
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("length", 3, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("date", 3, reportCreationUtil.FieldType.TEXT_FIELD), new reportCreationUtil.FieldConfig("vehicle", 6, reportCreationUtil.FieldType.TEXT_FIELD))
                ),
                new reportCreationUtil.SectionConfig("Callout Notes",
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("notes", 12, reportCreationUtil.FieldType.TEXT_AREA))
                ),
                new reportCreationUtil.SectionConfig("Transfer Information To New Report",
                        new reportCreationUtil.RowConfig(new reportCreationUtil.FieldConfig("testBtn", 12, FieldType.TRANSFER_BUTTON))
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
        Button transferBtn = (Button) patrolReportMap.get("testBtn");

        transferBtn.setText("New Callout Report");
        BorderPane root = (BorderPane) patrolReport.get("root");
        Stage stage = (Stage) root.getScene().getWindow();

        /*
        Save last layout
        stage.setX(960);
        stage.setY(344);
        stage.setWidth(960);
        stage.setHeight(696);*/


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

        transferBtn.setOnAction(actionEvent -> {
            Map<String, Object> toCallout = calloutLayout();

            Map<String, Object> calloutReportMap = (Map<String, Object>) toCallout.get("Callout Report Map");

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
            TextField calloutcode = (TextField) calloutReportMap.get("code");

            BorderPane calloutPane = (BorderPane) toCallout.get("root");
            Stage rootstage = (Stage) calloutPane.getScene().getWindow();
            rootstage.toFront();
            Label callutwarningLabel = (Label) toCallout.get("warningLabel");

            calloutdate.setText(date.getText());
            callouttime.setText(stoptime.getText());
            officername.setText(name.getText());
            officerrank.setText(rank.getText());
            officerdiv.setText(div.getText());
            officeragen.setText(agen.getText());
            officernum.setText(num.getText());
            calloutnotes.setText(notes.getText());
            calloutnum.setText(patrolnum.getText());

            Button pullNotesBtn = (Button) toCallout.get("pullNotesBtn");
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

            Button submitBtn = (Button) toCallout.get("submitBtn");
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
                    callutwarningLabel.setVisible(true);
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            callutwarningLabel.setVisible(false);
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
                rootstage.close();
            });

        });

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
            System.out.println(stage.getX());
            System.out.println(stage.getY());
            System.out.println(stage.getWidth());
            System.out.println(stage.getHeight());


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

            Stage rootstage = (Stage) root.getScene().getWindow();
            rootstage.close();
        });
    }


    //</editor-fold>


    //<editor-fold desc="Report Windows">


    public enum FieldType {
        TEXT_FIELD,
        TEXT_AREA,
        COMBO_BOX_COLOR,
        CITATION_TREE_VIEW,
        TRANSFER_BUTTON;
    }

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
