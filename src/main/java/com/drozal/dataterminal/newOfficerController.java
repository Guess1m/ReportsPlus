package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.dropdownInfo;
import com.drozal.dataterminal.util.Window.windowUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.controllerUtils.*;
import static com.drozal.dataterminal.util.Window.windowUtils.*;

public class newOfficerController {
    public static actionController controller;
    @javafx.fxml.FXML
    public TextField numberField;
    @javafx.fxml.FXML
    public TextField nameField;
    @javafx.fxml.FXML
    public ComboBox rankDropdown;
    @javafx.fxml.FXML
    public ComboBox agencyDropDown;
    @javafx.fxml.FXML
    public ComboBox divisionDropDown;
    @javafx.fxml.FXML
    public AnchorPane vbox;
    @javafx.fxml.FXML
    public Label incompleteLabel;
    private double xOffset = 0;
    private double yOffset = 0;

    public void initialize() {
        rankDropdown.getItems().addAll(dropdownInfo.ranks);
        divisionDropDown.getItems().addAll(dropdownInfo.divisions);
        divisionDropDown.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
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

                            // Check if the item contains "=" and set it as bold
                            if (item.contains("=")) {
                                setStyle("-fx-font-weight: bold;");
                            } else {
                                setStyle("-fx-font-weight: none;");
                            }
                        }
                    }
                };
            }
        });
        agencyDropDown.getItems().addAll(dropdownInfo.agencies);
    }

    @javafx.fxml.FXML
    public void loginButtonClick(ActionEvent actionEvent) throws IOException {
        // Check if any of the ComboBox values are null
        if (agencyDropDown.getValue() == null || divisionDropDown.getValue() == null ||
                rankDropdown.getValue() == null || nameField.getText().isEmpty() ||
                numberField.getText().isEmpty()) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(new KeyFrame(Duration.seconds(1), evt -> {
                incompleteLabel.setVisible(false);
            }));
            timeline1.play();
        } else {
            String jarPath = null;
            try {
                jarPath = newOfficerApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            } catch (URISyntaxException e) {
                logError("NewOfficer JarPath URISyntax Exception", e);
            }
            // Decode the URI path to handle spaces or special characters
            jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);
            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();
            // Construct the path for the config.properties file in the JAR directory
            String configFilePath = jarDir + File.separator + "config.properties";
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                log("exists, printing values", LogUtils.Severity.INFO);
            } else {
                try {
                    // Create the config.properties file in the JAR directory
                    configFile.createNewFile();
                    log("Config: " + configFile.getAbsolutePath(), LogUtils.Severity.INFO);
                } catch (IOException e) {
                    logError("Failed to create config file", e);
                }
            }
            // Access the values only if they are not null
            String agency = agencyDropDown.getValue().toString();
            String division = divisionDropDown.getValue().toString();
            String rank = rankDropdown.getValue().toString();

            // Proceed with further processing
            ConfigWriter.configwrite("Agency", agency);
            ConfigWriter.configwrite("Division", division);
            ConfigWriter.configwrite("Name", nameField.getText());
            ConfigWriter.configwrite("Rank", rank);
            ConfigWriter.configwrite("Number", numberField.getText());
            addConfigurationValues();

            Stage stag = (Stage) vbox.getScene().getWindow();
            stag.close();

            Stage mainRT = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root);
            mainRT.setScene(scene);
            mainRT.initStyle(StageStyle.UNDECORATED);
            mainRT.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png")));
            mainRT.show();

            String startupValue = ConfigReader.configRead("mainWindowLayout");
            switch (startupValue) {
                case "TopLeft" -> snapToTopLeft(mainRT);
                case "TopRight" -> snapToTopRight(mainRT);
                case "BottomLeft" -> snapToBottomLeft(mainRT);
                case "BottomRight" -> snapToBottomRight(mainRT);
                case "FullLeft" -> snapToLeft(mainRT);
                case "FullRight" -> snapToRight(mainRT);
                default -> {
                    mainRT.centerOnScreen();
                    mainRT.setMinHeight(450);
                    mainRT.setMinWidth(450);
                    if (ConfigReader.configRead("fullscreenOnStartup").equals("true")) {
                        windowUtils.setWindowedFullscreen(mainRT);
                    } else {
                        mainRT.setHeight(800);
                        mainRT.setWidth(1150);
                    }
                }

            }
            mainRT.setAlwaysOnTop(false);
        }
    }

    @javafx.fxml.FXML
    public void onMouseDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    @javafx.fxml.FXML
    public void onMousePress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    @javafx.fxml.FXML
    public void onExitButtonClick(MouseEvent actionEvent) {
        Platform.exit();
    }

    @javafx.fxml.FXML
    public void bypassButtonClick(ActionEvent actionEvent) throws IOException {
        String jarPath = null;
        try {
            jarPath = newOfficerApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            logError("BypassBtnJarPath URISyntax Exception", e);
            throw new RuntimeException(e);
        }
        // Decode the URI path to handle spaces or special characters
        jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);
        // Extract the directory path from the JAR path
        String jarDir = new File(jarPath).getParent();
        // Construct the path for the config.properties file in the JAR directory
        String configFilePath = jarDir + File.separator + "config.properties";
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            log("exists, printing values", LogUtils.Severity.INFO);
        } else {
            try {
                // Create the config.properties file in the JAR directory
                configFile.createNewFile();
                log("Config: " + configFile.getAbsolutePath(), LogUtils.Severity.INFO);
            } catch (IOException e) {
                logError("Failed to create config file: ", e);
            }
        }

        ConfigWriter.configwrite("Agency", "Los Santos Sheriffs Office");
        ConfigWriter.configwrite("Division", "North Area Patrol Division");
        ConfigWriter.configwrite("Name", "McKennedy");
        ConfigWriter.configwrite("Rank", "Deputy Sheriff");
        ConfigWriter.configwrite("Number", "1-18");
        addConfigurationValues();

        Stage stag = (Stage) vbox.getScene().getWindow();
        stag.close();

        mainRT = new Stage();
        mainRT.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root);
        mainRT.setScene(scene);
        mainRT.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png")));
        mainRT.show();

        String startupValue = ConfigReader.configRead("mainWindowLayout");
        switch (startupValue) {
            case "TopLeft" -> snapToTopLeft(mainRT);
            case "TopRight" -> snapToTopRight(mainRT);
            case "BottomLeft" -> snapToBottomLeft(mainRT);
            case "BottomRight" -> snapToBottomRight(mainRT);
            case "FullLeft" -> snapToLeft(mainRT);
            case "FullRight" -> snapToRight(mainRT);
            default -> {
                mainRT.centerOnScreen();
                mainRT.setMinHeight(450);
                mainRT.setMinWidth(450);
                if (ConfigReader.configRead("fullscreenOnStartup").equals("true")) {
                    windowUtils.setWindowedFullscreen(mainRT);
                } else {
                    mainRT.setHeight(800);
                    mainRT.setWidth(1150);
                }
            }
        }
        mainRT.setAlwaysOnTop(false);
    }

    private static void addConfigurationValues(){
        ConfigWriter.configwrite("fullscreenOnStartup", "true");
        updateMain(Color.valueOf("#524992"));
        updateSecondary(Color.valueOf("#665cb6"));
        updateAccent(Color.valueOf("#9c95d0"));
        ConfigWriter.configwrite("mainWindowLayout", "Default");
        ConfigWriter.configwrite("notesWindowLayout", "Default");
        ConfigWriter.configwrite("reportWindowLayout", "Default");
        ConfigWriter.configwrite("reportWindowDarkMode", "true");
        ConfigWriter.configwrite("calloutDuration", "7");
        ConfigWriter.configwrite("AOTNotes", "true");
        ConfigWriter.configwrite("AOTReport", "true");
        ConfigWriter.configwrite("AOTCallout", "true");
        ConfigWriter.configwrite("AOTID", "true");
        ConfigWriter.configwrite("AOTSettings", "true");
        ConfigWriter.configwrite("AOTMap", "true");
        ConfigWriter.configwrite("AOTDebug", "true");
        ConfigWriter.configwrite("AOTClient", "true");
    }
}
