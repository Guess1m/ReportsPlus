package com.drozal.dataterminal.Windows.Misc;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.config.ConfigWriter;
import com.drozal.dataterminal.util.Misc.LogUtils;
import com.drozal.dataterminal.util.Misc.dropdownInfo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

import static com.drozal.dataterminal.DataTerminalHomeApplication.mainDesktopControllerObj;
import static com.drozal.dataterminal.Desktop.mainDesktopController.userManager;
import static com.drozal.dataterminal.util.Misc.LogUtils.log;
import static com.drozal.dataterminal.util.Misc.LogUtils.logError;
import static com.drozal.dataterminal.util.Misc.stringUtil.getJarPath;

public class UserManagerController {
    public static UserManagerController userManagerController;
    @javafx.fxml.FXML
    private BorderPane root;
    @javafx.fxml.FXML
    private AnchorPane mainPane;
    @javafx.fxml.FXML
    private ComboBox agencyDropDown;
    @javafx.fxml.FXML
    private Button loginBtn;
    @javafx.fxml.FXML
    private TextField numberField;
    @javafx.fxml.FXML
    private TextField nameField;
    @javafx.fxml.FXML
    private ComboBox divisionDropDown;
    @javafx.fxml.FXML
    private ComboBox rankDropdown;
    @javafx.fxml.FXML
    private Label incompleteLabel;
    @javafx.fxml.FXML
    private Label lbl1;

    public void initialize() throws IOException {
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

        rankDropdown.setValue(ConfigReader.configRead("userInfo", "Rank"));
        agencyDropDown.setValue(ConfigReader.configRead("userInfo", "Agency"));
        divisionDropDown.setValue(ConfigReader.configRead("userInfo", "Division"));
        nameField.setText(ConfigReader.configRead("userInfo", "Name"));
        numberField.setText(ConfigReader.configRead("userInfo", "Number"));
    }

    @javafx.fxml.FXML
    public void updateBtn(ActionEvent actionEvent) {
        if (agencyDropDown.getValue() == null || divisionDropDown.getValue() == null || rankDropdown.getValue() == null || nameField.getText().isEmpty() || numberField.getText().isEmpty()) {
            incompleteLabel.setText("Fill Out Form.");
            incompleteLabel.setStyle("-fx-text-fill: red;");
            incompleteLabel.setVisible(true);
            Timeline timeline1 = new Timeline(
                    new KeyFrame(Duration.seconds(1), evt -> incompleteLabel.setVisible(false)));
            timeline1.play();
        } else {
            String configFilePath = getJarPath() + File.separator + "config.properties";
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                log("exists, printing values", LogUtils.Severity.INFO);

                String agency = agencyDropDown.getValue().toString().trim();
                String division = divisionDropDown.getValue().toString().trim();
                String rank = rankDropdown.getValue().toString().trim();

                ConfigWriter.configwrite("userInfo", "Agency", agency);
                ConfigWriter.configwrite("userInfo", "Division", division);
                ConfigWriter.configwrite("userInfo", "Name", nameField.getText().trim());
                ConfigWriter.configwrite("userInfo", "Rank", rank);
                ConfigWriter.configwrite("userInfo", "Number", numberField.getText().trim());

                if (userManager != null) {
                    userManager.closeWindow();
                }
                if (mainDesktopControllerObj != null) {
                    try {
                        mainDesktopControllerObj.getOfficerInfoName().setText(
                                ConfigReader.configRead("userInfo", "Name"));
                    } catch (IOException e) {
                        logError("Unable to read userInfo name from config, ", e);
                    }
                }
            }
        }
    }

    public BorderPane getRoot() {
        return root;
    }

    public Label getLbl1() {
        return lbl1;
    }

    public ComboBox getAgencyDropDown() {
        return agencyDropDown;
    }

    public ComboBox getRankDropdown() {
        return rankDropdown;
    }

    public TextField getNumberField() {
        return numberField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public ComboBox getDivisionDropDown() {
        return divisionDropDown;
    }
}
