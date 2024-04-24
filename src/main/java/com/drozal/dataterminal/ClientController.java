package com.drozal.dataterminal;

import com.drozal.dataterminal.util.LogUtils;
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

    public void initialize() {
        new Thread(() -> {
            try {
                ServerUtils.connectToService("ReportPlusService");
            } catch (Exception e) {
                System.err.println("Error during service connection: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();

        ServerUtils.setStatusListener(this::updateConnectionStatus);
    }

    private void updateConnectionStatus(boolean isConnected) {
        if (!isConnected) {
            LogUtils.log("Server No Longer Connected", LogUtils.Severity.WARN);
            // Add new label under settings dropdown, set to no longer connected
        }
    }

}
