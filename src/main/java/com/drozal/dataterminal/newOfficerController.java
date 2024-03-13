package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.dropdownInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;

public class newOfficerController {
    public TextField numberField;
    public TextField nameField;
    public ComboBox rankDropdown;
    public ComboBox agencyDropDown;
    public ComboBox divisionDropDown;
    public VBox vboxid;
    public Label incompleteLabel;
    boolean hasEntered = false;
    private double xOffset = 0;
    private double yOffset = 0;

    public void onMouseEnter(MouseEvent mouseEvent) {
        if (hasEntered) {
        } else {
            rankDropdown.getItems().addAll(dropdownInfo.ranks);
            divisionDropDown.getItems().addAll(dropdownInfo.divisions);
            agencyDropDown.getItems().addAll(dropdownInfo.agencies);
        }
    }

    public void onMouseExit(MouseEvent mouseEvent) {
        hasEntered = true;
    }

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
            jarPath = URLDecoder.decode(jarPath, "UTF-8");
            // Extract the directory path from the JAR path
            String jarDir = new File(jarPath).getParent();
            // Construct the path for the config.properties file in the JAR directory
            String configFilePath = jarDir + File.separator + "config.properties";
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                System.out.println("exists, printing values");
            } else {
                System.out.println("doesnt exist");
                try {
                    // Create the config.properties file in the JAR directory
                    configFile.createNewFile();
                    System.out.println("Config file created, printing values, located at: " + configFile.getAbsolutePath());
                } catch (IOException e) {
                    System.out.println("Failed to create config file: " + e.getMessage());
                }
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

        Stage stag = (Stage) vboxid.getScene().getWindow();
        stag.close();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DataTerminalHome-view.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setTitle("Data Terminal");
        stage.setScene(newScene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.show();

        actionController actionController = loader.getController();
        actionController.getInfoPane().setDisable(true);
        actionController.getInfoPane().setVisible(false);
        actionController.getShiftInformationPane().setDisable(false);
        actionController.getShiftInformationPane().setVisible(true);

    }

    public void onMouseDrag(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setX(mouseEvent.getScreenX() - xOffset);
        stage.setY(mouseEvent.getScreenY() - yOffset);
    }

    public void onMousePress(MouseEvent mouseEvent) {
        xOffset = mouseEvent.getSceneX();
        yOffset = mouseEvent.getSceneY();
    }

    public void onExitButtonClick(MouseEvent actionEvent) {
        Platform.exit();
    }
}
