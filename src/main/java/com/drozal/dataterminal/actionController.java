package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.logs.Callout.CalloutLogEntry;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import com.drozal.dataterminal.logs.ParkingCitation.ParkingCitationReportLogs;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReportLogs;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportLogs;
import com.drozal.dataterminal.util.ResizeHelper;
import com.drozal.dataterminal.util.dropdownInfo;
import com.drozal.dataterminal.util.stringUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static com.drozal.dataterminal.DataTerminalHomeApplication.createSpinner;
import static com.drozal.dataterminal.util.windowUtils.toggleWindowedFullscreen;

public class actionController {
    @javafx.fxml.FXML
    public Button notesButton;
    @javafx.fxml.FXML
    public StackPane notesPane;
    @javafx.fxml.FXML
    public Button shiftInfoBtn;
    @javafx.fxml.FXML
    public StackPane shiftInformationPane;
    @javafx.fxml.FXML
    public TextField OfficerInfoName;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoDivision;
    @javafx.fxml.FXML
    public TextArea ShiftInfoNotesTextArea;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoAgency;
    @javafx.fxml.FXML
    public TextField OfficerInfoCallsign;
    @javafx.fxml.FXML
    public TextField OfficerInfoNumber;
    @javafx.fxml.FXML
    public ComboBox OfficerInfoRank;
    @javafx.fxml.FXML
    public Label generatedDateTag;
    @javafx.fxml.FXML
    public Label generatedByTag;
    @javafx.fxml.FXML
    public StackPane infoPane;
    @javafx.fxml.FXML
    public TextArea notepadTextArea;
    @javafx.fxml.FXML
    public Label updatedNotification;
    @javafx.fxml.FXML
    public AnchorPane vbox;
    @javafx.fxml.FXML
    public AnchorPane UISettingsPane;
    @javafx.fxml.FXML
    public BarChart reportChart;
    @javafx.fxml.FXML
    public TextArea notepadTextArea1;
    @javafx.fxml.FXML
    public TextArea notepadTextArea2;
    @javafx.fxml.FXML
    public TextArea notepadTextArea3;
    @javafx.fxml.FXML
    public ColorPicker colorSelectMain;
    @javafx.fxml.FXML
    public AnchorPane topPane;
    @javafx.fxml.FXML
    public AnchorPane sidepane;
    @javafx.fxml.FXML
    public ColorPicker colorSelectSecondary;
    @javafx.fxml.FXML
    public Label mainColor8;
    @javafx.fxml.FXML
    public Label mainColor9Bkg;
    @javafx.fxml.FXML
    public Button updateInfoBtn;
    @javafx.fxml.FXML
    public MenuButton settingsDropdown;
    @javafx.fxml.FXML
    public Label secondaryColorBkgNotes3;
    @javafx.fxml.FXML
    public Label secondaryColorBkgNotes1;
    @javafx.fxml.FXML
    public Label secondaryColorBkgNotes4;
    @javafx.fxml.FXML
    public Label secondaryColorBkgNotes2;
    @javafx.fxml.FXML
    public Button resetDefaultsBtn;
    @javafx.fxml.FXML
    public Label primaryColor;
    @javafx.fxml.FXML
    public Label secondaryColor;
    @javafx.fxml.FXML
    public AnchorPane calloutReportPane;
    private double xOffset = 0;
    private double yOffset = 0;
    @javafx.fxml.FXML
    private Label secondaryColor2;
    @javafx.fxml.FXML
    private Label secondaryColor5;
    @javafx.fxml.FXML
    private Label secondaryColor3;
    @javafx.fxml.FXML
    private Label secondaryColor4;
    @javafx.fxml.FXML
    private Label secondaryColor3Bkg;
    @javafx.fxml.FXML
    private Label mainColor6;
    @javafx.fxml.FXML
    private Label mainColor7Bkg;
    @javafx.fxml.FXML
    private Label secondaryColor4Bkg;
    @javafx.fxml.FXML
    private Label secondaryColor5Bkg;
    @javafx.fxml.FXML
    private Button clearbtnnotepad;
    @javafx.fxml.FXML
    private Button logsButton;
    @javafx.fxml.FXML
    private Button mapButton;
    @javafx.fxml.FXML
    private MenuButton createReportBtn;
    @javafx.fxml.FXML
    private TextField calloutReportResponseCounty;
    @javafx.fxml.FXML
    private MenuItem parkingCitationReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportName;
    @javafx.fxml.FXML
    private Label calloutincompleteLabel;
    @javafx.fxml.FXML
    private MenuItem searchReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportAgency;
    @javafx.fxml.FXML
    private MenuItem trafficReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportResponseAddress;
    @javafx.fxml.FXML
    private MenuItem impoundReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportResponseGrade;
    @javafx.fxml.FXML
    private TextArea calloutReportNotesTextArea;
    @javafx.fxml.FXML
    private Button calloutReportSubmitBtn;
    @javafx.fxml.FXML
    private MenuItem incidentReportButton;
    @javafx.fxml.FXML
    private MenuItem patrolReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportTime;
    @javafx.fxml.FXML
    private MenuItem calloutReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportDivision;
    @javafx.fxml.FXML
    private Spinner calloutReportSpinner;
    @javafx.fxml.FXML
    private TextField calloutReportResponeType;
    @javafx.fxml.FXML
    private TextField calloutReportResponseArea;
    @javafx.fxml.FXML
    private MenuItem arrestReportButton;
    @javafx.fxml.FXML
    private MenuItem trafficCitationReportButton;
    @javafx.fxml.FXML
    private TextField calloutReportNumber;
    @javafx.fxml.FXML
    private TextField calloutReportDate;
    @javafx.fxml.FXML
    private TextField calloutReportRank;

