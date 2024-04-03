package com.drozal.dataterminal.util;

import com.drozal.dataterminal.actionController;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.logs.Arrest.ArrestReportLogs;
import com.drozal.dataterminal.logs.Callout.CalloutReportLogs;
import com.drozal.dataterminal.logs.Impound.ImpoundReportLogs;
import com.drozal.dataterminal.logs.Incident.IncidentReportLogs;
import com.drozal.dataterminal.logs.Patrol.PatrolReportLogs;
import com.drozal.dataterminal.logs.Search.SearchReportLogs;
import com.drozal.dataterminal.logs.TrafficCitation.TrafficCitationReportLogs;
import com.drozal.dataterminal.logs.TrafficStop.TrafficStopReportLogs;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.drozal.dataterminal.util.stringUtil.getDataLogsFolderPath;
import static com.drozal.dataterminal.util.stringUtil.getJarPath;

public class controllerUtils {

    private static final String[][] keys = {
            {"-name", "-na", "-n", "-fullname", "-fname"},
            {"-number", "-num", "-phonenumber", "-phone", "-contact", "-telephonenumber"},
            {"-age", "-years", "-old", "-birthdate", "-a", "-dob", "-date", "-dateofbirth"},
            {"-address", "-addr", "-residence", "-place", "-add", "-ad"},
            {"-model", "-mod", "-mo", "-m"},
            {"-plate", "-platenum", "-platenumber", "-licenseplate", "-lc", "-plt", "-plte"},
            {"-gender", "-sex", "-identity", "-biological", "-g", "-gen"},
            {"-area", "-region", "-zone", "-territory", "-locale", "-ar"},
            {"-county", "-cty", "-state", "-province", "-territorial", "-cnty", "-ct"},
            {"-notes", "-nts"},
            {"-comments", "-cmts", "-cmt"},
            {"-description", "-des", "-desc", "-d"},
            {"-street", "-st", "-road", "-avenue", "-boulevard", "-dr", "-strt"}
    };

