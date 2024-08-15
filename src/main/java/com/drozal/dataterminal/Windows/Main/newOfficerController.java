package com.drozal.dataterminal.Windows.Main;

import com.drozal.dataterminal.Launcher;
import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.newOfficerApplication;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.dropdownInfo;
import com.drozal.dataterminal.util.Window.windowUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainRT;
import static com.drozal.dataterminal.Windows.Main.actionController.handleClose;
import static com.drozal.dataterminal.config.ConfigReader.checkAndSetDefaultValues;
import static com.drozal.dataterminal.util.Misc.LogUtils.*;
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
        endLog();
        handleClose();
    }

    @javafx.fxml.FXML
    public void loginButtonClick(ActionEvent actionEvent) throws IOException {

        if (agencyDropDown.getValue() == null || divisionDropDown.getValue() == null || rankDropdown.getValue() == null || nameField.getText().isEmpty() || numberField.getText().isEmpty()) {
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

            jarPath = URLDecoder.decode(Objects.requireNonNull(jarPath), StandardCharsets.UTF_8);

            String jarDir = new File(jarPath).getParent();

            String configFilePath = jarDir + File.separator + "config.properties";
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                log("exists, printing values", LogUtils.Severity.INFO);
            } else {
                try {
                    configFile.createNewFile();
                    log("Config: " + configFile.getAbsolutePath(), LogUtils.Severity.INFO);
                } catch (IOException e) {
                    logError("Failed to create config file", e);
                }
            }

            String agency = agencyDropDown.getValue().toString();
            String division = divisionDropDown.getValue().toString();
            String rank = rankDropdown.getValue().toString();

            ConfigWriter.configwrite("userInfo", "Agency", agency);
            ConfigWriter.configwrite("userInfo", "Division", division);
            ConfigWriter.configwrite("userInfo", "Name", nameField.getText());
            ConfigWriter.configwrite("userInfo", "Rank", rank);
            ConfigWriter.configwrite("userInfo", "Number", numberField.getText());
            checkAndSetDefaultValues();

            Stage stag = (Stage) vbox.getScene().getWindow();
            stag.close();

            mainRT = new Stage();
            FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Main/DataTerminalHome-view.fxml"));
            Parent root = loader.load();
            controller = loader.getController();
            Scene scene = new Scene(root);
            mainRT.setScene(scene);
            mainRT.initStyle(StageStyle.UNDECORATED);
            mainRT.getIcons().add(new Image(Objects.requireNonNull(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png"))));
            mainRT.show();

            String startupValue = ConfigReader.configRead("layout", "mainWindowLayout");
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
                    if (ConfigReader.configRead("uiSettings", "fullscreenOnStartup").equals("true")) {
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
    public void bypassButtonClick(ActionEvent actionEvent) throws IOException {
        String jarPath = null;
        try {
            jarPath = newOfficerApplication.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (URISyntaxException e) {
            logError("BypassBtnJarPath URISyntax Exception", e);
            throw new RuntimeException(e);
        }

        jarPath = URLDecoder.decode(jarPath, StandardCharsets.UTF_8);

        String jarDir = new File(jarPath).getParent();

        String configFilePath = jarDir + File.separator + "config.properties";
        File configFile = new File(configFilePath);
        if (configFile.exists()) {
            log("exists, printing values", LogUtils.Severity.INFO);
        } else {
            try {

                //noinspection ResultOfMethodCallIgnored
                configFile.createNewFile();
                log("Config: " + configFile.getAbsolutePath(), LogUtils.Severity.INFO);
            } catch (IOException e) {
                logError("Failed to create config file: ", e);
            }
        }

        ConfigWriter.configwrite("userInfo", "Agency", "Los Santos Sheriffs Office");
        ConfigWriter.configwrite("userInfo", "Division", "North Area Patrol Division");
        ConfigWriter.configwrite("userInfo", "Name", "McKennedy");
        ConfigWriter.configwrite("userInfo", "Rank", "Deputy Sheriff");
        ConfigWriter.configwrite("userInfo", "Number", "1-18");
        checkAndSetDefaultValues();

        Stage stag = (Stage) vbox.getScene().getWindow();
        stag.close();

        mainRT = new Stage();
        mainRT.initStyle(StageStyle.UNDECORATED);
        FXMLLoader loader = new FXMLLoader(Launcher.class.getResource("Windows/Main/DataTerminalHome-view.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        Scene scene = new Scene(root);
        mainRT.setScene(scene);
        mainRT.getIcons().add(new Image(Objects.requireNonNull(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png"))));
        mainRT.show();

        String startupValue = ConfigReader.configRead("layout", "mainWindowLayout");
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
                if (ConfigReader.configRead("uiSettings", "fullscreenOnStartup").equals("true")) {
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
