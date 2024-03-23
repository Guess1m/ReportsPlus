package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
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
import javafx.scene.layout.StackPane;
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

public class actionController {
    public Button notesButton;
    public StackPane notesPane;
    public Button shiftInfoBtn;
    public StackPane shiftInformationPane;
    public TextField OfficerInfoName;
    public ComboBox OfficerInfoDivision;
    public TextArea ShiftInfoNotesTextArea;
    public ComboBox OfficerInfoAgency;
    public TextField OfficerInfoCallsign;
    public TextField OfficerInfoNumber;
    public ComboBox OfficerInfoRank;
    public Label generatedDateTag;
    public Label generatedByTag;
    public StackPane infoPane;
    public TextArea notepadTextArea;
    public Label updatedNotification;
    public StackPane logPane;
    public AnchorPane vbox;
    public StackPane UISettingsPane;
    public BarChart reportChart;
    public TextArea notepadTextArea1;
    public TextArea notepadTextArea2;
    public TextArea notepadTextArea3;
    private double xOffset = 0;
    private double yOffset = 0;

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

    public void refreshChart() {
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
        series1.getData().add(new XYChart.Data<>(categories[0], CalloutReportLogs.countReports())); // Value for "Callout"
        series1.getData().add(new XYChart.Data<>(categories[1], ArrestReportLogs.countReports())); // Value for "Arrests"
        series1.getData().add(new XYChart.Data<>(categories[2], TrafficStopReportLogs.countReports())); // Value for "Traffic Stops"
        series1.getData().add(new XYChart.Data<>(categories[3], PatrolReportLogs.countReports())); // Value for "Patrols"
        series1.getData().add(new XYChart.Data<>(categories[4], SearchReportLogs.countReports())); // Value for "Searches"
        series1.getData().add(new XYChart.Data<>(categories[5], IncidentReportLogs.countReports())); // Value for "Incidents"
        series1.getData().add(new XYChart.Data<>(categories[6], ImpoundReportLogs.countReports())); // Value for "Impounds"
        series1.getData().add(new XYChart.Data<>(categories[7], ParkingCitationReportLogs.countReports())); // Value for "PCitations"
        series1.getData().add(new XYChart.Data<>(categories[8], TrafficCitationReportLogs.countReports())); // Value for "TCitations"
        getReportChart().getData().add(series1);
        getReportChart().setLegendVisible(false);
        getReportChart().getXAxis().setTickLabelGap(20);
    }

    public BarChart getReportChart() {
        return reportChart;
    }

    public void initialize() throws IOException {
        refreshChart();

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

    public void onNotesButtonClicked(ActionEvent actionEvent) {
        setActive(notesPane);
        setDisable(shiftInformationPane);
        setDisable(infoPane);
        logPane.setVisible(false);
        logPane.setDisable(true);
        setDisable(UISettingsPane);
    }

    public void onShiftInfoBtnClicked(ActionEvent actionEvent) {
        setDisable(notesPane);
        setActive(shiftInformationPane);
        setDisable(infoPane);
        logPane.setVisible(false);
        logPane.setDisable(true);
        setDisable(UISettingsPane);
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

    public void onCalloutReportButtonClick(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("calloutReport-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Callout Report");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/terminal.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setY(stage.getY() * 3f / 2f);
    }

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

    public void onclearclick(ActionEvent actionEvent) {
        notepadTextArea.setText("");
        notepadTextArea1.setText("");
        notepadTextArea2.setText("");
        notepadTextArea3.setText("");
    }

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

    public void onExitButtonClick(MouseEvent actionEvent) {
        Platform.exit();
    }

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
        stage.setY(stage.getY() * 3f / 2f);
        stage.setMinHeight(stage.getHeight() - 200);
        stage.setMinWidth(stage.getWidth() - 200);
        ResizeHelper.addResizeListener(stage);
    }

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
        stage.setY(stage.getY() * 3f / 2f);
    }

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
        stage.setY(stage.getY() * 3f / 2f);
    }

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
        stage.setY(stage.getY() * 3f / 2f);
    }

    public void onTopCLick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onTopDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    public void onSideClick(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onSideDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

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
        stage.setY(stage.getY() * 3f / 2f);
    }

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
        stage.setY(stage.getY() * 3f / 2f);
    }

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
        stage.setY(stage.getY() * 3f / 2f);
    }

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
        stage.setY(stage.getY() * 3f / 2f);
    }

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
        stage.setY(stage.getY() * 3f / 2f);
    }

    public void aboutBtnClick(ActionEvent actionEvent) {
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        setActive(infoPane);
        logPane.setVisible(false);
        logPane.setDisable(true);
        setDisable(UISettingsPane);
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

    public void clearLogsBtnClick(ActionEvent actionEvent) {
        clearDataLogs();
    }

    public void clearAllSaveDataBtnClick(ActionEvent actionEvent) {
        clearDataLogs();
        clearConfig();
    }

    public void UISettingsBtnClick(ActionEvent actionEvent) {
        setActive(UISettingsPane);
        logPane.setVisible(false);
        logPane.setDisable(true);
        setDisable(notesPane);
        setDisable(shiftInformationPane);
        setDisable(infoPane);
    }

    public void onMouseEnter(MouseEvent mouseEvent) {
        updateChartIfMismatch(reportChart);
    }

    public void testBtnPress(ActionEvent actionEvent) throws IOException {

    }
}