    public static void showButtonAnimation(Button button) {
        // Create a scale transition
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.2), button);
        scaleTransition.setFromX(0.9);
        scaleTransition.setFromY(0.9);
        scaleTransition.setToX(1);
        scaleTransition.setToY(1);
        scaleTransition.play();
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

    public static void updateSecondary(Color color) {
        String hexColor = toHexString(color);
        ConfigWriter.configwrite("secondaryColor", hexColor);
    }

    public static void updateAccent(Color color) {
        String hexColor = toHexString(color);
        ConfigWriter.configwrite("accentColor", hexColor);
    }

    public static void updateMain(Color color) {
        String hexColor = toHexString(color);
        ConfigWriter.configwrite("mainColor", hexColor);
    }

    public static void showNotification(String title, String message, Object owner) {
        Label label = new Label(message);

        VBox vbox1 = new VBox(label);
        vbox1.setAlignment(Pos.CENTER);

        // Create and show the notification
        Notifications.create()
                .title(title)
                .text(message)
                .graphic(null) // You can add a graphic if needed
                .position(Pos.TOP_RIGHT)
                .hideAfter(Duration.seconds(1.15))
                .owner(owner)
                .show();
    }

    public static void setActive(AnchorPane pane) {
        pane.setVisible(true);
        pane.setDisable(false);
    }

    public static void setDisable(AnchorPane... panes) {
        for (AnchorPane pane : panes) {
            pane.setVisible(false);
            pane.setDisable(true);
        }
    }

    public static void confirmLogClearDialog(Stage ownerStage, BarChart barChart, AreaChart areaChart) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.initOwner(ownerStage);
        dialog.setTitle("Confirm Action");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label messageLabel = new Label("Are you sure you want to perform this action?\nThis will clear all your logs.");
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            dialog.setResult(true);
            dialog.close();
        });
        Button noButton = new Button("No");
        noButton.getStyleClass().add("menuButton");
        noButton.setOnAction(e -> {
            dialog.setResult(false);
            dialog.close();
        });

        dialog.getDialogPane().setContent(new VBox(10, messageLabel, yesButton, noButton));

        dialog.showAndWait().ifPresent(result -> {
            if (result) {
                clearDataLogs();
                updateChartIfMismatch(barChart);
                controllerUtils.refreshChart(areaChart, "area");
            } else {
            }
        });
    }

    public static void changeBarColors(BarChart<String, Number> barChart) throws IOException {
        // Get the list of series from the bar chart
        ObservableList<XYChart.Series<String, Number>> seriesList = barChart.getData();

        // Iterate over each series
        for (XYChart.Series<String, Number> series : seriesList) {
            // Iterate over each data point in the series
            for (XYChart.Data<String, Number> data : series.getData()) {
                // Access the node representing the bar for the data point
                javafx.scene.Node node = data.getNode();
                // Set the style of the node to change the color of the bar
                node.setStyle("-fx-bar-fill: " + ConfigReader.configRead("accentColor") + "; -fx-border-color: " + ConfigReader.configRead("secondaryColor") + "; -fx-border-width: 2.5 2.5 0.5 2.5");
            }
        }
    }

    public static void changeStatisticColors(AreaChart chart) throws IOException {
        //Accent
        String accclr = ConfigReader.configRead("accentColor");
        String mainclr = ConfigReader.configRead("mainColor");
        String secclr = ConfigReader.configRead("secondaryColor");
        chart.lookup(".chart-series-area-fill").setStyle("-fx-fill: " + accclr + ";");
        chart.lookup(".chart-series-area-line").setStyle("-fx-fill: " + secclr + "; -fx-stroke: " + mainclr + ";");
    }

    public static String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public static void updateChartIfMismatch(BarChart<String, Number> chart) {
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
                    case 7: // TCitations
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

    public static void createSpinnerNumListener(Spinner spinner) {
        spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,3}")) {
                // If the new value does not match the pattern of up to three digits, revert to the old value
                spinner.getEditor().setText(oldValue);
            }
        });
    }

    public static void clearDataLogs() {
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

    public static void clearDataFolder() {
        try {
            // Get the path to the DataLogs folder
            String dataLogsFolderPath = getJarPath() + File.separator + "data";

            // Print the path for debugging
            System.out.println("Data folder path: " + dataLogsFolderPath);

            // Check if the DataLogs folder exists
            File dataLogsFolder = new File(dataLogsFolderPath);
            if (dataLogsFolder.exists() && dataLogsFolder.isDirectory()) {
                System.out.println("Data folder exists.");

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
                    System.out.println("All files in Data folder deleted successfully.");
                } else {
                    System.out.println("Data folder is empty.");
                }
            } else {
                System.out.println("Data folder does not exist.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearDataFolderAsync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            clearDataFolder();
        });

        executor.shutdown();
    }

    public static void clearDataLogsAsync() {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.submit(() -> {
            clearDataLogs();
        });

        executor.shutdown();
    }

    public static void confirmSaveDataClearDialog(Stage ownerStage) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.initOwner(ownerStage);
        dialog.setTitle("Confirm Action");
        dialog.initModality(Modality.APPLICATION_MODAL);

        Label messageLabel = new Label("Are you sure you want to perform this action?\nThis will remove all save data including logs and config.");
        Button yesButton = new Button("Yes");
        yesButton.setOnAction(e -> {
            dialog.setResult(true);
            dialog.close();
        });
        Button noButton = new Button("No");
        noButton.getStyleClass().add("menuButton");
        noButton.setOnAction(e -> {
            dialog.setResult(false);
            dialog.close();
        });

        dialog.getDialogPane().setContent(new VBox(10, messageLabel, yesButton, noButton));

        dialog.showAndWait().ifPresent(result -> {
            if (result) {
                clearDataLogsAsync();
                clearConfig();
                clearDataFolderAsync();
            } else {
            }
        });
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

    public static void parseLogData(String logURL, Map<String, Integer> combinedAreasMap, String value) {
        Map<String, Integer> areasMap = new HashMap<>();
        File xmlFile = new File(logURL);
        if (!xmlFile.exists()) {
            return; // Return without parsing if file doesn't exist
        }

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("*");

            for (int temp = 0; temp < nodeList.getLength(); temp++) {
                Element element = (Element) nodeList.item(temp);
                String nodeName = element.getNodeName();
                if (nodeName.toLowerCase().contains(value) && !nodeName.toLowerCase().contains("textarea")) {
                    String area = element.getTextContent().trim();
                    if (!area.isEmpty()) {
                        combinedAreasMap.put(area, combinedAreasMap.getOrDefault(area, 0) + 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static XYChart.Series<String, Number> parseEveryLog(String value) {
        Map<String, Integer> combinedAreasMap = new HashMap<>();
        parseLogData(stringUtil.arrestLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.calloutLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.impoundLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.incidentLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.patrolLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.searchLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.trafficCitationLogURL, combinedAreasMap, value);
        parseLogData(stringUtil.trafficstopLogURL, combinedAreasMap, value);

        // Sort the areas alphabetically ignoring case
        Map<String, Integer> sortedAreasMap = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        sortedAreasMap.putAll(combinedAreasMap);
        // Create series and populate with sorted data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<String, Integer> entry : sortedAreasMap.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }
        return series;
    }

    public static void refreshChart(AreaChart chart, String value) {
        chart.getData().clear(); // Clear existing data from the chart
        chart.getData().add(parseEveryLog(value)); // Add new data to the chart
        try {
            changeStatisticColors(chart);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> pullNotesValues(String notepad) {
        String text = notepad;
        Map<String, String> values = new HashMap<>();

        for (String[] keyGroup : keys) {
            for (String key : keyGroup) {
                String value = extractValue(text, key);
                if (value != null) {
                    for (String k : keyGroup) {
                        values.put(k, value);
                    }
                    break;
                }
            }
        }

        return values;
    }

    private static String extractValue(String text, String key) {
        Pattern pattern = Pattern.compile(key + "\\s+(.*?)(?=\\s+-|$)");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static void updateTextFromNotepad(TextField textField, TextArea notepadText, String... keys) {
        Map<String, String> values = pullNotesValues(notepadText.getText());
        String extractedValue = null;
        for (String key : keys) {
            extractedValue = values.get(key);
            if (extractedValue != null) {
                break;
            }
            // Check for alternatives
            for (Map.Entry<String, String> entry : values.entrySet()) {
                for (String altKey : entry.getKey().split("\\|")) {
                    if (altKey.equals(key)) {
                        extractedValue = entry.getValue();
                        break;
                    }
                }
                if (extractedValue != null) {
                    break;
                }
            }
            if (extractedValue != null) {
                break;
            }
        }
        String labelText = extractedValue;
        textField.setText(labelText);
    }

    public static void updateTextFromNotepad(TextArea textArea, TextArea notepadText, String... keys) {
        Map<String, String> values = pullNotesValues(notepadText.getText());
        String extractedValue = null;
        for (String key : keys) {
            extractedValue = values.get(key);
            if (extractedValue != null) {
                break;
            }
            // Check for alternatives
            for (Map.Entry<String, String> entry : values.entrySet()) {
                for (String altKey : entry.getKey().split("\\|")) {
                    if (altKey.equals(key)) {
                        extractedValue = entry.getValue();
                        break;
                    }
                }
                if (extractedValue != null) {
                    break;
                }
            }
            if (extractedValue != null) {
                break;
            }
        }
        String labelText = extractedValue;
        textArea.setText(labelText);
    }

    public static void updateTextFromNotepad(Spinner spinner, TextArea notepadText, String... keys) {
        Map<String, String> values = pullNotesValues(notepadText.getText());
        String extractedValue = null;
        for (String key : keys) {
            extractedValue = values.get(key);
            if (extractedValue != null) {
                break;
            }
            // Check for alternatives
            for (Map.Entry<String, String> entry : values.entrySet()) {
                for (String altKey : entry.getKey().split("\\|")) {
                    if (altKey.equals(key)) {
                        extractedValue = entry.getValue();
                        break;
                    }
                }
                if (extractedValue != null) {
                    break;
                }
            }
            if (extractedValue != null) {
                break;
            }
        }
        String labelText = (extractedValue != null) ? extractedValue : "0";
        spinner.getEditor().setText(labelText);
    }
}
