package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.LogUtils;
import com.drozal.dataterminal.util.server.ClientUtils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

import static com.drozal.dataterminal.util.LogUtils.log;
import static com.drozal.dataterminal.util.server.ClientUtils.isConnected;
import static com.drozal.dataterminal.util.server.ClientUtils.notifyStatusChanged;

public class ClientController {
    @javafx.fxml.FXML
    private TextField inetField;
    @javafx.fxml.FXML
    private BorderPane root;
    @javafx.fxml.FXML
    private TextField portField;
    @javafx.fxml.FXML
    private TextField inputPortField;
    @javafx.fxml.FXML
    private TextField inputHostField;
    @javafx.fxml.FXML
    private Label statusLabel;

    public Label getStatusLabel() {
        return statusLabel;
    }

    public TextField getInetField() {
        return inetField;
    }

    public TextField getPortField() {
        return portField;
    }

    public void initialize() throws IOException {
        if (ConfigReader.configRead("lastIPV4Connection") != null) {
            inputHostField.setText(ConfigReader.configRead("lastIPV4Connection"));
        }
        if (ConfigReader.configRead("lastPortConnection") != null) {
            inputPortField.setText(ConfigReader.configRead("lastPortConnection"));
        }
    }

    @javafx.fxml.FXML
    public void connectBtnPress(ActionEvent actionEvent) throws IOException {
        if (ClientUtils.connectToService(inputHostField.getText(), Integer.parseInt(inputPortField.getText()))) {
            Platform.runLater(() -> {
                statusLabel.setText("Connected");
                statusLabel.setStyle("-fx-text-fill: green;");
            });

        } else {
            log("Server Not Found", LogUtils.Severity.ERROR);
            Platform.runLater(() -> {
                isConnected = false;
                notifyStatusChanged(isConnected);
                statusLabel.setText("Server Not Found");
                statusLabel.setStyle("-fx-text-fill: red;");
            });
        }


    }
}
