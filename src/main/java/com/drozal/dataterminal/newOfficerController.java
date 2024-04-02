package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.ResizeHelper;
import com.drozal.dataterminal.util.dropdownInfo;
import com.drozal.dataterminal.util.windowUtils;
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
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class newOfficerController {
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
    @javafx.fxml.FXML
    private Button bypassBtn;

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
                System.out.println("exists, printing values");
            } else {
                try {
                    // Create the config.properties file in the JAR directory
                    configFile.createNewFile();
                    System.out.println("Config: " + configFile.getAbsolutePath());
                } catch (IOException e) {
                    System.out.println("Failed to create config file: " + e.getMessage());
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
            ConfigWriter.configwrite("fullscreenOnStartup", "true");
            ConfigWriter.configwrite("mainColor", "#524992");
            ConfigWriter.configwrite("secondaryColor", "#665cb6");
            ConfigWriter.configwrite("accentColor", "#9c95d0");

            Stage stag = (Stage) vbox.getScene().getWindow();
            stag.close();

            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            stage.setTitle("Reports Plus+");
            stage.setScene(newScene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setResizable(false);
            stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png")));
            stage.show();
            stage.centerOnScreen();
            stage.setMinHeight(stage.getHeight() - 200);
            stage.setMinWidth(stage.getWidth() - 200);
            windowUtils.setWindowedFullscreen(stage);
            ResizeHelper.addResizeListener(stage);
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
            System.out.println("exists, printing values");
        } else {
            try {
                // Create the config.properties file in the JAR directory
                configFile.createNewFile();
                System.out.println("Config: " + configFile.getAbsolutePath());
            } catch (IOException e) {
                System.out.println("Failed to create config file: " + e.getMessage());
            }
        }

        // Proceed with further processing
        ConfigWriter.configwrite("Agency", "Los Santos Sheriffs Office");
        ConfigWriter.configwrite("Division", "North Area Patrol Division");
        ConfigWriter.configwrite("Name", "McKennedy");
        ConfigWriter.configwrite("Rank", "Deputy Sheriff");
        ConfigWriter.configwrite("Number", "1-18");
        ConfigWriter.configwrite("fullscreenOnStartup", "true");
        ConfigWriter.configwrite("mainColor", "#524992");
        ConfigWriter.configwrite("secondaryColor", "#665cb6");
        ConfigWriter.configwrite("accentColor", "#9c95d0");

        Stage stag = (Stage) vbox.getScene().getWindow();
        stag.close();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Reports Plus+");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.getIcons().add(new Image(newOfficerApplication.class.getResourceAsStream("imgs/icons/Icon.png")));
        stage.show();
        stage.centerOnScreen();
        stage.setMinHeight(stage.getHeight() - 200);
        stage.setMinWidth(stage.getWidth() - 200);
        windowUtils.setWindowedFullscreen(stage);
        ResizeHelper.addResizeListener(stage);
    }
}
