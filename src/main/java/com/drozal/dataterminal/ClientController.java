package com.drozal.dataterminal;

import com.drozal.dataterminal.util.server.ServerUtils;
import javafx.scene.control.Label;
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
    private TextField serviceAddressField;
    @javafx.fxml.FXML
    private Label clientRefreshInLabel;

    public static void connect() {
        new Thread(() -> {
            try {
                ServerUtils.connectToService("ReportPlusService");
            } catch (Exception e) {
                System.err.println("Error during service connection: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    public Label getClientRefreshInLabel() {
        return clientRefreshInLabel;
    }

    public TextField getInetField() {
        return inetField;
    }

    public TextField getPortField() {
        return portField;
    }

    public TextField getServiceAddressField() {
        return serviceAddressField;
    }

    public void initialize() {

    }

}
