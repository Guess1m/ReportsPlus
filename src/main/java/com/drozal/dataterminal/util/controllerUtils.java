package com.drozal.dataterminal.util;

import com.drozal.dataterminal.actionController;
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
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.drozal.dataterminal.util.stringUtil.getDataLogsFolderPath;

public class controllerUtils {

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

    public static void confirmLogClearDialog(Stage ownerStage, BarChart chart) {
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
                updateChartIfMismatch(chart);
            } else {
            }
        });
    }

    public static void changeBarColors(BarChart<String, Number> barChart, String color) {
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
                clearDataLogs();
                clearConfig();
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


}