    public static String getDataLogsFolderPath() {
        try {
            // Get the location of the JAR file
            String jarPath = stringUtil.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();

            // Construct the path for the DataLogs folder
            return jarDir + File.separator + "DataLogs" + File.separator;
        } catch (URISyntaxException e) {
            // Handle exception if URI syntax is incorrect
            e.printStackTrace();
            return ""; // Return empty string if an error occurs
        }
    }

    public static String getJarDirectoryPath() {
        try {
            // Get the location of the JAR file
            String jarPath = actionController.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

            // Extract the directory path from the JAR path
            return new File(jarPath).getParent();
        } catch (Exception e) {
            // Handle exception if URI syntax is incorrect
            e.printStackTrace();
            return ""; // Return empty string if an error occurs
        }
    }

    public static void clearConfig() {
        try {
            // Get the path to the config.properties file
            String configFilePath = getJarDirectoryPath() + File.separator + "config.properties";
            File configFile = new File(configFilePath);

            // Check if the config.properties file exists
            if (configFile.exists() && configFile.isFile()) {
                // Delete the config.properties file
                try {
                    Files.deleteIfExists(configFile.toPath());
                    System.out.println("Deleted config.properties file.");
                } catch (IOException e) {
                    System.err.println("Failed to delete config.properties file: " + e.getMessage());
                }
            } else {
                System.out.println("config.properties file does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the application after deleting the files
            Platform.exit();
        }
    }

    public AnchorPane getUISettingsPane() {
        return UISettingsPane;
    }

    public void updateChartIfMismatch(BarChart<String, Number> chart) {
        XYChart.Series<String, Number> series = null;
        for (XYChart.Series<String, Number> s : chart.getData()) {
            if (s.getName().equals("Series 1")) {
                series = s;
                break;
            }
        }

        if (series != null) {
            for (int i = 0; i < series.getData().size(); i++) {
                XYChart.Data<String, Number> data = series.getData().get(i);
                int reportsCount = 0;
                switch (i) {
                    case 0: // Callout
                        reportsCount = CalloutReportLogs.countReports();
                        break;
                    case 1: // Arrests
                        reportsCount = ArrestReportLogs.countReports();
                        break;
                    case 2: // Traffic Stops
                        reportsCount = TrafficStopReportLogs.countReports();
                        break;
                    case 3: // Patrols
                        reportsCount = PatrolReportLogs.countReports();
                        break;
                    case 4: // Searches
                        reportsCount = SearchReportLogs.countReports();
                        break;
                    case 5: // Incidents
                        reportsCount = IncidentReportLogs.countReports();
                        break;
                    case 6: // Impounds
                        reportsCount = ImpoundReportLogs.countReports();
                        break;
                    case 7: // PCitations
                        reportsCount = ParkingCitationReportLogs.countReports();
                        break;
                    case 8: // TCitations
                        reportsCount = TrafficCitationReportLogs.countReports();
                        break;
                }
                if (data.getYValue().intValue() != reportsCount) {
                    // Update the data point to match the report count
                    data.setYValue(reportsCount);
                }
            }
        }
    }

    public void refreshChart() throws IOException {
        // Clear existing data from the chart
        reportChart.getData().clear();
        String[] categories = {"Callout", "Arrests", "Traffic Stops", "Patrols", "Searches", "Incidents", "Impounds", "Parking Cit.", "Traffic Cit."};
        CategoryAxis xAxis = (CategoryAxis) getReportChart().getXAxis();
        NumberAxis yAxis = (NumberAxis) getReportChart().getYAxis();

        // Setting font for X and Y axis labels
        Font axisLabelFont = Font.font("Segoe UI Bold", 11.5); // Change the font family and size as needed
        xAxis.setTickLabelFont(axisLabelFont);
        yAxis.setTickLabelFont(axisLabelFont);

        xAxis.setCategories(FXCollections.observableArrayList(Arrays.asList(categories)));
        yAxis.setAutoRanging(true);
        yAxis.setMinorTickVisible(false);
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Series 1");

        String color = ConfigReader.configRead("mainColor");
        for (String category : categories) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(category, 1); // Value doesn't matter, just need to add a data point
            data.nodeProperty().addListener((obs, oldNode, newNode) -> {
                if (newNode != null) {
                    newNode.setStyle("-fx-bar-fill: " + color + ";");
                }
            });
            series1.getData().add(data);
        }

        getReportChart().getData().add(series1);
        getReportChart().setLegendVisible(false);
        getReportChart().getXAxis().setTickLabelGap(20);
    }

    public BarChart getReportChart() {
        return reportChart;
    }

    public void initialize() throws IOException {
        setActive(shiftInformationPane);
        setDisable(notesPane);
        setDisable(infoPane);
        UISettingsPane.setDisable(true);
        UISettingsPane.setVisible(false);
        calloutReportPane.setDisable(true);
        calloutReportPane.setVisible(false);
        refreshChart();
        loadTheme();
        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        getOfficerInfoRank().getItems().addAll(dropdownInfo.ranks);
        getOfficerInfoDivision().getItems().addAll(dropdownInfo.divisions);
        getOfficerInfoDivision().setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> p) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item);
                            setAlignment(javafx.geometry.Pos.CENTER);
                        }
                    }
                };
            }
        });
        getOfficerInfoAgency().getItems().addAll(dropdownInfo.agencies);

        OfficerInfoName.setText(name);
        OfficerInfoDivision.setValue(division);
        OfficerInfoRank.setValue(rank);
        OfficerInfoAgency.setValue(agency);
        OfficerInfoNumber.setText(number);

        //Top Generations
        generatedByTag.setText("Generated By:" + " " + name);
        String date = DataTerminalHomeApplication.getDate();
        generatedDateTag.setText("Generated On: " + date);
        /*

        shiftInfoBtn.setStyle("-fx-background-color: red;");

        Initialize Dark/Light mode here

         */
    }

    public void setDisable(StackPane pane) {
        pane.setVisible(false);
        pane.setDisable(true);
    }

    public void setActive(StackPane pane) {
        pane.setVisible(true);
        pane.setDisable(false);
    }

    @javafx.fxml.FXML
    public void onNotesButtonClicked(ActionEvent actionEvent) {
        setActive(notesPane);
        setDisable(shiftInformationPane);
        setDisable(infoPane);
        UISettingsPane.setDisable(true);
        UISettingsPane.setVisible(false);
        calloutReportPane.setDisable(true);
        calloutReportPane.setVisible(false);
    }

    @javafx.fxml.FXML
    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
        setDisable(notesPane);
        setActive(shiftInformationPane);
        setDisable(infoPane);
        UISettingsPane.setDisable(true);
        UISettingsPane.setVisible(false);
        calloutReportPane.setDisable(true);
        calloutReportPane.setVisible(false);
    }

    public ComboBox getOfficerInfoAgency() {
        return OfficerInfoAgency;
    }

    public ComboBox getOfficerInfoDivision() {
        return OfficerInfoDivision;
    }

    public TextField getOfficerInfoName() {
        return OfficerInfoName;
    }

    public TextField getOfficerInfoNumber() {
        return OfficerInfoNumber;
    }

    public ComboBox getOfficerInfoRank() {
        return OfficerInfoRank;
    }

    public Label getGeneratedByTag() {
        return generatedByTag;
    }

    public Label getGeneratedDateTag() {
        return generatedDateTag;
    }

    public StackPane getNotesPane() {
        return notesPane;
    }

    public StackPane getShiftInformationPane() {
        return shiftInformationPane;
    }

    public StackPane getInfoPane() {
        return infoPane;
    }

    @javafx.fxml.FXML
    public void onCalloutReportButtonClick(ActionEvent actionEvent) throws IOException {
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        setDisable(infoPane);
        UISettingsPane.setDisable(true);
        UISettingsPane.setVisible(false);
        calloutReportPane.setDisable(false);
        calloutReportPane.setVisible(true);

        createSpinner(calloutReportSpinner, 0, 9999, 0);

        String name = ConfigReader.configRead("Name");
        String division = ConfigReader.configRead("Division");
        String rank = ConfigReader.configRead("Rank");
        String number = ConfigReader.configRead("Number");
        String agency = ConfigReader.configRead("Agency");

        calloutReportName.setText(name);
        calloutReportDivision.setText(division);
        calloutReportRank.setText(rank);
        calloutReportAgency.setText(agency);
        calloutReportNumber.setText(number);

        calloutReportDate.setText(DataTerminalHomeApplication.getDate());
        calloutReportTime.setText(DataTerminalHomeApplication.getTime());
    }

    @javafx.fxml.FXML
    public void onMapButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("map-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Los Santos Map");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UTILITY);
        stage.setResizable(false);
        stage.show();
    }

    @javafx.fxml.FXML
    public void onclearclick(ActionEvent actionEvent) {
        notepadTextArea.setText("");
        notepadTextArea1.setText("");
        notepadTextArea2.setText("");
        notepadTextArea3.setText("");
    }

    @javafx.fxml.FXML
    public void updateInfoButtonClick(ActionEvent actionEvent) {
        if (getOfficerInfoAgency().getValue() == null || getOfficerInfoDivision().getValue() == null ||
                getOfficerInfoRank().getValue() == null || getOfficerInfoName().getText().isEmpty() ||
                getOfficerInfoNumber().getText().isEmpty()) {
            updatedNotification.setText("Fill Out Form.");
            updatedNotification.setStyle("-fx-text-fill: red;");
            updatedNotification.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline1.play();
        } else {
            ConfigWriter.configwrite("Agency", getOfficerInfoAgency().getValue().toString());
            ConfigWriter.configwrite("Division", getOfficerInfoDivision().getValue().toString());
            ConfigWriter.configwrite("Name", getOfficerInfoName().getText());
            ConfigWriter.configwrite("Rank", getOfficerInfoRank().getValue().toString());
            ConfigWriter.configwrite("Number", getOfficerInfoNumber().getText());
            generatedByTag.setText("Generated By:" + " " + getOfficerInfoName().getText());
            updatedNotification.setText("updated.");
            updatedNotification.setStyle("-fx-text-fill: green;");
            updatedNotification.setVisible(true);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                updatedNotification.setVisible(false);
            }));
            timeline.play();
        }
    }

    @javafx.fxml.FXML
    public void onExitButtonClick(MouseEvent actionEvent) {
        Platform.exit();
    }

    @javafx.fxml.FXML
    public void onLogsButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("logBrowser.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Log Browser");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setResizable(true);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setMinHeight(stage.getHeight() - 200);
        stage.setMinWidth(stage.getWidth() - 200);
        ResizeHelper.addResizeListener(stage);
    }

    @javafx.fxml.FXML
    public void trafficStopReportButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("trafficStopReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Traffic Stop Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onIncidentReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("incidentReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Incident Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onSearchReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("searchReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Search Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onTopCLick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    @javafx.fxml.FXML
    public void onTopDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @javafx.fxml.FXML
    public void onSideClick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    @javafx.fxml.FXML
    public void onSideDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @javafx.fxml.FXML
    public void onArrestReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("arrestReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Arrest Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onPatrolButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("patrolReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Patrol Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onCitationReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("trafficCitationReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Citation Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onImpoundReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("impoundReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Impound Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void onParkingCitationReportBtnClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("parkingCitationReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Parking Citation Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
    }

    @javafx.fxml.FXML
    public void aboutBtnClick(ActionEvent actionEvent) {
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        setActive(infoPane);
        UISettingsPane.setDisable(true);
        UISettingsPane.setVisible(false);
        calloutReportPane.setDisable(true);
        calloutReportPane.setVisible(false);
    }

    public void clearDataLogs() {
        try {
            // Get the path to the DataLogs folder
            String dataLogsFolderPath = getDataLogsFolderPath();

            // Print the path for debugging
            System.out.println("DataLogs folder path: " + dataLogsFolderPath);

            // Check if the DataLogs folder exists
            File dataLogsFolder = new File(dataLogsFolderPath);
            if (dataLogsFolder.exists() && dataLogsFolder.isDirectory()) {
                System.out.println("DataLogs folder exists.");

                // Get a list of files in the DataLogs folder
                File[] files = dataLogsFolder.listFiles();

                if (files != null) {
                    // Delete each file in the DataLogs folder
                    for (File file : files) {
                        if (file.isFile()) {
                            try {
                                Files.deleteIfExists(file.toPath());
                                System.out.println("Deleted file: " + file.getName());
                            } catch (IOException e) {
                                System.err.println("Failed to delete file: " + file.getName() + " " + e.getMessage());
                            }
                        }
                    }
                    System.out.println("All files in DataLogs folder deleted successfully.");
                } else {
                    System.out.println("DataLogs folder is empty.");
                }
            } else {
                System.out.println("DataLogs folder does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @javafx.fxml.FXML
    public void clearLogsBtnClick(ActionEvent actionEvent) {
        clearDataLogs();
    }

    @javafx.fxml.FXML
    public void clearAllSaveDataBtnClick(ActionEvent actionEvent) {
        clearDataLogs();
        clearConfig();
    }

    @javafx.fxml.FXML
    public void UISettingsBtnClick(ActionEvent actionEvent) {
        showSettingsWindow();
    }

    private void showSettingsWindow() {
        // Create a new stage for the settings window
        Stage settingsStage = new Stage();
        settingsStage.setTitle("UI Settings");

        // Create color pickers for selecting colors
        Label primColor = primaryColor;
        Label secColor = secondaryColor;

        ColorPicker colorPicker1 = colorSelectMain;
        ColorPicker colorPicker2 = colorSelectSecondary;

        Button saveButton = resetDefaultsBtn;

        // Create a GridPane layout for the settings window
        GridPane root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(15);
        root.setVgap(15);
        root.addRow(0, primColor);
        root.addRow(1, colorPicker1);
        root.addRow(2, secColor);
        root.addRow(3, colorPicker2);
        root.add(saveButton, 0, 4);

        // Create the scene for the settings window
        Scene scene = new Scene(root);

        // Set the scene on the settings stage
        settingsStage.setScene(scene);

        // Show the settings window
        settingsStage.initStyle(StageStyle.DECORATED);
        settingsStage.setResizable(false);
        settingsStage.show();
    }


    @javafx.fxml.FXML
    public void onMouseEnter(MouseEvent mouseEvent) {
        updateChartIfMismatch(reportChart);
    }

    @javafx.fxml.FXML
    public void onFullscreenBtnClick(Event event) {
        Stage stage = (Stage) vbox.getScene().getWindow();
        if (stage != null) {
            toggleWindowedFullscreen(stage, 1027, 740);
        }
    }

    @javafx.fxml.FXML
    public void onColorSelectMainPress(ActionEvent actionEvent) throws IOException {
        Color selectedColor = colorSelectMain.getValue();
        updateMain(selectedColor);
        loadTheme();
    }

    private void updateMain(Color color) {
        String hexColor = toHexString(color);
        ConfigWriter.configwrite("mainColor", hexColor);
    }

    public void changeBarColors(BarChart<String, Number> barChart, String color) {
        // Get the list of series from the bar chart
        ObservableList<XYChart.Series<String, Number>> seriesList = barChart.getData();

        // Iterate over each series
        for (XYChart.Series<String, Number> series : seriesList) {
            // Iterate over each data point in the series
            for (XYChart.Data<String, Number> data : series.getData()) {
                // Access the node representing the bar for the data point
                javafx.scene.Node node = data.getNode();
                // Set the style of the node to change the color of the bar
                node.setStyle("-fx-bar-fill: " + color + ";");
            }
        }
    }

    private void loadTheme() throws IOException {
        colorSelectMain.setValue(Color.valueOf(ConfigReader.configRead("mainColor")));
        colorSelectSecondary.setValue(Color.valueOf(ConfigReader.configRead("secondaryColor")));
        changeBarColors(getReportChart(), ConfigReader.configRead("mainColor"));
        //Main
        String mainclr = ConfigReader.configRead("mainColor");
        topPane.setStyle("-fx-background-color: " + mainclr + ";");
        mainColor6.setStyle("-fx-text-fill: " + mainclr + ";");
        mainColor8.setStyle("-fx-text-fill: " + mainclr + ";");
        mainColor7Bkg.setStyle("-fx-background-color: " + mainclr + ";");
        mainColor9Bkg.setStyle("-fx-background-color: " + mainclr + ";");
        primaryColor.setStyle("-fx-text-fill: " + mainclr + ";");
        //Secondary
        String secclr = ConfigReader.configRead("secondaryColor");
        secondaryColor.setStyle("-fx-text-fill: " + secclr + ";");
        sidepane.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor2.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor3.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor4.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColor5.setStyle("-fx-text-fill: " + secclr + ";");
        secondaryColorBkgNotes1.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColorBkgNotes2.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColorBkgNotes3.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColorBkgNotes4.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor3Bkg.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor4Bkg.setStyle("-fx-background-color: " + secclr + ";");
        secondaryColor5Bkg.setStyle("-fx-background-color: " + secclr + ";");
        //Buttons
        String hoverStyle = "-fx-background-color: " + ConfigReader.configRead("mainColor");
        String initialStyle = "-fx-background-color: transparent;";
        String nonTransparentBtn = "-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";";
        updateInfoBtn.setStyle("-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";");
        clearbtnnotepad.setStyle("-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";");
        resetDefaultsBtn.setStyle("-fx-background-color: " + ConfigReader.configRead("secondaryColor") + ";");

        // Add hover event handling
        shiftInfoBtn.setOnMouseEntered(e -> shiftInfoBtn.setStyle(hoverStyle));
        shiftInfoBtn.setOnMouseExited(e -> shiftInfoBtn.setStyle(initialStyle));
        settingsDropdown.setOnMouseEntered(e -> settingsDropdown.setStyle(hoverStyle));
        settingsDropdown.setOnMouseExited(e -> settingsDropdown.setStyle(initialStyle));
        notesButton.setOnMouseEntered(e -> notesButton.setStyle(hoverStyle));
        notesButton.setOnMouseExited(e -> notesButton.setStyle(initialStyle));
        createReportBtn.setOnMouseEntered(e -> createReportBtn.setStyle(hoverStyle));
        createReportBtn.setOnMouseExited(e -> createReportBtn.setStyle(initialStyle));
        logsButton.setOnMouseEntered(e -> logsButton.setStyle(hoverStyle));
        logsButton.setOnMouseExited(e -> logsButton.setStyle(initialStyle));
        mapButton.setOnMouseEntered(e -> mapButton.setStyle(hoverStyle));
        mapButton.setOnMouseExited(e -> mapButton.setStyle(initialStyle));
        updateInfoBtn.setOnMouseEntered(e -> updateInfoBtn.setStyle(hoverStyle));
        updateInfoBtn.setOnMouseExited(e -> {
            updateInfoBtn.setStyle(nonTransparentBtn);
        });
        clearbtnnotepad.setOnMouseEntered(e -> clearbtnnotepad.setStyle(hoverStyle));
        clearbtnnotepad.setOnMouseExited(e -> {
            clearbtnnotepad.setStyle(nonTransparentBtn);
        });
        resetDefaultsBtn.setOnMouseEntered(e -> resetDefaultsBtn.setStyle(hoverStyle));
        resetDefaultsBtn.setOnMouseExited(e -> {
            resetDefaultsBtn.setStyle(nonTransparentBtn);
        });
    }

    private void updateSecondary(Color color) {
        String hexColor = toHexString(color);
        ConfigWriter.configwrite("secondaryColor", hexColor);
    }

    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    @javafx.fxml.FXML
    public void onColorSelectSecondaryPress(ActionEvent actionEvent) throws IOException {
        Color selectedColor = colorSelectSecondary.getValue();
        updateSecondary(selectedColor);
        loadTheme();
    }

    @javafx.fxml.FXML
    public void resetDefaultsBtnPress(ActionEvent actionEvent) throws IOException {
        updateMain(Color.valueOf("#524992"));
        updateSecondary(Color.valueOf("#665cb6"));
        loadTheme();
    }

    @javafx.fxml.FXML
    public void onCalloutReportSubmitBtnClick(ActionEvent actionEvent) {
        if (calloutReportSpinner.getValue() == null) {
            calloutincompleteLabel.setText("Fill Out Form.");
            calloutincompleteLabel.setStyle("-fx-text-fill: red;");
            calloutincompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                calloutincompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {// Load existing logs from XML
            List<CalloutLogEntry> logs = CalloutReportLogs.loadLogsFromXML();

            // Add new entry
            logs.add(new CalloutLogEntry(
                    calloutReportDate.getText(),
                    calloutReportTime.getText(),
                    calloutReportName.getText(),
                    calloutReportRank.getText(),
                    calloutReportNumber.getText(),
                    calloutReportDivision.getText(),
                    calloutReportAgency.getText(),
                    calloutReportResponeType.getText(),
                    calloutReportResponseGrade.getText(),
                    calloutReportSpinner.getValue().toString(),
                    calloutReportNotesTextArea.getText(),
                    calloutReportResponseAddress.getText(),
                    calloutReportResponseCounty.getText(),
                    calloutReportResponseArea.getText()

            ));
            // Save logs to XML
            CalloutReportLogs.saveLogsToXML(logs);
            setActive(shiftInformationPane);
            calloutReportPane.setDisable(true);
            calloutReportPane.setVisible(false);
        }
    }

    @javafx.fxml.FXML
    public void testBtnPress(ActionEvent actionEvent) throws IOException {

    }
}