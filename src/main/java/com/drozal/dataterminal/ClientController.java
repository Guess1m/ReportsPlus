package com.drozal.dataterminal;

import com.drozal.dataterminal.util.server.ClientUtils;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

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


    public TextField getInetField() {
        return inetField;
    }

    public TextField getPortField() {
        return portField;
    }

    @javafx.fxml.FXML
    public void connectBtnPress(ActionEvent actionEvent) {
        ClientUtils.connectToService(inputHostField.getText(), Integer.parseInt(inputPortField.getText()));

    }
}
