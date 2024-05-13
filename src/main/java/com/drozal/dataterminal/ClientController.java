package com.drozal.dataterminal;

import com.drozal.dataterminal.config.ConfigReader;
import com.drozal.dataterminal.util.reportCreationUtil;
import com.drozal.dataterminal.util.server.ClientUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class ClientController {
    public static AnchorPane topBar;
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

    public static AnchorPane getTopBar() {
        return topBar;
    }

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
        topBar = reportCreationUtil.createSimpleTitleBar("Client Interface");

        root.setTop(topBar);

        if (ConfigReader.configRead("lastIPV4Connection") != null) {
            inputHostField.setText(ConfigReader.configRead("lastIPV4Connection"));
        }
        if (ConfigReader.configRead("lastPortConnection") != null) {
            inputPortField.setText(ConfigReader.configRead("lastPortConnection"));
        }
    }

    @javafx.fxml.FXML
    public void connectBtnPress(ActionEvent actionEvent) throws IOException {
        ClientUtils.connectToService(inputHostField.getText(), Integer.parseInt(inputPortField.getText()));
    }
}
